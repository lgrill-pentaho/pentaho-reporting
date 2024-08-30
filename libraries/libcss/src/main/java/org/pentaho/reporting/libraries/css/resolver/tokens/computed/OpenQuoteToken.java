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

package org.pentaho.reporting.libraries.css.resolver.tokens.computed;

public class OpenQuoteToken extends ComputedToken {
  private boolean surpress;

  public OpenQuoteToken( final boolean surpress ) {
    this.surpress = surpress;
  }

  public boolean isSurpressQuoteText() {
    return surpress;
  }
}
