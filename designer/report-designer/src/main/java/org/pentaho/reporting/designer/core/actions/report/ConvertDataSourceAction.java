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

package org.pentaho.reporting.designer.core.actions.report;

import org.pentaho.reporting.designer.core.actions.AbstractElementSelectionAction;
import org.pentaho.reporting.designer.core.actions.ActionMessages;
import org.pentaho.reporting.designer.core.editor.ReportDocumentContext;
import org.pentaho.reporting.designer.core.editor.structuretree.ReportQueryNode;
import org.pentaho.reporting.designer.core.model.selection.DocumentContextSelectionModel;
import org.pentaho.reporting.designer.core.util.exceptions.UncaughtExceptionsModel;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.StaticDataRow;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.designtime.DataFactoryChange;
import org.pentaho.reporting.engine.classic.core.designtime.datafactory.DesignTimeDataFactoryContext;
import org.pentaho.reporting.engine.classic.core.event.ReportModelEvent;
import org.pentaho.reporting.engine.classic.core.metadata.DataFactoryMetaData;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.engine.classic.core.util.beans.BeanException;
import org.pentaho.reporting.libraries.designtime.swing.background.BackgroundCancellableProcessHelper;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;

public class ConvertDataSourceAction extends AbstractElementSelectionAction {
  public static class ConvertDataSourceTask implements Runnable {
    private Object[] selectedElements;
    private ReportDocumentContext activeContext;

    public ConvertDataSourceTask( final ReportDocumentContext activeContext ) {
      this.activeContext = activeContext;
      this.selectedElements = activeContext.getSelectionModel().getSelectedElements();
    }

    public void run() {
      for ( int i = 0; i < selectedElements.length; i++ ) {
        final Object element = selectedElements[ i ];
        if ( element instanceof ReportQueryNode ) {
          try {
            final ReportQueryNode queryNode = (ReportQueryNode) element;
            final DataFactory dataFactory = queryNode.getDataFactory().derive();
            final MasterReport report = activeContext.getContextRoot();
            dataFactory.initialize( new DesignTimeDataFactoryContext( report ) );
            if ( dataFactory.isQueryExecutable( queryNode.getQueryName(), new StaticDataRow() ) == false ) {
              return;
            }

            final TableModel tableModel = dataFactory.queryData( queryNode.getQueryName(), new StaticDataRow() );

            final TableDataFactory tableDataFactory = new TableDataFactory();
            tableDataFactory.addTable( queryNode.getQueryName(), createModel( tableModel ) );
            AddDataFactoryAction.addDataFactory( activeContext, tableDataFactory, new DataFactoryChange[ 0 ] );
          } catch ( Exception e1 ) {
            UncaughtExceptionsModel.getInstance().addException( e1 );
          }
          break;
        }
      }
    }


    public TableModel createModel( final TableModel model ) throws BeanException {
      final TypedTableModel tableModel = new TypedTableModel();
      final int columnCount = model.getColumnCount();
      for ( int col = 0; col < columnCount; col++ ) {
        tableModel.addColumn( model.getColumnName( col ), model.getColumnClass( col ) );
      }

      final int rowCount = model.getRowCount();
      for ( int r = 0; r < rowCount; r++ ) {
        for ( int col = 0; col < columnCount; col++ ) {
          tableModel.setValueAt( process( model.getValueAt( r, col ) ), r, col );
        }
      }

      return tableModel;
    }

    protected Object process( final Object o ) throws BeanException {
      return o;
    }
  }

  public ConvertDataSourceAction() {
    putValue( Action.NAME, ActionMessages.getString( "ConvertDataSourceAction.Text" ) );
    putValue( Action.DEFAULT, ActionMessages.getString( "ConvertDataSourceAction.Description" ) );
    putValue( Action.MNEMONIC_KEY, ActionMessages.getOptionalMnemonic( "ConvertDataSourceAction.Mnemonic" ) );
    putValue( Action.ACCELERATOR_KEY, ActionMessages.getOptionalKeyStroke( "ConvertDataSourceAction.Accelerator" ) );
  }

  protected void selectedElementPropertiesChanged( final ReportModelEvent event ) {
  }

  protected void updateSelection() {
    final DocumentContextSelectionModel model = getSelectionModel();
    if ( model == null ) {
      setEnabled( false );
      return;
    }

    final Object[] selectedObjects = model.getSelectedElements();
    for ( int i = 0; i < selectedObjects.length; i++ ) {
      final Object selectedObject = selectedObjects[ i ];
      if ( selectedObject instanceof ReportQueryNode == false ) {
        continue;
      }
      final ReportQueryNode queryNode = (ReportQueryNode) selectedObject;
      final DataFactory dataFactory = queryNode.getDataFactory();
      final DataFactoryMetaData metadata = dataFactory.getMetaData();
      if ( metadata.isEditable() ) {
        setEnabled( true );
        return;
      }

    }

    setEnabled( false );
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed( final ActionEvent e ) {
    final ReportDocumentContext activeContext = getActiveContext();
    if ( activeContext == null ) {
      return;
    }

    final Thread thread = new Thread( new ConvertDataSourceTask( activeContext ) );
    thread.setName( "ConvertDataSource-Worker" );
    thread.setDaemon( true );
    BackgroundCancellableProcessHelper.executeProcessWithCancelDialog( thread, null,
      getReportDesignerContext().getView().getParent(),
      ActionMessages.getString( "ConvertDataSourceAction.TaskTitle" ) );
  }
}
