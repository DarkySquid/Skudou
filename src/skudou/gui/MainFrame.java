package skudou.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 5803967378134023834L;

	private GridPanel panel;
	
	private JButton generateButton, submitButton;
	
	public MainFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Skudou");
		this.setSize(600, 600);
		this.setLocationRelativeTo(null);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		panel = new GridPanel();
		mainPanel.add(panel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		generateButton = new JButton("Generate");
		submitButton = new JButton("Submit");
		submitButton.setEnabled(false);
		
		buttonPanel.add(generateButton);
		buttonPanel.add(submitButton);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		this.setContentPane(mainPanel);
	}
	
	public void setGenerateAction(ActionListener listener) {
		generateButton.addActionListener(listener);
	}
	
	public void setSubmitAction(ActionListener listener) {
		submitButton.addActionListener(listener);
	}

	public GridPanel getPanel() {
		return panel;
	}
	
	public void setGridLoaded(boolean loaded) {
		submitButton.setEnabled(loaded);
	}
	
}
