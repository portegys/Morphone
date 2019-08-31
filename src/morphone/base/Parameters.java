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

/**
 * Morphone parameters.
 */
public interface Parameters
{
   // Grid dimension in cell units.
   static final Dimension DIMENSIONS = new Dimension(90, 90);

   //static final Dimension DIMENSIONS = new Dimension(120, 120);
   //static final Dimension DIMENSIONS = new Dimension(50, 50);
   // Directions.
   static final int NORTH     = 0;
   static final int NORTHEAST = 1;
   static final int EAST      = 2;
   static final int SOUTHEAST = 3;
   static final int SOUTH     = 4;
   static final int SOUTHWEST = 5;
   static final int WEST      = 6;
   static final int NORTHWEST = 7;
   static final int CENTER    = 8;

   // Background color.
   static final Color BGCOLOR = Color.white;
}
