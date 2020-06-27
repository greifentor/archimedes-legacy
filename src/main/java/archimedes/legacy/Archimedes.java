/*
 * Archimedes.java
 *
 * 20.03.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import archimedes.gui.FontConfigurator;
import archimedes.legacy.app.ApplicationObject;
import archimedes.legacy.app.ApplicationUtil;
import archimedes.legacy.app.ArchimedesApplication;
import archimedes.legacy.app.ArchimedesDescriptorFactory;
import archimedes.legacy.app.DefaultArchimedesDescriptorFactory;
import archimedes.legacy.gui.FrameArchimedes;
import archimedes.legacy.model.ObjectFactory;
import archimedes.legacy.scheme.DefaultObjectFactory;
import archimedes.legacy.scheme.Diagramm;
import baccara.files.PropertyFileManager;
import baccara.gui.FileImageProvider;
import baccara.gui.GUIBundle;
import baccara.gui.ImageMapBuilder;
import baccara.gui.ImageProvider;
import baccara.gui.PropertyResourceManager;
import baccara.gui.ResourceManager;
import corent.Corent;
import corent.base.StrUtil;
import corent.db.DBExecMode;
import corent.db.xs.DBFactory;
import corent.db.xs.DBFactoryController;
import corent.files.Inifile;
import corent.files.StructuredTextFile;
import corent.gui.AbstractExtendedColorPalette;
import corent.gui.ExtendedColor;
import corent.security.SecurityController;
import corentx.util.Str;
import logging.Logger;

/**
 * Starterklasse f&uuml;r die Archimedes-Applikation. <BR>
 * <HR SIZE=3>
 * <H2>Geplante oder bearbeitete Features</H2>
 *
 * <OL>
 * <LI>Kombinierte Indices: es wird eine M&ouml;glichkeit geschaffen, Indices zu definieren, die &uuml;ber mehreren
 * Spalten gebildet werden (zuvor pr&uuml;fen, ob das auch in mySQL funktioniert).</LI>
 * <LI>Der Codewriter wird in der Form angepa&szlig;t, Udschebtis zu produzieren, bei denen die Listen-Bezeichner
 * f&uuml;r die N-Seiten stimmen.</LI>
 * <LI>Einbau einer M&ouml;glichkeit die Versionshistorie (eventuell sogar automatisiert) vorzuhalten.</LI>
 * <LI>Einbau einer Kontrolle zur Verhinderung der Anlage von Tabellen mit einem Namen, der bereits einer anderen
 * Tabelle geh&ouml;rt.</LI>
 * <LI>Einbau einer Versionskontrolle. Archimedes soll Diagramme, deren Versionsnummer g&ouml;&szlig;er ist, als die des
 * genutzten Archimedesprogramms, nicht mehr laden k&ouml;nnen.</LI>
 * <LI>Relationen m&uuml;ssen bei Tabellenverkleinerungen am unteren Rand bzw. in der unteren Ecke verbleiben.</LI>
 * <LI>Ausklammern der PK-Spalten (so sie nicht als referenzierbar gekennzeichnet sind) aus den ComboBoxen zur Auswahl
 * referenzierter Spalten.</LI>
 * </OL>
 *
 * <P>
 * <B>Grundlegende Vorgehensweise zur Anglisierung</B>
 * <P>
 * Es werden Zwischenklassen und Interfaces gebildet, die das alte Interface erweitern und eine Basis f&uuml;r die alte
 * Klasse bilden. Die Implementierung der Methoden wird von den alten, deutschen Methoden in die neuen englisch
 * bezeichneten verlagert.
 *
 * <I>Beispiel:</I> TabellenspaltenModel > TableColumnModel > TableColumn > Tabellenspalte.
 *
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=3>
 * <H2>Neue Features</H2> <BR>
 * <HR SIZE=1>
 *
 * <P>
 * <B>1.80</B>
 * <P>
 * &Uuml;bernahme der Button-Id in den ComponentDiagramListener.
 * <P>
 * Neuer Tabellendialog.
 *
 * <P>
 * <B>1.79</B>
 * <P>
 * Einbau komplexer Fremdschl&uuml;ssel.
 *
 * <P>
 * <B>1.78</B>
 * <P>
 * Aktualisieren des Bean-Generator-Plug-Ins.
 *
 * <P>
 * <B>1.77</B>
 * <P>
 * Korrekturen im Versionanzeigebereich.
 *
 * <P>
 * <B>1.76</B>
 * <P>
 * Anpassung des Domain-Imports/Exports &uuml;ber Inidateien.
 *
 * <P>
 * <B>1.74</B>
 * <P>
 * Anpassen der Default-Werte f&uuml;r den Code-Folder und den Daten-Panel-Namen.
 *
 * <P>
 * <B>1.67</B>
 * <P>
 * Herausl&ouml;sen von Basis-Klassen und &Uuml;bertrag in das Archimedes-Core-Projekt.
 *
 * <P>
 * <B>1.66</B>
 * <P>
 * &Uuml;bernahme der <CODE>archimedes.acf</CODE>-Packages aus dem Baccara-Projekt.
 * <P>
 * Einbau der Error- und Warnungslogik bei &Auml;nderungen am Modell.
 *
 * <P>
 * <B>1.65</B>
 * <P>
 * Der XML-Reader kann nun auch mit InputStreams.
 *
 * <P>
 * <B>1.64</B>
 * <P>
 * Aktivierung des XML-Datei-Formates.
 * <P>
 * Internationalisierung der Men&uuml;struktur (&uuml;ber Ressourcen-Dateien).
 *
 * <P>
 * <B>1.63</B>
 * <P>
 * Einbau Server-/Client-Kommunikation mit Bildung einer Hauptinstanz (zur Benutzerabfrage).
 * <P>
 * Anpassung "Double"-Felder im SQL-Script f&uuml;r PostgreSQL ("double precision").
 *
 * <P>
 * <B>1.62</B>
 * <P>
 * Entfernung des <CODE>Beschreibungstraeger</CODE>-Interfaces (Ersatz ist <CODE>CommentOwner</CODE>).
 * <P>
 * Korrektur der Behandlung von Boolean-Feldern f&uuml; PostgreSQL-Datenbanken.
 *
 * <P>
 * <B>1.61</B>
 * <P>
 * Erweiterung des Interfaces <CODE>OptionListProvider</CODE> um die Methode "getOptionByName(String)". Implementierung
 * der Methode in den Klassen <CODE>Diagramm</CODE> und <CODE>Tabelle</CODE>.
 *
 * <P>
 * <B>1.60</B>
 * <P>
 * Erh&ouml;hung der im Datei-Men&uuml; vorgehaltenen Verweise auf fr&uuml;her bereits ge&ouml;ffnete Dateien auf 20.
 *
 * <P>
 * <B>1.59</B>
 * <P>
 * Erweiterung um einen Abgleich zwischen zwei Datenmodellen auf SQL-Basis.
 *
 * <P>
 * <B>1.58</B>
 * <P>
 * Erweiterung um Unique-Constraints, die ein Feld enthalten, das auf NULL gesetzt werden kann und dadurch aus dem
 * Constraint herausf&auml;llt.
 *
 * <P>
 * <B>1.57</B>
 * <P>
 * Debugging des Abgleichs der komplexen Unique-Constraints zwischen Model und Schema.
 *
 * <P>
 * <B>1.56</B>
 * <P>
 * Einbau einer Wiederherstellung des 100%-View.
 *
 * <P>
 * <B>1.55</B>
 * <P>
 * Refactoring der Logik zur Erzeugung des SQL-Update-Scripts.
 *
 * <P>
 * <B>1.54</B>
 * <P>
 * Erweiterung um die F&auml;higkeit Foreign-Key-Constraints zur Werte-Kontrolle als Option zu definieren.
 *
 * <P>
 * <B>1.53</B>
 * <P>
 * Erweiterung des Option-Dialogs um eine Auswahl von m&ouml;glichen Optionen (via Kontextmen&uuml;)
 *
 * <P>
 * <B>1.52</B>
 * <P>
 * ???
 *
 * <P>
 * <B>1.51</B>
 * <P>
 * Korrektur der Spaltensortierung Einlesen des Modells.
 *
 * <P>
 * <B>1.50</B>
 * <P>
 * Erweiterung der Tabellenspalte um eine "Transient"-Flagge.
 *
 * <P>
 * <B>1.49</B>
 * <P>
 * Speicherung der zuletzt eingestellten Datenbankverbindung im Auswahldialog vor Erzeugen Update-Scripts.
 * <P>
 * &Uuml;berpr&uuml;fung bereits vergebener Namen f&uuml;r Datenbankverbindungen.
 * <P>
 * Verbesserung des Outputs beim Einlesen von Datenbankverbindungen.
 *
 * <P>
 * <B>1.48</B>
 * <P>
 * Erlauben von Wildcards "%DB_SERVER_NAME%", "%DB_NAME%" und "%DB_USER_NAME%" in Datenbankverbindungsdaten.
 *
 * <P>
 * <B>1.47</B>
 * <P>
 * Einbau eines Warnhinweises im Fall das kein Datenbankverbindung f&uuml;r die Generierung des Update-Scripts
 * gew&auml;hlt ist.
 * <P>
 * Lokalisierung des Fehlerdialogs bei der Generierung der Update-Scripts.
 *
 * <P>
 * <B>1.46</B>
 * <P>
 * Erweiterung um eine Liste von Datenbankverbindungen.
 *
 * <P>
 * <B>1.45</B>
 * <P>
 * Einbau der Logik f&uuml;r externe Tabellen.
 *
 * <P>
 * <B>1.44.1</B>
 * <P>
 * Internes Review: &Uuml;bernahme der Methoden "isNMRelation()" und "setNMRelation(boolean)" in das
 * <CODE>TableModel</CODE>. <BR>
 * Erweiterung um einen Dialog zum Editieren der pers&ouml;nlichen Einstellungen.
 *
 * <P>
 * <B>1.43.1</B>
 * <P>
 * Internes Review: Entfernung der <CODE>Tabelle-</CODE> und <CODE>TabellenModel-</CODE>Referenzen aus der
 * <CODE>ColumnModel-</CODE>Implementierung.
 * 
 * <P>
 * <B>1.42.1</B>
 * <P>
 * Einbau der M&ouml;glichkeit, einen alternativen, Schema abh&auml;ngigen Basispfad angeben zu k&ouml;nnen.
 * <P>
 * Erweiterung um die M&ouml;glichkeit die Version des Datenmodells im Schema zu hinterlegen.
 * 
 * <P>
 * <B>1.41.5</B>
 * <P>
 * Korrektur durch Unterbinden der Generierung leerer Prim&auml;rsch&uuml;ssel f&uuml;r Tabellen ohne
 * Schl&uuml;sselfelder.
 * <P>
 * Erweiterung des <CODE>ToStringContainers</CODE> um einen Konstruktor mit <CODE>ColumnModel</CODE>.
 *
 * <P>
 * <B>1.41.4</B>
 * <P>
 * Korrektur der Foreign-Key-Constraint-Namensbildung.
 *
 * <P>
 * <B>1.41.3</B>
 * <P>
 * Korrekturen am Script-Bau: Ber&uuml:;cksichtigung der individuellen Defaultwerte.
 *
 * <P>
 * <B>1.41.2</B>
 * <P>
 * Korrekturen am Script-Bau f&uuml;r mySQL.
 *
 * <P>
 * <B>1.41.1</B>
 * <P>
 * Einbau einer Update-Funktion, die automatisch die zus&auml;tzlichen SQL-Scripte l&ouml;scht.
 *
 * <P>
 * <B>1.40.1</B>
 * <P>
 * &Uuml;berarbeitung des Constraint-Definition und -namensgebung zur Vermeidung von Problemen bei &Auml;nderungen in
 * den Defaulteinstellungen der DBMS (z. B. PostgreSQL 8 zu 9).
 *
 * <P>
 * <B>1.39.4</B>
 * <P>
 * Das Setzen des NOT-NULL-Constraints erfolgt nun im Reducing-Teil des Update-Scripts. Das L&ouml;schen des Constraints
 * erfolgt im Adding-Teil.
 * 
 * <P>
 * <B>1.39.3</B>
 * <P>
 * SelectionAttribute-Klasse um Bezeichner "PHANTOM" erweitert. Hiermit k&ouml;nnen Attribute gekennzeichnet werden, die
 * zwar von der Persistenzschicht f&uuml;r Suchanfragen &uuml;bertragen werden, aber nicht in der GUI angezeigt werden.
 * 
 * <P>
 * <B>1.39.2</B>
 * <P>
 * Korrektur des Statements zum L&ouml;schen von Prim&auml;rschl&uuml;sseln f&uuml;r PostgreSQL-Scripte.
 * 
 * <P>
 * <B>1.39.1</B>
 * <P>
 * Reorganisation der Update-Scripts (erst Erweiterungen, dann L&ouml;schen).
 * <P>
 * Erweiterung um eine Eingabem&ouml;glichkeit f&uuml;r Scriptfragmente zu Beginn des Scripts und zwischen Erweiterungs-
 * und L&ouml;schteil.
 * 
 * <P>
 * <B>1.38.3</B>
 * <P>
 * Korrektur eines Problems bei der Erstellung und dem L&ouml;schen von Unique-Constraints.
 * <P>
 * Erweiterung des <CODE>DataModel</CODE>-Interfaces um die Methode <CODE>getDomainByName(String)</CODE>.
 * 
 * <P>
 * <B>1.38.2</B>
 * <P>
 * Parameter in Spaltentabelle des Tabellendialoges.
 * 
 * <P>
 * <B>1.38.1</B>
 * <P>
 * Einbau der zus&auml;tzlichen Constraints f&uuml;r CREATE-Statements.
 * 
 * <P>
 * <B>1.37.5</B>
 * <P>
 * Herausnahme des Dialogs beim Start des Codegenerators, der den Pfad anzeigt, in den der Code generiert wird.
 * <P>
 * erweiterung um den <CODE>DataModelListener</CODE> und dessen Logik.
 * <P>
 * &nbsp;
 * 
 * <P>
 * <B>1.37.4</B>
 * <P>
 * Behebung des Problems mit dem Aufheben von Unique-Constraints.
 * <P>
 * &nbsp;
 * 
 * <P>
 * <B>1.37.3</B>
 * <P>
 * Verweise auf Sequenzen bei Tabellenspalten.
 * <P>
 * &nbsp;
 * 
 * <P>
 * <B>1.37.1</B>
 * <P>
 * Erweiterung um die Tabellenoptionen.
 * <P>
 * &nbsp;
 * 
 * <P>
 * <B>1.36.12</B>
 * <P>
 * Erweiterung der CodeGeneratorUtil-Klasse um Methoden zum Erzeugen von Attributnamen aus Domains und String.
 * <P>
 * Erweiterung des NReference-Interfaces um Methoden zum Lesen und Setzen der Flagge zur Pr&uuml;fung, ob eine
 * Best&auml;tigung beim L&ouml;schen eines Listenelements n&ouml;tig ist.
 * <P>
 * Erweiterung des TableModels um eine Methode, die zu einem Panel die dazugeh&ouml;rige NReference findet, sofern eine
 * existiert.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.36.11</B>
 * <P>
 * Erweiterung des ColumnModel um eine Methode zum Lesen einer eventuell referenzierten Spalte.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.36.8</B>
 * <P>
 * Erweiterung des TableModel um eine Methode zum Lesen des Context-Namen.
 * <P>
 * Erweiterung der Konfiguration der Auswahlmembers um ein Attribut, welches eine Ausgabe zur Gewichtung des
 * Auswahlmembers macht (z. B. Optional).
 * <P>
 * Aufr&aumlu;men des <CODE>TabellenModel</CODE> interfaces.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.36.6</B>
 * <P>
 * Korrektur des Alter-Statements bei Boolean-Feldern.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.36.5</B>
 * <P>
 * Erweiterung der Domains um ein Parameters-Feld.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.36.4</B>
 * <P>
 * Im Fenster f&uuml;r den zus&auml;tzlichen SQL-Code k&ouml;nnen Tabellen- und Spaltennamen nun &uuml;ber
 * Tastenkombinationen ausgew&auml;hlt werden.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.36.3</B>
 * <P>
 * ??? :o)
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.36.2</B>
 * <P>
 * Korrekturen bei Erstellen von Tabellen mit UNIQUE-Indices (die &uuml;berfl&uuml;ssige ALTER-Anweisung entf&auml;llt).
 * <P>
 * Korrekturen beim CREATE-Statement: Alles kommt jetzt in Gro&azlig;buchstaben.
 * <P>
 * Entfernen der Benutzereinstellungen aus der Ini-Datei (Gruppe: UserInfos, Felder: Name, Token, Vendor).
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.36.1</B>
 * <P>
 * Personalisierte Benutzerdaten (z. B. f&uuml;r automatische Historien-Eintr&auml;ge).
 * <P>
 * Erweiterung der generierten SQL-Scripte um einen Header mit deutlichen Hinweisen, dass das Script nicht manuell
 * ge&auml;ndert werden sollte.
 * <P>
 * Entfernung &uuml;berfl&uuml;ssiger Kommentare aus dem Script.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.35.8</B>
 * <P>
 * Abgleich von Sequenzen.
 * <P>
 * Abgleich von Fremdschl&uuml;sselconstraints.
 * <p>
 * Erg&auml;nzung um ein Feld und einen Eingabedialog f&uuml;r zus&auml;tzlichen SQL-Code, der bei der
 * Update-Script-Generierung an den generierten SQL-Code angef&uuml;gt wird.
 * <P>
 * Abgleich von Uniqueconstraints.
 * <P>
 * Abspaltung der Ansicht vom eigentlichen Inhalt. Die Oberfl&auml;che kann nun f&uuml;r &auml;hnliche Aufgabe genutzt
 * werden (z. B. familytree).
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.35.7</B>
 * <P>
 * Entfernung der unique-Constraints aus dem Index-Abgleich.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.35.6</B>
 * <P>
 * Beseitigung kleinerer Unsch&auml;rfen beim Erzeugen der SQL-Scripte (z. B. BLOBS und Begrenzung der betrachteten
 * Columns auf die Tabellen des Schemas).
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.35.5</B>
 * <P>
 * Korrektur am Create-Statement-Generator: Unique-Constraints auf einzelne Spalten werden nun auch verarbeitet.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.35.4</B>
 * <P>
 * Uebernahme der Unique-Klausel (aus dem Codegenerator-Panel Feld Unifikatsformel) in den JPA-Klassen-Generator.
 * <P>
 * Uebernahme der Unique-Klausel (aus dem Codegenerator-Panel Feld Unifikatsformel) in das Erstellen des SQL-Scripts.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.35.3</B>
 * <P>
 * Kleinere Anpassungen des durch das JPA-Plugin produzierten Codes.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.35.2</B>
 * <P>
 * Einbau von Benutzerinformationen, die aus der Ini-Datei im Home-Verzeichnis des Benutzers gelesen werden (Gruppe:
 * UserInfos, Felder: Name, Token, Vendor).
 * <P>
 * Ber&uuml;cksichtigung der durch die Ini-Datei konfigurierten Benutzerdaten im JPA-Plug-in.
 * <P>
 * Erweiterung des JPA-Plug-Ins um die M&ouml;glichkeit, die Constraints-Generation zu- bzw. wegzuschalten.
 * <P>
 * Anpassung des JPA-Plug-ins: Es wird eine Table-Annotation wird mit dem Klassennamen generiert und bei jeder
 * Column-Annotation wird der Spaltenname angegeben.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.35.1</B>
 * <P>
 * Die Fehlermeldung beim Erzeugen eines Update-Scripts f&uuml;r eine leere Datenbank ist verst&auml;ndlicher gestaltet
 * worden.
 * <P>
 * Das L&ouml;schen von Tabellen setzt den Status des Diagramms auf ge&auml;ndert.
 * <P>
 * Die Flaggen "Unique" und "Not Null" werden nun auch in der Tabellenspalten&uuml;bersicht angezeigt. Die Unique-Flagge
 * findet nun auch ihren Niederschlag im Diagramm.
 * <P>
 * Die Beachtung der Not-Null-Flagge wird beim Scriptbau in angemessener Weise beachtet.
 * <P>
 * Einbau eines Eingabefeldes f&uuml;r einen Schemanamen in den Diagrammparametern.
 * <P>
 * Ber&uuml;cksichtigung des Schemawechsels beim Erstellen der Update-Scripts.
 * <P>
 * Korrekte Umwandlung der boolschen Defaultwerte in PostgreSQL-Umgebung.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.34.2</B>
 * <P>
 * Korrektur des CreateStatementGenerators: Vor einem Primary-Key-Constraint wird ein Komma gesetzt.
 * <P>
 * Eine mit 'Boolean' bezeichnete Domain wird im PostgreSQL-Modus in den SQL-Typ 'boolean' umgesetzt, sofern nicht mit
 * Domains auf Datenbankebene gearbeitet wird.
 * <P>
 * Feld- und Tabellennamen k&ouml;nnen in den Update-Scripten in Anf&uuml;hrungszeichen gesetzt werden.
 * <P>
 * &nbsp;
 * 
 * <P>
 * <B>1.34.1</B>
 * <P>
 * Erweiterung um die Umsetzung von BLOB's im Update-Script.
 * <P>
 * &nbsp;
 * 
 * <P>
 * <B>1.33.2</B>
 * <P>
 * Erweiterung um Historienfelder f&uuml;r das Diagramm, Domains, Stereotypen, Tabellen und Tabellenspalten.
 * <P>
 * &nbsp;
 * 
 * <P>
 * <B>1.32.2</B>
 * <P>
 * Erweiterung um ein Plugin zur Ausf&uuml;hrung von JavaScripten.
 * <P>
 * &nbsp;
 * 
 * <P>
 * <B>1.32.1</B>
 * <P>
 * Erweiterung der Tabellenspalte um die Angabe eines individuellen Defaultwertes.
 * <P>
 * Erweiterung des Plugin zum Ermitteln der Tabellen zu einem Stereotyp.
 * <P>
 * &nbsp;
 * 
 * <P>
 * <B>1.31.1</B>
 * <P>
 * Erweiterung um die Utility zur Erstellung einer Auswertung &uuml;ber die Anzahl der Datens&auml;tze pro Tabelle in
 * einer Datenbank.
 * <P>
 * Korrektur des Austauschs des Tabellen- und des Typnamens in der Methode
 * <TT>changeWildcards(String, String, String, String)</TT> der Klasse <TT>Diagramm</TT>.
 * <P>
 * Erweiterung um die F&auml;higkeit, die Ini-Datei im Home-Verzeichnis des aktuellen Benutzers zu speichern und von
 * dort gegebenenfalls wieder zu lesen.
 * <P>
 * Automatisches Zur&uuml;cksetzen der Flagge "Erstkodierung erfolgt" beim Kopieren einer Tabelle &uuml;ber die
 * Oberfl&auml;che.
 * <P>
 * &nbsp;
 * 
 * <P>
 * <B>1.30.6</B>
 * <P>
 * Korrektur am Bau des Create-Statements f&uuml;r Tabellen mit mehrstelligen Schl&uuml;sseln.
 * <P>
 * Erweiterung um das Plugin <TT>ArchimedesPluginCopyTable</TT>.
 * <P>
 * Erweiterung um das Plugin <TT>ArchimedesPluginBeanGenerator</TT> und Umfeld.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.30.5</B>
 * <P>
 * Die Angaben zur Version-Tabelle werden nun unabh&uml;ngig vom Modus geschrieben.
 * <P>
 * Erweiterung der Klasse <TT>ApplicationUtil</TT> um eine Variante der GetMetadata-Methode, in der die Entscheidung
 * &uuml;ber das Schliessen der genutzten Connection im Aufruf festgelegt wird.
 * <P>
 * Anpassung an die Erweiterungen des GenGen-Interfaces <TT>ClassMetaData</TT> und Implementierung des
 * <TT>SelectionViewMetaData</TT>-Interfaces. festgelegt wird..
 * <P>
 * Erweiterung des Domain-Models um die Methode <TT>getType(DBExecMode)</TT>, die DBMS-angepasste Typenbezeichnungen
 * zur&uuml;ckliefert.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.30.4</B>
 * <P>
 * Datentypen&auml;nderungen werden nun auch im HSQL-Modus durchgef&uuml;hrt.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.30.3</B>
 * <P>
 * Korrektur des alter-Statements in der Build-Routine der Script-Produktion (Typ&auml;nderung).
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.30.2</B>
 * <P>
 * Korrektur des alter-Statements in der Build-Routine der Script-Produktion.
 * <P>
 * Einbau einer M&ouml;glichkeit, den Output beim Einlesen eines Modells weitr&auml;umig zu unterdr&uuml;cken.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.30.1</B>
 * <P>
 * Erweiterung um das Plugin "ArchimedesPluginTableLister".
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.29.1</B>
 * <P>
 * Erweiterung um die erste Version des Packages <TT>archimedes.sql</TT>.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.28.1</B>
 * <P>
 * Anpassung der Tabellenspalten-Spezifikation an Erweiterungen des gengen-AttributeMetaDatas
 * (<TT>isPrimaryKeyMember()</TT>).
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.27.2</B>
 * <P>
 * Anpassung der DiagrammModel-Spezifikation an Erweiterungen des gengen-ModelMetaDatas (<TT>getProjectToken()</TT>).
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.27.1</B>
 * <P>
 * Neubindung des Projektes.
 * <P>
 * Anpassungen an Erweiterungen der gengen-Metadaten-Interfaces.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.26.1</B>
 * <P>
 * Anpassungen zur Arbeit mit GenGen.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.25.1</B>
 * <P>
 * Erweiterung um die Methode <TT>isDifferentToScheme()</TT> in den Klassen <TT>DiagrammModel</TT> und
 * <TT>Diagramm</TT>. Der erste Wurf funktioniert nur mit Einschr&auml;nkungen, die durch eine Vorbedingung (Assertion)
 * abgesichert sind.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.24.2</B>
 * <P>
 * Debugging an der Methode <TT>getFieldsPKFirst(...)</TT>.
 * <P>
 * &nbsp;
 *
 * <P>
 * <B>1.24.1</B>
 * <P>
 * Erweiterung des TabellenModels um die Methode <TT>getFieldsPKFirst(...)</TT>.
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.23.4</B>
 * <P>
 * Herausnahme &uuml;berfl&uuml;ssiger Leerzeichen aus den Create- und den Insert-Statements, die durch die das
 * <TT>ArchimedesPluginMakeFillerCode</TT> generiert werden. Erweiterung des Plugins um Felder zu Definition von
 * Sortierungen (Order) und Einmaligkeit (Distinct).
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.23.3</B>
 * <P>
 * Verbesserung der Benutzeroberfl&auml;che des <TT>ArchimedesPluginMakeFillerCode</TT>.
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.23.2</B>
 * <P>
 * Implementierung der Voreinstellung der Datenbankverbindungsdaten im Konfigurationsdialog zum
 * <TT>ArchimedesPluginMakeFillerCode</TT>.
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.23.1</B>
 * <P>
 * Erweiterung des TabellenModels um die Methode <TT>makeInsertStatementCounted()</TT>. Damit verbunden: Implementierung
 * der Methode in der Klasse <TT>Tabelle</TT>.
 * <P>
 * Einbau des <TT>ArchimedesPluginMakeFillerCode</TT>.
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.22.1</B>
 * <P>
 * Einbau der ArchimedesPlugins.
 * <P>
 * Erweiterung der Methode makeCreateStatement aus der Tabellenklasse um eine M&ouml;glichkeit Tabellen- und
 * Spaltennamen zu quoten.
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.21.3</B>
 * <P>
 * Anpassung der Klasse <TT>ApplicationUtil</TT> an log4j.
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.21.2</B>
 * <P>
 * Erweiterung um ein SQLScriptEvent, das gegebenenfalls vor dem Generieren des Statements zum Aktualisieren der
 * Datenbankversion geworfen wird.
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.21.1</B>
 * <P>
 * Erweiterung des <TT>SQLScriptEvents</TT> um eines Referenz auf den aktuellen Zustand des aktuell generierten
 * Update-Scripts.
 * <P>
 * Anpassungen zum Erm&ouml;glichen des nachtr&auml;glichen Klassensetzens in MassiveListSelectors bei der Nutzung von
 * Editor-Descriptoren.
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.20.2</B>
 * <P>
 * Erweiterung ein Event, das beim SQL-Update-Scriptbau vor dem .
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.20.1</B>
 * <P>
 * Erweiterung um Logik zur Einbindung alternativer SQLScriptListener. Die f&uuml;r den Benutzer wichtigen Klassen und
 * Interfaces tummeln sich im Package <TT>archimedes.script.sql</TT>.
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.19.1</B>
 * <P>
 * Erweiterung des DiagrammModels um die Methode <TT>getCodegeneratorOptionsListTag(String, 
 *         String)</TT>.
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.18.1</B>
 * <P>
 * Entfernung der Konsolenausgaben aus der Methode <TT>ApplicationUtil.ReadProperties(Properties, String)</TT>.
 * <P>
 * Herausnahme des Ge&auml;ndertflaggensetzens bei Springen zu einer Tabelle.
 * <P>
 * &nbsp;
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.16.2</B>
 * <P>
 * Erweiterung des Kommancoprozessors um die Anweisung "mds", die ein Script mit DELETE-FROM-Statements f&uuml;r jede
 * Tabelle erstellt und in die Zwischenablage kopiert.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.15.7</B>
 * <P>
 * Erweiterung Klasse <TT>ApplicationUtil</TT> um die Methoden <TT>BuildBorder()</TT>, <TT>BuildFullBorder()</TT>,
 * <TT>BuildBorderedPanel()</TT> und <TT>BuildFullBorderedPanel()</TT>. Hier mit k&ouml;nnen Panels und Borders mit den
 * Standardwerten aus der Klasse <TT>corent.base.Constants</TT> generiert werden.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.15.6</B>
 * <P>
 * Anpassung an die Aktualisierung der Interfaces <TT>EditorDescriptor</TT> und <TT>SubEditor</TT> in Version 1.40.6 von
 * Corent.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.15.5</B>
 * <P>
 * Die SQL-Statements zur Erneuerung eines Fremdschl&uuml;ssels werden &uuml;ber eine Property konfiguriert.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.15.4</B>
 * <P>
 * Erweiterung um die Kommandos "ckf" und "lst".
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.15.3</B>
 * <P>
 * Korrektur der Primarykey-&Auml;nderungen in Updatescripten.
 * <P>
 * Definition von Defaultwerten in Create- und Alter-Statements in Updatescripten.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.15.2</B>
 * <P>
 * Erweiterung der Methode <TT>ApplicationUtil.EvaluateArgLine(String[], String)</TT> um Parameter f&uuml;r die
 * Konfiguration von Datenbankenzugriffen.
 * <P>
 * Erweiterung um die Methode <TT>ApplicationUtil.EvaluateArgLine(String[], 
 *         Dictionary&lt;String, String&gt;)</TT>.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.15.1</B>
 * <P>
 * Einbau eines zweiten Sichtfensters mit einem 10%-Zoom.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.14.1</B>
 * <P>
 * Ausdehnung der Importfunktion auf Referenzangaben aus der Datenbank.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.12.2</B>
 * <P>
 * Einbau der ArchimedesScript-Kommandos <TT>slf</TT> und <TT>clf</TT>.
 * <P>
 * Debugging des <TT>cav</TT>-Kommandos.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.12.1</B>
 * <P>
 * Diverse neue ArchimedesScript-Befehle.
 * <P>
 * Knicke lassen sich nun auch im Zoom-Modus entfernen (Die Funktion wird &uuml;bringens jetzt aufgrund von
 * Inkompatibilit&auml;ten mit Linux durch die rechte Maustaste ausgel&ouml;st).
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.11.6</B>
 * <P>
 * Erste Version des JavaCodeGenerators.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.11.5</B>
 * <P>
 * Die Knicke in Relationen werden nun in der Bearbeitungsansicht durch einen Kreis besser gekennzeichnet.
 * <P>
 * Erm&ouml;glichen der Relationsbearbeitung auch im Zoom-Modus.
 * <P>
 * Erweiterung des View-Men&uuml;s um die M&ouml;glichkeit die Anzeige von Referenzen und technischen Felder f&uuml;r
 * den aktuellen View direkt zu beeinflu&szlig;en (ohne den View editieren zu m&uuml;ssen).
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.11.4</B>
 * <P>
 * Einbau der M&ouml;glichkeit zum Umgehen der Aktivflaggenzur&uuml;cksetzung in der ReconstructableDBFactory.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.11.3</B>
 * <P>
 * Erweiterung um das 'cdm' (change domain) f&uuml;r den ArchimedesCommandProcessor.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.11.2</B>
 * <P>
 * Erweiterung um das 'mos' (make owner script) f&uuml;r den ArchimedesCommandProcessor.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.11.1</B>
 * <P>
 * Erweiterung der Tabellenspalte um ein Kennzeichen f&uuml;r Positionsattribute bei der Modellierung von
 * "flachgespeicherten" Listen.
 * <P>
 * Einbau einer M&ouml;glichkeit das Homeverzeichnis des Benutzers im Codepfad anzugeben.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.10.2</B>
 * <P>
 * Erweiterung der Debugginginformationen.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.10.1</B>
 * <P>
 * DIN-A3-Druck.
 * <P>
 * Erweiterung des TabellenModels um Methoden zum Setzen und Lesen von CodeGeneratorOptionen.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.9.4</B>
 * <P>
 * Einbau des AfterWriteScripts und Installation der notwendigen Infrastruktur zu Ausf&uuml;hrung von Scripten. Das
 * AfterWriteScript wird zun&auml;chst noch nicht automatisiert ausgef&uuml;hrt.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.9.3</B>
 * <P>
 * Erweiterung der Kommandozeile um den Befehl [Tabellenname] "bfl", der eine Textzeile mit den durch Kommata getrennten
 * Spaltennamen der Tabelle in das Clipboard kopiert.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.9.2</B>
 * <P>
 * Einbau einer optionalen Zeitmessung f&uuml;r das Einlesen von Archimedes-Datenmodellen.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.9.1</B>
 * <P>
 * Erweiterung um den DiagramSaveMode, mit dessen Hilfe verkleinerte ads-Dateien erzeugt werden k&ouml;nnen.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.8.4</B>
 * <P>
 * Der <TT>fto</TT>-Befehl funktioniert nun auch bei den Kommandozeilenbefehlen <TT>add</TT>, <TT>del</TT> und
 * <TT>ren</TT>.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.8.3</B>
 * <P>
 * Debugging: Der L&ouml;schvorgang von Tabellenspalten &uuml;ber die Kommandozeile ist vervollst&auml;ndigt worden. Nun
 * werden auch die Listen, in denen sich die Tabellenspalte befindet, von ihr befreit.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.8.2</B>
 * <P>
 * Einbeziehung der TechnicalField-Flagge in das Copy-&-Paste-Verfahren.
 * <P>
 * Tabellenspalten mit bereits vorkommenden Namen k&ouml;nnen nun nicht mehr &uuml;ber die Oberfl&auml;che angelegt
 * werden.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.8.1</B>
 * <P>
 * Kennzeichnungsm&ouml;glichkeit f&uuml;r rein technische Datenfelder. Diese Felder k&ouml;nnen optional in der Ansicht
 * ausgegraut angezeigt werden. Alternativ dazu k&ouml;nnen technische Datenfelder in einzelnen Views auch vollkommen
 * ausgeblendet werden.
 * <P>
 * RemovedStateFields werden nun durch das K&uuml;rzel "(RS)" im Diagramm gekennzeichnet.
 * <P>
 * Erweiterung der Kommandozeile um die Befehle zum Setzen und L&ouml;schen von TechnicalField- und
 * RemovedStateField-Flaggen (stf, ctf und srf, crf).
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.7.9</B>
 * <P>
 * Einbau der Entfernungsfunktion f&uuml;r Referenzen.
 * <P>
 * Einbau der M&ouml;glichkeit einen einzelnen Knick in einer Referenzenlinie zu entfernen (&uuml;ber Rechtsclick+ALT
 * auf den zu entfernenden Punkt).
 * <P>
 * Archimedes-Kommandozeile.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.7.8</B>
 * <P>
 * Verenglischung der eingebauten Standardtabelle.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.7.7</B>
 * <P>
 * Erweiterung um den FilenameSelector.
 * <P>
 * Anpassung der CodeGeneratoren auf das aktuelle Format der Klassenkommentare.
 * <P>
 * Optische Kennzeichnung von N:M-Tabellen (durch die Buchstaben "N:M" in der linken oberen Ecke des Tabellenkopfes.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.7.6</B>
 * <P>
 * Einbau einer M&ouml;glichkeit Tabs propertyressourcengesteuert zu initialisieren.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.7.5</B>
 * <P>
 * Einbau einer Warnung bei Werfen einer Exception w&auml;hrend des Baus eines Update-Scripts.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.7.4</B>
 * <P>
 * Erweiterung des <TT>SortedListSubEditorEventObject</TT> um eine Referenz auf den manipulierten Vector.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.7.2</B>
 * <P>
 * Korrektur Umlaut in Hinweisdialog (ge&aunl;ndert ...).
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.7.1</B>
 * <P>
 * Sprachliche Vereinheitlichung der Methoden- und Klassennamen auf englische Namensgebungen. (erste Schritte).
 * <P>
 * Anpassungen an die Erweiterungen im TabbedEditable-Interface aus der corent-Bibliothek.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.6.5</B>
 * <P>
 * Erweiterung des <TT>DBFactoryTableCache</TT> um die Methode <TT>remove(Object)</TT>.
 * <P>
 * Der NullPointerException-Bug bei nicht gesetzter Hintergrundfarbe beim Tabellenkopieren ist nun behoben.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.6.4</B>
 * <P>
 * Korrektur des Vergleichs von BIGINT-Feldern unter PostgreSQL.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.6.3</B>
 * <P>
 * Kleine &Auml;nderungen am DBFactoryTableCache.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.6.2</B>
 * <P>
 * Kleine &Auml;nderungen im Rahmen der Arbeiten an der automatischen Generierung von Ressourcenscripten.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.6.1</B>
 * <P>
 * Erste Schritte in Richtung generierter Ressourcendateien.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.5.8</B>
 * <P>
 * Anpassungen an die Corent-Version 1.28.1.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.5.7</B>
 * <P>
 * Neubau nach Einf&uuml;hrung des EditorDjinnModes.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.5.6</B>
 * <P>
 * Debugging an der Mehrsprachf&auml;higkeit.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.5.5</B>
 * <P>
 * Erweiterung der MassiveListSelectoren um die M&ouml;glichkeit die ausgew&auml;hlte Klasse nachtr&auml;glich zu
 * &auml;ndern.
 * <P>
 * Schaffung einer M&ouml;glichkeit aus MassiveListSelectoren eine Neuanlage zu initiieren.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.5.4</B>
 * <P>
 * Kleinere Arbeiten im Rahmen des Encodings.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.5.3</B>
 * <P>
 * Die Feldnamen f&uuml;r die ReconstructableDBFactory k&ouml;nnen nun &uuml;ber Properties initialisiert werden.
 * <P>
 * Korrektur des Drop-Index-Statements.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.5.2</B>
 * <P>
 * Debuggings im Reconstructable-Umfeld.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.5.1</B>
 * <P>
 * Erweiterung des DiagrammModels um das Feld <TT>UdschebtiBaseClass</TT> zur alternativen Definition einer
 * Applikationsgrundklasse (die immer von archimedes.legacy.app.ApplicationObject</TT> abgeleitet werden sollte).
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.4.2</B>
 * <P>
 * Debugging am DBFactoryTableCache, soda&szlig; nun auch deaktivierte Deactivatables eingelesen werden k&ouml;nnen.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.4.1</B>
 * <P>
 * Arbeiten an der Zeichenkodierung des Quelltextes.
 * <P>
 * Umzug der SpeedyInput-Logik aus dem corent-djinn-Package.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.3.5</B>
 * <P>
 * Korrektur des ModificationOf-Bugs in der ReconstructableDBFactory.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.3.4</B>
 * <P>
 * Schreiben von User-Id's bei Operationen der Reconstructable-Factory.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.3.3</B>
 * <P>
 * Erweiterung DBFactoryTableCache um die Methode <TT>dump()</TT>. Synchronisation der Methoden dieser Klasse.
 * <P>
 * Erkennung von im Datenmodell nicht gesetzten aber in der Datenbank pr&auml;senten Indices (f&uuml;r mySQL und
 * PostgreSQL).
 * <P>
 * Debugging an der Reconstructable-Factory und -Logik.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.3.2</B>
 * <P>
 * Debugging im Bereich der Panelreihenfolge im DefaultEditorDjinnPanel.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.3.1</B>
 * <P>
 * Bindung von LineTextEditoren auch an Domains mit Namen "Langtext" oder "Longtext".
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.2.1</B>
 * <P>
 * Verbesserung der Erstellung des Update-Scripts (text- und index-Problematiken).
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>1.1.1</B>
 * <P>
 * Copy &amp; Paste f&uuml;r Tabellen :o). Die meisten Attribute einer Tabelle werden kopiert. Die Kopie ist auf jeden
 * Fall zu &uuml;berpr&uuml;fen.
 * <P>
 * Anpassung des Alter-Table-Statements an PostgreSQL.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.34.3</B>
 * <P>
 * Erweiterung der Tabellensicht der Generatoroptionen um die Flagge "Aktiv in Applikation".
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.34.2</B>
 * <P>
 * Die Tabellenparameter des Tabellen-Stapelpflege-Dialoges werden nun auch in die Inidatei &uuml;bernommen.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.34.1</B>
 * <P>
 * Erweiterung um den Stapelpflege-Dialog f&uuml;r Tabellen.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.33.3</B>
 * <P>
 * Die Templates k&ouml;nnen nun auch Referenzen enthalten.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.33.2</B>
 * <P>
 * Erweiterung der SortedListSubEditoren um das Interface <TT>TableListSubEditor</TT>.
 * <P>
 * Erweiterung der Klasse <TT>DBFactoryTableCache</TT> um die Methoden <TT>update</TT> und <TT>put</TT>.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.33.1</B>
 * <P>
 * &uuml;berf&uuml;hrung der Klasse <TT>DefaultComment</TT> in die Archimedes-Applikationslogik. Bei dieser Gelegenheit
 * ist auch das Interface <TT>DefaultCommentModel</TT> eingeenglischt worden.
 * <P>
 * Umbau der SortedListEditor-Varianten auf LockDjinns.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.32.4</B>
 * <P>
 * Neu&uuml;bersetzung nach Corent-Neubau.
 * <P>
 * Entfernung einiger Aufrufe aufgehobener Methoden.
 * <P>
 * Bugfix: Bei Relationen lassen sich nun Punkte vor dem Endpunkt bereits im ersten Anlauf ziehen.
 * 
 * <BR>
 * <P>
 * <B>0.32.3</B>
 * <P>
 * Debugging in bezug auf die <TT>null</TT>-Referenzen im SortedListSubEditor.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.32.2</B>
 * <P>
 * Wiedereinf&uuml;hrung des Konstruktors der <TT>DefaultDBFactory</TT>, der nur eine Referenz auf die genutzte
 * <TT>ArchimedesDescriptorFactory<TT> erfordert.
 * <P>TabellenModel und Tabelle sind um die Methode <TT>isStereotype(String)</TT> erweitert worden. Hier&uuml;ber
 * l&auml;&szlig;t sich komfortabel feststellen, ob die Tabelle von der angegebenen Stereotype ist.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.32.1</B>
 * <P>
 * Anpassungen an die neue Corent-Version (1.15.1) mit neuen Methoden im Bereich des <TT>DBFactory</TT>-Interfaces.
 * <P>
 * Einbau zus&auml;tzlicher Konsolenausgaben in der Methode <TT>toSTF()</TT>, die bei der Speicherung von Diagrammen
 * aufgerufen wird. Dieser Output wird nur dann erzeugt, wenn die Property <TT>corent.scheme.Diagramm.cout</TT> auf den
 * Wert <TT>true</TT> gesetzt wird.
 * <P>
 * Erweiterung um die M&ouml;glichkeit Tabellen &uuml;r Stereotypen aus dem Diagramm auszublenden.
 * <P>
 * Der Datenbankmodus f&uuml;r PostgreSQL wird nun auch beim Speichern korrekt ber&uuml;cksichtigt.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.31.1</B>
 * <P>
 * Einbau der Methoden makeCreateStatement, makeInsertStatement und makeUpdateStatement.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.30.1</B>
 * <P>
 * Korrektur des Fehlers bei der Verwendung von EditableColumnViewables. Alle Methoden arbeiten nun korrekt auf dem
 * Objekt zur Zeile und nicht mehr auf einer Blaupause.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.29.12</B>
 * <P>
 * Einbau einer detailierteren Fehlermeldung, beim Schreiben von Modelldateien.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.29.11</B>
 * <P>
 * Einbau von Debugging-Output.
 * <P>
 * Korrektur der DefaultCodeFactory. Vererbung wird nun nur noch angenommen, wenn die Schl&uuml;sselspalten auf die
 * Schl&uuml;sselspalten der Supertabelle verweisen <B>UND</B> das Inherited-Flag gesetzt ist.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.29.10</B>
 * <P>
 * Das Suchen der PrimaryKeys in der Methode ApplicationUtil.GetMetaData() ist nun in einem separaten try-catch-Block
 * untergebracht. Datenbanken, die dieses Feature nicht unterst&uuml;tzen k&ouml;nnen auf diese Weise trotzdem
 * bearbeitet werden.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.29.9</B>
 * <P>
 * Der MassiveSelector akzeptiert im Archimedes-Applikationsumfeld nun auch Long-Werte.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.29.8</B>
 * <P>
 * Umsetzung der Methoden codeTable, getBeschreibung und makeDirAndPackage von <TT>private</TT> auf <TT>protected</TT>.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.29.7</B>
 * <P>
 * Erweiterung der Klassen Tabelle und TabellenModel um die Methoden getPrimaryKeyTabellenspalten,
 * getParentTabellenModel, isParent und IsFullReferenced durch M.Eckstein (d. h. &Uuml;bernahme von M.Eckstein
 * implementierten der Funktionen).
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.29.6</B>
 * <P>
 * Die Standard-Tabelle (ohne Template) l&auml;&szlig;t sich wieder anlegen.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.29.5</B>
 * <P>
 * Unlock bei SortedListSubEditors funktioniert jetzt.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.29.4</B>
 * <P>
 * Der Fehler, der bei "eigenst&auml;ndigem" Betrieb von SortedListSubEditors f&uuml;r eine Verdoppelung der Objekte
 * nach Neuanlage sorgte, ist behoben.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.29.3</B>
 * <P>
 * Die Listenansicht bei eigenst&auml;ndigen Objekten im SortedListSobEditor wird nun nach &Auml;nderungen aktualisiert.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.29.2</B>
 * <P>
 * Einbau des Nachladens in den SortedSubEditors, wenn der InternalFrame-Modus eingeschaltet ist.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.29.1</B>
 * <P>
 * Erweiterung der Tabellenspalte um die Flagge HideReference. Hier&uuml;ber kann die Referenzanzeige (gemeint ist die
 * Linie im Diagramm) explizit f&uuml;r eine Tabellenspalte ausgeschaltet werden.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.28.4</B>
 * <P>
 * Die Templates funktionieren nun richtig.
 * <P>
 * Erweiterung der Templatefunktion um die CanBeReferenced-Flagge.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.28.3</B>
 * <P>
 * Die Aktualisierung der Listenansicht in den SortedListSubEditor-Implementierungen.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.28.2</B>
 * <P>
 * Die in Entwicklung befindlichen Tabellen werden nun nicht mehr im Update-Script und in der Update-Dokumentation
 * ber&uuml;cksichtigt.
 * 
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.28.1</B>
 * <P>
 * Erweiterung um die Reconstructable-Logik.
 * <P>
 * Herausnahme der CLASS-Konstante aus aus dem Code-Writer.
 *
 * <BR>
 * <HR SIZE=1>
 * <P>
 * <B>0.27.1</B>
 * <P>
 * Erweiterung um die Bearbeitungen des LongPTimestamp.
 *
 * <BR>
 * <P>
 * <B>0.26.1</B>
 * <P>
 * Erweiterung des DBFactoryTableCache um eine M&ouml;glichkeit die Auswahl der gecacheten Elemente einzuschr&auml;nken
 * (Where-Klausel).
 * <P>
 * Anpassungen an den ExtendedListOwner im Bereich der SortedSubEditoren.
 * 
 * <P>
 * <B>0.25.3</B>
 * <P>
 * Behebung des Bugs, der in der ArchimedesComponentFactory.transferValue()-Methode null-Referenzen abgelehnt hat.
 * 
 * <BR>
 * <P>
 * <B>0.25.2</B>
 * <P>
 * Der ArchimedesMassiveListSelector kann nun auch Referenzen verarbeiten, die nicht auf Primary-Key-Spalten zeigen.
 * 
 * <BR>
 * <P>
 * <B>0.25.1</B>
 * <P>
 * Anpassungen der SubEditorFactories an die &Auml;nderungen des Interfaces in der Version 1.6.x der Corent-Bibliothek.
 * <P>
 * Erweiterung um zus&auml;tzliche Methodik, die zus&auml;tzlich Ereignisse des SubEditors abf&auml;ngt bzw. einen
 * Zugriff auf die Komponenten des den SubEditor besitzenden EditorDjinnPanels.
 *
 * <BR>
 * <P>
 * <B>0.24.2</B>
 * <P>
 * Debugging an der Funktionalit&auml;t von eigenst&auml;ndigen Panels.
 * 
 * <BR>
 * <P>
 * <B>0.24.1</B>
 * <P>
 * Einbau des Paneltyps "eigenst&auml;ndig" mit den entsprechenden Anpassungen im SortedListSubEditor.
 * 
 * <BR>
 * <P>
 * <B>0.23.3</B>
 * <P>
 * Die Tabellen in der &lt;Projekt&gt;Core.java werden nun in alphabetischer Reihenfolge generiert.
 * 
 * <BR>
 * <P>
 * <B>0.23.2</B>
 * <P>
 * Einbuchstaben-Spalten werden nun durch den Codewriter korrekt behandelt.
 * 
 * <BR>
 * <P>
 * <B>0.23.1</B>
 * <P>
 * Einbau einer Flagge "Referenzierbar" in die Tabellenspalte. Tabellenspalten, bei denen diese Flagge gesetzt ist,
 * werden auch in den Auswahlen zu referenzierten Spalten angeboten, auch wenn sie keine Schl&uuml;sselspalten sind.
 * <P>
 * Der Aufbau der Standardtabelle kann &uuml;ber eine Datei definiert werden.
 * <P>
 * Die initial eingestellte Schriftgr&ouml;&szlig;e f&uuml;r Tabelleninhalte betr&auml;gt nun 12 Punkt.
 *
 * <BR>
 * <P>
 * <B>0.22.2</B>
 * <P>
 * Entfernung der sinnlosen Ausgabe f&uuml;r nicht gefundene Parameter beim Modellabgleich.
 *
 * <BR>
 * <P>
 * <B>0.22.1</B>
 * <P>
 * Erweiterung der Farbpalette.
 * <P>
 * Einbau eines Rastermodus. Wird bei der Bewegung eines grafischen Objektes die Shifttaste gedr&uuml;ckt gehalten, so
 * bewegt sich das Objekt auf den Rasterkoordinaten. Im Bereich der Punkte, bei denen Relationen auf Tabellen treffen,
 * ist die Anwendung dieser Funktion (noch) <U>nicht</U> zu empfehlen.
 * <P>
 * Erweiterung der Modelldatei um die benutzten Farben. Auf diese Weise k&ouml;nnen f&uuml;r jedes Modell individuelle
 * Farbpaletten zusammengestellt werden. Derzeit l&auml;szlig; sich die Palette allerdings nur in der ADS-Datei
 * bearbeiten. Soll ein Modell auf die Archimedes-Standard-Palette zur&uuml;ckgestellt werden, so mu&szlig; lediglich
 * die Sektion <TT>Colors</TT> aus der ADS-Datei entfernt werden.
 * 
 * <P>
 * <B>0.21.1</B>
 * <P>
 * Einbau eines Feldes <I>InEntwicklung</I> in die Tabelle. Hiermit k&ouml;nnen Tabellen gekennzeichnet werden, noch in
 * Entwicklung sind und deshalb aus produktiven Aktionen herausgehalten werden sollen.
 * <P>
 * Die ALT-N-Kombination ist aus dem ersten Panel des Tabellenspalten-Dialoges entfernt worden.
 * <P>
 * Rudiment&auml;re Sicherungsfunktion f&uuml;r ge&auml;nderte Diagramme. Allerdings befindet sich diese Funktion zur
 * Zeit noch in der Testphase. Also: nicht darauf verlassen!
 *
 * <BR>
 * <P>
 * <B>0.20.1</B>
 * <P>
 * Kennzeichnung der indizierten Spalten in der Spalten&uuml;bersicht des Tabellendialoges.
 * <P>
 * Einbeziehen der Indizierung in den Scriptbau.
 * <P>
 * Erweiterung der Tabellenspalte um die Flagge <I>Indexsuchfeld</I>.
 * <P>
 * Die CodeFactory wird nun &uuml;ber die Model-Datei konfiguriert. Ihr Name kann unter in den Diagrammparametern im Tab
 * <I>Codegenerator</I> angegeben werden.
 * <P>
 * In den Udschebti wird nun auch die Methode <I>isRemoved()</I> aus dem Deactivatable-Interface vorbereitend
 * implementiert.
 *
 * <BR>
 * <P>
 * <B>0.19.16</B>
 * <P>
 * Verbesserungen beim Einlesen der Metadatenmodelle zwecks Abgleich.
 *
 * <BR>
 * <P>
 * <B>0.19.15</B>
 * <P>
 * Erweiterung des TabellenspaltenModels um eine Flagge zu Kennzeichnung von Geloescht-Status-Spalten.
 * <P>
 * Erweiterung des TabellenModels um eine Methode zur Erzeugung eines Create-Statements und eine Methode zur Erzeugung
 * von Namenslisten f&uuml;r Datenfelder.
 *
 * <BR>
 * <P>
 * <B>0.19.14</B>
 * <P>
 * Korrektur des Bugs, der deaktivierte Datens&auml;tze auch in MassiveListSelectoren nicht angezeigt hat. Dies ist nun
 * anders.
 *
 * <BR>
 * <P>
 * <B>0.19.12</B>
 * <P>
 * Erweiterung der Tabellenspalte um eine Flagge zur expliziten Kennzeichnung von Letzte-Aenderungs-Feldern.
 * <P>
 * Aufgehobene Tabellen werden nun beim Abgleich des Archimedesmodells mit dem Datenschema nicht mehr
 * ber&uuml;cksichtigt.
 *
 * <BR>
 * <P>
 * <B>0.19.11</B>
 * <P>
 * &Uuml;ber die Property <I>archimedes.app.ApplicationUtil.GetMetaData.output</I> (Boolean) kann der Konsolenoutput
 * f&uuml;r die Methode <TT>GetMetaData(JDBCDataSourceRecord, 
 *         List&lt;String&gt;)</TT> eingeschaltet werden.
 * <P>
 * Die Methoden <TT>Diagramm.getTabelle(String)</TT> und <TT>Tabelle.getTabellenspalte(String)</TT> liefern nun einen
 * <TT>null</TT>-Wert, anstatt einer NoSuchElementException.
 *
 * <BR>
 * <P>
 * <B>0.19.10</B>
 * <P>
 * Herausl&ouml;sung der Methode getMetadata(JDBCDataSourceRecord) aus FrameArchimedes und Verlagerung als statische
 * Methode nach ApplicationUtil.
 * <P>
 * Die Editormember-Flagge neu erzeugter Tabellenspalten sind zur&uuml;ckgesetzt.
 * 
 * <BR>
 * <P>
 * <B>0.19.9</B>
 * <P>
 * Aufnahme einer Stapelpflege-Flagge in das Tabellenspalten- und Tabellenmodel. Dies dient als Vorbereitung einer
 * Stapelpflegefunktion in die Archimedes-Applikationslogik.
 * <P>
 * Einbau des Men&uuml;punkts Grafikexport. Hiermit kann das gesamte Diagramm in eine JPEG-Grafik exportiert werden.
 * <P>
 * F&uuml;r Tabellen k&ouml;nnen nun auch Hintergrundfarben festgelegt werden.
 * <P>
 * Der SmartPositioner bei der Tabellenbewegung ist nun (schaltbar) an der linken oberen Ecke angebracht.
 * <P>
 * Erweiterung der Farbpalette um ein paar Pastellt&ouml;ne (im Zuge der Corent.Version 1.4.6 ist die Auswahl der
 * Farbt&ouml;ne nun alphabetisch sortiert).
 * 
 * <BR>
 * <P>
 * <B>0.19.8</B>
 * <P>
 * Das Exceptionhandling im ApplicationObject ist zum besseren Debugging in den Methoden
 * <TT>getPersistenceDescriptor()</TT> und <TT>getEditorDescriptorList()</TT> verbessert worden.
 * <P>
 * Der Fehler in der Implementierung des Interfaces <TT>EditorDjinnMaster</TT> in den durch die DefaultCodeFactory
 * generierten Klassen ist behoben worden.
 *
 * <BR>
 * <P>
 * <B>0.19.7</B>
 * <P>
 * Einf&uuml;hrung von Flaggen f&uuml;r globale Attribute und globale Id's (beide Tabellenspalte), sowie f&uuml;r
 * bereits einmal generierte Klassen zu Tabellen (Tabelle).
 * <P>
 * Die DefaultCodeFactory generiert nur noch den Udschebti, wenn zu einer Tabelle die Erstgenerierungsflagge gesetzt
 * ist.
 * <P>
 * Pr&uuml;fung der ObjectFactory, mit der ein Archimedes-Modell zuletzt gespeichert worden ist. Im Falle einer
 * Abweichung wird der Benutzer nach dem Einlesen des Modells durch einen Info-Dialog gewarnt.
 *
 * <BR>
 * <P>
 * <B>0.19.6</B>
 * <P>
 * Einbau einer Flagge, mit der Tabellen als Zwischentabellen f&uuml;r N:M-Beziehungen gekennzeichnet werden
 * k&ouml;nnen.
 * <P>
 * Erweiterung der DefaultCodeFactory um die F&auml;higkeit die N-Seite von 1:N-Beziehungen in den betreffenden
 * DBFactory-Ableitung zu implementieren.
 * 
 * <BR>
 * <P>
 * <B>0.19.5</B>
 * <P>
 * Es ist nun m&ouml;glich, die Anzeige des Tabellendiagramms nach Auswahl einer Tabelle direkt auf die Position der
 * Tabelle springen zu lassen. <BR>
 * <HR>
 * <H2>Geplante Features</H2>
 * <HR SIZE=1>
 * <P>
 * <I>Indices:</I> Einbeziehen der Indizierungsflagge in den Scriptbau.
 * <P>
 * <I>Stapelpflege:</I> Das Diagramm mu&szlig; um ein Feld erweitert werden, mit dem stapelpflegbare Attribute von
 * Tabellen gekennzeichnet werden k&ouml;nnen. Anschlie&szlig;end kann eine Funktion in den entsprechenden
 * Corent-Klassen implementiert werden, mit denen die Funktionalit&auml;t umgesetzt werden kann.
 * <P>
 * <I>Mausbewegung:</I> F&uuml;r die Mausbewegung wird ein Rastermodus implementiert, &uuml;ber den die grafischen
 * Objekte am Raster orientiert bewegt werden.
 * <P>
 * <I>&Uuml;bersicht Kodierungsflaggen:</I> Es wird ein Dialog entstehen, &uuml;ber den die Kodierungsflaggen der
 * einzelnen Tabellen sichtbar sind und diese gesetzt bzw. gel&ouml;scht werden k&ouml;nnen.
 *
 * @author ollie
 *         <P>
 *
 * @changed OLI 10.01.2009 - Erweiterung der Implementierung der ArchimedesApplication in der main-Methode um den
 *          Methodendummy der createFactories-Methode.
 *          <P>
 *
 */

public class Archimedes {

	public static final String CONF_PATH = "./src/main/resources/conf/";

	private static Logger log = Logger.getLogger(Archimedes.class);

	/** Der DBMode, unter dem die Anwendung laufen soll. */
	public static DBExecMode DBMODE = DBExecMode.MYSQL;
	/** Referenz auf die g&uuml;tige Archimedes-ObjectFactory. */
	public static ObjectFactory Factory = null;
	/** Die Farbenpalette zur Benutzung im Programm. */
	public static AbstractExtendedColorPalette PALETTE = null;
	/**
	 * Referenz auf die ArchimedesDescriptorFactory mit der Archimedes l&auml;uft.
	 */
	public static DefaultArchimedesDescriptorFactory ADF = null;
	/** An access to the application GUI bundle. */
	public static GUIBundle guiBundle = null;

	// REQUEST OLI 04.06.2010 - Sind das Ueberbleibsel der Kompilierprobleme ?!?
	/*
	 * private MediSysObjectFactory tmp = null; private archimedes.legacy.app.ApplicationUtil tmp0 = null; private
	 * archimedes.legacy.djinn.speedy.SpeedyInputDjinn sid = null; private
	 * archimedes.legacy.djinn.speedy.SpeedyInputUtil siu = null;
	 */

	/**
	 * Liefert Versionsnummer von Archimedes. Das mu&szlig;te so umgebaut werden, weil die Konstante beim Compilieren
	 * offensichtlich direkt in den Bytecode eingef&uuml;gt worden ist.
	 * 
	 * @return Liefert die Versionsnummer der Archimedes-Bibliothek.
	 * 
	 * @changed OLI 16.02.2008 - Hinzugef&uuml;gt.
	 * @changed OLI 20.12.2011 - Aktualisierung auf Aufruf des Versionsobjektes.
	 */
	public static String GetVersion() {
		return Version.INSTANCE.getVersion();
	}

	/**
	 * Liefert die Nummer mindestens ben&ouml;tigten Corent-Library.
	 * 
	 * @return Die Nummer mindestens ben&ouml;tigten Corent-Library.
	 * 
	 * @changed OLI 09.01.2009 - Hinzugef&uuml;gt.
	 *          <P>
	 * 
	 */
	public static String GetMinimumCorentVersion() {
		return "1.54.2";
	}

	/** Reinitialisiert die Standard-Palette. */
	public static void InitPalette() {
		PALETTE = new AbstractExtendedColorPalette() {
			@Override
			public Hashtable<String, ExtendedColor> createPalette() {
				Hashtable<String, ExtendedColor> htse = new Hashtable<String, ExtendedColor>();
				htse.put("blau", new ExtendedColor("blau", Color.blue));
				htse.put("blaugrau", new ExtendedColor("blaugrau", 175, 212, 221));
				htse.put("braun", new ExtendedColor("braun", 170, 125, 61));
				htse.put("dunkelgrau", new ExtendedColor("dunkelgrau", Color.darkGray));
				htse.put(StrUtil.FromHTML("dunkelgr&uuml;n"),
						new ExtendedColor(StrUtil.FromHTML("dunkelgr&uuml;n"), 94, 132, 84));
				htse.put("gelb", new ExtendedColor("gelb", Color.yellow));
				htse.put("goldgelb", new ExtendedColor("goldgelb", 255, 225, 0));
				htse.put("grau", new ExtendedColor("grau", Color.gray));
				htse.put(StrUtil.FromHTML("gr&uuml;n"), new ExtendedColor(StrUtil.FromHTML("gr&uuml;n"), Color.green));
				htse.put("hellblau", new ExtendedColor("hellblau", 191, 212, 255));
				htse.put("hellgrau", new ExtendedColor("hellgrau", Color.lightGray));
				htse.put("kamesinrot", new ExtendedColor("kamesinrot", Color.magenta));
				htse.put("orange", new ExtendedColor("orange", Color.orange));
				htse.put("pastell-blau", new ExtendedColor("pastell-blau", 211, 247, 255));
				htse.put("pastell-gelb", new ExtendedColor("pastell-gelb", 255, 245, 186));
				htse.put(StrUtil.FromHTML("pastell-gr&uuml;n"),
						new ExtendedColor(StrUtil.FromHTML("pastell-gr&uuml;n"), 211, 255, 234));
				htse.put("pastell-lila", new ExtendedColor("pastell-lila", 244, 211, 255));
				htse.put("pastell-rot", new ExtendedColor("pastell-rot", 244, 165, 191));
				htse.put("pink", new ExtendedColor("pink", Color.pink));
				htse.put("rot", new ExtendedColor("rot", Color.red));
				htse.put(StrUtil.FromHTML("t&uuml;rkis"),
						new ExtendedColor(StrUtil.FromHTML("t&uuml;rkis"), Color.cyan));
				htse.put("schwarz", new ExtendedColor("schwarz", Color.black));
				htse.put(StrUtil.FromHTML("wei&szlig;"),
						new ExtendedColor(StrUtil.FromHTML("wei&szlig;"), Color.white));
				return htse;
			}
		};
	}

	/**
	 * @changed OLI 06.10.2007 - Anpassung des Lesens der Propertydatei auf den neusten Stand. Erweiterung um das
	 *          Einlesen des Archimedesmodells.
	 * @changed OLI 11.03.2011 - Die Standard-Ini-Datei wird nun aus dem Home-Verzeichnis des Benutzers gelesen.
	 */
	public static void main(String[] args) {
		String inifilename = corentx.io.FileUtil.completePath(System.getProperty("user.home")).concat("archimedes.ini");
		boolean serverMode = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-i")) {
				i++;
				inifilename = args[i];
			} else if (args[i].equalsIgnoreCase("-s")) {
				serverMode = true;
			}
		}
		Inifile ini = new Inifile(inifilename);
		try {
			ini.load();
			log.info("Ini-Datei (" + inifilename + ") gelesen ...");
			String cn = ini.readStr("Factories", "Objects", "");
			if (cn.length() > 0) {
				try {
					Factory = (ObjectFactory) Class.forName(cn).newInstance();
					log.info("DefaultObjectFactory changed to " + Factory);
					InputStream in = ClassLoader.getSystemResourceAsStream("archimedes/dm/archimedes.ads");
					StructuredTextFile stf = new StructuredTextFile(in);
					try {
						stf.load();
						Diagramm d = new Diagramm();
						d = (Diagramm) d.createDiagramm(stf);
						ADF = new DefaultArchimedesDescriptorFactory(d);
						Factory.setADF(ADF);
					} catch (Exception e) {
						log.error("error while creating ADF: " + e.getMessage(), e);
					}
				} catch (ClassNotFoundException cnfe) {
					log.warn("ObjectFactory invocation failed. Factory set to default: " + cnfe.getMessage());
					// cnfe.printStackTrace();
				}
			}
		} catch (Exception e) {
			log.error("\nProblem beim Lesen der Ini-Datei (" + inifilename + ")\n", e);
		}
		String propfn = System.getProperty("archimedes.Archimedes.properties", CONF_PATH + "archimedes.properties");
		try {
			corentx.io.FileUtil.readProperties(System.getProperties(), propfn);
		} catch (FileNotFoundException e) {
			log.error("configured properties file not found: " + propfn, e);
		} catch (IOException e) {
			log.error("error while loading configured properties file: " + propfn, e);
		}
		String perspropfn = corentx.io.FileUtil.completePath(System.getProperty("user.home"))
				.concat(".archimedes.properties");
		if (new File(perspropfn).exists()) {
			try {
				corentx.io.FileUtil.readProperties(System.getProperties(), perspropfn);
			} catch (FileNotFoundException e) {
				log.error(e.getMessage(), e);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		} else {
			JOptionPane.showMessageDialog(null,
					Str.fromHTML("Datei mit pers&ouml;lichen Daten " + "wird vermisst:\n" + perspropfn));
		}
		String adsfn = System.getProperty("archimedes.Archimedes.ads");
		if (adsfn != null) {
			ADF = ApplicationUtil.CreateADF(adsfn);
			Factory.setADF(ADF);
		}
		/*
		 * try { File f = new File(propfn); FileInputStream fis = new FileInputStream(f);
		 * System.getProperties().load(fis); fis.close(); System.out.println("ok"); } catch (Exception e) {
		 * System.out.println("ERROR (" + e.getMessage() + ")"); }
		 */
		if (System.getProperty("archimedes.user.language") == null) {
			System.setProperty("archimedes.user.language", "en");
		}
		String lang = System.getProperty("archimedes.user.language");
		ResourceManager rm = new PropertyResourceManager(new PropertyFileManager()
				.open(System.getProperty("archimedes.user.language.resource.path", Archimedes.CONF_PATH)
						+ "archimedes_resource_" + lang + ".properties"));
		ImageProvider ip = new FileImageProvider(
				new ImageMapBuilder(System.getProperty("archimedes.img.path", "img")).build());
		guiBundle = new GUIBundle(ini, rm, ip, 3, 3);
		final FrameArchimedes frame = new FrameArchimedes(guiBundle, serverMode);
		/*
		 * Start Server ---------------------------------------------------------- -------------- if (serverMode) { if
		 * (System.getSecurityManager() == null) { System.setSecurityManager(new SecurityManager()); } try { String name
		 * = "UserMonitor"; UserMonitor engine = userMonitor; UserMonitor stub = (UserMonitor)
		 * UnicastRemoteObject.exportObject(engine, 0); Registry registry = LocateRegistry.createRegistry(1099);
		 * registry.rebind(name, stub); System.out.println("User monitor process bound"); } catch (Exception e) {
		 * System.err.println("User monitor process exception:"); e.printStackTrace(); } } else { String serverName =
		 * System.getProperty("archimedes.main.instance.host.name", "RHODOS"); userPing = new UserPing(serverName,
		 * System.getProperty("archimedes.user.token")); frame.setUserPing(userPing); }
		 */
		if (ADF != null) {
			ADF.setApplication(new ArchimedesApplication() {
				public String getPrintername() {
					return null;
				}

				public SecurityController getSecurityController() {
					return null;
				}

				public JFrame getFrame() {
					return frame;
				}

				public JDesktopPane getDesktop() {
					return null;
				}

				public DBFactoryController getDFC() {
					return null;
				}

				public String getImagePath() {
					return null;
				}

				public ArchimedesDescriptorFactory getADF() {
					return ADF;
				}

				public Object getUser() {
					return null;
				}

				public Hashtable<Class, DBFactory> createFactories(ArchimedesDescriptorFactory adf) {
					return null;
				}
			});
			ApplicationObject.STANDARDAPP = ADF.getApplication();
		}
	}

	/**
	 * @changed OLI 07.10.2007 - Erweiterung um das Prozedere zum Einlesen des Archimedes eigenen Datenmodells.
	 *          <P>
	 *          OLI 09.01.2009 - Erweiterung um die Versionspr&uuml;fung der Corent-Bibliothek.
	 *          <P>
	 * 
	 */
	static {
		InitPalette();
		// Initialisierung der ObjectFactory.
		Factory = DefaultObjectFactory.INSTANCE;
		InputStream in = Factory.getClass().getClassLoader().getResourceAsStream("archimedes/dm/archimedes.ads");
		StructuredTextFile stf = new StructuredTextFile(in);
		try {
			if (in != null) {
				stf.load();
				Diagramm d = new Diagramm();
				d = (Diagramm) d.createDiagramm(stf);
				Factory.setADF(new DefaultArchimedesDescriptorFactory(d));
			} else {
				log.warn("WARNING: ADS file not found in resources");
			}
		} catch (Exception e) {
			log.error("ERROR: while loading Archimedes data model: " + e.getMessage(), e);
		}
		try {
			new FontConfigurator().readFonts();
		} catch (Exception e) {
			log.error(
					"ERROR: while configuring the fonts: " + e.getClass().getSimpleName() + "(" + e.getMessage() + ")");
		}
		if (Corent.GetVersion().compareTo(GetMinimumCorentVersion()) == -1) {
			log.error("************************************************************");
			log.error("");
			log.error("Archimdes " + GetVersion() + " requires Corent " + GetMinimumCorentVersion() + " or higher!");
			log.error("");
			log.error("System halted ...");
			log.error("");
			log.error("************************************************************");
			System.exit(1);
		}
	}

}