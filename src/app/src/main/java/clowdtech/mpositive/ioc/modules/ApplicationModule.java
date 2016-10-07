package clowdtech.mpositive.ioc.modules;

import android.content.Context;
import android.content.res.Resources;

import com.clowdtech.data.repository.ICategoryRepository;
import com.clowdtech.data.repository.IProductRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import clowdtech.mpositive.App;
import clowdtech.mpositive.ISharedPreferences;
import clowdtech.mpositive.MposPreferences;
import clowdtech.mpositive.data.InventoryStore;
import clowdtech.mpositive.data.InventoryStoreImpl;
import clowdtech.mpositive.ioc.qualifiers.QualifierApplication;
import clowdtech.mpositive.queue.EventBus;
import clowdtech.mpositive.queue.IEventBus;
import dagger.Module;
import dagger.Provides;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */
@Module
public class ApplicationModule {
    private final App application;

    public ApplicationModule(App application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link QualifierApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @QualifierApplication
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    IEventBus provideEventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    @Named("data")
    IEventBus provideDataEventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    Resources provideResources() {
        return this.application.getResources();
    }

    @Provides
    ISharedPreferences provideSharedPreferences(@QualifierApplication Context context) {
        return new MposPreferences(context, context.getResources());
    }

    @Provides
    @Singleton
    InventoryStore providesInventoryStore(IProductRepository productsRepo, ICategoryRepository categoriesRepo, IEventBus eventBus) {
        return new InventoryStoreImpl(productsRepo, categoriesRepo, eventBus, application);
    }
}