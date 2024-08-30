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

package org.pentaho.reporting.engine.classic.core.modules.misc.datafactory;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.metadata.DataFactoryMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.DefaultDataFactoryCore;

public class StaticDataFactoryCore extends DefaultDataFactoryCore {
  public StaticDataFactoryCore() {
  }

  public Object getQueryHash( final DataFactoryMetaData dataFactoryMetaData, final DataFactory dataFactory,
      final String queryName, final DataRow parameter ) {
    final StaticDataFactory staticDataFactory = (StaticDataFactory) dataFactory;
    return staticDataFactory.translateQuery( queryName );
  }

  public String[] getReferencedFields( final DataFactoryMetaData metaData, final DataFactory element,
      final String query, final DataRow parameter ) {
    final StaticDataFactory staticDataFactory = (StaticDataFactory) element;
    try {
      return staticDataFactory.getParameterFields( staticDataFactory.translateQuery( query ) );
    } catch ( ReportDataFactoryException e ) {
      throw new IllegalStateException( e );
    }
  }
}
