package skudou.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public enum GridRule {
	
	SQUARE_IS_SET(cellInfo -> {
		return !getValuesInSquare(cellInfo.getCurrentCell(), cellInfo.getCellList()).contains(cellInfo.getCurrentCell().getExpectedValue());
	}), LINE_IS_SET(cellInfo -> {
		return !getValuesInLine(cellInfo.getCurrentCell(), cellInfo.getCellList()).contains(cellInfo.getCurrentCell().getExpectedValue());
	}), COLUMN_IS_SET(cellInfo -> {
		return !getValuesInColumn(cellInfo.getCurrentCell(), cellInfo.getCellList()).contains(cellInfo.getCurrentCell().getExpectedValue());
	}), DIAGONAL_IS_SET(cellInfo -> {
		return !getValuesInDiagonal(cellInfo.getCurrentCell(), cellInfo.getCellList()).contains(cellInfo.getCurrentCell().getExpectedValue());
	});
	
	// Taille d'un carré = Nombre de carrés sur un côté de la grille
	public static final int SQUARE_SIZE = 3;
	
	private Predicate<CellContext> matches;
	
	private GridRule(Predicate<CellContext> matches) {
		this.matches = matches;
	}
	
	public boolean matches(CellContext cellInfo) {
		return this.matches.test(cellInfo);
	}
	
	private static List<Integer> getValuesInSquare(Cell cell, List<Cell> cellList) {
		List<Integer> valuesInSquare = new ArrayList<>();
		int squareX = (cell.squareNb * GridRule.SQUARE_SIZE) % (GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE);
		int squareY = (int)(Math.floor((float)cell.squareNb / (float)GridRule.SQUARE_SIZE) * GridRule.SQUARE_SIZE);
		for(int y = 0; y < GridRule.SQUARE_SIZE; y++) {
			for (int x = 0; x < GridRule.SQUARE_SIZE; x++) {
				int currentIndex = squareX + x + (squareY + y) * GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE;
				if (currentIndex == cell.cellNb) continue;
				int value = cellList.get(currentIndex).getExpectedValue();
				if (value > 0) valuesInSquare.add(value);
			}
		}
		return valuesInSquare;
	}
	
	private static List<Integer> getValuesInLine(Cell cell, List<Cell> cellList) {
		List<Integer> valuesInLine = new ArrayList<>();
		int lineIndex = (int) Math.floor((float)cell.cellNb / (float)(GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE));
		for(int x = 0; x < GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE; x++) {
			int currentIndex = x + lineIndex * GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE;
			if (currentIndex == cell.cellNb) continue;
			int value = cellList.get(currentIndex).getExpectedValue();
			if (value > 0) valuesInLine.add(value);
		}
		return valuesInLine;
	}
	
	private static List<Integer> getValuesInColumn(Cell cell, List<Cell> cellList) {
		List<Integer> valuesInColumn = new ArrayList<>();
		int colIndex = cell.cellNb % (GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE);
		for(int y = 0; y < GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE; y++) {
			int currentIndex = y * GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE + colIndex;
			if (currentIndex == cell.cellNb) continue;
			int value = cellList.get(currentIndex).getExpectedValue();
			if (value > 0) valuesInColumn.add(value);
		}
		return valuesInColumn;
	}
	
	private static List<Integer> getValuesInDiagonal(Cell cell, List<Cell> cellList) {
		List<Integer> valuesInDiagonal = new ArrayList<>();
		// Diagonale Haut-Gauche/Bas-Droite
		if (cell.x == cell.y) {
			for (int nb = 0; 
				nb < GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE; 
				nb += GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE + 1) {
				if(nb != cell.cellNb && cellList.get(nb).getExpectedValue() > 0) 
					valuesInDiagonal.add(cellList.get(nb).getExpectedValue());
			}
		}
		// Diagonale Haut-Droite/Bas-Gauche
		if((GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE - cell.x - 1) == cell.y) {
			for (int nb = GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE - 1;
				nb < GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE;
				nb += GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE - 1) {
				if(nb != cell.cellNb && cellList.get(nb).getExpectedValue() > 0) 
					valuesInDiagonal.add(cellList.get(nb).getExpectedValue());
			}
		}
		return valuesInDiagonal;
	}
	
}
