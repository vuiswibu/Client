/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.User;
import view.InGameFrm;

/**
 *
 * @author boi09
 */
public class TestFrm {
    public static void main(String[] args) {
        User user = new User(1,"cac","cac","cac","cac",1,12,12,1);
        InGameFrm ingame = new InGameFrm(user,100,1);
        ingame.setVisible(true);
    }
}
