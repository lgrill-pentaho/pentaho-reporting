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

package org.pentaho.reporting.engine.classic.core.modules.parser.simple.readhandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.Band;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.PropertyAttributes;
import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.libraries.xmlns.common.ParserUtil;
import org.xml.sax.SAXException;

public class ReportHeaderReadHandler extends RootLevelBandReadHandler {
  private static final Log logger = LogFactory.getLog( ReportHeaderReadHandler.class );

  public ReportHeaderReadHandler( final Band band ) {
    super( band );
  }

  /**
   * Starts parsing.
   *
   * @param attr
   *          the attributes.
   * @throws org.xml.sax.SAXException
   *           if there is a parsing error.
   */
  protected void startParsing( final PropertyAttributes attr ) throws SAXException {
    super.startParsing( attr );
    handleOwnPageAttr( attr );
  }

  private void handleOwnPageAttr( final PropertyAttributes attr ) throws SAXException {
    final String ownPageAttr = attr.getValue( getUri(), "ownpage" );
    if ( ownPageAttr != null ) {
      ReportHeaderReadHandler.logger.warn( "The 'ownpage' attribute of the <report-header> tag is deprecated. "
          + "Use the 'pagebreak-after' attribute instead." );
      final Boolean ownPage = ParserUtil.parseBoolean( ownPageAttr, getLocator() );
      getBand().getStyle().setStyleProperty( BandStyleKeys.PAGEBREAK_AFTER, ownPage );
    }
  }

}
