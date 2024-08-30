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

package org.pentaho.reporting.engine.classic.extensions.datasources.olap4j.types;

import org.pentaho.reporting.engine.classic.core.wizard.ConceptQueryMapper;
import org.pentaho.reporting.engine.classic.core.wizard.DataAttributeContext;

public class FontUnderlineConceptMapper implements ConceptQueryMapper {
  public FontUnderlineConceptMapper() {
  }

  public Object getValue( final Object value,
                          final Class type,
                          final DataAttributeContext context ) {
    if ( value == null ) {
      return null;
    }

    final int nvalue;
    if ( value instanceof String ) {
      try {
        nvalue = Integer.parseInt( String.valueOf( value ) );
      } catch ( NumberFormatException nfe ) {
        return null;
      }
    } else if ( value instanceof Number ) {
      final Number numvalue = (Number) value;
      nvalue = numvalue.intValue();
    } else {
      return null;
    }
    if ( Boolean.class.isAssignableFrom( type ) ) {
      if ( ( nvalue & 4 ) == 4 ) {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }

    if ( String.class.isAssignableFrom( type ) ) {
      if ( ( nvalue & 4 ) == 4 ) {
        return "true";
      }
      return "false";
    }

    return null;
  }
}
