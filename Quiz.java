package beta;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
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
	private File _file;
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
	private JLabel lblCorrect = new JLabel("Hello there, I will give you feedback on your test");
	private int _correct=0;
	private int incorrect;

	private JFrame _frame;


	/**
	 * Create the panel.
	 */
	public Quiz(File file,int level, JFrame frame, int maxNum) throws  Exception {
		_file=file;
		_level = level;
		_frame = frame;
		_maxNum = maxNum;
		getAccuracy();

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


		lblPleaseSpellWord.setFont(new Font("Calibri", Font.PLAIN, 18));
		lblPleaseSpellWord.setBounds(20, 130, 178, 37);
		this.add(lblPleaseSpellWord);

		textField = new JTextField();
		textField.setFont(new Font("Calibri", Font.PLAIN, 12));
		textField.setBounds(20, 172, 410, 26);
		this.add(textField);
		textField.setColumns(10);

		btnSubmit.setBounds(341, 209, 89, 23);
		this.add(btnSubmit);

		btnListenAgain.setBounds(220, 209, 111, 23);
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

		JLabel beelbl = new JLabel("");
		beelbl.setIcon(new ImageIcon("bee_h.png"));
		beelbl.setBounds(307, 27, 157, 134);
		this.add(beelbl);

		JLabel lblSpeech1 = new JLabel("");
		lblSpeech1.setIcon(new ImageIcon("test.png"));
		lblSpeech1.setBounds(17, 110, 320, 69);
		this.add(lblSpeech1);

		btnStatistics.setFont(new Font("Calibri", Font.PLAIN, 18));
		btnStatistics.setBounds(304, 399, 105, 41);
		this.add(btnStatistics);

		JLabel lblSBee = new JLabel("");
		lblSBee.setIcon(new ImageIcon("sbee2.png"));
		lblSBee.setBounds(10, 364, 81, 76);
		this.add(lblSBee);

		lblCorrect.setBounds(163, 268, 69, 41);
		this.add(lblCorrect);

		JLabel lblSpeech = new JLabel("");
		lblSpeech.setIcon(new ImageIcon("speech2.png"));
		lblSpeech.setBounds(85, 234, 205, 176);
		this.add(lblSpeech);


		//Getting the words and doing the Please Spell Stuff

		WordList wordlist = null;
		//Creating the wordlist using the file name
		wordlist = new WordList(_file);

		_wc = wordlist.getWordCount(_level);

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
		btnListenAgain.addActionListener(this);
		btnSubmit.addActionListener(this);
		btnStatistics.addActionListener(this);

		_frame.getRootPane().setDefaultButton(btnSubmit);
		//Word to be tested
		setTestList(wordlist);
		festival(_testList.get(_testNo-1));
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
				//if spelling is pressed, the alphabet of the word being tested  is spoken
			}else if (button.equals(btnStatistics)){
				makeTable();
				return;
			}
			//If user is correct
			if(_testList.get(_testNo-1).equalsIgnoreCase(word)){
				//Showing and telling correct message
				lblCorrect.setText("Correct!!");

				//Remove word from failed test list
				removeFailed(_testList.get(_testNo-1));


				_attempts++;
				_testNo++;
				_correct++;

				_streak++;

				if (_streak >5) {
					_coins+=50;
				} else if (_streak > 2) {
					_coins+=20;
				} else {
					_coins+=10;
				}
				lblCoin.setText("Coins: "+ _coins);

				//Setting the new label
				lblPleaseSpellWord.setText("Spell word "+(_testNo)+" of "+_maxNum+": ");
				if(_wc<_maxNum)
					lblPleaseSpellWord.setText("Spell word "+(_testNo)+" of "+ _wc+": ");


				incorrect =0;
				//If user gets incorrect
			}else{
				//If second time failing
				if(incorrect<1){
					//Setting message to the user about the fault
					lblCorrect.setText("Incorrect, please try again!!");
					festival("Incorrect!! Spell"+_testList.get(_testNo-1)+".");
					//Word is spoken again.
					incorrect++;
					textField.setText("");
					return;
					//First time failing
				}else{
					//Result message to user
					lblCorrect.setText("Failed!!");

					//increase test number and fail value
					_attempts++;
					_fails++;

					_streak = 0;
					//Adding failed word to the failed list.
					failed();
					failedTotal();

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
				//opens options menu where user can choose their next action.
				SubMenu sub = new SubMenu(_file,_level,_correct,_testNo-1, _frame , _maxNum);
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
		bw.close();
	}

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

	private void makeTable() {
		ViewAccuracy va = new ViewAccuracy();
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

	public class Settings extends JFrame {

		private JPanel contentPane;

		/**
		 * Create the frame.
		 */
		public Settings() {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 346, 215);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);

			JLabel lblChangeVoice = new JLabel("Change Voice");
			lblChangeVoice.setBounds(10, 99, 85, 24);
			contentPane.add(lblChangeVoice);

			String[] voices = _voices.toArray(new String[_voices.size()]);

			final JComboBox<String> comboBox = new JComboBox<String>(voices);
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
			toggleButton.setIcon(new ImageIcon("mute2.png"));
			toggleButton.setBounds(118, 50, 57, 40);
			contentPane.add(toggleButton);

			JButton btnOK = new JButton("Ok");
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String data = (String) comboBox.getItemAt(comboBox.getSelectedIndex());
					_voice = data;
					dispose();
				}
			});
			btnOK.setBounds(215, 132, 89, 23);
			contentPane.add(btnOK);
		}
	}

}
