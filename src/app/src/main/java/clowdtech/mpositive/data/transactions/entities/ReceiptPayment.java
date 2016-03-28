package clowdtech.mpositive.data.transactions.entities;

import com.clowdtech.data.entities.IPayment;
import com.clowdtech.data.entities.PaymentTypes;

import java.math.BigDecimal;

public class ReceiptPayment implements IPayment {
    private PaymentTypes _type;
    private BigDecimal _amountPaid;

    // unexpected usages of this class if not setting up amount

    public ReceiptPayment(PaymentTypes type, BigDecimal paid) {
        this._type = type;
        this._amountPaid = paid;
    }

    public ReceiptPayment(PaymentTypes type) {
        this._type = type;
    }

    public BigDecimal getAmountPaid() {
        return _amountPaid;
    }

    public PaymentTypes getType() {
        return _type;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this._amountPaid = amountPaid;
    }

    @Override
    public BigDecimal getAmount() {
        return getAmountPaid();
    }

    @Override
    public PaymentTypes getPaymentType() {
        return getType();
    }
}
