package android.support.v4.content.pm;

import android.content.pm.PackageInfo;
import android.os.Build;
import android.support.annotation.NonNull;

public final class PackageInfoCompat {
  public static long getLongVersionCode(@NonNull PackageInfo paramPackageInfo) { return (Build.VERSION.SDK_INT >= 28) ? paramPackageInfo.getLongVersionCode() : paramPackageInfo.versionCode; }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\content\pm\PackageInfoCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */