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

package org.pentaho.reporting.designer.core.actions.elements.format;

import org.pentaho.reporting.designer.core.actions.AbstractElementSelectionAction;
import org.pentaho.reporting.designer.core.actions.ActionMessages;
import org.pentaho.reporting.designer.core.actions.ToggleStateAction;
import org.pentaho.reporting.designer.core.model.selection.DocumentContextSelectionModel;
import org.pentaho.reporting.designer.core.util.undo.CompoundUndoEntry;
import org.pentaho.reporting.designer.core.util.undo.StyleEditUndoEntry;
import org.pentaho.reporting.designer.core.util.undo.UndoEntry;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.designtime.ReportModelEventFilter;
import org.pentaho.reporting.engine.classic.core.designtime.ReportModelEventFilterFactory;
import org.pentaho.reporting.engine.classic.core.event.ReportModelEvent;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleSheet;
import org.pentaho.reporting.engine.classic.core.style.TextStyleKeys;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public final class StrikethroughAction extends AbstractElementSelectionAction implements ToggleStateAction {
  private ReportModelEventFilter eventFilter;

  public StrikethroughAction() {
    putValue( Action.NAME, ActionMessages.getString( "StrikethroughAction.Text" ) );
    putValue( Action.SHORT_DESCRIPTION, ActionMessages.getString( "StrikethroughAction.Description" ) );
    putValue( Action.MNEMONIC_KEY, ActionMessages.getOptionalMnemonic( "StrikethroughAction.Mnemonic" ) );
    putValue( Action.ACCELERATOR_KEY, ActionMessages.getOptionalKeyStroke( "StrikethroughAction.Accelerator" ) );
    putValue( Action.SELECTED_KEY, Boolean.FALSE );

    eventFilter = new ReportModelEventFilterFactory().createStyleFilter( TextStyleKeys.STRIKETHROUGH );
  }

  protected void selectedElementPropertiesChanged( final ReportModelEvent event ) {
    if ( eventFilter.isFilteredEvent( event ) ) {
      updateSelection();
    }
  }

  public boolean isSelected() {
    return Boolean.TRUE.equals( getValue( Action.SELECTED_KEY ) );
  }

  public void setSelected( final boolean selected ) {
    putValue( Action.SELECTED_KEY, selected );
  }

  protected void updateSelection() {
    super.updateSelection();

    final DocumentContextSelectionModel model = getSelectionModel();
    if ( model == null ) {
      return;
    }
    final List<Element> visualElements = model.getSelectedElementsOfType( Element.class );

    boolean selected;
    if ( visualElements.isEmpty() ) {
      selected = false;
    } else {
      selected = true;
      for ( Element visualElement : visualElements ) {
        selected &= visualElement.getStyle().getBooleanStyleProperty( TextStyleKeys.STRIKETHROUGH );
      }
    }
    setSelected( selected );

  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed( final ActionEvent e ) {
    final DocumentContextSelectionModel model = getSelectionModel();
    if ( model == null ) {
      return;
    }
    final List<Element> visualElements = model.getSelectedElementsOfType( Element.class );
    final ArrayList<UndoEntry> undos = new ArrayList<UndoEntry>();

    Boolean value = null;
    for ( Element element : visualElements ) {
      final ElementStyleSheet styleSheet = element.getStyle();
      if ( value == null ) {
        if ( styleSheet.getBooleanStyleProperty( TextStyleKeys.STRIKETHROUGH ) ) {
          value = Boolean.FALSE;
        } else {
          value = Boolean.TRUE;
        }
      }
      undos.add( StyleEditUndoEntry.createConditional( element, TextStyleKeys.STRIKETHROUGH, value ) );
      styleSheet.setStyleProperty( TextStyleKeys.STRIKETHROUGH, value );
      element.notifyNodePropertiesChanged();
    }
    getActiveContext().getUndo().addChange( ActionMessages.getString( "StrikethroughAction.UndoName" ),
      new CompoundUndoEntry( undos.toArray( new UndoEntry[ undos.size() ] ) ) );
  }
}
