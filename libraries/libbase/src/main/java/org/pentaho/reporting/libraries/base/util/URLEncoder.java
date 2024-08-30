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

package org.pentaho.reporting.libraries.base.util;

import java.io.CharArrayWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.BitSet;

/**
 * Creation-Date: Jan 22, 2007, 4:36:38 PM
 *
 * @author Thomas Morgner moved by Tyler Band
 */
@Deprecated
public class URLEncoder {

  static BitSet dontNeedEncoding;
  static final int caseDiff = ( 'a' - 'A' );

  static {
    dontNeedEncoding = new BitSet( 256 );
    int i;
    for ( i = 'a'; i <= 'z'; i++ ) {
      dontNeedEncoding.set( i );
    }
    for ( i = 'A'; i <= 'Z'; i++ ) {
      dontNeedEncoding.set( i );
    }
    for ( i = '0'; i <= '9'; i++ ) {
      dontNeedEncoding.set( i );
    }
    //dontNeedEncoding.set(' '); /* encoding a space to a + is done
    //                              * in the encode() method */
    dontNeedEncoding.set( '-' );
    dontNeedEncoding.set( '_' );
    dontNeedEncoding.set( '.' );
    dontNeedEncoding.set( '!' );
    dontNeedEncoding.set( '~' );
    dontNeedEncoding.set( '*' );
    dontNeedEncoding.set( '\'' );
    dontNeedEncoding.set( '(' );
    dontNeedEncoding.set( ')' );
  }

  private URLEncoder() {
  }

  /**
   * Encode a string according to RFC 1738.
   * <p/>
   * <quote> "...Only alphanumerics [0-9a-zA-Z], the special characters "$-_.+!*'()," [not including the quotes - ed],
   * and reserved characters used for their reserved purposes may be used unencoded within a URL."</quote>
   * <p/>
   * <ul> <li><p>The ASCII characters 'a' through 'z', 'A' through 'Z', and '0' through '9' remain the same.
   * <p/>
   * <li><p>The unreserved characters - _ . ! ~ * ' ( ) remain the same.
   * <p/>
   * <li><p>All other ASCII characters are converted into the 3-character string "%xy", where xy is the two-digit
   * hexadecimal representation of the character code
   * <p/>
   * <li><p>All non-ASCII characters are encoded in two steps: first to a sequence of 2 or 3 bytes, using the UTF-8
   * algorithm; secondly each of these bytes is encoded as "%xx". </ul>
   * <p/>
   * This method was adapted from http://www.w3.org/International/URLUTF8Encoder.java Licensed under
   * http://www.w3.org/Consortium/Legal/2002/copyright-software-20021231
   *
   * @param s The string to be encoded
   * @return The encoded string
   */

  public static String encode( final String s, String enc )
    throws UnsupportedEncodingException {
    boolean needToChange = false;
    final StringBuffer sbuf = new StringBuffer( s.length() );
    final char[] sChars = s.toCharArray();
    final int len = sChars.length;
    Charset charset = charset = Charset.forName( enc );
    CharArrayWriter charArrayWriter = new CharArrayWriter();

    for ( int i = 0; i < len; ) {
      int c = sChars[ i ];
      if ( dontNeedEncoding.get( c ) ) {
        sbuf.append( (char) c );
        i++;
      } else {    // other ASCII
        // convert to external encoding before hex conversion
        do {
          charArrayWriter.write( c );
                    /*
                     * If this character represents the start of a Unicode
                     * surrogate pair, then pass in two characters. It's not
                     * clear what should be done if a bytes reserved in the
                     * surrogate pairs range occurs outside of a legal
                     * surrogate pair. For now, just treat it as if it were
                     * any other character.
                     */
          if ( c >= 0xD800 && c <= 0xDBFF ) {
                        /*
                          System.out.println(Integer.toHexString(c)
                          + " is high surrogate");
                        */
            if ( ( i + 1 ) < s.length() ) {
              int d = (int) s.charAt( i + 1 );
                            /*
                              System.out.println("\tExamining "
                              + Integer.toHexString(d));
                            */
              if ( d >= 0xDC00 && d <= 0xDFFF ) {
                                /*
                                  System.out.println("\t"
                                  + Integer.toHexString(d)
                                  + " is low surrogate");
                                */
                charArrayWriter.write( d );
                i++;
              }
            }
          }
          i++;
        } while ( i < s.length() && !dontNeedEncoding.get( ( c = (int) s.charAt( i ) ) ) );

        charArrayWriter.flush();
        String str = new String( charArrayWriter.toCharArray() );
        byte[] ba = str.getBytes( charset );
        for ( int j = 0; j < ba.length; j++ ) {
          sbuf.append( '%' );
          char ch = Character.forDigit( ( ba[ j ] >> 4 ) & 0xF, 16 );
          // converting to use uppercase letter as part of
          // the hex value if ch is a letter.
          if ( Character.isLetter( ch ) ) {
            ch -= caseDiff;
          }
          sbuf.append( ch );
          ch = Character.forDigit( ba[ j ] & 0xF, 16 );
          if ( Character.isLetter( ch ) ) {
            ch -= caseDiff;
          }
          sbuf.append( ch );
        }
        charArrayWriter.reset();
        needToChange = true;
      }
    }
    return ( needToChange ? sbuf.toString() : s );
  }

  public static String encodeUTF8( final String s ) {
    try {
      return encode( s, "UTF-8" );
    } catch ( UnsupportedEncodingException e ) {
      //ignore
    }
    return null;

  }

}
