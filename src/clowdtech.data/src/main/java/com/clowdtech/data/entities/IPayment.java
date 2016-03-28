package com.clowdtech.data.entities;

import java.math.BigDecimal;

public interface IPayment {
    BigDecimal getAmount();

    PaymentTypes getPaymentType();
}
