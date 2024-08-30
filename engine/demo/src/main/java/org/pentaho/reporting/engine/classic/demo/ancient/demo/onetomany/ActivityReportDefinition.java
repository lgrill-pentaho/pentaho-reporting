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

package org.pentaho.reporting.engine.classic.demo.ancient.demo.onetomany;

import java.awt.BasicStroke;
import java.awt.geom.Point2D;

import org.pentaho.reporting.engine.classic.core.Band;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.RelationalGroup;
import org.pentaho.reporting.engine.classic.core.elementfactory.HorizontalLineElementFactory;
import org.pentaho.reporting.engine.classic.core.elementfactory.LabelElementFactory;
import org.pentaho.reporting.engine.classic.core.elementfactory.TextFieldElementFactory;
import org.pentaho.reporting.engine.classic.core.function.HideElementByNameFunction;
import org.pentaho.reporting.engine.classic.core.style.TextStyleKeys;
import org.pentaho.reporting.libraries.base.util.FloatDimension;

public class ActivityReportDefinition
{
  private MasterReport report;

  public ActivityReportDefinition(final MasterReport report)
  {
    this.report = report;
  }

  public void configure()
  {
    configureRecordGroup();
    configureItemBand();
    configureFunctions();
  }

  private void configureRecordGroup()
  {
    final Band b = new Band();
    b.setName("activitylog");
    b.getStyle().setStyleProperty(TextStyleKeys.BOLD, Boolean.TRUE);
    b.getStyle().setStyleProperty(TextStyleKeys.FONT, "SansSerif");
    b.getStyle().setStyleProperty(TextStyleKeys.FONTSIZE, new Integer(12));

    LabelElementFactory labelFactory = new LabelElementFactory();
    labelFactory.setAbsolutePosition(new Point2D.Float(0, 0));
    labelFactory.setMinimumSize(new FloatDimension(200, 15));
    labelFactory.setText("Time");
    b.addElement(labelFactory.createElement());

    labelFactory = new LabelElementFactory();
    labelFactory.setAbsolutePosition(new Point2D.Float(200, 0));
    labelFactory.setMinimumSize(new FloatDimension(-100, 15));
    labelFactory.setText("Task");
    b.addElement(labelFactory.createElement());

    b.addElement(HorizontalLineElementFactory.createHorizontalLine
        (15, null, new BasicStroke(1)));

    final RelationalGroup group = report.getGroupByName("record-group");
    group.getHeader().addElement(b);
  }

  private void configureItemBand()
  {
    final Band b = new Band();
    b.setName("activitylog");
    b.getStyle().setStyleProperty(TextStyleKeys.BOLD, Boolean.FALSE);
    b.getStyle().setStyleProperty(TextStyleKeys.FONT, "SansSerif");
    b.getStyle().setStyleProperty(TextStyleKeys.FONTSIZE, new Integer(10));

    TextFieldElementFactory textFieldFactory = new TextFieldElementFactory();
    textFieldFactory.setFieldname("activitylog.Time");
    textFieldFactory.setAbsolutePosition(new Point2D.Float(0, 0));
    textFieldFactory.setMinimumSize(new FloatDimension(200, 12));
    b.addElement(textFieldFactory.createElement());

    textFieldFactory = new TextFieldElementFactory();
    textFieldFactory.setFieldname("activitylog.Task");
    textFieldFactory.setAbsolutePosition(new Point2D.Float(200, 0));
    textFieldFactory.setMinimumSize(new FloatDimension(-100, 12));
    textFieldFactory.setDynamicHeight(Boolean.TRUE);
    b.addElement(textFieldFactory.createElement());

    report.getItemBand().addElement(b);
  }

  private void configureFunctions()
  {
    final HideElementByNameFunction function = new HideElementByNameFunction();
    function.setName("hideActivity");
    function.setField("recordType");
    function.setElement("activitylog");
    report.addExpression(function);
  }
}

