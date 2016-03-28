package clowdtech.mpositive.activities;

import android.content.Context;
import android.content.Intent;

import clowdtech.mpositive.areas.inventory.activities.ProductManagement;
import clowdtech.mpositive.areas.reporting.product.activities.ProductReporting;
import clowdtech.mpositive.areas.reporting.transaction.activities.TransactionsReporting;
import clowdtech.mpositive.areas.till.activities.TillActivity;

public class NavDrawerIntents {

    public Intent getIntent(int position, Context baseContext) {
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(baseContext, TillActivity.class);
                break;
            case 1:
                intent = new Intent(baseContext, ProductManagement.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            case 2:
                intent = new Intent(baseContext, TransactionsReporting.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            case 3:
                intent = new Intent(baseContext, ProductReporting.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
        }
        return intent;
    }
}
