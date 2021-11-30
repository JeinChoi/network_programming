import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.FileDialog;

public class MultiChat extends JFrame{
   private JPanel contentPane;
   private JTextField txtInput;
   // private JTextArea textArea;
    private JTextPane textArea;
    private JButton btnSend;
    private JButton imgBtn;
    private JLabel lblUserName;
    private Frame frame;
    private FileDialog fd;
    private MultiChat multiChat;
    
    JPanel panel;
    
    //private Graphics gc;
   private int pen_size = 2; // minimum 2
   // 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.
   private Image panelImage = null; 
   private Graphics gc2 = null;
    
   String username;
   int multiNum; // 방번호
   String invitedFriendsArr[];
   public ChatFriendList chatFriendList;
   class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
    {
       @Override
       public void actionPerformed(ActionEvent e) {
          // Send button을 누르거나 메시지 입력하고 Enter key 치면
          if (e.getSource() == btnSend || e.getSource() == txtInput) {
             String msg = null;
             msg = String.format("[%s] %s\n", username, txtInput.getText());
             ChatMsg obcm = new ChatMsg(username,"210",msg);
             obcm.multiChatNum=multiNum;
             chatFriendList.SendObject(obcm);
             //방넘버랑 방리스트
             
             txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
             txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
             if (msg.contains("/exit")) // 종료 처리
                System.exit(0);
          }
       }
    }
   class ImageSendAction implements ActionListener { // 이미지 보내기 
      @Override
      public void actionPerformed(ActionEvent e) {
         // 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
         if (e.getSource() == imgBtn) {
            frame = new Frame("이미지첨부");
            fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
            // frame.setVisible(true);
            // fd.setDirectory(".\\");
            fd.setVisible(true);
            // System.out.println(fd.getDirectory() + fd.getFile());
            if (fd.getDirectory().length() > 0 && fd.getFile().length() > 0) {
               ChatMsg obcm = new ChatMsg(username, "300", "IMG"); // 코드 300과 함께 이미지 보냄 
               ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
               System.out.println(fd.getDirectory() + fd.getFile());
               obcm.img = img;
               obcm.multiChatNum=multiNum;
                   
               chatFriendList.SendObject(obcm);
            }
         }
      }
   }
   
   class EmoticonSendAction implements ActionListener { // 이모티콘 보내기 
         @Override
         public void actionPerformed(ActionEvent e) {
            
            System.out.println("이모티콘 버튼 눌림");
            Emoticon emoticon = new Emoticon(username, multiNum, chatFriendList);
            emoticon.setVisible(true);
            
         }
      }

   
   public MultiChat(String username, int multiNum,String [] invitedFriendsArr,ChatFriendList chatFriendList) {
      this.chatFriendList = chatFriendList;
      this.username = username; 
      this.multiNum = multiNum;
      this.invitedFriendsArr = invitedFriendsArr;
      multiChat = this;
         setSize(416,634);
         setBounds(100, 100, 390, 634);
         JPanel contentPane = new JPanel();
         contentPane.setBackground(Color.WHITE);
         contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
         setContentPane(contentPane);
         contentPane.setLayout(null);
         JScrollPane scrollPane = new JScrollPane();
       scrollPane.setBounds(0, 10, 376, 471);
       contentPane.add(scrollPane);

       textArea = new JTextPane();
       textArea.setEditable(true);
       textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
       scrollPane.setViewportView(textArea);
       
       Color textAreaColor=new Color(155,187,212);  
       textArea.setBackground(textAreaColor);
      

       txtInput = new JTextField();
       txtInput.setBounds(89, 501, 209, 30);
       contentPane.add(txtInput);
       txtInput.setColumns(10);
     
       Color yellowColor=new Color(254,240,27);  
       
     
       btnSend = new JButton("\uC804\uC1A1");
       btnSend.setFont(new Font("굴림", Font.PLAIN, 12));
       btnSend.setBounds(310, 501, 54, 30);
       contentPane.add(btnSend);
       
       btnSend.setBackground(yellowColor);

       Myaction action = new Myaction();
      btnSend.addActionListener(action); // 내부클래스로 액션 리스너를 상속받은 클래스로
      txtInput.addActionListener(action);
       
       lblUserName = new JLabel("Name");
       lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
       lblUserName.setBackground(Color.WHITE);
       lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
       lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
       lblUserName.setBounds(161, 539, 62, 40);
       contentPane.add(lblUserName);
       this.setVisible(true);
       
       //AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
       //UserName = username;
       lblUserName.setText(username);
       
       // 변수명 바꿈
     
       String invitedFriendsString = String.join(",", invitedFriendsArr);
       AppendText(invitedFriendsString+"님이 초대되었습니다.");
       
       ImageIcon addImageIcon = new ImageIcon("src/imageBtn.png");
       Image tempImgIcon = addImageIcon.getImage();
       Image changeAddImageIcon = tempImgIcon.getScaledInstance(40,30,Image.SCALE_SMOOTH);
       ImageIcon newAddImageIcon = new ImageIcon(changeAddImageIcon);
       imgBtn = new JButton(newAddImageIcon);
       imgBtn.setBorderPainted(false);
       imgBtn.setContentAreaFilled(false);
       imgBtn.setFocusPainted(false);
       imgBtn.setFont(new Font("굴림", Font.PLAIN, 16));
       imgBtn.setBounds(12, 501, 40, 30);
       contentPane.add(imgBtn);
       //imgBtn.setBackground(yellowColor);
       ImageSendAction action2 = new ImageSendAction();
      imgBtn.addActionListener(action2);
       
       panel = new JPanel();
      panel.setBorder(new LineBorder(new Color(0, 0, 0)));
      panel.setBackground(Color.WHITE);
      panel.setBounds(376, 10, 400, 520);
      contentPane.add(panel);
      
      ImageIcon emoticonBtnIcon = new ImageIcon("src/emoticonBtn.png");
      JButton emoticonBtn = new JButton(emoticonBtnIcon); // 이모티콘 버튼
      emoticonBtn.setBorderPainted(false);
      emoticonBtn.setContentAreaFilled(false);
      emoticonBtn.setFocusPainted(false);
      emoticonBtn.setBounds(49, 501, 40, 30);
      contentPane.add(emoticonBtn);
      
      EmoticonSendAction action3 = new EmoticonSendAction();
      emoticonBtn.addActionListener(action3);
      
     // gc = panel.getGraphics();
      
      // Image 영역 보관용. paint() 에서 이용한다.
      panelImage = createImage(panel.getWidth(), panel.getHeight());
      gc2 = panelImage.getGraphics();
      gc2.setColor(panel.getBackground());
      gc2.fillRect(0,0, panel.getWidth(),  panel.getHeight());
      gc2.setColor(Color.BLACK);
      gc2.drawRect(0,0, panel.getWidth()-1,  panel.getHeight()-1);
      
    
      
    
  
   }
   public int getMultiNum() {
      return multiNum;
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
   public void paint(Graphics g) {
      super.paint(g);
      // Image 영역이 가려졌다 다시 나타날 때 그려준다.
      //gc.drawImage(panelImage, 0, 0, this);
   }
   
   
   public void AppendTextR(String msg) {
      msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다. 
      
      StyledDocument doc = textArea.getStyledDocument();
      SimpleAttributeSet right = new SimpleAttributeSet();
      StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
      StyleConstants.setForeground(right, Color.BLUE);   
       doc.setParagraphAttributes(doc.getLength(), 1, right, false);
      try {
         doc.insertString(doc.getLength(),msg+"\n", right );
      } catch (BadLocationException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
public void AppendImage(ImageIcon ori_icon) {
      
   Image image = ori_icon.getImage();
    
    image.getScaledInstance(40,40,Image.SCALE_SMOOTH);
    ori_icon.setImage(image);
    
       int len = textArea.getDocument().getLength();
       textArea.setCaretPosition(len); // place caret at the end (with no selection)
       Image ori_img = ori_icon.getImage();
       Image new_img;
       ImageIcon new_icon;
       int width, height;
       double ratio;
       width = ori_icon.getIconWidth();
       height = ori_icon.getIconHeight();
       // Image가 너무 크면 최대 가로 또는 세로 200 기준으로 축소시킨다.
       if (width > 200 || height > 200) {
          if (width > height) { // 가로 사진
             ratio = (double) height / width;
             width = 200;
             height = (int) (width * ratio);
          } else { // 세로 사진
             ratio = (double) width / height;
             height = 200;
             width = (int) (height * ratio);
          }
          new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
          new_icon = new ImageIcon(new_img);
          textArea.insertIcon(new_icon);
       } else {
          textArea.insertIcon(ori_icon);
          new_img = ori_img;
       }
       len = textArea.getDocument().getLength();
       textArea.setCaretPosition(len);
       textArea.replaceSelection("\n");
       // ImageViewAction viewaction = new ImageViewAction();
       // new_icon.addActionListener(viewaction); // 내부클래스로 액션 리스너를 상속받은 클래스로
       // panelImage = ori_img.getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_DEFAULT);

       gc2.drawImage(ori_img,  0,  0, panel.getWidth(), panel.getHeight(), panel);
       //gc.drawImage(panelImage, 0, 0, panel.getWidth(), panel.getHeight(), panel);
    }
}