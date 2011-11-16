/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.persistent;
/**
 * Interface pour les objets pouvant �tre enregistr� dans un model.
 * 
 * <p>
 * Un objet poss�de plusieurs �tats :
 * 
 * <ul>
 * <li>
 * Synchronized / not Synchronized : L'objet est egal a son etat dans la base.
 * </li>
 * <li>
 * Stored (true/false) : qui indique si l'objet est deja enregistre.
 * </li>
 * <li>
 * Dead (true/false) : qui indique si l'objet est efface ou non.
 * </li>
 * </ul>
 * </p>
 *
 * @author $Author: blazart $
 * @version $Revision: 1.2 $
 *
 */
public interface Persistent {
    /**
     * The object saves itself to the datastore <b>without issuing any commit</b> .
     *
     * @exception PersistenceException
     */
    public void save() throws PersistenceException;


    /**
     * Retourne l'identifiant de l'objet Persistent.
     *
     * @return The Id value
     */
    public Object getId();


    /**
     * The object deletes itself from the datastore <b>without issuing any commit </b>.
     *
     * @exception PersistenceException
     */
    public void delete() throws PersistenceException;


    /**
     * Gets the Synchronized attribute of the Persistent object
     *
     * @return The Synchronized value
     */
    public boolean isSynchronized();


    /**
     * Gets the Stored attribute of the Persistent object
     *
     * @return The Stored value
     */
    public boolean isStored();


    /**
     * Indique si l'objet sera efface a la prochaine synchronisation
     *
     * @return The Dead value
     */
    public boolean isDead();


    /**
     * Sets the Synchronized attribute of the Persistent object
     *
     * @param sync The new Synchronized value
     */
    public void setSynchronized(boolean sync);


    /**
     * Passe le Persistent dans l'�tat Dead.
     */
    public void setDead();


    /**
     * Passe le Persistent dans l'�tat stored.
     */
    public void setStored();
}
