// JavaChatClientMain.java
// Java Client 시작import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.sun.tools.javac.Main;
/*
import JavaGameClientView.ImageSendAction;
import JavaGameClientView.ListenNetwork;
import JavaGameClientView.MyMouseEvent;
import JavaGameClientView.MyMouseWheelEvent;
import JavaGameClientView.TextSendAction;*/

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.*;
public class ChatFriendList extends JFrame {

   private JPanel contentPane;
   private JTextField txtIpAddress;
   private JTextField txtPortNumber;

   /**
    * Launch the application.
    */
   private static final long serialVersionUID = 1L;
   
   
   private String UserName;
   private JButton btnSend;
   private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
   private Socket socket; // 연결소켓
   private InputStream is;
   private OutputStream os;
   private DataInputStream dis;
   private DataOutputStream dos;

   private ObjectInputStream ois;
   private ObjectOutputStream oos;

   private JLabel lblUserName;
   // private JTextArea textArea;
   private JTextPane textArea;

   private Frame frame;
   private FileDialog fd;
   private JButton imgBtn;

   JPanel panel;
   private JLabel lblMouseEvent;
   private Graphics gc;
   private int pen_size = 2; // minimum 2
   // 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.
   private Image panelImage = null; 
   private Graphics gc2 = null;
   /**
    * Create the frame.
    */
   public ChatFriendList(String username, String ip_addr, String port_no) {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(400,600);
      setBounds(100, 100, 386, 512);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);
      
      JPanel contentPane_1 = new JPanel();
      contentPane_1.setBackground(Color.WHITE);
      contentPane_1.setLayout(null);
      contentPane_1.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane_1.setBounds(61, 0, 311, 485);
      contentPane.add(contentPane_1);
      
      JLabel FriendLabel = new JLabel("\uCE5C\uAD6C");
      FriendLabel.setFont(new Font("굴림", Font.BOLD, 18));
      FriendLabel.setBounds(23, 23, 76, 34);
      contentPane_1.add(FriendLabel);
      
      JLabel userNameLabel = new JLabel(username);
      userNameLabel.setFont(new Font("굴림", Font.BOLD, 14));
      userNameLabel.setBounds(77, 75, 68, 15);
      contentPane_1.add(userNameLabel);
      
      JPanel userImgPanel = new JPanel();
      userImgPanel.setBackground(Color.WHITE);
      userImgPanel.setBounds(23, 66, 42, 42);
      contentPane_1.add(userImgPanel);
//      MyPanel panel = new MyPanel("src/icon1.jpg");
//      contentPane_1.add(panel);
      ImageIcon icon = new ImageIcon("src/basicProfileImg.jpg");
      Image img = icon.getImage();
      Image changeImg = img.getScaledInstance(41,41, Image.SCALE_SMOOTH);
      ImageIcon changeIcon = new ImageIcon(changeImg);
      JLabel label = new JLabel(changeIcon);
      
      userImgPanel.add(label);
      
      JPanel friendIconPanel = new JPanel();
      friendIconPanel.setBounds(12, 47, 40, 40);
      contentPane.add(friendIconPanel);
      
      ImageIcon menuIcon1 = new ImageIcon("src/menuIcon1.png");
      JLabel MenuIconlabel = new JLabel(menuIcon1);
      friendIconPanel.add(MenuIconlabel);
      
      
      JPanel chatIconPanel = new JPanel();
      chatIconPanel.setBounds(12, 119, 40, 40);
      contentPane.add(chatIconPanel);
      
      ImageIcon menuIcon2 = new ImageIcon("src/menuIcon2.png");
      JLabel chatIconLabel = new JLabel(menuIcon2);
      chatIconPanel.add(chatIconLabel);
      
      
      //txtIpAddress.addActionListener(action);
      //txtPortNumber.addActionListener(action);


      try {
         socket = new Socket(ip_addr, Integer.parseInt(port_no));
//         is = socket.getInputStream();
//         dis = new DataInputStream(is);
//         os = socket.getOutputStream();
//         dos = new DataOutputStream(os);

         oos = new ObjectOutputStream(socket.getOutputStream());
         oos.flush();
         ois = new ObjectInputStream(socket.getInputStream());

         // SendMessage("/login " + UserName);
         ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
         //SendObject(obcm);

         ListenNetwork net = new ListenNetwork();
         net.start();
         //TextSendAction action = new TextSendAction();
         //btnSend.addActionListener(action);
         //txtInput.addActionListener(action);
         //txtInput.requestFocus();
         //ImageSendAction action2 = new ImageSendAction();
         //imgBtn.addActionListener(action2);
         //MyMouseEvent mouse = new MyMouseEvent();
         //panel.addMouseMotionListener(mouse);
         //panel.addMouseListener(mouse);
         //MyMouseWheelEvent wheel = new MyMouseWheelEvent();
         //panel.addMouseWheelListener(wheel);


      } catch (NumberFormatException | IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         //AppendText("connect error");
      }
     
   }
   class ListenNetwork extends Thread {
      public void run() {
         while (true) {
            try {

               Object obcm = null;
               String msg = null;
               ChatMsg cm;
               try {
                  obcm = ois.readObject();
               } catch (ClassNotFoundException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                  break;
               }
               if (obcm == null)
                  break;
               if (obcm instanceof ChatMsg) {
                  cm = (ChatMsg) obcm;
                  msg = String.format("[%s]\n%s", cm.UserName, cm.data);
               } else
                  continue;
               /*
                * switch (cm.code) { case "200": // chat message if
                * (cm.UserName.equals(UserName)) AppendTextR(msg); // 내 메세지는 우측에 else
                * AppendText(msg); break; //case "300": // Image 첨부 if
                * (cm.UserName.equals(UserName)) AppendTextR("[" + cm.UserName + "]"); else
                * AppendText("[" + cm.UserName + "]"); AppendImage(cm.img); break; case "500":
                * // Mouse Event 수신 DoMouseEvent(cm); break; }
                */
            } catch (IOException e) {
               AppendText("ois.readObject() error");
               try {
//                  dos.close();
//                  dis.close();
                  ois.close();
                  oos.close();
                  socket.close();

                  break;
               } catch (Exception ee) {
                  break;
               } // catch문 끝
            } // 바깥 catch문끝

         }
      }
   }
   public void AppendText(String msg) {
      // textArea.append(msg + "\n");
      // AppendIcon(icon1);
      msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
      int len = textArea.getDocument().getLength();
      // 끝으로 이동
      //textArea.setCaretPosition(len);
      //textArea.replaceSelection(msg + "\n");
      
      StyledDocument doc = textArea.getStyledDocument();
      SimpleAttributeSet left = new SimpleAttributeSet();
      StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
      StyleConstants.setForeground(left, Color.BLACK);
       doc.setParagraphAttributes(doc.getLength(), 1, left, false);
      try {
         doc.insertString(doc.getLength(), msg+"\n", left );
      } catch (BadLocationException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }
   


}