package android.support.v4.view;

import android.os.Build;
import android.view.ViewGroup;

public final class MarginLayoutParamsCompat {
  public static int getLayoutDirection(ViewGroup.MarginLayoutParams paramMarginLayoutParams) {
    byte b1;
    if (Build.VERSION.SDK_INT >= 17) {
      b1 = paramMarginLayoutParams.getLayoutDirection();
    } else {
      b1 = 0;
    } 
    byte b2 = b1;
    if (b1) {
      b2 = b1;
      if (b1 != 1)
        b2 = 0; 
    } 
    return b2;
  }
  
  public static int getMarginEnd(ViewGroup.MarginLayoutParams paramMarginLayoutParams) { return (Build.VERSION.SDK_INT >= 17) ? paramMarginLayoutParams.getMarginEnd() : paramMarginLayoutParams.rightMargin; }
  
  public static int getMarginStart(ViewGroup.MarginLayoutParams paramMarginLayoutParams) { return (Build.VERSION.SDK_INT >= 17) ? paramMarginLayoutParams.getMarginStart() : paramMarginLayoutParams.leftMargin; }
  
  public static boolean isMarginRelative(ViewGroup.MarginLayoutParams paramMarginLayoutParams) { return (Build.VERSION.SDK_INT >= 17) ? paramMarginLayoutParams.isMarginRelative() : 0; }
  
  public static void resolveLayoutDirection(ViewGroup.MarginLayoutParams paramMarginLayoutParams, int paramInt) {
    if (Build.VERSION.SDK_INT >= 17)
      paramMarginLayoutParams.resolveLayoutDirection(paramInt); 
  }
  
  public static void setLayoutDirection(ViewGroup.MarginLayoutParams paramMarginLayoutParams, int paramInt) {
    if (Build.VERSION.SDK_INT >= 17)
      paramMarginLayoutParams.setLayoutDirection(paramInt); 
  }
  
  public static void setMarginEnd(ViewGroup.MarginLayoutParams paramMarginLayoutParams, int paramInt) {
    if (Build.VERSION.SDK_INT >= 17) {
      paramMarginLayoutParams.setMarginEnd(paramInt);
      return;
    } 
    paramMarginLayoutParams.rightMargin = paramInt;
  }
  
  public static void setMarginStart(ViewGroup.MarginLayoutParams paramMarginLayoutParams, int paramInt) {
    if (Build.VERSION.SDK_INT >= 17) {
      paramMarginLayoutParams.setMarginStart(paramInt);
      return;
    } 
    paramMarginLayoutParams.leftMargin = paramInt;
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\view\MarginLayoutParamsCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */