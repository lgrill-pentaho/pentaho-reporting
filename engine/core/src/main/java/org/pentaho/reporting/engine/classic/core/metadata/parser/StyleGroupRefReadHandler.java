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

package org.pentaho.reporting.engine.classic.core.metadata.parser;

import org.pentaho.reporting.engine.classic.core.metadata.DefaultStyleKeyMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.StyleMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.builder.StyleMetaDataBuilder;
import org.pentaho.reporting.engine.classic.core.style.StyleKey;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Map;

public class StyleGroupRefReadHandler extends AbstractXmlReadHandler {
  private Map<StyleKey, StyleMetaData> styles;
  private GlobalMetaDefinition styleGroups;
  private String bundle;

  /**
   * @param styles
   *          the style-keys for which metadata is created.
   * @param styleGroups
   *          the global collection of style-groups.
   * @param bundle
   *          the default resource-bundle that is used if the group defines no own bundle.
   * @noinspection AssignmentToCollectionOrArrayFieldFromParameter
   */
  public StyleGroupRefReadHandler( final Map<StyleKey, StyleMetaData> styles, final GlobalMetaDefinition styleGroups,
      final String bundle ) {
    this.styles = styles;
    this.styleGroups = styleGroups;
    this.bundle = bundle;
  }

  /**
   * Starts parsing.
   *
   * @param attrs
   *          the attributes.
   * @throws SAXException
   *           if there is a parsing error.
   */
  protected void startParsing( final Attributes attrs ) throws SAXException {
    final String name = attrs.getValue( getUri(), "ref" );
    if ( name == null ) {
      throw new ParseException( "Attribute 'ref' is undefined", getLocator() );
    }
    final StyleGroup group = styleGroups.getStyleGroup( name );
    if ( group == null ) {
      throw new ParseException( "Attribute 'ref' is invalid. There is no style-group '" + name + "' defined.",
          getLocator() );
    }

    for ( StyleMetaDataBuilder handler : group.getMetaData() ) {
      final StyleKey key = handler.getKey();
      if ( handler.getBundleLocation() == null ) {
        handler = handler.clone().bundle( this.bundle, "style." );
      }

      final DefaultStyleKeyMetaData metaData = new DefaultStyleKeyMetaData( handler );
      styles.put( key, metaData );
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
    return null;
  }
}
