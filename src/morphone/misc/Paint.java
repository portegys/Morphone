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

/*
 *
 * Phenotype target pattern painter.
 *
 * java Paint [<screen width> <screen height>]
 *
 */
package morphone.misc;

import morphone.base.*;

import java.applet.Applet;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.net.*;

import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;


public class Paint extends Applet implements Runnable
{
   // Parameters.
   static final int DEFAULT_SCREEN_WIDTH  = 500;
   static final int DEFAULT_SCREEN_HEIGHT = 500;

   // Milliseconds between display updates.
   static final int DISPLAY_UPDATE_DELAY = 50;
   static final int PAINT = 0;
   static final int FILE  = 1;
   static final int RED   = 0;
   static final int GREEN = 1;
   static final int BLUE  = 2;
   static final int SAVE  = 0;
   static final int LOAD  = 1;

   // Pattern.
   Pattern pattern;
   String  patternName;
   URL     baseURL;

   // Screen and canvas (buffered).
   JFrame    screen;
   Canvas    canvas;
   Dimension canvasSize;
   Graphics  canvasGraphics;
   Image     image;
   Graphics  imageGraphics;
   double    cellWidth;
   double    cellHeight;
   Thread    displayThread;

   // Controls.
   JTabbedPane   controlTabs;
   int           mode;
   JPanel        paintPanel;
   Checkbox      eraseCheck;
   boolean       erase;
   CheckboxGroup colorGroup;
   Checkbox      redCheck;
   Checkbox      greenCheck;
   Checkbox      blueCheck;
   JSlider       colorSlider;
   int           color;
   int           red;
   int           green;
   int           blue;
   JPanel        filePanel;
   Choice        fileOperationChoice;
   JComboBox     fileComboBox;
   int           fileOperation;

   // Font.
   Font        font;
   FontMetrics fontMetrics;
   int         fontAscent;
   int         fontWidth;
   int         fontHeight;

   // Constructor.
   public Paint(Dimension screenSize)
   {
      // Create screen.
      screen = new JFrame("Pattern Painter");
      screen.addWindowListener(new WindowAdapter()
                               {
                                  public void windowClosing(WindowEvent e)
                                  {
                                     System.exit(0);
                                  }
                               }
                               );
      screen.setSize(screenSize);
      screen.setVisible(true);

      // Create canvas.
      screen.getContentPane().setLayout(new BorderLayout());
      canvas     = new Canvas();
      canvasSize = new Dimension(screenSize.width,
                                 (int)((double)screenSize.height * .80));
      canvas.setBounds(0, 0, canvasSize.width, canvasSize.height);
      canvas.addMouseListener(new canvasMouseListener());
      canvas.addMouseMotionListener(new canvasMouseMotionListener());
      screen.getContentPane().add(canvas, BorderLayout.NORTH);
      canvasGraphics = canvas.getGraphics();
      image          = screen.getContentPane().createImage(canvasSize.width,
                                                           canvasSize.height);
      imageGraphics = image.getGraphics();
      cellWidth     = (double)canvasSize.width / (double)Parameters.DIMENSIONS.width;
      cellHeight    = (double)canvasSize.height / (double)Parameters.DIMENSIONS.height;

      // Create pattern.
      pattern     = new Pattern();
      patternName = "";

      // Create controls.
      controlTabs = new JTabbedPane();
      controlTabs.addChangeListener(new controlTabsChangeListener());
      paintPanel = new JPanel();
      eraseCheck = new Checkbox("Erase");
      erase      = false;
      eraseCheck.addItemListener(new eraseCheckItemListener());
      paintPanel.add(eraseCheck);
      colorGroup = new CheckboxGroup();
      redCheck   = new Checkbox("Red", colorGroup, true);
      color      = RED;
      redCheck.addItemListener(new redCheckItemListener());
      paintPanel.add(redCheck);
      greenCheck = new Checkbox("Green", colorGroup, false);
      greenCheck.addItemListener(new greenCheckItemListener());
      paintPanel.add(greenCheck);
      blueCheck = new Checkbox("Blue", colorGroup, false);
      blueCheck.addItemListener(new blueCheckItemListener());
      paintPanel.add(blueCheck);
      colorSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
      colorSlider.setBorder(new LineBorder(Parameters.BGCOLOR));
      colorSlider.addChangeListener(new colorSliderChangeListener());
      red = green = blue = 0;
      paintPanel.add(colorSlider);
      filePanel           = new JPanel();
      fileOperationChoice = new Choice();
      fileOperationChoice.addItemListener(new fileOperationChoiceItemListener());
      fileOperationChoice.add("Save pattern:");
      fileOperationChoice.add("Load pattern:");
      filePanel.add(fileOperationChoice);
      fileComboBox = new JComboBox();
      fileComboBox.setEditable(true);
      fileComboBox.setEnabled(true);
      fileComboBox.addActionListener(new fileComboBoxActionListener());
      filePanel.add(fileComboBox);
      fileOperation = SAVE;
      controlTabs.addTab("Paint", null, paintPanel, "Paint");
      controlTabs.addTab("File", null, filePanel, "File operations");
      screen.getContentPane().add(controlTabs);
      controlTabs.setSelectedIndex(1);
      controlTabs.setSelectedIndex(0);
      mode = PAINT;

      // Set font data.
      font = new Font("Helvetica", Font.BOLD, 14);
      canvasGraphics.setFont(font);
      imageGraphics.setFont(font);
      fontMetrics = canvasGraphics.getFontMetrics();
      fontAscent  = fontMetrics.getMaxAscent();
      fontWidth   = fontMetrics.getMaxAdvance();
      fontHeight  = fontMetrics.getHeight();

      // Get base URL.
      try {
         String s = System.getProperty("user.dir");
         baseURL = new File(s).toURI().toURL();
      }
      catch (SecurityException e) {
         System.err.println("Cannot get property for current directory");
         System.exit(1);
      }
      catch (MalformedURLException e) {
         System.err.println("Cannot get URL of current directory");
         System.exit(1);
      }

      // Draw display.
      drawDisplay();

      // Start display thread.
      start();
   }


   // Main.
   public static void main(String[] args)
   {
      Dimension screenSize;

      // Get screen size.
      screenSize = new Dimension(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);

      if (args.length == 2)
      {
         screenSize.width  = Integer.parseInt(args[0]);
         screenSize.height = Integer.parseInt(args[1]);
      }
      else if (args.length != 0)
      {
         System.err.println("java Composer [<screen width> <screen height>]");
         System.exit(1);
      }

      if ((screenSize.width <= 0) || (screenSize.height <= 0))
      {
         System.err.println("Invalid screen size");
         System.exit(1);
      }

      // Create the painter.
      new Paint(screenSize);
   }


   // Start.
   public void start()
   {
      // Create display thread.
      if (displayThread == null)
      {
         displayThread = new Thread(this);
         displayThread.start();
      }
   }


   // Stop.
   public void stop()
   {
      if (displayThread != null)
      {
         displayThread = null;
      }
   }


   // Run.
   public void run()
   {
      // Lower this thread's priority.
      Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

      // Display update loop.
      while (Thread.currentThread() == displayThread)
      {
         canvasGraphics.drawImage(image, 0, 0, this);

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
      canvasGraphics.drawImage(image, 0, 0, this);
      super.paint(g);
   }


   // Draw display.
   private void drawDisplay()
   {
      int   x;
      int   y;
      int   x2;
      int   y2;
      Color c;

      c = imageGraphics.getColor();
      imageGraphics.setColor(Parameters.BGCOLOR);
      imageGraphics.fillRect(0, 0, canvasSize.width, canvasSize.height);
      imageGraphics.setColor(Color.black);

      y2 = canvasSize.height;

      for (x = 1, x2 = (int)cellWidth - 1; x < Parameters.DIMENSIONS.width;
           x++, x2 = (int)(cellWidth * (double)x) - 1)
      {
         imageGraphics.drawLine(x2, 0, x2, y2);
      }

      x2 = canvasSize.width;

      for (y = 1, y2 = (int)cellHeight - 1;
           y < Parameters.DIMENSIONS.height;
           y++, y2 = (int)(cellHeight * (double)y) - 1)
      {
         imageGraphics.drawLine(0, y2, x2, y2);
      }

      for (x = 0; x < Parameters.DIMENSIONS.width; x++)
      {
         for (y = 0; y < Parameters.DIMENSIONS.height; y++)
         {
            imageGraphics.setColor(pattern.get(x, y));
            drawCell(x, y);
         }
      }

      imageGraphics.setColor(c);
   }


   // Draw cell.
   private void drawCell(int x, int y)
   {
      int x2;
      int y2;

      x2 = (int)(cellWidth * (double)x);
      y2 = (int)(cellHeight * (double)y);
      imageGraphics.fillRect(x2, y2, (int)cellWidth - 1, (int)cellHeight -
                             1);
   }


   // Draw message.
   private void drawMessage(String s)
   {
      Color c;
      int   w;

      drawDisplay();
      c = imageGraphics.getColor();
      imageGraphics.setColor(Color.white);
      w = fontMetrics.stringWidth(s);
      imageGraphics.fillRect(((canvasSize.width - w) / 2) - 2,
                             (canvasSize.height / 2) - (fontAscent + 2), w + 4, fontHeight + 4);
      imageGraphics.setColor(Color.black);
      imageGraphics.drawString(s, (canvasSize.width - w) / 2,
                               canvasSize.height / 2);
      imageGraphics.setColor(c);
   }


   // File choice listener.
   class fileOperationChoiceItemListener implements ItemListener
   {
      public void itemStateChanged(ItemEvent evt)
      {
         // Set current operation and get list of matching files.
         fileOperation = fileOperationChoice.getSelectedIndex();
         fileComboBox.setSelectedItem((Object)"");
      }
   }

   // Control tabs listener.
   class controlTabsChangeListener implements ChangeListener
   {
      public void stateChanged(ChangeEvent evt)
      {
         // Set current mode.
         mode = controlTabs.getSelectedIndex();
         drawDisplay();
      }
   }

   // Erase check button listener.
   class eraseCheckItemListener implements ItemListener
   {
      public void itemStateChanged(ItemEvent evt)
      {
         Color c;

         if (eraseCheck.getState())
         {
            erase = true;
            c     = Parameters.BGCOLOR;
         }
         else
         {
            erase = false;
            c     = new Color(red, green, blue);
         }

         colorSlider.setBorder(new LineBorder(c));
         imageGraphics.setColor(c);
      }
   }

   // Red check button listener.
   class redCheckItemListener implements ItemListener
   {
      public void itemStateChanged(ItemEvent evt)
      {
         color = RED;
         colorSlider.setValue(red);
      }
   }

   // Green check button listener.
   class greenCheckItemListener implements ItemListener
   {
      public void itemStateChanged(ItemEvent evt)
      {
         color = GREEN;
         colorSlider.setValue(green);
      }
   }

   // Blue check button listener.
   class blueCheckItemListener implements ItemListener
   {
      public void itemStateChanged(ItemEvent evt)
      {
         color = BLUE;
         colorSlider.setValue(blue);
      }
   }

   // Color slider listener.
   class colorSliderChangeListener implements ChangeListener
   {
      public void stateChanged(ChangeEvent evt)
      {
         Color c;

         switch (color)
         {
         case RED:
            red = colorSlider.getValue();

            break;

         case GREEN:
            green = colorSlider.getValue();

            break;

         case BLUE:
            blue = colorSlider.getValue();

            break;
         }

         if (erase == true)
         {
            return;
         }

         c = new Color(red, green, blue);
         colorSlider.setBorder(new LineBorder(c));
         imageGraphics.setColor(c);
      }
   }

   // Canvas mouse listener.
   class canvasMouseListener extends MouseAdapter
   {
      // Mouse pressed.
      public void mousePressed(MouseEvent evt)
      {
         int x;
         int y;

         if (mode != PAINT)
         {
            return;
         }

         x = (int)((double)evt.getX() / cellWidth);
         y = (int)((double)evt.getY() / cellHeight);

         if (pattern.set(x, y, imageGraphics.getColor()))
         {
            drawCell(x, y);
         }
      }
   }

   // Canvas mouse motion listener.
   class canvasMouseMotionListener extends MouseMotionAdapter
   {
      // Mouse dragged.
      public void mouseDragged(MouseEvent evt)
      {
         int x;
         int y;

         if (mode != PAINT)
         {
            return;
         }

         x = (int)((double)evt.getX() / cellWidth);
         y = (int)((double)evt.getY() / cellHeight);

         if (pattern.set(x, y, imageGraphics.getColor()))
         {
            drawCell(x, y);
         }
      }
   }

   // File combo box selection listener.
   @SuppressWarnings("unchecked")
   class fileComboBoxActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent evt)
      {
         int    i;
         int    j;
         String s;

         if (((patternName = (String)fileComboBox.getSelectedItem()) != null) &&
             !patternName.equals(""))
         {
            if (fileOperation == SAVE)
            {
               if (!pattern.save(patternName))
               {
                  drawMessage("Save failed: " + pattern.statusMessage);

                  return;
               }

               drawMessage(patternName + " saved");
            }
            else
            {
               if (!pattern.load(patternName, baseURL))
               {
                  drawMessage("Load failed: " + pattern.statusMessage);

                  return;
               }
               else
               {
                  drawDisplay();
                  drawMessage(patternName + " loaded");
               }
            }

            for (i = 0, j = fileComboBox.getItemCount(); i < j; i++)
            {
               s = (String)fileComboBox.getItemAt(i);

               if (s.equals(patternName))
               {
                  break;
               }
            }

            if (i == j)
            {
               fileComboBox.addItem((Object)patternName);
            }

            fileComboBox.setSelectedIndex(i);
            fileComboBox.setSelectedItem((Object)patternName);
         }
      }
   }
}
