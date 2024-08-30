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

package org.pentaho.reporting.engine.classic.core.style.css;

import org.pentaho.reporting.engine.classic.core.style.css.namespaces.NamespaceCollection;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CharacterDataSelector;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.ProcessingInstructionSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorFactory;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

public class FixNamespaceSelectorFactory implements SelectorFactory {
  private SelectorFactory parent;
  private StyleSheetParserUtil styleSheetParserUtil;
  private NamespaceCollection namespaceCollection;

  public FixNamespaceSelectorFactory( final SelectorFactory parent, final NamespaceCollection namespaceCollection ) {
    this.namespaceCollection = namespaceCollection;
    if ( parent == null ) {
      throw new NullPointerException();
    }
    this.styleSheetParserUtil = StyleSheetParserUtil.getInstance();
    this.parent = parent;
  }

  public ConditionalSelector createConditionalSelector( final SimpleSelector selector, final Condition condition )
    throws CSSException {
    return parent.createConditionalSelector( selector, condition );
  }

  public SimpleSelector createAnyNodeSelector() throws CSSException {
    return parent.createAnyNodeSelector();
  }

  public SimpleSelector createRootNodeSelector() throws CSSException {
    return parent.createRootNodeSelector();
  }

  public NegativeSelector createNegativeSelector( final SimpleSelector selector ) throws CSSException {
    return parent.createNegativeSelector( selector );
  }

  public ElementSelector createElementSelector( final String namespaceURI, final String tagName ) throws CSSException {

    if ( namespaceURI != null ) {
      return parent.createElementSelector( namespaceURI, tagName );
    } else {
      if ( tagName == null ) {
        return parent.createElementSelector( null, null );
      } else {
        styleSheetParserUtil = StyleSheetParserUtil.getInstance();
        final String[] ns = styleSheetParserUtil.parseNamespaceIdent( tagName, namespaceCollection );
        return parent.createElementSelector( ns[0], ns[1] );
      }
    }
  }

  public CharacterDataSelector createTextNodeSelector( final String data ) throws CSSException {
    return parent.createTextNodeSelector( data );
  }

  public CharacterDataSelector createCDataSectionSelector( final String data ) throws CSSException {
    return parent.createCDataSectionSelector( data );
  }

  public ProcessingInstructionSelector createProcessingInstructionSelector( final String target, final String data )
    throws CSSException {
    return parent.createProcessingInstructionSelector( target, data );
  }

  public CharacterDataSelector createCommentSelector( final String data ) throws CSSException {
    return parent.createCommentSelector( data );
  }

  public ElementSelector createPseudoElementSelector( final String namespaceURI, final String pseudoName )
    throws CSSException {
    if ( namespaceURI != null ) {
      return parent.createPseudoElementSelector( namespaceURI, pseudoName );
    } else {
      final String[] ns = styleSheetParserUtil.parseNamespaceIdent( pseudoName, namespaceCollection );
      return parent.createPseudoElementSelector( ns[0], ns[1] );
    }
  }

  public DescendantSelector createDescendantSelector( final Selector parent, final SimpleSelector descendant )
    throws CSSException {
    return this.parent.createDescendantSelector( parent, descendant );
  }

  public DescendantSelector createChildSelector( final Selector parent, final SimpleSelector child )
    throws CSSException {
    return this.parent.createChildSelector( parent, child );
  }

  public SiblingSelector createDirectAdjacentSelector( final short nodeType, final Selector child,
      final SimpleSelector directAdjacent ) throws CSSException {
    return parent.createDirectAdjacentSelector( nodeType, child, directAdjacent );
  }
}
