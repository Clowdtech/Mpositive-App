package clowdtech.mpositive.sync;

import android.util.Log;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.ISyncData;
import com.clowdtech.data.repository.IProductRepository;
import com.clowdtech.data.repository.ISyncRepository;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.sync.api.ISyncProductApiService;
import clowdtech.mpositive.sync.api.NewProductRequest;
import clowdtech.mpositive.sync.api.NewProductResponse;
import clowdtech.mpositive.sync.api.NewProductsRequest;
import clowdtech.mpositive.sync.api.NewProductsResponse;
import clowdtech.mpositive.sync.api.UpdateProductRequest;
import clowdtech.mpositive.sync.api.UpdateProductsRequest;
import clowdtech.mpositive.sync.api.UpdateProductsResponse;
import clowdtech.mpositive.sync.api.UpdatedProduct;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductSync {
    public static final String TAG = "SyncAdapter";

    private final IProductRepository productsRepository;
    private final ISyncProductApiService syncProductApiService;
    private ISyncRepository syncRepository;

    public ProductSync(IProductRepository productsRepository, ISyncProductApiService syncProductApiService, ISyncRepository syncRepository) {
        this.productsRepository = productsRepository;
        this.syncProductApiService = syncProductApiService;
        this.syncRepository = syncRepository;
    }

    public void Sync(String accountNumber) {
        if (accountNumber == null || accountNumber.equals("")) {
            return;
        }

        syncNewProducts(accountNumber);
        syncUpdatedProducts(accountNumber);
    }

    private void syncNewProducts(String accountNumber) {
        NewProductsRequest newProductsRequest = new NewProductsRequest();
        newProductsRequest.products = new ArrayList<>();

        List<IProduct> newProducts = productsRepository.getNewProducts();

        if (newProducts.size() == 0) {
            return;
        }

        for (IProduct product : newProducts) {
            NewProductRequest apiProduct = new NewProductRequest();
            apiProduct.ExternalId = product.getId(); // should we sync one at a time and avoid the server knowing about external ids?
            newProductsRequest.products.add(apiProduct);
        }

        syncProductApiService.createProducts(accountNumber, newProductsRequest, new ProductCreateCallback());
    }

    private class ProductCreateCallback implements Callback<NewProductsResponse> {
        @Override
        public void success(NewProductsResponse model, Response response) {
            for (NewProductResponse createdProduct : model.newProductResponses) {
                IProduct product = productsRepository.getProduct(createdProduct.externalId);
                product.setRemoteId(createdProduct.id);
                product.setLastUpdatedDate(DateTime.now());
//                productsRepository.updateProduct(product);
//                Log.i(TAG, String.format("Product id %d created with RemoteId %d", createdProduct.externalId, createdProduct.id));
            }
        }

        @Override
        public void failure(RetrofitError error) {
            String baseErrorMessage = "NEW PRODUCTS SYNC HAS DEADED:";
            printError(error, baseErrorMessage);
        }
    }

    private void syncUpdatedProducts(String accountNumber) {
        Boolean haveUpdatedProductsToSync = false;

        UpdateProductsRequest updateProductsRequest = new UpdateProductsRequest();
        updateProductsRequest.updateProductRequests = new ArrayList<>();

        final DateTime currentDateStamp = DateTime.now();
        final ISyncData lastSyncedDate = syncRepository.getLatestSync();

        List<IProduct> productsToUpdate = productsRepository.getProductsToUpdate(lastSyncedDate.getLastSync().toDate().getTime(), currentDateStamp.toDate().getTime());

        for (IProduct product : productsToUpdate) {
            haveUpdatedProductsToSync = true;
            UpdateProductRequest apiProduct = new UpdateProductRequest();
            apiProduct.externalId = product.getId();
            apiProduct.deleted = product.getDeleted();
            apiProduct.id = product.getRemoteId();
            apiProduct.name = product.getName();
            updateProductsRequest.updateProductRequests.add(apiProduct);
        }

        if (haveUpdatedProductsToSync) {
            ProductUpdateCallback callback = new ProductUpdateCallback(currentDateStamp);

            syncProductApiService.updateProducts(accountNumber, updateProductsRequest, callback);
        }
    }

    private class ProductUpdateCallback implements Callback<UpdateProductsResponse> {
        private final DateTime currentDateStamp;

        public ProductUpdateCallback(DateTime currentDateStamp) {
            this.currentDateStamp = currentDateStamp;
        }

        @Override
        public void success(UpdateProductsResponse model, Response response) {
            syncRepository.setLastSync(currentDateStamp);

            for (UpdatedProduct updatedProduct : model.updatedProducts) {
                IProduct product = productsRepository.getProduct(updatedProduct.externalId);
                // TODO handle if null
                product.setLastUpdatedDate(currentDateStamp);
//                productsRepository.updateProduct(product);
                Log.i(TAG, String.format("Product id %d updated with RemoteId %d", updatedProduct.externalId, updatedProduct.id));
            }
        }

        @Override
        public void failure(RetrofitError error) {
            String baseErrorMessage = "UPDATED PRODUCTS SYNC HAS DEADED:";
            printError(error, baseErrorMessage);
        }
    }

    private void printError(RetrofitError error, String baseErrorMessage) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(baseErrorMessage);
        if (error.getResponse() != null) {
            errorMessage.append(error.getResponse().getReason());
        }

        Log.i(TAG, errorMessage.toString());
    }
}
