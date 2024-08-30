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

package org.pentaho.reporting.libraries.resourceloader;

import junit.framework.TestCase;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class ResourceKeyTest extends TestCase {
  public ResourceKeyTest() {
  }

  public ResourceKeyTest( final String string ) {
    super( string );
  }


  protected void setUp()
    throws Exception {
    LibLoaderBoot.getInstance().start();
  }

  public void testResourceKeyCreation()
    throws ResourceKeyCreationException {
    final ResourceManager manager = new ResourceManager();
    manager.registerDefaults();

    final ResourceKey key = manager.createKey
      ( "res://org/pentaho/reporting/libraries/resourceloader/test1.properties" );
    assertNotNull( key );
    final ResourceKey key1 = manager.deriveKey( key, "test2.properties" );
    assertNotNull( key1 );
  }

  public void testURLKeyCreation()
    throws ResourceKeyCreationException {
    final ResourceManager manager = new ResourceManager();
    manager.registerDefaults();

    final URL url = ResourceKeyTest.class.getResource
      ( "/org/pentaho/reporting/libraries/resourceloader/test1.properties" );
    assertNotNull( url );
    final ResourceKey key = manager.createKey( url );
    assertNotNull( key );
    final ResourceKey key1 = manager.deriveKey( key, "test2.properties" );
    assertNotNull( key1 );
  }

  public void testFileKeyCreation()
    throws ResourceKeyCreationException, IOException {
    final ResourceManager manager = new ResourceManager();
    manager.registerDefaults();

    final File f1 = File.createTempFile( "junit-test", ".tmp" );
    f1.deleteOnExit();
    final File f2 = File.createTempFile( "junit-test", ".tmp" );
    f2.deleteOnExit();

    assertNotNull( f1 );
    final ResourceKey key = manager.createKey( f1 );
    assertNotNull( key );
    final ResourceKey key1 = manager.deriveKey( key, f2.getName() );
    assertNotNull( key1 );
  }

  public void testFileObjectKeyCreation()
          throws ResourceKeyCreationException, IOException {
    final ResourceManager manager = new ResourceManager();
    manager.registerDefaults();

    final FileObject fileObject =
            VFS.getManager().resolveFile(
                    Paths.get( "src/test/resources/org/pentaho/reporting/libraries/resourceloader/SVG.svg" ).toAbsolutePath().toString() );
    assertNotNull( fileObject );
    final ResourceKey key = manager.createKey( fileObject );
    assertNotNull( key );
  }

  public void testMixedKeyDerivation()
    throws ResourceKeyCreationException, IOException {
    final File f1 = File.createTempFile( "junit-test", ".tmp" );
    f1.deleteOnExit();
    assertNotNull( f1 );

    final URL url = ResourceKeyTest.class.getResource
      ( "/org/pentaho/reporting/libraries/resourceloader/test1.properties" );
    assertNotNull( url );

    final ResourceManager manager = new ResourceManager();
    manager.registerDefaults();
    final ResourceKey key = manager.createKey( f1 );
    assertNotNull( key );

    final ResourceKey key2 = manager.deriveKey( key, url.toString() );
    assertNotNull( key2 );

    final ResourceKey key3 = manager.createKey( url );
    assertNotNull( key3 );

    final ResourceKey key4 = manager.deriveKey( key3, f1.getAbsolutePath() );
    assertNotNull( key4 );
  }
}
