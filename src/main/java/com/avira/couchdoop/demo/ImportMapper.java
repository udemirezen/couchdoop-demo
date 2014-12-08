package com.avira.couchdoop.demo;

import com.couchbase.client.protocol.views.ViewRow;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * TODO
 */
public class ImportMapper extends Mapper<Text, ViewRow, Text, Text> {

  private final StringBuilder STRING_BUILDER = new StringBuilder();
  private final Text OUTPUT_KEY = new Text();
  private final Text OUTPUT_VALUE = new Text();

  private final static String DELIMITER = ",";

  private final static ObjectMapper JACKSON = new ObjectMapper();

  public enum Counters {
    INVALID_COUCHBASE_KEYS,
    JSON_PARSE_ERRORS
  }

  @Override
  protected void map(Text key, ViewRow value, Context context) throws IOException, InterruptedException {
    // Extract user ID from the Couchbase key (doc ID).
    String[] tokens = key.toString().split("::");
    if (tokens.length != 4) {
      context.getCounter(Counters.INVALID_COUCHBASE_KEYS).increment(1);
      return;
    }
    String userId = tokens[3];

    // Parse Couchbase JSON document.
    String jsonString = value.getDocument().toString();
    Session session;
    try {
      session = JACKSON.readValue(jsonString, Session.class);
    } catch (IOException e) {
      context.getCounter(Counters.JSON_PARSE_ERRORS).increment(1);
      return;
    }

    // Extract article names from the JSON document.
    STRING_BUILDER.setLength(0);
    for (Session.Article article : session.getArticles()) {
      STRING_BUILDER.append(article.getName());
      STRING_BUILDER.append(DELIMITER);
    }
    STRING_BUILDER.setLength(STRING_BUILDER.length() - 1);
    String articleNames = STRING_BUILDER.toString();

    // Write output.
    OUTPUT_KEY.set(userId);
    OUTPUT_VALUE.set(articleNames);
    context.write(OUTPUT_KEY, OUTPUT_VALUE);
  }
}
