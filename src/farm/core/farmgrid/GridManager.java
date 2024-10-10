package farm.core.farmgrid;

import java.util.List;

public interface GridManager {
    int getRows();
    int getColumns();
    boolean isValidCell(int row, int column);
    boolean isCellEmpty(int row, int column);
    boolean placeEntity(int row, int column, FarmEntity entity, List<String> positionInfo);
    void removeEntity(int row, int column);
    Cell getCell(int row, int column);
    String getGridDisplay();
    List<List<String>> getTheFarmStatsList();
    void addToCell(int row, int column, FarmEntity entity);
}
