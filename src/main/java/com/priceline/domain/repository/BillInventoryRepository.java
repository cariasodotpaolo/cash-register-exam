package com.priceline.domain.repository;

import com.priceline.domain.exception.InsufficentFundsException;
import com.priceline.domain.model.BillInventory;
import com.priceline.domain.model.Denomination;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class BillInventoryRepository {

    @Value("${initial.inventory.count}")
    private String initialInventoryCount;

    private Map<Integer, BillInventory> billsInventory;

    @Autowired
    public BillInventoryRepository(Map<Integer, BillInventory> billsInventory) {
        this.billsInventory = billsInventory;
    }

    public List<BillInventory> getBillsInventory() {

        return billsInventory.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
    }

    public BillInventory getBillInventory(Denomination denomination) {

        BillInventory billInventory = billsInventory.get(denomination.getValue());

        if (billInventory.getDenomination().equals(denomination)) {
            return billInventory;
        } else {
            throw new IllegalStateException("Denomination values did not match.");
        }

    }

    public BillInventory credit(Denomination denomination, int creditCount) {

        BillInventory billInventory = billsInventory.get(denomination.getValue());
        int currentCount = billInventory.getCount();
        billInventory.setCount(currentCount + creditCount);

        billsInventory.put(denomination.getValue(), billInventory);

        return billInventory;
    }

    public BillInventory debit(Denomination denomination, int debitCount)
                                throws InsufficentFundsException {

        BillInventory billInventory = billsInventory.get(denomination.getValue());
        int currentCount = billInventory.getCount();

        if(currentCount < debitCount) {
            throw new InsufficentFundsException(String.format("Insufficient funds for denomination: %s%s",
                                                                denomination.getSign(), denomination.getValue()));
        }

        billInventory.setCount(currentCount - debitCount);

        billsInventory.put(denomination.getValue(), billInventory);

        return billInventory;
    }

    public void resetInventory() {

        for ( BillInventory bill : billsInventory.values() ) {
            bill.setCount(Integer.parseInt(initialInventoryCount));
        }
    }
}
