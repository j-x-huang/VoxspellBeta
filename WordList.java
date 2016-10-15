package beta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Random;


//Written by Injae Park
public class WordList implements Serializable{

    private static final long serialVersionUID = 1L;
	private HashMap<Integer, ArrayList<Word>> _levelMap;;
	private ArrayList<Integer> _levelArray;

	private int _level;
	private File _file;
	private File _saveFile = new File(".save.ser");

	//This constructor is for normal quiz.
	public WordList(File file) throws IOException{
		
		_file=file;
		if (_saveFile.exists()) {
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(_saveFile));
				WordList wl = (WordList)ois.readObject();
				_levelMap = wl.getLevelMap();
				_levelArray = wl.getLevelArray();
                ois.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}

		} else {
			_levelMap = new HashMap<Integer, ArrayList<Word>>();
			_levelArray = new ArrayList<Integer>();
			BufferedReader wordlist = new BufferedReader(new FileReader(file));

			String line;
			int a=0;
			//Reading word where and adding it to arrayList if it is not an empty line
			while ((line = wordlist.readLine()) != null){
				if(!"".equals(line.trim())){
					String[] lines = line.split(" ");
					if(lines[0].equals("%Level")){
						_level = Integer.parseInt(lines[1]); //use level as key for the hashtable
					} else if (line.equals("0")){ //stop if it sees a sero
						break;
					} else {
						if (_levelMap.containsKey(_level)) {
							_levelMap.get(_level).add(new Word(line));
						} else {
							ArrayList<Word> tempList = new ArrayList<Word>();
							tempList.add(new Word(line));
							_levelMap.put(_level, tempList);
							_levelArray.add(_level);
						}
					}
				}
			}
		}
	}

	/*
	 * This constructor is for making wordlist for review function.
	 */
	/*
	public WordList(String file, int level) throws IOException{

		BufferedReader wordlist = new BufferedReader(new FileReader("."+File.separator+file+level));

		String line;
		int a=0;
		//Reading word where and adding it to arrayList if it is not an empty line
		while ((line = wordlist.readLine()) != null){
			if(!"".equals(line.trim())){
				_wordList.add(line);
			}
		}
		_failed = true;
}
	 */

	//Return wordCount for each level.
	public int getWordCount(int level){
		return _levelMap.get(level).size();

		/*
		if(_failed){
			LinkedHashSet<String> temp = new LinkedHashSet<String>(_wordList);
			return temp.size();
		}
		if (level == 11) {
			return _wordList.size() -1 - pos[level-1];
		} 
		return pos[level]-1-pos[level-1];
		 */

	}

	//Get random word from the arrayList
	public Word getRandomWord(int level){
		Random random = new Random();
		//Getting random position
		/*
		if(_failed){
			int rand = Math.abs(r.nextInt()) % testList.size();
			return testList.get(rand);
		} */
		int rand = Math.abs(random.nextInt()) % this.getWordCount(level);
		return _levelMap.get(level).get(rand);
	}

	//Sort the list and return the word at the position specified.
	/*
	public String getSortedWord(int a){
		Collections.sort(_wordList);
		return _wordList.get(a);
	}
	 */

	//Create a list of words to be tested.
	public ArrayList<Word> createTestList(int level, int maxWords){
		//Store only unique values.
		LinkedHashSet<Word> list = new LinkedHashSet<Word>();
		int wordCount = getWordCount(level);
		while(list.size()<maxWords||list.size()<wordCount){
			list.add(getRandomWord(level));
			if(list.size()==wordCount)
				break;
		}

		ArrayList<Word> testList = new ArrayList<Word>();
		testList.addAll(list);
		return testList;
	}
	//get the levels in the wordlist as an array of integers
	public int[] getLevels() {
		int size = _levelArray.size();
		int[] array = new int[size];

		for (int i = 0; i < size; i++) {
			array[i] = _levelArray.get(i);
		}

		return array;

	}
	//get if the level after a certain level (if possible)
	public int getNextLevel(int level) {
		int index = _levelArray.indexOf(level);

		if (index + 1 == _levelArray.size()) {
			return level;
		} else {
			return _levelArray.get(index + 1);
		}
	}
	//get filename of wordlist
	public String getFileName() {
		return _file.getName();
	}

	public void saveData() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(_saveFile));
			oos.writeObject(this);
			oos.flush();
			oos.close();
		} catch (IOException e) {

		}
	}

	public HashMap<Integer, ArrayList<Word>> getLevelMap() {
		return this._levelMap;
	}

	public ArrayList<Integer> getLevelArray() {
		return this._levelArray;
	}
	
	public int getLevelLength(int level) {
		return _levelMap.get(level).size();
	}
	
	public void clearInfo() {
		_levelArray.clear();
		_levelMap.clear();
	}


}
