/*! ******************************************************************************
 *
 * Pentaho Community Edition
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.reporting.libraries.css.counter.glyph;

import org.pentaho.reporting.libraries.css.counter.CounterStyle;

public class SquareCounterStyle implements CounterStyle {
  public SquareCounterStyle() {
  }

  public String getCounterValue( final int index ) {
    return "\u25fc";
  }

  public String getSuffix() {
    return "";
  }

}
