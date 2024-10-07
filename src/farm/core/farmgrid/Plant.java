package farm.core.farmgrid;

import farm.core.UnableToInteractException;

import java.util.List;

public class Plant extends FarmEntity {

    private int growthStage;
    private char[] growthStageSymbols;

    public Plant(char symbol) {
        super(symbol);
        this.growthStage = 1;
        this.setGrowthStageSymbols();
    }

    @Override
    public void checkReadyForHarvest() throws UnableToInteractException {
        if (isPlantFullyGrown()) {
            throw new UnableToInteractException("The crop is not fully grown!");
        }
    }

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
        if (isPlantFullyGrown()) {
            setGrowthStage(getGrowthStage() + 1);
        }
        return getPositionInfo();
    }

    public List<String> getPositionInfo() {
           return List.of(
              getName(),
              String.valueOf(getSymbol()),
              "Stage: " + getGrowthStage()
        );
    }

    public int getGrowthStage() {
        return growthStage;
    }

    public void setGrowthStage(int growthStage) {
        this.growthStage = growthStage;
    }

    private void setGrowthStageSymbols() {
        switch (super.getName()) {
            case "berry":
                this.growthStageSymbols = new char[]{'.', 'o', '@'};
                break;
            case "wheat":
                this.growthStageSymbols = new char[]{'\u1F34', '#'};
                break;
            case "coffee":
                this.growthStageSymbols = new char[]{':', ';', '*', '%'};
                break;
            default:
                throw new IllegalArgumentException("Unknown plant type: " + super.getName());
        }
    }


    private boolean isPlantFullyGrown() {
        String symbol = String.valueOf(super.getFarmEntity().getSymbol());
        return !symbol.equals("#") && !symbol.equals("%") && !symbol.equals("@");
    }
}
