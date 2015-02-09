package com.bti360.gt.search.analysis;

import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.FacetParams;
import org.junit.Test;

import com.bti360.gt.search.BaseSolrTestCase;

public class IndexedTermsTest extends BaseSolrTestCase {

    @Test
    public void continentSynonymTerms() throws SolrServerException {
        final String continentField = "continent";
        replaceIndex(sdoc(UNIQUE_ID_FIELD, "1.1.1.1", continentField, "NORTH AMERICA"),
                sdoc(UNIQUE_ID_FIELD, "2.2.2.2", continentField, "North America"),
                sdoc(UNIQUE_ID_FIELD, "3.3.3.3", continentField, "na"),
                sdoc(UNIQUE_ID_FIELD, "4.4.4.4", continentField, "eu"),
                sdoc(UNIQUE_ID_FIELD, "5.5.5.5", continentField, "Europe"),
                sdoc(UNIQUE_ID_FIELD, "6.6.6.6", continentField, "Foo")
        );

        assertThat(getIndexedTerms("1.1.1.1", continentField), contains("north america"));
        assertThat(getIndexedTerms("2.2.2.2", continentField), contains("north america"));
        assertThat(getIndexedTerms("3.3.3.3", continentField), contains("north america"));
        assertThat(getIndexedTerms("4.4.4.4", continentField), contains("europe"));
        assertThat(getIndexedTerms("5.5.5.5", continentField), contains("europe"));
        assertThat(getIndexedTerms("6.6.6.6", continentField), contains("foo"));
    }
    
    @Test
    public void countrySynonymTerms() throws SolrServerException {
        final String countryField = "country";
        replaceIndex(sdoc(UNIQUE_ID_FIELD, "1.1.1.1", countryField, "USA"),
                sdoc(UNIQUE_ID_FIELD, "2.2.2.2", countryField, "us"),
                sdoc(UNIQUE_ID_FIELD, "3.3.3.3", countryField, "United States"),
                sdoc(UNIQUE_ID_FIELD, "4.4.4.4", countryField, "canada"),
                sdoc(UNIQUE_ID_FIELD, "5.5.5.5", countryField, "can"),
                sdoc(UNIQUE_ID_FIELD, "6.6.6.6", countryField, "Foo")
        );

        assertThat(getIndexedTerms("1.1.1.1", countryField), contains("united states"));
        assertThat(getIndexedTerms("2.2.2.2", countryField), contains("united states"));
        assertThat(getIndexedTerms("3.3.3.3", countryField), contains("united states"));
        assertThat(getIndexedTerms("4.4.4.4", countryField), contains("canada"));
        assertThat(getIndexedTerms("5.5.5.5", countryField), contains("canada"));
        assertThat(getIndexedTerms("6.6.6.6", countryField), contains("foo"));
    }

    private List<String> getIndexedTerms(String id, String field) throws SolrServerException {
        QueryResponse resp = getSolrServer().query(params(
                CommonParams.Q, String.format("%s:\"%s\"", UNIQUE_ID_FIELD, id),
                FacetParams.FACET, "true", FacetParams.FACET_LIMIT, "-1",
                FacetParams.FACET_MINCOUNT, "1", FacetParams.FACET_FIELD, field));

        List<String> terms = new ArrayList<String>();
        for(Count facetTermCount : resp.getFacetField(field).getValues()) {
            terms.add(facetTermCount.getName());
        }

        return terms;
    }
}
