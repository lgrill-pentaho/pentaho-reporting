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

package org.pentaho.reporting.designer.core.model.data;

import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.util.Timeout;

import java.util.concurrent.TimeUnit;

public class ActorSystemHost {
  public static final ActorSystemHost INSTANCE = new ActorSystemHost();

  private ActorSystem system;

  protected ActorSystemHost() {
    system = ActorSystem.create( "Pentaho-Report-Designer" );
  }

  public ActorSystem getSystem() {
    return system;
  }

  public <IFace, Impl extends IFace> IFace createActor( final Class<IFace> iface, final Class<Impl> impl ) {
    final TypedProps<Impl> queryMetaDataActorTypedProps =
      new TypedProps<Impl>( iface, impl ).withTimeout( Timeout.apply( 30, TimeUnit.MINUTES ) );
    return TypedActor.get( system ).typedActorOf( queryMetaDataActorTypedProps );
  }

  public void stopNow( final Object actor ) {
    TypedActor.get( system ).stop( actor );
  }

  public void shutdown( final Object actor ) {
    TypedActor.get( system ).poisonPill( actor );
  }
}
