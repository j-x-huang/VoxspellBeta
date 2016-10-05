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
		
		createAccuracy();
		levelSelect();
		
		File file = new File("/afs/ec.auckland.ac.nz/users/x/h/xhua451/unixhome/workspace/VoxspellBeta/NZCER-spelling-lists.txt");
		
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
	
	//Creates save files to store the accuracy, then add zeros to the file. There is a save 
	//file for each level
	private void createAccuracy() {

		for (int i = 1; i <= 11; i++) {
			try {
				File accuracy = new File(".accuracy_" + i);
				if (! accuracy.exists()) {
					accuracy.createNewFile();

					FileWriter fw = new FileWriter(accuracy);
					BufferedWriter bw = new BufferedWriter(fw);

					bw.write("0" + "\n");
					bw.write("0" + "\n");

					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	private void levelSelect() {
		String[] levelStrings = { "1", "2", "3", "4", "5", "6", "7", "8", 
				"9", "10", "11" };
		final JComboBox<String> combo = new JComboBox<>(levelStrings);
		String[] options = { "OK" };


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
