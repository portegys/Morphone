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
 * Morphone cell orientation.
 */

// Orientation.
public class Orientation
{
   public int     direction;
   public boolean mirrored;

   // Constructors.
   public Orientation()
   {
      direction = 0;
      mirrored  = false;
   }


   public Orientation(int d)
   {
      direction = d;
      mirrored  = false;
   }


   public Orientation(int d, boolean m)
   {
      direction = d;
      mirrored  = m;
   }


   public Orientation(Orientation o1, Orientation o2)
   {
      if (o1.mirrored)
      {
         if (o2.mirrored)
         {
            mirrored = false;
         }
         else
         {
            mirrored = true;
         }
      }
      else
      {
         if (o2.mirrored)
         {
            mirrored = true;
         }
         else
         {
            mirrored = false;
         }
      }

      if (mirrored)
      {
         if ((direction = o1.direction - o2.direction) < 0)
         {
            direction += 8;
         }
      }
      else
      {
         if ((direction = o1.direction + o2.direction) > 7)
         {
            direction -= 8;
         }
      }
   }


   // Rotate.
   public void rotate(int o)
   {
      direction = offset(o);
   }


   // Orientation offset.
   public int offset(int o)
   {
      int i;

      if (mirrored)
      {
         if ((i = direction - o) < 0)
         {
            i += 8;
         }
      }
      else
      {
         if ((i = direction + o) > 7)
         {
            i -= 8;
         }
      }

      return(i);
   }
}
