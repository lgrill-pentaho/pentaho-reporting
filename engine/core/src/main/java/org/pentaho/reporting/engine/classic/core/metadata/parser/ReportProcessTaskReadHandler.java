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

package org.pentaho.reporting.engine.classic.core.metadata.parser;

import org.pentaho.reporting.engine.classic.core.ReportProcessTask;
import org.pentaho.reporting.engine.classic.core.metadata.DefaultReportProcessTaskMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.ReportProcessTaskMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.builder.ReportProcessTaskMetaDataBuilder;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.pentaho.reporting.libraries.xmlns.parser.StringReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

/**
 * @noinspection HardCodedStringLiteral
 */
public class ReportProcessTaskReadHandler extends AbstractMetaDataReadHandler {
  private ArrayList<StringReadHandler> aliasReadHandlers;
  private ReportProcessTaskMetaDataBuilder builder;

  public ReportProcessTaskReadHandler() {
    aliasReadHandlers = new ArrayList<StringReadHandler>();
    builder = new ReportProcessTaskMetaDataBuilder();
  }

  public ReportProcessTaskMetaDataBuilder getBuilder() {
    return builder;
  }

  /**
   * Starts parsing.
   *
   * @param attrs
   *          the attributes.
   * @throws org.xml.sax.SAXException
   *           if there is a parsing error.
   */
  protected void startParsing( final Attributes attrs ) throws SAXException {
    super.startParsing( attrs );
    getBuilder().implementation( parseImpl( attrs ) );
    getBuilder().bundle( getBundle(), "" );
  }

  private Class<? extends ReportProcessTask> parseImpl( final Attributes attrs ) throws ParseException {
    final String implText = attrs.getValue( getUri(), "class" );
    if ( implText == null ) {
      throw new ParseException( "Attribute 'class' is undefined", getLocator() );
    }
    try {
      Class<? extends ReportProcessTask> c =
          ObjectUtilities.loadAndValidate( implText, ReportProcessTaskReadHandler.class, ReportProcessTask.class );
      if ( c == null ) {
        throw new ParseException( "Attribute 'class' is not valid", getLocator() );
      }
      return c;
    } catch ( ParseException pe ) {
      throw pe;
    } catch ( Exception e ) {
      throw new ParseException( "Attribute 'class' is not valid", e, getLocator() );
    }
  }

  protected XmlReadHandler getHandlerForChild( final String uri, final String tagName, final Attributes atts )
    throws SAXException {
    if ( isSameNamespace( uri ) == false ) {
      return null;
    }
    if ( "alias".equals( tagName ) ) {
      final StringReadHandler rh = new StringReadHandler();
      aliasReadHandlers.add( rh );
      return rh;
    }
    return null;
  }

  protected void doneParsing() throws SAXException {
    for ( int i = 0; i < aliasReadHandlers.size(); i++ ) {
      final StringReadHandler readHandler = aliasReadHandlers.get( i );
      getBuilder().alias( readHandler.getResult() );
    }
  }

  /**
   * Returns the object for this element or null, if this element does not create an object.
   *
   * @return the object.
   * @throws org.xml.sax.SAXException
   *           if an parser error occured.
   */
  public ReportProcessTaskMetaData getObject() throws SAXException {
    return new DefaultReportProcessTaskMetaData( getBuilder() );
  }
}
