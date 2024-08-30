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

package org.pentaho.reporting.libraries.formula.function.text;

/**
 * This function returns the given value as text.
 *
 * @author Cedric Pronzato
 */
public class CodeFunction extends UnicodeFunction {
  private static final long serialVersionUID = 3505313019941429911L;

  public CodeFunction() {
  }

  public String getCanonicalName() {
    return "CODE";
  }

}
