package com.priceline.domain.service;

import com.priceline.domain.exception.InsufficentFundsException;
import com.priceline.domain.model.BillExchange;
import com.priceline.domain.model.BillInventory;
import com.priceline.domain.model.Denomination;
import com.priceline.domain.repository.BillInventoryRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillInventoryServiceImpl implements BillInventoryService {

    private BillInventoryRepository billInventoryRepository;

    @Autowired
    public BillInventoryServiceImpl(
        BillInventoryRepository billInventoryRepository) {
        this.billInventoryRepository = billInventoryRepository;
    }

    @Override
    public List<BillInventory> getAll() {

        List<BillInventory> inventory = billInventoryRepository.getBillsInventory();

        Collections.sort(inventory, (b1, b2) -> b2.getDenomination().getValue() - b1.getDenomination().getValue());

        return inventory;
    }

    @Override
    public BillInventory get(Denomination denomination) {

        return billInventoryRepository.getBillInventory(denomination);
    }

    @Override
    public void replenish(List<BillExchange> newBillExchanges) {

        for (BillExchange billExchange : newBillExchanges) {
            billInventoryRepository.credit(billExchange.getDenomination(), billExchange.getCount());
        }
    }

    @Override
    public void dispense(List<BillExchange> changeBillExchanges) throws InsufficentFundsException {

        for (BillExchange billExchange : changeBillExchanges) {
            billInventoryRepository.debit(billExchange.getDenomination(), billExchange.getCount());
        }
    }

    @Override
    public Integer getTotalAmount() {

        List<BillInventory> bills = getAll();

        return bills.stream().mapToInt(b ->
                                       Math.multiplyExact(b.getCount(),
                                                          b.getDenomination().getValue())).sum();
    }


    @Override
    public void resetInventory() {
        billInventoryRepository.resetInventory();
    }
}
