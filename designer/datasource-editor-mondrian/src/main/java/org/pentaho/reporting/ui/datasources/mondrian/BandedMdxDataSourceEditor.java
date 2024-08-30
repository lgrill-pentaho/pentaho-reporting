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

package org.pentaho.reporting.ui.datasources.mondrian;

import org.pentaho.reporting.engine.classic.core.designtime.DesignTimeContext;
import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.AbstractMDXDataFactory;
import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.BandedMDXDataFactory;

import java.awt.*;

/**
 * @author Michael D'Amour
 */
public class BandedMdxDataSourceEditor extends MondrianDataSourceEditor {
  public BandedMdxDataSourceEditor( final DesignTimeContext context ) {
    super( context );
  }

  public BandedMdxDataSourceEditor( final DesignTimeContext context, final Dialog owner ) {
    super( context, owner );
  }

  public BandedMdxDataSourceEditor( final DesignTimeContext context, final Frame owner ) {
    super( context, owner );
  }

  protected void init( final DesignTimeContext context ) {
    super.init( context );
    setTitle( Messages.getString( "BandedMdxDataSourceEditor.Title" ) );
  }

  protected String getDialogId() {
    return "MondrianDataSourceEditor.Banded";
  }

  protected AbstractMDXDataFactory createDataFactory() {
    final BandedMDXDataFactory returnDataFactory = new BandedMDXDataFactory();
    configureConnection( returnDataFactory );
    configureQueries( returnDataFactory );
    return returnDataFactory;
  }

}
