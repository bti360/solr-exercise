function processAdd(cmd) {

  var doc = cmd.solrDoc;  // org.apache.solr.common.SolrInputDocument
  var latitude = doc.getFieldValue("latitude");
  var longitude = doc.getFieldValue("longitude");
  
  if(latitude && longitude){
  	doc.setField("lat-lon", latitude + "," + longitude);
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