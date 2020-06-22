/*
 * AbstractCodeGenerator.java
 *
 * 07.09.2009
 *
 * (c) by O.Lieshoff
 *
 */

package gengen.generator;

import java.io.File;
import java.util.List;

import logging.Logger;

import corentx.dates.PDate;
import corentx.io.FileUtil;
import corentx.util.AlphabeticalStringComparator;
import corentx.util.SortedVector;
import corentx.util.Utl;
import gengen.metadata.AttributeMetaData;
import gengen.metadata.ClassMetaData;
import gengen.metadata.ModelMetaData;

/**
 * Dieser dient als Grundlage f&uuml;r die durch den GenGen erzeugten Generatoren. Er bietet grundlegende Methoden, die
 * durch die Ableitungen genutzt werden.
 * <P>
 * Die eigentliche Implementierung des Interfaces <TT>CodeGenerator</TT> wird in den abgeleiteten CodeGenerator-Klassen
 * realisiert.
 *
 * <P>
 * <H3>Properties</H3>
 * <TABLE BORDER=1 WIDTH="100%">
 * <TR>
 * <TH>Property</TH>
 * <TH>Default</TH>
 * <TH>Bedeutung</TH>
 * </TR>
 * <TR>
 * <TD>gengen.generator.base.path</TD>
 * <TD>tmp/</TD>
 * <TD>Der Pfad, auf dem die Code-Generierung basieren soll. Hier wird die Packagestruktur angelegt, unter der der
 * CodeGenerator seine Klassen generiert.</TD>
 * </TR>
 * </TABLE>
 *
 * @author O.Lieshoff
 *
 * @changed OLI 07.09.2009 - Hinzugef&uuml;gt
 * @changed OLI 28.09.2009 - Anpassungen an den CLI-Betrieb. Einbau der property-gesteuerten Angabe des
 *          Basisverzeichnisses f&uuml;r die Codegenerierung.
 * @changed OLI 04.10.2009 - Aufhebung der Methode <TT>getSpaces(int)</TT>.
 * @changed OLI 07.10.2009 - Die Methode <TT>generate(MetaDataModell)</TT> schreibt keine leeren Dateien mehr.
 *          Stattdessen wird eine Warnung im Log erzeugt. Diese Eigenschaft kann dazu genutzt werden, um anhand der
 *          Klassenmetadaten zu entscheiden, ob Code f&uuml;r die Klasse generiert werden soll.
 * @changed OLI 07.10.2009 - Die Methode <TT>getDefaultValue(AttributeMetaData)</TT> liefert nun auch f&uuml;r
 *          elementare Boolean-Werte korrekte Werte, wenn die Defaultwerte in der Schreibweise nicht korrekt sind oder
 *          numerisch ausgedr&uuml;ckt werden.
 * @changed OLI 19.10.2010 - Erweiterung um die Methode <TT>isCodeClass(boolean)</TT> und deren Einbindung in die
 *          Pr&uuml;fung, ob Code f&uuml;r eine Klasse erzeugt werden soll. Einbindung des PTime-Typs.
 */

public abstract class AbstractCodeGenerator implements CodeGenerator {

	/* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
	private static Logger log = Logger.getLogger(AbstractCodeGenerator.class);

	/* Der Basispfad, in den die generierten Dateien geschrieben werden sollen. */
	private String path = null;

	/** Generiert ein AbstractGenerator-Objekt mit Defaultwerten. */
	public AbstractCodeGenerator() {
		super();
	}

	/**
	 * Liefert einen Getter-Namen zum Attribute (z. B. isDeleted oder getName).
	 *
	 * @param amd Die Attributmetadaten zu dem Attribute zu dem der Gettername generiert werden soll.
	 * @return Der Gettername zum Attribut.
	 * @throws NullPointerException Falls die Attributmetadaten als <TT>null</TT>-Referenz angegeben werden.
	 *
	 * @precondition amd != <TT>null</TT>
	 *
	 * @changed OLI 21.09.2009 - Hinzugef&uuml;gt.
	 */
	public String getAccessorName(AttributeMetaData amd) {
		assert amd != null : "Method getAccessorName does not work with null attribute.";
		if (amd.getJavaType().equals("boolean")) {
			return "is".concat(amd.getName());
		}
		return "get".concat(amd.getName());
	}

	/**
	 * Generiert einen Block von privaten Attributen anhand der Klassenmetadaten.
	 *
	 * @param cmd Die Klassenmetadaten, anhand derer der Attributblock generiert werden soll.
	 * @return Ein Codefragment mit den privaten Attributdefinitionen zur Klasse.
	 * @throws NullPointerException Falls die Klassenmetadaten als <TT>null</TT>-Pointer &uuml;bergeben werden.
	 *
	 * @precondition cmd != <TT>null</TT>
	 */
	public String getAttributeDefinition(ClassMetaData cmd) {
		assert cmd != null : "class meta data can not be null for generating an attribute " + "definition block.";
		AttributeMetaData amd = null;
		int i = 0;
		int len = 0;
		List<AttributeMetaData> amds = cmd.getAttributes();
		SortedVector<String> lines = new SortedVector<String>(new AlphabeticalStringComparator());
		StringBuffer sb = null;
		for (i = 0, len = amds.size(); i < len; i++) {
			amd = amds.get(i);
			sb = new StringBuffer();
			sb.append("    private ").append(amd.getJavaType()).append(" ");
			sb.append(amd.getName().toLowerCase());
			if ((amd.getDefaultValue() != null) && !amd.getDefaultValue().equals("")) {
				sb.append(" = ");
				if (amd.getJavaType().equals("boolean")) {
					if (amd.getDefaultValue().equals("0")) {
						sb.append("false");
					} else {
						sb.append("true");
					}
				} else if (amd.getJavaType().endsWith("PDate") || amd.getJavaType().endsWith("PTimestamp")
						|| amd.getJavaType().endsWith("LongPTimestamp")) {
					sb.append("null");
				} else {
					sb.append(amd.getDefaultValue());
				}
			} else if ((amd.getDefaultValue() == null) && (!isElementaryType(amd.getJavaType()))) {
				sb.append(" = null");
			}
			sb.append(";\n");
			lines.add(sb.toString());
		}
		sb = new StringBuffer();
		for (i = 0, len = lines.size(); i < len; i++) {
			sb.append(lines.get(i));
		}
		return sb.toString();
	}

	@Deprecated
	/**
	 * Generiert eine Liste mit den durch Kommata abgesetzten Namen der Attribute der Klasse.
	 *
	 * @param cmd Die Klassenmetadaten, anhand derer der Attributblock generiert werden soll.
	 * @return Eine Liste mit den durch Kommata abgesetzten Namen der Attribute der Klasse.
	 * @throws NullPointerException Falls die Klassenmetadaten als <TT>null</TT>-Pointer &uuml;bergeben werden.
	 * @precondition cmd != <TT>null</TT>
	 *
	 * @deprecated OLI 30.09.2009 - Bitte die gleichnamige Methode aus der Utility-Klasse <TT>gengen.util.SQLUtil</TT>
	 *             nutzen.
	 */
	public String getAttributeNameList(ClassMetaData cmd) throws NullPointerException {
		assert cmd != null : "class meta data can not be null for generating an attribute " + "definition block.";
		return gengen.util.SQLUtil.getAttributeNameList(cmd);
	}

	/**
	 * Liefert den Basispfad, unter dem die generierten Artefakte gespeichert werden sollen.
	 *
	 * @return Der Basispfad, unter dem die generierten Artefakte gespeichert werden sollen.
	 */
	public String getBasePath() {
		return this.path;
	}

	/**
	 * Liefert einen String zur Angabe des Defaultwertes zum angegebenen Attribut.
	 *
	 * @param amd Die Attributmetadaten, zu denen der Defaultwert geliefert werden soll.
	 * @return Ein String mit dem Defaultwert zu den angegebenen Attributmetadaten zum Einf&uuml;gen in den generierten
	 *         Code. F&uuml;r Attribute mit <TT>null</TT> als Defaultwert wird der String 'null' zur&uuml;ckgegeben.
	 *         String werden mit Anf&uuml;hrungszeichen zur&uuml;ckgeliefert. <BR>
	 *         F&uuml;r Attribute vom Typ <TT><B>boolen</B></TT> gilt: ein Defaultwert von "0" oder "false" (Gro&szlig;-
	 *         und Kleinschreibung wird nicht beachtet) ergibt den String "false"; alles andere den String "true".
	 *
	 * @changed OLI 04.10.2009 - Hinzugef&uuml;gt.
	 * @changed OLI 07.10.2009 - Ber&uuml;cksichtigung von elementaren Boolean-Werten.
	 */
	public String getDefaultValueString(AttributeMetaData amd) {
		boolean isElementaryBoolean = amd.getJavaType().equals("boolean");
		String s = amd.getDefaultValue();
		if (isElementaryBoolean) {
			return (s.equals("0") || s.equalsIgnoreCase("false") ? "false" : "true");
		} else if (s == null) {
			return "null";
		}
		return s;
	}

	/**
	 * Liefert ein Session-Tag f&uuml;r die angegebene Klasse zur Integration in eine + EJB-JAR-XML-Datei.
	 *
	 * @param cmd    Die Metadaten der Klasse, zu dem das Session-Tag erzeugt werden soll.
	 * @param bpn    Der Name des Basispackages, der den Projectpackages vorangestellt werden soll.
	 * @param indent Die Einr&uuml;ckung der &auml;&szlig;eren linken Linie des generierten codes.
	 * @return Ein Code-Fragment mit dem Session-Tag.
	 * @throws IllegalArgumentException Falls die Einr&uuml;ckung kleiner als null ist.
	 * @throws NullPointerException     Falls die Klassenmetadaten oder der Basispackagename als <TT>null</TT>-Referenz
	 *                                  &uuml;bergeben werden.
	 * @precondition cmd != <TT>null</TT>.
	 * @precondition indent &gt; 0.
	 * @precondition pn != <TT>null</TT>.
	 *
	 * @changed OLI 30.09.2009 - Hinzugef&uuml;gt.
	 */
	public String getEJBJarSessionTag(ClassMetaData cmd, String bpn, int indent)
			throws IllegalArgumentException, NullPointerException {
		String spc = this.getSpaces(indent);
		String s = spc + "<session>\n" + spc + "    <description>$PROJECTTOKEN$MasterDataInterface$CLASSNAME$"
				+ "</description>\n" + spc + "    <ejb-name>$PROJECTTOKEN$MasterDataInterface$CLASSNAME$</ejb-name>\n"
				+ spc + "    <home>$PN$.scheme.ejb.interfaces.$PROJECTTOKEN$MasterData"
				+ "Interface$CLASSNAME$Home</home>\n" + spc
				+ "    <remote>$PN$.scheme.ejb.interfaces.$PROJECTTOKEN$Master"
				+ "DataInterface$CLASSNAME$Remote</remote>\n" + spc
				+ "    <local-home>$PN$.scheme.ejb.interfaces.$PROJECTTOKEN$Master"
				+ "DataInterface$CLASSNAME$LocalHome</local-home>\n" + spc
				+ "    <local>$PN$.scheme.ejb.interfaces.$PROJECTTOKEN$MasterData"
				+ "Interface$CLASSNAME$LocalRemote</local>\n" + spc
				+ "    <ejb-class>$PN$.scheme.ejb.beans.$PROJECTTOKEN$MasterData"
				+ "Interface$CLASSNAME$Bean</ejb-class>\n" + spc + "    <session-type>Stateless</session-type>\n" + spc
				+ "    <transaction-type>Container</transaction-type>\n" + spc + "</session>\n";
		s = s.replace("$PN$", bpn);
		s = s.replace("$CLASSNAME$", cmd.getName());
		s = s.replace("$PROJECTTOKEN$", cmd.getModel().getProjectToken());
		return s;
	}

	/**
	 * Liefert ein Return-Statement f&uuml;r einen Vergleich der Attribute zweier Instanzen der angegebenen Klasse.
	 *
	 * @param cmd Die Klassenmetadaten, anhand derer der Attributblock generiert werden soll.
	 * @param vn  Der Name der Variablen, auf die sich der Vergleich beziehen soll.
	 * @return Eine Liste mit den durch Kommata abgesetzten Namen der Attribute der Klasse.
	 * @throws NullPointerException Falls die Klassenmetadaten oder der angegebene Variablenname als
	 *                              <TT>null</TT>-Pointer &uuml;bergeben werden.
	 *
	 * @precondition cmd != <TT>null</TT> &amp;&amp; vn != <TT>null</TT>
	 *
	 * @changed OLI 21.09.2009 - Hinzugef&uuml;gt.
	 */
	public String getEqualsReturnStatement(ClassMetaData cmd, String vn) throws NullPointerException {
		assert cmd != null : "class meta data can not be null for generating an equals return " + "statement.";
		assert vn != null : "null is not a valid name for a variable.";
		AttributeMetaData amd = null;
		int i = 0;
		int len = 0;
		List<AttributeMetaData> amds = cmd.getAttributes();
		String an = null;
		StringBuffer sb = new StringBuffer();
		for (i = 0, len = amds.size(); i < len; i++) {
			amd = amds.get(i);
			if (sb.length() > 0) {
				sb.append("\n                && ");
			}
			an = this.getAccessorName(amd);
			sb.append("Utl.equals(this.").append(an).append("(), ").append(vn).append(".").append(an).append("())");
			/*
			 * // OLI 07.10.2009 - Das war die alte, differenzierte Version. if
			 * (this.isElementaryType(amd.getJavaType())) {
			 * sb.append("(this.").append(an).append("() == ").append(vn).append(".").append(an ).append("())"); } else
			 * { sb.append("this.").append(an).append("().equals(").append(vn).append("." ).append(an).append("())"); }
			 */
		}
		return "        return ".concat(sb.toString()).concat(";\n");
	}

	/**
	 * Liefert ein Codefragment zum Einlesen des Attributs aus einem ResultSet (rs).
	 *
	 * @param amd    Die Attributmetadaten, zu dem das Codefragment gebildet werden soll.
	 * @param indent Die Anzahl der Zeichen, die das Codefragment einger&uuml;ckt werden soll.
	 * @param id     Die Id der Spalte des ResultSet, aus dem die Daten f&uuml;r das Attribut gelesen werden sollen.
	 * @return Ein Codefragment zum Einlesen des Attributs aus einem REsultSet (rs).
	 * @throws NullPointerException Falls die Attributmetadaten als <TT>null</TT>-Pointer &uuml;bergeben werden.
	 *
	 * @precondition amd != <TT>null</TT>
	 */
	public String getReadStatement(AttributeMetaData amd, int indent, int id) {
		assert amd != null : "attribute meta data can not be null for generating a read " + "statement";
		String defaultLineClose = id + "));\n";
		StringBuffer sb = new StringBuffer(this.getSpaces(indent)).append("o.set").append(amd.getName()).append("(");
		if (amd.getJavaType().equals("int")) {
			sb.append("rs.getInt(").append(defaultLineClose);
		} else if (amd.getJavaType().equals("long")) {
			sb.append("rs.getLong(").append(defaultLineClose);
		} else if (amd.getJavaType().equals("String")) {
			sb.append("rs.getString(").append(defaultLineClose);
		} else {
			// @todo OLI - Hier muss spaeter eine IllegalArgumentException geworfen werden.
			log.warn("building read statement is not supported for type \"" + amd.getJavaType()
					+ "\". Attribute will be ignored.");
			sb.append(" --- TODO --- );\n");
		}
		return sb.toString();
	}

	/**
	 * Generiert ein Java-Codefragment, mit dem f&uuml;r das angegbene Attribut von der angegebenen Position aus einem
	 * ResultSet gelesem werden kann. <BR>
	 * Die erzeugten Codefragmente sind auf die Nutzung in Kombination mit dem AbstractCodeGenerator und einer
	 * AbstractDBDataSource-Implementierung abgestimmt.
	 *
	 * <P>
	 * <I><B>Beispiel:</B> <TT>rs.getString(10)</TT> oder <TT>this.getNumber(rs, 42, 1L)</I>
	 *
	 * &#64;param amd Die Attributmetadaten, zu denen das Codefragment gebildet werden soll.
	 * &#64;param i Die Position (Spalte) innerhalb des ResultSets, aus der das Attribut seine
	 *         Daten lesen soll. Der &uuml;bergebene mu&szlig; gr&ouml;&szlig;er als null sein.
	 * &#64;param vn Der Name der ResultSet-Variablen, auf die im Code zugegriffen werden soll. Wird
	 *         hier eine <TT>null</TT>-Referenz &uuml;bergeben, so wird der Name "rs" f&uuml;r das ResultSet angenommen
	 * und in den Code generiert.
	 * @throws IllegalArgumentException Falls i < als 1 &uuml;bergeben wird.
	 * @throws NullPointerException     Falls die Attributmetadaten als <TT>null</TT>-Referenz angegebenen werden.
	 * @precondition amd != <TT>null</TT>.
	 * @precondition i &gt; 0.
	 *
	 * @changed OLI 01.10.2009 - Hinzugef&uuml;gt.
	 */
	public String getRSGet(AttributeMetaData amd, int i, String vn)
			throws IllegalArgumentException, NullPointerException {
		assert amd != null : "method doesn't works with attribute meta data null reference.";
		assert i > 0 : "index for result set have to be 1 or more.";
		StringBuffer sb = null;
		if (i < 1) {
			throw new IllegalArgumentException("index for result set have to be 1 or more.");
		}
		if (vn == null) {
			vn = "rs";
		}
		sb = new StringBuffer();
		if (amd.getJavaType().equals("boolean")) {
			return sb.append("(").append(vn).append(".getInt(").append(i).append(") != 0)").toString();
		} else if (amd.getJavaType().equals("String")) {
			return sb.append(vn).append(".getString(").append(i).append(")").toString();
		} else if (amd.getJavaType().endsWith("LongPTimestamp")) {
			return sb.append("DBDataSourceUtil.getLongPTimestamp(").append(vn).append(", ").append(i).append(")")
					.toString();
		} else if (amd.getJavaType().endsWith("PDate")) {
			return sb.append("DBDataSourceUtil.getPDate(").append(vn).append(", ").append(i).append(")").toString();
		} else if (amd.getJavaType().endsWith("PTime")) {
			return sb.append("DBDataSourceUtil.getPTime(").append(vn).append(", ").append(i).append(")").toString();
		} else if (amd.getJavaType().endsWith("PTimestamp")) {
			return sb.append("DBDataSourceUtil.getPTimestamp(").append(vn).append(", ").append(i).append(")")
					.toString();
		} else if (gengen.util.SQLUtil.isElementaryNumberType(amd.getJavaType())) {
			return sb.append("DBDataSourceUtil.getNumber(").append(vn).append(", ").append(i).append(", ")
					.append((amd.getDefaultValue() != null
							? amd.getDefaultValue().toString() + gengen.util.SQLUtil.getTypeSuffix(amd.getJavaType())
							: "0L"))
					.append(")").toString();
		}
		log.info("type '" + amd.getJavaType() + "' (" + amd.getName() + ") can not be processed"
				+ " by AbstractCodeGenerator (method getRSGet(AttributeMetaData amd, int i, " + "String vn))");
		return "<<<<< MANUAL CODING REQUIERED (" + amd.getJavaType() + ") >>>>>";
	}

	/**
	 * Liefert ein Codefragment mit einem Setter, der das angegebene Attribut aus einer Stringvariablen bel&auml;dt.
	 *
	 * @param amd      Das Attribut, zu dem der Setter gebildet werden soll.
	 * @param vn       Der Name der Stringvariablen, aus der der Setter den Wert in dem Codefragment lesen soll.
	 * @param tsAsLong Wird diese Flagge gesetzt, so werden Zeitstempelsetter mit umgewandelten Longwerten (in
	 *                 entsprechenden Konstruktoren) aufgerufen.
	 * @return Ein Codefragment mit einem Setter zum Attribut, f&uuml;r den der &Uum;bergabeparameter aus einem String
	 *         konvertiert wird.
	 * @throws IllegaleArgumentException Falls der Name der Stringvariablen leer &uuml;bergeben wird.
	 * @throws NullPointerException      Falls die Attributmetadaten oder der Name der Stringvariablen als
	 *                                   <TT>null</TT>-Referenz &uuml;bergeben wird.
	 * @precondition amd != <TT>null</TT>.
	 * @precondition vn != <TT>null</TT> &amp;&amp; !vn.equals("").
	 *
	 * @changed OLI 04.10.2009 - Hinzugef&uuml;gt.
	 */
	public static String getSetterFromString(AttributeMetaData amd, String vn, boolean tsAsLong)
			throws IllegalArgumentException, NullPointerException {
		assert amd != null : "method doesn't works with null attribute meta data";
		assert vn != null : "method doesn't works with null variable name.";
		assert vn.length() > 0 : "empty string is not valid for variable names.";
		StringBuffer sb = null;
		if (vn.length() == 0) {
			throw new IllegalArgumentException("empty string is not valid for variable names.");
		}
		sb = new StringBuffer("set").append(amd.getName()).append("(");
		if (tsAsLong && (amd.getJavaType().endsWith("LongPTimestamp") || amd.getJavaType().endsWith("PDate")
				|| amd.getJavaType().endsWith("PTimestamp"))) {
			sb.append("new ").append(amd.getJavaType()).append("(Long.valueOf(").append(vn).append(")));");
		} else {
			sb.append(AbstractCodeGenerator.getWrapperType(amd.getJavaType())).append(".valueOf(").append(vn)
					.append("));");
		}
		return sb.toString();
	}

	/**
	 * Liefert einen String mit der angegebenen Zahl von Spaces.
	 *
	 * @param i Die Anzahl der Spaces, die der String enthalten soll.
	 * @return Ein String mit der angegebenen ANzahl von Spaces.
	 * @throws IllegalArgumentException Falls eine negative Anzahl von Spaces verlangt wird.
	 * @precondition i > 0
	 *
	 * @deprecated OLI 04.10.2009 - Bitte die Methode corentx.util.Str.spaces(int) benutzen.
	 */
	public String getSpaces(int i) {
		return corentx.util.Str.spaces(i);
	}

	/**
	 * Liefert gegebenenfalls den Namen eines Wrappertyps, sonst den angegebenen Typ.
	 *
	 * @param tn Der Name eine Javatyps.
	 * @return Ein Wrappertyp, falls es sich bei dem Javatyp um einen elementaren Typen handelt, den angegebenen Typen
	 *         sonst.
	 * @throws IllegalArgumentException Falls der Typname leer &uuml;bergeben wird.
	 * @throws NullPointerException     Falls der Typname als <TT>null</TT>-Referenz &uuml;bergeben wird.
	 * @precondition tn != <TT>null</TT> &amp;&amp; tn.length() > 0.
	 *
	 * @changed OLI 04.10.2009 - Hinzugef&uuml;gt.
	 */
	public static String getWrapperType(String tn) throws IllegalArgumentException, NullPointerException {
		assert tn != null : "type name null is not valid.";
		assert tn.length() > 0 : "empty type name is not valid.";
		if (tn.length() == 0) {
			throw new IllegalArgumentException("empty type name is not valid.");
		}
		if (tn.equals("boolean")) {
			return "Boolean";
		} else if (tn.equals("byte")) {
			return "Byte";
		} else if (tn.equals("char")) {
			return "Character";
		} else if (tn.equals("double")) {
			return "Double";
		} else if (tn.equals("float")) {
			return "Float";
		} else if (tn.equals("int")) {
			return "Integer";
		} else if (tn.equals("long")) {
			return "Long";
		} else if (tn.equals("short")) {
			return "Short";
		}
		return tn;
	}

	/**
	 * Pr&uuml;ft, ob Code f&uuml;r die Klasse erzeugt werden soll. Die Methode sollte &uuml;berschrieben, falls eine
	 * Einschr&auml;nkung bei der Erzeugung des Codes erw&uuml;nscht ist. Standardm&auml;&szlig;ig wird der Wert
	 * <TT>true</TT> zur&uuml;ckgeliefert.
	 *
	 * @param cmd Die Klassenmetadaten, zu denen die Pr&uuml;fung stattfinden soll.
	 * @return <TT>true</TT>, falls der Code f&uuml;r die Klasse erzeugt werden soll, <TT>false</TT> sonst.
	 * @throws NullPointerException Falls die Klassenmetadaten als <TT>null</TT>-Pointer &uuml;bergeben werden.
	 * @precondition cmd != <TT>null</TT>
	 *
	 * @changed OLI 19.10.2010 - Hinzugef&uuml;gt.
	 */
	public boolean isCodeClass(ClassMetaData cmd) throws NullPointerException {
		return true;
	}

	/**
	 * Pr&uuml;ft, ob der angegbene Java-Type ein elementarer Basistyp ist.
	 *
	 * @param typeName Der Name des zu pr&uuml;fenden Typs.
	 * @return <TT>true</TT>, wenn es sich bei dem Typen um einen atomaren Basistypen handelt (z. B. <TT>int</TT>,
	 *         <TT>boolean</TT> etc.) und <TT>false</TT>falls der angegebenen Typ ein Referenztyp ist.
	 * @throws NullPointerException Falls der angegebene Typname eine Null-Referenz ist.
	 * @precondition typeName != <TT>null</TT>
	 *
	 * @todo * - Es ist zu &uuml;berlegen, ob die Methode hier nicht vollkommen herausfliegt, verstatict wird oder
	 *       einfach so belassen wird (trotz Redundanz mit der Methode in der Klasse <TT>gengen.util.SQLUtil</TT> (OLI,
	 *       30.09.2009).
	 */
	public boolean isElementaryType(String typeName) throws NullPointerException {
		assert typeName != null : "type name can not be null for reference type check.";
		return gengen.util.SQLUtil.isElementaryType(typeName);
	}

	/**
	 * Tauscht ein paar Standardbezeichner in Bezug auf eine Klasse aus. Die g&uuml;tigen Bezeichner sind:
	 * 
	 * <PRE>
	 * $AUTHOR$ - Der Autor der Klasse. $CLASSNAME$ - Der Klassenname. $COMPLETECLASSNAME$ - Der vollst&auml;ndige
	 * Klassenname. $DATE$ - Das aktuelle Tagesdatum.
	 *
	 * @param source Der bisher erzeugte Source-Code zur Klasse.
	 * @param cmd    Die Klassenmetadaten mit den Informationen zum Austausch der Bezeichner.
	 * @return Der Source-Code mit den entsprechenden Ersetzungen.
	 * @throws NullPointerException Wenn der &uuml;bergebene Source-Code oder die Klassenmetadaten als Null-Referenz
	 *                              &uuml;bergeben werden.
	 *
	 * @precondition source != <TT>null</TT> &amp;&amp; cmd != <TT>null</TT>
	 */
	public StringBuffer replaceClassWildCards(StringBuffer source, ClassMetaData cmd) {
		assert source != null : "source code to replace can not be null.";
		assert cmd != null : "class meta data can not be null for identifier replacement.";
		String s = source.toString();
		s = s.replace("$AUTHOR$", cmd.getAuthor());
		s = s.replace("$CLASSNAME$", cmd.getName());
		s = s.replace("$COMPLETECLASSNAME$",
				(this.getCompleteClassName(cmd) == null ? "<null>" : this.getCompleteClassName(cmd)));
		s = s.replace("$DATE$", new PDate().toString());
		return new StringBuffer(s);
	}

	/**
	 * Tauscht ein paar Standardbezeichner in Bezug auf das Modell aus. Die g&uuml;tigen Bezeichner sind:
	 * 
	 * <PRE>
	 * $BASEPACKAGE$ - Der Name des Basispackages der Anwendung. $VENDOR$ - Der Herstellername (z. B. f&uuml;r
	 * Copyrightvermerke).
	 *
	 * @param source Der bisher erzeugte Source-Code zur Klasse.
	 * @param mmd    Die Modellmetadaten mit den Informationen zum Austausch der Bezeichner.
	 * @return Der Source-Code mit den entsprechenden Ersetzungen.
	 * @throws NullPointerException Wenn der &uuml;bergebene Source-Code oder die Klassenmetadaten als Null-Referenz
	 *                              &uuml;bergeben werden.
	 *
	 * @precondition source != <TT>null</TT> &amp;&amp; cmd != <TT>null</TT>
	 */
	public StringBuffer replaceModelWildCards(StringBuffer source, ModelMetaData mmd) {
		assert source != null : "source code to replace can not be null.";
		assert mmd != null : "model meta data can not be null for identifier replacement.";
		String s = source.toString();
		s = s.replace("$BASEPACKAGE$", mmd.getBasePackageName());
		s = s.replace("$PROJECTTOKEN$", mmd.getProjectToken());
		s = s.replace("$VENDOR$", mmd.getVendor());
		return new StringBuffer(s);
	}

	/**
	 * Setzt den Basispfad, unter dem die generierten Dateien gespeichert werden sollen auf den angegebenen Wert.
	 *
	 * @param path Der neue Basispfad f&uuml;r den Generator.
	 * @throws NullPointerException Falls der &uuml;bergebene Pfad eine Null-Referenz ist.
	 *
	 * @precondition path != <TT>null</TT>
	 */
	public void setBasePath(String path) {
		assert path != null : "new path can not be null.";
		path = path.replace("\\", "/");
		this.path = FileUtil.completePath(path);
	}

	/* Teilweise Implementierung des Interfaces CodeGenerator. */

	/**
	 * @changed OLI 07.10.2009 - Leere Codedateien werden nicht mehr geschrieben. Stattdessen gibt's eine Meldung im
	 *          Log.
	 * @changed OLI 19.10.2010 - M&ouml;glichkeit der Einschr&auml;nkung der Erzeugung des Codes.
	 */
	public void generate(ModelMetaData mmd) throws Exception {
		ClassMetaData cmd = null;
		int i = 0;
		int len = 0;
		List<ClassMetaData> cmds = mmd.getClasses();
		String baseCodePath = FileUtil.completePath(Utl.getProperty("gengen.generator.base.path", "tmp/")).replace("~",
				System.getProperty("user.home"));
		String code = null;
		String fn = null;
		String packagePath = null;
		this.setBasePath(baseCodePath);
		for (i = 0, len = cmds.size(); i < len; i++) {
			cmd = cmds.get(i);
			if (this.isCodeClass(cmd)) {
				packagePath = FileUtil.completePath(cmd.getPackageName().replace(".", "/"));
				if (this.getPackageName() != null) {
					packagePath = packagePath.concat(FileUtil.completePath(this.getPackageName().replace(".", "/")));
				}
				new File(this.getBasePath() + packagePath).mkdirs();
				fn = this.getBasePath() + packagePath + this.getCompleteClassName(cmd) + ".java";
				code = this.generate(cmd);
				if (code.length() == 0) {
					log.warn("code file for class " + cmd.getName() + " has not been generated!");
				} else {
					log.info("writing to file " + fn);
					FileUtil.writeTextToFile(fn, false, code);
				}
			}
		}
	}

	public String getCompleteClassName(ClassMetaData cmd) {
		return cmd.getName();
	}

	public String getPackageName() {
		return null;
	}

}
