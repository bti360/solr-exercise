package com.bti360.gt.search.query;

import static com.bti360.gt.search.QueryExpectation.hit;
import static com.bti360.gt.search.QueryExpectation.miss;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.params.CommonParams;
import org.junit.Test;

import com.bti360.gt.search.QueryExpectationTestCase;
import com.google.common.base.Charsets;

public class FieldAnalyzerQueryTest extends QueryExpectationTestCase {

    @Test
    public void ipField() throws SolrServerException {
	testExpectations("ip", "192.168.1.1", 
	    hit("192.168.1.1"), hit("192.168*"), hit("192*"), hit("192.168.*"),  
	    miss("33.33.33.10"), miss("192.168.1.2"), miss("10.2*"));
    }
    
    @Test
    public void rdnsHostNameField() throws SolrServerException {
	testExpectations("rdns-hostname", "www.bti360.com",
	    hit("www.bti360.com"), hit("WWW.BTI360.COM"), hit("bti360"),
	    hit("bti360.com"), miss("mail.bti360.com")
	);
    }
    
    @Test
    public void sslCertsHostNameField() throws SolrServerException {
        testExpectations("sslcerts-hostname", "d-128-100-108.bootp.virginia.edu",
            hit("VIRGINIA.EDU"), hit("bootp.virginia.edu"), hit("\"d-128-100-108.bootp.virginia.edu\""),
            miss("mail.virginia.edu")
        );
    }

    @Test
    public void cityField() throws SolrServerException {
	testExpectations("city", new String("Macaé".getBytes(), Charsets.UTF_8),
            hit(new String("macaé".getBytes(), Charsets.UTF_8)), hit("macae"),
            miss("maca")
        );
    }
    
    @Test
    public void latitudeField() throws SolrServerException {
        testExpectations("latitude", "-33.8615",
            hit("-33.8615"), hit("[* TO -33.8615]"), hit("[-34 TO -33]"),
            miss("-33"), miss("-34"), miss("[-33.9 TO -34]")
        );
    }
    
    @Test
    public void longitudeField() throws SolrServerException {
        testExpectations("longitude", "151.2055",
            hit("151.2055"), hit("[* TO 151.2055]"), hit("[151 TO 152]"), hit("[151.201 TO 151.21]"),
            miss("151"), miss("152"), miss("[151.3 TO *]")
        );
    }
    
    @Test
    public void latLonField() throws SolrServerException {
        testExpectations("lat-lon", "-33.8615,151.2055",
            hit("[-34,151 TO -33,152]"), hit("[-33.8615,151.2055 TO -30,152]"),
            miss("[-32,155 TO *]"), miss("[-33.75,151 TO -30,152]")
         );
        
        assertEquals("Couldn't find document based on a geospatial radius search.", 1, query(
            params(CommonParams.Q, "*:*", CommonParams.FQ, "{!geofilt sfield=lat-lon}", "pt", "-33.85,151.2", "d", "5")
        ).getResults().getNumFound());
    }
    
    @Test
    public void postalCodeField() throws SolrServerException {
        testExpectations("postal-code", "   20190   ",
            hit("20190"), miss("20191")
        );
    }
    
    @Test
    public void continentField() throws SolrServerException {
        testExpectations("continent", "NORTH AMERICA", hit("NA"),
            hit("na"), hit("\"NORTH AMERICA\""), hit("\"North America\""), miss("\"South America\""));
        
        testExpectations("continent", "EU", hit("Europe"),
            hit("eu"), miss("Africa"), miss("\"South America\""));
    }
    
    @Test
    public void countryField() throws SolrServerException {
        testExpectations("country", "USA", hit("US"), hit("USA"), 
            hit("\"united states of america\""), hit("\"united states\""), miss("united"));
    }
    
    /**
     * Verify the base 64 encoded value is decoded and we can't search on the
     * html syntax. Decoded value equates to:
     * 
     * <pre>
     * {@literal
     *         HTTP/1.1 302 Found
     *         Cache-Control: private
     *         Content-Type: text/html; charset=utf-8
     *         Location: https://109.173.13.13/remote
     *         Server: Microsoft-IIS/7.5
     *         Set-Cookie: ASP.NET_SessionId=ucbyr2sik04xl1oog45zlrdv; path=/; HttpOnly
     *         X-AspNet-Version: 4.0.30319
     *         X-Powered-By: ASP.NET
     *         Date: Tue, 29 Oct 2013 11:58:05 GMT
     *         Connection: close
     *         Content-Length: 145
     * 
     * <html><head><title>Object moved</title></head><body>
     * <h2>Object moved to <a href="https://109.173.13.13/remote">here</a>.</h2>
     * </body></html>
     * }
     * </pre>
     * 
     */
    @Test
    public void tcpResponseField() throws SolrServerException {
        testExpectations("tcp80-response", "SFRUUC8xLjEgMzAyIEZvdW5kDQpDYWNoZS1Db250cm9sOiBwcml2YXRlDQpDb250ZW50LVR5cGU6IHRleHQvaHRtbDsgY2hhcnNldD11dGYtOA0KTG9jYXRpb246IGh0dHBzOi8vMTA5LjE3My4xMy4xMy9yZW1vdGUNClNlcnZlcjogTWljcm9zb2Z0LUlJUy83LjUNClNldC1Db29raWU6IEFTUC5ORVRfU2Vzc2lvbklkPXVjYnlyMnNpazA0eGwxb29nNDV6bHJkdjsgcGF0aD0vOyBIdHRwT25seQ0KWC1Bc3BOZXQtVmVyc2lvbjogNC4wLjMwMzE5DQpYLVBvd2VyZWQtQnk6IEFTUC5ORVQNCkRhdGU6IFR1ZSwgMjkgT2N0IDIwMTMgMTE6NTg6MDUgR01UDQpDb25uZWN0aW9uOiBjbG9zZQ0KQ29udGVudC1MZW5ndGg6IDE0NQ0KDQo8aHRtbD48aGVhZD48dGl0bGU+T2JqZWN0IG1vdmVkPC90aXRsZT48L2hlYWQ+PGJvZHk+DQo8aDI+T2JqZWN0IG1vdmVkIHRvIDxhIGhyZWY9Imh0dHBzOi8vMTA5LjE3My4xMy4xMy9yZW1vdGUiPmhlcmU8L2E+LjwvaDI+DQo8L2JvZHk+PC9odG1sPg0K",
            hit("microsoft"), hit("iis"), hit("microsoft-iis"),
            hit("ASP.NET"), hit("\"object moved to here\""), miss("href"), miss("body")
        );
    }
    
    @Test
    public void updatedOnField() throws SolrServerException {
        //Specifically left indexed-on null, we can use default values with relative dates to 'NOW' in the schema.
        testExpectations("updated-on", null,
            hit("[* TO NOW]"), hit("[NOW-1MINUTE-15SECONDS TO NOW]"), hit("[2015-01-31T20:10:00.000Z TO NOW]"),
            hit("[NOW/HOUR TO NOW]"), // /HOUR rounds down to the nearest hour
            miss("[NOW TO *]"), miss("[* TO NOW-1HOUR")
        );
    }
}
