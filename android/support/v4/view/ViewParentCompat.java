package android.support.v4.view;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;

public final class ViewParentCompat {
  private static final String TAG = "ViewParentCompat";
  
  public static void notifySubtreeAccessibilityStateChanged(ViewParent paramViewParent, View paramView1, View paramView2, int paramInt) {
    if (Build.VERSION.SDK_INT >= 19)
      paramViewParent.notifySubtreeAccessibilityStateChanged(paramView1, paramView2, paramInt); 
  }
  
  public static boolean onNestedFling(ViewParent paramViewParent, View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 21) {
      try {
        return paramViewParent.onNestedFling(paramView, paramFloat1, paramFloat2, paramBoolean);
      } catch (AbstractMethodError paramView) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ViewParent ");
        stringBuilder.append(paramViewParent);
        stringBuilder.append(" does not implement interface ");
        stringBuilder.append("method onNestedFling");
        Log.e("ViewParentCompat", stringBuilder.toString(), paramView);
      } 
    } else if (paramViewParent instanceof NestedScrollingParent) {
      return ((NestedScrollingParent)paramViewParent).onNestedFling(paramView, paramFloat1, paramFloat2, paramBoolean);
    } 
    return false;
  }
  
  public static boolean onNestedPreFling(ViewParent paramViewParent, View paramView, float paramFloat1, float paramFloat2) {
    if (Build.VERSION.SDK_INT >= 21) {
      try {
        return paramViewParent.onNestedPreFling(paramView, paramFloat1, paramFloat2);
      } catch (AbstractMethodError paramView) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ViewParent ");
        stringBuilder.append(paramViewParent);
        stringBuilder.append(" does not implement interface ");
        stringBuilder.append("method onNestedPreFling");
        Log.e("ViewParentCompat", stringBuilder.toString(), paramView);
      } 
    } else if (paramViewParent instanceof NestedScrollingParent) {
      return ((NestedScrollingParent)paramViewParent).onNestedPreFling(paramView, paramFloat1, paramFloat2);
    } 
    return false;
  }
  
  public static void onNestedPreScroll(ViewParent paramViewParent, View paramView, int paramInt1, int paramInt2, int[] paramArrayOfInt) { onNestedPreScroll(paramViewParent, paramView, paramInt1, paramInt2, paramArrayOfInt, 0); }
  
  public static void onNestedPreScroll(ViewParent paramViewParent, View paramView, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3) {
    if (paramViewParent instanceof NestedScrollingParent2) {
      ((NestedScrollingParent2)paramViewParent).onNestedPreScroll(paramView, paramInt1, paramInt2, paramArrayOfInt, paramInt3);
      return;
    } 
    if (paramInt3 == 0) {
      StringBuilder stringBuilder;
      if (Build.VERSION.SDK_INT >= 21)
        try {
          paramViewParent.onNestedPreScroll(paramView, paramInt1, paramInt2, paramArrayOfInt);
          return;
        } catch (AbstractMethodError paramView) {
          stringBuilder = new StringBuilder();
          stringBuilder.append("ViewParent ");
          stringBuilder.append(paramViewParent);
          stringBuilder.append(" does not implement interface ");
          stringBuilder.append("method onNestedPreScroll");
          Log.e("ViewParentCompat", stringBuilder.toString(), paramView);
          return;
        }  
      if (paramViewParent instanceof NestedScrollingParent)
        ((NestedScrollingParent)paramViewParent).onNestedPreScroll(paramView, paramInt1, paramInt2, stringBuilder); 
    } 
  }
  
  public static void onNestedScroll(ViewParent paramViewParent, View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) { onNestedScroll(paramViewParent, paramView, paramInt1, paramInt2, paramInt3, paramInt4, 0); }
  
  public static void onNestedScroll(ViewParent paramViewParent, View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    if (paramViewParent instanceof NestedScrollingParent2) {
      ((NestedScrollingParent2)paramViewParent).onNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
      return;
    } 
    if (paramInt5 == 0) {
      if (Build.VERSION.SDK_INT >= 21)
        try {
          paramViewParent.onNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
          return;
        } catch (AbstractMethodError paramView) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("ViewParent ");
          stringBuilder.append(paramViewParent);
          stringBuilder.append(" does not implement interface ");
          stringBuilder.append("method onNestedScroll");
          Log.e("ViewParentCompat", stringBuilder.toString(), paramView);
          return;
        }  
      if (paramViewParent instanceof NestedScrollingParent)
        ((NestedScrollingParent)paramViewParent).onNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4); 
    } 
  }
  
  public static void onNestedScrollAccepted(ViewParent paramViewParent, View paramView1, View paramView2, int paramInt) { onNestedScrollAccepted(paramViewParent, paramView1, paramView2, paramInt, 0); }
  
  public static void onNestedScrollAccepted(ViewParent paramViewParent, View paramView1, View paramView2, int paramInt1, int paramInt2) {
    if (paramViewParent instanceof NestedScrollingParent2) {
      ((NestedScrollingParent2)paramViewParent).onNestedScrollAccepted(paramView1, paramView2, paramInt1, paramInt2);
      return;
    } 
    if (paramInt2 == 0) {
      StringBuilder stringBuilder;
      if (Build.VERSION.SDK_INT >= 21)
        try {
          paramViewParent.onNestedScrollAccepted(paramView1, paramView2, paramInt1);
          return;
        } catch (AbstractMethodError paramView1) {
          stringBuilder = new StringBuilder();
          stringBuilder.append("ViewParent ");
          stringBuilder.append(paramViewParent);
          stringBuilder.append(" does not implement interface ");
          stringBuilder.append("method onNestedScrollAccepted");
          Log.e("ViewParentCompat", stringBuilder.toString(), paramView1);
          return;
        }  
      if (paramViewParent instanceof NestedScrollingParent)
        ((NestedScrollingParent)paramViewParent).onNestedScrollAccepted(paramView1, stringBuilder, paramInt1); 
    } 
  }
  
  public static boolean onStartNestedScroll(ViewParent paramViewParent, View paramView1, View paramView2, int paramInt) { return onStartNestedScroll(paramViewParent, paramView1, paramView2, paramInt, 0); }
  
  public static boolean onStartNestedScroll(ViewParent paramViewParent, View paramView1, View paramView2, int paramInt1, int paramInt2) {
    if (paramViewParent instanceof NestedScrollingParent2)
      return ((NestedScrollingParent2)paramViewParent).onStartNestedScroll(paramView1, paramView2, paramInt1, paramInt2); 
    if (paramInt2 == 0) {
      StringBuilder stringBuilder;
      if (Build.VERSION.SDK_INT >= 21) {
        try {
          return paramViewParent.onStartNestedScroll(paramView1, paramView2, paramInt1);
        } catch (AbstractMethodError paramView1) {
          stringBuilder = new StringBuilder();
          stringBuilder.append("ViewParent ");
          stringBuilder.append(paramViewParent);
          stringBuilder.append(" does not implement interface ");
          stringBuilder.append("method onStartNestedScroll");
          Log.e("ViewParentCompat", stringBuilder.toString(), paramView1);
        } 
      } else if (paramViewParent instanceof NestedScrollingParent) {
        return ((NestedScrollingParent)paramViewParent).onStartNestedScroll(paramView1, stringBuilder, paramInt1);
      } 
    } 
    return false;
  }
  
  public static void onStopNestedScroll(ViewParent paramViewParent, View paramView) { onStopNestedScroll(paramViewParent, paramView, 0); }
  
  public static void onStopNestedScroll(ViewParent paramViewParent, View paramView, int paramInt) {
    if (paramViewParent instanceof NestedScrollingParent2) {
      ((NestedScrollingParent2)paramViewParent).onStopNestedScroll(paramView, paramInt);
      return;
    } 
    if (paramInt == 0) {
      if (Build.VERSION.SDK_INT >= 21)
        try {
          paramViewParent.onStopNestedScroll(paramView);
          return;
        } catch (AbstractMethodError paramView) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("ViewParent ");
          stringBuilder.append(paramViewParent);
          stringBuilder.append(" does not implement interface ");
          stringBuilder.append("method onStopNestedScroll");
          Log.e("ViewParentCompat", stringBuilder.toString(), paramView);
          return;
        }  
      if (paramViewParent instanceof NestedScrollingParent)
        ((NestedScrollingParent)paramViewParent).onStopNestedScroll(paramView); 
    } 
  }
  
  @Deprecated
  public static boolean requestSendAccessibilityEvent(ViewParent paramViewParent, View paramView, AccessibilityEvent paramAccessibilityEvent) { return paramViewParent.requestSendAccessibilityEvent(paramView, paramAccessibilityEvent); }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\view\ViewParentCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */