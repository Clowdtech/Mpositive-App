package clowdtech.mpositive.areas.reporting.product.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.clowdtech.data.entities.ITransactionLineProduct;
import com.clowdtech.data.entities.ITransaction;
import com.clowdtech.data.repository.ITransactionsRepository;
import com.clowdtech.data.repository.RepositoryProvider;

import java.util.List;

import clowdtech.mpositive.R;
import clowdtech.mpositive.activities.NavDrawerActivity;
import clowdtech.mpositive.areas.reporting.product.fragments.ProductReportingRightFragment;
import clowdtech.mpositive.areas.reporting.product.viewModels.ProductReportingViewModel;
import clowdtech.mpositive.areas.reporting.IReportingDaySelected;
import clowdtech.mpositive.areas.reporting.product.fragments.ReportingDaysFragment;
import clowdtech.mpositive.data.ReportingHelper;

public class ProductReporting extends NavDrawerActivity implements IReportingDaySelected {
    private int CURRENT_VIEW = 1;

    public static final String PRODUCT_REPORTING_LEFT_FRAG = "PRODUCT_REPORTING_LEFT_FRAG";
    public static final String PRODUCT_REPORTING_RIGHT_FRAG = "PRODUCT_REPORTING_RIGHT_FRAG";

    private ProductReportingRightFragment _productsFrag;

    private boolean twoColumnView;

    private ReportingHelper reportingHelper;
    private ReportingDaysFragment _daysFrag;

    private static ITransactionsRepository transactionRepo = RepositoryProvider.getTransactionsRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reportingHelper = new ReportingHelper(transactionRepo); // split of constructor / view logic here

        getLayoutInflater().inflate(R.layout.activity_product_reporting, frameLayout);

        setupActionBar();

        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction remove = manager.beginTransaction();

        _daysFrag = (ReportingDaysFragment) manager.findFragmentByTag(PRODUCT_REPORTING_LEFT_FRAG);

        if (_daysFrag == null) {
            _daysFrag = ReportingDaysFragment.newInstance();
        } else {
            remove.remove(_daysFrag);
        }

        _productsFrag = (ProductReportingRightFragment) manager.findFragmentByTag(PRODUCT_REPORTING_RIGHT_FRAG);

        if (_productsFrag == null) {
            _productsFrag = ProductReportingRightFragment.newInstance();
        } else {
            remove.remove(_productsFrag);
        }

        if (!remove.isEmpty()) {
            remove.commit();
            manager.executePendingTransactions();
        }

        twoColumnView = findViewById(R.id.product_reporting_column_two) != null;

        if (twoColumnView) {
            setView(_daysFrag, PRODUCT_REPORTING_LEFT_FRAG, R.id.product_reporting_column_one);
        } else {
            CURRENT_VIEW = 1;

            setView(_daysFrag, PRODUCT_REPORTING_LEFT_FRAG, R.id.product_reporting_single_pane);
        }
    }

    @Override
    public void onBackPressed() {
        if (_drawerLayout.isDrawerOpen(_drawerLayoutContainer)) {
            _drawerLayout.closeDrawer(_drawerLayoutContainer);
        } else if (!twoColumnView && CURRENT_VIEW == 2) {
            CURRENT_VIEW = 1;

            setView(_daysFrag, PRODUCT_REPORTING_LEFT_FRAG, R.id.product_reporting_single_pane);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onNavItemSelected(int position) {
        // special behaviour if currently in reporting class
        if (position == 3) {
            return;
        }

        super.onNavItemSelected(position);
    }

    @Override
    public void ItemSelected(long lower, long upper) {
        ProductReportingViewModel viewModel = getReportingViewModel(lower, upper);

        _productsFrag.setHeading(String.format("%s %s", getResources().getString(R.string.reporting_product_header), viewModel.getHeader()));

        _productsFrag.setViewModel(viewModel.getViewModels());

        if (twoColumnView) {
            setView(_productsFrag, PRODUCT_REPORTING_RIGHT_FRAG, R.id.product_reporting_column_two);
        } else {
            CURRENT_VIEW = 2;

            setView(_productsFrag, PRODUCT_REPORTING_RIGHT_FRAG, R.id.product_reporting_single_pane);
        }
    }

    public ProductReportingViewModel getReportingViewModel(long lower, long upper) {

        ProductReportingViewModel viewModel = new ProductReportingViewModel(lower);

        List<ITransaction> transactions = reportingHelper.getTransactionsBetweenDates_forProductReporting(lower, upper);
        for (ITransaction transaction : transactions) {
            if (transaction.getRefunded()) {
                continue;
            }

            for (ITransactionLineProduct productLine : transaction.productLines()) {
                viewModel.addReportingItem(productLine.getName(), productLine.getProductId(), productLine.getQuantity(), productLine.getUnitPrice());
            }
        }

        return viewModel;
    }

    private void setView(Fragment frag, String tag, int container) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction remove = fragmentManager.beginTransaction();

        remove.remove(frag);
        remove.commit();
        fragmentManager.executePendingTransactions();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(container, frag, tag);

        transaction.commit();
    }
}