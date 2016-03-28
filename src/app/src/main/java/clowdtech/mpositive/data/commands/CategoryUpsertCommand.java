package clowdtech.mpositive.data.commands;

import com.clowdtech.data.repository.ICategoryRepository;

import java.util.ArrayList;

import clowdtech.mpositive.queue.events.CategorySavedEvent;
import clowdtech.mpositive.queue.IEventBus;

public class CategoryUpsertCommand extends DataCommand {
    private ICategoryRepository categoryRepository;
    private IEventBus eventBus;

    private String name;
    private Long categoryId;
    private ArrayList<Long> newProducts;
    private ArrayList<Long> oldProducts;

    private String tileBackground;
    private String tileForeground;
    private Long tileId;
    private boolean visibility;

    public CategoryUpsertCommand(ICategoryRepository categoryRepository, IEventBus eventBus) {
        this.categoryRepository = categoryRepository;
        this.eventBus = eventBus;
    }

    @Override
    public void execute() {
        long updatedTileId = this.categoryRepository.saveTile(this.tileId, this.tileForeground, this.tileBackground, this.visibility);

        long updatedCategoryId = this.categoryRepository.saveCategory(this.categoryId, this.name);

        categoryRepository.saveTileForCategory(updatedTileId, updatedCategoryId);

        categoryRepository.removeProducts(updatedCategoryId, this.oldProducts);

        categoryRepository.addProducts(updatedCategoryId, this.newProducts);

        this.eventBus.post(new CategorySavedEvent(updatedCategoryId));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.categoryId = id;
    }

    public void setNewProducts(ArrayList<Long> newProducts) {
        this.newProducts = newProducts;
    }

    public void setOldProducts(ArrayList<Long> oldProducts) {
        this.oldProducts = oldProducts;
    }

    public void setTileBackground(String background) {
        this.tileBackground = background;
    }

    public void setTileForeground(String foreground) {
        this.tileForeground = foreground;
    }

    public void setTileId(Long id) {
        this.tileId = id;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
