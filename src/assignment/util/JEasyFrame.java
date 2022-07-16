package assignment.util;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;

public class JEasyFrame extends JFrame{
	public Component comp;
	public JEasyFrame(Component comp, String title) {
		super(title);
		this.comp = comp;
		add(BorderLayout.CENTER, comp);
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
	}
}
