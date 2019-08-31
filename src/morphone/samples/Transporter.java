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
 * Transporter morphogen - transports a "cargo" hormone along path.
 * Cargo delivered at time |x|+|y|.
 * <p>
 * TRANSPORTER hormone parameters: Path vector, cargo hormone.
 */

// Transporter morphogen.
public class Transporter extends Morphogen
{
   // Hormone types.
   Compound TRANSPORTER;

   // Hormone type scoping.
   Scope scope;

   // Constructor.
   public Transporter()
   {
      scope       = ScopeFactory.newScope();
      TRANSPORTER = scope.newValue();
   }


   // Cell morphogenesis.
   public void morphCell(Cell cell)
   {
      int       i;
      int       j;
      Secretion s;
      Hormone   h;
      Dimension d;

      for (i = 0, j = cell.absorption.size(); i < j; i++)
      {
         s = (Secretion)cell.absorption.elementAt(i);

         if (s.hormone.type.equals(TRANSPORTER))
         {
            // Secrete cargo hormone?
            d = (Dimension)(s.hormone.parameters.elementAt(0));

            if ((d.width == 0) && (d.height == 0))
            {
               h = (Hormone)(s.hormone.parameters.elementAt(1));
               cell.secrete(new Secretion(h, new Orientation(CENTER), 1.0));

               continue;
            }

            // Move toward destination.
            if (d.width < 0)
            {
               d.width++;
               cell.secrete(new Secretion(s.hormone,
                                          new Orientation(WEST), 1.0));

               continue;
            }

            if (d.width > 0)
            {
               d.width--;
               cell.secrete(new Secretion(s.hormone,
                                          new Orientation(EAST), 1.0));

               continue;
            }

            if (d.height < 0)
            {
               d.height++;
               cell.secrete(new Secretion(s.hormone,
                                          new Orientation(SOUTH), 1.0));

               continue;
            }

            if (d.height > 0)
            {
               d.height--;
               cell.secrete(new Secretion(s.hormone,
                                          new Orientation(NORTH), 1.0));

               continue;
            }
         }
      }
   }


   // Create a transporter hormone.
   @SuppressWarnings("unchecked")
   Hormone hormone(int x, int y, Hormone cargo)
   {
      Vector p = new Vector();

      p.addElement(new Dimension(x, y));
      p.addElement(cargo);

      return(new Hormone(TRANSPORTER, p));
   }
}
;
