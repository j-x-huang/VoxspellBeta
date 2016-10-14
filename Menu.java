package beta;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;


public class Menu extends JPanel {


	JLabel lblLevel = new JLabel();

	private int _level;
	private JFrame _frame;
	private int _wordNum;
	private File _file;

	private WordList _wordlist;

	private Sound _sound;

	/**
	 * This is a menu panel of the quiz
	 */
	public Menu(int level, JFrame frame, WordList wordlist, int wordNum, File file, Sound sound) {
		_frame = frame;
		_level = level;
		_wordlist = wordlist;
		_wordNum = wordNum;
		_file = file;
		_sound = sound;

		createAccuracy();

		//Set up menu buttons and labels
		this.setBackground(new Color(255, 255, 153));
		this.setLayout(null);

		JLabel titleLbl = new JLabel("Voxspell");
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		titleLbl.setBounds(0,11, 850, 57);
		this.add(titleLbl);

		JLabel subTitleLbl = new JLabel("Spelling Aid. Level: " + _level);
		subTitleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		subTitleLbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		subTitleLbl.setBounds(0, 65, 850, 28);
		this.add(subTitleLbl);
		
		JLabel lblSBee = new JLabel("");
		lblSBee.setIcon(new ImageIcon("sbee2.png"));
		lblSBee.setBounds(650, 10, 81, 76);
		this.add(lblSBee);
		//Add functionality to quiz button
		JButton quizBtn = new JButton("New Quiz");
		quizBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//If no wordlist is found show error message to user
				File f = _file;
				if(!f.exists()){
					JOptionPane.showMessageDialog(null, "No wordlist file is found!!\n(Please place wordlist file in the working directory)");
					//If there is no word inside the lsit
				}else{ 
					
					if(_wordlist.getWordCount(_level)<1){
						JOptionPane.showMessageDialog(null, "No word to be tested!!");
					}else{
						//else start the quiz
						Quiz q;
						try {
							q = new Quiz(_wordlist, _level, _frame, _wordNum, _file, _sound);

							_frame.getContentPane().add(q);
							setVisible(false);
							q.setVisible(true);

						}  catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

			}
		});
		quizBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		quizBtn.setFont(new Font("Arial Black", Font.PLAIN, 20));
		quizBtn.setBorder(new MatteBorder(3,3,3,3, new Color(0,0,0)));
		quizBtn.setBounds(313, 147, 220, 69);
		quizBtn.setBackground(new Color(255,153, 51));
		quizBtn.setForeground(new Color(255,255, 153));
		this.add(quizBtn);

		JButton statsBtn = new JButton("View Statistics");
		statsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				makeTable();
			}
		});
		statsBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		statsBtn.setFont(new Font("Arial Black", Font.PLAIN, 20));
		statsBtn.setBorder(new MatteBorder(3,3,3,3, new Color(0,0,0)));
		statsBtn.setBounds(313, 247, 220, 69);
		statsBtn.setBackground(new Color(255,153, 51));
		statsBtn.setForeground(new Color(255,255, 153));
		
		this.add(statsBtn);
		//Functionality to clear statistics button
		JButton clearBtn = new JButton("Clear Statistics");
		clearBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
	
				int clear = JOptionPane.showConfirmDialog (null, "Would You Like to Clear the Statistics?","Warning",JOptionPane.YES_NO_OPTION);
				if(clear == JOptionPane.YES_OPTION){
					try {
						clearStats();
						createAccuracy();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		clearBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		clearBtn.setFont(new Font("Arial Black", Font.PLAIN, 20));
		clearBtn.setBorder(new MatteBorder(3,3,3,3, new Color(0,0,0)));
		clearBtn.setBounds(313, 347, 220, 69);
		clearBtn.setBackground(new Color(255,153, 51));
		clearBtn.setForeground(new Color(255,255, 153));
		this.add(clearBtn);

		lblLevel.setHorizontalAlignment(SwingConstants.CENTER);
		lblLevel.setBounds(0, 94, 464, 14);
		this.add(lblLevel);
		
		//statistics for settings button
		JButton settingsBtn = new JButton("Settings");
		settingsBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainSettings ms = new MainSettings(_frame, _file, _wordNum, _level, Menu.this, _sound);
				_frame.getContentPane().add(ms);
				setVisible(false);
				ms.setVisible(true);
			}

		});
		settingsBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		settingsBtn.setFont(new Font("Arial Black", Font.PLAIN, 20));
		settingsBtn.setBorder(new MatteBorder(3,3,3,3, new Color(0,0,0)));
		settingsBtn.setBounds(313, 447, 220, 69);
		settingsBtn.setBackground(new Color(255,153, 51));
		settingsBtn.setForeground(new Color(255,255, 153));
		this.add(settingsBtn);


	}



	public void setTitle(){
		lblLevel.setText("Level "+_level);
	}
	//makes table (on a separate panel)
	protected void makeTable() {
		ViewAccuracy va = new ViewAccuracy(_wordlist, this);

		_frame.getContentPane().add(va);
		this.setVisible(false);
		va.setVisible(true);

	}

	//This method clears the stats by overwriting existing files that
	//stores information.
	protected void clearStats() throws IOException{
		
		WordList wl = null;
		try {
			wl = new WordList(_file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int[] levels = wl.getLevels();
		int size = levels.length;

		for (int i = 0; i < size; i++) {
			File accuracy = new File(".accuracy_" + levels[i]);
			accuracy.delete();
		}
	}

	//Creates save files to store the accuracy, then add zeros to the file. There is a save 
	//file for each level. This function also creates the save files that keeps track of coins
	private void createAccuracy() {
		WordList wl = null;
		try {
			wl = new WordList(_file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int[] levels = wl.getLevels();
		int size = levels.length;
		try {
			for (int i = 0; i < size; i++) {
				File accuracy = new File(".accuracy_" + levels[i]);
				if (! accuracy.exists()) {
					accuracy.createNewFile();

					FileWriter fw = new FileWriter(accuracy);
					BufferedWriter bw = new BufferedWriter(fw);

					bw.write("0" + "\n");
					bw.write("0" + "\n");
					bw.write("0" + "\n");

					bw.close();
				}
			} 
			File coin = new File(".coinSave"); //make a save file to save coin value
			if (! coin.exists()) {
				coin.createNewFile();
				
				FileWriter fw = new FileWriter(coin);
				BufferedWriter bw = new BufferedWriter(fw);
				
				bw.write("0" + "\n");
				bw.close();
			}

		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}
