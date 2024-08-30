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

package org.pentaho.reporting.engine.classic.core.modules.parser.ext.factory.base;

/**
 * An object-description for a <code>Float</code> object.
 *
 * @author Thomas Morgner
 */
public class FloatObjectDescription extends AbstractObjectDescription {

  /**
   * Creates a new object description.
   */
  public FloatObjectDescription() {
    super( Float.class );
    setParameterDefinition( "value", String.class );
  }

  /**
   * Creates an object based on this description.
   *
   * @return The object.
   */
  public Object createObject() {
    final String o = (String) getParameter( "value" );
    return Float.valueOf( o.trim() );
  }

  /**
   * Sets the parameters of this description object to match the supplied object.
   *
   * @param o
   *          the object (should be an instance of <code>Float</code>).
   * @throws ObjectFactoryException
   *           if the object is not an instance of <code>Float</code>.
   */
  public void setParameterFromObject( final Object o ) throws ObjectFactoryException {
    if ( !( o instanceof Float ) ) {
      throw new ObjectFactoryException( "The given object is no java.lang.Float." );
    }
    setParameter( "value", String.valueOf( o ) );
  }

  /**
   * Tests for equality.
   *
   * @param o
   *          the object to test.
   * @return A boolean.
   */
  public boolean equals( final Object o ) {
    if ( this == o ) {
      return true;
    }
    if ( !( o instanceof AbstractObjectDescription ) ) {
      return false;
    }

    final AbstractObjectDescription abstractObjectDescription = (AbstractObjectDescription) o;

    if ( Float.TYPE.equals( abstractObjectDescription.getObjectClass() ) ) {
      return true;
    }
    if ( Float.class.equals( abstractObjectDescription.getObjectClass() ) ) {
      return true;
    }
    return false;
  }

  /**
   * Returns a hash code.
   *
   * @return A hash code.
   */
  public int hashCode() {
    return getObjectClass().hashCode();
  }

}
