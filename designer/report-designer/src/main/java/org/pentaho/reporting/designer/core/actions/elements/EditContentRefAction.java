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
import org.pentaho.reporting.designer.core.util.table.ResourcePropertyEditor;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.designtime.ReportModelEventFilter;
import org.pentaho.reporting.engine.classic.core.designtime.ReportModelEventFilterFactory;
import org.pentaho.reporting.engine.classic.core.event.ReportModelEvent;
import org.pentaho.reporting.engine.classic.core.metadata.AttributeMetaData;
import org.pentaho.reporting.libraries.designtime.swing.propertyeditors.CustomPropertyEditorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditContentRefAction extends AbstractElementSelectionAction {
  private ReportModelEventFilter eventFilter;

  public EditContentRefAction() {
    putValue( Action.NAME, ActionMessages.getString( "EditContentRefAction.Text" ) );
    putValue( Action.SHORT_DESCRIPTION, ActionMessages.getString( "EditContentRefAction.Description" ) );
    putValue( Action.MNEMONIC_KEY, ActionMessages.getOptionalMnemonic( "EditContentRefAction.Mnemonic" ) );
    putValue( Action.ACCELERATOR_KEY, ActionMessages.getOptionalKeyStroke( "EditContentRefAction.Accelerator" ) );

    eventFilter = new ReportModelEventFilterFactory().createAttributeFilter
      ( AttributeNames.Core.NAMESPACE, AttributeNames.Core.ELEMENT_TYPE );
  }

  protected void selectedElementPropertiesChanged( final ReportModelEvent event ) {
    if ( eventFilter.isFilteredEvent( event ) ) {
      updateSelection();
    }
  }

  protected void updateSelection() {
    if ( isSingleElementSelection() == false ) {
      setEnabled( false );
      return;
    }
    final Object o = getSelectionModel().getLeadSelection();
    if ( o instanceof Element == false ) {
      setEnabled( false );
      return;
    }
    final Element e = (Element) o;
    final AttributeMetaData data = e.getElementType().getMetaData().getAttributeDescription
      ( AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE );
    if ( data == null ) {
      setEnabled( false );
      return;
    }
    setEnabled( "Resource".equals( data.getValueRole() ) ); // NON-NLS
  }

  public void actionPerformed( final ActionEvent e ) {
    if ( isSingleElementSelection() == false ) {
      return;
    }
    final Object o = getSelectionModel().getLeadSelection();
    if ( o instanceof Element == false ) {
      return;
    }
    final Element element = (Element) o;
    final AttributeMetaData data = element.getElementType().getMetaData().getAttributeDescription
      ( AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE );
    if ( data == null ) {
      return;
    }
    if ( "Resource".equals( data.getValueRole() ) == false ) // NON-NLS
    {
      return;
    }

    final CustomPropertyEditorDialog editorDialog;
    final Component window = getReportDesignerContext().getView().getParent();

    if ( window instanceof Frame ) {
      editorDialog = new CustomPropertyEditorDialog( (Frame) window );
    } else if ( window instanceof Dialog ) {
      editorDialog = new CustomPropertyEditorDialog( (Dialog) window );
    } else {
      editorDialog = new CustomPropertyEditorDialog();
    }
    editorDialog.setTitle( ActionMessages.getString( "EditContentRefAction.Text" ) );
    final ResourcePropertyEditor propertyEditor = new ResourcePropertyEditor( getActiveContext() );
    propertyEditor.setValue
      ( element.getAttribute( AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE ) );
    if ( editorDialog.performEdit( propertyEditor ) ) {
      element.setAttribute
        ( AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE, propertyEditor.getValue() );
    }

  }
}
