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

package org.pentaho.reporting.libraries.resourceloader.cache;

import org.pentaho.reporting.libraries.resourceloader.ResourceBundleData;
import org.pentaho.reporting.libraries.resourceloader.ResourceData;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * A very simple implementation which is suitable for smaller objects. The complete data is read into memory.
 *
 * @author Thomas Morgner
 */
public class CachingResourceBundleData implements ResourceBundleData, Serializable {
  private static final int CACHE_THRESHOLD = 512 * 1024;

  private ResourceBundleData data;
  private HashMap attributes;
  // The cached raw data. This is stored on the serialized stream as well
  // so that we can cache that stuff.
  private transient byte[] rawData;
  private static final long serialVersionUID = -7272527822440412818L;

  public CachingResourceBundleData( final ResourceBundleData data ) {
    if ( data == null ) {
      throw new NullPointerException();
    }
    this.data = data;
  }

  public InputStream getResourceAsStream( final ResourceManager caller ) throws ResourceLoadingException {
    final byte[] data = getResource( caller );
    return new ByteArrayInputStream( data );
  }

  public long getLength() {
    if ( rawData != null ) {
      return rawData.length;
    }
    return data.getLength();
  }

  public synchronized byte[] getResource( final ResourceManager caller ) throws ResourceLoadingException {
    if ( rawData == null ) {
      rawData = data.getResource( caller );
    }
    return (byte[]) rawData.clone();
  }

  public synchronized int getResource
    ( final ResourceManager caller, final byte[] target, final long offset, final int length )
    throws ResourceLoadingException {
    if ( target == null ) {
      throw new NullPointerException();
    }
    if ( target.length < ( offset + length ) ) {
      throw new IndexOutOfBoundsException();
    }

    if ( rawData == null ) {
      rawData = data.getResource( caller );
    }

    final int iOffset = (int) ( 0x7FFFFFFF & offset );
    final int maxLength = Math.min( rawData.length - iOffset, length );
    if ( maxLength <= 0 ) {
      return -1;
    }

    System.arraycopy( rawData, iOffset, target, 0, maxLength );
    return maxLength;
  }

  public synchronized Object getAttribute( final String key ) {
    if ( attributes == null ) {
      attributes = new HashMap();
    } else {
      final Object cached = attributes.get( key );
      if ( cached != null ) {
        return cached;
      }
    }

    final Object value = data.getAttribute( key );
    if ( value != null ) {
      attributes.put( key, value );
    }
    return value;
  }

  public ResourceKey getKey() {
    return data.getKey();
  }

  public long getVersion( final ResourceManager caller )
    throws ResourceLoadingException {
    return data.getVersion( caller );
  }

  public ResourceKey getBundleKey() {
    return data.getBundleKey();
  }

  public ResourceBundleData deriveData( final ResourceKey key ) throws ResourceLoadingException {
    return data.deriveData( key );
  }

  public ResourceManager deriveManager( final ResourceManager parent ) throws ResourceLoadingException {
    return data.deriveManager( parent );
  }

  public ResourceBundleData getBackend() {
    return data;
  }

  public static ResourceBundleData createCached( final ResourceBundleData data ) {
    // this relieves the pain of having to re-open the same stream more than
    // once. This is no real long term caching, but at least a caching during
    // the current request.
    final Object rawCl = data.getAttribute( ResourceData.CONTENT_LENGTH );
    if ( rawCl instanceof Number ) {
      final Number contentLength = (Number) rawCl;
      if ( contentLength.intValue() < CACHE_THRESHOLD ) {
        // only buffer all data if the content is less than 512kb.
        // Else, we may run into trouble if we try to load a huge item into memory ..
        return new CachingResourceBundleData( data );
      }
    }
    return data;
  }
}
