package cn.mr;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
public class RunnerPlant {
	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		JobConf conf = new JobConf();
		    Job job = Job.getInstance(conf, "word count");
		    job.setJarByClass(RunnerPlant.class);
		    job.setMapperClass(MapperPlant.class);
		    job.setReducerClass(ReducerPlant.class);
		    job.setOutputKeyClass(Text.class);
		    job.setOutputValueClass(Text.class);
		    
		    FileInputFormat.setInputPaths( conf, "");
		    FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		    System.exit(job.waitForCompletion(true) ? 0 : 1);	
	}

}
