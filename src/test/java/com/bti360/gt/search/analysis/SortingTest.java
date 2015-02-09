package com.bti360.gt.search.analysis;

import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CommonParams;
import org.junit.Test;

import com.bti360.gt.search.BaseSolrTestCase;

public class SortingTest extends BaseSolrTestCase {

    @Test
    public void dynamicSortFieldSendsEmptyValuesToEnd() throws SolrServerException {
        replaceIndex(
            sdoc(UNIQUE_ID_FIELD, "192.168.1.1", "foo_sort", "a"),
            sdoc(UNIQUE_ID_FIELD, "192.168.1.2", "bar_sort", "a"),
            sdoc(UNIQUE_ID_FIELD, "192.168.1.3", "foo_sort", "b", "bar_sort", "b")
        );

        assertThat(getOrderedIdentifiers("foo_sort asc"), contains("192.168.1.1", "192.168.1.3", "192.168.1.2"));
        assertThat(getOrderedIdentifiers("foo_sort desc"), contains("192.168.1.3", "192.168.1.1", "192.168.1.2"));
        assertThat(getOrderedIdentifiers("bar_sort asc"), contains("192.168.1.2", "192.168.1.3", "192.168.1.1"));
    }
    
    @Test
    public void dynamicSortFieldCaseInsensitive() throws SolrServerException {
        replaceIndex(
            sdoc(UNIQUE_ID_FIELD, "192.168.1.1", "foo_sort", "A"),
            sdoc(UNIQUE_ID_FIELD, "192.168.1.2", "foo_sort", "a"),
            sdoc(UNIQUE_ID_FIELD, "192.168.1.3", "foo_sort", "b")
        );

        assertThat(getOrderedIdentifiers("foo_sort asc, ip asc"), contains("192.168.1.1", "192.168.1.2", "192.168.1.3"));
        assertThat(getOrderedIdentifiers("foo_sort desc, ip asc"), contains("192.168.1.3", "192.168.1.1", "192.168.1.2"));
    }
    
    private List<String> getOrderedIdentifiers(String sortCriteria) throws SolrServerException {
        List<String> ids = new ArrayList<>();
        
        QueryResponse resp = query(params(CommonParams.Q, "*:*", CommonParams.FL, UNIQUE_ID_FIELD, CommonParams.SORT, sortCriteria));
        for(SolrDocument doc : resp.getResults()) {
            ids.add(Objects.toString(doc.getFieldValue(UNIQUE_ID_FIELD)));
        }
        
        return ids;
    }

}
