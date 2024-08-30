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

package org.pentaho.reporting.engine.classic.core.modules.output.pageable.graphics.internal;

import org.pentaho.reporting.engine.classic.core.layout.output.LogicalPageKey;
import org.pentaho.reporting.engine.classic.core.layout.output.PhysicalPageKey;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.graphics.PageDrawable;

/**
 * Creation-Date: 10.11.2006, 20:41:29
 *
 * @author Thomas Morgner
 */
public class QueryPhysicalPageInterceptor implements GraphicsContentInterceptor {
  private PageDrawable drawable;
  private PhysicalPageKey pageKey;

  public QueryPhysicalPageInterceptor( final PhysicalPageKey pageKey ) {
    if ( pageKey == null ) {
      throw new NullPointerException();
    }

    this.pageKey = pageKey;
  }

  public boolean isLogicalPageAccepted( final LogicalPageKey key ) {
    return false;
  }

  public void processLogicalPage( final LogicalPageKey key, final PageDrawable page ) {
  }

  public boolean isPhysicalPageAccepted( final PhysicalPageKey key ) {
    return pageKey.equals( key );
  }

  public void processPhysicalPage( final PhysicalPageKey key, final PageDrawable page ) {
    this.drawable = page;
  }

  public boolean isMoreContentNeeded() {
    return drawable == null;
  }

  public PageDrawable getDrawable() {
    return drawable;
  }
}
