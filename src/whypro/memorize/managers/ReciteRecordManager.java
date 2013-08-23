// ReviewManager.java

package whypro.memorize.managers;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import whypro.memorize.models.ReciteRecord;

public class ReciteRecordManager {
	private Vector<ReciteRecord> reciteRecords = new Vector<ReciteRecord>();
	private String recordPath;
	
	public ReciteRecordManager(String recordPath) throws IOException {
		this.recordPath = recordPath;
		loadReciteRecords();
	}
	
	public void saveAllReciteRecords() throws FileNotFoundException, IOException {
		ObjectOutputStream outputStream = 
			new ObjectOutputStream(
					new FileOutputStream(recordPath));

		for (ReciteRecord r : reciteRecords) {
			outputStream.writeUTF(r.word);
			outputStream.writeLong(r.startDate);
			outputStream.writeLong(r.lastDate);
			outputStream.writeInt(r.stage);
			outputStream.writeInt(r.strange);	
		}
		outputStream.close();
	}
	
	private void loadReciteRecords() throws IOException {
		try {
			ObjectInputStream inputStream = 
					new ObjectInputStream(
							new FileInputStream(recordPath));
			while (true) {
				ReciteRecord temp = new ReciteRecord();
				try {
					temp.word = inputStream.readUTF();
					temp.startDate = inputStream.readLong();
					temp.lastDate = inputStream.readLong();
					temp.stage = inputStream.readInt();
					temp.strange = inputStream.readInt();	
				}
				catch (EOFException e) {
					break;
				}
				this.reciteRecords.addElement(temp);
			}
			
			inputStream.close();
		}
		catch (FileNotFoundException e){
			return;
		}
	}
	
	public Vector<ReciteRecord> getReciteRecords() {
		return reciteRecords;
	}
	
	// 保存单条记录
	public boolean saveReciteRecord(ReciteRecord record) throws FileNotFoundException, IOException {
		// 如果单词已存在于背诵记录中，则不保存
		if (!addRecord(record)) {
			return false;
		}
		saveAllReciteRecords();
		/* 追加时存在 BUG
		// 如果单词不存在于背诵记录中，追加写入
		ObjectOutputStream outputStream = 
				new ObjectOutputStream(
						new FileOutputStream(recordPath, true));
		
		outputStream.writeUTF(record.word);
		outputStream.writeLong(record.startDate);
		outputStream.writeLong(record.lastDate);
		outputStream.writeInt(record.stage);
		outputStream.writeInt(record.strange);
		
		outputStream.close();
		*/
		return true;
	}
	
	private boolean addRecord(ReciteRecord record) {
		for (ReciteRecord i : reciteRecords) {
			if (record.word.equals(i.word)) {
				return false;
			}
		}
		reciteRecords.addElement(record);
		return true;
	}
}
