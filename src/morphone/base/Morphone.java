/*
 * This software is provided under the terms of the GNU General
 * Public License as published by the Free Software Foundation.
 *
 * Copyright (c) 2002 Tom Portegys, All Rights Reserved.
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.
 */
package morphone.base;

import java.applet.Applet;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;


/**
 * Morphone - a morphogenesis experiment.
 * <p>
 * The purpose of this experiment is to investigate the properties of an
 * organism whose cells exchange hormone-like signals, as living cells do.
 * <p>
 * The organism consists of a cellular automaton in which each cell is
 * capable of secreting and absorbing hormones to and from its neighbors.
 * In each cell, morphogenic functions translate absorbed hormones into
 * changes in orientation, coloring, and subsequent hormone secretions.
 * <p>
 * Initially, all cells reside in a quiescent state, having the same color,
 * orientation, and secreting no hormones.  One cell is initially stimulated
 * by a secretion to begin the morphogenesis, causing the organism
 * to grow into patterns determined by its morphogenic functions.
 * <p>
 * Usage:
 * <p>
 * Morphone is a Java applet:
 * <p>
 * &ltapplet code="Morphone.class" width=w height=h&gt<br>
 * [&ltparam name=Morphogen value="Morphogen class name"&gt]<br>
 * &lt/applet&gt
 */

// Main applet.
public class Morphone extends Applet implements Parameters, Runnable
{
   // Morphogenesis rate: milliseconds between iterations.
   static final int MIN_MORPH_DELAY = 100;
   static final int MAX_MORPH_DELAY = 1000;

   // Milliseconds between display updates.
   static final int DISPLAY_UPDATE_DELAY = 50;

   // Organism.
   Organism organism;

   // Screen size.
   Dimension screenSize;

   // Display (buffered).
   Canvas    canvas;
   Graphics  canvasGraphics;
   Dimension canvasSize;
   double    cellWidth;
   double    cellHeight;
   Image     image;
   Graphics  imageGraphics;
   Dimension imageSize;
   String    statusMessage = "";
   boolean   drawGrid      = true;

   // Threads.
   Thread morphThread;
   Thread displayThread;

   // Control panel.
   Panel     controlPanel;
   Dimension controlPanelSize;
   Checkbox  stepButton;
   JSlider   morphSlider;
   int       morphDelay = MAX_MORPH_DELAY;

   // Font.
   Font        font = new Font("Helvetica", Font.BOLD, 12);
   FontMetrics fontMetrics;
   int         fontAscent;
   int         fontWidth;
   int         fontHeight;

   // Applet information.
   public String getAppletInfo()
   {
      return("Morphone by Tom Portegys (Version 1.2, 2002)");
   }


   // Initialize.
   public void init()
   {
      String s;

      // Create display.
      screenSize = getSize();
      setLayout(new BorderLayout());
      canvas     = new Canvas();
      canvasSize = new Dimension(screenSize.width,
                                 (int)((double)screenSize.height * .95));
      cellWidth  = (double)canvasSize.width / (double)DIMENSIONS.width;
      cellHeight = (double)canvasSize.height / (double)DIMENSIONS.height;
      canvas.setBounds(0, 0, canvasSize.width, canvasSize.height);
      add(canvas, BorderLayout.NORTH);
      canvasGraphics = canvas.getGraphics();
      image          = createImage(canvasSize.width, canvasSize.height);
      imageGraphics  = image.getGraphics();
      imageSize      = canvasSize;

      // Create control panel.
      controlPanel     = new Panel();
      controlPanelSize = new Dimension(screenSize.width,
                                       (int)((double)screenSize.height * .05));
      controlPanel.setBounds(0, canvasSize.height, controlPanelSize.width,
                             controlPanelSize.height);
      add(controlPanel);
      stepButton = new Checkbox("Step");
      controlPanel.add(stepButton);
      controlPanel.add(new Label("Fast", Label.RIGHT));
      morphSlider = new JSlider(Scrollbar.HORIZONTAL, MIN_MORPH_DELAY,
                                MAX_MORPH_DELAY, MAX_MORPH_DELAY);
      morphSlider.addChangeListener(new morphSliderListener());
      controlPanel.add(morphSlider);
      controlPanel.add(new Label("Stop", Label.LEFT));

      // Set font data.
      Graphics g = getGraphics();
      g.setFont(font);
      fontMetrics = g.getFontMetrics();
      fontAscent  = fontMetrics.getMaxAscent();
      fontWidth   = fontMetrics.getMaxAdvance();
      fontHeight  = fontMetrics.getHeight();

      // Create the organism with given morphogen.
      if ((s = getParameter("Morphogen")) != null)
      {
         showStatus("Creating organism with morphogen " + s + "...");

         try {
            organism = new Organism(s);

            if (s.equals("Bug"))
            {
               drawGrid = false;      // special case
            }
         }
         catch (Exception e) {
            statusMessage = "Cannot create organism with morphogen " + s;
            showStatus(statusMessage);
         }
      }
      else
      {
         statusMessage = "Morphogen parameter missing";
         showStatus(statusMessage);
      }
   }


   // Start.
   public void start()
   {
      // Create morphogenesis thread.
      if ((morphThread == null) && (organism != null))
      {
         morphThread = new Thread(this);
         morphThread.setPriority(Thread.MIN_PRIORITY);
         morphThread.start();
      }

      // Create display update thread.
      if (displayThread == null)
      {
         displayThread = new Thread(this);
         displayThread.setPriority(Thread.MIN_PRIORITY);
         displayThread.start();
      }
   }


   // Stop.
   public void stop()
   {
      if (morphThread != null)
      {
         morphThread = null;
      }

      if (displayThread != null)
      {
         displayThread = null;
      }
   }


   // Run.
   public void run()
   {
      // Morphogenesis loop.
      while (Thread.currentThread() == morphThread &&
             !morphThread.isInterrupted())
      {
         if ((morphDelay < MAX_MORPH_DELAY) || stepButton.getState())
         {
            organism.morph();
         }

         if (stepButton.getState())
         {
            stepButton.setState(false);
            morphDelay = MAX_MORPH_DELAY;
            morphSlider.setValue(morphDelay);
         }

         // Set the timer for the next loop.
         try {
            Thread.sleep(morphDelay);
         }
         catch (InterruptedException e) {
            break;
         }
      }

      // Display update loop.
      while (Thread.currentThread() == displayThread &&
             !displayThread.isInterrupted())
      {
         updateDisplay();

         try {
            Thread.sleep(DISPLAY_UPDATE_DELAY);
         }
         catch (InterruptedException e) {
            break;
         }
      }
   }


   // Paint.
   public void paint(Graphics g)
   {
      updateDisplay();
      super.paint(g);
   }


   // Update display.
   public void updateDisplay()
   {
      int x;
      int y;
      int x2;
      int y2;
      int i;

      // Clear.
      imageGraphics.setColor(Color.white);
      imageGraphics.fillRect(0, 0, imageSize.width, imageSize.height);

      // Draw organism.
      if (organism != null)
      {
         imageGraphics.setColor(Color.black);

         if (drawGrid)
         {
            y2 = imageSize.height;

            for (x = 1, x2 = (int)cellWidth - 1; x < DIMENSIONS.width;
                 x++, x2 = (int)(cellWidth * (double)x) - 1)
            {
               imageGraphics.drawLine(x2, 0, x2, y2);
            }

            x2 = imageSize.width;

            for (y = 1, y2 = (int)cellHeight - 1; y < DIMENSIONS.height;
                 y++, y2 = (int)(cellHeight * (double)y) - 1)
            {
               imageGraphics.drawLine(0, y2, x2, y2);
            }
         }

         for (x = x2 = 0; x < DIMENSIONS.width;
              x++, x2 = (int)(cellWidth * (double)x))
         {
            for (y = 0, y2 = imageSize.height - (int)cellHeight;
                 y < DIMENSIONS.height;
                 y++, y2 = (int)(cellHeight * (double)(DIMENSIONS.height -
                                                       (y + 1))))
            {
               if (organism.cells[x][y].symbol != -1)
               {
                  // Draw special symbols.
                  drawSymbol(x2, y2, organism.cells[x][y].symbol);
               }
               else
               {
                  imageGraphics.setColor(organism.cells[x][y].color);

                  if (drawGrid)
                  {
                     imageGraphics.fillRect(x2, y2, (int)cellWidth - 1,
                                            (int)cellHeight - 1);
                  }
                  else
                  {
                     imageGraphics.fillRect(x2 - 1, y2 - 1,
                                            (int)cellWidth + 1, (int)cellHeight + 1);
                  }
               }
            }
         }
      }

      // Display message
      if (!statusMessage.equals(""))
      {
         imageGraphics.setFont(font);
         imageGraphics.setColor(Color.black);
         imageGraphics.drawString(statusMessage,
                                  (imageSize.width - fontMetrics.stringWidth(statusMessage)) / 2,
                                  imageSize.height / 2);
      }

      // Copy to display.
      canvasGraphics.drawImage(image, 0, 0, this);
   }


   // Draw a special symbol.
   void drawSymbol(int x, int y, int symbol)
   {
      String s;

      imageGraphics.setFont(font);
      imageGraphics.setColor(Color.black);

      switch (symbol)
      {
      case -2:   // START
         s  = "s";
         x += (((int)cellWidth - fontMetrics.stringWidth(s)) / 2);
         y += (((int)cellHeight + fontAscent) / 2);
         imageGraphics.drawString(s, x, y);

         break;

      case -3:   // BLANK
         s  = "b";
         x += (((int)cellWidth - fontMetrics.stringWidth(s)) / 2);
         y += (((int)cellHeight + fontAscent) / 2);
         imageGraphics.drawString(s, x, y);

         break;

      case -4:   // PERCENT
         s  = "%";
         x += (((int)cellWidth - fontMetrics.stringWidth(s)) / 2);
         y += (((int)cellHeight + fontAscent) / 2);
         imageGraphics.drawString(s, x, y);

         break;

      case -5:   // POUND
         s  = "#";
         x += (((int)cellWidth - fontMetrics.stringWidth(s)) / 2);
         y += (((int)cellHeight + fontAscent) / 2);
         imageGraphics.drawString(s, x, y);

         break;

      case -6:   // CIRCLE
         imageGraphics.drawArc(x + 1, y + 1, (int)cellWidth - 2,
                               (int)cellHeight - 2, 0, 360);

         break;

      case -7:   // DISC
         imageGraphics.fillArc(x + 1, y + 1, (int)cellWidth - 2,
                               (int)cellHeight - 2, 0, 360);

         break;

      case -8:   // GRID
         imageGraphics.drawRect(x + 1, y + 1, (int)cellWidth - 2,
                                (int)cellHeight - 2);

         break;

      default:   // NUMBER
         s  = Integer.toString(symbol);
         x += (((int)cellWidth - fontMetrics.stringWidth(s)) / 2);
         y += (((int)cellHeight + fontAscent) / 2);
         imageGraphics.drawString(s, x, y);

         break;
      }
   }


   // Morphogenesis rate slider listener.
   class morphSliderListener implements ChangeListener
   {
      public void stateChanged(ChangeEvent evt)
      {
         morphDelay = morphSlider.getValue();
      }
   }
}
