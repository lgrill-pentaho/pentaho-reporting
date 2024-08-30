/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql;

import java.sql.Connection;

public class DefaultParametrizationProviderFactory implements ParametrizationProviderFactory {
  public DefaultParametrizationProviderFactory() {
  }

  public ParametrizationProvider create( final Connection connection ) {
    return new DefaultParametrizationProvider();
  }
}
