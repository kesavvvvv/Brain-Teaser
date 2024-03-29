package android.support.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.animation.Interpolator;
import org.xmlpull.v1.XmlPullParser;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class PathInterpolatorCompat implements Interpolator {
  public static final double EPSILON = 1.0E-5D;
  
  public static final int MAX_NUM_POINTS = 3000;
  
  private static final float PRECISION = 0.002F;
  
  private float[] mX;
  
  private float[] mY;
  
  public PathInterpolatorCompat(Context paramContext, AttributeSet paramAttributeSet, XmlPullParser paramXmlPullParser) { this(paramContext.getResources(), paramContext.getTheme(), paramAttributeSet, paramXmlPullParser); }
  
  public PathInterpolatorCompat(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, XmlPullParser paramXmlPullParser) {
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_PATH_INTERPOLATOR);
    parseInterpolatorFromTypeArray(typedArray, paramXmlPullParser);
    typedArray.recycle();
  }
  
  private void initCubic(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    Path path = new Path();
    path.moveTo(0.0F, 0.0F);
    path.cubicTo(paramFloat1, paramFloat2, paramFloat3, paramFloat4, 1.0F, 1.0F);
    initPath(path);
  }
  
  private void initPath(Path paramPath) {
    PathMeasure pathMeasure = new PathMeasure(paramPath, false);
    float f = pathMeasure.getLength();
    int i = Math.min(3000, (int)(f / 0.002F) + 1);
    if (i > 0) {
      this.mX = new float[i];
      this.mY = new float[i];
      float[] arrayOfFloat = new float[2];
      byte b;
      for (b = 0; b < i; b++) {
        pathMeasure.getPosTan(b * f / (i - 1), arrayOfFloat, null);
        this.mX[b] = arrayOfFloat[0];
        this.mY[b] = arrayOfFloat[1];
      } 
      if (Math.abs(this.mX[0]) <= 1.0E-5D && Math.abs(this.mY[0]) <= 1.0E-5D && Math.abs(this.mX[i - 1] - 1.0F) <= 1.0E-5D && Math.abs(this.mY[i - 1] - 1.0F) <= 1.0E-5D) {
        StringBuilder stringBuilder2;
        f = 0.0F;
        b = 0;
        byte b1 = 0;
        while (b1 < i) {
          arrayOfFloat = this.mX;
          float f1 = arrayOfFloat[b];
          if (f1 >= f) {
            arrayOfFloat[b1] = f1;
            f = f1;
            b1++;
            b++;
            continue;
          } 
          stringBuilder2 = new StringBuilder();
          stringBuilder2.append("The Path cannot loop back on itself, x :");
          stringBuilder2.append(f1);
          throw new IllegalArgumentException(stringBuilder2.toString());
        } 
        if (!stringBuilder2.nextContour())
          return; 
        throw new IllegalArgumentException("The Path should be continuous, can't have 2+ contours");
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("The Path must start at (0,0) and end at (1,1) start: ");
      stringBuilder1.append(this.mX[0]);
      stringBuilder1.append(",");
      stringBuilder1.append(this.mY[0]);
      stringBuilder1.append(" end:");
      stringBuilder1.append(this.mX[i - 1]);
      stringBuilder1.append(",");
      stringBuilder1.append(this.mY[i - 1]);
      throw new IllegalArgumentException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("The Path has a invalid length ");
    stringBuilder.append(f);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private void initQuad(float paramFloat1, float paramFloat2) {
    Path path = new Path();
    path.moveTo(0.0F, 0.0F);
    path.quadTo(paramFloat1, paramFloat2, 1.0F, 1.0F);
    initPath(path);
  }
  
  private void parseInterpolatorFromTypeArray(TypedArray paramTypedArray, XmlPullParser paramXmlPullParser) {
    StringBuilder stringBuilder;
    String str;
    if (TypedArrayUtils.hasAttribute(paramXmlPullParser, "pathData")) {
      str = TypedArrayUtils.getNamedString(paramTypedArray, paramXmlPullParser, "pathData", 4);
      Path path = PathParser.createPathFromPathData(str);
      if (path != null) {
        initPath(path);
        return;
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("The path is null, which is created from ");
      stringBuilder.append(str);
      throw new InflateException(stringBuilder.toString());
    } 
    if (TypedArrayUtils.hasAttribute(stringBuilder, "controlX1")) {
      if (TypedArrayUtils.hasAttribute(stringBuilder, "controlY1")) {
        float f1 = TypedArrayUtils.getNamedFloat(str, stringBuilder, "controlX1", 0, 0.0F);
        float f2 = TypedArrayUtils.getNamedFloat(str, stringBuilder, "controlY1", 1, 0.0F);
        boolean bool = TypedArrayUtils.hasAttribute(stringBuilder, "controlX2");
        if (bool == TypedArrayUtils.hasAttribute(stringBuilder, "controlY2")) {
          if (!bool) {
            initQuad(f1, f2);
            return;
          } 
          initCubic(f1, f2, TypedArrayUtils.getNamedFloat(str, stringBuilder, "controlX2", 2, 0.0F), TypedArrayUtils.getNamedFloat(str, stringBuilder, "controlY2", 3, 0.0F));
          return;
        } 
        throw new InflateException("pathInterpolator requires both controlX2 and controlY2 for cubic Beziers.");
      } 
      throw new InflateException("pathInterpolator requires the controlY1 attribute");
    } 
    throw new InflateException("pathInterpolator requires the controlX1 attribute");
  }
  
  public float getInterpolation(float paramFloat) {
    if (paramFloat <= 0.0F)
      return 0.0F; 
    if (paramFloat >= 1.0F)
      return 1.0F; 
    int i = 0;
    int j = this.mX.length - 1;
    while (j - i > 1) {
      int k = (i + j) / 2;
      if (paramFloat < this.mX[k]) {
        j = k;
        continue;
      } 
      i = k;
    } 
    float[] arrayOfFloat = this.mX;
    float f = arrayOfFloat[j] - arrayOfFloat[i];
    if (f == 0.0F)
      return this.mY[i]; 
    paramFloat = (paramFloat - arrayOfFloat[i]) / f;
    arrayOfFloat = this.mY;
    f = arrayOfFloat[i];
    return (arrayOfFloat[j] - f) * paramFloat + f;
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\graphics\drawable\PathInterpolatorCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */