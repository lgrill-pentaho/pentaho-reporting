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
package org.pentaho.reporting.engine.classic.core.bugs;

import junit.framework.TestCase;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.layout.model.LayoutNodeTypes;
import org.pentaho.reporting.engine.classic.core.layout.model.LogicalPageBox;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderNode;
import org.pentaho.reporting.engine.classic.core.layout.table.TableTestUtil;
import org.pentaho.reporting.engine.classic.core.testsupport.DebugReportRunner;
import org.pentaho.reporting.engine.classic.core.testsupport.selector.MatchFactory;

public class Prd4738IT extends TestCase {
  public Prd4738IT() {
  }

  protected void setUp() throws Exception {
    ClassicEngineBoot.getInstance().start();
  }

  public void testBug() throws Exception {
    final MasterReport report = new MasterReport();
    report.getReportHeader().setLayout( "row" );
    report.getReportHeader().addElement( TableTestUtil.createDataItem( "Test1" ) );
    report.getReportHeader().addElement( TableTestUtil.createDataItem( "Test2" ) );
    report.getReportConfiguration().setConfigProperty(
        "org.pentaho.reporting.engine.classic.core.modules.output.table.xml.AssumeOverflowX", "true" );
    report.getReportConfiguration().setConfigProperty(
        "org.pentaho.reporting.engine.classic.core.modules.output.table.xls.AssumeOverflowX", "true" );
    final LogicalPageBox box = DebugReportRunner.layoutTablePage( report, 0 );

    final RenderNode[] elementsByNodeType =
        MatchFactory.findElementsByNodeType( box, LayoutNodeTypes.TYPE_BOX_PARAGRAPH );
    for ( int i = 0; i < elementsByNodeType.length; i++ ) {
      RenderNode renderNode = elementsByNodeType[i];
      assertEquals( 10000000, renderNode.getWidth() );
    }
  }
}
