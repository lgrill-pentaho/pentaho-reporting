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

package org.pentaho.reporting.engine.classic.extensions.modules.sparklines;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.StyleSheet;
import org.pentaho.reporting.libraries.libsparklines.BarGraphDrawable;

public class BarSparklinesWrapperTest {

  private BarSparklinesWrapper wrapper;
  private BarGraphDrawable sparkline;

  @Before
  public void setUp() {
    sparkline = mock( BarGraphDrawable.class );
    wrapper = new BarSparklinesWrapper( sparkline );
  }

  @Test
  public void testDraw() {
    Graphics2D g2d = mock( Graphics2D.class );
    Rectangle2D bounds = mock( Rectangle2D.class );

    wrapper.draw( g2d, bounds );

    verify( sparkline ).draw( g2d, bounds );
  }

  @Test
  public void testGetPreferredSize() {
    Dimension size = wrapper.getPreferredSize();
    assertThat( size, is( nullValue() ) );
  }

  @Test
  public void testSetStyleSheet() {
    StyleSheet style = null;

    wrapper.setStyleSheet( style );

    verify( sparkline, never() ).setLastColor( any( Color.class ) );
    verify( sparkline, never() ).setHighColor( any( Color.class ) );
    verify( sparkline, never() ).setBackground( any( Color.class ) );
    verify( sparkline, never() ).setColor( any( Color.class ) );

    style = mock( StyleSheet.class );
    doReturn( Color.BLACK ).when( style ).getStyleProperty( ElementStyleKeys.BACKGROUND_COLOR );
    doReturn( Color.BLUE ).when( style ).getStyleProperty( ElementStyleKeys.PAINT );
    doReturn( Color.RED ).when( style ).getStyleProperty( SparklineStyleKeys.LAST_COLOR );
    doReturn( Color.GRAY ).when( style ).getStyleProperty( SparklineStyleKeys.HIGH_COLOR );

    wrapper.setStyleSheet( style );

    verify( sparkline ).setLastColor( Color.RED );
    verify( sparkline ).setHighColor( Color.GRAY );
    verify( sparkline ).setBackground( Color.BLACK );
    verify( sparkline ).setColor( Color.BLUE );
  }

  @Test
  public void testGetImageMap() {
    assertThat( wrapper.getImageMap( null ), is( nullValue() ) );
  }

  @Test
  public void testIsPreserveAspectRatio() {
    assertThat( wrapper.isPreserveAspectRatio(), is( equalTo( false ) ) );
  }
}
