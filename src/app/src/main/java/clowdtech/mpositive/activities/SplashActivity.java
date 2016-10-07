package clowdtech.mpositive.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.clowdtech.data.repository.IProductRepository;

import javax.inject.Inject;

import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.till.activities.TillActivity;
import clowdtech.mpositive.data.InventoryStore;
import clowdtech.mpositive.easydata.DBAppFuncs;
import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.sync.SyncUtils;

public class SplashActivity extends BaseActivity {
    public static int LOGIN_REQUEST_CODE = 123;

    @Inject
    InventoryStore inventoryStoreImpl;

    @Inject
    IProductRepository productRepository;

    @Inject
    IEventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getApplicationContext()).getApplicationComponent().inject(this);

        final DBAppFuncs dbAppFuncs = ((App) getApplicationContext()).getDBAppFunctionsObj();

        SyncUtils.CreateSyncAccount(getApplication());

        setContentView(R.layout.splash);

        inventoryStoreImpl.initialise();

        dbAppFuncs.initMainTables();
        dbAppFuncs.initCategories();
        dbAppFuncs.initProducts();
        dbAppFuncs.initTransactions();

        Thread splashTimer = new Thread() {
            public void run() {
                int splashCountDown = 0;

                while (splashCountDown < 2500) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    splashCountDown += 100;
                }



//                if (productRepository.getProducts().size() == 0) {
//                    ActiveAndroid.beginTransaction();
//
//                    for (int c = 0; c < 10; c++) {
//                        CategoryAA category = new CategoryAA("category" + c);
//                        category.save();
//
//                        for (int p = 0; p < 10; p++) {
//                            ProductTile tile = new ProductTile();
//                            tile.setVisibleInCategory(true);
//                            tile.setVisibleOnHomePage(true);
//                            tile.save();
//
//                            ProductAA product = new ProductAA();
//
//                            product.setName("product" + p + ": c" + c);
//                            product.setPrice(new BigDecimal(String.valueOf((p + 10) * 10)));
//                            product.setTile(tile);
//                            product.save();
//
//                            ProductCategory link = new ProductCategory(product, category);
//                            link.save();
//
//
//                        }
//                    }
//
//                    ActiveAndroid.setTransactionSuccessful();
//                    ActiveAndroid.endTransaction();
//                }

                Intent intent;

                if (dbAppFuncs.hasLogin()) {
                    intent = new Intent(getBaseContext(), TillActivity.class);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }

            }
        };

        splashTimer.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent(getBaseContext(), TillActivity.class);
                startActivity(intent);
            }
            else
                finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //trackScreenView(TrackingConstants.ScreenNames.Splash);
    }
}
