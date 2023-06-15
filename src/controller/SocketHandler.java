package controller;

import static controller.RunClient.ingameFrm;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import model.User;

public class SocketHandler implements Runnable{
    private BufferedWriter os;
    private BufferedReader is;
    private Socket socketOfClient;
    private int ID_Server;
    
    @Override
    public void run() {

        try {
            // Gửi yêu cầu kết nối tới Server đang lắng nghe
            socketOfClient = new Socket("192.168.50.37", 7777);
            System.out.println("Kết nối thành công!");
            // Tạo luồng đầu ra tại client (Gửi dữ liệu tới server)
            os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
            // Luồng đầu vào tại Client (Nhận dữ liệu từ server).
            is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
            String message;
            // Bắt đầu nhận dữ liệu
            while (true) {
                message = is.readLine();
                if (message == null) {
                    break;
                }
                String[] messageSplit = message.split(",");
                if(messageSplit[0].equals("server-send-id")){
                    ID_Server = Integer.parseInt(messageSplit[1]);
                    System.out.println(messageSplit[1]);
                }
                if(messageSplit[0].equals("login-success")){
                    System.out.println("Đăng nhập thành công");
                    RunClient.closeAllViews();
                    User user= getUserFromString(1,messageSplit);
                    RunClient.user = user;
                    RunClient.openView(RunClient.View.HOME);
                }
                //Tài khoản đã đăng nhập ở nơi khác
                if(messageSplit[0].equals("dupplicate-login")){
                    System.out.println("Đã đăng nhập");
                    RunClient.closeView(RunClient.View.WAITINGVERIFY);
                    RunClient.openView(RunClient.View.LOGIN,messageSplit[1],messageSplit[2]);
                    RunClient.loginFrm.showError("Your account is currently being accessed from another location.");
                }
                //Mật khẩu không đúng
                if(messageSplit[0].equals("duppassword")){
                    System.out.println("Mật khẩu không đúng");
                    RunClient.closeView(RunClient.View.WAITINGVERIFY);
                    RunClient.openView(RunClient.View.CPASS,messageSplit[1],messageSplit[2]);
                    RunClient.changepassFrm.showError("Wrong password");
                }
                //Xử lý register trùng tên
                if(messageSplit[0].equals("duplicate-username")){
                    RunClient.closeAllViews();
                    RunClient.openView(RunClient.View.REGISTER);
                    JOptionPane.showMessageDialog(RunClient.registeruserFrm, "The username is already in use by someone else.");
                }
                //Xử lý nhận thông tin, chat từ toàn server
                if(messageSplit[0].equals("chat-server")){
                    if(RunClient.mainmenuFrm!=null){
                        RunClient.mainmenuFrm.addMessage(messageSplit[1]);
                    }
                }
                //Tài khoản đã bị banned
                if(messageSplit[0].equals("banned-user")){
                    RunClient.closeView(RunClient.View.WAITINGVERIFY);
                    RunClient.openView(RunClient.View.LOGIN,messageSplit[1],messageSplit[2]);
                    RunClient.loginFrm.showError("Account have been banned");
                }
                //Đăng nhập sai mật khẩu hoặc tên đăng nhập
                 if(messageSplit[0].equals("wrong-user")){
                    System.out.println("Thông tin sai");
                    RunClient.closeView(RunClient.View.WAITINGVERIFY);
                    RunClient.openView(RunClient.View.LOGIN,messageSplit[1],messageSplit[2]);
                    RunClient.loginFrm.showError("Wrong username or password");
                }
                if(messageSplit[0].equals("return-get-rank-charts")){
                    if(RunClient.rankFrm!=null){
                        RunClient.rankFrm.setDataToTable(getListRank(messageSplit));
                    }
                }
                //Lấy danh sách bạn bè
                if(messageSplit[0].equals("return-friend-list")){
                    if(RunClient.friendFrm!=null){
                        RunClient.friendFrm.updateFriendList(getListUser(messageSplit));
                    }
                }
                 //Lấy danh sách bạn bè
                if(messageSplit[0].equals("user-list")){
                    if(RunClient.mainmenuFrm!=null){
                        RunClient.mainmenuFrm.updateuser(getListUsers(messageSplit));
                    }
                }
                //Lấy danh sách phòng
                if(messageSplit[0].equals("room-list")){
                    Vector<Integer> rooms = new Vector<>();
                    Vector<String> hostroom = new Vector<>();
                    Vector<String> password = new Vector<>();
                    for(int i=1; i<messageSplit.length; i=i+3){
                        rooms.add(Integer.parseInt(messageSplit[i]));
                        hostroom.add(messageSplit[i+1]);
                        password.add(messageSplit[i+2]);
                    }
                    RunClient.mainmenuFrm.updateRoomList(rooms, hostroom ,password);
                }
                 //Xử lý yêu cầu kết bạn tới
                if(messageSplit[0].equals("make-friend-request")){
                    int ID = Integer.parseInt(messageSplit[1]);
                    String nickname = messageSplit[2];
                    RunClient.openView(RunClient.View.FRIENDREQUEST, ID, nickname);
                }
                 //Xử lý yêu cầu kết bạn tới
                if(messageSplit[0].equals("Friend-request-accepted")){
                    if(RunClient.competitorInfoFrm!=null)
                        RunClient.competitorInfoFrm.acceptedfriend();
                }
                //Xử lý hiển thị thông tin đối thủ là bạn bè/không
                if(messageSplit[0].equals("check-friend-response")){
                    if(RunClient.competitorInfoFrm!=null){
                        RunClient.competitorInfoFrm.checkFriend((messageSplit[1].equals("1")));
                    }
                }
                //Tạo phòng và server trả về tên phòng
                if(messageSplit[0].equals("your-created-room")){
                    RunClient.closeAllViews();
                    RunClient.openView(RunClient.View.CHILLROOM);
                    RunClient.chillFrm.setRoomName(messageSplit[1]);
                    if(messageSplit.length==3)
                        RunClient.chillFrm.setRoomPassword("Password: "+messageSplit[2]);
                }
                 if(messageSplit[0].equals("go-to-room")){
                    System.out.println("Vào phòng");
                    int roomID = Integer.parseInt(messageSplit[1]);
                    int isStart = Integer.parseInt(messageSplit[2]);
                    User competitor = getUserFromString(3, messageSplit);
                    if(RunClient.chillFrm!=null){
                        RunClient.chillFrm.showFindedCompetitor();
                        
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException ex) {
                            JOptionPane.showMessageDialog(RunClient.chillFrm, "Lỗi khi sleep thread");
                        }
                    }
                    else{
                        RunClient.mainmenuFrm.setAllVisibleFalse();
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException ex) {
                            JOptionPane.showMessageDialog(RunClient.mainmenuFrm, "Lỗi khi sleep thread");
                        }
                    }
                    RunClient.closeAllViews();
                    System.out.println("Đã vào phòng: "+roomID);
                    //Xử lý vào phòng
                    RunClient.openView(RunClient.View.GAMECLIENT
                            , competitor
                            , roomID
                            ,isStart
                            );
                    RunClient.ingameFrm.newgame();
                }
                if(messageSplit[0].equals("Found-room")){
                     RunClient.mainmenuFrm.showFindedRoom();
                }
                if(messageSplit[0].equals("left-room")){
                    //RunClient.gameClientFrm.stopTimer();
                    RunClient.closeAllViews();
                    RunClient.openView(RunClient.View.WAITINGVERIFY,"The opponent have quit","Return to mainmenu");
                    Thread.sleep(3000);       
                    RunClient.closeAllViews();
                    RunClient.openView(RunClient.View.HOME);
                }
                if(messageSplit[0].equals("new-game")){
                    System.out.println("New game");
                    Thread.sleep(4000);
                    RunClient.ingameFrm.updateNumberOfGame();
                    RunClient.closeView(RunClient.View.WAITINGVERIFY);
                    RunClient.ingameFrm.numberOfMatch++;
                    RunClient.ingameFrm.newgame();
                }
                //Xử lý đánh một nước trong ván chơi
                if(messageSplit[0].equals("caro")){
                    RunClient.ingameFrm.addCompetitorMove(messageSplit[1], messageSplit[2]);
                }
                if(messageSplit[0].equals("chat")){
                    RunClient.ingameFrm.addMessage(messageSplit[1]);
                }
                if(messageSplit[0].equals("draw-request")){
                    RunClient.ingameFrm.showDrawRequest();
                }
                if(messageSplit[0].equals("draw-refuse")){
                    if(RunClient.waitingFrm!=null) 
                        RunClient.closeView(RunClient.View.WAITINGVERIFY);
                    RunClient.ingameFrm.displayDrawRefuse();
                }
                if(messageSplit[0].equals("draw-game")){
                    RunClient.closeView(RunClient.View.WAITINGVERIFY);
                    RunClient.openView(RunClient.View.WAITINGVERIFY, "Draw", "A new game being setup");
                    RunClient.ingameFrm.displayDrawGame();
                    Thread.sleep(4000);
                    RunClient.ingameFrm.updateNumberOfGame();
                    RunClient.closeView(RunClient.View.WAITINGVERIFY);
                    RunClient.ingameFrm.newgame();
                }
                if(messageSplit[0].equals("undo-request")){
                    RunClient.ingameFrm.showUndoRequest();
                }
                if(messageSplit[0].equals("undo-refuse")){
                    if(RunClient.waitingFrm!=null) 
                        RunClient.closeView(RunClient.View.WAITINGVERIFY);
                    RunClient.ingameFrm.displayUndoRefuse();
                }
                if(messageSplit[0].equals("undo-game")){
                    RunClient.closeView(RunClient.View.WAITINGVERIFY);
                    RunClient.ingameFrm.undo();
                }
                
                if(messageSplit[0].equals("banned-notice")){
                    RunClient.socketHandle.write("offline,"+RunClient.user.getID());
                    RunClient.closeAllViews();
                    RunClient.openView(RunClient.View.LOGIN);
                    JOptionPane.showMessageDialog(RunClient.loginFrm, messageSplit[1], "you have been banned", JOptionPane.WARNING_MESSAGE);
                }
                //Xử lý cảnh cáo
                if(messageSplit[0].equals("warning-notice")){
                    JOptionPane.showMessageDialog(null, messageSplit[1] , "warnning", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    public User getUserFromString(int start, String[] message){
        return new User(Integer.parseInt(message[start]),
                message[start+1],
                message[start+2],
                message[start+3],
                message[start+4],
                Integer.parseInt(message[start+5]),
                Integer.parseInt(message[start+6]),
                Integer.parseInt(message[start+7]),
                Integer.parseInt(message[start+8]));
    }
    
    
    public List<User> getListRank(String[] message){
        List<User> friend = new ArrayList<>();
        for(int i=1; i<message.length; i=i+9){
            friend.add(new User(Integer.parseInt(message[i]),
                message[i+1],
                message[i+2],
                message[i+3],
                message[i+4],
                Integer.parseInt(message[i+5]),
                Integer.parseInt(message[i+6]),
                Integer.parseInt(message[i+7]),
                Integer.parseInt(message[i+8])));
        }
        return friend;
    }
    public List<User> getListUser(String[] message){
        List<User> friend = new ArrayList<>();
        for(int i=1; i<message.length; i=i+4){
            friend.add(new User(Integer.parseInt(message[i]),
                    message[i+1],
                    message[i+2].equals("1"),
                    message[i+3].equals("1")));
        }
        return friend;
    }
    public List<User> getListUsers(String[] message){
        List<User> user = new ArrayList<>();
        for(int i=1; i<message.length; i=i+9){
            user.add(new User(Integer.parseInt(message[i]),
                message[i+1],
                message[i+2],
                message[i+3],
                message[i+4],
                Integer.parseInt(message[i+5]),
                Integer.parseInt(message[i+6]),
                Integer.parseInt(message[i+7]),
                Integer.parseInt(message[i+8])));
        }
        return user;
    }
    
    
    public void write(String message) throws IOException{
        os.write(message);
        os.newLine();
        os.flush();
    }

    public Socket getSocketOfClient() {
        return socketOfClient;
    }

}
