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

package org.pentaho.reporting.engine.classic.core.style.css.selector.conditions;

import org.pentaho.reporting.engine.classic.core.style.css.namespaces.NamespaceCollection;
import org.w3c.css.sac.AttributeCondition;

public class PseudoClassCSSCondition implements AttributeCondition, CSSCondition {
  private String namespace;
  private String value;

  public PseudoClassCSSCondition( final String namespace, final String value ) {
    this.namespace = namespace;
    this.value = value;
  }

  /**
   * An integer indicating the type of <code>Condition</code>.
   */
  public short getConditionType() {
    return SAC_PSEUDO_CLASS_CONDITION;
  }

  /**
   * Returns the <a href="http://www.w3.org/TR/REC-xml-names/#dt-NSName">namespace URI</a> of this attribute condition.
   * <p>
   * <code>NULL</code> if :
   * <ul>
   * <li>this attribute condition can match any namespace.
   * <li>this attribute is an id attribute.
   * </ul>
   */
  public String getNamespaceURI() {
    return namespace;
  }

  /**
   * Returns <code>true</code> if the attribute must have an explicit value in the original document, <code>false</code>
   * otherwise.
   */
  public final boolean getSpecified() {
    return false;
  }

  public String getValue() {
    return value;
  }

  /**
   * Returns the <a href="http://www.w3.org/TR/REC-xml-names/#NT-LocalPart">local part</a> of the <a
   * href="http://www.w3.org/TR/REC-xml-names/#ns-qualnames">qualified name</a> of this attribute.
   * <p>
   * <code>NULL</code> if :
   * <ul>
   * <li>
   * <p>
   * this attribute condition can match any attribute.
   * <li>
   * <p>
   * this attribute is a class attribute.
   * <li>
   * <p>
   * this attribute is an id attribute.
   * <li>
   * <p>
   * this attribute is a pseudo-class attribute.
   * </ul>
   */
  public String getLocalName() {
    return null;
  }

  public String print( final NamespaceCollection namespaces ) {
    return ":" + value;
  }
}
