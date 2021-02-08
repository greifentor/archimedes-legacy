/*
 * Corent.java
 *
 * 08.02.2007
 *
 * (c) by O.Lieshoff
 *
 */

package corent;


import logging.Logger;


/**
 * In dieser Klasse ist eigentlich nicht vielmehr als ein Versionshinweis zur Corent-Bibliothek
 * untergebracht.
 * <P>F&uuml;her diente sie zum &Uuml;bersetzen der Corent-Klassen.
 * <BR>
 * <HR SIZE=1>
 * <H2>Geplante oder in Arbeit befindliche Features</H2>
 * <HR SIZE=1>
 * <OL>
 *     <LI>Konfigurierbarer PDate- und PTimestamp-String (z. B. format).</LI>
 *     <LI>Plus-/Minus-Button f&uuml;r PDate-Editor-Element.</LI>
 *     <LI>Bei zeitaufw&auml;ndigen Operationen w&auml;re eine Sanduhr sch&ouml;n.</LI>
 *     <LI>Eigenes JOptionPane mit frei definierbaren Defaultbuttons.</LI>
 *     <LI>
 *         Einbau einer Backup-Strategie f&uuml;r Wartungsdialoge (zur Absicherung von 
 *         Abst&uuml;rzen und dem Umgehen des aktiven Zwischenspeicherns).
 *     </LI>
 *         Anzeige der Locks (wer und von wo) in der Oberfl&auml;che.
 *     </LI>
 * </OL>
 * <BR>&nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <H2>Verworfene Features</H2>
 * <HR SIZE=1>
 * <OL>
 *     <LI>
 *         <I>Begrenzung der Action-Ids durch einen enum-Typen:</I> Das Problem l&auml;&szlig;t
 *         nicht so l&ouml;sen, da&szlig; Fehler bereits w&auml;hrend der Laufzeit entdeckt 
 *         werden. Zudem ist es aufgrund fehlender M&ouml;glichkeiten der Vererbung von Enums
 *         nicht m&ouml;glich Instanzengleichheit bei RMI-Betrieb zu gew&auml;hrleisten. Das 
 *         bringt also erstmal nichts ...
 *     </LI>
 * </OL>
 * <BR>&nbsp;
 *
 * <HR>
 * <H2>Neue Features</H2>
 *
 * <BR><HR SIZE=1>
 * <P><B>1.67.1</B>
 * <P>Korrektur in den SelectionEditorDjinn-Klassen.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.66.1</B>
 * <P>Erweiterung um das Interface <CODE>NamedObject</CODE>. 
 * <P>Erweiterung der <CODE>ExtendedColor</CODE> um das Interface <CODE>NamedObject</CODE>. 
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.65.1</B>
 * <P>Erweiterung des <CODE>FrameTextViewer</CODE> um einen Copy-All-Button. 
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.64.2</B>
 * <P>Erweiterung der DBExecMode-Klasse um einen Bezeichner f&uuml;r Standard-SQL.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.64.1</B>
 * <P>Implementierung einer M&ouml;glichkeit, an das Textfeld in einem TextAreaDialog einen
 *         KeyListener anzubinden.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.63.1</B>
 * <P>Umwandlung der <TT>DBExecMode</TT>-Klasse in einen echten Enum-Typ.
 * <P>Erweiterung der <TT>DBExecMode</TT>-Klasse um eine Methode, &uuml;ber die ein
 *         Teststatement geholt werden kann, mit Hilfe dessen die Ansprechbarkeit der Datenbank
 *         gepr&uuml;ft werden kann.
 * <P>Erweiterung des MemoryMonitors um die R&uuml;ckage einer Frame-Referenz f&uuml;r die
 *         show-Methode und Aktivierung des Close-Buttons.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.62.1</B>
 * <P>Erweiterung der <TT>DBExec</TT>-Klasse um die den Listener mit Hilfe dessen auf die
 *         Ereignisse innerhalb der DBExec-Instanzen reagiert werden kann.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.61.4</B>
 * <P>Erweiterung PDateDialoges um die Kennzeichnung von Feiertagen.
 * <P>Erweiterung des Interfaces <TT>RessourceManager</TT> um die Methode <TT>getText(String,
 *         String)</TT>. Anpassung der Implementierungen des Interfaces.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.61.3</B>
 * <P>Erweiterung des AbstractUPNInterpreters um die M&ouml;glichkeit, Exceptions an die
 *         aufrufende Programmschicht weiterzumelden.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.61.2</B>
 * <P>Korrektur in der Klasse <TT>PTime</TT>: Bei &Uuml;bergabe des Wertes <TT>0</TT> wird nun
 *         keine Exception mehr von den Methoden <TT>naechsteMinute(int)</TT>,
 *         <TT>naechsteSekunde(int)</TT>, <TT>vorherigeMinute(int)</TT> und
 *         <TT>vorherigeSekunde(int)</TT> geworfen.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.61.1</B>
 * <P>Erweiterung der Klasse <TT>SysUtil</TT> um die Methode <TT>equalsRef(Object, Object)</TT>.
 * <P>Implementierung des Interfaces <TT>Ressourced</TT> in der Klasse COCheckBox.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.60.2</B>
 * <P>Umwandlung von System.out.prints in log4j-Aufrufe in einigen Klassen (DBUtil, jsql).
 * <P>Erweiterung des TextArea-Dialoges, der in der <TT>ModalLineTextEditorComponentFactory</TT>
 *         generiert wird um die M&ouml;glichkeit eine Line am Zeilenende einzublenden.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.60.1</B>
 * <P>Umwandlung von System.out.prints in log4j-Aufrufe in einigen Klassen (DBFjsqlw, DBFReader,
 *         und DBType).
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.59.2</B>
 * <P>Erweiterung der DBFactoryUtil-Klasse um eine Property zu Einschr&auml;nkung von bestimmten
 *         Reconstructable-Tabellen.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.58.3</B>
 * <P>Umwandlung von System.out.prints in log4j-Aufrufe in einigen Klassen (DBExec, MrMemory).
 * <P>Einbau einer M&ouml;glichkeit den Konsolenoutput von MrMemory zu unterdr&uuml;cken.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.58.2</B>
 * <P>Erweiterung um die ersten Taglets.
 * <P>Umwandlung von System.out.prints in log4j-Aufrufe in einigen Klassen. Da ist aber noch
 *         jede Menge zu tun.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.58.1</B>
 * <P>Erweiterung des <TT>AbstractRemoteDBFCServer</TT> um die Methode
 *         <TT>executeAfterConstructed()</TT>.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.57.1</B>
 * <P>Erweiterung der Klasse <TT>DynamicObject</TT> um die Methode
 *         <TT>getAttributeName(String)</TT>. Dabei: Aufwertung der Dokumentation der Klasse.
 * <P>Ersetzen der Methode <TT>getAttributenames()</TT> in der Klasse <TT>DynamicObject</TT>
 *         gegen die Methode <TT>getAttributeNames()</TT>. Die alte Methode ist auf
 *         <TT>deprecated</TT> gesetzt.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.56.1</B>
 * <P>Erweiterung um die Methoden <TT>StrUtil.GermanComparison(String, String)</TT> und
 *         <TT>StrUtil.ReplaceVowelMutations(String, VowelMutationReplaceStrategy)</TT>.
 * <P>In diesem Zug: Erweiterung um den Enum <TT>VowelMutationReplaceStrategy</TT>.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.55.1</B>
 * <P>Erweiterung um die Methode <TT>StrUtil.ExtractXMLTagValue(String, String)</TT>.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.54.2</B>
 * <P>Erweiterung des <TT>DialogBenutzerAnmeldung</TT> um die Methode
 *         <TT>afterConstructing()</TT>.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.54.1</B>
 * <P>Erweiterung der Klasse COUtil um die Methode <TT>FindComponent(String, Component)</TT>.
 * <P>Erweiterung um die Ber&uuml;cksichtigung von JMenuBars in der Methode
 *         <TT>Update(Component, String , RessourceManager)</TT>.
 * <P>Erweiterung um den GUITester.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.53.2</B>
 * <P>Kleinere &Auml;nderungen am JDialogThrowable.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.53.1</B>
 * <P>Anglisierung der Klasse <TT>Feiertag</TT>, die damit als aufgehoben wird. Dokumentation 
 *         der Konfigurationsm&ouml;glichkeiten der Klasse.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.52.1</B>
 * <P>Erweiterung um den <TT>JDialogThrowable</TT>.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.50.1</B>
 * <P>Erweiterung der beiden DBF-Djinns, um eine M&ouml;glichkeit, einen alternativen LockDjinn
 *         angeben zu k&ouml;nnen. Hier durch kann bei Bedarf eine Anzeige gelockten 
 *         Datens&auml;tzen angezeigt werden.</TT>.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.51.1</B>
 * <P>Anpassungen in der DBExec an HSQL.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.50.1</B>
 * <P>Erweiterte LockDjinn--Anbindung.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.49.2</B>
 * <P>Korrekturen an der Methoden <TT>GetMontagVonWoche(int, int)</TT>.
 * <P>&nbsp;
 *
 * <P><B>1.49.1</B>
 * <P>Erweiterung FileUtil um die Methode <TT>Tail(String, int)</TT>.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.48.1</B>
 * <P>Erweiterung FileUtil um ReadFromTextFile mit Kommentarsequenz..
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.47.3</B>
 * <P>Korrektur des Panelaufbaus bei Timestamps in der Klasse <TT>TimestampField</TT>.
 * <P>Debugging im LockDjinn.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.47.2</B>
 * <P>Debugging am ConnectionManager (wegen Broken-Pipe-Problem).
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.47.1</B>
 * <P><TT>CentrallyHeld</TT>-Objekte werden nun aufgrund eines speziellen Status im 
 *         <TT>DefaultEditorPanel</TT> behandelt.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.46.4</B>
 * <P>Wiederzuschalten der Property zur generellen Unterdr&uuml;ckung der Warnmeldung in der 
 *         Klasse <TT>LongPTimestamp</TT>.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.46.3</B>
 * <P>Erweiterung der Utility-Klasse <TT>StrUtil</TT> um die Methode <TT>Trim(String, 
 *         String)</TT>.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.46.2</B>
 * <P>Erweiterung der Klasse <TT>PDateFormat</TT> nach W&uuml;nschen von V.Medvid.
 * <P>Ersetzen System.out.println in LongPTimestamp duch log4j-Eintrag (WARN). Vorschlag durch 
 *         T.V&ouml;lker.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.46.1</B>
 * <P>Internationalisierung der Datumsanzeigen der Klasse <TT>PDate</TT> mit Hilfe der 
 *         Utility-Klasse <TT>PDateFormat</TT>.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.45.3</B>
 * <P>Kleinere Layout&auml;nderungen am TimestampField.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.45.2</B>
 * <P>Der PTimestamp wirft nun eine Exception, sobald versucht wird eine Manipulation mit dem
 *         TimestampUnit.MILLI durchzuf&uuml;hren.
 * <P>Plus-/Minus-Tasten im DateField. Hiermit kann das Datum um einen Tag nach vorn bzw. hinten
 *         verschoben werden.
 * <P>Aufnahme einer DBFactory-Musterimplementierung als Superklasse f&uuml;r DBFactories, die
 *         spezielle Aufgaben wahrnehmen sollen und bei denen die meisten Funktionen der 
 *         DBFactory nicht unterst&uuml;tzt werden (<TT>DBFactorySpecialFunctionPattern</TT>).
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.45.1</B>
 * <P>Neuer Betriebsmodus f&uuml;r den <TT>ConnectionManager</TT>. Diese kann nun auch gezwungen
 *         werden immer mit der selben Connection zu arbeiten. Hierdurch lassen sich 
 *         Transaktionen realisieren. Allerdings kann dieser Modus nicht mit nebenl&auml;ufigen
 *         Prozessen eingesetzt werden.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.44.3</B>
 * <P>&Uuml;bertragung der Verantwortung f&uuml;r einen Programmabbruch bei Fehlschlag der
 *         Versionspr&uuml;fung an die Implementierung des Interfaces 
 *         <TT>CompatiblityChecker</TT>.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.44.2</B>
 * <P>Erweiterung am <TT>VersionChecker</TT>: Interfaces und Versionschecker reichen Exceptions
 *         jetzt durch.
 * <P>Verfeinerung der <TT>VersionChecker</TT>-Logik um die M&ouml;glichkeit einen 
 *         Programmabbruch bei Fehlschlag der Versionspr&uuml;fung zu initiieren.
 * <P>Erweiterung der Klasse <TT>AbstractRemoteDBFCServer</TT> um die entsorechende Logik, die
 *         Pr&uuml;fung und den eventuellen Abbruch durchzuf&uuml;hren.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.44.1</B>
 * <P>Erweiterung um den <TT>VersionChecker</TT> und die damit verbundene Logik. 
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.43.2</B>
 * <P>Umkehr der Kopierrichtung in der Methode <TT>copyAttributes(DynamicObject)</TT> der Klasse
 *         <TT>DynamicObject</TT>.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.43.1</B>
 * <P>Erweiterung um den ToolBarDjinn und die damit verbundene Logik.
 * <P>Erweiterung der Klasse corent.util.FileUtil um die Methode <TT>CompletePath(String)</TT>.
 * <P>Erweiterung um die Methode <TT>corent.util.FileUtil.ReadStringList(String)</TT>.
 * <P>Erweiterung der Klasse <TT>DynamicObject</TT> um die Methode 
 *         <TT>copyAttributes(DynamicObject)</TT>. 
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.42.1</B>
 * <P>Erweiterung der Klasse <TT>PTimestamp</TT> um die Methode <TT>toPDate()</TT>. Dabei 
 *         Debugging an der <TT>valueOf(java.util.Date)</TT>-Methode der Klasse.
 * <P>Erweiterung der Klasse <TT>PDate</TT> um die Methode <TT>toPTimestamp()</TT>.
 * <P>&nbsp;
 *
 * <BR><HR SIZE=1>
 * <P><B>1.41.1</B>
 * <P>Erweiterung des DBFactory-Interfaces um eine M&ouml;glichkeit auch leere SelectionModels
 *         &uuml;ber die Methode getSelectionView() zu lesen. Das kann aus 
 *         Performancegr&uuml;nde zu weilen n&ouml;tig sein. Damit verbunden wird der beim
 *         Erstellen eines SelectionDjinnPanels eine &uuml;ber eine Property gepr&uuml;ft, ob
 *         eine Vorselektion stattfinden soll, oder nicht.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.40.8</B>
 * <P>SelectionComponents in MassiveListSelector-Auswahldialogen.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.40.7</B>
 * <P>Ausblenden des Speichernbuttons bei EditorDjinnPanels, in denen der EditorDjinnMode 
 *         ungleich EDIT ist.
 * <P>Erweiterung der Klasse DBExec um die Methode <TT>getSystemTime()</TT> zum Lesen der 
 *         aktuellen Systemzeit auf dem DBMS.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.40.6</B>
 * <P>Debugging am Zwischenspeichern f&uuml;r Reconstructables.
 * <P>Erweiterung des Interfaces <TT>EditorDescriptor</TT> um die Methode 
 *         <TT>setObject(Attributed)</TT>. 
 * <P>Erweiterung des Interfaces <TT>SubEditor</TT> um die Methode 
 *         <TT>setObject(Attributed)</TT>. Dieser und der vorherige Punkt waren im Rahmen der 
 *         Aktivierung des Speichern-Buttons notwendig.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.40.5</B>
 * <P>Konfigurierbares Wegschalten der Warnung im LongPTimestamp ("WARNING: PTimestamp cannot
 *         ...").
 * <P>Debugging in der Methode <TT>SysUtil.GetHostname()</TT>. Jetzt &uuml;berschreibt der Wert
 *         in der Property <I>hostname</I> die Betriebssystemeinstellung, falls diese kein 
 *         Ergebnis liefert. Nur, wenn in der Property auch kein Wert zu finden ist, wird die 
 *         Fehlermeldung ausgegeben. 
 *
 * <BR><HR SIZE=1>
 * <P><B>1.40.4</B>
 * <P>Methoden <TT>addPDateDialogListener(DateListener)</TT> und
 *         <TT>removePDateDialogListener(DateListener)</TT> auf <TT>deprecated</TT> gesetzt.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.40.3</B>
 * <P>Erweiterung des JPasswordFields zum COPasswordField.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.40.2</B>
 * <P>Untersuchung der und Behebung des Fehlers im Konstruktor PDate(java.util.Date). 
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.40.1</B>
 * <P>Erweiterung der PTimstamp-Klasse um die java.util.Date-Verarbeitung.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.39.1</B>
 * <P>Erweiterung um die corent.dates.DateTimeFactory.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.38.3</B>
 * <P>Erweiterung der Konfigurationsm&ouml;glichkeiten der Kopfzeilen der Wartungsdialoge.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.38.2</B>
 * <P>Debugging (Update nach &Ouml;ffnen des Auswahldialoges) an den frei definierbaren
 *         ViewComponentFactories im InternalFrameSelectionDjinnDBF. 
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.38.1</B>
 * <P>Frei definierbare ViewComponentFactories im InternalFrameSelectionDjinnDBF.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.37.6</B>
 * <P>L&ouml;sung des Hochkommata-Problems im Datenbankenbereich.
 * <P>Erweiterung um eine M&ouml;glichkeit Distincts in die Selektionen und Leseoperationen der
 *          DBFactoryController aufzunehmen (DistinctReader-Interface).
 * <P>Konfigurierbare Zeichensatzkodierung beim CSV-Druck.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.37.5</B>
 * <P>Entsch&auml;rfung des Threads zur Umrahmung der fokussierten Komponente im 
 *          DefaultEditorDjinnPanel.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.37.4</B>
 * <P>Ausdehnung der UserNotification-Logik auf das L&ouml;schen von Datens&auml;tzen.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.37.3</B>
 * <P>Erweiterung des Interfaces <TT>UserChangesNoticeable</TT> um die Methode 
 *         <TT>isSuppressUserNotification()</TT>. Dementsprechend Anpassungen im Bereich der 
 *         DBFactoryController-Implementierungen.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.37.2</B>
 * <P>Erweiterung des RemoteDBFactorControllers um Debug-Informationen im Rahmen der 
 *         Verbindungsdaten.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.37.1</B>
 * <P>Erweiterung um das A3-Attribut in der PrintJobConf.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.36.1</B>
 * <P>Erweiterung um die Klasse <TT>LabelInstallationName</TT>.
 * <P>Erweiterung um eine Abstract- und eine Default-Implementierung des 
 *         JasperReportable-Interfaces.
 * <P>Erweiterung der Klasse <TT>LongPTimestamp</TT> um die Methode <TT>getMillis()</TT>.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.35.1</B>
 * <P>Erweiterung um den PathFinderMonitor.
 * <P>Erweiterung des OrderByDescriptors um eine toSQL-Methode. Diese erzeugt eine 
 *         Order-By-Clause aus dem Descriptor im String-Format.
 * <P>Korrektur der Methode <TT>getServerTime()</TT>. Sie liefert nun bei Remote-Betrieb 
 *         tats&auml;chlich die aktuelle Uhrzeit (mit Datum) vom Server.  
 *
 * <BR><HR SIZE=1>
 * <P><B>1.34.1</B>
 * <P>Erweiterung des StruturedTextFiles um eine Option zum Fortlassen der Formatierung der
 *         Datei (durch Fortlassen von f&uuml;hrenden Leerzeichen und Zeilenumbr&uuml;chen.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.33.9</B>
 * <P>Erweiterung der Klasse <TT>PTimestamp</TT> um die Methode <TT>getMillis()</TT>.
 * <P>Zusatzoutput im AbstractRemoteDBFCServer. Zudem kann hier die Unbind-Aktion beim 
 *         Serverstart durch Konfiguration &uuml;ber eine Property deaktiviert werden.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.33.8</B>
 * <P>Erweiterung MassiveListSelector um M&ouml;glichkeit einen Leer-String zu konfigurieren.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.33.7</B>
 * <P>Erweiterung um den PathFinder.
 * <P>Konfigurierbarkeit Funktionstasten in den DjinnPanels.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.33.6</B>
 * <P>Erweiterung um den FilenameSelector.
 * <P>Schl&uuml;sselr&uuml;ckgabe bei der write-Methode der DBFactory.
 * <P>Bereinigungen im FileUtil-Klassen-Bereich.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.33.5</B>
 * <P>Anpassungen im Ressourcenbereich.
 * <P>Ressourcensteuerung f&uuml;r den Titel des TextAreaDialoges (LineTextEditor).
 *
 * <BR><HR SIZE=1>
 * <P><B>1.33.4</B>
 * <P>Erweiterung des DialogEditorDjinn um einen Konstruktor ohne Fensterreferenz.
 * <P>Erweiterung der Klasse DefaultSelectionTableModel und des Interfaces SelectionTableModel
 *         um die Methode <TT>removeKey(int)</TT>.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.33.3</B>
 * <P>Debugging der Fokussierung im DialogEditorDjinn.
 * <P>Erweiterung des InternalFrameEditorDjinn um die Methode <TT>doOpened()</TT>.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.33.2</B>
 * <P>Fokussierung der ersten Komponente im <TT>DefaultEditorPanel</TT> (auch wenn mehrere 
 *         TabbedPanels gebildet werden).
 *
 * <BR><HR SIZE=1>
 * <P><B>1.33.1</B>
 * <P>Erweiterung um das Interface <TT>EditorDjinnObserver</TT>, das mittelfristig das Interface
 *         <TT>EditorDjinnMaster</TT> ersetzen soll. In der ersten Ausbaustufe reagiert der 
 *         Observer auf das Ereignis "Djinn erzeugt" (SUMMONED) und "Djinn geschlossen" 
 *         (DISPELLED).
 *
 * <BR><HR SIZE=1>
 * <P><B>1.32.5</B>
 * <P>Debugging am doppelten Hinweis zu nicht ausgef&uuml;llten Pflichtfeldern. 
 *
 * <BR><HR SIZE=1>
 * <P><B>1.32.4</B>
 * <P>Fehlermeldung und Kommaausblendung bei Zahlenfeldern im DefaultEditorPanel.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.32.3</B>
 * <P>Ausdehnung der Obligation-Logik auf COLabels und deren <TT>update</TT>-Methode.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.32.2</B>
 * <P>Erweiterung der Klasse <TT>AbstractDBFactory</TT> um die Methode 
 *         <TT>prepareDuplication(T extends Persistent, T extends Persistent)</TT>. Hier durch 
 *         l&auml;&szlig;t sich die Vorlage f&uuml;r das Duplikat vor dem &Uuml;bertragen von 
 *         Daten manipulieren.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.32.1</B>
 * <P>Korrektur der Propertybezeichnung <I>corent.db.xs.DBFactoryUtil.pk.maximum</I> in der 
 *         Klasse <TT>DBFactoryUtil</TT>.
 * <P>Einbau Startzeitlogging in den <TT>AbstractRemoteDBFCServer</TT>.
 * <P>Einbau &Auml;nderungslogging zur Er&ouml;ffnung der M&ouml;glichkeit einer gesteuerten,
 *         zentralisierten, clientseitigen Cache-Aktualisierung (Infrastruktur).
 *
 * <BR><HR SIZE=1>
 * <P><B>1.31.3</B>
 * <P>Umwandlung COMenuItem in Ressourced.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.31.2</B>
 * <P>Einbau der Behandlung von <TT>SplittedEditorDjinnPanelListenern</TT> im 
 *         DefaultEditorDjinnPanel.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.31.1</B>
 * <P>Erweiterung der Klasse <TT>AbstractRemoteDBFCServer</TT> um die F&auml;higkeit die Zeit zu
 *         messen, die der Server beim Start ben&ouml;tigt.
 * <P>Erweiterung des Interfaces <TT>TabbedEditable</TT> um die Methode 
 *         <TT>isTabEnabled(int)</TT>. Hierdurch wird die M&ouml;glichkeit er&ouml;ffnet, Tabs 
 *         in den Standardwartungsdialogen zu deaktivieren. 
 * <P>Erweiterung des Klasse <TT>GUIUtil</TT> um die Methode <TT>SetEnabled(Component, boolean)
 *         </TT>. Mit Hilfe dieser Methode k&ouml;nnen Panels (Container) mit alle ihren 
 *         enthaltenen Komponenten auf- bzw. abgeblendet werden.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.30.3</B>
 * <P>Erweiterung um konfigurierbare Alternativklassen bei der Generierung des 
 *         SelectionTableModels in der Klasse <TT>DBFactoryUtil</TT>.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.30.2</B>
 * <P>Korrektur des $SQL:-Verhaltens bei vordefinierten Where-Klauseln in der Klasse 
 *         <TT>DBFTableViewComponent</TT>. 
 *
 * <BR><HR SIZE=1>
 * <P><B>1.30.1</B>
 * <P>Einf&uuml;hrung des Interfaces <TT>Cached</TT>. Hiermit k&ouml;nnen gecachte Objekte auch
 *         serverseitig vern&uuml;nftig genutzt werden.
 * <P>Erweiterung des DBFactoryControllers um die M&ouml;glichkeit Scriptspracheninterpreter
 *         einzubinden und auf ihnen serverseitige Programme auszuf&uuml;hren.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.29.4</B>
 * <P>DBUtil.GetObjects kann nun auch LONGVARBINARY-Felder als String lesen.
 * <P>DBUtil.GetObjects kann nun auch so konfiguriert werden, da&szlig; Null-Referenzen in 
 *         Zahlenfeldern als 0 gelesen werden.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.29.3</B>
 * <P>Korrektur der Fehlfunktion des DateDialogs (Jahresfeldproblem). Dabei Einbau einer 
 *         Testfunktion &uuml;ber die <TT>main(String[])</TT>-Methode.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.29.2</B>
 * <P>Synchronisation des Datenbankzugriffs im <TT>DefaultDBFactoryController</TT>.
 * <P>Anpassungen der Dokumentation des Interfaces <TT>JasperReportable</TT>.
 * <P>Kleinre &Auml;nderungen im djinn- und gui-Package im Rahmen der Einf&uuml;hrung an die 
 *         property-basierten Ressourcen. 
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.29.1</B>
 * <P>Erweiterung des Interfaces <TT>JasperReportable</TT> um die Methode 
 *         <TT>getStandardReportNumbers()</TT>.
 * <P>Umstellung des Clear-Buttons im TimeField im Rahmen der Anpassung an den 
 *         MassiveListSelector. 
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.28.3</B>
 * <P>Behebung des Bug, der im unter 1.28.2. genannten Fall das neuerzeugte Objekt nicht 
 *         l&ouml;scht, wenn der Neuanlage Dialog &uuml;ber den Schliessenbutton geschlossen 
 *         wird.   
 *
 * <BR><HR SIZE=1>
 * <P><B>1.28.2</B>
 * <P>Behebung der Fehlfunktion im DialogSelectionEditorDjinnDBF, die das L&ouml;schen von 
 *         neuangelegten Objekten nach Abbruch der Neuanlage nicht unterst&uuml;tzte. Jetzt 
 *         werden diese Datens&auml;tze gel&ouml;scht.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.28.1</B>
 * <P>Erweiterung der Klasse corent.base.Utl um die Methode <TT>GetProperty</TT>.  
 *
 * <BR><HR SIZE=1>
 * <P><B>1.27.1</B>
 * <P>Erweiterung um die CommentOwner-, CommentAppender-Klassen.  
 *
 * <BR><HR SIZE=1>
 * <P><B>1.26.3</B>
 * <P>Erweiterung um eine Methode zur Abfrage der Dimension eines CorePrintJobs.  
 *
 * <BR><HR SIZE=1>
 * <P><B>1.26.2</B>
 * <P>Erweiterung um die JButton- und die JLabelFactory.  
 *
 * <BR><HR SIZE=1>
 * <P><B>1.26.1</B>
 * <P>Umbau der EditorDjinn-Logik auf EditorDjinnModes.  
 *
 * <BR><HR SIZE=1>
 * <P><B>1.25.1</B>
 * <P>Erweiterung der MassiveListSelectoren um die M&ouml;glichkeit die ausgew&auml;hlte Klasse
 *         nachtr&auml;glich zu &auml;ndern. 
 * <P>Schaffung einer M&ouml;glichkeit aus MassiveListSelectoren eine Neuanlage zu initiieren. 
 *
 * <BR><HR SIZE=1>
 * <P><B>1.24.4</B>
 * <P>Kleinere Debuggings im Bereich Encoding. 
 *
 * <BR><HR SIZE=1>
 * <P><B>1.24.3</B>
 * <P>Der MenuDjinn kann nun auch abgeblendete Men&uuml;punkte darstellen.
 * <P>Orientierung der Zeilenh&ouml;hen im DefaultEditorDjinnPanel an der H&ouml;he eines 
 *         Buttons (auch wenn keiner im Dialog ist).
 *
 * <BR><HR SIZE=1>
 * <P><B>1.24.2</B>
 * <P>Kleinere Debuggings an den Debuggings im Reconstructable-Umfeld :o).
 *
 * <BR><HR SIZE=1>
 * <P><B>1.24.1</B>
 * <P>Kleinere Debuggings im Reconstructable-Umfeld.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.23.3</B>
 * <P>Debugging am DBFactoryUtil zum Thema read mit deaktivierten Deactivatables.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.23.2</B>
 * <P>Debugging an der Zu- bzw, Wegschaltung von Joins.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.23.1</B>
 * <P>Erweiterung um die Klasse <TT>corent.base.Util</TT>.
 * <P>Erweiterung der Klasse <TT>corent.db.xs.DBFactoryUtil</TT> um die Methode GetSelectionView
 *         ohne die automatische Einbeziehung von Joins. 
 *
 * <BR><HR SIZE=1>
 * <P><B>1.22.1</B>
 * <P>Hinzuf&uuml;gen der Klasse U und Anpassungen im Rahmen der Codierungsproblematik.
 * <P>Umzug der SpeedyInput-Klassen nach archimedes.djinn.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.21.6</B>
 * <P>Anpassung des TableSorters an PDate, PTime und PTimestamp.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.21.5</B>
 * <P>Umzug des Reconstructable-Interfaces von archimedes.app nach corent.db.xs.
 * <P>Einbau der Benutzeridentifikation in Schreibvorg&auml;nge bei Reconstructable Objekten.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.21.4</B>
 * <P>Internationalisierung der und Debugging an der Unifikatspr&uuml;fung.
 * <P>Erweiterung um das Interface CentrallyHeld zur Realisierung einer zentralen Datenhaltung.
 * <P>&Ouml;ffnung des InternalFrameSelectionDjinnDBF f&uuml;r Eingriff in die Lock-Strategie.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.21.3</B>
 * <P>Korrekturen am DefaultDBFactoryController. 
 * <P>Einbau Ressourcenupdate nach Aufbau von InternalFrameSelectionDjinnDBF und 
 *         InternalFrameEditorDjinn.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.21.2</B>
 * <P>Debugging und Erweiterungen zum Thema long-Schl&uuml;ssel, Mehrproze&szlig;f&auml;higkeit
 *         und Panelreihenfolge im DefaultEditorDjinnPanel.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.21.1</B>
 * <P>Erweiterung der Datenbankzugriff auf long-Schl&uuml;ssel.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.20.1</B>
 * <P>Erweiterung um eine M&ouml;glichkeit Zeichenketten vor dem Schreibvorgang automatisch 
 *         auszutauschen (in der Klasse DBFactoryUtil).
 *
 * <BR><HR SIZE=1>
 * <P><B>1.19.3</B>
 * <P>Einbau der Druckfunktion f&uuml;r Neuanlagen und Duplikate (so diese vor dem Drucken 
 *         gespeichert werden m&uuml;ssen).
 * <P>Erweiterung der Klasse DBUtil um ein forciertes Einlesen als Double bei Numerics ohne 
 *         Nachkommastellen (sonst wird hier ein Long-Objekt generiert).
 *
 * <BR><HR SIZE=1>
 * <P><B>1.19.2</B>
 * <P>Die Felder des Email-Vorschau-Dialoges k&ouml;nnen nun (konfigurierbar) editiert werden.
 * <P>Neuer Konstruktor f&uuml;r jsql und Schaffung einer M&ouml;glichkeit den Dialog in 
 *         Applikationen zu integrieren.
 * <P>Geschachtelte Men&uuml;s innerhalb des DefaultMenuDjinns. Au&szlig;erdem versteht der 
 *         DefaultMenuDjinn nun ein Separator-Tag.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.19.1</B>
 * <P>Erweiterung der Klasse corent.util.SysUtil um die Methode Execute zu Ausf&uuml;hrung 
 *         externer Prozesse.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.18.5</B>
 * <P>Kleinere Debuggings.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.18.4</B>
 * <P>Entfernen eines Bugs der Erweiterung von Version 1.18.3.
 * <P>Einbau der M&ouml;glichkeit den Speichern-Button per Property auszublenden.
 * <P>Einbau des Abblendens von Speichern- und Drucken-Buttons (letzterer nur in Verbindung mit
 *         Objekten die vor dem Drucken gespeichert werden m&uuml;ssen) bei gelockten Objekten.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.18.3</B>
 * <P>Beseitigung des bei PostgreSQL im Suchdialog auftretenden Bugs bei 
 *         <TT>null</TT>-Referenzen.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.18.2</B>
 * <P>Einbau eines Speichern-Buttons in die Wartungdialoge.
 * <P>Einbau von automatischer Speicherung vor dem Drucken bei JasperReportables.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.18.1</B>
 * <P>Erweiterung um die Interfaces und Defaultimplementierungen rund um den UserActionLogger.
 *         In dieser Version ist das aber noch ohne Wirkung. Diese wird in der n&auml;chsten 
 *         Version nachgeliefert.
 * <P>Einbau der Moonphase zur Berechnung von Feiertagen (noch nicht umgesetzt).
 * <P>Erweiterung um die Implementierung des Interfaces <TT>TableListSubEditor</TT> als 
 *         Erweiterung zum Interface <TT>SubEditor</TT>. Hiermit wird der Zugriff auf die Listen
 *         eines entsprechenden SubEditors erm&ouml;glicht.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.17.4</B>
 * <P>Die Klasse <TT>JInternalFrameWithInifile</TT> ist um die Implementierung des Interfaces
 *         <TT>Ressourced</TT> erweitert worden. In diesem Rahmen: Erweiterung der Klasse
 *         <TT>RessourceManager</TT> und deren Implementierung <TT>PropertyRessourceManager</TT>
 *         um die Methoden <TT>getTitle(String)</TT>.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.17.3</B>
 * <P>Erweiterung der Klasse <TT>StructuredTextFile</TT> um einen Konstruktor, der als Parameter
 *         bereits einen InputStream akzeptiert.
 * <P>Erweiterung um den LockDjinn.
 * <P>Umbau der Klasse <TT>InternalFrameSelectionEditorDjinnDBF</TT> und
 *         <TT>DialogSelectionEditorDjinnDBF</TT> auf LockDjinns. 
 *
 * <BR>><HR SIZE=1>
 * <P><B>1.17.2</B>
 * <P>Behebung des "Fensterbugs", der Anwendungen zum Absturz bringen kann.
 *
 * <BR><P><B>1.17.1</B>
 * <P>Erweiterung der Klasse <TT>SpeedyInputUtil</TT> um eine Methode zur Bearbeitung von
 *         LineTextEditor-Feldern.
 * <P>Erweiterung des <TT>SpeedyInputInternalFrames</TT> um die F&auml;higkeit der 
 *         Zusammenarbeit mit Inifiles.
 * <P>Erweiterung des LineTextEditors um die Methode <TT>initText(String)</TT>.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.16.3</B>
 * <P>Auff&auuml;lligkeiten im Rahmen der Schnelleingabe entfernt.
 *
 * <BR><P><B>1.16.2</B>
 * <P>Behebung des Bugs, der bei der Neuanlage von Reconstructables ein Lock auf ein Objekt mit
 *         Id 0 setzt.
 * <P>Einbau der Methode <TT>GetKeyCode(String)</TT> in die Klasse GUIUtil.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.16.1</B>
 * <P>Erweiterungen der Dokumentation.
 * <P>Erweiterung um das package <TT>corent.djinn.clipboard</TT>. Hierdurch wird das gesamte 
 *         Framework um eine M&ouml;glichkeit erweitert, Daten unkompliziert aus 
 *         Datens&auml;tzen in das Clipboard zu &uuml;bernehmen.
 * <P>In diesem Zusammenhang ist im package <TT>corent.gui.dialog</TT> ein InternalFrameInfo 
 *         hinzugekommen, in dem Systemmeldungen ausgegeben werden k&ouml;nnen, und der sich 
 *         nach einer konfigurierbaren Zeit selbstst&auml;ndig wieder schlie&szlig;t.
 * <P>Erweiterung um das package <TT>corent.djinn.speedy</TT>. Mit Hilfe der Klassen des 
 *         Packages wird es f&uuml;r EditorDjinns m&ouml;glich, einen Schnelleingabedialog 
 *         anzudocken.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.15.5</B>
 * <P>Erweiterungen der Beispiel-Programmcodes und Dokumentationen im Bereich 
 *         <TT>PTimestamp</TT> und <TT>DBExec</TT>.
 * <P>Reanimation des jDiff-Programms aus dem Package <TT>corent.util</TT>.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.15.4</B>
 * <P>Die Klasse DBExecMode ist um die Methode <TT>getDBExecMode(String, DBExecMode)</TT> 
 *         erweitert worden. Hier&uuml;ber kann ein DBExecMode auf einer Property gelesen 
 *         werden. In der Property mu&szlig; das Token des DBExecModes gespeichert sein.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.15.3</B>
 * <P>Die Methode <TT>valueOf(String)</TT> ist nun statisch.
 * <P>Einbau der Pr&uuml;fung auf die Property 
 *         <TT>corent.db.xs.AbstractDBFactory.suppress.dbfc.warning</TT> (Boolean) in der Klasse
 *         <TT>AbstractDBFactory</TT>.
 * <P>Erweiterung des Enums <TT>TodoListItemState</TT> um den Bezeichner <TT>BLUE</TT>.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.15.2</B>
 * <P>Wiedereinf&uuml;hrung des parameterlosen Konstruktors der <TT>AbstractDBFactory</TT>.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.15.1</B>
 * <P>Erweiterung des Interfaces <TT>DBFactoryController</TT> um die Merhoden 
 *         <TT>getControllerProperty(String)</TT> und <TT>setControllerProperty(String, 
 *         Object)</TT>. Hier&uuml;ber k&ouml;nnen beispielweise asynchrone Methodenaufrufe in 
 *         den jeweiligen DBFactory-Implementierungen realisiert werden.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.14.1</B>
 * <P>Erg&auml;nzung der Dokumentation und Einbau einiger Testausgaben.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.12.1</B>
 * <P>Die <TT>toString()</TT>-Methode der Klasse PTimestamp kann nun auch so konfiguriert 
 *         werden, da&szlig; sie die Sekunden nicht mit in den String &uuml;bertr&auml;gt. 
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.11.9</B>
 * <P>Erweiterung des AbstractRemoteDBFCServer um die Akzeptanz f&uuml;r den 
 *         DBExecMode.POSTGRESQL.
 * <P>Erweiterung um die Klasse <TT>corent.util.SysUtil</TT> mit den Methoden <TT>GetHostname()
 *         </TT> und <TT>ShowProperties(boolean)</TT>.
 * <P>PostgreSQL-Modusanpassung bei der Methode <TT>Truncate</TT> aus der Klasse 
 *         <TT>DBExec</TT>. 
 * <P>Erweiterung um das Interface <TT>Archivable</TT> und damit verbundene Erweiterungen in der
 *         Funktionalit&auml;t des <TT>DefaultDBFactoryController</TT> im Rahmen der 
 *         <TT>write</TT>-Methode. 
 *
 * <BR><HR SIZE=1>
 * <P><B>1.11.8</B>
 * <P>Erweiterung der Klasse VectorTableModel um die Methode getVector().
 *
 * <BR><HR SIZE=1>
 * <P><B>1.11.7</B>
 * <P>Der InternalFrameTodoList kann nun auch ge&ouml;ffnet werden, wenn die Todoliste leer ist
 *         (dies geschied &uuml;ber eine spezielle Property).
 *
 * <BR><HR SIZE=1>
 * <P><B>1.11.6</B>
 * <P>Anpassung der DBExec in Hinsicht auf die Concatenation von Strings und der Limitierung der
 *         Treffermenge von Selektionen unter PostgreSQL.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.11.5</B>
 * <P>Die TodoListItems rendern sich nun mit dem Font der Todolistenansicht.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.11.4</B>
 * <P>Erweiterung um die M&ouml;glichkeit die isnull im MSSQL- und HSQL-Betrieb beim Aufruf der
 *         DBExec.Convert-Methode zu erzwingen.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.11.3</B>
 * <P>Der GenerateExpander ist um dir Methode doChangeKeys(int) erweitert worden. Die 
 *         Generate-Methode der DBFactoryUtil-Klasse ist entsprechend erweitert worden.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.11.2</B>
 * <P>Es k&ouml;nnen jetzt automatisch generierte SubEditors mit den einfachen Erweiterungen 
 *         durch auf andere Panels ausgelagerte Komponenten gemischt werden.
 * <P>Erweiterung des InternalFrameTodoList um einen Aktualisierungsbutton.  
 *
 * <BR><HR SIZE=1>
 * <P><B>1.11.1</B>
 * <P>Erweiterung der DBExec-Klasse um die Methode <TT>Truncate</TT>.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.10.1</B>
 * <P>Erweiterung um das Interface EditorDjinnPrintMaster und die damit verbundenen Funktionen.
 * <P>Einbindung des LintTextEditors in die ContextOwner-Logik.
 * <P>Erweiterung um den HistoryWriter und die damit verbundenen &Auml;nderungen am 
 *         EditorDjinnPanel und den dieses nutzenden Klassen. 
 *
 * <BR><HR SIZE=1>
 * <P><B>1.9.3</B>
 * <P>Korrektur der compareTo-Methode der AbstractTodoListItem, soda&szlig; sie auch in der Lage
 *         ist, mit long-Werten zusammenzuarbeiten, die bei der getSortOrder-Methode entstehen 
 *         k&ouml;nnen.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.9.2</B>
 * <P>Umwandlung des SortedVector in eine generische Klasse.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.9.1</B>
 * <P>Einbau der LongPTimestamp-Klasse.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.8.2</B>
 * <P>Debugging an der Beladung von MassiveListSelectoren durch Nicht-PK-Referenzierter Felder.
 * <P>Einbau eines Erweiterten ListOwner-Interfaces, das zus&auml;tzliche Methoden zur Kontrolle
 *         der Buttons der Listenansichten von ListOwnern bietet. 
 *
 * <BR><HR SIZE=1>
 * <P><B>1.8.1</B>
 * <P>Erweiterung um das Interface PostItActionExtender.
 * <P>Erweiterung der Klasse COLabel von ContextOwner auf Ressourced.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.7.1</B>
 * <P>Erweiterung um die Verarbeitung des Interfaces GenerateExpander.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.6.1</B>
 * <P>Erweiterung der create-Methode der SubEditorFactory um den Parameter, der die Tabelle mit
 *         den Komponenten des EditorDjinnPanels enth&auml;lt. Dazu Anpassungen an der 
 *         VectorSubEditorFactory.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.5.2</B>
 * <P>Behebung des Bugs, wegen dem der PostIt sich nicht aktualisiert (roter Rahmen), wenn der
 *         Zeitpunkt zur&uuml;ckgesetzt wird.
 * 
 * <BR><HR SIZE=1>
 * <P><B>1.5.1</B>
 * <P>Erweiterung um die Tools-Klasse. Der Start erfolgt mit den Methoden zum Lesen und 
 *         Schreiben von Textfiles.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.4.18</B>
 * <BR><P>Korrektur der Pinned-Routine der PostIts.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.4.16</B>
 * <P>Ber&uuml;cksichtigung Ablaufdatum PostIts.
 * <P>Hinzuf&uuml;gen eines R&uuml;cksetz-Buttons f&uuml;r das Datum im PostIt.
 * <P>Behebung des merkw&uuml;rdigen Verhaltens der PostIt's, sich nach dem Neuladen als 
 *         ge&auml;ndert zu markieren. 
 *
 * <BR><HR SIZE=1>
 * <P><B>1.4.15</B>
 * <P>Debugging PostIt's.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.4.14</B>
 * <P>PostIt's Grundlagen.
 * <P>Erweiterung der DBTypes um eine valueOf-Methode.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.4.12</B>
 * <P>Erweiterung der AbstractExtendedColorPalette um Methoden zum L&ouml;schen und Setzen von
 *         Farben im laufenden Betrieb.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.4.11</B>
 * <P>Erweiterung um das jDiff-Programm.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.4.10</B>
 * <P>Erweiterung der DBFactory-Logik um eine read-Methode, die es auch erm&ouml;glicht, als 
 *         gel&ouml;scht gekenntzeichnete Deactivatables zu selektieren. Im Zuge dessen ist das 
 *         Interface Deactivatable um eine Methode erweitert worden, die eine gezielte Abfrage 
 *         des Gel&ouml;schtstatus erlaubt.
 * <P>MassiveListSelectoren zeigen nun auch gel&ouml;schte Deactivatables an und kennzeichnen 
 *         diese mit einem (definierbaren) Pr&auml;fix.
 * <P>Die Inhalte von MassiveListSelectoren werden nun besser sichtbar (in schwarz) dargestellt.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.4.9</B>
 * <P>Einbau einer Zeitnahmem&ouml;glichkeit im DefaultDBFactoryController. Dies wird mit Hilfe
 *         der Property <TT>corent.db.xs.DefaultDBFactoryController.showAccesstimes</TT> 
 *         voreingestellt und kann &uuml;ber die Methoden <TT>isShowAccesstimes()</TT> und 
 *         <TT>setShowAccesstimes(boolean)</TT> zur Laufzeit ge&auml;ndert werden. Ebenso im
 *         RemoteDBFactoryController. Hier hei&szlig;t die Property allerdings
 *         <TT>corent.db.xs.RemoteDBFactoryController.showAccesstimes</TT>, die Methoden
 *         <TT>isShowAccesstimesLocal()</TT> und <TT>setShowAccesstimesLocal(boolean)</TT>.
 * <P>Behebung des Bugs in der Selektion, der im Zusammenhang mit der Tabellensortierung 
 *         &uuml;ber Buttonclick in der DBFTableViewComponent entstanden ist.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.4.8</B>
 * <P>Behebung des Bugs, der eine NullPointerException verursacht hat, wenn der Benutzer aus  
 *         einem listenbasierten DialogSelectionDjinn ein Element ausgew&auml;hlt hat.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.4.7</B>
 * <P>Die Buttons des SelectionEditorDjinnPanels sind nun mit Kontexten versehen.
 * <P>Der SortedVector Konstruktor, der eine Collection als Parameter akzeptiert, sortiert diese
 *         nun.
 * <P>Behebung des Neuanlage-Flaggen-Bugs im InternalFrameEditorDjinn.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.4.6</B>
 * <P>Einbau einer Stapel&auml;nderungsfunktion auf Datenbankebene und im Auswahldialog der
 *         Applikationslogik.
 * <P>Die R&uuml;ckgabe der Methode <TT>AbstractExtendedColorPalette.getColors()</TT> ist 
 *         alphabetisch sortiert. 
 *
 * <BR><HR SIZE=1>
 * <P><B>1.4.5</B>
 * <P>Einf&uuml;hrung des Interfaces <TT>AlternateSelectionTableModelOwner</TT> und der damit 
 *         verbundenen Funktionalit&auml;t in der SelectionDjinn-Logik.
 * <P>Erweiterung des DBFactoryControllers um eine M&ouml;glichkeit, Statements-Ausgaben trotz
 *         eingeschalteten Loggings zu unterdr&uuml;cken (z. B. f&uuml;r Passwortabfragen).
 * <P>Erweiterung der DBExec-Klasse um eine Methode mit Hilfe deren eine einheitliche Ausgabe
 *         von Meldungen m&ouml;glich wird.
 * <P>Einbau einer Funktionalit&auml;t, &uuml;ber die die Tabellenansichten bei Selektionen
 *         sortiert werden k&ouml;nnen (per Click auf die Spaltenk&ouml;pfe).
 *  
 * <BR><HR SIZE=1>
 * <P><B>1.4.4</B>
 * <P>Einf&uuml;hrung des Interfaces <TT>JoinExtender</TT> und der damit verbundenen 
 *         Funktionalit&auml;t im DefaultDBFactoryController.
 *
 * <BR><HR SIZE=1>
 * <P><B>1.4.3</B>
 * <P><I>DBFTableViewComponent:</I> Tabellen mit Key-Attributen vom Typ <TT>varchar</TT> 
 *         k&ouml;nnen nun im Rahmen der DBFTableViewComponent-Logik genutzt werden (bisher 
 *         verursachte die Auswahl eines solchen Objektes einen Fehler, der dazu f&uuml;hrte, 
 *         da&szlig; der Editordialog sich nicht &ouml;ffnete).
 * <BR>
 * <HR>
 * <H2>Geplante Features</H2>
 * <HR SIZE=1>
 * <P>Ausdehnung der &Auml;nderungslistener auf echte Comboboxen (auf diese Weise kann auf
 *         Ereignisse von ComboBox-Komponenten innerhalb von EditorDjinnPanels reagiert werden).
 * <P>Umwandlung der restlichen CO-Komponenten in Ressourced-Komponenten. In diesem Rahmen wird
 *         auch eine COTextArea erstellt.
 * <P>Stapell&ouml;schfunktion.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 15.06.2009 - Anpassungen an log4j.
 *
 */

public class Corent {

    /* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
    private static Logger log = Logger.getLogger(Corent.class);

    /**
     * Liefert Versionsnummer der Corent. Das mu&szlig;te so umgebaut werden, weil die Konstante
     * beim Compilieren offensichtlich direkt in den Bytecode eingef&uuml;gt worden ist.
     *
     * @return Liefert die Versionsnummer der Corent-Bibliothek.
     *
     * @changed OLI 16.02.2008 - Hinzugef&uuml;gt;
     *
     */
    public static String GetVersion() {
        return Version.INSTANCE.getVersion();
    }

    /** Gibt die Corent-Versionsnummer auf der Konsole aus. */
    public static void ShowVersion() {
        if (Boolean.getBoolean("corent.Corent.showversion")) {
            System.out.println("Corent version " + Corent.GetVersion());
        }
        log.info("Corent version " + Corent.GetVersion());
    }

}
