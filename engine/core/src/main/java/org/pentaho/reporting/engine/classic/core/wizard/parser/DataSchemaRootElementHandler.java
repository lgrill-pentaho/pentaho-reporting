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

package org.pentaho.reporting.engine.classic.core.wizard.parser;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.wizard.DataSchemaRule;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultDataSchemaDefinition;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

public class DataSchemaRootElementHandler extends AbstractXmlReadHandler {
  private ArrayList handlers;
  private DefaultDataSchemaDefinition dataSchemaDefinition;

  public DataSchemaRootElementHandler() {
    handlers = new ArrayList();
  }

  /**
   * Returns the handler for a child element.
   *
   * @param uri
   *          the URI of the namespace of the current element.
   * @param tagName
   *          the tag name.
   * @param atts
   *          the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException
   *           if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild( final String uri, final String tagName, final Attributes atts )
    throws SAXException {
    if ( ClassicEngineBoot.DATASCHEMA_NAMESPACE.equals( uri ) == false ) {
      return null;
    }

    if ( "global-mapping".equals( tagName ) ) {
      final XmlReadHandler handler = new GlobalRuleReadHandler();
      handlers.add( handler );
      return handler;
    }

    if ( "direct-mapping".equals( tagName ) ) {
      final XmlReadHandler handler = new DirectRuleReadHandler();
      handlers.add( handler );
      return handler;
    }

    if ( "indirect-mapping".equals( tagName ) ) {
      final XmlReadHandler handler = new MetaSelectorRuleReadHandler();
      handlers.add( handler );
      return handler;
    }

    return null;
  }

  /**
   * Done parsing.
   *
   * @throws SAXException
   *           if there is a parsing error.
   */
  protected void doneParsing() throws SAXException {
    dataSchemaDefinition = new DefaultDataSchemaDefinition();
    for ( int i = 0; i < handlers.size(); i++ ) {
      final XmlReadHandler handler = (XmlReadHandler) handlers.get( i );
      final DataSchemaRule rule = (DataSchemaRule) handler.getObject();
      dataSchemaDefinition.addRule( rule );
    }
  }

  /**
   * Returns the object for this element or null, if this element does not create an object.
   *
   * @return the object.
   * @throws SAXException
   *           if an parser error occured.
   */
  public Object getObject() throws SAXException {
    return dataSchemaDefinition;
  }
}
