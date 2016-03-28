package clowdtech.mpositive.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.clowdtech.data.repository.IProductRepository;
import com.clowdtech.data.repository.ISyncRepository;
import com.clowdtech.data.repository.RepositoryProvider;

import java.util.Objects;

import clowdtech.mpositive.R;
import clowdtech.mpositive.sync.api.ISyncProductApiService;
import retrofit.RestAdapter;

class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";
    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;
    private final Context mContext;

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mContext = context;
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        Log.i(TAG, "Dans Magic Sync");


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String accountNumber = prefs.getString(mContext.getResources().getString(R.string.preference_sync_account), "");

        // TODO sync product changes (name and deletion) by setting the lastupdateddate
        // TODO bug double products. for whatever reason what if same product gets synced twice, server is not doing anything to stop this
        // TODO bug- getting the timeouts
        // TODO linking up an account and authenticatoin
        // TODO error handling
        // TODO - extract the sync algorithms into a class. Can I integration test this?
        final IProductRepository productsRepository = RepositoryProvider.getProductsRepository();
        final ISyncRepository syncRepository = RepositoryProvider.getSyncRepository();

        //RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://local.sync.com/api/").build();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://mpositive-site.apphb.com//api/").build();

        ISyncProductApiService service = restAdapter.create(ISyncProductApiService.class);

        ProductSync syncer = new ProductSync(productsRepository, service, syncRepository);

        // toggle here so that the rest of the mechanism is in and running
        if (getContext().getResources().getString(R.string.FeatureToggle_ProductSync).equals("true")) {
            syncer.Sync(accountNumber);
        }
    }
}