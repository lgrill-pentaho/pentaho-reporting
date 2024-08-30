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

package org.pentaho.reporting.engine.classic.core.modules.misc.survey;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

/**
 * A Drawable object that represents a legend item for a {@link SurveyScale}.
 */
public class SurveyScaleLegendItem {

  /**
   * The shape.
   */
  private Shape shape;

  /**
   * The label.
   */
  private String label;

  /**
   * Draw the shape?
   */
  private boolean draw;

  /**
   * Fill the shape?
   */
  private boolean fill;

  /**
   * The label font.
   */
  private Font font;

  public SurveyScaleLegendItem() {
    font = new Font( "Serif", Font.ITALIC, 10 ); //$NON-NLS-1$
  }

  /**
   * Creates a new legend item.
   *
   * @param shape
   *          the shape.
   * @param label
   *          the label.
   * @param draw
   *          draw the shape?
   * @param fill
   *          fill the shape?
   */
  public SurveyScaleLegendItem( final Shape shape, final String label, final boolean draw, final boolean fill ) {
    this.shape = shape;
    this.label = label;
    this.draw = draw;
    this.fill = fill;
  }

  /**
   * Draws the legend item.
   *
   * @param g2
   *          the graphic device.
   * @param area
   *          the area.
   */
  public void draw( final Graphics2D g2, final Rectangle2D area ) {
    if ( shape == null || font == null || label == null ) {
      return;
    }
    if ( draw == false && fill == false ) {
      return;
    }

    final Rectangle2D b = this.shape.getBounds2D();
    double x = area.getMinX() + b.getWidth() / 2.0 + 1.0;
    final double y = area.getCenterY();
    final Shape s = getShape();
    g2.translate( x, y );
    g2.setPaint( Color.black );
    if ( this.draw ) {
      g2.setStroke( new BasicStroke( 0.5f ) );
      g2.draw( s );
    }
    if ( this.fill ) {
      g2.fill( s );
    }
    g2.translate( -x, -y );
    x += b.getWidth() / 2.0 + 3.0;
    g2.setFont( this.font );

    final FontRenderContext frc = g2.getFontRenderContext();
    final Font f = g2.getFont();
    // final FontMetrics fm = g2.getFontMetrics(f);
    final LineMetrics metrics = f.getLineMetrics( label, frc );
    final float ascent = metrics.getAscent();
    final float halfAscent = ascent / 2.0f;
    g2.drawString( label, (float) x, (float) ( y + halfAscent ) );
  }

  public boolean isDraw() {
    return draw;
  }

  public void setDraw( final boolean draw ) {
    this.draw = draw;
  }

  public boolean isFill() {
    return fill;
  }

  public void setFill( final boolean fill ) {
    this.fill = fill;
  }

  public Font getFont() {
    return font;
  }

  public void setFont( final Font font ) {
    this.font = font;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel( final String label ) {
    this.label = label;
  }

  public Shape getShape() {
    return shape;
  }

  public void setShape( final Shape shape ) {
    this.shape = shape;
  }

}
