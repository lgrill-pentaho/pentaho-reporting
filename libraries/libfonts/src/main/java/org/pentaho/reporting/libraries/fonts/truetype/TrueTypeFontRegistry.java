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

package org.pentaho.reporting.libraries.fonts.truetype;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.fonts.FontException;
import org.pentaho.reporting.libraries.fonts.LibFontBoot;
import org.pentaho.reporting.libraries.fonts.cache.FontCache;
import org.pentaho.reporting.libraries.fonts.registry.AbstractFontFileRegistry;
import org.pentaho.reporting.libraries.fonts.registry.DefaultFontFamily;
import org.pentaho.reporting.libraries.fonts.registry.FontMetricsFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;

/**
 * Creation-Date: 07.11.2005, 19:05:46
 *
 * @author Thomas Morgner
 */
public class TrueTypeFontRegistry extends AbstractFontFileRegistry {
  private static FontCache secondLevelCache;

  protected static synchronized FontCache internalGetSecondLevelCache() {
    if ( secondLevelCache == null ) {
      secondLevelCache = LibFontBoot.getInstance().createDefaultCache();
    }
    return secondLevelCache;
  }

  private static final Log logger = LogFactory.getLog( TrueTypeFontRegistry.class );

  /**
   * The font path filter is used to collect font files and directories during the font path registration.
   */
  private static class FontPathFilter implements FileFilter, Serializable {
    /**
     * Default Constructor.
     */
    protected FontPathFilter() {
    }

    /**
     * Tests whether or not the specified abstract pathname should be included in a pathname list.
     *
     * @param pathname The abstract pathname to be tested
     * @return <code>true</code> if and only if <code>pathname</code> should be included
     */
    public boolean accept( final File pathname ) {
      if ( pathname.canRead() == false ) {
        return false;
      }
      if ( pathname.isDirectory() ) {
        return true;
      }
      final String name = pathname.getName();
      if ( StringUtils.endsWithIgnoreCase( name, ".ttf" ) ) {
        return true;
      }
      if ( StringUtils.endsWithIgnoreCase( name, ".ttc" ) ) {
        return true;
      }
      if ( StringUtils.endsWithIgnoreCase( name, ".otf" ) ) {
        return true;
      }
      return false;
    }

  }

  /**
   * The singleton instance of the font path filter.
   */
  private static final FontPathFilter FONTPATHFILTER = new FontPathFilter();

  public TrueTypeFontRegistry() {
  }

  public FontCache getSecondLevelCache() {
    return internalGetSecondLevelCache();
  }

  protected FileFilter getFileFilter() {
    return FONTPATHFILTER;
  }

  protected boolean addFont( final File file, final String encoding ) throws IOException {
    try {
      if ( StringUtils.endsWithIgnoreCase( file.getName(), ".ttc" ) ) {
        final TrueTypeCollection ttc = new TrueTypeCollection( file );
        for ( int i = 0; i < ttc.getNumFonts(); i++ ) {
          TrueTypeFont font = null;
          try {
            font = ttc.getFont( i );
            registerTrueTypeFont( font );
          } finally {
            if ( font != null ) {
              font.dispose();
            }
          }
        }
      } else {
        TrueTypeFont font = null;
        try {
          font = new TrueTypeFont( file );
          registerTrueTypeFont( font );
        } finally {
          if ( font != null ) {
            font.dispose();
          }
        }
      }
      return true;
    } catch ( Exception e ) {
      if ( logger.isDebugEnabled() ) {
        logger.debug( "Unable to register font file " + file, e );
      } else if ( logger.isInfoEnabled() ) {
        logger.info( "Unable to register font file " + file + " - " + e.getMessage() );
      }
      // An error must not stop us on our holy mission to find and register
      // all fonts :)
      return false;
    }
  }

  private void registerTrueTypeFont( final TrueTypeFont font )
    throws IOException {
    final NameTable table = (NameTable) font.getTable( NameTable.TABLE_ID );
    if ( table == null ) {
      throw new IOException
        ( "The font '" + font.getFilename() + "' does not have a 'name' table. It is not valid." );
    }
    if ( font.getTable( OS2Table.TABLE_ID ) == null ) {
      throw new IOException
        ( "The font '" + font.getFilename() + "' does not have a 'os/2' table. It is not valid." );
    }
    if ( font.getTable( FontHeaderTable.TABLE_ID ) == null ) {
      throw new IOException
        ( "The font '" + font.getFilename() + "' does not have a 'head' table. It is not valid." );
    }
    if ( font.getTable( HorizontalHeaderTable.TABLE_ID ) == null ) {
      throw new IOException
        ( "The font '" + font.getFilename() + "' does not have a 'hhea' table. It is not valid." );
    }


    final String familyName = table.getPrimaryName( NameTable.NAME_FAMILY );
    final DefaultFontFamily fontFamily = createFamily( familyName );
    try {
      final TrueTypeFontRecord record = new TrueTypeFontRecord( font, fontFamily );
      fontFamily.addFontRecord( record );
    } catch ( FontException e ) {
      logger.info( "The font '" + font.getFilename() + "' is invalid.", e );
      return;
    }

    registerPrimaryName( familyName, fontFamily );
    registerAlternativeName( familyName, fontFamily );

    final String[] allNames = table.getAllNames( NameTable.NAME_FAMILY );
    final int nameCount = allNames.length;
    for ( int i = 0; i < nameCount; i++ ) {
      final String name = allNames[ i ];
      fontFamily.addName( name );
      registerAlternativeName( name, fontFamily );
    }

    final String[] allFullNames = table.getAllNames( NameTable.NAME_FULLNAME );
    final int allNameCount = allFullNames.length;
    for ( int i = 0; i < allNameCount; i++ ) {
      final String name = allFullNames[ i ];
      registerFullName( name, fontFamily );
    }

  }

  /**
   * Creates a new font metrics factory. That factory is specific to a certain font registry and is not required to
   * handle font records from foreign font registries.
   * <p/>
   * A font metrics factory should never be used on its own. It should be embedded into and used by a FontStorage
   * implementation.
   *
   * @return a new FontMetricsFactory instance
   */
  public FontMetricsFactory createMetricsFactory() {
    return new TrueTypeFontMetricsFactory();
  }

  protected String getCacheFileName() {
    return "ttf-fontcache.ser";
  }

}
