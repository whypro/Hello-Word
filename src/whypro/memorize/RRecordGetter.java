// RRecordGetter.java
// 取得一条记忆记录
package whypro.memorize;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

class RRecordGetter {
	public static boolean saveRRecords(
			String filePath, Vector<ReciteRecord> vect) {
		try {
			ObjectOutputStream outputStream = 
				new ObjectOutputStream(
						new FileOutputStream(filePath));

			for (ReciteRecord i : vect) {
				outputStream.writeUTF(i.word);
				outputStream.writeLong(i.startDate);
				outputStream.writeLong(i.lastDate);
				outputStream.writeInt(i.stage);
				outputStream.writeInt(i.strange);	
			}
			outputStream.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}

		return true;
	}
	public static Vector<ReciteRecord> loadRRecords(
			String filePath, int n) {

		Vector<ReciteRecord> vect = new Vector<ReciteRecord>();

		try {
			ObjectInputStream inputStream = 
				new ObjectInputStream(
						new FileInputStream(filePath));


			for (int i = 0; i < n; ++i) {
				ReciteRecord temp = new ReciteRecord();
				temp.word = inputStream.readUTF();
				temp.startDate = inputStream.readLong();
				temp.lastDate = inputStream.readLong();
				temp.stage = inputStream.readInt();
				temp.strange = inputStream.readInt();	
				vect.addElement(temp);
			}

			inputStream.close();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return vect;
	}
}