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
 * "Fractal Spiral" morphogen.
 */

// Fractal Spiral morphogen.
public class FractalSpiral extends Morphogen
{
   // Spiral parameters.
   static final int INITIAL_LENGTH         = 10;
   static final int LENGTH_INCREMENT       = 8;
   static final int TURN_ANGLE             = 6;
   static final int NUMBER_TURNS           = 9;
   static final int INITIAL_LENGTH_DELTA   = -4;
   static final int LENGTH_INCREMENT_DELTA = -3;
   static final int TURN_ANGLE_DELTA       = 0;
   static final int NUMBER_TURNS_DELTA     = 0;

   // Hormone types.
   Compound INITIAL;
   Compound SPIRAL;

   // Hormone type scoping.
   Scope scope;

   // Constructor.
   public FractalSpiral()
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
            p.addElement(new SpiralState());
            cell.secrete(new Secretion(new Hormone(SPIRAL, p),
                                       new Orientation(NORTH), (double)INITIAL_LENGTH));

            continue;
         }

         if (s.hormone.type.equals(SPIRAL))
         {
            r          = (SpiralState)(s.hormone.parameters.elementAt(0));
            cell.color = r.color;

            if (s.strength > 1.0)
            {
               // Continue building segment.
               cell.secrete(new Secretion(
                               new Hormone(SPIRAL, s.hormone.parameters),
                               s.orientation, s.strength - 1.0));
            }
            else
            {
               // Turn spiral.
               r.turnCount++;

               if (r.turnCount > r.numberTurns)
               {
                  break;
               }

               s.orientation.rotate(r.turnAngle);
               r.length += r.lengthIncrement;
               cell.secrete(new Secretion(
                               new Hormone(SPIRAL, s.hormone.parameters),
                               s.orientation, (double)r.length));

               // Create fractal spiral.
               r = r.copy();

               if (r.delta())
               {
                  p = new Vector();
                  p.addElement(r);
                  cell.secrete(new Secretion(new Hormone(SPIRAL, p),
                                             new Orientation(NORTH), (double)r.initialLength));
               }
            }

            continue;
         }
      }
   }


   // Spiral state.
   class SpiralState
   {
      int   initialLength   = INITIAL_LENGTH;
      int   lengthIncrement = LENGTH_INCREMENT;
      int   turnAngle       = TURN_ANGLE;
      int   numberTurns     = NUMBER_TURNS;
      int   length;
      int   turnCount;
      Color color = Color.black;

      // Constructor.
      SpiralState()
      {
         length    = initialLength;
         turnCount = 0;
      }


      // Create copy.
      SpiralState copy()
      {
         SpiralState s;

         s = new SpiralState();
         s.initialLength   = this.initialLength;
         s.lengthIncrement = this.lengthIncrement;
         s.turnAngle       = this.turnAngle;
         s.numberTurns     = this.numberTurns;
         s.length          = s.initialLength;
         s.turnCount       = 0;

         if (color == Color.black)
         {
            s.color = Color.gray;
         }
         else if (color == Color.gray)
         {
            s.color = Color.lightGray;
         }
         else
         {
            s.color = Color.black;
         }

         return(s);
      }


      // Apply deltas to parameters.
      // Return true if modified parameters are valid.
      boolean delta()
      {
         initialLength   += INITIAL_LENGTH_DELTA;
         lengthIncrement += LENGTH_INCREMENT_DELTA;
         turnAngle       += TURN_ANGLE_DELTA;

         while (turnAngle < 0)
         {
            turnAngle += 8;
         }

         while (turnAngle > 7)
         {
            turnAngle -= 8;
         }

         numberTurns += NUMBER_TURNS_DELTA;
         length       = initialLength;
         turnCount    = 0;

         if (initialLength <= 0)
         {
            return(false);
         }

         if (lengthIncrement < 0)
         {
            return(false);
         }

         if (numberTurns <= 0)
         {
            return(false);
         }

         return(true);
      }
   }
}
