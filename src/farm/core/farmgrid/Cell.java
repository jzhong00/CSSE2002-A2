package farm.core.farmgrid;

import java.util.List;

public class Cell {
    private Entity entity;
    private List<String> positionInfo;

    public void addEntity(Entity entity, List<String> positionInfo) {
        this.entity = entity;
        this.positionInfo = positionInfo;
    }

    public Entity getEntity() {
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
