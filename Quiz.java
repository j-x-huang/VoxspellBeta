package beta;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import beta.ViewAccuracy;

public class Quiz extends JPanel implements ActionListener {
	private JTextField textField;
	private int _level;
	private int _testNo=1;
	private int _attempts;
	private int _fails;
	private int _maxNum;
	protected ArrayList<String> _voices;
	private ArrayList<Word> _testList = new ArrayList<Word>();
	private String _voice;
	private JComboBox<String> _selectVoices;
	private JPanel _main;
	private int _wc;
	private int _testNum;
	private int _coins = 0;
	private int _streak = 0;

	private JButton btnListenAgain = new JButton("Repeat");
	private JButton btnSubmit = new JButton("Submit");
	private JButton btnSettings = new JButton("");
	private JButton btnMenu = new JButton("Menu");

	private JLabel lblCoin = new JLabel("Coins: 0");
	private JLabel lblPleaseSpellWord = new JLabel("Please spell word 1 of 3:");
	private JLabel lblAcc = new JLabel("Accuracy: 0/10");
	private JLabel lblCorrect = new JLabel("<html>Oh hello there! My name is Richard Parker, and I will be helping you with your spelling today.</html>");
	private JLabel lblStreak = new JLabel();
	private int _correct=0;
	private int incorrect;

	private JFrame _frame;
	private WordList _wordlist;
	private int _hiddenCoins;
	private File _file;
	private Sound _sound;
	private Timer _timer;
	private int _highScore;
	private String _fileName;
	private String _correctness;

	/**
	 * Create the panel.
	 */
	public Quiz(WordList wordlist,int level, JFrame frame, int maxNum, File file, Sound sound) throws  Exception {
		_level = level;
		_frame = frame;
		_maxNum = maxNum;
		_wordlist = wordlist;
		_file = file;
		_sound = sound;
		_fileName = _file.getName();

		getAccuracy();
		getCoins();

		getVoices();
		_voice = _voices.get(0);

		//changeVoice("voice_akl_nz_jdt_diphone");
		this.setBackground(new Color(255, 255, 153));
		this.setLayout(null);

		JLabel lblQuiz = new JLabel("Quiz");
		lblQuiz.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuiz.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		lblQuiz.setBounds(0, 11, 850, 57);
		this.add(lblQuiz);

		//Level Subtitle
		JLabel lblLevel = new JLabel("Level " + _level);
		lblLevel.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblLevel.setHorizontalAlignment(SwingConstants.CENTER);
		lblLevel.setBounds(10, 65, 850, 26);

		this.add(lblLevel);

		//Setting up GUI features
		lblPleaseSpellWord.setFont(new Font("Arial", Font.PLAIN, 26));
		lblPleaseSpellWord.setBounds(56, 157, 433, 49);
		this.add(lblPleaseSpellWord);

		textField = new JTextField();
		textField.setFont(new Font("Calibri", Font.PLAIN, 20));
		textField.setBounds(56, 227, 738, 49);
		this.add(textField);
		textField.setColumns(10);

		btnSubmit.setBounds(442, 287, 157, 49);
		btnSubmit.setFont(new Font("Arial Black", Font.PLAIN, 20));
		btnSubmit.setBorder(new MatteBorder(1,1,1,1, new Color(0,0,0)));
		btnSubmit.setBackground(new Color(255,153, 51));
		btnSubmit.setForeground(new Color(255,255, 153));
		this.add(btnSubmit);

		btnListenAgain.setBounds(268, 287, 157, 49);
		btnListenAgain.setFont(new Font("Arial Black", Font.PLAIN, 20));
		btnListenAgain.setBorder(new MatteBorder(1,1,1,1, new Color(0,0,0)));
		btnListenAgain.setBackground(new Color(255,153, 51));
		btnListenAgain.setForeground(new Color(255,255, 153));
		this.add(btnListenAgain);

		lblAcc.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblAcc.setBounds(616, 436, 157, 49);
		this.add(lblAcc);

		btnSettings.setIcon(new ImageIcon("gear_ss.png"));
		btnSettings.setBounds(737, 511, 57, 57);
		btnSettings.setBackground(new Color(255,255,50));
		btnSettings.setBorder(new MatteBorder(1,1,1,1, new Color(0,0,0)));
		btnSettings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Settings set = new Settings(_frame, "", Dialog.ModalityType.APPLICATION_MODAL);
				set.setVisible(true);
			}
		});
		this.add(btnSettings);
	

		lblCoin.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblCoin.setIcon(new ImageIcon("Coin1.png"));
		lblCoin.setBounds(616, 385, 148, 49);
		this.add(lblCoin);

		lblStreak.setFont(new Font("Calibri", Font.PLAIN, 15));
		lblStreak.setBounds(580, 385, 70, 49);
		this.add(lblStreak);

		JLabel beelbl = new JLabel("");
		beelbl.setIcon(new ImageIcon("bee_h.png"));
		beelbl.setBounds(611, 55, 157, 134);
		this.add(beelbl);

		JLabel lblUpperBox = new JLabel("");
		lblUpperBox.setIcon(new ImageIcon("uppertbox.png"));
		lblUpperBox.setBounds(46, 135, 584, 92);
		this.add(lblUpperBox);
		
		btnMenu.setFont(new Font("Calibri", Font.PLAIN, 18));
		btnMenu.setBounds(620, 511, 100, 57);
		btnMenu.setBackground(new Color(255,255,50));
		btnMenu.setBorder(new MatteBorder(1,1,1,1, new Color(0,0,0)));
		btnMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog (_frame, "Are you sure you want to return to the main menu?","",JOptionPane.YES_NO_OPTION);
				if(option == JOptionPane.YES_OPTION){
				setVisible(false);
				Menu menu = new Menu(_level, _frame, _wordlist, _maxNum, _file, _sound);
				_frame.getContentPane().add(menu);
				menu.setVisible(true);
				}
			}
			
		});
		this.add(btnMenu);

		JLabel lblSBee = new JLabel("");
		lblSBee.setIcon(new ImageIcon("sbee2.png"));
		lblSBee.setBounds(39, 492, 81, 76);
		this.add(lblSBee);

		lblCorrect.setBounds(140, 200, 400, 400);
		lblCorrect.setFont(new Font("Arial", Font.PLAIN, 18));
		this.add(lblCorrect);

		JLabel lblLowerBox = new JLabel("");
		lblLowerBox.setIcon(new ImageIcon("lowerbox.png"));
		lblLowerBox.setBounds(130, 349, 487, 208);
		this.add(lblLowerBox);


		//get the wordcount (of a particular level) rom the WordList class

		_wc = _wordlist.getWordCount(_level);

		//Choosing the number of quiz depending on the word count
		String tts = "";
		if(_wc<_maxNum){
			_testNum=_wc;
			tts="Spell word 1 of "+_wc+": ";
			lblPleaseSpellWord.setText(tts);
		}else{
			_testNum = _maxNum;
			tts="Spell word 1 of "+_maxNum+": ";
			lblPleaseSpellWord.setText(tts);
		}
		lblAcc.setText("Accuracy: 0" +"/"+_testNum); //set up the accuracy level

		btnListenAgain.addActionListener(this);
		btnSubmit.addActionListener(this);

		_frame.getRootPane().setDefaultButton(btnSubmit); //make submit default button
		//Word to be tested
		setTestList(_wordlist);
		festival("Spell " + _testList.get(_testNo-1).toString(), null);
	}

	public void actionPerformed(ActionEvent e) {
		//Getting the word that user wrote
		String word = textField.getText();

		try{
			//If user pressed speak button,  the word
			//is spoken by festival.
			JButton button = (JButton) e.getSource();  
			if (button.equals(btnListenAgain)){  

				festival(_testList.get(_testNo-1).toString(), btnListenAgain);
				return;
			}
			//If user is correct
			if(_testList.get(_testNo-1).toString().equalsIgnoreCase(word)){
				//Showing and telling correct message
				Sound sound = new Sound("cheering.wav"); //plays a cheering sound
				sound.play();
				
				_testList.get(_testNo -1).increaseCorrect();
				//update accuracy and streak
				_attempts++;
				_testNo++;
				_correct++;

				_streak++;
				//get 50 coins if streak > 5, 20 coins if streak > 2
				if (_streak >5) {
					_coins+=50;
					_hiddenCoins+=50;
					lblStreak.setText("+50");
					lblCorrect.setText("<html>Correct! Very Nice.</html>");
				} else if (_streak > 2) {
					_coins+=20;
					_hiddenCoins+=20;
					lblStreak.setText("+20");
					lblCorrect.setText("<html>Correct! Well Done! Keep Going.</html>");
				} else {
					_coins+=10;
					_hiddenCoins+=10;
					lblStreak.setText("+10");
					lblCorrect.setText("<html>Correct! <br>Awesome, you're on a roll!</html>");

				}
				blink(); //flash 'Streak' label text
				updateCoins();
				lblCoin.setText("Coins: "+ _coins);
				_correctness = "Correct!!";

				//Setting the new label
				lblPleaseSpellWord.setText("Spell word "+(_testNo)+" of "+_maxNum+": ");
				if(_wc<_maxNum)
					lblPleaseSpellWord.setText("Spell word "+(_testNo)+" of "+ _wc+": ");


				incorrect =0;
				//If user gets incorrect
			}else{
				lblStreak.setText("");
				//If second time failing
				if(incorrect<1){
					//Setting message to the user about the fault
					lblCorrect.setText("Incorrect. Try again");
					festival("Incorrect!! Spell"+_testList.get(_testNo-1).toString()+".", btnSubmit);
					//Word is spoken again.
					incorrect++;
					textField.setText("");
					return;
					//First time failing
				}else{
					//Result message to user
					lblCorrect.setText("Incorrect");

					//increase test number and fail value
					_attempts++;
					_fails++;
					_testList.get(_testNo -1).increaseFails();

					_streak = 0; // streak resets
					//Changing field as needed

					_testNo++;
					incorrect =0;
					_correctness = "Incorrect";


					//Setting new label for new quiz
					lblPleaseSpellWord.setText("Spell word "+(_testNo)+" of "+_maxNum+": ");
					if(_wc<_maxNum)
						lblPleaseSpellWord.setText("Spell word "+(_testNo)+" of "+ _wc+": ");

				}
			}
			//updating the values.
			lblAcc.setText("Accuracy:" + _correct+"/"+_testNum);
			updateAccuracy();
			//Clearing the Jtext field
			textField.setText("");
			//If test is finished
			if((_testNo==_maxNum+1)||(_wc<_testNo)){
				//Telling the user the teset is finished
				lblCorrect.setText(_correctness+" Quiz Finished!!");
				festival(lblCorrect.getText(), null);
				//Update high score
				if (_correct > _highScore) {
					_highScore = _correct;
					updateAccuracy();
				}
				//opens options menu where user can choose their next action.
				SubMenu sub = new SubMenu(_wordlist,_level,_correct,_testNo-1, _frame , _maxNum, _file, _sound);
				_frame.getContentPane().add(sub);
				this.setVisible(false);
				sub.setVisible(true);

			}else{
				//Continue the quiz
				festival(_correctness+" Spell "+_testList.get(_testNo-1).toString()+".", btnSubmit);
			}
		}catch(Exception excep){
			excep.printStackTrace();
		}
	}

	//Function that allows user to change the voice of festival.
	private void getVoices() throws Exception{

		Festival f = new Festival("","1");
		_voices = f.listOfVoices();

	}

	//method to change the voice.
	private void changeVoice(String voice) throws IOException{
		File failed = new File(".festivalrc");
		//If file does not exist, create new file
		if(!failed.exists()) {
			failed.createNewFile();
		} 

		//Appending the word to the file
		Writer output;
		output = new BufferedWriter(new FileWriter(failed,false)); 
		output.append("(set! voice_default '"+voice +")");
		output.close();
	}


	//Method that uses festival to speak out the string passed into it
	private void festival(String tts, JButton btn) throws Exception{
		if (btn != null) {
			btn.setEnabled(false);
		}
		Festival say = new Festival(tts,_voice, btn);
		say.execute();

	}
	
	/*
	//Speaking out the spelling of the word passed into it
	private void festivalAlphabet(String tts) throws Exception{
		String word="";
		String[] alpha = tts.split("");

		for(int i=0;i<alpha.length;i++)
			word = word + alpha[i]+" ";
		festival(word);
	}*/


	
	//method makes the list of words to be tested 
	private void setTestList(WordList wordlist) throws IOException{
		_testList = wordlist.createTestList(_level,_maxNum);	
	}

	//Obtains the accuracy values from the save file (for the current quiz level)
	// then add the levels to the corresponding fields
	private void getAccuracy() throws IOException {
		File accuracy = new File("." + _fileName + "_" + _level);
		if (! accuracy.exists()) {
			accuracy.createNewFile();
		} else {

			FileReader fr = new FileReader(accuracy);
			BufferedReader br = new BufferedReader(fr);
			String str;
			str = br.readLine();
			_attempts = Integer.parseInt(str);
			str = br.readLine();
			_fails = Integer.parseInt(str);
			str = br.readLine();
			_highScore = Integer.parseInt(str);

		}
	}
	
	//obtains the number of coins the user has from the save file
	private void getCoins() throws IOException{
		File file = new File(".coinSave");
		if (! file.exists()) {
			file.createNewFile();
		} else {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String str;
			str = br.readLine();
			_hiddenCoins = Integer.parseInt(str);
		}
	}
	//Writes the value from the hiddenCoin filed to the coin save file
	private void updateCoins() {
		File file = new File(".coinSave");

		PrintWriter pw;
		try {
			pw = new PrintWriter(file);
			pw.close();

			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(_hiddenCoins + "\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	//Write the values from the accuracy fields (_attempts, _fails) to the corresponding
	// save files
	private void updateAccuracy() throws IOException {
		File accuracy = new File("." + _fileName + "_" + _level);

		PrintWriter pw = new PrintWriter(accuracy);
		pw.close();

		FileWriter fw = new FileWriter(accuracy);
		BufferedWriter bw = new BufferedWriter(fw);

		bw.write(_attempts + "\n");
		bw.write(_fails + "\n");
		bw.write(_highScore + "\n");
		bw.close();
	}
	
	//makes the streak label flash several times
	private void blink() {

		_timer = new Timer(50, new TimerListener());
		_timer.start();


	}
	// this is an action listener for the timer. Flashes should stop
	// after 18 'flashes'
	public class TimerListener implements ActionListener {
		int count = 0;
		@Override
		public void actionPerformed(ActionEvent e) {
			if (count == 18) {
				lblStreak.setText("");
				_timer.stop();
			}
			else if(count % 2 == 0) {
				lblStreak.setForeground(new Color(255,0,0));
			}
			else {
				lblStreak.setForeground(new Color(0,0,255));
			}
			count++;
			
		}
	}
	
	/**
	 * The class implements a mini settings dialog box
	 * It allows the user to control the background music as well
	 * as the ability to change the voice
	 *
	 */
	public class Settings extends JDialog {

		private JPanel contentPane;

		/**
		 * Create the frame.
		 */
		public Settings(Window owner, String title, Dialog.ModalityType modType) {
			super(owner, title, modType);
			//set up the GUI
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 500, 230);
			setResizable(false);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);
			contentPane.setBackground(new Color(255,255,152));

			JLabel lblChangeVoice = new JLabel("Change Voice");
			lblChangeVoice.setFont(new Font("Arial", Font.PLAIN, 18));
			lblChangeVoice.setBounds(25, 130, 135, 24);
			contentPane.add(lblChangeVoice);

			String[] voices = {"New Zealander","American", "British"}; //get the list of voices installed on users computer

			final JComboBox<String> comboBox = new JComboBox<String>(voices);
			comboBox.setBounds(220, 125, 210, 30);
			contentPane.add(comboBox);

			JLabel lblSettings = new JLabel("Settings");
			lblSettings.setHorizontalAlignment(SwingConstants.CENTER);
			lblSettings.setFont(new Font("Calibri Light", Font.PLAIN, 25));
			lblSettings.setBounds(10, 11, 500, 29);
			contentPane.add(lblSettings);

			JLabel lblMute = new JLabel("Stop Music");
			lblMute.setFont(new Font("Arial", Font.PLAIN, 18));
			lblMute.setBounds(25, 74, 135, 20);
			contentPane.add(lblMute);
			
			//This buttons stops and starts background music
			JToggleButton toggleButton = new JToggleButton("");
			toggleButton.setIcon(new ImageIcon("mute2.png"));
			toggleButton.setBounds(220, 60, 57, 40);
			if(! _sound.isActive()) {
				toggleButton.setSelected(true);
			}
			toggleButton.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent ev) {
					if(ev.getStateChange()==ItemEvent.SELECTED){
						_sound.stop();
					} else if(ev.getStateChange()==ItemEvent.DESELECTED){
						_sound.loop();
					}

				}
			});
			contentPane.add(toggleButton);

			//Upon pressing the ok button. The voice is changed.
			JButton btnOK = new JButton("Ok");
			btnOK.setBackground(new Color(255,255,50));
			btnOK.setBorder(new MatteBorder(1,1,1,1, new Color(0,0,0)));
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String data = (String) comboBox.getItemAt(comboBox.getSelectedIndex());
					if (data.equals("New Zealander"))
						_voice = _voices.get(0);
					else if (data.equals("American"))
						_voice = _voices.get(1);
					else 
						_voice = _voices.get(2);
					dispose();
				}
			});
			btnOK.setBounds(350, 170, 89, 40);
			contentPane.add(btnOK);
		}
	}

}
