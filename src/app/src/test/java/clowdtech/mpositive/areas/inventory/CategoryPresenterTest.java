package clowdtech.mpositive.areas.inventory;

import com.clowdtech.data.entities.Category;
import com.clowdtech.data.entities.CategoryTile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import clowdtech.mpositive.areas.inventory.presenters.CategoryPresenter;

import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CategoryPresenterTest {
    CategoryPresenterBuilder presenterBuilder;

    @Mock
    Category mockCategory;

    @Mock
    CategoryTile mockTile;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        doReturn(mockTile).when(mockCategory).getTile();

        doReturn("#FFFFFF").when(mockTile).getBackground();
        doReturn("#FFFFFF").when(mockTile).getForeground();

        presenterBuilder = new CategoryPresenterBuilder();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void set_item_category_name_set() {
        CategoryPresenter presenter = presenterBuilder
                .buildAndBind();

        doReturn("mars bar").when(mockCategory).getName();

        presenter.setItem(mockCategory);

        presenterBuilder.verifyViewSetName("mars bar");
    }

    @Test
    public void set_item_tile_visible_set() {
        CategoryPresenter presenter = presenterBuilder
                .buildAndBind();

        doReturn(true).when(mockTile).getVisibleOnHomePage();

        presenter.setItem(mockCategory);

        presenterBuilder.verifyViewSetVisibleOnHomePage(true);
    }

    @Test
    public void set_item_background_tile_set() {
        CategoryPresenter presenter = presenterBuilder
                .buildAndBind();

        doReturn("#FFFFFF").when(mockTile).getBackground();

        presenter.setItem(mockCategory);

        presenterBuilder.verifyViewSetBackgroundTile();
    }

    @Test
    public void set_item_background_button_set() {
        CategoryPresenter presenter = presenterBuilder
                .buildAndBind();

        doReturn("#FFFFFF").when(mockTile).getBackground();

        presenter.setItem(mockCategory);

        presenterBuilder.verifyViewSetBackgroundButton();
    }

    @Test
    public void set_item_foreground_tile_set() {
        CategoryPresenter presenter = presenterBuilder
                .buildAndBind();

        doReturn("#FFFFFF").when(mockTile).getForeground();

        presenter.setItem(mockCategory);

        presenterBuilder.verifyViewSetForegroundTile();
    }

    @Test
    public void set_item_foreground_button_set() {
        CategoryPresenter presenter = presenterBuilder
                .buildAndBind();

        doReturn("#FFFFFF").when(mockTile).getForeground();

        presenter.setItem(mockCategory);

        presenterBuilder.verifyViewSetForegroundButton();
    }
}
