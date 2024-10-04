package farm.core.farmgrid;

import java.util.List;

public class Cell {
    private FarmEntity entity;
    private List<String> positionInfo;

    public void addEntity(FarmEntity entity, List<String> positionInfo) {
        this.entity = entity;
        this.positionInfo = positionInfo;
    }

    public FarmEntity getEntity() {
        return entity;
    }

    public List<String> getPositionInfo() {
        return positionInfo;
    }

    public boolean isEmpty() {
        return entity == null;
    }

    public void removeEntity() {
        this.entity = null;
        this.positionInfo = null;
    }

}
