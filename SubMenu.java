package beta;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

public class SubMenu extends JPanel {

	private JPanel _menu;
	private JFrame _main;
	private int _level;

	private JButton btnReturnToMenu = new JButton("Return to Menu");
	private JButton btnRepeatQuiz = new JButton("Repeat Quiz");
	private JButton btnViewStatistics = new JButton("View Statistics");
	private JButton btnWatchVideo = new JButton("Watch Video");
	private JButton btnAdvance = new JButton("Advance ");
	private int _maxNum;
	private WordList _wordList;
	private File _file;
	private Sound _sound;
	private boolean _advance = false;
	private JButton btnCoins = new JButton("Buy Level(1000)");


	/**
	 * Create the panel.
	 */
	public SubMenu(WordList wordlist, int level, int correct, int testNum, JFrame main, int maxNum, File file, Sound sound) {

		//Setting the size of the main menu and choosing the layout of it.
		_wordList = wordlist;
		_main = main;
		_level = level;
		_maxNum = maxNum;
		_file = file;
		_sound = sound;

		//Set up the GUI
		this.setBackground(new Color(255, 255, 102));
		this.setLayout(null);

		JLabel lblQuizFinished = new JLabel("Quiz Finished");
		lblQuizFinished.setFont(new Font("Calibri Light", Font.PLAIN, 30));
		lblQuizFinished.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuizFinished.setBounds(10, 55, 850, 45);
		this.add(lblQuizFinished);


		JLabel lblNewLabel = new JLabel("You scored: " + correct + "/" + testNum); //display results of the quiz
		lblNewLabel.setFont(new Font("Calibri Light", Font.PLAIN, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 100, 850, 28);
		this.add(lblNewLabel);

		double percentage = ((double) correct * 100 )/ (double) testNum;

		//Label changes depending on users score. If user gets at least 90% they get a different message and the 
		// ability to progress to the next next and/or watch the video reward
		JLabel lblWellDone = new JLabel();
		if (percentage >= 90) {
			lblWellDone.setText("Well Done! You can progress to the next level.");
			_advance = true;
		} else {
			lblWellDone.setText("Good effort! Please retry quiz in order to progress");
		}

		lblWellDone.setHorizontalAlignment(SwingConstants.CENTER);
		lblWellDone.setFont(new Font("Calibri Light", Font.PLAIN, 16));
		lblWellDone.setBounds(20, 121, 850, 28);
		this.add(lblWellDone);

		//This button lets users go back to the menu for the same level as before
		btnReturnToMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Menu menu = new Menu(_level, _main, _wordList, _maxNum, _file, _sound);
				_main.getContentPane().add(menu);
				menu.setVisible(true);
			}
		});
		setButtonDesign(252, btnReturnToMenu);
		this.add(btnReturnToMenu);
		//this button lets users repeat quiz
		btnRepeatQuiz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Quiz q = null;
				try {
					q = new Quiz(_wordList, _level, _main, _maxNum, _file, _sound);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				_main.getContentPane().add(q);
				setVisible(false);
				q.setVisible(true);
			}

		});
		setButtonDesign(343, btnRepeatQuiz);
		this.add(btnRepeatQuiz);
		
		//Makes statistics panel visible
		btnViewStatistics.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				makeTable();

			}

		});
		setButtonDesign(432, btnViewStatistics);
		this.add(btnViewStatistics);
		
		//This button only appears if users scored at least 90%
		//makes the video player
		btnWatchVideo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_sound.stop();
				MediaPlayer mp = new MediaPlayer();

			}

		});
		setButtonDesign(522, btnWatchVideo);
		
		//This button only appears if users scored at least 90%
		//allows users to do quizzes at the next level (if possible)
		btnAdvance.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				Quiz q = null;
				try {
					int nextLevel = _wordList.getNextLevel(_level);
					q = new Quiz(_wordList, nextLevel, _main, _maxNum, _file, _sound);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				_main.getContentPane().add(q);
				setVisible(false);
				q.setVisible(true);
				
			}

		});
		setButtonDesign(162, btnAdvance);

		//This button allows users to purchase using the coins the next level
		// so they can start doing quizzes at a higher level. Only appears if
		// user scored <90%
		btnCoins.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int coin = getCoins();
				if ((coin - 1000) > 0) {
					updateCoins(coin - 1000);
					
					int nextLevel = _wordList.getNextLevel(_level);
					setVisible(false);
					Menu menu = new Menu(nextLevel, _main, _wordList, _maxNum, _file, _sound );
					_main.getContentPane().add(menu);
					menu.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "Insufficient funds!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});
		setButtonDesign(162 ,btnCoins);

		if (_advance) {
			this.add(btnWatchVideo);
			this.add(btnAdvance);
		} else {
			this.add(btnCoins);
		}
		
		_wordList.saveData();

	}
	
	private void setButtonDesign(int height, JButton btn) {
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setFont(new Font("Arial Black", Font.PLAIN, 20));
		btn.setBorder(new MatteBorder(3,3,3,3, new Color(0,0,0)));
		btn.setBounds(334, height, 200, 69);
		btn.setBackground(new Color(255,153, 51));
		btn.setForeground(new Color(255,255, 153));

		
	}
	//makes table panel visible
	private void makeTable() {
		ViewAccuracy va = new ViewAccuracy(_wordList, this);

		_main.getContentPane().add(va);
		this.setVisible(false);
		va.setVisible(true);
	}
	//get the number of coins user has 
	private int getCoins(){

		int coin = 0;
		File file = new File(".coinSave");
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String str;
			str = br.readLine();
			coin =  Integer.parseInt(str);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return coin;
	}
	//updates the number of coins users have and saves it on a file
	// (only if user decides to purchase a level)
	private void updateCoins(int coin) {
		File file = new File(".coinSave");

		PrintWriter pw;
		try {
			pw = new PrintWriter(file);
			pw.close();

			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(coin + "\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
