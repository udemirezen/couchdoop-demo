package com.avira.couchdoop.demo;

import com.avira.couchdoop.exp.CouchbaseAction;
import com.avira.couchdoop.exp.CouchbaseOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;

public class ExportMapper  extends Mapper<LongWritable, Text, String, CouchbaseAction> {

    private static final String DELIMITER = "\t";
    private static final String SECONDARY_DELIMITER = "\t";

    private static final ObjectMapper JACKSON = new ObjectMapper();


    @Override
    /**
     * Recommended articles are in the format: session_id   article_name1;score1    article_name2;score2    ....
     */
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] tokens = value.toString().split(DELIMITER);

        String documentKey = tokens[0];

        Recommendation rec = new Recommendation();
        for(int i=1;i<tokens.length;i++) {
            String[] recData = tokens[i].split(SECONDARY_DELIMITER);
            rec.addArticle( new RecommendedItem(recData[0], Float.parseFloat(recData[1])) );
        }

        CouchbaseAction action = new CouchbaseAction(CouchbaseOperation.SET, JACKSON.writeValueAsString(rec));

        context.write(documentKey, action);
    }

}
