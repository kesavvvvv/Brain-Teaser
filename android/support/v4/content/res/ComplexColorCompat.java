package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public final class ComplexColorCompat {
  private static final String LOG_TAG = "ComplexColorCompat";
  
  private int mColor;
  
  private final ColorStateList mColorStateList;
  
  private final Shader mShader;
  
  private ComplexColorCompat(Shader paramShader, ColorStateList paramColorStateList, @ColorInt int paramInt) {
    this.mShader = paramShader;
    this.mColorStateList = paramColorStateList;
    this.mColor = paramInt;
  }
  
  @NonNull
  private static ComplexColorCompat createFromXml(@NonNull Resources paramResources, @ColorRes int paramInt, @Nullable Resources.Theme paramTheme) throws IOException, XmlPullParserException { // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: invokevirtual getXml : (I)Landroid/content/res/XmlResourceParser;
    //   5: astore #4
    //   7: aload #4
    //   9: invokestatic asAttributeSet : (Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   12: astore #6
    //   14: aload #4
    //   16: invokeinterface next : ()I
    //   21: istore_3
    //   22: iconst_1
    //   23: istore_1
    //   24: iload_3
    //   25: iconst_2
    //   26: if_icmpeq -> 37
    //   29: iload_3
    //   30: iconst_1
    //   31: if_icmpeq -> 37
    //   34: goto -> 14
    //   37: iload_3
    //   38: iconst_2
    //   39: if_icmpne -> 196
    //   42: aload #4
    //   44: invokeinterface getName : ()Ljava/lang/String;
    //   49: astore #5
    //   51: aload #5
    //   53: invokevirtual hashCode : ()I
    //   56: istore_3
    //   57: iload_3
    //   58: ldc 89650992
    //   60: if_icmpeq -> 87
    //   63: iload_3
    //   64: ldc 1191572447
    //   66: if_icmpeq -> 72
    //   69: goto -> 100
    //   72: aload #5
    //   74: ldc 'selector'
    //   76: invokevirtual equals : (Ljava/lang/Object;)Z
    //   79: ifeq -> 69
    //   82: iconst_0
    //   83: istore_1
    //   84: goto -> 102
    //   87: aload #5
    //   89: ldc 'gradient'
    //   91: invokevirtual equals : (Ljava/lang/Object;)Z
    //   94: ifeq -> 69
    //   97: goto -> 102
    //   100: iconst_m1
    //   101: istore_1
    //   102: iload_1
    //   103: tableswitch default -> 124, 0 -> 183, 1 -> 170
    //   124: new java/lang/StringBuilder
    //   127: dup
    //   128: invokespecial <init> : ()V
    //   131: astore_0
    //   132: aload_0
    //   133: aload #4
    //   135: invokeinterface getPositionDescription : ()Ljava/lang/String;
    //   140: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   143: pop
    //   144: aload_0
    //   145: ldc ': unsupported complex color tag '
    //   147: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   150: pop
    //   151: aload_0
    //   152: aload #5
    //   154: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   157: pop
    //   158: new org/xmlpull/v1/XmlPullParserException
    //   161: dup
    //   162: aload_0
    //   163: invokevirtual toString : ()Ljava/lang/String;
    //   166: invokespecial <init> : (Ljava/lang/String;)V
    //   169: athrow
    //   170: aload_0
    //   171: aload #4
    //   173: aload #6
    //   175: aload_2
    //   176: invokestatic createFromXmlInner : (Landroid/content/res/Resources;Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;Landroid/content/res/Resources$Theme;)Landroid/graphics/Shader;
    //   179: invokestatic from : (Landroid/graphics/Shader;)Landroid/support/v4/content/res/ComplexColorCompat;
    //   182: areturn
    //   183: aload_0
    //   184: aload #4
    //   186: aload #6
    //   188: aload_2
    //   189: invokestatic createFromXmlInner : (Landroid/content/res/Resources;Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;Landroid/content/res/Resources$Theme;)Landroid/content/res/ColorStateList;
    //   192: invokestatic from : (Landroid/content/res/ColorStateList;)Landroid/support/v4/content/res/ComplexColorCompat;
    //   195: areturn
    //   196: new org/xmlpull/v1/XmlPullParserException
    //   199: dup
    //   200: ldc 'No start tag found'
    //   202: invokespecial <init> : (Ljava/lang/String;)V
    //   205: athrow }
  
  static ComplexColorCompat from(@ColorInt int paramInt) { return new ComplexColorCompat(null, null, paramInt); }
  
  static ComplexColorCompat from(@NonNull ColorStateList paramColorStateList) { return new ComplexColorCompat(null, paramColorStateList, paramColorStateList.getDefaultColor()); }
  
  static ComplexColorCompat from(@NonNull Shader paramShader) { return new ComplexColorCompat(paramShader, null, 0); }
  
  @Nullable
  public static ComplexColorCompat inflate(@NonNull Resources paramResources, @ColorRes int paramInt, @Nullable Resources.Theme paramTheme) throws IOException, XmlPullParserException {
    try {
      return createFromXml(paramResources, paramInt, paramTheme);
    } catch (Exception paramResources) {
      Log.e("ComplexColorCompat", "Failed to inflate ComplexColor.", paramResources);
      return null;
    } 
  }
  
  @ColorInt
  public int getColor() { return this.mColor; }
  
  @Nullable
  public Shader getShader() { return this.mShader; }
  
  public boolean isGradient() { return (this.mShader != null); }
  
  public boolean isStateful() {
    if (this.mShader == null) {
      ColorStateList colorStateList = this.mColorStateList;
      if (colorStateList != null && colorStateList.isStateful())
        return true; 
    } 
    return false;
  }
  
  public boolean onStateChanged(int[] paramArrayOfInt) {
    byte b = 0;
    int i = b;
    if (isStateful()) {
      ColorStateList colorStateList = this.mColorStateList;
      int j = colorStateList.getColorForState(paramArrayOfInt, colorStateList.getDefaultColor());
      i = b;
      if (j != this.mColor) {
        i = 1;
        this.mColor = j;
      } 
    } 
    return i;
  }
  
  public void setColor(@ColorInt int paramInt) { this.mColor = paramInt; }
  
  public boolean willDraw() { return (isGradient() || this.mColor != 0); }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\content\res\ComplexColorCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */