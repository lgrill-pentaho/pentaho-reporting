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

package org.pentaho.reporting.designer.core.actions.report.export;

import org.pentaho.reporting.engine.classic.core.modules.gui.xls.ExcelExportPlugin;

/**
 * Todo: Document Me
 *
 * @author Ezequiel Cuellar
 */
public final class ExportXlsAction extends AbstractExportAction {
  public ExportXlsAction() {
    super( new ExcelExportPlugin() );
  }
}
