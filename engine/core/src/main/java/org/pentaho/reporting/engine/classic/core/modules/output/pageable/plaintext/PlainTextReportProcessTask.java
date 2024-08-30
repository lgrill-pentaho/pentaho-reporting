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

package org.pentaho.reporting.engine.classic.core.modules.output.pageable.plaintext;

import org.pentaho.reporting.engine.classic.core.AbstractReportProcessTask;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.event.ReportProgressListener;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.base.PageableReportProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.plaintext.driver.TextFilePrinterDriver;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.repository.ContentItem;
import org.pentaho.reporting.libraries.repository.ContentLocation;
import org.pentaho.reporting.libraries.repository.NameGenerator;
import org.pentaho.reporting.libraries.xmlns.common.ParserUtil;

import java.io.OutputStream;

public class PlainTextReportProcessTask extends AbstractReportProcessTask {
  public PlainTextReportProcessTask() {
  }

  /**
   * @noinspection ThrowableInstanceNeverThrown
   */
  public void run() {
    if ( isValid() == false ) {
      setError( new ReportProcessingException( "Error: The task is not configured properly." ) );
      return;
    }

    setError( null );
    try {
      final MasterReport masterReport = getReport();
      final Configuration configuration = masterReport.getConfiguration();

      final ContentLocation contentLocation = getBodyContentLocation();
      final NameGenerator nameGenerator = getBodyNameGenerator();
      final ContentItem contentItem = contentLocation.createItem( nameGenerator.generateName( null, "text/plain" ) );
      final OutputStream outputStream = contentItem.getOutputStream();

      try {
        final String cpiText =
            configuration
                .getConfigProperty( "org.pentaho.reporting.engine.classic.core.modules.output.pageable.plaintext.CharsPerInch" );
        final String lpiText =
            configuration
                .getConfigProperty( "org.pentaho.reporting.engine.classic.core.modules.output.pageable.plaintext.LinesPerInch" );

        final TextFilePrinterDriver driver =
            new TextFilePrinterDriver( outputStream, ParserUtil.parseInt( cpiText, 10 ), ParserUtil.parseInt( lpiText,
                6 ) );

        final PageableTextOutputProcessor outputProcessor = new PageableTextOutputProcessor( driver, configuration );
        final PageableReportProcessor streamReportProcessor =
            new PageableReportProcessor( masterReport, outputProcessor );
        try {
          final ReportProgressListener[] progressListeners = getReportProgressListeners();
          for ( int i = 0; i < progressListeners.length; i++ ) {
            final ReportProgressListener listener = progressListeners[i];
            streamReportProcessor.addReportProgressListener( listener );
          }
          streamReportProcessor.processReport();
        } finally {
          streamReportProcessor.close();
        }
      } finally {
        outputStream.close();
      }
    } catch ( Throwable e ) {
      setError( e );
    }
  }

  public String getReportMimeType() {
    return "text/plain";
  }
}
