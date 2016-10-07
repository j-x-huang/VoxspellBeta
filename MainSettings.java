package beta;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.SwingConstants;
import javax.swing.JToggleButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

public class MainSettings extends JPanel {

	private JPanel contentPane;
	private JLabel label_4 = new JLabel();
	private File _file;
	private File _oldFile;
	private int _maxNum;
	private JSpinner spinner = new JSpinner();
	private JFrame _frame;
	private WordList _wl;
	private int _level;
	private Menu _oldMenu;


	/**
	 * Create the frame.
	 */
	public MainSettings(JFrame frame, File file, int maxNum, int level, Menu menu) {
		_frame = frame;
		_file = file;
		_oldFile = file;
		_maxNum = maxNum;
		_level = level;
		_oldMenu = menu;

		label_4.setText(file.getName());

		this.setLayout(null);
		JLabel label = new JLabel("Settings");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		label.setBounds(10, 11, 454, 24);
		this.add(label);

		JLabel label_1 = new JLabel("Mute background");
		label_1.setFont(new Font("Calibri", Font.PLAIN, 15));
		label_1.setBounds(59, 84, 130, 24);
		this.add(label_1);

		JToggleButton toggleButton = new JToggleButton("");
		toggleButton.setBounds(245, 68, 57, 40);
		this.add(toggleButton);

		JLabel label_2 = new JLabel("Number of words tested");
		label_2.setFont(new Font("Calibri", Font.PLAIN, 15));
		label_2.setBounds(59, 156, 159, 14);
		this.add(label_2);

		spinner.setModel(new SpinnerNumberModel(maxNum, 5, 20, 1));
		spinner.setBounds(245, 153, 57, 20);
		this.add(spinner);


		JLabel label_3 = new JLabel("Current spelling list:");
		label_3.setFont(new Font("Calibri", Font.PLAIN, 15));
		label_3.setBounds(59, 213, 130, 14);
		this.add(label_3);

		label_4.setFont(new Font("Calibri", Font.PLAIN, 15));
		label_4.setBounds(245, 213, 112, 14);
		//---------------------------------------------------
		final JFileChooser fc = new JFileChooser();

		JButton button = new JButton("Select Spelling List");
		button.setBounds(245, 250, 119, 23);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int val = JOptionPane.showConfirmDialog(null, "Warning. Changing spellings lists will clear your save files and progress. Do you wish "
						+ "to continue", "alert", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (val == JOptionPane.OK_OPTION) {
					int returnVal = fc.showOpenDialog(MainSettings.this);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						_file = fc.getSelectedFile();

						label_4.setText(_file.getName());
					}
				}
			}
		});
		this.add(label_4);
		this.add(button);

		JButton button_1 = new JButton("Ok");
		button_1.setBounds(375, 417, 89, 23);
		button_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				File file = _file;
				int spinnerVal = (int) spinner.getValue();
				int level = 0;
				try {
					_wl = new WordList(file);
					level = levelSelect();

				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (level != 0) {
					try {
						_oldMenu.clearStats();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Menu menu = new Menu(level, _frame, file, spinnerVal);
					_frame.getContentPane().add(menu);
					setVisible(false);
					menu.setVisible(true);
				}

			}

		});
		this.add(button_1);

		JButton button_2 = new JButton("Cancel");
		button_2.setBounds(268, 417, 89, 23);
		button_2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Menu menu = new Menu(_level, _frame, _oldFile, _maxNum);
				_frame.getContentPane().add(menu);
				setVisible(false);
				menu.setVisible(true);
			}
		});
		this.add(button_2);
	}


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
