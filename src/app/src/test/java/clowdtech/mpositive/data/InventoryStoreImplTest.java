package clowdtech.mpositive.data;

import android.content.Context;
import android.content.res.Resources;

import com.clowdtech.data.entities.IProduct;
import com.clowdtech.data.entities.IProductTile;
import com.clowdtech.data.entities.Category;
import com.clowdtech.data.repository.ICategoryRepository;
import com.clowdtech.data.entities.CategoryTile;
import com.clowdtech.data.repository.IProductRepository;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import clowdtech.mpositive.queue.IEventBus;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class InventoryStoreImplTest {
    @Mock
    IProductRepository mockProductRepo;

    @Mock
    ICategoryRepository mockCategoryRepo;

    @Mock
    IEventBus mockEventBus;

    @Mock
    Context mockContext;

    @Mock
    Resources mockResources;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void get_product_expect_tile_set() throws Exception {
        doReturn(new IProduct() {
            public IProductTile tile;

            @Override
            public BigDecimal getPrice() {
                return null;
            }

            @Override
            public void setPrice(BigDecimal price) {

            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public Boolean getDeleted() {
                return null;
            }

            @Override
            public int getRemoteId() {
                return 0;
            }

            @Override
            public Long getId() {
                return null;
            }

            @Override
            public double getVat() {
                return 0;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void setName(String name) {

            }

            @Override
            public void setVat(double vat) {

            }

            @Override
            public void setDescription(String description) {

            }

            @Override
            public void setLastUpdatedDate(DateTime lastUpdate) {

            }

            @Override
            public void setRemoteId(int id) {

            }

            @Override
            public void setDeleted(boolean deleted) {

            }

            @Override
            public void setTile(IProductTile tile) {
                this.tile = tile;
            }

            @Override
            public IProductTile getTile() {
                return tile;
            }
        }).when(mockProductRepo).getProduct(anyLong());
        doReturn(mockResources).when(mockContext).getResources();
        doReturn(new IProductTile() {
            public String foreground;
            public String background;

            @Override
            public boolean getVisibleOnHomePage() {
                return false;
            }

            @Override
            public void setVisibleOnHomePage(boolean visible) {

            }

            @Override
            public boolean getVisibleInCategory() {
                return false;
            }

            @Override
            public void setVisibleInCategory(boolean visible) {

            }

            @Override
            public String getBackground() {
                return background;
            }

            @Override
            public void setBackground(String background) {
                this.background = background;
            }

            @Override
            public String getForeground() {
                return this.foreground;
            }

            @Override
            public void setForeground(String foreground) {
                this.foreground = foreground;
            }

            @Override
            public Long getId() {
                return null;
            }
        }).when(mockProductRepo).getProductTile();

        InventoryStoreImpl inventoryStore = new InventoryStoreImpl(mockProductRepo, mockCategoryRepo, mockEventBus, mockContext);

        IProduct product = inventoryStore.getProduct(5);

        assertNotNull(product.getTile().getBackground());
        assertNotNull(product.getTile().getForeground());
    }

    @Test
    public void get_category_expect_tile_set() throws Exception {
        Category stubCategory = new Category() {
            public CategoryTile tile;

            @Override
            public String getName() {
                return null;
            }

            @Override
            public CategoryTile getTile() {
                return tile;
            }

            @Override
            public void setTile(CategoryTile tile) {
                this.tile = tile;
            }

            @Override
            public Long getId() {
                return null;
            }

            @Override
            public void setName(String name) {

            }
        };

        CategoryTile stubCategoryTile = new CategoryTile() {
            public boolean visibleOnHome;
            public String background;
            public String foreground;

            @Override
            public Long id() {
                return null;
            }

            @Override
            public String getForeground() {
                return foreground;
            }

            @Override
            public String getBackground() {
                return background;
            }

            @Override
            public void setBackground(String background) {
                this.background = background;
            }

            @Override
            public void setForeground(String foreground) {
                this.foreground = foreground;
            }

            @Override
            public boolean getVisibleOnHomePage() {
                return visibleOnHome;
            }

            @Override
            public void setVisibleOnHomePage(boolean visible) {
                visibleOnHome = visible;
            }
        };

        doReturn(stubCategory).when(mockCategoryRepo).getCategory(anyLong());
        doReturn(stubCategoryTile).when(mockCategoryRepo).getCategoryTile();
        doReturn(mockResources).when(mockContext).getResources();

        InventoryStoreImpl inventoryStore = new InventoryStoreImpl(mockProductRepo, mockCategoryRepo, mockEventBus, mockContext);

        Category category = inventoryStore.getCategory(5);

        assertNotNull(category.getTile().getBackground());
        assertNotNull(category.getTile().getForeground());
    }
}

