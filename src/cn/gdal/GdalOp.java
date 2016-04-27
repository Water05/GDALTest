package cn.gdal;

import java.io.IOException;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.ogr.ogr;

import com.neunn.ne.hdfs.HdfsOperator;

public class GdalOp {
	public void gdalOp(String fileName_tif, String svmPath, String classifySaveLocalPath, String classifySaveHdfsPath) throws IOException{
		gdal.AllRegister();
		ogr.RegisterAll();//记得添加驱动注册 
		// 为了支持中文路径，请添加下面这句代码  
		gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","NO");  
		// 为了使属性表字段支持中文，请添加下面这句  
		gdal.SetConfigOption("SHAPE_ENCODING",""); 
		//第一步：     将Tiff读取到数组       
				System.out.println("1）将Tiff读取到数组");
				//String fileName_tif = "E:\\JAVA_neunn\\gdalTest\\source\\GF2_PMS2_132120030030_20150212_0000647892.TIFF";
			//	gdal.AllRegister();

				Dataset pInputDataset = gdal.Open(fileName_tif, gdalconstConstants.GA_ReadOnly);
				if (pInputDataset == null)
				{
					System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
					System.err.println(gdal.GetLastErrorMsg());

					System.exit(1); 
				}
				int xSize=pInputDataset.GetRasterXSize();
				int ySize=pInputDataset.GetRasterYSize();
				int bandCount=pInputDataset.GetRasterCount();
				System.out.println("xSize  "+xSize + "  "+"ySize  "+ ySize+"   "+"bandCount  "+bandCount);
				int [] pInputBuf=new int[xSize*ySize*bandCount];
				int [] pBandMap=new int[bandCount];
				for(int i=0;i<bandCount;i++){
					pBandMap[i]=i+1;
				}
				double []gt={0,0,0,0,0,0};
				pInputDataset.GetGeoTransform(gt);
				String strProjection=pInputDataset.GetProjectionRef();
				pInputDataset.ReadRaster(0, 0, xSize, ySize, xSize, ySize, gdalconstConstants.GDT_Int32, pInputBuf, pBandMap);
			     //	pInputDataset.delete();
				System.out.println("          读取完成");
				
				
				// 可选
				//gdal.GDALDestroyDriverManager();
				
				
				
				
				
				//第二步:   加载分类模型
				System.out.println("2）读取SVM模型");
				svm_model pModel = svm.svm_load_model(svmPath);
				svm_node []sn = new svm_node[bandCount+1];
				for( int i = 0; i < bandCount; i++)
				{   sn[i]=new svm_node();
					sn[i].index = i;
				}
				sn[bandCount]=new svm_node();
				sn[bandCount].index = -1;	// 结束符，代替波段数
				System.out.println("    读取完成");
				
				
				
				
				
				
				
				
				
				//第三步：应用分类模型：
				System.out.println("3）植被检测");
				//创建分类结果数据集
				int nPixelNum = xSize*ySize;
				//double []pDst = new double[nPixelNum];
				int []pDst = new int[nPixelNum];
				//int []pDst = new int[4000000];

				//植被检测
				int totalPixelNum = 0;
				int plantPixelNum = 0; 
				for( int i = 0; i < nPixelNum; i++ )
					//for( int i = 0; i < 4000000; i++ )
				{
					//计算无效点
					int inValidNum = 0;
					for( int j = 0; j < bandCount; j++ )
					{
						sn[j].value = (double)pInputBuf[j*nPixelNum+i];
						//if ( sn[j].value < MIN_VAL)
						//{
						//	inValidNum++;
						//}
					}

					if ( inValidNum != bandCount )
					{
						totalPixelNum++;
						//svm_predict( pModel, sn );
						pDst[i]=(int)svm.svm_predict(pModel,sn);
						//pDst[i] = (BYTE)svm_predict( pModel, sn );
						if ( pDst[i] == 1 )
						{
							plantPixelNum++;
						}
					}
					else
					{
						pDst[i] = 0;
					}
				}
				
				//计算植被含量百分比
				if( totalPixelNum > 0 )
				{
					double cloudContent = plantPixelNum*1.0/totalPixelNum;
					System.out.println("植被含量百分比    "+cloudContent);
				}
				
				
				
				
				//第四步：保存生成的分类文件
				System.out.println("4）保存生成的分类文件 ");
				//生成矢量文件;
				Driver drivers=pInputDataset.GetDriver();
				 String strDriverName = "ESRIShapefile";
		         org.gdal.ogr.Driver oDriver =ogr.GetDriverByName(strDriverName);
				//String classifyNamePath = "E:\\JAVA_neunn\\gdalTest\\source\\mask.tiff";
				Dataset pMaskDataSet =drivers.Create( classifySaveLocalPath, xSize,  ySize,1);
				pMaskDataSet.SetGeoTransform(gt);
				pMaskDataSet.SetProjection(strProjection);
				int [] pBandMap2=new int[1];
				for(int i=0;i<1;i++){
					pBandMap2[i]=i+1;
				}
				pMaskDataSet.WriteRaster(0, 0, xSize, ySize, xSize, ySize, gdalconstConstants.GDT_UInt32, pDst, pBandMap2);
				System.out.println("分类文件 完成   "+classifySaveLocalPath);
				//上传到HDFS上
				HdfsOperator.putFileToHdfs(classifySaveLocalPath, classifySaveHdfsPath);
	}

}
