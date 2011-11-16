/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.gui;
/**
 * Interface permettant de r�cup�rer le JTree sp�cifique � chaque application. Elle est
 * utilis�e pour l'affichage de l'explorateur des donn�es.
 *
 * @version $Revision: 1.3 $
 *
 *
 */
public interface ExplorerTreeBuilder {
    /**
     * Retourne le JTree de l'application.
     *
     * @return Le JTree.
     */
    public javax.swing.JTree getTree();


    /**
     * Retourne le user courant
     *
     * @return The user value
     */
    public net.codjo.profile.User getUser();


    /**
     * Affecte user � newCurrentUser
     *
     * @param newCurrentUser The new currentUser value
     */
    public void setCurrentUser(net.codjo.profile.User newCurrentUser);
}
