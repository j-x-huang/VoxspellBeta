package beta;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JToggleButton;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Settings extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Settings frame = new Settings();
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
	public Settings() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 346, 215);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblChangeVoice = new JLabel("Change Voice");
		lblChangeVoice.setBounds(10, 99, 85, 24);
		contentPane.add(lblChangeVoice);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(118, 101, 186, 20);
		contentPane.add(comboBox);
		
		JLabel lblSettings = new JLabel("Settings");
		lblSettings.setHorizontalAlignment(SwingConstants.CENTER);
		lblSettings.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		lblSettings.setBounds(10, 11, 310, 24);
		contentPane.add(lblSettings);
		
		JLabel lblMute = new JLabel("Mute background");
		lblMute.setBounds(10, 74, 99, 14);
		contentPane.add(lblMute);
		
		JToggleButton toggleButton = new JToggleButton("");
		toggleButton.setIcon(new ImageIcon("C:\\Users\\Jack\\workspace\\VoxspellGUI\\mute2.png"));
		toggleButton.setBounds(118, 50, 57, 40);
		contentPane.add(toggleButton);
		
		JButton btnOK = new JButton("Ok");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnOK.setBounds(215, 132, 89, 23);
		contentPane.add(btnOK);
	}
}
