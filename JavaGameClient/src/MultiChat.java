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
   // �׷��� Image�� �����ϴ� �뵵, paint() �Լ����� �̿��Ѵ�.
   private Image panelImage = null; 
   private Graphics gc2 = null;
    
   String username;
   int multiNum; // ���ȣ
   String invitedFriendsArr[];
   public ChatFriendList chatFriendList;
   class Myaction implements ActionListener // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
    {
       @Override
       public void actionPerformed(ActionEvent e) {
          // Send button�� �����ų� �޽��� �Է��ϰ� Enter key ġ��
          if (e.getSource() == btnSend || e.getSource() == txtInput) {
             String msg = null;
             msg = String.format("[%s] %s\n", username, txtInput.getText());
             ChatMsg obcm = new ChatMsg(username,"210",msg);
             obcm.multiChatNum=multiNum;
             chatFriendList.SendObject(obcm);
             //��ѹ��� �渮��Ʈ
             
             txtInput.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
             txtInput.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
             if (msg.contains("/exit")) // ���� ó��
                System.exit(0);
          }
       }
    }
   class ImageSendAction implements ActionListener { // �̹��� ������ 
      @Override
      public void actionPerformed(ActionEvent e) {
         // �׼� �̺�Ʈ�� sendBtn�϶� �Ǵ� textField ���� Enter key ġ��
         if (e.getSource() == imgBtn) {
            frame = new Frame("�̹���÷��");
            fd = new FileDialog(frame, "�̹��� ����", FileDialog.LOAD);
            // frame.setVisible(true);
            // fd.setDirectory(".\\");
            fd.setVisible(true);
            // System.out.println(fd.getDirectory() + fd.getFile());
            if (fd.getDirectory().length() > 0 && fd.getFile().length() > 0) {
               ChatMsg obcm = new ChatMsg(username, "300", "IMG"); // �ڵ� 300�� �Բ� �̹��� ���� 
               ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
               System.out.println(fd.getDirectory() + fd.getFile());
               obcm.img = img;
               obcm.multiChatNum=multiNum;
                   
               chatFriendList.SendObject(obcm);
            }
         }
      }
   }
   
   class EmoticonSendAction implements ActionListener { // �̸�Ƽ�� ������ 
         @Override
         public void actionPerformed(ActionEvent e) {
            
            System.out.println("�̸�Ƽ�� ��ư ����");
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
       textArea.setFont(new Font("����ü", Font.PLAIN, 14));
       scrollPane.setViewportView(textArea);
       
       Color textAreaColor=new Color(155,187,212);  
       textArea.setBackground(textAreaColor);
      

       txtInput = new JTextField();
       txtInput.setBounds(89, 501, 209, 30);
       contentPane.add(txtInput);
       txtInput.setColumns(10);
     
       Color yellowColor=new Color(254,240,27);  
       
     
       btnSend = new JButton("\uC804\uC1A1");
       btnSend.setFont(new Font("����", Font.PLAIN, 12));
       btnSend.setBounds(310, 501, 54, 30);
       contentPane.add(btnSend);
       
       btnSend.setBackground(yellowColor);

       Myaction action = new Myaction();
      btnSend.addActionListener(action); // ����Ŭ������ �׼� �����ʸ� ��ӹ��� Ŭ������
      txtInput.addActionListener(action);
       
       lblUserName = new JLabel("Name");
       lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
       lblUserName.setBackground(Color.WHITE);
       lblUserName.setFont(new Font("����", Font.BOLD, 14));
       lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
       lblUserName.setBounds(161, 539, 62, 40);
       contentPane.add(lblUserName);
       this.setVisible(true);
       
       //AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
       //UserName = username;
       lblUserName.setText(username);
       
       // ������ �ٲ�
     
       String invitedFriendsString = String.join(",", invitedFriendsArr);
       AppendText(invitedFriendsString+"���� �ʴ�Ǿ����ϴ�.");
       
       ImageIcon addImageIcon = new ImageIcon("src/imageBtn.png");
       Image tempImgIcon = addImageIcon.getImage();
       Image changeAddImageIcon = tempImgIcon.getScaledInstance(40,30,Image.SCALE_SMOOTH);
       ImageIcon newAddImageIcon = new ImageIcon(changeAddImageIcon);
       imgBtn = new JButton(newAddImageIcon);
       imgBtn.setBorderPainted(false);
       imgBtn.setContentAreaFilled(false);
       imgBtn.setFocusPainted(false);
       imgBtn.setFont(new Font("����", Font.PLAIN, 16));
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
      JButton emoticonBtn = new JButton(emoticonBtnIcon); // �̸�Ƽ�� ��ư
      emoticonBtn.setBorderPainted(false);
      emoticonBtn.setContentAreaFilled(false);
      emoticonBtn.setFocusPainted(false);
      emoticonBtn.setBounds(49, 501, 40, 30);
      contentPane.add(emoticonBtn);
      
      EmoticonSendAction action3 = new EmoticonSendAction();
      emoticonBtn.addActionListener(action3);
      
     // gc = panel.getGraphics();
      
      // Image ���� ������. paint() ���� �̿��Ѵ�.
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
      msg = msg.trim(); // �յ� blank�� \n�� �����Ѵ�.
      int len = textArea.getDocument().getLength();
      // ������ �̵�
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
      // Image ������ �������� �ٽ� ��Ÿ�� �� �׷��ش�.
      //gc.drawImage(panelImage, 0, 0, this);
   }
   
   
   public void AppendTextR(String msg) {
      msg = msg.trim(); // �յ� blank�� \n�� �����Ѵ�. 
      
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
       // Image�� �ʹ� ũ�� �ִ� ���� �Ǵ� ���� 200 �������� ��ҽ�Ų��.
       if (width > 200 || height > 200) {
          if (width > height) { // ���� ����
             ratio = (double) height / width;
             width = 200;
             height = (int) (width * ratio);
          } else { // ���� ����
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
       // new_icon.addActionListener(viewaction); // ����Ŭ������ �׼� �����ʸ� ��ӹ��� Ŭ������
       // panelImage = ori_img.getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_DEFAULT);

       gc2.drawImage(ori_img,  0,  0, panel.getWidth(), panel.getHeight(), panel);
       //gc.drawImage(panelImage, 0, 0, panel.getWidth(), panel.getHeight(), panel);
    }
}