package com.priceline.api.shell;

import com.priceline.PricelineApplication;
import com.priceline.domain.exception.InsufficentFundsException;
import com.priceline.domain.exception.NoExactChangeException;
import com.priceline.domain.model.BillExchange;
import com.priceline.domain.model.BillInventory;
import com.priceline.domain.model.Denomination;
import com.priceline.domain.service.BillChangeService;
import com.priceline.domain.service.BillInventoryService;
import com.priceline.domain.service.DenominationService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class CashRegisterShellController {

    private DenominationService denominationService;
    private BillInventoryService billInventoryService;
    private BillChangeService billChangeService;

    @Value("${currency.description}")
    private String currencyDescription;
    @Value("${currency.code}")
    private String currencyCode;
    @Value("${currency.sign}")
    private String currencySign;

    @Autowired
    public CashRegisterShellController(DenominationService denominationService,
        BillInventoryService billInventoryService,
        BillChangeService billChangeService) {
        this.denominationService = denominationService;
        this.billInventoryService = billInventoryService;
        this.billChangeService = billChangeService;
    }

    @ShellMethod("Show current bills inventory")
    public String show() {

        StringBuilder sb = new StringBuilder();

        Integer totalAmount = billInventoryService.getTotalAmount();
        List<BillInventory> bills = billInventoryService.getAll();

        sb.append(currencySign);
        sb.append(totalAmount);
        sb.append(" ");

        for (BillInventory bill: bills) {
            sb.append(bill.getCount());
            sb.append(" ");
        }

        return sb.toString().trim();
    }

    @ShellMethod("Replenish bills inventory")
    public String put(@ShellOption int[] billsCount) {

        List<BillExchange> newBills = new ArrayList<>();

        List<Integer> denominationValues = denominationService.getValues();

        if (billsCount.length != denominationValues.size()) {
            throw new IllegalStateException("Input parameter does not match accepted denominations");
        }

        for ( int i = 0; i < billsCount.length; i++ ) {

            newBills.add(new BillExchange(
                            new Denomination(currencyDescription, currencyCode, currencySign,
                                              denominationValues.get(i)), billsCount[i]));
        }

        billInventoryService.replenish(newBills);

        return show();
    }

    @ShellMethod("Withdraw bills from inventory")
    public String take(@ShellOption int[] billsCount) {

        List<BillExchange> withdrawBills = new ArrayList<>();

        List<Integer> denominationValues = denominationService.getValues();

        if (billsCount.length != denominationValues.size()) {
            throw new IllegalStateException("Input parameter does not match accepted denominations");
        }

        for ( int i = 0; i < billsCount.length; i++ ) {

            withdrawBills.add(new BillExchange(
                new Denomination(currencyDescription, currencyCode, currencySign,
                    denominationValues.get(i)), billsCount[i]));
        }

        try {
            billInventoryService.dispense(withdrawBills);
        } catch (InsufficentFundsException e) {
            return e.getMessage();
        }

        return show();
    }

    @ShellMethod("Dispense change.")
    public String change(@ShellOption int changeAmount) {

        List<BillExchange> changeBills;

        try {
            changeBills = billChangeService.getChange(changeAmount);

        } catch (InsufficentFundsException e) {
            return e.getMessage();
        } catch (NoExactChangeException e) {
            return e.getMessage();
        }

        List<Integer> denominationValues = denominationService.getValues();

        Integer totalAmount = changeBills.stream().mapToInt(c -> c.getTotalAmount()).sum();

        if(!totalAmount.equals(changeAmount)) {
            return "No exact change or insufficient funds.";
        }

        StringBuilder sb = new StringBuilder();


        for (Integer denomination : denominationValues) {

            boolean isPresent = false;

            for (BillExchange bill: changeBills) {

                if (bill.getDenomination().getValue().equals(denomination)) {
                    sb.append(bill.getCount());
                    isPresent = true;
                    break;
                } else {
                    isPresent = false;
                }
            }

            if (!isPresent) {
                sb.append("0");
            }

            sb.append(" ");
        }

        return sb.toString().trim();
    }

}
