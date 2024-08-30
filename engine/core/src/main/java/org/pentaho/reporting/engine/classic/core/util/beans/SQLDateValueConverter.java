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

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Creation-Date: 09.10.2007, 19:00:02
 *
 * @author Thomas Morgner
 */
public class SQLDateValueConverter implements ValueConverter {
  private SimpleDateFormat dateFormat;

  public SQLDateValueConverter() {
    dateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US );
    dateFormat.setLenient( false );
  }

  public String toAttributeValue( final Object o ) throws BeanException {
    if ( o == null ) {
      throw new NullPointerException();
    }
    if ( o instanceof Date == false ) {
      throw new BeanException( "Failed to convert object of type " + o.getClass() + ": Not a SQL-Date." );
    }
    final DateFormat format = (DateFormat) dateFormat.clone();
    return format.format( (Date) o );
  }

  public Object toPropertyValue( final String s ) throws BeanException {
    if ( s == null ) {
      throw new NullPointerException();
    }
    try {
      final DateFormat format = (DateFormat) dateFormat.clone();
      final java.util.Date date = format.parse( s );
      return new Date( date.getTime() );
    } catch ( ParseException e ) {
      throw new BeanException( "Not a parsable SQL-date" );
    }
  }
}
