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

package org.pentaho.reporting.designer.core.actions.elements;

import org.pentaho.reporting.designer.core.actions.AbstractElementSelectionAction;
import org.pentaho.reporting.designer.core.actions.ActionMessages;
import org.pentaho.reporting.designer.core.editor.ReportDocumentContext;
import org.pentaho.reporting.designer.core.model.ModelUtility;
import org.pentaho.reporting.designer.core.settings.SettingsListener;
import org.pentaho.reporting.designer.core.settings.WorkspaceSettings;
import org.pentaho.reporting.designer.core.util.IconLoader;
import org.pentaho.reporting.designer.core.util.exceptions.UncaughtExceptionsModel;
import org.pentaho.reporting.designer.core.util.undo.UndoEntry;
import org.pentaho.reporting.engine.classic.core.CrosstabCell;
import org.pentaho.reporting.engine.classic.core.CrosstabCellBody;
import org.pentaho.reporting.engine.classic.core.event.ReportModelEvent;
import org.pentaho.reporting.engine.classic.core.filter.types.CrosstabElementType;
import org.pentaho.reporting.engine.classic.core.util.InstanceID;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Inserts a crosstab cell into a cell-body.
 *
 * @author Will Gorman (wgorman@pentaho.com)
 */
public final class InsertCrosstabCellAction extends AbstractElementSelectionAction implements SettingsListener {
  private static final long serialVersionUID = 8941387470673515186L;

  public InsertCrosstabCellAction() {
    putValue( Action.NAME, ActionMessages.getString( "InsertCrosstabCellAction.Text" ) );
    putValue( Action.SHORT_DESCRIPTION, ActionMessages.getString( "InsertCrosstabCellAction.Description" ) );
    putValue( Action.MNEMONIC_KEY, ActionMessages.getOptionalMnemonic( "InsertCrosstabCellAction.Mnemonic" ) );
    putValue( Action.ACCELERATOR_KEY, ActionMessages.getOptionalKeyStroke( "InsertCrosstabCellAction.Accelerator" ) );
    putValue( Action.SMALL_ICON, IconLoader.getInstance().getGenericSquare() );

    setVisible( WorkspaceSettings.getInstance().isVisible( CrosstabElementType.INSTANCE.getMetaData() ) );
    WorkspaceSettings.getInstance().addSettingsListener( this );
  }

  public void actionPerformed( final ActionEvent e ) {
    final ReportDocumentContext activeContext = getActiveContext();
    if ( activeContext == null ) {
      return;
    }

    try {
      Object selectedElement = null;
      if ( getSelectionModel().getSelectionCount() > 0 ) {
        selectedElement = getSelectionModel().getSelectedElement( 0 );
      }
      if ( selectedElement instanceof CrosstabCellBody ) {
        // execution order is important here.
        // first unlink the old root-group by setting a new one ...
        final CrosstabCellBody selectedGroup = (CrosstabCellBody) selectedElement;
        final CrosstabCell crosstabCell = new CrosstabCell();
        selectedGroup.addElement( crosstabCell );

        activeContext.getUndo().addChange( ActionMessages.getString( "InsertCrosstabCellAction.UndoName" ),
          new InsertCellBodyUndoEntry( selectedGroup.getObjectID(), crosstabCell ) );
      }
    } catch ( Exception ex ) {
      UncaughtExceptionsModel.getInstance().addException( ex );
    }
  }

  protected void selectedElementPropertiesChanged( final ReportModelEvent event ) {
  }

  protected void updateSelection() {
    if ( isVisible() == false ) {
      setEnabled( false );
      return;
    }

    if ( getSelectionModel() != null && getSelectionModel().getSelectionCount() == 0 ) {
      // there's nothing selected, we can safely add a new group
      // at the report level (AbstractReportDefinition)
      setEnabled( false );
      return;
    }
    if ( isSingleElementSelection() == false ) {
      // there's more than 1 element selected, disable because
      // we can't know where to insert in this case
      setEnabled( false );
      return;
    }

    final Object selectedElement = getSelectionModel().getSelectedElement( 0 );
    if ( selectedElement instanceof CrosstabCellBody ) {
      // if the selectedElement is the report-definition or a relational group
      // then we can safely insert to those
      setEnabled( true );
      return;
    }

    setEnabled( false );
  }

  private static class InsertCellBodyUndoEntry implements UndoEntry {
    private static final long serialVersionUID = 6615171451777587555L;

    private InstanceID target;
    private CrosstabCell cell;

    private InsertCellBodyUndoEntry( final InstanceID target, final CrosstabCell cell ) {
      this.target = target;
      this.cell = cell;
    }

    public void undo( final ReportDocumentContext renderContext ) {
      final CrosstabCellBody selectedGroup = (CrosstabCellBody)
        ModelUtility.findElementById( renderContext.getReportDefinition(), target );
      selectedGroup.removeElement( cell );
    }

    public void redo( final ReportDocumentContext renderContext ) {
      final CrosstabCellBody selectedGroup = (CrosstabCellBody)
        ModelUtility.findElementById( renderContext.getReportDefinition(), target );
      selectedGroup.addElement( cell );
    }

    public UndoEntry merge( final UndoEntry newEntry ) {
      return null;
    }
  }

  public void settingsChanged() {
    setVisible( WorkspaceSettings.getInstance().isVisible( CrosstabElementType.INSTANCE.getMetaData() ) );
  }
}
