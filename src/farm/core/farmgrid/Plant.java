package farm.core.farmgrid;

import farm.core.UnableToInteractException;

import java.util.List;

/**
 * A class representing an entity of a plant type, which can be placed and grown on the farm.
 */
public class Plant extends FarmEntity {

    // Define growth stage symbols for each of the plant types
    private static final char[] BERRY_GROWTH_STAGES = {'.', 'o', '@'};
    private static final char[] WHEAT_GROWTH_STAGES = {'ἴ', '#'};
    private static final char[] COFFEE_GROWTH_STAGES = {':', ';', '*', '%'};

    private int growthStage;
    private char[] growthStageSymbols;

    /**
     * Constructor for a Plant
     * @param symbol the symbol representing the plant's representation at its initial growth stage.
     * @requires the symbol to represent a valid plant.
     */
    public Plant(char symbol) {
        super(symbol);
        setType("plant");
        this.growthStage = 1;
        this.setGrowthStageSymbols();
    }

    /**
     * Determines whether the plant can be harvested in its current form.
     * (i.e., not fully grown)
     * @throws UnableToInteractException if the plant is not fully grown.
     */
    @Override
    public void checkReadyForHarvest() throws UnableToInteractException {
        if (!isPlantFullyGrown()) {
            throw new UnableToInteractException("The crop is not fully grown!");
        }
    }

    /**
     * Retrieves the symbol for the plant in its current stage.
     * @return a character representing the plant.
     */
    @Override
    public char getSymbol() {
        if (growthStage > 0) {
            // Account for zero indexing of the growthStageSymbols list.
            return growthStageSymbols[growthStage - 1];
        } else {
            // Plant has just been harvested, show the same symbol as stage 1.
            return growthStageSymbols[growthStage];
        }
    }

    @Override
    public List<String> harvestEntity() {
        // Reset the growth stage of the plant to zero.
        setGrowthStage(0);
        return getPositionInfo();
    }

    @Override
    public List<String> feed() throws UnableToInteractException {
        // Plants are unable to be fed.
        throw new UnableToInteractException("You cannot feed something that is not an animal!");
    }

    @Override
    public List<String> reset() {
        if (!isPlantFullyGrown()) {
            setGrowthStage(getGrowthStage() + 1);
        }
        return getPositionInfo();
    }

    @Override
    public List<String> getPositionInfo() {
        return List.of(
          getName(),
          String.valueOf(getSymbol()),
          "Stage: " + getGrowthStage()
        );
    }

    @Override
    public void initialiseFromPositionInfo(List<String> positionInfo) {
        // Get the stage information from positionInfo
        String stageInfo = positionInfo.get(2);
        // Extract the last character, which represents the stage
        char stageChar = stageInfo.charAt(stageInfo.length() - 1);
        // Parse the stage character into an integer
        int stage = Character.getNumericValue(stageChar);

        this.setGrowthStage(stage);
    }


    /** Private helper methods */

    private int getGrowthStage() {
        return growthStage;
    }

    private void setGrowthStage(int growthStage) {
        this.growthStage = growthStage;
    }

    private void setGrowthStageSymbols() {
        switch (getName()) {
            case "berry":
                this.growthStageSymbols = BERRY_GROWTH_STAGES;
                break;
            case "wheat":
                this.growthStageSymbols = WHEAT_GROWTH_STAGES;
                break;
            case "coffee":
                this.growthStageSymbols = COFFEE_GROWTH_STAGES;
                break;
            default:
                throw new IllegalArgumentException("You cannot place that on a plant farm!");
        }
    }

    private boolean isPlantFullyGrown() {
        return growthStage >= growthStageSymbols.length;
    }

}
