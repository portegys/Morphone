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

/**
 * Morphogen - base class.
 */

// Morphogen.
public class Morphogen implements Parameters
{
   // Hormone types.
   public Compound INITIAL;

   // Constructor.
   public Morphogen()
   {
      int[] v;

      v       = new int[2];
      v[0]    = v[1] = 0;
      INITIAL = new Compound(v);
   }


   // Cell morphogenesis.
   public void morphCell(Cell cell)
   {
   }
}
