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

package org.pentaho.reporting.engine.classic.core.imagemap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import org.junit.Test;

public class ImageMapEntryTest {

  private static final String NAMESPACE = "test_namespace";
  private static final String NAME = "test_name";
  private static final String VALUE = "test_value";

  @Test
  public void testCircleEntry() {
    CircleImageMapEntry entry = new CircleImageMapEntry( 1.5f, 2f, 3f );
    assertThat( entry.getAreaType(), is( equalTo( "circle" ) ) );
    assertThat( entry.getAreaCoordinates(), is( equalTo( new float[] { 1.5f, 2f, 3f } ) ) );
    assertThat( entry.getShape(), is( instanceOf( Ellipse2D.Float.class ) ) );
    assertThat( entry.contains( 3f, 3f ), is( equalTo( true ) ) );
    entry.setAttribute( NAMESPACE, NAME, VALUE );
    assertThat( entry.getAttribute( NAMESPACE, NAME ), is( equalTo( VALUE ) ) );
    assertThat( entry.getNames( NAMESPACE ), is( equalTo( new String[] { NAME } ) ) );
    assertThat( entry.getNameSpaces(), is( equalTo( new String[] { NAMESPACE } ) ) );
    assertThat( entry.getFirstAttribute( NAME ), is( notNullValue() ) );
    assertThat( entry.toString(), is( equalTo( "<area type=\"circle\" coords=\"1.5,2.0,3.0\" test_name=test_value/>" ) ) );
  }

  @Test
  public void testDefaultEntry() {
    DefaultImageMapEntry entry = new DefaultImageMapEntry();
    assertThat( entry.getAreaType(), is( equalTo( "default" ) ) );
    assertThat( entry.getAreaCoordinates(), is( equalTo( new float[] {} ) ) );
    assertThat( entry.getShape(), is( instanceOf( Rectangle2D.Double.class ) ) );
  }

  @Test
  public void testRectEntry() {
    RectangleImageMapEntry entry = new RectangleImageMapEntry( 3, 4, 1, 2 );
    assertThat( entry.getAreaType(), is( equalTo( "rect" ) ) );
    assertThat( entry.getAreaCoordinates(), is( equalTo( new float[] { 3, 4, 1, 2 } ) ) );
    assertThat( entry.getShape(), is( instanceOf( Rectangle2D.Double.class ) ) );

    assertThat( entry.contains( 1.5f, 2.5f ), is( equalTo( true ) ) );
    assertThat( entry.contains( 1.0f, 1.0f ), is( equalTo( false ) ) );
  }

  @Test( expected = NullPointerException.class )
  public void testPolyEntryWithoutCoords() {
    new PolygonImageMapEntry( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testPolyEntryWithEmptyCoords() {
    new PolygonImageMapEntry( new float[] {} );
  }

  @Test
  public void testPolyEntry() {
    float[] coords = new float[] { 20, 20, 25, 20, 23, 15, 25, 10, 20, 10 };
    PolygonImageMapEntry entry = new PolygonImageMapEntry( coords );
    assertThat( entry.getAreaType(), is( equalTo( "poly" ) ) );
    assertThat( entry.getAreaCoordinates(), is( equalTo( coords ) ) );
    assertThat( entry.getShape(), is( instanceOf( GeneralPath.class ) ) );
  }

  @Test( expected = NullPointerException.class )
  public void testImageMapAddingNullEntry() {
    ImageMap imageMap = new ImageMap();
    imageMap.addMapEntry( null );
  }

  @Test
  public void testImageMap() {
    ImageMap imageMap = new ImageMap();
    CircleImageMapEntry entry = new CircleImageMapEntry( 1, 2, 3 );
    imageMap.addMapEntry( entry );
    assertThat( imageMap.getMapEntries(), is( equalTo( new ImageMapEntry[] { entry } ) ) );
    assertThat( imageMap.getMapEntryCount(), is( equalTo( 1 ) ) );
    assertThat( (CircleImageMapEntry) imageMap.getMapEntry( 0 ), is( equalTo( entry ) ) );

    imageMap.setAttribute( NAMESPACE, NAME, VALUE );
    assertThat( imageMap.getAttribute( NAMESPACE, NAME ), is( equalTo( VALUE ) ) );
    assertThat( imageMap.getNames( NAMESPACE ), is( equalTo( new String[] { NAME } ) ) );
    assertThat( imageMap.getNameSpaces(), is( equalTo( new String[] { NAMESPACE } ) ) );

    assertThat( imageMap.getEntriesForPoint( 1f, 1f ), is( equalTo( new ImageMapEntry[] {} ) ) );
    assertThat( imageMap.getEntriesForPoint( 2.5f, 2.5f ), is( equalTo( new ImageMapEntry[] { entry } ) ) );
  }
}
