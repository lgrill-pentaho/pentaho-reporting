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

package org.pentaho.reporting.engine.classic.extensions.legacy.charts.parser;

import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.layout.elements.AbstractElementReadHandler;
import org.pentaho.reporting.engine.classic.extensions.legacy.charts.LegacyChartType;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;

/**
 * A read handler that produces line-sparkline elements. As the attributes and style is already handled in the abstract
 * super-class, there is no need to add any other implementation here.
 *
 * @author Thomas Morgner
 */
@Deprecated
public class LegacyChartElementReadHandler extends AbstractElementReadHandler {
  public LegacyChartElementReadHandler()
    throws ParseException {
    super( new LegacyChartType() );
  }
}
