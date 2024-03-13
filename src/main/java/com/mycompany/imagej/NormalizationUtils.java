package com.mycompany.imagej;

import net.imglib2.Cursor;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import java.util.Arrays;

public class NormalizationUtils {
    private static int img_num=1;

    public static void setImg_num(int img_num) {
        NormalizationUtils.img_num = img_num;
    }

    public static float[] normalizeMiMa(float[] x, float mi, float ma, boolean clip, float eps) {
        // In Java, there's no direct equivalent of numexpr, so we'll use a simple loop for the calculation.
        System.out.println("IMAGE["+(img_num)+"] upper bound of pixel value："+ma);
        System.out.println("IMAGE["+(img_num++)+"] lower bound of pixel value："+mi);
        for (int i = 0; i < x.length; i++) {
            x[i] = (x[i] - mi) / (ma - mi + eps);
        }
        if (clip) {
            for (int i = 0; i < x.length; i++) {
                x[i] = Math.max(0, Math.min(x[i], 1));
            }
        }
        return x;
    }

    public static float[] normalizePercentage(float[] x, float pmin, float pmax,  boolean clip, float eps) {
        // Java doesn't have a direct equivalent of numpy's percentile method, so you'd need to implement it or use a third-party library.
        // Here's a simple implementation for a flat array.
        float mi = percentile(x, pmin);
        float ma = percentile(x, pmax);

        return normalizeMiMa(x, mi, ma, clip, eps);
    }
//@Add(overview = "选取p/100小的数据")
    private static float percentile(float[] arr, float p) {
        float[] copy = Arrays.copyOf(arr, arr.length);
        Arrays.sort(copy);
        int index = (int) Math.ceil(p / 100.0 * (arr.length - 1));
        return copy[index];
    }
//@Add(ToBeMention = "可接受任意维度数据",overview = "img转换到1维数组")
    public static <T extends RealType<T>> float[] Img2Float1D(RandomAccessibleInterval<T> img) {
        int PixSize = 1;
        for (int i = 0; i < img.numDimensions(); i++) {
            PixSize *= img.dimension(i);
        }
        float[] res = new float[PixSize];
        int i = 0;

        Cursor<T> cursor = Views.iterable(img).cursor();
        while (cursor.hasNext()) {
            cursor.fwd();
            res[i++] = cursor.get().getRealFloat();
        }
        return res;
    }
    //dimensions是img的shape eg:[100,123]
//    @Add(ToBeMention = "可接受任意维度数据",overview = "1维数组到img")
    public static RandomAccessibleInterval<FloatType> Float1D2Img(float[] array, long... dimensions) {
        // 创建一个与给定维度匹配的新RandomAccessibleInterval<FloatType>
        RandomAccessibleInterval<FloatType> img = ArrayImgs.floats(dimensions);
        int i=0;
        // 将一维数组的值填充到RandomAccessibleInterval<FloatType>
        Cursor<FloatType> cursor = Views.iterable(img).cursor();
        while (cursor.hasNext()) {
            cursor.fwd();
            cursor.get().setReal(array[i++]);
        }

        return img;
    }
}
