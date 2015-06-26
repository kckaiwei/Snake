import java.awt.EventQueue;

import javax.swing.JFrame;


public class Snake extends JFrame{

	public Snake() {
		// TODO Auto-generated constructor stub
		
		add (new Board());
		
		setResizable(false);
		pack();
		
		setTitle("Nibbles");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public static void main (String[] args){
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				JFrame ex = new Snake ();
				ex.setVisible(true);
			}
		});
	}

}
