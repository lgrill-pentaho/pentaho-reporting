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

package org.pentaho.reporting.engine.classic.core.modules.gui.base.parameters;

import javax.swing.JComponent;

import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;

public interface ParameterComponent {
  public JComponent getUIComponent();

  public void initialize() throws ReportDataFactoryException;
}
