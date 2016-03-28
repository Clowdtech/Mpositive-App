package clowdtech.mpositive.data.transactions.entities;

import com.clowdtech.data.entities.ITransactionLineManual;
import com.clowdtech.data.entities.ITransactionLineProduct;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

public class Receipt  {
    private final long id;
    private final DateTime created;
    private final ReceiptPayment payment;
    private List<ITransactionLineProduct> productLines;
    private List<ITransactionLineManual> manualLines;
    private Boolean refunded;

    public Receipt(long id, DateTime created, ReceiptPayment payment, List<ITransactionLineProduct> productLines, List<ITransactionLineManual> manualLines, Boolean refunded) {
        this.id = id;
        this.created = created;
        this.payment = payment;
        this.productLines = productLines;
        this.manualLines = manualLines;
        this.refunded = refunded;
    }

//    public List<IReceiptLine> getReceiptRoll() {
//        List<IReceiptLine> roll = new ArrayList<>();
//
//        roll.addAll(productLines);
//        roll.addAll(manualLines);
//
//        Collections.sort(roll, new ReceiptLineComparer());
//
//        return roll;
//   }

   public BigDecimal getTotal() {
       BigDecimal total = new BigDecimal(0.00);

        for (ITransactionLineProduct line : productLines) {
            total = total.add(line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())));
        }

        for (ITransactionLineManual line : manualLines) {
            total = total.add(line.getPrice());
        }

        return total;
    }

    public BigDecimal getChange() {
        return getPayment().getAmountPaid().subtract(getTotal());
    }

    public long getId() {
        return id;
    }

    public Boolean isRefunded() { return refunded; }

    public DateTime getCreated() {
        return created;
    }

    public ReceiptPayment getPayment() {
        return payment;
    }

    public void setRefunded() {
        this.refunded = true;
    }

    public List<ITransactionLineProduct> getProductRoll() {
        return this.productLines;
    }

    public List<ITransactionLineManual> getManualRoll() {
        return this.manualLines;
    }

//    private class ReceiptLineComparer implements Comparator<IReceiptLine> {
//
//        @Override
//        public int compare(IReceiptLine lhs, IReceiptLine rhs) {
//            return lhs.getCreatedDate().isBefore(rhs.getCreatedDate()) ? -1
//                    : lhs.getCreatedDate().isAfter(rhs.getCreatedDate()) ? 1
//                    : 0;
//        }
//    }
}
