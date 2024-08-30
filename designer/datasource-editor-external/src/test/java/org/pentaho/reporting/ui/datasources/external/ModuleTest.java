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

package org.pentaho.reporting.ui.datasources.external;

import junit.framework.TestCase;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.ExternalDataFactory;
import org.pentaho.reporting.engine.classic.core.designtime.DataSourcePlugin;
import org.pentaho.reporting.engine.classic.core.metadata.DataFactoryRegistry;

public class ModuleTest extends TestCase
{
  public ModuleTest()
  {
  }

  protected void setUp() throws Exception
  {
    ClassicEngineBoot.getInstance().start();
  }

  public void testModuleExists()
  {
    assertTrue(ClassicEngineBoot.getInstance().getPackageManager().isModuleAvailable(ExternalDataSourceModule.class.getName()));
  }

  public void testEditorRegistered()
  {
    DataSourcePlugin editor =
        DataFactoryRegistry.getInstance().getMetaData(ExternalDataFactory.class.getName()).createEditor();
    assertNotNull(editor);

    // this editor only creates, never modifies
    assertFalse(editor.canHandle(new ExternalDataFactory()));
  }

}
