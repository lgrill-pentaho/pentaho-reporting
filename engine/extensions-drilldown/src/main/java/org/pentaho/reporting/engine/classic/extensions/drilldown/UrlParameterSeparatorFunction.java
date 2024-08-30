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

package org.pentaho.reporting.engine.classic.extensions.drilldown;

import org.pentaho.reporting.libraries.formula.EvaluationException;
import org.pentaho.reporting.libraries.formula.FormulaContext;
import org.pentaho.reporting.libraries.formula.function.Function;
import org.pentaho.reporting.libraries.formula.function.ParameterCallback;
import org.pentaho.reporting.libraries.formula.lvalues.TypeValuePair;
import org.pentaho.reporting.libraries.formula.typing.coretypes.TextType;

public class UrlParameterSeparatorFunction implements Function {
  public UrlParameterSeparatorFunction() {
  }

  public String getCanonicalName() {
    return "URLPARAMETERSEPARATOR";
  }

  public TypeValuePair evaluate( final FormulaContext context,
                                 final ParameterCallback parameters ) throws EvaluationException {
    if ( parameters.getParameterCount() == 0 ) {
      return new TypeValuePair( TextType.TYPE, "?" );
    }
    final String text = context.getTypeRegistry().convertToText( parameters.getType( 0 ), parameters.getValue( 0 ) );
    if ( text == null ) {
      return new TypeValuePair( TextType.TYPE, "?" );
    }
    if ( text.indexOf( '?' ) == -1 ) {
      return new TypeValuePair( TextType.TYPE, "?" );
    }

    if ( text.endsWith( "?" ) ) {
      return new TypeValuePair( TextType.TYPE, text );
    }

    return new TypeValuePair( TextType.TYPE, text + "&" );
  }
}
