package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.support.annotation.AnimatorRes;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.support.v4.graphics.PathParser.PathDataNode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class AnimatorInflaterCompat {
  private static final boolean DBG_ANIMATOR_INFLATER = false;
  
  private static final int MAX_NUM_POINTS = 100;
  
  private static final String TAG = "AnimatorInflater";
  
  private static final int TOGETHER = 0;
  
  private static final int VALUE_TYPE_COLOR = 3;
  
  private static final int VALUE_TYPE_FLOAT = 0;
  
  private static final int VALUE_TYPE_INT = 1;
  
  private static final int VALUE_TYPE_PATH = 2;
  
  private static final int VALUE_TYPE_UNDEFINED = 4;
  
  private static Animator createAnimatorFromXml(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, float paramFloat) throws XmlPullParserException, IOException { return createAnimatorFromXml(paramContext, paramResources, paramTheme, paramXmlPullParser, Xml.asAttributeSet(paramXmlPullParser), null, 0, paramFloat); }
  
  private static Animator createAnimatorFromXml(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, AnimatorSet paramAnimatorSet, int paramInt, float paramFloat) throws XmlPullParserException, IOException {
    int i = paramXmlPullParser.getDepth();
    AnimatorSet animatorSet = null;
    ArrayList arrayList = null;
    while (true) {
      int j = paramXmlPullParser.next();
      if ((j != 3 || paramXmlPullParser.getDepth() > i) && j != 1) {
        if (j != 2)
          continue; 
        ArrayList arrayList1 = paramXmlPullParser.getName();
        j = 0;
        if (arrayList1.equals("objectAnimator")) {
          animatorSet = loadObjectAnimator(paramContext, paramResources, paramTheme, paramAttributeSet, paramFloat, paramXmlPullParser);
        } else if (arrayList1.equals("animator")) {
          animatorSet = loadAnimator(paramContext, paramResources, paramTheme, paramAttributeSet, null, paramFloat, paramXmlPullParser);
        } else {
          TypedArray typedArray;
          if (arrayList1.equals("set")) {
            animatorSet = new AnimatorSet();
            typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_ANIMATOR_SET);
            int k = TypedArrayUtils.getNamedInt(typedArray, paramXmlPullParser, "ordering", 0, 0);
            createAnimatorFromXml(paramContext, paramResources, paramTheme, paramXmlPullParser, paramAttributeSet, (AnimatorSet)animatorSet, k, paramFloat);
            typedArray.recycle();
          } else if (typedArray.equals("propertyValuesHolder")) {
            PropertyValuesHolder[] arrayOfPropertyValuesHolder = loadValues(paramContext, paramResources, paramTheme, paramXmlPullParser, Xml.asAttributeSet(paramXmlPullParser));
            if (arrayOfPropertyValuesHolder != null && animatorSet != null && animatorSet instanceof ValueAnimator)
              ((ValueAnimator)animatorSet).setValues(arrayOfPropertyValuesHolder); 
            j = 1;
          } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown animator name: ");
            stringBuilder.append(paramXmlPullParser.getName());
            throw new RuntimeException(stringBuilder.toString());
          } 
        } 
        arrayList1 = arrayList;
        if (paramAnimatorSet != null) {
          arrayList1 = arrayList;
          if (j == 0) {
            arrayList1 = arrayList;
            if (arrayList == null)
              arrayList1 = new ArrayList(); 
            arrayList1.add(animatorSet);
          } 
        } 
        arrayList = arrayList1;
        continue;
      } 
      break;
    } 
    if (paramAnimatorSet != null && arrayList != null) {
      Animator[] arrayOfAnimator = new Animator[arrayList.size()];
      byte b = 0;
      Iterator iterator = arrayList.iterator();
      while (iterator.hasNext()) {
        arrayOfAnimator[b] = (Animator)iterator.next();
        b++;
      } 
      if (paramInt == 0) {
        paramAnimatorSet.playTogether(arrayOfAnimator);
        return animatorSet;
      } 
      paramAnimatorSet.playSequentially(arrayOfAnimator);
    } 
    return animatorSet;
  }
  
  private static Keyframe createNewKeyframe(Keyframe paramKeyframe, float paramFloat) { return (paramKeyframe.getType() == float.class) ? Keyframe.ofFloat(paramFloat) : ((paramKeyframe.getType() == int.class) ? Keyframe.ofInt(paramFloat) : Keyframe.ofObject(paramFloat)); }
  
  private static void distributeKeyframes(Keyframe[] paramArrayOfKeyframe, float paramFloat, int paramInt1, int paramInt2) {
    paramFloat /= (paramInt2 - paramInt1 + 2);
    while (paramInt1 <= paramInt2) {
      paramArrayOfKeyframe[paramInt1].setFraction(paramArrayOfKeyframe[paramInt1 - 1].getFraction() + paramFloat);
      paramInt1++;
    } 
  }
  
  private static void dumpKeyframes(Object[] paramArrayOfObject, String paramString) {
    if (paramArrayOfObject != null) {
      if (paramArrayOfObject.length == 0)
        return; 
      Log.d("AnimatorInflater", paramString);
      int i = paramArrayOfObject.length;
      for (byte b = 0; b < i; b++) {
        String str;
        Keyframe keyframe = (Keyframe)paramArrayOfObject[b];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Keyframe ");
        stringBuilder.append(b);
        stringBuilder.append(": fraction ");
        if (keyframe.getFraction() < 0.0F) {
          paramString = "null";
        } else {
          str = Float.valueOf(keyframe.getFraction());
        } 
        stringBuilder.append(str);
        stringBuilder.append(", ");
        stringBuilder.append(", value : ");
        if (keyframe.hasValue()) {
          Object object = keyframe.getValue();
        } else {
          str = "null";
        } 
        stringBuilder.append(str);
        Log.d("AnimatorInflater", stringBuilder.toString());
      } 
      return;
    } 
  }
  
  private static PropertyValuesHolder getPVH(TypedArray paramTypedArray, int paramInt1, int paramInt2, int paramInt3, String paramString) {
    boolean bool3;
    byte b2;
    byte b1;
    boolean bool2;
    boolean bool1;
    StringBuilder stringBuilder;
    TypedValue typedValue = paramTypedArray.peekValue(paramInt2);
    if (typedValue != null) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (bool1) {
      b1 = typedValue.type;
    } else {
      b1 = 0;
    } 
    typedValue = paramTypedArray.peekValue(paramInt3);
    if (typedValue != null) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (bool2) {
      b2 = typedValue.type;
    } else {
      b2 = 0;
    } 
    if (paramInt1 == 4)
      if ((bool1 && isColorType(b1)) || (bool2 && isColorType(b2))) {
        paramInt1 = 3;
      } else {
        paramInt1 = 0;
      }  
    if (paramInt1 == 0) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    if (paramInt1 == 2) {
      String str1 = paramTypedArray.getString(paramInt2);
      str = paramTypedArray.getString(paramInt3);
      PathParser.PathDataNode[] arrayOfPathDataNode1 = PathParser.createNodesFromPathData(str1);
      PathParser.PathDataNode[] arrayOfPathDataNode2 = PathParser.createNodesFromPathData(str);
      if (arrayOfPathDataNode1 != null || arrayOfPathDataNode2 != null) {
        if (arrayOfPathDataNode1 != null) {
          PathDataEvaluator pathDataEvaluator = new PathDataEvaluator();
          if (arrayOfPathDataNode2 != null) {
            PropertyValuesHolder propertyValuesHolder;
            if (PathParser.canMorph(arrayOfPathDataNode1, arrayOfPathDataNode2)) {
              propertyValuesHolder = PropertyValuesHolder.ofObject(paramString, pathDataEvaluator, new Object[] { arrayOfPathDataNode1, arrayOfPathDataNode2 });
            } else {
              stringBuilder = new StringBuilder();
              stringBuilder.append(" Can't morph from ");
              stringBuilder.append(str1);
              stringBuilder.append(" to ");
              stringBuilder.append(propertyValuesHolder);
              throw new InflateException(stringBuilder.toString());
            } 
          } else {
            PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofObject(stringBuilder, pathDataEvaluator, new Object[] { arrayOfPathDataNode1 });
          } 
        } else {
          if (arrayOfPathDataNode2 != null)
            return PropertyValuesHolder.ofObject(stringBuilder, new PathDataEvaluator(), new Object[] { arrayOfPathDataNode2 }); 
          str = null;
        } 
        return str;
      } 
    } else {
      ArgbEvaluator argbEvaluator;
      PropertyValuesHolder propertyValuesHolder;
      typedValue = null;
      if (paramInt1 == 3)
        argbEvaluator = ArgbEvaluator.getInstance(); 
      if (bool3) {
        if (bool1) {
          float f;
          if (b1 == 5) {
            f = str.getDimension(paramInt2, 0.0F);
          } else {
            f = str.getFloat(paramInt2, 0.0F);
          } 
          if (bool2) {
            float f1;
            if (b2 == 5) {
              f1 = str.getDimension(paramInt3, 0.0F);
            } else {
              f1 = str.getFloat(paramInt3, 0.0F);
            } 
            propertyValuesHolder = PropertyValuesHolder.ofFloat(stringBuilder, new float[] { f, f1 });
          } else {
            propertyValuesHolder = PropertyValuesHolder.ofFloat(stringBuilder, new float[] { f });
          } 
        } else {
          float f;
          if (b2 == 5) {
            f = propertyValuesHolder.getDimension(paramInt3, 0.0F);
          } else {
            f = propertyValuesHolder.getFloat(paramInt3, 0.0F);
          } 
          propertyValuesHolder = PropertyValuesHolder.ofFloat(stringBuilder, new float[] { f });
        } 
      } else if (bool1) {
        if (b1 == 5) {
          paramInt1 = (int)propertyValuesHolder.getDimension(paramInt2, 0.0F);
        } else if (isColorType(b1)) {
          paramInt1 = propertyValuesHolder.getColor(paramInt2, 0);
        } else {
          paramInt1 = propertyValuesHolder.getInt(paramInt2, 0);
        } 
        if (bool2) {
          if (b2 == 5) {
            paramInt2 = (int)propertyValuesHolder.getDimension(paramInt3, 0.0F);
          } else if (isColorType(b2)) {
            paramInt2 = propertyValuesHolder.getColor(paramInt3, 0);
          } else {
            paramInt2 = propertyValuesHolder.getInt(paramInt3, 0);
          } 
          propertyValuesHolder = PropertyValuesHolder.ofInt(stringBuilder, new int[] { paramInt1, paramInt2 });
        } else {
          propertyValuesHolder = PropertyValuesHolder.ofInt(stringBuilder, new int[] { paramInt1 });
        } 
      } else if (bool2) {
        if (b2 == 5) {
          paramInt1 = (int)propertyValuesHolder.getDimension(paramInt3, 0.0F);
        } else if (isColorType(b2)) {
          paramInt1 = propertyValuesHolder.getColor(paramInt3, 0);
        } else {
          paramInt1 = propertyValuesHolder.getInt(paramInt3, 0);
        } 
        propertyValuesHolder = PropertyValuesHolder.ofInt(stringBuilder, new int[] { paramInt1 });
      } else {
        propertyValuesHolder = null;
      } 
      if (propertyValuesHolder != null && argbEvaluator != null)
        propertyValuesHolder.setEvaluator(argbEvaluator); 
      return propertyValuesHolder;
    } 
    String str = null;
  }
  
  private static int inferValueTypeFromValues(TypedArray paramTypedArray, int paramInt1, int paramInt2) {
    byte b;
    TypedValue typedValue2 = paramTypedArray.peekValue(paramInt1);
    int j = 1;
    int i = 0;
    if (typedValue2 != null) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if (paramInt1 != 0) {
      b = typedValue2.type;
    } else {
      b = 0;
    } 
    TypedValue typedValue1 = paramTypedArray.peekValue(paramInt2);
    if (typedValue1 != null) {
      paramInt2 = j;
    } else {
      paramInt2 = 0;
    } 
    if (paramInt2 != 0)
      i = typedValue1.type; 
    return ((paramInt1 != 0 && isColorType(b)) || (paramInt2 != 0 && isColorType(i))) ? 3 : 0;
  }
  
  private static int inferValueTypeOfKeyframe(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, XmlPullParser paramXmlPullParser) {
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_KEYFRAME);
    byte b = 0;
    TypedValue typedValue = TypedArrayUtils.peekNamedValue(typedArray, paramXmlPullParser, "value", 0);
    if (typedValue != null)
      b = 1; 
    if (b && isColorType(typedValue.type)) {
      b = 3;
    } else {
      b = 0;
    } 
    typedArray.recycle();
    return b;
  }
  
  private static boolean isColorType(int paramInt) { return (paramInt >= 28 && paramInt <= 31); }
  
  public static Animator loadAnimator(Context paramContext, @AnimatorRes int paramInt) throws Resources.NotFoundException { return (Build.VERSION.SDK_INT >= 24) ? AnimatorInflater.loadAnimator(paramContext, paramInt) : loadAnimator(paramContext, paramContext.getResources(), paramContext.getTheme(), paramInt); }
  
  public static Animator loadAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, @AnimatorRes int paramInt) throws Resources.NotFoundException { return loadAnimator(paramContext, paramResources, paramTheme, paramInt, 1.0F); }
  
  public static Animator loadAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, @AnimatorRes int paramInt, float paramFloat) throws Resources.NotFoundException {
    XmlResourceParser xmlResourceParser2 = null;
    XmlResourceParser xmlResourceParser3 = null;
    XmlResourceParser xmlResourceParser1 = null;
    try {
      XmlResourceParser xmlResourceParser = paramResources.getAnimation(paramInt);
      xmlResourceParser1 = xmlResourceParser;
      xmlResourceParser2 = xmlResourceParser;
      xmlResourceParser3 = xmlResourceParser;
      Animator animator = createAnimatorFromXml(paramContext, paramResources, paramTheme, xmlResourceParser, paramFloat);
      if (xmlResourceParser != null)
        xmlResourceParser.close(); 
      return animator;
    } catch (XmlPullParserException paramContext) {
      xmlResourceParser1 = xmlResourceParser3;
      StringBuilder stringBuilder = new StringBuilder();
      xmlResourceParser1 = xmlResourceParser3;
      stringBuilder.append("Can't load animation resource ID #0x");
      xmlResourceParser1 = xmlResourceParser3;
      stringBuilder.append(Integer.toHexString(paramInt));
      xmlResourceParser1 = xmlResourceParser3;
      Resources.NotFoundException notFoundException = new Resources.NotFoundException(stringBuilder.toString());
      xmlResourceParser1 = xmlResourceParser3;
      notFoundException.initCause(paramContext);
      xmlResourceParser1 = xmlResourceParser3;
      throw notFoundException;
    } catch (IOException paramContext) {
      xmlResourceParser1 = xmlResourceParser2;
      StringBuilder stringBuilder = new StringBuilder();
      xmlResourceParser1 = xmlResourceParser2;
      stringBuilder.append("Can't load animation resource ID #0x");
      xmlResourceParser1 = xmlResourceParser2;
      stringBuilder.append(Integer.toHexString(paramInt));
      xmlResourceParser1 = xmlResourceParser2;
      Resources.NotFoundException notFoundException = new Resources.NotFoundException(stringBuilder.toString());
      xmlResourceParser1 = xmlResourceParser2;
      notFoundException.initCause(paramContext);
      xmlResourceParser1 = xmlResourceParser2;
      throw notFoundException;
    } finally {}
    if (xmlResourceParser1 != null)
      xmlResourceParser1.close(); 
    throw paramContext;
  }
  
  private static ValueAnimator loadAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, ValueAnimator paramValueAnimator, float paramFloat, XmlPullParser paramXmlPullParser) throws Resources.NotFoundException {
    TypedArray typedArray2 = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_ANIMATOR);
    TypedArray typedArray1 = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
    ValueAnimator valueAnimator = paramValueAnimator;
    if (paramValueAnimator == null)
      valueAnimator = new ValueAnimator(); 
    parseAnimatorFromTypeArray(valueAnimator, typedArray2, typedArray1, paramFloat, paramXmlPullParser);
    int i = TypedArrayUtils.getNamedResourceId(typedArray2, paramXmlPullParser, "interpolator", 0, 0);
    if (i > 0)
      valueAnimator.setInterpolator(AnimationUtilsCompat.loadInterpolator(paramContext, i)); 
    typedArray2.recycle();
    if (typedArray1 != null)
      typedArray1.recycle(); 
    return valueAnimator;
  }
  
  private static Keyframe loadKeyframe(Context paramContext, Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, int paramInt, XmlPullParser paramXmlPullParser) throws XmlPullParserException, IOException {
    boolean bool;
    Keyframe keyframe;
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_KEYFRAME);
    paramResources = null;
    float f = TypedArrayUtils.getNamedFloat(typedArray, paramXmlPullParser, "fraction", 3, -1.0F);
    TypedValue typedValue = TypedArrayUtils.peekNamedValue(typedArray, paramXmlPullParser, "value", 0);
    if (typedValue != null) {
      bool = true;
    } else {
      bool = false;
    } 
    int i = paramInt;
    if (paramInt == 4)
      if (bool && isColorType(typedValue.type)) {
        i = 3;
      } else {
        i = 0;
      }  
    if (bool) {
      if (i != 3) {
        switch (i) {
          case 0:
            keyframe = Keyframe.ofFloat(f, TypedArrayUtils.getNamedFloat(typedArray, paramXmlPullParser, "value", 0, 0.0F));
            break;
          case 1:
            keyframe = Keyframe.ofInt(f, TypedArrayUtils.getNamedInt(typedArray, paramXmlPullParser, "value", 0, 0));
            break;
        } 
      } else {
      
      } 
    } else if (i == 0) {
      keyframe = Keyframe.ofFloat(f);
    } else {
      keyframe = Keyframe.ofInt(f);
    } 
    paramInt = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "interpolator", 1, 0);
    if (paramInt > 0)
      keyframe.setInterpolator(AnimationUtilsCompat.loadInterpolator(paramContext, paramInt)); 
    typedArray.recycle();
    return keyframe;
  }
  
  private static ObjectAnimator loadObjectAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, float paramFloat, XmlPullParser paramXmlPullParser) throws Resources.NotFoundException {
    ObjectAnimator objectAnimator = new ObjectAnimator();
    loadAnimator(paramContext, paramResources, paramTheme, paramAttributeSet, objectAnimator, paramFloat, paramXmlPullParser);
    return objectAnimator;
  }
  
  private static PropertyValuesHolder loadPvh(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, String paramString, int paramInt) throws XmlPullParserException, IOException {
    int j;
    boolean bool = false;
    ArrayList arrayList = null;
    int i = paramInt;
    while (true) {
      paramInt = paramXmlPullParser.next();
      j = paramInt;
      if (paramInt != 3 && j != 1) {
        if (paramXmlPullParser.getName().equals("keyframe")) {
          if (i == 4)
            i = inferValueTypeOfKeyframe(paramResources, paramTheme, Xml.asAttributeSet(paramXmlPullParser), paramXmlPullParser); 
          Keyframe keyframe = loadKeyframe(paramContext, paramResources, paramTheme, Xml.asAttributeSet(paramXmlPullParser), i, paramXmlPullParser);
          ArrayList arrayList1 = arrayList;
          if (keyframe != null) {
            arrayList1 = arrayList;
            if (arrayList == null)
              arrayList1 = new ArrayList(); 
            arrayList1.add(keyframe);
          } 
          paramXmlPullParser.next();
          arrayList = arrayList1;
        } 
        continue;
      } 
      break;
    } 
    if (arrayList != null) {
      paramInt = arrayList.size();
      int k = paramInt;
      if (paramInt > 0) {
        Keyframe keyframe1 = (Keyframe)arrayList.get(0);
        Keyframe keyframe2 = (Keyframe)arrayList.get(k - 1);
        float f = keyframe2.getFraction();
        paramInt = k;
        if (f < 1.0F)
          if (f < 0.0F) {
            keyframe2.setFraction(1.0F);
            paramInt = k;
          } else {
            arrayList.add(arrayList.size(), createNewKeyframe(keyframe2, 1.0F));
            paramInt = k + 1;
          }  
        f = keyframe1.getFraction();
        int m = paramInt;
        if (f != 0.0F)
          if (f < 0.0F) {
            keyframe1.setFraction(0.0F);
            m = paramInt;
          } else {
            arrayList.add(0, createNewKeyframe(keyframe1, 0.0F));
            m = paramInt + 1;
          }  
        Keyframe[] arrayOfKeyframe = new Keyframe[m];
        arrayList.toArray(arrayOfKeyframe);
        k = 0;
        paramInt = j;
        while (k < m) {
          keyframe2 = arrayOfKeyframe[k];
          if (keyframe2.getFraction() < 0.0F)
            if (k == 0) {
              keyframe2.setFraction(0.0F);
            } else if (k == m - 1) {
              keyframe2.setFraction(1.0F);
            } else {
              int i1 = k + 1;
              int n = k;
              j = paramInt;
              for (paramInt = i1; paramInt < m - 1 && arrayOfKeyframe[paramInt].getFraction() < 0.0F; paramInt++)
                n = paramInt; 
              distributeKeyframes(arrayOfKeyframe, arrayOfKeyframe[n + 1].getFraction() - arrayOfKeyframe[k - 1].getFraction(), k, n);
              paramInt = j;
            }  
          k++;
        } 
        PropertyValuesHolder propertyValuesHolder2 = PropertyValuesHolder.ofKeyframe(paramString, arrayOfKeyframe);
        PropertyValuesHolder propertyValuesHolder1 = propertyValuesHolder2;
        if (i == 3) {
          propertyValuesHolder2.setEvaluator(ArgbEvaluator.getInstance());
          return propertyValuesHolder2;
        } 
        return propertyValuesHolder1;
      } 
    } 
    return null;
  }
  
  private static PropertyValuesHolder[] loadValues(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet) throws XmlPullParserException, IOException {
    PropertyValuesHolder[] arrayOfPropertyValuesHolder;
    ArrayList arrayList = null;
    while (true) {
      int i = paramXmlPullParser.getEventType();
      if (i != 3 && i != 1) {
        if (i != 2) {
          paramXmlPullParser.next();
          continue;
        } 
        if (paramXmlPullParser.getName().equals("propertyValuesHolder")) {
          TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
          ArrayList arrayList1 = TypedArrayUtils.getNamedString(typedArray, paramXmlPullParser, "propertyName", 3);
          i = TypedArrayUtils.getNamedInt(typedArray, paramXmlPullParser, "valueType", 2, 4);
          PropertyValuesHolder propertyValuesHolder = loadPvh(paramContext, paramResources, paramTheme, paramXmlPullParser, arrayList1, i);
          if (propertyValuesHolder == null)
            propertyValuesHolder = getPVH(typedArray, i, 0, 1, arrayList1); 
          arrayList1 = arrayList;
          if (propertyValuesHolder != null) {
            arrayList1 = arrayList;
            if (arrayList == null)
              arrayList1 = new ArrayList(); 
            arrayList1.add(propertyValuesHolder);
          } 
          typedArray.recycle();
          arrayList = arrayList1;
        } 
        paramXmlPullParser.next();
        continue;
      } 
      break;
    } 
    paramContext = null;
    if (arrayList != null) {
      int i = arrayList.size();
      PropertyValuesHolder[] arrayOfPropertyValuesHolder1 = new PropertyValuesHolder[i];
      byte b = 0;
      while (true) {
        arrayOfPropertyValuesHolder = arrayOfPropertyValuesHolder1;
        if (b < i) {
          arrayOfPropertyValuesHolder1[b] = (PropertyValuesHolder)arrayList.get(b);
          b++;
          continue;
        } 
        break;
      } 
    } 
    return arrayOfPropertyValuesHolder;
  }
  
  private static void parseAnimatorFromTypeArray(ValueAnimator paramValueAnimator, TypedArray paramTypedArray1, TypedArray paramTypedArray2, float paramFloat, XmlPullParser paramXmlPullParser) {
    long l1 = TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "duration", 1, 300);
    long l2 = TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "startOffset", 2, 0);
    int i = TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "valueType", 7, 4);
    int j = i;
    if (TypedArrayUtils.hasAttribute(paramXmlPullParser, "valueFrom")) {
      j = i;
      if (TypedArrayUtils.hasAttribute(paramXmlPullParser, "valueTo")) {
        int k = i;
        if (i == 4)
          k = inferValueTypeFromValues(paramTypedArray1, 5, 6); 
        PropertyValuesHolder propertyValuesHolder = getPVH(paramTypedArray1, k, 5, 6, "");
        j = k;
        if (propertyValuesHolder != null) {
          paramValueAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder });
          j = k;
        } 
      } 
    } 
    paramValueAnimator.setDuration(l1);
    paramValueAnimator.setStartDelay(l2);
    paramValueAnimator.setRepeatCount(TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "repeatCount", 3, 0));
    paramValueAnimator.setRepeatMode(TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "repeatMode", 4, 1));
    if (paramTypedArray2 != null)
      setupObjectAnimator(paramValueAnimator, paramTypedArray2, j, paramFloat, paramXmlPullParser); 
  }
  
  private static void setupObjectAnimator(ValueAnimator paramValueAnimator, TypedArray paramTypedArray, int paramInt, float paramFloat, XmlPullParser paramXmlPullParser) { // Byte code:
    //   0: aload_0
    //   1: checkcast android/animation/ObjectAnimator
    //   4: astore_0
    //   5: aload_1
    //   6: aload #4
    //   8: ldc_w 'pathData'
    //   11: iconst_1
    //   12: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   15: astore #5
    //   17: aload #5
    //   19: ifnull -> 125
    //   22: aload_1
    //   23: aload #4
    //   25: ldc_w 'propertyXName'
    //   28: iconst_2
    //   29: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   32: astore #6
    //   34: aload_1
    //   35: aload #4
    //   37: ldc_w 'propertyYName'
    //   40: iconst_3
    //   41: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   44: astore #4
    //   46: iload_2
    //   47: iconst_2
    //   48: if_icmpeq -> 56
    //   51: iload_2
    //   52: iconst_4
    //   53: if_icmpne -> 56
    //   56: aload #6
    //   58: ifnonnull -> 106
    //   61: aload #4
    //   63: ifnull -> 69
    //   66: goto -> 106
    //   69: new java/lang/StringBuilder
    //   72: dup
    //   73: invokespecial <init> : ()V
    //   76: astore_0
    //   77: aload_0
    //   78: aload_1
    //   79: invokevirtual getPositionDescription : ()Ljava/lang/String;
    //   82: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: pop
    //   86: aload_0
    //   87: ldc_w ' propertyXName or propertyYName is needed for PathData'
    //   90: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   93: pop
    //   94: new android/view/InflateException
    //   97: dup
    //   98: aload_0
    //   99: invokevirtual toString : ()Ljava/lang/String;
    //   102: invokespecial <init> : (Ljava/lang/String;)V
    //   105: athrow
    //   106: aload #5
    //   108: invokestatic createPathFromPathData : (Ljava/lang/String;)Landroid/graphics/Path;
    //   111: aload_0
    //   112: ldc_w 0.5
    //   115: fload_3
    //   116: fmul
    //   117: aload #6
    //   119: aload #4
    //   121: invokestatic setupPathMotion : (Landroid/graphics/Path;Landroid/animation/ObjectAnimator;FLjava/lang/String;Ljava/lang/String;)V
    //   124: return
    //   125: aload_0
    //   126: aload_1
    //   127: aload #4
    //   129: ldc_w 'propertyName'
    //   132: iconst_0
    //   133: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   136: invokevirtual setPropertyName : (Ljava/lang/String;)V
    //   139: return }
  
  private static void setupPathMotion(Path paramPath, ObjectAnimator paramObjectAnimator, float paramFloat, String paramString1, String paramString2) {
    PathMeasure pathMeasure = new PathMeasure(paramPath, false);
    float f = 0.0F;
    ArrayList arrayList = new ArrayList();
    arrayList.add(Float.valueOf(0.0F));
    do {
      f += pathMeasure.getLength();
      arrayList.add(Float.valueOf(f));
    } while (pathMeasure.nextContour());
    PropertyValuesHolder propertyValuesHolder1 = new PathMeasure(paramPath, false);
    int i = Math.min(100, (int)(f / paramFloat) + 1);
    float[] arrayOfFloat2 = new float[i];
    float[] arrayOfFloat1 = new float[i];
    float[] arrayOfFloat3 = new float[2];
    f /= (i - 1);
    paramFloat = 0.0F;
    byte b2 = 0;
    byte b1 = 0;
    while (b1 < i) {
      propertyValuesHolder1.getPosTan(paramFloat - ((Float)arrayList.get(b2)).floatValue(), arrayOfFloat3, null);
      arrayOfFloat2[b1] = arrayOfFloat3[0];
      arrayOfFloat1[b1] = arrayOfFloat3[1];
      paramFloat += f;
      byte b = b2;
      if (b2 + 1 < arrayList.size()) {
        b = b2;
        if (paramFloat > ((Float)arrayList.get(b2 + 1)).floatValue()) {
          b = b2 + 1;
          propertyValuesHolder1.nextContour();
        } 
      } 
      b1++;
      b2 = b;
    } 
    propertyValuesHolder1 = null;
    arrayList = null;
    if (paramString1 != null)
      propertyValuesHolder1 = PropertyValuesHolder.ofFloat(paramString1, arrayOfFloat2); 
    PropertyValuesHolder propertyValuesHolder2 = arrayList;
    if (paramString2 != null)
      propertyValuesHolder2 = PropertyValuesHolder.ofFloat(paramString2, arrayOfFloat1); 
    if (propertyValuesHolder1 == null) {
      paramObjectAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder2 });
      return;
    } 
    if (propertyValuesHolder2 == null) {
      paramObjectAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder1 });
      return;
    } 
    paramObjectAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder1, propertyValuesHolder2 });
  }
  
  private static class PathDataEvaluator extends Object implements TypeEvaluator<PathParser.PathDataNode[]> {
    private PathParser.PathDataNode[] mNodeArray;
    
    PathDataEvaluator() {}
    
    PathDataEvaluator(PathParser.PathDataNode[] param1ArrayOfPathDataNode) { this.mNodeArray = param1ArrayOfPathDataNode; }
    
    public PathParser.PathDataNode[] evaluate(float param1Float, PathParser.PathDataNode[] param1ArrayOfPathDataNode1, PathParser.PathDataNode[] param1ArrayOfPathDataNode2) {
      if (PathParser.canMorph(param1ArrayOfPathDataNode1, param1ArrayOfPathDataNode2)) {
        PathParser.PathDataNode[] arrayOfPathDataNode = this.mNodeArray;
        if (arrayOfPathDataNode == null || !PathParser.canMorph(arrayOfPathDataNode, param1ArrayOfPathDataNode1))
          this.mNodeArray = PathParser.deepCopyNodes(param1ArrayOfPathDataNode1); 
        byte b;
        for (b = 0; b < param1ArrayOfPathDataNode1.length; b++)
          this.mNodeArray[b].interpolatePathDataNode(param1ArrayOfPathDataNode1[b], param1ArrayOfPathDataNode2[b], param1Float); 
        return this.mNodeArray;
      } 
      throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\graphics\drawable\AnimatorInflaterCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */