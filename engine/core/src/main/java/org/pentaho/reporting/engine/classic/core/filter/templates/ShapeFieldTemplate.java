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

package org.pentaho.reporting.engine.classic.core.filter.templates;

import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.filter.DataRowDataSource;
import org.pentaho.reporting.engine.classic.core.filter.ShapeFilter;
import org.pentaho.reporting.engine.classic.core.function.ExpressionRuntime;

/**
 * A shape field template.
 *
 * @author Thomas Morgner.
 */
public class ShapeFieldTemplate extends AbstractTemplate {
  /**
   * The data row reader.
   */
  private DataRowDataSource dataRowDataSource;

  /**
   * A shape filter.
   */
  private ShapeFilter shapeFilter;

  /**
   * Creates a new shape field template.
   */
  public ShapeFieldTemplate() {
    dataRowDataSource = new DataRowDataSource();
    shapeFilter = new ShapeFilter();
    shapeFilter.setDataSource( dataRowDataSource );
  }

  /**
   * Returns the field name.
   *
   * @return The field name.
   */
  public String getField() {
    return dataRowDataSource.getDataSourceColumnName();
  }

  /**
   * Sets the field name.
   *
   * @param field
   *          the field name.
   */
  public void setField( final String field ) {
    dataRowDataSource.setDataSourceColumnName( field );
  }

  /**
   * Returns the formula used to compute the value of the data source.
   *
   * @return the formula.
   */
  public String getFormula() {
    return dataRowDataSource.getFormula();
  }

  /**
   * Defines the formula used to compute the value of this data source.
   *
   * @param formula
   *          the formula for the data source.
   */
  public void setFormula( final String formula ) {
    dataRowDataSource.setFormula( formula );
  }

  /**
   * Returns the current value for the data source.
   *
   * @param runtime
   *          the expression runtime that is used to evaluate formulas and expressions when computing the value of this
   *          filter.
   * @param element
   * @return the value.
   */
  public Object getValue( final ExpressionRuntime runtime, final ReportElement element ) {
    return shapeFilter.getValue( runtime, element );
  }

  /**
   * Clones the template.
   *
   * @return the clone.
   * @throws CloneNotSupportedException
   *           this should never happen.
   */
  public ShapeFieldTemplate clone() throws CloneNotSupportedException {
    final ShapeFieldTemplate template = (ShapeFieldTemplate) super.clone();
    template.shapeFilter = shapeFilter.clone();
    template.dataRowDataSource = (DataRowDataSource) template.shapeFilter.getDataSource();
    return template;
  }

  /**
   * Returns the datarow data source used in this template.
   *
   * @return the datarow data source.
   */
  protected DataRowDataSource getDataRowDataSource() {
    return dataRowDataSource;
  }
}
