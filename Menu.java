package beta;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
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
	private File _file;
	private JFrame _frame;
	private int _wordNum;

	/**
	 * Create the frame.
	 */
	public Menu(int level, JFrame frame, File file, int wordNum) {
		_frame = frame;
		_level = level;
		_file = file;
		_wordNum = wordNum;
		


		//Menu Panel Stuff
		this.setBackground(new Color(255, 255, 153));
		this.setLayout(null);

		JLabel titleLbl = new JLabel("Voxspell");
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		titleLbl.setBounds(0, 0, 474, 57);
		this.add(titleLbl);

		JLabel subTitleLbl = new JLabel("Spelling Aid. Level: " + _level);
		subTitleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		subTitleLbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		subTitleLbl.setBounds(0, 55, 474, 28);
		this.add(subTitleLbl);

		JButton quizBtn = new JButton("New Quiz");
		quizBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//If no wordlist is found show error message to user
				File f = _file;
				if(!f.exists()){
					JOptionPane.showMessageDialog(null, "No wordlist file is found!!\n(Please place wordlist file in the working directory)");
					//If there is no word inside the lsit
				}else{ 
					WordList word = null;
					try {
						word = new WordList(_file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(word.getWordCount(_level)<1){
						JOptionPane.showMessageDialog(null, "No word to be tested!!");
					}else{
						//else start the quiz
						Quiz q;
						try {
							q = new Quiz(_file, _level, _frame, _wordNum);

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
		quizBtn.setFont(new Font("Calibri Light", Font.PLAIN, 20));
		quizBtn.setBounds(151, 121, 170, 50);
		this.add(quizBtn);

		JButton statsBtn = new JButton("View Statistics");
		statsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				makeTable();
				System.out.println("clicked");
			}
		});
		statsBtn.setFont(new Font("Calibri Light", Font.PLAIN, 20));
		statsBtn.setBounds(151, 195, 170, 50);
		this.add(statsBtn);

		JButton clearBtn = new JButton("Clear Statistics");
		clearBtn.setFont(new Font("Calibri Light", Font.PLAIN, 20));
		clearBtn.setBounds(151, 270, 170, 50);
		this.add(clearBtn);

		lblLevel.setHorizontalAlignment(SwingConstants.CENTER);
		lblLevel.setBounds(0, 94, 464, 14);
		this.add(lblLevel);

		JButton btnSettings = new JButton("Settings");
		btnSettings.setFont(new Font("Calibri Light", Font.PLAIN, 20));
		btnSettings.setBounds(151, 348, 170, 50);
		btnSettings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainSettings ms = new MainSettings(_frame, _file);
				_frame.getContentPane().add(ms);
				setVisible(false);
				ms.setVisible(true);
			}
			
		});
		this.add(btnSettings);


	}



	public void setTitle(){
		//label.setText("Welcome to the Spelling Aid Level "+_level+"!!");
		lblLevel.setText("Level "+_level);
	}

	protected void makeTable() {
		ViewAccuracy va = new ViewAccuracy();
		JTable table = new JTable(va);
		final JPanel statsPanel = new JPanel();
		//Add a close button to close the frame
		JButton returnBtn = new JButton("Close Stats");
		statsPanel.setLayout(new BorderLayout());
		statsPanel.add(new JScrollPane(table), BorderLayout.CENTER);
		returnBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				statsPanel.setVisible(false);
				setVisible(true);

			}

		});
		statsPanel.add(returnBtn, BorderLayout.SOUTH);

		_frame.getContentPane().add(statsPanel);
		this.setVisible(false);
		statsPanel.setVisible(true);

	}

	//This method clears the stats by overwriting existing files that
	//stores information.
	private void clearStats() throws IOException{

		for (int i = 1; i <= 11; i++) {
			File accuracy = new File(".accuracy_" + i);
			accuracy.delete();
		}
		createAccuracy();
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

	public void nextLevel(){
		_level++;
	}
}
