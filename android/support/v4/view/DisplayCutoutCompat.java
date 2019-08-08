package android.support.v4.view;

import android.graphics.Rect;
import android.os.Build;
import android.view.DisplayCutout;
import java.util.List;

public final class DisplayCutoutCompat {
  private final Object mDisplayCutout;
  
  public DisplayCutoutCompat(Rect paramRect, List<Rect> paramList) { this(paramRect); }
  
  private DisplayCutoutCompat(Object paramObject) { this.mDisplayCutout = paramObject; }
  
  static DisplayCutoutCompat wrap(Object paramObject) { return (paramObject == null) ? null : new DisplayCutoutCompat(paramObject); }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject != null) {
      if (getClass() != paramObject.getClass())
        return false; 
      paramObject = (DisplayCutoutCompat)paramObject;
      Object object = this.mDisplayCutout;
      return (object == null) ? ((paramObject.mDisplayCutout == null)) : object.equals(paramObject.mDisplayCutout);
    } 
    return false;
  }
  
  public List<Rect> getBoundingRects() { return (Build.VERSION.SDK_INT >= 28) ? ((DisplayCutout)this.mDisplayCutout).getBoundingRects() : null; }
  
  public int getSafeInsetBottom() { return (Build.VERSION.SDK_INT >= 28) ? ((DisplayCutout)this.mDisplayCutout).getSafeInsetBottom() : 0; }
  
  public int getSafeInsetLeft() { return (Build.VERSION.SDK_INT >= 28) ? ((DisplayCutout)this.mDisplayCutout).getSafeInsetLeft() : 0; }
  
  public int getSafeInsetRight() { return (Build.VERSION.SDK_INT >= 28) ? ((DisplayCutout)this.mDisplayCutout).getSafeInsetRight() : 0; }
  
  public int getSafeInsetTop() { return (Build.VERSION.SDK_INT >= 28) ? ((DisplayCutout)this.mDisplayCutout).getSafeInsetTop() : 0; }
  
  public int hashCode() {
    Object object = this.mDisplayCutout;
    return (object == null) ? 0 : object.hashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("DisplayCutoutCompat{");
    stringBuilder.append(this.mDisplayCutout);
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\view\DisplayCutoutCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */