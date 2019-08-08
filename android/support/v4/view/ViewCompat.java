package android.support.v4.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.UiThread;
import android.support.compat.R;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeProvider;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewCompat {
  public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
  
  public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
  
  public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
  
  public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
  
  public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
  
  public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
  
  public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
  
  @Deprecated
  public static final int LAYER_TYPE_HARDWARE = 2;
  
  @Deprecated
  public static final int LAYER_TYPE_NONE = 0;
  
  @Deprecated
  public static final int LAYER_TYPE_SOFTWARE = 1;
  
  public static final int LAYOUT_DIRECTION_INHERIT = 2;
  
  public static final int LAYOUT_DIRECTION_LOCALE = 3;
  
  public static final int LAYOUT_DIRECTION_LTR = 0;
  
  public static final int LAYOUT_DIRECTION_RTL = 1;
  
  @Deprecated
  public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
  
  @Deprecated
  public static final int MEASURED_SIZE_MASK = 16777215;
  
  @Deprecated
  public static final int MEASURED_STATE_MASK = -16777216;
  
  @Deprecated
  public static final int MEASURED_STATE_TOO_SMALL = 16777216;
  
  @Deprecated
  public static final int OVER_SCROLL_ALWAYS = 0;
  
  @Deprecated
  public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
  
  @Deprecated
  public static final int OVER_SCROLL_NEVER = 2;
  
  public static final int SCROLL_AXIS_HORIZONTAL = 1;
  
  public static final int SCROLL_AXIS_NONE = 0;
  
  public static final int SCROLL_AXIS_VERTICAL = 2;
  
  public static final int SCROLL_INDICATOR_BOTTOM = 2;
  
  public static final int SCROLL_INDICATOR_END = 32;
  
  public static final int SCROLL_INDICATOR_LEFT = 4;
  
  public static final int SCROLL_INDICATOR_RIGHT = 8;
  
  public static final int SCROLL_INDICATOR_START = 16;
  
  public static final int SCROLL_INDICATOR_TOP = 1;
  
  private static final String TAG = "ViewCompat";
  
  public static final int TYPE_NON_TOUCH = 1;
  
  public static final int TYPE_TOUCH = 0;
  
  private static boolean sAccessibilityDelegateCheckFailed;
  
  private static Field sAccessibilityDelegateField;
  
  private static Method sChildrenDrawingOrderMethod;
  
  private static Method sDispatchFinishTemporaryDetach;
  
  private static Method sDispatchStartTemporaryDetach;
  
  private static Field sMinHeightField;
  
  private static boolean sMinHeightFieldFetched;
  
  private static Field sMinWidthField;
  
  private static boolean sMinWidthFieldFetched;
  
  private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
  
  private static boolean sTempDetachBound;
  
  private static ThreadLocal<Rect> sThreadLocalRect;
  
  private static WeakHashMap<View, String> sTransitionNameMap;
  
  private static WeakHashMap<View, ViewPropertyAnimatorCompat> sViewPropertyAnimatorMap = null;
  
  static  {
    sAccessibilityDelegateCheckFailed = false;
  }
  
  public static void addKeyboardNavigationClusters(@NonNull View paramView, @NonNull Collection<View> paramCollection, int paramInt) {
    if (Build.VERSION.SDK_INT >= 26)
      paramView.addKeyboardNavigationClusters(paramCollection, paramInt); 
  }
  
  public static void addOnUnhandledKeyEventListener(@NonNull View paramView, @NonNull OnUnhandledKeyEventListenerCompat paramOnUnhandledKeyEventListenerCompat) {
    if (Build.VERSION.SDK_INT >= 28) {
      Map map = (Map)paramView.getTag(R.id.tag_unhandled_key_listeners);
      ArrayMap arrayMap = map;
      if (map == null) {
        arrayMap = new ArrayMap();
        paramView.setTag(R.id.tag_unhandled_key_listeners, arrayMap);
      } 
      OnUnhandledKeyEventListenerWrapper onUnhandledKeyEventListenerWrapper = new OnUnhandledKeyEventListenerWrapper(paramOnUnhandledKeyEventListenerCompat);
      arrayMap.put(paramOnUnhandledKeyEventListenerCompat, onUnhandledKeyEventListenerWrapper);
      paramView.addOnUnhandledKeyEventListener(onUnhandledKeyEventListenerWrapper);
      return;
    } 
    ArrayList arrayList2 = (ArrayList)paramView.getTag(R.id.tag_unhandled_key_listeners);
    ArrayList arrayList1 = arrayList2;
    if (arrayList2 == null) {
      arrayList1 = new ArrayList();
      paramView.setTag(R.id.tag_unhandled_key_listeners, arrayList1);
    } 
    arrayList1.add(paramOnUnhandledKeyEventListenerCompat);
    if (arrayList1.size() == 1)
      UnhandledKeyEventManager.registerListeningView(paramView); 
  }
  
  @NonNull
  public static ViewPropertyAnimatorCompat animate(@NonNull View paramView) {
    if (sViewPropertyAnimatorMap == null)
      sViewPropertyAnimatorMap = new WeakHashMap(); 
    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2 = (ViewPropertyAnimatorCompat)sViewPropertyAnimatorMap.get(paramView);
    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat1 = viewPropertyAnimatorCompat2;
    if (viewPropertyAnimatorCompat2 == null) {
      viewPropertyAnimatorCompat1 = new ViewPropertyAnimatorCompat(paramView);
      sViewPropertyAnimatorMap.put(paramView, viewPropertyAnimatorCompat1);
    } 
    return viewPropertyAnimatorCompat1;
  }
  
  private static void bindTempDetach() {
    try {
      sDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach", new Class[0]);
      sDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach", new Class[0]);
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("ViewCompat", "Couldn't find method", noSuchMethodException);
    } 
    sTempDetachBound = true;
  }
  
  @Deprecated
  public static boolean canScrollHorizontally(View paramView, int paramInt) { return paramView.canScrollHorizontally(paramInt); }
  
  @Deprecated
  public static boolean canScrollVertically(View paramView, int paramInt) { return paramView.canScrollVertically(paramInt); }
  
  public static void cancelDragAndDrop(@NonNull View paramView) {
    if (Build.VERSION.SDK_INT >= 24)
      paramView.cancelDragAndDrop(); 
  }
  
  @Deprecated
  public static int combineMeasuredStates(int paramInt1, int paramInt2) { return View.combineMeasuredStates(paramInt1, paramInt2); }
  
  private static void compatOffsetLeftAndRight(View paramView, int paramInt) {
    paramView.offsetLeftAndRight(paramInt);
    if (paramView.getVisibility() == 0) {
      tickleInvalidationFlag(paramView);
      ViewParent viewParent = paramView.getParent();
      if (viewParent instanceof View)
        tickleInvalidationFlag((View)viewParent); 
    } 
  }
  
  private static void compatOffsetTopAndBottom(View paramView, int paramInt) {
    paramView.offsetTopAndBottom(paramInt);
    if (paramView.getVisibility() == 0) {
      tickleInvalidationFlag(paramView);
      ViewParent viewParent = paramView.getParent();
      if (viewParent instanceof View)
        tickleInvalidationFlag((View)viewParent); 
    } 
  }
  
  public static WindowInsetsCompat dispatchApplyWindowInsets(@NonNull View paramView, WindowInsetsCompat paramWindowInsetsCompat) {
    WindowInsets windowInsets;
    if (Build.VERSION.SDK_INT >= 21) {
      windowInsets = (WindowInsets)WindowInsetsCompat.unwrap(paramWindowInsetsCompat);
      WindowInsets windowInsets2 = paramView.dispatchApplyWindowInsets(windowInsets);
      WindowInsets windowInsets1 = windowInsets;
      if (windowInsets2 != windowInsets)
        windowInsets1 = new WindowInsets(windowInsets2); 
      return WindowInsetsCompat.wrap(windowInsets1);
    } 
    return windowInsets;
  }
  
  public static void dispatchFinishTemporaryDetach(@NonNull View paramView) {
    if (Build.VERSION.SDK_INT >= 24) {
      paramView.dispatchFinishTemporaryDetach();
      return;
    } 
    if (!sTempDetachBound)
      bindTempDetach(); 
    Method method = sDispatchFinishTemporaryDetach;
    if (method != null) {
      try {
        method.invoke(paramView, new Object[0]);
      } catch (Exception paramView) {
        Log.d("ViewCompat", "Error calling dispatchFinishTemporaryDetach", paramView);
      } 
      return;
    } 
    paramView.onFinishTemporaryDetach();
  }
  
  public static boolean dispatchNestedFling(@NonNull View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean) { return (Build.VERSION.SDK_INT >= 21) ? paramView.dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean) : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean) : 0); }
  
  public static boolean dispatchNestedPreFling(@NonNull View paramView, float paramFloat1, float paramFloat2) { return (Build.VERSION.SDK_INT >= 21) ? paramView.dispatchNestedPreFling(paramFloat1, paramFloat2) : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).dispatchNestedPreFling(paramFloat1, paramFloat2) : 0); }
  
  public static boolean dispatchNestedPreScroll(@NonNull View paramView, int paramInt1, int paramInt2, @Nullable int[] paramArrayOfInt1, @Nullable int[] paramArrayOfInt2) { return (Build.VERSION.SDK_INT >= 21) ? paramView.dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2) : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2) : 0); }
  
  public static boolean dispatchNestedPreScroll(@NonNull View paramView, int paramInt1, int paramInt2, @Nullable int[] paramArrayOfInt1, @Nullable int[] paramArrayOfInt2, int paramInt3) { return (paramView instanceof NestedScrollingChild2) ? ((NestedScrollingChild2)paramView).dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2, paramInt3) : ((paramInt3 == 0) ? dispatchNestedPreScroll(paramView, paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2) : 0); }
  
  public static boolean dispatchNestedScroll(@NonNull View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, @Nullable int[] paramArrayOfInt) { return (Build.VERSION.SDK_INT >= 21) ? paramView.dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt) : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt) : 0); }
  
  public static boolean dispatchNestedScroll(@NonNull View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, @Nullable int[] paramArrayOfInt, int paramInt5) { return (paramView instanceof NestedScrollingChild2) ? ((NestedScrollingChild2)paramView).dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5) : ((paramInt5 == 0) ? dispatchNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt) : 0); }
  
  public static void dispatchStartTemporaryDetach(@NonNull View paramView) {
    if (Build.VERSION.SDK_INT >= 24) {
      paramView.dispatchStartTemporaryDetach();
      return;
    } 
    if (!sTempDetachBound)
      bindTempDetach(); 
    Method method = sDispatchStartTemporaryDetach;
    if (method != null) {
      try {
        method.invoke(paramView, new Object[0]);
      } catch (Exception paramView) {
        Log.d("ViewCompat", "Error calling dispatchStartTemporaryDetach", paramView);
      } 
      return;
    } 
    paramView.onStartTemporaryDetach();
  }
  
  @UiThread
  static boolean dispatchUnhandledKeyEventBeforeCallback(View paramView, KeyEvent paramKeyEvent) { return (Build.VERSION.SDK_INT >= 28) ? false : UnhandledKeyEventManager.at(paramView).dispatch(paramView, paramKeyEvent); }
  
  @UiThread
  static boolean dispatchUnhandledKeyEventBeforeHierarchy(View paramView, KeyEvent paramKeyEvent) { return (Build.VERSION.SDK_INT >= 28) ? false : UnhandledKeyEventManager.at(paramView).preDispatch(paramKeyEvent); }
  
  public static int generateViewId() {
    int j;
    int i;
    if (Build.VERSION.SDK_INT >= 17)
      return View.generateViewId(); 
    do {
      j = sNextGeneratedId.get();
      int k = j + 1;
      i = k;
      if (k <= 16777215)
        continue; 
      i = 1;
    } while (!sNextGeneratedId.compareAndSet(j, i));
    return j;
  }
  
  public static int getAccessibilityLiveRegion(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 19) ? paramView.getAccessibilityLiveRegion() : 0; }
  
  public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(@NonNull View paramView) {
    if (Build.VERSION.SDK_INT >= 16) {
      AccessibilityNodeProvider accessibilityNodeProvider = paramView.getAccessibilityNodeProvider();
      if (accessibilityNodeProvider != null)
        return new AccessibilityNodeProviderCompat(accessibilityNodeProvider); 
    } 
    return null;
  }
  
  @Deprecated
  public static float getAlpha(View paramView) { return paramView.getAlpha(); }
  
  public static ColorStateList getBackgroundTintList(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 21) ? paramView.getBackgroundTintList() : ((paramView instanceof TintableBackgroundView) ? ((TintableBackgroundView)paramView).getSupportBackgroundTintList() : null); }
  
  public static PorterDuff.Mode getBackgroundTintMode(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 21) ? paramView.getBackgroundTintMode() : ((paramView instanceof TintableBackgroundView) ? ((TintableBackgroundView)paramView).getSupportBackgroundTintMode() : null); }
  
  @Nullable
  public static Rect getClipBounds(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 18) ? paramView.getClipBounds() : null; }
  
  @Nullable
  public static Display getDisplay(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 17) ? paramView.getDisplay() : (isAttachedToWindow(paramView) ? ((WindowManager)paramView.getContext().getSystemService("window")).getDefaultDisplay() : null); }
  
  public static float getElevation(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 21) ? paramView.getElevation() : 0.0F; }
  
  private static Rect getEmptyTempRect() {
    if (sThreadLocalRect == null)
      sThreadLocalRect = new ThreadLocal(); 
    Rect rect2 = (Rect)sThreadLocalRect.get();
    Rect rect1 = rect2;
    if (rect2 == null) {
      rect1 = new Rect();
      sThreadLocalRect.set(rect1);
    } 
    rect1.setEmpty();
    return rect1;
  }
  
  public static boolean getFitsSystemWindows(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 16) ? paramView.getFitsSystemWindows() : 0; }
  
  public static int getImportantForAccessibility(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 16) ? paramView.getImportantForAccessibility() : 0; }
  
  @SuppressLint({"InlinedApi"})
  public static int getImportantForAutofill(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 26) ? paramView.getImportantForAutofill() : 0; }
  
  public static int getLabelFor(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 17) ? paramView.getLabelFor() : 0; }
  
  @Deprecated
  public static int getLayerType(View paramView) { return paramView.getLayerType(); }
  
  public static int getLayoutDirection(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 17) ? paramView.getLayoutDirection() : 0; }
  
  @Deprecated
  @Nullable
  public static Matrix getMatrix(View paramView) { return paramView.getMatrix(); }
  
  @Deprecated
  public static int getMeasuredHeightAndState(View paramView) { return paramView.getMeasuredHeightAndState(); }
  
  @Deprecated
  public static int getMeasuredState(View paramView) { return paramView.getMeasuredState(); }
  
  @Deprecated
  public static int getMeasuredWidthAndState(View paramView) { return paramView.getMeasuredWidthAndState(); }
  
  public static int getMinimumHeight(@NonNull View paramView) {
    if (Build.VERSION.SDK_INT >= 16)
      return paramView.getMinimumHeight(); 
    if (!sMinHeightFieldFetched) {
      try {
        sMinHeightField = View.class.getDeclaredField("mMinHeight");
        sMinHeightField.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {}
      sMinHeightFieldFetched = true;
    } 
    Field field = sMinHeightField;
    if (field != null)
      try {
        return ((Integer)field.get(paramView)).intValue();
      } catch (Exception paramView) {} 
    return 0;
  }
  
  public static int getMinimumWidth(@NonNull View paramView) {
    if (Build.VERSION.SDK_INT >= 16)
      return paramView.getMinimumWidth(); 
    if (!sMinWidthFieldFetched) {
      try {
        sMinWidthField = View.class.getDeclaredField("mMinWidth");
        sMinWidthField.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {}
      sMinWidthFieldFetched = true;
    } 
    Field field = sMinWidthField;
    if (field != null)
      try {
        return ((Integer)field.get(paramView)).intValue();
      } catch (Exception paramView) {} 
    return 0;
  }
  
  public static int getNextClusterForwardId(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 26) ? paramView.getNextClusterForwardId() : -1; }
  
  @Deprecated
  public static int getOverScrollMode(View paramView) { return paramView.getOverScrollMode(); }
  
  @Px
  public static int getPaddingEnd(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 17) ? paramView.getPaddingEnd() : paramView.getPaddingRight(); }
  
  @Px
  public static int getPaddingStart(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 17) ? paramView.getPaddingStart() : paramView.getPaddingLeft(); }
  
  public static ViewParent getParentForAccessibility(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 16) ? paramView.getParentForAccessibility() : paramView.getParent(); }
  
  @Deprecated
  public static float getPivotX(View paramView) { return paramView.getPivotX(); }
  
  @Deprecated
  public static float getPivotY(View paramView) { return paramView.getPivotY(); }
  
  @Deprecated
  public static float getRotation(View paramView) { return paramView.getRotation(); }
  
  @Deprecated
  public static float getRotationX(View paramView) { return paramView.getRotationX(); }
  
  @Deprecated
  public static float getRotationY(View paramView) { return paramView.getRotationY(); }
  
  @Deprecated
  public static float getScaleX(View paramView) { return paramView.getScaleX(); }
  
  @Deprecated
  public static float getScaleY(View paramView) { return paramView.getScaleY(); }
  
  public static int getScrollIndicators(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 23) ? paramView.getScrollIndicators() : 0; }
  
  @Nullable
  public static String getTransitionName(@NonNull View paramView) {
    if (Build.VERSION.SDK_INT >= 21)
      return paramView.getTransitionName(); 
    WeakHashMap weakHashMap = sTransitionNameMap;
    return (weakHashMap == null) ? null : (String)weakHashMap.get(paramView);
  }
  
  @Deprecated
  public static float getTranslationX(View paramView) { return paramView.getTranslationX(); }
  
  @Deprecated
  public static float getTranslationY(View paramView) { return paramView.getTranslationY(); }
  
  public static float getTranslationZ(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 21) ? paramView.getTranslationZ() : 0.0F; }
  
  public static int getWindowSystemUiVisibility(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 16) ? paramView.getWindowSystemUiVisibility() : 0; }
  
  @Deprecated
  public static float getX(View paramView) { return paramView.getX(); }
  
  @Deprecated
  public static float getY(View paramView) { return paramView.getY(); }
  
  public static float getZ(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 21) ? paramView.getZ() : 0.0F; }
  
  public static boolean hasAccessibilityDelegate(@NonNull View paramView) {
    boolean bool2 = sAccessibilityDelegateCheckFailed;
    boolean bool1 = false;
    if (bool2)
      return false; 
    if (sAccessibilityDelegateField == null)
      try {
        sAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate");
        sAccessibilityDelegateField.setAccessible(true);
      } catch (Throwable paramView) {
        sAccessibilityDelegateCheckFailed = true;
        return false;
      }  
    try {
      Object object = sAccessibilityDelegateField.get(paramView);
      if (object != null)
        bool1 = true; 
      return bool1;
    } catch (Throwable paramView) {
      sAccessibilityDelegateCheckFailed = true;
      return false;
    } 
  }
  
  public static boolean hasExplicitFocusable(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 26) ? paramView.hasExplicitFocusable() : paramView.hasFocusable(); }
  
  public static boolean hasNestedScrollingParent(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 21) ? paramView.hasNestedScrollingParent() : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).hasNestedScrollingParent() : 0); }
  
  public static boolean hasNestedScrollingParent(@NonNull View paramView, int paramInt) {
    if (paramView instanceof NestedScrollingChild2) {
      ((NestedScrollingChild2)paramView).hasNestedScrollingParent(paramInt);
    } else if (paramInt == 0) {
      return hasNestedScrollingParent(paramView);
    } 
    return false;
  }
  
  public static boolean hasOnClickListeners(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 15) ? paramView.hasOnClickListeners() : 0; }
  
  public static boolean hasOverlappingRendering(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 16) ? paramView.hasOverlappingRendering() : 1; }
  
  public static boolean hasTransientState(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 16) ? paramView.hasTransientState() : 0; }
  
  public static boolean isAttachedToWindow(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 19) ? paramView.isAttachedToWindow() : ((paramView.getWindowToken() != null) ? 1 : 0); }
  
  public static boolean isFocusedByDefault(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 26) ? paramView.isFocusedByDefault() : 0; }
  
  public static boolean isImportantForAccessibility(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 21) ? paramView.isImportantForAccessibility() : 1; }
  
  public static boolean isImportantForAutofill(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 26) ? paramView.isImportantForAutofill() : 1; }
  
  public static boolean isInLayout(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 18) ? paramView.isInLayout() : 0; }
  
  public static boolean isKeyboardNavigationCluster(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 26) ? paramView.isKeyboardNavigationCluster() : 0; }
  
  public static boolean isLaidOut(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 19) ? paramView.isLaidOut() : ((paramView.getWidth() > 0 && paramView.getHeight() > 0) ? 1 : 0); }
  
  public static boolean isLayoutDirectionResolved(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 19) ? paramView.isLayoutDirectionResolved() : 0; }
  
  public static boolean isNestedScrollingEnabled(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 21) ? paramView.isNestedScrollingEnabled() : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).isNestedScrollingEnabled() : 0); }
  
  @Deprecated
  public static boolean isOpaque(View paramView) { return paramView.isOpaque(); }
  
  public static boolean isPaddingRelative(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 17) ? paramView.isPaddingRelative() : 0; }
  
  @Deprecated
  public static void jumpDrawablesToCurrentState(View paramView) { paramView.jumpDrawablesToCurrentState(); }
  
  public static View keyboardNavigationClusterSearch(@NonNull View paramView1, View paramView2, int paramInt) { return (Build.VERSION.SDK_INT >= 26) ? paramView1.keyboardNavigationClusterSearch(paramView2, paramInt) : null; }
  
  public static void offsetLeftAndRight(@NonNull View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 23) {
      paramView.offsetLeftAndRight(paramInt);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 21) {
      Rect rect = getEmptyTempRect();
      boolean bool = false;
      ViewParent viewParent = paramView.getParent();
      if (viewParent instanceof View) {
        View view = (View)viewParent;
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        bool = rect.intersects(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom()) ^ true;
      } 
      compatOffsetLeftAndRight(paramView, paramInt);
      if (bool && rect.intersect(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom()))
        ((View)viewParent).invalidate(rect); 
      return;
    } 
    compatOffsetLeftAndRight(paramView, paramInt);
  }
  
  public static void offsetTopAndBottom(@NonNull View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 23) {
      paramView.offsetTopAndBottom(paramInt);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 21) {
      Rect rect = getEmptyTempRect();
      boolean bool = false;
      ViewParent viewParent = paramView.getParent();
      if (viewParent instanceof View) {
        View view = (View)viewParent;
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        bool = rect.intersects(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom()) ^ true;
      } 
      compatOffsetTopAndBottom(paramView, paramInt);
      if (bool && rect.intersect(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom()))
        ((View)viewParent).invalidate(rect); 
      return;
    } 
    compatOffsetTopAndBottom(paramView, paramInt);
  }
  
  public static WindowInsetsCompat onApplyWindowInsets(@NonNull View paramView, WindowInsetsCompat paramWindowInsetsCompat) {
    WindowInsets windowInsets;
    if (Build.VERSION.SDK_INT >= 21) {
      windowInsets = (WindowInsets)WindowInsetsCompat.unwrap(paramWindowInsetsCompat);
      WindowInsets windowInsets2 = paramView.onApplyWindowInsets(windowInsets);
      WindowInsets windowInsets1 = windowInsets;
      if (windowInsets2 != windowInsets)
        windowInsets1 = new WindowInsets(windowInsets2); 
      return WindowInsetsCompat.wrap(windowInsets1);
    } 
    return windowInsets;
  }
  
  @Deprecated
  public static void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent) { paramView.onInitializeAccessibilityEvent(paramAccessibilityEvent); }
  
  public static void onInitializeAccessibilityNodeInfo(@NonNull View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) { paramView.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfoCompat.unwrap()); }
  
  @Deprecated
  public static void onPopulateAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent) { paramView.onPopulateAccessibilityEvent(paramAccessibilityEvent); }
  
  public static boolean performAccessibilityAction(@NonNull View paramView, int paramInt, Bundle paramBundle) { return (Build.VERSION.SDK_INT >= 16) ? paramView.performAccessibilityAction(paramInt, paramBundle) : 0; }
  
  public static void postInvalidateOnAnimation(@NonNull View paramView) {
    if (Build.VERSION.SDK_INT >= 16) {
      paramView.postInvalidateOnAnimation();
      return;
    } 
    paramView.postInvalidate();
  }
  
  public static void postInvalidateOnAnimation(@NonNull View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (Build.VERSION.SDK_INT >= 16) {
      paramView.postInvalidateOnAnimation(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    paramView.postInvalidate(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public static void postOnAnimation(@NonNull View paramView, Runnable paramRunnable) {
    if (Build.VERSION.SDK_INT >= 16) {
      paramView.postOnAnimation(paramRunnable);
      return;
    } 
    paramView.postDelayed(paramRunnable, ValueAnimator.getFrameDelay());
  }
  
  public static void postOnAnimationDelayed(@NonNull View paramView, Runnable paramRunnable, long paramLong) {
    if (Build.VERSION.SDK_INT >= 16) {
      paramView.postOnAnimationDelayed(paramRunnable, paramLong);
      return;
    } 
    paramView.postDelayed(paramRunnable, ValueAnimator.getFrameDelay() + paramLong);
  }
  
  public static void removeOnUnhandledKeyEventListener(@NonNull View paramView, @NonNull OnUnhandledKeyEventListenerCompat paramOnUnhandledKeyEventListenerCompat) {
    View.OnUnhandledKeyEventListener onUnhandledKeyEventListener;
    if (Build.VERSION.SDK_INT >= 28) {
      Map map = (Map)paramView.getTag(R.id.tag_unhandled_key_listeners);
      if (map == null)
        return; 
      onUnhandledKeyEventListener = (View.OnUnhandledKeyEventListener)map.get(paramOnUnhandledKeyEventListenerCompat);
      if (onUnhandledKeyEventListener != null)
        paramView.removeOnUnhandledKeyEventListener(onUnhandledKeyEventListener); 
      return;
    } 
    ArrayList arrayList = (ArrayList)paramView.getTag(R.id.tag_unhandled_key_listeners);
    if (arrayList != null) {
      arrayList.remove(onUnhandledKeyEventListener);
      if (arrayList.size() == 0)
        UnhandledKeyEventManager.unregisterListeningView(paramView); 
    } 
  }
  
  public static void requestApplyInsets(@NonNull View paramView) {
    if (Build.VERSION.SDK_INT >= 20) {
      paramView.requestApplyInsets();
      return;
    } 
    if (Build.VERSION.SDK_INT >= 16)
      paramView.requestFitSystemWindows(); 
  }
  
  @NonNull
  public static <T extends View> T requireViewById(@NonNull View paramView, @IdRes int paramInt) {
    if (Build.VERSION.SDK_INT >= 28)
      return (T)paramView.requireViewById(paramInt); 
    paramView = paramView.findViewById(paramInt);
    if (paramView != null)
      return (T)paramView; 
    throw new IllegalArgumentException("ID does not reference a View inside this View");
  }
  
  @Deprecated
  public static int resolveSizeAndState(int paramInt1, int paramInt2, int paramInt3) { return View.resolveSizeAndState(paramInt1, paramInt2, paramInt3); }
  
  public static boolean restoreDefaultFocus(@NonNull View paramView) { return (Build.VERSION.SDK_INT >= 26) ? paramView.restoreDefaultFocus() : paramView.requestFocus(); }
  
  public static void setAccessibilityDelegate(@NonNull View paramView, AccessibilityDelegateCompat paramAccessibilityDelegateCompat) {
    View.AccessibilityDelegate accessibilityDelegate;
    if (paramAccessibilityDelegateCompat == null) {
      paramAccessibilityDelegateCompat = null;
    } else {
      accessibilityDelegate = paramAccessibilityDelegateCompat.getBridge();
    } 
    paramView.setAccessibilityDelegate(accessibilityDelegate);
  }
  
  public static void setAccessibilityLiveRegion(@NonNull View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 19)
      paramView.setAccessibilityLiveRegion(paramInt); 
  }
  
  @Deprecated
  public static void setActivated(View paramView, boolean paramBoolean) { paramView.setActivated(paramBoolean); }
  
  @Deprecated
  public static void setAlpha(View paramView, @FloatRange(from = 0.0D, to = 1.0D) float paramFloat) { paramView.setAlpha(paramFloat); }
  
  public static void setAutofillHints(@NonNull View paramView, @Nullable String... paramVarArgs) {
    if (Build.VERSION.SDK_INT >= 26)
      paramView.setAutofillHints(paramVarArgs); 
  }
  
  public static void setBackground(@NonNull View paramView, @Nullable Drawable paramDrawable) {
    if (Build.VERSION.SDK_INT >= 16) {
      paramView.setBackground(paramDrawable);
      return;
    } 
    paramView.setBackgroundDrawable(paramDrawable);
  }
  
  public static void setBackgroundTintList(@NonNull View paramView, ColorStateList paramColorStateList) {
    Drawable drawable;
    if (Build.VERSION.SDK_INT >= 21) {
      paramView.setBackgroundTintList(paramColorStateList);
      if (Build.VERSION.SDK_INT == 21) {
        boolean bool;
        drawable = paramView.getBackground();
        if (paramView.getBackgroundTintList() != null || paramView.getBackgroundTintMode() != null) {
          bool = true;
        } else {
          bool = false;
        } 
        if (drawable != null && bool) {
          if (drawable.isStateful())
            drawable.setState(paramView.getDrawableState()); 
          paramView.setBackground(drawable);
        } 
        return;
      } 
    } else if (paramView instanceof TintableBackgroundView) {
      ((TintableBackgroundView)paramView).setSupportBackgroundTintList(drawable);
    } 
  }
  
  public static void setBackgroundTintMode(@NonNull View paramView, PorterDuff.Mode paramMode) {
    Drawable drawable;
    if (Build.VERSION.SDK_INT >= 21) {
      paramView.setBackgroundTintMode(paramMode);
      if (Build.VERSION.SDK_INT == 21) {
        boolean bool;
        drawable = paramView.getBackground();
        if (paramView.getBackgroundTintList() != null || paramView.getBackgroundTintMode() != null) {
          bool = true;
        } else {
          bool = false;
        } 
        if (drawable != null && bool) {
          if (drawable.isStateful())
            drawable.setState(paramView.getDrawableState()); 
          paramView.setBackground(drawable);
        } 
        return;
      } 
    } else if (paramView instanceof TintableBackgroundView) {
      ((TintableBackgroundView)paramView).setSupportBackgroundTintMode(drawable);
    } 
  }
  
  @Deprecated
  public static void setChildrenDrawingOrderEnabled(ViewGroup paramViewGroup, boolean paramBoolean) {
    if (sChildrenDrawingOrderMethod == null) {
      try {
        sChildrenDrawingOrderMethod = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[] { boolean.class });
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.e("ViewCompat", "Unable to find childrenDrawingOrderEnabled", noSuchMethodException);
      } 
      sChildrenDrawingOrderMethod.setAccessible(true);
    } 
    try {
      sChildrenDrawingOrderMethod.invoke(paramViewGroup, new Object[] { Boolean.valueOf(paramBoolean) });
    } catch (IllegalAccessException paramViewGroup) {
      Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", paramViewGroup);
    } catch (IllegalArgumentException paramViewGroup) {
      Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", paramViewGroup);
    } catch (InvocationTargetException paramViewGroup) {
      Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", paramViewGroup);
      return;
    } 
  }
  
  public static void setClipBounds(@NonNull View paramView, Rect paramRect) {
    if (Build.VERSION.SDK_INT >= 18)
      paramView.setClipBounds(paramRect); 
  }
  
  public static void setElevation(@NonNull View paramView, float paramFloat) {
    if (Build.VERSION.SDK_INT >= 21)
      paramView.setElevation(paramFloat); 
  }
  
  @Deprecated
  public static void setFitsSystemWindows(View paramView, boolean paramBoolean) { paramView.setFitsSystemWindows(paramBoolean); }
  
  public static void setFocusedByDefault(@NonNull View paramView, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 26)
      paramView.setFocusedByDefault(paramBoolean); 
  }
  
  public static void setHasTransientState(@NonNull View paramView, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 16)
      paramView.setHasTransientState(paramBoolean); 
  }
  
  public static void setImportantForAccessibility(@NonNull View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 19) {
      paramView.setImportantForAccessibility(paramInt);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 16) {
      int i = paramInt;
      if (paramInt == 4)
        i = 2; 
      paramView.setImportantForAccessibility(i);
    } 
  }
  
  public static void setImportantForAutofill(@NonNull View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 26)
      paramView.setImportantForAutofill(paramInt); 
  }
  
  public static void setKeyboardNavigationCluster(@NonNull View paramView, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 26)
      paramView.setKeyboardNavigationCluster(paramBoolean); 
  }
  
  public static void setLabelFor(@NonNull View paramView, @IdRes int paramInt) {
    if (Build.VERSION.SDK_INT >= 17)
      paramView.setLabelFor(paramInt); 
  }
  
  public static void setLayerPaint(@NonNull View paramView, Paint paramPaint) {
    if (Build.VERSION.SDK_INT >= 17) {
      paramView.setLayerPaint(paramPaint);
      return;
    } 
    paramView.setLayerType(paramView.getLayerType(), paramPaint);
    paramView.invalidate();
  }
  
  @Deprecated
  public static void setLayerType(View paramView, int paramInt, Paint paramPaint) { paramView.setLayerType(paramInt, paramPaint); }
  
  public static void setLayoutDirection(@NonNull View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 17)
      paramView.setLayoutDirection(paramInt); 
  }
  
  public static void setNestedScrollingEnabled(@NonNull View paramView, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 21) {
      paramView.setNestedScrollingEnabled(paramBoolean);
      return;
    } 
    if (paramView instanceof NestedScrollingChild)
      ((NestedScrollingChild)paramView).setNestedScrollingEnabled(paramBoolean); 
  }
  
  public static void setNextClusterForwardId(@NonNull View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 26)
      paramView.setNextClusterForwardId(paramInt); 
  }
  
  public static void setOnApplyWindowInsetsListener(@NonNull View paramView, final OnApplyWindowInsetsListener listener) {
    if (Build.VERSION.SDK_INT >= 21) {
      if (paramOnApplyWindowInsetsListener == null) {
        paramView.setOnApplyWindowInsetsListener(null);
        return;
      } 
      paramView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            public WindowInsets onApplyWindowInsets(View param1View, WindowInsets param1WindowInsets) {
              WindowInsetsCompat windowInsetsCompat;
              return (WindowInsets)(windowInsetsCompat = WindowInsetsCompat.wrap(param1WindowInsets)).unwrap(listener.onApplyWindowInsets(param1View, windowInsetsCompat));
            }
          });
    } 
  }
  
  @Deprecated
  public static void setOverScrollMode(View paramView, int paramInt) { paramView.setOverScrollMode(paramInt); }
  
  public static void setPaddingRelative(@NonNull View paramView, @Px int paramInt1, @Px int paramInt2, @Px int paramInt3, @Px int paramInt4) {
    if (Build.VERSION.SDK_INT >= 17) {
      paramView.setPaddingRelative(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    paramView.setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  @Deprecated
  public static void setPivotX(View paramView, float paramFloat) { paramView.setPivotX(paramFloat); }
  
  @Deprecated
  public static void setPivotY(View paramView, float paramFloat) { paramView.setPivotY(paramFloat); }
  
  public static void setPointerIcon(@NonNull View paramView, PointerIconCompat paramPointerIconCompat) {
    if (Build.VERSION.SDK_INT >= 24) {
      if (paramPointerIconCompat != null) {
        Object object = paramPointerIconCompat.getPointerIcon();
      } else {
        paramPointerIconCompat = null;
      } 
      paramView.setPointerIcon((PointerIcon)paramPointerIconCompat);
    } 
  }
  
  @Deprecated
  public static void setRotation(View paramView, float paramFloat) { paramView.setRotation(paramFloat); }
  
  @Deprecated
  public static void setRotationX(View paramView, float paramFloat) { paramView.setRotationX(paramFloat); }
  
  @Deprecated
  public static void setRotationY(View paramView, float paramFloat) { paramView.setRotationY(paramFloat); }
  
  @Deprecated
  public static void setSaveFromParentEnabled(View paramView, boolean paramBoolean) { paramView.setSaveFromParentEnabled(paramBoolean); }
  
  @Deprecated
  public static void setScaleX(View paramView, float paramFloat) { paramView.setScaleX(paramFloat); }
  
  @Deprecated
  public static void setScaleY(View paramView, float paramFloat) { paramView.setScaleY(paramFloat); }
  
  public static void setScrollIndicators(@NonNull View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 23)
      paramView.setScrollIndicators(paramInt); 
  }
  
  public static void setScrollIndicators(@NonNull View paramView, int paramInt1, int paramInt2) {
    if (Build.VERSION.SDK_INT >= 23)
      paramView.setScrollIndicators(paramInt1, paramInt2); 
  }
  
  public static void setTooltipText(@NonNull View paramView, @Nullable CharSequence paramCharSequence) {
    if (Build.VERSION.SDK_INT >= 26)
      paramView.setTooltipText(paramCharSequence); 
  }
  
  public static void setTransitionName(@NonNull View paramView, String paramString) {
    if (Build.VERSION.SDK_INT >= 21) {
      paramView.setTransitionName(paramString);
      return;
    } 
    if (sTransitionNameMap == null)
      sTransitionNameMap = new WeakHashMap(); 
    sTransitionNameMap.put(paramView, paramString);
  }
  
  @Deprecated
  public static void setTranslationX(View paramView, float paramFloat) { paramView.setTranslationX(paramFloat); }
  
  @Deprecated
  public static void setTranslationY(View paramView, float paramFloat) { paramView.setTranslationY(paramFloat); }
  
  public static void setTranslationZ(@NonNull View paramView, float paramFloat) {
    if (Build.VERSION.SDK_INT >= 21)
      paramView.setTranslationZ(paramFloat); 
  }
  
  @Deprecated
  public static void setX(View paramView, float paramFloat) { paramView.setX(paramFloat); }
  
  @Deprecated
  public static void setY(View paramView, float paramFloat) { paramView.setY(paramFloat); }
  
  public static void setZ(@NonNull View paramView, float paramFloat) {
    if (Build.VERSION.SDK_INT >= 21)
      paramView.setZ(paramFloat); 
  }
  
  public static boolean startDragAndDrop(@NonNull View paramView, ClipData paramClipData, View.DragShadowBuilder paramDragShadowBuilder, Object paramObject, int paramInt) { return (Build.VERSION.SDK_INT >= 24) ? paramView.startDragAndDrop(paramClipData, paramDragShadowBuilder, paramObject, paramInt) : paramView.startDrag(paramClipData, paramDragShadowBuilder, paramObject, paramInt); }
  
  public static boolean startNestedScroll(@NonNull View paramView, int paramInt) { return (Build.VERSION.SDK_INT >= 21) ? paramView.startNestedScroll(paramInt) : ((paramView instanceof NestedScrollingChild) ? ((NestedScrollingChild)paramView).startNestedScroll(paramInt) : 0); }
  
  public static boolean startNestedScroll(@NonNull View paramView, int paramInt1, int paramInt2) { return (paramView instanceof NestedScrollingChild2) ? ((NestedScrollingChild2)paramView).startNestedScroll(paramInt1, paramInt2) : ((paramInt2 == 0) ? startNestedScroll(paramView, paramInt1) : 0); }
  
  public static void stopNestedScroll(@NonNull View paramView) {
    if (Build.VERSION.SDK_INT >= 21) {
      paramView.stopNestedScroll();
      return;
    } 
    if (paramView instanceof NestedScrollingChild)
      ((NestedScrollingChild)paramView).stopNestedScroll(); 
  }
  
  public static void stopNestedScroll(@NonNull View paramView, int paramInt) {
    if (paramView instanceof NestedScrollingChild2) {
      ((NestedScrollingChild2)paramView).stopNestedScroll(paramInt);
      return;
    } 
    if (paramInt == 0)
      stopNestedScroll(paramView); 
  }
  
  private static void tickleInvalidationFlag(View paramView) {
    float f = paramView.getTranslationY();
    paramView.setTranslationY(1.0F + f);
    paramView.setTranslationY(f);
  }
  
  public static void updateDragShadow(@NonNull View paramView, View.DragShadowBuilder paramDragShadowBuilder) {
    if (Build.VERSION.SDK_INT >= 24)
      paramView.updateDragShadow(paramDragShadowBuilder); 
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface FocusDirection {}
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface FocusRealDirection {}
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface FocusRelativeDirection {}
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface NestedScrollType {}
  
  public static interface OnUnhandledKeyEventListenerCompat {
    boolean onUnhandledKeyEvent(View param1View, KeyEvent param1KeyEvent);
  }
  
  @RequiresApi(28)
  private static class OnUnhandledKeyEventListenerWrapper implements View.OnUnhandledKeyEventListener {
    private ViewCompat.OnUnhandledKeyEventListenerCompat mCompatListener;
    
    OnUnhandledKeyEventListenerWrapper(ViewCompat.OnUnhandledKeyEventListenerCompat param1OnUnhandledKeyEventListenerCompat) { this.mCompatListener = param1OnUnhandledKeyEventListenerCompat; }
    
    public boolean onUnhandledKeyEvent(View param1View, KeyEvent param1KeyEvent) { return this.mCompatListener.onUnhandledKeyEvent(param1View, param1KeyEvent); }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface ScrollAxis {}
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface ScrollIndicators {}
  
  static class UnhandledKeyEventManager {
    private static final ArrayList<WeakReference<View>> sViewsWithListeners = new ArrayList();
    
    private SparseArray<WeakReference<View>> mCapturedKeys = null;
    
    private WeakReference<KeyEvent> mLastDispatchedPreViewKeyEvent = null;
    
    @Nullable
    private WeakHashMap<View, Boolean> mViewsContainingListeners = null;
    
    static UnhandledKeyEventManager at(View param1View) {
      UnhandledKeyEventManager unhandledKeyEventManager2 = (UnhandledKeyEventManager)param1View.getTag(R.id.tag_unhandled_key_event_manager);
      UnhandledKeyEventManager unhandledKeyEventManager1 = unhandledKeyEventManager2;
      if (unhandledKeyEventManager2 == null) {
        unhandledKeyEventManager1 = new UnhandledKeyEventManager();
        param1View.setTag(R.id.tag_unhandled_key_event_manager, unhandledKeyEventManager1);
      } 
      return unhandledKeyEventManager1;
    }
    
    @Nullable
    private View dispatchInOrder(View param1View, KeyEvent param1KeyEvent) {
      WeakHashMap weakHashMap = this.mViewsContainingListeners;
      if (weakHashMap != null) {
        if (!weakHashMap.containsKey(param1View))
          return null; 
        if (param1View instanceof ViewGroup) {
          ViewGroup viewGroup = (ViewGroup)param1View;
          for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
            View view = dispatchInOrder(viewGroup.getChildAt(i), param1KeyEvent);
            if (view != null)
              return view; 
          } 
        } 
        return onUnhandledKeyEvent(param1View, param1KeyEvent) ? param1View : null;
      } 
      return null;
    }
    
    private SparseArray<WeakReference<View>> getCapturedKeys() {
      if (this.mCapturedKeys == null)
        this.mCapturedKeys = new SparseArray(); 
      return this.mCapturedKeys;
    }
    
    private boolean onUnhandledKeyEvent(@NonNull View param1View, @NonNull KeyEvent param1KeyEvent) {
      ArrayList arrayList = (ArrayList)param1View.getTag(R.id.tag_unhandled_key_listeners);
      if (arrayList != null)
        for (int i = arrayList.size() - 1; i >= 0; i--) {
          if (((ViewCompat.OnUnhandledKeyEventListenerCompat)arrayList.get(i)).onUnhandledKeyEvent(param1View, param1KeyEvent))
            return true; 
        }  
      return false;
    }
    
    private void recalcViewsWithUnhandled() {
      null = this.mViewsContainingListeners;
      if (null != null)
        null.clear(); 
      if (sViewsWithListeners.isEmpty())
        return; 
      synchronized (sViewsWithListeners) {
        if (this.mViewsContainingListeners == null)
          this.mViewsContainingListeners = new WeakHashMap(); 
        for (int i = sViewsWithListeners.size() - 1;; i--) {
          if (i >= 0) {
            View view = (View)((WeakReference)sViewsWithListeners.get(i)).get();
            if (view == null) {
              sViewsWithListeners.remove(i);
            } else {
              this.mViewsContainingListeners.put(view, Boolean.TRUE);
              for (ViewParent viewParent = view.getParent(); viewParent instanceof View; viewParent = viewParent.getParent())
                this.mViewsContainingListeners.put((View)viewParent, Boolean.TRUE); 
            } 
          } else {
            return;
          } 
        } 
      } 
    }
    
    static void registerListeningView(View param1View) {
      synchronized (sViewsWithListeners) {
        Iterator iterator = sViewsWithListeners.iterator();
        while (iterator.hasNext()) {
          if (((WeakReference)iterator.next()).get() == param1View)
            return; 
        } 
        sViewsWithListeners.add(new WeakReference(param1View));
        return;
      } 
    }
    
    static void unregisterListeningView(View param1View) { // Byte code:
      //   0: getstatic android/support/v4/view/ViewCompat$UnhandledKeyEventManager.sViewsWithListeners : Ljava/util/ArrayList;
      //   3: astore_2
      //   4: aload_2
      //   5: monitorenter
      //   6: iconst_0
      //   7: istore_1
      //   8: iload_1
      //   9: getstatic android/support/v4/view/ViewCompat$UnhandledKeyEventManager.sViewsWithListeners : Ljava/util/ArrayList;
      //   12: invokevirtual size : ()I
      //   15: if_icmpge -> 46
      //   18: getstatic android/support/v4/view/ViewCompat$UnhandledKeyEventManager.sViewsWithListeners : Ljava/util/ArrayList;
      //   21: iload_1
      //   22: invokevirtual get : (I)Ljava/lang/Object;
      //   25: checkcast java/lang/ref/WeakReference
      //   28: invokevirtual get : ()Ljava/lang/Object;
      //   31: aload_0
      //   32: if_acmpne -> 54
      //   35: getstatic android/support/v4/view/ViewCompat$UnhandledKeyEventManager.sViewsWithListeners : Ljava/util/ArrayList;
      //   38: iload_1
      //   39: invokevirtual remove : (I)Ljava/lang/Object;
      //   42: pop
      //   43: aload_2
      //   44: monitorexit
      //   45: return
      //   46: aload_2
      //   47: monitorexit
      //   48: return
      //   49: astore_0
      //   50: aload_2
      //   51: monitorexit
      //   52: aload_0
      //   53: athrow
      //   54: iload_1
      //   55: iconst_1
      //   56: iadd
      //   57: istore_1
      //   58: goto -> 8
      // Exception table:
      //   from	to	target	type
      //   8	45	49	finally
      //   46	48	49	finally
      //   50	52	49	finally }
    
    boolean dispatch(View param1View, KeyEvent param1KeyEvent) {
      if (param1KeyEvent.getAction() == 0)
        recalcViewsWithUnhandled(); 
      param1View = dispatchInOrder(param1View, param1KeyEvent);
      if (param1KeyEvent.getAction() == 0) {
        int i = param1KeyEvent.getKeyCode();
        if (param1View != null && !KeyEvent.isModifierKey(i))
          getCapturedKeys().put(i, new WeakReference(param1View)); 
      } 
      return (param1View != null);
    }
    
    boolean preDispatch(KeyEvent param1KeyEvent) {
      WeakReference weakReference1 = this.mLastDispatchedPreViewKeyEvent;
      if (weakReference1 != null && weakReference1.get() == param1KeyEvent)
        return false; 
      this.mLastDispatchedPreViewKeyEvent = new WeakReference(param1KeyEvent);
      WeakReference weakReference2 = null;
      SparseArray sparseArray = getCapturedKeys();
      weakReference1 = weakReference2;
      if (param1KeyEvent.getAction() == 1) {
        int i = sparseArray.indexOfKey(param1KeyEvent.getKeyCode());
        weakReference1 = weakReference2;
        if (i >= 0) {
          weakReference1 = (WeakReference)sparseArray.valueAt(i);
          sparseArray.removeAt(i);
        } 
      } 
      weakReference2 = weakReference1;
      if (weakReference1 == null)
        weakReference2 = (WeakReference)sparseArray.get(param1KeyEvent.getKeyCode()); 
      if (weakReference2 != null) {
        View view = (View)weakReference2.get();
        if (view != null && ViewCompat.isAttachedToWindow(view))
          onUnhandledKeyEvent(view, param1KeyEvent); 
        return true;
      } 
      return false;
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\view\ViewCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */