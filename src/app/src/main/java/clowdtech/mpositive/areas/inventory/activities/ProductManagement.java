package clowdtech.mpositive.areas.inventory.activities;

import android.os.Bundle;

import clowdtech.mpositive.R;
import clowdtech.mpositive.activities.NavDrawerActivity;
import clowdtech.mpositive.areas.inventory.Container;

public class ProductManagement extends NavDrawerActivity {
    private Container container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.products_activity, frameLayout);

        setupActionBar();

        container = (Container) findViewById(R.id.container);

        setViewData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        setViewData();
    }

    private void setViewData() {
        container.initialise(this.customActionBar);
    }

    public Container getContainer() {
        return container;
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.container.bindView();
    }

    @Override
    public void onNavItemSelected(int position) {
        if (position == 1) {
            container.navToHomeView();

            return;
        }

        super.onNavItemSelected(position);
    }

    @Override public void onBackPressed() {
        boolean handled = container.onBackPressed();
        if (!handled) {
            finish();
        }
    }
}
