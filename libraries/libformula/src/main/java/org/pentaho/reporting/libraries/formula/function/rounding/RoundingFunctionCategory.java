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

package org.pentaho.reporting.libraries.formula.function.rounding;

import org.pentaho.reporting.libraries.formula.function.AbstractFunctionCategory;
import org.pentaho.reporting.libraries.formula.function.FunctionCategory;

/**
 * @author Cedric Pronzato
 */
public final class RoundingFunctionCategory extends AbstractFunctionCategory {
  public static final FunctionCategory CATEGORY = new RoundingFunctionCategory();

  private RoundingFunctionCategory() {
    super( "org.pentaho.reporting.libraries.formula.function.rounding.category" );
  }
}
