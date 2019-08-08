package android.support.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Xml;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class AnimationUtilsCompat {
  private static Interpolator createInterpolatorFromXml(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser) throws XmlPullParserException, IOException {
    PathInterpolatorCompat pathInterpolatorCompat;
    paramResources = null;
    int i = paramXmlPullParser.getDepth();
    while (true) {
      int j = paramXmlPullParser.next();
      if ((j != 3 || paramXmlPullParser.getDepth() > i) && j != 1) {
        if (j != 2)
          continue; 
        pathInterpolatorCompat = Xml.asAttributeSet(paramXmlPullParser);
        String str = paramXmlPullParser.getName();
        if (str.equals("linearInterpolator")) {
          pathInterpolatorCompat = new LinearInterpolator();
          continue;
        } 
        if (str.equals("accelerateInterpolator")) {
          pathInterpolatorCompat = new AccelerateInterpolator(paramContext, pathInterpolatorCompat);
          continue;
        } 
        if (str.equals("decelerateInterpolator")) {
          DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(paramContext, pathInterpolatorCompat);
          continue;
        } 
        if (str.equals("accelerateDecelerateInterpolator")) {
          pathInterpolatorCompat = new AccelerateDecelerateInterpolator();
          continue;
        } 
        if (str.equals("cycleInterpolator")) {
          pathInterpolatorCompat = new CycleInterpolator(paramContext, pathInterpolatorCompat);
          continue;
        } 
        if (str.equals("anticipateInterpolator")) {
          pathInterpolatorCompat = new AnticipateInterpolator(paramContext, pathInterpolatorCompat);
          continue;
        } 
        if (str.equals("overshootInterpolator")) {
          pathInterpolatorCompat = new OvershootInterpolator(paramContext, pathInterpolatorCompat);
          continue;
        } 
        if (str.equals("anticipateOvershootInterpolator")) {
          AnticipateOvershootInterpolator anticipateOvershootInterpolator = new AnticipateOvershootInterpolator(paramContext, pathInterpolatorCompat);
          continue;
        } 
        if (str.equals("bounceInterpolator")) {
          pathInterpolatorCompat = new BounceInterpolator();
          continue;
        } 
        if (str.equals("pathInterpolator")) {
          pathInterpolatorCompat = new PathInterpolatorCompat(paramContext, pathInterpolatorCompat, paramXmlPullParser);
          continue;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown interpolator name: ");
        stringBuilder.append(paramXmlPullParser.getName());
        throw new RuntimeException(stringBuilder.toString());
      } 
      break;
    } 
    return pathInterpolatorCompat;
  }
  
  public static Interpolator loadInterpolator(Context paramContext, int paramInt) throws Resources.NotFoundException {
    Interpolator interpolator;
    if (Build.VERSION.SDK_INT >= 21)
      return AnimationUtils.loadInterpolator(paramContext, paramInt); 
    XmlResourceParser xmlResourceParser2 = null;
    XmlResourceParser xmlResourceParser3 = null;
    XmlResourceParser xmlResourceParser1 = null;
    if (paramInt == 17563663) {
      try {
        interpolator = new FastOutLinearInInterpolator();
        if (false)
          throw new NullPointerException(); 
        return interpolator;
      } catch (XmlPullParserException paramContext) {
      
      } catch (IOException paramContext) {
      
      } finally {}
    } else {
      LinearOutSlowInInterpolator linearOutSlowInInterpolator;
      if (paramInt == 17563661) {
        linearOutSlowInInterpolator = new FastOutSlowInInterpolator();
        if (false)
          throw new NullPointerException(); 
        return linearOutSlowInInterpolator;
      } 
      if (paramInt == 17563662) {
        linearOutSlowInInterpolator = new LinearOutSlowInInterpolator();
        if (false)
          throw new NullPointerException(); 
        return linearOutSlowInInterpolator;
      } 
      XmlResourceParser xmlResourceParser = linearOutSlowInInterpolator.getResources().getAnimation(paramInt);
      xmlResourceParser1 = xmlResourceParser;
      xmlResourceParser2 = xmlResourceParser;
      xmlResourceParser3 = xmlResourceParser;
      interpolator = createInterpolatorFromXml(linearOutSlowInInterpolator, linearOutSlowInInterpolator.getResources(), linearOutSlowInInterpolator.getTheme(), xmlResourceParser);
      if (xmlResourceParser != null)
        xmlResourceParser.close(); 
      return interpolator;
    } 
    xmlResourceParser1 = xmlResourceParser2;
    StringBuilder stringBuilder = new StringBuilder();
    xmlResourceParser1 = xmlResourceParser2;
    stringBuilder.append("Can't load animation resource ID #0x");
    xmlResourceParser1 = xmlResourceParser2;
    stringBuilder.append(Integer.toHexString(paramInt));
    xmlResourceParser1 = xmlResourceParser2;
    Resources.NotFoundException notFoundException = new Resources.NotFoundException(stringBuilder.toString());
    xmlResourceParser1 = xmlResourceParser2;
    notFoundException.initCause(interpolator);
    xmlResourceParser1 = xmlResourceParser2;
    throw notFoundException;
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\graphics\drawable\AnimationUtilsCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */