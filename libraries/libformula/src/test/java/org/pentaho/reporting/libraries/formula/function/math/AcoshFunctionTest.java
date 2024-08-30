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

import org.pentaho.reporting.libraries.formula.EvaluationException;
import org.pentaho.reporting.libraries.formula.FormulaContext;
import org.pentaho.reporting.libraries.formula.FormulaTestBase;
import org.pentaho.reporting.libraries.formula.LibFormulaErrorValue;
import org.pentaho.reporting.libraries.formula.common.TestFormulaContext;
import org.pentaho.reporting.libraries.formula.function.ParameterCallback;
import org.pentaho.reporting.libraries.formula.lvalues.TypeValuePair;
import org.pentaho.reporting.libraries.formula.typing.Type;
import org.pentaho.reporting.libraries.formula.typing.coretypes.AnyType;
import org.pentaho.reporting.libraries.formula.typing.coretypes.LogicalType;
import org.pentaho.reporting.libraries.formula.typing.coretypes.NumberType;
import org.pentaho.reporting.libraries.formula.typing.coretypes.TextType;

import java.math.BigDecimal;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AcoshFunctionTest extends FormulaTestBase {
  private static final double DELTA = 0.0000001;

  private FormulaContext context = new TestFormulaContext();

  /**
   * A list of valid values to be given to the ACOSH function and the (expected) resulting value
   */
  private static final BigDecimal[][] VALID_VALUES = {
    { BigDecimal.ONE, BigDecimal.ZERO },
    { new BigDecimal( "1.1" ), new BigDecimal( "0.443568254385115" ) },
    { BigDecimal.TEN, new BigDecimal( "2.99322284612638" ) },
    { new BigDecimal( "10000000.0" ), new BigDecimal( "16.8112428315183" ) } };

  /**
   * A list of invalid values to be given to the ACOSH function. An exception is expected for any of these invocations.
   */
  private static final BigDecimal[] INVALID_VALUES = {
    new BigDecimal( "-123456789.0" ),
    new BigDecimal( "-1.2" ),
    BigDecimal.ZERO,
    new BigDecimal( "0.9999999999999" ) };


  public void testDefault() throws Exception {
    runDefaultTest();
  }

  public Object[][] createDataTest() {
    return new Object[][]
      {
        { "LEFT(ACOSH(10);10)", "2.99322284" },
      };
  }

  public void testValuesInRange() throws EvaluationException {
    AcoshFunction acoshFunction = new AcoshFunction();

    for ( BigDecimal[] testValues : VALID_VALUES ) {
      TypeValuePair result = acoshFunction.evaluate( context, getMockedParameterCallback( testValues[ 0 ] ) );
      assertNotNull( result );
      assertEquals( NumberType.GENERIC_NUMBER, result.getType() );

      assertEquals( testValues[ 1 ].doubleValue(), ( (BigDecimal) ( result.getValue() ) ).doubleValue(), DELTA );
    }
  }

  /**
   * For each invocation, an exception is expected.
   */
  public void testValuesNotInRange() {
    AcoshFunction acoshFunction = new AcoshFunction();

    for ( BigDecimal testValue : INVALID_VALUES ) {
      try {
        acoshFunction.evaluate( context, getMockedParameterCallback( testValue ) );

        fail( "Should not have reached this far; an Exception should have been raised!" );
      } catch ( EvaluationException e ) {
        // We know the actual error that should be referred by the exception, so let's check it!
        assertEquals( LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE, e.getErrorValue() );
      }
    }
  }

  public void testWrongParameterNumber_Zero() {
    AcoshFunction acoshFunction = new AcoshFunction();

    try {
      acoshFunction.evaluate( context, getMockedParameterCallback() );

      fail( "Should not have reached this far; an Exception should have been raised!" );
    } catch ( EvaluationException e ) {
      // We know the actual error that should be referred by the exception, so let's check it!
      assertEquals( LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE, e.getErrorValue() );
    }
  }

  public void testWrongParameterNumber_Two() {
    AcoshFunction acoshFunction = new AcoshFunction();

    try {
      acoshFunction.evaluate( context, getMockedParameterCallback( BigDecimal.ONE, BigDecimal.TEN ) );

      fail( "Should not have reached this far; an Exception should have been raised!" );
    } catch ( EvaluationException e ) {
      // We know the actual error that should be referred by the exception, so let's check it!
      assertEquals( LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE, e.getErrorValue() );
    }
  }

  public void testWrongParameterType() {
    AcoshFunction acoshFunction = new AcoshFunction();

    try {
      acoshFunction.evaluate( context, getMockedParameterCallback( "error" ) );

      fail( "Should not have reached this far; an Exception should have been raised!" );
    } catch ( EvaluationException e ) {
      // We know the actual error that should be referred by the exception, so let's check it!
      assertEquals( LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE, e.getErrorValue() );
    }
  }

  private ParameterCallback getMockedParameterCallback( Object... parameterValues ) throws EvaluationException {
    ParameterCallback parameter = mock( ParameterCallback.class );
    when( parameter.getParameterCount() ).thenReturn( parameterValues.length );
    int i = 0;
    for ( Object parameterValue : parameterValues ) {
      Type parameterType = AnyType.TYPE;
      if ( parameterValue instanceof BigDecimal ) {
        parameterType = NumberType.GENERIC_NUMBER;
      } else if ( parameterValue instanceof Boolean ) {
        parameterType = LogicalType.TYPE;
      } else if ( parameterValue instanceof String ) {
        parameterType = TextType.TYPE;
      }
      when( parameter.getType( eq( i ) ) ).thenReturn( NumberType.GENERIC_NUMBER );
      when( parameter.getValue( eq( i ) ) ).thenReturn( parameterValue );
      ++i;
    }

    return parameter;
  }
}
