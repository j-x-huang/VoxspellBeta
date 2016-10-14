package beta;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;


import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame{
	private JPanel contentPane;
	private int _level;
	private File _mainFile;
	private WordList _wl;
	private Sound sound;

	public Main() {
		//Set up frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 630);
		setResizable(false);
		contentPane = new JPanel();

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		//get current wordlist file and make a wordlist 
		try {
			File file = new File(".defaultList.txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String str;
			str = br.readLine();
			_mainFile = new File(str);
			_wl = new WordList(_mainFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		_level = levelSelect();
		
		Sound sound = new Sound("bensound-acousticbreeze.wav"); //play background music
		sound.loop();
		Menu menu = new Menu(_level, this, _wl, 10, _mainFile, sound);
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


	//A level select dialog which pops up at start of application.
	private int levelSelect() {
		int[] levels = _wl.getLevels(); //Get the level numbers
		String[] levelStrings=Arrays.toString(levels).split("[\\[\\]]")[1].split(", "); 

		String num = (String) JOptionPane.showInputDialog(this, "Please select a level", "Level Select", 
				JOptionPane.PLAIN_MESSAGE, null, levelStrings, levelStrings[0]);


		if(num==null){
			return 1; //if user cancels option just return to level 1
		}else{
			return Integer.parseInt(num);
		} 
	}
	
	protected Sound getSound() {
		return sound;
	}


}
