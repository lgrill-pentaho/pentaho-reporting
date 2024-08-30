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

package org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.writer;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.ReportWriterContext;
import org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.ReportWriterException;
import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.DenormalizedMDXDataFactory;
import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.MondrianDataFactoryModule;
import org.pentaho.reporting.libraries.xmlns.common.AttributeList;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriter;

import java.io.IOException;

/**
 * Todo: Document me!
 *
 * @author : Thomas Morgner
 */
public class DenormalizedMDXDataFactoryWriteHandler extends AbstractNamedMDXDataFactoryWriteHandler {
  public DenormalizedMDXDataFactoryWriteHandler() {
  }

  public void write( final ReportWriterContext reportWriter,
                     final XmlWriter xmlWriter,
                     final DataFactory dataFactory )
    throws IOException, ReportWriterException {
    final AttributeList rootAttrs = new AttributeList();
    rootAttrs.addNamespaceDeclaration( "data", MondrianDataFactoryModule.NAMESPACE );

    xmlWriter.writeTag( MondrianDataFactoryModule.NAMESPACE, "denormalized-mdx-datasource", rootAttrs, XmlWriter.OPEN );

    final DenormalizedMDXDataFactory pmdDataFactory = (DenormalizedMDXDataFactory) dataFactory;
    writeBody( reportWriter, pmdDataFactory, xmlWriter );
    xmlWriter.writeCloseTag();
  }
}
