package com.priceline.domain.service;

import com.priceline.domain.model.Denomination;
import java.util.List;

public interface DenominationService {

    List<Integer> getValues();

    Denomination get(Integer value);
}
