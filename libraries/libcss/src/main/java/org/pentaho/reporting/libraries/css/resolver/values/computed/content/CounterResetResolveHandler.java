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

package org.pentaho.reporting.libraries.css.resolver.values.computed.content;

import org.pentaho.reporting.libraries.css.dom.DocumentContext;
import org.pentaho.reporting.libraries.css.dom.LayoutElement;
import org.pentaho.reporting.libraries.css.keys.box.BoxStyleKeys;
import org.pentaho.reporting.libraries.css.model.StyleKey;
import org.pentaho.reporting.libraries.css.resolver.values.ResolveHandler;
import org.pentaho.reporting.libraries.css.values.CSSAttrFunction;
import org.pentaho.reporting.libraries.css.values.CSSNumericValue;
import org.pentaho.reporting.libraries.css.values.CSSValue;

public class CounterResetResolveHandler implements ResolveHandler {
  public CounterResetResolveHandler() {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent is the resolver on other
   * resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles() {
    return new StyleKey[] {
      BoxStyleKeys.DISPLAY_ROLE
    };
  }

  /**
   * Resolves a single property.
   */
  public void resolve( final DocumentContext process,
                       final LayoutElement element,
                       final StyleKey key ) {
    // todo: We have no counter-management yet.
    //    final LayoutStyle layoutContext = element.getLayoutStyle();
    //    final CSSValue displayRole = layoutContext.getValue(BoxStyleKeys.DISPLAY_ROLE);
    //    if (DisplayRole.NONE.equals(displayRole))
    //    {
    //      // [GENERATED] 8.3. Counters in elements with 'display: none'
    //      //
    //      // An element that is not displayed ('display' set to 'none') cannot
    //      // increment or reset a counter.
    //      return;
    //    }
    //
    //    final CSSValue value = layoutContext.getValue(key);
    //    if (value instanceof CSSValueList == false)
    //    {
    //      return; // do nothing.
    //    }
    //
    //    final CSSValueList valueList = (CSSValueList) value;
    //    for (int i = 0; i < valueList.getLength(); i++)
    //    {
    //      final CSSValue item = valueList.getItem(i);
    //      if (item instanceof CSSValuePair == false)
    //      {
    //        continue;
    //      }
    //      final CSSValuePair counter = (CSSValuePair) item;
    //      final CSSValue counterName = counter.getFirstValue();
    //      if (counterName instanceof CSSConstant == false)
    //      {
    //        continue;
    //      }
    //
    //      final CSSValue counterValue = counter.getSecondValue();
    //      final int counterIntValue = parseCounterValue(counterValue, element);
    //      element.resetCounter(counterName.getCSSText(), counterIntValue);
    //    }
  }

  private int parseCounterValue( final CSSValue rawValue,
                                 final LayoutElement element ) {

    if ( rawValue instanceof CSSNumericValue ) {
      final CSSNumericValue nval = (CSSNumericValue) rawValue;
      return (int) nval.getValue();
    }
    if ( rawValue instanceof CSSAttrFunction ) {
      final CSSAttrFunction attrFunction = (CSSAttrFunction) rawValue;
      final String attrName = attrFunction.getName();
      final String attrNamespace = attrFunction.getNamespace();
      final Object rawAttribute = element.getAttribute( attrNamespace, attrName );
      if ( rawAttribute instanceof Number ) {
        final Number nAttr = (Number) rawAttribute;
        return nAttr.intValue();
      }
    }
    return 0;
  }
}
