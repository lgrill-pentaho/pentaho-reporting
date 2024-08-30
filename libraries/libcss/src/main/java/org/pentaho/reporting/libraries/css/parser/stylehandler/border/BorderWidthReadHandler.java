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

package org.pentaho.reporting.libraries.css.parser.stylehandler.border;

import org.pentaho.reporting.libraries.css.keys.border.BorderStyleKeys;
import org.pentaho.reporting.libraries.css.keys.border.BorderWidth;
import org.pentaho.reporting.libraries.css.model.StyleKey;
import org.pentaho.reporting.libraries.css.parser.CSSCompoundValueReadHandler;
import org.pentaho.reporting.libraries.css.parser.stylehandler.AbstractWidthReadHandler;
import org.pentaho.reporting.libraries.css.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

import java.util.HashMap;
import java.util.Map;

/**
 * Creation-Date: 27.11.2005, 19:07:11
 *
 * @author Thomas Morgner
 */
public class BorderWidthReadHandler extends AbstractWidthReadHandler
  implements CSSCompoundValueReadHandler {
  public BorderWidthReadHandler() {
    super( true, false );
  }

  protected BorderWidthReadHandler( boolean allowPercentages, boolean allowAuto ) {
    super( allowPercentages, allowAuto );
  }

  protected CSSValue parseWidth( final LexicalUnit value ) {
    if ( value.getLexicalUnitType() == LexicalUnit.SAC_IDENT ) {
      if ( value.getStringValue().equalsIgnoreCase( "thin" ) ) {
        return BorderWidth.THIN;
      }
      if ( value.getStringValue().equalsIgnoreCase( "medium" ) ) {
        return BorderWidth.MEDIUM;
      }
      if ( value.getStringValue().equalsIgnoreCase( "thick" ) ) {
        return BorderWidth.THICK;
      }
    }
    return super.parseWidth( value );
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues( LexicalUnit unit ) {
    final CSSValue topWidth = parseWidth( unit );
    if ( topWidth == null ) {
      return null;
    }

    unit = unit.getNextLexicalUnit();

    final CSSValue rightWidth;
    if ( unit == null ) {
      rightWidth = topWidth;
    } else {
      rightWidth = parseWidth( unit );
      if ( rightWidth == null ) {
        return null;
      }
      unit = unit.getNextLexicalUnit();
    }

    final CSSValue bottomWidth;
    if ( unit == null ) {
      bottomWidth = topWidth;
    } else {
      bottomWidth = parseWidth( unit );
      if ( bottomWidth == null ) {
        return null;
      }
      unit = unit.getNextLexicalUnit();
    }

    final CSSValue leftWidth;
    if ( unit == null ) {
      leftWidth = rightWidth;
    } else {
      leftWidth = parseWidth( unit );
      if ( leftWidth == null ) {
        return null;
      }
    }

    final Map map = new HashMap();
    map.put( BorderStyleKeys.BORDER_TOP_WIDTH, topWidth );
    map.put( BorderStyleKeys.BORDER_RIGHT_WIDTH, rightWidth );
    map.put( BorderStyleKeys.BORDER_BOTTOM_WIDTH, bottomWidth );
    map.put( BorderStyleKeys.BORDER_LEFT_WIDTH, leftWidth );
    return map;
  }

  public StyleKey[] getAffectedKeys() {
    return new StyleKey[] {
      BorderStyleKeys.BORDER_TOP_WIDTH,
      BorderStyleKeys.BORDER_RIGHT_WIDTH,
      BorderStyleKeys.BORDER_BOTTOM_WIDTH,
      BorderStyleKeys.BORDER_LEFT_WIDTH
    };
  }
}
