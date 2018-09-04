package com.priceline.domain.service;

import com.priceline.domain.model.Denomination;
import com.priceline.domain.repository.BillInventoryRepository;
import com.priceline.domain.repository.DenominationRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DenominationServiceImpl implements DenominationService {

    private DenominationRepository denominationRepository;

    private BillInventoryRepository billInventoryRepository;

    @Autowired
    public DenominationServiceImpl(
        DenominationRepository denominationRepository) {
        this.denominationRepository = denominationRepository;
    }

    @Override
    public List<Integer> getValues() {
        return denominationRepository.list();
    }

    @Override
    public Denomination get(Integer value) {

        return denominationRepository.get(value);
    }


}
