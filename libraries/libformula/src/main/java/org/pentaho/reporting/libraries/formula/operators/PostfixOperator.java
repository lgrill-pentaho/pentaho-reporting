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

package org.pentaho.reporting.libraries.formula.operators;

import org.pentaho.reporting.libraries.formula.EvaluationException;
import org.pentaho.reporting.libraries.formula.FormulaContext;
import org.pentaho.reporting.libraries.formula.lvalues.TypeValuePair;

import java.io.Serializable;

/**
 * Creation-Date: 02.11.2006, 10:17:03
 *
 * @author Thomas Morgner
 */
public interface PostfixOperator extends Serializable {
  public TypeValuePair evaluate( FormulaContext context, TypeValuePair value1 )
    throws EvaluationException;
}
