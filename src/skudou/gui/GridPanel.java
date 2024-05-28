package skudou.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import skudou.gen.Cell;
import skudou.gen.GridRule;

public class GridPanel extends JPanel {

	private static final long serialVersionUID = 897706350437360314L;
	
	private SquarePanel[][] squares = new SquarePanel[GridRule.SQUARE_SIZE][GridRule.SQUARE_SIZE];
	
	private boolean failed = false, success = false;
	
	public GridPanel() {
		this.setLayout(new GridLayout(GridRule.SQUARE_SIZE, GridRule.SQUARE_SIZE));
		for(int i = 0; i < GridRule.SQUARE_SIZE; i++) {
			for (int j = 0; j < GridRule.SQUARE_SIZE; j++) {
				SquarePanel panel = new SquarePanel();
				panel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
				squares[i][j] = panel;
				this.add(panel);
			}
		}
	}
	
	public boolean isFailed() {
		return failed;
	}

	public boolean isSuccess() {
		return success;
	}
	
	public void setFailed(boolean failed) {
		for (int i = 0; i < GridRule.SQUARE_SIZE; i++) {
			for (int j = 0; j < GridRule.SQUARE_SIZE; j++) {
				for (int k = 0; k < GridRule.SQUARE_SIZE; k++) {
					for (int l = 0; l < GridRule.SQUARE_SIZE; l++) {
						if (failed) squares[j][i].getCells()[l][k].setBackground(Color.red);
						else squares[j][i].getCells()[l][k].setBackground(Color.WHITE);
					}
				}
			}
		}
		this.failed = failed;
	}
	
	public void setSuccess(boolean success) {
		for (int i = 0; i < GridRule.SQUARE_SIZE; i++) {
			for (int j = 0; j < GridRule.SQUARE_SIZE; j++) {
				for (int k = 0; k < GridRule.SQUARE_SIZE; k++) {
					for (int l = 0; l < GridRule.SQUARE_SIZE; l++) {
						if (success) {
							squares[j][i].getCells()[l][k].setBackground(Color.GREEN);
							squares[j][i].getCells()[l][k].setEditable(false);
						} else {
							squares[j][i].getCells()[l][k].setBackground(Color.WHITE);
							squares[j][i].getCells()[l][k].setEditable(true);
						}
					}
				}
			}
		}
		this.success = success;
	}
	
	public List<Cell> getGrid() {
		List<Cell> grid = new ArrayList<>();
		int cellNb = 0;
		// Square Y
		for(int i = 0; i < GridRule.SQUARE_SIZE; i++) {
			// Cell Y
			for(int j = 0; j < GridRule.SQUARE_SIZE; j++) {
				// Square X
				for(int k = 0; k < GridRule.SQUARE_SIZE; k++) {
					// Cell X
					for(int l = 0; l < GridRule.SQUARE_SIZE; l++) {
						Cell cell = new Cell(cellNb++);
						try {
							cell.setCurrentValue(Integer.parseInt(squares[i][k].getCells()[j][l].getText()));
						} catch (NumberFormatException e) {
							cell.setCurrentValue(0);
						}
						grid.add(cell);
					}
				}
			}
		}
		return grid;
	}
	
	public SquarePanel[][] getSquares() {
		return squares;
	}

	public class SquarePanel extends JPanel {

		private static final long serialVersionUID = 3338573911106715483L;
		
		private CellTextField[][] cells = new CellTextField[GridRule.SQUARE_SIZE][GridRule.SQUARE_SIZE];
		
		public SquarePanel() {
			this.setLayout(new GridLayout(GridRule.SQUARE_SIZE, GridRule.SQUARE_SIZE));
			for(int i = 0; i < GridRule.SQUARE_SIZE; i++) {
				for (int j = 0; j < GridRule.SQUARE_SIZE; j++) {
					CellTextField textField = new CellTextField();
					textField.setHorizontalAlignment(JTextField.CENTER);
					textField.setFont(new Font("", Font.BOLD, 25));
					textField.setOptsFont(new Font("", Font.PLAIN, 10));
					textField.setBackground(Color.LIGHT_GRAY);
					textField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					textField.setEditable(false);
					cells[i][j] = textField;
					this.add(textField);
					PlainDocument doc = (PlainDocument) textField.getDocument();
					doc.setDocumentFilter(new CellFilter());
				}
			}
		}

		public CellTextField[][] getCells() {
			return cells;
		}
		
	}
	
	private class CellFilter extends DocumentFilter {
		
		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.insert(offset, string);
			
			if (test(sb.toString())) {
				if (failed) setFailed(false);
				super.insertString(fb, offset, string, attr);
			}
		}
		
		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attr) throws BadLocationException {
			Document doc = fb.getDocument();
		    StringBuilder sb = new StringBuilder();
		    sb.append(doc.getText(0, doc.getLength()));
		    sb.replace(offset, offset + length, text);
		    
		    if (test(sb.toString())) {
		    	if (failed) setFailed(false);
		    	super.replace(fb, offset, length, text, attr);
		    }
		}
		
		private boolean test(String string) {
			if(string.length() == 0) return true;
			try {
				int num = Integer.parseInt(string);
				if (num > 9 || num < 1) return false;
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

}
