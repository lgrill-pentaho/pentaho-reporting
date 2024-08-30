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

package org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.styles;

import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.StyleWriterUtility;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleSheet;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriter;

import java.io.IOException;

public class RotationStyleSetWriteHandler implements BundleStyleSetWriteHandler {
  public RotationStyleSetWriteHandler() {
  }

  public void writeStyle( final XmlWriter writer, final ElementStyleSheet style ) throws IOException {
    StyleWriterUtility.writeRotationStyles( writer, style );
  }
}
