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
package org.pentaho.reporting.engine.classic.core.modules.misc.connections;

public class DatasourceServiceException extends RuntimeException {
  public DatasourceServiceException() {
  }

  public DatasourceServiceException( final String message ) {
    super( message );
  }

  public DatasourceServiceException( final String message, final Throwable cause ) {
    super( message, cause );
  }

  public DatasourceServiceException( final Throwable cause ) {
    super( cause );
  }
}
