/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.utils.sql;

//Lib
import net.codjo.model.Table;
import net.codjo.persistent.Reference;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
/**
 * Model d'affichage generique du contenu d'une table BD.
 *
 * @author $Author: blazart $
 * @version $Revision: 1.5 $
 */
public class GenericTableModel extends AbstractTableModel {
    //Le buffer de chargement de ligne
    /** Description of the Field */
    protected static final int BUFFER_SIZE = 1000;
    private static final Logger APP = Logger.getLogger(GenericTableModel.class);

    //Encore des donn�es apr�s cette page
    private boolean babyOneMoreTime;

    //Tableau des classes des colonnes
    private Class[] columnClass;

    //Liste des noms physiques des colonnes
    private List columnDBNameList;

    //Liste des libell�s des colonnes
    private List columnLabelList;

    //Liste des donn�es
    private List dataList;

    //Les droits d'edition par cellule
    private List editableColumnList;

    //La connection forc�e
    private Connection forcedConnection;

    //Derniere clause where
    private String fromAndWhereClause = "";

    //Liste des noms des champs cl�s primaires
    private List keyDBNameList;

    //Liste des cl�s
    private List keyValueList;

    //Num�ro du premier enregistrement affich�
    private int numberOfFirstRow;

    //Num�ro du premier enregistrement affich�
    private int numberOfLastRow;

    //Nombre de lignes de la derniere requete
    private int numberOfRows;

    // Clause de order by
    private String orderByClause = null;

    //La table est en read-only
    private boolean readOnly;

    //La requete de r�cup�ration des donn�es
    private String request;

    //Table destination
    private Reference tableRef;

    //Mode de selection Distinct
    private boolean distinctMode = false;

    /**
     * Constructor complet pour l'objet GenericTableModel
     *
     * @param con Description of Parameter
     * @param tableDestRef Description of Parameter
     * @param columnList La liste des DB noms de colonnes
     * @param editableColumnList Description of Parameter
     * @param readOnly Le composant est en read only
     * @param whereClause La clause where initiale (peut �tre vide)
     * @param orderByClause Clause order by (eg "order by LABEL")
     *
     * @exception SQLException SQL
     * @throws IllegalArgumentException TODO
     */
    public GenericTableModel(Connection con, Reference tableDestRef, List columnList,
        List editableColumnList, boolean readOnly, String whereClause,
        String orderByClause) throws SQLException {
        if (tableDestRef == null || columnList == null) {
            throw new IllegalArgumentException("Param�tres non valides");
        }
        forcedConnection = con;
        tableRef = tableDestRef;
        columnDBNameList = columnList;
        this.readOnly = readOnly;
        setOrderByClause(orderByClause);
        columnLabelList = new ArrayList(columnDBNameList.size());
        this.editableColumnList = editableColumnList;
        columnClass = new Class[columnDBNameList.size()];
        initKeysDBNames();
        initRequest(whereClause);
        initFieldLabel();
        initColumnClassName();
        loadData(0);
    }

    /**
     * Retourne la classe d'une colonne du model
     *
     * @param columnIndex Le num�ro de colonne
     *
     * @return La classe de la colonne
     */
    public Class getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }


    /**
     * Retourne le nombre de colonnes du model
     *
     * @return The ColumnCount value
     */
    public int getColumnCount() {
        return columnDBNameList.size();
    }


    /**
     * Retourne la liste des noms physiques des champs du model
     *
     * @return Une liste de string
     */
    public List getColumnDBNameList() {
        return columnDBNameList;
    }


    /**
     * Retourne le nom d'une colonne du model
     *
     * @param columnIndex Le num�ro de colonne
     *
     * @return Le libell� de la colonne
     */
    public String getColumnName(int columnIndex) {
        return (String)columnLabelList.get(columnIndex);
    }


    /**
     * Retourne le num�ro de la colonne / dbName
     *
     * @param columnDBName Le nom physique de la colonne
     *
     * @return La position de la colonne
     *
     * @throws IllegalArgumentException TODO
     */
    public int getColumnNumber(String columnDBName) {
        for (int i = 0; i < this.columnDBNameList.size(); i++) {
            if (columnDBName.equals(columnDBNameList.get(i))) {
                return i;
            }
        }
        throw new IllegalArgumentException("Colonne " + columnDBName
            + " non trouv�e dans le model");
    }


    /**
     * Retourne une connection utilisable. La methode releaseConnection() doit etre
     * imperativement appele apres utilisation
     *
     * @return La connection
     *
     * @exception SQLException Description of Exception
     */
    public Connection getConnection() throws SQLException {
        if (forcedConnection != null) {
            return forcedConnection;
        }
        else {
            return Dependency.getConnectionManager().getConnection();
        }
    }


    /**
     * Retourne l'attribut orderByClause de GenericTableModel
     *
     * @return La valeur de orderByClause
     */
    public String getOrderByClause() {
        return orderByClause;
    }


    /**
     * Retourne le nombre de lignes dans le model
     *
     * @return Nombre de lignes
     */
    public int getRowCount() {
        return dataList.size();
    }


    /**
     * Renvoie un element du model � afficher
     *
     * @param row Le num�ro de ligne � afficher
     * @param column Le num�ro de colonne � afficher
     *
     * @return La valeur correspondant au num�ros de ligne et de colonne
     *
     * @throws IllegalArgumentException TODO
     */
    public Object getValueAt(int row, int column) {
        List aRow = null;
        if ((column >= 0)
                && (column < getColumnCount())
                && (row >= 0)
                && (row < dataList.size())) {
            aRow = (List)dataList.get(row);
            return aRow.get(column);
        }
        else if (column == -1) {
            return new Integer(row);
        }
        else {
            throw new IllegalArgumentException("Cellule inconnue L=" + row + " C="
                + column);
        }
    }


    /**
     * La cellule est'elle editable ?
     *
     * @param row Num�ro de la ligne (on ne g�re pas)
     * @param col Num�ro de la colonne
     *
     * @return Editable ?
     */
    public boolean isCellEditable(int row, int col) {
        if (readOnly) {
            return false;
        }
        else {
            return ((Boolean)editableColumnList.get(col)).booleanValue();
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param con
     *
     * @exception SQLException
     */
    public void releaseConnection(Connection con)
            throws SQLException {
        if (con == null) {
            return;
        }
        if (forcedConnection == con) {
            return;
        }
        Dependency.getConnectionManager().releaseConnection(con);
    }


    /**
     * DOCUMENT ME!
     *
     * @param con
     * @param stmt
     *
     * @exception SQLException
     */
    public void releaseConnection(Connection con, Statement stmt)
            throws SQLException {
        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch(Exception e){
        	//To hell !!!!
        }
        finally {
            releaseConnection(con);
        }
    }


    /**
     * Change la clause <code>order by</code> de rafraichissement de la table. Aucun
     * rechargement est effectu�.
     *
     * @param orderByClause La clause avec le mot clef "order by" (ex: "order by LABEL
     *        desc, ID")
     *
     * @exception SQLException
     */
    public void setOrderByClause(String orderByClause)
            throws SQLException {
        this.orderByClause = orderByClause;
    }


    /**
     * Stocke la nouvelle valeur dans les donn�es
     *
     * @param value La nouvelle valeur
     * @param row Le num�ro de la ligne
     * @param col Le num�ro de la colonne
     *
     * @throws Error TODO
     */
    public void setValueAt(Object value, int row, int col) {
        if (readOnly) {
            throw new Error(
                "Impossible de modifier une cellule si le composant est en mode lecture seule");
        }
        else {
            List aRow = (List)dataList.get(row);
            aRow.set(col, value);
            fireTableCellUpdated(row, col);
        }
    }


    /**
     * La selection des lignes s'effectuera en mode disctinct ou pas
     *
     * @param distinctMode Mode select distinct ?
     */
    void setDistinctModeOn(boolean distinctMode) {
        this.distinctMode = distinctMode;
    }


    /**
     * D�finir une nouvelle connexion � utiliser
     *
     * @param con La nouvelle connexion
     */
    void setNewConnection(Connection con) {
        forcedConnection = con;
    }


    /**
     * Ajoute une ligne dans le model
     *
     * @throws RuntimeException TODO
     * @throws IllegalArgumentException TODO
     */
    void addNewLine() {
        if (readOnly) {
            throw new RuntimeException(
                "Impossible d'ajouter une ligne si le composant est en mode lecture seule");
        }

        //On cr�� une ligne pour les donn�es
        List newBlankLineList = new ArrayList();
        for (int i = 0; i < columnDBNameList.size(); i++) {
            if (columnClass[i] == Boolean.class) {
                newBlankLineList.add(new Boolean(false));
            }
            else if (columnClass[i] == Date.class) {
                newBlankLineList.add(null);
            }
            else if (columnClass[i] == Integer.class) {
                newBlankLineList.add(null);
            }
            else if (columnClass[i] == String.class) {
                newBlankLineList.add(null);
            }
            else if (columnClass[i] == Number.class) {
                newBlankLineList.add(null);
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        dataList.add(0, newBlankLineList);

        //On cr�� une ligne vide pour les cl�s
        List newkeyValueLineList = new ArrayList();
        keyValueList.add(0, newkeyValueLineList);
        fireTableDataChanged();
    }


    /**
     * Supprime une ligne dans le model
     *
     * @param index Num�ro de la ligne � virer
     *
     * @throws RuntimeException TODO
     */
    void deleteLine(int index) {
        if (readOnly) {
            throw new RuntimeException(
                "Impossible de supprimer une ligne si le composant est en mode lecture seule");
        }
        dataList.remove(index);
        keyValueList.remove(index);
        fireTableDataChanged();
    }


    /**
     * R�cup�re la liste des valeurs des cl�s d'une ligne si c'est une nouvelle ligne,
     * les cl�s ne sont pas d�finies et la m�thode renvoie donc null
     *
     * @param index Le num�ro de ligne
     *
     * @return Une hashMap contenant les valeurs des cl�s ou null
     */
    Map getALineOfKey(int index) {
        if (index > keyValueList.size() - 1) {
            return null;
        }
        List keyList = (List)keyValueList.get(index);
        if (keyList.size() != 0) {
            Map hm = new HashMap();
            for (int i = 0; i < keyList.size(); i++) {
                hm.put(keyDBNameList.get(i), keyList.get(i));
            }
            return hm;
        }
        else {
            return null;
        }
    }


    /**
     * Retourne le num�ro de la premiere ligne de la page courante
     *
     * @return Le num�ro
     */
    int getNumberOfFirstRow() {
        return numberOfFirstRow;
    }


    /**
     * Retourne le num�ro de la derni�re ligne de la page courante
     *
     * @return Le num�ro
     */
    int getNumberOfLastRow() {
        return numberOfLastRow;
    }


    /**
     * Retourne le nombre total d'enregistrements dans la table (correspondants � la
     * dern�re clause where appliqu�e)
     *
     * @return Le nombre de lignes total
     */
    int getNumberOfRows() {
        return numberOfRows;
    }


    /**
     * Reste-t'il des donn�es apr�s la page courante
     *
     * @return Oui ou Non !
     */
    boolean hasMoreData() {
        return babyOneMoreTime;
    }


    /**
     * Reconstruit la requete et lance le rechargement des donn�es
     *
     * @param newFromAndWhereClause Description of Parameter
     * @param page Description of Parameter
     *
     * @exception SQLException Description of Exception
     */
    void reloadData(String newFromAndWhereClause, int page)
            throws SQLException {
        fromAndWhereClause = newFromAndWhereClause;
        initBodyOfRequest();
        request = request + " " + newFromAndWhereClause;
        if (getOrderByClause() != null) {
            request += " " + getOrderByClause();
        }
        loadData(page);
        fireTableDataChanged();
    }


    /**
     * Retourne la table
     *
     * @return La table
     */
    private Table getTable() {
        return (Table)tableRef.getLoadedObject();
    }


    /**
     * Construit le corps de la requete (la partie invariante) jusqu'au 'from' (exclus)
     *
     * @exception SQLException Description of Exception
     */
    private void initBodyOfRequest() throws SQLException {
        if (columnDBNameList.size() == 0) {
            throw new SQLException(
                "Aucune colonne � afficher n'est param�tr�e pour le composant GenericTable");
        }

        //Les champs � afficher
        request = "select ";
        if (distinctMode) {
            request += "distinct ";
        }
        request += getTable().getDBTableName() + "." + columnDBNameList.get(0);
        for (int i = 1; i < columnDBNameList.size(); i++) {
            request =
                request + ", " + getTable().getDBTableName() + "."
                + columnDBNameList.get(i);
        }

        //Les champs qui sont des cl�s primaires
        for (int i = 0; i < keyDBNameList.size(); i++) {
            request =
                request + ", " + getTable().getDBTableName() + "." + keyDBNameList.get(i);
        }
        request = request + " ";
    }


    /**
     * Charge les classes de colonnes affich�es dans le model;
     *
     * @throws RuntimeException TODO
     */
    private void initColumnClassName() {
        for (int i = 0; i < columnDBNameList.size(); i++) {
            int sqlType = getTable().getColumnSqlType((String)columnDBNameList.get(i));
            switch (sqlType) {
                case Types.BIT:
                    columnClass[i] = Boolean.class;
                    break;
                case Types.DATE:
                    columnClass[i] = Date.class;
                    break;
                case Types.INTEGER:
                    columnClass[i] = Integer.class;
                    break;
                case Types.VARCHAR:
                    columnClass[i] = String.class;
                    break;
                case Types.CHAR:
                    columnClass[i] = String.class;
                    break;
                case Types.NUMERIC:
                    columnClass[i] = Number.class;
                    break;
                case Types.TIMESTAMP:
                    columnClass[i] = Date.class;
                    break;
                case Types.TINYINT:
                case Types.SMALLINT:
                    columnClass[i] = Integer.class;
                    break;
                case Types.LONGVARCHAR:
                    columnClass[i] = String.class;
                    break;
                case Types.FLOAT:
                    columnClass[i] = Float.class;
                    break;
                case Types.DOUBLE:
                    columnClass[i] = Double.class;
                    break;
                default:
                    throw new RuntimeException("Type de colonne non support� : "
                        + sqlType + "=>" + columnDBNameList.get(i));
            }
        }
    }


    /**
     * Charge les libell�s de champs affich�s
     *
     * @exception SQLException Description of Exception
     */
    private void initFieldLabel() throws SQLException {
        String query;
        Statement stmt = null;
        Connection con = getConnection();
        try {
            stmt = con.createStatement();
            for (int i = 0; i < columnDBNameList.size(); i++) {
                query =
                    "select FIELD_LABEL from PM_FIELD_LABEL " + "where DB_TABLE_NAME='"
                    + this.getTable().getDBTableName() + "' and DB_FIELD_NAME='"
                    + columnDBNameList.get(i) + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    columnLabelList.add(i, rs.getString("FIELD_LABEL"));
                }
                else {
                    columnLabelList.add(i, columnDBNameList.get(i));
                }
            }
        }
        finally {
            releaseConnection(con, stmt);
        }
    }


    /**
     * Initialise la liste des noms des champs �tant des cl�s primaires.
     *
     * @exception SQLException : un pb de base ?
     */
    private void initKeysDBNames() throws SQLException {
        keyDBNameList = new ArrayList(getTable().getPkNames());
    }


    /**
     * Cr�ation de la requ�te de recup�ration des donn�es
     *
     * @param whereClause Le texte de la clause where
     *
     * @exception SQLException Pas de param�trage de colonne � afficher trouv� en BD
     */
    private void initRequest(String whereClause) throws SQLException {
        initBodyOfRequest();
        fromAndWhereClause =
            " from " + this.getTable().getDBTableName() + " " + whereClause;
        request = request + fromAndWhereClause;
        if (getOrderByClause() != null) {
            request += " " + getOrderByClause();
        }
    }


    /**
     * Charge les donn�es (liste des cl�s et des donn�es � afficher)
     *
     * @param page Description of Parameter
     *
     * @exception SQLException Description of Exception
     */
    private void loadData(int page) throws SQLException {
        dataList = new ArrayList();
        keyValueList = new ArrayList();
        numberOfFirstRow = (page * BUFFER_SIZE) + 1;
        numberOfLastRow = (page + 1) * BUFFER_SIZE;

        BufferLoader sw = new BufferLoader(page);

//        sw.start();
// TEMP
        sw.loadBuffer();
        sw.fillData();
// END TEMP
    }

    /**
     * Description of the Class
     *
     * @author VIRASIS
     * @version $Revision: 1.5 $
     */
    private class BufferLoader {
        List dataListTH = new ArrayList();
        List keyValueListTH = new ArrayList();
        int numberOfRowsTH;
        int page;

        /**
         * DOCUMENT ME!
         *
         * @param page
         */
        BufferLoader(int page) {
            this.page = page;
        }

        /**
         * Description of the Method
         */
        public void fillData() {
            numberOfRows = numberOfRowsTH;
            dataList = dataListTH;
            keyValueList = keyValueListTH;
            fireTableDataChanged();
        }


        /**
         * Description of the Method
         *
         * @exception SQLException Description of Exception
         */
        public void loadBuffer() throws SQLException {
            APP.debug("Begin load Data with " + request);
            int line = 0;

            Statement stmt = null;
            Connection con = getConnection();
            try {
                stmt = con.createStatement();
                //Recherche du nombre de lignes
                ResultSet rs = stmt.executeQuery("select count(*) " + fromAndWhereClause);
                if (rs.next()) {
                    numberOfRowsTH = rs.getInt(1);
                }
                rs.close();

                stmt.setFetchSize(BUFFER_SIZE);
                rs = stmt.executeQuery(request);

                while (rs.next() && (line < numberOfLastRow)) {
                    line++;
                    if ((line >= numberOfFirstRow) && (line <= numberOfLastRow)) {
                        //On stocke les donn�es d'une ligne
                        List dataLineList = new ArrayList();
                        for (int i = 0; i < columnDBNameList.size(); i++) {
                            Object obj = rs.getObject(i + 1);
                            dataLineList.add(obj);
                        }
                        dataListTH.add(dataLineList);

                        //On stocke les cl�s d'une ligne
                        List keyValueLineList = new ArrayList();
                        for (int i = 0; i < keyDBNameList.size(); i++) {
                            keyValueLineList.add(rs.getObject(columnDBNameList.size() + i
                                    + 1));
                        }
                        keyValueListTH.add(keyValueLineList);
                    }
                }

                if (rs.isAfterLast()) {
                    numberOfLastRow = numberOfRowsTH;
                    babyOneMoreTime = false;
                }
                else if (line == 0) {
                    numberOfFirstRow = 0;
                    numberOfLastRow = 0;
                    babyOneMoreTime = false;
                }
                else {
                    babyOneMoreTime = true;
                }
            }
            finally {
                releaseConnection(con, stmt);
                APP.debug("End load Data with " + request);
            }
        }
    }


    public List getKeyValueList() {
        return keyValueList;
    }
}
