package com.bti360.gt.search.query;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;

import com.bti360.gt.search.BaseSolrTestCase;

public class LatLonMapperTest extends BaseSolrTestCase {

    private static String LAT_FIELD = "latitude";
    private static String LON_FIELD = "longitude";
    private static String LAT_LON_FIELD = "lat-lon";

    @Test
    public void latLongConcatStorage() throws SolrServerException {
	replaceIndex(sdoc(UNIQUE_ID_FIELD, "192.168.1.1",
	    LAT_FIELD, "13.4244", LON_FIELD, "99.9569"));

	assertTrue("Unable to search on the comnined lat-lon field",
	    isHit(buildQ(LAT_LON_FIELD, "[13.4,99.9 TO 13.5,100]")));
    }
}
