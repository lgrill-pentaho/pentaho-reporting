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

package org.pentaho.reporting.engine.classic.core.modules.misc.connections;

import java.io.File;

public class InMemoryDataSourceMgmtService extends FileDataSourceMgmtService {
  public InMemoryDataSourceMgmtService() {
  }

  /**
   * A quick and dirty way of removing load and save from the file store.
   *
   * @return
   */
  protected File createTargetFile() {
    return null;
  }
}
