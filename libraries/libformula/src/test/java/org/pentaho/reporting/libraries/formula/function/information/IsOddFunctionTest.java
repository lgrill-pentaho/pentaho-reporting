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

package org.pentaho.reporting.libraries.formula.function.information;

import org.pentaho.reporting.libraries.formula.FormulaTestBase;
import org.pentaho.reporting.libraries.formula.LibFormulaErrorValue;

/**
 * @author Cedric Pronzato
 */
public class IsOddFunctionTest extends FormulaTestBase {
  public void testDefault() throws Exception {
    runDefaultTest();
  }

  public Object[][] createDataTest() {
    return new Object[][]
      {
        { "ISODD(3)", Boolean.TRUE },
        { "ISODD(5)", Boolean.TRUE },
        { "ISODD(3.1)", Boolean.TRUE },
        { "ISODD(3.5)", Boolean.TRUE },
        { "ISODD(3.9)", Boolean.TRUE },
        { "ISODD(4)", Boolean.FALSE },
        { "ISODD(4.9)", Boolean.FALSE },
        { "ISODD(-3)", Boolean.TRUE },
        { "ISODD(-3.1)", Boolean.TRUE },
        { "ISODD(-3.5)", Boolean.TRUE },
        { "ISODD(-3.9)", Boolean.TRUE },
        { "ISODD(-4)", Boolean.FALSE },
        { "ISODD(NA())", LibFormulaErrorValue.ERROR_NA_VALUE },
        { "ISODD(0)", Boolean.FALSE },
        { "ISODD(1)", Boolean.TRUE },
        { "ISODD(2)", Boolean.FALSE },
        { "ISODD(2.9)", Boolean.FALSE }, };
  }

}
