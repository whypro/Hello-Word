package whypro.memorize.managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import whypro.memorize.models.Word;


public class WordManager {
	private String thesPath;
	private String thesName;
	private int indexBound;	// 词库中记录总条数
	
	public WordManager(String thesPath) throws IOException {
		this.thesPath = thesPath;
		calcIndexBound();
	}
	
	// 从词库中随机去取出单词
	public Word getRandomWord() throws IOException {
		Random random = new Random(System.currentTimeMillis());
		int index = Math.abs(random.nextInt() % indexBound);
		
		FileReader fr = new FileReader(thesPath);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();// 词库名称
		
		for (int i = 0; i < 4 * index; i++) {
			line = br.readLine();
		}
		while (!line.equals("")) {
			line = br.readLine();
		}
		
		Word word = new Word();
		word.name = br.readLine();
		word.phonetic = br.readLine();
		word.interp = br.readLine();
		
		br.close();
		fr.close();
		
		return word;
	}
	
	// 返回词库名称
	public String getThesaurusName() {
		return thesName;
	}
	
	// 计算词库的单词条数
	private void calcIndexBound() throws IOException {
		FileReader fr = new FileReader(thesPath);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();	// 词库名称
		thesName = line;
		
		int indexBound = 0;
		while (line != null) {
			if (line.equals("")) {
				indexBound++;
			}
			line = br.readLine();
		}
		this.indexBound = indexBound;
		br.close();
		fr.close();
	}
}
