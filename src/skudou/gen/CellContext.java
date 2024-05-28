package skudou.gen;

import java.util.List;

public class CellContext {

	private Cell currentCell;
	private List<Cell> cellList;
	
	public CellContext(Cell currentCell, List<Cell> cellList) {
		this.currentCell = currentCell;
		this.cellList = cellList;
	}

	public Cell getCurrentCell() {
		return currentCell;
	}

	public List<Cell> getCellList() {
		return cellList;
	}

}
