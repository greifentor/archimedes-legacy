/*
 * EditorFrameListener.java
 *
 * 30.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.generics;


import java.awt.*;

import baccara.events.*;


/**
 * An interface for special listeners which are listening to an editor frame.
 *
 * @param <ET> The type of the event which is listened to.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 30.05.2013 - Added.
 */

public interface EditorFrameListener<ET extends EditorFrameEvent<?, ? extends Window>>
        extends SimpleListener<ET> {
}