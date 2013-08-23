package whypro.memorize.managers;

import java.io.FileNotFoundException;
import java.io.IOException;

import whypro.memorize.models.ReciteRecord;
import whypro.memorize.models.Word;

public class ReciteManager {
	public enum Modes {NEW, REVIEW};
	private Modes reciteMode = Modes.NEW;
	
	private Word currentWord;
	private int strange = 0;
	
	private String thesName;
	private WordManager wordManager;
	private ReciteRecordManager reciteRecordManager;
	
	public ReciteManager(String recordPath) throws IOException {
		reciteRecordManager = new ReciteRecordManager(recordPath);
	}

	// 陌生度++
	public int increaseStrange() {
		return ++strange;
	}
	
	// 陌生度清零
	public void clearStrange() {
		strange = 0;
	}
	
	// 返回词库名
	public String getThesaurusName() {
		return thesName;
	}
	
	// 返回记忆模式
	public Modes getReciteMode() {
		return reciteMode;
	}
	
	// 设置记忆模式
	public void setReciteMode(Modes mode) {
		reciteMode = mode;
	}
	
	// 设置词库
	public void setThesaurus(String thesPath) throws IOException {
		wordManager = new WordManager(thesPath);
		thesName = wordManager.getThesaurusName();
	}
	
	public Word getWord() {
		return currentWord;
	}
	
	public Word nextWord() throws IOException {

		switch (reciteMode) {
		case NEW:
			//WordManager wordManager = new WordManager();
			break;
		case REVIEW:
			//WordManager wordManager = new WordManager();
			break;
		}
		currentWord = wordManager.getRandomWord();
		strange = 0;
		return currentWord;
	}
	
	public boolean saveReciteRecord() throws FileNotFoundException, IOException {
		ReciteRecord reciteRecord = new ReciteRecord(
				currentWord.name, 
				System.currentTimeMillis(), 	// 首次记忆时间
				System.currentTimeMillis(), 	// 上次记忆时间
				0, 								// 阶段
				strange							// 陌生度
				);
		reciteRecordManager.saveReciteRecord(reciteRecord);
		
		return false;
	}

	
	
}