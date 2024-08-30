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

package org.pentaho.plugin.jfreereport.reportcharts;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.event.ReportEvent;
import org.pentaho.reporting.engine.classic.core.function.Expression;
import org.pentaho.reporting.engine.classic.core.function.FunctionUtilities;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creation-Date: 22.09.2005, 18:30:22
 *
 * @author Thomas Morgner
 * @deprecated for new reports, use the CategorySetDataCollector
 */
public class CategorySetCollectorFunction extends BaseCollectorFunction {
  private static final long serialVersionUID = -8138304452870844825L;
  private ArrayList valueColumns;
  private ArrayList ignoreColumns;
  private String categoryColumn;
  private int categoryStartColumn;
  private boolean generatedReport;

  public CategorySetCollectorFunction() {
    this.valueColumns = new ArrayList();
    this.ignoreColumns = new ArrayList();

    generatedReport = false;
  }

  public void setGeneratedReport( final boolean value ) {
    generatedReport = value;
  }

  public boolean isGeneratedReport() {
    return generatedReport;
  }

  public void setIgnoreColumn( final int index, final String field ) {
    if ( ignoreColumns.size() == index ) {
      ignoreColumns.add( field );
    } else {
      ignoreColumns.set( index, field );
    }
  }

  public String getIgnoreColumn( final int index ) {
    return (String) ignoreColumns.get( index );
  }

  public int getIgnoreColumnCount() {
    return ignoreColumns.size();
  }

  public String[] getIgnoreColumn() {
    return (String[]) ignoreColumns.toArray( new String[ ignoreColumns.size() ] );
  }

  public void setIgnoreColumn( final String[] fields ) {
    this.ignoreColumns.clear();
    this.ignoreColumns.addAll( Arrays.asList( fields ) );
  }

  public void setCategoryStartColumn( final int value ) {
    categoryStartColumn = value;
  }

  public int getCategoryStartColumn() {
    return categoryStartColumn;
  }

  public void setValueColumn( final int index, final String field ) {
    if ( valueColumns.size() == index ) {
      valueColumns.add( field );
    } else {
      valueColumns.set( index, field );
    }
  }

  public String getValueColumn( final int index ) {
    return (String) valueColumns.get( index );
  }

  public int getValueColumnCount() {
    return valueColumns.size();
  }

  public String[] getValueColumn() {
    return (String[]) valueColumns.toArray( new String[ valueColumns.size() ] );
  }

  public void setValueColumn( final String[] fields ) {
    this.valueColumns.clear();
    this.valueColumns.addAll( Arrays.asList( fields ) );
  }

  public String getCategoryColumn() {
    return categoryColumn;
  }

  public void setCategoryColumn( final String categoryColumn ) {
    this.categoryColumn = categoryColumn;
  }

  /*
   * ---------------------------------------------------------------- Now the function implementation ...
   */

  public Dataset createNewDataset() {
    return new DefaultCategoryDataset();
  }

  public void itemsAdvanced( final ReportEvent reportEvent ) {
    if ( FunctionUtilities.isDefinedPrepareRunLevel( this, reportEvent ) == false ) {
      // we do not modify the created dataset if this is not the function
      // computation run. (FunctionLevel '0')
      return;
    }

    if ( this.isSummaryOnly() ) {
      return;
    }

    buildDataset();
  }

  protected void buildDataset() {
    final DefaultCategoryDataset categoryDataset = (DefaultCategoryDataset) getDatasourceValue();
    if ( isGeneratedReport() ) {
      buildAutoGeneratedDataSet( categoryDataset );
      return;
    }

    final DataRow dataRow = getDataRow();
    final Object categoryObject = dataRow.get( getCategoryColumn() );
    final Comparable categoryComparable;
    if ( categoryObject instanceof Comparable ) {
      categoryComparable = (Comparable) categoryObject;
    } else {
      // ok, we need some better error management here. Its a
      // prototype :)
      categoryComparable = ( "CATEGORYSETCOLL.USER_ERROR_CATEGORY_NOT_COMPARABLE" ); //$NON-NLS-1$
    }

    // I love to be paranoid!
    final String[] seriesNames = getSeriesName();
    final int maxIndex = Math.min( seriesNames.length, this.valueColumns.size() );
    for ( int i = 0; i < maxIndex; i++ ) {
      String seriesName = seriesNames[ i ];
      final String column = (String) valueColumns.get( i );
      final Object valueObject = dataRow.get( column );
      if ( isSeriesColumn() ) {
        final Object tmp = dataRow.get( seriesName );
        if ( tmp != null ) {
          seriesName = tmp.toString();
        }
      }

      final Number value = ( valueObject instanceof Number ) ? (Number) valueObject : null;

      final Number existingValue = queryExistingValueFromDataSet( categoryDataset, seriesName, categoryComparable );
      if ( existingValue != null ) {
        if ( value != null ) {
          final double val = value.doubleValue();
          categoryDataset.addValue( new Double( val + existingValue.doubleValue() ), seriesName, categoryComparable );
        }
      } else {
        categoryDataset.addValue( value, seriesName, categoryComparable );
      }
    }
  }

  protected Number queryExistingValueFromDataSet( final CategoryDataset dataset,
                                                  final Comparable seriesName,
                                                  final Comparable columnKey ) {
    try {
      return dataset.getValue( seriesName, columnKey );
    } catch ( Exception ignored ) {
      // dataset.getValue throws exceptions if the keys dont match ..
    }
    return null;
  }


  public void groupFinished( final ReportEvent reportEvent ) {
    logger.debug( ( "CATEGORYSETCOLL.USER_DEBUG_GROUPS_FINISHED" ) ); //$NON-NLS-1$

    if ( FunctionUtilities.isDefinedPrepareRunLevel( this, reportEvent ) == false ) {
      // we do not modify the created dataset if this is not the function
      // computation run. (FunctionLevel '0')
      return;
    }

    if ( !this.isSummaryOnly() ) {
      return;
    }

    if ( !FunctionUtilities.isDefinedGroup( getGroup(), reportEvent ) ) {
      return;
    }
    buildDataset();
  }

  private void buildAutoGeneratedDataSet( final DefaultCategoryDataset categoryDataset ) {
    final DataRow dataRow = getDataRow();
    // generatedReport == true

    // if the generatedReport flag is true, then we are dynamically
    // generating the number of columns in the report ...

    // We expect additional information in order to get the dataset
    // built properly:
    // ignoreColumns and categoryStartColumn

    final String[] columnNames = dataRow.getColumnNames();
    for ( int i = categoryStartColumn; i < columnNames.length; i++ ) {
      String seriesName = columnNames[ i ];

      // this is legacy code ..
      if ( !seriesName.startsWith( "Summary_" ) ) {
        continue;
      }

      if ( ignoreColumns.contains( seriesName ) ) {
        continue;
      }

      seriesName = seriesName.substring( 8, seriesName.length() - 10 );
      final Object valueObject = dataRow.get( seriesName );

      final Number value = ( valueObject instanceof Number ) ? (Number) valueObject : null;
      categoryDataset.addValue( value, seriesName, seriesName );
    }
  }

  /**
   * Return a completly separated copy of this function. The copy no longer shares any changeable objects with the
   * original function. Also from Thomas: Should retain data from the report definition, but clear calculated data.
   *
   * @return a copy of this function.
   */
  public Expression getInstance() {
    final CategorySetCollectorFunction fn = (CategorySetCollectorFunction) super.getInstance();
    fn.valueColumns = (ArrayList) valueColumns.clone();
    fn.ignoreColumns = (ArrayList) ignoreColumns.clone();
    return fn;
  }

}
