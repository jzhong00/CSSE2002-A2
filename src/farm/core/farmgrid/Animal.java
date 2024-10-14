package farm.core.farmgrid;

import farm.core.UnableToInteractException;

import java.util.List;

/**
 * A class representing an animal that can be placed and interacted with on the farm.
 */
public class Animal extends FarmEntity {

    private boolean fed;
    private boolean collected;

    /**
     * Creates an animal instance, which is set to not fed or collected by default.
     * @param symbol the symbol representing the animal on the farm grid.
     * @requires the symbol to be a valid animal type.
     */
    public Animal(char symbol) {
        super(symbol);
        setType("animal");
        // Fed and collected are false when the animal is initially placed
        fed = false;
        collected = false;
    }

    /**
     * Check if the animal is ready to be harvested.
     * Animals can only be harvested if they have been fed and haven't been collected that day.
     * @throws UnableToInteractException iff they have not been fed or have been collected.
     * @requires animal to be fed and not collected that day
     */
    @Override
    public void checkReadyForHarvest() throws UnableToInteractException {
        if (!getFed()) {
            throw new UnableToInteractException("You have not fed this animal today!");
        }
        if (getCollected()) {
            throw new UnableToInteractException("This animal has produced an item already today!");
        }
    }

    /**
     * Harvests an animal and returns updated information.
     * When harvested, an animal has now been collected.
     * @return information representing the animal's updated state.
     */
    @Override
    public List<String> harvestEntity() {
        // Set the animal as collected
        setCollected(true);
        return getPositionInfo();
    }

    /**
     * Retrieves the current information for an animal.
     * The structure is a list of strings containing its name, symbol, whether it has been fed,
     * and if it has been collected.
     * i.e., ["Cow", '४', "Fed: false", "Collected: true"]
     * @return a list representing the animal's current key information.
     * @ensures the position information returned is a valid representation of the animal's state.
     */
    @Override
    public List<String> getPositionInfo() {
        return List.of(
              getName(),
              String.valueOf(getSymbol()),
              "Fed: " + getFed(),
              "Collected: " + getCollected()
        );
    }

    /**
     * Feeds an animal. This allows it to later be harvested that day.
     * @return information regarding the updated state of the animal.
     */
    @Override
    public List<String> feed() {
        // Set the animal as fed for that day.
        setFed(true);
        return getPositionInfo();
    }

    /**
     * Resets the animal to the state at the beginning of the day.
     * An animal is reset by returning its fed and collected status to false.
     * @return a list representing the reset animal state.
     */
    @Override
    public List<String> reset() {
        // Reset the animal by setting both fed and collected back to false.
        setFed(false);
        setCollected(false);
        return getPositionInfo();
    }

    /**
     * Updates the information for an animal based on a list of strings
     * as is it is represented on the farm grid.
     * Note: the animal is not fed or collected by default when it is instantiated.
     * @param positionInfo the stats to update the entity to.
     * @requires the position information to be a valid representation of the entity's state,
     * in the expected format, e.g., ["Cow", '४', "Fed: false", "Collected: true"]
     */
    @Override
    public void initialiseFromPositionInfo(List<String> positionInfo) {
        // Update the fed status of the animal if the position information for fed is true.
        if (positionInfo.get(2).contains("true")) {
            setFed(true);
        }

        // Update the animal to collected if the information indicates collected as true.
        if (positionInfo.get(3).contains("true")) {
            setCollected(true);
        }
    }

    /** Private helper methods */

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
