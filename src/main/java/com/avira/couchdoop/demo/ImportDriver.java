package com.avira.couchdoop.demo;

import com.avira.couchdoop.imp.CouchbaseViewInputFormat;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * TODO Document!
 */
public class ImportDriver extends Configured implements Tool {

  public static void main(String args[]) {
    int exitCode;
    try {
      exitCode = ToolRunner.run(new ImportDriver(), args);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    System.exit(exitCode);
  }

  @Override
  public int run(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage: <output_path>");
      return 1;
    }
    String output = args[0];

    Job job = Job.getInstance(getConf());
    job.setJarByClass(this.getClass());

    // User classpath takes precedence in favor of Hadoop classpath.
    // This is because the Couchbase client requires a newer version of
    // org.apache.httpcomponents:httpcore.
    job.setUserClassesTakesPrecedence(true);

    // Input
    job.setInputFormatClass(CouchbaseViewInputFormat.class);

    // Mapper
    job.setMapperClass(ImportMapper.class);

    // Reducer
    job.setNumReduceTasks(0);

    // Output
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileOutputFormat.setOutputPath(job, new Path(output));

    return 0;
  }
}
