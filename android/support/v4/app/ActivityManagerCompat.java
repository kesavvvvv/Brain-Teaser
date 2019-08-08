package android.support.v4.app;

import android.app.ActivityManager;
import android.os.Build;
import android.support.annotation.NonNull;

public final class ActivityManagerCompat {
  public static boolean isLowRamDevice(@NonNull ActivityManager paramActivityManager) { return (Build.VERSION.SDK_INT >= 19) ? paramActivityManager.isLowRamDevice() : 0; }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\app\ActivityManagerCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */