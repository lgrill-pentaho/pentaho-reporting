/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
 */

package org.pentaho.reporting.engine.classic.core.layout.build;

import org.pentaho.reporting.engine.classic.core.layout.model.LayoutNodeTypes;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderBox;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderNode;
import org.pentaho.reporting.engine.classic.core.layout.output.OutputProcessorFeature;
import org.pentaho.reporting.engine.classic.core.layout.output.OutputProcessorMetaData;
import org.pentaho.reporting.engine.classic.core.states.ReportStateKey;

/**
 * A layout context that defers adding the current box to the parent until the context is closed. If this state is used
 * outside of a atomic layout modification, it produces interesting errors.
 */
public class BandSectionLayoutModelBuilderContext implements LayoutModelBuilderContext, Cloneable {
  private LayoutModelBuilderContext parent;
  private RenderBox renderBox;
  private boolean empty;
  private boolean keepWrapperBoxAlive;
  private boolean autoGeneratedWrapperBox;
  private boolean strictCompatibilityMode;
  private boolean committedAsEmpty;

  public BandSectionLayoutModelBuilderContext( final OutputProcessorMetaData metaData,
      final LayoutModelBuilderContext parent, final RenderBox renderBox ) {
    if ( metaData == null ) {
      throw new NullPointerException();
    }
    if ( renderBox == null ) {
      throw new NullPointerException();
    }
    this.strictCompatibilityMode = metaData.isFeatureSupported( OutputProcessorFeature.STRICT_COMPATIBILITY );
    this.parent = parent;
    this.renderBox = renderBox;
    this.empty = true;

    if ( renderBox.getNodeType() == LayoutNodeTypes.TYPE_BOX_AUTOLAYOUT
        || ( strictCompatibilityMode && renderBox.getNodeType() == LayoutNodeTypes.TYPE_BOX_SECTION ) ) {
      if ( parent != null && renderBox.getParent() == null ) {
        committedAsEmpty = true;
        parent.addChild( renderBox );
      }
    }
  }

  public RenderBox getRenderBox() {
    return renderBox;
  }

  public LayoutModelBuilderContext getParent() {
    return parent;
  }

  public boolean isEmpty() {
    return empty;
  }

  public boolean mergeSection( final ReportStateKey stateKey ) {
    return false;
  }

  public void setEmpty( final boolean empty ) {
    this.empty = empty;
  }

  public boolean isKeepWrapperBoxAlive() {
    return keepWrapperBoxAlive;
  }

  /**
   * A post-fix box stays open after the origin-box is closed.
   *
   * @param keepWrapperBoxAlive
   */
  public void setKeepWrapperBoxAlive( final boolean keepWrapperBoxAlive ) {
    this.keepWrapperBoxAlive = keepWrapperBoxAlive;
  }

  public boolean isAutoGeneratedWrapperBox() {
    return autoGeneratedWrapperBox;
  }

  /**
   * A prefix box is closed immediately after the origin-box is closed. Prefix boxes are not merged with silbling boxes.
   *
   * @param autoGeneratedWrapperBox
   */
  public void setAutoGeneratedWrapperBox( final boolean autoGeneratedWrapperBox ) {
    this.autoGeneratedWrapperBox = autoGeneratedWrapperBox;
  }

  public void addChild( final RenderBox child ) {
    this.renderBox.addChild( child );
  }

  public void removeChild( final RenderBox child ) {
    this.renderBox.remove( child );
  }

  public Object clone() {
    try {
      return super.clone();
    } catch ( CloneNotSupportedException e ) {
      throw new IllegalStateException( e );
    }
  }

  public LayoutModelBuilderContext deriveForPagebreak() {
    final BandSectionLayoutModelBuilderContext clone = (BandSectionLayoutModelBuilderContext) clone();
    if ( parent != null ) {
      clone.parent = parent.deriveForPagebreak();
    }
    return clone;
  }

  public LayoutModelBuilderContext deriveForStorage( final RenderBox clonedRoot ) {
    if ( clonedRoot == null ) {
      throw new NullPointerException();
    }
    final BandSectionLayoutModelBuilderContext clone = (BandSectionLayoutModelBuilderContext) clone();
    if ( parent == null ) {
      clone.renderBox = clonedRoot;
    } else {
      clone.parent = parent.deriveForStorage( clonedRoot );
      clone.renderBox = (RenderBox) renderBox.derive( true );
    }
    return clone;
  }

  public void validateAfterCommit() {

  }

  public void performParanoidModelCheck() {

  }

  public void restoreStateAfterRollback() {

  }

  public void commitAsEmpty() {

  }

  public void undoCommit() {
    if ( !committedAsEmpty ) {
      return;
    }

    getParent().removeChild( getRenderBox() );
  }

  public LayoutModelBuilderContext close() {
    final LayoutModelBuilderContext parentContext = getParent();
    final RenderBox sectionBox = getRenderBox();
    final RenderNode firstChild = sectionBox.getFirstChild();
    if ( firstChild == null ) {
      undoCommit();
      return parentContext;
    }

    final int type = firstChild.getNodeType();
    if ( parentContext != null
        && sectionBox.getLastChild() == firstChild
        && ( type == LayoutNodeTypes.TYPE_BOX_INLINE_PROGRESS_MARKER || type == LayoutNodeTypes.TYPE_BOX_PROGRESS_MARKER ) ) {
      if ( parentContext.mergeSection( firstChild.getStateKey() ) ) {
        undoCommit();
        return parentContext;
      }
    }

    this.renderBox.close();

    if ( isEmpty() == false ) {
      if ( parentContext != null && sectionBox.getParent() == null ) {
        if ( committedAsEmpty == false ) {
          parentContext.addChild( sectionBox );
        }
        parentContext.setEmpty( false );
      }
    } else if ( committedAsEmpty && parentContext != null ) {
      if ( parentContext.mergeSection( firstChild.getStateKey() ) ) {
        undoCommit();
      }
    }
    return parentContext;
  }

  public int getDepth() {
    if ( parent == null ) {
      return 1;
    }
    return 1 + parent.getDepth();
  }
}
