/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.utils.sql;

// Common
import net.codjo.model.Table;
//Penelope
import net.codjo.utils.SqlTypeConverter;
import java.sql.Types;

//Java
import java.util.ArrayList;
/**
 * Cette classe g�re 6 listes en parall�le de la sqlList de la classe SqlRequetor. Pour
 * un index donn�, l'ensemble des �l�ments de ces listes correspond � la ligne de m�me
 * index de la sqlList.
 *
 * @author $Author: spinae $
 * @version $Revision: 1.2 $
 */
class SqlRequetorRequest {
    // Constantes
    static final int EQUAL = 0;
    static final int SUP = 1;
    static final int SUP_EQUAL = 2;
    static final int INF = 3;
    static final int INF_EQUAL = 4;
    static final int DIFFERENT = 5;
    static final int BEGIN_BY = 6;
    static final int END_BY = 7;
    static final int CONTAIN = 8;
    static final int NOT_CONTAIN = 9;
    static final int IS_NULL = 10;
    static final int IS_NOT_NULL = 11;

    //L'op�rateur logique (and ou or)
    private ArrayList logicalOper = new ArrayList();

    //L'objet table
    private ArrayList table = new ArrayList();

    //Le nom physique du champ de la table courante
    private ArrayList field = new ArrayList();

    //L'op�rateur de comparaison (=, like, ...)
    private ArrayList compareOper = new ArrayList();

    //Le pr�fixe de la valeur du champ(', '%, ...)
    private ArrayList prefixValue = new ArrayList();

    //La valeur du champ
    private ArrayList value = new ArrayList();

    //Le suffixe de la valeur du champ(', %', ...)
    private ArrayList suffixValue = new ArrayList();

    /**
     * Constructor for the SqlRequetorRequest object
     */
    public SqlRequetorRequest() {}


    /**
     * Constructor par copie
     *
     * @param req Description of Parameter
     */
    public SqlRequetorRequest(SqlRequetorRequest req) {
        logicalOper = new ArrayList(req.logicalOper);
        table = new ArrayList(req.table);
        field = new ArrayList(req.field);
        compareOper = new ArrayList(req.compareOper);
        prefixValue = new ArrayList(req.prefixValue);
        value = new ArrayList(req.value);
        suffixValue = new ArrayList(req.suffixValue);
    }

    /**
     * Met � jour l'op�rateur logique � l'index donn�.
     *
     * @param newLogicalOper La valeur de l'op�rateur
     * @param idx L'index
     */
    public void setLogicalOper(String newLogicalOper, int idx) {
        logicalOper.set(idx, newLogicalOper);
    }


    /**
     * Met � jour le nom physique du champ � l'index donn�.
     *
     * @param newField Le nom du champ
     * @param idx L'index
     */
    public void setField(String newField, int idx) {
        field.set(idx, newField);
    }


    /**
     * Met � jour l'op�rateur de comparaison � l'index donn�.
     *
     * @param newCompareOper La valeur de l'op�rateur
     * @param idx L'index
     */
    public void setCompareOper(int newCompareOper, int idx) {
        compareOper.set(idx, new Integer(newCompareOper));
    }


    /**
     * Met � jour le pr�fixe de la valeur du champ � l'index donn�.
     *
     * @param newPrefixValue La valeur du pr�fixe
     * @param idx L'index
     */
    public void setPrefixValue(String newPrefixValue, int idx) {
        prefixValue.set(idx, newPrefixValue);
    }


    /**
     * Met � jour la valeur du champ � l'index donn�.
     *
     * @param newValue La valeur du champ
     * @param idx L'index
     */
    public void setValue(String newValue, int idx) {
        value.set(idx, addQuote(newValue));
    }


    /**
     * Met � jour le suffixe de la valeur du champ � l'index donn�.
     *
     * @param newSuffixValue La valeur du suffixe
     * @param idx L'index
     */
    public void setSuffixValue(String newSuffixValue, int idx) {
        suffixValue.set(idx, newSuffixValue);
    }


    /**
     * Retourne l'op�rateur logique � l'index donn�.
     *
     * @param idx L'index
     *
     * @return L'op�rateur
     */
    public String getLogicalOper(int idx) {
        return (String)logicalOper.get(idx);
    }


    /**
     * Retourne le nom physique du champ � l'index donn�.
     *
     * @param idx L'index
     *
     * @return Le nom du champ
     */
    public String getField(int idx) {
        return (String)field.get(idx);
    }


    /**
     * Retourne la taille des listes de la requ�te.
     *
     * @return La taille des listes.
     */
    public int getRequestListSize() {
        return field.size();
    }


    /**
     * Retourne l'op�rateur de comparaison � l'index donn�.
     *
     * @param idx L'index
     *
     * @return L'op�rateur
     */
    public int getCompareOperValue(int idx) {
        return ((Integer)compareOper.get(idx)).intValue();
    }


    /**
     * Retourne l'op�rateur de comparaison � l'index donn�.
     *
     * @param idx L'index
     *
     * @return L'op�rateur
     */
    public String getCompareOperTraducValue(int idx) {
        int oper = getCompareOperValue(idx);
        if (oper != -1) {
            return traductOperator(oper);
        }
        else {
            return "";
        }
    }


    /**
     * Retourne le pr�fixe de la valeur du champ � l'index donn�.
     *
     * @param idx L'index
     *
     * @return Le pr�fixe
     */
    public String getPrefixValue(int idx) {
        return (String)prefixValue.get(idx);
    }


    /**
     * Retourne la valeur du champ � l'index donn�.
     *
     * @param idx L'index
     *
     * @return La valeur du champ
     */
    public String getValue(int idx) {
        return (String)value.get(idx);
    }


    /**
     * Retourne le suffixe de la valeur du champ � l'index donn�.
     *
     * @param idx L'index
     *
     * @return Le suffixe
     */
    public String getSuffixValue(int idx) {
        return (String)suffixValue.get(idx);
    }


    /**
     * Retourne l'ensemble des �l�ments des listes � l'index donn�.
     *
     * @param idx L'index
     *
     * @return Les �l�ments des listes
     */
    public String getRequest(int idx) {
        StringBuffer str = new StringBuffer();
        if (logicalOper.size() != 0 && logicalOper.size() > idx) {
            str.append(getLogicalOper(idx));
        }
        if (table.size() != 0 && table.size() > idx) {
            if (getTable(idx) != null) {
                str.append(getTable(idx).getDBTableName() + ".");
            }
        }

        if (field.size() != 0 && field.size() > idx) {
            str.append(getField(idx));
        }
        if (compareOper.size() != 0 && compareOper.size() > idx) {
            str.append(getCompareOperTraducValue(idx));
        }
        if (prefixValue.size() != 0 && prefixValue.size() > idx) {
            str.append(getPrefixValue(idx));
        }
        if (value.size() != 0 && value.size() > idx) {
            str.append(getValue(idx));
        }
        if (suffixValue.size() != 0 && suffixValue.size() > idx) {
            str.append(getSuffixValue(idx));
        }
        return str.toString();
    }


    /**
     * Overview.
     * 
     * <p>
     * Description
     * </p>
     *
     * @param idx Description of Parameter
     */
    public void removeElements(int idx) {
        logicalOper.remove(idx);
        field.remove(idx);
        compareOper.remove(idx);
        prefixValue.remove(idx);
        value.remove(idx);
        suffixValue.remove(idx);
        table.remove(idx);
    }


    /**
     * Ajoute un �l�ment vide � chacune des listes pour l'index donn�.
     *
     * @param idx L'index
     */
    public void addElements(int idx) {
        logicalOper.add(idx, "");
        field.add(idx, "");
        compareOper.add(idx, new Integer(-1));
        prefixValue.add(idx, "");
        value.add(idx, "");
        suffixValue.add(idx, "");
        table.add(idx, null);
    }


    /**
     * Supprime tous les �l�ments des listes.
     */
    public void removeAllElements() {
        for (int i = 0; i < value.size(); i++) {
            removeElements(i);
        }
    }


    /**
     * Met � jour l'objet table � l'index donn�.
     *
     * @param newTable L'objet table
     * @param idx L'index
     */
    void setTable(Table newTable, int idx) {
        table.set(idx, newTable);
    }


    /**
     * Retourne l'objet table � l'index donn�
     *
     * @param idx L'index
     *
     * @return L'objet table
     */
    Table getTable(int idx) {
        return (Table)table.get(idx);
    }


    /**
     * Met � jour le pr�fixe et le suffixe d'une valeur pour l'index donn� en fonction de
     * l'op�rateur de comparaison et du type SQL du champ.
     *
     * @param oper L'op�rateur de comparaison.
     * @param idx L'index.
     * @param sqlType Le type SQL du champ.
     */
    void updatePrefSuffValue(int oper, int idx, int sqlType) {
        if (oper == CONTAIN || oper == NOT_CONTAIN) {
            setPrefixValue("'%", idx);
            setSuffixValue("%'", idx);
        }
        else if (oper == BEGIN_BY) {
            setPrefixValue("'", idx);
            setSuffixValue("%'", idx);
        }
        else if (oper == END_BY) {
            setPrefixValue("'%", idx);
            setSuffixValue("'", idx);
        }
        else if (sqlType == Types.BIT
                || SqlTypeConverter.isNumeric(sqlType)
                || oper == IS_NULL
                || oper == IS_NOT_NULL) {
            setPrefixValue("", idx);
            setSuffixValue("", idx);
        }
        else {
            setPrefixValue("'", idx);
            setSuffixValue("'", idx);
        }
    }


    /**
     * Adds a feature to the Quote attribute of the SqlRequetorRequest object
     *
     * @param value The feature to be added to the Quote attribute
     *
     * @return Description of the Returned Value
     */
    private String addQuote(String value) {
        StringBuffer tmp = new StringBuffer(value);
        char quote = '\'';
        int index = 0;
        while (index < tmp.length()) {
            if (tmp.charAt(index) == quote) {
                tmp.insert(index, quote);
                index++;
            }
            index++;
        }
        return tmp.toString();
    }


    /**
     * Traduit les op�rateurs de comparaison en "langage Sybase".
     *
     * @param oper L'op�rateur s�lection� dans la liste.
     *
     * @return La valeur traduite.
     *
     * @throws IllegalArgumentException TODO
     */
    private String traductOperator(int oper) {
        String strOper;
        switch (oper) {
            case EQUAL:
                strOper = " = ";
                break;
            case SUP:
                strOper = " > ";
                break;
            case SUP_EQUAL:
                strOper = " >= ";
                break;
            case INF:
                strOper = " < ";
                break;
            case INF_EQUAL:
                strOper = " <= ";
                break;
            case DIFFERENT:
                strOper = " <> ";
                break;
            case BEGIN_BY:
                strOper = " like ";
                break;
            case END_BY:
                strOper = " like ";
                break;
            case CONTAIN:
                strOper = " like ";
                break;
            case NOT_CONTAIN:
                strOper = " not like ";
                break;
            case IS_NULL:
                strOper = " is null ";
                break;
            case IS_NOT_NULL:
                strOper = " is not null ";
                break;
            default:
                throw new IllegalArgumentException("Operateur inconnu");
        }
        return strOper;
    }
}
