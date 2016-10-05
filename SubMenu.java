package beta;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SubMenu extends JPanel {

	private File _file;
	private JPanel _menu;
	private JFrame _main;
	private int _level;
	
	private JButton btnReturnToMenu = new JButton("Return to Menu");
	private JButton btnRepeatQuiz = new JButton("Repeat Quiz");
	private JButton btnViewStatistics = new JButton("View Statistics");
	private JButton btnWatchVideo = new JButton("Watch Video");
	private JButton btnAdvance = new JButton("Advance ");


	/**
	 * Create the panel.
	 */
	public SubMenu(File file, int level, int correct, int testNum, JFrame main) {
		
		//Setting the size of the main menu and choosing the layout of it.
		_file = file;
		_main = main;
		_level = level;
		
		this.setBackground(new Color(255, 255, 102));
		this.setLayout(null);
		
		JLabel lblQuizFinished = new JLabel("Quiz Finished");
		lblQuizFinished.setFont(new Font("Calibri Light", Font.PLAIN, 26));
		lblQuizFinished.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuizFinished.setBounds(10, 72, 454, 45);
		this.add(lblQuizFinished);
		
		JLabel lblNewLabel = new JLabel("You scored: 10/10");
		lblNewLabel.setFont(new Font("Calibri Light", Font.PLAIN, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 120, 454, 28);
		this.add(lblNewLabel);
		
		JLabel lblWellDone = new JLabel("Well Done! You can progress the the next level.");
		lblWellDone.setHorizontalAlignment(SwingConstants.CENTER);
		lblWellDone.setFont(new Font("Calibri Light", Font.PLAIN, 16));
		lblWellDone.setBounds(20, 141, 444, 28);
		this.add(lblWellDone);
		
		btnReturnToMenu.setBounds(170, 232, 135, 35);
		btnReturnToMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Menu menu = new Menu(_level, _main, _file, 10);
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
					q = new Quiz(_file, _level, _main);
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
				// TODO Auto-generated method stub
				
			}
			
		});
		this.add(btnViewStatistics);
		
		btnWatchVideo.setBounds(170, 370, 135, 35);
		this.add(btnWatchVideo);
		
		btnAdvance.setBounds(170, 190, 135, 35);
		btnAdvance.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Menu menu = new Menu(_level + 1, _main, _file, 10);
				_main.getContentPane().add(menu);
				menu.setVisible(true);
			}
			
		});
		this.add(btnAdvance);
	}
	
	public void actionPerformed(ActionEvent e) {
		//Finding the button where the action event occured i.e. finding 
		//the button that is clicked
		try{
		JButton button = (JButton) e.getSource();
		if (button.equals(btnReturnToMenu)){  
			setVisible(false);
			_main.setVisible(true);
		}else if(button.equals(btnRepeatQuiz)){

		}else if(button.equals(btnViewStatistics)){
			//_main.makeTable();
			
		}else if(button.equals(btnAdvance)){
			
		}else if(button.equals(btnWatchVideo)){
			MediaPlayer player = new MediaPlayer();

		}
		}catch(Exception e2){
			e2.printStackTrace();
		}
	}

}
