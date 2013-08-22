package whypro.memorize;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.util.Vector;

class WordReview {
	Vector<ReciteRecord> recordsVector = new Vector<ReciteRecord>();
	
	WordReview() {
		recordsVector = RRecordGetter.loadRRecords("./record/recite.rec", 100);
	}
	
	public Word getRandomWord() {
		Random random = new Random(System.currentTimeMillis());
		int index = Math.abs(random.nextInt() % recordsVector.size());
		
		ReciteRecord record = recordsVector.elementAt(index);

		return null;
	}
}