function processAdd(cmd) {
  var doc = cmd.solrDoc;  // org.apache.solr.common.SolrInputDocument
  
  var field = params.get('field');
  var encodedTcpResponse = doc.getFieldValue(field);

  if(encodedTcpResponse){
  	doc.setField(field, new java.lang.String(javax.xml.bind.DatatypeConverter.parseBase64Binary(encodedTcpResponse), "UTF-8"));
  }
}

function processDelete(cmd) {
  // no-op
}

function processMergeIndexes(cmd) {
  // no-op
}

function processCommit(cmd) {
  // no-op
}

function processRollback(cmd) {
  // no-op
}

function finish() {
  // no-op
}