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

package org.pentaho.reporting.engine.classic.core;

import org.pentaho.reporting.engine.classic.core.util.PageFormatFactory;
import org.pentaho.reporting.libraries.serializer.SerializerHelper;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A page definition, that consists of one or many pages. The pages are allowed to overlapp or to leave areas of the
 * page uncovered.
 *
 * @author Thomas Morgner
 * @see PageDefinition
 */
public class CustomPageDefinition implements PageDefinition {
  /**
   * The page bounds, the imageable area on the global virtual page.
   */
  private transient ArrayList pageBoundsList;
  /**
   * The page format list.
   */
  private transient ArrayList pageFormatList;
  /**
   * The total width of the page.
   */
  private float width;
  /**
   * The total height of the page.
   */
  private float height;

  /**
   * Creates a new (initialy empty and therefore invalid) page definition.
   */
  public CustomPageDefinition() {
    pageBoundsList = new ArrayList();
    pageFormatList = new ArrayList();
  }

  /**
   * Adds a new page format to the page definition.
   *
   * @param format
   *          the page format
   * @param x
   *          the x-position to where the imageable-x of the pageformat is mapped.
   * @param y
   *          the y-position to where the imageable-y of the pageformat is mapped.
   */
  public void addPageFormat( final PageFormat format, final float x, final float y ) {
    if ( format == null ) {
      throw new NullPointerException( "The given pageformat must not be null." );
    }
    width = Math.max( width, (float) ( format.getImageableWidth() + x ) );
    height = Math.max( height, (float) ( format.getImageableHeight() + y ) );
    final Rectangle2D bounds = new Rectangle2D.Double( x, y, format.getImageableWidth(), format.getImageableHeight() );
    pageBoundsList.add( bounds );
    pageFormatList.add( format.clone() );
  }

  /**
   * Returns the number of physical pages in the logical page definition.
   *
   * @return the number of physical pages.
   */
  public int getPageCount() {
    return pageBoundsList.size();
  }

  /**
   * Returns the page format for the given page number. The page format contains local coordinates - that means that the
   * point (0,0) denotes the upper left corner of this returned page format and not global coordinates.
   *
   * @param pos
   *          the position of the pageformat within the page
   * @return the given pageformat.
   */
  public PageFormat getPageFormat( final int pos ) {
    final PageFormat fmt = (PageFormat) pageFormatList.get( pos );
    return (PageFormat) fmt.clone();
  }

  /**
   * Describes the internal position of the given page within the logical page. The logical page does not include any
   * page margins, the printable area for a page starts at (0,0).
   *
   * @param index
   *          the index of the page.
   * @return the position of the page (within the global page).
   */
  public Rectangle2D getPagePosition( final int index ) {
    final Rectangle2D rec = (Rectangle2D) pageBoundsList.get( index );
    return rec.getBounds2D();
  }

  /**
   * Returns all page positions as array.
   *
   * @return the collected page positions
   * @see PageDefinition#getPagePosition(int)
   */
  public Rectangle2D[] getPagePositions() {
    final Rectangle2D[] rects = new Rectangle2D[pageBoundsList.size()];
    for ( int i = 0; i < pageBoundsList.size(); i++ ) {
      final Rectangle2D rec = (Rectangle2D) pageBoundsList.get( i );
      rects[i] = rec.getBounds2D();
    }
    return rects;
  }

  /**
   * Returns the total width of the page definition.
   *
   * @return the total width of the page definition.
   */
  public float getWidth() {
    return width;
  }

  /**
   * Returns the total height of the page definition.
   *
   * @return the total height of the page definition.
   */
  public float getHeight() {
    return height;
  }

  /**
   * Clones the given page definition object.
   *
   * @return a clone of this page definition.
   * @throws CloneNotSupportedException
   *           if an error occured.
   */
  public Object clone() throws CloneNotSupportedException {
    final CustomPageDefinition def = (CustomPageDefinition) super.clone();
    def.pageBoundsList = (ArrayList) pageBoundsList.clone();
    def.pageFormatList = (ArrayList) pageFormatList.clone();
    return def;
  }

  /**
   * Deserizalize the report and restore the pageformat.
   *
   * @param out
   *          the objectoutput stream
   * @throws java.io.IOException
   *           if errors occur
   */
  private void writeObject( final ObjectOutputStream out ) throws IOException {
    out.defaultWriteObject();
    final SerializerHelper instance = SerializerHelper.getInstance();
    final Iterator pageBoundsIterator = pageBoundsList.iterator();
    while ( pageBoundsIterator.hasNext() ) {
      instance.writeObject( pageBoundsIterator.next(), out );
    }
    instance.writeObject( null, out );
    final Iterator pageFormatIterator = pageFormatList.iterator();
    while ( pageFormatIterator.hasNext() ) {
      instance.writeObject( pageFormatIterator.next(), out );
    }
    instance.writeObject( null, out );
  }

  /**
   * Resolve the pageformat, as PageFormat is not serializable.
   *
   * @param in
   *          the input stream.
   * @throws java.io.IOException
   *           if there is an IO problem.
   * @throws ClassNotFoundException
   *           if there is a class problem.
   */
  private void readObject( final ObjectInputStream in ) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    final SerializerHelper instance = SerializerHelper.getInstance();
    pageBoundsList = new ArrayList();
    pageFormatList = new ArrayList();

    Object o = instance.readObject( in );
    while ( o != null ) {
      final Rectangle2D rect = (Rectangle2D) o;
      pageBoundsList.add( rect );
      o = instance.readObject( in );
    }

    o = instance.readObject( in );
    while ( o != null ) {
      final PageFormat format = (PageFormat) o;
      pageFormatList.add( format );
      o = instance.readObject( in );
    }
  }

  /**
   * Checks whether the given object is equal to this one.
   *
   * @param obj
   *          the other object.
   * @return true, if the other object is equal, false otherwise.
   */
  public boolean equals( final Object obj ) {
    if ( this == obj ) {
      return true;
    }
    if ( !( obj instanceof CustomPageDefinition ) ) {
      return false;
    }

    final CustomPageDefinition customPageDefinition = (CustomPageDefinition) obj;

    if ( height != customPageDefinition.height ) {
      return false;
    }
    if ( width != customPageDefinition.width ) {
      return false;
    }
    if ( !pageBoundsList.equals( customPageDefinition.pageBoundsList ) ) {
      return false;
    }

    if ( pageFormatList.size() != customPageDefinition.pageFormatList.size() ) {
      return false;
    }

    for ( int i = 0; i < pageFormatList.size(); i++ ) {
      final PageFormat pf = (PageFormat) pageFormatList.get( i );
      final PageFormat cpf = (PageFormat) customPageDefinition.pageFormatList.get( i );
      if ( PageFormatFactory.isEqual( pf, cpf ) == false ) {
        return false;
      }
    }

    return true;
  }

  /**
   * Computes the hashcode of this page definition.
   *
   * @return the hashcode.
   */
  public int hashCode() {
    int result = pageBoundsList.hashCode();
    result = 29 * result + pageFormatList.hashCode();
    result = 29 * result + width == 0.0f ? 0 : Float.floatToIntBits( width );
    result = 29 * result + height == 0.0f ? 0 : Float.floatToIntBits( height );
    return result;
  }
}
