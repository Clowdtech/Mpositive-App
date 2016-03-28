package clowdtech.mpositive.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class BasePresenter<T> implements Presenter<T> {
    protected T view;

    @Override
    public void bindView(T view) {
        this.view = view;
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
    }
}
