package farm.core.farmgrid;

import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;


/**
 * A collection of all entities that can be placed on the grid.
 * <p>
 * Each entity has associated information including its symbol representation, name, and a barcode
 * corresponding to the product they produce when harvested.
 * </p>
 */
public enum Entity {
    BERRY('.', "berry", Barcode.JAM),
    COFFEE(':', "coffee", Barcode.COFFEE),
    WHEAT('\u1F34', "wheat", Barcode.BREAD),
    CHICKEN('\u09EC', "chicken", Barcode.EGG),
    COW('\u096A', "cow", Barcode.MILK),
    SHEEP('\u0D94', "sheep", Barcode.WOOL);

    /**
     * Retrieves the symbol representation of the entity.
     * @return the symbol of the entity's initial state.
     */
    public char getSymbol() {
        return this.symbol;
    }

    /**
     * Returns the name of the entity.
     * @return the string name of the entity.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the Barcode for the product that is produced by the entity.
     * @return the barcode for the entity's product.
     */
    public Barcode getBarcode() {
        return this.barcode;
    }

    /**
     * Returns a Product for this entity with the provided quality.
     * @param quality the quality of the produced product
     * @return a Product object
     * @throws IllegalStateException if the barcode is invalid.
     */
    public Product getProduct(Quality quality) {
        return switch (this.barcode) {
            case MILK -> new Milk(quality);
            case EGG -> new Egg(quality);
            case WOOL -> new Wool(quality);
            case BREAD -> new Bread(quality);
            case COFFEE -> new Coffee(quality);
            case JAM -> new Jam(quality);
            default ->
                    throw new IllegalStateException("Unexpected product barcode: " + this.barcode);
        };
    }

    /**
     * Returns an Entity by its associated symbol
     * @param symbol the symbol representing the entity
     * @return the Entity corresponding to the symbol
     * @throws IllegalStateException if the symbol is not recognised.
     */
    public static Entity getBySymbol(char symbol) {
        for (Entity entity : values()) {
            if (entity.getSymbol() == symbol) {
                return entity;
            }
        }
        throw new IllegalArgumentException("Invalid Symbol");
    }

    public static char getSymbolByName(String name) {
        for (Entity entity : values()) {
            if (entity.getName().equals(name)) {
                return entity.getSymbol();
            }
        }
        throw new IllegalArgumentException("Unable to find symbol for " + name);
    }

    private final char symbol;
    private final String name;
    private final Barcode barcode;

    /**
     * Constructor for an Entity.
     * @param symbol the character symbol representing the initial state of an entity.
     * @param name the name of the entity
     * @param barcode the Barcode associated with the product produced by the entity.
     */
    Entity(char symbol, String name, Barcode barcode) {
        this.symbol = symbol;
        this.name = name;
        this.barcode = barcode;
    }
}
