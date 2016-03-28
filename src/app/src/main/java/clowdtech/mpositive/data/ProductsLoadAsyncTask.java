package clowdtech.mpositive.data;

import android.os.AsyncTask;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.repository.IProductRepository;

import java.util.List;

public class ProductsLoadAsyncTask extends AsyncTask<Void, Void, List<IProduct>> {

    private IProductRepository productsRepo;
    private IProductsLoadHandler handler;

    public ProductsLoadAsyncTask(IProductRepository productsRepo, IProductsLoadHandler handler) {
        this.productsRepo = productsRepo;
        this.handler = handler;
    }

    @Override
    protected List<IProduct> doInBackground(Void... params) {
        List<IProduct> products = productsRepo.getProducts(); // db call

        return products;
    }

    @Override
    protected void onPostExecute(List<IProduct> inventoryItems) {
        super.onPostExecute(inventoryItems);

        handler.productsLoaded(inventoryItems);
    }
}
