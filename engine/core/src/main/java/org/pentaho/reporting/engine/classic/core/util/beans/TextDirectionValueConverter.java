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

package org.pentaho.reporting.engine.classic.core.util.beans;

import org.pentaho.reporting.engine.classic.core.style.TextDirection;

public class TextDirectionValueConverter implements ValueConverter {
  public TextDirectionValueConverter() {
  }

  public String toAttributeValue( final Object o ) throws BeanException {
    if ( o instanceof TextDirection ) {
      return String.valueOf( o );
    } else {
      throw new BeanException( "Failed to convert object of type " + o.getClass() + ": Not a TextWrap." );
    }
  }

  public Object toPropertyValue( final String o ) throws BeanException {
    if ( o == null ) {
      throw new NullPointerException();
    }

    if ( TextDirection.LTR.toString().equalsIgnoreCase( o ) ) {
      return TextDirection.LTR;
    }
    if ( TextDirection.RTL.toString().equalsIgnoreCase( o ) ) {
      return TextDirection.RTL;
    }
    throw new BeanException( "Invalid value specified for TextWrap" );
  }
}
