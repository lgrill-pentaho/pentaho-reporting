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

package org.pentaho.reporting.engine.classic.core.modules.parser.base.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.function.Expression;
import org.pentaho.reporting.engine.classic.core.function.FormulaExpression;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.PropertyAttributes;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.compat.CompatibilityMapperUtil;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.StyleKey;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;
import org.pentaho.reporting.libraries.xmlns.parser.IgnoreAnyChildReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.xml.sax.SAXException;

import java.beans.IntrospectionException;

public class StyleExpressionHandler extends AbstractPropertyXmlReadHandler {
  private static final Log logger = LogFactory.getLog( StyleExpressionHandler.class );

  private String originalClassName;
  private String expressionClassName;
  private Expression expression;
  private StyleKey key;

  public StyleExpressionHandler() {
  }

  /**
   * Starts parsing.
   *
   * @param attrs
   *          the attributes.
   * @throws org.xml.sax.SAXException
   *           if there is a parsing error.
   */
  protected void startParsing( final PropertyAttributes attrs ) throws SAXException {
    final String expressionName = attrs.getValue( getUri(), "style-key" );
    if ( expressionName == null ) {
      throw new SAXException( "Required attribute 'style-key' is missing." );
    }

    if ( ElementStyleKeys.isLegacyKey( expressionName ) ) {
      return;
    }

    key = StyleKey.getStyleKey( expressionName );
    if ( key == null ) {
      throw new SAXException( "Required attribute 'style-key' is invalid." );
    }

    final String className = attrs.getValue( getUri(), "class" );
    final String formula = attrs.getValue( getUri(), "formula" );
    if ( className == null ) {
      if ( formula != null ) {
        final FormulaExpression expression = new FormulaExpression();
        expression.setFormula( formula );
        this.expression = expression;
        this.expression.setName( expressionName );

        this.originalClassName = FormulaExpression.class.getName();
        this.expressionClassName = FormulaExpression.class.getName();
      } else {
        logger.warn( "Required attribute 'class' is missing. Gracefully ignoring the error." + getLocator() );
      }
    }

    if ( expression == null && className != null ) {

      final String mappedClassName = CompatibilityMapperUtil.mapClassName( className );
      expression = (Expression) ObjectUtilities.loadAndInstantiate( mappedClassName, getClass(), Expression.class );
      if ( expression == null ) {
        throw new ParseException( "Expression '" + className + "' is not valid. The specified class was not found.",
            getLocator() );
      }

      this.originalClassName = className;
      this.expressionClassName = mappedClassName;
    }
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

    if ( key == null ) {
      return new IgnoreAnyChildReadHandler();
    }
    if ( "properties".equals( tagName ) && expression != null ) {
      try {
        return new ExpressionPropertiesReadHandler( expression, originalClassName, expressionClassName );
      } catch ( IntrospectionException e ) {
        throw new SAXException( "Unable to create Introspector for the specified expression." );
      }
    }
    return null;
  }

  /**
   * Returns the object for this element or null, if this element does not create an object.
   *
   * @return the object.
   */
  public Object getObject() {
    return expression;
  }

  public Expression getExpression() {
    return expression;
  }

  public StyleKey getKey() {
    return key;
  }
}
