package com.bti360.gt.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Ignore;

@Ignore
public abstract class QueryExpectationTestCase extends BaseSolrTestCase {

    public void testExpectations(String field, String indexedText,
            QueryExpectation... expectations) throws SolrServerException {
        
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField(field, indexedText);
        if (!doc.containsKey(UNIQUE_ID_FIELD)) {
            doc.setField(UNIQUE_ID_FIELD, "192.168.1.1");
        }
        replaceIndex(doc);

        List<QueryExpectation> failedExpectations = new ArrayList<QueryExpectation>();
        for (QueryExpectation expectation : expectations) {
            String q = String.format("%s:%s", field, expectation.getQuery());
            if (isHit(q) != expectation.isHit()) {
                failedExpectations.add(expectation);
            }
        }

        if (!failedExpectations.isEmpty()) {
            fail(String.format("The following query expectations failed for field [%s] using text [%s]: %s",
                    field, indexedText, failedExpectations));
        }
    }

}
