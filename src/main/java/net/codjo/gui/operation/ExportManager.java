/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.gui.operation;
import net.codjo.gui.toolkit.swing.SwingWorker;
import net.codjo.gui.toolkit.util.ErrorDialog;
import net.codjo.operation.OperationInterruptedException;
import net.codjo.utils.sql.DataFormater;
import net.codjo.utils.sql.GenericTable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Classe g�rant l'export et sa progression.
 *
 * @author $Author: blazart $
 * @version $Revision: 1.3 $
 */
public class ExportManager {
    private int lengthOfTask;
    private int current = 0;
    private GenericTable genericTable;
    private String filename;
    private SwingWorker taskWorker;
    private DataFormater dataFormater;
    private boolean doReloadAtEnd;


    /**
     * Constructeur.
     *
     * @param g        La GenericTable.
     * @param filename Le nom du fichier d'export.
     * @param doReload TODO
     *
     * @throws IllegalArgumentException TODO
     */
    ExportManager(GenericTable g, String filename, boolean doReload) {
        if (g == null || filename == null) {
            throw new IllegalArgumentException();
        }
        genericTable = g;
        this.filename = filename;
        doReloadAtEnd = doReload;
        lengthOfTask = genericTable.getNumberOfRows();
    }


    /**
     * Retourne le SwingWorker.
     *
     * @return Le SwingWorker.
     */
    SwingWorker getTaskWorker() {
        return taskWorker;
    }


    /**
     * Retourne le nombre de lignes � exporter.
     *
     * @return Le nombre de lignes � exporter.
     */
    int getLengthOfTask() {
        return lengthOfTask;
    }


    /**
     * Retourne le num�ro de la ligne export�e.
     *
     * @return Le num�ro de la ligne export�e.
     */
    int getCurrent() {
        return dataFormater.getCurrentLine();
    }


    /**
     * Retourne le message � afficher sur le ProgressMonitor.
     *
     * @return Le message � afficher sur le ProgressMonitor.
     */
    String getMessage() {
        return "Enregistrement " + getCurrent() + " / " + getLengthOfTask() + ".";
    }


    /**
     * Lance l'export.
     *
     * @param endOperationListener Runner dont la m�thode run sera appel�e en fin de t�che (dans le thread
     *                             Event).
     */
    void go(Runnable endOperationListener) {
        current = 0;
        try {
            File outputFile = new File(filename);
            FileWriter out = new FileWriter(outputFile);
            dataFormater = new DataFormater(genericTable);
            taskWorker =
                  new TaskWorker(out, dataFormater, endOperationListener,
                                 doReloadAtEnd);
            taskWorker.start();
        }
        catch (IOException ex) {
            ex.printStackTrace();
            ErrorDialog.show(genericTable, "L'export a �chou� !", ex);
        }
    }


    /**
     * Arr�te le ProgressMonitor.
     */
    void stop() {
        current = lengthOfTask;
    }


    /**
     * Indique si l'export est termin�.
     *
     * @return true si termin� false sinon.
     */
    boolean done() {
        return current >= lengthOfTask;
    }


    /**
     * Execute l'export dans un thread de type SwingWorker.
     *
     * @author $Author: blazart $
     * @version $Revision: 1.3 $
     */
    private class TaskWorker extends SwingWorker {
        private FileWriter out;
        private DataFormater dataFormater;
        private Runnable endOperationListener;
        private boolean doReloadAtEnd;


        /**
         * Constructeur.
         *
         * @param out                  Le FileWriter utilis� pour l'�criture.
         * @param dataFormater         L'objet qui g�re l'�criture.
         * @param endOperationListener Runner dont la m�thode run sera appel�e en fin de t�che (dans le thread
         *                             Event).
         * @param doReload             TODO
         */
        private TaskWorker(FileWriter out,
                           DataFormater dataFormater,
                           Runnable endOperationListener,
                           boolean doReload) {
            this.out = out;
            this.dataFormater = dataFormater;
            this.endOperationListener = endOperationListener;
            doReloadAtEnd = doReload;
        }


        /**
         * Execute l'export.
         *
         * @return -
         */
        @Override
        public Object construct() {
            try {
                dataFormater.buildDataForExport(out);
                out.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
                ErrorDialog.show(genericTable, "L'export a �chou� !", ex);
            }
            catch (OperationInterruptedException ex) {
                ex.printStackTrace();
                ErrorDialog.show(genericTable, "L'export a �chou� !", ex);
            }
            return null;
        }


        /**
         * Previent l'IHM et recharge les donn�es (on revient sur la 1�re page).
         */
        @Override
        public void finished() {
            if (doReloadAtEnd) {
                endOperationListener.run();
            }
        }
    }
}
