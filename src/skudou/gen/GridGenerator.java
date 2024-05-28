package skudou.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum GridGenerator {

	INSTANCE;
	
	private GridGenerator() {}
	
	/**
	 * Génère une grille en donnant une valeur attendue à chaque cellule
	 * @param seed
	 * @param ruleset : liste des GridRule à respecter pour la grille
	 * @return la grille avec les valeurs attendues
	 * @throws Exception
	 */
	public List<Cell> generateGrid(Random rand, Set<GridRule> ruleset) throws Exception {
		// Get a random order in which the Cells are filled
		List<Cell> cellList = new ArrayList<>();
		int gridLength = GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE;
		List<Integer> possibleValues = IntStream.range(1, gridLength + 1).boxed().collect(Collectors.toList());
		for(int i = 0; i < (gridLength * gridLength); i++) cellList.add(new Cell(i));
		
		// Fill grid according to the ruleset
		fillGrid(cellList, possibleValues, 0, ruleset, rand);
		
		// Dig grid
		List<Integer> digOrder = IntStream.range(0, cellList.size()).boxed().collect(Collectors.toList());
		Collections.shuffle(digOrder, rand);
		digGrid(cellList, digOrder, possibleValues, ruleset, rand);
		for(Cell cell : cellList) {
			if(cell.getExpectedValue() == 0) {
				cellList.get(cell.cellNb).setExpectedValue(cell.getCurrentValue());
				cellList.get(cell.cellNb).setCurrentValue(0);
			}
		}
		
		return cellList;
	}
	
	/**
	 * Affiche les valeurs attendues pour la grille donnée en paramètre
	 * @param cellList
	 */
	public void printExpectedGrid(List<Cell> cellList) {
		String gridStr = "";
		int gridLength = GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE;
		for(int cellNb = 0; cellNb < cellList.size(); cellNb++) {
			if (cellNb % (GridRule.SQUARE_SIZE * gridLength) == 0 && cellNb != 0) gridStr += "\n-------------------";
			if (cellNb != 0 && cellNb % gridLength == 0) gridStr += "\n";
			if (cellNb % GridRule.SQUARE_SIZE == 0) gridStr += "|";
			gridStr += Integer.toString(cellList.get(cellNb).getExpectedValue()) 
					+ (cellNb % GridRule.SQUARE_SIZE != GridRule.SQUARE_SIZE - 1 ? " " : "")
					+ (cellNb % gridLength == gridLength - 1 ? "|" : "");
		}
		System.out.println(gridStr);
	}
	
	/**
	 * Affiche les valeurs courantes pour la grille donnée en paramètre
	 * @param cellList
	 */
	public void printCurrentGrid(List<Cell> cellList) {
		String gridStr = "";
		int gridLength = GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE;
		for(int cellNb = 0; cellNb < cellList.size(); cellNb++) {
			if (cellNb % (GridRule.SQUARE_SIZE * gridLength) == 0 && cellNb != 0) gridStr += "\n-------------------";
			if (cellNb != 0 && cellNb % gridLength == 0) gridStr += "\n";
			if (cellNb % GridRule.SQUARE_SIZE == 0) gridStr += "|";
			gridStr += Integer.toString(cellList.get(cellNb).getCurrentValue()) 
					+ (cellNb % GridRule.SQUARE_SIZE != GridRule.SQUARE_SIZE - 1 ? " " : "")
					+ (cellNb % gridLength == gridLength - 1 ? "|" : "");
		}
		int nbClues = 0;
		for(Cell cell : cellList) {
			if (cell.getCurrentValue() != 0) nbClues++;
		}
		gridStr += "\nNB CLUES: " + nbClues;
		System.out.println(gridStr);
	}
	
	/**
	 * Méthode récursive de remplissage des valeurs attendues dans la grille
	 * @param cellList
	 * @param possibleValues
	 * @param cellNb
	 * @param ruleset
	 * @param rand
	 * @return
	 */
	private boolean fillGrid(List<Cell> cellList, List<Integer> possibleValues, int cellNb, Set<GridRule> ruleset, Random rand) {
		if(cellNb == cellList.size()) return true;
		Cell cell = cellList.get(cellNb);
		List<Integer> possibleValuesForCell = new ArrayList<>();
		if (cell.getExpectedValue() == 0) {
			for (int value : possibleValues) possibleValuesForCell.add(value);
			Collections.shuffle(possibleValuesForCell, rand);
			for(int value : possibleValuesForCell) {
				boolean isOk = true;
				cell.setExpectedValue(value);
				cell.setCurrentValue(value);
				CellContext context = new CellContext(cell, cellList);
				for (GridRule rule : ruleset) {
					if(!rule.matches(context)) isOk = false;
				}
				if (isOk && fillGrid(cellList, possibleValues, cellNb + 1, ruleset, rand)) return true;
				cell.setExpectedValue(0);
				cell.setCurrentValue(0);
			}
		}
		return false;
	}
	
	private List<Cell> cacheGrid = new ArrayList<>();
	private int recursionNum = 0;
	private int maxRecursions = 100000;
	
	private int solveGrid(List<Cell> cellList, List<Integer> possibleValues, int count, Set<GridRule> ruleset) {
		if (recursionNum > maxRecursions) return 2;
		recursionNum++;
		for(Cell cell : cellList) {
			if (cell.getExpectedValue() == 0) {
				for (int value : possibleValues) {
					if (count >= 2) break;
					boolean isOk = true;
					cell.setExpectedValue(value);
					CellContext context = new CellContext(cell, cellList);
					for (GridRule rule : ruleset) {
						if(!rule.matches(context)) {
							isOk = false;
							break;
						}
					}
					if (isOk) {
						int cache = solveGrid(cellList, possibleValues, count, ruleset);
						if (cache > count) {
							count = cache;
							if (cacheGrid.isEmpty()) {
								for (int i = 0; i < cellList.size(); i++) {
									cacheGrid.add(new Cell(i));
								}
							}
							for (int i = 0; i < cellList.size(); i++) {
								int currentValue = cellList.get(i).getExpectedValue();
								if (currentValue != 0) {
									cacheGrid.get(i).setExpectedValue(currentValue);
								}
							}
						}
					}
					cell.setExpectedValue(0);
				}
				return count;
			}
		}
		return count + 1;
	}
	
	private List<Cell> cacheDigGrid;
	
	private boolean digGrid(List<Cell> cellList, List<Integer> order, List<Integer> possibleValues, Set<GridRule> ruleset, Random rand) {
		cacheDigGrid = new ArrayList<>();
		for(Cell cell : cellList) cacheDigGrid.add(cell);
		for (int i = 0; i < cellList.size(); i++) {
			Cell cell = cellList.get(order.get(i));
			int oldVal = cell.getExpectedValue();
			cell.setExpectedValue(0);
			cacheDigGrid.get(cell.cellNb).setExpectedValue(0);
			recursionNum = 0;
			int sols = solveGrid(cacheDigGrid, possibleValues, 0, ruleset);
			if (sols != 1) {
				cell.setExpectedValue(oldVal);
				cacheDigGrid.get(cell.cellNb).setExpectedValue(oldVal);
			}
		}
		return false;
	}
}
