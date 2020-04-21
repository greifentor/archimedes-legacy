/*
 * DialogSelectionEditorDjinnDBF.java
 *
 * 06.06.2005
 *
 * (c) by O.Lieshoff
 *
 */

package corent.djinn;


import corent.base.*;
import corent.db.*;
import corent.db.xs.*;
import corent.files.*;
import corent.gui.*;

import java.util.*;

import javax.swing.*;


/**
 * Diese Implementierung des SelectionEditorDjinns spielt sich in einem Dialog ab. F&uuml;r 
 * Anwendungen, die nur zum Manipulieren eines einzelnen Datensatzes erstellt werden sollen, ist
 * diese Variante die erste Wahl. Alternativ kann er auch genutzt werden, um 
 * InternalFrame-Applikationen zu umschiffen. Im Gegensatz zur Klasse 
 * <TT>FrameSelectionEditorDjinn</TT> versorgt sich dieser Djinn mit Daten aus einer 
 * DBFactory
 * <P>Der Frame arbeitet eng mit dem SelectionEditorDjinn auf Panel-Basis zusammen, in dem die 
 * eigentliche Arbeit getan wird.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 09.10.2007 - Umbau auf LockDjinns.
 *     <P>OLI 20.02.2008 - Arbeiten im Rahmen der Anpassung an die Funktion Neuanlage &amp;
 *             Selection.
 *     <P>OLI 26.02.2008 - Umstellung auf den EditorDjinnMode-Betrieb.
 *     <P>OLI 18.03.2008 - Korrektur des fehlenden L&ouml;schens bei Abbruch der Neuanlage.
 *     <P>OLI 05.01.2009 - Ber&uuml;cksichtigung der Objekt-R&uuml;ckgabe bei 
 *             Schreiboperationen.
 *     <P>OLI 12.01.2009 - Vorbereitung der Aktivierung des Speichern-Buttons.
 *     <P>OLI 22.04.2009 - Erweiterung um einen Aufruf, bei dem ein vordefinierter LockDjinn 
 *             an den Dialog &uuml;gerbenen werden kann.
 *     <P>
 *
 */

public class DialogSelectionEditorDjinnDBF extends JDialogWithInifile {

    /*
     * Diese Flagge wird gesetzt, wenn der Dialog mir dem Auswahlbutton beendet worden ist (nur
     * im selectiondialog-Modus).
     */
    private boolean selected = false;
    /*
     * Diese Flagge mu&szlig; gesetzt werden, wenn der Dialog aus Auswahldialog mi&szlig;braucht
     * wird. 
     */
    private boolean selectiondialog = false;
    /*
     * Diese Flagge mu&szlig; gesetzt werden, wenn der Dialog aus Auswahldialog mit 
     * Neuanlagefunktion mi&szlig;braucht wird. 
     */
    private boolean selectionandcreatedialog = false;
    /*
     * Diese Flagge mu&szlig; gesetzt werden, wenn die EditorDjinns im splitted-Modus erzeugt 
     * werden sollen.
     */
    private boolean splitted = false;
    /* Die Klasse zur Auswahl der richtigen DBFactory innerhalb des Controllers. */
    private Class bp = null;
    /* Referenz auf das aktuell behandelte Element. */
    private Editable edi = null;
    /* Referenz auf das Selektionspanel. */
    private DefaultSelectionDjinnPanel panel = null;
    /* Referenz auf den DBFactoryController, der als Datenlieferant arbeiten soll. */
    private DBFactoryController dfc = null;
    /* Referenz auf den f&uuml;r den Djinn g&uuml;ltigen LockDjinn. */
    private LockDjinn lockDjinn = null;
    /* Die Liste der ausgew&auml;hlten Elemente (nur im selectiondialog-Modus. */
    private Vector selection = new Vector();

    /**
     * Generiert einen SelectionDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param parent Der JFrame, von dem aus der Dialog aufgerufen worden ist. 
     * @param titel Der Titel des Frames.
     * @param dfc Der DBFactoryController, dessen zur cls passenden Daten bearbeiten werden 
     *         sollen.
     * @param cls Die Klasse der Objekte, deren Daten in dem Djinn bearbeitet werden sollen.
     */ 
    public DialogSelectionEditorDjinnDBF(JFrame parent, String titel, DBFactoryController dfc, 
            Class cls) {
        this(parent, titel, dfc, cls, null, true, false, false, false, null);
    }

    /**
     * Generiert einen SelectionDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param parent Der JFrame, von dem aus der Dialog aufgerufen worden ist. 
     * @param titel Der Titel des Frames.
     * @param dbfc Der DBFactoryController, dessen zur cls passenden Daten bearbeiten werden 
     *         sollen.
     * @param cls Die Klasse der Objekte, deren Daten in dem Djinn bearbeitet werden sollen.
     * @param ini Eine Inidatei zu Speicherung der Fensterdaten.
     * @param selector Diese Flagge mu&szlig; gesetzt werden, wenn die Eingabezeile zur Vorgabe 
     *         von Suchkriterien eingeblendet werden soll.
     * @param split Wird diese Flagge gesetzt, so werden die EditorDjinns zur gew&auml;hlten
     *         Aktion im split-Modus erzeugt.
     */ 
    public DialogSelectionEditorDjinnDBF(JFrame parent, String titel, DBFactoryController dbfc, 
            Class cls, Inifile ini, boolean selector, boolean split) {
        this(parent, titel, dbfc, cls, ini, selector, split, false, false, null);
    }

    /**
     * Generiert einen SelectionDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param parent Der JFrame, von dem aus der Dialog aufgerufen worden ist. 
     * @param titel Der Titel des Frames.
     * @param dbfc Der DBFactoryController, dessen zur cls passenden Daten bearbeiten werden 
     *         sollen.
     * @param cls Die Klasse der Objekte, deren Daten in dem Djinn bearbeitet werden sollen.
     * @param ini Eine Inidatei zu Speicherung der Fensterdaten.
     * @param selector Diese Flagge mu&szlig; gesetzt werden, wenn die Eingabezeile zur Vorgabe 
     *         von Suchkriterien eingeblendet werden soll.
     * @param split Wird diese Flagge gesetzt, so werden die EditorDjinns zur gew&auml;hlten
     *         Aktion im split-Modus erzeugt.
     * @param pselectiondialog Diese Flagge mu&szlig; gesetzt werden, wenn der Dialog zur 
     *         Auswahl von Obkjekten dient und nicht direkt als Editorwurzel.
     */
    public DialogSelectionEditorDjinnDBF(JFrame parent, String titel, DBFactoryController dbfc, 
            Class cls, Inifile ini, boolean selector, boolean split, boolean pselectiondialog) {
        this(parent, titel, dbfc, cls, ini, selector, split, pselectiondialog, false, null);
    }

    /**
     * Generiert einen SelectionDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param parent Der JFrame, von dem aus der Dialog aufgerufen worden ist. 
     * @param titel Der Titel des Frames.
     * @param dbfc Der DBFactoryController, dessen zur cls passenden Daten bearbeiten werden 
     *         sollen.
     * @param cls Die Klasse der Objekte, deren Daten in dem Djinn bearbeitet werden sollen.
     * @param ini Eine Inidatei zu Speicherung der Fensterdaten.
     * @param selector Diese Flagge mu&szlig; gesetzt werden, wenn die Eingabezeile zur Vorgabe 
     *         von Suchkriterien eingeblendet werden soll.
     * @param split Wird diese Flagge gesetzt, so werden die EditorDjinns zur gew&auml;hlten
     *         Aktion im split-Modus erzeugt.
     * @param pselectiondialog Diese Flagge mu&szlig; gesetzt werden, wenn der Dialog zur 
     *         Auswahl von Obkjekten dient und nicht direkt als Editorwurzel.
     * @param scd Diese Flagge ist zu setzen, um den Dialog als Auswahldialog mit 
     *         Neuanlagefunktion zu mi&szlig;brauchen.
     *
     * @changed
     *     OLI 18.03.2008 - Korrektur des L&ouml;schens von Datens&auml;tzen nach Abbruch einer
     *             Neuanlage.
     *     <P>
     *
     */
    public DialogSelectionEditorDjinnDBF(JFrame parent, String titel, DBFactoryController dbfc, 
            Class cls, Inifile ini, boolean selector, boolean split, boolean pselectiondialog, 
            boolean scd) {
        this(parent, titel, dbfc, cls, ini, selector, split, pselectiondialog, scd, null);
    }

    /**
     * Generiert einen SelectionDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param parent Der JFrame, von dem aus der Dialog aufgerufen worden ist. 
     * @param titel Der Titel des Frames.
     * @param dbfc Der DBFactoryController, dessen zur cls passenden Daten bearbeiten werden 
     *         sollen.
     * @param cls Die Klasse der Objekte, deren Daten in dem Djinn bearbeitet werden sollen.
     * @param ini Eine Inidatei zu Speicherung der Fensterdaten.
     * @param selector Diese Flagge mu&szlig; gesetzt werden, wenn die Eingabezeile zur Vorgabe 
     *         von Suchkriterien eingeblendet werden soll.
     * @param split Wird diese Flagge gesetzt, so werden die EditorDjinns zur gew&auml;hlten
     *         Aktion im split-Modus erzeugt.
     * @param pselectiondialog Diese Flagge mu&szlig; gesetzt werden, wenn der Dialog zur 
     *         Auswahl von Obkjekten dient und nicht direkt als Editorwurzel.
     * @param scd Diese Flagge ist zu setzen, um den Dialog als Auswahldialog mit 
     *         Neuanlagefunktion zu mi&szlig;brauchen.
     * @param lockDjinn Ein vordefinierter LockDjinn oder <TT>null</TT>, wenn ein Standard 
     *         LockDjinn genutzt werden soll.
     *
     * @changed
     *     OLI 22.04.2009 - Erweiterung um die &Uuml;bergabe des LockDjinns. Daher prinzipiell
     *             eine Neuanlage.
     *     <P>
     *
     */
    public DialogSelectionEditorDjinnDBF(JFrame parent, String titel, DBFactoryController dbfc, 
            Class cls, Inifile ini, boolean selector, boolean split, boolean pselectiondialog, 
            boolean scd, LockDjinn lockDjinn) {
        super(parent, titel, true, ini);
        final JDialog dialog = this;
        final Inifile inf = ini;
        this.setIdentifier(this.getClass().toString() + "-" + cls.toString());
        this.bp = cls;
        this.dfc = dbfc;
        if (lockDjinn == null) {
            this.lockDjinn = new LockDjinn(dbfc, null, parent);
        } else {
            this.lockDjinn = lockDjinn;
        }
        this.selectiondialog = pselectiondialog;
        this.selectionandcreatedialog = scd;
        this.splitted = split;
        this.panel = new DefaultSelectionDjinnPanel(new DBFListViewComponentFactory(this.dfc, 
                this.bp, ini, null, parent), new DefaultSelectionEditorDjinnButtonFactory(), 
                selector);
        this.panel.addSelectionDjinnListener(new SelectionDjinnAdapter() {
            public void selectionDone(Vector vselected) {
                if (selectiondialog) {
                    doSelected(vselected);
                    setSelected(true);
                    dispose();
                    setVisible(false);
                } else {
                    edi = (Editable) vselected.elementAt(0);
                    new DialogEditorDjinn(dialog, StrUtil.FromHTML(
                            "&Auml;ndern"), edi, true, edi instanceof HistoryWriter, inf, 
                            splitted, lock(edi), EditorDjinnMode.EDIT) {
                        public void doChanged(boolean saveOnly) {
                            try {
                                // this.setEditable((Editable) dfc.write(this.getEditable()));
                                Object o = dfc.write(this.getEditable());
                                if (this.getEditable() instanceof ChangeableThruSelectionDjinn)
                                        {
                                    ((ChangeableThruSelectionDjinn) this.getEditable()
                                            ).copyAfterWrite(o);
                                }
                                if (this.getEditable() instanceof Traceable) {
                                    ((Traceable) this.getEditable()).traceChanged();
                                }
                                if (!saveOnly) {
                                    unlock(this.getEditable());
                                }
                                panel.updateView();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        public void doDeleted() {
                            try {
                                dfc.remove(this.getEditable(), false);
                                if (this.getEditable() instanceof Traceable) {
                                    ((Traceable) this.getEditable()).traceDeleted();
                                }
                                unlock(this.getEditable());
                                panel.updateView();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        public void doDiscarded() {
                            try {
                                unlock(this.getEditable());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                }
            }
            public void selectionDuplicated(Vector selected) {
                Editable edi = null;
                try {
                    edi = (Editable) dfc.duplicate(selected.elementAt(0)); 
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DialogEditorDjinn ded = new DialogEditorDjinn(dialog, "Duplikat", edi, false, 
                        false, inf, splitted, (Boolean.getBoolean(
                        "corent.djinn.SelectionEditorDjinn.suppress.lock.at.create") ? false :
                        lock(edi)), EditorDjinnMode.DUPLICATE) {
                    public boolean isObjectUnique(Persistent p) {
                        try {
                            return dfc.isUnique(p);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                    public void doChanged(boolean saveOnly) {
                        try {                            
                            // this.setEditable((Editable) dfc.write(this.getEditable()));
                            Object o = dfc.write(this.getEditable());
                            if (this.getEditable() instanceof ChangeableThruSelectionDjinn) {
                                ((ChangeableThruSelectionDjinn) this.getEditable()
                                        ).copyAfterWrite(o);
                            }
                            if (this.getEditable() instanceof Traceable) {
                                ((Traceable) this.getEditable()).traceDuplicated();
                            }
                            if (!saveOnly) {
                                unlock(this.getEditable());
                            }
                            panel.updateView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    public void doDiscarded() {
                        try {
                            unlock(this.getEditable());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                if (!ded.isCanceled() && selectionandcreatedialog) {
                    doSelected(ded.getEditable());
                }
            }
            public void elementCreated() {
                Editable edi = null; 
                try {
                    edi = (Editable) dfc.generate(bp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DialogEditorDjinn ded = new DialogEditorDjinn(dialog, "Neuanlage", edi, false, 
                        false, inf, splitted, (Boolean.getBoolean(
                        "corent.djinn.SelectionEditorDjinn.suppress.lock.at.create") ? false :
                        lock(edi)), EditorDjinnMode.CREATE) {
                    public boolean isObjectUnique(Persistent p) {
                        try {
                            return dfc.isUnique(p);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                    public void doChanged(boolean saveOnly) {
                        try {
                            Object o = dfc.write(this.getEditable());
                            if (this.getEditable() instanceof ChangeableThruSelectionDjinn) {
                                ((ChangeableThruSelectionDjinn) this.getEditable()
                                        ).copyAfterWrite(o);
                            }
                            if (this.getEditable() instanceof Traceable) {
                                ((Traceable) this.getEditable()).traceCreated();
                            }
                            if (!saveOnly) {
                                unlock(this.getEditable());
                            }
                            panel.updateView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    public void doDiscarded() {
                        try {
                            /*
                            dfc.remove(this.getEditable(), true);
                            panel.updateView();
                            unlock(this.getEditable());
                            */
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                if (!ded.isCanceled() && selectionandcreatedialog) {
                    doSelected(ded.getEditable());
                } else if (ded.isCanceled()) {
                    try {
                        dfc.remove(ded.getEditable(), true);
                        panel.updateView();
                        unlock(ded.getEditable());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            public void djinnClosed() {
                // unlock(edi);
                dispose();
                setVisible(false);
            }
        });
        this.setContentPane(this.panel);
        this.pack();
        this.setVisible(true);
        this.panel.initView();
    }

    /**
     * F&uuml;gt den &uuml;bergebenen SelectionDjinnListener an die Liste der den Djinn 
     * abh&ouml;renden Listener an.
     *
     * @param listener Der anzuf&uuml;gende Listener.
     */
    public void addSelectionDjinnListener(SelectionDjinnListener listener) {
        this.panel.addSelectionDjinnListener(listener);
    }

    /**
     * L&ouml;scht den &uuml;bergebenen SelectionDjinnListener aus der Liste der den Djinn 
     * abh&ouml;renden Listener an.
     *
     * @param listener Der zu l&ouml;schende Listener.
     */
    public void removeSelectionDjinnListener(SelectionDjinnListener listener) {
        this.panel.removeSelectionDjinnListener(listener);
    }

    /**
     * Diese Methode wird aufgerufen, wenn der Benutzer den L&ouml;schen-Button im 
     * &Auml;ndern-Dialog dr&uuml;ckt.
     *
     * @param obj Das Objekt, welches gel&ouml;scht werden soll.
     * @return <TT>true</TT>, wenn das Objekt zum Abschu&szlig; freigegeben werden soll.
     */
    public boolean permitDelete(Object obj) {
        return true;
    }

    // REQUEST OLI 04.06.2010 - Ist diese Methode wirklich ueberfluessig ?!?
    /*
    private void requestFocusThreaded() {
        (new Thread() {
            public void run() {
                toFront();
                requestFocus();
            }
        }).start();
    }
    */

    private boolean lock(Object o) {
        return this.lockDjinn.lock(o);
    }

    private boolean unlock(Object o) {
        return this.lockDjinn.unlock(o);
    }

    /** 
     * Diese Methode wird aufgerufen, wenn &uuml;ber den Dialog eine 
     * Neuanlage-/Auswahl-Kombination stattgefunden hat.
     *
     * @param value Das neuangelegte und ausgew&auml;hlte Objekt.
     */
    public void doSelected(Editable value) {
        this.selection.addElement(value);
        this.setSelected(true);
        this.dispose();
        this.setVisible(false);
    }

    /** 
     * Diese Methode wird aufgerufen, wenn &uuml;ber den Dialog eine Objekt-Auswahl 
     * stattgefunden hat.
     *
     * @param values Die ausgew&auml;hlten Objekte.
     */
    public void doSelected(Vector values) {
        for (int i = 0, len = values.size(); i < len; i++) {
            this.selection.addElement(values.elementAt(i));
        }
    }

    /** @return Das Selektionsergebnis vor dem Schliessen des Dialoges. */
    public Vector getSelection() {
        return this.selection;
    }

    /** @return <TT>true</TT>, wenn der Dialog mit dem Auswahl-Button beendet worden ist. */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * Setzt die Selected-Flagge des Dialoges auf den angegebenen Wert.
     *
     * @param b Der neue Wert f&uuml;r die Selected-Flagge des Dialoges.
     *
     * @changed
     *     OLI 20.02.2008 - Hinzugef&uuml;gt.
     *
     */
    public void setSelected(boolean b) {
        this.selected = b;
    }

}
