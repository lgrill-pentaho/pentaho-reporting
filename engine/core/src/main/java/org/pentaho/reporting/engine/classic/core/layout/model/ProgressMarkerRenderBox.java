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

package org.pentaho.reporting.engine.classic.core.layout.model;

import org.pentaho.reporting.engine.classic.core.layout.model.context.StaticBoxLayoutProperties;
import org.pentaho.reporting.engine.classic.core.states.ReportStateKey;

public class ProgressMarkerRenderBox extends BlockRenderBox {
  public ProgressMarkerRenderBox() {
    getStaticBoxLayoutProperties().setPlaceholderBox( StaticBoxLayoutProperties.PlaceholderType.SECTION );
  }

  public int getNodeType() {
    return LayoutNodeTypes.TYPE_BOX_PROGRESS_MARKER;
  }

  public void setStateKey( final ReportStateKey stateKey ) {
    super.setStateKey( stateKey );
  }
}
