package farm.core.farmgrid;

import farm.core.UnableToInteractException;

import java.util.List;

public class Animal extends FarmEntity {

    private boolean fed;
    private boolean collected;


    public Animal(char symbol) {
        super(symbol);
        fed = false;
        collected = false;
    }

    @Override
    public void checkReadyForHarvest() throws UnableToInteractException {
        if (!getFed()) {
            throw new UnableToInteractException("You have not fed this animal today!");
        }
        if (getCollected()) {
            throw new UnableToInteractException("This animal has produced an item already today!");
        }
    }

    @Override
    public List<String> harvestEntity() {
        this.setCollected(true);
        return getPositionInfo();
    }

    @Override
    public List<String> getPositionInfo() {
        return List.of(
              getName(),
              String.valueOf(getSymbol()),
              "Fed: " + getFed(),
              "Collected: " + getCollected()
        );
    }

    @Override
    public List<String> feed() {
        setFed(true);
        return getPositionInfo();
    }

    @Override
    public List<String> reset() {
        setFed(false);
        setCollected(false);
        return getPositionInfo();
    }

    @Override
    public String getType() {
        return "animal";
    }

    public boolean getFed() {
        return fed;
    }

    public void setFed(boolean fed) {
        this.fed = fed;
    }

    public boolean getCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
