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
import view.InGameFrm;

public class RunClient {
    public enum View{
        LOGIN,
        REGISTER,
        HOME,
        ROOMLIST,
        FRIENDLIST,
        FINDROOM,
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
        CHILLROOM
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
//    public static JoinRoomPasswordFrm joinRoomPasswordFrm;
//    public static CompetitorInfoFrm competitorInfoFrm;

//    public static GameNoticeFrm gameNoticeFrm;
//    public static FriendRequestFrm friendRequestFrm;
//    public static GameAIFrm gameAIFrm;
//    public static RoomNameFrm roomNameFrm;
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
//                case FINDROOM:
//                    findRoomFrm = new FindRoomFrm();
//                    findRoomFrm.setVisible(true);
//                    break;
                case CHILLROOM:
                    chillFrm = new ChillFrm();
                    chillFrm.setVisible(true);
                    break;
                case WAITINGVERIFY:
                    waitingFrm = new WaitingFrm();
                    waitingFrm.setVisible(true);
                    break;                
                case CREATEROOMPASSWORD:
                    createRoomPasswordFrm = new CreateRoomPasswordFrm();
                    createRoomPasswordFrm.setVisible(true);
                    break;

//                case GAMEAI:
//                    gameAIFrm = new GameAIFrm();
//                    gameAIFrm.setVisible(true);
//                    break;
//                case ROOMNAMEFRM:
//                    roomNameFrm = new RoomNameFrm();
//                    roomNameFrm.setVisible(true);
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
//                case ROOMLIST:
//                    roomListFrm.dispose();
//                    break;
                case CHILLROOM:
                    chillFrm.dispose();
                    break;
                case FRIENDLIST:
                    friendFrm.stopAllThread();
                    friendFrm.dispose();
                    break;
//                case FINDROOM:
//                    findRoomFrm.stopAllThread();
//                    findRoomFrm.dispose();
//                    break;
//                case WAITINGROOM:
//                    waitingRoomFrm.dispose();
//                    break;
//                case GAMECLIENT:
//                    gameClientFrm.stopAllThread();
//                    gameClientFrm.dispose();
//                    break;
                case CREATEROOMPASSWORD:
                    createRoomPasswordFrm.dispose();
                    break;
//                case JOINROOMPASSWORD:
//                    joinRoomPasswordFrm.dispose();
//                    break;
//                case COMPETITORINFO:
//                    competitorInfoFrm.dispose();
//                    break;
//                case GAMENOTICE:
//                    gameNoticeFrm.dispose();
//                    break;
//                case FRIENDREQUEST:
//                    friendRequestFrm.dispose();
//                    break;
//                case GAMEAI:
//                    gameAIFrm.dispose();
//                    break;
//                case ROOMNAMEFRM:
//                    roomNameFrm.dispose();
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
                mainmenuFrm.stopAllThread();
        }
        if(rankFrm!=null) rankFrm.dispose();
        if(profileFrm!=null) 
        {
            profileFrm.dispose();
        }
//        if(roomListFrm!=null) roomListFrm.dispose();
        if(friendFrm!=null){
            friendFrm.stopAllThread();
            friendFrm.dispose();
        }
        if(profileFrm!=null) profileFrm.dispose();
        if(chillFrm!=null) chillFrm.dispose();
//        if(findRoomFrm!=null){
//            findRoomFrm.stopAllThread();
//            findRoomFrm.dispose();
//        } 
//        if(waitingRoomFrm!=null) waitingRoomFrm.dispose();
//        if(gameClientFrm!=null){
//            gameClientFrm.stopAllThread();
//            gameClientFrm.dispose();
//        } 
        if(createRoomPasswordFrm!=null) createRoomPasswordFrm.dispose();
//        if(joinRoomPasswordFrm!=null) joinRoomPasswordFrm.dispose();
//        if(competitorInfoFrm!=null) competitorInfoFrm.dispose();

//        if(gameNoticeFrm!=null) gameNoticeFrm.dispose();
//        if(friendRequestFrm!=null) friendRequestFrm.dispose();
//        if(gameAIFrm!=null) gameAIFrm.dispose();
//        if(roomNameFrm!=null) roomNameFrm.dispose();
    }
      public static void main(String[] args) {
        new RunClient().initView();
    }
}
