package com.avira.couchdoop.demo;

import com.avira.couchdoop.exp.CouchbaseAction;
import com.avira.couchdoop.exp.CouchbaseOutputFormat;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * TODO
 */
public class ExportDriver extends Configured implements Tool {

  public static void main(String args[]) {
    int exitCode;
    try {
      exitCode = ToolRunner.run(new ExportDriver(), args);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    System.exit(exitCode);
  }

  @Override
  public int run(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage: <input_path>");
      return 1;
    }
    String input = args[0];

    Job job = Job.getInstance(getConf());
    job.setJarByClass(ExportDriver.class);

    // User classpath takes precedence in favor of Hadoop classpath.
    // This is because the Couchbase client requires a newer version of
    // org.apache.httpcomponents:httpcore.
    job.setUserClassesTakesPrecedence(true);

    // Input
    FileInputFormat.setInputPaths(job, input);

    // Mapper
//    job.setMapperClass(Mapper.class); // TODO

    // Reducer
    job.setNumReduceTasks(0);

    // Output
    job.setOutputKeyClass(String.class);
    job.setOutputValueClass(CouchbaseAction.class);
    job.setOutputFormatClass(CouchbaseOutputFormat.class);

    return 0;
  }
}
