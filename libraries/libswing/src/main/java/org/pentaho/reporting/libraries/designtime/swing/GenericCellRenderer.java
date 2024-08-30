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

package org.pentaho.reporting.libraries.designtime.swing;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class GenericCellRenderer implements TableCellRenderer, ListCellRenderer {
  private DefaultTableCellRenderer tableCellRenderer;
  private DefaultListCellRenderer listCellRenderer;

  public GenericCellRenderer() {
    tableCellRenderer = new DefaultTableCellRenderer();
    tableCellRenderer.putClientProperty( "html.disable", Boolean.TRUE );

    listCellRenderer = new DefaultListCellRenderer();
    listCellRenderer.putClientProperty( "html.disable", Boolean.TRUE );
  }

  /**
   * Returns the component used for drawing the cell.  This method is used to configure the renderer appropriately
   * before drawing.
   *
   * @param table      the <code>JTable</code> that is asking the renderer to draw; can be <code>null</code>
   * @param value      the value of the cell to be rendered.  It is up to the specific renderer to interpret and draw
   *                   the value.  For example, if <code>value</code> is the string "true", it could be rendered as a
   *                   string or it could be rendered as a check box that is checked.  <code>null</code> is a valid
   *                   value
   * @param isSelected true if the cell is to be rendered with the selection highlighted; otherwise false
   * @param hasFocus   if true, render cell appropriately.  For example, put a special border on the cell, if the cell
   *                   can be edited, render in the color used to indicate editing
   * @param row        the row index of the cell being drawn.  When drawing the header, the value of <code>row</code> is
   *                   -1
   * @param column     the column index of the cell being drawn
   */
  public Component getTableCellRendererComponent( final JTable table,
                                                  final Object value,
                                                  final boolean isSelected,
                                                  final boolean hasFocus,
                                                  final int row,
                                                  final int column ) {
    return tableCellRenderer.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
  }

  /**
   * Return a component that has been configured to display the specified value. That component's <code>paint</code>
   * method is then called to "render" the cell.  If it is necessary to compute the dimensions of a list because the
   * list cells do not have a fixed size, this method is called to generate a component on which
   * <code>getPreferredSize</code> can be invoked.
   *
   * @param list         The JList we're painting.
   * @param value        The value returned by list.getModel().getElementAt(index).
   * @param index        The cells index.
   * @param isSelected   True if the specified cell was selected.
   * @param cellHasFocus True if the specified cell has the focus.
   * @return A component whose paint() method will render the specified value.
   * @see javax.swing.JList
   * @see javax.swing.ListSelectionModel
   * @see javax.swing.ListModel
   */
  public Component getListCellRendererComponent( final JList list,
                                                 final Object value,
                                                 final int index,
                                                 final boolean isSelected,
                                                 final boolean cellHasFocus ) {
    return listCellRenderer.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
  }
}
