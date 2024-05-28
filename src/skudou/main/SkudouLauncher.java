package skudou.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.BevelBorder;

import skudou.gen.Cell;
import skudou.gen.GridGenerator;
import skudou.gen.GridRule;
import skudou.gui.CellTextField;
import skudou.gui.GridPanel.SquarePanel;
import skudou.gui.MainFrame;

public class SkudouLauncher {
	
	public static SkudouLauncher _instance;
	
	private static final int FRAME_WIDTH = 600, FRAME_HEIGHT = 600;
	
	private MainFrame frame;
	private Set<GridRule> ruleset;
	private List<Cell> grid = null;
	
	private SkudouLauncher() {
		// Initialisation des règles
		ruleset = new HashSet<>();
		ruleset.add(GridRule.SQUARE_IS_SET);
		ruleset.add(GridRule.LINE_IS_SET);
		ruleset.add(GridRule.COLUMN_IS_SET);
		//ruleset.add(GridRule.DIAGONAL_IS_SET);
	}
	
	private void loadMainFrame() {
		// Lancement de l'IHM
		frame = new MainFrame();
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setGenerateAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!frame.getPanel().isSuccess()) {
					String confirmStr = "Generate a new grid and stop solving the current one?";
					if (JOptionPane.showConfirmDialog(frame.getPanel(), confirmStr) != JOptionPane.YES_OPTION) {
						return;
					}
				}
				generateNewGrid();
			}
		});
		frame.setSubmitAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkGrid(frame.getPanel().getGrid());
			}
		});
		frame.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				for (int i = 0; i < GridRule.SQUARE_SIZE; i++) {
					for (int j = 0; j < GridRule.SQUARE_SIZE; j++) {
						for(int k = 0; k < GridRule.SQUARE_SIZE; k++) {
							for (int l = 0; l < GridRule.SQUARE_SIZE; l++) {
								CellTextField cellField = frame.getPanel().getSquares()[j][i].getCells()[l][k];
								cellField.setFont(
									new Font("", Font.BOLD, (int)((float)frame.getHeight()/24.0))
								);
								cellField.setOptsFont(
									new Font("", Font.BOLD, (int)((float)frame.getHeight()/50.0))
								);
							}
						}
					}
				}
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
		frame.setVisible(true);
		generateNewGrid();
	}
	
	public void generateNewGrid() {
		frame.setGridLoaded(false);
		// Génération de la seed
		long seed = new Random().nextLong();
		System.out.println("SEED: " + seed);
		Random rand = new Random(seed);
		long timeBeforeGen = System.currentTimeMillis();
		try {
			grid = GridGenerator.INSTANCE.generateGrid(rand, ruleset);
			System.out.println("GRILLE COURANTE :");
			GridGenerator.INSTANCE.printCurrentGrid(grid);
			System.out.println("\nGRILLE ATTENDUE :");
			GridGenerator.INSTANCE.printExpectedGrid(grid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Gen Time: " + (System.currentTimeMillis() - timeBeforeGen) + "ms");
		
		// Affichage de la grille
		for (int i = 0; i < GridRule.SQUARE_SIZE; i++) {
			for (int j = 0; j < GridRule.SQUARE_SIZE; j++) {
				SquarePanel square = frame.getPanel().getSquares()[j][i];
				for(int k = 0; k < GridRule.SQUARE_SIZE; k++) {
					for (int l = 0; l < GridRule.SQUARE_SIZE; l++) {
						Cell cell = grid.get(j * (GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE)
								+ i * GridRule.SQUARE_SIZE + l * (GridRule.SQUARE_SIZE * GridRule.SQUARE_SIZE) + k);
						CellTextField cellField = square.getCells()[l][k];
						if (cell.getCurrentValue() != 0) {
							cellField.setForeground(Color.GRAY);
							cellField.setText(Integer.toString(cell.getCurrentValue()));
							cellField.setEditable(false);
						} else {
							cellField.setForeground(Color.BLACK);
							cellField.setText("");
							cellField.setEditable(true);
						}
						cellField.setFont(new Font("", Font.BOLD, (int)((float)frame.getHeight()/24.0)));
						cellField.setOptsFont(new Font("", Font.BOLD, (int)((float)frame.getHeight()/50.0)));
						cellField.setOptsText("");
						cellField.setBackground(Color.WHITE);
						cellField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					}
				}
			}
		}
		frame.setGridLoaded(true);
	}
	
	public boolean checkGrid(List<Cell> currentGrid) {
		for(int i = 0; i < grid.size(); i++) {
			if (grid.get(i).getExpectedValue() != currentGrid.get(i).getCurrentValue()) {
				frame.getPanel().setFailed(true);
				return false;
			}
		}
		frame.getPanel().setSuccess(true);
		frame.setGridLoaded(false);
		return true;
	}
	
	public static void main(String[] args) {
		if(_instance == null) _instance = new SkudouLauncher();
		_instance.loadMainFrame();
	}

	public MainFrame getFrame() {
		return frame;
	}

}
