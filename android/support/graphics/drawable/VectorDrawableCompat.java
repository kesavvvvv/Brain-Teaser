package android.support.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.ComplexColorCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class VectorDrawableCompat extends VectorDrawableCommon {
  private static final boolean DBG_VECTOR_DRAWABLE = false;
  
  static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
  
  private static final int LINECAP_BUTT = 0;
  
  private static final int LINECAP_ROUND = 1;
  
  private static final int LINECAP_SQUARE = 2;
  
  private static final int LINEJOIN_BEVEL = 2;
  
  private static final int LINEJOIN_MITER = 0;
  
  private static final int LINEJOIN_ROUND = 1;
  
  static final String LOGTAG = "VectorDrawableCompat";
  
  private static final int MAX_CACHED_BITMAP_SIZE = 2048;
  
  private static final String SHAPE_CLIP_PATH = "clip-path";
  
  private static final String SHAPE_GROUP = "group";
  
  private static final String SHAPE_PATH = "path";
  
  private static final String SHAPE_VECTOR = "vector";
  
  private boolean mAllowCaching = true;
  
  private Drawable.ConstantState mCachedConstantStateDelegate;
  
  private ColorFilter mColorFilter;
  
  private boolean mMutated;
  
  private PorterDuffColorFilter mTintFilter;
  
  private final Rect mTmpBounds = new Rect();
  
  private final float[] mTmpFloats = new float[9];
  
  private final Matrix mTmpMatrix = new Matrix();
  
  private VectorDrawableCompatState mVectorState = new VectorDrawableCompatState();
  
  VectorDrawableCompat() {}
  
  VectorDrawableCompat(@NonNull VectorDrawableCompatState paramVectorDrawableCompatState) { this.mTintFilter = updateTintFilter(this.mTintFilter, paramVectorDrawableCompatState.mTint, paramVectorDrawableCompatState.mTintMode); }
  
  static int applyAlpha(int paramInt, float paramFloat) { return paramInt & 0xFFFFFF | (int)(Color.alpha(paramInt) * paramFloat) << 24; }
  
  @Nullable
  public static VectorDrawableCompat create(@NonNull Resources paramResources, @DrawableRes int paramInt, @Nullable Resources.Theme paramTheme) {
    if (Build.VERSION.SDK_INT >= 24) {
      VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
      vectorDrawableCompat.mDelegateDrawable = ResourcesCompat.getDrawable(paramResources, paramInt, paramTheme);
      vectorDrawableCompat.mCachedConstantStateDelegate = new VectorDrawableDelegateState(vectorDrawableCompat.mDelegateDrawable.getConstantState());
      return vectorDrawableCompat;
    } 
    try {
      XmlResourceParser xmlResourceParser = paramResources.getXml(paramInt);
      AttributeSet attributeSet = Xml.asAttributeSet(xmlResourceParser);
      while (true) {
        paramInt = xmlResourceParser.next();
        if (paramInt != 2 && paramInt != 1)
          continue; 
        break;
      } 
      if (paramInt == 2)
        return createFromXmlInner(paramResources, xmlResourceParser, attributeSet, paramTheme); 
      throw new XmlPullParserException("No start tag found");
    } catch (XmlPullParserException paramResources) {
      Log.e("VectorDrawableCompat", "parser error", paramResources);
    } catch (IOException paramResources) {
      Log.e("VectorDrawableCompat", "parser error", paramResources);
    } 
    return null;
  }
  
  public static VectorDrawableCompat createFromXmlInner(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
    vectorDrawableCompat.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    return vectorDrawableCompat;
  }
  
  private void inflateInternal(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme) throws XmlPullParserException, IOException { // Byte code:
    //   0: aload_0
    //   1: getfield mVectorState : Landroid/support/graphics/drawable/VectorDrawableCompat$VectorDrawableCompatState;
    //   4: astore #9
    //   6: aload #9
    //   8: getfield mVPathRenderer : Landroid/support/graphics/drawable/VectorDrawableCompat$VPathRenderer;
    //   11: astore #10
    //   13: iconst_1
    //   14: istore #5
    //   16: new java/util/ArrayDeque
    //   19: dup
    //   20: invokespecial <init> : ()V
    //   23: astore #11
    //   25: aload #11
    //   27: aload #10
    //   29: getfield mRootGroup : Landroid/support/graphics/drawable/VectorDrawableCompat$VGroup;
    //   32: invokevirtual push : (Ljava/lang/Object;)V
    //   35: aload_2
    //   36: invokeinterface getEventType : ()I
    //   41: istore #7
    //   43: aload_2
    //   44: invokeinterface getDepth : ()I
    //   49: istore #8
    //   51: iload #7
    //   53: iconst_1
    //   54: if_icmpeq -> 423
    //   57: aload_2
    //   58: invokeinterface getDepth : ()I
    //   63: iload #8
    //   65: iconst_1
    //   66: iadd
    //   67: if_icmpge -> 76
    //   70: iload #7
    //   72: iconst_3
    //   73: if_icmpeq -> 423
    //   76: iload #7
    //   78: iconst_2
    //   79: if_icmpne -> 370
    //   82: aload_2
    //   83: invokeinterface getName : ()Ljava/lang/String;
    //   88: astore #13
    //   90: aload #11
    //   92: invokevirtual peek : ()Ljava/lang/Object;
    //   95: checkcast android/support/graphics/drawable/VectorDrawableCompat$VGroup
    //   98: astore #12
    //   100: ldc 'path'
    //   102: aload #13
    //   104: invokevirtual equals : (Ljava/lang/Object;)Z
    //   107: ifeq -> 186
    //   110: new android/support/graphics/drawable/VectorDrawableCompat$VFullPath
    //   113: dup
    //   114: invokespecial <init> : ()V
    //   117: astore #13
    //   119: aload #13
    //   121: aload_1
    //   122: aload_3
    //   123: aload #4
    //   125: aload_2
    //   126: invokevirtual inflate : (Landroid/content/res/Resources;Landroid/util/AttributeSet;Landroid/content/res/Resources$Theme;Lorg/xmlpull/v1/XmlPullParser;)V
    //   129: aload #12
    //   131: getfield mChildren : Ljava/util/ArrayList;
    //   134: aload #13
    //   136: invokevirtual add : (Ljava/lang/Object;)Z
    //   139: pop
    //   140: aload #13
    //   142: invokevirtual getPathName : ()Ljava/lang/String;
    //   145: ifnull -> 164
    //   148: aload #10
    //   150: getfield mVGTargetsMap : Landroid/support/v4/util/ArrayMap;
    //   153: aload #13
    //   155: invokevirtual getPathName : ()Ljava/lang/String;
    //   158: aload #13
    //   160: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   163: pop
    //   164: iconst_0
    //   165: istore #6
    //   167: aload #9
    //   169: aload #9
    //   171: getfield mChangingConfigurations : I
    //   174: aload #13
    //   176: getfield mChangingConfigurations : I
    //   179: ior
    //   180: putfield mChangingConfigurations : I
    //   183: goto -> 367
    //   186: ldc 'clip-path'
    //   188: aload #13
    //   190: invokevirtual equals : (Ljava/lang/Object;)Z
    //   193: ifeq -> 269
    //   196: new android/support/graphics/drawable/VectorDrawableCompat$VClipPath
    //   199: dup
    //   200: invokespecial <init> : ()V
    //   203: astore #13
    //   205: aload #13
    //   207: aload_1
    //   208: aload_3
    //   209: aload #4
    //   211: aload_2
    //   212: invokevirtual inflate : (Landroid/content/res/Resources;Landroid/util/AttributeSet;Landroid/content/res/Resources$Theme;Lorg/xmlpull/v1/XmlPullParser;)V
    //   215: aload #12
    //   217: getfield mChildren : Ljava/util/ArrayList;
    //   220: aload #13
    //   222: invokevirtual add : (Ljava/lang/Object;)Z
    //   225: pop
    //   226: aload #13
    //   228: invokevirtual getPathName : ()Ljava/lang/String;
    //   231: ifnull -> 250
    //   234: aload #10
    //   236: getfield mVGTargetsMap : Landroid/support/v4/util/ArrayMap;
    //   239: aload #13
    //   241: invokevirtual getPathName : ()Ljava/lang/String;
    //   244: aload #13
    //   246: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   249: pop
    //   250: aload #9
    //   252: aload #9
    //   254: getfield mChangingConfigurations : I
    //   257: aload #13
    //   259: getfield mChangingConfigurations : I
    //   262: ior
    //   263: putfield mChangingConfigurations : I
    //   266: goto -> 363
    //   269: ldc 'group'
    //   271: aload #13
    //   273: invokevirtual equals : (Ljava/lang/Object;)Z
    //   276: ifeq -> 363
    //   279: new android/support/graphics/drawable/VectorDrawableCompat$VGroup
    //   282: dup
    //   283: invokespecial <init> : ()V
    //   286: astore #13
    //   288: aload #13
    //   290: aload_1
    //   291: aload_3
    //   292: aload #4
    //   294: aload_2
    //   295: invokevirtual inflate : (Landroid/content/res/Resources;Landroid/util/AttributeSet;Landroid/content/res/Resources$Theme;Lorg/xmlpull/v1/XmlPullParser;)V
    //   298: aload #12
    //   300: getfield mChildren : Ljava/util/ArrayList;
    //   303: aload #13
    //   305: invokevirtual add : (Ljava/lang/Object;)Z
    //   308: pop
    //   309: aload #11
    //   311: aload #13
    //   313: invokevirtual push : (Ljava/lang/Object;)V
    //   316: aload #13
    //   318: invokevirtual getGroupName : ()Ljava/lang/String;
    //   321: ifnull -> 340
    //   324: aload #10
    //   326: getfield mVGTargetsMap : Landroid/support/v4/util/ArrayMap;
    //   329: aload #13
    //   331: invokevirtual getGroupName : ()Ljava/lang/String;
    //   334: aload #13
    //   336: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   339: pop
    //   340: aload #9
    //   342: aload #9
    //   344: getfield mChangingConfigurations : I
    //   347: aload #13
    //   349: getfield mChangingConfigurations : I
    //   352: ior
    //   353: putfield mChangingConfigurations : I
    //   356: iload #5
    //   358: istore #6
    //   360: goto -> 367
    //   363: iload #5
    //   365: istore #6
    //   367: goto -> 408
    //   370: iload #5
    //   372: istore #6
    //   374: iload #7
    //   376: iconst_3
    //   377: if_icmpne -> 367
    //   380: iload #5
    //   382: istore #6
    //   384: ldc 'group'
    //   386: aload_2
    //   387: invokeinterface getName : ()Ljava/lang/String;
    //   392: invokevirtual equals : (Ljava/lang/Object;)Z
    //   395: ifeq -> 408
    //   398: aload #11
    //   400: invokevirtual pop : ()Ljava/lang/Object;
    //   403: pop
    //   404: iload #5
    //   406: istore #6
    //   408: aload_2
    //   409: invokeinterface next : ()I
    //   414: istore #7
    //   416: iload #6
    //   418: istore #5
    //   420: goto -> 51
    //   423: iload #5
    //   425: ifne -> 429
    //   428: return
    //   429: new org/xmlpull/v1/XmlPullParserException
    //   432: dup
    //   433: ldc_w 'no path defined'
    //   436: invokespecial <init> : (Ljava/lang/String;)V
    //   439: athrow }
  
  private boolean needMirroring() { return (Build.VERSION.SDK_INT >= 17) ? ((isAutoMirrored() && DrawableCompat.getLayoutDirection(this) == 1)) : false; }
  
  private static PorterDuff.Mode parseTintModeCompat(int paramInt, PorterDuff.Mode paramMode) {
    if (paramInt != 3) {
      if (paramInt != 5) {
        if (paramInt != 9) {
          switch (paramInt) {
            default:
              return paramMode;
            case 16:
              return PorterDuff.Mode.ADD;
            case 15:
              return PorterDuff.Mode.SCREEN;
            case 14:
              break;
          } 
          return PorterDuff.Mode.MULTIPLY;
        } 
        return PorterDuff.Mode.SRC_ATOP;
      } 
      return PorterDuff.Mode.SRC_IN;
    } 
    return PorterDuff.Mode.SRC_OVER;
  }
  
  private void printGroupTree(VGroup paramVGroup, int paramInt) {
    String str = "";
    byte b;
    for (b = 0; b < paramInt; b++) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str);
      stringBuilder1.append("    ");
      str = stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append("current group is :");
    stringBuilder.append(paramVGroup.getGroupName());
    stringBuilder.append(" rotation is ");
    stringBuilder.append(paramVGroup.mRotate);
    Log.v("VectorDrawableCompat", stringBuilder.toString());
    stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append("matrix is :");
    stringBuilder.append(paramVGroup.getLocalMatrix().toString());
    Log.v("VectorDrawableCompat", stringBuilder.toString());
    for (b = 0; b < paramVGroup.mChildren.size(); b++) {
      VObject vObject = (VObject)paramVGroup.mChildren.get(b);
      if (vObject instanceof VGroup) {
        printGroupTree((VGroup)vObject, paramInt + 1);
      } else {
        ((VPath)vObject).printVPath(paramInt + 1);
      } 
    } 
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray, XmlPullParser paramXmlPullParser) throws XmlPullParserException {
    String str;
    VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
    VPathRenderer vPathRenderer = vectorDrawableCompatState.mVPathRenderer;
    vectorDrawableCompatState.mTintMode = parseTintModeCompat(TypedArrayUtils.getNamedInt(paramTypedArray, paramXmlPullParser, "tintMode", 6, -1), PorterDuff.Mode.SRC_IN);
    ColorStateList colorStateList = paramTypedArray.getColorStateList(1);
    if (colorStateList != null)
      vectorDrawableCompatState.mTint = colorStateList; 
    vectorDrawableCompatState.mAutoMirrored = TypedArrayUtils.getNamedBoolean(paramTypedArray, paramXmlPullParser, "autoMirrored", 5, vectorDrawableCompatState.mAutoMirrored);
    vPathRenderer.mViewportWidth = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "viewportWidth", 7, vPathRenderer.mViewportWidth);
    vPathRenderer.mViewportHeight = TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "viewportHeight", 8, vPathRenderer.mViewportHeight);
    if (vPathRenderer.mViewportWidth > 0.0F) {
      if (vPathRenderer.mViewportHeight > 0.0F) {
        vPathRenderer.mBaseWidth = paramTypedArray.getDimension(3, vPathRenderer.mBaseWidth);
        vPathRenderer.mBaseHeight = paramTypedArray.getDimension(2, vPathRenderer.mBaseHeight);
        if (vPathRenderer.mBaseWidth > 0.0F) {
          if (vPathRenderer.mBaseHeight > 0.0F) {
            vPathRenderer.setAlpha(TypedArrayUtils.getNamedFloat(paramTypedArray, paramXmlPullParser, "alpha", 4, vPathRenderer.getAlpha()));
            str = paramTypedArray.getString(0);
            if (str != null) {
              vPathRenderer.mRootName = str;
              vPathRenderer.mVGTargetsMap.put(str, vPathRenderer);
            } 
            return;
          } 
          StringBuilder stringBuilder3 = new StringBuilder();
          stringBuilder3.append(str.getPositionDescription());
          stringBuilder3.append("<vector> tag requires height > 0");
          throw new XmlPullParserException(stringBuilder3.toString());
        } 
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str.getPositionDescription());
        stringBuilder2.append("<vector> tag requires width > 0");
        throw new XmlPullParserException(stringBuilder2.toString());
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str.getPositionDescription());
      stringBuilder1.append("<vector> tag requires viewportHeight > 0");
      throw new XmlPullParserException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str.getPositionDescription());
    stringBuilder.append("<vector> tag requires viewportWidth > 0");
    throw new XmlPullParserException(stringBuilder.toString());
  }
  
  public boolean canApplyTheme() {
    if (this.mDelegateDrawable != null)
      DrawableCompat.canApplyTheme(this.mDelegateDrawable); 
    return false;
  }
  
  public void draw(Canvas paramCanvas) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.draw(paramCanvas);
      return;
    } 
    copyBounds(this.mTmpBounds);
    if (this.mTmpBounds.width() > 0) {
      if (this.mTmpBounds.height() <= 0)
        return; 
      ColorFilter colorFilter = this.mColorFilter;
      PorterDuffColorFilter porterDuffColorFilter = colorFilter;
      if (colorFilter == null)
        porterDuffColorFilter = this.mTintFilter; 
      paramCanvas.getMatrix(this.mTmpMatrix);
      this.mTmpMatrix.getValues(this.mTmpFloats);
      float f1 = Math.abs(this.mTmpFloats[0]);
      float f2 = Math.abs(this.mTmpFloats[4]);
      float f4 = Math.abs(this.mTmpFloats[1]);
      float f3 = Math.abs(this.mTmpFloats[3]);
      if (f4 != 0.0F || f3 != 0.0F) {
        f1 = 1.0F;
        f2 = 1.0F;
      } 
      int i = (int)(this.mTmpBounds.width() * f1);
      int j = (int)(this.mTmpBounds.height() * f2);
      i = Math.min(2048, i);
      j = Math.min(2048, j);
      if (i > 0) {
        if (j <= 0)
          return; 
        int k = paramCanvas.save();
        paramCanvas.translate(this.mTmpBounds.left, this.mTmpBounds.top);
        if (needMirroring()) {
          paramCanvas.translate(this.mTmpBounds.width(), 0.0F);
          paramCanvas.scale(-1.0F, 1.0F);
        } 
        this.mTmpBounds.offsetTo(0, 0);
        this.mVectorState.createCachedBitmapIfNeeded(i, j);
        if (!this.mAllowCaching) {
          this.mVectorState.updateCachedBitmap(i, j);
        } else if (!this.mVectorState.canReuseCache()) {
          this.mVectorState.updateCachedBitmap(i, j);
          this.mVectorState.updateCacheStates();
        } 
        this.mVectorState.drawCachedBitmapWithRootAlpha(paramCanvas, porterDuffColorFilter, this.mTmpBounds);
        paramCanvas.restoreToCount(k);
        return;
      } 
      return;
    } 
  }
  
  public int getAlpha() { return (this.mDelegateDrawable != null) ? DrawableCompat.getAlpha(this.mDelegateDrawable) : this.mVectorState.mVPathRenderer.getRootAlpha(); }
  
  public int getChangingConfigurations() { return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getChangingConfigurations() : (super.getChangingConfigurations() | this.mVectorState.getChangingConfigurations()); }
  
  public Drawable.ConstantState getConstantState() {
    if (this.mDelegateDrawable != null && Build.VERSION.SDK_INT >= 24)
      return new VectorDrawableDelegateState(this.mDelegateDrawable.getConstantState()); 
    this.mVectorState.mChangingConfigurations = getChangingConfigurations();
    return this.mVectorState;
  }
  
  public int getIntrinsicHeight() { return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getIntrinsicHeight() : (int)this.mVectorState.mVPathRenderer.mBaseHeight; }
  
  public int getIntrinsicWidth() { return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getIntrinsicWidth() : (int)this.mVectorState.mVPathRenderer.mBaseWidth; }
  
  public int getOpacity() { return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getOpacity() : -3; }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public float getPixelSize() {
    VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
    if (vectorDrawableCompatState == null || vectorDrawableCompatState.mVPathRenderer == null || this.mVectorState.mVPathRenderer.mBaseWidth == 0.0F || this.mVectorState.mVPathRenderer.mBaseHeight == 0.0F || this.mVectorState.mVPathRenderer.mViewportHeight == 0.0F || this.mVectorState.mVPathRenderer.mViewportWidth == 0.0F)
      return 1.0F; 
    float f1 = this.mVectorState.mVPathRenderer.mBaseWidth;
    float f2 = this.mVectorState.mVPathRenderer.mBaseHeight;
    float f3 = this.mVectorState.mVPathRenderer.mViewportWidth;
    float f4 = this.mVectorState.mVPathRenderer.mViewportHeight;
    return Math.min(f3 / f1, f4 / f2);
  }
  
  Object getTargetByName(String paramString) { return this.mVectorState.mVPathRenderer.mVGTargetsMap.get(paramString); }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet) throws XmlPullParserException, IOException {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.inflate(paramResources, paramXmlPullParser, paramAttributeSet);
      return;
    } 
    inflate(paramResources, paramXmlPullParser, paramAttributeSet, null);
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.inflate(this.mDelegateDrawable, paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
      return;
    } 
    VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
    vectorDrawableCompatState.mVPathRenderer = new VPathRenderer();
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_TYPE_ARRAY);
    updateStateFromTypedArray(typedArray, paramXmlPullParser);
    typedArray.recycle();
    vectorDrawableCompatState.mChangingConfigurations = getChangingConfigurations();
    vectorDrawableCompatState.mCacheDirty = true;
    inflateInternal(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    this.mTintFilter = updateTintFilter(this.mTintFilter, vectorDrawableCompatState.mTint, vectorDrawableCompatState.mTintMode);
  }
  
  public void invalidateSelf() {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.invalidateSelf();
      return;
    } 
    super.invalidateSelf();
  }
  
  public boolean isAutoMirrored() { return (this.mDelegateDrawable != null) ? DrawableCompat.isAutoMirrored(this.mDelegateDrawable) : this.mVectorState.mAutoMirrored; }
  
  public boolean isStateful() {
    if (this.mDelegateDrawable != null)
      return this.mDelegateDrawable.isStateful(); 
    if (!super.isStateful()) {
      VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
      if (vectorDrawableCompatState == null || (!vectorDrawableCompatState.isStateful() && (this.mVectorState.mTint == null || !this.mVectorState.mTint.isStateful())))
        return false; 
    } 
    return true;
  }
  
  public Drawable mutate() {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.mutate();
      return this;
    } 
    if (!this.mMutated && super.mutate() == this) {
      this.mVectorState = new VectorDrawableCompatState(this.mVectorState);
      this.mMutated = true;
    } 
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect) {
    if (this.mDelegateDrawable != null)
      this.mDelegateDrawable.setBounds(paramRect); 
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt) {
    if (this.mDelegateDrawable != null)
      return this.mDelegateDrawable.setState(paramArrayOfInt); 
    int i = 0;
    VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
    byte b = i;
    if (vectorDrawableCompatState.mTint != null) {
      b = i;
      if (vectorDrawableCompatState.mTintMode != null) {
        this.mTintFilter = updateTintFilter(this.mTintFilter, vectorDrawableCompatState.mTint, vectorDrawableCompatState.mTintMode);
        invalidateSelf();
        b = 1;
      } 
    } 
    i = b;
    if (vectorDrawableCompatState.isStateful()) {
      i = b;
      if (vectorDrawableCompatState.onStateChanged(paramArrayOfInt)) {
        invalidateSelf();
        i = 1;
      } 
    } 
    return i;
  }
  
  public void scheduleSelf(Runnable paramRunnable, long paramLong) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.scheduleSelf(paramRunnable, paramLong);
      return;
    } 
    super.scheduleSelf(paramRunnable, paramLong);
  }
  
  void setAllowCaching(boolean paramBoolean) { this.mAllowCaching = paramBoolean; }
  
  public void setAlpha(int paramInt) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setAlpha(paramInt);
      return;
    } 
    if (this.mVectorState.mVPathRenderer.getRootAlpha() != paramInt) {
      this.mVectorState.mVPathRenderer.setRootAlpha(paramInt);
      invalidateSelf();
    } 
  }
  
  public void setAutoMirrored(boolean paramBoolean) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setAutoMirrored(this.mDelegateDrawable, paramBoolean);
      return;
    } 
    this.mVectorState.mAutoMirrored = paramBoolean;
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setColorFilter(paramColorFilter);
      return;
    } 
    this.mColorFilter = paramColorFilter;
    invalidateSelf();
  }
  
  public void setTint(int paramInt) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setTint(this.mDelegateDrawable, paramInt);
      return;
    } 
    setTintList(ColorStateList.valueOf(paramInt));
  }
  
  public void setTintList(ColorStateList paramColorStateList) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setTintList(this.mDelegateDrawable, paramColorStateList);
      return;
    } 
    VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
    if (vectorDrawableCompatState.mTint != paramColorStateList) {
      vectorDrawableCompatState.mTint = paramColorStateList;
      this.mTintFilter = updateTintFilter(this.mTintFilter, paramColorStateList, vectorDrawableCompatState.mTintMode);
      invalidateSelf();
    } 
  }
  
  public void setTintMode(PorterDuff.Mode paramMode) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setTintMode(this.mDelegateDrawable, paramMode);
      return;
    } 
    VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
    if (vectorDrawableCompatState.mTintMode != paramMode) {
      vectorDrawableCompatState.mTintMode = paramMode;
      this.mTintFilter = updateTintFilter(this.mTintFilter, vectorDrawableCompatState.mTint, paramMode);
      invalidateSelf();
    } 
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2) { return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.setVisible(paramBoolean1, paramBoolean2) : super.setVisible(paramBoolean1, paramBoolean2); }
  
  public void unscheduleSelf(Runnable paramRunnable) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.unscheduleSelf(paramRunnable);
      return;
    } 
    super.unscheduleSelf(paramRunnable);
  }
  
  PorterDuffColorFilter updateTintFilter(PorterDuffColorFilter paramPorterDuffColorFilter, ColorStateList paramColorStateList, PorterDuff.Mode paramMode) { return (paramColorStateList == null || paramMode == null) ? null : new PorterDuffColorFilter(paramColorStateList.getColorForState(getState(), 0), paramMode); }
  
  private static class VClipPath extends VPath {
    public VClipPath() {}
    
    public VClipPath(VClipPath param1VClipPath) { super(param1VClipPath); }
    
    private void updateStateFromTypedArray(TypedArray param1TypedArray) {
      String str2 = param1TypedArray.getString(0);
      if (str2 != null)
        this.mPathName = str2; 
      String str1 = param1TypedArray.getString(1);
      if (str1 != null)
        this.mNodes = PathParser.createNodesFromPathData(str1); 
    }
    
    public void inflate(Resources param1Resources, AttributeSet param1AttributeSet, Resources.Theme param1Theme, XmlPullParser param1XmlPullParser) {
      if (!TypedArrayUtils.hasAttribute(param1XmlPullParser, "pathData"))
        return; 
      TypedArray typedArray = TypedArrayUtils.obtainAttributes(param1Resources, param1Theme, param1AttributeSet, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_CLIP_PATH);
      updateStateFromTypedArray(typedArray);
      typedArray.recycle();
    }
    
    public boolean isClipPath() { return true; }
  }
  
  private static class VFullPath extends VPath {
    private static final int FILL_TYPE_WINDING = 0;
    
    float mFillAlpha = 1.0F;
    
    ComplexColorCompat mFillColor;
    
    int mFillRule = 0;
    
    float mStrokeAlpha = 1.0F;
    
    ComplexColorCompat mStrokeColor;
    
    Paint.Cap mStrokeLineCap = Paint.Cap.BUTT;
    
    Paint.Join mStrokeLineJoin = Paint.Join.MITER;
    
    float mStrokeMiterlimit = 4.0F;
    
    float mStrokeWidth = 0.0F;
    
    private int[] mThemeAttrs;
    
    float mTrimPathEnd = 1.0F;
    
    float mTrimPathOffset = 0.0F;
    
    float mTrimPathStart = 0.0F;
    
    public VFullPath() {}
    
    public VFullPath(VFullPath param1VFullPath) {
      super(param1VFullPath);
      this.mThemeAttrs = param1VFullPath.mThemeAttrs;
      this.mStrokeColor = param1VFullPath.mStrokeColor;
      this.mStrokeWidth = param1VFullPath.mStrokeWidth;
      this.mStrokeAlpha = param1VFullPath.mStrokeAlpha;
      this.mFillColor = param1VFullPath.mFillColor;
      this.mFillRule = param1VFullPath.mFillRule;
      this.mFillAlpha = param1VFullPath.mFillAlpha;
      this.mTrimPathStart = param1VFullPath.mTrimPathStart;
      this.mTrimPathEnd = param1VFullPath.mTrimPathEnd;
      this.mTrimPathOffset = param1VFullPath.mTrimPathOffset;
      this.mStrokeLineCap = param1VFullPath.mStrokeLineCap;
      this.mStrokeLineJoin = param1VFullPath.mStrokeLineJoin;
      this.mStrokeMiterlimit = param1VFullPath.mStrokeMiterlimit;
    }
    
    private Paint.Cap getStrokeLineCap(int param1Int, Paint.Cap param1Cap) {
      switch (param1Int) {
        default:
          return param1Cap;
        case 2:
          return Paint.Cap.SQUARE;
        case 1:
          return Paint.Cap.ROUND;
        case 0:
          break;
      } 
      return Paint.Cap.BUTT;
    }
    
    private Paint.Join getStrokeLineJoin(int param1Int, Paint.Join param1Join) {
      switch (param1Int) {
        default:
          return param1Join;
        case 2:
          return Paint.Join.BEVEL;
        case 1:
          return Paint.Join.ROUND;
        case 0:
          break;
      } 
      return Paint.Join.MITER;
    }
    
    private void updateStateFromTypedArray(TypedArray param1TypedArray, XmlPullParser param1XmlPullParser, Resources.Theme param1Theme) {
      this.mThemeAttrs = null;
      if (!TypedArrayUtils.hasAttribute(param1XmlPullParser, "pathData"))
        return; 
      String str = param1TypedArray.getString(0);
      if (str != null)
        this.mPathName = str; 
      str = param1TypedArray.getString(2);
      if (str != null)
        this.mNodes = PathParser.createNodesFromPathData(str); 
      this.mFillColor = TypedArrayUtils.getNamedComplexColor(param1TypedArray, param1XmlPullParser, param1Theme, "fillColor", 1, 0);
      this.mFillAlpha = TypedArrayUtils.getNamedFloat(param1TypedArray, param1XmlPullParser, "fillAlpha", 12, this.mFillAlpha);
      this.mStrokeLineCap = getStrokeLineCap(TypedArrayUtils.getNamedInt(param1TypedArray, param1XmlPullParser, "strokeLineCap", 8, -1), this.mStrokeLineCap);
      this.mStrokeLineJoin = getStrokeLineJoin(TypedArrayUtils.getNamedInt(param1TypedArray, param1XmlPullParser, "strokeLineJoin", 9, -1), this.mStrokeLineJoin);
      this.mStrokeMiterlimit = TypedArrayUtils.getNamedFloat(param1TypedArray, param1XmlPullParser, "strokeMiterLimit", 10, this.mStrokeMiterlimit);
      this.mStrokeColor = TypedArrayUtils.getNamedComplexColor(param1TypedArray, param1XmlPullParser, param1Theme, "strokeColor", 3, 0);
      this.mStrokeAlpha = TypedArrayUtils.getNamedFloat(param1TypedArray, param1XmlPullParser, "strokeAlpha", 11, this.mStrokeAlpha);
      this.mStrokeWidth = TypedArrayUtils.getNamedFloat(param1TypedArray, param1XmlPullParser, "strokeWidth", 4, this.mStrokeWidth);
      this.mTrimPathEnd = TypedArrayUtils.getNamedFloat(param1TypedArray, param1XmlPullParser, "trimPathEnd", 6, this.mTrimPathEnd);
      this.mTrimPathOffset = TypedArrayUtils.getNamedFloat(param1TypedArray, param1XmlPullParser, "trimPathOffset", 7, this.mTrimPathOffset);
      this.mTrimPathStart = TypedArrayUtils.getNamedFloat(param1TypedArray, param1XmlPullParser, "trimPathStart", 5, this.mTrimPathStart);
      this.mFillRule = TypedArrayUtils.getNamedInt(param1TypedArray, param1XmlPullParser, "fillType", 13, this.mFillRule);
    }
    
    public void applyTheme(Resources.Theme param1Theme) {
      if (this.mThemeAttrs == null)
        return; 
    }
    
    public boolean canApplyTheme() { return (this.mThemeAttrs != null); }
    
    float getFillAlpha() { return this.mFillAlpha; }
    
    @ColorInt
    int getFillColor() { return this.mFillColor.getColor(); }
    
    float getStrokeAlpha() { return this.mStrokeAlpha; }
    
    @ColorInt
    int getStrokeColor() { return this.mStrokeColor.getColor(); }
    
    float getStrokeWidth() { return this.mStrokeWidth; }
    
    float getTrimPathEnd() { return this.mTrimPathEnd; }
    
    float getTrimPathOffset() { return this.mTrimPathOffset; }
    
    float getTrimPathStart() { return this.mTrimPathStart; }
    
    public void inflate(Resources param1Resources, AttributeSet param1AttributeSet, Resources.Theme param1Theme, XmlPullParser param1XmlPullParser) {
      TypedArray typedArray = TypedArrayUtils.obtainAttributes(param1Resources, param1Theme, param1AttributeSet, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_PATH);
      updateStateFromTypedArray(typedArray, param1XmlPullParser, param1Theme);
      typedArray.recycle();
    }
    
    public boolean isStateful() { return (this.mFillColor.isStateful() || this.mStrokeColor.isStateful()); }
    
    public boolean onStateChanged(int[] param1ArrayOfInt) { return this.mFillColor.onStateChanged(param1ArrayOfInt) | this.mStrokeColor.onStateChanged(param1ArrayOfInt); }
    
    void setFillAlpha(float param1Float) { this.mFillAlpha = param1Float; }
    
    void setFillColor(int param1Int) { this.mFillColor.setColor(param1Int); }
    
    void setStrokeAlpha(float param1Float) { this.mStrokeAlpha = param1Float; }
    
    void setStrokeColor(int param1Int) { this.mStrokeColor.setColor(param1Int); }
    
    void setStrokeWidth(float param1Float) { this.mStrokeWidth = param1Float; }
    
    void setTrimPathEnd(float param1Float) { this.mTrimPathEnd = param1Float; }
    
    void setTrimPathOffset(float param1Float) { this.mTrimPathOffset = param1Float; }
    
    void setTrimPathStart(float param1Float) { this.mTrimPathStart = param1Float; }
  }
  
  private static class VGroup extends VObject {
    int mChangingConfigurations;
    
    final ArrayList<VectorDrawableCompat.VObject> mChildren = new ArrayList();
    
    private String mGroupName = null;
    
    final Matrix mLocalMatrix = new Matrix();
    
    private float mPivotX = 0.0F;
    
    private float mPivotY = 0.0F;
    
    float mRotate = 0.0F;
    
    private float mScaleX = 1.0F;
    
    private float mScaleY = 1.0F;
    
    final Matrix mStackedMatrix = new Matrix();
    
    private int[] mThemeAttrs;
    
    private float mTranslateX = 0.0F;
    
    private float mTranslateY = 0.0F;
    
    public VGroup() { super(null); }
    
    public VGroup(VGroup param1VGroup, ArrayMap<String, Object> param1ArrayMap) {
      super(null);
      this.mRotate = param1VGroup.mRotate;
      this.mPivotX = param1VGroup.mPivotX;
      this.mPivotY = param1VGroup.mPivotY;
      this.mScaleX = param1VGroup.mScaleX;
      this.mScaleY = param1VGroup.mScaleY;
      this.mTranslateX = param1VGroup.mTranslateX;
      this.mTranslateY = param1VGroup.mTranslateY;
      this.mThemeAttrs = param1VGroup.mThemeAttrs;
      this.mGroupName = param1VGroup.mGroupName;
      this.mChangingConfigurations = param1VGroup.mChangingConfigurations;
      String str = this.mGroupName;
      if (str != null)
        param1ArrayMap.put(str, this); 
      this.mLocalMatrix.set(param1VGroup.mLocalMatrix);
      ArrayList arrayList = param1VGroup.mChildren;
      for (byte b = 0; b < arrayList.size(); b++) {
        Object object = arrayList.get(b);
        if (object instanceof VGroup) {
          object = (VGroup)object;
          this.mChildren.add(new VGroup(object, param1ArrayMap));
        } else {
          if (object instanceof VectorDrawableCompat.VFullPath) {
            object = new VectorDrawableCompat.VFullPath((VectorDrawableCompat.VFullPath)object);
          } else if (object instanceof VectorDrawableCompat.VClipPath) {
            object = new VectorDrawableCompat.VClipPath((VectorDrawableCompat.VClipPath)object);
          } else {
            throw new IllegalStateException("Unknown object in the tree!");
          } 
          this.mChildren.add(object);
          if (object.mPathName != null)
            param1ArrayMap.put(object.mPathName, object); 
        } 
      } 
    }
    
    private void updateLocalMatrix() {
      this.mLocalMatrix.reset();
      this.mLocalMatrix.postTranslate(-this.mPivotX, -this.mPivotY);
      this.mLocalMatrix.postScale(this.mScaleX, this.mScaleY);
      this.mLocalMatrix.postRotate(this.mRotate, 0.0F, 0.0F);
      this.mLocalMatrix.postTranslate(this.mTranslateX + this.mPivotX, this.mTranslateY + this.mPivotY);
    }
    
    private void updateStateFromTypedArray(TypedArray param1TypedArray, XmlPullParser param1XmlPullParser) throws XmlPullParserException {
      this.mThemeAttrs = null;
      this.mRotate = TypedArrayUtils.getNamedFloat(param1TypedArray, param1XmlPullParser, "rotation", 5, this.mRotate);
      this.mPivotX = param1TypedArray.getFloat(1, this.mPivotX);
      this.mPivotY = param1TypedArray.getFloat(2, this.mPivotY);
      this.mScaleX = TypedArrayUtils.getNamedFloat(param1TypedArray, param1XmlPullParser, "scaleX", 3, this.mScaleX);
      this.mScaleY = TypedArrayUtils.getNamedFloat(param1TypedArray, param1XmlPullParser, "scaleY", 4, this.mScaleY);
      this.mTranslateX = TypedArrayUtils.getNamedFloat(param1TypedArray, param1XmlPullParser, "translateX", 6, this.mTranslateX);
      this.mTranslateY = TypedArrayUtils.getNamedFloat(param1TypedArray, param1XmlPullParser, "translateY", 7, this.mTranslateY);
      String str = param1TypedArray.getString(0);
      if (str != null)
        this.mGroupName = str; 
      updateLocalMatrix();
    }
    
    public String getGroupName() { return this.mGroupName; }
    
    public Matrix getLocalMatrix() { return this.mLocalMatrix; }
    
    public float getPivotX() { return this.mPivotX; }
    
    public float getPivotY() { return this.mPivotY; }
    
    public float getRotation() { return this.mRotate; }
    
    public float getScaleX() { return this.mScaleX; }
    
    public float getScaleY() { return this.mScaleY; }
    
    public float getTranslateX() { return this.mTranslateX; }
    
    public float getTranslateY() { return this.mTranslateY; }
    
    public void inflate(Resources param1Resources, AttributeSet param1AttributeSet, Resources.Theme param1Theme, XmlPullParser param1XmlPullParser) {
      TypedArray typedArray = TypedArrayUtils.obtainAttributes(param1Resources, param1Theme, param1AttributeSet, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_GROUP);
      updateStateFromTypedArray(typedArray, param1XmlPullParser);
      typedArray.recycle();
    }
    
    public boolean isStateful() {
      for (byte b = 0; b < this.mChildren.size(); b++) {
        if (((VectorDrawableCompat.VObject)this.mChildren.get(b)).isStateful())
          return true; 
      } 
      return false;
    }
    
    public boolean onStateChanged(int[] param1ArrayOfInt) {
      boolean bool = false;
      for (byte b = 0; b < this.mChildren.size(); b++)
        bool |= ((VectorDrawableCompat.VObject)this.mChildren.get(b)).onStateChanged(param1ArrayOfInt); 
      return bool;
    }
    
    public void setPivotX(float param1Float) {
      if (param1Float != this.mPivotX) {
        this.mPivotX = param1Float;
        updateLocalMatrix();
      } 
    }
    
    public void setPivotY(float param1Float) {
      if (param1Float != this.mPivotY) {
        this.mPivotY = param1Float;
        updateLocalMatrix();
      } 
    }
    
    public void setRotation(float param1Float) {
      if (param1Float != this.mRotate) {
        this.mRotate = param1Float;
        updateLocalMatrix();
      } 
    }
    
    public void setScaleX(float param1Float) {
      if (param1Float != this.mScaleX) {
        this.mScaleX = param1Float;
        updateLocalMatrix();
      } 
    }
    
    public void setScaleY(float param1Float) {
      if (param1Float != this.mScaleY) {
        this.mScaleY = param1Float;
        updateLocalMatrix();
      } 
    }
    
    public void setTranslateX(float param1Float) {
      if (param1Float != this.mTranslateX) {
        this.mTranslateX = param1Float;
        updateLocalMatrix();
      } 
    }
    
    public void setTranslateY(float param1Float) {
      if (param1Float != this.mTranslateY) {
        this.mTranslateY = param1Float;
        updateLocalMatrix();
      } 
    }
  }
  
  private static abstract class VObject {
    private VObject() {}
    
    public boolean isStateful() { return false; }
    
    public boolean onStateChanged(int[] param1ArrayOfInt) { return false; }
  }
  
  private static abstract class VPath extends VObject {
    int mChangingConfigurations;
    
    protected PathParser.PathDataNode[] mNodes = null;
    
    String mPathName;
    
    public VPath() { super(null); }
    
    public VPath(VPath param1VPath) {
      super(null);
      this.mPathName = param1VPath.mPathName;
      this.mChangingConfigurations = param1VPath.mChangingConfigurations;
      this.mNodes = PathParser.deepCopyNodes(param1VPath.mNodes);
    }
    
    public void applyTheme(Resources.Theme param1Theme) {}
    
    public boolean canApplyTheme() { return false; }
    
    public PathParser.PathDataNode[] getPathData() { return this.mNodes; }
    
    public String getPathName() { return this.mPathName; }
    
    public boolean isClipPath() { return false; }
    
    public String nodesToString(PathParser.PathDataNode[] param1ArrayOfPathDataNode) {
      String str = " ";
      for (byte b = 0; b < param1ArrayOfPathDataNode.length; b++) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append((param1ArrayOfPathDataNode[b]).mType);
        stringBuilder.append(":");
        str = stringBuilder.toString();
        float[] arrayOfFloat = (param1ArrayOfPathDataNode[b]).mParams;
        for (byte b1 = 0; b1 < arrayOfFloat.length; b1++) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append(str);
          stringBuilder1.append(arrayOfFloat[b1]);
          stringBuilder1.append(",");
          str = stringBuilder1.toString();
        } 
      } 
      return str;
    }
    
    public void printVPath(int param1Int) {
      String str = "";
      for (byte b = 0; b < param1Int; b++) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(str);
        stringBuilder1.append("    ");
        str = stringBuilder1.toString();
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append("current path is :");
      stringBuilder.append(this.mPathName);
      stringBuilder.append(" pathData is ");
      stringBuilder.append(nodesToString(this.mNodes));
      Log.v("VectorDrawableCompat", stringBuilder.toString());
    }
    
    public void setPathData(PathParser.PathDataNode[] param1ArrayOfPathDataNode) {
      if (!PathParser.canMorph(this.mNodes, param1ArrayOfPathDataNode)) {
        this.mNodes = PathParser.deepCopyNodes(param1ArrayOfPathDataNode);
        return;
      } 
      PathParser.updateNodes(this.mNodes, param1ArrayOfPathDataNode);
    }
    
    public void toPath(Path param1Path) {
      param1Path.reset();
      PathParser.PathDataNode[] arrayOfPathDataNode = this.mNodes;
      if (arrayOfPathDataNode != null)
        PathParser.PathDataNode.nodesToPath(arrayOfPathDataNode, param1Path); 
    }
  }
  
  private static class VPathRenderer {
    private static final Matrix IDENTITY_MATRIX = new Matrix();
    
    float mBaseHeight = 0.0F;
    
    float mBaseWidth = 0.0F;
    
    private int mChangingConfigurations;
    
    Paint mFillPaint;
    
    private final Matrix mFinalPathMatrix = new Matrix();
    
    Boolean mIsStateful = null;
    
    private final Path mPath = new Path();
    
    private PathMeasure mPathMeasure;
    
    private final Path mRenderPath = new Path();
    
    int mRootAlpha = 255;
    
    final VectorDrawableCompat.VGroup mRootGroup = new VectorDrawableCompat.VGroup();
    
    String mRootName = null;
    
    Paint mStrokePaint;
    
    final ArrayMap<String, Object> mVGTargetsMap = new ArrayMap();
    
    float mViewportHeight = 0.0F;
    
    float mViewportWidth = 0.0F;
    
    public VPathRenderer() {}
    
    public VPathRenderer(VPathRenderer param1VPathRenderer) {
      this.mBaseWidth = param1VPathRenderer.mBaseWidth;
      this.mBaseHeight = param1VPathRenderer.mBaseHeight;
      this.mViewportWidth = param1VPathRenderer.mViewportWidth;
      this.mViewportHeight = param1VPathRenderer.mViewportHeight;
      this.mChangingConfigurations = param1VPathRenderer.mChangingConfigurations;
      this.mRootAlpha = param1VPathRenderer.mRootAlpha;
      this.mRootName = param1VPathRenderer.mRootName;
      String str = param1VPathRenderer.mRootName;
      if (str != null)
        this.mVGTargetsMap.put(str, this); 
      this.mIsStateful = param1VPathRenderer.mIsStateful;
    }
    
    private static float cross(float param1Float1, float param1Float2, float param1Float3, float param1Float4) { return param1Float1 * param1Float4 - param1Float2 * param1Float3; }
    
    private void drawGroupTree(VectorDrawableCompat.VGroup param1VGroup, Matrix param1Matrix, Canvas param1Canvas, int param1Int1, int param1Int2, ColorFilter param1ColorFilter) {
      param1VGroup.mStackedMatrix.set(param1Matrix);
      param1VGroup.mStackedMatrix.preConcat(param1VGroup.mLocalMatrix);
      param1Canvas.save();
      byte b;
      for (b = 0; b < param1VGroup.mChildren.size(); b++) {
        VectorDrawableCompat.VObject vObject = (VectorDrawableCompat.VObject)param1VGroup.mChildren.get(b);
        if (vObject instanceof VectorDrawableCompat.VGroup) {
          drawGroupTree((VectorDrawableCompat.VGroup)vObject, param1VGroup.mStackedMatrix, param1Canvas, param1Int1, param1Int2, param1ColorFilter);
        } else if (vObject instanceof VectorDrawableCompat.VPath) {
          drawPath(param1VGroup, (VectorDrawableCompat.VPath)vObject, param1Canvas, param1Int1, param1Int2, param1ColorFilter);
        } 
      } 
      param1Canvas.restore();
    }
    
    private void drawPath(VectorDrawableCompat.VGroup param1VGroup, VectorDrawableCompat.VPath param1VPath, Canvas param1Canvas, int param1Int1, int param1Int2, ColorFilter param1ColorFilter) {
      float f2 = param1Int1 / this.mViewportWidth;
      float f3 = param1Int2 / this.mViewportHeight;
      float f1 = Math.min(f2, f3);
      Matrix matrix = param1VGroup.mStackedMatrix;
      this.mFinalPathMatrix.set(matrix);
      this.mFinalPathMatrix.postScale(f2, f3);
      f2 = getMatrixScale(matrix);
      if (f2 == 0.0F)
        return; 
      param1VPath.toPath(this.mPath);
      Path path = this.mPath;
      this.mRenderPath.reset();
      if (param1VPath.isClipPath()) {
        this.mRenderPath.addPath(path, this.mFinalPathMatrix);
        param1Canvas.clipPath(this.mRenderPath);
        return;
      } 
      param1VPath = (VectorDrawableCompat.VFullPath)param1VPath;
      if (param1VPath.mTrimPathStart != 0.0F || param1VPath.mTrimPathEnd != 1.0F) {
        float f6 = param1VPath.mTrimPathStart;
        float f7 = param1VPath.mTrimPathOffset;
        float f4 = param1VPath.mTrimPathEnd;
        float f5 = param1VPath.mTrimPathOffset;
        if (this.mPathMeasure == null)
          this.mPathMeasure = new PathMeasure(); 
        this.mPathMeasure.setPath(this.mPath, false);
        f3 = this.mPathMeasure.getLength();
        f6 = (f6 + f7) % 1.0F * f3;
        f4 = (f4 + f5) % 1.0F * f3;
        path.reset();
        if (f6 > f4) {
          this.mPathMeasure.getSegment(f6, f3, path, true);
          this.mPathMeasure.getSegment(0.0F, f4, path, true);
        } else {
          this.mPathMeasure.getSegment(f6, f4, path, true);
        } 
        path.rLineTo(0.0F, 0.0F);
      } 
      this.mRenderPath.addPath(path, this.mFinalPathMatrix);
      if (param1VPath.mFillColor.willDraw()) {
        Path.FillType fillType = param1VPath.mFillColor;
        if (this.mFillPaint == null) {
          this.mFillPaint = new Paint(1);
          this.mFillPaint.setStyle(Paint.Style.FILL);
        } 
        Paint paint = this.mFillPaint;
        if (fillType.isGradient()) {
          fillType = fillType.getShader();
          fillType.setLocalMatrix(this.mFinalPathMatrix);
          paint.setShader(fillType);
          paint.setAlpha(Math.round(param1VPath.mFillAlpha * 255.0F));
        } else {
          paint.setColor(VectorDrawableCompat.applyAlpha(fillType.getColor(), param1VPath.mFillAlpha));
        } 
        paint.setColorFilter(param1ColorFilter);
        Path path1 = this.mRenderPath;
        if (param1VPath.mFillRule == 0) {
          Path.FillType fillType1 = Path.FillType.WINDING;
        } else {
          fillType = Path.FillType.EVEN_ODD;
        } 
        path1.setFillType(fillType);
        param1Canvas.drawPath(this.mRenderPath, paint);
      } 
      if (param1VPath.mStrokeColor.willDraw()) {
        Shader shader = param1VPath.mStrokeColor;
        if (this.mStrokePaint == null) {
          this.mStrokePaint = new Paint(1);
          this.mStrokePaint.setStyle(Paint.Style.STROKE);
        } 
        Paint paint = this.mStrokePaint;
        if (param1VPath.mStrokeLineJoin != null)
          paint.setStrokeJoin(param1VPath.mStrokeLineJoin); 
        if (param1VPath.mStrokeLineCap != null)
          paint.setStrokeCap(param1VPath.mStrokeLineCap); 
        paint.setStrokeMiter(param1VPath.mStrokeMiterlimit);
        if (shader.isGradient()) {
          shader = shader.getShader();
          shader.setLocalMatrix(this.mFinalPathMatrix);
          paint.setShader(shader);
          paint.setAlpha(Math.round(param1VPath.mStrokeAlpha * 255.0F));
        } else {
          paint.setColor(VectorDrawableCompat.applyAlpha(shader.getColor(), param1VPath.mStrokeAlpha));
        } 
        paint.setColorFilter(param1ColorFilter);
        paint.setStrokeWidth(param1VPath.mStrokeWidth * f1 * f2);
        param1Canvas.drawPath(this.mRenderPath, paint);
      } 
    }
    
    private float getMatrixScale(Matrix param1Matrix) {
      float[] arrayOfFloat = new float[4];
      arrayOfFloat[0] = 0.0F;
      arrayOfFloat[1] = 1.0F;
      arrayOfFloat[2] = 1.0F;
      arrayOfFloat[3] = 0.0F;
      arrayOfFloat;
      param1Matrix.mapVectors(arrayOfFloat);
      float f1 = (float)Math.hypot(arrayOfFloat[0], arrayOfFloat[1]);
      float f3 = (float)Math.hypot(arrayOfFloat[2], arrayOfFloat[3]);
      float f2 = cross(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3]);
      f3 = Math.max(f1, f3);
      f1 = 0.0F;
      if (f3 > 0.0F)
        f1 = Math.abs(f2) / f3; 
      return f1;
    }
    
    public void draw(Canvas param1Canvas, int param1Int1, int param1Int2, ColorFilter param1ColorFilter) { drawGroupTree(this.mRootGroup, IDENTITY_MATRIX, param1Canvas, param1Int1, param1Int2, param1ColorFilter); }
    
    public float getAlpha() { return getRootAlpha() / 255.0F; }
    
    public int getRootAlpha() { return this.mRootAlpha; }
    
    public boolean isStateful() {
      if (this.mIsStateful == null)
        this.mIsStateful = Boolean.valueOf(this.mRootGroup.isStateful()); 
      return this.mIsStateful.booleanValue();
    }
    
    public boolean onStateChanged(int[] param1ArrayOfInt) { return this.mRootGroup.onStateChanged(param1ArrayOfInt); }
    
    public void setAlpha(float param1Float) { setRootAlpha((int)(255.0F * param1Float)); }
    
    public void setRootAlpha(int param1Int) { this.mRootAlpha = param1Int; }
  }
  
  private static class VectorDrawableCompatState extends Drawable.ConstantState {
    boolean mAutoMirrored;
    
    boolean mCacheDirty;
    
    boolean mCachedAutoMirrored;
    
    Bitmap mCachedBitmap;
    
    int mCachedRootAlpha;
    
    int[] mCachedThemeAttrs;
    
    ColorStateList mCachedTint;
    
    PorterDuff.Mode mCachedTintMode;
    
    int mChangingConfigurations;
    
    Paint mTempPaint;
    
    ColorStateList mTint = null;
    
    PorterDuff.Mode mTintMode = VectorDrawableCompat.DEFAULT_TINT_MODE;
    
    VectorDrawableCompat.VPathRenderer mVPathRenderer;
    
    public VectorDrawableCompatState() { this.mVPathRenderer = new VectorDrawableCompat.VPathRenderer(); }
    
    public VectorDrawableCompatState(VectorDrawableCompatState param1VectorDrawableCompatState) {
      if (param1VectorDrawableCompatState != null) {
        this.mChangingConfigurations = param1VectorDrawableCompatState.mChangingConfigurations;
        this.mVPathRenderer = new VectorDrawableCompat.VPathRenderer(param1VectorDrawableCompatState.mVPathRenderer);
        if (param1VectorDrawableCompatState.mVPathRenderer.mFillPaint != null)
          this.mVPathRenderer.mFillPaint = new Paint(param1VectorDrawableCompatState.mVPathRenderer.mFillPaint); 
        if (param1VectorDrawableCompatState.mVPathRenderer.mStrokePaint != null)
          this.mVPathRenderer.mStrokePaint = new Paint(param1VectorDrawableCompatState.mVPathRenderer.mStrokePaint); 
        this.mTint = param1VectorDrawableCompatState.mTint;
        this.mTintMode = param1VectorDrawableCompatState.mTintMode;
        this.mAutoMirrored = param1VectorDrawableCompatState.mAutoMirrored;
      } 
    }
    
    public boolean canReuseBitmap(int param1Int1, int param1Int2) { return (param1Int1 == this.mCachedBitmap.getWidth() && param1Int2 == this.mCachedBitmap.getHeight()); }
    
    public boolean canReuseCache() { return (!this.mCacheDirty && this.mCachedTint == this.mTint && this.mCachedTintMode == this.mTintMode && this.mCachedAutoMirrored == this.mAutoMirrored && this.mCachedRootAlpha == this.mVPathRenderer.getRootAlpha()); }
    
    public void createCachedBitmapIfNeeded(int param1Int1, int param1Int2) {
      if (this.mCachedBitmap == null || !canReuseBitmap(param1Int1, param1Int2)) {
        this.mCachedBitmap = Bitmap.createBitmap(param1Int1, param1Int2, Bitmap.Config.ARGB_8888);
        this.mCacheDirty = true;
      } 
    }
    
    public void drawCachedBitmapWithRootAlpha(Canvas param1Canvas, ColorFilter param1ColorFilter, Rect param1Rect) {
      Paint paint = getPaint(param1ColorFilter);
      param1Canvas.drawBitmap(this.mCachedBitmap, null, param1Rect, paint);
    }
    
    public int getChangingConfigurations() { return this.mChangingConfigurations; }
    
    public Paint getPaint(ColorFilter param1ColorFilter) {
      if (!hasTranslucentRoot() && param1ColorFilter == null)
        return null; 
      if (this.mTempPaint == null) {
        this.mTempPaint = new Paint();
        this.mTempPaint.setFilterBitmap(true);
      } 
      this.mTempPaint.setAlpha(this.mVPathRenderer.getRootAlpha());
      this.mTempPaint.setColorFilter(param1ColorFilter);
      return this.mTempPaint;
    }
    
    public boolean hasTranslucentRoot() { return (this.mVPathRenderer.getRootAlpha() < 255); }
    
    public boolean isStateful() { return this.mVPathRenderer.isStateful(); }
    
    @NonNull
    public Drawable newDrawable() { return new VectorDrawableCompat(this); }
    
    @NonNull
    public Drawable newDrawable(Resources param1Resources) { return new VectorDrawableCompat(this); }
    
    public boolean onStateChanged(int[] param1ArrayOfInt) {
      boolean bool = this.mVPathRenderer.onStateChanged(param1ArrayOfInt);
      this.mCacheDirty |= bool;
      return bool;
    }
    
    public void updateCacheStates() {
      this.mCachedTint = this.mTint;
      this.mCachedTintMode = this.mTintMode;
      this.mCachedRootAlpha = this.mVPathRenderer.getRootAlpha();
      this.mCachedAutoMirrored = this.mAutoMirrored;
      this.mCacheDirty = false;
    }
    
    public void updateCachedBitmap(int param1Int1, int param1Int2) {
      this.mCachedBitmap.eraseColor(0);
      Canvas canvas = new Canvas(this.mCachedBitmap);
      this.mVPathRenderer.draw(canvas, param1Int1, param1Int2, null);
    }
  }
  
  @RequiresApi(24)
  private static class VectorDrawableDelegateState extends Drawable.ConstantState {
    private final Drawable.ConstantState mDelegateState;
    
    public VectorDrawableDelegateState(Drawable.ConstantState param1ConstantState) { this.mDelegateState = param1ConstantState; }
    
    public boolean canApplyTheme() { return this.mDelegateState.canApplyTheme(); }
    
    public int getChangingConfigurations() { return this.mDelegateState.getChangingConfigurations(); }
    
    public Drawable newDrawable() {
      VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
      vectorDrawableCompat.mDelegateDrawable = (VectorDrawable)this.mDelegateState.newDrawable();
      return vectorDrawableCompat;
    }
    
    public Drawable newDrawable(Resources param1Resources) {
      VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
      vectorDrawableCompat.mDelegateDrawable = (VectorDrawable)this.mDelegateState.newDrawable(param1Resources);
      return vectorDrawableCompat;
    }
    
    public Drawable newDrawable(Resources param1Resources, Resources.Theme param1Theme) {
      VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
      vectorDrawableCompat.mDelegateDrawable = (VectorDrawable)this.mDelegateState.newDrawable(param1Resources, param1Theme);
      return vectorDrawableCompat;
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\graphics\drawable\VectorDrawableCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */