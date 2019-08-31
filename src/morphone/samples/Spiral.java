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
 * "Spiral" morphogen.
 */

// Spiral morphogen.
public class Spiral extends Morphogen
{
   // Spiral parameters.
   static final int INITIAL_LENGTH   = 2;
   static final int LENGTH_INCREMENT = 1;
   static final int TURN_ANGLE       = 1;
   static final int NUMBER_TURNS     = 7;

   // Hormone types.
   Compound INITIAL;
   Compound SPIRAL;

   // Hormone type scoping.
   Scope scope;

   // Constructor.
   public Spiral()
   {
      scope   = ScopeFactory.newScope();
      INITIAL = scope.newValue();
      SPIRAL  = scope.newValue();
   }


   // Cell morphogenesis.
   @SuppressWarnings("unchecked")
   public void morphCell(Cell cell)
   {
      int         i;
      int         j;
      Secretion   s;
      Vector      p;
      SpiralState r;

      for (i = 0, j = cell.absorption.size(); i < j; i++)
      {
         s = (Secretion)cell.absorption.elementAt(i);

         if (s.hormone.type.equals(INITIAL))
         {
            cell.color = Color.black;
            p          = new Vector();
            p.addElement(new SpiralState(INITIAL_LENGTH, 0));
            cell.secrete(new Secretion(new Hormone(SPIRAL, p),
                                       new Orientation(NORTH), (double)INITIAL_LENGTH));

            continue;
         }

         if (s.hormone.type.equals(SPIRAL))
         {
            cell.color = Color.black;

            if (s.strength > 1.0)
            {
               cell.secrete(new Secretion(
                               new Hormone(SPIRAL, s.hormone.parameters),
                               s.orientation, s.strength - 1.0));
            }
            else
            {
               r = (SpiralState)(s.hormone.parameters.elementAt(0));
               r.turnCount++;

               if (r.turnCount > NUMBER_TURNS)
               {
                  break;
               }

               s.orientation.rotate(TURN_ANGLE);
               r.length += LENGTH_INCREMENT;
               cell.secrete(new Secretion(
                               new Hormone(SPIRAL, s.hormone.parameters),
                               s.orientation, (double)r.length));
            }

            continue;
         }
      }
   }


   // Spiral state.
   class SpiralState
   {
      int length;
      int turnCount;

      // Constructor.
      SpiralState(int l, int t)
      {
         length    = l;
         turnCount = t;
      }
   }
}
