package clowdtech.mpositive.ui;

import android.os.Bundle;
import android.view.View;

public class PresenterControllerDelegate<P extends Presenter> {
    private boolean isDestroyedBySystem;
    private P presenter;

    public void onCreate(P presenter, Bundle savedInstanceState) {
        this.presenter = presenter;
        presenter.onCreate(savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    public void onViewCreated(View view) {
        try {
            presenter.bindView(view);
        } catch (ClassCastException e) {
            throw new RuntimeException("The view provided does not"
                    + " implement the view interface expected by "
                    + presenter.getClass().getSimpleName(), e);
        }
    }

    public void onResume() {
        isDestroyedBySystem = false;
    }

    public void onSaveInstanceState(Bundle outState) {
        isDestroyedBySystem = true;

        presenter.onSaveInstanceState(outState);
    }

    public void onDestroyView() {
        presenter.unbindView();
    }

    public void onDestroy() {
        if (!isDestroyedBySystem) {
            presenter.onDestroy();
        }
    }
}
