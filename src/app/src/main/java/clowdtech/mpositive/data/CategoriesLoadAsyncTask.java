package clowdtech.mpositive.data;

import android.os.AsyncTask;

import com.clowdtech.data.entities.Category;
import com.clowdtech.data.repository.ICategoryRepository;

import java.util.List;

public class CategoriesLoadAsyncTask extends AsyncTask<Void, Void, List<Category>> {

    private ICategoryRepository categoryRepository;
    private ICategoriesLoadHandler handler;

    public CategoriesLoadAsyncTask(ICategoryRepository categoryRepository, ICategoriesLoadHandler handler) {
        this.categoryRepository = categoryRepository;
        this.handler = handler;
    }

    @Override
    protected List<Category> doInBackground(Void... params) {
        return categoryRepository.getCategories();
    }

    @Override
    protected void onPostExecute(List<Category> inventoryItems) {
        super.onPostExecute(inventoryItems);

        this.handler.categoriesLoaded(inventoryItems);
    }
}
