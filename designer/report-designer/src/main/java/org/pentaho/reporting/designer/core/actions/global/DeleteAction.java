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

package org.pentaho.reporting.designer.core.actions.global;

import org.pentaho.reporting.designer.core.actions.AbstractElementSelectionAction;
import org.pentaho.reporting.designer.core.actions.ActionMessages;
import org.pentaho.reporting.designer.core.editor.ReportDocumentContext;
import org.pentaho.reporting.designer.core.model.selection.DocumentContextSelectionModel;
import org.pentaho.reporting.designer.core.util.IconLoader;
import org.pentaho.reporting.designer.core.util.dnd.InsertationUtil;
import org.pentaho.reporting.designer.core.util.undo.CompoundUndoEntry;
import org.pentaho.reporting.designer.core.util.undo.UndoEntry;
import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.Group;
import org.pentaho.reporting.engine.classic.core.GroupBody;
import org.pentaho.reporting.engine.classic.core.ReportDefinition;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.Section;
import org.pentaho.reporting.engine.classic.core.event.ReportModelEvent;
import org.pentaho.reporting.engine.classic.core.parameters.ParameterDefinitionEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public final class DeleteAction extends AbstractElementSelectionAction {
  public DeleteAction() {
    putValue( Action.NAME, ActionMessages.getString( "DeleteAction.Text" ) );
    putValue( Action.SHORT_DESCRIPTION, ActionMessages.getString( "DeleteAction.Description" ) );
    putValue( Action.MNEMONIC_KEY, ActionMessages.getOptionalMnemonic( "DeleteAction.Mnemonic" ) );
    putValue( Action.SMALL_ICON, IconLoader.getInstance().getDeleteIconSmall() );
    putValue( Action.ACCELERATOR_KEY, ActionMessages.getOptionalKeyStroke( "DeleteAction.Accelerator" ) );
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed( final ActionEvent e ) {
    final ReportDocumentContext activeContext = getActiveContext();
    if ( activeContext == null ) {
      return;
    }

    final DocumentContextSelectionModel selectionModel = activeContext.getSelectionModel();
    final Object[] selectedElements = selectionModel.getSelectedElements();
    if ( isParameter() ) {
      final String theTitle = ActionMessages.getString( "DeleteAction.Warning" );
      final String theDeleteMessage = ActionMessages.getString( "DeleteAction.Confirmation" );
      final Component theParent = getReportDesignerContext().getView().getParent();
      final int result =
        JOptionPane.showConfirmDialog( theParent, theDeleteMessage, theTitle, JOptionPane.YES_NO_OPTION );
      if ( result != JOptionPane.YES_OPTION ) {
        return;
      }
    }

    final ArrayList<UndoEntry> undos = new ArrayList<UndoEntry>();
    for ( int i = 0; i < selectedElements.length; i++ ) {
      final Object element = selectedElements[ i ];
      final UndoEntry undoEntry = InsertationUtil.delete( activeContext, element );
      if ( undoEntry != null ) {
        undos.add( undoEntry );
      }
    }
    if ( undos.isEmpty() == false ) {
      activeContext.getUndo().addChange( ActionMessages.getString( "DeleteAction.Text" ),
        new CompoundUndoEntry( (UndoEntry[]) undos.toArray( new UndoEntry[ undos.size() ] ) ) );
    }
  }

  private boolean isParameter() {
    final DocumentContextSelectionModel selectionModel = getActiveContext().getSelectionModel();
    final Object[] selectedElements = selectionModel.getSelectedElements();
    for ( int i = 0; i < selectedElements.length; i++ ) {
      if ( selectedElements[ i ] instanceof ParameterDefinitionEntry ) {
        return true;
      }
    }
    return false;
  }

  protected void selectedElementPropertiesChanged( final ReportModelEvent event ) {
  }

  protected void updateSelection() {
    final DocumentContextSelectionModel selectionModel1 = getSelectionModel();
    if ( selectionModel1 == null || selectionModel1.getSelectionCount() == 0 ) {
      setEnabled( false );
      return;
    }
    final Object[] selectedElements = selectionModel1.getSelectedElements();
    for ( int i = 0; i < selectedElements.length; i++ ) {
      final Object selectedElement = selectedElements[ i ];
      if ( selectedElement instanceof AbstractReportDefinition ) {
        // you cannot delete the report itself
        if ( getActiveContext().getReportDefinition() == selectedElement ) {
          setEnabled( false );
          return;
        }
      } else if ( selectedElement instanceof ReportElement ) {
        final Section parent = ( (ReportElement) selectedElement ).getParentSection();
        if ( parent instanceof ReportDefinition ) {
          // prevent the deletion of the top level bands
          setEnabled( false );
          return;
        } else if ( parent instanceof Group || parent instanceof GroupBody ) {
          // prevent the deletion of the group headers/footers
          setEnabled( false );
          return;
        }

      }
    }
    setEnabled( true );
  }

}
