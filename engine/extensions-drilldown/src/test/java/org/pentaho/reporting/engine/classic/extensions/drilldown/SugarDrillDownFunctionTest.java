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

package org.pentaho.reporting.engine.classic.extensions.drilldown;

/**
 * @author Thomas Morgner.
 */
public class SugarDrillDownFunctionTest extends FormulaTestBase {
  public SugarDrillDownFunctionTest() {
  }

  protected Object[][] createDataTest() {
    return new Object[][] {
      { "DRILLDOWN(\"local-sugar\"; 0; {\"::pentaho-path\" ; \"/public/steel-wheels/test.prpt\" | \"test\" ; "
        + "\"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
        "http://localhost:8080/pentaho/api/repos/:public:steel-wheels:test"
          + ".prpt/viewer?test=value&mtest=v1&mtest=v2&mtest=v3" },
      { "DRILLDOWN(\"remote-sugar\"; \"ssh://domain.example\"; {\"::pentaho-path\" ; \"/public/steel-wheels/test"
        + ".prpt\" | \"test\" ; \"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
        "ssh://domain.example/api/repos/:public:steel-wheels:test.prpt/viewer?test=value&mtest=v1&mtest=v2&mtest=v3" },
      { "DRILLDOWN(\"remote-sugar\"; \"ssh://domain.example/\"; {\"::pentaho-path\" ; \"/public/steel-wheels/test"
        + ".prpt\" | \"test\" ; \"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
        "ssh://domain.example/api/repos/:public:steel-wheels:test.prpt/viewer?test=value&mtest=v1&mtest=v2&mtest=v3" },
      { "DRILLDOWN(\"local-sugar-no-parameter\"; \"ssh://domain.example\"; {\"::pentaho-path\" ; "
        + "\"/public/steel-wheels/test.prpt\" | \"test\" ; \"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
        "http://localhost:8080/pentaho/api/repos/:public:steel-wheels:test"
          + ".prpt/viewer?showParameters=false&test=value&mtest=v1&mtest=v2&mtest=v3" },
      { "DRILLDOWN(\"remote-sugar-no-parameter\"; \"ssh://domain.example\"; {\"::pentaho-path\" ; "
        + "\"/public/steel-wheels/test.prpt\" | \"test\" ; \"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
        "ssh://domain.example/api/repos/:public:steel-wheels:test"
          + ".prpt/viewer?showParameters=false&test=value&mtest=v1&mtest=v2&mtest=v3" },
      { "DRILLDOWN(\"remote-sugar-no-parameter\"; \"ssh://domain.example/\"; {\"::pentaho-path\" ; "
        + "\"/public/steel-wheels/test.prpt\" | \"test\" ; \"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
        "ssh://domain.example/api/repos/:public:steel-wheels:test"
          + ".prpt/viewer?showParameters=false&test=value&mtest=v1&mtest=v2&mtest=v3" },
      { "DRILLDOWN(\"remote-sugar-prpti\"; \"http://domain.example/\"; {\"::pentaho-path\" ; "
        + "\"/public/steel-wheels/test.prpt\" | \"test\" ; \"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
        "http://domain.example/api/repos/:public:steel-wheels:test.prpt/prpti"
          + ".view?test=value&mtest=v1&mtest=v2&mtest=v3" },
      { "DRILLDOWN(\"local-sugar-prpti\"; 0; {\"::pentaho-path\" ; \"/public/steel-wheels/test.prpt\" | \"test\" ; "
        + "\"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
        "http://localhost:8080/pentaho/api/repos/:public:steel-wheels:test.prpt/prpti"
          + ".view?test=value&mtest=v1&mtest=v2&mtest=v3" },
      { "DRILLDOWN(\"local-sugar-analyzer\"; 0; {\"::pentaho-path\" ; \"/public/steel-wheels/test.prpt\" | \"test\" ;"
        + " \"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
        "http://localhost:8080/pentaho/api/repos/:public:steel-wheels:test"
          + ".prpt/viewer?test=value&mtest=v1&mtest=v2&mtest=v3" },
      { "DRILLDOWN(\"local-sugar-xaction\"; 0; {\"::pentaho-path\" ; \"/public/steel-wheels/test.prpt\" | \"test\" ; "
        + "\"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
        "http://localhost:8080/pentaho/api/repos/:public:steel-wheels:test"
          + ".prpt/generatedContent?test=value&mtest=v1&mtest=v2&mtest=v3" },
      { "DRILLDOWN(\"remote-sugar-analyzer\"; \"http://domain.example/\"; {\"::pentaho-path\" ; "
        + "\"/public/steel-wheels/test.prpt\" | \"test\" ; \"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
        "http://domain.example/api/repos/:public:steel-wheels:test.prpt/viewer?test=value&mtest=v1&mtest=v2&mtest=v3" },
      { "DRILLDOWN(\"remote-sugar-xaction\"; \"http://domain.example/\"; {\"::pentaho-path\" ; "
        + "\"/public/steel-wheels/test.prpt\" | \"test\" ; \"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
        "http://domain.example/api/repos/:public:steel-wheels:test"
          + ".prpt/generatedContent?test=value&mtest=v1&mtest=v2&mtest=v3" },
    };

  }

  public void testDefault() throws Exception {
    runDefaultTest();
  }

}
