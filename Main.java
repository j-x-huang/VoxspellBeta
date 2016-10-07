package beta;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame{
	private JPanel contentPane;
	private int _level;

	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		levelSelect();
		
		File file = new File("NZCER-spelling-lists.txt");
		
		Menu menu = new Menu(_level, this, file, 10);
		contentPane.add(menu);
		menu.setVisible(true);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	
	private void levelSelect() {
		String[] levelStrings = { "1", "2", "3", "4", "5", "6", "7", "8", 
				"9", "10", "11" };
		final JComboBox<String> combo = new JComboBox<>(levelStrings);

		
		String num = (String) JOptionPane.showInputDialog(this, "Please select a level", "Level Select", 
				JOptionPane.PLAIN_MESSAGE, null, levelStrings, levelStrings[0]);
		
		
		if(num==null){
			this.dispose();
		}else{
		_level = Integer.parseInt(num);
		this.setVisible(true);
		} 
	}
}
