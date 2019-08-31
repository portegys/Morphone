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
 * "Spiraling Glider Guns" morphogen.
 */

// Spiraling Glider Guns morphogen.
public class SpiralingGliderGuns extends Morphogen
{
   // Spiral parameters.
   static final int INITIAL_LENGTH   = 25;
   static final int LENGTH_INCREMENT = 10;
   static final int TURN_ANGLE       = 6;
   static final int NUMBER_TURNS     = 4;

   // Hormone types.
   Compound INITIAL;
   Compound SPIRAL;

   // Glider Gun.
   GliderGun gliderGun;

   // Hormone type scoping.
   Scope scope;

   // Constructor.
   public SpiralingGliderGuns()
   {
      scope     = ScopeFactory.newScope();
      INITIAL   = scope.newValue();
      SPIRAL    = scope.newValue();
      gliderGun = new GliderGun();
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
            cell.color = Color.lightGray;
            p          = new Vector();
            p.addElement(new SpiralState());
            cell.secrete(new Secretion(new Hormone(SPIRAL, p),
                                       new Orientation(NORTH), (double)INITIAL_LENGTH));

            continue;
         }

         if (s.hormone.type.equals(SPIRAL))
         {
            cell.color = Color.lightGray;

            if (s.strength > 1.0)
            {
               // Continue building segment.
               cell.secrete(new Secretion(
                               new Hormone(SPIRAL, s.hormone.parameters),
                               s.orientation, s.strength - 1.0));
            }
            else
            {
               // Orient cell for gun.
               switch (s.orientation.direction)
               {
               case NORTH:
               case EAST:
               case SOUTH:
               case WEST:
                  cell.orientation.direction = s.orientation.direction;
               }

               // Turn spiral.
               r = (SpiralState)(s.hormone.parameters.elementAt(0));
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

               // Create Glider Gun.
               cell.secrete(new Secretion(new Hormone(gliderGun.INITIAL),
                                          new Orientation(CENTER), 1.0));
            }

            continue;
         }
      }

      // Glider Gun morphing.
      gliderGun.morphCell(cell);
   }


   // Spiral state.
   class SpiralState
   {
      int initialLength   = INITIAL_LENGTH;
      int lengthIncrement = LENGTH_INCREMENT;
      int turnAngle       = TURN_ANGLE;
      int numberTurns     = NUMBER_TURNS;
      int length;
      int turnCount;

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

         return(s);
      }
   }
}
