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

package org.pentaho.reporting.engine.classic.core.layout.process.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.layout.model.FinishedRenderNode;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderBox;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderNode;
import org.pentaho.reporting.engine.classic.core.util.RingBuffer;

public class WidowBlockContext implements WidowContext {
  private static final Log logger = LogFactory.getLog( WidowBlockContext.class );
  private StackedObjectPool<WidowBlockContext> pool;
  private WidowContext parent;
  private RenderBox contextBox;
  private int widows;
  private int widowCount;
  private RingBuffer<RenderNode> widowSize;
  private long widowOverride;
  private long widowOverrideWithKeepTogether;
  private RenderNode currentNode;
  private boolean breakMarkerSeen;

  public WidowBlockContext() {
  }

  public void init( final StackedObjectPool<WidowBlockContext> pool, final WidowContext parent,
      final RenderBox contextBox, final int widows ) {
    this.breakMarkerSeen = false;
    this.pool = pool;
    this.parent = parent;
    this.contextBox = contextBox;
    this.widows = widows;
    this.widowCount = 0;
    this.widowOverride = contextBox.getCachedY2();
    this.widowOverrideWithKeepTogether = contextBox.getCachedY2();

    if ( widows > 0 ) {
      if ( this.widowSize == null ) {
        this.widowSize = new RingBuffer<RenderNode>( widows );
      } else {
        this.widowSize.resize( widows );
      }
    }
  }

  public void startChild( final RenderBox box ) {
    currentNode = box;

    if ( parent != null ) {
      parent.startChild( box );
    }
  }

  public void endChild( final RenderBox box ) {
    if ( currentNode != null ) {
      if ( widowCount < widows && widows > 0 ) {
        widowSize.add( box );
        box.setRestrictFinishedClearOut( RenderBox.RestrictFinishClearOut.LEAF );
      }
      widowCount += 1;
      currentNode = null;
    }

    if ( parent != null ) {
      parent.endChild( box );
    }
  }

  public void registerFinishedNode( final FinishedRenderNode box ) {
    if ( widowCount < widows && widows > 0 ) {
      widowSize.add( box );
      box.getParent().setRestrictFinishedClearOut( RenderBox.RestrictFinishClearOut.RESTRICTED );
    }
    widowCount += box.getWidowLeafCount();

    currentNode = null;
    if ( parent != null ) {
      parent.registerFinishedNode( box );
    }
  }

  public void registerBreakMark( final RenderBox box ) {
    breakMarkerSeen = true;
    if ( parent != null ) {
      parent.registerBreakMark( box );
    }
  }

  private long getWidowValue() {
    if ( widows == 0 ) {
      return widowOverride;
    }
    final RenderNode box = widowSize.getLastValue();
    if ( box == null ) {
      return widowOverride;
    }
    final long y2 = box.getCachedY2() - box.getCachedHeight();
    return Math.min( widowOverride, y2 );
  }

  public WidowContext commit( final RenderBox box ) {
    final boolean keepTogether = box.getStaticBoxLayoutProperties().isAvoidPagebreakInside();
    final long constraintSize;
    final long widowValue = getWidowValue();
    if ( keepTogether ) {
      constraintSize = box.getCachedY2() - box.getCachedY();
    } else {
      constraintSize = box.getCachedY2() - widowValue;
    }
    box.setWidowConstraintSizeWithKeepTogether( constraintSize );
    box.setWidowConstraintSize( box.getCachedY2() - widowValue );
    box.setWidowLeafCount( widowCount );

    if ( breakMarkerSeen == false && box.isInvalidWidowOrphanNode() == false ) {
      final boolean incomplete = box.isOpen() || box.getContentRefCount() > 0;
      if ( incomplete ) {
        if ( widows > 0 && widowCount == 0 ) {
          // the box is open, has a widow-constraint and has not seen a single widow box yet.
          box.setInvalidWidowOrphanNode( true );
        } else {
          box.setInvalidWidowOrphanNode( false );
        }
      } else {
        // the box is safe to process
        box.setInvalidWidowOrphanNode( false );
      }
    }

    if ( widowSize != null ) {
      for ( int i = 0; i < widowSize.size(); i += 1 ) {
        final RenderNode renderNode = widowSize.get( i );
        if ( renderNode == null ) {
          continue;
        }

        if ( renderNode instanceof RenderBox ) {
          final RenderBox rbox = (RenderBox) renderNode;
          rbox.setWidowBox( true );
        }
      }
    }

    if ( parent != null ) {
      parent.subContextCommitted( box );
    }

    return parent;
  }

  public void subContextCommitted( final RenderBox contextBox ) {
    final long cachedY2 = contextBox.getCachedY2();
    if ( cachedY2 > getWidowValue() || ( cachedY2 == this.contextBox.getCachedY2() && cachedY2 == getWidowValue() ) ) {
      widowOverride = Math.min( widowOverride, cachedY2 - contextBox.getWidowConstraintSize() );
      widowOverrideWithKeepTogether =
          Math.min( widowOverrideWithKeepTogether, cachedY2 - contextBox.getWidowConstraintSizeWithKeepTogether() );
    }

    if ( parent != null ) {
      parent.subContextCommitted( contextBox );
    }
  }

  public void clearForPooledReuse() {
    parent = null;
    contextBox = null;
    pool.free( this );
  }
}
