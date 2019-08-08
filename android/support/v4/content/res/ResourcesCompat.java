package android.support.v4.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.util.Preconditions;
import android.util.Log;
import android.util.TypedValue;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

public final class ResourcesCompat {
  private static final String TAG = "ResourcesCompat";
  
  @ColorInt
  public static int getColor(@NonNull Resources paramResources, @ColorRes int paramInt, @Nullable Resources.Theme paramTheme) throws Resources.NotFoundException { return (Build.VERSION.SDK_INT >= 23) ? paramResources.getColor(paramInt, paramTheme) : paramResources.getColor(paramInt); }
  
  @Nullable
  public static ColorStateList getColorStateList(@NonNull Resources paramResources, @ColorRes int paramInt, @Nullable Resources.Theme paramTheme) throws Resources.NotFoundException { return (Build.VERSION.SDK_INT >= 23) ? paramResources.getColorStateList(paramInt, paramTheme) : paramResources.getColorStateList(paramInt); }
  
  @Nullable
  public static Drawable getDrawable(@NonNull Resources paramResources, @DrawableRes int paramInt, @Nullable Resources.Theme paramTheme) throws Resources.NotFoundException { return (Build.VERSION.SDK_INT >= 21) ? paramResources.getDrawable(paramInt, paramTheme) : paramResources.getDrawable(paramInt); }
  
  @Nullable
  public static Drawable getDrawableForDensity(@NonNull Resources paramResources, @DrawableRes int paramInt1, int paramInt2, @Nullable Resources.Theme paramTheme) throws Resources.NotFoundException { return (Build.VERSION.SDK_INT >= 21) ? paramResources.getDrawableForDensity(paramInt1, paramInt2, paramTheme) : ((Build.VERSION.SDK_INT >= 15) ? paramResources.getDrawableForDensity(paramInt1, paramInt2) : paramResources.getDrawable(paramInt1)); }
  
  @Nullable
  public static Typeface getFont(@NonNull Context paramContext, @FontRes int paramInt) throws Resources.NotFoundException { return paramContext.isRestricted() ? null : loadFont(paramContext, paramInt, new TypedValue(), 0, null, null, false); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static Typeface getFont(@NonNull Context paramContext, @FontRes int paramInt1, TypedValue paramTypedValue, int paramInt2, @Nullable FontCallback paramFontCallback) throws Resources.NotFoundException { return paramContext.isRestricted() ? null : loadFont(paramContext, paramInt1, paramTypedValue, paramInt2, paramFontCallback, null, true); }
  
  public static void getFont(@NonNull Context paramContext, @FontRes int paramInt, @NonNull FontCallback paramFontCallback, @Nullable Handler paramHandler) throws Resources.NotFoundException {
    Preconditions.checkNotNull(paramFontCallback);
    if (paramContext.isRestricted()) {
      paramFontCallback.callbackFailAsync(-4, paramHandler);
      return;
    } 
    loadFont(paramContext, paramInt, new TypedValue(), 0, paramFontCallback, paramHandler, false);
  }
  
  private static Typeface loadFont(@NonNull Context paramContext, int paramInt1, TypedValue paramTypedValue, int paramInt2, @Nullable FontCallback paramFontCallback, @Nullable Handler paramHandler, boolean paramBoolean) {
    Resources resources = paramContext.getResources();
    resources.getValue(paramInt1, paramTypedValue, true);
    StringBuilder stringBuilder = loadFont(paramContext, resources, paramTypedValue, paramInt1, paramInt2, paramFontCallback, paramHandler, paramBoolean);
    if (stringBuilder == null) {
      if (paramFontCallback != null)
        return stringBuilder; 
      stringBuilder = new StringBuilder();
      stringBuilder.append("Font resource ID #0x");
      stringBuilder.append(Integer.toHexString(paramInt1));
      stringBuilder.append(" could not be retrieved.");
      throw new Resources.NotFoundException(stringBuilder.toString());
    } 
    return stringBuilder;
  }
  
  private static Typeface loadFont(@NonNull Context paramContext, Resources paramResources, TypedValue paramTypedValue, int paramInt1, int paramInt2, @Nullable FontCallback paramFontCallback, @Nullable Handler paramHandler, boolean paramBoolean) {
    Resources resources;
    StringBuilder stringBuilder2;
    if (paramTypedValue.string != null) {
      String str = paramTypedValue.string.toString();
      if (!str.startsWith("res/")) {
        if (paramFontCallback != null)
          paramFontCallback.callbackFailAsync(-3, paramHandler); 
        return null;
      } 
      resources = TypefaceCompat.findFromCache(paramResources, paramInt1, paramInt2);
      if (resources != null) {
        if (paramFontCallback != null)
          paramFontCallback.callbackSuccessAsync(resources, paramHandler); 
        return resources;
      } 
      try {
        boolean bool = str.toLowerCase().endsWith(".xml");
        if (bool) {
          try {
            FontResourcesParserCompat.FamilyResourceEntry familyResourceEntry = FontResourcesParserCompat.parse(paramResources.getXml(paramInt1), paramResources);
            if (familyResourceEntry == null) {
              try {
                Log.e("ResourcesCompat", "Failed to find font-family tag");
                if (paramFontCallback != null)
                  paramFontCallback.callbackFailAsync(-3, paramHandler); 
                return null;
              } catch (XmlPullParserException paramContext) {
              
              } catch (IOException paramContext) {}
            } else {
              try {
                return TypefaceCompat.createFromResourcesFamilyXml(paramContext, familyResourceEntry, paramResources, paramInt1, paramInt2, paramFontCallback, paramHandler, paramBoolean);
              } catch (XmlPullParserException paramContext) {
              
              } catch (IOException paramContext) {}
            } 
          } catch (XmlPullParserException paramContext) {
          
          } catch (IOException paramContext) {}
        } else {
          try {
            Resources resources1 = TypefaceCompat.createFromResourcesFontFile(paramContext, paramResources, paramInt1, str, paramInt2);
            if (paramFontCallback != null) {
              if (resources1 != null) {
                try {
                  paramFontCallback.callbackSuccessAsync(resources1, paramHandler);
                  return resources1;
                } catch (XmlPullParserException paramResources) {
                  resources = resources1;
                  resources1 = paramResources;
                } catch (IOException paramResources) {
                  resources = resources1;
                  resources1 = paramResources;
                } 
              } else {
                paramFontCallback.callbackFailAsync(-3, paramHandler);
                return resources1;
              } 
            } else {
              return resources1;
            } 
          } catch (XmlPullParserException paramContext) {
          
          } catch (IOException paramContext) {}
        } 
      } catch (XmlPullParserException paramContext) {
      
      } catch (IOException paramContext) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to read xml resource ");
        stringBuilder.append(str);
        Log.e("ResourcesCompat", stringBuilder.toString(), paramContext);
      } 
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append("Failed to parse xml resource ");
      stringBuilder2.append(str);
      Log.e("ResourcesCompat", stringBuilder2.toString(), paramContext);
      if (paramFontCallback != null)
        paramFontCallback.callbackFailAsync(-3, paramHandler); 
      return null;
    } 
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("Resource \"");
    stringBuilder1.append(stringBuilder2.getResourceName(paramInt1));
    stringBuilder1.append("\" (");
    stringBuilder1.append(Integer.toHexString(paramInt1));
    stringBuilder1.append(") is not a Font: ");
    stringBuilder1.append(resources);
    throw new Resources.NotFoundException(stringBuilder1.toString());
  }
  
  public static abstract class FontCallback {
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public final void callbackFailAsync(final int reason, @Nullable Handler param1Handler) {
      Handler handler = param1Handler;
      if (param1Handler == null)
        handler = new Handler(Looper.getMainLooper()); 
      handler.post(new Runnable() {
            public void run() { ResourcesCompat.FontCallback.this.onFontRetrievalFailed(reason); }
          });
    }
    
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public final void callbackSuccessAsync(final Typeface typeface, @Nullable Handler param1Handler) {
      Handler handler = param1Handler;
      if (param1Handler == null)
        handler = new Handler(Looper.getMainLooper()); 
      handler.post(new Runnable() {
            public void run() { ResourcesCompat.FontCallback.this.onFontRetrieved(typeface); }
          });
    }
    
    public abstract void onFontRetrievalFailed(int param1Int);
    
    public abstract void onFontRetrieved(@NonNull Typeface param1Typeface);
  }
  
  class null implements Runnable {
    public void run() { this.this$0.onFontRetrieved(typeface); }
  }
  
  class null implements Runnable {
    public void run() { this.this$0.onFontRetrievalFailed(reason); }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\content\res\ResourcesCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */