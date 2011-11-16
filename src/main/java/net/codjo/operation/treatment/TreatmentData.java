/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.operation.treatment;
import net.codjo.model.Table;
import net.codjo.operation.AnomalyReport;
import net.codjo.operation.Operation;
/**
 * Implementation de l'interface OperationData pour les applications Penelope/Alis.
 *
 * @author $Author: blazart $
 * @version $Revision: 1.5 $
 *
 * @see net.codjo.operation.treatment.OperationData
 */
public class TreatmentData implements OperationData {
    private Operation operation;

    /**
     * Constructeur.
     *
     * @param operation L'operation.
     *
     * @throws IllegalArgumentException TODO
     */
    public TreatmentData(Operation operation) {
        if (operation == null) {
            throw new IllegalArgumentException();
        }
        this.operation = operation;
    }

    /**
     * Retourne la p�riode de l'op�ration.
     *
     * @return La p�riode de l'op�ration.
     */
    public String getPeriod() {
        return operation.getPeriod().getPeriod();
    }


    /**
     * Retourne la p�riode N-1 de l'op�ration.
     *
     * @return La p�riode de l'op�ration.
     */
    public String getPreviousPeriod() {
        return operation.getPreviousPeriod();
    }


    /**
     * Retourne le groupe de portefeuilles de l'op�ration.
     *
     * @return Le groupe de portefeuilles de l'op�ration.
     */
    public String getPortfolioGroupName() {
        return operation.getPortfolioGroupName();
    }


    /**
     * Retourne le behavior de l'op�ration.
     *
     * @return Le behavior de l'op�ration.
     */
    public TreatmentBehavior getLoadedBehavior() {
        return (TreatmentBehavior)operation.getLoadedBehavior();
    }


    /**
     * Retourne l'anomalyReport de l'op�ration.
     *
     * @return L'anomalyReport de l'op�ration.
     */
    public AnomalyReport getAnomalyReport() {
        return operation.getAnomalyReport();
    }


    /**
     * Construction de la clause "from" pour une table donn�e.
     *
     * @param tableOfQuery Table sur laquelle va porter la requ�te
     *
     * @return Une liste de table (ex: "AP_PORTFOLIO, BO_PORTFOLIO")
     */
    public String buidTableClauseFor(Table tableOfQuery) {
        return operation.buidTableClauseFor(tableOfQuery);
    }


    /**
     * Construction de la clause "where" pour une table donn�e.
     *
     * @param tableOfQuery Table sur laquelle vas porter la clause "where"
     *
     * @return Une clause where (ex: " where ...") ou null
     */
    public String buildWhereClauseFor(Table tableOfQuery) {
        return operation.buildWhereClauseFor(tableOfQuery);
    }


    public Operation getOperation() {
        return operation;
    }
}
