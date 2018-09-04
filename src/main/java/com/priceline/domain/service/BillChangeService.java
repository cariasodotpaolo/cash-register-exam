package com.priceline.domain.service;

import com.priceline.domain.exception.InsufficentFundsException;
import com.priceline.domain.exception.NoExactChangeException;
import com.priceline.domain.model.BillExchange;
import java.util.List;

public interface BillChangeService {

    List<BillExchange> getChange(Integer changeAmount)
        throws InsufficentFundsException, NoExactChangeException;
}
