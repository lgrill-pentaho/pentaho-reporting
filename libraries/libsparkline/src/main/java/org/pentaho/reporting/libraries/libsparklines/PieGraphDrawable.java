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

package org.pentaho.reporting.libraries.libsparklines;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PieGraphDrawable {
  private static final Color DEFAULT_MEDIUM_COLOR = Color.YELLOW;
  private static final Color DEFAULT_HIGH_COLOR = Color.RED;
  private static final Color DEFAULT_LOW_COLOR = Color.GREEN;

  private static final Double DEFAULT_LOW_SLICE = new Double( 0.30 );
  private static final Double DEFAULT_MEDIUM_SLICE = new Double( 0.70 );
  private static final Double DEFAULT_HIGH_SLICE = new Double( 1 );


  private Color color;
  private Color background;

  private Color mediumColor;
  private Color highColor;
  private Color lowColor;

  private Number mediumSlice;
  private Number highSlice;
  private Number lowSlice;

  private boolean counterClockWise = false;
  private int startAngle = 0;

  private Number value;

  public PieGraphDrawable() {
    color = Color.LIGHT_GRAY;
    mediumColor = DEFAULT_MEDIUM_COLOR;
    lowColor = DEFAULT_LOW_COLOR;
    highColor = DEFAULT_HIGH_COLOR;
    mediumSlice = DEFAULT_MEDIUM_SLICE;
    lowSlice = DEFAULT_LOW_SLICE;
    highSlice = DEFAULT_HIGH_SLICE;
  }

  public void draw( final Graphics2D graphics, final Rectangle2D drawArea ) {
    // compute the centred square area inside the drawArea
    final double adjustX =
      drawArea.getWidth() < drawArea.getHeight() ? 0 : ( drawArea.getHeight() - drawArea.getWidth() ) / 2.0;
    final double adjustY =
      drawArea.getHeight() < drawArea.getWidth() ? 0 : ( drawArea.getWidth() - drawArea.getHeight() ) / 2.0;
    final Graphics2D g = (Graphics2D) graphics.create();
    // draw background
    if ( background != null ) {
      g.setBackground( background );
      g.clearRect( 0, 0, (int) drawArea.getWidth(), (int) drawArea.getHeight() );
    }

    g.translate( drawArea.getX() - adjustX, drawArea.getY() - adjustY );
    final int radius =
      (int) ( drawArea.getWidth() < drawArea.getHeight() ? drawArea.getWidth() : drawArea.getHeight() );
    // draw a filled circle form its center to its edge with the background color
    g.setPaint( color );
    g.fillOval( 0, 0, radius, radius );

    // draw the value arc
    final int endArc = (int) ( value.doubleValue() * 360 );
    //find the color
    g.setBackground( Color.BLUE );  //init some debug color
    if ( value.doubleValue() <= lowSlice.doubleValue() ) {
      g.setPaint( lowColor );
    } else if ( value.doubleValue() <= mediumSlice.doubleValue() ) {
      g.setPaint( mediumColor );
    } else if ( value.doubleValue() <= highSlice.doubleValue() ) {
      g.setPaint( highColor );
    }

    final int counterClockWise = this.counterClockWise ? 1 : -1;
    g.fillArc( 0, 0, radius, radius, -startAngle + 90, counterClockWise * endArc );

    g.dispose();
  }

  public Color getColor() {
    return color;
  }

  public void setColor( final Color color ) {
    this.color = color;
  }

  public Color getMediumColor() {
    return mediumColor;
  }

  public void setMediumColor( final Color mediumColor ) {
    this.mediumColor = mediumColor;
  }

  public Color getHighColor() {
    return highColor;
  }

  public void setHighColor( final Color highColor ) {
    this.highColor = highColor;
  }

  public Color getLowColor() {
    return lowColor;
  }

  public void setLowColor( final Color lowColor ) {
    this.lowColor = lowColor;
  }

  public Number getMediumSlice() {
    return mediumSlice;
  }

  public void setMediumSlice( final Number mediumSlice ) {
    this.mediumSlice = mediumSlice;
  }

  public Number getHighSlice() {
    return highSlice;
  }

  public void setHighSlice( final Number highSlice ) {
    this.highSlice = highSlice;
  }

  public Number getLowSlice() {
    return lowSlice;
  }

  public void setLowSlice( final Number lowSlice ) {
    this.lowSlice = lowSlice;
  }

  public Number getValue() {
    return value;
  }

  public void setValue( final Number value ) {
    this.value = value;
  }

  public boolean isCounterClockWise() {
    return counterClockWise;
  }

  public void setCounterClockWise( final boolean counterClockWise ) {
    this.counterClockWise = counterClockWise;
  }

  public int getStartAngle() {
    return startAngle;
  }

  public void setStartAngle( final int startAngle ) {
    this.startAngle = startAngle;
  }

  public Color getBackground() {
    return background;
  }

  public void setBackground( final Color background ) {
    this.background = background;
  }
}
