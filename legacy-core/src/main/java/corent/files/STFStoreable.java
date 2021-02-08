/*
 * STFStoreable.java
 *
 * 11.05.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.files;


/**
 * Dieses Interface definiert Funktionalit&auml;t, die zur Speicherung implementierender Objekte
 * in ein StructuredTextFile n&ouml;tig sind.
 * <P>Hier&uuml;ber k&ouml;nnen Objekte geschaffen werden, die klar erkennbar in eine STF-Datei
 * gespeichert werden und daraus gelesen werden k&ouml;nnen. F&uuml;r die 
 * <TT>JDBCDataSourceRecord</TT> aus dem package <TT>corent.db</TT> sieht die Implementierung 
 * des Interfaces wie folgt aus:
 * <PRE>
 *    public void toSTF(StructuredTextFile stf, String[] path) {
 *        stf.writeStr(StructuredTextFile.AddPath(path, "DBName"), StrUtil.ToHTML(this.getDBName()
 *                ));
 *        stf.writeStr(StructuredTextFile.AddPath(path, "Description"), StrUtil.ToHTML(
 *                this.getDescription()));
 *        stf.writeStr(StructuredTextFile.AddPath(path, "Driver"), StrUtil.ToHTML(this.getDriver()
 *                ));
 *        stf.writeStr(StructuredTextFile.AddPath(path, "Name"), StrUtil.ToHTML(this.getName()));
 *        stf.writeStr(StructuredTextFile.AddPath(path, "User"), StrUtil.ToHTML(this.getUser())
 *                );
 *    }
 *    
 *    public void fromSTF(StructuredTextFile stf, String[] path) {
 *        this.setDBName(StrUtil.FromHTML(stf.readStr(StructuredTextFile.AddPath(path, "DBName"), 
 *                "")));
 *        this.setDescription(StrUtil.FromHTML(stf.readStr(StructuredTextFile.AddPath(path, 
 *                "Description"), "")));
 *        this.setDriver(StrUtil.FromHTML(stf.readStr(StructuredTextFile.AddPath(path, "Driver"), 
 *                "")));
 *        this.setName(StrUtil.FromHTML(stf.readStr(StructuredTextFile.AddPath(path, "Name"), 
 *                "")));
 *        this.setUser(StrUtil.FromHTML(stf.readStr(StructuredTextFile.AddPath(path, "User"), 
 *                "")));
 *        this.setPassword("");
 *    }
 * </PRE>
 * &Uuml;ber diese Methoden kann der Inhalt des <TT>JDBCDataSourceRecord</TT> in einer STF-Datei
 * abgelegt bzw. aus ihr gelesen werden. 
 *
 * @author O.Lieshoff
 *
 */

public interface STFStoreable {
    
    /**
     * Speichert das Objekt unter dem angegebenen Pfad in das angegebene STF.
     *
     * @param stf Das STF, in das die Objekt-Daten gespeichert werden sollen.
     * @param path Der Pfad, unterhalb dessen die Objekt-Daten im STF abgelegt sind.
     */
    public void toSTF(StructuredTextFile stf, String[] path);
    
    /**
     * L&auml;dt die Objekt-Daten unter dem angegebenen Pfad aus dem angegebenen STF.
     *
     * @param stf Das STF, aus dem die Objekt-Daten gelesen werden sollen.
     * @param path Der Pfad, unterhalb dessen die Objekt-Daten im STF abgelegt sind.
     */
    public void fromSTF(StructuredTextFile stf, String[] path);
    
}
