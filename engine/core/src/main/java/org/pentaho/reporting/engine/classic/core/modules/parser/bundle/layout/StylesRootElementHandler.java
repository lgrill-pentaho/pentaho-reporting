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

package org.pentaho.reporting.engine.classic.core.modules.parser.bundle.layout;

import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.PageFooter;
import org.pentaho.reporting.engine.classic.core.PageHeader;
import org.pentaho.reporting.engine.classic.core.SubReport;
import org.pentaho.reporting.engine.classic.core.Watermark;
import org.pentaho.reporting.engine.classic.core.function.Expression;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.ReportParserUtil;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.BundleNamespaces;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.layout.elements.PageFooterReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.layout.elements.PageHeaderReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.layout.elements.WatermarkReadHandler;
import org.pentaho.reporting.engine.classic.core.style.css.ElementStyleDefinition;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.IgnoreAnyChildReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Todo: Document Me
 *
 * @author Thomas Morgner
 */
public class StylesRootElementHandler extends AbstractXmlReadHandler {
  private PageFooterReadHandler pageFooterReadHandler;
  private PageHeaderReadHandler pageHeaderReadHandler;
  private WatermarkReadHandler watermarkReadHandler;
  private LayoutProcessorReadHandler layoutProcessorHandler;
  private AbstractReportDefinition report;
  private StyleDefinitionReadHandler styleDefinitionReadHandler;

  public StylesRootElementHandler() {
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
    final Object maybeReport = getRootHandler().getHelperObject( ReportParserUtil.HELPER_OBJ_REPORT_NAME );
    if ( maybeReport instanceof SubReport ) {
      report = (SubReport) maybeReport;
    } else if ( maybeReport instanceof MasterReport ) {
      report = (MasterReport) maybeReport;
    } else {
      if ( ReportParserUtil.INCLUDE_PARSING_VALUE.equals( getRootHandler().getHelperObject(
          ReportParserUtil.INCLUDE_PARSING_KEY ) ) ) {
        report = new SubReport();
      } else {
        report = new MasterReport();
      }
      getRootHandler().setHelperObject( ReportParserUtil.HELPER_OBJ_REPORT_NAME, report );
    }
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
    if ( BundleNamespaces.STYLE.equals( uri ) ) {
      if ( "page-definition".equals( tagName ) ) {
        return new PageDefinitionReadHandler();
      }

      if ( "style-rule".equals( tagName ) ) {
        return new IgnoreAnyChildReadHandler();
      }
      if ( "style-definition".equals( tagName ) ) {
        styleDefinitionReadHandler = new StyleDefinitionReadHandler();
        return styleDefinitionReadHandler;
      }
    }
    if ( BundleNamespaces.LAYOUT.equals( uri ) ) {
      if ( "layout-processors".equals( tagName ) ) {
        layoutProcessorHandler = new LayoutProcessorReadHandler();
        return layoutProcessorHandler;
      }

      if ( "watermark".equals( tagName ) ) {
        if ( watermarkReadHandler == null ) {
          watermarkReadHandler = new WatermarkReadHandler();
        }
        return watermarkReadHandler;
      }

      if ( "page-header".equals( tagName ) ) {
        if ( pageHeaderReadHandler == null ) {
          pageHeaderReadHandler = new PageHeaderReadHandler();
        }
        return pageHeaderReadHandler;
      }

      if ( "page-footer".equals( tagName ) ) {
        if ( pageFooterReadHandler == null ) {
          pageFooterReadHandler = new PageFooterReadHandler();
        }
        return pageFooterReadHandler;
      }
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
    super.doneParsing(); // To change body of overridden methods use File | Settings | File Templates.
    if ( pageHeaderReadHandler != null ) {
      report.setPageHeader( (PageHeader) pageHeaderReadHandler.getElement() );
    }
    if ( watermarkReadHandler != null ) {
      report.setWatermark( (Watermark) watermarkReadHandler.getElement() );
    }
    if ( pageFooterReadHandler != null ) {
      report.setPageFooter( (PageFooter) pageFooterReadHandler.getElement() );
    }

    if ( layoutProcessorHandler != null ) {
      final Expression[] expressions = layoutProcessorHandler.getExpressions();
      for ( int i = 0; i < expressions.length; i++ ) {
        final Expression expression = expressions[i];
        report.addExpression( expression );
      }
    }
    if ( styleDefinitionReadHandler != null ) {
      final ElementStyleDefinition styleDefinition = styleDefinitionReadHandler.getObject();
      if ( report instanceof MasterReport && styleDefinition != null ) {
        final MasterReport master = (MasterReport) report;
        master.setStyleDefinition( styleDefinition );
      }
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
    return report;
  }
}
