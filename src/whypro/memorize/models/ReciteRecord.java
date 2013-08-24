package whypro.memorize.models;

public class ReciteRecord {
    
	public String word;
	public long startTime;
	public long lastTime;
	public int stage;
	public int strange;
	
	public ReciteRecord() {
		
	}
	
	public ReciteRecord(
			String word, 
			long startTime, long lastTime, 
			int stage, int strange) {
		this.word = word;
		this.startTime = startTime;
		this.lastTime = lastTime;
		this.stage = stage;
		this.strange = strange;
	}
}