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
package org.pentaho.reporting.engine.classic.core.modules.gui.csv;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.modules.gui.base.ExportTaskCommon;
import org.pentaho.reporting.engine.classic.core.modules.gui.pdf.PdfExportTask;
import org.pentaho.reporting.libraries.base.config.DefaultConfiguration;
import org.pentaho.reporting.libraries.base.config.ModifiableConfiguration;

import java.io.File;

public class CSVTableExportTaskTest extends ExportTaskCommon {
  @Test
  public void testCreateParentFolder() {
    String filename = getTestDirectory() + "/" + "testfile.csv";

    MasterReport report = Mockito.mock( MasterReport.class );
    ModifiableConfiguration conf = Mockito.spy( new DefaultConfiguration() );
    conf.setConfigProperty( "org.pentaho.reporting.engine.classic.core.modules.gui.csv.FileName", filename );
    conf.setConfigProperty( "org.pentaho.reporting.engine.classic.core.modules.gui.csv.CreateParentFolder", "true" );
    Mockito.doReturn(conf).when(report).getConfiguration();
    File file = null;
    File directory;
    try {
      file = new File( filename ).getCanonicalFile();
      directory = file.getParentFile();
      if ( directory != null ) {
        if ( directory.exists() == true ) {
          directory.delete();
        }
      }
      Assert.assertFalse( directory.exists() );
      CSVTableExportTask task = new CSVTableExportTask( report, null, null );
      task.run();
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    directory = file.getParentFile();
    Assert.assertTrue( directory != null );
    Assert.assertTrue( directory.exists() );
    file.delete();
    directory.delete();
  }
}
