package android.support.v4.os;

import android.os.Build;
import android.os.Trace;

public final class TraceCompat {
  public static void beginSection(String paramString) {
    if (Build.VERSION.SDK_INT >= 18)
      Trace.beginSection(paramString); 
  }
  
  public static void endSection() {
    if (Build.VERSION.SDK_INT >= 18)
      Trace.endSection(); 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\os\TraceCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */