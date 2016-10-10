package beta;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

		this.setBackground(new Color(255, 255, 102));
		this.setLayout(null);

		JLabel lblQuizFinished = new JLabel("Quiz Finished");
		lblQuizFinished.setFont(new Font("Calibri Light", Font.PLAIN, 26));
		lblQuizFinished.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuizFinished.setBounds(10, 72, 454, 45);
		this.add(lblQuizFinished);


		JLabel lblNewLabel = new JLabel("You scored: " + correct + "/" + testNum);
		lblNewLabel.setFont(new Font("Calibri Light", Font.PLAIN, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 120, 454, 28);
		this.add(lblNewLabel);

		double percentage = ((double) correct * 100 )/ (double) testNum;

		JLabel lblWellDone = new JLabel();
		if (percentage >= 90) {
			lblWellDone.setText("Well Done! You can progress to the next level.");
			_advance = true;
		} else {
			lblWellDone.setText("Good effort! Please retry quiz in order to progress");
		}

		lblWellDone.setHorizontalAlignment(SwingConstants.CENTER);
		lblWellDone.setFont(new Font("Calibri Light", Font.PLAIN, 16));
		lblWellDone.setBounds(20, 141, 444, 28);
		this.add(lblWellDone);

		btnReturnToMenu.setBounds(170, 232, 145, 35);
		btnReturnToMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Menu menu = new Menu(_level, _main, _wordList, _maxNum, _file, _sound);
				_main.getContentPane().add(menu);
				menu.setVisible(true);
			}
		});
		this.add(btnReturnToMenu);

		btnRepeatQuiz.setBounds(170, 278, 145, 35);
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
		this.add(btnRepeatQuiz);

		btnViewStatistics.setBounds(170, 324, 145, 35);
		btnViewStatistics.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				makeTable();

			}

		});
		this.add(btnViewStatistics);

		btnWatchVideo.setBounds(170, 370, 145, 35);
		btnWatchVideo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MediaPlayer mp = new MediaPlayer();

			}

		});

		btnAdvance.setBounds(170, 190, 145, 35);
		btnAdvance.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				WordList wl= null;
				try {
					wl = new WordList(_file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int nextLevel = wl.getNextLevel(_level);
				setVisible(false);
				Menu menu = new Menu(nextLevel, _main, _wordList, _maxNum, _file, _sound );
				_main.getContentPane().add(menu);
				menu.setVisible(true);
			}

		});

		btnCoins .setBounds(170, 190, 145, 35);
		btnCoins.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int coin = getCoins();
				if ((coin - 1000) > 0) {
					updateCoins(coin - 1000);
					WordList wl= null;
					try {
						wl = new WordList(_file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					int nextLevel = wl.getNextLevel(_level);
					setVisible(false);
					Menu menu = new Menu(nextLevel, _main, _wordList, _maxNum, _file, _sound );
					_main.getContentPane().add(menu);
					menu.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "Insufficient funds!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		if (_advance) {
			this.add(btnWatchVideo);
			this.add(btnAdvance);
		} else {
			this.add(btnCoins);
		}
	}

	private void makeTable() {
		ViewAccuracy va = new ViewAccuracy(_wordList, this);

		_main.getContentPane().add(va);
		this.setVisible(false);
		va.setVisible(true);
	}

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
