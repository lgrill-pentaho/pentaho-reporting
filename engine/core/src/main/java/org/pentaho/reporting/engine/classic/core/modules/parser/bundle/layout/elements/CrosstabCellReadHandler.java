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

import org.pentaho.reporting.engine.classic.core.CrosstabCell;
import org.pentaho.reporting.engine.classic.core.filter.types.bands.CrosstabCellType;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;

public class CrosstabCellReadHandler extends BandReadHandler {
  public CrosstabCellReadHandler() throws ParseException {
    super( CrosstabCellType.INSTANCE );
  }

  public CrosstabCell getElement() {
    return (CrosstabCell) super.getElement();
  }
}
