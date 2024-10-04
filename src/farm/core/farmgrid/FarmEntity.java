package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

import java.util.List;

public abstract class FarmEntity {
    private final Entity entity;

    public FarmEntity(char symbol) {
        this.entity = Entity.getBySymbol(symbol);
    }

    public static FarmEntity createFarmEntity(char symbol, String farmType) throws UnableToInteractException {
        return switch (farmType) {
            case "plant" -> new Plant(symbol);
            case "animal" -> new Animal(symbol);
            default -> throw new IllegalArgumentException("Unknown farm type: " + farmType);
        };
    }

    protected FarmEntity getFarmEntity() {
        return this;
    }

    public int getSymbol() {
        return entity.getSymbol();
    }

    public String getName() {
        return entity.getName();
    }

    public Barcode getBarcode() {
        return entity.getBarcode();
    }

    public String getType() {
        return entity.getType();
    }

    public Product getProduct(Quality quality) {
        return entity.getProduct(quality);
    }

    public abstract List<String> grow() throws UnableToInteractException;

    public abstract void checkReadyForHarvest() throws UnableToInteractException;

    public abstract void harvestEntity() throws UnableToInteractException;
}
