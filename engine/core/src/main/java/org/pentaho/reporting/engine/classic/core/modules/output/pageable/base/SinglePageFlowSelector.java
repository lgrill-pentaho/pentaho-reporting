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

package org.pentaho.reporting.engine.classic.core.modules.output.pageable.base;

import org.pentaho.reporting.engine.classic.core.layout.output.LogicalPageKey;
import org.pentaho.reporting.engine.classic.core.layout.output.PhysicalPageKey;

public class SinglePageFlowSelector implements PageFlowSelector {
  private int acceptedPage;
  private boolean logicalPage;

  public SinglePageFlowSelector( final int acceptedPage, final boolean logicalPage ) {
    this.acceptedPage = acceptedPage;
    this.logicalPage = logicalPage;
  }

  public SinglePageFlowSelector( final int acceptedPage ) {
    this( acceptedPage, true );
  }

  public boolean isPhysicalPageAccepted( final PhysicalPageKey key ) {
    if ( key == null ) {
      return false;
    }
    return logicalPage == false && key.getSequentialPageNumber() == acceptedPage;
  }

  public boolean isLogicalPageAccepted( final LogicalPageKey key ) {
    if ( key == null ) {
      return false;
    }
    return logicalPage && key.getPosition() == acceptedPage;
  }
}
