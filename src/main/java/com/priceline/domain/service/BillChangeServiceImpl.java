package com.priceline.domain.service;

import com.priceline.domain.exception.InsufficentFundsException;
import com.priceline.domain.exception.NoExactChangeException;
import com.priceline.domain.model.BillExchange;
import com.priceline.domain.model.BillInventory;
import com.priceline.domain.model.Denomination;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillChangeServiceImpl implements BillChangeService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DenominationService denominationService;
    private BillInventoryService billInventoryService;


    @Autowired
    public BillChangeServiceImpl(DenominationService denominationService,
                                 BillInventoryService billInventoryService) {
        this.denominationService = denominationService;
        this.billInventoryService = billInventoryService;
    }

    @Override
    public List<BillExchange> getChange(Integer changeAmount)
        throws InsufficentFundsException, NoExactChangeException {

        List<BillExchange> billsChange = changeProcessor(changeAmount);

        billInventoryService.dispense(billsChange);

        return billsChange;
    }

    private List<BillExchange> changeProcessor(Integer changeAmount)
        throws NoExactChangeException, InsufficentFundsException {

        List<BillExchange> billsChange = new ArrayList<>();
        Integer remainingAmount = changeAmount;

        if(changeAmount > billInventoryService.getTotalAmount()) {
            throw new InsufficentFundsException("Insufficent funds.");
        }

        for (Integer denominationValue: denominationService.getValues()) {

            Denomination denomination = denominationService.get(denominationValue);
            Integer remainder = Math.floorMod(remainingAmount, denominationValue);

            BillInventory billInventory = billInventoryService.get(denomination);

            /* MOVE TO SMALLER BILL IF NOT AVAILABLE OR
             *remainderAmount is smaller than current denomination value (modulo returns the first(left) value)
             */
            if ( billInventory.getCount() == 0 || remainder == remainingAmount) {
                continue;
            }

            if(remainder >= 0 && remainder != remainingAmount) {

                int billCount = Math.floorDiv(remainingAmount, denominationValue);

                /* USE UP ALL AVAILABLE BILLS */
                if (billInventory.getCount() < billCount) {
                    billCount = billInventory.getCount();
                }

                billsChange.add(new BillExchange(denominationService.get(denominationValue),
                    billCount));

                remainingAmount -= Math.multiplyExact(denominationValue, billCount);

                logger.debug("\nChange Bill: {} | Count: {}", denominationValue, billCount);
            }
        }

        if (remainingAmount > 0) {
            logger.error("No exact change. Remaining Amount is {}.", remainingAmount);
            throw new NoExactChangeException("No exact change.");
        }

        return billsChange;
    }
}
