package whypro.memorize.managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
		int index = random.nextInt(indexBound);
		
		FileReader fr = new FileReader(thesPath);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();// 词库名称
		
		// 跳过前面的内容
		for (int i = 0; i < 4 * index; i++) {
			line = br.readLine();
		}
		// TODO: 有越界的 BUG
		// 定位到单词条目开始处
		while (line != null && !line.equals("")) {
			line = br.readLine();
		}
		
		do {
			line = br.readLine();
		}
		while (line != null && line.equals(""));
		
		ArrayList<String> wordItem = new ArrayList<String>();
		// 直到单词条目结束
		while (line !=null && !line.equals("")) {
			wordItem.add(line);
			line = br.readLine();
		}
		
		Word word = null;
		switch(wordItem.size()) {
		case 2:
			word = new Word();
			word.name = wordItem.get(0);
			word.phonetic = " ";
			word.interp = wordItem.get(1);
			break;
		case 3:
			word = new Word();
			word.name = wordItem.get(0);
			word.phonetic = wordItem.get(1);
			word.interp = wordItem.get(2);
			break;
		default:
			break;
		}
		
		br.close();
		fr.close();
		
		return word;
	}
	
	public static Word getWordByName(String name) throws FileNotFoundException, IOException {
		String thesDir = "./thesaurus/";
		String[] thesList = {"CET4.txt", "CET6.txt", "GRE.txt", "TOFEL.txt", "研究生入学考试词汇.txt"};
		Word word = null;
		for (int i = 0; i < thesList.length; i++) {
			FileReader fr = new FileReader(thesDir + thesList[i]);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();	// 词库名称
			
			while (line != null) {
				line = br.readLine();
				if (name.equals(line)) {
					String phonetic = br.readLine();
					String interp = br.readLine();
					word = new Word(name, phonetic, interp);
					break;
				}
			}
		}
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
