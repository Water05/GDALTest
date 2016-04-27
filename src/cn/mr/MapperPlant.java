package cn.mr;
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.neunn.ne.hdfs.HdfsOperator;
public class MapperPlant  extends Mapper<LongWritable , Text, Text, Text>{

	@Override
	protected void map(LongWritable  key, Text value,Context context)
			throws IOException, InterruptedException {
		String hdfsPath = value.toString();
		//"/home/kjxx/block/GF1/PMS1/20150904/0001020088/GF1_PMS1_130323331003_20150904_0001020088.TIFF"
        String fileName = hdfsPath.substring(hdfsPath.length()-46, hdfsPath.length());
		String localPath = "/apptar";
		HdfsOperator.getFileFromHdfs(hdfsPath, localPath);
		
		super.map(key, value, context);
	}

}
