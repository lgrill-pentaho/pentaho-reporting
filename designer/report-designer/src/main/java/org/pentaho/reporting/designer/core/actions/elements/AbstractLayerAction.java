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
import org.pentaho.reporting.designer.core.util.exceptions.UncaughtExceptionsModel;
import org.pentaho.reporting.designer.core.util.undo.CompoundUndoEntry;
import org.pentaho.reporting.designer.core.util.undo.UndoEntry;
import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.RelationalGroup;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.event.ReportModelEvent;
import org.pentaho.reporting.engine.classic.core.function.Expression;
import org.pentaho.reporting.engine.classic.core.parameters.ParameterDefinitionEntry;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

public abstract class AbstractLayerAction extends AbstractElementSelectionAction {
  protected AbstractLayerAction() {
  }

  protected void selectedElementPropertiesChanged( final ReportModelEvent event ) {
  }

  protected void updateSelection() {
    setEnabled( isSingleElementSelection() );
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed( final ActionEvent e ) {
    final ReportDocumentContext activeContext = getActiveContext();
    if ( activeContext == null ) {
      return;
    }

    final Object[] selectedElements = activeContext.getSelectionModel().getSelectedElements();
    final AbstractReportDefinition report = activeContext.getReportDefinition();

    final ArrayList<UndoEntry> undos = new ArrayList<UndoEntry>();
    if ( collectChange( selectedElements, report, undos ) == false ) {
      // rollback ..
      for ( int i = undos.size() - 1; i >= 0; i-- ) {
        final UndoEntry undoEntry = undos.get( i );
        undoEntry.undo( activeContext );
      }
    } else {
      final UndoEntry[] undoEntries = undos.toArray( new UndoEntry[ undos.size() ] );
      activeContext.getUndo().addChange( ActionMessages.getString( "AbstractLayerAction.UndoName" ),
        new CompoundUndoEntry( undoEntries ) );
    }
    // re-select the elements (moving them causes them to be unselected)
    activeContext.getSelectionModel().setSelectedElements( selectedElements );
  }


  protected abstract boolean collectChange( final Object[] selectedElements,
                                            final AbstractReportDefinition report,
                                            final ArrayList<UndoEntry> undos );

  protected boolean move( final Object element, final AbstractReportDefinition report,
                          final ArrayList<UndoEntry> undos ) {
    try {
      if ( element instanceof Expression ) {
        final UndoEntry undoEntry = moveExpressions( report, element );
        if ( undoEntry == null ) {
          return false;
        }
        undos.add( undoEntry );
      } else if ( element instanceof ParameterDefinitionEntry ) {
        final UndoEntry undoEntry = moveParameters( report, element );
        if ( undoEntry == null ) {
          return false;
        }
        undos.add( undoEntry );
      } else if ( element instanceof DataFactory ) {
        final UndoEntry undoEntry = moveDataFactories( report, element );
        if ( undoEntry == null ) {
          return false;
        }
        undos.add( undoEntry );
      } else if ( element instanceof RelationalGroup ) {
        final UndoEntry undoEntry = moveGroup( (RelationalGroup) element );
        if ( undoEntry == null ) {
          return false;
        }
        undos.add( undoEntry );
      } else if ( element instanceof Element ) {
        final UndoEntry undoEntry = moveVisualElement( report, (Element) element );
        if ( undoEntry == null ) {
          return false;
        }
        undos.add( undoEntry );
      }
    } catch ( Exception ex ) {
      UncaughtExceptionsModel.getInstance().addException( ex );
      return false;
    }
    return true;
  }

  protected abstract UndoEntry moveExpressions( final AbstractReportDefinition report, final Object element );

  protected abstract UndoEntry moveVisualElement( final AbstractReportDefinition report, final Element element );

  protected abstract UndoEntry moveGroup( final RelationalGroup element ) throws CloneNotSupportedException;

  protected abstract UndoEntry moveParameters( final AbstractReportDefinition report, final Object element );

  protected abstract UndoEntry moveDataFactories( final AbstractReportDefinition report,
                                                  final Object element ) throws ReportDataFactoryException;

}
