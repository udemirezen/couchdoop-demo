function (doc, meta) {
  // Retrieve only JSON documents.
  if (meta.type != "json") {
    return;
  }

  var NUM_PARTITIONS = 64;
  var re = /sid::([\d]+)::([\d]+)/
  var fields = re.exec(meta.id);
  var date = fields[1];
  var sid = parseInt(fields[2]);

  // Compute sample number based in session ID
  // (should provide enough randomness).
  var partition = sid % NUM_PARTITIONS;

  emit([date, partition], null);
}
