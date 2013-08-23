// ReciteStat.java
package whypro.memorize.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import whypro.memorize.managers.ReciteRecordManager;
import whypro.memorize.models.ReciteRecord;

public class ReciteStatUI extends JFrame {

	private static final long serialVersionUID = 7247900451593318205L;
	
	final JTable table;
	Vector<ReciteRecord> recordsVector;
	
	public ReciteStatUI(String recordPath) {
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		try {
			recordsVector = (new ReciteRecordManager(recordPath)).getReciteRecords();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(-1);
		}

		Object[][] rowData = loadReciteRecords();
		String[] names = {"单词", "首次记忆时间", "上次记忆时间", "记忆阶段", "陌生度"}; 
		
		table = new JTable(rowData, names) {
			// 将词汇统计表格控件设为只读
			public boolean isCellEditable(int row, int column) {
				return false;  
			}
		};

		mainPanel.add(new JScrollPane(table));
		
		this.add(mainPanel);
		// 
		this.setTitle("背诵情况统计");
		this.setSize(600, 400);
		this.setResizable(false);

		// 使窗口居中
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width / 2; // 获取屏幕的宽
		int screenHeight = screenSize.height / 2; // 获取屏幕的高
		int height = this.getHeight();
		int width = this.getWidth();
		setLocation(screenWidth - width / 2, screenHeight - height / 2);

		this.setVisible(true);
	}
	
	private Object[][] loadReciteRecords() {
		Object[][] obj = new Object[recordsVector.size()][5];
		int j = 0;
		String str;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		for (ReciteRecord i : recordsVector) {
			obj[j][0] = i.word;
			str = format.format(new Date(i.startDate));
			obj[j][1] = str;
			str = format.format(new Date(i.lastDate));
			obj[j][2] = str;
			obj[j][3] = i.stage;
			obj[j][4] = i.strange;
			++j;
		}
		return obj;
	}

}