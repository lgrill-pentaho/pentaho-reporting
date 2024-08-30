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

package org.pentaho.reporting.designer.core.actions.elements.distribute;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class DistributeTopAction extends AbstractElementSelectionAction {
  private static class ElementPositionComparator implements Comparator<Element> {
    public int compare( final Element o1, final Element o2 ) {
      final CachedLayoutData data1 = ModelUtility.getCachedLayoutData( o1 );
      final long x1 = data1.getY();
      final CachedLayoutData data2 = ModelUtility.getCachedLayoutData( o2 );
      final long x2 = data2.getY();
      if ( x1 < x2 ) {
        return -1;
      }
      if ( x1 > x2 ) {
        return +1;
      }
      return 0;
    }
  }

  public DistributeTopAction() {
    putValue( Action.NAME, ActionMessages.getString( "DistributeTopAction.Text" ) );
    putValue( Action.SHORT_DESCRIPTION, ActionMessages.getString( "DistributeTopAction.Description" ) );
    putValue( Action.MNEMONIC_KEY, ActionMessages.getOptionalMnemonic( "DistributeTopAction.Mnemonic" ) );
    putValue( Action.SMALL_ICON, IconLoader.getInstance().getDistributeTopIcon() );
    putValue( Action.ACCELERATOR_KEY, ActionMessages.getOptionalKeyStroke( "DistributeTopAction.Accelerator" ) );
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
    if ( visualElements.size() <= 2 ) {
      return;
    }

    final List<Element> reportElements = ModelUtility.filterParents( visualElements );
    if ( reportElements.size() <= 2 ) {
      return;
    }

    Collections.sort( reportElements, new ElementPositionComparator() );
    final MassElementStyleUndoEntryBuilder builder = new MassElementStyleUndoEntryBuilder( reportElements );
    final Element[] carrier = new Element[ 1 ];

    final int lastElementIdx = reportElements.size() - 1;
    final Element lastElement = reportElements.get( lastElementIdx );
    final Element firstElement = reportElements.get( 0 );

    final CachedLayoutData firstLayoutData = ModelUtility.getCachedLayoutData( firstElement );
    final CachedLayoutData lastLayoutData = ModelUtility.getCachedLayoutData( lastElement );

    final long firstElementY = firstLayoutData.getY();
    final long height = ( lastLayoutData.getY() ) - ( firstElementY );

    final long incr = height / lastElementIdx;
    long currentY = firstElementY;
    currentY += incr;//start from second element

    for ( Element reportElement : reportElements ) {
      final CachedLayoutData layoutData = ModelUtility.getCachedLayoutData( reportElement );

      carrier[ 0 ] = reportElement;
      final Point2D.Double originPoint =
        new Point2D.Double( 0, StrictGeomUtility.toExternalValue( layoutData.getY() ) );
      final MoveDragOperation mop =
        new MoveDragOperation( Arrays.asList( carrier ), originPoint, EmptySnapModel.INSTANCE,
          EmptySnapModel.INSTANCE );
      mop.update( new Point2D.Double( 0, StrictGeomUtility.toExternalValue( currentY ) ), 1 );
      mop.finish();

      currentY += incr;
    }
    final MassElementStyleUndoEntry massElementStyleUndoEntry = builder.finish();
    getActiveContext().getUndo()
      .addChange( ActionMessages.getString( "DistributeTopAction.UndoName" ), massElementStyleUndoEntry );
  }
}
