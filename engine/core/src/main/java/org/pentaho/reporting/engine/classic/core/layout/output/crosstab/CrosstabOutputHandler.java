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

package org.pentaho.reporting.engine.classic.core.layout.output.crosstab;

import org.pentaho.reporting.engine.classic.core.Band;
import org.pentaho.reporting.engine.classic.core.CrosstabGroup;
import org.pentaho.reporting.engine.classic.core.GroupBody;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.event.ReportEvent;
import org.pentaho.reporting.engine.classic.core.layout.Renderer;
import org.pentaho.reporting.engine.classic.core.layout.output.DefaultOutputFunction;
import org.pentaho.reporting.engine.classic.core.layout.output.GroupOutputHandler;
import org.pentaho.reporting.engine.classic.core.states.crosstab.CrosstabSpecification;

public class CrosstabOutputHandler implements GroupOutputHandler {
  public CrosstabOutputHandler() {
  }

  public void groupStarted( final DefaultOutputFunction outputFunction, final ReportEvent event )
    throws ReportProcessingException {
    final int gidx = event.getState().getCurrentGroupIndex();
    final CrosstabGroup group = (CrosstabGroup) event.getReport().getGroup( gidx );
    final Band b = group.getHeader();
    final GroupBody groupBody = group.getBody();

    final RenderedCrosstabLayout renderedCrosstabLayout = outputFunction.startRenderedCrosstabLayout();
    final CrosstabSpecification crosstabSpecification =
        event.getState().getFlowController().getMasterRow().getCrosstabSpecification();
    renderedCrosstabLayout.initialize( crosstabSpecification, group, gidx );

    outputFunction.updateFooterArea( event );
    final Renderer renderer = outputFunction.getRenderer();
    renderer.startGroup( group, event.getState().getPredictedStateCount() );
    renderer.startSection( Renderer.SectionType.NORMALFLOW );
    renderer.add( b, outputFunction.getRuntime() );
    outputFunction.addSubReportMarkers( renderer.endSection() );

    renderer.startGroupBody( groupBody, event.getState().getPredictedStateCount() );
  }

  public void groupBodyFinished( final DefaultOutputFunction outputFunction, final ReportEvent event )
    throws ReportProcessingException {
    CrosstabOutputHelper.closeCrosstabTable( outputFunction );

    final Renderer renderer = outputFunction.getRenderer();
    outputFunction.updateFooterArea( event );
    renderer.endGroupBody();
  }

  public void groupFinished( final DefaultOutputFunction outputFunction, final ReportEvent event )
    throws ReportProcessingException {
    final int gidx = event.getState().getCurrentGroupIndex();
    final CrosstabGroup g = (CrosstabGroup) event.getReport().getGroup( gidx );
    final Band b = g.getFooter();

    final Renderer renderer = outputFunction.getRenderer();
    outputFunction.updateFooterArea( event );

    renderer.startSection( Renderer.SectionType.NORMALFLOW );
    renderer.add( b, outputFunction.getRuntime() );
    outputFunction.addSubReportMarkers( renderer.endSection() );
    renderer.endGroup();

    outputFunction.endRenderedCrosstabLayout();
  }

  public void itemsStarted( final DefaultOutputFunction outputFunction, final ReportEvent event )
    throws ReportProcessingException {
    throw new ReportProcessingException( "A crosstab-group cannot contain a detail band. Never." );
  }

  public void itemsAdvanced( final DefaultOutputFunction outputFunction, final ReportEvent event )
    throws ReportProcessingException {
    throw new ReportProcessingException( "A crosstab-group cannot contain a detail band. Never." );
  }

  public void itemsFinished( final DefaultOutputFunction outputFunction, final ReportEvent event )
    throws ReportProcessingException {
    throw new ReportProcessingException( "A crosstab-group cannot contain a detail band. Never." );
  }

  public void summaryRowStart( final DefaultOutputFunction outputFunction, final ReportEvent event )
    throws ReportProcessingException {
    throw new ReportProcessingException( "A crosstab-group cannot contain a summary band. Never." );
  }

  public void summaryRowEnd( final DefaultOutputFunction outputFunction, final ReportEvent event )
    throws ReportProcessingException {
    throw new ReportProcessingException( "A crosstab-group cannot contain a summary band. Never." );
  }

  public void summaryRow( final DefaultOutputFunction outputFunction, final ReportEvent event )
    throws ReportProcessingException {
    throw new ReportProcessingException( "A crosstab-group cannot contain a summary band. Never." );
  }
}
