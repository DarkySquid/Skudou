package skudou.gen;

public class Cell {
	
	public int x, y, cellNb, squareNb;
	private int currentValue = 0, expectedValue = 0;
	
	public Cell(int cellNb) {
		this.cellNb = cellNb;
		this.x = cellNb % (GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE);
		this.y = (int) Math.floor((float)cellNb / (float)(GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE));
		this.squareNb = (int)(GridRule.SQUARE_SIZE * Math.floor((float)this.y / (float)GridRule.SQUARE_SIZE) 
				+ Math.floor((float)this.x / (float)GridRule.SQUARE_SIZE));
	}
	
	public int getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	public int getExpectedValue() {
		return expectedValue;
	}

	public void setExpectedValue(int expectedValue) {
		this.expectedValue = expectedValue;
	}
	
}
