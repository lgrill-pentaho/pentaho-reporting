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

package org.pentaho.reporting.libraries.css.selectors;

import org.w3c.css.sac.SimpleSelector;

/**
 * Creation-Date: 30.11.2005, 16:38:41
 *
 * @author Thomas Morgner
 */
public class CSSAnyNodeSelector extends AbstractSelector
  implements SimpleSelector {
  public CSSAnyNodeSelector() {
  }

  protected SelectorWeight createWeight() {
    return new SelectorWeight( 0, 0, 0, 1 );
  }

  /**
   * An integer indicating the type of <code>Selector</code>
   */
  public short getSelectorType() {
    return SAC_ANY_NODE_SELECTOR;
  }
}
