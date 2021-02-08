/*
 * LockDjinn.java
 *
 * 09.10.2007
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db.xs;


import corent.base.*;

import javax.swing.*;


/**
 * Mit Hilfe des LockDjinns kann gew&auml;hrleistet werden, das Locks, die an einen 
 * DBFactoryController weitergegeben werden, in einer konformen Art gebildet werden.
 *
 * @author
 *     O.Lieshoff
 *
 * @changed
 *     OLI 09.10.2007 - Erstellt.
 *     <P>OLI 01.04.2009 - Korrektur der Lock-Routine: Einbeziehen von <TT>isLockable()</TT> und
 *             <TT>getLockString()</TT>.
 *     <P>
 *
 */

public class LockDjinn {

    private DBFactoryController dfc = null;
    private JFrame frame = null;
    private String userlogin = null;

    /**
     * Beschw&ouml;rt ;o) einen LockDjinn mit den angegebenen Parametern.
     *
     * @param dfc Der DBFactoryController, auf dem der LockDjinn die Locks anfragen soll.
     */
    public LockDjinn(DBFactoryController dfc) {
        this(dfc, null, null);
    }

    /**
     * Beschw&ouml;rt ;o) einen LockDjinn mit den angegebenen Parametern.
     *
     * @param dfc Der DBFactoryController, auf dem der LockDjinn die Locks anfragen soll.
     * @param userlogin Der Userloginname des angemeldeten Benutzers.
     */
    public LockDjinn(DBFactoryController dfc, String userlogin) {
        this(dfc, userlogin, null);
    }

    /**
     * Beschw&ouml;rt ;o) einen LockDjinn mit den angegebenen Parametern.
     *
     * @param dfc Der DBFactoryController, auf dem der LockDjinn die Locks anfragen soll.
     * @param userlogin Der Userloginname des angemeldeten Benutzers.
     * @param frame Der Frame zu dem sich ein eventueller Hinweisdialog modal verhalten soll.
     */
    public LockDjinn(DBFactoryController dfc, String userlogin, JFrame frame) {
        super();
        this.dfc = dfc;
        this.frame = frame;
        this.userlogin = userlogin;
    }

    /**
     * Lockt das &uuml;bergebene Objekt im DBFactoryController.
     *
     * @param o Das Objekt, das gelockt werden soll.
     * @return <TT>true</TT>, wenn das Objekt gelockt werden konnte, <TT>false</TT> sonst.
     *
     * @changed
     *     OLI 01.04.2009 - Ber&uuml;cksichtigung der Methoden <TT>isLockable()</TT> und
     *             <TT>getLockString</TT>.
     *     <P>
     *
     */
    public boolean lock(Object o) {
        boolean locked = false;
        Object lock = null;
        String locker = null;
        String user = null;
        if ((o instanceof Lockable) || (o instanceof HasKey)) {
            if ((o instanceof Lockable) && (!((Lockable) o).isLockable())) {
                return locked;
            }
            if (o instanceof HasKey) {
                lock = ((HasKey) o).getKey();
            }
            if (o instanceof Lockable) {
                lock = ((Lockable) o).getLockString();
            }
            try  {
                user = (this.userlogin == null ? System.getProperty(
                        "corent.db.xs.DBFactoryController.userlogin") : this.userlogin); 
                locker = this.dfc.lock(o.getClass(), lock, user + "@" + System.getProperty(
                        "hostname", "UNKNOWN"));
                if (locker != null) {
                    if (this.frame != null) {
                        JOptionPane.showMessageDialog(this.frame, 
                                Utl.GetProperty(
                                "corent.db.xs.LockDjinn.information.locked.text", 
                                "Der Datensatz wird bereits von $LOCKER bearbeitet!").replace(
                                "$LOCKER", locker), Utl.GetProperty(
                                "corent.db.xs.LockDjinn.information.locked.header", "Sperre"), 
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    locked = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return locked;
    }

    /**
     * Hebt das Lock f&uuml;r das angegebene Objekt auf, falls es sich im Besitz des 
     * angemeldeten Benutzers befindet.
     *
     * @param o Das Objekt, dessen Lock aufgehoben werden soll.
     * @return <TT>true</TT> falls das Objekt f&uuml;r den angegebenen Benutzer entsperrt werden
     *         konnte, sonst <TT>false</TT>.
     *
     * @changed
     *     OLI 01.04.2009 - Ber&uuml;cksichtigung der Methoden <TT>isLockable()</TT> und
     *             <TT>getLockString</TT>.
     *     <P>
     *
     */
    public boolean unlock(Object o) {
        boolean unlocked = false;
        Object lock = null;
        String user = null;
        if ((o instanceof Lockable) || (o instanceof HasKey)) {
            if ((o instanceof Lockable) && (!((Lockable) o).isLockable())) {
                return unlocked;
            }
            if (o instanceof HasKey) {
                lock = ((HasKey) o).getKey();
            }
            if (o instanceof Lockable) {
                lock = ((Lockable) o).getLockString();
            }
            try  {
                user = (this.userlogin == null ? System.getProperty(
                        "corent.db.xs.DBFactoryController.userlogin") : this.userlogin); 
                unlocked = this.dfc.unlock(o.getClass(), lock, user + "@" + System.getProperty(
                        "hostname", "UNKNOWN"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return unlocked;
    }

}
