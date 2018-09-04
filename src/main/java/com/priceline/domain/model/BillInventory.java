package com.priceline.domain.model;

public class BillInventory {

    private Denomination denomination;
    private Integer count;

    public BillInventory(Denomination denomination, int count) {
        this.denomination = denomination;
        this.count = count;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public void setDenomination(Denomination denomination) {
        this.denomination = denomination;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
