package com.clowdtech.data.repository;

import com.clowdtech.data.dbflow.repository.CategoryRepository;
import com.clowdtech.data.dbflow.repository.OrderRepository;
import com.clowdtech.data.dbflow.repository.ProductsRepository;
import com.clowdtech.data.dbflow.repository.SyncRepository;
import com.clowdtech.data.dbflow.repository.TransactionsRepository;

public class RepositoryProvider {
    public static ITransactionsRepository getTransactionsRepository() {
        return new TransactionsRepository();
    }

    public static IOrderRepository getOrderRepository() {
        return new OrderRepository();
    }

    public static IProductRepository getProductsRepository()
    {
        return new ProductsRepository();
    }

    public static ICategoryRepository getCategoriesRepository()
    {
        return new CategoryRepository();
    }

    public static ISyncRepository getSyncRepository() {
        return new SyncRepository();
    }
}
