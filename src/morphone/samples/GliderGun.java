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
 * Game of Life "Glider Gun" morphogen.
 */

// Glider Gun morphogen.
public class GliderGun extends Morphogen
{
   // Hormone types.
   Compound INITIAL;
   Compound GLIDER_GUN_EGG;
   Compound START;
   Compound ALIVE;
   Compound NEIGHBOR;

   // Transporter.
   Transporter transporter;

   // Timer.
   Timer timer;

   // Hormone type scoping.
   Scope scope;

   // Constructor.
   public GliderGun()
   {
      scope          = ScopeFactory.newScope();
      INITIAL        = scope.newValue();
      GLIDER_GUN_EGG = scope.newValue();
      START          = scope.newValue();
      ALIVE          = scope.newValue();
      NEIGHBOR       = scope.newValue();
      transporter    = new Transporter();
      timer          = new Timer();
   }


   // Cell morphogenesis.
   public void morphCell(Cell cell)
   {
      int       i;
      int       j;
      boolean   alive;
      int       neighbors;
      Secretion s;

      alive     = false;
      neighbors = 0;

      for (i = 0, j = cell.absorption.size(); i < j; i++)
      {
         s = (Secretion)cell.absorption.elementAt(i);

         if (s.hormone.type.equals(INITIAL))
         {
            cell.secrete(new Secretion(new Hormone(GLIDER_GUN_EGG),
                                       new Orientation(CENTER), 1.0));

            continue;
         }

         if (s.hormone.type.equals(GLIDER_GUN_EGG))
         {
            placeCell(cell, 0, 0);
            placeCell(cell, 17, 2);
            placeCell(cell, 17, 1);
            placeCell(cell, 18, 2);
            placeCell(cell, 18, 1);
            placeCell(cell, -16, 1);
            placeCell(cell, -16, 0);
            placeCell(cell, -17, 1);
            placeCell(cell, -17, 0);
            placeCell(cell, 3, 2);
            placeCell(cell, 4, 0);
            placeCell(cell, 4, 1);
            placeCell(cell, 4, 3);
            placeCell(cell, 4, 4);
            placeCell(cell, 6, -1);
            placeCell(cell, 6, 5);
            placeCell(cell, 8, -1);
            placeCell(cell, 8, 0);
            placeCell(cell, 8, 2);
            placeCell(cell, 8, 4);
            placeCell(cell, 8, 5);
            placeCell(cell, -1, -1);
            placeCell(cell, -1, 1);
            placeCell(cell, -2, -2);
            placeCell(cell, -2, 2);
            placeCell(cell, -3, -2);
            placeCell(cell, -3, -1);
            placeCell(cell, -3, 0);
            placeCell(cell, -3, 1);
            placeCell(cell, -3, 2);
            placeCell(cell, -4, -3);
            placeCell(cell, -4, -2);
            placeCell(cell, -4, 2);
            placeCell(cell, -4, 3);
            placeCell(cell, -5, -2);
            placeCell(cell, -5, -1);
            placeCell(cell, -5, 0);
            placeCell(cell, -5, 1);
            placeCell(cell, -5, 2);
            placeCell(cell, -6, -1);
            placeCell(cell, -6, 0);
            placeCell(cell, -6, 1);
            placeCell(cell, -7, 0);

            continue;
         }

         if (s.hormone.type.equals(START))
         {
            // Start live cell.
            alive     = true;
            neighbors = 2;

            continue;
         }

         if (s.hormone.type.equals(ALIVE))
         {
            alive = true;

            continue;
         }

         if (s.hormone.type.equals(NEIGHBOR))
         {
            neighbors++;

            continue;
         }
      }

      // If alive, tell neighbors.
      if (alive)
      {
         cell.secrete(new Secretion(new Hormone(NEIGHBOR),
                                    new Orientation(NORTH), 1.0));
         cell.secrete(new Secretion(new Hormone(NEIGHBOR),
                                    new Orientation(NORTHEAST), 1.0));
         cell.secrete(new Secretion(new Hormone(NEIGHBOR),
                                    new Orientation(EAST), 1.0));
         cell.secrete(new Secretion(new Hormone(NEIGHBOR),
                                    new Orientation(SOUTHEAST), 1.0));
         cell.secrete(new Secretion(new Hormone(NEIGHBOR),
                                    new Orientation(SOUTH), 1.0));
         cell.secrete(new Secretion(new Hormone(NEIGHBOR),
                                    new Orientation(SOUTHWEST), 1.0));
         cell.secrete(new Secretion(new Hormone(NEIGHBOR),
                                    new Orientation(WEST), 1.0));
         cell.secrete(new Secretion(new Hormone(NEIGHBOR),
                                    new Orientation(NORTHWEST), 1.0));
      }

      // Birth and death.
      if (alive)
      {
         if ((cell.color == Color.lightGray) || (cell.color == Color.gray))
         {
            cell.color = Color.gray;
         }
         else
         {
            cell.color = Color.black;
         }

         if ((neighbors == 2) || (neighbors == 3))
         {
            cell.secrete(new Secretion(new Hormone(ALIVE),
                                       new Orientation(CENTER), 1.0));
         }
         else
         {
            if ((cell.color == Color.lightGray) ||
                (cell.color == Color.gray))
            {
               cell.color = Color.lightGray;
            }
            else
            {
               cell.color = Parameters.BGCOLOR;
            }
         }
      }
      else
      {
         if (neighbors == 3)
         {
            if ((cell.color == Color.lightGray) ||
                (cell.color == Color.gray))
            {
               cell.color = Color.gray;
            }
            else
            {
               cell.color = Color.black;
            }

            cell.secrete(new Secretion(new Hormone(ALIVE),
                                       new Orientation(CENTER), 1.0));
         }
         else
         {
            if ((cell.color == Color.lightGray) ||
                (cell.color == Color.gray))
            {
               cell.color = Color.lightGray;
            }
            else
            {
               cell.color = Parameters.BGCOLOR;
            }
         }
      }

      // Transport and timer processing.
      transporter.morphCell(cell);
      timer.morphCell(cell);
   }


   // Place a life cell.
   void placeCell(Cell cell, int x, int y)
   {
      final int FARTHEST = 20;
      int       i;
      Hormone   h;

      i = FARTHEST - (Math.abs(x) + Math.abs(y));
      h = timer.hormone(i, new Hormone(START));

      switch (cell.orientation.direction)
      {
      case EAST:
         i = y;

         if (cell.orientation.mirrored)
         {
            y = x;
            x = -i;
         }
         else
         {
            y = -x;
            x = i;
         }

         break;

      case SOUTH:
         y = -y;
         x = -x;

         break;

      case WEST:
         i = y;

         if (cell.orientation.mirrored)
         {
            y = -x;
            x = i;
         }
         else
         {
            y = x;
            x = -i;
         }

         break;
      }

      cell.secrete(new Secretion(transporter.hormone(x, y, h),
                                 new Orientation(CENTER), 1.0));
   }
}
