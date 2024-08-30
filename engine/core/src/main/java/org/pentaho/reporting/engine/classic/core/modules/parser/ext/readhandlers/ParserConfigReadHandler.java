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

package org.pentaho.reporting.engine.classic.core.modules.parser.ext.readhandlers;

import org.pentaho.reporting.engine.classic.core.modules.parser.base.PropertyAttributes;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.common.AbstractPropertyXmlReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.common.ParserConfigurationReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.xml.sax.SAXException;

public class ParserConfigReadHandler extends AbstractPropertyXmlReadHandler {
  public ParserConfigReadHandler() {
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName
   *          the tag name.
   * @param atts
   *          the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws org.xml.sax.SAXException
   *           if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild( final String uri, final String tagName, final PropertyAttributes atts )
    throws SAXException {
    if ( isSameNamespace( uri ) == false ) {
      return null;
    }

    if ( "element-factory".equals( tagName ) ) {
      return new ElementFactoryReadHandler();
    } else if ( "stylekey-factory".equals( tagName ) ) {
      return new StyleKeyFactoryReadHandler();
    } else if ( "template-factory".equals( tagName ) ) {
      return new TemplatesFactoryReadHandler();
    } else if ( "object-factory".equals( tagName ) ) {
      return new ClassFactoryReadHandler();
    } else if ( "datasource-factory".equals( tagName ) ) {
      return new DataSourceFactoryReadHandler();
    } else if ( "parser-properties".equals( tagName ) ) {
      return new ParserConfigurationReadHandler();
    }

    return null;
  }

  /**
   * Returns the object for this element or null, if this element does not create an object.
   *
   * @return the object.
   */
  public Object getObject() {
    return null;
  }

}
