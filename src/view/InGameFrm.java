package view;

import controller.RunClient;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import model.State;
import model.User;
import model.Value;

public class InGameFrm extends javax.swing.JFrame {

    private JButton[][] button;
    private State root;
    
    private User competitor;
    
    private Timer timer;
    private Integer second, minute;
    
    private int xUndo[] = new int[Value.SIZE*Value.SIZE];
    private int yUndo[] = new int[Value.SIZE*Value.SIZE];
    
    private String normalItem[];
    private String winItem[];
    private String preItem[];
    
    private JButton preButton;
    private int userWin;
    private int competitorWin;

    public int numberOfMatch;
    public InGameFrm(User competitor,int room_ID, int isStart) {
        initComponents();
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//        } catch (Exception e) {
//
//        }
        this.competitor = competitor;
        numberOfMatch = isStart;
        root = new State();
        //init score
        userWin = 0;
        competitorWin = 0;
        
        this.setTitle("Phòng: "+room_ID+" ("+RunClient.user.getUsername()+")");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        NameUser.setText(RunClient.user.getNickname());
        NameCompetitor.setText(competitor.getNickname());
        avatar1.setIcon(new ImageIcon(new ImageIcon("src/assets/avatar/"+RunClient.user.getAvatar()+".png").getImage().getScaledInstance(avatar1.getWidth(), avatar1.getHeight(),Image.SCALE_SMOOTH)));
        avatar2.setIcon(new ImageIcon(new ImageIcon("src/assets/avatar/"+competitor.getAvatar()+".png").getImage().getScaledInstance(avatar2.getWidth(), avatar2.getHeight(),Image.SCALE_SMOOTH)));
        
        //Setup timer
        second = 0;
        minute = 1;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = minute.toString();
                String temp1 = second.toString();
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                if (temp1.length() == 1) {
                    temp1 = "0" + temp1;
                }
                LabelTimer.setText(temp + ":" + temp1);
                if (second == 0 && minute == 0) {
                    ((Timer) (e.getSource())).stop();
                    second = 0; // check lại xem có nên bỏ không?
                    minute = 1;
                    try {
                        RunClient.openView(RunClient.View.WAITINGVERIFY, "Bạn đã thua do quá thời gian", "Đang thiết lập ván chơi mới");
                        increaseWinMatchToCompetitor();
                        RunClient.socketHandle.write("lose,");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(rootPane, ex.getMessage());
                    }
                } else if (minute > 0 && second == 0) {
                    second = 59;
                    minute--;
                } else {
                    second--;
                }
            }

        });
        
        //Setup icon (odd for Oicon and even for Xicon)
        normalItem = new String[2];
        normalItem[1] = "src/assets/Oicon1.png";
        normalItem[0] = "src/assets/Xicon1.png";
        winItem = new String[2];
        winItem[1] = "src/assets/winOicon.png";
        winItem[0] = "src/assets/winXicon.png";
        preItem = new String[2];
        preItem[1] = "src/assets/preOicon1.png";
        preItem[0] = "src/assets/preXicon1.png";
        
        this.getContentPane().setLayout(null);
        PanelBanco.setLayout(new GridLayout(Value.SIZE, Value.SIZE));
        button = new JButton[Value.SIZE][Value.SIZE];
        for (int i = 0; i < Value.SIZE; i++) {
            for (int j = 0; j < Value.SIZE; j++) {
                button[i][j] = new JButton("");
                button[i][j].setBackground(Color.WHITE);
                button[i][j].setOpaque(true);
                PanelBanco.add(button[i][j]);
            }
        }
        setupButton();
        setEnableButton(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitGame();
            }
        });

    }
    public void exitGame() {
        try {
            timer.stop();
            RunClient.socketHandle.write("left-room,");
            RunClient.closeAllViews();
            RunClient.openView(RunClient.View.HOME);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
        RunClient.closeAllViews();
        RunClient.openView(RunClient.View.HOME);
    }
    public void stopAllThread(){
        timer.stop();
    }
    void setupButton() {
        for (int i = 0; i < Value.SIZE; i++) {
            for (int j = 0; j < Value.SIZE; j++) {
                final int a = i, b = j;

                button[a][b].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            button[a][b].setDisabledIcon(new ImageIcon(normalItem[(numberOfMatch % 2)]));
                            xUndo[root.steps] = a;
                            yUndo[root.steps] = b;                           
                            root.update(a, b, Value.USER_VALUE);
                            button[a][b].setEnabled(false);
                            try {
                                if(root.isOver()){
                                    RunClient.socketHandle.write("draw-confirm,");
                                }
                                else if (root.checkWinner(button,Value.USER_VALUE)==1) {
                                    //Xử lý khi người chơi này thắng
                                    for(JButton jButton: root.getlist()){
                                        jButton.setIcon(new ImageIcon(winItem[numberOfMatch % 2]));
                                        jButton.setDisabledIcon(new ImageIcon(winItem[numberOfMatch % 2]));
                                    }
                                    increaseWinMatchToUser();
                                    RunClient.socketHandle.write("win,"+a+","+b);
                                    RunClient.openView(RunClient.View.CHILLROOM,"You win","Creating a newgame");
                                    timer.stop();
                                }
                                else{
                                    RunClient.socketHandle.write("caro," + a + "," + b);
                                    displayCompetitorTurn();
                                    timer.stop();
                                }
                            } catch (Exception ie) {
                                ie.printStackTrace();
                            }
                            setEnableButton(false);

                        }
                        catch(Exception ex){
                            
                        }
                    }
                });
                button[a][b].addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        if(button[a][b].isEnabled()) {
                            button[a][b].setIcon(new ImageIcon(normalItem[(numberOfMatch % 2)]));
                        }
                    }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        if(button[a][b].isEnabled()){
                            button[a][b].setIcon(null);
                        }
                    }
                });
            }
        }
    }
    public void newgame() {
        
        if (numberOfMatch % 2 == 0) {
            JOptionPane.showMessageDialog(rootPane, "You go first");
            displayUserTurn();
            startTimer();
        } else {
            JOptionPane.showMessageDialog(rootPane, "The opponent go fisrt ");
            displayCompetitorTurn();
        }
        for (int i = 0; i < Value.SIZE; i++) {
            for (int j = 0; j < Value.SIZE; j++) {
                button[i][j].setIcon(new ImageIcon("src/assets/new.png"));
                button[i][j].setDisabledIcon(new ImageIcon("src/assets/new.png"));
                button[i][j].setText("");
            }
        }
        root = new State();
        //IF Enemy Turn 
        if(numberOfMatch % 2 != 0){
            setEnableButton(false);
        }
        else{
            setEnableButton(true);
        }
        IconUser.setIcon(new ImageIcon(normalItem[numberOfMatch % 2]));
        IconCompetitor.setIcon(new ImageIcon(normalItem[not(numberOfMatch % 2)]));
        preButton = null;
    }
    public void startTimer(){
        second = 0;
        minute = 1;
        timer.start();
    }
    public void displayUserTurn(){
        LabTextTurn.setText("My Turn");
//        btnUndo.setEnabled(false);
        LabelTimer.setVisible(true);
        if(numberOfMatch % 2==0){
            LabTextTurn.setForeground(Color.RED);
        }
        else{
            LabTextTurn.setForeground(Color.GREEN);
        }
        LabIconTurn.setIcon(new ImageIcon(normalItem[(numberOfMatch % 2)])); 
    }
    public void displayCompetitorTurn() {
        LabTextTurn.setText("Enemy Turn");
//        btnUndo.setEnabled(true);
        LabelTimer.setVisible(false);
        if(not(numberOfMatch % 2)==0){
            LabTextTurn.setForeground(Color.RED);
        }
        else{
            LabTextTurn.setForeground(Color.GREEN);
        }
        LabIconTurn.setIcon(new ImageIcon(normalItem[not(numberOfMatch % 2)]));
    }
    public void displayDrawGame(){
        String tmp = jTextArea1.getText();
        tmp += "--Draw match--\n";
        jTextArea1.setText(tmp);
    }
    public void displayDrawRefuse(){
        JOptionPane.showMessageDialog(rootPane, "The opponent does not accept a draw, please continue to play");
        timer.start();
        setEnableButton(true);
    }
    public void displayUndoRefuse(){
        JOptionPane.showMessageDialog(rootPane, "The opponent does not accept undo, please continue to play");
        timer.start();
        setEnableButton(true);
    }
    public void addMessage(String message){
        String temp = jTextArea1.getText();
        temp += competitor.getNickname() + ": " + message+"\n";
        jTextArea1.setText(temp);
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
    }
    public void setEnableButton(boolean b) {
        for (int i = 0; i < Value.SIZE; i++) {
            for (int j = 0; j < Value.SIZE; j++) {
                if (root.getState()[i][j] == 0) {
                    button[i][j].setEnabled(b);
                    button[i][j].setIcon(null);
                    button[i][j].setDisabledIcon(null);
                }
            }
        }
    }
    int not(int i) {
        if (i == 1) {
            return 0;
        }
        if (i == 0) {
            return 1;
        }
        return 0;
    }
    public void updateNumberOfGame(){
        competitor.setNumberOfGame(competitor.getNumberOfGame() + 1);
        RunClient.user.setNumberOfGame(RunClient.user.getNumberOfGame() + 1);
    }
    public void increaseWinMatchToUser(){
        RunClient.user.setNumberOfwin(RunClient.user.getNumberOfwin()+1);
        userWin++;
        LabelTiso.setText("Score: "+userWin+"-"+competitorWin);
        JOptionPane.showMessageDialog(rootPane, 
                        "--You won, the score is now "+userWin+"-"+competitorWin+"--");
    }
    public void increaseWinMatchToCompetitor(){
        competitor.setNumberOfwin(competitor.getNumberOfwin()+1);
        competitorWin++;
        LabelTiso.setText("Score: "+userWin+"-"+competitorWin);
        JOptionPane.showMessageDialog(rootPane, 
                        "--You lose, the score is now "+userWin+"-"+competitorWin+"--");
    }
    public void showDrawRequest() {
        int res = JOptionPane.showConfirmDialog(rootPane, "The opponent wants to draw this game, do you agree?", "Draw request", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            try {
                timer.stop();
                setEnableButton(false);
                RunClient.socketHandle.write("draw-confirm,");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            }
        }
        else{
            try {
                RunClient.socketHandle.write("draw-refuse,");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            }
        }
    }
    public void showUndoRequest() {
        int res = JOptionPane.showConfirmDialog(rootPane, "The opponent wants to undo their move , do you agree?", "Undo request", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            try {
                timer.stop();
                setEnableButton(false);
                RunClient.socketHandle.write("undo-confirm,");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            }
        }
        else{
            try {
                RunClient.socketHandle.write("undo-refuse,");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            }
        }
    }
    public void undo() {
	if (root.steps > 0) {
            button[xUndo[root.steps - 1]][yUndo[root.steps - 1]].setText("");
            button[xUndo[root.steps - 1]][yUndo[root.steps - 1]].setIcon(null);
            button[xUndo[root.steps - 1]][yUndo[root.steps - 1]].setDisabledIcon(null);
            root.updatestate(xUndo[root.steps - 1], yUndo[root.steps - 1]);
            if(preButton!=null){
                preButton.setIcon(new ImageIcon(normalItem[not(numberOfMatch % 2)]));
                preButton.setDisabledIcon(new ImageIcon(normalItem[not(numberOfMatch % 2)]));
            }
            preButton = null;
            if (LabTextTurn.getText().equals("My Turn")){
                displayCompetitorTurn();
                timer.stop();         
                setEnableButton(false);

            } 
            else {
                displayUserTurn();
                startTimer();
                setEnableButton(true);
            }
                
            root.steps--;
//            if (root.steps == 0)
//		btnUndo.setEnabled(false);
        }
    }
    
    public void addCompetitorMove(String x, String y){
        displayUserTurn();
        startTimer();
        setEnableButton(true);
        caro(x, y);
    }
    
    
    public void caro(String x, String y) {
        int xx = Integer.parseInt(x);
        int yy = Integer.parseInt(y);
        // danh dau vi tri danh
        xUndo[root.steps] = xx;
        yUndo[root.steps] = yy;   
        root.update(xx, yy, Value.COMPETITOR_VALUE);
        button[xx][yy].setEnabled(false);
        //playSound1();
        //Set bình thường về như cũ
        if(preButton!=null){
            preButton.setIcon(new ImageIcon(normalItem[not(numberOfMatch % 2)]));
            preButton.setDisabledIcon(new ImageIcon(normalItem[not(numberOfMatch % 2)]));
        }
        preButton = button[xx][yy];
        
        //set button preitem
        button[xx][yy].setIcon(new ImageIcon(preItem[not(numberOfMatch % 2)]));
        button[xx][yy].setDisabledIcon(new ImageIcon(preItem[not(numberOfMatch % 2)]));
        if(root.isOver()){
            try {
                RunClient.socketHandle.write("draw-confirm,");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if(root.checkWinner(button,Value.COMPETITOR_VALUE)==1){
            for(JButton jButton: root.getlist()){
                jButton.setIcon(new ImageIcon(winItem[not(numberOfMatch % 2)]));
                jButton.setDisabledIcon(new ImageIcon(winItem[not(numberOfMatch % 2)]));
            }
            timer.stop();
            setEnableButton(false);
            increaseWinMatchToCompetitor();
            RunClient.openView(RunClient.View.CHILLROOM,"You lose","Creating a new game");
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelBanco = new javax.swing.JPanel();
        PanelRight = new javax.swing.JPanel();
        Pnguoichoi = new javax.swing.JPanel();
        avatar2 = new javax.swing.JLabel();
        avatar1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        IconUser = new javax.swing.JLabel();
        IconCompetitor = new javax.swing.JLabel();
        NameUser = new javax.swing.JLabel();
        NameCompetitor = new javax.swing.JLabel();
        LabelTiso = new javax.swing.JLabel();
        Pchucnang = new javax.swing.JPanel();
        btnUndo = new javax.swing.JButton();
        btnhoa = new javax.swing.JButton();
        buttonExit = new javax.swing.JButton();
        Pthoigian = new javax.swing.JPanel();
        LabelTimer = new javax.swing.JLabel();
        LabIconTurn = new javax.swing.JLabel();
        LabTextTurn = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        PanelBanco.setPreferredSize(new java.awt.Dimension(600, 600));

        javax.swing.GroupLayout PanelBancoLayout = new javax.swing.GroupLayout(PanelBanco);
        PanelBanco.setLayout(PanelBancoLayout);
        PanelBancoLayout.setHorizontalGroup(
            PanelBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );
        PanelBancoLayout.setVerticalGroup(
            PanelBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        PanelRight.setBackground(new java.awt.Color(153, 255, 255));

        Pnguoichoi.setBorder(javax.swing.BorderFactory.createTitledBorder("Player"));
        Pnguoichoi.setForeground(new java.awt.Color(204, 204, 204));

        avatar2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        avatar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/avatar/exam1.png"))); // NOI18N
        avatar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                avatar2MouseClicked(evt);
            }
        });

        avatar1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        avatar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/avatar/exam1.png"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/double_swords_24px.png"))); // NOI18N

        IconUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Oicon1.png"))); // NOI18N

        IconCompetitor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Xicon1.png"))); // NOI18N

        NameUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NameUser.setText("{Name 1}");

        NameCompetitor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NameCompetitor.setText("{Name 2}");

        LabelTiso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelTiso.setText("Score: 0-0");

        javax.swing.GroupLayout PnguoichoiLayout = new javax.swing.GroupLayout(Pnguoichoi);
        Pnguoichoi.setLayout(PnguoichoiLayout);
        PnguoichoiLayout.setHorizontalGroup(
            PnguoichoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnguoichoiLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(IconUser, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(IconCompetitor, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
            .addGroup(PnguoichoiLayout.createSequentialGroup()
                .addGroup(PnguoichoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnguoichoiLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(avatar1)
                        .addGap(44, 44, 44)
                        .addComponent(jLabel1))
                    .addGroup(PnguoichoiLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(NameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LabelTiso, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PnguoichoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnguoichoiLayout.createSequentialGroup()
                        .addComponent(NameCompetitor, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnguoichoiLayout.createSequentialGroup()
                        .addComponent(avatar2)
                        .addGap(29, 29, 29))))
        );
        PnguoichoiLayout.setVerticalGroup(
            PnguoichoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnguoichoiLayout.createSequentialGroup()
                .addGroup(PnguoichoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnguoichoiLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(PnguoichoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(avatar1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(avatar2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnguoichoiLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(PnguoichoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NameUser)
                    .addComponent(NameCompetitor)
                    .addComponent(LabelTiso))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PnguoichoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(IconUser)
                    .addComponent(IconCompetitor))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        Pchucnang.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnUndo.setText("Undo");
        btnUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUndoActionPerformed(evt);
            }
        });

        btnhoa.setText("Draw request");
        btnhoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhoaActionPerformed(evt);
            }
        });

        buttonExit.setText("Exit Room");
        buttonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PchucnangLayout = new javax.swing.GroupLayout(Pchucnang);
        Pchucnang.setLayout(PchucnangLayout);
        PchucnangLayout.setHorizontalGroup(
            PchucnangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PchucnangLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(btnhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PchucnangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112))
        );
        PchucnangLayout.setVerticalGroup(
            PchucnangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PchucnangLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(PchucnangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        Pthoigian.setBorder(javax.swing.BorderFactory.createTitledBorder("Turn"));

        LabelTimer.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        LabelTimer.setText("01:00");

        LabIconTurn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Oicon1.png"))); // NOI18N

        LabTextTurn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LabTextTurn.setText("My Turn");

        javax.swing.GroupLayout PthoigianLayout = new javax.swing.GroupLayout(Pthoigian);
        Pthoigian.setLayout(PthoigianLayout);
        PthoigianLayout.setHorizontalGroup(
            PthoigianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PthoigianLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(LabIconTurn, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LabTextTurn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelTimer)
                .addContainerGap())
        );
        PthoigianLayout.setVerticalGroup(
            PthoigianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PthoigianLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PthoigianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LabTextTurn)
                    .addComponent(LabIconTurn))
                .addGap(12, 12, 12))
            .addGroup(PthoigianLayout.createSequentialGroup()
                .addComponent(LabelTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
        );

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jButton1.setText("Gửi");

        javax.swing.GroupLayout PanelRightLayout = new javax.swing.GroupLayout(PanelRight);
        PanelRight.setLayout(PanelRightLayout);
        PanelRightLayout.setHorizontalGroup(
            PanelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Pchucnang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PanelRightLayout.createSequentialGroup()
                        .addComponent(jTextField1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(Pthoigian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Pnguoichoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        PanelRightLayout.setVerticalGroup(
            PanelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelRightLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Pchucnang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Pnguoichoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Pthoigian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(PanelBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(PanelBanco, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
                    .addComponent(PanelRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
       if (evt.getKeyCode() == 10) {
            try {
                if (jTextField1.getText().isEmpty()) {
                    return;
                }
                String temp = jTextArea1.getText();
                temp += "Me: " + jTextField1.getText() + "\n";
                jTextArea1.setText(temp);
                RunClient.socketHandle.write("chat," + jTextField1.getText());
                jTextField1.setText("");
                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            }
        }
    }//GEN-LAST:event_jTextField1KeyPressed
    private void buttonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitActionPerformed
        exitGame();
    }//GEN-LAST:event_buttonExitActionPerformed
    private void avatar2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_avatar2MouseClicked
         RunClient.openView(RunClient.View.COMPETITORINFO, competitor);
    }//GEN-LAST:event_avatar2MouseClicked
    private void btnhoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhoaActionPerformed
        try {
            int res = JOptionPane.showConfirmDialog(rootPane, "Do you really want to draw this game?", "Draw Request", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                RunClient.socketHandle.write("draw-request,");
                timer.stop();
                setEnableButton(false);
                RunClient.openView(RunClient.View.WAITINGVERIFY, "Draw Request", "Chill man wait for the opponent's response ");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }//GEN-LAST:event_btnhoaActionPerformed

    private void btnUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUndoActionPerformed
         try {
            int res = JOptionPane.showConfirmDialog(rootPane, "Would you like to request an undo?", "Undo Request", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                RunClient.socketHandle.write("undo-request,");
                timer.stop();
                setEnableButton(false);
                RunClient.openView(RunClient.View.WAITINGVERIFY, "Undo Request", "Chill man wait for the opponent's response ");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }//GEN-LAST:event_btnUndoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IconCompetitor;
    private javax.swing.JLabel IconUser;
    private javax.swing.JLabel LabIconTurn;
    private javax.swing.JLabel LabTextTurn;
    private javax.swing.JLabel LabelTimer;
    private javax.swing.JLabel LabelTiso;
    private javax.swing.JLabel NameCompetitor;
    private javax.swing.JLabel NameUser;
    private javax.swing.JPanel PanelBanco;
    private javax.swing.JPanel PanelRight;
    private javax.swing.JPanel Pchucnang;
    private javax.swing.JPanel Pnguoichoi;
    private javax.swing.JPanel Pthoigian;
    private javax.swing.JLabel avatar1;
    private javax.swing.JLabel avatar2;
    private javax.swing.JButton btnUndo;
    private javax.swing.JButton btnhoa;
    private javax.swing.JButton buttonExit;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
