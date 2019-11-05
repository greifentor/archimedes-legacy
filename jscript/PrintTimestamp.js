/*
 * Schreibt das aktuelle Datum mit Uhrzeit auf die Konsole. Das Script dient als Demo f&uuml;r
 * den Einsatz von externen Klassen.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 15.08.2011 - Hinzugef&uuml;gt.
 */

load("nashorn:mozilla_compat.js");
importPackage(Packages.corentx.dates);
importPackage(java.lang);

System.out.println(new PTimestamp());

