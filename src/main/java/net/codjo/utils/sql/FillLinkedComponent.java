/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.utils.sql;
import net.codjo.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Classe charg�e de r�cup�rer diff�rents libell�s
 *
 * @author $Author: blazart $
 * @version $Revision: 1.3 $
 *
 */
public class FillLinkedComponent {
    private ConnectionManager conMan;

    /**
     * Constructeur
     *
     * @exception Exception Le connection Manager de la classe Dependency du package n'a
     *            pas �t� initialis�
     */
    public FillLinkedComponent() throws Exception {
        conMan = Dependency.getConnectionManager();
        if (conMan == null) {
            throw new Exception("Le gestionnaire de connexion n'est pas initialis� !");
        }
    }

    /**
     * Retourne le libell� d'un portefeuille/code
     *
     * @param portfolioCode Le code
     *
     * @return Le libell� correspondant au code
     */
    public String getPortfolioLabel(String portfolioCode) {
        return getLabel("BO_PORTFOLIO", "PORTFOLIO_NAME", "PORTFOLIO_CODE",
            portfolioCode, null);
    }


    /**
     * Retourne le libell� d'un composite / code
     *
     * @param compositeId Le code
     *
     * @return Le libell� correspondant au code
     */
    public String getCompositeLabel(String compositeId) {
        return getLabel("PR_COMPOSITE", "COMPOSITE_NAME", "COMPOSITE_ID", compositeId,
            null);
    }


    /**
     * Retourne le libell� d'un axe / num�ro
     *
     * @param assetClassificationId Le num�ro de l'axe
     *
     * @return Le libell� correspondant au num�ro
     */
    public String getAssetClassificationLabel(String assetClassificationId) {
        if ("".equals(assetClassificationId)) {
            return "inconnu";
        }
        return getLabel("PR_ASSET_CLASSIFICATION", "ASSET_CLASSIFICATION_NAME",
            "ASSET_CLASSIFICATION_ID", new Integer(assetClassificationId), null);
    }


    /**
     * Retourne le libell� d'une devise / code
     *
     * @param currencyCode Le code
     *
     * @return Le libell� correspondant au code
     */
    public String getCurrencyLabel(String currencyCode) {
        return getLabel("BO_REF_CURRENCY", "CURRENCY_NAME", "CURRENCY", currencyCode, null);
    }


    /**
     * Retourne le libell� d'une poche / code
     *
     * @param sleeveCode Le code
     *
     * @return Le libell� correspondant au code
     */
    public String getSleeveLabel(String sleeveCode) {
        return getLabel("PR_ASSET_CLASS_STRUCTURE", "ASSET_SLEEVE_NAME",
            "ASSET_SLEEVE_CODE", sleeveCode, null);
    }


    /**
     * Retourne le libell� d'un pays / code
     *
     * @param countryCode Description of the Parameter
     *
     * @return Le libell� correspondant au code
     */
    public String getCountryLabel(String countryCode) {
        return getLabel("BO_REF_COUNTRY", "COUNTRY_NAME", "COUNTRY", countryCode, null);
    }


    /**
     * Retourne le libell� d'un titre / code & p�riode
     *
     * @param securityCode Description of the Parameter
     * @param period Description of the Parameter
     *
     * @return Le libell� correspondant au code
     */
    public String getSecurityLabel(String securityCode, String period) {
        return getLabel("BO_SECURITY", "SECURITY_NAME", "SECURITY_CODE", securityCode,
            period);
    }


    /**
     * Retourne l'attribut label de FillLinkedComponent
     *
     * @param dbTableName Le nom physique de la table o� se trouve le libell�
     * @param dbLabelFieldName Le nom physique du champ o� se trouve le libell�
     * @param dbCodeFieldName Le nom physique du champ o� se trouve l'identifiant
     * @param codeValue La valeur de l'identifiant
     * @param period Description of the Parameter
     *
     * @return Le libell�
     */
    private String getLabel(String dbTableName, String dbLabelFieldName,
        String dbCodeFieldName, Object codeValue, String period) {
        Statement stmt = null;
        ResultSet rs = null;
        String label = "";
        Connection con = null;
        try {
            con = conMan.getConnection();
            stmt = con.createStatement();
            String query =
                "select " + dbLabelFieldName + " from " + dbTableName + " where "
                + dbCodeFieldName;
            if (codeValue instanceof Integer) {
                query += "=" + codeValue;
            }
            else {
                query += "='" + codeValue + "'";
            }
            if (period != null) {
                query += " and PERIOD='" + period + "'";
            }
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                label = rs.getString(dbLabelFieldName);
            }
            else {
                label = "inconnu";
            }
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            label = "erreur SQL !";
        }
        finally {
            try {
                conMan.releaseConnection(con, stmt);
            }
            catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            finally {
                return label;
            }
        }
    }
}
