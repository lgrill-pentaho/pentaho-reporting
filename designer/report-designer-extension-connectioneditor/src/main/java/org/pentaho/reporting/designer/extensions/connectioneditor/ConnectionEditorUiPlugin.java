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
package org.pentaho.reporting.designer.extensions.connectioneditor;

import org.pentaho.reporting.designer.core.AbstractReportDesignerUiPlugin;

public class ConnectionEditorUiPlugin extends AbstractReportDesignerUiPlugin {
  public ConnectionEditorUiPlugin() {
  }

  public String[] getOverlaySources() {
    return new String[] { "org/pentaho/reporting/designer/extensions/connectioneditor/ui-overlay.xul" };
  }
}
