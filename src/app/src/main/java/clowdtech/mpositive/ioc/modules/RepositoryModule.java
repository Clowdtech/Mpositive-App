package clowdtech.mpositive.ioc.modules;

import com.clowdtech.data.repository.ICategoryRepository;
import com.clowdtech.data.repository.IOrderRepository;
import com.clowdtech.data.repository.IProductRepository;
import com.clowdtech.data.repository.ITransactionsRepository;
import com.clowdtech.data.repository.RepositoryProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    public RepositoryModule() {
    }

    @Provides
    ITransactionsRepository provideTransactionRepository() {
        return RepositoryProvider.getTransactionsRepository();
    }

    @Provides
    IOrderRepository provideOrderRepository() {
        return RepositoryProvider.getOrderRepository();
    }

    @Provides
    IProductRepository provideProductRepository() {
        return RepositoryProvider.getProductsRepository();
    }

    @Provides
    ICategoryRepository provideCategoryRepository() {
        return RepositoryProvider.getCategoriesRepository();
    }
}
