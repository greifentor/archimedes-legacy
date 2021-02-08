/*
 * CoreNTX.java
 *
 * 21.07.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corentx;


import logging.Logger;


/**
 * CoreNTX ist eine Neufassung der CoreNT-Bibliothek. Die enthaltenen Klassen, so sie auf
 * CoreNT basieren, sind &uuml;berarbeitet und konsequent mit Tests versehen.
 *
 * <P>&nbsp;
 *
 * <P><H3>Versionshistorie</H3>
 * <P>Die Projektsourcen k&ouml;nnen unter dem folgenden Link heruntergeladen werden. Das Archiv
 * muss in einen Ordner "corentx" ausgecheckt werden, und kann dann mit ant und der Builddatei
 * bearbeitet werden.
 * <P><I><B>Hinweis:</B> &Auml;nderungen sind unbedingt an den O.Lieshoff weiterzumelden, um in
 * den offiziellen Entwicklungspfad &uuml;bernommen zu werden.</I>
 *
 * <P>&nbsp;
 * <A HREF="../corentx-src.zip">Projektsourcen hier herunterladen</A>
 *
 * <P>&nbsp;
 * <A HREF="../corentx.jar">JAR-Archiv (ohne Versionsnummer) hier herunterladen</A>
 *
 * <P>&nbsp;
 * 
 * <P><H3>Offene &Auml;nderungen</H3>
 *
 * TODO (5) OLI 08.09.2010 - Erweiterung um eine Utility-Klasse zum Auslesen von IP- und
 *         MAC-Adresse des Rechners, auf dem die VM l&auml;uft. Die MAC-Adresse l&auml;sst sich
 *         &uuml;ber die Klasse java.net.NetworkInterface.getHardwareAddress() auslesen. Das aus
 *         diese Methode resultierende byte-Array muss umformatiert werden:
 *         System.out.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
 *
 * <P><H3>Versionshistorie</H3>
 * <TABLE BORDER=1 WIDTH="100%">
 *     <TR>
 *         <TH>Version</TH>
 *         <TH>&Auml;nderungen</TH>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.46</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Extension of the FileUtil by a method which is able to determine the file
 *                    encoding while reading a string list. 
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.45</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Extension of the copy file logic by buffer size defined by the customer
 *                    and having the option to assign a progress listener. 
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.44</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Extended by the process factory logic. 
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.43</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Extended by the stream monitor logic. 
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.42</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Extended by the call counter logic. 
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.40</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um die Klassen zum Lesen und Ausf&uuml;hren eines JavaScripts.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.38</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Einf&uuml;hrung der Klasse <CODE>XMLAttribute</CODE>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.37.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Einf&uuml;hrung des Interfaces f&uuml;r monadischen Operatoren im Package
 *                    <CODE>corentx.ds.expr</CODE>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.36</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Herausnahme der &ouml;ffentlichen Konstruktoren f&uuml;r die
 *                    XML-Node-Klassen. Einf&uuml;hrung einer Factory-Klasse zum Zwecke der
 *                    Erzeugung neuer XML-Nodes.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.35.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um eine M&ouml;glichkeit die Lenient-Einstellung &uuml;ber
 *                    eine Konstante zu regeln. 
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.34.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um die Expression-Implementierung <CODE>In</CODE>. 
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.33.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Die Datumsklassen (PDate, PTime, PTimestamp und LongPTimestamp) werden nun
 *                    bei Nutzung der <CODE>valueOf(...)</CODE>-Methoden validiert. Diese
 *                    Funktionalit&auml;t ist &uuml;ber die Properties
 *                    <CODE>corentx.dates.lenient.suppressed.global</CODE> und
 *                    <CODE>corentx.dates.lenient.suppressed.{SimpleClassName]</CODE>
 *                    abschaltbar. 
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.32.2</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Debugging und Generalisierung der monadischen Ausdr&uuml;cke auf eine
 *                    gemeinsame Basisklasse.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.32.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der Expressions um eine funktionsf&auml;hige
 *                    <CODE>equals(Object)</CODE>-Methode.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.31.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um die Map-Persistierung im <CODE>corent.io</CODE>-Package.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.30.4</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Aufnahme der Entit&auml;tenumwandlung in den XMLValue.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.30.3</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der Utl-Klasse um Methoden zur Generierung von hashCodes
 *                    f&uuml;r boolean und long Werte.
 *                </LI>
 *                <LI>
 *                    Erweiterung, um eine Klasse zur &Ouml;ffnung von XML-Streams. 
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.30.2</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der XML-Komponenten um Konstructoren f&uuml;r, die bereits die
 *                    Verbindung zu einem Eltern-Objekt herstellen. 
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.30.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um die Klassen zur Darstellung einer XML-Struktur.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.29.2</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der Klasse <CODE>TimestampUtil</CODE> um die Methode
 *                    getWeekDay(...) f&uuml;r PTimestamps, LongPTimestamps und PDates.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.29.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um eine erste Version des Eingabedialoges f&uuml;r PDates
 *                    (als &Uuml;bernahme aus der corent-Bibliothek).
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.28.2</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der Klasse <TT>Checks</TT>, sodass sie nicht nur
 *                    RuntimeExceptions, sondern auch Exceptions werfen kann.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.28.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der Klasse <TT>Checks</TT> aus dem package
 *                    <TT>corentx.util</TT> um die Methoden
 *                    <TT>ensureNotNull(Object, String)</TT>,
 *                    <TT>ensureNotEmpty(String, String)</TT> und
 *                    <TT>ensureNotNullAndNotEmpty(String, String)</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.27.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um Klassen zur Ausf&uuml;hrung von Java-Scripten.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.26.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um eine Factory zur Erzeugung des Systemdatums. Dieses
 *                    Konstrukt soll vor allem in Testumgebungen zum Einsatz kommen, in denen
 *                    ein festes, testbares Datum w&uuml;nschenswert ist.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.25.3</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Debugging am <TT>QueryLimiter</TT>: Queries, die unter dem HSQL-Modus
 *                    behandelt werden sollen, werden nun nicht nicht mehr per Replacement,
 *                    sondern durch Abschneiden der ersten sechs Zeichen und Voranstellen eines
 *                    neuen, gegebenenfalls um das Limit-Statement erweitertes "select"
 *                    abge&auml;ndert.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.25.2</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Debugging am <TT>QueryLimiter</TT>: Queries, die als Feld- und/oder
 *                    Tabellennamen den String "select" enthalten, werden korrekt behandelt. 
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.25.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um die Klasse <TT>QueryLimiter</TT>, die zur Erweiterung eines
 *                    Query um ein Limit-/Offset-Statement dient.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.24.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der Klasse <TT>DBDataSourceUtil</TT> um die Methode
 *                    <TT>getFieldFullNames(List&kl;Attribute&gt; lattr)</TT>.
 *                </LI>
 *                <LI>
 *                    Erweiterung der <TT>OrderExpression</TT>-Implementierungen um eine
 *                    <TT>equals(Object)</TT>-Implementierung.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.23.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der <TT>TimestampUtil</TT>-Klasse um die Methode
 *                    <TT>diff(Timestamp, Timestamp)</TT>.
 *                </LI>
 *                <LI>
 *                    Erweiterung um die Expressions Div und Mod, sowie die zusammengesetzten
 *                    Expression NTime.
 *                </LI>
 *                <LI>
 *                    Erweiterung der Klasse <TT>TransactionDBDataSourceContext</TT> um die
 *                    Methode <TT>getInfos()</TT> zur Realisation von Statusreports zur
 *                    Connectionvergabe.
 *                </LI>
 *                <LI>
 *                    Erweiterung um die Klasse <TT>ClipboardCleaner</TT> im Package
 *                    <TT>corentx.gui.swing</TT>. Mit Hilfe dieser Class l&auml;sst sich ein
 *                    Clipboard bereinigen oder ein Transferable erstellen, das die Aufgabe
 *                    &uuml;bernehmen kann.
 *                </LI>
 *                <LI>
 *                    Erweiterung um die Klasse <TT>TestSystemExiterListener</TT> im Package
 *                    <TT>corentx.util</TT>. Diese Implementierung des SystemExiterListeners
 *                    ist zur Nutzung im Testumfeld gedacht.
 *                    <BR>In diesem Rahmen ist die Spezifikation der Methode
 *                    <TT>systemExitPerformed(SystemExiterEvent)</TT> so angepasst worden, dass
 *                    <TT>null</TT>-Events nicht &uuml;bergeben werden d&uuml;rfen.
 *                </LI>
 *                <LI>
 *                    Erweiterung um das Annotationenpackage und die Annotation Immutable.
 *                </LI>
 *                <LI>
 *                    Erweiterung um die Methode <TT>overrideWithAsterix(String)</TT> in der
 *                    Klasse <TT>FileUtil</TT>.
 *                </LI>
 *                <LI>
 *                    Erweiterung um eine Implementierung der Recorderlogik im Package
 *                    <TT>corentx.util.recorder</TT>.
 *                </LI>
 *                <LI>
 *                    Bereitstellen der Methoden <TT>getPDate(Integer)</TT>,
 *                    <TT>getPDate(Object)</TT>, <TT>getPTime(Integer)</TT>,
 *                    <TT>getPTime(Object)</TT>, <TT>getPTimestamp(BigDecimal)</TT>,
 *                    <TT>getPTimestamp(Long)</TT> und <TT>getPTimestamp(Object)</TT> in der
 *                     Klasse <TT>DBDataSourceUtil</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.22.2</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um die Klasse <TT>corentx.util.Checks</TT>.
 *                </LI>
 *                <LI>
 *                    Erweiterung der Klasse <TT>FileUtil</TT> um die Methoden
 *                    <TT>writeProperties(String)</TT> und <TT>writeProperties(Properties,
 *                    String)</TT>
 *                </LI>
 *                <LI>
 *                    Erweiterung um die Klasse <TT>corentx.dates.PTime</TT>.
 *                </LI>
 *                <LI>
 *                    Erweiterung der Klasse <TT>DBDataSourceUtil</TT> um die Methoden
 *                    <TT>getPTime(ResultSet, int)</TT> und <TT>toDBString(PTime)</TT>.
 *                </LI>
 *                <LI>
 *                    Implementierung der <TT>hashCode</TT>-Methoden f&uuml;r die Klassen
 *                    <TT>PTimestamp</TT> und <TT>PDate</TT>.
 *                </LI>
 *                <LI>
 *                    Erweiterung um die Taglets f&uuml;r die Vor- unc Nachbedingungen von
 *                    Methoden und die Implementierungsdetails.
 *                </LI>
 *                <LI>
 *                    Debugging: Die Methode DBDataSourceUtil.getNumber(ResultSet, int, double)
 *                    liefert nun tats&auml;chlich Double-Werte zur&uuml;ck.
 *                </LI>
 *                <LI>
 *                    Erweiterung um die Methode <TT>Str.isNullOrEmtpy(String)</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.22.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um den SystemExiter und sein Umfeld.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.21.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um das PerformanceTracker-Framework.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.20.4</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung des Interfaces <TT>corentx.ds.Attribute</TT> um die Methode
 *                    <TT>getFullName()</TT>. Diese Methode wird vom
 *                    <TT>corentx.ds.expr.Identifier</TT> genutzt, um bei Evaluierungen den
 *                    Namen eines Attributs zu bilden.
 *                </LI>
 *                <LI>
 *                    Ausgabe der aktiven Connections in der Klasse
 *                    <TT>TransactionDBDataSourceContext</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.20.3</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um das Interface <TT>corentx.io.StringReader</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.20.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der FileUtil-Klasse um die Methoden <TT>copyFile</TT> und
 *                    <TT>moveFile</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.19.2</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    <B>WICHTIG!</B> Korrektur der <TT>null</TT>-Logik in den Methoden
 *                    <TT>compareNullRef(Object, Object)</TT> und <TT>compareRef(Comparable,
 *                    Comparable)</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.19.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um die Methode <TT>FileUtil.cutLastPathDir(String)</TT>.
 *                    <BR>Implementierung Serializable im LockableWrapper.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.18.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    &Uuml;bertragen rudiment&auml;rer Lock-Logik aus der corent-Bibliothek.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.17.2</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    &Auml;nderungen an der Methode Str.toCamilCase(): Die Gro&szlig;buchstaben
 *                    in den Worten bleiben nun unver&auml;ndert.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.17.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um die Title-Updater-Logik im Package
 *                    <TT>corentx.gui.swing</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.16.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um die Focus-Liste f&uuml;r GUI-Komponenten.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.15.2</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um die Container-Klasse <TT>XY</TT>, die ein Tupel aus einer
 *                    X- und eine Y-Koordinate speichern kann.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.15.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der Klasse GUIUtil um die Methode <TT>findComponent(String,
 *                    Component)</TT>, die eine Komponente anhand ihres Namens finden kann.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.14.4</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>Debugging am DocTaglet.</LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.14.3</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um Debuginformationen in der Klasse
 *                    <TT>TransactionDBDataSourceContext</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.14.2</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Korrektur des Comma-Operators durch Herausnahme der Klammern bei der
 *                    Auswertung.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.14.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Anpassung der Ausgabe des ILike-Operators an einen Datenbankenmodus. Die
 *                    erste Implementierung erfolgt &uuml;ber die Property
 *                    <I>corentx.ds.expr.ILike.db.mode</I> und erfordert den Namen einer
 *                    <TT>corent.db.DBExecMode</TT>-Konstanten. Der Operator liefert
 *                    zun&auml;chst nur im PostGreSQL-Modus ein "ilike" zur&uuml;ck. Alle
 *                    anderen Modi bekommen ein "like" zur&uuml;ck.
 *                </LI>
 *                <LI>
 *                    Aufnahme des Doc-Tags in die Taglet-Bibliothek.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.12.2</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um einen UserContext und eine UserContext-abh&uml;ngige
 *                    TechChangeAuxiliaryData-Implementierung.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.12.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der Klasse <TT>TransactionDBDataSourceContext</TT> um die
 *                    M&ouml;glichkeit der Anbindung von <TT>javax.sql.DataSources</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.11.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der Utility-Klasse <TT>corentx.util.Str</TT> um die Methode
 *                    <TT>toCamilCase(String)</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.10.3</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Debugging am EvaluationUtil. Die Methode <TT>isBefore</TT> kommt nun auch
 *                    mit Attributen zurecht, die <TT>null</TT>-Werte enthalten.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.10.2</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Umbenennung der Methode getId() nach getKey() im Interface Identifiable.
 *                    Hiermit wird eine wahrscheinliche Kollision mit anderen Methoden der
 *                    implementierenden Objekte verhindert.
 *                </LI>
 *                <LI>Erweiterung um die Auswertung des Like-Operators im WhereEvaluator.</LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.10.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um die speicherbasierte Datenquelle
 *                    <TT>AbstractDataSource</TT>. Die Implementierung erfolgte vorallem mit
 *                    Blick auf das Umfeld der Unittests, in dem die DataSource dazu genutzt
 *                    werden kann DataSources wegzumocken.
 *                </LI>
 *                <LI>Erweiterung um die Utility-Sammlung <TT>EvaluationUtil</TT>.</LI>
 *                <LI>Erweiterung um den ilike-Operator <TT>corentx.ds.expr.ILike</TT>.</LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.9.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der Klasse <TT>corentx.io.FileUtil</TT> um die Methoden
 *                    zum Einlesen von Properties-Dateien.
 *                </LI>
 *                <LI>
 *                    Erste Version der Utilityklasse <TT>corentx.gui.swing.GUIUtil</TT>.
 *                </LI>
 *                <LI>
 *                    Erweiterung um das Interface <TT>corentx.ds.MemoryDataSource</TT>.
 *                </LI>
 *                <LI>
 *                    Erweiterung um die Methoden <TT>corentx.uti.Str.nextSubstring(String,
 *                    String)</TT> und <TT>corentx.util.Str.nextTailString(String, String)</TT>.
 *                </LI>
 *                <LI>Erweiterung des WhereEvaluators um IsNull und IsNotNull.</LI>
 *                <LI>
 *                    Erweiterung der Methoden getElements und getView um die Angabe eines
 *                    offsets.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.8.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der Klasse <TT>corentx.util.Utl</TT> um die Methode
 *                    <TT>equals(Object, Object)</TT>, die auch mit <TT>null</TT>Referenzen
 *                    arbeiten kann.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.7.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der Klasse <TT>corentx.util.Str</TT> um die Methode
 *                    <TT>quote(String, String)</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.6.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung Klasse <TT>corentx.util.Str</TT> um die Methode
 *                    <TT>spaces(int)</TT>.
 *                </LI>
 *                <LI>
 *                    Entfernung der restlichen Bez&uuml;ge auf corent.dates.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.5.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung der Datums- und Zeitstempelklassen um die statische Methode
 *                    <TT>isNullValue()</TT> mit Hilfe dessen gepr&uuml;ft werden kann, ob ein
 *                    Zahlenwert eine Null-Referenz ergibt.
 
 *                </LI>
 *                <LI>
 *                    &Auml;nderung der Signatur der Methode <TT>releaseConnection()</TT>
 *                    (vorher <TT>releaseConnection(Connection)</TT>). Die Implementierungen
 *                    m&uuml;ssen die Connections nun selbstst&auml;ndig speichern, wenn sie
 *                    zu schliessen sein sollen bzw. die Implementierung des Schlie&szlig;ens
 *                    auf den Benutzer verlagern.
 *                    <BR>Dabei Anpassung der Implementierungen des Interfaces.
 *                </LI>
 *                <LI>
 *                    Erweiterung um die Klasse <TT>DBDataSourceUtil</TT> mit einer Sammlung
 *                    von Methoden, die der Arbeit mit der Datenbank dienen.
 *                </LI>
 *                <LI>
 *                    Entfernung der corent.dates-Nutzung aus dem Package corentx.ds.
 *                </LI>
 *                <LI>
 *                    Herstellung der Serialisierbarkeit von <TT>PDate</TT>,
 +                    <TT>LongPTimestamp</TT> und <TT>PTimestamp</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.4.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung den WhereEvaluator zur Nutzung der Expressions auch abseits
 *                    von SQL.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.3.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung um die AuxiliaryTransactionData und die DataSourceContexts im
 *                    DataSource-Bereich.
 *                </LI>
 *                <LI>Erweiterung um den LongPTimestamp.</LI>
 *                <LI>Where- und Order-Expressions inklusive ExpressionBuilder.</LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.2.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Erweiterung des <TT>DataSource</TT>-Interfaces um die Methode
 *                    <TT>remove(T)</TT>.
 *                </LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD ALIGN=CENTER><B>1.1.1</B></TD>
 *         <TD>
 *            <BR>
 *            <UL>
 *                <LI>
 *                    Neufassung des <TT>dates</TT>-Packages. Alle Klassen sind auf ein
 *                    Interface (<TT>Timestamp</TT>) programmiert.</LI>
 *            </UL>
 *            <BR>
 *         </TD>
 *     </TR>
 * </TABLE>
 *
 * <BR><HR SIZE=1>
 * <P>&nbsp;
 *
 * @author O.Lieshoff
 *
 * @changed OLI 21.07.2009 - Hinzugef&uuml;gt
 * @changed OLI 01.10.2009 - Umwandlung der &Auml;nderungshistorie in Tabellenform.
 */

public class CoreNTX {

    /* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
    private static Logger log = Logger.getLogger(CoreNTX.class);

    /**
     * Liefert Versionsnummer der CoreNTX.
     *
     * @return Liefert die Versionsnummer der CoreNTX-Bibliothek.
     *
     * @changed OLI 21.07.2009 - Hinzugef&uuml;gt;
     *
     */
    public static String GetVersion() {
        return Version.INSTANCE.getVersion();
    }

    /** Gibt die CoreNTX-Versionsnummer auf der Konsole aus. */
    public static void ShowVersion() {
        if (Boolean.getBoolean("corentx.CoreNTX.showversion")) {
            System.out.println("CoreNTX version " + CoreNTX.GetVersion());
        }
        log.info("CoreNTX version " + CoreNTX.GetVersion());
    }

}
