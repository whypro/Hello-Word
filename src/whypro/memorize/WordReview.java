package whypro.memorize;

import java.util.Vector;

class WordReview {
	Vector<ReciteRecord> recordsVector = new Vector<ReciteRecord>();
	
	WordReview() {
		recordsVector = RRecordGetter.loadRRecords("./record/recite.rec", 100);
	}
}