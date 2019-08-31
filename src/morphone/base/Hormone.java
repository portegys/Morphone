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

import java.util.*;


/**
 * Morphone hormone.
 */

// Hormone.
public class Hormone
{
   public Compound type;
   public Vector   parameters;

   // Constructors.
   public Hormone(Compound t)
   {
      type       = t;
      parameters = new Vector();
   }


   public Hormone(Compound t, Vector p)
   {
      type       = t;
      parameters = p;
   }
}
