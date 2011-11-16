/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.persistent;
/**
 * Un Model est responsable de l'interfacage entre les objets persistants et la base de
 * donn�es.
 * 
 * <p>
 * Son but est d'assurer l'unicit� des r�f�rences pointant sur un m�me objet et la
 * sauvegarde coh�rente des r�f�rences qu'il contient. Un Model est monothread et ne
 * poss�de donc qu'une seule connection.
 * </p>
 *
 * @author $Author: blazart $
 * @version $Revision: 1.3 $
 *
 */
public interface Model {
    /**
     * Retourne une r�f�rence unique poss�dant la clef primaire pass�e en param�tre. La
     * r�f�rence se verra automatiquement ajout�e au Model.
     *
     * @param pk La clef Primaire.
     *
     * @return Une r�f�rence.
     */
    public Reference getReference(Object pk);


    /**
     * Enregistre ref dans la base.
     *
     * @param ref Une r�f�rence.
     *
     * @exception PersistenceException Description of Exception
     */
    public void save(Reference ref) throws PersistenceException;


    /**
     * The object deletes itself from the datastore <b>without issuing any commit </b> .
     *
     * @param ref Une r�f�rence.
     *
     * @exception PersistenceException Description of Exception
     */
    public void delete(Reference ref) throws PersistenceException;


    /**
     * Charge l'objet point� par la r�f�rence.
     *
     * @param ref Une r�f�rence.
     *
     * @exception PersistenceException Description of Exception
     */
    public void load(Reference ref) throws PersistenceException;
}
