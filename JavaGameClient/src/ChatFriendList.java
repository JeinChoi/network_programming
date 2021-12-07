// ChatFriendList.java
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
// Java Client ����import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.awt.event.*;
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JTextArea;
public class ChatFriendList extends JFrame {
   ArrayList<String> userNameList= new ArrayList<String>();
   ArrayList<String> selectedNameList = new ArrayList<String>();
   ArrayList<String> sendSelectedNameList = new ArrayList<String>();
   ArrayList<ListPanel> userOneListPanel = new ArrayList<ListPanel>();
   //Map<Integer,ArrayList<String>> myMultiChat= new HashMap<>();
   ArrayList<MultiChat> myMultiChat = new ArrayList<MultiChat>();
   ArrayList<OneChatRoomPanel> oneChatRoomPanelList = new ArrayList<OneChatRoomPanel>();
   Map<String,PicturePanel> userPicturePanel = new  HashMap<String,PicturePanel>();
   private JPanel contentPane;
   private JTextField txtIpAddress;
   private JTextField txtPortNumber;
   

   public ChatFriendList chatFriendList;
   public MultiChat multiChat;

   /**
    * Launch the application.
    */
   private static final long serialVersionUID = 1L;
   
   
   private String UserName;
   private JButton btnSend;
   private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����
   private Socket socket; // �������
   private InputStream is;
   private OutputStream os;
   private DataInputStream dis;
   private DataOutputStream dos;

   private ObjectInputStream ois;
   private ObjectOutputStream oos;
   JPanel contentPane_1;
   private JLabel lblUserName;
   // private JTextArea textArea;
   private JTextPane textArea;

   private Frame frame;
   private FileDialog fd;
   private JButton imgBtn;
   JPanel chatPanel;
   JPanel panel;
   private JLabel lblMouseEvent;
   private Graphics gc;
   private int pen_size = 2; // minimum 2
   // �׷��� Image�� �����ϴ� �뵵, paint() �Լ����� �̿��Ѵ�.
   private Image panelImage = null; 
   private Graphics gc2 = null;
   
   private JLabel userListLabel;
   
   private JPanel userListPanel;
   
   private JButton testButton;
   private int i=0;
   private int myRoomNum=0; 
   
  // private  JCheckBox checkBox;
   
  // public ShowInvitedFriendsList showInvitedFriendsList;
   
   /**
    * Create the frame.
    */
   public ChatFriendList(String username, String ip_addr, String port_no) {
     chatFriendList = this;
     
     
     this.UserName = username;
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(400,600);
      setBounds(100, 100, 386, 512);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);
      
     contentPane_1 = new JPanel();
      contentPane_1.setBackground(Color.WHITE);
      contentPane_1.setLayout(null);
      contentPane_1.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane_1.setBounds(61, 0, 311, 485);
      contentPane.add(contentPane_1);
      
      JLabel FriendLabel = new JLabel("\uCE5C\uAD6C");
      FriendLabel.setFont(new Font("����", Font.BOLD, 18));
      FriendLabel.setBounds(23, 23, 76, 34);
      contentPane_1.add(FriendLabel);
      
      JLabel userNameLabel = new JLabel(username);
      userNameLabel.setFont(new Font("����", Font.BOLD, 14));
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
      JLabel label1 = new JLabel(changeIcon);
      JLabel label2 = new JLabel(changeIcon);
      userImgPanel.add(label1);
      
      //JPanel friendImgPanel = new JPanel();
     // friendImgPanel.setBackground(Color.WHITE);
     // friendImgPanel.setBounds(23, 119, 42, 42);
     
      ImageIcon friendIcon = new ImageIcon("src/basicProfileImg.jpg");
      JLabel friendIconlabel = new JLabel(friendIcon);
     // friendImgPanel.add(friendIconlabel);
      
     // contentPane_1.add(friendImgPanel);
      
   
     
     
      JLabel friendNameLabel = new JLabel("");
      friendNameLabel.setFont(new Font("����", Font.BOLD, 14));
      friendNameLabel.setBounds(77, 134, 68, 15);
      contentPane_1.add(friendNameLabel);
      
      class FriendAction extends MouseAdapter // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
      {
        
         public void mouseClicked(MouseEvent e) {
            contentPane_1.setVisible(true);
            chatPanel.setVisible(false);
            
           }
      }
      
      FriendAction friendAction = new FriendAction();
      JPanel friendIconPanel = new JPanel();
      friendIconPanel.setBounds(12, 47, 40, 40);
      contentPane.add(friendIconPanel);
      friendIconPanel.addMouseListener(friendAction);
      ImageIcon menuIcon1 = new ImageIcon("src/menuIcon1.png");
      JLabel MenuIconlabel = new JLabel(menuIcon1);
      friendIconPanel.add(MenuIconlabel);
      
      
      JPanel chatIconPanel = new JPanel();
      chatIconPanel.setBounds(12, 119, 40, 40);
      contentPane.add(chatIconPanel);
      
      ImageIcon menuIcon2 = new ImageIcon("src/menuIcon2.png");
      
      chatPanel = new JPanel();//ä��â ��� ����� setvisible(false);
      chatPanel.setBounds(61, 0, 311, 485);
      chatPanel.setVisible(false);
      chatPanel.setBackground(Color.WHITE);
      chatPanel.setLayout(null);
      contentPane.add(chatPanel);
      
      JLabel chatLabel = new JLabel("\uCC44\uD305");
      chatLabel.setFont(new Font("����", Font.BOLD, 18));
      chatLabel.setBounds(23, 23, 76, 34);
      chatPanel.add(chatLabel);
      
   
      
      
      class ChatAction extends MouseAdapter // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
      {
        
         public void mouseClicked(MouseEvent e) { // chat ������ ������ ä��â ��� ȭ������ ���� 
            contentPane_1.setVisible(false);
            chatPanel.setVisible(true); // ä��â ��� ȭ�� �������� �� 
      
           }
     
      }
      ChatAction chatIconeve = new ChatAction();
      JLabel chatIconLabel = new JLabel(menuIcon2);
      chatIconPanel.add(chatIconLabel);
      chatIconPanel.addMouseListener(chatIconeve);//�� ������ ������ ä��â �ߵ���
      
     
      //txtIpAddress.addActionListener(action);
      //txtPortNumber.addActionListener(action);


      try {
         socket = new Socket(ip_addr, Integer.parseInt(port_no));

         oos = new ObjectOutputStream(socket.getOutputStream());
         oos.flush();
         ois = new ObjectInputStream(socket.getInputStream());

         ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");//connect
         //���� ������ ������ ������ �Դϴ�.
         SendObject(obcm);//send
         EnterUserNetwork net = new EnterUserNetwork();
         net.start();
      


      } catch (NumberFormatException | IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         //AppendText("connect error");
      }
      
      
    
      
    
     // friendImgPanel.addMouseListener(my);
      JLabel label_1 = new JLabel((Icon) null);
      //friendImgPanel.add(label_1);
      
      //userListLabel = new JLabel("");
      //userListLabel.setBounds(68, 432, 178, 42);
      //contentPane_1.add(userListLabel);
      
      userListPanel = new JPanel();
      userListPanel.setBackground(Color.WHITE);
      userListPanel.setBounds(12, 183, 287, 273);
      userListPanel.setLayout(null);
      contentPane_1.add(userListPanel);
      
      JPanel whitePanel = new JPanel();
      whitePanel.setBounds(219, 0, 68, 273);
      userListPanel.add(whitePanel);
      whitePanel.setVisible(false);
      
      
      class SendNameList extends MouseAdapter {
         //private static final String ListPanel = null;

      public void mouseClicked(MouseEvent e) { // ����� �ʴ��ϱ� 
            ChatMsg chatMsg=null;
            chatMsg = new ChatMsg(UserName,"500","send selected string list");
            chatMsg.i = i;
            selectedNameList.add(UserName); // ���α��� �߰���
            sendSelectedNameList=selectedNameList;
            chatMsg.setList(sendSelectedNameList);
            
           //System.out.println(chatMsg.getList());

            i++; // ���̵� ����
            
            SendObject(chatMsg);  // ����濡 �ʴ�� ������ ����Ʈ ������ ������ 
 
            String selectedNameListStr = String.join(",", selectedNameList);

            sendSelectedNameList.clear();

    
            for(int j=0;j<userOneListPanel.size();j++) {
            userOneListPanel.get(j).getCheckBox().setSelected(false);
            
            }
            chatMsg=null;
            selectedNameList.clear();
          //  (ListPanel)userListPanel.getCompontent(0);
         }
         
      }
      
      ImageIcon inviteImg = new ImageIcon("src/newRoomImg.png");
      testButton = new JButton(inviteImg);
      testButton.setBorderPainted(false);
      testButton.setContentAreaFilled(false);
      testButton.setFocusPainted(false);
      testButton.setBounds(253, 150, 30, 30);
      contentPane_1.add(testButton);
      SendNameList sendName = new SendNameList();
      testButton.addMouseListener(sendName);
      
      class ShowCheckBox extends MouseAdapter {
          //private static final String ListPanel = null;

       public void mouseClicked(MouseEvent e) { // üũ�ڽ� ���̰�
              //checkBox.setVisible(true);
          whitePanel.setVisible(false);
    
          }
          
       }
      
      JButton checkboxVisibleBtn = new JButton("\uCD08\uB300\uD558\uAE30");
      checkboxVisibleBtn.setBounds(104, 150, 91, 23);
      contentPane_1.add(checkboxVisibleBtn);
      
      JLabel onlineFriendLabel = new JLabel("\uC811\uC18D\uC790");
      onlineFriendLabel.setFont(new Font("����", Font.BOLD, 14));
      onlineFriendLabel.setBounds(23, 134, 76, 34);
      contentPane_1.add(onlineFriendLabel);
      
      JButton changeProfileBtn = new JButton("\uD504\uB85C\uD544 \uBCC0\uACBD");
      changeProfileBtn.setBounds(197, 85, 102, 23);
      contentPane_1.add(changeProfileBtn);
      
      class SelectProfile extends MouseAdapter {
          //private static final String ListPanel = null;

       public void mouseClicked(MouseEvent e) { // ����� �ʴ��ϱ� 
           System.out.println("���� �ٲٱ� ��ư ����");
           if (e.getSource() == changeProfileBtn) {
               frame = new Frame("�̹���÷��");
               fd = new FileDialog(frame, "�̹��� ����", FileDialog.LOAD);
               // frame.setVisible(true);
               fd.setDirectory(".\\");
               fd.setVisible(true);
               // System.out.println(fd.getDirectory() + fd.getFile());
               if (fd.getDirectory().length() > 0 && fd.getFile().length() > 0) {
                  ChatMsg obcm = new ChatMsg(username, "700", "IMG"); // �ڵ� 300�� �Բ� �̹��� ���� 
                  ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
                  System.out.println(fd.getDirectory() + fd.getFile());
                  obcm.img = img;
                  //obcm.multiChatNum=multiNum;
                    refreshPanel(img);
                  chatFriendList.SendObject(obcm);
               }
            }
          }
          
       }
      
      
      SelectProfile selectProfile = new SelectProfile();
      changeProfileBtn.addMouseListener(selectProfile);
      
      ShowCheckBox showCheckBox = new ShowCheckBox();
      checkboxVisibleBtn.addMouseListener(showCheckBox);
      checkboxVisibleBtn.setVisible(false);
      
          
    
      
     
   }
   public void refreshPanel(ImageIcon img){
	   for(int i=0;i<userPicturePanel.size();i++) {
		   String temp = userNameList.get(i);//arraylist get(0),get(1) => 0��°�� ����� username 
		   userPicturePanel.get(userNameList.get(i)).setImg();//map key:string(username) value:picturepanel
	   }//���ڽ��� ������ ������ ����
 	  this.revalidate();
   }
  public class CheckAction implements ItemListener{
     String userName;
   public CheckAction(String name) {
      this.userName= name;
   }
     @Override
   public void itemStateChanged(ItemEvent e) {
      // TODO Auto-generated method stub
      switch(e.getStateChange()) {
      case 1:
         //������ ������ ���õ� ����� �̸� ������
         selectedNameList.add(userName);
         break;
      case 2:
         selectedNameList.remove(userName);
       
         break;
      }
   }
     
  }
  public class PicturePanel extends JPanel{
	  JLabel picturelabel;
	  String userName;
	  public PicturePanel(String userName) {
		  this.userName = userName;
	        ImageIcon icon = new ImageIcon("src/basicProfileImg.jpg");
	         Image img = icon.getImage();
	         Image changeImg = img.getScaledInstance(41,41, Image.SCALE_SMOOTH);
	         ImageIcon changeIcon = new ImageIcon(changeImg);
	        picturelabel = new JLabel(changeIcon);
	          this.add(picturelabel);
	          userPicturePanel.put(userName,this);
	   
	         //picturepanel �ۿ��� list������ 700��޵� ������ picturePanel.setImgȣ��
	         
	     }
	  public void setImg() {
	       this.remove(picturelabel);
	          revalidate();
	          System.out.println(userName);
	         ImageIcon icon1 = new ImageIcon("src/icon1.jpg");
	         Image img2 = icon1.getImage();
	         Image changeImg1 = img2.getScaledInstance(41,41, Image.SCALE_SMOOTH);
	         ImageIcon changeIcon1 = new ImageIcon(changeImg1);
	         //JLabel picturelabel2 = new JLabel(changeIcon1);
	         picturelabel.setIcon(changeIcon1);
	        this.add(picturelabel);
	  }
//	  class mouseAdapter extends MouseAdapter {
//		  public void mouseClicked(MouseEvent e) {
//			  picturelabel.setIcon(changeIcon1);
//		  }
//	  }
//	  public void setImg(String src) {
//		  this.remove(picturelabel);
//		 ImageIcon change = new ImageIcon("D:\\FromNewVolume\\���б�\\����3-2\\��Ʈ��ũ ���α׷���\\NetP10-java-game (�����o) - ���纻\\JavaGameClient\\image2.jpg");
//		 System.out.println(change);
//		img = change.getImage();
//		  Image changeImg2 = img.getScaledInstance(41,41, Image.SCALE_SMOOTH);
//		  changeIcon1 = new ImageIcon(changeImg2);
//		// picturelabel.setIcon(changeIcon1);
//		 this.revalidate();
//		 MouseAdapter m = new mouseAdapter();
//		 this.addMouseListener(m);
//	  }
	
     
  }

  class ChatPanelEvent extends MouseAdapter{     
     int multiChatNum;
     public ChatPanelEvent(int multiChatNum) {
        this.multiChatNum = multiChatNum;              
     }
     @Override
     public void mouseClicked(MouseEvent e) {
        for(int i=0;i<myMultiChat.size();i++) {
             if(myMultiChat.get(i).multiNum==multiChatNum) {
                myMultiChat.get(i).setVisible(true);
                System.out.println("��Ƽä�ù�ȣ"+multiChatNum);
             }
        }
   }
 }
  public class OneChatRoomPanel extends JPanel{
     int multiChatNum;
     MultiChat multiChat;
     public OneChatRoomPanel(int multiChatNum,MultiChat multiChat) {
        this.multiChatNum = multiChatNum;
        ChatPanelEvent chatPanelEvent = new ChatPanelEvent(multiChatNum);
        this.addMouseListener(chatPanelEvent);
     }
     int getMultiChatNum() {
        return multiChatNum;
     }
                      
  }
   
  
  public class ListPanel extends JPanel{
           JCheckBox checkBox;
           
          
         public ListPanel(JLabel userNameLabel,int y,String name) {
            this.setLayout(new GridLayout(1,3));
            PicturePanel picturePanel = new PicturePanel(name);
          //  picturePanel.setImg();
            //name picturepanel map
//            map.add(name,picturepanel);
//            700
//            �������� ���� �����ʻ���+username
//            map�̶�  pcitdj
            // panel �� ����
            picturePanel.setBackground(Color.WHITE);
            this.setBackground(Color.WHITE);
           // checkBox.setBackground(Color.WHITE);
            this.add(picturePanel);
           
            picturePanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                   
               }            
            });
            this.add(userNameLabel);
            this.setSize(280,60);
            this.setLocation(0,y);
            
            this.checkBox = new JCheckBox();
            this.checkBox.setBackground(Color.WHITE);
            //checkBox = new JCheckBox();
            checkBox.setVisible(true);
            CheckAction checkAction= new CheckAction(name);
            checkBox.addItemListener(checkAction);
            this.add(checkBox);
         }
         public JCheckBox getCheckBox() {
            return this.checkBox;
            //return checkBox;
         }
      }
   class EnterUserNetwork extends Thread {
      
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
               switch (cm.code) {
                     case "100":
                    
             
                         break;
                         
                     case "101": // ���� ����Ʈ �޾Ҵٸ�
                       
                    
                        String[] userListString= cm.data.split(",");
                        userNameList = cm.al;
                        System.out.println(userNameList);
                        //ShowInvitedFriendsList showInvitedFriendsList = new ShowInvitedFriendsList(cm.data, UserName);
                        
                               ListPanel userOnePanel;
                                 JLabel userOneLabel;
                                 //userListPanel.removeAll();

                                 int j=0;
                                 for (int i=0; i<userListString.length; i++) {
                                   // System.out.println(userListString[i]);
                                    if(!UserName.equals(userListString[i])) {
                                        userOneLabel = new JLabel();
                                       
                                        userOneLabel.setText(userListString[i]);
                                        userOneLabel.setBackground(Color.WHITE);
                                        userOnePanel = new ListPanel(userOneLabel,j*60,userListString[i]);
                                        userOnePanel.setBackground(Color.WHITE);
                                        //userOnePanel.setBorder(BorderFactory.createLineBorder(Color.black));
                                        j++;
                                        userListPanel.add(userOnePanel);//��ġ for��  ���鼭 ������ �������� ��. 
                                       userOneListPanel.add(userOnePanel);
                                           
                                        
                                    }
                                   
                                }
                                 userListPanel.revalidate();
                                 userListPanel.repaint();
                       
                       break;
                       
                     case "550": // 
                        
                        String[] invitedUsersArr= cm.data.split(",");
                        
                        // invitedUsersArr: a,b,c
                        // username: a
                       
                        multiChat = new MultiChat(UserName, cm.multiChatNum,invitedUsersArr,chatFriendList);
                        
                        myMultiChat.add(multiChat);
                        for(int i=0;i<myMultiChat.size();i++) {
                           System.out.print(myMultiChat.get(i).multiNum+" ");
                        }
                        System.out.println("�гθ���Ʈ");
                        
                        OneChatRoomPanel oneChatRoomPanel = new OneChatRoomPanel(cm.multiChatNum,multiChat);
                        for(int i=0;i<oneChatRoomPanelList.size();i++) {
                           System.out.print(oneChatRoomPanelList.get(i).getMultiChatNum()+" ");
                        }
                        System.out.println("");
                        for(int i=0; i<invitedUsersArr.length; i++) { // �ʴ�� �������� ���鼭 
                           if(UserName.equals(invitedUsersArr[i])) { // �ڱ� �ڽ��� �� �� ���ԵǾ� ������ ������� �߰��Ѵ�. 
                              myRoomNum++;
                           
                                      // ���� �ϳ��� �ĳ�
                                 oneChatRoomPanel.setLayout(null);
                                 oneChatRoomPanel.setSize(280,60);
                                 oneChatRoomPanel.setLocation(20,myRoomNum*60);
                                 
                                 oneChatRoomPanel.setBorder(BorderFactory.createLineBorder(Color.black));
                                 
                                 String invitedUsersStr = String.join(", ", invitedUsersArr);
                                 
                                 JLabel oneChatRoomLabel = new JLabel(invitedUsersStr); // ���� �ϳ��� ����Ʈ
                                 oneChatRoomLabel.setSize(270,30);
                                 oneChatRoomLabel.setLocation(30,15);
                                 oneChatRoomPanel.add(oneChatRoomLabel);
                                 oneChatRoomPanel.setBackground(Color.WHITE);
                                 
                                 chatPanel.add(oneChatRoomPanel);
                                 break;
                           }
                           
                        }
                        oneChatRoomPanelList.add(oneChatRoomPanel);
                     
                        //MultiChat multiChat = new MultiChat(UserName, cm.multiChatNum,invitedUsersArr,this);
                        //number�� list ����
                        //oneChatPanel���ٰ� addevent�ؼ� ������ multichat frame�� �����ǵ���
                    
                           
                           
                        
                           
                        break;
                     case "210": //����濡�� ���� �޽����� ������ ���� ����.
                         System.out.println("chtFriendList Ŭ���� �� ����"+cm.data);
                        // cm.multiChatNum�� �����ؼ� multiChat�� ����
                         for(int i=0;i<myMultiChat.size();i++) {//������ ������ �ִ� ��Ƽê���߿� � ê�濡�� �޽����� �°��� Ȯ��
                            MultiChat findMultiRoom = myMultiChat.get(i);
                            if(findMultiRoom.getMultiNum()==cm.multiChatNum) {
                               if (cm.UserName.equals(UserName)) {
                                  System.out.println("cm.data:"+cm.data);
                                  findMultiRoom.AppendTextR(cm.data); // �� �޼����� ������
                               }
                                   else {
                                      findMultiRoom.AppendText(cm.data);
                                   }
                               
                               
                            }
                         }
                        
                         break;
                     case "300": // Image ÷��
                        
                        //Image image = cm.img.getImage();
                         
                        // image.getScaledInstance(40,40,Image.SCALE_SMOOTH);
                         //cm.img.setImage(image);
                         
                         for(int i=0;i<myMultiChat.size();i++) {//������ ������ �ִ� ��Ƽê���߿� � ê�濡�� �޽����� �°��� Ȯ��
                            MultiChat findMultiRoom = myMultiChat.get(i);
                            if(findMultiRoom.getMultiNum()==cm.multiChatNum) {
                              
                               
                               if (cm.UserName.equals(UserName)) {
                                  findMultiRoom.AppendTextR("[" + cm.UserName + "]");
                                  findMultiRoom.AppendImage(cm.img);
                               }
                                   else {
                                     findMultiRoom.AppendText("[" + cm.UserName + "]");
                                     findMultiRoom.AppendImage(cm.img);
                                   }
                            }
                         }
                          
                          break;
                     case "700":
                    	 //refreshPanel();
                    	 break;
                        
                  
            /*case "200": // chat message
               if (cm.UserName.equals(UserName))
                  //AppendTextR(msg); // �� �޼����� ������
               else
                  //AppendText(msg);
               break;
            case "300": // Image ÷��
               if (cm.UserName.equals(UserName))
                  //AppendTextR("[" + cm.UserName + "]");
               else
                  //AppendText("[" + cm.UserName + "]");
               //AppendImage(cm.img);
               break;
            case "500": // Mouse Event ����
               //DoMouseEvent(cm);
               break;
               */
            }
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
               } // catch�� ��
            } // �ٱ� catch����

         }
      }
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
   
    public void SendObject(Object ob) { // ������ �޼����� ������ �޼ҵ�
      try {

         oos.writeObject(ob);
         oos.reset();
      } catch (IOException e) {
         // textArea.append("�޼��� �۽� ����!!\n");
         AppendText("SendObject Error");
      }
   }
}

//class MyMouse implements MouseListener{
//   @Override
//   public void mouseClicked(MouseEvent e) {
//      (Window) e.setVisible(false);
//   }
//   @Override
//      public void mouseExited(MouseEvent e){
//
//   } 
//   @Override
//   public void mouseReleased(MouseEvent e){
//
//   } 
//   @Override
//   public void mousePressed(MouseEvent e ) {
//      
//   }
//
//}