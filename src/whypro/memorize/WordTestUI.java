package whypro.memorize;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Vector;

import javax.swing.*;


class WordTestUI extends JFrame implements KeyListener, ActionListener {

	private static final long serialVersionUID = 1L;

	WordManager wordManager;

	final private JPanel mainPanel;
	final private JLabel lblEnglish;
	final private JLabel lblPhonetic;
	final private JTextArea txtChinese;
	final private JLabel lblStatus;

	final private JMenuBar menuBar = new JMenuBar();
	final private JMenu fileMenu = new JMenu("文件 (F)");
	final private JMenuItem chooseThItem = new JMenuItem("选择词库 (T)");
	final private JMenuItem statItem = new JMenuItem("词汇统计 (S)");
	final private JMenuItem modeItem = new JMenuItem("复习 (R)");
	final private JMenuItem exitItem = new JMenuItem("退出 (X)");
	final private JMenu helpMenu = new JMenu("帮助 (H)");
	final private JMenuItem aboutItem = new JMenuItem("关于 (A)");

	Word currentWord = new Word();	// 当前单词
	
	String strSpelling = "";
	int wordLength;	// 单词长度
	int strange = 0;	// 陌生度

	enum Modes {NEW_WORD, REVIEW};
	Modes reciteMode = Modes.NEW_WORD;
	
			
	// 标志位，忽略一次Type事件
	boolean isCorrect = false;
	

	String fontPath = "./font";	// 字体路径
	String fontName = "TOPhonetic.ttf";
	String thesPath = "./thesaurus/TOFEL.txt";	// 词库路径
	String thesName = "";	
	
	String recordPath = "./record/recite.rec";	// 背诵记录文件路径
	
	Vector<ReciteRecord> recordsVector = new Vector<ReciteRecord>();	// 背诵记录
	
	
	public WordTestUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// 初始化菜单栏
		
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);	
		
		chooseThItem.setMnemonic('T');
		fileMenu.add(chooseThItem);
		
		statItem.setMnemonic('S');
		fileMenu.add(statItem);
		
		modeItem.setMnemonic('R');
		fileMenu.add(modeItem);
		
		exitItem.setMnemonic('X');
		fileMenu.add(exitItem);
		
		helpMenu.setMnemonic('H');
		menuBar.add(helpMenu);
		
		aboutItem.setMnemonic('A');
		helpMenu.add(aboutItem);
		
		chooseThItem.addActionListener(this);
		statItem.addActionListener(this);
		modeItem.addActionListener(this);
		exitItem.addActionListener(this);
		aboutItem.addActionListener(this);
		this.setJMenuBar(menuBar);


		lblEnglish = new JLabel(currentWord.name);
		lblEnglish.setBackground(Color.WHITE);
		// lblEnglish.setFont(new Font("Bradley Hand ITC", Font.BOLD, 50));
		lblEnglish.setFont(new Font("Arial", Font.BOLD, 50));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		mainPanel.add(lblEnglish, c);

		lblPhonetic = new JLabel(currentWord.phonetic);
		lblPhonetic.setBackground(Color.WHITE);

		// 获取音标字体
		try {
			lblPhonetic.setFont(MyFont.getFont(
					fontPath, fontName, Font.PLAIN,	20));
		} catch (FontFormatException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0.1;
		mainPanel.add(lblPhonetic, c);

		txtChinese = new JTextArea(currentWord.interp);
		txtChinese.setLineWrap(true);
		txtChinese.setFont(new Font("华文仿宋", Font.PLAIN, 28));
		//txtChinese.setBackground(Color.black);
		//txtChinese.setForeground(Color.white);

		txtChinese.setEditable(false);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1;
		c.weighty = 2;
		mainPanel.add(txtChinese, c);

		lblStatus = new JLabel("");		// 词库名称 wordManager.thesName
		// lblEnglish.setFont(new Font("Bradley Hand ITC", Font.BOLD, 50));
		lblStatus.setFont(new Font("宋体", Font.PLAIN, 12));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.weighty = 0.2;
		mainPanel.add(lblStatus, c);


		txtChinese.setFocusable(false);
		this.addKeyListener(this);
		lblEnglish.addKeyListener(this);
		lblPhonetic.addKeyListener(this);
		txtChinese.addKeyListener(this);

		// 软件运行时将背诵记录从文件载入内存
		recordsVector = RRecordGetter.loadRRecords(recordPath, 100);
		nextWord();


		this.add(mainPanel);
		// 
		this.setTitle("Hello Word");
		this.setSize(400, 400);
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

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {

		// 当拼写正确时，忽略一次键盘事件，并产生新词
		if (isCorrect) {
			isCorrect = false;
			nextWord();
			return;
		}

		char ch = e.getKeyChar();
		String word = currentWord.name;
		// 单词长度限制
		if (strSpelling.length() < word.length()) {
			// 字母限制
			if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch == '-'
				|| ch == '(' || ch == ')') {
				lblEnglish.setForeground(Color.BLACK);
				strSpelling += ch;
				lblEnglish.setText(strSpelling);
			}
		}

		// 退格键
		if (strSpelling.length() > 0) {
			if (ch == '\b') {
				strSpelling = strSpelling
				.substring(0, strSpelling.length() - 1);
				if (strSpelling.length() > 0) {
					lblEnglish.setText(strSpelling);
				} else {
					lblEnglish.setText(" ");
				}
			}
		}

		// 回车键
		if (ch == '\n' || ch == ' ') {

			if (strSpelling.equals(word)) {
				// 当拼写正确时，以蓝色字体显示，并设置isCorrect标志
				strSpelling = "";
				lblEnglish.setForeground(Color.blue);
				lblEnglish.setText(word);
				isCorrect = true;
				
				// 生成该单词背诵数据，并写入文件
				ReciteRecord record = new ReciteRecord(
						word, 
						System.currentTimeMillis(), System.currentTimeMillis(), 
						0, strange);
				recordsVector.addElement(record);
				RRecordGetter.saveRRecords(recordPath, recordsVector);
				
			} else {
				// 当拼写不正确时，以红色字体显示
				strSpelling = "";
				lblEnglish.setForeground(Color.red);
				lblEnglish.setText(word);
				// 陌生度
				strange++;
			}
		}

		// ` 键，跳过该单词
		if (ch == '`') {
			nextWord();
		}
	}

	public void nextWord() {
		strSpelling = "";
		strange = 0;
		// Random random = new Random(System.currentTimeMillis());
		// int index = Math.abs(random.nextInt() % 3000);
		try {
			wordManager = new WordManager(thesPath, thesName);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		}
		
		switch (reciteMode) {
		case NEW_WORD:
			try {
				currentWord = wordManager.getRandomWord();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
				System.exit(0);
			}
			break;
		case REVIEW:
			try {
				currentWord = wordManager.getWordByName(recordsVector.lastElement().word);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
				System.exit(0);
			}

			
			break;
		}

		wordLength = currentWord.name.length();

		lblEnglish.setText(" ");
		lblPhonetic.setText(currentWord.phonetic);
		txtChinese.setText(currentWord.interp);
		lblStatus.setText(wordManager.thesName);
		
	}

	public void setEventToLable(String strWord) {
		lblEnglish.setText(strWord);
	}
	
	public void chooseThesaurus() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("./thesaurus"));
		chooser.setDialogTitle("选择词库");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			thesPath = chooser.getSelectedFile().getPath();
			// thesName = chooser.getSelectedFile().getName();
			nextWord();


		} else {
			return;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == chooseThItem) {
			chooseThesaurus();
		} 
		else if (e.getSource() == statItem) {
			new ReciteStat();
		}
		else if (e.getSource() == modeItem) {
			if (reciteMode == Modes.REVIEW) {
				reciteMode = Modes.NEW_WORD;
				modeItem.setText("复习 (R)");
				modeItem.setMnemonic('R');
				nextWord();
			}
			else if (reciteMode == Modes.NEW_WORD) {
				reciteMode = Modes.REVIEW;
				modeItem.setText("学习 (N)");
				modeItem.setMnemonic('N');
				nextWord();
			}
			
		}
		else if (e.getSource() == exitItem){
			System.exit(0);
		}
		else if (e.getSource() == aboutItem) {
			JOptionPane.showMessageDialog(this, "版权所有 2011-2013 whypro\n献给... Q");
		}

	}
}
