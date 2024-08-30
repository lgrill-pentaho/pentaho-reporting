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

package org.pentaho.reporting.tools.configeditor;

import org.pentaho.reporting.libraries.base.util.ObjectUtilities;
import org.pentaho.reporting.libraries.base.util.ResourceBundleSupport;

import java.util.HashMap;
import java.util.Locale;

public class Messages extends ResourceBundleSupport {
  private static HashMap<Locale, Messages> locales;

  public static Messages getInstance() {
    return getInstance( Locale.getDefault() );
  }

  public static synchronized Messages getInstance( final Locale locale ) {
    if ( locales == null ) {
      locales = new HashMap<Locale, Messages>();
      final Messages retval = new Messages( locale, ConfigEditorBoot.BUNDLE_NAME );
      locales.put( locale, retval );
      return retval;
    }

    final Messages o = locales.get( locale );
    if ( o != null ) {
      return o;
    }

    final Messages retval = new Messages( locale, ConfigEditorBoot.BUNDLE_NAME );
    locales.put( locale, retval );
    return retval;
  }

  private Messages( final Locale locale, final String s ) {
    super( locale, s, ObjectUtilities.getClassLoader( Messages.class ) );
  }
}
