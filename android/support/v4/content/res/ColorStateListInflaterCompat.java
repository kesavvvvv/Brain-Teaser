package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.compat.R;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public final class ColorStateListInflaterCompat {
  private static final int DEFAULT_COLOR = -65536;
  
  @NonNull
  public static ColorStateList createFromXml(@NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @Nullable Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    int i;
    AttributeSet attributeSet = Xml.asAttributeSet(paramXmlPullParser);
    while (true) {
      i = paramXmlPullParser.next();
      if (i != 2 && i != 1)
        continue; 
      break;
    } 
    if (i == 2)
      return createFromXmlInner(paramResources, paramXmlPullParser, attributeSet, paramTheme); 
    throw new XmlPullParserException("No start tag found");
  }
  
  @NonNull
  public static ColorStateList createFromXmlInner(@NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    String str = paramXmlPullParser.getName();
    if (str.equals("selector"))
      return inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramXmlPullParser.getPositionDescription());
    stringBuilder.append(": invalid color state list tag ");
    stringBuilder.append(str);
    throw new XmlPullParserException(stringBuilder.toString());
  }
  
  private static ColorStateList inflate(@NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    int j = paramXmlPullParser.getDepth() + 1;
    int i = -65536;
    int[][] arrayOfInt3 = new int[20][];
    int[] arrayOfInt4 = new int[arrayOfInt3.length];
    byte b = 0;
    while (true) {
      int k = paramXmlPullParser.next();
      if (k != 1) {
        int m = paramXmlPullParser.getDepth();
        if (m >= j || k != 3) {
          if (k != 2 || m > j || !paramXmlPullParser.getName().equals("item"))
            continue; 
          TypedArray typedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.ColorStateListItem);
          int n = typedArray.getColor(R.styleable.ColorStateListItem_android_color, -65281);
          float f = 1.0F;
          if (typedArray.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
            f = typedArray.getFloat(R.styleable.ColorStateListItem_android_alpha, 1.0F);
          } else if (typedArray.hasValue(R.styleable.ColorStateListItem_alpha)) {
            f = typedArray.getFloat(R.styleable.ColorStateListItem_alpha, 1.0F);
          } 
          typedArray.recycle();
          m = paramAttributeSet.getAttributeCount();
          int[] arrayOfInt = new int[m];
          k = 0;
          byte b1 = 0;
          while (b1 < m) {
            int i2 = paramAttributeSet.getAttributeNameResource(b1);
            int i1 = k;
            if (i2 != 16843173) {
              i1 = k;
              if (i2 != 16843551) {
                i1 = k;
                if (i2 != R.attr.alpha) {
                  if (paramAttributeSet.getAttributeBooleanValue(b1, false)) {
                    i1 = i2;
                  } else {
                    i1 = -i2;
                  } 
                  arrayOfInt[k] = i1;
                  i1 = k + 1;
                } 
              } 
            } 
            b1++;
            k = i1;
          } 
          arrayOfInt = StateSet.trimStateSet(arrayOfInt, k);
          k = modulateColorAlpha(n, f);
          if (!b || arrayOfInt.length == 0)
            i = k; 
          arrayOfInt4 = GrowingArrayUtils.append(arrayOfInt4, b, k);
          arrayOfInt3 = (int[][])GrowingArrayUtils.append(arrayOfInt3, b, arrayOfInt);
          b++;
          continue;
        } 
      } 
      break;
    } 
    int[] arrayOfInt1 = new int[b];
    int[][] arrayOfInt2 = new int[b][];
    System.arraycopy(arrayOfInt4, 0, arrayOfInt1, 0, b);
    System.arraycopy(arrayOfInt3, 0, arrayOfInt2, 0, b);
    return new ColorStateList(arrayOfInt2, arrayOfInt1);
  }
  
  @ColorInt
  private static int modulateColorAlpha(@ColorInt int paramInt, @FloatRange(from = 0.0D, to = 1.0D) float paramFloat) { return 0xFFFFFF & paramInt | Math.round(Color.alpha(paramInt) * paramFloat) << 24; }
  
  private static TypedArray obtainAttributes(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, int[] paramArrayOfInt) { return (paramTheme == null) ? paramResources.obtainAttributes(paramAttributeSet, paramArrayOfInt) : paramTheme.obtainStyledAttributes(paramAttributeSet, paramArrayOfInt, 0, 0); }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\content\res\ColorStateListInflaterCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */