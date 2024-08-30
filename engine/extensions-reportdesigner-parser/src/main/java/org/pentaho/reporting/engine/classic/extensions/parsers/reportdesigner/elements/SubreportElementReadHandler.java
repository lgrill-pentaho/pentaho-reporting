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

package org.pentaho.reporting.engine.classic.extensions.parsers.reportdesigner.elements;

import org.pentaho.reporting.engine.classic.core.ParameterMapping;
import org.pentaho.reporting.engine.classic.core.SubReport;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.ReportParserUtil;
import org.pentaho.reporting.libraries.resourceloader.FactoryParameterKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.xmlns.parser.IgnoreAnyChildReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.pentaho.reporting.libraries.xmlns.parser.PropertiesReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Map;
import java.util.Properties;

public class SubreportElementReadHandler extends PropertiesReadHandler {
  private SubReport subReport;
  private SubReportParametersReadHandler parametersReadHandler;

  public SubreportElementReadHandler() {
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild( final String uri, final String tagName, final Attributes atts )
    throws SAXException {
    if ( isSameNamespace( uri ) ) {
      if ( "parameters".equals( tagName ) ) {
        parametersReadHandler = new SubReportParametersReadHandler();
        return parametersReadHandler;
      }
      if ( "padding".equals( tagName ) ) {
        return new IgnoreAnyChildReadHandler();
      }
    }
    return super.getHandlerForChild( uri, tagName, atts );
  }

  /**
   * Returns the object for this element or null, if this element does not create an object.
   *
   * @return the object.
   * @throws SAXException if an parser error occured.
   */
  public Object getObject() throws SAXException {
    return subReport;
  }

  /**
   * Done parsing.
   *
   * @throws SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException {
    super.doneParsing();
    final Properties result1 = getResult();
    final String filePath = result1.getProperty( "filePath" );

    if ( filePath == null ) {
      throw new ParseException( "There was no subreport file specified.", getLocator() );
    }

    final Map parameters = deriveParseParameters();
    parameters.put( new FactoryParameterKey( ReportParserUtil.HELPER_OBJ_REPORT_NAME ), null );
    parameters
      .put( new FactoryParameterKey( ReportParserUtil.INCLUDE_PARSING_KEY ), ReportParserUtil.INCLUDE_PARSING_VALUE );
    try {
      subReport = (SubReport) performExternalParsing( filePath, SubReport.class, parameters );
    } catch ( ResourceLoadingException e ) {
      throw new ParseException( "The specified subreport was not found or could not be loaded.", e, getLocator() );
    }

    final String query = result1.getProperty( "query" );
    subReport.setQuery( query );

    if ( parametersReadHandler != null ) {
      subReport.clearExportParameters();
      subReport.clearInputParameters();

      if ( parametersReadHandler.isGlobalImport() ) {
        subReport.addInputParameter( "*", "*" );
      } else {
        final ParameterMapping[] importMapping = parametersReadHandler.getImportParameterMappings();
        for ( int i = 0; i < importMapping.length; i++ ) {
          final ParameterMapping mapping = importMapping[ i ];
          subReport.addInputParameter( mapping.getName(), mapping.getAlias() );
        }
      }

      if ( parametersReadHandler.isGlobalExport() ) {
        subReport.addExportParameter( "*", "*" );
      } else {
        final ParameterMapping[] exportMapping = parametersReadHandler.getImportParameterMappings();
        for ( int i = 0; i < exportMapping.length; i++ ) {
          final ParameterMapping mapping = exportMapping[ i ];
          subReport.addExportParameter( mapping.getName(), mapping.getAlias() );
        }
      }

    }
  }
}
