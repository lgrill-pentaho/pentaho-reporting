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

package org.pentaho.reporting.designer.core.actions.elements.align;

import org.pentaho.reporting.designer.core.actions.AbstractElementSelectionAction;
import org.pentaho.reporting.designer.core.actions.ActionMessages;
import org.pentaho.reporting.designer.core.editor.report.drag.MoveDragOperation;
import org.pentaho.reporting.designer.core.editor.report.snapping.EmptySnapModel;
import org.pentaho.reporting.designer.core.model.CachedLayoutData;
import org.pentaho.reporting.designer.core.model.ModelUtility;
import org.pentaho.reporting.designer.core.model.selection.DocumentContextSelectionModel;
import org.pentaho.reporting.designer.core.util.IconLoader;
import org.pentaho.reporting.designer.core.util.undo.MassElementStyleUndoEntry;
import org.pentaho.reporting.designer.core.util.undo.MassElementStyleUndoEntryBuilder;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.event.ReportModelEvent;
import org.pentaho.reporting.engine.classic.core.util.geom.StrictGeomUtility;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

public final class AlignMiddleAction extends AbstractElementSelectionAction {
  private static final Point2D.Double ORIGIN_POINT = new Point2D.Double();

  public AlignMiddleAction() {
    putValue( Action.NAME, ActionMessages.getString( "AlignMiddleAction.Text" ) );
    putValue( Action.SHORT_DESCRIPTION, ActionMessages.getString( "AlignMiddleAction.Description" ) );
    putValue( Action.MNEMONIC_KEY, ActionMessages.getOptionalMnemonic( "AlignMiddleAction.Mnemonic" ) );
    putValue( Action.SMALL_ICON, IconLoader.getInstance().getAlignMiddleIcon() );
    putValue( Action.ACCELERATOR_KEY, ActionMessages.getOptionalKeyStroke( "AlignMiddleAction.Accelerator" ) );
  }

  protected void selectedElementPropertiesChanged( final ReportModelEvent event ) {
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
    if ( visualElements.size() <= 1 ) {
      return;
    }
    final Element[] carrier = new Element[ 1 ];
    final List<Element> objects = ModelUtility.filterParents( visualElements );
    final MassElementStyleUndoEntryBuilder builder = new MassElementStyleUndoEntryBuilder( objects );

    long minY = Long.MAX_VALUE;
    long maxY = Long.MIN_VALUE;
    for ( Element object : objects ) {
      final CachedLayoutData data = ModelUtility.getCachedLayoutData( object );
      final long y1 = data.getY();
      final long y2 = y1 + data.getHeight();
      if ( y2 > maxY ) {
        maxY = y2;
      }
      if ( y1 < minY ) {
        minY = y1;
      }
    }

    final long centerPoint = minY + ( maxY - minY ) / 2;

    for ( Element object : objects ) {
      final CachedLayoutData data = ModelUtility.getCachedLayoutData( object );
      final long elementCenter = data.getY() + data.getHeight() / 2;

      final long delta = centerPoint - elementCenter;
      if ( delta == 0 ) {
        continue;
      }

      carrier[ 0 ] = object;
      final MoveDragOperation mop = new MoveDragOperation
        ( Arrays.asList( carrier ), ORIGIN_POINT, EmptySnapModel.INSTANCE, EmptySnapModel.INSTANCE );
      mop.update( new Point2D.Double( 0, StrictGeomUtility.toExternalValue( delta ) ), 1 );
      mop.finish();
    }
    final MassElementStyleUndoEntry massElementStyleUndoEntry = builder.finish();
    getActiveContext().getUndo()
      .addChange( ActionMessages.getString( "AlignMiddleAction.UndoName" ), massElementStyleUndoEntry );
  }
}
