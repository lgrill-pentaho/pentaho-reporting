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

package org.pentaho.reporting.libraries.base.util;

import java.io.Serializable;

public class FormattedMessage implements Serializable {
  private String pattern;
  private Object[] data;

  public FormattedMessage( String pattern, Object... data ) {
    ArgumentNullException.validate( "pattern", pattern );
    ArgumentNullException.validate( "data", data );

    this.pattern = pattern;
    this.data = data;
  }

  public String toString() {
    return String.format( pattern, data );
  }
}
