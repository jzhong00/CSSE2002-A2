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
    private String type;

    /**
     * Constructs a FarmEntity based on the provided symbol.
     * @param symbol a character representation of the entity to be created.
     * @requires the symbol to be valid and represent an entity.
     */
    protected FarmEntity(char symbol) {
        // Create a FarmEntity based on its symbol
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

    /**
     * Retrieves the symbol that represents the FarmEntity on the grid.
     * @return a character symbolising the entity.
     */
    public char getSymbol() {
        return entity.getSymbol();
    }

    /**
     * Returns the FarmEntity's name.
     * @return the name of the entity.
     */
    public String getName() {
        return entity.getName();
    }

    /**
     * Returns the type of the entity.
     * @return a string representing the entity's type.
     */
    public String getType() {
        return entity.getFarmType().getName();
    }

    /**
     * Creates and returns the product of the specified quality produced by harvesting the entity.
     * @param quality the quality of the produced product.
     * @return a Product harvested from the entity.
     */
    public Product getProduct(Quality quality) {
        return entity.getProduct(quality);
    }

    /**
     * Sets the type of the farm entity to be used in the constructor of an entity.
     * @param type the type of the entity
     * @requires the type to be valid (i.e., plant or animal)
     */
    protected void setType(String type) {
        this.type = type;
    }

    /**
     * Checks whether the FarmEntity is ready to be harvested.
     * @throws UnableToInteractException if any of entity's preconditions are violated.
     */
    public abstract void checkReadyForHarvest() throws UnableToInteractException;

    /**
     * Harvests the FarmEntity and returns the updated representation of the entity.
     * @return an updated list of strings representing the entity's state.
     */
    public abstract List<String> harvestEntity();


    /**
     * Feeds the entity if it is able to be fed (i.e., an animal).
     * @return the updated entity information if it is fed successfully.
     * @throws UnableToInteractException if the entity cannot be fed.
     * @requires the entity to be of a type that is able to be fed.
     */
    public abstract List<String> feed() throws UnableToInteractException;

    /**
     * Resets an entity at the end of the day, depending on what type of entity it is.
     * @return the updated entity information after it has been reset.
     */
    public abstract List<String> reset();

    /**
     * Gets the position information for an entity.
     * @return the entity's current information.
     */
    public abstract List<String> getPositionInfo();

    /**
     * Updates an entity's internal information (such as its stage), based on a list of information.
     * @param positionInfo the stats to update the entity to.
     */
    public abstract void initialiseFromPositionInfo(List<String> positionInfo);

}
