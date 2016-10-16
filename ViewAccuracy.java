package beta;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;



public class ViewAccuracy extends JPanel implements ItemListener{


	private ArrayList<Integer> attemptsList = new ArrayList<Integer>();
	private ArrayList<Integer> failsList = new ArrayList<Integer>();
	private ArrayList<Integer> scoreList = new ArrayList<Integer>();
	private ArrayList<Double> accuracyList = new ArrayList<Double>();



	private int _size;
	private int[] _levels;
	private JPanel returnPanel;
	private int _coins;
	private JComboBox levelSelect;
	private WordList wl;
	private JPanel _mainPanel;


	public ViewAccuracy(WordList wordlist, JPanel panel) {
		returnPanel = panel;
		wl = wordlist;

		_levels = wl.getLevels(); //get the number of levels in the wordlist
		_size = _levels.length;
		String fileName = wl.getFileName();
		
	
		this.setLayout(new BorderLayout());
		JButton returnBtn = new JButton("Return"); ///make return button
		returnBtn.addActionListener(new ActionListener() { //return button makes the panel invisible

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				returnPanel.setVisible(true);
			}
		});
		
		
		//this.add(new JScrollPane(table), BorderLayout.CENTER);
		getCoins();
		JLabel coinLbl = new JLabel("Coins: " + _coins);	//Show the coin value in a label
		coinLbl.setHorizontalAlignment(SwingConstants.TRAILING);
		JLabel titleLbl = new JLabel("Wordlist: " + wordlist.getFileName()); //Show name of wordlist in a label
		titleLbl.setHorizontalAlignment(SwingConstants.LEADING);
		JPanel topPanel = new JPanel(new GridLayout(1,2));
		topPanel.add(titleLbl);
		topPanel.add(coinLbl);
		this.add(topPanel, BorderLayout.NORTH);
		
		//This for loop reads all accuracy save files and adds the values to some lists
		for (int i = 0; i < _size; i++) {
			try {
				FileReader fr = new FileReader("." + fileName + "_" + _levels[i]);
				BufferedReader br = new BufferedReader(fr);
				String str;
				str = br.readLine();
				attemptsList.add(Integer.parseInt(str));
				str = br.readLine();
				failsList.add(Integer.parseInt(str));
				str = br.readLine();
				scoreList.add(Integer.parseInt(str));
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//This for loop calculates the overall accuracy (as a percentage) for all levels
		for (int j = 0; j < _size; j++) {
			double num = 0;
			if (attemptsList.get(j) != 0) {
				num = (double) failsList.get(j) / (double) attemptsList.get(j);
				num = 100 - (num * 100);

				num = (double) Math.round(num * 100) / 100;

			}

			accuracyList.add(num);
		}
		

		
		//combobox stuff
		String[] levelStrings = new String[_size + 1];
		levelStrings[0] = "Overall";
		
		for (int k = 1; k < _size + 1; k++) {
			levelStrings[k] = Integer.toString(_levels[k-1]);
		}
		levelSelect = new JComboBox(levelStrings);
		levelSelect.addItemListener(this);
		JPanel southPanel = new JPanel();
		southPanel.add(levelSelect);
		southPanel.add(returnBtn);
		this.add(southPanel, BorderLayout.SOUTH);
		
		int accSum = 0;
		for (int x : attemptsList) {
			accSum+=x;
		}
		int failSum = 0;
		for (int x : failsList) {
			failSum+=x;
		}
		int correctSum = accSum - failSum;
		
		//Pie Chart Stuff
		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(null);
		outerPanel.setPreferredSize(new Dimension(600,830));
		outerPanel.setBackground(new Color(255,255,152));
		JScrollPane sp = new JScrollPane(outerPanel);
		PieModel pm = new PieModel(correctSum, failSum, "All Levels");
		JPanel piePanel = pm.getPieChart();
		piePanel.setBounds(10, 10, 800, 500);
		outerPanel.add(piePanel);
		
		_mainPanel = new JPanel(new CardLayout());
		_mainPanel.add(sp, "Overall");
		createCharts();
		
		this.add(_mainPanel);
		
	}
	
	@Override
	public void itemStateChanged(ItemEvent evt) {
		CardLayout cl = (CardLayout)(_mainPanel.getLayout());
	    cl.show(_mainPanel, (String)evt.getItem());
		
	}
	
	private void createCharts() {
		for (int i = 0; i < _size; i++) {
			int level = _levels[i];
			JPanel outerPanel = new JPanel();
			outerPanel.setLayout(null);
			outerPanel.setPreferredSize(new Dimension(600,830));
			outerPanel.setBackground(new Color(255,255,152));
			JScrollPane sp = new JScrollPane(outerPanel);
			
			int correct = attemptsList.get(i) - failsList.get(i);
			PieModel pm = new PieModel(correct, failsList.get(i), "Level "  + Integer.toString(level));
			JPanel piePanel = pm.getPieChart();
			piePanel.setBounds(10, 10, 800, 500);
			outerPanel.add(piePanel);
						
			JScrollPane tablePanel = new JScrollPane();
			tablePanel.setBounds(20, 570, 750, 200);
			StatsTable st = new StatsTable(level);
			JTable table = new JTable(st);
			tablePanel.setViewportView(table);
			outerPanel.add(tablePanel);
			
			JLabel scoreLabel = new JLabel("High Score: " + scoreList.get(i));
			scoreLabel.setBounds(20, 780, 250, 40);
			scoreLabel.setFont(new Font("Arial", Font.PLAIN, 26));
			outerPanel.add(scoreLabel);
			
			_mainPanel.add(sp, Integer.toString(level));

			
		}
		
	}
	
	//reads the coin value from save file and adds it to the coins field
	private void getCoins() {
		try {
		File file = new File(".coinSave");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String str;
			str = br.readLine();
			_coins = Integer.parseInt(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	


	class StatsTable extends AbstractTableModel {
		
		private final String[] COLUMN_HEADERS = {"Word", "Correct", "Incorrects", "Attempts"};
		private final Class<?> _colClasses[] = {String.class, Integer.class,Integer.class, Integer.class};
		private int _level;
		private int _levelSize;
		private ArrayList<Word> wlist;
		
		public StatsTable(int level) {
			_level = level;
			
			_levelSize = wl.getLevelLength(level);
			
			wlist = wl.getLevelMap().get(level);
			
		}

		@Override
		public int getRowCount() {
			return _levelSize;
		}

		@Override
		public int getColumnCount() {
			return 4;

		}
		//Add values from the arraylists into the table
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return wlist.get(rowIndex).toString();
			} else if (columnIndex == 1) {
				return wlist.get(rowIndex).getCorrect();
			} else if (columnIndex == 2) {
				return wlist.get(rowIndex).getFails();
			} else if (columnIndex == 3) {
				return wlist.get(rowIndex).getAttempts();

			} 
			return null;
		}

		@Override
		public String getColumnName(int column) {
			return COLUMN_HEADERS[column];

		}
		@Override
		public Class<?> getColumnClass(int column) {
			return _colClasses[column];
		}
	}


}




