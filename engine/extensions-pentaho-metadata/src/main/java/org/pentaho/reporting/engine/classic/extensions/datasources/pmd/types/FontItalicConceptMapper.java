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

package org.pentaho.reporting.engine.classic.extensions.datasources.pmd.types;

import org.pentaho.metadata.model.concept.types.Font;
import org.pentaho.reporting.engine.classic.core.wizard.ConceptQueryMapper;
import org.pentaho.reporting.engine.classic.core.wizard.DataAttributeContext;

public class FontItalicConceptMapper implements ConceptQueryMapper {
  public static final ConceptQueryMapper INSTANCE = new FontItalicConceptMapper();
  public FontItalicConceptMapper() {
  }

  /**
   * @param value
   * @param type
   * @return
   */
  public Object getValue( final Object value, final Class type, final DataAttributeContext context ) {
    if ( value == null ) {
      return null;
    }

    if ( value instanceof Font == false ) {
      return null;
    }

    final Font fontSettings = (Font) value;
    if ( type == null || Object.class.equals( type ) || String.class.isAssignableFrom( type ) ) {
      return String.valueOf( fontSettings.isItalic() );
    }
    if ( Boolean.class.isAssignableFrom( type ) ) {
      if ( fontSettings.isItalic() ) {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }
    return null;
  }
}
