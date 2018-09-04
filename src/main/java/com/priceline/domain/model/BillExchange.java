package com.priceline.domain.model;

public class BillExchange {

    private Denomination denomination;
    private Integer count;

    public BillExchange(Denomination denomination, Integer count) {
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

    public Integer getTotalAmount() {
        return Math.multiplyExact(denomination.getValue(), count);
    }
}
