package com.priceline.domain.service;

import com.priceline.domain.exception.InsufficentFundsException;
import com.priceline.domain.model.BillExchange;
import com.priceline.domain.model.BillInventory;
import com.priceline.domain.model.Denomination;
import java.util.List;

public interface BillInventoryService {

    List<BillInventory> getAll();

    BillInventory get(Denomination denomination);

    void replenish(List<BillExchange> newBillExchanges);

    void dispense(List<BillExchange> changeBillExchanges) throws InsufficentFundsException;

    Integer getTotalAmount();

    void resetInventory();
}
