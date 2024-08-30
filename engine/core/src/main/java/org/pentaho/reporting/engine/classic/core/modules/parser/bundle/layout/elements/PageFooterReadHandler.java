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

package org.pentaho.reporting.engine.classic.core.modules.parser.bundle.layout.elements;

import org.pentaho.reporting.engine.classic.core.filter.types.bands.PageFooterType;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;

public class PageFooterReadHandler extends BandReadHandler {
  public PageFooterReadHandler() throws ParseException {
    super( PageFooterType.INSTANCE );
  }
}
