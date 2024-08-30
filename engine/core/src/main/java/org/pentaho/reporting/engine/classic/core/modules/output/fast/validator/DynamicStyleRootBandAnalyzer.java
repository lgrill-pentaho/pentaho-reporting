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

package org.pentaho.reporting.engine.classic.core.modules.output.fast.validator;

import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.Section;
import org.pentaho.reporting.engine.classic.core.function.Expression;
import org.pentaho.reporting.engine.classic.core.metadata.ExpressionMetaData;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.StyleKey;
import org.pentaho.reporting.engine.classic.core.util.AbstractStructureVisitor;
import org.pentaho.reporting.engine.classic.core.util.InstanceID;
import org.pentaho.reporting.libraries.base.util.HashNMap;

import java.util.HashMap;
import java.util.Iterator;

public class DynamicStyleRootBandAnalyzer extends AbstractStructureVisitor {
  private HashNMap<InstanceID, StyleKey> dynamicTemplateInfo;
  private HashNMap<String, StyleKey> styleByElementName;

  public DynamicStyleRootBandAnalyzer( final HashNMap<String, StyleKey> styleByElementName,
                                       final HashNMap<InstanceID, StyleKey> styleById ) {
    this.styleByElementName = styleByElementName;
    this.dynamicTemplateInfo = new HashNMap<InstanceID, StyleKey>();

    for ( final InstanceID id : styleById.keySet() ) {
      Iterator<StyleKey> it = styleById.getAll( id );
      while ( it.hasNext() ) {
        this.dynamicTemplateInfo.put( id, it.next() );
      }
    }
  }

  public void compute( final Section rootLevelBand ) {
    this.dynamicTemplateInfo.clear();
    inspectElement( rootLevelBand );
    traverseSection( rootLevelBand );

    HashMap<InstanceID, StyleKey[]> stash = buildStash();
    rootLevelBand.setAttribute( AttributeNames.Internal.NAMESPACE, AttributeNames.Internal.FAST_EXPORT_DYNAMIC_STASH,
        stash );
  }

  private HashMap<InstanceID, StyleKey[]> buildStash() {
    HashMap<InstanceID, StyleKey[]> stash = new HashMap<InstanceID, StyleKey[]>();

    for ( final InstanceID id : this.dynamicTemplateInfo.keySet() ) {
      int valueCount = this.dynamicTemplateInfo.getValueCount( id );
      StyleKey[] styleKeys = this.dynamicTemplateInfo.toArray( id, new StyleKey[valueCount] );
      stash.put( id, styleKeys );
    }
    return stash;
  }

  protected void traverseSection( final Section section ) {
    traverseSectionWithoutSubReports( section );
  }

  protected void inspectElement( final ReportElement element ) {
    dynamicTemplateInfo.add( element.getObjectID(), ElementStyleKeys.VISIBLE );
    String name = element.getName();
    if ( styleByElementName.containsKey( name ) ) {
      Iterator<StyleKey> it = styleByElementName.getAll( name );
      while ( it.hasNext() ) {
        this.dynamicTemplateInfo.put( element.getObjectID(), it.next() );
      }
    }
    traverseStyleExpressions( element );
  }

  protected void inspectStyleExpression( final ReportElement element, final StyleKey styleKey,
      final Expression expression, final ExpressionMetaData expressionMetaData ) {
    dynamicTemplateInfo.add( element.getObjectID(), styleKey );
  }
}
