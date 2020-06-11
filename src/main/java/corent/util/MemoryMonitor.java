/*
 * @(#)MemoryMonitor.java	1.24 98/09/13
 *
 * Copyright 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Sun Microsystems, Inc. 
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered into with Sun.
 */

package corent.util;


import corent.base.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;


/**
 * Tracks Memory allocated & used, displayed in graph form.
 *
 * @changed OLI 05.09.2007 - &Auml;nderung der <TT>show()</TT>-Aufrufe in
 *         <TT>setVisible(true)</TT>-Aufrufe.
 * @changed OLI 19.09.2011 - Erm&ouml;glichen des Schliessens des MemoryMonitors durch Clicken
 *         des Schliessen-Buttons. Die statische show-Methode reicht nun eine
 *         <TT>Frame</TT>-Referenz zur&uuml;ck. Aufr&auml;men der Warnungen.
 */
public class MemoryMonitor extends JPanel {

    public MonitorComponent1 mc;
    public MonitorComponent2 mc2;

    public MemoryMonitor() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(new EtchedBorder(), "Memory Monitor"));
        mc = new MonitorComponent1();
        add(mc);
    }

    public MemoryMonitor(JPanel p) {
        mc2 = new MonitorComponent2(p);
        add(mc2);
    }

    public class MonitorComponent1 extends JPanel implements Runnable {
        public Thread thread;
        private int w, h;
        private BufferedImage bimg;
        private Graphics2D big;
        private Font font = new Font("Monospaced", Font.PLAIN, 14);
        private Runtime r = Runtime.getRuntime();
        private int columnInc;
        private int pts[];
        private int ptNum;
        private int ascent, descent;
        private Rectangle graphOutlineRect = new Rectangle();
        private Rectangle2D mfRect = new Rectangle2D.Float();
        private Rectangle2D muRect = new Rectangle2D.Float();
        private Line2D graphLine = new Line2D.Float();
        private Color graphColor = new Color(46, 139, 87);
        private Color mfColor = new Color(0, 100, 0);

        public MonitorComponent1() {
            setBackground(Color.black);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (thread == null) start(); else stop();
                }
            });
            start();
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(140,80);
        }

        @Override
        public void paint(Graphics g) {
            if (big == null) {
                return;
            }
            big.setBackground(getBackground());
            big.clearRect(0,0,w,h);
            float freeMemory = (float) r.freeMemory();
            float totalMemory = (float) r.totalMemory();
            // .. Draw allocated and used strings ..
            big.setColor(Color.green);
            big.drawString(String.valueOf((int) totalMemory/1024) + "K allocated",  4.0f,
                    (float) ascent+0.5f);
            big.drawString(String.valueOf(((int) (totalMemory - freeMemory))/1024) + "K used",
                    4, h-descent);
            // Calculate remaining size
            float ssH = ascent + descent;
            float remainingHeight = (float) (h - (ssH*2) - 0.5f);
            float blockHeight = remainingHeight/10;
            float blockWidth = 20.0f;
            // .. Memory Free ..
            big.setColor(mfColor);
            int MemUsage = (int) ((freeMemory / totalMemory) * 10);
            int i = 0;
            for ( ; i < MemUsage ; i++) {
                mfRect.setRect(5, (float) ssH+i*blockHeight, blockWidth, (float) blockHeight-1);
                big.fill(mfRect);
            }
            // .. Memory Used ..
            big.setColor(Color.green);
            for ( ; i < 10; i++)  {
                muRect.setRect(5, (float) ssH+i*blockHeight, blockWidth, (float) blockHeight-1);
                big.fill(muRect);
            }
            // .. Draw History Graph ..
            big.setColor(graphColor);
            int graphX = 30;
            int graphY = (int) ssH;
            int graphW = w - graphX - 5;
            int graphH = (int) remainingHeight;
            graphOutlineRect.setRect(graphX, graphY, graphW, graphH);
            big.draw(graphOutlineRect);
            int graphRow = graphH/10;
            // .. Draw row ..
            for (int j = graphY; j <= graphH+graphY; j += graphRow) {
                graphLine.setLine(graphX, j, graphX+graphW, j);
                big.draw(graphLine);
            }
            // .. Draw animated column movement ..
            int graphColumn = graphW/15;
            if (columnInc == 0) {
                columnInc = graphColumn;
            }
            for (int j = graphX+columnInc; j < graphW+graphX; j+=graphColumn) {
                graphLine.setLine(j, graphY, j, graphY+graphH);
                big.draw(graphLine);
            }
            --columnInc;
            if (pts == null) {
                pts = new int[graphW];
                ptNum = 0;
            } else if (pts.length != graphW) {
                int tmp[] = null;
                if (ptNum < graphW) {
                    tmp = new int[ptNum];
                    System.arraycopy(pts, 0, tmp, 0, tmp.length);
                } else {
                    tmp = new int[graphW];
                    System.arraycopy(pts, pts.length-tmp.length, tmp, 0, tmp.length);
                    ptNum = tmp.length - 2;
                }
                pts = new int[graphW];
                System.arraycopy(tmp, 0, pts, 0, tmp.length);
            } else {
                big.setColor(Color.yellow);
                pts[ptNum] = (int)(graphY+graphH*(freeMemory/totalMemory));
                for (int j=graphX+graphW-ptNum, k=0;k < ptNum; k++, j++) {
                    if (k != 0) {
                        if (pts[k] != pts[k-1]) {
                            big.drawLine(j-1, pts[k-1], j, pts[k]);
                        } else {
                            big.fillRect(j, pts[k], 1, 1);
                        }
                    }
                }
                if (ptNum+2 == pts.length) {
                    // throw out oldest point
                    for (int j = 1;j < ptNum; j++) {
                        pts[j-1] = pts[j];
                    }
                    --ptNum;
                } else {
                    ptNum++;
                }
            }
            g.drawImage(bimg, 0, 0, this);
        }

        public void start() {
            thread = new Thread(this);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.setName("MemoryMonitor");
            thread.start();
        }

        public synchronized void stop() {
            thread = null;
            notify();
        }

        public void run() {
            Thread me = Thread.currentThread();
            while (thread == me && !isShowing() || getSize().width == 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { thread = null; return; }
            }
            while (thread == me && isShowing()) {
                Dimension d = getSize();
                if (d.width != w || d.height != h) {
                    w = d.width;
                    h = d.height;
                    bimg = (BufferedImage) createImage(w, h);
                    big = bimg.createGraphics();
                    big.setFont(font);
                    FontMetrics fm = big.getFontMetrics(font);
                    ascent = (int) fm.getAscent();
                    descent = (int) fm.getDescent();
                }
                repaint();
                try {
                    Thread.sleep(999);
                } catch (InterruptedException e) { break; }
            }
            thread = null;
        }
    }

    public static void main(String s[]) {
        final MemoryMonitor demo = new MemoryMonitor();
        WindowListener l = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {System.exit(0);}
            @Override
            public void windowDeiconified(WindowEvent e) { demo.mc.start(); }
            @Override
            public void windowIconified(WindowEvent e) { demo.mc.stop(); }
        };
        Frame f = new Frame("Java2D Demo - MemoryMonitor");
        f.addWindowListener(l);
        f.add("Center", demo);
        f.pack();
        f.setSize(new Dimension(200,200));
        f.setVisible(true);
        /*
        Frame f = showMemoryMonitor();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        f.dispose();
        */
    }

    public class MonitorComponent2 extends JPanel implements Runnable {
        public Thread thread;
        JLabel label1;
        public MonitorComponent2(JPanel p) {
            label1 = new JLabel("Datum", JLabel.LEFT);
            label1.setBorder(new EtchedBorder(Constants.ETCH));
            Border margin = new EmptyBorder(new Insets(0, 0, 0, 0));
            Border border = label1.getBorder();
            CompoundBorder compoundBorder = new CompoundBorder(border, margin);
            label1.setBorder(compoundBorder);
            p.add(label1);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (thread == null) {
                        start();
                    } else {
                        stop();
                    }
                }
            });
            start();
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        public void start() {
            thread = new Thread(this);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.setName("ClockMonitor");
            thread.start();
        }

        public synchronized void stop() {
            thread = null;
            notify();
        }

        public void run() {
            Thread me = Thread.currentThread();
            while (thread == me && !isShowing() || getSize().width == 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    thread = null;
                    return;
                }
            }
            while (thread == me && isShowing()) {
                // Format the current time.
                SimpleDateFormat formatter = new SimpleDateFormat ("dd.MM.yyyy  HH:mm:ss");
                Date currentTime_1 = new Date();
                String dateString = formatter.format(currentTime_1);
                label1.setText(dateString);
                try {
                    Thread.sleep(999);
                } catch (InterruptedException e) {
                    break;
                }
            }
            thread = null;
        }
    }

    /**
     * Ein statischer Aufruf f&uuml;r einen MemoryMonitor.
     *
     * @return Referenz auf den ge&ouml;ffneten Monitor-Frame.
     *
     * @changed OLI 19.09.2011 - R&uuml;ckgabe des erzeugten Frames.
     */
    public static Frame showMemoryMonitor() {
        final Frame f = new Frame("Sun Memory-Monitor");
        final MemoryMonitor demo = new MemoryMonitor();
        WindowListener l = new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                demo.mc.stop();
            }
            @Override
            public void windowClosing(WindowEvent e) {
                demo.mc.stop();
                f.dispose();
            }
            @Override
            public void windowDeiconified(WindowEvent e) {
                demo.mc.start();
            }
            @Override
            public void windowIconified(WindowEvent e) {
                demo.mc.stop();
            }
        };
        f.addWindowListener(l);
        f.add("Center", demo);
        f.pack();
        f.setSize(new Dimension(200,200));
        f.setVisible(true);
        return f;
    }

}