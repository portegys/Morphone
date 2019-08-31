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
 * Compound number.
 */
public class Compound
{
   public int[] value;

   // Constructors.
   public Compound()
   {
      value = new int[0];
   }


   public Compound(int[] v)
   {
      value = v;
   }


   // Equality.
   public boolean equals(Compound n)
   {
      int i;

      if (value.length != n.value.length)
      {
         return(false);
      }

      for (i = 0; i < value.length; i++)
      {
         if (value[i] != n.value[i])
         {
            return(false);
         }
      }

      return(true);
   }
}
