package android.support.v4.graphics;

import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.Collection;

public final class PathUtils {
  @NonNull
  @RequiresApi(26)
  public static Collection<PathSegment> flatten(@NonNull Path paramPath) { return flatten(paramPath, 0.5F); }
  
  @NonNull
  @RequiresApi(26)
  public static Collection<PathSegment> flatten(@NonNull Path paramPath, @FloatRange(from = 0.0D) float paramFloat) {
    float[] arrayOfFloat = paramPath.approximate(paramFloat);
    int i = arrayOfFloat.length / 3;
    ArrayList arrayList = new ArrayList(i);
    byte b;
    for (b = 1; b < i; b++) {
      byte b1 = b * 3;
      byte b2 = (b - true) * 3;
      paramFloat = arrayOfFloat[b1];
      float f1 = arrayOfFloat[b1 + 1];
      float f2 = arrayOfFloat[b1 + 2];
      float f3 = arrayOfFloat[b2];
      float f4 = arrayOfFloat[b2 + 1];
      float f5 = arrayOfFloat[b2 + 2];
      if (paramFloat != f3 && (f1 != f4 || f2 != f5))
        arrayList.add(new PathSegment(new PointF(f4, f5), f3, new PointF(f1, f2), paramFloat)); 
    } 
    return arrayList;
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\graphics\PathUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */