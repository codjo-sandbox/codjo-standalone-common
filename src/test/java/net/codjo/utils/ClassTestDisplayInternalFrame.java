package net.codjo.utils;
import javax.swing.JInternalFrame;
/**
 *
 */
public class ClassTestDisplayInternalFrame extends JInternalFrame {

    private int parametreTest = 0;


    public ClassTestDisplayInternalFrame() {
        //constructeur sans param�tres
        parametreTest = 1;
    }


    public ClassTestDisplayInternalFrame(String a, String b) {
        //constructeur a 2 param�tres
        parametreTest = 2;
    }


    public ClassTestDisplayInternalFrame(int a) {
        //constructeur a 1 param�tres
        parametreTest = 3;
    }


    public int getParametreTest() {
        return parametreTest;
    }
}