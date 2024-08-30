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

import java.util.Map;

/**
 * A resource bundle is a compound archive that holds several resources.
 *
 * @author Thomas Morgner
 */
public interface ResourceBundleLoader {
  /**
   * Tries to load the bundle. If the key does not point to a usable resource-bundle, this method returns null. The
   * Exception is only thrown if the bundle is not readable because of IO-Errors.
   * <p/>
   * A resource-bundle loader should only load the bundle for the key itself, never for any of the derived subkeys. It
   * is the ResourceManager's responsibility to search the key's hierachy for the correct key.
   *
   * @param key the resource key pointing to the bundle.
   * @return the loaded bundle or null, if the resource was not understood.
   * @throws ResourceLoadingException if something goes wrong.
   */
  public ResourceBundleData loadBundle
  ( final ResourceManager resourceManager, final ResourceKey key ) throws ResourceLoadingException;

  /**
   * Checks, whether this resource loader implementation was responsible for creating this key.
   *
   * @param key the key that should be tested.
   * @return true, if the key is supported.
   */
  public boolean isSupportedKey( ResourceKey key );

  /**
   * Derives a new resource key from the given key. If neither a path nor new factory-keys are given, the parent key is
   * returned.
   *
   * @param parent      the parent
   * @param path        the derived path (can be null).
   * @param factoryKeys the optional factory keys (can be null).
   * @return the derived key.
   * @throws ResourceKeyCreationException if the key cannot be derived for any reason.
   */
  public ResourceKey deriveKey( ResourceKey parent,
                                String path,
                                Map<? extends ParameterKey, ? extends Object> factoryKeys )
    throws ResourceKeyCreationException;

  /**
   * Serializes the resource key to a String representation which can be recreated using the
   * <code>deserialize(ResourceKey)<code> method.
   *
   * @param bundleKey
   * @param key
   * @return a <code>String<code> which is a serialized version of the <code>ResourceKey</code>
   * @throws ResourceException indicates an error serializing the resource key
   */
  public String serialize( final ResourceKey bundleKey, final ResourceKey key ) throws ResourceException;

  /**
   * Creates a <code>ResourceKey</code> based off the <code>String</code> representation of the key. The
   * <code>String</code> should have been created using the <code>serialize</code> method.
   *
   * @param bundleKey
   * @param stringKey the <code>String</code> representation of the <code>ResourceKey</code>  @return a
   *                  <code>ResourceKey</code> which matches the <code>String</code> representation
   * @throws ResourceKeyCreationException indicates an error occurred in the creation or deserialization of the
   *                                      <code>ResourceKey</code>
   */
  public ResourceKey deserialize( final ResourceKey bundleKey, final String stringKey )
    throws ResourceKeyCreationException;

  public boolean isSupportedDeserializer( final String data ) throws ResourceKeyCreationException;

}
