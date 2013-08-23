package whypro.memorize.models;

public class ReciteRecord {
    
	public String word;
	public long startDate;
	public long lastDate;
	public int stage;
	public int strange;
	
	public ReciteRecord() {
		
	}
	
	public ReciteRecord(
			String word, 
			long startDate, long lastDate, 
			int stage, int strange) {
		this.word = word;
		this.startDate = startDate;
		this.lastDate = lastDate;
		this.stage = stage;
		this.strange = strange;
	}
}