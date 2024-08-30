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

package org.pentaho.reporting.engine.classic.core.dom;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.Section;

public class DescendantMatcherTest {

  @Test
  public void testMatches() {
    NodeMatcher childMatcher = mock( NodeMatcher.class );
    MatcherContext context = mock( MatcherContext.class );
    ReportElement node = mock( ReportElement.class );
    Section parent = mock( Section.class );

    DescendantMatcher matcher = new DescendantMatcher( childMatcher );
    assertThat( matcher.matches( context, null ), is( equalTo( false ) ) );

    assertThat( matcher.matches( context, node ), is( equalTo( false ) ) );

    doReturn( parent ).when( context ).getParent( node );
    assertThat( matcher.matches( context, node ), is( equalTo( false ) ) );

    doReturn( true ).when( childMatcher ).matches( context, parent );
    assertThat( matcher.matches( context, node ), is( equalTo( true ) ) );
  }
}
