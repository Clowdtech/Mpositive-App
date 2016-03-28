package clowdtech.mpositive.areas.till.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.clowdtech.data.repository.ITransactionsRepository;
import com.clowdtech.data.repository.RepositoryProvider;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import clowdtech.mpositive.App;
import clowdtech.mpositive.ISharedPreferences;
import clowdtech.mpositive.R;
import clowdtech.mpositive.TaskListener;
import clowdtech.mpositive.activities.NavDrawerActivity;
import clowdtech.mpositive.areas.preferences.PreferenceEntry;
import clowdtech.mpositive.areas.till.Container;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.MenuItemVisibilityEvent;
import clowdtech.mpositive.queue.events.MenuSelectedEvent;
import clowdtech.mpositive.queue.events.PreferenceChangedEvent;
import clowdtech.mpositive.receipt.PopCashDrawerAsync;
import clowdtech.mpositive.ui.Presentable;

public class TillActivity extends NavDrawerActivity {
    private Container container;

    @Inject
    protected ISharedPreferences prefs;

    @Inject
    protected IEventBus eventBus;

    private boolean menuItemsVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // line up the available dependencies for use
//        ((App) getApplication()).inject(this);

//        DaggerApp_CheckoutActivityComponent
//                .builder()
//                .applicationComponent(((App) getApplicationContext()).getApplicationComponent())     //Takes appComponent explicitly (depends on it)
//                .checkoutActivityModule(new CheckoutActivityModule(this))
//                .build()
//                .inject(this);

        ((App)getApplicationContext()).getApplicationComponent().inject(this);

        getLayoutInflater().inflate(R.layout.till_activity, frameLayout);

        setupActionBar();

        container = (Container) findViewById(R.id.container);

        ((Presentable)findViewById(R.id.container)).getPresenter().onCreate(savedInstanceState);

        if (prefs.getCashDrawerIntegrated()) {
            setupDrawerPop();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ((Presentable)findViewById(R.id.container)).getPresenter().onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (container.onBackPressed()) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        eventBus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.container.bindView();

        eventBus.register(this);
    }

    public Container getContainer() {
        return container;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuItemsVisible) {
            MenuInflater inflater = getMenuInflater();

            inflater.inflate(R.menu.checkout, menu);

            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // change this
        if (super.onOptionsItemSelected(item)) {
            return true;
        }

        eventBus.post(new MenuSelectedEvent());

        return false;
    }

    @Override
    public void onNavItemSelected(int position) {
        // special behaviour if currently in till class
        if (position == 0) {
            return;
        }

        super.onNavItemSelected(position);
    }

    @Subscribe
    public void subscribePreferenceChangedEvent(PreferenceChangedEvent event) {
        if (event.getKey() == PreferenceEntry.CashDrawerIntegration) {
            Boolean value = (Boolean) event.getValue();

            if (value) {
                setupDrawerPop();
            } else {
                customActionBar.clearViewMenu();
            }
        }
    }

    @Subscribe
    public void subscribeMenuItemVisibility(MenuItemVisibilityEvent event) {
        this.menuItemsVisible = event.getVisibility();

        invalidateOptionsMenu();
    }

    private void setupDrawerPop() {
        View view = getLayoutInflater().inflate(R.layout.action_bar_items_till, null, false);

        customActionBar.setViewMenu(view);

        final ITransactionsRepository transactionRepo = RepositoryProvider.getTransactionsRepository();

        ButterKnife.findById(view, R.id.drawer_pop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PopCashDrawerAsync(TillActivity.this, new TaskListener() {
                    @Override
                    public void onFinished(boolean success) {
                        if (success) {
                            transactionRepo.registerNoSale();
                        }
                    }
                }).execute();
            }
        });
    }
}
