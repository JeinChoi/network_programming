import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

class Example{
	int i;
	public Example(int i) {
		this.i =i;
	}
	
}
class MyPanel extends JPanel{
	 ImageIcon icon;
	  Image img;
	  String src;
	  JLabel picturelabel;
	public MyPanel(String src) {
	    icon = new ImageIcon(src);
        img = icon.getImage();
        Image changeImg = img.getScaledInstance(41,41, Image.SCALE_SMOOTH);
        ImageIcon changeIcon = new ImageIcon(changeImg);
        picturelabel = new JLabel(changeIcon);
        
        this.add(picturelabel);
	}
}
public class Ex extends JFrame {
	public Ex() {
		
		MyPanel mypanel = new MyPanel("JavaGameClient/image2.jpg");
		getContentPane().setLayout(null);
		this.setSize(100,100);
		this.setLocation(50,50);
		this.add(mypanel);
		setVisible(true);
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      setSize(400,600);
	      setBounds(100, 100, 386, 512);
	     JLabel d = new JLabel("djkfjdlfjsfjlksdjflksdjf");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		ArrayList<Example> a = new ArrayList<Example>();
//		
//		for(int i=0;i<3;i++) {
//		Example ex3 = new Example(1);
//		a.add(ex3);
//		}Example ex4 = new Example(1);
//		System.out.println(a.get(0).equals(ex4));//false
//		System.out.println(ex4.equals(ex4));//false
		new Ex();
	}

}
