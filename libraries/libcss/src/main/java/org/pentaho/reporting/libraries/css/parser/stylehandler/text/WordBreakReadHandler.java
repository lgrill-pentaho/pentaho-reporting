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

import org.pentaho.reporting.libraries.css.keys.text.WordBreak;
import org.pentaho.reporting.libraries.css.parser.stylehandler.OneOfConstantsReadHandler;

/**
 * Creation-Date: 28.11.2005, 19:45:53
 *
 * @author Thomas Morgner
 */
public class WordBreakReadHandler extends OneOfConstantsReadHandler {
  public WordBreakReadHandler() {
    super( false );
    addValue( WordBreak.BREAK_ALL );
    addValue( WordBreak.BREAK_STRICT );
    addValue( WordBreak.KEEP_ALL );
    addValue( WordBreak.LOOSE );
    addValue( WordBreak.NORMAL );
  }
}
