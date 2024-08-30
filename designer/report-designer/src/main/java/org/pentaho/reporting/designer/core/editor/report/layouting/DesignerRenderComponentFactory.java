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

package org.pentaho.reporting.designer.core.editor.report.layouting;

import org.pentaho.reporting.engine.classic.core.layout.DefaultRenderComponentFactory;
import org.pentaho.reporting.engine.classic.core.layout.TextProducer;
import org.pentaho.reporting.engine.classic.core.layout.build.LayoutModelBuilder;
import org.pentaho.reporting.engine.classic.core.layout.output.OutputProcessorMetaData;

public class DesignerRenderComponentFactory extends DefaultRenderComponentFactory {
  private TextProducer textProducer;

  public DesignerRenderComponentFactory( final OutputProcessorMetaData metaData ) {
    this.textProducer = new TextProducer( metaData );
  }

  public LayoutModelBuilder createLayoutModelBuilder( final String name ) {
    return new DesignerLayoutModelBuilder( name, this );
  }

  public TextProducer createTextProducer() {
    return textProducer;
  }
}
