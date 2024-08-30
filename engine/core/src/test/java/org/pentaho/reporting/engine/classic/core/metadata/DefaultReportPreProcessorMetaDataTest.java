/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.reporting.engine.classic.core.metadata;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.beans.IntrospectionException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.DefaultReportPreProcessor;
import org.pentaho.reporting.engine.classic.core.ReportPreProcessor;
import org.pentaho.reporting.engine.classic.core.metadata.builder.ReportPreProcessorMetaDataBuilder;

public class DefaultReportPreProcessorMetaDataTest {

  private static final boolean DESIGN_MODE = true;
  private static final boolean AUTO_PROCESS = true;
  private static final String PROP_KEY = "name";

  private ReportPreProcessorMetaDataBuilder builder;
  private DefaultReportPreProcessorMetaData metaData;
  private Map<String, ReportPreProcessorPropertyMetaData> properties;
  private ReportPreProcessorPropertyMetaData itemMetaData;

  @Before
  public void setUp() {
    itemMetaData = mock( ReportPreProcessorPropertyMetaData.class );
    properties = new HashMap<String, ReportPreProcessorPropertyMetaData>();
    properties.put( PROP_KEY, itemMetaData );

    builder = mock( ReportPreProcessorMetaDataBuilder.class );

    doReturn( 1 ).when( builder ).getPriority();
    doReturn( DESIGN_MODE ).when( builder ).isDesignMode();
    doReturn( AUTO_PROCESS ).when( builder ).isAutoProcess();
    doReturn( properties ).when( builder ).getProperties();
    doReturn( DefaultReportPreProcessor.class ).when( builder ).getImpl();
    doReturn( "test_name" ).when( builder ).getName();
    doReturn( "test_bundle" ).when( builder ).getBundleLocation();
    doReturn( "test_prefix" ).when( builder ).getKeyPrefix();

    metaData = new DefaultReportPreProcessorMetaData( builder );
  }

  @Test
  public void testIsExecuteInDesignMode() {
    assertThat( metaData.isExecuteInDesignMode(), is( equalTo( DESIGN_MODE ) ) );
  }

  @Test
  public void testComputePrefix() {
    assertThat( metaData.computePrefix( "keyPrefix", "name" ), is( equalTo( StringUtils.EMPTY ) ) );
  }

  @SuppressWarnings( "rawtypes" )
  @Test
  public void testGetPreProcessorType() {
    assertThat( metaData.getPreProcessorType(), is( equalTo( (Class) DefaultReportPreProcessor.class ) ) );
  }

  @Test
  public void testGetPropertyDescription() {
    assertThat( metaData.getPropertyDescription( PROP_KEY ), is( equalTo( itemMetaData ) ) );
  }

  @Test
  public void testGetPropertyNames() {
    assertThat( metaData.getPropertyNames(), is( equalTo( new String[] { PROP_KEY } ) ) );
  }

  @Test
  public void testGetPropertyDescriptions() {
    assertThat( metaData.getPropertyDescriptions(),
        is( equalTo( new ReportPreProcessorPropertyMetaData[] { itemMetaData } ) ) );
  }

  @Test
  public void testGetBeanDescriptor() throws IntrospectionException {
    assertThat( metaData.getBeanDescriptor(), is( notNullValue() ) );
  }

  @Test
  public void testIsAutoProcessor() {
    assertThat( metaData.isAutoProcessor(), is( equalTo( AUTO_PROCESS ) ) );
  }

  @Test
  public void testGetExecutionPriority() {
    assertThat( metaData.getExecutionPriority(), is( equalTo( 1 ) ) );
  }

  @Test
  public void testCreate() throws InstantiationException {
    assertThat( metaData.create(), is( notNullValue() ) );
  }

  @Test( expected = InstantiationException.class )
  public void testCreateException() throws InstantiationException {
    doReturn( ReportPreProcessor.class ).when( builder ).getImpl();
    metaData = new DefaultReportPreProcessorMetaData( builder );
    metaData.create();
  }
}
