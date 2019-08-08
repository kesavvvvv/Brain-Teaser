package android.support.v4.app;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import java.lang.reflect.InvocationTargetException;

@RequiresApi(28)
public class AppComponentFactory extends AppComponentFactory {
  public final Activity instantiateActivity(ClassLoader paramClassLoader, String paramString, Intent paramIntent) throws InstantiationException, IllegalAccessException, ClassNotFoundException { return (Activity)CoreComponentFactory.checkCompatWrapper(instantiateActivityCompat(paramClassLoader, paramString, paramIntent)); }
  
  @NonNull
  public Activity instantiateActivityCompat(@NonNull ClassLoader paramClassLoader, @NonNull String paramString, @Nullable Intent paramIntent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    try {
      return (Activity)paramClassLoader.loadClass(paramString).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    } catch (InvocationTargetException paramClassLoader) {
    
    } catch (NoSuchMethodException paramClassLoader) {}
    throw new RuntimeException("Couldn't call constructor", paramClassLoader);
  }
  
  public final Application instantiateApplication(ClassLoader paramClassLoader, String paramString) throws InstantiationException, IllegalAccessException, ClassNotFoundException { return (Application)CoreComponentFactory.checkCompatWrapper(instantiateApplicationCompat(paramClassLoader, paramString)); }
  
  @NonNull
  public Application instantiateApplicationCompat(@NonNull ClassLoader paramClassLoader, @NonNull String paramString) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    try {
      return (Application)paramClassLoader.loadClass(paramString).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    } catch (InvocationTargetException paramClassLoader) {
    
    } catch (NoSuchMethodException paramClassLoader) {}
    throw new RuntimeException("Couldn't call constructor", paramClassLoader);
  }
  
  public final ContentProvider instantiateProvider(ClassLoader paramClassLoader, String paramString) throws InstantiationException, IllegalAccessException, ClassNotFoundException { return (ContentProvider)CoreComponentFactory.checkCompatWrapper(instantiateProviderCompat(paramClassLoader, paramString)); }
  
  @NonNull
  public ContentProvider instantiateProviderCompat(@NonNull ClassLoader paramClassLoader, @NonNull String paramString) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    try {
      return (ContentProvider)paramClassLoader.loadClass(paramString).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    } catch (InvocationTargetException paramClassLoader) {
    
    } catch (NoSuchMethodException paramClassLoader) {}
    throw new RuntimeException("Couldn't call constructor", paramClassLoader);
  }
  
  public final BroadcastReceiver instantiateReceiver(ClassLoader paramClassLoader, String paramString, Intent paramIntent) throws InstantiationException, IllegalAccessException, ClassNotFoundException { return (BroadcastReceiver)CoreComponentFactory.checkCompatWrapper(instantiateReceiverCompat(paramClassLoader, paramString, paramIntent)); }
  
  @NonNull
  public BroadcastReceiver instantiateReceiverCompat(@NonNull ClassLoader paramClassLoader, @NonNull String paramString, @Nullable Intent paramIntent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    try {
      return (BroadcastReceiver)paramClassLoader.loadClass(paramString).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    } catch (InvocationTargetException paramClassLoader) {
    
    } catch (NoSuchMethodException paramClassLoader) {}
    throw new RuntimeException("Couldn't call constructor", paramClassLoader);
  }
  
  public final Service instantiateService(ClassLoader paramClassLoader, String paramString, Intent paramIntent) throws InstantiationException, IllegalAccessException, ClassNotFoundException { return (Service)CoreComponentFactory.checkCompatWrapper(instantiateServiceCompat(paramClassLoader, paramString, paramIntent)); }
  
  @NonNull
  public Service instantiateServiceCompat(@NonNull ClassLoader paramClassLoader, @NonNull String paramString, @Nullable Intent paramIntent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    try {
      return (Service)paramClassLoader.loadClass(paramString).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    } catch (InvocationTargetException paramClassLoader) {
    
    } catch (NoSuchMethodException paramClassLoader) {}
    throw new RuntimeException("Couldn't call constructor", paramClassLoader);
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\app\AppComponentFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */