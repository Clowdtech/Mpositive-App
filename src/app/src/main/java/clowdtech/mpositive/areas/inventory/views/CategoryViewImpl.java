package clowdtech.mpositive.areas.inventory.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.Category;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import clowdtech.mpositive.App;
import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.inventory.Container;
import clowdtech.mpositive.areas.inventory.activities.ProductManagement;
import clowdtech.mpositive.areas.inventory.presenters.CategoryPresenter;
import clowdtech.mpositive.areas.shared.InventoryItem;
import clowdtech.mpositive.controls.ColorPickerDialog;
import clowdtech.mpositive.controls.ColorPickerSwatch;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class CategoryViewImpl extends RelativeLayout implements CategoryView, PresentedView, Presentable {
    @Inject
    protected CategoryPresenter presenter;

    @Bind(R.id.sku_add_name)
    protected EditText nameText;

    @Bind(R.id.productSelection)
    protected ProductSelectView productsDisplay;

    private ProductSelectView productsDialogView;

    private Dialog productsDialog;

    @Bind(R.id.item_container)
    protected CardView tileBackground;

    @Bind(R.id.tile_title)
    protected TextView tileForeground;

    @Bind(R.id.category_visible_home)
    protected CheckBox visibleOnHomeToggle;

    @Bind(R.id.background_colour_indicator)
    protected View backgroundColourIndicator;

    @Bind(R.id.foreground_colour_indicator)
    protected View foregroundColourIndicator;

    private int[] colors;

    public CategoryViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        buildProductsDialog(context, attrs);

        ((App) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Resources resources = getResources();

        colors = new int[]{
                resources.getColor(R.color.tile_category_background_default),
                resources.getColor(R.color.tile_category_foreground_default),
                resources.getColor(R.color.font_primary),
                resources.getColor(R.color.tile_category_one),
                resources.getColor(R.color.tile_category_two),
                resources.getColor(R.color.tile_category_three),
                resources.getColor(R.color.tile_category_four),
                resources.getColor(R.color.tile_category_five),
                resources.getColor(R.color.tile_category_six),
                resources.getColor(R.color.tile_category_seven),
                resources.getColor(R.color.tile_category_eight),
                resources.getColor(R.color.tile_category_nine),
                resources.getColor(R.color.tile_category_ten),
                resources.getColor(R.color.tile_category_eleven)
        };

        ButterKnife.bind(this);

        productsDisplay.setOnProductCreate(new OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addProduct();
            }
        });

        productsDisplay.setOnProductSelect(new OnItemSelectionListener<InventoryItem>() {
            @Override
            public void onItemSelected(final InventoryItem productViewModel) {
                String unformattedTitle = getResources().getString(R.string.category_product_remove_title);

                String title = String.format(unformattedTitle, productViewModel.getTitle(), getName());

                new AlertDialog.Builder(getContext())
                        .setTitle(title)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.productRemoved(productViewModel.getItemId());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });

        if (isInEditMode()) {
            return;
        }

//        presenter.bindView(this);
    }

    private void buildProductsDialog(Context context, AttributeSet attrs) {

        productsDialog = new Dialog(context);

        productsDialogView = new ProductSelectView(context, attrs);

        productsDialogView.hideNewItemOption();

        productsDialogView.setOnProductSelect(new OnItemSelectionListener<InventoryItem>() {
            @Override
            public void onItemSelected(InventoryItem category) {
                presenter.productAdded(category.getItemId());

                productsDialog.cancel();
            }
        });

        productsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ScrollView view = new ScrollView(context, attrs);

        view.addView(productsDialogView);

        final float scale = getResources().getDisplayMetrics().density;
        int padding_5dp = (int) (5 * scale + 0.5f);

        view.setPadding(padding_5dp, padding_5dp, padding_5dp, padding_5dp);

        productsDialog.setContentView(view);
    }

    @Override
    public String getName() {
        return String.valueOf(nameText.getText());
    }

    @Override
    public void setName(String value) {
        nameText.setText(value);
    }

    @Override
    public void setProducts(List<IProduct> dataSource) {
        productsDisplay.setItems(dataSource);
    }

    @Override
    public void refreshCategories(List<IProduct> categories) {
        productsDisplay.refreshView(categories);
    }

    @Override
    public void setTileBackground(int background) {
        tileBackground.setCardBackgroundColor(background);
    }

    @Override
    public void setTileTextColour(int foreground) {
        tileForeground.setTextColor(foreground);
    }

    @Override
    public void setTileText(String name) {
        tileForeground.setText(name);
    }

    @Override
    public void setButtonBackgroundColour(int color) {
        backgroundColourIndicator.setBackgroundColor(color);
    }

    @Override
    public void setButtonForegroundColour(int color) {
        foregroundColourIndicator.setBackgroundColor(color);
    }

    public void setItem(Category cat) {
        presenter.setItem(cat);
    }

    @Override
    public Container getContainer() {
        return ((ProductManagement)getContext()).getContainer();
    }

    @Override
    public int getTileBackgroundColor() {
        ColorDrawable background = (ColorDrawable) backgroundColourIndicator.getBackground();

        return background.getColor();
    }

    @Override
    public int getTileForegroundColor() {
        ColorDrawable background = (ColorDrawable) foregroundColourIndicator.getBackground();

        return background.getColor();
    }

    @Override
    public void setAvailableProducts(List<IProduct> availableProducts) {
        productsDialogView.setItems(availableProducts);
    }

    @Override
    public boolean getVisibleOnHomePage() {
        return visibleOnHomeToggle.isChecked();
    }

    @Override
    public void setVisibleOnHomePage(boolean value) {
        visibleOnHomeToggle.setChecked(value);
    }

    @Override
    public void showProductsPicker() {
        productsDialog.show();
    }

    @OnTextChanged(R.id.sku_add_name)
    public void onNameChanged(CharSequence text) {
        presenter.categoryNameChanged(text.toString());
    }

    @OnClick(R.id.product_save)
    public void onSaveClick() {
        presenter.save();
    }

    @OnClick(R.id.tile_foreground)
    public void onTileForegroundClick() {
        ColorPickerDialog dialog = ColorPickerDialog.newInstance(
                R.string.color_picker_foreground_title,
                colors,
                getResources().getColor(R.color.tile_category_foreground_default),
                4,
                ColorPickerDialog.SIZE_SMALL);

        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                presenter.tileForegroundColourSelected(color);
            }
        });

        dialog.show(((Activity) getContext()).getFragmentManager(), "col");
    }

    @OnClick(R.id.tile_background)
    public void onTileBackgroundClick() {
        ColorPickerDialog dialog = ColorPickerDialog.newInstance(
                R.string.color_picker_background_title,
                colors,
                getResources().getColor(R.color.tile_category_background_default),
                4,
                ColorPickerDialog.SIZE_SMALL);

        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                presenter.tileBackgroundColourSelected(color);
            }
        });

        dialog.show(((Activity) getContext()).getFragmentManager(), "col");
    }

    @Override
    public void unbindViews() {
        this.productsDisplay.getPresenter().unbindView();
        this.productsDialogView.getPresenter().unbindView();
    }

    @Override
    public void bindViews() {
        this.productsDisplay.bindView();
        this.productsDialogView.bindView();
    }

    @Override
    public void bindView() {
        presenter.bindView(this);
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }
}
