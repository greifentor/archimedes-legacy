/*
 * AbstractMassiveListSelector.java
 *
 * 03.08.2005
 *
 * (c) by O.Lieshoff
 *
 */

package corent.gui;


import corent.base.*;
import corent.db.xs.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;


/**
 * Diese Klasse ist eine Musterimplementierung des MassiveListSelector-Interfaces. Sie bietet 
 * alle visuellen Komponenten des Selektors, erfordert jedoch eine Implementierung f&uuml;r den
 * eigentlichen Auswahlproze&szlig;.
 * <P>&Uuml;ber die Property <TT>corent.gui.AbstractMassiveListSelector.deactivated.token</TT>
 * kann eine alternative Kennung f&uuml;r gel&ouml;schte Deactivatables gesetzt werden.
 * <P>Mit Hilfe der Property 
 * <TT>corent.gui.AbstractMassiveListSelector.display.[foreground|background].color.[disabled
 * |enabled]</TT> k&ouml;nnen die Farben f&uuml;r das Displayfeld des MassiveListSelectors 
 * angepa&szlig;t werden.
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 30.01.2008 - Erweiterung um das &Uuml;berschreiben der Methoden zur Anbindung von
 *             Mouse- und KeyListenern. Die angebundenen Listener werden nun an alle Komponenten
 *             des Selectors angebunden (bzw. von ihnen abgenabelt).
 *     <P>OLI 17.02.2008 - Implementierung der Strong-Disabled-Logik.
 *     <P>OLI 31.03.2008 - Der Null-String wird nun immer wieder aus der zust&auml;ndigen 
 *             ComponentFactory ausgelesen. Dies ist im Rahmen des Einbaus der Ressourcendateien
 *             notwendig gewesen. Das Attribut <TT>nullstring</TT> habe ich entfernt. Nebenbei
 *             konnte ich die Schriftfarbe bei Abblendung auf schwarz setzen.
 *     <P>
 *
 */

abstract public class AbstractMassiveListSelector extends JPanel implements MassiveListSelector 
        {

    /* Flagge f&uuml;r die harte Abblendung. */
    private boolean strongdisabled = false;
    /* Der L&ouml;schen-Button zum Selektor. */
    private JButton buttonClear = null;
    /* Der Auswahl-Button zum Selektor. */
    private JButton buttonSelect = null;
    /* 
     * Das Anzeigefeld, in dem Informationen zum aktuell ausgew&auml;hlten Objekt angezeigt 
     * werden sollen.
     */
    private JTextField textFieldAnzeige = null;
    /* Das ausgew&auml;hlte Objekt. */
    private Object selected = null;
    /* Eine Liste mit den MassiveListSelectorListenern, die die Komponente abhorchen. */
    private Vector listener = new Vector();

    /** Referenz auf die genutzte MassiveListSelectorComponentFactory. */
    protected MassiveListSelectorComponentFactory mlscf = null;

    /** 
     * Generiert einen AbstractMassiveListSelector anhand der angegebenen Parameter.
     *
     * @param mlscf Die MassiveListSelectorComponentFactory, aus der die Komponente ihre 
     *     Bestandteile beziehen soll.
     * @param selected Ein gegebenenfalls vorausgew&auml;hltes Objekt.
     *
     * @changed
     *     OLI 31.03.2008 - Einbau der Hintergrundfarbe schwarz f&uuml;r abgeblendete 
     *             Komponenten.
     *
     */
    public AbstractMassiveListSelector(MassiveListSelectorComponentFactory mlscf, 
            Object selected) {
        super(new BorderLayout(Constants.HGAP, Constants.VGAP));
        this.mlscf = mlscf;
        KeyAdapter ka = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    doButtonClear();
                } else if (e.getKeyCode() == KeyEvent.VK_F3) {
                    doButtonSelect();
                }
            }
        };
        this.addKeyListener(ka);
        this.textFieldAnzeige = mlscf.createDisplayField();
        this.textFieldAnzeige.addKeyListener(ka);
        this.buttonClear = mlscf.createButtonClear();
        this.buttonClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doButtonClear();
            }
        });
        this.buttonClear.addKeyListener(ka);
        this.buttonSelect = mlscf.createButtonSelect();
        this.buttonSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doButtonSelect();
            }
        });
        this.buttonSelect.addKeyListener(ka);
        JPanel panelButtons = new JPanel(new GridLayout(1, 2, Constants.HGAP, Constants.VGAP));
        panelButtons.add(this.buttonClear);
        panelButtons.add(this.buttonSelect);
        this.add(this.textFieldAnzeige, BorderLayout.CENTER);
        this.add(panelButtons, BorderLayout.EAST);
        this.selected = selected;
        this.doUpdateDisplay();
    }

    /* Diese Methode wird aufgerufen, wenn der Benutzer den L&ouml;schen-Button dr&uuml;ckt. */
    protected void doButtonClear() {
        this.selected = null;
        this.doUpdateDisplay();
        this.fireSelectionCleared();
    }

    /* Diese Methode wird aufgerufen, wenn der Benutzer den Auswahl-Button dr&uuml;ckt. */
    protected void doButtonSelect() {
        this.selected = this.selectObject();
        this.doUpdateDisplay();
        this.fireSelectionAltered();
    }

    /* Aktualisiert die Anzeige im Anzeigefeld mit den Daten des selektierten Objektes. */
    protected void doUpdateDisplay() {
        Color colorBackground = ColorUtil.GetColor(System.getProperty(
                "corent.gui.AbstractMassiveListSelector.background.color." + (this.isEnabled() ?
                "enabled" : "disabled"), "white"));
        Color colorForeground = ColorUtil.GetColor(System.getProperty(
                "corent.gui.AbstractMassiveListSelector.foreground.color." + (this.isEnabled() ?
                "enabled" : "disabled"), "black"));
        String s = (this.selected == null ? this.mlscf.createNullString() 
                : this.selected.toString());
        if ((this.selected instanceof Deactivatable) && ((Deactivatable) this.selected
                ).isRemoved()) {
            s = this.getDeactivatedToken().concat(s);
        }
        this.textFieldAnzeige.setBackground(colorBackground);
        this.textFieldAnzeige.setForeground(colorForeground);
        this.textFieldAnzeige.setText(s);
    }

    /**
     * Liefert eine Kennung, die gel&ouml;schten Deactivatables vorangestellt wird.
     *
     * @return Die besagte Kennung.
     */
    protected String getDeactivatedToken() {
        return Utl.GetProperty("corent.gui.AbstractMassiveListSelector.deactivated.token", 
                "(Gel&ouml;scht) ");
    }

    /**
     * Diese Methode informiert die an der Komponente lauschenden Listener &uuml;ber eine 
     * &Auml;nderung des Selectorinhaltes.
     */
    public void fireSelectionAltered() {
        MassiveListSelectorEvent mlse = new MassiveListSelectorEvent(this, 
                MassiveListSelectorEventtype.SELECTION_ALTERED); 
        for (int i = 0, len = this.listener.size(); i < len; i++) {
            ((MassiveListSelectorListener) this.listener.elementAt(i)).selectionAltered(mlse);
        }
    }

    /**
     * Diese Methode informiert die an der Komponente lauschenden Listener &uuml;ber ein 
     * L&ouml;schen des Selectorinhaltes.
     */
    public void fireSelectionCleared() {
        MassiveListSelectorEvent mlse = new MassiveListSelectorEvent(this, 
                MassiveListSelectorEventtype.SELECTION_CLEARED); 
        for (int i = 0, len = this.listener.size(); i < len; i++) {
            ((MassiveListSelectorListener) this.listener.elementAt(i)).selectionCleared(mlse);
        }
    }


    /* Ueberschreiben von Methoden der Superklasse. */

    public void setEnabled(boolean b) {
        if (b && this.strongdisabled) {
            return;
        }
        this.buttonClear.setEnabled(b);
        this.buttonSelect.setEnabled(b);
        super.setEnabled(b);
    }


    /* Die abstrakten Methoden der Klasse. */

    /** 
     * Ein Aufruf dieser Methode mu&szlig; alle notwendigen Aktionen ausf&uuml;hren, um ein 
     * neues Objekt durch den Benutzer ausw&auml;hlen zu lassen. Das ausgew&auml;hlte Objekt 
     * wird aus Auswahl in die Komponente &uuml;bernommen. Wird kein neues Objekt 
     * ausgew&auml;hlt, so bleibt die Auswahl unver&auml;ndert.
     *
     * @return Das neuausgew&auml;hlte Objekt bzw. das alte, falls kein neues gew&auml;hlt 
     *     wurde.
     */
    abstract public Object selectObject();


    /* Ueberschreiben von Methoden der Superklasse. */

    public void addKeyListener(KeyListener ml) {
        super.addKeyListener(ml);
        if (this.buttonClear != null) {
            this.buttonClear.addKeyListener(ml);
        }
        if (this.buttonSelect != null) {
            this.buttonSelect.addKeyListener(ml);
        }
        if (this.textFieldAnzeige != null) {
            this.textFieldAnzeige.addKeyListener(ml);
        }
    }

    public void addMouseListener(MouseListener ml) {
        super.addMouseListener(ml);
        if (this.buttonClear != null) {
            this.buttonClear.addMouseListener(ml);
        }
        if (this.buttonSelect != null) {
            this.buttonSelect.addMouseListener(ml);
        }
        if (this.textFieldAnzeige != null) {
            this.textFieldAnzeige.addMouseListener(ml);
        }
    }

    public boolean hasFocus() {
        if ((this.buttonClear != null) && this.buttonClear.hasFocus()) {
            return true;
        } else if ((this.buttonSelect != null) && this.buttonSelect.hasFocus()) {
            return true;
        }
        return super.hasFocus();
    }

    public void removeKeyListener(KeyListener ml) {
        super.removeKeyListener(ml);
        if (this.buttonClear != null) {
            this.buttonClear.removeKeyListener(ml);
        }
        if (this.buttonSelect != null) {
            this.buttonSelect.removeKeyListener(ml);
        }
        if (this.textFieldAnzeige != null) {
            this.textFieldAnzeige.removeKeyListener(ml);
        }
    }

    public void removeMouseListener(MouseListener ml) {
        super.removeMouseListener(ml);
        if (this.buttonClear != null) {
            this.buttonClear.removeMouseListener(ml);
        }
        if (this.buttonSelect != null) {
            this.buttonSelect.removeMouseListener(ml);
        }
        if (this.textFieldAnzeige != null) {
            this.textFieldAnzeige.removeMouseListener(ml);
        }
    }


    /* Implementierung des Interfaces MassiveListSelector. */

    public void addMassiveListSelectorListener(MassiveListSelectorListener l) {
        if (!this.listener.contains(l)) {
            this.listener.addElement(l);
        }
    }

    public Object getSelected() {
        return this.selected;
    }

    public boolean isStrongDisabled() {
        return this.strongdisabled;
    }

    public void removeMassiveListSelectorListener(MassiveListSelectorListener l) {
        if (this.listener.contains(l)) {
            this.listener.remove(l);
        }
    }

    public void setNullString(String s) {
        if (this.mlscf != null) {
            this.mlscf.setNullString(s);
        }
    }

    public void setSelected(Object selected) {
        this.selected = selected;
        this.doUpdateDisplay();
    }

    public synchronized void setStrongDisabled(boolean b) {
        this.setEnabled(false);
        this.strongdisabled = b;
    }

}
