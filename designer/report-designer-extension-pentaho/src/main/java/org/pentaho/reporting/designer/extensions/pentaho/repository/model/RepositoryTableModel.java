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

package org.pentaho.reporting.designer.extensions.pentaho.repository.model;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.pentaho.reporting.designer.core.util.exceptions.UncaughtExceptionsModel;
import org.pentaho.reporting.designer.extensions.pentaho.repository.Messages;
import org.pentaho.reporting.designer.extensions.pentaho.repository.util.PublishUtil;

import javax.swing.table.AbstractTableModel;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

public class RepositoryTableModel extends AbstractTableModel {
  private FileObject selectedPath;
  private String[] filters;
  private boolean showHiddenFiles;

  public RepositoryTableModel() {
    this.filters = new String[0];
  }

  public boolean isShowHiddenFiles() {
    return showHiddenFiles;
  }

  public void setShowHiddenFiles( final boolean showHiddenFiles ) {
    this.showHiddenFiles = showHiddenFiles;
    fireTableDataChanged();
  }

  public String[] getFilters() {
    return filters.clone();
  }

  public void setFilters( final String[] filters ) {
    this.filters = filters.clone();
    fireTableDataChanged();
  }

  public FileObject getSelectedPath() {
    return selectedPath;
  }

  public void setSelectedPath( final FileObject selectedPath ) {
    this.selectedPath = selectedPath;
    fireTableDataChanged();
  }

  public int getColumnCount() {
    return 4;
  }

  public int getRowCount() {
    if ( selectedPath == null ) {
      return 0;
    }

    try {
      if ( selectedPath.getType() != FileType.FOLDER ) {
        return 0;
      }

      final FileObject[] children = selectedPath.getChildren();
      int count = 0;
      for ( int i = 0; i < children.length; i++ ) {
        final FileObject child = children[i];
        if ( isShowHiddenFiles() == false && child.isHidden() ) {
          continue;
        }
        if ( child.getType() != FileType.FOLDER ) {
          if ( PublishUtil.acceptFilter( filters, child.getName().getBaseName() ) == false ) {
            continue;
          }
        }

        count += 1;
      }
      return count;
    } catch ( FileSystemException fse ) {
      UncaughtExceptionsModel.getInstance().addException( fse );
      return 0;
    }
  }

  public String getColumnName( final int col ) {
    switch ( col ) {
      case 0:
        return Messages.getInstance().getString( "SolutionRepositoryTableView.Title" );
      case 1:
        return Messages.getInstance().getString( "SolutionRepositoryTableView.Name" );
      case 2:
        return Messages.getInstance().getString( "SolutionRepositoryTableView.DateModified" );
      case 3:
        return Messages.getInstance().getString( "SolutionRepositoryTableView.Description" );
    }
    throw new IndexOutOfBoundsException();
  }

  public FileObject getElementForRow( final int row ) {
    if ( selectedPath == null ) {
      return null;
    }

    try {
      if ( selectedPath.getType() != FileType.FOLDER ) {
        return null;
      }

      final FileObject[] children = selectedPath.getChildren();
      int count = 0;
      for ( int i = 0; i < children.length; i++ ) {
        final FileObject child = children[i];
        if ( isShowHiddenFiles() == false && child.isHidden() ) {
          continue;
        }
        if ( child.getType() != FileType.FOLDER ) {
          if ( PublishUtil.acceptFilter( filters, child.getName().getBaseName() ) == false ) {
            continue;
          }
        }

        if ( count == row ) {
          return child;
        }
        count += 1;
      }
      return null;
    } catch ( FileSystemException fse ) {
      UncaughtExceptionsModel.getInstance().addException( fse );
      return null;
    }
  }

  public Object getValueAt( final int row, final int column ) {
    final FileObject node1 = getElementForRow( row );

    try {
      switch ( column ) {
        case 0:
          return node1.getContent().getAttribute( "localized-name" );
        case 1:
          return URLDecoder.decode( node1.getName().getBaseName().replaceAll( "\\+", "%2B" ), "UTF-8" );
        case 2:
          final long lastModifiedTime = node1.getContent().getLastModifiedTime();
          if ( lastModifiedTime == -1 ) {
            return null;
          }
          return new Date( lastModifiedTime );
        case 3:
          return node1.getContent().getAttribute( "description" );
        default:
          throw new IndexOutOfBoundsException();
      }
    } catch ( FileSystemException fse ) {
      // ignre the exception, assume the file is not valid
      UncaughtExceptionsModel.getInstance().addException( fse );
      return null;
    } catch ( UnsupportedEncodingException e ) {
      UncaughtExceptionsModel.getInstance().addException( e );
      return null;
    }
  }

  public Class getColumnClass( final int column ) {
    if ( column == 2 ) {
      return Date.class;
    }
    return String.class;
  }
}
