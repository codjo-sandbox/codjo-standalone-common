/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.operation.imports;
/**
 * Cette exception est lanc�e par une op�ration FieldImport afin d'indiquer que le champ
 * est introuvable dans la ligne.
 * 
 * <p>
 * NB: c'est-�-dire la ligne est trop courte.
 * </p>
 *
 * @author $Author: blazart $
 * @version $Revision: 1.3 $
 *
 */
public class FieldNotFoundException extends Exception {
    /**
     * Constructor for the FieldNotFoundException object
     *
     * @param msg Description of Parameter
     */
    public FieldNotFoundException(String msg) {
        super(msg);
    }
}
