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

package org.pentaho.reporting.libraries.css.keys.text;

import org.pentaho.reporting.libraries.css.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 20:11:13
 *
 * @author Thomas Morgner
 */
public class TextWrap {
  public static final CSSConstant NORMAL = new CSSConstant( "normal" );
  public static final CSSConstant UNRESTRICTED = new CSSConstant( "unrestricted" );
  public static final CSSConstant NONE = new CSSConstant( "none" );
  public static final CSSConstant SUPPRESS = new CSSConstant( "suppress" );

  private TextWrap() {
  }
}
