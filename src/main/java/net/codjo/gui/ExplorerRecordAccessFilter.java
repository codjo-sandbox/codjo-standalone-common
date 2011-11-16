/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.gui;
import net.codjo.model.Table;
/**
 * Interface permettant de g�rer la visibilit� des enregistrements sur les tables
 * partag�es entre les diff�rentes applications.
 *
 * @version $Revision: 1.2 $
 *
 *
 */
public interface ExplorerRecordAccessFilter {
    /**
     * Retourne la clause where obligatoire � utiliser pour le filtrage des donn�es.
     *
     * @param table La table � afficher.
     *
     * @return La String de la clause where obligatoire.
     */
    public String getMandatoryWhereClause(Table table);
}
