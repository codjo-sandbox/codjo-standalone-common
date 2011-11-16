/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.operation.imports;
/**
 * Interface decrivant un filtre d'import.
 *
 * @author $Author: blazart $
 * @version $Revision: 1.3 $
 *
 */
public interface ImportFilter {
    /**
     * Indique si la ligne pass� en param�tre doit �tre filtr�e (cad elle n'est pas
     * import�e).
     *
     * @param line La ligne a tester
     *
     * @return <code>true</code> si la ligne est filtr�e.
     */
    public boolean filteredLine(String line);
}
