package android.support.v4.graphics;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;

public final class BitmapCompat {
  public static int getAllocationByteCount(@NonNull Bitmap paramBitmap) { return (Build.VERSION.SDK_INT >= 19) ? paramBitmap.getAllocationByteCount() : paramBitmap.getByteCount(); }
  
  public static boolean hasMipMap(@NonNull Bitmap paramBitmap) { return (Build.VERSION.SDK_INT >= 18) ? paramBitmap.hasMipMap() : 0; }
  
  public static void setHasMipMap(@NonNull Bitmap paramBitmap, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 18)
      paramBitmap.setHasMipMap(paramBoolean); 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\graphics\BitmapCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */