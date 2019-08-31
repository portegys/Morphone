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
 * Bug morphogen.
 */

// Bug morphogen.
public class Bug extends Morphogen
{
   // Hormone types.
   Compound INITIAL;
   Compound HEAD;
   Compound EYE;
   Compound THORAX;
   Compound LEG;
   Compound ABDOMEN;

   // Transporter.
   Transporter transporter;

   // Edge drawing.
   Edge edge;

   // Balls.
   Ball ball;

   // Hormone type scoping.
   Scope scope;

   // Constructor.
   public Bug()
   {
      scope       = ScopeFactory.newScope();
      INITIAL     = scope.newValue();
      HEAD        = scope.newValue();
      EYE         = scope.newValue();
      THORAX      = scope.newValue();
      LEG         = scope.newValue();
      ABDOMEN     = scope.newValue();
      transporter = new Transporter();
      ball        = new Ball();
      edge        = new Edge();
   }


   // Cell morphogenesis.
   public void morphCell(Cell cell)
   {
      int       i;
      int       j;
      Secretion s;
      Vector    p;

      for (i = 0, j = cell.absorption.size(); i < j; i++)
      {
         s = (Secretion)cell.absorption.elementAt(i);

         if (s.hormone.type.equals(INITIAL))
         {
            cell.secrete(new Secretion(transporter.hormone(0, 10,
                                                           new Hormone(HEAD)), new Orientation(CENTER), 1.0));
            cell.secrete(new Secretion(transporter.hormone(0, 0,
                                                           new Hormone(THORAX)), new Orientation(CENTER), 1.0));
            cell.secrete(new Secretion(transporter.hormone(0, -15,
                                                           new Hormone(ABDOMEN)), new Orientation(CENTER), 1.0));

            continue;
         }

         if (s.hormone.type.equals(HEAD))
         {
            // Draw head, eyes, and antennae.
            cell.secrete(new Secretion(ball.hormone(4, 0, 0, Ball.SOLID),
                                       new Orientation(CENTER), 1.0));
            cell.secrete(new Secretion(transporter.hormone(3, 1,
                                                           new Hormone(EYE)), new Orientation(CENTER), 1.0));
            cell.secrete(new Secretion(transporter.hormone(-3, 1,
                                                           new Hormone(EYE)), new Orientation(CENTER), 1.0));
            cell.secrete(new Secretion(transporter.hormone(2, 4,
                                                           edge.hormone(2, 5)), new Orientation(CENTER), 1.0));
            cell.secrete(new Secretion(transporter.hormone(-2, 4,
                                                           edge.hormone(-2, 5)), new Orientation(CENTER), 1.0));

            continue;
         }

         if (s.hormone.type.equals(EYE))
         {
            cell.secrete(new Secretion(ball.hormone(2, 0, 0, Ball.GRID),
                                       new Orientation(CENTER), 1.0));

            continue;
         }

         if (s.hormone.type.equals(THORAX))
         {
            // Draw thorax and legs.
            cell.secrete(new Secretion(ball.hormone(6, 0, 0, Ball.SOLID),
                                       new Orientation(CENTER), 1.0));
            cell.secrete(new Secretion(transporter.hormone(6, 2,
                                                           edge.hormone(4, 7)), new Orientation(CENTER), 1.0));
            cell.secrete(new Secretion(transporter.hormone(7, 0,
                                                           edge.hormone(5, 2)), new Orientation(CENTER), 1.0));
            cell.secrete(new Secretion(transporter.hormone(6, -2,
                                                           edge.hormone(5, -6)), new Orientation(CENTER), 1.0));
            cell.secrete(new Secretion(transporter.hormone(-6, 2,
                                                           edge.hormone(-4, 7)), new Orientation(CENTER), 1.0));
            cell.secrete(new Secretion(transporter.hormone(-7, 0,
                                                           edge.hormone(-5, 2)), new Orientation(CENTER), 1.0));
            cell.secrete(new Secretion(transporter.hormone(-6, -2,
                                                           edge.hormone(-5, -6)), new Orientation(CENTER), 1.0));

            continue;
         }

         if (s.hormone.type.equals(ABDOMEN))
         {
            cell.secrete(new Secretion(ball.hormone(9, 0, 0,
                                                    Ball.HORIZONTAL_STRIPES), new Orientation(CENTER),
                                       1.0));

            continue;
         }
      }

      // Other morph processing.
      transporter.morphCell(cell);
      ball.morphCell(cell);
      edge.morphCell(cell);
   }
}
