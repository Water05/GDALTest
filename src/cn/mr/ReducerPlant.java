package cn.mr;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ReducerPlant extends Reducer<Text, PlantWritable, Text, Text> {
   private Text result = new Text();
	@Override
	protected void reduce(Text key, Iterable<PlantWritable> values,
			Reducer<Text, PlantWritable, Text, Text>.Context context)
			throws IOException, InterruptedException {
          int plantCount = 0;
          int noPlantCount = 0;
          double percentPlant = 0;
          for (PlantWritable val : values) {
        	  plantCount += Integer.parseInt(val.plant);
        	  noPlantCount += Integer.parseInt(val.noPlant);
          }
          percentPlant = ((float)(plantCount))/(plantCount + noPlantCount);
          result.set("植被数目："+ plantCount + "总数目：" + (plantCount + noPlantCount) + "覆盖率：" + percentPlant);
          context.write(key, result);   
          
	}

}
