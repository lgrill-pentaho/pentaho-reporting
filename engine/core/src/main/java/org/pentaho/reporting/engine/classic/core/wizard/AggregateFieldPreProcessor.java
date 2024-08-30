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

package org.pentaho.reporting.engine.classic.core.wizard;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.AbstractReportPreProcessor;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.CrosstabCell;
import org.pentaho.reporting.engine.classic.core.CrosstabColumnGroup;
import org.pentaho.reporting.engine.classic.core.CrosstabRowGroup;
import org.pentaho.reporting.engine.classic.core.Group;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportDefinition;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.Section;
import org.pentaho.reporting.engine.classic.core.SubReport;
import org.pentaho.reporting.engine.classic.core.function.AggregationFunction;
import org.pentaho.reporting.engine.classic.core.function.FieldAggregationFunction;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;

import java.util.HashSet;

public class AggregateFieldPreProcessor extends AbstractReportPreProcessor {
  private static final Log logger = LogFactory.getLog( AggregateFieldPreProcessor.class );
  private HashSet<String> generatedExpressionNames;
  private DataSchema schema;
  private AbstractReportDefinition definition;
  private Group[] groups;

  public AggregateFieldPreProcessor() {
  }

  public MasterReport performPreProcessing( final MasterReport definition, final DefaultFlowController flowController )
    throws ReportProcessingException {
    try {
      this.generatedExpressionNames = new HashSet<String>();
      this.definition = definition;
      this.schema = flowController.getDataSchema();
      this.groups = AutoGeneratorUtility.getGroups( definition );

      processSection( definition );
      return definition;
    } finally {
      this.groups = null;
      this.definition = null;
      this.schema = null;
      this.generatedExpressionNames = null;
    }
  }

  public SubReport performPreProcessing( final SubReport definition, final DefaultFlowController flowController )
    throws ReportProcessingException {
    try {
      this.generatedExpressionNames = new HashSet<String>();
      this.definition = definition;
      this.schema = flowController.getDataSchema();
      this.groups = AutoGeneratorUtility.getGroups( definition );

      processSection( definition );
      return definition;
    } finally {
      this.groups = null;
      this.definition = null;
      this.schema = null;
      this.generatedExpressionNames = null;
    }
  }

  private void processSection( final Section section ) throws ReportProcessingException {
    final int count = section.getElementCount();
    for ( int i = 0; i < count; i++ ) {
      final ReportElement element = section.getElement( i );
      if ( element instanceof SubReport ) {
        continue;
      }

      if ( element instanceof Section ) {
        processSection( (Section) element );
        continue;
      }

      final Object attribute =
          element.getAttribute( AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.AGGREGATION_TYPE );
      if ( attribute instanceof Class == false ) {
        continue;
      }

      final Class aggType = (Class) attribute;
      if ( AggregationFunction.class.isAssignableFrom( aggType ) == false ) {
        continue;
      }

      try {
        processAggregateElement( element, aggType );
      } catch ( Exception e ) {
        throw new ReportProcessingException( "Failed to pre-process the report", e );
      }
    }
  }

  protected void processAggregateElement( final ReportElement element, final Class<AggregationFunction> aggType )
    throws InstantiationException, IllegalAccessException, ReportProcessingException {
    final AggregationFunction o = aggType.newInstance();

    if ( configureCrosstabAggregation( element, o ) == false ) {
      configureRelationalAggreation( element, o );
    }

    final String fieldName = (String) element.getAttribute( AttributeNames.Core.NAMESPACE, AttributeNames.Core.FIELD );
    if ( o instanceof FieldAggregationFunction ) {
      final FieldAggregationFunction fo = (FieldAggregationFunction) o;
      fo.setField( fieldName );
    }

    final Object labelFor = element.getAttribute( AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.LABEL_FOR );
    if ( labelFor == null ) {
      element.setAttribute( AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.LABEL_FOR, fieldName );
    }

    final String name =
        AutoGeneratorUtility.generateUniqueExpressionName( schema, "::wizard:aggregation:{0}", generatedExpressionNames
            .toArray( new String[generatedExpressionNames.size()] ) );
    o.setName( name );
    generatedExpressionNames.add( name );

    element.setAttribute( AttributeNames.Core.NAMESPACE, AttributeNames.Core.FIELD, name );
    // finally clean up
    element.setAttribute( AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.AGGREGATION_TYPE, null );
    definition.addExpression( o );
  }

  private void configureRelationalAggreation( final ReportElement element, final AggregationFunction o ) {
    // relational element ...
    final String group =
        (String) element.getAttribute( AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.AGGREGATION_GROUP );
    if ( group != null ) {
      o.setGroup( group );
    } else {
      final Group g = findGroup( element );
      if ( g != null ) {
        o.setGroup( g.getGeneratedName() );
      }
    }
  }

  private boolean configureCrosstabAggregation( final ReportElement element, final AggregationFunction o )
    throws ReportProcessingException {
    final CrosstabCell crosstabCell = findCrosstabCell( element );
    if ( crosstabCell == null ) {
      return false;
    }

    final String columnField = crosstabCell.getColumnField();
    final String rowField = crosstabCell.getRowField();

    if ( columnField == null && rowField == null ) {
      // special case handling for detail cells.
      // detail cells have no filter, and reset on the innermost column group.
      // This saves a few bytes as we dont have to run a result-sequence for this case.
      final CrosstabColumnGroup group = (CrosstabColumnGroup) groups[groups.length - 1];
      final String name = group.getName();
      o.setGroup( name );
      o.setCrosstabFilterGroup( null );
      return true;
    }

    if ( rowField == null ) {
      // this is a detail-row.
      final CrosstabRowGroup lastRowGroup = findLastRowGroup();
      o.setGroup( lastRowGroup.getName() );
    } else {
      final CrosstabRowGroup rowGroup = findRowGroup( rowField );
      final Section containingBody = rowGroup.getParentSection();
      final Section containingGroup = containingBody.getParentSection();
      o.setGroup( containingGroup.getName() );
    }

    if ( columnField == null ) {
      final Group lastColumnGroup = groups[groups.length - 1];
      o.setCrosstabFilterGroup( lastColumnGroup.getName() );
    } else {
      final CrosstabColumnGroup columnGroup = findColumnGroup( columnField );
      final Section containingBody = columnGroup.getParentSection();
      final Section containingGroup = containingBody.getParentSection();
      if ( containingGroup instanceof CrosstabColumnGroup ) {
        o.setCrosstabFilterGroup( containingGroup.getName() );
      }
    }

    logger.debug( "Aggregation-Configuration: " + o.getClass() );
    logger.debug( " - column         : " + columnField );
    logger.debug( " - row            : " + rowField );
    logger.debug( " - filter-group   : " + o.getCrosstabFilterGroup() );
    logger.debug( " - reset-group    : " + o.getGroup() );
    return true;
  }

  private CrosstabRowGroup findLastRowGroup() throws ReportProcessingException {
    for ( int i = groups.length - 1; i >= 0; i -= 1 ) {
      final Group group = groups[i];
      if ( group instanceof CrosstabRowGroup ) {
        return (CrosstabRowGroup) group;
      }
    }

    // This is a hard error. No point in waiting to fail here.
    throw new ReportProcessingException( "Trying to find a crosstab-row, but there is none." );
  }

  private Group findGroup( final ReportElement element ) {
    Section parentSection = element.getParentSection();
    while ( parentSection != null ) {
      if ( parentSection instanceof ReportDefinition ) {
        break;
      }

      if ( parentSection instanceof Group ) {
        return (Group) parentSection;
      }
      parentSection = parentSection.getParentSection();
    }
    return null;
  }

  private CrosstabRowGroup findRowGroup( final String field ) throws ReportProcessingException {
    for ( int i = 0; i < groups.length; i++ ) {
      final Group group = groups[i];
      if ( group instanceof CrosstabRowGroup ) {
        final CrosstabRowGroup rowGroup = (CrosstabRowGroup) group;
        if ( ObjectUtilities.equal( rowGroup.getField(), field ) ) {
          return rowGroup;
        }
      }
    }

    // This is a hard error. No point in waiting to fail here.
    throw new ReportProcessingException( "Trying to find a crosstab-row for field '" + field + "', but there is none." );
  }

  private CrosstabColumnGroup findColumnGroup( final String field ) throws ReportProcessingException {
    for ( int i = 0; i < groups.length; i++ ) {
      final Group group = groups[i];
      if ( group instanceof CrosstabColumnGroup ) {
        final CrosstabColumnGroup columnGroup = (CrosstabColumnGroup) group;
        if ( ObjectUtilities.equal( columnGroup.getField(), field ) ) {
          return columnGroup;
        }
      }
    }

    // This is a hard error. No point in waiting to fail here.
    throw new ReportProcessingException( "Trying to find a crosstab-column for field '" + field
        + "', but there is none." );
  }

  private CrosstabCell findCrosstabCell( final ReportElement element ) {
    Section parentSection = element.getParentSection();
    while ( parentSection != null ) {
      if ( parentSection instanceof ReportDefinition ) {
        break;
      }

      if ( parentSection instanceof CrosstabCell ) {
        return (CrosstabCell) parentSection;
      }
      parentSection = parentSection.getParentSection();
    }
    return null;
  }
}
