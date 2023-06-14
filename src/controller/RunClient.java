package controller;

import controller.SocketHandler;
import model.User;
import view.LoginFrm;
import view.WaitingFrm;
import view.RegisterUserFrm;
import view.MainMenuFrm;
import view.RankFrm;
import view.ProfileFrm;
import view.FriendFrm;
import view.CreateRoomPasswordFrm;
import view.ChillFrm;
import view.CompetitorInfoFrm;
import view.InGameFrm;
import view.JoinRoomPasswordFrm;
import view.ChangePassFrm;
import view.FriendRequestFrm;

public class RunClient {
    public enum View{
        LOGIN,
        REGISTER,
        HOME,
        FRIENDLIST,
        GAMECLIENT,
        CREATEROOMPASSWORD,
        JOINROOMPASSWORD,
        COMPETITORINFO,
        RANK,
        WAITINGVERIFY,
        FRIENDREQUEST,
        GAMEAI,
        ROOMNAMEFRM,
        PROFILE,
        CHILLROOM,
        CPASS
    }
    public static SocketHandler socketHandle;
    public static User user;
    //Danh sách giao diện
    public static LoginFrm loginFrm;
    public static RegisterUserFrm registeruserFrm;
    public static MainMenuFrm mainmenuFrm;
    public static RankFrm rankFrm;
    public static ProfileFrm profileFrm;
    public static FriendFrm friendFrm;
    public static ChillFrm chillFrm;
    public static WaitingFrm waitingFrm;
    public static InGameFrm ingameFrm;
    public static CreateRoomPasswordFrm createRoomPasswordFrm;
    public static JoinRoomPasswordFrm joinRoomPasswordFrm;
    public static CompetitorInfoFrm competitorInfoFrm;
    public static ChangePassFrm changepassFrm;
    public static FriendRequestFrm friendRequestFrm;
//    public static GameAIFrm gameAIFrm;
    public void initView(){
        loginFrm = new LoginFrm();
        loginFrm.setVisible(true);
        socketHandle = new SocketHandler();
        socketHandle.run();
    }
    public static void openView(View viewName){
        if(viewName != null){
            switch(viewName){
                case LOGIN:
                    loginFrm = new LoginFrm();
                    loginFrm.setVisible(true);
                    break;
                case REGISTER:
                    registeruserFrm = new RegisterUserFrm();
                    registeruserFrm.setVisible(true);
                    break;
                case HOME:
                    mainmenuFrm = new MainMenuFrm();
                    mainmenuFrm.setVisible(true);
                    break;
                case RANK:
                    rankFrm = new RankFrm();
                    rankFrm.setVisible(true);
                    break;
                case PROFILE:
                    profileFrm = new ProfileFrm();
                    profileFrm.setVisible(true);
                    break;
                case FRIENDLIST:
                    friendFrm = new FriendFrm();
                    friendFrm.setVisible(true);
                    break;
                case CHILLROOM:
                    chillFrm = new ChillFrm();
                    chillFrm.setVisible(true);
                    break;         
                case CREATEROOMPASSWORD:
                    createRoomPasswordFrm = new CreateRoomPasswordFrm();
                    createRoomPasswordFrm.setVisible(true);
                    break;
                case CPASS:
                    changepassFrm = new ChangePassFrm();
                    changepassFrm.setVisible(true);
                    break;                   
//                case GAMEAI:
//                    gameAIFrm = new GameAIFrm();
//                    gameAIFrm.setVisible(true);
//                    break;
            }
        }
    }
    public static void openView(View viewName, String arg1, String arg2){
        if(viewName != null){
            switch(viewName){
                case WAITINGVERIFY:
                    waitingFrm = new WaitingFrm(arg1, arg2);
                    waitingFrm.setVisible(true);
                    break;
                case LOGIN:
                    loginFrm = new LoginFrm(arg1, arg2);
                    loginFrm.setVisible(true);
                    break;
                case CPASS:
                    changepassFrm = new ChangePassFrm(arg1, arg2);
                    changepassFrm.setVisible(true); 
                    break;
            }
        }
    }
    public static void openView(View viewName,User user){
        if(viewName != null){
            switch(viewName){
                case COMPETITORINFO:
                    competitorInfoFrm=new CompetitorInfoFrm(user);
                    competitorInfoFrm.setVisible(true);
                    break;
            }
        }
    }
    public static void openView(View viewName, User competitor, int room_ID, int isStart){
        if(viewName != null){
            switch(viewName){
                case GAMECLIENT:
                    ingameFrm = new InGameFrm(competitor, room_ID, isStart);
                    ingameFrm.setVisible(true);
                    break;
            }
        }
    }
    public static void openView(View viewName, int arg1, String arg2){
        if(viewName != null){
            switch(viewName){
                case JOINROOMPASSWORD:
                    joinRoomPasswordFrm = new JoinRoomPasswordFrm(arg1, arg2);
                    joinRoomPasswordFrm.setVisible(true);
                    break;
                case FRIENDREQUEST:
                    friendRequestFrm = new FriendRequestFrm(arg1, arg2);
                    friendRequestFrm.setVisible(true);
            }
        }
    }
    
    public static void closeView(View viewName){
        if(viewName != null){
            switch(viewName){
                case LOGIN:
                    loginFrm.dispose();
                    break;
                case REGISTER:
                    registeruserFrm.dispose();
                    break;
                case HOME:
                    mainmenuFrm.dispose();
                    break;
                case RANK:
                    rankFrm.dispose();
                    break; 
                case PROFILE:
                    profileFrm.dispose();
                    break;                     
                case CHILLROOM:
                    chillFrm.dispose();
                    break;
                case FRIENDLIST:
                    friendFrm.stopAllThread();
                    friendFrm.dispose();
                    break;
                case CPASS:
                    changepassFrm.dispose();
                    break;
                case GAMECLIENT:
                    ingameFrm.stopAllThread();
                    ingameFrm.dispose();
                    break;
                case CREATEROOMPASSWORD:
                    createRoomPasswordFrm.dispose();
                    break;
                case JOINROOMPASSWORD:
                    joinRoomPasswordFrm.dispose();
                    break;
                case COMPETITORINFO:
                    competitorInfoFrm.dispose();
                    break;
                case WAITINGVERIFY:
                    waitingFrm.dispose();
                    break;
                case FRIENDREQUEST:
                    friendRequestFrm.dispose();
                    break;
//                case GAMEAI:
//                    gameAIFrm.dispose();
//                    break;
            }
            
        }
    }
    public static void closeAllViews(){
        if(loginFrm!=null) loginFrm.dispose();
        if(registeruserFrm!=null) registeruserFrm.dispose();
        if(mainmenuFrm!=null) 
        {
            mainmenuFrm.dispose();
            if(mainmenuFrm.timer != null)
                mainmenuFrm.timer.stop();
            mainmenuFrm.stopAllThread();
        }
        if(rankFrm!=null) rankFrm.dispose();
        if(friendFrm!=null){
            friendFrm.stopAllThread();
            friendFrm.dispose();
        }
        if(profileFrm!=null) profileFrm.dispose();
        if(chillFrm!=null) chillFrm.dispose();
        if(changepassFrm!=null) changepassFrm.dispose();
        if(waitingFrm!=null) waitingFrm.dispose();
        if(ingameFrm!=null){
            ingameFrm.stopAllThread();
            ingameFrm.dispose();
       } 
        if(createRoomPasswordFrm!=null) createRoomPasswordFrm.dispose();
        if(joinRoomPasswordFrm!=null) joinRoomPasswordFrm.dispose();
        if(competitorInfoFrm!=null) competitorInfoFrm.dispose();

        if(friendRequestFrm!=null) friendRequestFrm.dispose();
//        if(gameAIFrm!=null) gameAIFrm.dispose();
    }
    public static void main(String[] args) {
        new RunClient().initView();
    }
}
