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

package org.pentaho.reporting.engine.classic.core.states.process;

import org.pentaho.reporting.engine.classic.core.ReportPreProcessor;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.SubReport;
import org.pentaho.reporting.engine.classic.core.layout.output.OutputProcessorFeature;
import org.pentaho.reporting.engine.classic.core.layout.output.OutputProcessorMetaData;
import org.pentaho.reporting.engine.classic.core.states.StateUtilities;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.wizard.DataSchemaDefinition;

class SubReportProcessPreprocessor {
  private final DefaultFlowController startFlowController;
  private DefaultFlowController flowController;
  private ReportPreProcessor[] processors;
  private boolean designtime;

  public SubReportProcessPreprocessor( final DefaultFlowController startFlowController ) {
    this.startFlowController = startFlowController;
    final OutputProcessorMetaData md = startFlowController.getReportContext().getOutputProcessorMetaData();
    this.designtime = md.isFeatureSupported( OutputProcessorFeature.DESIGNTIME );
  }

  public DefaultFlowController getFlowController() {
    return flowController;
  }

  public SubReport invokePreDataProcessing( final SubReport report ) throws ReportProcessingException {
    flowController = startFlowController;

    processors = StateUtilities.getAllPreProcessors( report, designtime );
    DataSchemaDefinition fullDefinition = report.getDataSchemaDefinition();
    SubReport fullReport = report;
    for ( int i = 0; i < processors.length; i++ ) {
      final ReportPreProcessor processor = processors[i];
      fullReport = processor.performPreDataProcessing( fullReport, flowController );
      if ( fullReport.getDataSchemaDefinition() != fullDefinition ) {
        fullDefinition = fullReport.getDataSchemaDefinition();
        flowController = flowController.updateDataSchema( fullDefinition );
      }
    }
    return fullReport;
  }

  public SubReport invokePreProcessing( final SubReport report ) throws ReportProcessingException {
    flowController = startFlowController;

    processors = StateUtilities.getAllPreProcessors( report, designtime );
    DataSchemaDefinition fullDefinition = report.getDataSchemaDefinition();
    SubReport fullReport = report;
    for ( int i = 0; i < processors.length; i++ ) {
      final ReportPreProcessor processor = processors[i];
      fullReport = processor.performPreProcessing( fullReport, flowController );
      if ( fullReport.getDataSchemaDefinition() != fullDefinition ) {
        fullDefinition = fullReport.getDataSchemaDefinition();
        flowController = flowController.updateDataSchema( fullDefinition );
      }
    }
    return fullReport;
  }
}
