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
    public List<String> grow() throws UnableToInteractException{
        throw new UnableToInteractException();
    }

    private boolean getFed() {
        return fed;
    }

    private void setFed(boolean fed) {
        this.fed = fed;
    }

    private boolean getCollected() {
        return collected;
    }

    private void setCollected(boolean collected) {
        this.collected = collected;
    }
}
