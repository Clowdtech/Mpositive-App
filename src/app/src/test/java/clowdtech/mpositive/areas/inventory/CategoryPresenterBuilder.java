package clowdtech.mpositive.areas.inventory;

import com.clowdtech.data.repository.ICategoryRepository;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import clowdtech.mpositive.areas.inventory.presenters.CategoryPresenter;
import clowdtech.mpositive.areas.inventory.views.CategoryView;
import clowdtech.mpositive.data.InventoryStore;
import clowdtech.mpositive.queue.IEventBus;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;

public class CategoryPresenterBuilder {
    @Mock
    InventoryStore inventoryStore;

    @Mock
    IEventBus mockEventBus;

    @Mock
    ICategoryRepository categoryRepository;

    @Mock
    CategoryView mockView;

    @Captor
    ArgumentCaptor<Boolean> booleanArgumentCaptor;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    public CategoryPresenterBuilder() {
        MockitoAnnotations.initMocks(this);
    }

    public CategoryPresenter buildAndBind() {
        CategoryPresenter presenter = new CategoryPresenter(inventoryStore, categoryRepository, mockEventBus);

        presenter.bindView(mockView);

        return presenter;
    }

    public void verifyViewSetVisibleOnHomePage(Boolean visible) {
        verify(mockView).setVisibleOnHomePage(booleanArgumentCaptor.capture());

        assertEquals(visible, booleanArgumentCaptor.getValue());
    }

    public void verifyViewSetBackgroundTile() {
        verify(mockView).setTileBackground(anyInt());
    }

    public void verifyViewSetBackgroundButton() {
        verify(mockView).setButtonBackgroundColour(anyInt());
    }

    public void verifyViewSetForegroundTile() {
        verify(mockView).setTileTextColour(anyInt());
    }

    public void verifyViewSetForegroundButton() {
        verify(mockView).setButtonForegroundColour(anyInt());
    }

    public void verifyViewSetName(String name) {
        verify(mockView).setName(stringArgumentCaptor.capture());

        assertEquals(name, stringArgumentCaptor.getValue());
    }
}
