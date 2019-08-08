package android.support.v4.app;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BundleCompat {
  @Nullable
  public static IBinder getBinder(@NonNull Bundle paramBundle, @Nullable String paramString) { return (Build.VERSION.SDK_INT >= 18) ? paramBundle.getBinder(paramString) : BundleCompatBaseImpl.getBinder(paramBundle, paramString); }
  
  public static void putBinder(@NonNull Bundle paramBundle, @Nullable String paramString, @Nullable IBinder paramIBinder) {
    if (Build.VERSION.SDK_INT >= 18) {
      paramBundle.putBinder(paramString, paramIBinder);
      return;
    } 
    BundleCompatBaseImpl.putBinder(paramBundle, paramString, paramIBinder);
  }
  
  static class BundleCompatBaseImpl {
    private static final String TAG = "BundleCompatBaseImpl";
    
    private static Method sGetIBinderMethod;
    
    private static boolean sGetIBinderMethodFetched;
    
    private static Method sPutIBinderMethod;
    
    private static boolean sPutIBinderMethodFetched;
    
    public static IBinder getBinder(Bundle param1Bundle, String param1String) {
      if (!sGetIBinderMethodFetched) {
        try {
          sGetIBinderMethod = Bundle.class.getMethod("getIBinder", new Class[] { String.class });
          sGetIBinderMethod.setAccessible(true);
        } catch (NoSuchMethodException noSuchMethodException) {
          Log.i("BundleCompatBaseImpl", "Failed to retrieve getIBinder method", noSuchMethodException);
        } 
        sGetIBinderMethodFetched = true;
      } 
      Method method = sGetIBinderMethod;
      if (method != null) {
        try {
          return (IBinder)method.invoke(param1Bundle, new Object[] { param1String });
        } catch (InvocationTargetException param1Bundle) {
        
        } catch (IllegalAccessException param1Bundle) {
        
        } catch (IllegalArgumentException param1Bundle) {}
        Log.i("BundleCompatBaseImpl", "Failed to invoke getIBinder via reflection", param1Bundle);
        sGetIBinderMethod = null;
        return null;
      } 
      return null;
    }
    
    public static void putBinder(Bundle param1Bundle, String param1String, IBinder param1IBinder) {
      if (!sPutIBinderMethodFetched) {
        try {
          sPutIBinderMethod = Bundle.class.getMethod("putIBinder", new Class[] { String.class, IBinder.class });
          sPutIBinderMethod.setAccessible(true);
        } catch (NoSuchMethodException noSuchMethodException) {
          Log.i("BundleCompatBaseImpl", "Failed to retrieve putIBinder method", noSuchMethodException);
        } 
        sPutIBinderMethodFetched = true;
      } 
      Method method = sPutIBinderMethod;
      if (method != null) {
        try {
          method.invoke(param1Bundle, new Object[] { param1String, param1IBinder });
          return;
        } catch (InvocationTargetException param1Bundle) {
        
        } catch (IllegalAccessException param1Bundle) {
        
        } catch (IllegalArgumentException param1Bundle) {}
        Log.i("BundleCompatBaseImpl", "Failed to invoke putIBinder via reflection", param1Bundle);
        sPutIBinderMethod = null;
        return;
      } 
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\app\BundleCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */