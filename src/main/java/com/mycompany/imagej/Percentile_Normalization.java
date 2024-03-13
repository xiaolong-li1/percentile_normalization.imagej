package com.mycompany.imagej;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;
import org.scijava.app.StatusService;
import java.awt.*;
import ij.ImagePlus;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
@Plugin(type = Command.class, menuPath = "Plugins>Percentile_Normalization")
public class Percentile_Normalization<T extends RealType<T>> implements Command  {
    @Parameter
    private Dataset currentData;
//    @Parameter(label="eps")
    private float eps=1e-8F;
    @Parameter
    StatusService statusService;
    @Parameter(label = "min_Percentile",persist = true)
    private float mi=10F;
    @Parameter(label = "max_Percentile")
    private float ma= 99.8F;
//    @Parameter(label = "clip")
    private boolean clip=true;
    @Parameter
    private UIService uiService;
    @Parameter(description = "从图片流找min max为3D;从单张图片找为2D",choices = {"2D","3D"})
    private String deminsion;

    @Parameter
    private OpService opService;
    @Override
    public void run() {
        final Img<T> image = (Img<T>)currentData.getImgPlus();
        if(deminsion.equals("2D")&&image.numDimensions()==3){
            System.out.println("View the data as 2D img(s) to manipulate dependently");
            List<RandomAccessibleInterval<FloatType>> results = new ArrayList<>();
            RandomAccessibleInterval<FloatType> res =new ArrayImgFactory<>(new FloatType()).create(10,10,10);;
            for(int i=0;i<image.dimension(2);i++){
                RandomAccessibleInterval<T> temp=Views.addDimension(Views.hyperSlice(image,2,i),0,0);
                final long[] shape=new long[temp.numDimensions()];
                for(int j=0;j< temp.numDimensions();j++){
                    shape[j]= temp.dimension(j);
                }
                float[] ImgFloat=NormalizationUtils.Img2Float1D(temp);
                float[] temp_res=NormalizationUtils.normalizePercentage(ImgFloat,mi,ma,clip,eps);
                RandomAccessibleInterval<FloatType> img=NormalizationUtils.Float1D2Img(temp_res,shape);
                results.add(img);
            }
            boolean flag=false;
            int num=0;
            for(RandomAccessibleInterval<FloatType> elem : results){
                if(num>10)
                    break;
                num++;
                if(flag==false){
                    res=elem;
                    flag=true;
                    continue;
                }
                res=Views.concatenate(2,res,elem);
            }
            ImagePlus imp = ImageJFunctions.wrap(res, "image");
//                final long[] shape=new long[res.numDimensions()];
//                for(int j=0;j< res.numDimensions();j++){
//                    shape[j]= res.dimension(j);
//                }
//                float[] ImgFloat=NormalizationUtils.Img2Float1D(res);
//                res=NormalizationUtils.Float1D2Img(ImgFloat,shape);
//                Img imp = (Img)res;
//                System.out.println("Gathering the generated data");

            uiService.show("preview",imp);

        }else{List<RandomAccessibleInterval<FloatType>> results = new ArrayList<>();
            System.out.println("View the data as a whole to manipulate");
            final long[] shape=new long[image.numDimensions()];
            for(int i=0;i< image.numDimensions();i++){
                shape[i]= image.dimension(i);
            }
          float[] ImgFloat=NormalizationUtils.Img2Float1D(image);
          float[] temp_res=NormalizationUtils.normalizePercentage(ImgFloat,mi,ma,clip,eps);
          RandomAccessibleInterval<FloatType> img=NormalizationUtils.Float1D2Img(temp_res,shape);
          results.add(img);
          for (RandomAccessibleInterval<FloatType> elem : results) {
                uiService.show("preview",elem);
          }
        statusService.clearStatus();
        }
        NormalizationUtils.setImg_num(1);
    }

    /**
     * This main function serves for development purposes.
     * It allows you to run the plugin immediately out of
     * your integrated development environment (IDE).
     *
     * @param args whatever, it's ignored
     * @throws Exception
     */
    public static void main(final String... args) throws Exception {
        // create the ImageJ application context with all available services
        final ImageJ ij = new ImageJ();
        ij.ui().showUI();
        File file_=new File("D:\\SR\\Data\\3Ddata\\temporal data\\c1-405-2.tif");
//        File file_=new File("C:\\Users\\32081\\Pictures\\微信图片_20240227090004.png");
        // ask the user for a file to open
//        final File file = ij.ui().chooseFile(file_, "open")
        if (file_ != null) {
            // load the dataset
            final Dataset dataset = ij.scifio().datasetIO().open(file_.getPath());
            // show the image
            ij.ui().show(dataset);
            ij.command().run(Percentile_Normalization.class,true);
        }
    }
}
