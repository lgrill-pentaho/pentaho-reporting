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

package org.pentaho.reporting.libraries.formula;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.libraries.formula.lvalues.LValue;
import org.pentaho.reporting.libraries.formula.lvalues.TypeValuePair;
import org.pentaho.reporting.libraries.formula.parser.FormulaParseException;
import org.pentaho.reporting.libraries.formula.parser.FormulaParser;
import org.pentaho.reporting.libraries.formula.parser.ParseException;
import org.pentaho.reporting.libraries.formula.parser.TokenMgrError;
import org.pentaho.reporting.libraries.formula.typing.ArrayCallback;
import org.pentaho.reporting.libraries.formula.typing.StaticArrayCallback;
import org.pentaho.reporting.libraries.formula.typing.Type;
import org.pentaho.reporting.libraries.formula.typing.coretypes.ErrorType;
import org.pentaho.reporting.libraries.formula.util.NumberUtil;

import java.io.Serializable;

/**
 * Creation-Date: 31.10.2006, 14:43:05
 *
 * @author Thomas Morgner
 */
public class Formula implements Serializable, Cloneable {
  private static final Log logger = LogFactory.getLog( Formula.class );
  private LValue rootReference;
  private static final long serialVersionUID = -1176925812499923546L;

  public Formula( final String formulaText ) throws ParseException {
    if ( formulaText == null ) {
      throw new NullPointerException();
    }

    try {
      final FormulaParser parser = new FormulaParser();
      this.rootReference = parser.parse( formulaText.trim() );
    } catch ( TokenMgrError tokenMgrError ) {
      // This is ugly.
      throw new FormulaParseException( tokenMgrError );
    }
  }

  public Formula( final LValue rootReference ) {
    if ( rootReference == null ) {
      throw new NullPointerException();
    }
    this.rootReference = rootReference;
  }

  public void initialize( final FormulaContext context ) throws EvaluationException {
    if ( context == null ) {
      throw new NullPointerException();
    }
    rootReference.initialize( context );
  }

  /**
   * Returns the root reference for this formula. This allows external programms to modify the formula directly.
   *
   * @return
   */
  public LValue getRootReference() {
    return rootReference;
  }

  public TypeValuePair evaluateTyped() {
    try {
      final TypeValuePair typeValuePair = rootReference.evaluate();
      if ( typeValuePair == null ) {
        throw EvaluationException.getInstance( LibFormulaErrorValue.ERROR_NA_VALUE );
      }
      final Type type = typeValuePair.getType();
      if ( type.isFlagSet( Type.ERROR_TYPE ) ) {
        logger.debug( "Error: " + typeValuePair.getValue() );
      } else if ( type.isFlagSet( Type.ARRAY_TYPE ) ) {
        final Object value = typeValuePair.getValue();
        if ( value instanceof ArrayCallback ) {
          return new TypeValuePair( type, new StaticArrayCallback( (ArrayCallback) value ) );
        }
      } else {
        final Object value = typeValuePair.getValue();
        if ( value instanceof Number ) {
          return new TypeValuePair( type,
            NumberUtil.performTuneRounding( NumberUtil.getAsBigDecimal( (Number) value ) ) );
        }
      }
      return typeValuePair;
    } catch ( EvaluationException ee ) {
      return new TypeValuePair( ErrorType.TYPE, ee.getErrorValue() );
    } catch ( Exception e ) {
      logger.warn( "Evaluation failed unexpectedly: ", e );
      return new TypeValuePair( ErrorType.TYPE, LibFormulaErrorValue.ERROR_UNEXPECTED_VALUE );
    }
  }

  public Object evaluate() {
    final TypeValuePair pair = evaluateTyped();
    final Object value = pair.getValue();
    if ( LibFormulaErrorValue.ERROR_NA_VALUE.equals( value ) ) {
      return null;
    }
    return value;
  }

  public Object clone() throws CloneNotSupportedException {
    final Formula o = (Formula) super.clone();
    o.rootReference = (LValue) rootReference.clone();
    return o;
  }
}
