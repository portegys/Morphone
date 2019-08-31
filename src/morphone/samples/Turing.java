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
 * Turing Machine morphogen.
 */

// Turing Machine morphogen.
public class Turing extends Morphogen
{
   // Turing machine types.
   final static int COUNTER = 0;
   final static int ADDER   = 1;
   final static int type    = ADDER;

   // Special symbols - see Morphone.java
   static final int NULL    = -1;
   static final int START   = -2;
   static final int BLANK   = -3;
   static final int PERCENT = -4;
   static final int POUND   = -5;
   static final int CIRCLE  = -6;
   static final int DISC    = -7;

   // Hormone types.
   Compound INITIAL;
   Compound EGG;
   Compound VALUE;
   Compound MOVE;
   Compound LEFT;
   Compound RIGHT;
   Compound SENSE;
   Compound READ;
   Compound WRITE;
   Compound STATE;
   Compound CURRENT;

   // State table.
   State[] states;

   // Initial tape.
   int[] tape;

   // Tape extent.
   int tapeExtent;

   // Transporter.
   Transporter transporter;

   // Timer.
   Timer timer;

   // Hormone type scoping.
   Scope scope;

   // Constructor.
   @SuppressWarnings("unchecked")
   public Turing()
   {
      int   i;
      int   j;
      State s;

      scope       = ScopeFactory.newScope();
      INITIAL     = scope.newValue();
      EGG         = scope.newValue();
      VALUE       = scope.newValue();
      SENSE       = scope.newValue();
      READ        = scope.newValue();
      WRITE       = scope.newValue();
      MOVE        = scope.newValue();
      LEFT        = scope.newValue();
      RIGHT       = scope.newValue();
      STATE       = scope.newValue();
      CURRENT     = scope.newValue();
      transporter = new Transporter();
      timer       = new Timer();

      // Binary counter:
      if (type == COUNTER)
      {
         // State table.
         states    = new State[2];
         states[0] = s = new State(0);
         s.io.addElement(new StateIO(START, 1, NULL, 1));
         s.io.addElement(new StateIO(0, 0, NULL, 0));
         s.io.addElement(new StateIO(1, 0, NULL, 0));
         s.io.addElement(new StateIO(BLANK, 0, NULL, 0));
         states[1] = s = new State(1);
         s.io.addElement(new StateIO(1, 1, 0, 1));
         s.io.addElement(new StateIO(0, 0, 1, 0));
         s.io.addElement(new StateIO(BLANK, 0, 1, 0));

         // Initial tape.
         tapeExtent = (Parameters.DIMENSIONS.width / 2) + 1;
         tape       = new int[6];
         tape[0]    = START;
         tape[1]    = 1;
         tape[2]    = 0;
         tape[3]    = 0;
         tape[4]    = 1;
         tape[5]    = 0;
      }

      // Binary adder:
      if (type == ADDER)
      {
         // State table.
         states    = new State[9];
         states[0] = s = new State(0);
         s.io.addElement(new StateIO(POUND, 4, 0, 1));
         s.io.addElement(new StateIO(START, 0, NULL, 1));
         s.io.addElement(new StateIO(PERCENT, 0, NULL, 1));
         s.io.addElement(new StateIO(0, 0, NULL, 1));
         s.io.addElement(new StateIO(1, 0, NULL, 1));
         s.io.addElement(new StateIO(BLANK, 7, 0, 0));
         states[1] = s = new State(1);
         s.io.addElement(new StateIO(POUND, 5, 0, 1));
         s.io.addElement(new StateIO(START, 1, NULL, 1));
         s.io.addElement(new StateIO(PERCENT, 1, NULL, 1));
         s.io.addElement(new StateIO(0, 1, NULL, 1));
         s.io.addElement(new StateIO(1, 1, NULL, 1));
         s.io.addElement(new StateIO(BLANK, 7, 1, 0));
         states[2] = s = new State(2);
         s.io.addElement(new StateIO(POUND, 6, 0, 1));
         s.io.addElement(new StateIO(START, 2, NULL, 1));
         s.io.addElement(new StateIO(PERCENT, 2, NULL, 1));
         s.io.addElement(new StateIO(0, 2, NULL, 1));
         s.io.addElement(new StateIO(1, 2, NULL, 1));
         s.io.addElement(new StateIO(BLANK, 8, 0, 0));
         states[3] = s = new State(3);
         s.io.addElement(new StateIO(BLANK, 8, 1, 0));
         states[4] = s = new State(4);
         s.io.addElement(new StateIO(0, 0, POUND, 1));
         s.io.addElement(new StateIO(1, 1, POUND, 1));
         s.io.addElement(new StateIO(PERCENT, 0, NULL, 1));
         states[5] = s = new State(5);
         s.io.addElement(new StateIO(0, 1, POUND, 1));
         s.io.addElement(new StateIO(1, 2, POUND, 1));
         s.io.addElement(new StateIO(PERCENT, 1, NULL, 1));
         states[6] = s = new State(6);
         s.io.addElement(new StateIO(0, 2, POUND, 1));
         s.io.addElement(new StateIO(1, 3, POUND, 1));
         s.io.addElement(new StateIO(PERCENT, 2, NULL, 1));
         states[7] = s = new State(7);
         s.io.addElement(new StateIO(START, 0, NULL, 1));
         s.io.addElement(new StateIO(POUND, 7, NULL, 0));
         s.io.addElement(new StateIO(PERCENT, 7, NULL, 0));
         s.io.addElement(new StateIO(0, 7, NULL, 0));
         s.io.addElement(new StateIO(1, 7, NULL, 0));
         s.io.addElement(new StateIO(BLANK, 7, NULL, 0));
         states[8] = s = new State(8);
         s.io.addElement(new StateIO(START, 1, NULL, 1));
         s.io.addElement(new StateIO(POUND, 8, NULL, 0));
         s.io.addElement(new StateIO(PERCENT, 8, NULL, 0));
         s.io.addElement(new StateIO(0, 8, NULL, 0));
         s.io.addElement(new StateIO(1, 8, NULL, 0));
         s.io.addElement(new StateIO(BLANK, 8, NULL, 0));

         // Initial tape.
         tapeExtent = (Parameters.DIMENSIONS.width / 2) + 1;
         tape       = new int[10];
         tape[0]    = START;
         tape[1]    = POUND;
         tape[2]    = 1;
         tape[3]    = 1;
         tape[4]    = PERCENT;
         tape[5]    = POUND;
         tape[6]    = 1;
         tape[7]    = 0;
         tape[8]    = 1;
         tape[9]    = PERCENT;
      }
   }


   // Cell morphogenesis.
   @SuppressWarnings("unchecked")
   public void morphCell(Cell cell)
   {
      int       i;
      int       j;
      int       k;
      int       t;
      Secretion s;
      Vector    p;
      int       value;
      int       read;
      int       write;
      boolean   sense;
      boolean   left;
      boolean   right;
      boolean   current;
      State     state;
      StateIO   io;
      Compound  move;
      Hormone   h;

      cell.color  = Parameters.BGCOLOR;
      cell.symbol = NULL;
      value       = read = write = NULL;
      sense       = left = right = current = false;
      state       = null;
      io          = null;
      move        = null;

      for (i = 0, j = cell.absorption.size(); i < j; i++)
      {
         s = (Secretion)cell.absorption.elementAt(i);

         if (s.hormone.type.equals(INITIAL))
         {
            k = (Parameters.DIMENSIONS.height / 2) - 2;

            if (k > 0)
            {
               k = -k;
            }
            else
            {
               k = 0;
            }

            cell.secrete(new Secretion(transporter.hormone(0, k,
                                                           new Hormone(EGG)), new Orientation(CENTER), 1.0));

            continue;
         }

         if (s.hormone.type.equals(EGG))
         {
            // Create tape.
            for (k = 0; k < tape.length; k++)
            {
               p = new Vector();
               p.addElement(new Integer(tape[k]));
               t = tapeExtent - k;
               h = timer.hormone(t, new Hormone(VALUE, p));
               cell.secrete(new Secretion(transporter.hormone(-k, 0, h),
                                          new Orientation(CENTER), 1.0));
            }

            for ( ; k < tapeExtent; k++)
            {
               p = new Vector();
               p.addElement(new Integer(BLANK));
               t = tapeExtent - k;
               h = timer.hormone(t, new Hormone(VALUE, p));
               cell.secrete(new Secretion(transporter.hormone(-k, 0, h),
                                          new Orientation(CENTER), 1.0));
            }

            for (k = 1; k < tapeExtent; k++)
            {
               p = new Vector();
               p.addElement(new Integer(BLANK));
               t = tapeExtent - k;
               h = timer.hormone(t, new Hormone(VALUE, p));
               cell.secrete(new Secretion(transporter.hormone(k, 0, h),
                                          new Orientation(CENTER), 1.0));
            }

            // Create state table.
            for (k = 0; k < states.length; k++)
            {
               p = new Vector();
               p.addElement(states[k]);
               cell.secrete(new Secretion(transporter.hormone(0, k + 1,
                                                              new Hormone(STATE, p)),
                                          new Orientation(CENTER), 1.0));
            }

            // Start machine.
            h = timer.hormone(tapeExtent, new Hormone(CURRENT));
            cell.secrete(new Secretion(transporter.hormone(0, 1, h),
                                       new Orientation(CENTER), 1.0));

            continue;
         }

         if (s.hormone.type.equals(VALUE))
         {
            value = ((Integer)(s.hormone.parameters.elementAt(0))).intValue();

            continue;
         }

         if (s.hormone.type.equals(SENSE))
         {
            sense = true;

            continue;
         }

         if (s.hormone.type.equals(READ))
         {
            read = ((Integer)(s.hormone.parameters.elementAt(0))).intValue();

            continue;
         }

         if (s.hormone.type.equals(WRITE))
         {
            write = ((Integer)(s.hormone.parameters.elementAt(0))).intValue();

            continue;
         }

         if (s.hormone.type.equals(LEFT))
         {
            left = true;

            continue;
         }

         if (s.hormone.type.equals(RIGHT))
         {
            right = true;

            continue;
         }

         if (s.hormone.type.equals(MOVE))
         {
            move = (Compound)(s.hormone.parameters.elementAt(0));

            continue;
         }

         if (s.hormone.type.equals(STATE))
         {
            state = (State)(s.hormone.parameters.elementAt(0));

            continue;
         }

         if (s.hormone.type.equals(CURRENT))
         {
            current = true;

            continue;
         }
      }

      // Tape processing.
      if (value != NULL)
      {
         // Sensing value?
         if (sense)
         {
            // Read value.
            p = new Vector();
            p.addElement(new Integer(value));
            cell.secrete(new Secretion(new Hormone(READ, p),
                                       new Orientation(NORTH), 1.0));
         }

         // Writing value?
         if (write != NULL)
         {
            value = write;
         }

         // Move value left?
         if (left)
         {
            p = new Vector();
            p.addElement(new Integer(value));
            cell.secrete(new Secretion(new Hormone(VALUE, p),
                                       new Orientation(WEST), 1.0));
         }
         // Move value right?
         else if (right)
         {
            p = new Vector();
            p.addElement(new Integer(value));
            cell.secrete(new Secretion(new Hormone(VALUE, p),
                                       new Orientation(EAST), 1.0));
         }
         else
         {
            // Refresh value.
            p = new Vector();
            p.addElement(new Integer(value));
            cell.secrete(new Secretion(new Hormone(VALUE, p),
                                       new Orientation(CENTER), 1.0));

            // Set for display.
            cell.symbol = value;
         }

         // Move tape?
         if (move != null)
         {
            // Distribute LEFT or RIGHT along tape.
            h = timer.hormone(tapeExtent, new Hormone(move));
            cell.secrete(new Secretion(transporter.hormone(0, 0, h),
                                       new Orientation(CENTER), 1.0));

            for (j = 1; j < tapeExtent; j++)
            {
               t = tapeExtent - j;
               h = timer.hormone(t, new Hormone(move));
               cell.secrete(new Secretion(transporter.hormone(j, 0, h),
                                          new Orientation(CENTER), 1.0));
               h = timer.hormone(t, new Hormone(move));
               cell.secrete(new Secretion(transporter.hormone(-j, 0, h),
                                          new Orientation(CENTER), 1.0));
            }

            // Fill blank into shifted-in cell.
            if (move.equals(LEFT))
            {
               if ((tapeExtent % 2) == 1)
               {
                  t = 2;
                  j = tapeExtent - 1;
               }
               else
               {
                  t = 3;
                  j = tapeExtent - 2;
               }
            }
            else
            {
               t = 2;
               j = -(tapeExtent - 1);
            }

            p = new Vector();
            p.addElement(new Integer(BLANK));
            h = timer.hormone(t, new Hormone(VALUE, p));
            cell.secrete(new Secretion(transporter.hormone(j, 0, h),
                                       new Orientation(CENTER), 1.0));
         }
      }
      else
      {
         // Propagate southbound signals.
         if (sense)
         {
            cell.secrete(new Secretion(new Hormone(SENSE),
                                       new Orientation(SOUTH), 1.0));
         }

         if (write != NULL)
         {
            p = new Vector();
            p.addElement(new Integer(write));
            cell.secrete(new Secretion(new Hormone(WRITE, p),
                                       new Orientation(SOUTH), 1.0));
         }

         if (move != null)
         {
            p = new Vector();
            p.addElement(move);
            cell.secrete(new Secretion(new Hormone(MOVE, p),
                                       new Orientation(SOUTH), 1.0));
         }
      }

      // State processing.
      if (state != null)
      {
         // Current state?
         if (state.current)
         {
            cell.symbol = DISC;

            if (read != NULL)
            {
               // Find state I/O matching read value.
               for (i = 0, j = state.io.size(); i < j; i++)
               {
                  io = (StateIO)(state.io.elementAt(i));

                  if (io.read == read)
                  {
                     break;
                  }
               }

               if (i == j)
               {
                  return;       // error.
               }

               // Write?
               if (io.write != NULL)
               {
                  p = new Vector();
                  p.addElement(new Integer(io.write));
                  cell.secrete(new Secretion(new Hormone(WRITE, p),
                                             new Orientation(SOUTH), 1.0));
               }

               // Move tape?
               if (io.move != NULL)
               {
                  // Set timer to wait for move completion.
                  t = tapeExtent + state.number + 1;
                  p = new Vector();

                  if (io.move == 0)
                  {
                     p.addElement(LEFT);
                  }
                  else
                  {
                     p.addElement(RIGHT);
                  }

                  cell.secrete(new Secretion(new Hormone(MOVE, p),
                                             new Orientation(SOUTH), 1.0));
               }
               else
               {
                  t = state.number + 1;
               }

               // Make next state current.
               h = timer.hormone(t, new Hormone(CURRENT));
               i = io.next - state.number;
               cell.secrete(new Secretion(transporter.hormone(0, i, h),
                                          new Orientation(CENTER), 1.0));

               state.current = false;

               read = NULL;
            }
         }
         else
         {
            cell.symbol = CIRCLE;
         }

         // New current state?
         if (current)
         {
            state.current = true;

            if (state.io.size() != 0)
            {
               // Issue sense command.
               cell.secrete(new Secretion(new Hormone(SENSE),
                                          new Orientation(SOUTH), 1.0));
            }
         }

         p = new Vector();
         p.addElement(state);
         cell.secrete(new Secretion(new Hormone(STATE, p),
                                    new Orientation(CENTER), 1.0));
      }

      // Propagate read signal.
      if (read != NULL)
      {
         p = new Vector();
         p.addElement(new Integer(read));
         cell.secrete(new Secretion(new Hormone(READ, p),
                                    new Orientation(NORTH), 1.0));
      }

      // Transport and timer processing.
      transporter.morphCell(cell);
      timer.morphCell(cell);
   }


   // State element.
   class State
   {
      boolean current;
      int     number;

      // State I/O: if empty then state is final.
      Vector io;

      // Constructor.
      State(int n)
      {
         current = false;
         number  = n;
         io      = new Vector();
      }
   }

   // State I/O
   class StateIO
   {
      int read;
      int next;
      int write;

      // left=0, right=1
      int move;

      // Constructor.
      StateIO(int r, int n, int w, int m)
      {
         read  = r;
         next  = n;
         write = w;
         move  = m;
      }
   }
}
