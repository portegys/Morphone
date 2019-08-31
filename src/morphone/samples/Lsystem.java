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

import java.text.ParseException;

import java.util.*;


/**
 * L-System morphogen.
 * <p>
 * Implements the production: F->FF+[+F-F-F]-[-F+F+F]
 * expanded EXPANSION times.
 */

// L-System morphogen.
public class Lsystem extends Morphogen
{
   // L-System production.
   static final int    EXPANSION  = 1;
   static final String PRODUCTION = new String("FF+[+F-F-F]-[-F+F+F]");
   static final int    FORWARD    = 10; // Forward move magnitude.
   static final int    ANGLE      = 20; // Turn angle delta (degrees).

   // Commands.
   static final int DRAW        = 0;
   static final int MOVE        = 1;
   static final int LEFT_TURN   = 2;
   static final int RIGHT_TURN  = 3;
   static final int TURN_AROUND = 4;
   static final int MODULE      = 5;
   static final int EOS         = 6;

   // Productions.
   String Production;
   String NextProduction;
   String ProductionModule;

   // Hormone types.
   Compound INITIAL;
   Compound LSYSTEM;

   // Transporter.
   Transporter transporter;

   // Edge drawing.
   Edge edge;

   // Hormone type scoping.
   Scope scope;

   // Constructor.
   public Lsystem()
   {
      scope       = ScopeFactory.newScope();
      INITIAL     = scope.newValue();
      LSYSTEM     = scope.newValue();
      transporter = new Transporter();
      edge        = new Edge();
      Production  = expand(PRODUCTION, EXPANSION);
   }


   // Cell morphogenesis.
   public void morphCell(Cell cell)
   {
      int       i;
      int       j;
      int       x;
      int       y;
      Secretion s;
      int       angle;

      for (i = 0, j = cell.absorption.size(); i < j; i++)
      {
         s = (Secretion)cell.absorption.elementAt(i);

         if (s.hormone.type.equals(INITIAL))
         {
            y = (Parameters.DIMENSIONS.height / 2) - 2;

            if (y > 0)
            {
               y = -y;
            }
            else
            {
               y = 0;
            }

            cell.secrete(new Secretion(transporter.hormone(0, y,
                                                           hormone(Production, 90)), new Orientation(CENTER),
                                       1.0));

            continue;
         }

         if (s.hormone.type.equals(LSYSTEM))
         {
            Production = (String)(s.hormone.parameters.elementAt(0));
            angle      = ((Integer)(s.hormone.parameters.elementAt(1))).intValue();

            // Execute command.
            NextProduction = Production;

            while (NextProduction != null)
            {
               x = (int)(Math.cos(toRadians(angle)) * (double)FORWARD);
               y = (int)(Math.sin(toRadians(angle)) * (double)FORWARD);

               try {
                  switch (parse())
                  {
                  case DRAW:
                     cell.secrete(new Secretion(edge.hormone(x, y),
                                                new Orientation(CENTER), 1.0));

                  // Fall through.
                  case MOVE:

                     if (NextProduction != null)
                     {
                        cell.secrete(new Secretion(transporter.hormone(
                                                      x, y, hormone(NextProduction, angle)),
                                                   new Orientation(CENTER), 1.0));
                     }

                     NextProduction = null;

                     break;

                  case LEFT_TURN:
                     angle     += ANGLE;
                     angle      = angle % 360;
                     Production = NextProduction;

                     break;

                  case RIGHT_TURN:
                     angle -= ANGLE;

                     while (angle < 0)
                     {
                        angle += 360;
                     }

                     Production = NextProduction;

                     break;

                  case TURN_AROUND:
                     angle     += 180;
                     angle      = angle % 360;
                     Production = NextProduction;

                     break;

                  case MODULE:

                     if (ProductionModule != null)
                     {
                        cell.secrete(new Secretion(hormone(
                                                      ProductionModule, angle),
                                                   new Orientation(CENTER), 1.0));
                     }

                     Production = NextProduction;

                     break;

                  case EOS:
                     break;
                  }
               }
               catch (ParseException e) {
                  System.err.println("Invalid production: " + Production);

                  return;
               }
            }

            continue;
         }
      }

      // Transport and branch drawing.
      transporter.morphCell(cell);
      edge.morphCell(cell);
   }


   // Expand production.
   String expand(String production, int expansion)
   {
      int    i;
      int    j;
      int    l;
      String p;

      for (i = 0; i < expansion; i++)
      {
         p = new String();

         for (j = 0, l = production.length(); j < l; j++)
         {
            if (production.charAt(j) == 'F')
            {
               p = p.concat(production);
            }
            else
            {
               p = p.concat(production.substring(j, j + 1));
            }
         }

         production = p;
      }

      return(new String(production));
   }


   // Parse production.
   // Globals: Production, NextProduction, ProductionModule.
   int parse() throws ParseException
   {
      int    i;
      int    j;
      int    l;
      String command;

      NextProduction = ProductionModule = null;

      if (Production == null)
      {
         return(EOS);
      }

      command        = Production.substring(0, 1);
      NextProduction = Production.substring(1);

      if (NextProduction.length() == 0)
      {
         NextProduction = null;
      }

      if (command.equals("F"))
      {
         return(DRAW);
      }
      else if (command.equals("f"))
      {
         return(MOVE);
      }
      else if (command.equals("+"))
      {
         return(LEFT_TURN);
      }
      else if (command.equals("-"))
      {
         return(RIGHT_TURN);
      }
      else if (command.equals("!"))
      {
         return(TURN_AROUND);
      }

      // Isolate command module.
      if (((l = Production.length()) < 3) || (Production.charAt(0) != '['))
      {
         throw (new ParseException(Production, 0));
      }

      for (i = j = 1; (i < l) && (j > 0); i++)
      {
         switch (Production.charAt(i))
         {
         case '[':
            j++;

            break;

         case ']':
            j--;

            break;
         }
      }

      if ((j > 0) || (i == 1))
      {
         throw (new ParseException(Production, 0));
      }

      ProductionModule = Production.substring(1, i - 1);

      if (i < (l - 1))
      {
         NextProduction = Production.substring(i, l);
      }
      else
      {
         NextProduction = null;
      }

      return(MODULE);
   }


   // Convert degrees to radians.
   double toRadians(int angle)
   {
      return((double)angle * (Math.PI / 180.0));
   }


   // Create an Lsystem hormone.
   @SuppressWarnings("unchecked")
   Hormone hormone(String production, int angle)
   {
      Vector p = new Vector();

      p.addElement(production);
      p.addElement(new Integer(angle));

      return(new Hormone(LSYSTEM, p));
   }
}
