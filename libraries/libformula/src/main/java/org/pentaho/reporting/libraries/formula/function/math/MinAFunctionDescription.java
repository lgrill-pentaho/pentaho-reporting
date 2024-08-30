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

package org.pentaho.reporting.libraries.formula.function.math;

import org.pentaho.reporting.libraries.formula.function.AbstractFunctionDescription;
import org.pentaho.reporting.libraries.formula.function.FunctionCategory;
import org.pentaho.reporting.libraries.formula.typing.Type;
import org.pentaho.reporting.libraries.formula.typing.coretypes.NumberType;

/**
 * Describes MinAFunction function.
 *
 * @author Cedric Pronzato
 * @see MinFunction
 */
public class MinAFunctionDescription extends AbstractFunctionDescription {
  private static final long serialVersionUID = -2974454751189094698L;

  public MinAFunctionDescription() {
    super( "MINA", "org.pentaho.reporting.libraries.formula.function.math.MinA-Function" );
  }

  public FunctionCategory getCategory() {
    return MathFunctionCategory.CATEGORY;
  }

  public int getParameterCount() {
    return 0;
  }

  @Override
  public boolean isInfiniteParameterCount() {
    return true;
  }

  public Type getParameterType( final int position ) {
    return NumberType.NUMBER_SEQUENCE;
  }

  public Type getValueType() {
    return NumberType.GENERIC_NUMBER;
  }

  public boolean isParameterMandatory( final int position ) {
    return true;
  }
}
