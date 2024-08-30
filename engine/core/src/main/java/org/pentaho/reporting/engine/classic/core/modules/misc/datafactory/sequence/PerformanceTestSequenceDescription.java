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

package org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sequence;

public class PerformanceTestSequenceDescription extends AbstractSequenceDescription {
  public PerformanceTestSequenceDescription() {
    super( "org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sequence.PerformanceTestSequenceBundle",
        PerformanceTestSequence.class );
  }
}
