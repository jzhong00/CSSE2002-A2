package farm.core.farmgrid;

import farm.core.UnableToInteractException;

import java.util.List;

public class Plant extends FarmEntity {

    private int growthStage;
    private String[] growthStageSymbols;

    public Plant(char symbol) {
        super(symbol);
    }

    @Override
    public void checkReadyForHarvest() throws UnableToInteractException {
        if (!isPlantFullyGrown(getFarmEntity())) {
            throw new UnableToInteractException("The crop is not fully grown!");
        }
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
                this.growthStageSymbols = new String[]{".", "o", "@"};
            case "wheat":
                this.growthStageSymbols = new String[]{".", "#"};
            case "coffee":
                this.growthStageSymbols = new String[]{".", "*"};
        }
    }

    public String[] getGrowthStageSymbols() {
        return growthStageSymbols;
    }

    public List<String> grow() throws UnsupportedOperationException {
        if (growthStage < growthStageSymbols.length) {
            return List.of(
                    super.getName(),
                    growthStageSymbols[growthStage],
                    "Stage:" + (growthStage + 1)
            );
        }
        return List.of(
                super.getName(),
                growthStageSymbols[growthStage],
                "Stage: " + growthStage
        );
    }

    private boolean isPlantFullyGrown(FarmEntity plant) {
        String symbol = String.valueOf(plant.getSymbol());
        return symbol.equals("#") || symbol.equals("%") || symbol.equals("@");

    }
}
