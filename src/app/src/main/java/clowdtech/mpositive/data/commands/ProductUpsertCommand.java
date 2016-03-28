package clowdtech.mpositive.data.commands;

import com.clowdtech.data.repository.IProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;

import clowdtech.mpositive.queue.IEventBus;
import clowdtech.mpositive.queue.events.ProductSavedEvent;

public class ProductUpsertCommand extends DataCommand {
    private IProductRepository productRepository;
    private IEventBus eventBus;

    private Long productId;
    private String name;
    private ArrayList<Long> newCategories;
    private ArrayList<Long> oldCategories;
    private BigDecimal price;
    private double vat;
    private String description;

    private Long tileId;
    private String tileBackground;
    private String tileForeground;
    private boolean tileVisibleOnHomePage;
    private boolean tileVisibleInCategory;

    public ProductUpsertCommand(IProductRepository productRepository, IEventBus eventBus) {
        this.productRepository = productRepository;
        this.eventBus = eventBus;
    }

    @Override
    public void execute() {
        long updatedTileId = productRepository.saveTile(this.tileId, this.tileForeground, this.tileBackground, this.tileVisibleOnHomePage, this.tileVisibleInCategory);

        long updatedProductId = productRepository.saveProduct(this.productId, this.name, this.description, this.price, this.vat);

        productRepository.removeFromCategories(updatedProductId, this.oldCategories);

        productRepository.addToCategories(updatedProductId, this.newCategories);

        productRepository.saveTileForProduct(updatedProductId, updatedTileId);

        this.eventBus.post(new ProductSavedEvent(updatedProductId));
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductId(Long id) {
        this.productId = id;
    }

    public void setNewCategories(ArrayList<Long> newCategories) {
        this.newCategories = newCategories;
    }

    public void setOldCategories(ArrayList<Long> oldCategories) {
        this.oldCategories = oldCategories;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setVat(double vat) {
        this.vat = vat;
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

    public void setTileVisibleOnHomePage(boolean tileVisibleOnHomePage) {
        this.tileVisibleOnHomePage = tileVisibleOnHomePage;
    }

    public void setTileVisibleInCategory(boolean tileVisibleInCategory) {
        this.tileVisibleInCategory = tileVisibleInCategory;
    }
}
