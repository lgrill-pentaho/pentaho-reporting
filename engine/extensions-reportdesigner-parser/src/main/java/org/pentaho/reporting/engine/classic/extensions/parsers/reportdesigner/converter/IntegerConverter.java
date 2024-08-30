/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.reporting.engine.classic.extensions.parsers.reportdesigner.converter;

import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.xml.sax.Locator;

public class IntegerConverter implements ObjectConverter {
  public IntegerConverter() {
  }

  public Object convertFromString( final String s, final Locator locator ) throws ParseException {
    try {
      return Integer.valueOf( s.trim() );
    } catch ( NumberFormatException nfe ) {
      throw new ParseException( nfe.getMessage(), locator );
    }
  }
}
