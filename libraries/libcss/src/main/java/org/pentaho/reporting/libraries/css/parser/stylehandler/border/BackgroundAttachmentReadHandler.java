/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.reporting.libraries.css.parser.stylehandler.border;

import org.pentaho.reporting.libraries.css.keys.border.BackgroundAttachment;
import org.pentaho.reporting.libraries.css.parser.stylehandler.ListOfConstantsReadHandler;

/**
 * Creation-Date: 25.11.2005, 18:00:23
 *
 * @author Thomas Morgner
 */
public class BackgroundAttachmentReadHandler extends ListOfConstantsReadHandler {
  public BackgroundAttachmentReadHandler() {
    super( false );
    addValue( BackgroundAttachment.FIXED );
    addValue( BackgroundAttachment.LOCAL );
    addValue( BackgroundAttachment.SCROLL );
  }

}
