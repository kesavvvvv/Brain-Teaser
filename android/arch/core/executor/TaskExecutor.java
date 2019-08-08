package android.arch.core.executor;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class TaskExecutor {
  public abstract void executeOnDiskIO(@NonNull Runnable paramRunnable);
  
  public void executeOnMainThread(@NonNull Runnable paramRunnable) {
    if (isMainThread()) {
      paramRunnable.run();
      return;
    } 
    postToMainThread(paramRunnable);
  }
  
  public abstract boolean isMainThread();
  
  public abstract void postToMainThread(@NonNull Runnable paramRunnable);
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\arch\core\executor\TaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */