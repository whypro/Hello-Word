package whypro.memorize;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class WordManager {
	protected String filePath;
	protected String fileName;
	protected int index;

	protected int indexBound;
	
	protected String word;
	protected String phonetic;
	protected String interp;
	protected String thesName;
	
	public WordManager(String filePath, String fileName) throws IOException {
		this.filePath = filePath;
		this.fileName = fileName;
		calcIndexBound();
	}
	
	
	public void getWordAndInterp() throws IOException {
		Random random = new Random(System.currentTimeMillis());
		this.index = Math.abs(random.nextInt() % indexBound);
		
		FileReader fr = new FileReader(filePath + '/' + fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();// ´Ê¿âÃû³Æ
		thesName = line;
		for (int i = 0; i < 4 * index; i++) {
			line = br.readLine();
		}
		while (!line.equals("")) {
			line = br.readLine();
		}
		
		this.word = br.readLine();
		this.phonetic = br.readLine();
		this.interp = br.readLine();
		br.close();
		fr.close();
	}
	
	public void calcIndexBound() throws IOException {
		FileReader fr = new FileReader(filePath + '/' + fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();	// ´Ê¿âÃû³Æ

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

