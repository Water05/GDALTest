package cn.test;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
public class ImageTest
{
	public static void main(String[] args)
	{
		//注册文件格式
		gdal.AllRegister();
		//使用只读的方式打开图像
		Dataset poDataset = gdal.Open("/apptar/data/aa.tif",gdalconst.GA_ReadOnly);
	    if (poDataset == null)
		{
			System.out.println("The image could not be read.");
		}else {
			//图像打开成功
			System.out.println("The image could be read.");
			//输出文件的格式
			System.out.println("文件格式："+poDataset.GetDriver().GetDescription());
		    //输出图像的大小和波段个数
			System.out.println("size is:x:"+poDataset.getRasterXSize()+" ,y:"+
		    poDataset.getRasterYSize()+" ,band size:"+poDataset.getRasterCount());
			//输出图像的投影信息
			if (poDataset.GetProjectionRef() != null)
			{
				System.out.println("Projection is "+poDataset.GetProjectionRef());
			}
			//输出图像的坐标和分辨率信息
			double[] adfGeoTransform = new double[6];
		    poDataset.GetGeoTransform(adfGeoTransform);
		    System.out.println("origin : "+adfGeoTransform[0]+","+adfGeoTransform[3]);
		    System.out.println("pixel size:"+adfGeoTransform[1]+","+adfGeoTransform[5]);
		//分别输出各个波段的块大小，并获取该波段的最大值和最小值,颜色表信息
		    for(int band = 0; band < poDataset.getRasterCount();band++)
		    {
		    	Band poBand = poDataset.GetRasterBand(band+1);
		    	System.out.println("Band"+(band+1)+":"+"size:x:"+poBand.getXSize()+",y:"+poBand.getYSize());
		    	Double[] min = new Double[1];
		    	Double[] max = new Double[2];
		    	poBand.GetMinimum(min);
		    	poBand.GetMaximum(max);
		    	if (min[0] != null || max != null)
				{
		    		System.out.println("Min="+min[0]+",max="+max[0]);
				}
		    	if (poBand.GetColorTable() != null)
				{
					System.out.println("band"+band+"has a color table with"
				    +poBand.GetRasterColorTable().GetCount()+"entries.");
				}
		    }
		}
	}
}
