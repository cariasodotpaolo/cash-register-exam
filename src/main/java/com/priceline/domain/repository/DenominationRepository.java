package com.priceline.domain.repository;

import com.priceline.domain.model.Denomination;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DenominationRepository {

    private Map<Integer, Denomination> denominations;
    private List<Integer> denominationValues;

    @Autowired
    public DenominationRepository (
                            Map<Integer, Denomination> denominations,
                            List<Integer> denominationValues) {
        this.denominations = denominations;
        this.denominationValues = denominationValues;
    }

    public Denomination get(Integer denomination) {

        return denominations.get(denomination);
    }

    public List<Integer> list() {

        //Collections.reverse(denominationValues);

        return denominationValues;
    }
}
