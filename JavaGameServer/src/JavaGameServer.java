//JavaObjServer.java ObjectStream 기반 채팅 Server

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class JavaGameServer extends JFrame {
   ArrayList<String> userNameList = new ArrayList<String>();

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private JPanel contentPane;
   JTextArea textArea;
   private JTextField txtPortNumber;
   int multiChatNum =0; // 단톡방 번호
   private ServerSocket socket; // 서버소켓
   private Socket client_socket; // accept() 에서 생성된 client 소켓
   private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
   private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
   Map<Integer,ArrayList<String>> multiChatNumNmems= new HashMap<>();
   Map<String,ObjectOutputStream> clientsOutputStream = new HashMap<String,ObjectOutputStream>();
   Map<String,ImageIcon> profileMems = new HashMap<String,ImageIcon>();
   String invitedUsersStr; // 하나의 단톡방에 초대된 사람들 리스트
//chatlist자체를 구별을 못하나? 구별할 이유가 없는데 
   ArrayList<ImageIcon> profileImageIcons = new ArrayList<ImageIcon>();
   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               JavaGameServer frame = new JavaGameServer();
               frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

   /**
    * Create the frame.
    */
   public JavaGameServer() {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 338, 440);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);

      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setBounds(12, 10, 300, 298);
      contentPane.add(scrollPane);

      textArea = new JTextArea();
      textArea.setEditable(false);
      scrollPane.setViewportView(textArea);

      JLabel lblNewLabel = new JLabel("Port Number");
      lblNewLabel.setBounds(13, 318, 87, 26);
      contentPane.add(lblNewLabel);

      txtPortNumber = new JTextField();
      txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
      txtPortNumber.setText("30000");
      txtPortNumber.setBounds(112, 318, 199, 26);
      contentPane.add(txtPortNumber);
      txtPortNumber.setColumns(10);

      JButton btnServerStart = new JButton("Server Start");
      btnServerStart.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
            } catch (NumberFormatException | IOException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
            AppendText("Chat Server Running..");
            btnServerStart.setText("Chat Server Running..");
            btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
            txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
            AcceptServer accept_server = new AcceptServer();
            accept_server.start();
         }
      });
      btnServerStart.setBounds(12, 356, 300, 35);
      contentPane.add(btnServerStart);
   }

   // 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
   class AcceptServer extends Thread {
      @SuppressWarnings("unchecked")
      public void run() {
         while (true) { // 사용자 접속을 계속해서 받기 위해 while문
            try {
               AppendText("Waiting new clients ...");
               client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
               AppendText("새로운 참가자 from " + client_socket);
               // User 당 하나씩 Thread 생성
               UserService new_user = new UserService(client_socket);
               UserVec.add(new_user); // 새로운 참가자 배열에 추가
               new_user.start(); // 만든 객체의 스레드 실행
               AppendText("현재 참가자 수 " + UserVec.size());
            } catch (IOException e) {
               AppendText("accept() error");
               // System.exit(0);
            }
         }
      }
   }

   public void AppendText(String str) {
      // textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
      textArea.append(str + "\n");
      textArea.setCaretPosition(textArea.getText().length());
   }

   public void AppendObject(ChatMsg msg) {
      // textArea.append("사용자로부터 들어온 object : " + str+"\n");
      textArea.append("code = " + msg.code + "\n");
      textArea.append("id = " + msg.UserName + "\n");
      textArea.append("data = " + msg.data + "\n");
      textArea.append("list = " + msg.al + "\n");
      
      textArea.setCaretPosition(textArea.getText().length());
   }

   // User 당 생성되는 Thread
   // Read One 에서 대기 -> Write All
   class UserService extends Thread {
      private InputStream is;
      private OutputStream os;
      private DataInputStream dis;
      private DataOutputStream dos;
      ArrayList<ChatMsg> chatList = new ArrayList<ChatMsg>();
      private ObjectInputStream ois;
      private ObjectOutputStream oos;

      private Socket client_socket;
      private Vector user_vc;
      public String UserName = "";
      public String UserStatus;

      public UserService(Socket client_socket) {
         // TODO Auto-generated constructor stub
         // 매개변수로 넘어온 자료 저장
         this.client_socket = client_socket;
         this.user_vc = UserVec;
         try {


            oos = new ObjectOutputStream(client_socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(client_socket.getInputStream());
            //여기다가 dataoutputstream과 username을 받자.
            clientsOutputStream.put(UserName,oos);

         } catch (Exception e) {
            AppendText("userService error");
         }
      }

      public void Login(String code) {
         AppendText("새로운 참가자 " + UserName + " 입장.");
         WriteOne("Welcome to Java chat server\n");
         WriteOne(UserName + "님 환영합니다.\n"); // 연결된 사용자에게 정상접속을 알림
         String msg = "[" + UserName + "]님이 입장 하였습니다.\n";
         WriteOthers(code); // 아직 user_vc에 새로 입장한 user는 포함되지 않았다.
      }

      public void Logout() {
         String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
         UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
         WriteAll(msg); // 나를 제외한 다른 User들에게 전송
         AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
         //list삭제
         //list삭제 방송 
         
      }

      // 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
      public void WriteAll(String str) {
         for (int i = 0; i < user_vc.size(); i++) {
            UserService user = (UserService) user_vc.elementAt(i);
            //if (user.UserStatus == "O")
               user.WriteOne(str);
         }
         if(str.equals("550")) { // 단톡방 생성 코드라면 방 번호 1씩 증가
           multiChatNum++;
           
         }
         
            
      }
      // 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
      public void WriteAllObject(Object ob) {
         for (int i = 0; i < user_vc.size(); i++) {
            UserService user = (UserService) user_vc.elementAt(i);
            if (user.UserStatus == "O")
               user.WriteOneObject(ob);
         }
      }

      // 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
      public void WriteOthers(String code) {
         for (int i = 0; i < user_vc.size(); i++) {
            UserService user = (UserService) user_vc.elementAt(i);
            if (user != this && user.UserStatus == "O")
               user.WriteOne(code);
         }
      }

      // Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
      public byte[] MakePacket(String msg) {
         byte[] packet = new byte[BUF_LEN];
         byte[] bb = null;
         int i;
         for (i = 0; i < BUF_LEN; i++)
            packet[i] = 0;
         try {
            bb = msg.getBytes("euc-kr");
         } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         for (i = 0; i < bb.length; i++)
            packet[i] = bb[i];
         return packet;
      }

      // UserService Thread가 담당하는 Client 에게 1:1 전송
      public void WriteOne(String msg) {
         try {
            // dos.writeUTF(msg);
//            byte[] bb;
//            bb = MakePacket(msg);
//            dos.write(bb, 0, bb.length);
            // msg 가 100이라면
            if(msg.matches("100")) { // 채팅 x 입장 o 
               ChatMsg obcm = new ChatMsg("SERVER", "100", msg);
               oos.writeObject(obcm);
            }
            if(msg.matches("101")) { // 유저 리스트 프로토콜 이라면
               String userNameListStr = String.join(",", userNameList);
               
               
               ChatMsg obcm = new ChatMsg("SERVER", "101", userNameListStr);
               obcm.al = userNameList;
               oos.writeObject(obcm);
               oos.reset();
            }
            
            if(msg.matches("550")) { // 일단 모두에게 단톡방에 초대된 사람들 리스트 보내기
                ChatMsg obcm = new ChatMsg("SERVER", "550", invitedUsersStr);
                obcm.multiChatNum=multiChatNum; //ChatMsg에 multiChatNum 설정
                
                 oos.writeObject(obcm); // 클라이언트에게 보냄
            }
            if(msg.matches("700")) {
            	 ChatMsg obcm = new ChatMsg("SERVER", "700", "profile update");
                 
                 
                  oos.writeObject(obcm); // 클라이언트에게 보냄
            }
            else {
               ChatMsg obcm = new ChatMsg("SERVER", "200", msg);
            }
            
         } catch (IOException e) {
            AppendText("dos.writeObject() error");
            try {
//               dos.close();
//               dis.close();
               ois.close();
               oos.close();
               client_socket.close();
               client_socket = null;
               ois = null;
               oos = null;
            } catch (IOException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
            Logout(); // 에러가난 현재 객체를 벡터에서 지운다
         }
      }

      // 귓속말 전송
      public void WritePrivate(String msg) {
         try {
            ChatMsg obcm = new ChatMsg("귓속말", "200", msg);
            oos.writeObject(obcm);
            
         } catch (IOException e) {
            AppendText("dos.writeObject() error");
            try {
               oos.close();
               client_socket.close();
               client_socket = null;
               ois = null;
               oos = null;
            } catch (IOException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
            Logout(); // 에러가난 현재 객체를 벡터에서 지운다
         }
      }
      public void WriteOneObject(Object ob) {
         try {
             oos.writeObject(ob);
            //oos.writeObject(userNameListStr);
         } 
         catch (IOException e) {
            AppendText("oos.writeObject(ob) error");      
            try {
               ois.close();
               oos.close();
               client_socket.close();
               client_socket = null;
               ois = null;
               oos = null;            
            } catch (IOException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
            Logout();
         }
      }
      
      public void WriteOthersObject(Object ob) {
         for(int i=0; i<user_vc.size(); i++) {
            UserService user = (UserService) user_vc.elementAt(i);
            if(user!=this) { // 나를 제외한 다른 유저들에게 
               user.WriteOneObject(ob);
            }
         }
      }
      public void sendMessage(ChatMsg cm,ArrayList<String> chatMem) {
         for(int i=0;i<chatMem.size();i++) {
            String chatMemName = chatMem.get(i);        
            for(int j=0;j<user_vc.size();j++) {
               UserService user = (UserService) user_vc.elementAt(j);
               if((user.UserName).equals(chatMemName)) {
                 // cm.code="220";
                  user.WriteOneObject(cm);                  
               }
            }
         }
         
        
      }
      public void saveProfile(String img, String userName)
      {
         
          File oldFile = new File(img);
          File newFile = new File("../JavaGameClient/src/profilesPackage/"+userName+".jpg");


          try {
              FileInputStream input = new FileInputStream(oldFile);
              FileOutputStream output = new FileOutputStream(newFile);


              byte[] buf = new byte[2048];

              int read;

              while((read = input.read(buf)) > 0)
              {
                  output.write(buf, 0, read);
              }

              input.close();
              output.close();
          }
          catch (IOException e)
          {
              e.printStackTrace();
          }
          
          
          
      }
      public void run() {
         while (true) { // 사용자 접속을 계속해서 받기 위해 while문
            try {
               Object obcm = null;
               String msg = null;
               ChatMsg cm = null;
               if (socket == null)
                  break;
               try {
                  obcm = ois.readObject();
               } catch (ClassNotFoundException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                  return;
               }
               if (obcm == null)
                  break;
               if (obcm instanceof ChatMsg) {
                  cm = (ChatMsg) obcm;
                  AppendObject(cm);
               } else
                  continue;
               if (cm.code.matches("100")) {
                  UserName = cm.UserName;
                  UserStatus = "O"; // Online 상태
                  //Login(cm.code);
                  
                  userNameList.add(cm.UserName);
                  //String userNameListStr = String.join(",", userNameList);
                  saveProfile("../JavaGameClient/src/basicProfileImg.jpg", cm.UserName);
                  
                 // WriteOthersObject(cm);
                  WriteAll("101");
               } 
               else if(cm.code.matches("101")){
               
               }
               else if(cm.code.matches("500")) { // 단톡방에 초대됐다면
                  // 단톡방에 초대된 사람 리스트: cm.al, 단톡방 아디: i
          
                
                  
                  AppendText("단톡방 생성 id:"+cm.i+"/유저들:"+cm.al);
                
                  // userNameList: a,b,c,
                  // cm.al:  a, b
                  
             
                  //여기서 단톡방 생성해줘야함. 
                  multiChatNumNmems.put(multiChatNum, cm.al); //서버에서 단톡방을 관리하기 위해서 방 넘버와 리스트를 추가함
                //al에 있는 userName 비교하면서 hashmap을 filter한 뒤에 전송
                 
                  MultiChat multiChat = new MultiChat(multiChatNum,cm.al);//number랑 list 전송
                  
                 //만약에 580으로 msg올때 그 메시지 안에는 num와 al이 있어야함. 
                  invitedUsersStr = String.join(",", cm.al);
                  //multichat도 보내야되고 multiChatNum도 보내야함.
                  WriteAll("550"); // 모두에게 일단 550 보낸다. 
                  
                  
               }
               else if(cm.code.matches("210")) {//단톡방에서 온 메시지 chatmembers에 전송
                 ArrayList<String> chatmems =multiChatNumNmems.get(cm.multiChatNum);
                 //multichatnum에 맞는 유저들을 가져옴 리스트로.
                 
                 sendMessage(cm,chatmems);
               }
               else if(cm.code.matches("580")) {
                  //단톡방 유저 확인하고  서버에 메시지 처리하고 그거를 또 객체에 보내야함. 
                  //a.sendMsg(msg); a. sendMsg(number,list);
           
                  
               }
               else if(cm.code.matches("700")) {
                   saveProfile(cm.img.toString(), cm.UserName);
                   //이미지 하나 받으면 저장.
                   //profilesPackage하나씩 보냄.
                   System.out.println("700서버에서 옴.");
                   for(int i=0;i<userNameList.size();i++) {
                	   ImageIcon icon1 = new ImageIcon("/src/profilesPackage/"+userNameList.get(i)+".jpg");
                	   profileImageIcons.add(icon1);
                   }
//                   ChatMsg profileListMsg = new ChatMsg("SERVER","700","send imageList");
//                   profileListMsg.imgList = profileImageIcons;
//                   
//                   WriteAllObject(profileListMsg);
                   WriteAll("700");
                   
                   
               }
               else if (cm.code.matches("200")) {
                  msg = String.format("[%s] %s", cm.UserName, cm.data);
                  AppendText(msg); // server 화면에 출력
                  String[] args = msg.split(" "); // 단어들을 분리한다.
                  if (args.length == 1) { // Enter key 만 들어온 경우 Wakeup 처리만 한다.
                     UserStatus = "O";
                  } else if (args[1].matches("/exit")) {
                     Logout();
                     break;
                  } else if (args[1].matches("/list")) {
                     WriteOne("User list\n");
                     WriteOne("Name\tStatus\n");
                     WriteOne("-----------------------------\n");
                     for (int i = 0; i < user_vc.size(); i++) {
                        UserService user = (UserService) user_vc.elementAt(i);
                        WriteOne(user.UserName + "\t" + user.UserStatus + "\n");
                     }
                     WriteOne("-----------------------------\n");
                  } else if (args[1].matches("/sleep")) {
                     UserStatus = "S";
                  } else if (args[1].matches("/wakeup")) {
                     UserStatus = "O";
                  } else if (args[1].matches("/to")) { // 귓속말
                     for (int i = 0; i < user_vc.size(); i++) {
                        UserService user = (UserService) user_vc.elementAt(i);
                        if (user.UserName.matches(args[2]) && user.UserStatus.matches("O")) {
                           String msg2 = "";
                           for (int j = 3; j < args.length; j++) {// 실제 message 부분
                              msg2 += args[j];
                              if (j < args.length - 1)
                                 msg2 += " ";
                           }
                           // /to 빼고.. [귓속말] [user1] Hello user2..
                           user.WritePrivate(args[0] + " " + msg2 + "\n");
                           //user.WriteOne("[귓속말] " + args[0] + " " + msg2 + "\n");
                           break;
                        }
                     }
                  } else { // 일반 채팅 메시지
                     UserStatus = "O";
                     //WriteAll(msg + "\n"); // Write All
                     WriteAllObject(cm);
                  }
               } 
               else if(cm.code.matches("300")) {
                  ArrayList<String> chatmems =multiChatNumNmems.get(cm.multiChatNum);
                  System.out.println("이모티콘보냄"+cm.img);
                  sendMessage(cm,chatmems);
               }
               else if (cm.code.matches("400")) { // logout message 처리
                  Logout();
                  break;
               }
              
               else { // 300, 500, ... 기타 object는 모두 방송한다.
                  WriteAllObject(cm);
               } 
            } catch (IOException e) {
               AppendText("ois.readObject() error");
               try {
//                  dos.close();
//                  dis.close();
                  ois.close();
                  oos.close();
                  client_socket.close();
                  Logout(); // 에러가난 현재 객체를 벡터에서 지운다
                  break;
               } catch (Exception ee) {
                  break;
               } // catch문 끝
            } // 바깥 catch문끝
         } // while
      } // run
   }

}