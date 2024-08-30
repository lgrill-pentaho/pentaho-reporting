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

package org.pentaho.reporting.engine.classic.core.states.process;

import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.event.ReportEvent;

public class EndCrosstabHandler implements AdvanceHandler {
  public static final EndCrosstabHandler HANDLER = new EndCrosstabHandler();

  private EndCrosstabHandler() {
  }

  public ProcessState advance( final ProcessState state ) throws ReportProcessingException {
    final ProcessState next = state.deriveForAdvance();
    next.fireReportEvent();
    return next;
  }

  public ProcessState commit( final ProcessState state ) throws ReportProcessingException {
    state.clearStoredCrosstabRowState();
    state.setAdvanceHandler( JoinEndCrosstabHandler.HANDLER );
    return state;
  }

  public boolean isFinish() {
    return false;
  }

  public int getEventCode() {
    return ReportEvent.CROSSTABBING | ReportEvent.GROUP_FINISHED;
  }

  public boolean isRestoreHandler() {
    return false;
  }
}
