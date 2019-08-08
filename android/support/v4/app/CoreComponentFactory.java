package android.support.v4.app;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;

@RequiresApi(api = 28)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class CoreComponentFactory extends AppComponentFactory {
  private static final String TAG = "CoreComponentFactory";
  
  static <T> T checkCompatWrapper(T paramT) {
    if (paramT instanceof CompatWrapped) {
      Object object = ((CompatWrapped)paramT).getWrapper();
      if (object != null)
        return (T)object; 
    } 
    return paramT;
  }
  
  public Activity instantiateActivity(ClassLoader paramClassLoader, String paramString, Intent paramIntent) throws InstantiationException, IllegalAccessException, ClassNotFoundException { return (Activity)checkCompatWrapper(super.instantiateActivity(paramClassLoader, paramString, paramIntent)); }
  
  public Application instantiateApplication(ClassLoader paramClassLoader, String paramString) throws InstantiationException, IllegalAccessException, ClassNotFoundException { return (Application)checkCompatWrapper(super.instantiateApplication(paramClassLoader, paramString)); }
  
  public ContentProvider instantiateProvider(ClassLoader paramClassLoader, String paramString) throws InstantiationException, IllegalAccessException, ClassNotFoundException { return (ContentProvider)checkCompatWrapper(super.instantiateProvider(paramClassLoader, paramString)); }
  
  public BroadcastReceiver instantiateReceiver(ClassLoader paramClassLoader, String paramString, Intent paramIntent) throws InstantiationException, IllegalAccessException, ClassNotFoundException { return (BroadcastReceiver)checkCompatWrapper(super.instantiateReceiver(paramClassLoader, paramString, paramIntent)); }
  
  public Service instantiateService(ClassLoader paramClassLoader, String paramString, Intent paramIntent) throws InstantiationException, IllegalAccessException, ClassNotFoundException { return (Service)checkCompatWrapper(super.instantiateService(paramClassLoader, paramString, paramIntent)); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static interface CompatWrapped {
    Object getWrapper();
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\app\CoreComponentFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */