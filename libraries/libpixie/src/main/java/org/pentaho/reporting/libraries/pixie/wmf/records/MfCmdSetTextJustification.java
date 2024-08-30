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

package org.pentaho.reporting.libraries.pixie.wmf.records;

import org.pentaho.reporting.libraries.pixie.wmf.MfDcState;
import org.pentaho.reporting.libraries.pixie.wmf.MfRecord;
import org.pentaho.reporting.libraries.pixie.wmf.MfType;
import org.pentaho.reporting.libraries.pixie.wmf.WmfFile;

/**
 * The SetTextJustification function specifies the amount of space the system should add to the break characters in a
 * string of text. The space is added when an application calls the TextOut or ExtTextOut functions.
 */
public class MfCmdSetTextJustification extends MfCmd {
  private static final int RECORD_SIZE = 2;
  private static final int POS_SPACELENGTH = 0;
  private static final int POS_BREAKCOUNT = 1;

  private int extraSpaceLength;
  private int breakCount;

  public MfCmdSetTextJustification() {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay( final WmfFile file ) {
    final MfDcState state = file.getCurrentState();
    state.setTextJustification( extraSpaceLength, breakCount );
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance() {
    return new MfCmdSetTextJustification();
  }

  public String toString() {
    final StringBuffer b = new StringBuffer();
    b.append( "[SET_TEXT_JUSTIFICATION] breakCount=" );
    b.append( getBreakCount() );
    b.append( " extraSpaceLength=" );
    b.append( getExtraSpaceLength() );
    return b.toString();
  }

  /**
   * Reads the command data from the given record and adjusts the internal parameters according to the data parsed.
   * <p/>
   * After the raw record was read from the datasource, the record is parsed by the concrete implementation.
   *
   * @param record the raw data that makes up the record.
   */
  public void setRecord( final MfRecord record ) {
    final int spaceLength = record.getParam( POS_SPACELENGTH );
    final int breakCount = record.getParam( POS_BREAKCOUNT );
    setExtraSpaceLength( spaceLength );
    setBreakCount( breakCount );
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord()
    throws RecordCreationException {
    final MfRecord record = new MfRecord( RECORD_SIZE );
    record.setParam( POS_BREAKCOUNT, getBreakCount() );
    record.setParam( POS_SPACELENGTH, getExtraSpaceLength() );
    return record;
  }

  /**
   * Reads the function identifier. Every record type is identified by a function number corresponding to one of the
   * Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction() {
    return MfType.SET_TEXT_JUSTIFICATION;
  }

  public int getBreakCount() {
    return breakCount;
  }

  public void setBreakCount( final int count ) {
    this.breakCount = count;
  }

  public int getExtraSpaceLength() {
    return extraSpaceLength;
  }

  public void setExtraSpaceLength( final int count ) {
    this.extraSpaceLength = count;
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the internal coordinate values have to
   * be adjusted.
   */
  protected void scaleXChanged() {
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the internal coordinate values have to
   * be adjusted.
   */
  protected void scaleYChanged() {
  }
}
