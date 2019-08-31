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
 * "Star" morphogen.
 */

// Star morphogen.
public class Star extends Morphogen
{
   // Hormone types.
   Compound INITIAL;
   Compound FLIP;
   Compound START;
   Compound ARM;
   Compound BASE;

   // Hormone type scoping.
   Scope scope;

   // Constructor.
   public Star()
   {
      scope   = ScopeFactory.newScope();
      INITIAL = scope.newValue();
      FLIP    = scope.newValue();
      START   = scope.newValue();
      ARM     = scope.newValue();
      BASE    = scope.newValue();
   }


   // Cell morphogenesis.
   public void morphCell(Cell cell)
   {
      int         i;
      int         j;
      Secretion   s;
      Orientation o;

      for (i = 0, j = cell.absorption.size(); i < j; i++)
      {
         s = (Secretion)cell.absorption.elementAt(i);

         if (s.hormone.type.equals(INITIAL))
         {
            cell.secrete(new Secretion(new Hormone(FLIP),
                                       new Orientation(SOUTH), 2.0));
            cell.secrete(new Secretion(new Hormone(START),
                                       new Orientation(NORTH), 2.0));
            cell.secrete(new Secretion(new Hormone(START),
                                       new Orientation(SOUTH), 2.0));

            continue;
         }

         if (s.hormone.type.equals(FLIP))
         {
            if (s.strength > 1.0)
            {
               cell.secrete(new Secretion(new Hormone(FLIP),
                                          s.orientation, s.strength - 1.0));
            }
            else
            {
               cell.orientation.rotate(4);
            }

            continue;
         }

         if (s.hormone.type.equals(START))
         {
            if (s.strength > 1.0)
            {
               cell.secrete(new Secretion(new Hormone(START),
                                          s.orientation, s.strength - 1.0));
            }
            else
            {
               cell.color = Color.black;
               o          = new Orientation(cell.orientation.offset(3));
               cell.secrete(new Secretion(new Hormone(ARM), o, 3.0));
               o = new Orientation(cell.orientation.offset(5), true);
               cell.secrete(new Secretion(new Hormone(ARM), o, 3.0));
            }

            continue;
         }

         if (s.hormone.type.equals(ARM))
         {
            cell.color = Color.black;

            if (s.strength > 1.0)
            {
               cell.secrete(new Secretion(new Hormone(ARM), s.orientation,
                                          s.strength - 1.0));
            }
            else
            {
               o = new Orientation(s.orientation.offset(3));
               cell.secrete(new Secretion(new Hormone(BASE), o, 3.0));
            }

            continue;
         }

         if (s.hormone.type.equals(BASE))
         {
            cell.color = Color.black;

            if (s.strength > 1.0)
            {
               cell.secrete(new Secretion(new Hormone(BASE),
                                          s.orientation, s.strength - 1.0));
            }

            continue;
         }
      }
   }
}
