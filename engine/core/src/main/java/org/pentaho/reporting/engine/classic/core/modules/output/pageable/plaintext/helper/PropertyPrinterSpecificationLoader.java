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

package org.pentaho.reporting.engine.classic.core.modules.output.pageable.plaintext.helper;

import org.pentaho.reporting.libraries.base.config.DefaultConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

public class PropertyPrinterSpecificationLoader {
  public static final String ENCODING_PREFIX = "encoding.";
  public static final String ENCODING_NAME = ".name";
  public static final String ENCODING_CHARSET = ".charset";
  public static final String ENCODING_BYTES = ".bytes";

  public static final String PRINTER_PREFIX = "printer.";
  public static final String PRINTER_NAME = ".name";
  public static final String PRINTER_ENCODINGS = ".encodings";
  public static final String PRINTER_OPERATIONS = ".operations";

  public PropertyPrinterSpecificationLoader() {
  }

  public PrinterSpecification[] loadPrinters( final DefaultConfiguration printerConfig,
      final PrinterEncoding[] encodings ) {
    if ( encodings == null ) {
      throw new NullPointerException();
    }
    if ( printerConfig == null ) {
      throw new NullPointerException();
    }

    final HashMap encodingsByKey = new HashMap();
    for ( int i = 0; i < encodings.length; i++ ) {
      encodingsByKey.put( encodings[i].getInternalName(), encodings[i] );
    }

    // collect all available printer model names ...
    final HashSet availablePrinterNames = new HashSet();
    final Iterator it = printerConfig.findPropertyKeys( PropertyPrinterSpecificationLoader.PRINTER_PREFIX );
    while ( it.hasNext() ) {
      final String name = (String) it.next();
      final int beginIndex = name.indexOf( '.' );
      if ( beginIndex == -1 ) {
        continue;
      }
      final int endIndex = name.indexOf( '.', beginIndex + 1 );
      if ( endIndex == -1 ) {
        continue;
      }
      availablePrinterNames.add( name.substring( beginIndex + 1, endIndex ) );
    }

    final PrinterSpecification[] retval = new PrinterSpecification[availablePrinterNames.size()];
    int index = 0;
    // and load them
    final Iterator printerIt = availablePrinterNames.iterator();
    while ( printerIt.hasNext() ) {
      final String printerKey = (String) printerIt.next();
      final String printerName =
          printerConfig.getProperty( PropertyPrinterSpecificationLoader.PRINTER_PREFIX + printerKey
              + PropertyPrinterSpecificationLoader.PRINTER_NAME );
      final String printerCharsets =
          printerConfig.getProperty( PropertyPrinterSpecificationLoader.PRINTER_PREFIX + printerKey
              + PropertyPrinterSpecificationLoader.PRINTER_ENCODINGS );
      // final String printerOperations = printerConfig.getProperty
      // (PRINTER_PREFIX + printerKey + ".operations");
      final String[] supportedCharsets = parseCSVString( printerCharsets );
      // final String[] supportedOperations = parseCSVString(printerOperations);
      final DefaultPrinterSpecification specification = createPrinterSpecification( printerKey, printerName );

      for ( int i = 0; i < supportedCharsets.length; i++ ) {
        final PrinterEncoding encoding = (PrinterEncoding) encodingsByKey.get( supportedCharsets[i] );
        if ( encoding == null ) {
          throw new NullPointerException( "PrinterEncoding '" + supportedCharsets[i] + "' is not defined." );
        }
        specification.addEncoding( encoding );
      }
      retval[index] = specification;
      index += 1;
    }
    return retval;
  }

  protected DefaultPrinterSpecification createPrinterSpecification( final String name, final String displayName ) {
    if ( displayName == null ) {
      return new DefaultPrinterSpecification( name, name );
    } else {
      return new DefaultPrinterSpecification( name, displayName );
    }
  }

  protected PrinterEncoding[] loadEncodings( final DefaultConfiguration encodingConfig ) {
    // collect all available encoding names ...
    final HashSet availableEncodingNames = new HashSet();
    final Iterator it = encodingConfig.findPropertyKeys( PropertyPrinterSpecificationLoader.ENCODING_PREFIX );
    while ( it.hasNext() ) {
      final String name = (String) it.next();
      final int beginIndex = name.indexOf( '.' );
      if ( beginIndex == -1 ) {
        continue;
      }
      final int endIndex = name.indexOf( '.', beginIndex + 1 );
      if ( endIndex == -1 ) {
        continue;
      }
      availableEncodingNames.add( name.substring( beginIndex + 1, endIndex ) );
    }

    // and load them
    final Iterator encIt = availableEncodingNames.iterator();
    final ArrayList encodings = new ArrayList();
    while ( encIt.hasNext() ) {
      final String encodingKey = (String) encIt.next();
      final String encodingName =
          encodingConfig.getProperty( PropertyPrinterSpecificationLoader.ENCODING_PREFIX + encodingKey
              + PropertyPrinterSpecificationLoader.ENCODING_NAME );
      final String encodingCharset =
          encodingConfig.getProperty( PropertyPrinterSpecificationLoader.ENCODING_PREFIX + encodingKey
              + PropertyPrinterSpecificationLoader.ENCODING_CHARSET );
      final String encodingBytes =
          encodingConfig.getProperty( PropertyPrinterSpecificationLoader.ENCODING_PREFIX + encodingKey
              + PropertyPrinterSpecificationLoader.ENCODING_BYTES );
      final byte[] encodingCode = parseBytes( encodingBytes );
      final PrinterEncoding encoding = new PrinterEncoding( encodingKey, encodingName, encodingCharset, encodingCode );
      encodings.add( encoding );
    }

    return (PrinterEncoding[]) encodings.toArray( new PrinterEncoding[encodings.size()] );
  }

  private byte[] parseBytes( final String encString ) {
    final StringTokenizer strtok = new StringTokenizer( encString, ",", false );
    final ArrayList tokens = new ArrayList();
    while ( strtok.hasMoreTokens() ) {
      final String token = strtok.nextToken();
      tokens.add( token );
    }

    final byte[] retval = new byte[tokens.size()];
    for ( int i = 0; i < tokens.size(); i++ ) {
      retval[i] = Byte.parseByte( (String) tokens.get( i ) );
    }
    return retval;
  }

  private String[] parseCSVString( final String encString ) {
    final StringTokenizer strtok = new StringTokenizer( encString, ",", false );
    final ArrayList tokens = new ArrayList();
    while ( strtok.hasMoreTokens() ) {
      final String token = strtok.nextToken();
      tokens.add( token );
    }

    final String[] retval = new String[tokens.size()];
    return (String[]) tokens.toArray( retval );
  }

}
