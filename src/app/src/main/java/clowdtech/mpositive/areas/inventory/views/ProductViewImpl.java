package clowdtech.mpositive.areas.inventory.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.Category;

import java.math.BigDecimal;
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
import clowdtech.mpositive.areas.inventory.presenters.ProductPresenter;
import clowdtech.mpositive.areas.inventory.viewModels.CategoryViewModel;
import clowdtech.mpositive.areas.shared.views.NumericEntryView;
import clowdtech.mpositive.controls.ColorPickerDialog;
import clowdtech.mpositive.controls.ColorPickerSwatch;
import clowdtech.mpositive.lifecycle.PresentedView;
import clowdtech.mpositive.ui.Presentable;
import clowdtech.mpositive.ui.Presenter;

public class ProductViewImpl extends RelativeLayout implements ProductView, PresentedView, Presentable {
    @Inject
    protected ProductPresenter presenter;

    private CategorySelectView categoriesDialogView;

    private Dialog categoriesDialog;

    @Bind(R.id.sku_add_name) protected EditText nameText;
    @Bind(R.id.product_visible_home) protected CheckBox visibleOnHomeToggle;
    @Bind(R.id.product_visible_category) protected CheckBox visibleInCategoryToggle;
    @Bind(R.id.sku_description) protected EditText descriptionText;
    @Bind(R.id.product_vat) protected RadioGroup vatRadios;
    @Bind(R.id.product_radio_zero) protected RadioButton vatZero;
    @Bind(R.id.product_radio_five) protected RadioButton vatFive;
    @Bind(R.id.product_radio_twenty) protected RadioButton vatTwenty;
    @Bind(R.id.scrollie) protected ScrollView scrollie;
    @Bind(R.id.categorySelection) protected CategorySelectView categoriesDisplay;
    @Bind(R.id.numeric_entry) protected NumericEntryView numericEntry;
    @Bind(R.id.item_container) protected CardView tileBackground;
    @Bind(R.id.tile_title) protected TextView tileTitle;
    @Bind(R.id.tile_subtitle) protected TextView tilePrice;
    @Bind(R.id.background_colour_indicator) protected View backgroundColourIndicator;
    @Bind(R.id.foreground_colour_indicator) protected View foregroundColourIndicator;

    private int[] colors;

    public ProductViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        buildCategoriesDialog(context, attrs);

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

        numericEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.priceDisplayChanged(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        categoriesDisplay.setOnCategoryCreate(new OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addCategory();
            }
        });

        categoriesDisplay.setOnCategorySelect(new OnItemSelectionListener<CategoryViewModel>() {
            @Override
            public void onItemSelected(final CategoryViewModel category) {
                String unformattedTitle = getResources().getString(R.string.products_category_remove_title);

                String title = String.format(unformattedTitle, getName(), category.getName());

                new AlertDialog.Builder(getContext())
                        .setTitle(title)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.categoryRemoved(category.getId());
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

    private void buildCategoriesDialog(Context context, AttributeSet attrs) {
        categoriesDialog = new Dialog(context);

        categoriesDialogView = new CategorySelectView(context, attrs);

        categoriesDialogView.hideNewItemOption();

        categoriesDialogView.setOnCategorySelect(new OnItemSelectionListener<CategoryViewModel>() {
            @Override
            public void onItemSelected(CategoryViewModel category) {
                presenter.categoryAdded(category.getId());

                categoriesDialog.cancel();
            }
        });

        categoriesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ScrollView view = new ScrollView(context, attrs);

        view.addView(categoriesDialogView);

        final float scale = getResources().getDisplayMetrics().density;
        int padding_5dp = (int) (5 * scale + 0.5f);

        view.setPadding(padding_5dp, padding_5dp, padding_5dp, padding_5dp);

        categoriesDialog.setContentView(view);
    }

    public void setName(String value) {
        nameText.setText(value);
    }

    public void setPrice(BigDecimal value) {
        numericEntry.setValue(value);
    }

    public void setVat(double value) {
        if (value == 5.0) {
            vatFive.setChecked(true);
        } else if (value == 20.0) {
            vatTwenty.setChecked(true);
        } else {
            vatZero.setChecked(true);
        }
    }

    public void setVisibleOnHomePage(boolean value) {
        visibleOnHomeToggle.setChecked(value);
    }

    public void setDescription(String value) {
        descriptionText.setText(value);
    }

    public void setCategories(List<Category> dataSource) {
        categoriesDisplay.setCategories(dataSource);
    }


    public String getName() {
        return String.valueOf(nameText.getText());
    }

    public BigDecimal getPrice() {
        return numericEntry.getValue();
    }

    public boolean getVisibleOnHomePage() {
        return visibleOnHomeToggle.isChecked();
    }

    public String getDescription() {
        return String.valueOf(descriptionText.getText());
    }

    public double getVat() {
        double vat = 0.00;

        switch (vatRadios.getCheckedRadioButtonId()) {
            case R.id.product_radio_five:
                vat = 5.00;
                break;
            case R.id.product_radio_twenty:
                vat = 20.00;
                break;
        }

        return vat;
    }

    public void refreshCategories(List<Category> categories) {
        categoriesDisplay.refreshView(categories);
    }

    public void setVisibleInCategory(boolean visibleInCategory) {
        visibleInCategoryToggle.setChecked(visibleInCategory);
    }

    public boolean getVisibleInCategory() {
        return visibleInCategoryToggle.isChecked();
    }

    public void setTileBackground(int background) {
        tileBackground.setCardBackgroundColor(background);
    }

    public void setTileTextColour(int foreground) {
        tileTitle.setTextColor(foreground);
        tilePrice.setTextColor(foreground);
    }

    public void setTileTitle(String name) {
        tileTitle.setText(name);
    }

    public void setTilePrice(String name) {
        tilePrice.setText(name);
    }

    public void setBackgroundButtonColour(int color) {
        backgroundColourIndicator.setBackgroundColor(color);
    }

    public void setForegroundButtonColour(int color) {
        foregroundColourIndicator.setBackgroundColor(color);
    }

    public void confirmDelete() {
        String unformattedTitle = getResources().getString(R.string.product_delete_title);

        String title = String.format(unformattedTitle, getName());

        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(getResources().getString(R.string.product_delete_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.delete();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    public void setItem(IProduct product) {
        this.presenter.setItem(product);
    }

    public Container getContainer() {
        return ((ProductManagement)getContext()).getContainer();
    }

    public void showActionItems() {
        final View viewMenu = inflate(getContext(), R.layout.action_bar_items_product, null);

        viewMenu.findViewById(R.id.item_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteActionItemClick();
            }
        });

        presenter.setMenu(viewMenu);
    }

    public int getTileBackgroundColor() {
        ColorDrawable background = (ColorDrawable) backgroundColourIndicator.getBackground();

        return background.getColor();
    }

    public int getTileForegroundColor() {
        ColorDrawable background = (ColorDrawable) foregroundColourIndicator.getBackground();

        return background.getColor();
    }

    @OnTextChanged(R.id.sku_add_name)
    public void onNameChanged(CharSequence text) {
        presenter.nameChanged(text.toString());
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

    public void setAvailableProducts(List<Category> categories) {
        categoriesDialogView.setCategories(categories);
    }

    public void showCategoriesPicker() {
        categoriesDialog.show();
    }

    @Override
    public void unbindViews() {
        this.categoriesDisplay.getPresenter().unbindView();
        this.categoriesDialogView.getPresenter().unbindView();
    }

    @Override
    public void bindViews() {
        this.categoriesDisplay.bindView();
        this.categoriesDialogView.bindView();
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
