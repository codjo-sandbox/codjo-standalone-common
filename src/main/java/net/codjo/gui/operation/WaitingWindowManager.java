/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.gui.operation;
import java.awt.Dimension;
import javax.swing.JDesktopPane;
/**
 * Cette classe g�re l'affichage et la fermeture de la fen�tre d'attente.
 *
 * @version $Revision: 1.3 $
 *
 *
 */
public class WaitingWindowManager {
    private WaitingWindow waitingWindow;
    private JDesktopPane gexPane;
    private String waitingMessage;
    private int numberOfOper;

    /**
     * Constructeur.
     *
     * @param dp Le desktopPane.
     * @param waitingMessage Le message � afficher dans la fen�tre d'attente.
     * @param nbOper Le nombre d'op�ration lanc�es en parall�le.
     *
     * @throws IllegalArgumentException TODO
     */
    public WaitingWindowManager(JDesktopPane dp, String waitingMessage, int nbOper) {
        if (dp == null) {
            throw new IllegalArgumentException();
        }
        gexPane = dp;
        this.waitingMessage = waitingMessage;
        numberOfOper = nbOper;
    }

    /**
     * Affiche la fen�tre d'attente
     */
    public void showWaitingWindow() {
        waitingWindow = new WaitingWindow(waitingMessage);
        gexPane.add(waitingWindow);
        Dimension desktopSize = gexPane.getSize();
        Dimension frameSize = waitingWindow.getSize();
        waitingWindow.setLocation(desktopSize.width - frameSize.width, 0);
        waitingWindow.setVisible(true);
        try {
            waitingWindow.setSelected(true);
        }
        catch (java.beans.PropertyVetoException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Ferme la fen�tre d'attente
     */
    public void disposeWaitingWindow() {
        if (numberOfOper == 1 && waitingWindow != null) {
            try {
                waitingWindow.setClosed(true);
                waitingWindow.dispose();
                waitingWindow = null;
            }
            catch (java.beans.PropertyVetoException ex) {
                ex.printStackTrace();
            }
        }
        else {
            numberOfOper--;
        }
    }
}
