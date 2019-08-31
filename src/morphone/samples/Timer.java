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
 * Timer morphogen - time-releases a cargo hormone.
 * <p>
 * TIMER hormone parameters: Timer, cargo hormone.
 */

// Timer morphogen.
public class Timer extends Morphogen
{
   // Hormone types.
   Compound TIMER;

   // Hormone type scoping.
   Scope scope;

   // Constructor.
   public Timer()
   {
      scope = ScopeFactory.newScope();
      TIMER = scope.newValue();
   }


   // Cell morphogenesis.
   @SuppressWarnings("unchecked")
   public void morphCell(Cell cell)
   {
      int       i;
      int       j;
      int       k;
      Vector    p;
      Secretion s;
      Hormone   h;

      for (i = 0, j = cell.absorption.size(); i < j; i++)
      {
         s = (Secretion)cell.absorption.elementAt(i);

         if (s.hormone.type.equals(TIMER))
         {
            // Continue timing?
            k = ((Integer)(s.hormone.parameters.elementAt(0))).intValue();

            if (k > 0)
            {
               k--;
               p = new Vector();
               p.addElement(new Integer(k));
               p.addElement((Hormone)s.hormone.parameters.elementAt(1));
               cell.secrete(new Secretion(new Hormone(TIMER, p),
                                          new Orientation(CENTER), 1.0));

               continue;
            }

            // Secrete payload.
            h = (Hormone)(s.hormone.parameters.elementAt(1));
            cell.secrete(new Secretion(h, new Orientation(CENTER), 1.0));
         }
      }
   }


   // Create a timer hormone.
   @SuppressWarnings("unchecked")
   Hormone hormone(int time, Hormone cargo)
   {
      Vector p = new Vector();

      p.addElement(new Integer(time));
      p.addElement(cargo);

      return(new Hormone(TIMER, p));
   }
}
