function (doc, meta) {
  // Retrieve only JSON documents.
  if (meta.type != "json") {
    return;
  }

  // Split each day into 64 smaller parts.
  var NUM_PARTITIONS = 64;
  // Use Regex to extract the date and the session id.
  var re = /ses::([\d]+)::([\w]+)::([\d]+)/
  var fields = re.exec(meta.id);

  if (fields != null) {
    var date = fields[1];
    var sid = parseInt(fields[3]);

    // Compute random partition number based on session ID
    // (should provide enough randomness).
    var partition = sid % NUM_PARTITIONS;

    emit([date, partition], null);
  }
}
