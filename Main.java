package beta;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 850, 630);
		setResizable(false);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				int clear = JOptionPane.showConfirmDialog (null, "Are you sure you want to exit Voxspell?\nLeaving "
						+ "during a quiz may cause some statistics to not save.","Warning",JOptionPane.YES_NO_OPTION);
				if(clear == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}
		});
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
			_wl = new WordList(_mainFile, _mainFile.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		_level = levelSelect();

		if (_level == 0 ) {
			setVisible(false);
			dispose();
			System.exit(0);
		} else {

			Sound sound = new Sound("bensound-acousticbreeze.wav"); //play background music
			sound.loop();
			Menu menu = new Menu(_level, this, _wl, 10, _mainFile, sound);
			contentPane.add(menu);
			menu.setVisible(true);
		}
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
			return 0; 
		}else{
			return Integer.parseInt(num);
		} 
	}

	protected Sound getSound() {
		return sound;
	}


}
