package android.support.v4.graphics;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import java.util.Objects;

public final class ColorUtils {
  private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
  
  private static final int MIN_ALPHA_SEARCH_PRECISION = 1;
  
  private static final ThreadLocal<double[]> TEMP_ARRAY = new ThreadLocal();
  
  private static final double XYZ_EPSILON = 0.008856D;
  
  private static final double XYZ_KAPPA = 903.3D;
  
  private static final double XYZ_WHITE_REFERENCE_X = 95.047D;
  
  private static final double XYZ_WHITE_REFERENCE_Y = 100.0D;
  
  private static final double XYZ_WHITE_REFERENCE_Z = 108.883D;
  
  @ColorInt
  public static int HSLToColor(@NonNull float[] paramArrayOfFloat) {
    float f1 = paramArrayOfFloat[0];
    float f2 = paramArrayOfFloat[1];
    float f3 = paramArrayOfFloat[2];
    f2 = (1.0F - Math.abs(f3 * 2.0F - 1.0F)) * f2;
    f3 -= 0.5F * f2;
    float f4 = (1.0F - Math.abs(f1 / 60.0F % 2.0F - 1.0F)) * f2;
    int m = (int)f1 / 60;
    int i = 0;
    int j = 0;
    int k = 0;
    switch (m) {
      default:
        return Color.rgb(constrain(i, 0, 255), constrain(j, 0, 255), constrain(k, 0, 255));
      case 5:
      case 6:
        i = Math.round((f2 + f3) * 255.0F);
        j = Math.round(f3 * 255.0F);
        k = Math.round((f4 + f3) * 255.0F);
      case 4:
        i = Math.round((f4 + f3) * 255.0F);
        j = Math.round(f3 * 255.0F);
        k = Math.round((f2 + f3) * 255.0F);
      case 3:
        i = Math.round(f3 * 255.0F);
        j = Math.round((f4 + f3) * 255.0F);
        k = Math.round((f2 + f3) * 255.0F);
      case 2:
        i = Math.round(f3 * 255.0F);
        j = Math.round((f2 + f3) * 255.0F);
        k = Math.round((f4 + f3) * 255.0F);
      case 1:
        i = Math.round((f4 + f3) * 255.0F);
        j = Math.round((f2 + f3) * 255.0F);
        k = Math.round(255.0F * f3);
      case 0:
        break;
    } 
    i = Math.round((f2 + f3) * 255.0F);
    j = Math.round((f4 + f3) * 255.0F);
    k = Math.round(255.0F * f3);
  }
  
  @ColorInt
  public static int LABToColor(@FloatRange(from = 0.0D, to = 100.0D) double paramDouble1, @FloatRange(from = -128.0D, to = 127.0D) double paramDouble2, @FloatRange(from = -128.0D, to = 127.0D) double paramDouble3) {
    double[] arrayOfDouble = getTempDouble3Array();
    LABToXYZ(paramDouble1, paramDouble2, paramDouble3, arrayOfDouble);
    return XYZToColor(arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2]);
  }
  
  public static void LABToXYZ(@FloatRange(from = 0.0D, to = 100.0D) double paramDouble1, @FloatRange(from = -128.0D, to = 127.0D) double paramDouble2, @FloatRange(from = -128.0D, to = 127.0D) double paramDouble3, @NonNull double[] paramArrayOfDouble) {
    double d2 = (paramDouble1 + 16.0D) / 116.0D;
    double d3 = paramDouble2 / 500.0D + d2;
    double d1 = d2 - paramDouble3 / 200.0D;
    paramDouble2 = Math.pow(d3, 3.0D);
    if (paramDouble2 <= 0.008856D)
      paramDouble2 = (d3 * 116.0D - 16.0D) / 903.3D; 
    if (paramDouble1 > 7.9996247999999985D) {
      paramDouble1 = Math.pow(d2, 3.0D);
    } else {
      paramDouble1 /= 903.3D;
    } 
    paramDouble3 = Math.pow(d1, 3.0D);
    if (paramDouble3 <= 0.008856D)
      paramDouble3 = (116.0D * d1 - 16.0D) / 903.3D; 
    paramArrayOfDouble[0] = 95.047D * paramDouble2;
    paramArrayOfDouble[1] = 100.0D * paramDouble1;
    paramArrayOfDouble[2] = 108.883D * paramDouble3;
  }
  
  public static void RGBToHSL(@IntRange(from = 0L, to = 255L) int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2, @IntRange(from = 0L, to = 255L) int paramInt3, @NonNull float[] paramArrayOfFloat) {
    float f1 = paramInt1 / 255.0F;
    float f3 = paramInt2 / 255.0F;
    float f5 = paramInt3 / 255.0F;
    float f6 = Math.max(f1, Math.max(f3, f5));
    float f7 = Math.min(f1, Math.min(f3, f5));
    float f2 = f6 - f7;
    float f4 = (f6 + f7) / 2.0F;
    if (f6 == f7) {
      f1 = 0.0F;
      f2 = 0.0F;
    } else {
      if (f6 == f1) {
        f1 = (f3 - f5) / f2 % 6.0F;
      } else if (f6 == f3) {
        f1 = (f5 - f1) / f2 + 2.0F;
      } else {
        f1 = (f1 - f3) / f2 + 4.0F;
      } 
      f3 = f2 / (1.0F - Math.abs(2.0F * f4 - 1.0F));
      f2 = f1;
      f1 = f3;
    } 
    f3 = 60.0F * f2 % 360.0F;
    f2 = f3;
    if (f3 < 0.0F)
      f2 = f3 + 360.0F; 
    paramArrayOfFloat[0] = constrain(f2, 0.0F, 360.0F);
    paramArrayOfFloat[1] = constrain(f1, 0.0F, 1.0F);
    paramArrayOfFloat[2] = constrain(f4, 0.0F, 1.0F);
  }
  
  public static void RGBToLAB(@IntRange(from = 0L, to = 255L) int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2, @IntRange(from = 0L, to = 255L) int paramInt3, @NonNull double[] paramArrayOfDouble) {
    RGBToXYZ(paramInt1, paramInt2, paramInt3, paramArrayOfDouble);
    XYZToLAB(paramArrayOfDouble[0], paramArrayOfDouble[1], paramArrayOfDouble[2], paramArrayOfDouble);
  }
  
  public static void RGBToXYZ(@IntRange(from = 0L, to = 255L) int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2, @IntRange(from = 0L, to = 255L) int paramInt3, @NonNull double[] paramArrayOfDouble) {
    if (paramArrayOfDouble.length == 3) {
      double d1 = paramInt1;
      Double.isNaN(d1);
      d1 /= 255.0D;
      if (d1 < 0.04045D) {
        d1 /= 12.92D;
      } else {
        d1 = Math.pow((d1 + 0.055D) / 1.055D, 2.4D);
      } 
      double d2 = paramInt2;
      Double.isNaN(d2);
      d2 /= 255.0D;
      if (d2 < 0.04045D) {
        d2 /= 12.92D;
      } else {
        d2 = Math.pow((d2 + 0.055D) / 1.055D, 2.4D);
      } 
      double d3 = paramInt3;
      Double.isNaN(d3);
      d3 /= 255.0D;
      if (d3 < 0.04045D) {
        d3 /= 12.92D;
      } else {
        d3 = Math.pow((0.055D + d3) / 1.055D, 2.4D);
      } 
      paramArrayOfDouble[0] = (0.4124D * d1 + 0.3576D * d2 + 0.1805D * d3) * 100.0D;
      paramArrayOfDouble[1] = (0.2126D * d1 + 0.7152D * d2 + 0.0722D * d3) * 100.0D;
      paramArrayOfDouble[2] = (0.0193D * d1 + 0.1192D * d2 + 0.9505D * d3) * 100.0D;
      return;
    } 
    throw new IllegalArgumentException("outXyz must have a length of 3.");
  }
  
  @ColorInt
  public static int XYZToColor(@FloatRange(from = 0.0D, to = 95.047D) double paramDouble1, @FloatRange(from = 0.0D, to = 100.0D) double paramDouble2, @FloatRange(from = 0.0D, to = 108.883D) double paramDouble3) {
    double d2 = (3.2406D * paramDouble1 + -1.5372D * paramDouble2 + -0.4986D * paramDouble3) / 100.0D;
    double d1 = (-0.9689D * paramDouble1 + 1.8758D * paramDouble2 + 0.0415D * paramDouble3) / 100.0D;
    paramDouble3 = (0.0557D * paramDouble1 + -0.204D * paramDouble2 + 1.057D * paramDouble3) / 100.0D;
    if (d2 > 0.0031308D) {
      paramDouble1 = Math.pow(d2, 0.4166666666666667D) * 1.055D - 0.055D;
    } else {
      paramDouble1 = d2 * 12.92D;
    } 
    if (d1 > 0.0031308D) {
      paramDouble2 = Math.pow(d1, 0.4166666666666667D) * 1.055D - 0.055D;
    } else {
      paramDouble2 = d1 * 12.92D;
    } 
    if (paramDouble3 > 0.0031308D) {
      paramDouble3 = Math.pow(paramDouble3, 0.4166666666666667D) * 1.055D - 0.055D;
    } else {
      paramDouble3 *= 12.92D;
    } 
    return Color.rgb(constrain((int)Math.round(paramDouble1 * 255.0D), 0, 255), constrain((int)Math.round(paramDouble2 * 255.0D), 0, 255), constrain((int)Math.round(255.0D * paramDouble3), 0, 255));
  }
  
  public static void XYZToLAB(@FloatRange(from = 0.0D, to = 95.047D) double paramDouble1, @FloatRange(from = 0.0D, to = 100.0D) double paramDouble2, @FloatRange(from = 0.0D, to = 108.883D) double paramDouble3, @NonNull double[] paramArrayOfDouble) {
    if (paramArrayOfDouble.length == 3) {
      paramDouble1 = pivotXyzComponent(paramDouble1 / 95.047D);
      paramDouble2 = pivotXyzComponent(paramDouble2 / 100.0D);
      paramDouble3 = pivotXyzComponent(paramDouble3 / 108.883D);
      paramArrayOfDouble[0] = Math.max(0.0D, 116.0D * paramDouble2 - 16.0D);
      paramArrayOfDouble[1] = (paramDouble1 - paramDouble2) * 500.0D;
      paramArrayOfDouble[2] = (paramDouble2 - paramDouble3) * 200.0D;
      return;
    } 
    throw new IllegalArgumentException("outLab must have a length of 3.");
  }
  
  @ColorInt
  public static int blendARGB(@ColorInt int paramInt1, @ColorInt int paramInt2, @FloatRange(from = 0.0D, to = 1.0D) float paramFloat) {
    float f1 = 1.0F - paramFloat;
    float f2 = Color.alpha(paramInt1);
    float f3 = Color.alpha(paramInt2);
    float f4 = Color.red(paramInt1);
    float f5 = Color.red(paramInt2);
    float f6 = Color.green(paramInt1);
    float f7 = Color.green(paramInt2);
    float f8 = Color.blue(paramInt1);
    float f9 = Color.blue(paramInt2);
    return Color.argb((int)(f2 * f1 + f3 * paramFloat), (int)(f4 * f1 + f5 * paramFloat), (int)(f6 * f1 + f7 * paramFloat), (int)(f8 * f1 + f9 * paramFloat));
  }
  
  public static void blendHSL(@NonNull float[] paramArrayOfFloat1, @NonNull float[] paramArrayOfFloat2, @FloatRange(from = 0.0D, to = 1.0D) float paramFloat, @NonNull float[] paramArrayOfFloat3) {
    if (paramArrayOfFloat3.length == 3) {
      float f = 1.0F - paramFloat;
      paramArrayOfFloat3[0] = circularInterpolate(paramArrayOfFloat1[0], paramArrayOfFloat2[0], paramFloat);
      paramArrayOfFloat3[1] = paramArrayOfFloat1[1] * f + paramArrayOfFloat2[1] * paramFloat;
      paramArrayOfFloat3[2] = paramArrayOfFloat1[2] * f + paramArrayOfFloat2[2] * paramFloat;
      return;
    } 
    throw new IllegalArgumentException("result must have a length of 3.");
  }
  
  public static void blendLAB(@NonNull double[] paramArrayOfDouble1, @NonNull double[] paramArrayOfDouble2, @FloatRange(from = 0.0D, to = 1.0D) double paramDouble, @NonNull double[] paramArrayOfDouble3) {
    if (paramArrayOfDouble3.length == 3) {
      double d = 1.0D - paramDouble;
      paramArrayOfDouble3[0] = paramArrayOfDouble1[0] * d + paramArrayOfDouble2[0] * paramDouble;
      paramArrayOfDouble3[1] = paramArrayOfDouble1[1] * d + paramArrayOfDouble2[1] * paramDouble;
      paramArrayOfDouble3[2] = paramArrayOfDouble1[2] * d + paramArrayOfDouble2[2] * paramDouble;
      return;
    } 
    throw new IllegalArgumentException("outResult must have a length of 3.");
  }
  
  public static double calculateContrast(@ColorInt int paramInt1, @ColorInt int paramInt2) {
    if (Color.alpha(paramInt2) == 255) {
      int i = paramInt1;
      if (Color.alpha(paramInt1) < 255)
        i = compositeColors(paramInt1, paramInt2); 
      double d1 = calculateLuminance(i) + 0.05D;
      double d2 = calculateLuminance(paramInt2) + 0.05D;
      return Math.max(d1, d2) / Math.min(d1, d2);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("background can not be translucent: #");
    stringBuilder.append(Integer.toHexString(paramInt2));
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @FloatRange(from = 0.0D, to = 1.0D)
  public static double calculateLuminance(@ColorInt int paramInt) {
    double[] arrayOfDouble = getTempDouble3Array();
    colorToXYZ(paramInt, arrayOfDouble);
    return arrayOfDouble[1] / 100.0D;
  }
  
  public static int calculateMinimumAlpha(@ColorInt int paramInt1, @ColorInt int paramInt2, float paramFloat) {
    if (Color.alpha(paramInt2) == 255) {
      if (calculateContrast(setAlphaComponent(paramInt1, 255), paramInt2) < paramFloat)
        return -1; 
      byte b = 0;
      char c2 = Character.MIN_VALUE;
      char c1 = 'ÿ';
      while (b <= 10 && c1 - c2 > '\001') {
        char c = (c2 + c1) / '\002';
        if (calculateContrast(setAlphaComponent(paramInt1, c), paramInt2) < paramFloat) {
          c2 = c;
        } else {
          c1 = c;
        } 
        b++;
      } 
      return c1;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("background can not be translucent: #");
    stringBuilder.append(Integer.toHexString(paramInt2));
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @VisibleForTesting
  static float circularInterpolate(float paramFloat1, float paramFloat2, float paramFloat3) {
    float f1 = paramFloat1;
    float f2 = paramFloat2;
    if (Math.abs(paramFloat2 - paramFloat1) > 180.0F)
      if (paramFloat2 > paramFloat1) {
        f1 = paramFloat1 + 360.0F;
        f2 = paramFloat2;
      } else {
        f2 = paramFloat2 + 360.0F;
        f1 = paramFloat1;
      }  
    return ((f2 - f1) * paramFloat3 + f1) % 360.0F;
  }
  
  public static void colorToHSL(@ColorInt int paramInt, @NonNull float[] paramArrayOfFloat) { RGBToHSL(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOfFloat); }
  
  public static void colorToLAB(@ColorInt int paramInt, @NonNull double[] paramArrayOfDouble) { RGBToLAB(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOfDouble); }
  
  public static void colorToXYZ(@ColorInt int paramInt, @NonNull double[] paramArrayOfDouble) { RGBToXYZ(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOfDouble); }
  
  private static int compositeAlpha(int paramInt1, int paramInt2) { return 255 - (255 - paramInt2) * (255 - paramInt1) / 255; }
  
  public static int compositeColors(@ColorInt int paramInt1, @ColorInt int paramInt2) {
    int i = Color.alpha(paramInt2);
    int j = Color.alpha(paramInt1);
    int k = compositeAlpha(j, i);
    return Color.argb(k, compositeComponent(Color.red(paramInt1), j, Color.red(paramInt2), i, k), compositeComponent(Color.green(paramInt1), j, Color.green(paramInt2), i, k), compositeComponent(Color.blue(paramInt1), j, Color.blue(paramInt2), i, k));
  }
  
  @NonNull
  @RequiresApi(26)
  public static Color compositeColors(@NonNull Color paramColor1, @NonNull Color paramColor2) {
    if (Objects.equals(paramColor1.getModel(), paramColor2.getModel())) {
      if (!Objects.equals(paramColor2.getColorSpace(), paramColor1.getColorSpace()))
        paramColor1 = paramColor1.convert(paramColor2.getColorSpace()); 
      float[] arrayOfFloat1 = paramColor1.getComponents();
      float[] arrayOfFloat2 = paramColor2.getComponents();
      float f4 = paramColor1.alpha();
      float f3 = paramColor2.alpha() * (1.0F - f4);
      int i = paramColor2.getComponentCount() - 1;
      arrayOfFloat2[i] = f4 + f3;
      float f2 = f4;
      float f1 = f3;
      if (arrayOfFloat2[i] > 0.0F) {
        f2 = f4 / arrayOfFloat2[i];
        f1 = f3 / arrayOfFloat2[i];
      } 
      byte b;
      for (b = 0; b < i; b++)
        arrayOfFloat2[b] = arrayOfFloat1[b] * f2 + arrayOfFloat2[b] * f1; 
      return Color.valueOf(arrayOfFloat2, paramColor2.getColorSpace());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Color models must match (");
    stringBuilder.append(paramColor1.getModel());
    stringBuilder.append(" vs. ");
    stringBuilder.append(paramColor2.getModel());
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private static int compositeComponent(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) { return (paramInt5 == 0) ? 0 : ((paramInt1 * 255 * paramInt2 + paramInt3 * paramInt4 * (255 - paramInt2)) / paramInt5 * 255); }
  
  private static float constrain(float paramFloat1, float paramFloat2, float paramFloat3) { return (paramFloat1 < paramFloat2) ? paramFloat2 : ((paramFloat1 > paramFloat3) ? paramFloat3 : paramFloat1); }
  
  private static int constrain(int paramInt1, int paramInt2, int paramInt3) { return (paramInt1 < paramInt2) ? paramInt2 : ((paramInt1 > paramInt3) ? paramInt3 : paramInt1); }
  
  public static double distanceEuclidean(@NonNull double[] paramArrayOfDouble1, @NonNull double[] paramArrayOfDouble2) { return Math.sqrt(Math.pow(paramArrayOfDouble1[0] - paramArrayOfDouble2[0], 2.0D) + Math.pow(paramArrayOfDouble1[1] - paramArrayOfDouble2[1], 2.0D) + Math.pow(paramArrayOfDouble1[2] - paramArrayOfDouble2[2], 2.0D)); }
  
  private static double[] getTempDouble3Array() {
    double[] arrayOfDouble2 = (double[])TEMP_ARRAY.get();
    double[] arrayOfDouble1 = arrayOfDouble2;
    if (arrayOfDouble2 == null) {
      arrayOfDouble1 = new double[3];
      TEMP_ARRAY.set(arrayOfDouble1);
    } 
    return arrayOfDouble1;
  }
  
  private static double pivotXyzComponent(double paramDouble) { return (paramDouble > 0.008856D) ? Math.pow(paramDouble, 0.3333333333333333D) : ((903.3D * paramDouble + 16.0D) / 116.0D); }
  
  @ColorInt
  public static int setAlphaComponent(@ColorInt int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2) {
    if (paramInt2 >= 0 && paramInt2 <= 255)
      return 0xFFFFFF & paramInt1 | paramInt2 << 24; 
    throw new IllegalArgumentException("alpha must be between 0 and 255.");
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\graphics\ColorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */