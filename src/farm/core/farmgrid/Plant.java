package farm.core.farmgrid;

import farm.core.UnableToInteractException;

import java.util.List;

public class Plant extends FarmEntity {

    private static final char[] BERRY_GROWTH_STAGES = {'.', 'o', '@'};
    private static final char[] WHEAT_GROWTH_STAGES = {'\u1F34', '#'};
    private static final char[] COFFEE_GROWTH_STAGES = {':', ';', '*', '%'};

    private int growthStage;
    private char[] growthStageSymbols;

    public Plant(char symbol) {
        super(symbol);
        this.growthStage = 1;
        this.setGrowthStageSymbols();
    }

    @Override
    public void checkReadyForHarvest() throws UnableToInteractException {
        if (!isPlantFullyGrown()) {
            throw new UnableToInteractException("The crop is not fully grown!");
        }
    }

    @Override
    public char getSymbol() {
        if (growthStage > 0) {
            return growthStageSymbols[growthStage - 1];
        } else {
            return growthStageSymbols[growthStage];
        }
    }

    @Override
    public List<String> harvestEntity() {
        setGrowthStage(0);
        return getPositionInfo();
    }

    @Override
    public List<String> feed() throws UnableToInteractException {
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
    public String getType() {
        return "plant";
    }

    @Override
    public void initialiseFromPositionInfo(List<String> positionInfo) {
        int stage = positionInfo.get(2).charAt(positionInfo.get(2).length() - 1);
        this.setGrowthStage(stage);
    }

    public int getGrowthStage() {
        return growthStage;
    }

    public void setGrowthStage(int growthStage) {
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
                throw new IllegalArgumentException("Unknown plant type: " + getName());
        }
    }


    private boolean isPlantFullyGrown() {
        return growthStage >= growthStageSymbols.length;
    }
}
