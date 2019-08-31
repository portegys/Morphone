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
 * Value scoping.
 */
public class Scope
{
   private int scope;
   private int next;

   // Constructor.
   public Scope(int s)
   {
      scope = s;
      next  = 0;
   }


   // New scoped value.
   public Compound newValue()
   {
      int[] v;

      v    = new int[2];
      v[0] = next;
      next++;
      v[1] = scope;

      return(new Compound(v));
   }
}
