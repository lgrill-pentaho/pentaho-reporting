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

package org.pentaho.reporting.libraries.css.parser.stylehandler.text;

import org.pentaho.reporting.libraries.css.keys.text.GlyphOrientationHorizontal;
import org.pentaho.reporting.libraries.css.parser.stylehandler.OneOfConstantsReadHandler;

/**
 * Creation-Date: 02.12.2005, 18:48:50
 *
 * @author Thomas Morgner
 */
public class GlyphOrientationHorizontalReadHandler extends OneOfConstantsReadHandler {
  public GlyphOrientationHorizontalReadHandler() {
    super( true );
    addValue( GlyphOrientationHorizontal.INLINE );
  }
}
