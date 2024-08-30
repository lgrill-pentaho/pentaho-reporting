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

package org.pentaho.reporting.designer.testsupport;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.EmptyReportException;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportParameterValidationException;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.base.PageableReportProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.graphics.PrintReportProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfOutputProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.plaintext.PlainTextReportUtil;
import org.pentaho.reporting.engine.classic.core.modules.output.table.csv.CSVReportUtil;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.HtmlReportUtil;
import org.pentaho.reporting.engine.classic.core.modules.output.table.rtf.RTFReportUtil;
import org.pentaho.reporting.engine.classic.core.modules.output.table.xls.ExcelReportUtil;
import org.pentaho.reporting.libraries.base.util.NullOutputStream;

import java.io.OutputStream;

public class DebugReportRunner {
  private static final Log logger = LogFactory.getLog( DebugReportRunner.class );

  private DebugReportRunner() {
  }

  public static boolean createPlainText( final MasterReport report ) {
    try {
      PlainTextReportUtil.createPlainText( report, new NullOutputStream(), 10, 15 );
      return true;
    } catch ( ReportParameterValidationException p ) {
      return true;
    } catch ( Exception rpe ) {
      logger.debug( "Failed to execute plain text: ", rpe );
      return false;
    }
  }

  public static void createRTF( final MasterReport report )
    throws Exception {
    try {
      RTFReportUtil.createRTF( report, new NullOutputStream() );
    } catch ( IndexOutOfBoundsException ibe ) {
      // this is a known iText bug that does not get fixed.
    } catch ( ReportParameterValidationException p ) {
      // reports that have mandatory parameters are ok to fail.
    }
  }

  public static void createCSV( final MasterReport report )
    throws Exception {
    try {
      CSVReportUtil.createCSV( report, new NullOutputStream(), null );
    } catch ( ReportParameterValidationException e ) {

    }
  }

  public static void createXLS( final MasterReport report )
    throws Exception {
    try {
      ExcelReportUtil.createXLS( report, new NullOutputStream() );
    } catch ( ReportParameterValidationException e ) {

    }
  }

  public static void createStreamHTML( final MasterReport report )
    throws Exception {
    try {
      HtmlReportUtil.createStreamHTML( report, new NullOutputStream() );
    } catch ( ReportParameterValidationException e ) {

    }
  }

  public static void createZIPHTML( final MasterReport report )
    throws Exception {
    try {
      HtmlReportUtil.createZIPHTML( report, new NullOutputStream(), "report.html" );
    } catch ( ReportParameterValidationException e ) {

    }
  }

  public static boolean execGraphics2D( final MasterReport report ) {
    try {
      final PrintReportProcessor proc = new PrintReportProcessor( report );
      final int nop = proc.getNumberOfPages();
      if ( proc.isError() ) {
        if ( proc.getErrorReason() instanceof ReportParameterValidationException ) {
          return true;
        }
        return false;
      }
      if ( nop == 0 ) {
        return false;
      }
      for ( int i = 0; i < nop; i++ ) {
        if ( proc.getPageDrawable( i ) == null ) {
          return false;
        }
      }
      proc.close();
      return true;
    } catch ( ReportParameterValidationException p ) {
      // reports that have mandatory parameters are ok to fail.
      return true;
    } catch ( EmptyReportException ere ) {
      return true;
    } catch ( Exception e ) {
      logger.error( "Generating Graphics2D failed.", e );
      return false;
    }
  }

  /**
   * Saves a report to PDF format.
   *
   * @param report the report.
   * @return true or false.
   */
  public static boolean createPDF( final MasterReport report ) {
    OutputStream out = new NullOutputStream();
    try {
      final PdfOutputProcessor outputProcessor = new PdfOutputProcessor( report.getConfiguration(), out,
        report.getResourceManager() );
      final PageableReportProcessor proc = new PageableReportProcessor( report, outputProcessor );
      proc.processReport();
      return true;
    } catch ( ReportParameterValidationException e ) {
      return true;
    } catch ( Exception e ) {
      logger.error( "Writing PDF failed.", e );
      return false;
    }
  }

  public static void executeAll( final MasterReport report ) throws Exception {
    logger.debug( "   GRAPHICS2D .." );
    TestCase.assertTrue( DebugReportRunner.execGraphics2D( report ) );
    logger.debug( "   PDF .." );
    TestCase.assertTrue( DebugReportRunner.createPDF( report ) );
    logger.debug( "   CSV .." );
    DebugReportRunner.createCSV( report );
    logger.debug( "   PLAIN_TEXT .." );
    TestCase.assertTrue( DebugReportRunner.createPlainText( report ) );
    logger.debug( "   RTF .." );
    DebugReportRunner.createRTF( report );
    logger.debug( "   STREAM_HTML .." );
    DebugReportRunner.createStreamHTML( report );
    logger.debug( "   EXCEL .." );
    DebugReportRunner.createXLS( report );
    logger.debug( "   ZIP_HTML .." );
    DebugReportRunner.createZIPHTML( report );
  }
}
