package com.bti360.gt.search.update.processor;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.processor.FieldMutatingUpdateProcessorFactory;
import org.apache.solr.update.processor.FieldValueMutatingUpdateProcessor;
import org.apache.solr.update.processor.UpdateRequestProcessor;

/**
 * This class decodes an incoming base64 encoded value and replaces the original value. 
 * Example usage in an update processor chain:
 * 
 * <pre class="prettyprint">
 * &lt;processor class="com.bti360.gt.search.update.processor.Base64DecodingUpdateProcessorFactory"&gt;
 *   &lt;str name="fieldName"&gt;content&lt;/str&gt;
 * &lt;/processor&gt;
 * </pre>
 */
public class Base64DecodingUpdateProcessorFactory extends FieldMutatingUpdateProcessorFactory {

    @Override
    public UpdateRequestProcessor getInstance(SolrQueryRequest req,
            SolrQueryResponse rsp, UpdateRequestProcessor next) {

        return new FieldValueMutatingUpdateProcessor(getSelector(), next) {
            @Override
            protected Object mutateValue(final Object src) {
                if (src instanceof String) {
                    return StringUtils.toEncodedString(DatatypeConverter.parseBase64Binary((String) src), StandardCharsets.UTF_8);
                }
                
                return src;
            }
        };
    }
}
