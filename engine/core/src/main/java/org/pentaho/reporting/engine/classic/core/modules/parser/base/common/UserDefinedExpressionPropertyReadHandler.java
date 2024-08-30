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

package org.pentaho.reporting.engine.classic.core.modules.parser.base.common;

import org.pentaho.reporting.engine.classic.core.util.beans.BeanUtility;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;

public interface UserDefinedExpressionPropertyReadHandler extends XmlReadHandler {
  public void init( final BeanUtility expression, final String originalExpressionClass,
                    final String expressionClass, final String expressionName );
}
