package view;


import controller.RunClient;
import java.awt.Image;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ProfileFrm extends javax.swing.JFrame {
    public ProfileFrm() {
        initComponents();
        this.setIconImage(new ImageIcon("src/assets/logoicon.png").getImage());
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        Nickname_info.setText(RunClient.user.getNickname());
        Nogwon_info.setText(Integer.toString(RunClient.user.getNumberOfwin()));
        Nogplayed_info.setText(Integer.toString(RunClient.user.getNumberOfGame()));
        for (int i=0; i<=21; i++) {
            avatarComboBox1.addItem(new ImageIcon(new ImageIcon("src/assets/avatar/"+i+".png").getImage().getScaledInstance(95, avatarComboBox1.getHeight(),Image.SCALE_SMOOTH)));
        }
        avatarComboBox1.setSelectedIndex(Integer.parseInt((RunClient.user.getAvatar())));
        changenntxt.setText(RunClient.user.getNickname());
        if(RunClient.user.getNumberOfGame()==0){
            wrate_info.setText("-");
        }
        else{
            wrate_info.setText(String.format("%.2f", (float)RunClient.user.getNumberOfwin()/RunClient.user.getNumberOfGame()*100)+"%");
        }
        Nogdrawn_info.setText(""+RunClient.user.getNumberOfDraw());
        totalscore_info.setText(""+(RunClient.user.getNumberOfGame()+RunClient.user.getNumberOfwin()*10));
        rank_info.setText(""+RunClient.user.getRank());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        Banner = new javax.swing.JLabel();
        chnn_label = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        Nickname_title = new javax.swing.JLabel();
        Nickname_info = new javax.swing.JLabel();
        Nogplayed_info = new javax.swing.JLabel();
        Nogplayed_title = new javax.swing.JLabel();
        Nogwon_title = new javax.swing.JLabel();
        Nogwon_info = new javax.swing.JLabel();
        totalscrore_title = new javax.swing.JLabel();
        totalscore_info = new javax.swing.JLabel();
        rank_title = new javax.swing.JLabel();
        rank_info = new javax.swing.JLabel();
        wrate_title = new javax.swing.JLabel();
        wrate_info = new javax.swing.JLabel();
        Nogdrawn_title = new javax.swing.JLabel();
        Nogdrawn_info = new javax.swing.JLabel();
        chnn_label1 = new javax.swing.JLabel();
        chnn_label2 = new javax.swing.JLabel();
        changenntxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        savebtt = new javax.swing.JButton();
        avatarComboBox1 = new javax.swing.JComboBox<>() {
            @Override
            public void paint(Graphics g) {
                Rectangle bounds = getBounds();
                Object selectedItem = getSelectedItem();

                // Vẽ lại giao diện người dùng của JComboBox mà không bao gồm thanh cuộn
                g.setColor(getBackground());
                g.fillRect(0, 0, bounds.width, bounds.height);

                if (selectedItem instanceof ImageIcon) {
                    // Nếu mục đã chọn là một ImageIcon, hiển thị hình ảnh
                    ImageIcon imageIcon = (ImageIcon) selectedItem;
                    Image image = imageIcon.getImage();
                    int x = 3; // Vị trí x để vẽ hình ảnh
                    int y = (bounds.height - imageIcon.getIconHeight()) / 2; // Vị trí y để căn giữa hình ảnh
                    g.drawImage(image, x, y, null);
                } else {
                    // Nếu không, hiển thị văn bản đã chọn
                    g.setColor(getForeground());
                    g.drawString(selectedItem.toString(), 3, bounds.height - 4);
                }
            }
        };
        profile_btt = new javax.swing.JButton();

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        Banner.setFont(new java.awt.Font("Tekton Pro Ext", 0, 24)); // NOI18N
        Banner.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Banner.setText("Game Caro ");

        chnn_label.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        chnn_label.setText("Change nickname:");

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        Nickname_title.setForeground(new java.awt.Color(255, 255, 255));
        Nickname_title.setText("NickName");

        Nickname_info.setForeground(new java.awt.Color(255, 255, 255));
        Nickname_info.setText("{Nick name}");

        Nogplayed_info.setForeground(new java.awt.Color(255, 255, 255));
        Nogplayed_info.setText("{Number of games played}");

        Nogplayed_title.setForeground(new java.awt.Color(255, 255, 255));
        Nogplayed_title.setText("No.of games played");

        Nogwon_title.setForeground(new java.awt.Color(255, 255, 255));
        Nogwon_title.setText("No.of games won");

        Nogwon_info.setForeground(new java.awt.Color(255, 255, 255));
        Nogwon_info.setText("{Number of games won}");

        totalscrore_title.setForeground(new java.awt.Color(255, 255, 255));
        totalscrore_title.setText("Total score ");

        totalscore_info.setForeground(new java.awt.Color(255, 255, 255));
        totalscore_info.setText("{Total score}");

        rank_title.setForeground(new java.awt.Color(255, 255, 255));
        rank_title.setText("Rank");

        rank_info.setForeground(new java.awt.Color(255, 255, 255));
        rank_info.setText("{Rank}");

        wrate_title.setForeground(new java.awt.Color(255, 255, 255));
        wrate_title.setText("Win rate");

        wrate_info.setForeground(new java.awt.Color(255, 255, 255));
        wrate_info.setText("{Win rate}");

        Nogdrawn_title.setForeground(new java.awt.Color(255, 255, 255));
        Nogdrawn_title.setText("No.of games drawn");

        Nogdrawn_info.setForeground(new java.awt.Color(255, 255, 255));
        Nogdrawn_info.setText("{No.of games drawn}");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(Nogdrawn_title, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(wrate_title, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rank_title, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(totalscrore_title, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(wrate_info)
                                    .addComponent(totalscore_info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(48, 48, 48))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rank_info, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Nogdrawn_info, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Nogwon_title)
                            .addComponent(Nickname_title)
                            .addComponent(Nogplayed_title))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Nickname_info, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Nogplayed_info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Nogwon_info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Nickname_title)
                    .addComponent(Nickname_info))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Nogplayed_title)
                    .addComponent(Nogplayed_info))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Nogwon_info)
                    .addComponent(Nogwon_title))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Nogdrawn_title)
                    .addComponent(Nogdrawn_info))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wrate_title)
                    .addComponent(wrate_info))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalscore_info)
                    .addComponent(totalscrore_title))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rank_info)
                    .addComponent(rank_title))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        chnn_label1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        chnn_label1.setText("Change password:");

        chnn_label2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        chnn_label2.setText("Change avatar:");

        jButton1.setText("Change password:");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        savebtt.setText("Save");
        savebtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savebttActionPerformed(evt);
            }
        });

        avatarComboBox1.setMaximumRowCount(25);

        profile_btt.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        profile_btt.setText("Close");
        profile_btt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profile_bttActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Banner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chnn_label)
                            .addComponent(chnn_label2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(changenntxt)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(avatarComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chnn_label1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(profile_btt)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(savebtt)
                                .addGap(17, 17, 17))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Banner)
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chnn_label2)
                        .addGap(18, 95, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(avatarComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chnn_label)
                    .addComponent(changenntxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chnn_label1)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(savebtt)
                    .addComponent(profile_btt))
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void savebttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savebttActionPerformed
        try{
            String nickname = changenntxt.getText();
            int iduser = RunClient.user.getID();
            if(nickname.isEmpty())
            throw new Exception("Enter nickname");
            int avatar = avatarComboBox1.getSelectedIndex();
            if(avatar==-1){
                throw new Exception("Choose avatar");  }
            RunClient.user.setAvatar(String.valueOf(avatar));
            RunClient.user.setNickname(nickname);
            //RunClient.openView(RunClient.View.WAITINGVERIFY, "Đang cập nhật", "Đang chờ phản hồi");
            RunClient.socketHandle.write("change_profile,"+iduser+","+nickname+","+avatar);
            this.dispose();
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());}
        catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }//GEN-LAST:event_savebttActionPerformed

    private void profile_bttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profile_bttActionPerformed
        RunClient.closeView(RunClient.View.PROFILE);
    }//GEN-LAST:event_profile_bttActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        RunClient.openView(RunClient.View.CPASS);
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Banner;
    private javax.swing.JLabel Nickname_info;
    private javax.swing.JLabel Nickname_title;
    private javax.swing.JLabel Nogdrawn_info;
    private javax.swing.JLabel Nogdrawn_title;
    private javax.swing.JLabel Nogplayed_info;
    private javax.swing.JLabel Nogplayed_title;
    private javax.swing.JLabel Nogwon_info;
    private javax.swing.JLabel Nogwon_title;
    private javax.swing.JComboBox<ImageIcon> avatarComboBox1;
    private javax.swing.JTextField changenntxt;
    private javax.swing.JLabel chnn_label;
    private javax.swing.JLabel chnn_label1;
    private javax.swing.JLabel chnn_label2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton profile_btt;
    private javax.swing.JLabel rank_info;
    private javax.swing.JLabel rank_title;
    private javax.swing.JButton savebtt;
    private javax.swing.JLabel totalscore_info;
    private javax.swing.JLabel totalscrore_title;
    private javax.swing.JLabel wrate_info;
    private javax.swing.JLabel wrate_title;
    // End of variables declaration//GEN-END:variables
}
