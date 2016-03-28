package clowdtech.mpositive.areas.reporting.transaction.activities;

import android.os.Bundle;

import clowdtech.mpositive.R;
import clowdtech.mpositive.activities.NavDrawerActivity;
import clowdtech.mpositive.areas.reporting.transaction.Container;

public class TransactionsReporting extends NavDrawerActivity {
    private Container container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_reporting, frameLayout);

        setupActionBar();

        this.container = (Container) findViewById(R.id.container);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.container.bindView();
    }

    public Container getContainer() {
        return container;
    }

    @Override
    public void onBackPressed() {
        if (!this.container.isBackHandled()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onNavItemSelected(int position) {
        if (!this.container.isNavItemHandled(position)) {

        }

        // special behaviour if currently in reporting class
        if (position == 2) {
            return;
        }

        super.onNavItemSelected(position);
    }
}