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

/**
 * A class that handles the conversion of {@link Double} attributes to and from their {@link String} representation.
 *
 * @author Thomas Morgner
 */
public class DoubleValueConverter implements ValueConverter {

  /**
   * Creates a new value converter.
   */
  public DoubleValueConverter() {
  }

  /**
   * Converts the attribute to a string.
   *
   * @param o
   *          the attribute ({@link Double} expected).
   * @return A string representing the {@link Double} value.
   */
  public String toAttributeValue( final Object o ) throws BeanException {
    if ( o == null ) {
      throw new NullPointerException();
    }
    if ( o instanceof Double ) {
      return o.toString();
    }
    throw new BeanException( "Failed to convert object of type " + o.getClass() + ": Not a Double." );
  }

  /**
   * Converts a string to a {@link Double}.
   *
   * @param s
   *          the string.
   * @return a {@link Double}.
   */
  public Object toPropertyValue( final String s ) throws BeanException {
    if ( s == null ) {
      throw new NullPointerException();
    }
    final String val = s.trim();
    if ( val.length() == 0 ) {
      throw BeanException.getInstance( "Failed to convert empty string to number", null );
    }

    try {
      return new Double( val );
    } catch ( NumberFormatException be ) {
      throw BeanException.getInstance( "Failed to parse number", be );
    }
  }
}
