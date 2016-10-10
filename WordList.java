package beta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Random;


//Written by Injae Park
public class WordList {
	
	private ArrayList<String> _wordList=new ArrayList<String>();
	
	private HashMap<Integer, ArrayList<String>> _levelMap = new HashMap<Integer, ArrayList<String>>();
	private ArrayList<Integer> _levelArray = new ArrayList<Integer>();
	
	private int[] pos = new int[11];
	private int _level;
	private boolean _failed = false;
	private File _file;
	
	//This constructor is for normal quiz.
	public WordList(File file) throws IOException{
			_file = file;
			BufferedReader wordlist = new BufferedReader(new FileReader(file));
			
			String line;
			int a=0;
			//Reading word where and adding it to arrayList if it is not an empty line
			while ((line = wordlist.readLine()) != null){
				if(!"".equals(line.trim())){
					String[] lines = line.split(" ");
					if(lines[0].equals("%Level")){
						_level = Integer.parseInt(lines[1]);
					} else if (line.equals("0")){
						break;
					} else {
						if (_levelMap.containsKey(_level)) {
							_levelMap.get(_level).add(line);
						} else {
							ArrayList<String> tempList = new ArrayList<String>();
							tempList.add(line);
							_levelMap.put(_level, tempList);
							_levelArray.add(_level);
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
	public String getRandomWord(int level){
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
	public ArrayList<String> createTestList(int level, int maxWords){
		//Store only unique values.
		LinkedHashSet<String> list = new LinkedHashSet<String>();
		int wordCount = getWordCount(level);
		while(list.size()<maxWords||list.size()<wordCount){
			list.add(getRandomWord(level));
			if(list.size()==wordCount)
				break;
		}
		
		ArrayList<String> testList = new ArrayList<String>();
		testList.addAll(list);
		return testList;
	}
	
	public int[] getLevels() {
		int size = _levelArray.size();
		int[] array = new int[size];
		
		for (int i = 0; i < size; i++) {
			array[i] = _levelArray.get(i);
		}
		
		return array;
		
	}
	
	public int getNextLevel(int level) {
		int index = _levelArray.indexOf(level);
		
		if (index + 1 == _levelArray.size()) {
			return level;
		} else {
			return _levelArray.get(index + 1);
		}
	}
	
	public String getFileName() {
		return _file.getName();
	}
	
}
