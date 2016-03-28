package clowdtech.mpositive.areas.till.contracts;

import com.clowdtech.data.entities.IOrderLineProduct;
import com.clowdtech.data.entities.ITransactionLineProduct;

import java.math.BigDecimal;

public interface IReceiptLineProduct extends ITransactionLineProduct, IOrderLineProduct {
    void decrementQuantity();

    void incrementQuantity();

    void setName(String name);

    void setUnitPrice(BigDecimal unitPrice);
}
