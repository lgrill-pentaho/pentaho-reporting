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

package org.pentaho.reporting.libraries.pensol;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileSystem;

import java.util.Collection;

public class JCRSolutionFileSystem extends AbstractFileSystem {
  private JCRSolutionFileModel solutionFileModel;
  public static final String LAST_REFRESH_TIME_ATTRIBUTE = "lastRefreshTime";
  public static final String MAJOR_VERSION = "version-major";
  public static final String MINOR_VERSION = "version-minor";
  public static final String RELEASE_VERSION = "version-release";
  public static final String MILESTONE_VERSION = "version-milestone";
  public static final String BUILD_VERSION = "version-build";

  public JCRSolutionFileSystem( final FileName rootName,
                                final FileSystemOptions fileSystemOptions,
                                final JCRSolutionFileModel solutionFileModel ) {
    super( rootName, null, fileSystemOptions );
    this.solutionFileModel = solutionFileModel;
  }

  /**
   * Creates a file object.  This method is called only if the requested file is not cached.
   */
  protected FileObject createFile(final AbstractFileName name) throws Exception
  {
    return new WebSolutionFileObject(name, this, solutionFileModel);
  }

  /**
   * Adds the capabilities of this file system.
   */
  protected void addCapabilities( final Collection caps ) {
    caps.addAll( PentahoSolutionFileProvider.capabilities );
  }

  public JCRSolutionFileModel getLocalFileModel() {
    return solutionFileModel;
  }

  /**
   * Retrieves the attribute with the specified name. The default implementation simply throws an exception.
   */
  public Object getAttribute( final String attrName ) throws FileSystemException {
    if ( LAST_REFRESH_TIME_ATTRIBUTE.equals( attrName ) ) {
      return Long.valueOf( solutionFileModel.getRefreshTime() );
    }
    if ( MAJOR_VERSION.equals( attrName ) ) {
      return ( solutionFileModel.getMajorVersion() );
    }
    if ( MINOR_VERSION.equals( attrName ) ) {
      return ( solutionFileModel.getMinorVersion() );
    }
    if ( BUILD_VERSION.equals( attrName ) ) {
      return ( solutionFileModel.getBuildVersion() );
    }
    if ( RELEASE_VERSION.equals( attrName ) ) {
      return ( solutionFileModel.getReleaseVersion() );
    }
    if ( MILESTONE_VERSION.equals( attrName ) ) {
      return ( solutionFileModel.getMilestoneVersion() );
    }
    return null;
  }
}
