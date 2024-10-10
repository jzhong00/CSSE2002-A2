package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

import java.util.List;

/**
 * An abstract class representing an entity that could be placed on the farm.
 * <p>
 * Each FarmEntity is an instance of a type of entity (i.e., plant or animal)
 * </p>
 */
public abstract class FarmEntity {
    private final Entity entity;

    /**
     * Constructs a FarmEntity based on the provided symbol.
     * @param symbol a character representation of the entity to be created.
     */
    protected FarmEntity(char symbol) {
        this.entity = Entity.getBySymbol(symbol);
    }

    /**
     * Factory method to create a FarmEntity instance based on the
     * specified symbol and farm type.
     * @param symbol the symbol representation of a farm entity.
     * @param farmType the type of farm to create an entity for.
     * @return a FarmEntity for the farm.
     * @throws IllegalArgumentException if the farm type is invalid.
     */
    public static FarmEntity createFarmEntity(char symbol, String farmType) {
        return switch (farmType) {
            case "plant" -> new Plant(symbol);
            case "animal" -> new Animal(symbol);
            default -> throw new IllegalArgumentException("Unknown farm type: " + farmType);
        };
    }

    public char getSymbol() {
        return entity.getSymbol();
    }

    public String getName() {
        return entity.getName();
    }

    public Barcode getBarcode() {
        return entity.getBarcode();
    }

    public Product getProduct(Quality quality) {
        return entity.getProduct(quality);
    }

    public abstract void checkReadyForHarvest() throws UnableToInteractException;

    public abstract List<String> harvestEntity();

    public abstract String getType();

    public abstract List<String> feed() throws UnableToInteractException;

    public abstract List<String> reset();

    public abstract List<String> getPositionInfo();

    public abstract void initialiseFromPositionInfo(List<String> positionInfo);

}
