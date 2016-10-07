package beta;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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


	/**
	 * Create the panel.
	 */
	public SubMenu(WordList wordlist, int level, int correct, int testNum, JFrame main, int maxNum, File file) {
		
		//Setting the size of the main menu and choosing the layout of it.
		_wordList = wordlist;
		_main = main;
		_level = level;
		_maxNum = maxNum;
		_file = file;
		
		this.setBackground(new Color(255, 255, 102));
		this.setLayout(null);
		
		JLabel lblQuizFinished = new JLabel("Quiz Finished");
		lblQuizFinished.setFont(new Font("Calibri Light", Font.PLAIN, 26));
		lblQuizFinished.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuizFinished.setBounds(10, 72, 454, 45);
		this.add(lblQuizFinished);
		
		
		int wc = _wordList.getWordCount(level);
		if (wc < maxNum) {
			maxNum = wc;
		}
		JLabel lblNewLabel = new JLabel("You scored: " + correct + "/" + maxNum);
		lblNewLabel.setFont(new Font("Calibri Light", Font.PLAIN, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 120, 454, 28);
		this.add(lblNewLabel);
		
		double percentage = ((double) correct * 100 )/ (double) maxNum;
		
		JLabel lblWellDone = new JLabel();
		if (percentage >= 90) {
			lblWellDone.setText("Well Done! You can progress the the next level.");
		} else {
			lblWellDone.setText("Good effort! Please retry quiz in order to progress");
		}
		
		lblWellDone.setHorizontalAlignment(SwingConstants.CENTER);
		lblWellDone.setFont(new Font("Calibri Light", Font.PLAIN, 16));
		lblWellDone.setBounds(20, 141, 444, 28);
		this.add(lblWellDone);
		
		btnReturnToMenu.setBounds(170, 232, 135, 35);
		btnReturnToMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Menu menu = new Menu(_level, _main, _wordList, _maxNum, _file);
				_main.getContentPane().add(menu);
				menu.setVisible(true);
			}
		});
		this.add(btnReturnToMenu);
		
		btnRepeatQuiz.setBounds(170, 278, 135, 35);
		btnRepeatQuiz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Quiz q = null;
				try {
					q = new Quiz(_wordList, _level, _main, _maxNum, _file);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				q.setVisible(true);
			}
			
		});
		this.add(btnRepeatQuiz);
		
		btnViewStatistics.setBounds(170, 324, 135, 35);
		btnViewStatistics.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				makeTable();
				
			}
			
		});
		this.add(btnViewStatistics);
		
		btnWatchVideo.setBounds(170, 370, 135, 35);
		btnWatchVideo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MediaPlayer mp = new MediaPlayer();
				
			}
			
		});
		this.add(btnWatchVideo);
		
		btnAdvance.setBounds(170, 190, 135, 35);
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
				Menu menu = new Menu(nextLevel, _main, _wordList, _maxNum, _file);
				_main.getContentPane().add(menu);
				menu.setVisible(true);
			}
			
		});
		this.add(btnAdvance);
	}
	
	private void makeTable() {
		ViewAccuracy va = new ViewAccuracy(_wordList);
		JTable table = new JTable(va);
		final JFrame fr = new JFrame();
		fr.setSize(500,500);
		fr.setVisible(true);
		//Create panels for Statistics. Add table to panel.
		JPanel statsPanel = new JPanel();
		JButton returnBtn = new JButton("Close Stats");
		statsPanel.setLayout(new BorderLayout());
		//statsPanel.add(_statLabel, BorderLayout.NORTH);
		statsPanel.add(new JScrollPane(table), BorderLayout.CENTER);
		returnBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fr.dispose();
			}

		});
		statsPanel.add(returnBtn, BorderLayout.SOUTH);
		fr.add(statsPanel);
	}

}
