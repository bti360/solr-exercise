package com.bti360.gt.search;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.carrotsearch.randomizedtesting.annotations.ThreadLeakScope;
import com.carrotsearch.randomizedtesting.annotations.ThreadLeakScope.Scope;

@ThreadLeakScope(Scope.NONE)
public abstract class BaseSolrTestCase extends SolrTestCaseJ4 {

    private static final String SOLR_HOME = "solr-conf/";
    private static final String DEFAULT_RESPONSE_HANDLER = "/select";
    private static final String DEFAULT_CORE_NAME = "gt";
    protected static final String UNIQUE_ID_FIELD = "ip";
    private static SolrServer server;

    @BeforeClass
    public static void setupBaseTest() throws Exception {
        initCore("solrconfig.xml", "schema.xml", SOLR_HOME, DEFAULT_CORE_NAME);
        lrf = h.getRequestFactory(DEFAULT_RESPONSE_HANDLER, 0, 20, CommonParams.VERSION, "2.2");
        server = new EmbeddedSolrServer(h.getCoreContainer(), DEFAULT_CORE_NAME);
    }

    @AfterClass
    public static void tearDownBaseTest() throws Exception {
        server.shutdown();
    }

    @Override
    public void clearIndex() {
        super.clearIndex();
        assertU(commit());
    }

    public void replaceIndex(SolrInputDocument... docs) {
        clearIndex();
        for (SolrInputDocument doc : docs) {
            assertU(adoc(doc));
        }
        assertU(commit());
    }

    public boolean isHit(String q) throws SolrServerException {
        return getHitCount(q) > 0;
    }

    public boolean isMiss(String q) throws SolrServerException {
        return !isHit(q);
    }

    public long getHitCount(String q) throws SolrServerException {
        return query(req(q).getParams()).getResults().getNumFound();
    }

    public QueryResponse query(SolrParams params) throws SolrServerException {
        return server.query(params);
    }

    protected static String buildQ(String field, String value) {
        return String.format("%s:%s", field, value);
    }
    
    protected SolrServer getSolrServer() {
        return server;
    }

}
