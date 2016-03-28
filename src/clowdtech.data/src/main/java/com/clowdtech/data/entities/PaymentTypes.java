package com.clowdtech.data.entities;

public enum PaymentTypes
{
    Cash,
    Card,
    Other;

    private final int _value;

    PaymentTypes() {
        _value = ordinal();
    }

    public int getValue()
    {
        return _value;
    }
}
