import java.awt.Font;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Init extends JFrame{

	public Init(){
		
		add(new Game());
		setSize(500, 500);
		setTitle("Time Out");
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String args[]){
		new Init();
	}
}
