/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.utils.sql;
import net.codjo.gui.Modal;
import net.codjo.utils.GuiUtil;
import java.awt.event.ActionEvent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
/**
 * Action permettant de rechercher des donn�es dans une table.
 *
 * @author $Author: marcona $
 * @version $Revision: 1.4 $
 *
 */
public class FindAction extends AbstractDbAction {
    String defaultValue = "";
    String mandatoryClause = "";
    SqlRequetorRequest previousRequest = null;

    /**
     * Constructor for the FindAction object
     */
    public FindAction() {
        putValue(NAME, "Rechercher");
        putValue(SHORT_DESCRIPTION, "Recherche de donn�es");
        putValue(SMALL_ICON, UIManager.getIcon("ListTable.find"));
    }


    /**
     * Constructeur.
     *
     * @param dp Le desktopPane.
     * @param frm Fen�tre d'affichage de la table (fen�tre m�re).
     * @param gt Le model de la table.
     */
    public FindAction(JDesktopPane dp, JInternalFrame frm, GenericTable gt) {
        super(dp, frm, gt);
        putValue(NAME, "Rechercher");
        putValue(SHORT_DESCRIPTION, "Recherche de donn�es");
        putValue(SMALL_ICON, UIManager.getIcon("ListTable.find"));
    }

    /**
     * Fixe la clause de recherche implicite
     *
     * @param clause The new DefaultValue value
     */
    public void setDefaultValue(String clause) {
        defaultValue = clause;
    }


    /**
     * Fixe la clause de recherche implicite
     *
     * @param clause The new DefaultValue value
     */
    public void setMandatoryClause(String clause) {
        mandatoryClause = clause;
    }


    /**
     * Met � jour la pr�c�dente requete.
     *
     * @param newPreviousRequest La nouvelle requ�te.
     */
    public void setPreviousRequest(SqlRequetorRequest newPreviousRequest) {
        previousRequest = newPreviousRequest;
    }


    /**
     * Retourne la pr�c�dente requ�te.
     *
     * @return La pr�c�dente requ�te.
     */
    public SqlRequetorRequest getPreviousRequest() {
        return previousRequest;
    }


    /**
     * Affichage de la fenetre de saisie des requ�tes SQL.
     *
     * @param evt evenement declenchant l'affichage
     */
    public void actionPerformed(ActionEvent evt) {
        fireActionEvent(evt);
        SqlRequetor sqlW =
            new SqlRequetor(getDesktopPane(), getWindowTable(), this, getGenericTable(),
                defaultValue);
        sqlW.setMandatoryClause(mandatoryClause);
        new Modal(getWindowTable(), sqlW);
        sqlW.setVisible(true);
        getDesktopPane().add(sqlW);
        GuiUtil.centerWindow(sqlW);
        try {
            sqlW.setSelected(true);
        }
        catch (java.beans.PropertyVetoException g) {}
    }
}
