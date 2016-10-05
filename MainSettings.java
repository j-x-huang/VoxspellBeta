package beta;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.SwingConstants;
import javax.swing.JToggleButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class MainSettings extends JPanel {

	private JPanel contentPane;
	private JLabel label_4 = new JLabel();
	private File _file;
	private JSpinner spinner = new JSpinner();
	private JFrame _frame;



	/**
	 * Create the frame.
	 */
	public MainSettings(JFrame frame, File file) {
		_frame = frame;
		_file = file;
		
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

		spinner.setModel(new SpinnerNumberModel(10, 5, 20, 1));
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
				int returnVal = fc.showOpenDialog(MainSettings.this);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            _file = fc.getSelectedFile();
		            
		            label_4.setText(_file.getName());
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
				
				Menu menu = new Menu(1, _frame, file, spinnerVal);
				_frame.getContentPane().add(menu);
				setVisible(false);
				menu.setVisible(true);
				
			}
			
		});
		this.add(button_1);

		JButton button_2 = new JButton("Cancel");
		button_2.setBounds(268, 417, 89, 23);
		this.add(button_2);
	}
}
