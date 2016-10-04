package beta;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JToggleButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;

public class MainSettings extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainSettings frame = new MainSettings();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainSettings() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 382, 270);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("Settings");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		label.setBounds(10, 11, 346, 24);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Mute background");
		label_1.setFont(new Font("Calibri", Font.PLAIN, 15));
		label_1.setBounds(10, 60, 130, 24);
		contentPane.add(label_1);
		
		JToggleButton toggleButton = new JToggleButton("");
		toggleButton.setBounds(196, 44, 57, 40);
		contentPane.add(toggleButton);
		
		JLabel lblNumberOfWords = new JLabel("Number of words tested");
		lblNumberOfWords.setFont(new Font("Calibri", Font.PLAIN, 15));
		lblNumberOfWords.setBounds(10, 100, 159, 14);
		contentPane.add(lblNumberOfWords);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(10, 5, 20, 1));
		spinner.setBounds(196, 97, 57, 20);
		contentPane.add(spinner);
		
		JLabel lblCurrentSpellingList = new JLabel("Current spelling list");
		lblCurrentSpellingList.setFont(new Font("Calibri", Font.PLAIN, 15));
		lblCurrentSpellingList.setBounds(10, 125, 130, 14);
		contentPane.add(lblCurrentSpellingList);
		
		JLabel lblWordlist = new JLabel("wordList");
		lblWordlist.setFont(new Font("Calibri", Font.PLAIN, 15));
		lblWordlist.setBounds(196, 125, 112, 14);
		contentPane.add(lblWordlist);
		
		JButton btnSelectSpellingList = new JButton("Select Spelling List");
		btnSelectSpellingList.setBounds(196, 150, 119, 23);
		contentPane.add(btnSelectSpellingList);
		
		JButton btnOk = new JButton("Ok");
		btnOk.setBounds(267, 197, 89, 23);
		contentPane.add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(163, 197, 89, 23);
		contentPane.add(btnCancel);
	}
}
