package cn.mr;
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import cn.bean.GetKeyOfCity;
import cn.bean.Plant;
import cn.gdal.GdalOp;

import com.neunn.ne.hdfs.HdfsOperator;
public class MapperPlant  extends Mapper<LongWritable , Text, Text, PlantWritable>{

	@Override
	protected void map(LongWritable  key, Text value,Context context)
			throws IOException, InterruptedException {
		String hdfsPath = value.toString();
		//"/home/kjxx/block/GF1/PMS1/20150904/0001020088/GF1_PMS1_130323331003_20150904_0001020088.TIFF"
        String fileName = hdfsPath.substring(hdfsPath.length()-46, hdfsPath.length());
		String localPath = "/apptar";
		//将在hdfs读取到的路径，下载到本地
		HdfsOperator.getFileFromHdfs(hdfsPath, localPath);
		//
		GdalOp gdalOp = new GdalOp();
		String svmPath = "";
		String classifySaveHdfsPath = "";
		Plant plant = gdalOp.gdalOp(localPath+"/"+fileName, svmPath,  classifySaveHdfsPath);
		//传到Reducer中的Value值
		PlantWritable plantWritable = new PlantWritable(plant.getPlant(), plant.getNoPlant());
		//传到Reducer中的Key值
		String cityName = GetKeyOfCity.getKeyOfImage(fileName);
		Text Reducekey =new Text(cityName);
		
		//写操作
		context.write(Reducekey, plantWritable);
	}

}
