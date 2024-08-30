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

package org.pentaho.reporting.engine.classic.core.metadata;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.function.Expression;
import org.pentaho.reporting.engine.classic.core.metadata.builder.ReportPreProcessorPropertyMetaDataBuilder;
import org.pentaho.reporting.engine.classic.core.metadata.propertyeditors.SharedPropertyDescriptorProxy;
import org.pentaho.reporting.libraries.designtime.swing.propertyeditors.BooleanPropertyEditor;

public class DefaultReportPreProcessorPropertyMetaDataTest {

  private static final String PROPERTY_ROLE = "propertyRole";
  private static final boolean COMPUTED = true;
  private static final boolean MANDATORY = true;

  private ReportPreProcessorPropertyMetaDataBuilder builder;
  private DefaultReportPreProcessorPropertyMetaData metaData;
  private SharedPropertyDescriptorProxy propertyDescriptor;
  private ReportPreProcessorPropertyCore reportPreProcessorCore;

  private Expression element;

  @Before
  public void setUp() {
    propertyDescriptor = mock( SharedPropertyDescriptorProxy.class );
    reportPreProcessorCore = mock( ReportPreProcessorPropertyCore.class );
    element = mock( Expression.class );
    PropertyDescriptor propDescr = mock( PropertyDescriptor.class );

    builder = mock( ReportPreProcessorPropertyMetaDataBuilder.class );

    doReturn( propDescr ).when( propertyDescriptor ).get();
    doReturn( PropertyEditor.class ).when( propDescr ).getPropertyType();
    doReturn( propertyDescriptor ).when( builder ).getDescriptor();
    doReturn( COMPUTED ).when( builder ).isComputed();
    doReturn( MANDATORY ).when( builder ).isMandatory();
    doReturn( reportPreProcessorCore ).when( builder ).getCore();
    doReturn( PropertyEditor.class ).when( builder ).getEditor();
    doReturn( PROPERTY_ROLE ).when( builder ).getValueRole();

    doReturn( "test_name" ).when( builder ).getName();
    doReturn( "test_bundle" ).when( builder ).getBundleLocation();
    doReturn( "test_prefix" ).when( builder ).getKeyPrefix();

    metaData = new DefaultReportPreProcessorPropertyMetaData( builder );
  }

  @Test
  public void testIsComputed() {
    assertThat( metaData.isComputed(), is( equalTo( COMPUTED ) ) );
  }

  @SuppressWarnings( "rawtypes" )
  @Test
  public void testGetPropertyType() {
    assertThat( metaData.getPropertyType(), is( CoreMatchers.<Class> equalTo( PropertyEditor.class ) ) );
  }

  @Test
  public void testGetPropertyRole() {
    assertThat( metaData.getPropertyRole(), is( equalTo( PROPERTY_ROLE ) ) );
  }

  @Test
  public void testIsMandatory() {
    assertThat( metaData.isMandatory(), is( equalTo( MANDATORY ) ) );
  }

  @Test
  public void testGetReferencedFields() {
    String[] expected = new String[] { "item" };
    doReturn( expected ).when( reportPreProcessorCore ).getReferencedFields( metaData, element, null );
    String[] result = metaData.getReferencedFields( element, null );
    assertThat( result, is( equalTo( expected ) ) );
  }

  @Test
  public void testGetReferencedGroups() {
    String[] expected = new String[] { "item" };
    doReturn( expected ).when( reportPreProcessorCore ).getReferencedGroups( metaData, element, null );
    String[] result = metaData.getReferencedGroups( element, null );
    assertThat( result, is( equalTo( expected ) ) );
  }

  @Test
  public void testGetReferencedElements() {
    String[] expected = new String[] { "item" };
    doReturn( expected ).when( reportPreProcessorCore ).getReferencedElements( metaData, element, null );
    String[] result = metaData.getReferencedElements( element, null );
    assertThat( result, is( equalTo( expected ) ) );
  }

  @Test
  public void testGetReferencedResources() {
    Element elem = mock( Element.class );
    ResourceReference[] expected = new ResourceReference[] { mock( ResourceReference.class ) };
    doReturn( expected ).when( reportPreProcessorCore ).getReferencedResources( metaData, element, null, elem, null );
    ResourceReference[] result = metaData.getReferencedResources( element, null, elem, null );
    assertThat( result, is( equalTo( expected ) ) );
  }

  @Test
  public void testGetEditor() {
    PropertyEditor result = metaData.getEditor();
    assertThat( result, is( nullValue() ) );

    doReturn( null ).when( builder ).getEditor();
    metaData = new DefaultReportPreProcessorPropertyMetaData( builder );
    result = metaData.getEditor();
    assertThat( result, is( nullValue() ) );

    doReturn( BooleanPropertyEditor.class ).when( builder ).getEditor();
    metaData = new DefaultReportPreProcessorPropertyMetaData( builder );
    result = metaData.getEditor();
    assertThat( result, is( instanceOf( BooleanPropertyEditor.class ) ) );
  }

  @Test
  public void testGetExtraCalculationFields() {
    String[] expected = new String[] { "item" };
    doReturn( expected ).when( reportPreProcessorCore ).getExtraCalculationFields( metaData );
    String[] result = metaData.getExtraCalculationFields();
    assertThat( result, is( equalTo( expected ) ) );
  }
}
