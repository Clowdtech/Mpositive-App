package clowdtech.mpositive.ioc.components;

import android.content.Context;

import clowdtech.mpositive.ioc.modules.ActivityModule;
import clowdtech.mpositive.ioc.scopes.ActivityScope;
import dagger.Component;

@Component(modules = ActivityModule.class)
@ActivityScope
public interface ActivityComponent {
    Context activityContext();
}
