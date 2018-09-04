package com.priceline.domain.model;

import java.util.Objects;

public class Denomination {

    private String currency;
    private String code;
    private String sign;
    private Integer value;

    public Denomination(String currency, String code, String sign, Integer value) {
        this.currency = currency;
        this.code = code;
        this.sign = sign;
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Denomination)) {
            return false;
        }
        Denomination denomination = (Denomination) o;
        return value == denomination.getValue() &&
            Objects.equals(currency, denomination.getCurrency()) &&
            Objects.equals(code, denomination.getCode()) &&
            Objects.equals(sign, denomination.getSign());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, currency, code, sign);
    }
}
