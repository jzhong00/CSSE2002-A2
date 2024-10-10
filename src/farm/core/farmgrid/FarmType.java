package farm.core.farmgrid;

public enum FarmType {
    PLANT("plant"),
    ANIMAL("animal");

    private final String name;

    FarmType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static FarmType fromString(String name) {
        for (FarmType farmType : FarmType.values()) {
            if (farmType.getName().equals(name)) {
                return farmType;
            }
        }
        throw new IllegalArgumentException("Invalid farm type: " + name);
    }
}
