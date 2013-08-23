package whypro.memorize;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import whypro.memorize.ui.ReciteUI;

class HelloWord {
	public static void main(String[] args) {
		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
		
		
		new ReciteUI();
	}
}
