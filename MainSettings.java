package beta;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.swing.SwingConstants;
import javax.swing.JToggleButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

public class MainSettings extends JPanel {

	private JPanel contentPane;
	private JLabel lblCurrentFile = new JLabel();
	private File _file;
	private File _oldFile;
	private int _maxNum;
	private JSpinner spinner = new JSpinner();
	private JFrame _frame;
	private WordList _wl;
	private int _level;
	private Menu _oldMenu;
	private Sound _sound;


	/**
	 * Create the frame.
	 */
	public MainSettings(JFrame frame, File file, int maxNum, int level, Menu menu, Sound sound) {
		_frame = frame;
		_file = file;
		_oldFile = file;
		_maxNum = maxNum;
		_level = level;
		_oldMenu = menu;
		_sound = sound;

		lblCurrentFile.setText(file.getName()); //get current wordlist file name

		//set up buttons and labels
		this.setLayout(null);
		this.setBackground(new Color(255, 255, 153));

		JLabel lblTile = new JLabel("Settings");
		lblTile.setHorizontalAlignment(SwingConstants.CENTER);
		lblTile.setFont(new Font("Calibri Light", Font.PLAIN, 35));
		lblTile.setBounds(10, 11, 850, 40);
		this.add(lblTile);

		JLabel lblStopMusic = new JLabel("Stop Music");
		lblStopMusic.setFont(new Font("Arial", Font.PLAIN, 24));
		lblStopMusic.setBounds(257, 90, 150, 30);
		this.add(lblStopMusic);

		JToggleButton toggleButton = new JToggleButton("");
		toggleButton.setIcon(new ImageIcon("mute2.png"));
		toggleButton.setBounds(500, 84, 57, 40);
		if(! _sound.isActive()) {
			toggleButton.setSelected(true);
		}
		toggleButton.addItemListener(new ItemListener() { //This button stops or plays background music 

			@Override
			public void itemStateChanged(ItemEvent ev) {
				if(ev.getStateChange()==ItemEvent.SELECTED){
					_sound.stop();
				} else if(ev.getStateChange()==ItemEvent.DESELECTED){
					_sound.loop();
				}

			}
		});
		this.add(toggleButton);

		JLabel lblMaxNum = new JLabel("Number of words tested");
		lblMaxNum.setFont(new Font("Arial", Font.PLAIN, 24));
		lblMaxNum.setBounds(100, 170, 300, 30);
		this.add(lblMaxNum);

		spinner.setModel(new SpinnerNumberModel(maxNum, 5, 20, 1)); //A spinner to select the max number of words tested. min is 5, max is 20
		spinner.setBounds(500, 170, 57, 30);
		this.add(spinner);


		JLabel lblCurrentList = new JLabel("Current spelling list:");
		lblCurrentList.setFont(new Font("Arial", Font.PLAIN, 24));
		lblCurrentList.setBounds(150, 256, 300, 30);
		this.add(lblCurrentList);

		lblCurrentFile.setFont(new Font("Arial", Font.PLAIN, 24));
		lblCurrentFile.setBounds(500, 256, 320, 30);
		//File choose allows users to select their own lists
		final JFileChooser fc = new JFileChooser();

		JButton btnSelectList = new JButton("Select Spelling List"); //pressing this button opens up file chooser
		btnSelectList.setFont(new Font("Arial", Font.PLAIN, 18));
		btnSelectList.setBounds(500, 300, 210, 40);
		btnSelectList.setBackground(new Color(255,153, 51));
		btnSelectList.setForeground(new Color(255,255, 153));
		btnSelectList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) { //warning message pop if users want to change list
				int returnVal = fc.showOpenDialog(MainSettings.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					_file = fc.getSelectedFile();

					lblCurrentFile.setText(_file.getName()); //get file name that the user has selected
				}

			}
		});
		this.add(lblCurrentFile);
		this.add(btnSelectList);

		JButton btnOk = new JButton("Ok"); //ok button confirms users file selection
		btnOk.setFont(new Font("Arial", Font.PLAIN, 18));
		btnOk.setBounds(700, 511, 90, 44);
		btnOk.setBackground(new Color(255,255,50));
		btnOk.setBorder(new MatteBorder(1,1,1,1, new Color(0,0,0)));
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				File file = _file;
				int spinnerVal = (int) spinner.getValue();
				try {
					_wl = new WordList(file, file.getName()); //make a wordlist from the file

				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (! file.equals(_oldFile)) { //if new file selected is not the same as the previous
					_level = levelSelect();    //choose a level (for new wordlist)
					if (_level != 0) {
						try {
							//_oldMenu.clearStats(); //clears old stats

							File listFile = new File(".defaultList.txt"); //change default spelling list save. So that if user boots up spelling list
							PrintWriter pw = new PrintWriter(listFile);   //the same one which he chose opens up
							pw.close();

							FileWriter fw = new FileWriter(listFile);
							BufferedWriter bw = new BufferedWriter(fw);
							String path = file.getAbsolutePath();
							bw.write(path);
							bw.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				Menu menu = new Menu(_level, _frame, _wl, spinnerVal, file, _sound); //make new menu
				_frame.getContentPane().add(menu);
				setVisible(false);
				menu.setVisible(true);
			}


		});
		this.add(btnOk);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Arial", Font.PLAIN, 18));
		btnCancel.setBounds(596, 511, 90, 44);
		btnCancel.setBackground(new Color(255,255,50));
		btnCancel.setBorder(new MatteBorder(1,1,1,1, new Color(0,0,0)));
		btnCancel.addActionListener(new ActionListener() { //returns users back to menu with the level and maxNum as before

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					_wl = new WordList(_oldFile, _oldFile.getName());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Menu menu = new Menu(_level, _frame, _wl, _maxNum, _oldFile, _sound);
				_frame.getContentPane().add(menu);
				setVisible(false);
				menu.setVisible(true);
			}
		});
		this.add(btnCancel);
	}

	//A level selection dialog box. Pops up only if users choose a new wordlist
	private int levelSelect() {


		int[] levels = _wl.getLevels();
		String[] levelStrings=Arrays.toString(levels).split("[\\[\\]]")[1].split(", "); 

		String num = (String) JOptionPane.showInputDialog(this, "Please select a level", "Level Select", 
				JOptionPane.PLAIN_MESSAGE, null, levelStrings, levelStrings[0]);


		if(num==null){
			return 0;
		}else{
			return Integer.parseInt(num);
		} 
	}


}
