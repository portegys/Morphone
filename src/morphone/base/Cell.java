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
package morphone.base;

import java.awt.*;
import java.util.*;

/**
 * Morphone cell.
 */

// Cell.
public class Cell
{
   // Cell state.
   public Orientation orientation;
   public Color       color;
   public int         symbol;

   // Absorbed and secreted hormones.
   public Vector absorption;
   public        Vector[] secretions;

   // Cell constructor.
   public Cell(Orientation o, Color c)
   {
      int i;

      orientation = o;
      color       = c;
      symbol      = -1;
      absorption  = new Vector();
      secretions  = new Vector[9];

      for (i = 0; i < 9; i++)
      {
         secretions[i] = new Vector();
      }
   }


   // Secrete.
   @SuppressWarnings("unchecked")
   public void secrete(Secretion s)
   {
      secretions[s.orientation.direction].addElement(s);
   }
}
