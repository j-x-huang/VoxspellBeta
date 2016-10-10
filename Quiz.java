package beta;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import beta.ViewAccuracy;

public class Quiz extends JPanel implements ActionListener {
	private JTextField textField;
	private int _level;
	private int _testNo=1;
	private int _attempts;
	private int _fails;
	private int _maxNum;
	protected ArrayList<String> _voices;
	private ArrayList<String> _testList = new ArrayList<String>();
	private String _voice;
	private JComboBox<String> _selectVoices;
	private JPanel _main;
	private int _wc;
	private int _testNum;
	private int _coins = 0;
	private int _streak = 0;

	private JButton btnListenAgain = new JButton("Listen Again");
	private JButton btnSubmit = new JButton("Submit");
	private JButton btnStatistics = new JButton("Statistics");
	private JButton btnSettings = new JButton("");

	private JLabel lblCoin = new JLabel("Coins: 0");
	private JLabel lblPleaseSpellWord = new JLabel("Please spell word 1 of 3:");
	private JLabel lblAcc = new JLabel("Accuracy: 0/10");
	private JLabel lblCorrect = new JLabel("Oh hello there!");
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
		lblQuiz.setBounds(0, 11, 464, 57);
		this.add(lblQuiz);

		//Level Subtitle
		JLabel lblLevel = new JLabel("Level " + _level);
		lblLevel.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblLevel.setHorizontalAlignment(SwingConstants.CENTER);
		lblLevel.setBounds(10, 65, 454, 26);

		this.add(lblLevel);

		//Setting up GUI features
		lblPleaseSpellWord.setFont(new Font("Calibri", Font.PLAIN, 18));
		lblPleaseSpellWord.setBounds(20, 130, 187, 37);
		this.add(lblPleaseSpellWord);

		textField = new JTextField();
		textField.setFont(new Font("Calibri", Font.PLAIN, 12));
		textField.setBounds(20, 172, 410, 26);
		this.add(textField);
		textField.setColumns(10);

		btnSubmit.setBounds(341, 209, 89, 23);
		this.add(btnSubmit);

		btnListenAgain.setBounds(205, 209, 126, 23);
		this.add(btnListenAgain);

		lblAcc.setFont(new Font("Calibri", Font.PLAIN, 15));
		lblAcc.setBounds(334, 270, 130, 37);
		this.add(lblAcc);

		btnSettings.setIcon(new ImageIcon("gear_ss.png"));
		btnSettings.setBounds(419, 399, 45, 41);
		btnSettings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Settings set = new Settings();
				set.setVisible(true);
			}
		});
		this.add(btnSettings);

		lblCoin.setFont(new Font("Calibri", Font.PLAIN, 15));
		lblCoin.setIcon(new ImageIcon("Coin1.png"));
		lblCoin.setBounds(334, 309, 140, 37);
		this.add(lblCoin);

		lblStreak.setFont(new Font("Calibri", Font.PLAIN, 15));
		lblStreak.setBounds(300, 309, 70, 37);
		this.add(lblStreak);

		JLabel beelbl = new JLabel("");
		beelbl.setIcon(new ImageIcon("bee_h.png"));
		beelbl.setBounds(307, 27, 157, 134);
		this.add(beelbl);

		JLabel lblSpeech1 = new JLabel("");
		lblSpeech1.setIcon(new ImageIcon("test.png"));
		lblSpeech1.setBounds(17, 110, 320, 69);
		this.add(lblSpeech1);

		btnStatistics.setFont(new Font("Calibri", Font.PLAIN, 18));
		btnStatistics.setBounds(294, 399, 115, 41);
		this.add(btnStatistics);

		JLabel lblSBee = new JLabel("");
		lblSBee.setIcon(new ImageIcon("sbee2.png"));
		lblSBee.setBounds(10, 364, 81, 76);
		this.add(lblSBee);

		lblCorrect.setBounds(115, 268, 150, 41);
		lblCorrect.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblCorrect);

		JLabel lblSpeech = new JLabel("");
		lblSpeech.setIcon(new ImageIcon("speech2.png"));
		lblSpeech.setBounds(85, 234, 205, 176);
		this.add(lblSpeech);


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
		btnStatistics.addActionListener(this);

		_frame.getRootPane().setDefaultButton(btnSubmit); //make submit default button
		//Word to be tested
		setTestList(_wordlist);
		festival("Spell " + _testList.get(_testNo-1));
	}

	public void actionPerformed(ActionEvent e) {
		//Getting the word that user wrote
		String word = textField.getText();

		try{
			//If user pressed speak button,  the word
			//is spoken by festival.
			JButton button = (JButton) e.getSource();  
			if (button.equals(btnListenAgain)){  

				festival(_testList.get(_testNo-1));
				return;
			}else if (button.equals(btnStatistics)){ //makes statistics on a separate panel
				makeTable();
				return;
			}
			//If user is correct
			if(_testList.get(_testNo-1).equalsIgnoreCase(word)){
				//Showing and telling correct message
				Sound sound = new Sound("cheering.wav");
				sound.play();
				
				lblCorrect.setText("Correct!!");

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
				} else if (_streak > 2) {
					_coins+=20;
					_hiddenCoins+=20;
					lblStreak.setText("+20");
				} else {
					_coins+=10;
					_hiddenCoins+=10;
					lblStreak.setText("+10");
				}
				blink(); //flash text
				updateCoins();
				lblCoin.setText("Coins: "+ _coins);

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
					festival("Incorrect!! Spell"+_testList.get(_testNo-1)+".");
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

					_streak = 0; // streak resets
					//Changing field as needed

					_testNo++;
					incorrect =0;

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
				lblCorrect.setText(lblCorrect.getText()+" Quiz Finished!!");
				festival(lblCorrect.getText());
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
				festival(lblCorrect.getText()+" Spell "+_testList.get(_testNo-1)+".");
			}
		}catch(Exception excep){
			excep.printStackTrace();
		}
	}

	//Function that allows user to change the voice of festival.
	private void getVoices() throws Exception{

		Festival f = new Festival("","");
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
	private void festival(String tts) throws Exception{
		Festival say = new Festival(tts,_voice);
		say.execute();

	}

	//Speaking out the spelling of the word passed into it
	private void festivalAlphabet(String tts) throws Exception{
		String word="";
		String[] alpha = tts.split("");

		for(int i=0;i<alpha.length;i++)
			word = word + alpha[i]+" ";
		festival(word);
	}

	/*
	//Setting the word to be tested using getRandomWord method from word list
	private void setWord() throws IOException{
		WordList wordlist = new WordList(_file);
		_testWord = wordlist.getRandomWord(1);	
	}
	 */

	private void setTestList(WordList wordlist) throws IOException{
		_testList = wordlist.createTestList(_level,_maxNum);	
	}

	//Obtains the accuracy values from the save file (for the current quiz level)
	// then add the levels to the corresponding fields
	private void getAccuracy() throws IOException {
		File accuracy = new File(".accuracy_" + _level);
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
		File accuracy = new File(".accuracy_" + _level);

		PrintWriter pw = new PrintWriter(accuracy);
		pw.close();

		FileWriter fw = new FileWriter(accuracy);
		BufferedWriter bw = new BufferedWriter(fw);

		bw.write(_attempts + "\n");
		bw.write(_fails + "\n");
		bw.write(_highScore + "\n");
		bw.close();
	}
	/*
	//Putting Failed word into failed list
	private void failed() throws IOException{
		File failed = new File(".failed"+_level);
		//If file does not exist, create new file
		if(!failed.exists()) {
			failed.createNewFile();
		} 

		//Appending the word to the file
		Writer output;
		output = new BufferedWriter(new FileWriter(failed,true)); 
		output.append(_testList.get(_testNo-1)+"\n");
		output.close();
	}

	private void removeFailed(String word) throws IOException{
		File failed = new File(".failed"+_level);
		//If file does not exist, create new file
		if(!failed.exists()) {
			failed.createNewFile();
		} 
		//Creating temporary file to store data
		File temp = new File(".temp");
		if(!temp.exists())
			temp.createNewFile();

		//Choosing input and output files
		BufferedReader input = new BufferedReader(new FileReader("."+File.separator+failed));
		PrintWriter output= new PrintWriter(new FileWriter("."+File.separator+temp));

		String line;

		//Reading word where and adding it to arrayList if it is not an empty line
		while ((line = input.readLine()) != null){
			//If the line does not equal to line to remove, it is copied to temp file
			if(!word.equalsIgnoreCase(line.trim())){
				output.println(line);
				output.flush();
			}	
		}

		//Closing input output streams
		input.close();
		output.close();

		//Delete orginal file
		failed.delete();

		//Changing output file to be the failed list file.
		temp.renameTo(failed);
	}

	//Adding failed word to the failed_total list
	private void failedTotal() throws IOException{
		File failed = new File(".failed_total"+_level);
		//If file does not exist, create new file
		if(!failed.exists()) {
			failed.createNewFile();
		} 

		//Appending the word to the file
		Writer output;
		output = new BufferedWriter(new FileWriter(failed,true)); 
		output.append(_testList.get(_testNo-1)+"\n");
		output.close();
	}
	*/

	private void makeTable() {
		ViewAccuracy va = new ViewAccuracy(_wordlist, this);

		_frame.getContentPane().add(va);
		this.setVisible(false);
		va.setVisible(true);
	}

	private void blink() {

		_timer = new Timer(50, new TimerListener());
		_timer.start();


	}

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
	public class Settings extends JFrame {

		private JPanel contentPane;

		/**
		 * Create the frame.
		 */
		public Settings() {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 410, 200);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);

			JLabel lblChangeVoice = new JLabel("Change Voice");
			lblChangeVoice.setBounds(10, 99, 135, 24);
			contentPane.add(lblChangeVoice);

			String[] voices = _voices.toArray(new String[_voices.size()]);

			final JComboBox<String> comboBox = new JComboBox<String>(voices);
			comboBox.setBounds(182, 101, 196, 20);
			contentPane.add(comboBox);

			JLabel lblSettings = new JLabel("Settings");
			lblSettings.setHorizontalAlignment(SwingConstants.CENTER);
			lblSettings.setFont(new Font("Calibri Light", Font.PLAIN, 25));
			lblSettings.setBounds(10, 11, 374, 29);
			contentPane.add(lblSettings);

			JLabel lblMute = new JLabel("Stop Music");
			lblMute.setBounds(10, 74, 135, 20);
			contentPane.add(lblMute);

			JToggleButton toggleButton = new JToggleButton("");
			toggleButton.setIcon(new ImageIcon("mute2.png"));
			toggleButton.setBounds(182, 50, 57, 40);
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

			JButton btnOK = new JButton("Ok");
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String data = (String) comboBox.getItemAt(comboBox.getSelectedIndex());
					_voice = data;
					dispose();
				}
			});
			btnOK.setBounds(289, 132, 89, 23);
			contentPane.add(btnOK);
		}
	}

}
