package skudou.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CellTextField extends JTextField {

	private static final long serialVersionUID = -1516968746725134661L;

	private JLabel optsLabel;
	
	public CellTextField() {
		super();
		optsLabel = new JLabel();
		optsLabel.setAlignmentX(JTextField.RIGHT_ALIGNMENT);
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					String newOpts = JOptionPane.showInputDialog("Add notes to current cell :", optsLabel.getText());
					if (newOpts == null) return;
					if (newOpts.length() > 9) newOpts = newOpts.substring(0, 9);
					optsLabel.setText(newOpts);
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		this.setLayout(new BorderLayout());
		this.add(optsLabel, BorderLayout.NORTH);
	}
	
	public void setOptsFont(Font font) {
		optsLabel.setFont(font);
	}
	
	public void setOptsText(String text) {
		optsLabel.setText(text);
	}
	
	public String getOpts() {
		return optsLabel.getText();
	}
	
}
