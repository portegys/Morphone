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
 * Edge morphogen - draws a line to given relative point.
 * <p>
 * EDGE hormone parameter: direction vector.
 */

// Draw morphogen.
public class Edge extends Morphogen
{
   // Hormone types.
   Compound EDGE;

   // Hormone type scoping.
   Scope scope;

   // Constructor.
   public Edge()
   {
      scope = ScopeFactory.newScope();
      EDGE  = scope.newValue();
   }


   // Cell morphogenesis.
   public void morphCell(Cell cell)
   {
      int       i;
      int       j;
      int       d;
      Secretion s;
      Vector    v;

      for (i = 0, j = cell.absorption.size(); i < j; i++)
      {
         s = (Secretion)cell.absorption.elementAt(i);

         if (s.hormone.type.equals(EDGE))
         {
            // Color cell.
            cell.color = Color.black;

            // End of path?
            v = s.hormone.parameters;

            if (v.size() == 0)
            {
               continue;
            }

            // Secrete to next cell.
            d = ((Integer)v.elementAt(0)).intValue();
            v.removeElementAt(0);
            cell.secrete(new Secretion(s.hormone, new Orientation(d), 1.0));
         }
      }
   }


   // Create an edge hormone.
   @SuppressWarnings("unchecked")
   Hormone hormone(int x, int y)
   {
      double rx;
      double ry;
      double d;
      int    x1;
      int    y1;
      int    x2;
      int    y2;
      int    x3;
      int    y3;
      int    dx;
      int    dy;
      int    dxy;
      Vector v = new Vector();

      // Build a direction vector.
      if ((x == 0) && (y == 0))
      {
         return(new Hormone(EDGE, v));
      }

      if (x > 0)
      {
         dx = EAST;

         if (y > 0)
         {
            dy  = NORTH;
            dxy = NORTHEAST;
         }
         else
         {
            dy  = SOUTH;
            dxy = SOUTHEAST;
         }
      }
      else
      {
         dx = WEST;

         if (y > 0)
         {
            dy  = NORTH;
            dxy = NORTHWEST;
         }
         else
         {
            dy  = SOUTH;
            dxy = SOUTHWEST;
         }
      }

      d  = Math.sqrt((double)(x * x) + (double)(y * y));
      x1 = Math.abs(x);
      y1 = Math.abs(y);
      rx = (double)x1 / d;
      ry = (double)y1 / d;

      for (x2 = x3 = 0, y2 = y3 = 0, d = 1.0; (x2 < x1) || (y2 < y1);
           d += 1.0)
      {
         x2 = (int)(d * rx);
         y2 = (int)(d * ry);

         if (x2 > x3)
         {
            if (y2 > y3)
            {
               v.addElement(new Integer(dxy));
            }
            else
            {
               v.addElement(new Integer(dx));
            }
         }
         else if (y2 > y3)
         {
            v.addElement(new Integer(dy));
         }

         x3 = x2;
         y3 = y2;
      }

      return(new Hormone(EDGE, v));
   }
}
