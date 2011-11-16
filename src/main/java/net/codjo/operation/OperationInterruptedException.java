/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.operation;
/**
 * Exception qui indique qu'une op�ration a �t� interrompue
 *
 * @author $Author: blazart $
 * @version $Revision: 1.2 $
 *
 */
public class OperationInterruptedException extends OperationFailureException {
    /**
     * Constructeur avec une op�ration.
     *
     * @param s le message d'erreur.
     * @param ope L'op�ration.
     */
    public OperationInterruptedException(String s, Operation ope) {
        super(s, ope);
    }


    /**
     * Constructeur sans op�ration.
     *
     * @param s le message d'erreur.
     */
    public OperationInterruptedException(String s) {
        this(s, null);
    }
}
