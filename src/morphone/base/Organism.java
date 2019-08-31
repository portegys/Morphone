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

import java.lang.reflect.*;

/**
 * Morphone organism.
 * <p>
 * The organism consists of a cellular automata in which each cell is
 * capable of secreting and absorbing hormones to and from its neighbors.
 * In each cell, morphogenic functions translate absorbed hormones into
 * changes in orientation, coloring, and subsequent hormone secretions.
 */

// Organism.
public class Organism implements Parameters
{
   // Dimensions.
   public int Width  = DIMENSIONS.width;
   public int Height = DIMENSIONS.height;

   // Cells.
   public Cell[][] cells;

   // Morphogen.
   public Morphogen morphogen;

   // Constructor.
   @SuppressWarnings("unchecked")
   public Organism(String morphogenName) throws Exception
   {
      int         x;
      int         y;
      Class       c;
      Constructor b;

      // Create morphogen.
      try {
         c         = Class.forName(morphogenName);
         b         = c.getConstructor(new Class[] {  });
         morphogen = (Morphogen)b.newInstance(new Object[] {  });
      }
      catch (Exception e) {
         throw (e);
      }

      // Create cells.
      cells = new Cell[Width][Height];

      for (x = 0; x < Width; x++)
      {
         for (y = 0; y < Height; y++)
         {
            cells[x][y] = new Cell(new Orientation(), BGCOLOR);
         }
      }

      // Center cell absorbs initial secretion.
      x = Width / 2;
      y = Height / 2;
      cells[x][y].secrete(new Secretion(new Hormone(morphogen.INITIAL),
                                        new Orientation(CENTER), 1.0));
   }


   // Organism morphogenesis.
   public void morph()
   {
      int x;
      int y;

      // Absorb hormones from neighbors.
      for (x = 0; x < Width; x++)
      {
         for (y = 0; y < Height; y++)
         {
            cells[x][y].absorption.removeAllElements();
            absorb(x, y, NORTH);
            absorb(x, y, NORTHEAST);
            absorb(x, y, EAST);
            absorb(x, y, SOUTHEAST);
            absorb(x, y, SOUTH);
            absorb(x, y, SOUTHWEST);
            absorb(x, y, WEST);
            absorb(x, y, NORTHWEST);
            absorb(x, y, CENTER);
         }
      }

      // Morph cells.
      for (x = 0; x < Width; x++)
      {
         for (y = 0; y < Height; y++)
         {
            morphogen.morphCell(cells[x][y]);
         }
      }
   }


   // Absorb secretions from neighbor.
   @SuppressWarnings("unchecked")
   public void absorb(int x, int y, int dir)
   {
      int       i;
      int       j;
      int       x2;
      int       y2;
      Secretion s;

      x2 = x;
      y2 = y;

      switch (dir)
      {
      case NORTH:

         if (y == (Height - 1))
         {
            return;
         }

         y2  = y + 1;
         dir = SOUTH;

         break;

      case NORTHEAST:

         if ((x == (Width - 1)) || (y == (Height - 1)))
         {
            return;
         }

         x2  = x + 1;
         y2  = y + 1;
         dir = SOUTHWEST;

         break;

      case EAST:

         if (x == (Width - 1))
         {
            return;
         }

         x2  = x + 1;
         dir = WEST;

         break;

      case SOUTHEAST:

         if ((x == (Width - 1)) || (y == 0))
         {
            return;
         }

         x2  = x + 1;
         y2  = y - 1;
         dir = NORTHWEST;

         break;

      case SOUTH:

         if (y == 0)
         {
            return;
         }

         y2  = y - 1;
         dir = NORTH;

         break;

      case SOUTHWEST:

         if ((x == 0) || (y == 0))
         {
            return;
         }

         x2  = x - 1;
         y2  = y - 1;
         dir = NORTHEAST;

         break;

      case WEST:

         if (x == 0)
         {
            return;
         }

         x2  = x - 1;
         dir = EAST;

         break;

      case NORTHWEST:

         if ((x == 0) || (y == (Height - 1)))
         {
            return;
         }

         x2  = x - 1;
         y2  = y + 1;
         dir = SOUTHEAST;

         break;
      }

      for (i = 0, j = cells[x2][y2].secretions[dir].size(); i < j; i++)
      {
         s = (Secretion)cells[x2][y2].secretions[dir].elementAt(i);
         cells[x][y].absorption.addElement(s);
      }

      cells[x2][y2].secretions[dir].removeAllElements();
   }
}
