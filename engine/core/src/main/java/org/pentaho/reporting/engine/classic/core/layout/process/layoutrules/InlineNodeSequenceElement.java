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

package org.pentaho.reporting.engine.classic.core.layout.process.layoutrules;

import org.pentaho.reporting.engine.classic.core.layout.model.RenderNode;

/**
 * Anthing that is not text. This could be an image or an inline-block element. For now, we assume that these beasts are
 * not breakable at the end of the line (outer linebreaks).
 *
 * @author Thomas Morgner
 */
public class InlineNodeSequenceElement implements InlineSequenceElement {
  public static final InlineSequenceElement INSTANCE = new InlineNodeSequenceElement();

  protected InlineNodeSequenceElement() {
  }

  /**
   * The width of the element. This is the minimum width of the element.
   *
   * @return
   */
  public long getMinimumWidth( final RenderNode node ) {
    return node.getMinimumChunkWidth();
  }

  /**
   * The extra-space width for an element. Some elements can expand to fill some more space (justified text is a good
   * example, adding some space between the letters of each word to reduce the inner-word spacing).
   *
   * @return
   */
  public long getMaximumWidth( final RenderNode node ) {
    return node.getMaximumBoxWidth();
  }

  public boolean isPreserveWhitespace( final RenderNode node ) {
    return false;
  }

  public int getClassification() {
    return CONTENT;
  }

  public Classification getType() {
    return Classification.CONTENT;
  }
}
