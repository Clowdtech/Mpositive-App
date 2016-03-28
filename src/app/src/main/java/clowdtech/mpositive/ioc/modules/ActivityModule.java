package clowdtech.mpositive.ioc.modules;

import android.content.Context;

import clowdtech.mpositive.ioc.qualifiers.QualifierActivity;
import clowdtech.mpositive.ioc.scopes.ActivityScope;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private Context activity;

    public ActivityModule(Context activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    Context provideActivityContext() {
        return activity;
    }
}
