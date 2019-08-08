package android.support.design.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.coordinatorlayout.R;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.util.Pools;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.DirectedAcyclicGraph;
import android.support.v4.widget.ViewGroupUtils;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinatorLayout extends ViewGroup implements NestedScrollingParent2 {
  static final Class<?>[] CONSTRUCTOR_PARAMS;
  
  static final int EVENT_NESTED_SCROLL = 1;
  
  static final int EVENT_PRE_DRAW = 0;
  
  static final int EVENT_VIEW_REMOVED = 2;
  
  static final String TAG = "CoordinatorLayout";
  
  static final Comparator<View> TOP_SORTED_CHILDREN_COMPARATOR;
  
  private static final int TYPE_ON_INTERCEPT = 0;
  
  private static final int TYPE_ON_TOUCH = 1;
  
  static final String WIDGET_PACKAGE_NAME;
  
  static final ThreadLocal<Map<String, Constructor<Behavior>>> sConstructors;
  
  private static final Pools.Pool<Rect> sRectPool;
  
  private OnApplyWindowInsetsListener mApplyWindowInsetsListener;
  
  private View mBehaviorTouchView;
  
  private final DirectedAcyclicGraph<View> mChildDag = new DirectedAcyclicGraph();
  
  private final List<View> mDependencySortedChildren = new ArrayList();
  
  private boolean mDisallowInterceptReset;
  
  private boolean mDrawStatusBarBackground;
  
  private boolean mIsAttachedToWindow;
  
  private int[] mKeylines;
  
  private WindowInsetsCompat mLastInsets;
  
  private boolean mNeedsPreDrawListener;
  
  private final NestedScrollingParentHelper mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
  
  private View mNestedScrollingTarget;
  
  ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
  
  private OnPreDrawListener mOnPreDrawListener;
  
  private Paint mScrimPaint;
  
  private Drawable mStatusBarBackground;
  
  private final List<View> mTempDependenciesList = new ArrayList();
  
  private final int[] mTempIntPair = new int[2];
  
  private final List<View> mTempList1 = new ArrayList();
  
  static  {
    Package package = CoordinatorLayout.class.getPackage();
    if (package != null) {
      String str = package.getName();
    } else {
      package = null;
    } 
    WIDGET_PACKAGE_NAME = package;
    if (Build.VERSION.SDK_INT >= 21) {
      TOP_SORTED_CHILDREN_COMPARATOR = new ViewElevationComparator();
    } else {
      TOP_SORTED_CHILDREN_COMPARATOR = null;
    } 
    CONSTRUCTOR_PARAMS = new Class[] { Context.class, AttributeSet.class };
    sConstructors = new ThreadLocal();
    sRectPool = new Pools.SynchronizedPool(12);
  }
  
  public CoordinatorLayout(@NonNull Context paramContext) { this(paramContext, null); }
  
  public CoordinatorLayout(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, R.attr.coordinatorLayoutStyle); }
  
  public CoordinatorLayout(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet, @AttrRes int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    if (paramInt == 0) {
      typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CoordinatorLayout, 0, R.style.Widget_Support_CoordinatorLayout);
    } else {
      typedArray = paramContext.obtainStyledAttributes(typedArray, R.styleable.CoordinatorLayout, paramInt, 0);
    } 
    paramInt = typedArray.getResourceId(R.styleable.CoordinatorLayout_keylines, 0);
    if (paramInt != 0) {
      Resources resources = paramContext.getResources();
      this.mKeylines = resources.getIntArray(paramInt);
      float f = (resources.getDisplayMetrics()).density;
      int i = this.mKeylines.length;
      for (paramInt = 0; paramInt < i; paramInt++) {
        int[] arrayOfInt = this.mKeylines;
        arrayOfInt[paramInt] = (int)(arrayOfInt[paramInt] * f);
      } 
    } 
    this.mStatusBarBackground = typedArray.getDrawable(R.styleable.CoordinatorLayout_statusBarBackground);
    typedArray.recycle();
    setupForInsets();
    super.setOnHierarchyChangeListener(new HierarchyChangeListener());
  }
  
  @NonNull
  private static Rect acquireTempRect() {
    Rect rect2 = (Rect)sRectPool.acquire();
    Rect rect1 = rect2;
    if (rect2 == null)
      rect1 = new Rect(); 
    return rect1;
  }
  
  private static int clamp(int paramInt1, int paramInt2, int paramInt3) { return (paramInt1 < paramInt2) ? paramInt2 : ((paramInt1 > paramInt3) ? paramInt3 : paramInt1); }
  
  private void constrainChildRect(LayoutParams paramLayoutParams, Rect paramRect, int paramInt1, int paramInt2) {
    int j = getWidth();
    int i = getHeight();
    j = Math.max(getPaddingLeft() + paramLayoutParams.leftMargin, Math.min(paramRect.left, j - getPaddingRight() - paramInt1 - paramLayoutParams.rightMargin));
    i = Math.max(getPaddingTop() + paramLayoutParams.topMargin, Math.min(paramRect.top, i - getPaddingBottom() - paramInt2 - paramLayoutParams.bottomMargin));
    paramRect.set(j, i, j + paramInt1, i + paramInt2);
  }
  
  private WindowInsetsCompat dispatchApplyWindowInsetsToBehaviors(WindowInsetsCompat paramWindowInsetsCompat) {
    if (paramWindowInsetsCompat.isConsumed())
      return paramWindowInsetsCompat; 
    byte b = 0;
    int i = getChildCount();
    WindowInsetsCompat windowInsetsCompat;
    for (windowInsetsCompat = paramWindowInsetsCompat; b < i; windowInsetsCompat = paramWindowInsetsCompat) {
      View view = getChildAt(b);
      paramWindowInsetsCompat = windowInsetsCompat;
      if (ViewCompat.getFitsSystemWindows(view)) {
        Behavior behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
        paramWindowInsetsCompat = windowInsetsCompat;
        if (behavior != null) {
          windowInsetsCompat = behavior.onApplyWindowInsets(this, view, windowInsetsCompat);
          paramWindowInsetsCompat = windowInsetsCompat;
          if (windowInsetsCompat.isConsumed())
            return windowInsetsCompat; 
        } 
      } 
      b++;
    } 
    return windowInsetsCompat;
  }
  
  private void getDesiredAnchoredChildRectWithoutConstraints(View paramView, int paramInt1, Rect paramRect1, Rect paramRect2, LayoutParams paramLayoutParams, int paramInt2, int paramInt3) {
    int i = GravityCompat.getAbsoluteGravity(resolveAnchoredChildGravity(paramLayoutParams.gravity), paramInt1);
    paramInt1 = GravityCompat.getAbsoluteGravity(resolveGravity(paramLayoutParams.anchorGravity), paramInt1);
    int k = i & 0x7;
    int j = i & 0x70;
    int m = paramInt1 & 0x7;
    i = paramInt1 & 0x70;
    if (m != 1) {
      if (m != 5) {
        paramInt1 = paramRect1.left;
      } else {
        paramInt1 = paramRect1.right;
      } 
    } else {
      paramInt1 = paramRect1.left + paramRect1.width() / 2;
    } 
    if (i != 16) {
      if (i != 80) {
        i = paramRect1.top;
      } else {
        i = paramRect1.bottom;
      } 
    } else {
      i = paramRect1.top + paramRect1.height() / 2;
    } 
    if (k != 1) {
      if (k != 5)
        paramInt1 -= paramInt2; 
    } else {
      paramInt1 -= paramInt2 / 2;
    } 
    if (j != 16) {
      if (j != 80)
        i -= paramInt3; 
    } else {
      i -= paramInt3 / 2;
    } 
    paramRect2.set(paramInt1, i, paramInt1 + paramInt2, i + paramInt3);
  }
  
  private int getKeyline(int paramInt) {
    StringBuilder stringBuilder = this.mKeylines;
    if (stringBuilder == null) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("No keylines defined for ");
      stringBuilder.append(this);
      stringBuilder.append(" - attempted index lookup ");
      stringBuilder.append(paramInt);
      Log.e("CoordinatorLayout", stringBuilder.toString());
      return 0;
    } 
    if (paramInt < 0 || paramInt >= stringBuilder.length) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("Keyline index ");
      stringBuilder.append(paramInt);
      stringBuilder.append(" out of range for ");
      stringBuilder.append(this);
      Log.e("CoordinatorLayout", stringBuilder.toString());
      return 0;
    } 
    return stringBuilder[paramInt];
  }
  
  private void getTopSortedChildren(List<View> paramList) {
    paramList.clear();
    boolean bool = isChildrenDrawingOrderEnabled();
    int j = getChildCount();
    for (int i = j - 1; i >= 0; i--) {
      int k;
      if (bool) {
        k = getChildDrawingOrder(j, i);
      } else {
        k = i;
      } 
      paramList.add(getChildAt(k));
    } 
    Comparator comparator = TOP_SORTED_CHILDREN_COMPARATOR;
    if (comparator != null)
      Collections.sort(paramList, comparator); 
  }
  
  private boolean hasDependencies(View paramView) { return this.mChildDag.hasOutgoingEdges(paramView); }
  
  private void layoutChild(View paramView, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    Rect rect1 = acquireTempRect();
    rect1.set(getPaddingLeft() + layoutParams.leftMargin, getPaddingTop() + layoutParams.topMargin, getWidth() - getPaddingRight() - layoutParams.rightMargin, getHeight() - getPaddingBottom() - layoutParams.bottomMargin);
    if (this.mLastInsets != null && ViewCompat.getFitsSystemWindows(this) && !ViewCompat.getFitsSystemWindows(paramView)) {
      rect1.left += this.mLastInsets.getSystemWindowInsetLeft();
      rect1.top += this.mLastInsets.getSystemWindowInsetTop();
      rect1.right -= this.mLastInsets.getSystemWindowInsetRight();
      rect1.bottom -= this.mLastInsets.getSystemWindowInsetBottom();
    } 
    Rect rect2 = acquireTempRect();
    GravityCompat.apply(resolveGravity(layoutParams.gravity), paramView.getMeasuredWidth(), paramView.getMeasuredHeight(), rect1, rect2, paramInt);
    paramView.layout(rect2.left, rect2.top, rect2.right, rect2.bottom);
    releaseTempRect(rect1);
    releaseTempRect(rect2);
  }
  
  private void layoutChildWithAnchor(View paramView1, View paramView2, int paramInt) {
    rect1 = acquireTempRect();
    rect2 = acquireTempRect();
    try {
      getDescendantRect(paramView2, rect1);
      getDesiredAnchoredChildRect(paramView1, paramInt, rect1, rect2);
      paramView1.layout(rect2.left, rect2.top, rect2.right, rect2.bottom);
      return;
    } finally {
      releaseTempRect(rect1);
      releaseTempRect(rect2);
    } 
  }
  
  private void layoutChildWithKeyline(View paramView, int paramInt1, int paramInt2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = GravityCompat.getAbsoluteGravity(resolveKeylineGravity(layoutParams.gravity), paramInt2);
    int i1 = i & 0x7;
    int n = i & 0x70;
    int m = getWidth();
    int k = getHeight();
    i = paramView.getMeasuredWidth();
    int j = paramView.getMeasuredHeight();
    if (paramInt2 == 1)
      paramInt1 = m - paramInt1; 
    paramInt1 = getKeyline(paramInt1) - i;
    paramInt2 = 0;
    if (i1 != 1) {
      if (i1 == 5)
        paramInt1 += i; 
    } else {
      paramInt1 += i / 2;
    } 
    if (n != 16) {
      if (n == 80)
        paramInt2 = 0 + j; 
    } else {
      paramInt2 = 0 + j / 2;
    } 
    paramInt1 = Math.max(getPaddingLeft() + layoutParams.leftMargin, Math.min(paramInt1, m - getPaddingRight() - i - layoutParams.rightMargin));
    paramInt2 = Math.max(getPaddingTop() + layoutParams.topMargin, Math.min(paramInt2, k - getPaddingBottom() - j - layoutParams.bottomMargin));
    paramView.layout(paramInt1, paramInt2, paramInt1 + i, paramInt2 + j);
  }
  
  private void offsetChildByInset(View paramView, Rect paramRect, int paramInt) {
    if (!ViewCompat.isLaidOut(paramView))
      return; 
    if (paramView.getWidth() > 0) {
      StringBuilder stringBuilder;
      if (paramView.getHeight() <= 0)
        return; 
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      Behavior behavior = layoutParams.getBehavior();
      Rect rect1 = acquireTempRect();
      Rect rect2 = acquireTempRect();
      rect2.set(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
      if (behavior != null && behavior.getInsetDodgeRect(this, paramView, rect1)) {
        if (!rect2.contains(rect1)) {
          stringBuilder = new StringBuilder();
          stringBuilder.append("Rect should be within the child's bounds. Rect:");
          stringBuilder.append(rect1.toShortString());
          stringBuilder.append(" | Bounds:");
          stringBuilder.append(rect2.toShortString());
          throw new IllegalArgumentException(stringBuilder.toString());
        } 
      } else {
        rect1.set(rect2);
      } 
      releaseTempRect(rect2);
      if (rect1.isEmpty()) {
        releaseTempRect(rect1);
        return;
      } 
      int j = GravityCompat.getAbsoluteGravity(layoutParams.dodgeInsetEdges, paramInt);
      int i = 0;
      paramInt = i;
      if ((j & 0x30) == 48) {
        int k = rect1.top - layoutParams.topMargin - layoutParams.mInsetOffsetY;
        paramInt = i;
        if (k < paramRect.top) {
          setInsetOffsetY(stringBuilder, paramRect.top - k);
          paramInt = 1;
        } 
      } 
      i = paramInt;
      if ((j & 0x50) == 80) {
        int k = getHeight() - rect1.bottom - layoutParams.bottomMargin + layoutParams.mInsetOffsetY;
        i = paramInt;
        if (k < paramRect.bottom) {
          setInsetOffsetY(stringBuilder, k - paramRect.bottom);
          i = 1;
        } 
      } 
      if (i == 0)
        setInsetOffsetY(stringBuilder, 0); 
      i = 0;
      paramInt = i;
      if ((j & 0x3) == 3) {
        int k = rect1.left - layoutParams.leftMargin - layoutParams.mInsetOffsetX;
        paramInt = i;
        if (k < paramRect.left) {
          setInsetOffsetX(stringBuilder, paramRect.left - k);
          paramInt = 1;
        } 
      } 
      i = paramInt;
      if ((j & 0x5) == 5) {
        j = getWidth() - rect1.right - layoutParams.rightMargin + layoutParams.mInsetOffsetX;
        i = paramInt;
        if (j < paramRect.right) {
          setInsetOffsetX(stringBuilder, j - paramRect.right);
          i = 1;
        } 
      } 
      if (i == 0)
        setInsetOffsetX(stringBuilder, 0); 
      releaseTempRect(rect1);
      return;
    } 
  }
  
  static Behavior parseBehavior(Context paramContext, AttributeSet paramAttributeSet, String paramString) {
    if (TextUtils.isEmpty(paramString))
      return null; 
    if (paramString.startsWith(".")) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramContext.getPackageName());
      stringBuilder.append(paramString);
      paramString = stringBuilder.toString();
    } else if (paramString.indexOf('.') < 0 && !TextUtils.isEmpty(WIDGET_PACKAGE_NAME)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(WIDGET_PACKAGE_NAME);
      stringBuilder.append('.');
      stringBuilder.append(paramString);
      paramString = stringBuilder.toString();
    } 
    try {
      Map map2 = (Map)sConstructors.get();
      Map map1 = map2;
      if (map2 == null) {
        map1 = new HashMap();
        sConstructors.set(map1);
      } 
      Constructor constructor2 = (Constructor)map1.get(paramString);
      Constructor constructor1 = constructor2;
      if (constructor2 == null) {
        constructor1 = paramContext.getClassLoader().loadClass(paramString).getConstructor(CONSTRUCTOR_PARAMS);
        constructor1.setAccessible(true);
        map1.put(paramString, constructor1);
      } 
      return (Behavior)constructor1.newInstance(new Object[] { paramContext, paramAttributeSet });
    } catch (Exception paramContext) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Could not inflate Behavior subclass ");
      stringBuilder.append(paramString);
      throw new RuntimeException(stringBuilder.toString(), paramContext);
    } 
  }
  
  private boolean performIntercept(MotionEvent paramMotionEvent, int paramInt) {
    boolean bool;
    byte b3 = 0;
    byte b2 = 0;
    LayoutParams layoutParams = null;
    int i = paramMotionEvent.getActionMasked();
    List list = this.mTempList1;
    getTopSortedChildren(list);
    int j = list.size();
    byte b1 = 0;
    while (true) {
      bool = b3;
      if (b1 < j) {
        boolean bool2;
        byte b;
        View view = (View)list.get(b1);
        LayoutParams layoutParams1 = (LayoutParams)view.getLayoutParams();
        Behavior behavior = layoutParams1.getBehavior();
        if ((b3 || b2) && i != 0) {
          bool2 = b3;
          b = b2;
          layoutParams1 = layoutParams;
          if (behavior != null) {
            layoutParams1 = layoutParams;
            if (layoutParams == null) {
              long l = SystemClock.uptimeMillis();
              layoutParams1 = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
            } 
            switch (paramInt) {
              case 1:
                behavior.onTouchEvent(this, view, layoutParams1);
                break;
              case 0:
                behavior.onInterceptTouchEvent(this, view, layoutParams1);
                break;
            } 
            bool2 = b3;
            b = b2;
          } 
        } else {
          bool = b3;
          if (b3 == 0) {
            bool = b3;
            if (behavior != null) {
              boolean bool4;
              switch (paramInt) {
                case 1:
                  bool4 = behavior.onTouchEvent(this, view, paramMotionEvent);
                  break;
                case 0:
                  bool4 = behavior.onInterceptTouchEvent(this, view, paramMotionEvent);
                  break;
              } 
              bool = bool4;
              if (bool4) {
                this.mBehaviorTouchView = view;
                bool = bool4;
              } 
            } 
          } 
          bool2 = layoutParams1.didBlockInteraction();
          boolean bool3 = layoutParams1.isBlockingInteractionBelow(this, view);
          if (bool3 && !bool2) {
            b = 1;
          } else {
            b = 0;
          } 
          b2 = b;
          bool2 = bool;
          b = b2;
          layoutParams1 = layoutParams;
          if (bool3) {
            bool2 = bool;
            b = b2;
            layoutParams1 = layoutParams;
            if (b2 == 0)
              break; 
          } 
        } 
        b1++;
        boolean bool1 = bool2;
        b2 = b;
        layoutParams = layoutParams1;
        continue;
      } 
      break;
    } 
    list.clear();
    return bool;
  }
  
  private void prepareChildren() {
    this.mDependencySortedChildren.clear();
    this.mChildDag.clear();
    byte b = 0;
    int i = getChildCount();
    while (b < i) {
      View view = getChildAt(b);
      LayoutParams layoutParams = getResolvedLayoutParams(view);
      layoutParams.findAnchorView(this, view);
      this.mChildDag.addNode(view);
      for (byte b1 = 0; b1 < i; b1++) {
        if (b1 != b) {
          View view1 = getChildAt(b1);
          if (layoutParams.dependsOn(this, view, view1)) {
            if (!this.mChildDag.contains(view1))
              this.mChildDag.addNode(view1); 
            this.mChildDag.addEdge(view1, view);
          } 
        } 
      } 
      b++;
    } 
    this.mDependencySortedChildren.addAll(this.mChildDag.getSortedList());
    Collections.reverse(this.mDependencySortedChildren);
  }
  
  private static void releaseTempRect(@NonNull Rect paramRect) {
    paramRect.setEmpty();
    sRectPool.release(paramRect);
  }
  
  private void resetTouchBehaviors(boolean paramBoolean) {
    int i = getChildCount();
    byte b;
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      Behavior behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
      if (behavior != null) {
        long l = SystemClock.uptimeMillis();
        MotionEvent motionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
        if (paramBoolean) {
          behavior.onInterceptTouchEvent(this, view, motionEvent);
        } else {
          behavior.onTouchEvent(this, view, motionEvent);
        } 
        motionEvent.recycle();
      } 
    } 
    for (b = 0; b < i; b++)
      ((LayoutParams)getChildAt(b).getLayoutParams()).resetTouchBehaviorTracking(); 
    this.mBehaviorTouchView = null;
    this.mDisallowInterceptReset = false;
  }
  
  private static int resolveAnchoredChildGravity(int paramInt) { return (paramInt == 0) ? 17 : paramInt; }
  
  private static int resolveGravity(int paramInt) {
    int i = paramInt;
    if ((paramInt & 0x7) == 0)
      i = paramInt | 0x800003; 
    paramInt = i;
    if ((i & 0x70) == 0)
      paramInt = i | 0x30; 
    return paramInt;
  }
  
  private static int resolveKeylineGravity(int paramInt) { return (paramInt == 0) ? 8388661 : paramInt; }
  
  private void setInsetOffsetX(View paramView, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (layoutParams.mInsetOffsetX != paramInt) {
      ViewCompat.offsetLeftAndRight(paramView, paramInt - layoutParams.mInsetOffsetX);
      layoutParams.mInsetOffsetX = paramInt;
    } 
  }
  
  private void setInsetOffsetY(View paramView, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (layoutParams.mInsetOffsetY != paramInt) {
      ViewCompat.offsetTopAndBottom(paramView, paramInt - layoutParams.mInsetOffsetY);
      layoutParams.mInsetOffsetY = paramInt;
    } 
  }
  
  private void setupForInsets() {
    if (Build.VERSION.SDK_INT < 21)
      return; 
    if (ViewCompat.getFitsSystemWindows(this)) {
      if (this.mApplyWindowInsetsListener == null)
        this.mApplyWindowInsetsListener = new OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) { return CoordinatorLayout.this.setWindowInsets(param1WindowInsetsCompat); }
          }; 
      ViewCompat.setOnApplyWindowInsetsListener(this, this.mApplyWindowInsetsListener);
      setSystemUiVisibility(1280);
      return;
    } 
    ViewCompat.setOnApplyWindowInsetsListener(this, null);
  }
  
  void addPreDrawListener() {
    if (this.mIsAttachedToWindow) {
      if (this.mOnPreDrawListener == null)
        this.mOnPreDrawListener = new OnPreDrawListener(); 
      getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
    } 
    this.mNeedsPreDrawListener = true;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams)); }
  
  public void dispatchDependentViewsChanged(@NonNull View paramView) {
    List list = this.mChildDag.getIncomingEdges(paramView);
    if (list != null && !list.isEmpty())
      for (byte b = 0; b < list.size(); b++) {
        View view = (View)list.get(b);
        Behavior behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
        if (behavior != null)
          behavior.onDependentViewChanged(this, view, paramView); 
      }  
  }
  
  public boolean doViewsOverlap(@NonNull View paramView1, @NonNull View paramView2) {
    int i = paramView1.getVisibility();
    boolean bool = false;
    if (i == 0 && paramView2.getVisibility() == 0) {
      rect2 = acquireTempRect();
      if (paramView1.getParent() != this) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      getChildRect(paramView1, bool1, rect2);
      rect1 = acquireTempRect();
      if (paramView2.getParent() != this) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      getChildRect(paramView2, bool1, rect1);
      bool1 = bool;
      try {
        if (rect2.left <= rect1.right) {
          bool1 = bool;
          if (rect2.top <= rect1.bottom) {
            bool1 = bool;
            if (rect2.right >= rect1.left) {
              i = rect2.bottom;
              int j = rect1.top;
              bool1 = bool;
              if (i >= j)
                bool1 = true; 
            } 
          } 
        } 
        return bool1;
      } finally {
        releaseTempRect(rect2);
        releaseTempRect(rect1);
      } 
    } 
    return false;
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (layoutParams.mBehavior != null) {
      float f = layoutParams.mBehavior.getScrimOpacity(this, paramView);
      if (f > 0.0F) {
        if (this.mScrimPaint == null)
          this.mScrimPaint = new Paint(); 
        this.mScrimPaint.setColor(layoutParams.mBehavior.getScrimColor(this, paramView));
        this.mScrimPaint.setAlpha(clamp(Math.round(255.0F * f), 0, 255));
        int i = paramCanvas.save();
        if (paramView.isOpaque())
          paramCanvas.clipRect(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom(), Region.Op.DIFFERENCE); 
        paramCanvas.drawRect(getPaddingLeft(), getPaddingTop(), (getWidth() - getPaddingRight()), (getHeight() - getPaddingBottom()), this.mScrimPaint);
        paramCanvas.restoreToCount(i);
      } 
    } 
    return super.drawChild(paramCanvas, paramView, paramLong);
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    byte b = 0;
    Drawable drawable = this.mStatusBarBackground;
    boolean bool = b;
    if (drawable != null) {
      bool = b;
      if (drawable.isStateful())
        bool = false | drawable.setState(arrayOfInt); 
    } 
    if (bool)
      invalidate(); 
  }
  
  void ensurePreDrawListener() {
    boolean bool1;
    boolean bool2 = false;
    int i = getChildCount();
    byte b = 0;
    while (true) {
      bool1 = bool2;
      if (b < i) {
        if (hasDependencies(getChildAt(b))) {
          bool1 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    if (bool1 != this.mNeedsPreDrawListener) {
      if (bool1) {
        addPreDrawListener();
        return;
      } 
      removePreDrawListener();
    } 
  }
  
  protected LayoutParams generateDefaultLayoutParams() { return new LayoutParams(-2, -2); }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) { return new LayoutParams(getContext(), paramAttributeSet); }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return (paramLayoutParams instanceof LayoutParams) ? new LayoutParams((LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams)); }
  
  void getChildRect(View paramView, boolean paramBoolean, Rect paramRect) {
    if (paramView.isLayoutRequested() || paramView.getVisibility() == 8) {
      paramRect.setEmpty();
      return;
    } 
    if (paramBoolean) {
      getDescendantRect(paramView, paramRect);
      return;
    } 
    paramRect.set(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
  }
  
  @NonNull
  public List<View> getDependencies(@NonNull View paramView) {
    List list = this.mChildDag.getOutgoingEdges(paramView);
    this.mTempDependenciesList.clear();
    if (list != null)
      this.mTempDependenciesList.addAll(list); 
    return this.mTempDependenciesList;
  }
  
  @VisibleForTesting
  final List<View> getDependencySortedChildren() {
    prepareChildren();
    return Collections.unmodifiableList(this.mDependencySortedChildren);
  }
  
  @NonNull
  public List<View> getDependents(@NonNull View paramView) {
    List list = this.mChildDag.getIncomingEdges(paramView);
    this.mTempDependenciesList.clear();
    if (list != null)
      this.mTempDependenciesList.addAll(list); 
    return this.mTempDependenciesList;
  }
  
  void getDescendantRect(View paramView, Rect paramRect) { ViewGroupUtils.getDescendantRect(this, paramView, paramRect); }
  
  void getDesiredAnchoredChildRect(View paramView, int paramInt, Rect paramRect1, Rect paramRect2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = paramView.getMeasuredWidth();
    int j = paramView.getMeasuredHeight();
    getDesiredAnchoredChildRectWithoutConstraints(paramView, paramInt, paramRect1, paramRect2, layoutParams, i, j);
    constrainChildRect(layoutParams, paramRect2, i, j);
  }
  
  void getLastChildRect(View paramView, Rect paramRect) { paramRect.set(((LayoutParams)paramView.getLayoutParams()).getLastChildRect()); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public final WindowInsetsCompat getLastWindowInsets() { return this.mLastInsets; }
  
  public int getNestedScrollAxes() { return this.mNestedScrollingParentHelper.getNestedScrollAxes(); }
  
  LayoutParams getResolvedLayoutParams(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (!layoutParams.mBehaviorResolved) {
      DefaultBehavior defaultBehavior;
      if (paramView instanceof AttachedBehavior) {
        behavior = ((AttachedBehavior)paramView).getBehavior();
        if (behavior == null)
          Log.e("CoordinatorLayout", "Attached behavior class is null"); 
        layoutParams.setBehavior(behavior);
        layoutParams.mBehaviorResolved = true;
        return layoutParams;
      } 
      Class clazz = behavior.getClass();
      behavior = null;
      while (true) {
        defaultBehavior = behavior;
        if (clazz != null) {
          DefaultBehavior defaultBehavior2 = (DefaultBehavior)clazz.getAnnotation(DefaultBehavior.class);
          DefaultBehavior defaultBehavior1 = defaultBehavior2;
          defaultBehavior = defaultBehavior1;
          if (defaultBehavior2 == null) {
            clazz = clazz.getSuperclass();
            continue;
          } 
        } 
        break;
      } 
      if (defaultBehavior != null)
        try {
          layoutParams.setBehavior((Behavior)defaultBehavior.value().getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
        } catch (Exception behavior) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Default behavior class ");
          stringBuilder.append(defaultBehavior.value().getName());
          stringBuilder.append(" could not be instantiated. Did you forget");
          stringBuilder.append(" a default constructor?");
          Log.e("CoordinatorLayout", stringBuilder.toString(), behavior);
        }  
      layoutParams.mBehaviorResolved = true;
    } 
    return layoutParams;
  }
  
  @Nullable
  public Drawable getStatusBarBackground() { return this.mStatusBarBackground; }
  
  protected int getSuggestedMinimumHeight() { return Math.max(super.getSuggestedMinimumHeight(), getPaddingTop() + getPaddingBottom()); }
  
  protected int getSuggestedMinimumWidth() { return Math.max(super.getSuggestedMinimumWidth(), getPaddingLeft() + getPaddingRight()); }
  
  public boolean isPointInChildBounds(@NonNull View paramView, int paramInt1, int paramInt2) {
    rect = acquireTempRect();
    getDescendantRect(paramView, rect);
    try {
      return rect.contains(paramInt1, paramInt2);
    } finally {
      releaseTempRect(rect);
    } 
  }
  
  void offsetChildToAnchor(View paramView, int paramInt) { // Byte code:
    //   0: aload_1
    //   1: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   4: checkcast android/support/design/widget/CoordinatorLayout$LayoutParams
    //   7: astore #6
    //   9: aload #6
    //   11: getfield mAnchorView : Landroid/view/View;
    //   14: ifnull -> 212
    //   17: invokestatic acquireTempRect : ()Landroid/graphics/Rect;
    //   20: astore #7
    //   22: invokestatic acquireTempRect : ()Landroid/graphics/Rect;
    //   25: astore #8
    //   27: invokestatic acquireTempRect : ()Landroid/graphics/Rect;
    //   30: astore #9
    //   32: aload_0
    //   33: aload #6
    //   35: getfield mAnchorView : Landroid/view/View;
    //   38: aload #7
    //   40: invokevirtual getDescendantRect : (Landroid/view/View;Landroid/graphics/Rect;)V
    //   43: iconst_0
    //   44: istore_3
    //   45: aload_0
    //   46: aload_1
    //   47: iconst_0
    //   48: aload #8
    //   50: invokevirtual getChildRect : (Landroid/view/View;ZLandroid/graphics/Rect;)V
    //   53: aload_1
    //   54: invokevirtual getMeasuredWidth : ()I
    //   57: istore #4
    //   59: aload_1
    //   60: invokevirtual getMeasuredHeight : ()I
    //   63: istore #5
    //   65: aload_0
    //   66: aload_1
    //   67: iload_2
    //   68: aload #7
    //   70: aload #9
    //   72: aload #6
    //   74: iload #4
    //   76: iload #5
    //   78: invokespecial getDesiredAnchoredChildRectWithoutConstraints : (Landroid/view/View;ILandroid/graphics/Rect;Landroid/graphics/Rect;Landroid/support/design/widget/CoordinatorLayout$LayoutParams;II)V
    //   81: aload #9
    //   83: getfield left : I
    //   86: aload #8
    //   88: getfield left : I
    //   91: if_icmpne -> 109
    //   94: iload_3
    //   95: istore_2
    //   96: aload #9
    //   98: getfield top : I
    //   101: aload #8
    //   103: getfield top : I
    //   106: if_icmpeq -> 111
    //   109: iconst_1
    //   110: istore_2
    //   111: aload_0
    //   112: aload #6
    //   114: aload #9
    //   116: iload #4
    //   118: iload #5
    //   120: invokespecial constrainChildRect : (Landroid/support/design/widget/CoordinatorLayout$LayoutParams;Landroid/graphics/Rect;II)V
    //   123: aload #9
    //   125: getfield left : I
    //   128: aload #8
    //   130: getfield left : I
    //   133: isub
    //   134: istore_3
    //   135: aload #9
    //   137: getfield top : I
    //   140: aload #8
    //   142: getfield top : I
    //   145: isub
    //   146: istore #4
    //   148: iload_3
    //   149: ifeq -> 157
    //   152: aload_1
    //   153: iload_3
    //   154: invokestatic offsetLeftAndRight : (Landroid/view/View;I)V
    //   157: iload #4
    //   159: ifeq -> 168
    //   162: aload_1
    //   163: iload #4
    //   165: invokestatic offsetTopAndBottom : (Landroid/view/View;I)V
    //   168: iload_2
    //   169: ifeq -> 197
    //   172: aload #6
    //   174: invokevirtual getBehavior : ()Landroid/support/design/widget/CoordinatorLayout$Behavior;
    //   177: astore #10
    //   179: aload #10
    //   181: ifnull -> 197
    //   184: aload #10
    //   186: aload_0
    //   187: aload_1
    //   188: aload #6
    //   190: getfield mAnchorView : Landroid/view/View;
    //   193: invokevirtual onDependentViewChanged : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;Landroid/view/View;)Z
    //   196: pop
    //   197: aload #7
    //   199: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   202: aload #8
    //   204: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   207: aload #9
    //   209: invokestatic releaseTempRect : (Landroid/graphics/Rect;)V
    //   212: return }
  
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    resetTouchBehaviors(false);
    if (this.mNeedsPreDrawListener) {
      if (this.mOnPreDrawListener == null)
        this.mOnPreDrawListener = new OnPreDrawListener(); 
      getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
    } 
    if (this.mLastInsets == null && ViewCompat.getFitsSystemWindows(this))
      ViewCompat.requestApplyInsets(this); 
    this.mIsAttachedToWindow = true;
  }
  
  final void onChildViewsChanged(int paramInt) {
    int i = ViewCompat.getLayoutDirection(this);
    int j = this.mDependencySortedChildren.size();
    Rect rect1 = acquireTempRect();
    Rect rect2 = acquireTempRect();
    Rect rect3 = acquireTempRect();
    for (byte b = 0; b < j; b++) {
      View view = (View)this.mDependencySortedChildren.get(b);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (paramInt == 0 && view.getVisibility() == 8)
        continue; 
      int k;
      for (k = 0; k < b; k++) {
        View view1 = (View)this.mDependencySortedChildren.get(k);
        if (layoutParams.mAnchorDirectChild == view1)
          offsetChildToAnchor(view, i); 
      } 
      getChildRect(view, true, rect2);
      if (layoutParams.insetEdge != 0 && !rect2.isEmpty()) {
        k = GravityCompat.getAbsoluteGravity(layoutParams.insetEdge, i);
        int m = k & 0x70;
        if (m != 48) {
          if (m == 80)
            rect1.bottom = Math.max(rect1.bottom, getHeight() - rect2.top); 
        } else {
          rect1.top = Math.max(rect1.top, rect2.bottom);
        } 
        k &= 0x7;
        if (k != 3) {
          if (k == 5)
            rect1.right = Math.max(rect1.right, getWidth() - rect2.left); 
        } else {
          rect1.left = Math.max(rect1.left, rect2.right);
        } 
      } 
      if (layoutParams.dodgeInsetEdges != 0 && view.getVisibility() == 0)
        offsetChildByInset(view, rect1, i); 
      if (paramInt != 2) {
        getLastChildRect(view, rect3);
        if (rect3.equals(rect2))
          continue; 
        recordLastChildRect(view, rect2);
      } 
      for (k = b + 1; k < j; k++) {
        View view1 = (View)this.mDependencySortedChildren.get(k);
        LayoutParams layoutParams1 = (LayoutParams)view1.getLayoutParams();
        Behavior behavior = layoutParams1.getBehavior();
        if (behavior != null && behavior.layoutDependsOn(this, view1, view))
          if (paramInt == 0 && layoutParams1.getChangedAfterNestedScroll()) {
            layoutParams1.resetChangedAfterNestedScroll();
          } else {
            boolean bool;
            if (paramInt != 2) {
              bool = behavior.onDependentViewChanged(this, view1, view);
            } else {
              behavior.onDependentViewRemoved(this, view1, view);
              bool = true;
            } 
            if (paramInt == 1)
              layoutParams1.setChangedAfterNestedScroll(bool); 
          }  
      } 
      continue;
    } 
    releaseTempRect(rect1);
    releaseTempRect(rect2);
    releaseTempRect(rect3);
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    resetTouchBehaviors(false);
    if (this.mNeedsPreDrawListener && this.mOnPreDrawListener != null)
      getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener); 
    View view = this.mNestedScrollingTarget;
    if (view != null)
      onStopNestedScroll(view); 
    this.mIsAttachedToWindow = false;
  }
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
      byte b;
      WindowInsetsCompat windowInsetsCompat = this.mLastInsets;
      if (windowInsetsCompat != null) {
        b = windowInsetsCompat.getSystemWindowInsetTop();
      } else {
        b = 0;
      } 
      if (b) {
        this.mStatusBarBackground.setBounds(0, 0, getWidth(), b);
        this.mStatusBarBackground.draw(paramCanvas);
      } 
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionMasked();
    if (i == 0)
      resetTouchBehaviors(true); 
    boolean bool = performIntercept(paramMotionEvent, 0);
    if (i == 1 || i == 3)
      resetTouchBehaviors(true); 
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt2 = ViewCompat.getLayoutDirection(this);
    paramInt3 = this.mDependencySortedChildren.size();
    for (paramInt1 = 0; paramInt1 < paramInt3; paramInt1++) {
      View view = (View)this.mDependencySortedChildren.get(paramInt1);
      if (view.getVisibility() != 8) {
        Behavior behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
        if (behavior == null || !behavior.onLayoutChild(this, view, paramInt2))
          onLayoutChild(view, paramInt2); 
      } 
    } 
  }
  
  public void onLayoutChild(@NonNull View paramView, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (!layoutParams.checkAnchorChanged()) {
      if (layoutParams.mAnchorView != null) {
        layoutChildWithAnchor(paramView, layoutParams.mAnchorView, paramInt);
        return;
      } 
      if (layoutParams.keyline >= 0) {
        layoutChildWithKeyline(paramView, layoutParams.keyline, paramInt);
        return;
      } 
      layoutChild(paramView, paramInt);
      return;
    } 
    throw new IllegalStateException("An anchor may not be changed after CoordinatorLayout measurement begins before layout is complete.");
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    boolean bool1;
    prepareChildren();
    ensurePreDrawListener();
    int i1 = getPaddingLeft();
    int i2 = getPaddingTop();
    int i3 = getPaddingRight();
    int i4 = getPaddingBottom();
    int i5 = ViewCompat.getLayoutDirection(this);
    boolean bool2 = true;
    if (i5 == 1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    int i6 = View.MeasureSpec.getMode(paramInt1);
    int i7 = View.MeasureSpec.getSize(paramInt1);
    int m = View.MeasureSpec.getMode(paramInt2);
    int i8 = View.MeasureSpec.getSize(paramInt2);
    int k = getSuggestedMinimumWidth();
    int i = getSuggestedMinimumHeight();
    if (this.mLastInsets == null || !ViewCompat.getFitsSystemWindows(this))
      bool2 = false; 
    int n = this.mDependencySortedChildren.size();
    byte b = 0;
    int j = 0;
    while (b < n) {
      View view = (View)this.mDependencySortedChildren.get(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        int i9 = 0;
        if (layoutParams.keyline >= 0 && i6 != 0) {
          int i12 = getKeyline(layoutParams.keyline);
          int i13 = GravityCompat.getAbsoluteGravity(resolveKeylineGravity(layoutParams.gravity), i5) & 0x7;
          if ((i13 == 3 && !bool1) || (i13 == 5 && bool1)) {
            i9 = Math.max(0, i7 - i3 - i12);
          } else if ((i13 == 5 && !bool1) || (i13 == 3 && bool1)) {
            i9 = Math.max(0, i12 - i1);
          } 
        } 
        int i10 = k;
        k = j;
        int i11 = i;
        if (bool2 && !ViewCompat.getFitsSystemWindows(view)) {
          i = this.mLastInsets.getSystemWindowInsetLeft();
          int i13 = this.mLastInsets.getSystemWindowInsetRight();
          j = this.mLastInsets.getSystemWindowInsetTop();
          int i12 = this.mLastInsets.getSystemWindowInsetBottom();
          i = View.MeasureSpec.makeMeasureSpec(i7 - i + i13, i6);
          j = View.MeasureSpec.makeMeasureSpec(i8 - j + i12, m);
        } else {
          i = paramInt1;
          j = paramInt2;
        } 
        Behavior behavior = layoutParams.getBehavior();
        if (behavior == null || !behavior.onMeasureChild(this, view, i, i9, j, 0))
          onMeasureChild(view, i, i9, j, 0); 
        i9 = Math.max(i10, i1 + i3 + view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
        i = Math.max(i11, i2 + i4 + view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
        j = View.combineMeasuredStates(k, view.getMeasuredState());
        k = i9;
      } 
      b++;
    } 
    setMeasuredDimension(View.resolveSizeAndState(k, paramInt1, 0xFF000000 & j), View.resolveSizeAndState(i, paramInt2, j << 16));
  }
  
  public void onMeasureChild(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) { measureChildWithMargins(paramView, paramInt1, paramInt2, paramInt3, paramInt4); }
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    int i = getChildCount();
    boolean bool = false;
    byte b = 0;
    while (b < i) {
      boolean bool1;
      View view = getChildAt(b);
      if (view.getVisibility() == 8) {
        bool1 = bool;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.isNestedScrollAccepted(0)) {
          bool1 = bool;
        } else {
          Behavior behavior = layoutParams.getBehavior();
          bool1 = bool;
          if (behavior != null)
            bool1 = behavior.onNestedFling(this, view, paramView, paramFloat1, paramFloat2, paramBoolean) | bool; 
        } 
      } 
      b++;
      bool = bool1;
    } 
    if (bool)
      onChildViewsChanged(1); 
    return bool;
  }
  
  public boolean onNestedPreFling(View paramView, float paramFloat1, float paramFloat2) {
    boolean bool = false;
    int i = getChildCount();
    byte b = 0;
    while (b < i) {
      boolean bool1;
      View view = getChildAt(b);
      if (view.getVisibility() == 8) {
        bool1 = bool;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.isNestedScrollAccepted(0)) {
          bool1 = bool;
        } else {
          Behavior behavior = layoutParams.getBehavior();
          bool1 = bool;
          if (behavior != null)
            bool1 = bool | behavior.onNestedPreFling(this, view, paramView, paramFloat1, paramFloat2); 
        } 
      } 
      b++;
      bool = bool1;
    } 
    return bool;
  }
  
  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfInt) { onNestedPreScroll(paramView, paramInt1, paramInt2, paramArrayOfInt, 0); }
  
  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3) {
    int m = getChildCount();
    int k = 0;
    int j = 0;
    int i = 0;
    byte b;
    for (b = 0; b < m; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.isNestedScrollAccepted(paramInt3)) {
          Behavior behavior = layoutParams.getBehavior();
          if (behavior != null) {
            int[] arrayOfInt = this.mTempIntPair;
            arrayOfInt[1] = 0;
            arrayOfInt[0] = 0;
            behavior.onNestedPreScroll(this, view, paramView, paramInt1, paramInt2, arrayOfInt, paramInt3);
            if (paramInt1 > 0) {
              i = Math.max(k, this.mTempIntPair[0]);
            } else {
              i = Math.min(k, this.mTempIntPair[0]);
            } 
            if (paramInt2 > 0) {
              j = Math.max(j, this.mTempIntPair[1]);
            } else {
              j = Math.min(j, this.mTempIntPair[1]);
            } 
            int n = 1;
            k = i;
            i = n;
          } 
        } 
      } 
    } 
    paramArrayOfInt[0] = k;
    paramArrayOfInt[1] = j;
    if (i != 0)
      onChildViewsChanged(1); 
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) { onNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4, 0); }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    int i = getChildCount();
    boolean bool = false;
    byte b;
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.isNestedScrollAccepted(paramInt5)) {
          Behavior behavior = layoutParams.getBehavior();
          if (behavior != null) {
            behavior.onNestedScroll(this, view, paramView, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
            bool = true;
          } 
        } 
      } 
    } 
    if (bool)
      onChildViewsChanged(1); 
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt) { onNestedScrollAccepted(paramView1, paramView2, paramInt, 0); }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt1, int paramInt2) {
    this.mNestedScrollingParentHelper.onNestedScrollAccepted(paramView1, paramView2, paramInt1, paramInt2);
    this.mNestedScrollingTarget = paramView2;
    int i = getChildCount();
    byte b;
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (layoutParams.isNestedScrollAccepted(paramInt2)) {
        Behavior behavior = layoutParams.getBehavior();
        if (behavior != null)
          behavior.onNestedScrollAccepted(this, view, paramView1, paramView2, paramInt1, paramInt2); 
      } 
    } 
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    SparseArray sparseArray = savedState.behaviorStates;
    byte b = 0;
    int i = getChildCount();
    while (b < i) {
      View view = getChildAt(b);
      int j = view.getId();
      Behavior behavior = getResolvedLayoutParams(view).getBehavior();
      if (j != -1 && behavior != null) {
        Parcelable parcelable = (Parcelable)sparseArray.get(j);
        if (parcelable != null)
          behavior.onRestoreInstanceState(this, view, parcelable); 
      } 
      b++;
    } 
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    SparseArray sparseArray = new SparseArray();
    byte b = 0;
    int i = getChildCount();
    while (b < i) {
      View view = getChildAt(b);
      int j = view.getId();
      Behavior behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
      if (j != -1 && behavior != null) {
        Parcelable parcelable = behavior.onSaveInstanceState(this, view);
        if (parcelable != null)
          sparseArray.append(j, parcelable); 
      } 
      b++;
    } 
    savedState.behaviorStates = sparseArray;
    return savedState;
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt) { return onStartNestedScroll(paramView1, paramView2, paramInt, 0); }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt1, int paramInt2) {
    int i = getChildCount();
    boolean bool = false;
    byte b;
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Behavior behavior = layoutParams.getBehavior();
        if (behavior != null) {
          boolean bool1 = behavior.onStartNestedScroll(this, view, paramView1, paramView2, paramInt1, paramInt2);
          layoutParams.setNestedScrollAccepted(paramInt2, bool1);
          bool |= bool1;
        } else {
          layoutParams.setNestedScrollAccepted(paramInt2, false);
        } 
      } 
    } 
    return bool;
  }
  
  public void onStopNestedScroll(View paramView) { onStopNestedScroll(paramView, 0); }
  
  public void onStopNestedScroll(View paramView, int paramInt) {
    this.mNestedScrollingParentHelper.onStopNestedScroll(paramView, paramInt);
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (layoutParams.isNestedScrollAccepted(paramInt)) {
        Behavior behavior = layoutParams.getBehavior();
        if (behavior != null)
          behavior.onStopNestedScroll(this, view, paramView, paramInt); 
        layoutParams.resetNestedScroll(paramInt);
        layoutParams.resetChangedAfterNestedScroll();
      } 
    } 
    this.mNestedScrollingTarget = null;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) { // Byte code:
    //   0: iconst_0
    //   1: istore #6
    //   3: iconst_0
    //   4: istore #4
    //   6: aconst_null
    //   7: astore #11
    //   9: aconst_null
    //   10: astore #10
    //   12: aload_1
    //   13: invokevirtual getActionMasked : ()I
    //   16: istore_2
    //   17: aload_0
    //   18: getfield mBehaviorTouchView : Landroid/view/View;
    //   21: ifnonnull -> 48
    //   24: aload_0
    //   25: aload_1
    //   26: iconst_1
    //   27: invokespecial performIntercept : (Landroid/view/MotionEvent;I)Z
    //   30: istore #7
    //   32: iload #7
    //   34: istore #4
    //   36: iload #6
    //   38: istore_3
    //   39: iload #4
    //   41: istore #5
    //   43: iload #7
    //   45: ifeq -> 91
    //   48: aload_0
    //   49: getfield mBehaviorTouchView : Landroid/view/View;
    //   52: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   55: checkcast android/support/design/widget/CoordinatorLayout$LayoutParams
    //   58: invokevirtual getBehavior : ()Landroid/support/design/widget/CoordinatorLayout$Behavior;
    //   61: astore #12
    //   63: iload #6
    //   65: istore_3
    //   66: iload #4
    //   68: istore #5
    //   70: aload #12
    //   72: ifnull -> 91
    //   75: aload #12
    //   77: aload_0
    //   78: aload_0
    //   79: getfield mBehaviorTouchView : Landroid/view/View;
    //   82: aload_1
    //   83: invokevirtual onTouchEvent : (Landroid/support/design/widget/CoordinatorLayout;Landroid/view/View;Landroid/view/MotionEvent;)Z
    //   86: istore_3
    //   87: iload #4
    //   89: istore #5
    //   91: aload_0
    //   92: getfield mBehaviorTouchView : Landroid/view/View;
    //   95: ifnonnull -> 113
    //   98: iload_3
    //   99: aload_0
    //   100: aload_1
    //   101: invokespecial onTouchEvent : (Landroid/view/MotionEvent;)Z
    //   104: ior
    //   105: istore #4
    //   107: aload #11
    //   109: astore_1
    //   110: goto -> 157
    //   113: iload_3
    //   114: istore #4
    //   116: aload #11
    //   118: astore_1
    //   119: iload #5
    //   121: ifeq -> 157
    //   124: aload #10
    //   126: astore_1
    //   127: iconst_0
    //   128: ifne -> 148
    //   131: invokestatic uptimeMillis : ()J
    //   134: lstore #8
    //   136: lload #8
    //   138: lload #8
    //   140: iconst_3
    //   141: fconst_0
    //   142: fconst_0
    //   143: iconst_0
    //   144: invokestatic obtain : (JJIFFI)Landroid/view/MotionEvent;
    //   147: astore_1
    //   148: aload_0
    //   149: aload_1
    //   150: invokespecial onTouchEvent : (Landroid/view/MotionEvent;)Z
    //   153: pop
    //   154: iload_3
    //   155: istore #4
    //   157: aload_1
    //   158: ifnull -> 165
    //   161: aload_1
    //   162: invokevirtual recycle : ()V
    //   165: iload_2
    //   166: iconst_1
    //   167: if_icmpeq -> 175
    //   170: iload_2
    //   171: iconst_3
    //   172: if_icmpne -> 180
    //   175: aload_0
    //   176: iconst_0
    //   177: invokespecial resetTouchBehaviors : (Z)V
    //   180: iload #4
    //   182: ireturn }
  
  void recordLastChildRect(View paramView, Rect paramRect) { ((LayoutParams)paramView.getLayoutParams()).setLastChildRect(paramRect); }
  
  void removePreDrawListener() {
    if (this.mIsAttachedToWindow && this.mOnPreDrawListener != null)
      getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener); 
    this.mNeedsPreDrawListener = false;
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean) {
    Behavior behavior = ((LayoutParams)paramView.getLayoutParams()).getBehavior();
    return (behavior != null && behavior.onRequestChildRectangleOnScreen(this, paramView, paramRect, paramBoolean)) ? true : super.requestChildRectangleOnScreen(paramView, paramRect, paramBoolean);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    super.requestDisallowInterceptTouchEvent(paramBoolean);
    if (paramBoolean && !this.mDisallowInterceptReset) {
      resetTouchBehaviors(false);
      this.mDisallowInterceptReset = true;
    } 
  }
  
  public void setFitsSystemWindows(boolean paramBoolean) {
    super.setFitsSystemWindows(paramBoolean);
    setupForInsets();
  }
  
  public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener paramOnHierarchyChangeListener) { this.mOnHierarchyChangeListener = paramOnHierarchyChangeListener; }
  
  public void setStatusBarBackground(@Nullable Drawable paramDrawable) {
    Drawable drawable = this.mStatusBarBackground;
    if (drawable != paramDrawable) {
      Drawable drawable1 = null;
      if (drawable != null)
        drawable.setCallback(null); 
      if (paramDrawable != null)
        drawable1 = paramDrawable.mutate(); 
      this.mStatusBarBackground = drawable1;
      paramDrawable = this.mStatusBarBackground;
      if (paramDrawable != null) {
        boolean bool;
        if (paramDrawable.isStateful())
          this.mStatusBarBackground.setState(getDrawableState()); 
        DrawableCompat.setLayoutDirection(this.mStatusBarBackground, ViewCompat.getLayoutDirection(this));
        paramDrawable = this.mStatusBarBackground;
        if (getVisibility() == 0) {
          bool = true;
        } else {
          bool = false;
        } 
        paramDrawable.setVisible(bool, false);
        this.mStatusBarBackground.setCallback(this);
      } 
      ViewCompat.postInvalidateOnAnimation(this);
    } 
  }
  
  public void setStatusBarBackgroundColor(@ColorInt int paramInt) { setStatusBarBackground(new ColorDrawable(paramInt)); }
  
  public void setStatusBarBackgroundResource(@DrawableRes int paramInt) {
    Drawable drawable;
    if (paramInt != 0) {
      drawable = ContextCompat.getDrawable(getContext(), paramInt);
    } else {
      drawable = null;
    } 
    setStatusBarBackground(drawable);
  }
  
  public void setVisibility(int paramInt) {
    boolean bool;
    super.setVisibility(paramInt);
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    Drawable drawable = this.mStatusBarBackground;
    if (drawable != null && drawable.isVisible() != bool)
      this.mStatusBarBackground.setVisible(bool, false); 
  }
  
  final WindowInsetsCompat setWindowInsets(WindowInsetsCompat paramWindowInsetsCompat) {
    WindowInsetsCompat windowInsetsCompat = paramWindowInsetsCompat;
    if (!ObjectsCompat.equals(this.mLastInsets, paramWindowInsetsCompat)) {
      int i;
      this.mLastInsets = paramWindowInsetsCompat;
      byte b = 1;
      if (paramWindowInsetsCompat != null && paramWindowInsetsCompat.getSystemWindowInsetTop() > 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mDrawStatusBarBackground = i;
      if (!this.mDrawStatusBarBackground && getBackground() == null) {
        i = b;
      } else {
        i = 0;
      } 
      setWillNotDraw(i);
      windowInsetsCompat = dispatchApplyWindowInsetsToBehaviors(paramWindowInsetsCompat);
      requestLayout();
    } 
    return windowInsetsCompat;
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) { return (super.verifyDrawable(paramDrawable) || paramDrawable == this.mStatusBarBackground); }
  
  public static interface AttachedBehavior {
    @NonNull
    CoordinatorLayout.Behavior getBehavior();
  }
  
  public static abstract class Behavior<V extends View> extends Object {
    public Behavior() {}
    
    public Behavior(Context param1Context, AttributeSet param1AttributeSet) {}
    
    @Nullable
    public static Object getTag(@NonNull View param1View) { return ((CoordinatorLayout.LayoutParams)param1View.getLayoutParams()).mBehaviorTag; }
    
    public static void setTag(@NonNull View param1View, @Nullable Object param1Object) { ((CoordinatorLayout.LayoutParams)param1View.getLayoutParams()).mBehaviorTag = param1Object; }
    
    public boolean blocksInteractionBelow(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V) { return (getScrimOpacity(param1CoordinatorLayout, param1V) > 0.0F); }
    
    public boolean getInsetDodgeRect(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull Rect param1Rect) { return false; }
    
    @ColorInt
    public int getScrimColor(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V) { return -16777216; }
    
    @FloatRange(from = 0.0D, to = 1.0D)
    public float getScrimOpacity(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V) { return 0.0F; }
    
    public boolean layoutDependsOn(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View) { return false; }
    
    @NonNull
    public WindowInsetsCompat onApplyWindowInsets(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull WindowInsetsCompat param1WindowInsetsCompat) { return param1WindowInsetsCompat; }
    
    public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams param1LayoutParams) {}
    
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View) { return false; }
    
    public void onDependentViewRemoved(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View) {}
    
    public void onDetachedFromLayoutParams() {}
    
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull MotionEvent param1MotionEvent) { return false; }
    
    public boolean onLayoutChild(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, int param1Int) { return false; }
    
    public boolean onMeasureChild(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, int param1Int1, int param1Int2, int param1Int3, int param1Int4) { return false; }
    
    public boolean onNestedFling(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, float param1Float1, float param1Float2, boolean param1Boolean) { return false; }
    
    public boolean onNestedPreFling(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, float param1Float1, float param1Float2) { return false; }
    
    @Deprecated
    public void onNestedPreScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, int param1Int1, int param1Int2, @NonNull int[] param1ArrayOfInt) {}
    
    public void onNestedPreScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, int param1Int1, int param1Int2, @NonNull int[] param1ArrayOfInt, int param1Int3) {
      if (param1Int3 == 0)
        onNestedPreScroll(param1CoordinatorLayout, param1V, param1View, param1Int1, param1Int2, param1ArrayOfInt); 
    }
    
    @Deprecated
    public void onNestedScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {}
    
    public void onNestedScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) {
      if (param1Int5 == 0)
        onNestedScroll(param1CoordinatorLayout, param1V, param1View, param1Int1, param1Int2, param1Int3, param1Int4); 
    }
    
    @Deprecated
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View1, @NonNull View param1View2, int param1Int) {}
    
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View1, @NonNull View param1View2, int param1Int1, int param1Int2) {
      if (param1Int2 == 0)
        onNestedScrollAccepted(param1CoordinatorLayout, param1V, param1View1, param1View2, param1Int1); 
    }
    
    public boolean onRequestChildRectangleOnScreen(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull Rect param1Rect, boolean param1Boolean) { return false; }
    
    public void onRestoreInstanceState(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull Parcelable param1Parcelable) {}
    
    @Nullable
    public Parcelable onSaveInstanceState(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V) { return View.BaseSavedState.EMPTY_STATE; }
    
    @Deprecated
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View1, @NonNull View param1View2, int param1Int) { return false; }
    
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View1, @NonNull View param1View2, int param1Int1, int param1Int2) { return (param1Int2 == 0) ? onStartNestedScroll(param1CoordinatorLayout, param1V, param1View1, param1View2, param1Int1) : 0; }
    
    @Deprecated
    public void onStopNestedScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View) {}
    
    public void onStopNestedScroll(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull View param1View, int param1Int) {
      if (param1Int == 0)
        onStopNestedScroll(param1CoordinatorLayout, param1V, param1View); 
    }
    
    public boolean onTouchEvent(@NonNull CoordinatorLayout param1CoordinatorLayout, @NonNull V param1V, @NonNull MotionEvent param1MotionEvent) { return false; }
  }
  
  @Deprecated
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface DefaultBehavior {
    Class<? extends CoordinatorLayout.Behavior> value();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface DispatchChangeEvent {}
  
  private class HierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
    public void onChildViewAdded(View param1View1, View param1View2) {
      if (CoordinatorLayout.this.mOnHierarchyChangeListener != null)
        CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewAdded(param1View1, param1View2); 
    }
    
    public void onChildViewRemoved(View param1View1, View param1View2) {
      CoordinatorLayout.this.onChildViewsChanged(2);
      if (CoordinatorLayout.this.mOnHierarchyChangeListener != null)
        CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewRemoved(param1View1, param1View2); 
    }
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public int anchorGravity = 0;
    
    public int dodgeInsetEdges = 0;
    
    public int gravity = 0;
    
    public int insetEdge = 0;
    
    public int keyline = -1;
    
    View mAnchorDirectChild;
    
    int mAnchorId = -1;
    
    View mAnchorView;
    
    CoordinatorLayout.Behavior mBehavior;
    
    boolean mBehaviorResolved = false;
    
    Object mBehaviorTag;
    
    private boolean mDidAcceptNestedScrollNonTouch;
    
    private boolean mDidAcceptNestedScrollTouch;
    
    private boolean mDidBlockInteraction;
    
    private boolean mDidChangeAfterNestedScroll;
    
    int mInsetOffsetX;
    
    int mInsetOffsetY;
    
    final Rect mLastChildRect = new Rect();
    
    public LayoutParams(int param1Int1, int param1Int2) { super(param1Int1, param1Int2); }
    
    LayoutParams(@NonNull Context param1Context, @Nullable AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.CoordinatorLayout_Layout);
      this.gravity = typedArray.getInteger(R.styleable.CoordinatorLayout_Layout_android_layout_gravity, 0);
      this.mAnchorId = typedArray.getResourceId(R.styleable.CoordinatorLayout_Layout_layout_anchor, -1);
      this.anchorGravity = typedArray.getInteger(R.styleable.CoordinatorLayout_Layout_layout_anchorGravity, 0);
      this.keyline = typedArray.getInteger(R.styleable.CoordinatorLayout_Layout_layout_keyline, -1);
      this.insetEdge = typedArray.getInt(R.styleable.CoordinatorLayout_Layout_layout_insetEdge, 0);
      this.dodgeInsetEdges = typedArray.getInt(R.styleable.CoordinatorLayout_Layout_layout_dodgeInsetEdges, 0);
      this.mBehaviorResolved = typedArray.hasValue(R.styleable.CoordinatorLayout_Layout_layout_behavior);
      if (this.mBehaviorResolved)
        this.mBehavior = CoordinatorLayout.parseBehavior(param1Context, param1AttributeSet, typedArray.getString(R.styleable.CoordinatorLayout_Layout_layout_behavior)); 
      typedArray.recycle();
      CoordinatorLayout.Behavior behavior = this.mBehavior;
      if (behavior != null)
        behavior.onAttachedToLayoutParams(this); 
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) { super(param1LayoutParams); }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) { super(param1LayoutParams); }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) { super(param1MarginLayoutParams); }
    
    private void resolveAnchorView(View param1View, CoordinatorLayout param1CoordinatorLayout) {
      this.mAnchorView = param1CoordinatorLayout.findViewById(this.mAnchorId);
      View view = this.mAnchorView;
      if (view != null) {
        if (view == param1CoordinatorLayout) {
          if (param1CoordinatorLayout.isInEditMode()) {
            this.mAnchorDirectChild = null;
            this.mAnchorView = null;
            return;
          } 
          throw new IllegalStateException("View can not be anchored to the the parent CoordinatorLayout");
        } 
        View view1 = this.mAnchorView;
        for (ViewParent viewParent = view.getParent(); viewParent != param1CoordinatorLayout && viewParent != null; viewParent = viewParent.getParent()) {
          if (viewParent == param1View) {
            if (param1CoordinatorLayout.isInEditMode()) {
              this.mAnchorDirectChild = null;
              this.mAnchorView = null;
              return;
            } 
            throw new IllegalStateException("Anchor must not be a descendant of the anchored view");
          } 
          if (viewParent instanceof View)
            view1 = (View)viewParent; 
        } 
        this.mAnchorDirectChild = view1;
        return;
      } 
      if (param1CoordinatorLayout.isInEditMode()) {
        this.mAnchorDirectChild = null;
        this.mAnchorView = null;
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Could not find CoordinatorLayout descendant view with id ");
      stringBuilder.append(param1CoordinatorLayout.getResources().getResourceName(this.mAnchorId));
      stringBuilder.append(" to anchor view ");
      stringBuilder.append(param1View);
      throw new IllegalStateException(stringBuilder.toString());
    }
    
    private boolean shouldDodge(View param1View, int param1Int) {
      int i = GravityCompat.getAbsoluteGravity(((LayoutParams)param1View.getLayoutParams()).insetEdge, param1Int);
      return (i != 0 && (GravityCompat.getAbsoluteGravity(this.dodgeInsetEdges, param1Int) & i) == i);
    }
    
    private boolean verifyAnchorView(View param1View, CoordinatorLayout param1CoordinatorLayout) {
      if (this.mAnchorView.getId() != this.mAnchorId)
        return false; 
      View view = this.mAnchorView;
      for (ViewParent viewParent = this.mAnchorView.getParent(); viewParent != param1CoordinatorLayout; viewParent = viewParent.getParent()) {
        if (viewParent == null || viewParent == param1View) {
          this.mAnchorDirectChild = null;
          this.mAnchorView = null;
          return false;
        } 
        if (viewParent instanceof View)
          view = (View)viewParent; 
      } 
      this.mAnchorDirectChild = view;
      return true;
    }
    
    boolean checkAnchorChanged() { return (this.mAnchorView == null && this.mAnchorId != -1); }
    
    boolean dependsOn(CoordinatorLayout param1CoordinatorLayout, View param1View1, View param1View2) {
      if (param1View2 != this.mAnchorDirectChild && !shouldDodge(param1View2, ViewCompat.getLayoutDirection(param1CoordinatorLayout))) {
        CoordinatorLayout.Behavior behavior = this.mBehavior;
        if (behavior == null || !behavior.layoutDependsOn(param1CoordinatorLayout, param1View1, param1View2))
          return false; 
      } 
      return true;
    }
    
    boolean didBlockInteraction() {
      if (this.mBehavior == null)
        this.mDidBlockInteraction = false; 
      return this.mDidBlockInteraction;
    }
    
    View findAnchorView(CoordinatorLayout param1CoordinatorLayout, View param1View) {
      if (this.mAnchorId == -1) {
        this.mAnchorDirectChild = null;
        this.mAnchorView = null;
        return null;
      } 
      if (this.mAnchorView == null || !verifyAnchorView(param1View, param1CoordinatorLayout))
        resolveAnchorView(param1View, param1CoordinatorLayout); 
      return this.mAnchorView;
    }
    
    @IdRes
    public int getAnchorId() { return this.mAnchorId; }
    
    @Nullable
    public CoordinatorLayout.Behavior getBehavior() { return this.mBehavior; }
    
    boolean getChangedAfterNestedScroll() { return this.mDidChangeAfterNestedScroll; }
    
    Rect getLastChildRect() { return this.mLastChildRect; }
    
    void invalidateAnchor() {
      this.mAnchorDirectChild = null;
      this.mAnchorView = null;
    }
    
    boolean isBlockingInteractionBelow(CoordinatorLayout param1CoordinatorLayout, View param1View) {
      boolean bool2 = this.mDidBlockInteraction;
      if (bool2)
        return true; 
      CoordinatorLayout.Behavior behavior = this.mBehavior;
      if (behavior != null) {
        bool1 = behavior.blocksInteractionBelow(param1CoordinatorLayout, param1View);
      } else {
        bool1 = false;
      } 
      boolean bool1 = bool2 | bool1;
      this.mDidBlockInteraction = bool1;
      return bool1;
    }
    
    boolean isNestedScrollAccepted(int param1Int) {
      switch (param1Int) {
        default:
          return false;
        case 1:
          return this.mDidAcceptNestedScrollNonTouch;
        case 0:
          break;
      } 
      return this.mDidAcceptNestedScrollTouch;
    }
    
    void resetChangedAfterNestedScroll() { this.mDidChangeAfterNestedScroll = false; }
    
    void resetNestedScroll(int param1Int) { setNestedScrollAccepted(param1Int, false); }
    
    void resetTouchBehaviorTracking() { this.mDidBlockInteraction = false; }
    
    public void setAnchorId(@IdRes int param1Int) {
      invalidateAnchor();
      this.mAnchorId = param1Int;
    }
    
    public void setBehavior(@Nullable CoordinatorLayout.Behavior param1Behavior) {
      CoordinatorLayout.Behavior behavior = this.mBehavior;
      if (behavior != param1Behavior) {
        if (behavior != null)
          behavior.onDetachedFromLayoutParams(); 
        this.mBehavior = param1Behavior;
        this.mBehaviorTag = null;
        this.mBehaviorResolved = true;
        if (param1Behavior != null)
          param1Behavior.onAttachedToLayoutParams(this); 
      } 
    }
    
    void setChangedAfterNestedScroll(boolean param1Boolean) { this.mDidChangeAfterNestedScroll = param1Boolean; }
    
    void setLastChildRect(Rect param1Rect) { this.mLastChildRect.set(param1Rect); }
    
    void setNestedScrollAccepted(int param1Int, boolean param1Boolean) {
      switch (param1Int) {
        default:
          return;
        case 1:
          this.mDidAcceptNestedScrollNonTouch = param1Boolean;
          return;
        case 0:
          break;
      } 
      this.mDidAcceptNestedScrollTouch = param1Boolean;
    }
  }
  
  class OnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
    public boolean onPreDraw() {
      CoordinatorLayout.this.onChildViewsChanged(0);
      return true;
    }
  }
  
  protected static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
        public CoordinatorLayout.SavedState createFromParcel(Parcel param2Parcel) { return new CoordinatorLayout.SavedState(param2Parcel, null); }
        
        public CoordinatorLayout.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) { return new CoordinatorLayout.SavedState(param2Parcel, param2ClassLoader); }
        
        public CoordinatorLayout.SavedState[] newArray(int param2Int) { return new CoordinatorLayout.SavedState[param2Int]; }
      };
    
    SparseArray<Parcelable> behaviorStates;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      int i = param1Parcel.readInt();
      int[] arrayOfInt = new int[i];
      param1Parcel.readIntArray(arrayOfInt);
      Parcelable[] arrayOfParcelable = param1Parcel.readParcelableArray(param1ClassLoader);
      this.behaviorStates = new SparseArray(i);
      for (byte b = 0; b < i; b++)
        this.behaviorStates.append(arrayOfInt[b], arrayOfParcelable[b]); 
    }
    
    public SavedState(Parcelable param1Parcelable) { super(param1Parcelable); }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      byte b1;
      super.writeToParcel(param1Parcel, param1Int);
      SparseArray sparseArray = this.behaviorStates;
      if (sparseArray != null) {
        b1 = sparseArray.size();
      } else {
        b1 = 0;
      } 
      param1Parcel.writeInt(b1);
      int[] arrayOfInt = new int[b1];
      Parcelable[] arrayOfParcelable = new Parcelable[b1];
      byte b2;
      for (b2 = 0; b2 < b1; b2++) {
        arrayOfInt[b2] = this.behaviorStates.keyAt(b2);
        arrayOfParcelable[b2] = (Parcelable)this.behaviorStates.valueAt(b2);
      } 
      param1Parcel.writeIntArray(arrayOfInt);
      param1Parcel.writeParcelableArray(arrayOfParcelable, param1Int);
    }
  }
  
  static final class null extends Object implements Parcelable.ClassLoaderCreator<SavedState> {
    public CoordinatorLayout.SavedState createFromParcel(Parcel param1Parcel) { return new CoordinatorLayout.SavedState(param1Parcel, null); }
    
    public CoordinatorLayout.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) { return new CoordinatorLayout.SavedState(param1Parcel, param1ClassLoader); }
    
    public CoordinatorLayout.SavedState[] newArray(int param1Int) { return new CoordinatorLayout.SavedState[param1Int]; }
  }
  
  static class ViewElevationComparator extends Object implements Comparator<View> {
    public int compare(View param1View1, View param1View2) {
      float f1 = ViewCompat.getZ(param1View1);
      float f2 = ViewCompat.getZ(param1View2);
      return (f1 > f2) ? -1 : ((f1 < f2) ? 1 : 0);
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\design\widget\CoordinatorLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */