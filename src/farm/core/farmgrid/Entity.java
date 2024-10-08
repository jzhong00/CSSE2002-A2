package farm.core.farmgrid;

import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

public enum Entity {
    BERRY('.', "berry", Barcode.JAM),
    COFFEE(':', "coffee", Barcode.COFFEE),
    WHEAT('\u1F34', "wheat", Barcode.BREAD),
    CHICKEN('\u09EC', "chicken", Barcode.EGG),
    COW('\u096A', "cow", Barcode.MILK),
    SHEEP('\u0D94', "sheep", Barcode.WOOL);

    private final char symbol;
    private final String name;
    private final Barcode barcode;

    Entity(char symbol, String name, Barcode barcode) {
        this.symbol = symbol;
        this.name = name;
        this.barcode = barcode;
    }


    public char getSymbol() {
        return this.symbol;
    }

    public String getName() {
        return this.name;
    }

    public Barcode getBarcode() {
        return this.barcode;
    }

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

    public static Entity getBySymbol(char symbol) {
        for (Entity entity : values()) {
            if (entity.getSymbol() == symbol) {
                return entity;
            }
        }
        throw new IllegalArgumentException("Invalid Symbol");
    }
}
