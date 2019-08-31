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
package morphone.samples;

import morphone.base.*;

import java.awt.*;

import java.util.*;


/**
 * Ball morphogen - create a textured ball.
 */

// Ball morphogen.
public class Ball extends Morphogen
{
   // Textures.
   final static int SOLID              = 0;
   final static int CHECKERS           = 1;
   final static int HORIZONTAL_STRIPES = 2;
   final static int VERTICAL_STRIPES   = 3;
   final static int GRID = -8;  // See Morphone.java

   // Hormone types.
   Compound INITIAL;
   Compound BALL;

   // Hormone type scoping.
   Scope scope;

   // Constructors.
   public Ball()
   {
      scope   = ScopeFactory.newScope();
      INITIAL = scope.newValue();
      BALL    = scope.newValue();
   }


   // Cell morphogenesis.
   public void morphCell(Cell cell)
   {
      int       i;
      int       j;
      int       k;
      int       x;
      int       y;
      Secretion s;
      Tracker   t;
      double    d;

      t = null;

      for (i = 0, j = cell.absorption.size(); i < j; i++)
      {
         s = (Secretion)cell.absorption.elementAt(i);

         if (s.hormone.type.equals(INITIAL))
         {
            cell.secrete(new Secretion(hormone(8, 0, 0, VERTICAL_STRIPES),
                                       new Orientation(CENTER), 1.0));

            continue;
         }

         if (s.hormone.type.equals(BALL))
         {
            t = (Tracker)(s.hormone.parameters.elementAt(0));

            continue;
         }
      }

      // Expand ball.
      if (t != null)
      {
         // Texture the ball.
         switch (t.texture)
         {
         case SOLID:
            cell.color = Color.black;

            break;

         case CHECKERS:

            if (((t.x + t.y) % 2) == 0)
            {
               cell.color = Color.black;
            }
            else
            {
               cell.color = Color.gray;
            }

            break;

         case HORIZONTAL_STRIPES:

            if ((t.y % 2) == 0)
            {
               cell.color = Color.black;
            }
            else
            {
               cell.color = Color.gray;
            }

            break;

         case VERTICAL_STRIPES:

            if ((t.x % 2) == 0)
            {
               cell.color = Color.black;
            }
            else
            {
               cell.color = Color.gray;
            }

            break;

         case GRID:
            cell.symbol = GRID;

            break;
         }

         for (k = 0; k < 8; k++)
         {
            x = y = 0;

            switch (k)
            {
            case 0:
               x = t.x;
               y = t.y + 1;

               break;

            case 1:

               continue;

            case 2:
               x = t.x + 1;
               y = t.y;

               break;

            case 3:

               continue;

            case 4:
               x = t.x;
               y = t.y - 1;

               break;

            case 5:

               continue;

            case 6:
               x = t.x - 1;
               y = t.y;

               break;

            case 7:

               continue;
            }

            d = Math.sqrt((x * x) + (y * y));

            if (((Math.abs(x) > Math.abs(t.x)) ||
                 (Math.abs(y) > Math.abs(t.y))) &&
                (d <= (double)t.radius))
            {
               cell.secrete(new Secretion(hormone(t.radius, x, y, t.texture),
                                          new Orientation(k), 1.0));
            }
         }
      }
   }


   // Create a ball hormone.
   Hormone hormone(int r, int x, int y)
   {
      return(hormone(r, x, y, SOLID));
   }


   @SuppressWarnings("unchecked")
   Hormone hormone(int r, int x, int y, int t)
   {
      Vector p = new Vector();

      p.addElement(new Tracker(r, x, y, t));

      return(new Hormone(BALL, p));
   }


   // Tracker.
   class Tracker
   {
      int radius;
      int x;
      int y;
      int texture;

      // Constructor.
      Tracker(int r, int x, int y, int t)
      {
         radius  = r;
         this.x  = x;
         this.y  = y;
         texture = t;
      }
   }
}
