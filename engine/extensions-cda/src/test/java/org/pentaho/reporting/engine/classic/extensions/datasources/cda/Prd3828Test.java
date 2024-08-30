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

package org.pentaho.reporting.engine.classic.extensions.datasources.cda;

import junit.framework.TestCase;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.DefaultResourceBundleFactory;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.StaticDataRow;
import org.pentaho.reporting.engine.classic.core.designtime.datafactory.DesignTimeDataFactoryContext;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class Prd3828Test extends TestCase {
  private class SimpleBackend extends CdaQueryBackend {
    private String url;
    private String paramUrl;

    public TypedTableModel fetchData( final DataRow dataRow,
                                      final String method,
                                      final Map<String, String> extraParameter )
      throws ReportDataFactoryException {
      if ( METHOD_LIST_PARAMETERS.equals( method ) ) {
        paramUrl = createURL( method, extraParameter );
        final TypedTableModel typedTableModel = new TypedTableModel();
        typedTableModel.addColumn( CdaQueryBackend.PARAM_NAME, String.class );
        typedTableModel.addColumn( CdaQueryBackend.PARAM_TYPE, String.class );
        typedTableModel.addColumn( CdaQueryBackend.PARAM_DEFAULT_VALUE, String.class );
        typedTableModel.addColumn( CdaQueryBackend.PARAM_PATTERN, String.class );

        typedTableModel.addRow( "P1", "String", "DefaultString", null );
        typedTableModel.addRow( "P2", "Integer", "10", null );
        typedTableModel.addRow( "P3", "Date", "2010-12-30", "yyyy-MM-dd" );
        typedTableModel.addRow( "P4", "StringArray", "A;B;C", null );
        return typedTableModel;
      }
      url = createURL( method, extraParameter );
      return new TypedTableModel();
    }

    public String getParamUrl() {
      return paramUrl;
    }

    public String getUrl() {
      return url;
    }
  }

  public Prd3828Test() {
  }

  protected void setUp() throws Exception {
  }

  public void testDefaultUrl() throws ReportDataFactoryException {
    final SimpleBackend simpleBackend = new SimpleBackend();

    final CdaDataFactory dataFactory = new CdaDataFactory();
    dataFactory.setBackend( simpleBackend );
    dataFactory.setBaseUrl( "http://localhost:12345/testcase" );
    dataFactory.setFile( "testcase.cda" );
    dataFactory.setSolution( "testsolution" );
    dataFactory.setPath( "testpath" );
    dataFactory.setUsername( "joe" );
    dataFactory.setPassword( "password" );
    dataFactory.setQueryEntry( "testQuery", new CdaQueryEntry( "myQuery", "cdaId" ) );

    dataFactory.initialize( createDataFactoryContext() );
    dataFactory.queryData( "testQuery", new StaticDataRow() );

    assertUrl( simpleBackend.getParamUrl(), "http://localhost:12345/testcase/content/cda/listParameters",
      "outputType=xml", "solution=testsolution", "path=testpath", "file=testcase.cda", "dataAccessId=cdaId" );
    assertUrl( simpleBackend.getUrl(), "http://localhost:12345/testcase/content/cda/doQuery",
      "outputType=xml", "solution=testsolution", "path=testpath", "file=testcase.cda", "paramP4=A%3BB%3BC",
      "dataAccessId=cdaId", "paramP3=2010-12-30", "paramP1=DefaultString", "paramP2=10" );
  }

  public void testFilledUrl() throws ReportDataFactoryException {
    final SimpleBackend simpleBackend = new SimpleBackend();

    final CdaDataFactory dataFactory = new CdaDataFactory();
    dataFactory.setBackend( simpleBackend );
    dataFactory.setBaseUrl( "http://localhost:12345/testcase" );
    dataFactory.setFile( "testcase.cda" );
    dataFactory.setSolution( "testsolution" );
    dataFactory.setPath( "testpath" );
    dataFactory.setUsername( "joe" );
    dataFactory.setPassword( "password" );
    dataFactory.setQueryEntry( "testQuery", new CdaQueryEntry( "myQuery", "cdaId" ) );

    dataFactory.initialize( createDataFactoryContext() );
    dataFactory.queryData( "testQuery", new StaticDataRow
      ( new String[] { "P1", "P2", "P3", "P4" },
        new Object[] { "x", 10, new Date( 1000000000000l ), new String[] { "x", "y", "z" } } ) );

    assertUrl( simpleBackend.getParamUrl(), "http://localhost:12345/testcase/content/cda/listParameters",
      "outputType=xml", "solution=testsolution", "path=testpath", "file=testcase.cda", "dataAccessId=cdaId" );
    assertUrl( simpleBackend.getUrl(), "http://localhost:12345/testcase/content/cda/doQuery",
      "outputType=xml", "solution=testsolution", "path=testpath", "file=testcase.cda", "paramP4=x%3By%3Bz",
      "dataAccessId=cdaId", "paramP3=2001-09-09", "paramP1=x", "paramP2=10" );
  }

  private DesignTimeDataFactoryContext createDataFactoryContext() {
    return new DesignTimeDataFactoryContext(
        ClassicEngineBoot.getInstance().getGlobalConfig(),
        new ResourceManager(), null,
        new DefaultResourceBundleFactory( Locale.getDefault(), TimeZone.getTimeZone( "UTC" ))
      );
  }

  private void assertUrl( String actual, String url, String... params ) {
    String[] baseAndParams = actual.split( "\\?" );
    assertEquals( url, baseAndParams[ 0 ] );

    Set<String> expectedParameters = new HashSet<String>( Arrays.asList( params ) );
    String[] pairs = baseAndParams[1].split( "&" );

    for ( String pair : pairs ) {
      assertTrue( pair, expectedParameters.remove( pair ) );
    }

    assertTrue( expectedParameters.toString(), expectedParameters.isEmpty() );
  }
}
