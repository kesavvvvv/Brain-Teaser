package android.support.v7.app;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.LongSparseArray;
import java.lang.reflect.Field;
import java.util.Map;

class ResourcesFlusher {
  private static final String TAG = "ResourcesFlusher";
  
  private static Field sDrawableCacheField;
  
  private static boolean sDrawableCacheFieldFetched;
  
  private static Field sResourcesImplField;
  
  private static boolean sResourcesImplFieldFetched;
  
  private static Class sThemedResourceCacheClazz;
  
  private static boolean sThemedResourceCacheClazzFetched;
  
  private static Field sThemedResourceCache_mUnthemedEntriesField;
  
  private static boolean sThemedResourceCache_mUnthemedEntriesFieldFetched;
  
  static void flush(@NonNull Resources paramResources) {
    if (Build.VERSION.SDK_INT >= 28)
      return; 
    if (Build.VERSION.SDK_INT >= 24) {
      flushNougats(paramResources);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 23) {
      flushMarshmallows(paramResources);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 21)
      flushLollipops(paramResources); 
  }
  
  @RequiresApi(21)
  private static void flushLollipops(@NonNull Resources paramResources) {
    if (!sDrawableCacheFieldFetched) {
      try {
        sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
        sDrawableCacheField.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {
        Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", noSuchFieldException);
      } 
      sDrawableCacheFieldFetched = true;
    } 
    Field field = sDrawableCacheField;
    if (field != null) {
      Resources resources = null;
      try {
        Map map = (Map)field.get(paramResources);
      } catch (IllegalAccessException paramResources) {
        Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", paramResources);
        paramResources = resources;
      } 
      if (paramResources != null) {
        paramResources.clear();
        return;
      } 
      return;
    } 
  }
  
  @RequiresApi(23)
  private static void flushMarshmallows(@NonNull Resources paramResources) {
    if (!sDrawableCacheFieldFetched) {
      try {
        sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
        sDrawableCacheField.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {
        Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", noSuchFieldException);
      } 
      sDrawableCacheFieldFetched = true;
    } 
    Resources resources = null;
    Field field = sDrawableCacheField;
    if (field != null) {
      try {
        Object object = field.get(paramResources);
      } catch (IllegalAccessException paramResources) {
        Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", paramResources);
        paramResources = resources;
      } 
    } else {
      paramResources = resources;
    } 
    if (paramResources == null)
      return; 
    flushThemedResourcesCache(paramResources);
  }
  
  @RequiresApi(24)
  private static void flushNougats(@NonNull Resources paramResources) {
    if (!sResourcesImplFieldFetched) {
      try {
        sResourcesImplField = Resources.class.getDeclaredField("mResourcesImpl");
        sResourcesImplField.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {
        Log.e("ResourcesFlusher", "Could not retrieve Resources#mResourcesImpl field", noSuchFieldException);
      } 
      sResourcesImplFieldFetched = true;
    } 
    Field field = sResourcesImplField;
    if (field == null)
      return; 
    resources = null;
    try {
      Object object = field.get(paramResources);
    } catch (IllegalAccessException paramResources) {
      Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mResourcesImpl", paramResources);
      paramResources = resources;
    } 
    if (paramResources == null)
      return; 
    if (!sDrawableCacheFieldFetched) {
      try {
        sDrawableCacheField = paramResources.getClass().getDeclaredField("mDrawableCache");
        sDrawableCacheField.setAccessible(true);
      } catch (NoSuchFieldException resources) {
        Log.e("ResourcesFlusher", "Could not retrieve ResourcesImpl#mDrawableCache field", resources);
      } 
      sDrawableCacheFieldFetched = true;
    } 
    resources = null;
    field = sDrawableCacheField;
    if (field != null) {
      try {
        Object object = field.get(paramResources);
      } catch (IllegalAccessException paramResources) {
        Log.e("ResourcesFlusher", "Could not retrieve value from ResourcesImpl#mDrawableCache", paramResources);
        paramResources = resources;
      } 
    } else {
      paramResources = resources;
    } 
    if (paramResources != null) {
      flushThemedResourcesCache(paramResources);
      return;
    } 
  }
  
  @RequiresApi(16)
  private static void flushThemedResourcesCache(@NonNull Object paramObject) {
    if (!sThemedResourceCacheClazzFetched) {
      try {
        sThemedResourceCacheClazz = Class.forName("android.content.res.ThemedResourceCache");
      } catch (ClassNotFoundException classNotFoundException) {
        Log.e("ResourcesFlusher", "Could not find ThemedResourceCache class", classNotFoundException);
      } 
      sThemedResourceCacheClazzFetched = true;
    } 
    clazz = sThemedResourceCacheClazz;
    if (clazz == null)
      return; 
    if (!sThemedResourceCache_mUnthemedEntriesFieldFetched) {
      try {
        sThemedResourceCache_mUnthemedEntriesField = clazz.getDeclaredField("mUnthemedEntries");
        sThemedResourceCache_mUnthemedEntriesField.setAccessible(true);
      } catch (NoSuchFieldException clazz) {
        Log.e("ResourcesFlusher", "Could not retrieve ThemedResourceCache#mUnthemedEntries field", clazz);
      } 
      sThemedResourceCache_mUnthemedEntriesFieldFetched = true;
    } 
    Field field = sThemedResourceCache_mUnthemedEntriesField;
    if (field == null)
      return; 
    clazz = null;
    try {
      paramObject = (LongSparseArray)field.get(paramObject);
    } catch (IllegalAccessException paramObject) {
      Log.e("ResourcesFlusher", "Could not retrieve value from ThemedResourceCache#mUnthemedEntries", paramObject);
      paramObject = clazz;
    } 
    if (paramObject != null) {
      paramObject.clear();
      return;
    } 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\app\ResourcesFlusher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */