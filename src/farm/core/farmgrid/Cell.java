package farm.core.farmgrid;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cell in the farm grid that could contain a {@link FarmEntity}
 * and its associated position information.
 */
public class Cell {

    private FarmEntity entity;
    private List<String> positionInfo;

    /**
     * Adds a FarmEntity to the cell, along with its position information.
     * @param entity the FarmEntity to be added to the cell
     * @param positionInfo a list of information associated with the entity
     */
    public void addEntity(FarmEntity entity, List<String> positionInfo) {
        this.entity = entity;
        this.positionInfo = positionInfo;
    }

    /**
     * Retrieves the entity currently in the cell.
     * @return the FarmEntity in the cell, or null if the cell is empty.
     */
    public FarmEntity getEntity() {
        return entity;
    }

    /**
     * Retrieves the position information of the entity in the cell.
     * @return a shallow copy representing the entity's information, or null
     * if the cell is empty.
     */
    public List<String> getPositionInfo() {
        return new ArrayList<>(positionInfo);
    }

    /**
     * Checks if the cell is empty. The cell is empty if there is no associated entity.
     * @return true if and only if the cell is empty, false otherwise.
     */
    public boolean isEmpty() {
        return entity == null;
    }

    /**
     * Removes the current entity and its associated information from the cell.
     */
    public void removeEntity() {
        this.entity = null;
        this.positionInfo = null;
    }
}
