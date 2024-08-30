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

package org.pentaho.reporting.engine.classic.core.modules.misc.datafactory;

public class SqlDataFactoryDriverTestGenerator {
  public static void main( String[] args ) throws Exception {
    final SqlDataFactoryDriverIT test = new SqlDataFactoryDriverIT();
    test.setUp();
    test.runGenerate( SqlDataFactoryDriverIT.QUERIES_AND_RESULTS );
  }

}
