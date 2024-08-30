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

package org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.parser;

import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.AbstractMDXDataFactory;
import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.SimpleLegacyBandedMDXDataFactory;

/**
 * Todo: Document Me
 *
 * @author Thomas Morgner
 */
public class SimpleLegacyBandedMDXDataSourceReadHandler extends AbstractMDXDataSourceReadHandler {
  public SimpleLegacyBandedMDXDataSourceReadHandler() {
  }

  protected AbstractMDXDataFactory createDataFactory() {
    return new SimpleLegacyBandedMDXDataFactory();
  }
}
