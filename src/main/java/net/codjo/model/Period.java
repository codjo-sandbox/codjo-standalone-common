/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.model;
import net.codjo.persistent.AbstractPersistent;
import net.codjo.persistent.Reference;
/**
 * Une periode est immutable (cad elle ne change pas de valeur dans le temps).
 *
 * @version $Revision: 1.3 $
 *
 */
public class Period extends AbstractPersistent {
    private String period;
    private boolean visible;

    /**
     * Constructeur de l'objet Period
     *
     * @param ref La reference
     * @param period L'id de la period
     * @param visible Description of the Parameter
     */
    public Period(Reference ref, String period, boolean visible) {
        super(ref);
        this.period = period;
        this.visible = visible;
    }

    /**
     * Retourne le nom de la periode
     *
     * @return Le nom de la periode
     */
    public String getPeriod() {
        return period;
    }


    /**
     * Met � jour la visibilit� de la p�riode
     *
     * @param visibility La visibilit� de la p�riode
     */
    public void setVisible(boolean visibility) {
        visible = visibility;
    }


    /**
     * Retourne la visibilit� de la p�riode
     *
     * @return La visibilit� de la p�riode
     */
    public boolean getVisible() {
        return visible;
    }


    /**
     * Permet de tester l'egalite entre des objets de ce type
     *
     * @param obj L'objet a tester
     *
     * @return Egalite VRAI/FAUX
     */
    public boolean equals(Object obj) {
        if ((obj != null) && (obj instanceof Period)) {
            return period.equals(((Period)obj).period);
        }
        return false;
    }


    /**
     * Permet de convertir cet objet en chaine de caracteres
     *
     * @return Le nom de la periode
     */
    public String toString() {
        return period;
    }
}
