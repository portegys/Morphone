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
 * Organism target pattern.
 *
 */
package morphone.misc;

import morphone.base.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.net.*;

import java.util.*;


public class Pattern
{
   // Pattern.
   Color[][] pattern;

   // Proximity map.
   private double[][] proximity;

   // Status.
   String statusMessage = "";

   // Constructor.
   Pattern()
   {
      int    i;
      int    j;
      int    n;
      int    w;
      int    h;
      double x1;
      double y1;
      double x2;
      double y2;
      double dx;
      double dy;

      // Create default pattern.
      pattern = new Color[Parameters.DIMENSIONS.width][Parameters.DIMENSIONS.height];
      w       = Parameters.DIMENSIONS.width;
      h       = Parameters.DIMENSIONS.height;

      for (i = 0; i < w; i++)
      {
         for (j = 0; j < h; j++)
         {
            pattern[i][j] = Parameters.BGCOLOR;
         }
      }

      // Create proximity map for fitness scoring.
      n         = w * h;
      proximity = new double[n][n];

      for (i = 0; i < n; i++)
      {
         for (j = 0; j < n; j++)
         {
            if (i >= j)
            {
               x1 = i % w;
               y1 = i / h;
               x2 = j % w;
               y2 = j / h;
               dx = (double)(x1 - x2);
               dy = (double)(y1 - y2);
               proximity[i][j] = Math.sqrt((dx * dx) + (dy * dy));
               proximity[j][i] = proximity[i][j];
            }
         }
      }
   }


   // Score phenotype fitness against pattern using "Glove Distance".
   double fitness(Organism organism)
   {
      int    x1;
      int    y1;
      int    x2;
      int    y2;
      int    i;
      int    j;
      int    k;
      int    w;
      int    h;
      int    n;
      int    n1;
      int    n2;
      double cd;
      double d;
      double d1;
      double d2;
      Color  c1;
      Color  c2;

      w  = Parameters.DIMENSIONS.width;
      h  = Parameters.DIMENSIONS.height;
      n  = w * h;
      n1 = n2 = 0;
      d1 = d2 = 0.0;

      for (i = 0; i < n; i++)
      {
         x1 = i % w;
         y1 = i / h;
         c1 = organism.cells[x1][y1].color;

         if (c1 == Parameters.BGCOLOR)
         {
            continue;
         }

         for (j = k = 0, d = 0.0; j < n; j++)
         {
            x2 = j % w;
            y2 = j / h;
            c2 = pattern[x2][y2];

            if (c2 == Parameters.BGCOLOR)
            {
               continue;
            }

            cd = colorDiff(c1, c2);

            if ((k == 0) || ((proximity[i][j] + cd) < d))
            {
               k = 1;
               d = proximity[i][j] + cd;
            }
         }

         d1 += d;
         n1++;
      }

      for (i = 0; i < n; i++)
      {
         x1 = i % w;
         y1 = i / h;
         c1 = pattern[x1][y1];

         if (c1 == Parameters.BGCOLOR)
         {
            continue;
         }

         for (j = k = 0, d = 0.0; j < n; j++)
         {
            x2 = j % w;
            y2 = j / h;
            c2 = organism.cells[x2][y2].color;

            if (c2 == Parameters.BGCOLOR)
            {
               continue;
            }

            cd = colorDiff(c1, c2);

            if ((k == 0) || ((proximity[i][j] + cd) < d))
            {
               k = 1;
               d = proximity[i][j] + cd;
            }
         }

         d2 += d;
         n2++;
      }

      if (n1 > 0)
      {
         d1 = d1 / (double)n1;
      }

      if (n2 > 0)
      {
         d2 = d2 / (double)n2;
      }

      return(d1 + d2);
   }


   // Computer color difference.
   private double colorDiff(Color c1, Color c2)
   {
      double d;

      d  = (double)Math.abs(c1.getRed() - c2.getRed());
      d += (double)Math.abs(c1.getGreen() - c2.getGreen());
      d += (double)Math.abs(c1.getBlue() - c2.getBlue());

      return(d);
   }


   // Get cell value.
   Color get(int x, int y)
   {
      statusMessage = "";

      if ((x < 0) || (x >= Parameters.DIMENSIONS.width))
      {
         statusMessage = "Invalid x coordinate";

         return(null);
      }

      if ((y < 0) || (y >= Parameters.DIMENSIONS.height))
      {
         statusMessage = "Invalid y coordinate";

         return(null);
      }

      return(pattern[x][y]);
   }


   // Set cell value.
   boolean set(int x, int y, Color c)
   {
      statusMessage = "";

      if ((x < 0) || (x >= Parameters.DIMENSIONS.width))
      {
         statusMessage = "Invalid x coordinate";

         return(false);
      }

      if ((y < 0) || (y >= Parameters.DIMENSIONS.height))
      {
         statusMessage = "Invalid y coordinate";

         return(false);
      }

      pattern[x][y] = c;

      return(true);
   }


   // Load pattern.
   // Format: <x> <y> <r-g-b>
   boolean load(String source, URL baseURL)
   {
      URL             u;
      BufferedReader  r;
      String          l;
      String          s;
      StringTokenizer t;
      StringTokenizer t2;
      int             x;
      int             y;
      int             red;
      int             green;
      int             blue;

      statusMessage = "";

      try {
         try {
            u = new URL(source);
         }
         catch (MalformedURLException e) {
            u = new URL(baseURL, source);
         }

         r = new BufferedReader(new InputStreamReader(u.openStream()));
      }
      catch (MalformedURLException e) {
         statusMessage = "Bad URL for pattern " + source;

         return(false);
      }
      catch (IOException e) {
         statusMessage = "Error opening " + source;

         return(false);
      }

      for (x = 0; x < Parameters.DIMENSIONS.width; x++)
      {
         for (y = 0; y < Parameters.DIMENSIONS.height; y++)
         {
            pattern[x][y] = Parameters.BGCOLOR;
         }
      }

      l = "";

      try {
         while ((l = r.readLine()) != null)
         {
            if ((l.length() == 0) || l.startsWith("#"))
            {
               continue;
            }

            t = new StringTokenizer(l, " ");

            try {
               x = Integer.parseInt(t.nextToken());

               if ((x < 0) || (x >= Parameters.DIMENSIONS.width))
               {
                  statusMessage = "Invalid x coordinate in " + source +
                                  ": " + l;

                  return(false);
               }

               y = Integer.parseInt(t.nextToken());

               if ((y < 0) || (y >= Parameters.DIMENSIONS.height))
               {
                  statusMessage = "Invalid y coordinate in " + source +
                                  ": " + l;

                  return(false);
               }

               t2  = new StringTokenizer(t.nextToken(), "-");
               red = Integer.parseInt(t2.nextToken());

               if ((red < 0) || (red > 255))
               {
                  statusMessage = "Bad red value in " + source + ": " +
                                  l;

                  return(false);
               }

               green = Integer.parseInt(t2.nextToken());

               if ((green < 0) || (green > 255))
               {
                  statusMessage = "Bad green value in " + source + ": " +
                                  l;

                  return(false);
               }

               blue = Integer.parseInt(t2.nextToken());

               if ((blue < 0) || (blue > 255))
               {
                  statusMessage = "Bad blue value in " + source + ": " +
                                  l;

                  return(false);
               }
            }
            catch (NumberFormatException e) {
               statusMessage = "Bad line in " + source + ": " + l;

               return(false);
            }
            catch (NoSuchElementException e) {
               statusMessage = "Bad line in " + source + ": " + l;

               return(false);
            }

            pattern[x][y] = new Color(red, green, blue);
         }
      }
      catch (IOException e) {
         statusMessage = "IOException processing " + source;

         return(false);
      }

      return(true);
   }


   // Save pattern.
   // Format: <x> <y> <r-g-b>
   boolean save(String target)
   {
      PrintWriter out;
      int         x;
      int         y;
      int         w;
      int         h;
      int         red;
      int         green;
      int         blue;

      statusMessage = "";

      try {
         out = new PrintWriter(new BufferedWriter(new FileWriter(target)));
         w   = Parameters.DIMENSIONS.width;
         h   = Parameters.DIMENSIONS.height;

         for (x = 0; x < w; x++)
         {
            for (y = 0; y < h; y++)
            {
               if (pattern[x][y] == Parameters.BGCOLOR)
               {
                  continue;
               }

               red   = pattern[x][y].getRed();
               green = pattern[x][y].getGreen();
               blue  = pattern[x][y].getBlue();
               out.println(x + " " + y + " " + red + "-" + green + "-" +
                           blue);
            }
         }

         out.close();
      }
      catch (IOException e) {
         statusMessage = "Error saving pattern file " + target;

         return(false);
      }

      return(true);
   }
}
