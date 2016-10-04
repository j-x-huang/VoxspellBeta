package beta;

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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Quiz extends JPanel implements ActionListener {
	private JTextField textField;
	private int _level;
	private int _testNo=1;
	private int _attempts;
	private int _fails;
	private int _maxNum;
	private ArrayList<String> _voices;
	private ArrayList<String> _testList = new ArrayList<String>();
	private String _voice;
	private JComboBox<String> _selectVoices;
	private JPanel _main;
	private String _file;
	private int _wc;
	private int _testNum;

	private JButton btnListenAgain = new JButton("Listen Again");
	private JButton btnSubmit = new JButton("Submit");
	private JButton btnStatistics = new JButton("Statistics");
	private JButton btnSettings = new JButton("");

	private JLabel lblCoin = new JLabel("Coins: 20");
	private JLabel lblPleaseSpellWord = new JLabel("Please spell word 1 of 3:");
	private JLabel lblAcc = new JLabel("Accuracy: 0/10");
	private JLabel lblCorrect = new JLabel("Correct");
	private int _correct=0;
	private int incorrect;
	
	private JFrame _frame;


	/**
	 * Create the panel.
	 */
	public Quiz(String file,int level, JFrame frame) throws  Exception {
		_file=file;
		_level = level;
		_frame = frame;

		getAccuracy();

		_selectVoices = selectVoice();
		_voice = _voices.get(0);

		this.setBackground(new Color(255, 255, 153));
		this.setLayout(null);

		JLabel lblQuiz = new JLabel("Quiz");
		lblQuiz.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuiz.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		lblQuiz.setBounds(0, 11, 464, 57);
		this.add(lblQuiz);

		//Level Subtile
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
		this.add(btnSettings);

		lblCoin.setFont(new Font("Calibri", Font.PLAIN, 15));
		lblCoin.setIcon(new ImageIcon("Coin1.png"));
		lblCoin.setBounds(334, 309, 99, 37);
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
		if(file.equals("NZCER-spelling-lists.txt")){
			wordlist = new WordList(_file);
			_maxNum = 10;
		}
		_wc = wordlist.getWordCount(level);

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

		//Word to be tested
		setTestList(wordlist);
		festival(_testList.get(_testNo-1));
	}

	public void actionPerformed(ActionEvent e) {
		//Getting the word that user wrote
		String word = textField.getText();

		if(e.getSource().equals(_selectVoices)){
			String data = (String) _selectVoices.getItemAt(_selectVoices.getSelectedIndex());
			_voice = data;
			return;
		}
		try{
			//If user pressed speak button,  the word
			//is spoken by festival.
			JButton button = (JButton) e.getSource();  
			if (button.equals(btnListenAgain)){  

				festival(_testList.get(_testNo-1));
				return;
				//if spelling is pressed, the alphabet of the word being tested  is spoken
			}else if (button.equals(btnStatistics)){
				//_main.makeTable();
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
				SubMenu sub = new SubMenu(_file,_level,_correct,_testNo-1, _frame );
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
	private JComboBox<String> selectVoice() throws Exception{

		Festival f = new Festival("","");
		_voices = f.listOfVoices();

		String[] str = new String[_voices.size()];
		for(int i = 0;i<_voices.size();i++)str[i]=_voices.get(i);


		JComboBox<String> voices = new JComboBox<String>(str);


		return voices;
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

	/*
	//Method that tells u if a string contains something other than alphabet
	private boolean onlyAlphabet(String s){
		return s.matches("[a-zA-Z]+");
	}
	 */

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

}
