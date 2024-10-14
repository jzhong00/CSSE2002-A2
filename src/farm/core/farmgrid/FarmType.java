package farm.core.farmgrid;

/**
 * A collection of supported farm types.
 */
public enum FarmType {
    PLANT("plant"),
    ANIMAL("animal");

    private final String name;

    /**
     * Constructor for a FarmType
     * @param name the name of the type.
     */
    FarmType(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the farm type.
     * @return a string of the farm type.
     */
    public String getName() {
        return name;
    }

    /**
     * Instantiates a FarmType based on the name of the type
     * @param name the string name of the type
     * @return the FarmType corresponding to the name
     * @throws IllegalArgumentException if the farm type is not found
     */
    public static FarmType fromString(String name) {
        for (FarmType farmType : FarmType.values()) {
            if (farmType.getName().equals(name)) {
                return farmType;
            }
        }
        throw new IllegalArgumentException("Invalid farm type: " + name);
    }
}
