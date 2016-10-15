package beta;

import java.io.Serializable;

public class Word implements Serializable{
	private String _word;
	private int _correct;
	private int _fails;
	private int _attempts;
    private static final long serialVersionUID = 1L;
	
	public Word(String word) {
		_word = word;
		
		_correct = 0;
		_fails = 0;
		_attempts = 0;
	}
	
	public void increaseCorrect() {
		_correct++;
		_attempts++;
		
	}
	
	public void increaseFails() {
		_fails++;
		_attempts++;
	}
	
	public int getCorrect() {
		return _correct;
	}
	
	public int getFails() {
		return _fails;
	}
	
	public int getAttempts() {
		return _attempts;
	}
	
	public String toString() {
		return _word;
	}
}
