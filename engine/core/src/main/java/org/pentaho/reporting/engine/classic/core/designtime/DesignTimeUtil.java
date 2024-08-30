/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.reporting.engine.classic.core.designtime;

import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.CompoundDataFactory;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportDefinition;
import org.pentaho.reporting.engine.classic.core.ResourceBundleFactory;
import org.pentaho.reporting.engine.classic.core.Section;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;
import org.pentaho.reporting.libraries.docbundle.DocumentMetaData;
import org.pentaho.reporting.libraries.docbundle.ODFMetaAttributeNames;
import org.pentaho.reporting.libraries.docbundle.WriteableDocumentMetaData;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import java.io.File;
import java.util.Date;

public class DesignTimeUtil {
  private DesignTimeUtil() {
  }

  public static File getContextAsFile( final AbstractReportDefinition reportDefinition ) {
    final ResourceKey key = getContextKey( reportDefinition );
    return getContextAsFile( key );
  }

  public static File getContextAsFile( ResourceKey key ) {
    while ( key != null ) {
      final Object identifier = key.getIdentifier();
      if ( identifier instanceof File ) {
        return (File) identifier;
      }

      key = key.getParent();
    }
    return null;
  }

  public static ResourceKey getContextKey( final AbstractReportDefinition reportDefinition ) {
    AbstractReportDefinition e = reportDefinition;
    while ( e != null ) {
      final ResourceKey base = e.getContentBase();
      if ( base != null ) {
        return base;
      }
      final Section parentSection = e.getParentSection();
      if ( parentSection != null ) {
        final ReportDefinition reportDefinition1 = parentSection.getReportDefinition();
        if ( reportDefinition1 instanceof AbstractReportDefinition ) {
          e = (AbstractReportDefinition) reportDefinition1;
        } else {
          e = null;
        }
      } else {
        e = null;
      }
    }
    return null;
  }

  public static ResourceBundleFactory getResourceBundleFactory( final AbstractReportDefinition reportDefinition ) {
    ReportDefinition e = reportDefinition.getMasterReport();
    if ( e instanceof MasterReport ) {
      final MasterReport report = (MasterReport) e;
      final ResourceBundleFactory base = report.getResourceBundleFactory();
      if ( base != null ) {
        return base;
      }
    }
    return null;
  }

  public static ResourceManager getResourceManager( final AbstractReportDefinition reportDefinition ) {
    ReportDefinition e = reportDefinition.getMasterReport();
    if ( e instanceof MasterReport ) {
      final MasterReport report = (MasterReport) e;
      return report.getResourceManager();
    }
    return new ResourceManager();
  }

  public static MasterReport getMasterReport( final Element element ) {
    ReportDefinition e = element.getMasterReport();
    if ( e instanceof MasterReport ) {
      return (MasterReport) e;
    }
    return null;
  }

  public static void resetTemplate( final MasterReport report ) {
    resetDocumentMetaData( report );
    report.setContentBase( null );
  }

  public static void resetDocumentMetaData( final MasterReport report ) {
    final DocumentMetaData metaData = report.getBundle().getMetaData();
    if ( metaData instanceof WriteableDocumentMetaData ) {
      final WriteableDocumentMetaData wmd = (WriteableDocumentMetaData) metaData;
      wmd.setBundleAttribute( ODFMetaAttributeNames.Meta.NAMESPACE, ODFMetaAttributeNames.Meta.INITIAL_CREATOR, wmd
          .getBundleAttribute( ODFMetaAttributeNames.DublinCore.NAMESPACE, ODFMetaAttributeNames.DublinCore.CREATOR ) );
      try {
        wmd.setBundleAttribute( ODFMetaAttributeNames.DublinCore.NAMESPACE, ODFMetaAttributeNames.DublinCore.CREATOR,
            System.getProperty( "user.name" ) );
      } catch ( Exception e ) {
        // ignore it, not that important ...
      }
      wmd.setBundleAttribute( ODFMetaAttributeNames.DublinCore.NAMESPACE, ODFMetaAttributeNames.DublinCore.DESCRIPTION,
          null );
      wmd.setBundleAttribute( ODFMetaAttributeNames.DublinCore.NAMESPACE, ODFMetaAttributeNames.DublinCore.SUBJECT,
          null );
      wmd.setBundleAttribute( ODFMetaAttributeNames.DublinCore.NAMESPACE, ODFMetaAttributeNames.DublinCore.TITLE, null );
      wmd.setBundleAttribute( ODFMetaAttributeNames.Meta.NAMESPACE, ODFMetaAttributeNames.Meta.CREATION_DATE,
          new Date() );
      wmd.setBundleAttribute( ODFMetaAttributeNames.Meta.NAMESPACE, ODFMetaAttributeNames.Meta.KEYWORDS, null );
    }
  }

  public static boolean isSelectedDataSource( final AbstractReportDefinition report, final DataFactory dataFactory,
      final String queryName ) {
    if ( ObjectUtilities.equal( queryName, report.getQuery() ) == false ) {
      // the query/datasource combination given in the parameter cannot be a selected
      // combination if the query does not match the report's active query ..
      return false;
    }

    AbstractReportDefinition reportDefinition = report;
    while ( reportDefinition != null ) {
      final DataFactory reportDataFactory = reportDefinition.getDataFactory();
      if ( reportDataFactory instanceof CompoundDataFactory ) {
        final CompoundDataFactory compoundDataFactory = (CompoundDataFactory) reportDataFactory;
        for ( int i = 0; i < compoundDataFactory.size(); i++ ) {
          final DataFactory df = compoundDataFactory.getReference( i );
          for ( final String query : df.getQueryNames() ) {
            if ( !query.equals( queryName ) ) {
              continue;
            }

            if ( df == dataFactory ) {
              return true;
            } else {
              return false;
            }
          }
        }
      } else {
        if ( reportDataFactory != null ) {
          for ( final String query : reportDataFactory.getQueryNames() ) {
            if ( !query.equals( queryName ) ) {
              continue;
            }

            if ( reportDataFactory == dataFactory ) {
              return true;
            } else {
              return false;
            }
          }
          return true;
        }

      }
      final Section parentSection = reportDefinition.getParentSection();
      if ( parentSection == null ) {
        reportDefinition = null;
      } else {
        reportDefinition = (AbstractReportDefinition) parentSection.getReportDefinition();
      }
    }

    return false;
  }

}
