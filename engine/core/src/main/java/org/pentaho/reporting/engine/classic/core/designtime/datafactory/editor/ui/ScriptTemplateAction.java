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

package org.pentaho.reporting.engine.classic.core.designtime.datafactory.editor.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import javax.script.ScriptEngineFactory;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.designtime.datafactory.DataFactoryEditorSupport;
import org.pentaho.reporting.libraries.base.util.IOUtils;
import org.pentaho.reporting.libraries.base.util.StringUtils;

public abstract class ScriptTemplateAction extends AbstractAction {
  private static final Log logger = LogFactory.getLog( ScriptTemplateAction.class );

  private static final String TEMPLATE_LOCATION_PATTERN =
      "/org/pentaho/reporting/engine/classic/core/designtime/datafactory/scripts/%s-template-%s.txt";
  private URL resource;
  private boolean global;

  protected ScriptTemplateAction( final boolean global ) {
    this.global = global;
    putValue( Action.NAME, Messages.getString( "QueryEditorPanel.InsertTemplate" ) );
    setEnabled( false );
  }

  protected URL getResource() {
    return resource;
  }

  public void actionPerformed( final ActionEvent e ) {
    if ( resource == null ) {
      return;
    }

    if ( checkOverwriteText() ) {
      return;
    }

    try {
      InputStreamReader r = new InputStreamReader( resource.openStream(), "UTF-8" );
      try {
        final StringWriter w = new StringWriter();
        IOUtils.getInstance().copyWriter( r, w );

        setText( w.toString() );
      } finally {
        r.close();
      }
    } catch ( IOException ex ) {
      logger.warn( "Unable to read template.", ex ); // NON-NLS
    }
  }

  private boolean checkOverwriteText() {
    if ( StringUtils.isEmpty( getText(), true ) == false ) {
      if ( JOptionPane.showConfirmDialog( getParentComponent(),
          Messages.getString( "QueryEditorPanel.OverwriteScript" ), Messages
              .getString( "QueryEditorPanel.OverwriteScriptTitle" ), JOptionPane.YES_NO_OPTION ) != JOptionPane.YES_OPTION ) {
        return true;
      }
    }
    return false;
  }

  protected abstract Component getParentComponent();

  protected abstract void setText( String text );

  protected abstract String getText();

  public void update( ScriptEngineFactory engine ) {
    if ( engine == null ) {
      setEnabled( false );
      resource = null;
      return;
    }

    String key = DataFactoryEditorSupport.mapLanguageToSyntaxHighlighting( engine );
    if ( key.startsWith( "text/" ) ) { // NON-NLS
      key = key.substring( 5 );
    }

    String templateType = isGlobal() ? "global" : "query"; // NON-NLS
    resource = QueryEditorPanel.class.getResource( String.format( TEMPLATE_LOCATION_PATTERN, templateType, key ) );
    if ( resource == null && logger.isDebugEnabled() ) {
      logger.debug( "No template for " + templateType + " script in language " + key ); // NON-NLS
    }
    setEnabled( resource != null );
  }

  protected boolean isGlobal() {
    return global;
  }
}
