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

package org.pentaho.reporting.engine.classic.core.filter.types.bands;

import org.pentaho.reporting.engine.classic.core.CrosstabColumnGroup;
import org.pentaho.reporting.engine.classic.core.ReportElement;

public class CrosstabColumnGroupType extends AbstractSectionType {
  public static final CrosstabColumnGroupType INSTANCE = new CrosstabColumnGroupType();

  public CrosstabColumnGroupType() {
    super( "crosstab-column-group", true );
  }

  public ReportElement create() {
    return new CrosstabColumnGroup();
  }
}
