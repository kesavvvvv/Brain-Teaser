package android.support.v4.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawerLayout extends ViewGroup {
  private static final boolean ALLOW_EDGE_LOCK = false;
  
  static final boolean CAN_HIDE_DESCENDANTS;
  
  private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
  
  private static final int DEFAULT_SCRIM_COLOR = -1728053248;
  
  private static final int DRAWER_ELEVATION = 10;
  
  static final int[] LAYOUT_ATTRS;
  
  public static final int LOCK_MODE_LOCKED_CLOSED = 1;
  
  public static final int LOCK_MODE_LOCKED_OPEN = 2;
  
  public static final int LOCK_MODE_UNDEFINED = 3;
  
  public static final int LOCK_MODE_UNLOCKED = 0;
  
  private static final int MIN_DRAWER_MARGIN = 64;
  
  private static final int MIN_FLING_VELOCITY = 400;
  
  private static final int PEEK_DELAY = 160;
  
  private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_IDLE = 0;
  
  public static final int STATE_SETTLING = 2;
  
  private static final String TAG = "DrawerLayout";
  
  private static final int[] THEME_ATTRS;
  
  private static final float TOUCH_SLOP_SENSITIVITY = 1.0F;
  
  private final ChildAccessibilityDelegate mChildAccessibilityDelegate = new ChildAccessibilityDelegate();
  
  private Rect mChildHitRect;
  
  private Matrix mChildInvertedMatrix;
  
  private boolean mChildrenCanceledTouch;
  
  private boolean mDisallowInterceptRequested;
  
  private boolean mDrawStatusBarBackground;
  
  private float mDrawerElevation;
  
  private int mDrawerState;
  
  private boolean mFirstLayout = true;
  
  private boolean mInLayout;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  private Object mLastInsets;
  
  private final ViewDragCallback mLeftCallback;
  
  private final ViewDragHelper mLeftDragger;
  
  @Nullable
  private DrawerListener mListener;
  
  private List<DrawerListener> mListeners;
  
  private int mLockModeEnd = 3;
  
  private int mLockModeLeft = 3;
  
  private int mLockModeRight = 3;
  
  private int mLockModeStart = 3;
  
  private int mMinDrawerMargin;
  
  private final ArrayList<View> mNonDrawerViews;
  
  private final ViewDragCallback mRightCallback;
  
  private final ViewDragHelper mRightDragger;
  
  private int mScrimColor = -1728053248;
  
  private float mScrimOpacity;
  
  private Paint mScrimPaint = new Paint();
  
  private Drawable mShadowEnd = null;
  
  private Drawable mShadowLeft = null;
  
  private Drawable mShadowLeftResolved;
  
  private Drawable mShadowRight = null;
  
  private Drawable mShadowRightResolved;
  
  private Drawable mShadowStart = null;
  
  private Drawable mStatusBarBackground;
  
  private CharSequence mTitleLeft;
  
  private CharSequence mTitleRight;
  
  static  {
    byte b1;
    byte b2 = 1;
    THEME_ATTRS = new int[] { 16843828 };
    LAYOUT_ATTRS = new int[] { 16842931 };
    if (Build.VERSION.SDK_INT >= 19) {
      b1 = 1;
    } else {
      b1 = 0;
    } 
    CAN_HIDE_DESCENDANTS = b1;
    if (Build.VERSION.SDK_INT >= 21) {
      b1 = b2;
    } else {
      b1 = 0;
    } 
    SET_DRAWER_SHADOW_FROM_ELEVATION = b1;
  }
  
  public DrawerLayout(@NonNull Context paramContext) { this(paramContext, null); }
  
  public DrawerLayout(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, 0); }
  
  public DrawerLayout(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    setDescendantFocusability(262144);
    float f1 = (getResources().getDisplayMetrics()).density;
    this.mMinDrawerMargin = (int)(64.0F * f1 + 0.5F);
    float f2 = 400.0F * f1;
    this.mLeftCallback = new ViewDragCallback(3);
    this.mRightCallback = new ViewDragCallback(5);
    this.mLeftDragger = ViewDragHelper.create(this, 1.0F, this.mLeftCallback);
    this.mLeftDragger.setEdgeTrackingEnabled(1);
    this.mLeftDragger.setMinVelocity(f2);
    this.mLeftCallback.setDragger(this.mLeftDragger);
    this.mRightDragger = ViewDragHelper.create(this, 1.0F, this.mRightCallback);
    this.mRightDragger.setEdgeTrackingEnabled(2);
    this.mRightDragger.setMinVelocity(f2);
    this.mRightCallback.setDragger(this.mRightDragger);
    setFocusableInTouchMode(true);
    ViewCompat.setImportantForAccessibility(this, 1);
    ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
    setMotionEventSplittingEnabled(false);
    if (ViewCompat.getFitsSystemWindows(this))
      if (Build.VERSION.SDK_INT >= 21) {
        setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
              public WindowInsets onApplyWindowInsets(View param1View, WindowInsets param1WindowInsets) {
                boolean bool;
                DrawerLayout drawerLayout = (DrawerLayout)param1View;
                if (param1WindowInsets.getSystemWindowInsetTop() > 0) {
                  bool = true;
                } else {
                  bool = false;
                } 
                drawerLayout.setChildInsets(param1WindowInsets, bool);
                return param1WindowInsets.consumeSystemWindowInsets();
              }
            });
        setSystemUiVisibility(1280);
        typedArray = paramContext.obtainStyledAttributes(THEME_ATTRS);
        try {
          this.mStatusBarBackground = typedArray.getDrawable(0);
        } finally {
          typedArray.recycle();
        } 
      } else {
        this.mStatusBarBackground = null;
      }  
    this.mDrawerElevation = 10.0F * f1;
    this.mNonDrawerViews = new ArrayList();
  }
  
  private boolean dispatchTransformedGenericPointerEvent(MotionEvent paramMotionEvent, View paramView) {
    if (!paramView.getMatrix().isIdentity()) {
      paramMotionEvent = getTransformedMotionEvent(paramMotionEvent, paramView);
      boolean bool1 = paramView.dispatchGenericMotionEvent(paramMotionEvent);
      paramMotionEvent.recycle();
      return bool1;
    } 
    float f1 = (getScrollX() - paramView.getLeft());
    float f2 = (getScrollY() - paramView.getTop());
    paramMotionEvent.offsetLocation(f1, f2);
    boolean bool = paramView.dispatchGenericMotionEvent(paramMotionEvent);
    paramMotionEvent.offsetLocation(-f1, -f2);
    return bool;
  }
  
  private MotionEvent getTransformedMotionEvent(MotionEvent paramMotionEvent, View paramView) {
    float f1 = (getScrollX() - paramView.getLeft());
    float f2 = (getScrollY() - paramView.getTop());
    paramMotionEvent = MotionEvent.obtain(paramMotionEvent);
    paramMotionEvent.offsetLocation(f1, f2);
    Matrix matrix = paramView.getMatrix();
    if (!matrix.isIdentity()) {
      if (this.mChildInvertedMatrix == null)
        this.mChildInvertedMatrix = new Matrix(); 
      matrix.invert(this.mChildInvertedMatrix);
      paramMotionEvent.transform(this.mChildInvertedMatrix);
    } 
    return paramMotionEvent;
  }
  
  static String gravityToString(int paramInt) { return ((paramInt & 0x3) == 3) ? "LEFT" : (((paramInt & 0x5) == 5) ? "RIGHT" : Integer.toHexString(paramInt)); }
  
  private static boolean hasOpaqueBackground(View paramView) {
    Drawable drawable = paramView.getBackground();
    boolean bool = false;
    if (drawable != null) {
      if (drawable.getOpacity() == -1)
        bool = true; 
      return bool;
    } 
    return false;
  }
  
  private boolean hasPeekingDrawer() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      if (((LayoutParams)getChildAt(b).getLayoutParams()).isPeeking)
        return true; 
    } 
    return false;
  }
  
  private boolean hasVisibleDrawer() { return (findVisibleDrawer() != null); }
  
  static boolean includeChildForAccessibility(View paramView) { return (ViewCompat.getImportantForAccessibility(paramView) != 4 && ViewCompat.getImportantForAccessibility(paramView) != 2); }
  
  private boolean isInBoundsOfChild(float paramFloat1, float paramFloat2, View paramView) {
    if (this.mChildHitRect == null)
      this.mChildHitRect = new Rect(); 
    paramView.getHitRect(this.mChildHitRect);
    return this.mChildHitRect.contains((int)paramFloat1, (int)paramFloat2);
  }
  
  private boolean mirror(Drawable paramDrawable, int paramInt) {
    if (paramDrawable == null || !DrawableCompat.isAutoMirrored(paramDrawable))
      return false; 
    DrawableCompat.setLayoutDirection(paramDrawable, paramInt);
    return true;
  }
  
  private Drawable resolveLeftShadow() {
    int i = ViewCompat.getLayoutDirection(this);
    if (i == 0) {
      Drawable drawable = this.mShadowStart;
      if (drawable != null) {
        mirror(drawable, i);
        return this.mShadowStart;
      } 
    } else {
      Drawable drawable = this.mShadowEnd;
      if (drawable != null) {
        mirror(drawable, i);
        return this.mShadowEnd;
      } 
    } 
    return this.mShadowLeft;
  }
  
  private Drawable resolveRightShadow() {
    int i = ViewCompat.getLayoutDirection(this);
    if (i == 0) {
      Drawable drawable = this.mShadowEnd;
      if (drawable != null) {
        mirror(drawable, i);
        return this.mShadowEnd;
      } 
    } else {
      Drawable drawable = this.mShadowStart;
      if (drawable != null) {
        mirror(drawable, i);
        return this.mShadowStart;
      } 
    } 
    return this.mShadowRight;
  }
  
  private void resolveShadowDrawables() {
    if (SET_DRAWER_SHADOW_FROM_ELEVATION)
      return; 
    this.mShadowLeftResolved = resolveLeftShadow();
    this.mShadowRightResolved = resolveRightShadow();
  }
  
  private void updateChildrenImportantForAccessibility(View paramView, boolean paramBoolean) {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if ((!paramBoolean && !isDrawerView(view)) || (paramBoolean && view == paramView)) {
        ViewCompat.setImportantForAccessibility(view, 1);
      } else {
        ViewCompat.setImportantForAccessibility(view, 4);
      } 
    } 
  }
  
  public void addDrawerListener(@NonNull DrawerListener paramDrawerListener) {
    if (paramDrawerListener == null)
      return; 
    if (this.mListeners == null)
      this.mListeners = new ArrayList(); 
    this.mListeners.add(paramDrawerListener);
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2) {
    if (getDescendantFocusability() == 393216)
      return; 
    int j = getChildCount();
    int i = 0;
    byte b;
    for (b = 0; b < j; b++) {
      View view = getChildAt(b);
      if (isDrawerView(view)) {
        if (isDrawerOpen(view)) {
          i = 1;
          view.addFocusables(paramArrayList, paramInt1, paramInt2);
        } 
      } else {
        this.mNonDrawerViews.add(view);
      } 
    } 
    if (!i) {
      i = this.mNonDrawerViews.size();
      for (b = 0; b < i; b++) {
        View view = (View)this.mNonDrawerViews.get(b);
        if (view.getVisibility() == 0)
          view.addFocusables(paramArrayList, paramInt1, paramInt2); 
      } 
    } 
    this.mNonDrawerViews.clear();
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    super.addView(paramView, paramInt, paramLayoutParams);
    if (findOpenDrawer() != null || isDrawerView(paramView)) {
      ViewCompat.setImportantForAccessibility(paramView, 4);
    } else {
      ViewCompat.setImportantForAccessibility(paramView, 1);
    } 
    if (!CAN_HIDE_DESCENDANTS)
      ViewCompat.setAccessibilityDelegate(paramView, this.mChildAccessibilityDelegate); 
  }
  
  void cancelChildViewTouch() {
    if (!this.mChildrenCanceledTouch) {
      long l = SystemClock.uptimeMillis();
      MotionEvent motionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
      int i = getChildCount();
      for (byte b = 0; b < i; b++)
        getChildAt(b).dispatchTouchEvent(motionEvent); 
      motionEvent.recycle();
      this.mChildrenCanceledTouch = true;
    } 
  }
  
  boolean checkDrawerViewAbsoluteGravity(View paramView, int paramInt) { return ((getDrawerViewAbsoluteGravity(paramView) & paramInt) == paramInt); }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams)); }
  
  public void closeDrawer(int paramInt) { closeDrawer(paramInt, true); }
  
  public void closeDrawer(int paramInt, boolean paramBoolean) {
    View view = findDrawerWithGravity(paramInt);
    if (view != null) {
      closeDrawer(view, paramBoolean);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No drawer view found with gravity ");
    stringBuilder.append(gravityToString(paramInt));
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void closeDrawer(@NonNull View paramView) { closeDrawer(paramView, true); }
  
  public void closeDrawer(@NonNull View paramView, boolean paramBoolean) {
    if (isDrawerView(paramView)) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      if (this.mFirstLayout) {
        layoutParams.onScreen = 0.0F;
        layoutParams.openState = 0;
      } else if (paramBoolean) {
        layoutParams.openState = 0x4 | layoutParams.openState;
        if (checkDrawerViewAbsoluteGravity(paramView, 3)) {
          this.mLeftDragger.smoothSlideViewTo(paramView, -paramView.getWidth(), paramView.getTop());
        } else {
          this.mRightDragger.smoothSlideViewTo(paramView, getWidth(), paramView.getTop());
        } 
      } else {
        moveDrawerToOffset(paramView, 0.0F);
        updateDrawerState(layoutParams.gravity, 0, paramView);
        paramView.setVisibility(4);
      } 
      invalidate();
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("View ");
    stringBuilder.append(paramView);
    stringBuilder.append(" is not a sliding drawer");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void closeDrawers() { closeDrawers(false); }
  
  void closeDrawers(boolean paramBoolean) {
    int i = 0;
    int j = getChildCount();
    byte b = 0;
    while (b < j) {
      View view = getChildAt(b);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      int k = i;
      if (isDrawerView(view))
        if (paramBoolean && !layoutParams.isPeeking) {
          k = i;
        } else {
          k = view.getWidth();
          if (checkDrawerViewAbsoluteGravity(view, 3)) {
            i |= this.mLeftDragger.smoothSlideViewTo(view, -k, view.getTop());
          } else {
            i |= this.mRightDragger.smoothSlideViewTo(view, getWidth(), view.getTop());
          } 
          layoutParams.isPeeking = false;
          k = i;
        }  
      b++;
      i = k;
    } 
    this.mLeftCallback.removeCallbacks();
    this.mRightCallback.removeCallbacks();
    if (i != 0)
      invalidate(); 
  }
  
  public void computeScroll() {
    int i = getChildCount();
    float f = 0.0F;
    for (byte b = 0; b < i; b++)
      f = Math.max(f, ((LayoutParams)getChildAt(b).getLayoutParams()).onScreen); 
    this.mScrimOpacity = f;
    boolean bool1 = this.mLeftDragger.continueSettling(true);
    boolean bool2 = this.mRightDragger.continueSettling(true);
    if (bool1 || bool2)
      ViewCompat.postInvalidateOnAnimation(this); 
  }
  
  public boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent) {
    if ((paramMotionEvent.getSource() & 0x2) == 0 || paramMotionEvent.getAction() == 10 || this.mScrimOpacity <= 0.0F)
      return super.dispatchGenericMotionEvent(paramMotionEvent); 
    int i = getChildCount();
    if (i != 0) {
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      while (--i >= 0) {
        View view = getChildAt(i);
        if (isInBoundsOfChild(f1, f2, view) && !isContentView(view) && dispatchTransformedGenericPointerEvent(paramMotionEvent, view))
          return true; 
        i--;
      } 
    } 
    return false;
  }
  
  void dispatchOnDrawerClosed(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if ((layoutParams.openState & true) == 1) {
      layoutParams.openState = 0;
      List list = this.mListeners;
      if (list != null)
        for (int i = list.size() - 1; i >= 0; i--)
          ((DrawerListener)this.mListeners.get(i)).onDrawerClosed(paramView);  
      updateChildrenImportantForAccessibility(paramView, false);
      if (hasWindowFocus()) {
        paramView = getRootView();
        if (paramView != null)
          paramView.sendAccessibilityEvent(32); 
      } 
    } 
  }
  
  void dispatchOnDrawerOpened(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if ((layoutParams.openState & true) == 0) {
      layoutParams.openState = 1;
      List list = this.mListeners;
      if (list != null)
        for (int i = list.size() - 1; i >= 0; i--)
          ((DrawerListener)this.mListeners.get(i)).onDrawerOpened(paramView);  
      updateChildrenImportantForAccessibility(paramView, true);
      if (hasWindowFocus())
        sendAccessibilityEvent(32); 
    } 
  }
  
  void dispatchOnDrawerSlide(View paramView, float paramFloat) {
    List list = this.mListeners;
    if (list != null)
      for (int i = list.size() - 1; i >= 0; i--)
        ((DrawerListener)this.mListeners.get(i)).onDrawerSlide(paramView, paramFloat);  
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    int m = getHeight();
    boolean bool1 = isContentView(paramView);
    int i = 0;
    int j = getWidth();
    int k = paramCanvas.save();
    if (bool1) {
      int n = getChildCount();
      byte b = 0;
      while (b < n) {
        View view = getChildAt(b);
        int i1 = i;
        int i2 = j;
        if (view != paramView) {
          i1 = i;
          i2 = j;
          if (view.getVisibility() == 0) {
            i1 = i;
            i2 = j;
            if (hasOpaqueBackground(view)) {
              i1 = i;
              i2 = j;
              if (isDrawerView(view))
                if (view.getHeight() < m) {
                  i1 = i;
                  i2 = j;
                } else if (checkDrawerViewAbsoluteGravity(view, 3)) {
                  i2 = view.getRight();
                  i1 = i;
                  if (i2 > i)
                    i1 = i2; 
                  i2 = j;
                } else {
                  int i3 = view.getLeft();
                  i1 = i;
                  i2 = j;
                  if (i3 < j) {
                    i2 = i3;
                    i1 = i;
                  } 
                }  
            } 
          } 
        } 
        b++;
        i = i1;
        j = i2;
      } 
      paramCanvas.clipRect(i, 0, j, getHeight());
    } else {
      i = 0;
    } 
    boolean bool2 = super.drawChild(paramCanvas, paramView, paramLong);
    paramCanvas.restoreToCount(k);
    float f = this.mScrimOpacity;
    if (f > 0.0F && bool1) {
      int n = this.mScrimColor;
      int i1 = (int)(((0xFF000000 & n) >>> 24) * f);
      this.mScrimPaint.setColor(i1 << 24 | n & 0xFFFFFF);
      paramCanvas.drawRect(i, 0.0F, j, getHeight(), this.mScrimPaint);
      return bool2;
    } 
    if (this.mShadowLeftResolved != null && checkDrawerViewAbsoluteGravity(paramView, 3)) {
      i = this.mShadowLeftResolved.getIntrinsicWidth();
      j = paramView.getRight();
      int n = this.mLeftDragger.getEdgeSize();
      f = Math.max(0.0F, Math.min(j / n, 1.0F));
      this.mShadowLeftResolved.setBounds(j, paramView.getTop(), j + i, paramView.getBottom());
      this.mShadowLeftResolved.setAlpha((int)(255.0F * f));
      this.mShadowLeftResolved.draw(paramCanvas);
    } else if (this.mShadowRightResolved != null && checkDrawerViewAbsoluteGravity(paramView, 5)) {
      i = this.mShadowRightResolved.getIntrinsicWidth();
      j = paramView.getLeft();
      int n = getWidth();
      int i1 = this.mRightDragger.getEdgeSize();
      f = Math.max(0.0F, Math.min((n - j) / i1, 1.0F));
      this.mShadowRightResolved.setBounds(j - i, paramView.getTop(), j, paramView.getBottom());
      this.mShadowRightResolved.setAlpha((int)(255.0F * f));
      this.mShadowRightResolved.draw(paramCanvas);
      return bool2;
    } 
    return bool2;
  }
  
  View findDrawerWithGravity(int paramInt) {
    int i = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection(this));
    int j = getChildCount();
    for (paramInt = 0; paramInt < j; paramInt++) {
      View view = getChildAt(paramInt);
      if ((getDrawerViewAbsoluteGravity(view) & 0x7) == (i & 0x7))
        return view; 
    } 
    return null;
  }
  
  View findOpenDrawer() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if ((((LayoutParams)view.getLayoutParams()).openState & true) == 1)
        return view; 
    } 
    return null;
  }
  
  View findVisibleDrawer() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (isDrawerView(view) && isDrawerVisible(view))
        return view; 
    } 
    return null;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() { return new LayoutParams(-1, -1); }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) { return new LayoutParams(getContext(), paramAttributeSet); }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return (paramLayoutParams instanceof LayoutParams) ? new LayoutParams((LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams)); }
  
  public float getDrawerElevation() { return SET_DRAWER_SHADOW_FROM_ELEVATION ? this.mDrawerElevation : 0.0F; }
  
  public int getDrawerLockMode(int paramInt) {
    int i = ViewCompat.getLayoutDirection(this);
    if (paramInt != 3) {
      if (paramInt != 5) {
        if (paramInt != 8388611) {
          if (paramInt == 8388613) {
            paramInt = this.mLockModeEnd;
            if (paramInt != 3)
              return paramInt; 
            if (i == 0) {
              paramInt = this.mLockModeRight;
            } else {
              paramInt = this.mLockModeLeft;
            } 
            if (paramInt != 3)
              return paramInt; 
          } 
        } else {
          paramInt = this.mLockModeStart;
          if (paramInt != 3)
            return paramInt; 
          if (i == 0) {
            paramInt = this.mLockModeLeft;
          } else {
            paramInt = this.mLockModeRight;
          } 
          if (paramInt != 3)
            return paramInt; 
        } 
      } else {
        paramInt = this.mLockModeRight;
        if (paramInt != 3)
          return paramInt; 
        if (i == 0) {
          paramInt = this.mLockModeEnd;
        } else {
          paramInt = this.mLockModeStart;
        } 
        if (paramInt != 3)
          return paramInt; 
      } 
    } else {
      paramInt = this.mLockModeLeft;
      if (paramInt != 3)
        return paramInt; 
      if (i == 0) {
        paramInt = this.mLockModeStart;
      } else {
        paramInt = this.mLockModeEnd;
      } 
      if (paramInt != 3)
        return paramInt; 
    } 
    return 0;
  }
  
  public int getDrawerLockMode(@NonNull View paramView) {
    if (isDrawerView(paramView))
      return getDrawerLockMode(((LayoutParams)paramView.getLayoutParams()).gravity); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("View ");
    stringBuilder.append(paramView);
    stringBuilder.append(" is not a drawer");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @Nullable
  public CharSequence getDrawerTitle(int paramInt) {
    paramInt = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection(this));
    return (paramInt == 3) ? this.mTitleLeft : ((paramInt == 5) ? this.mTitleRight : null);
  }
  
  int getDrawerViewAbsoluteGravity(View paramView) { return GravityCompat.getAbsoluteGravity(((LayoutParams)paramView.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(this)); }
  
  float getDrawerViewOffset(View paramView) { return ((LayoutParams)paramView.getLayoutParams()).onScreen; }
  
  @Nullable
  public Drawable getStatusBarBackgroundDrawable() { return this.mStatusBarBackground; }
  
  boolean isContentView(View paramView) { return (((LayoutParams)paramView.getLayoutParams()).gravity == 0); }
  
  public boolean isDrawerOpen(int paramInt) {
    View view = findDrawerWithGravity(paramInt);
    return (view != null) ? isDrawerOpen(view) : 0;
  }
  
  public boolean isDrawerOpen(@NonNull View paramView) {
    if (isDrawerView(paramView))
      return ((((LayoutParams)paramView.getLayoutParams()).openState & true) == 1); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("View ");
    stringBuilder.append(paramView);
    stringBuilder.append(" is not a drawer");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  boolean isDrawerView(View paramView) {
    int i = GravityCompat.getAbsoluteGravity(((LayoutParams)paramView.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(paramView));
    return ((i & 0x3) != 0) ? true : (((i & 0x5) != 0));
  }
  
  public boolean isDrawerVisible(int paramInt) {
    View view = findDrawerWithGravity(paramInt);
    return (view != null) ? isDrawerVisible(view) : 0;
  }
  
  public boolean isDrawerVisible(@NonNull View paramView) {
    if (isDrawerView(paramView))
      return (((LayoutParams)paramView.getLayoutParams()).onScreen > 0.0F); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("View ");
    stringBuilder.append(paramView);
    stringBuilder.append(" is not a drawer");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  void moveDrawerToOffset(View paramView, float paramFloat) {
    float f = getDrawerViewOffset(paramView);
    int i = paramView.getWidth();
    int j = (int)(i * f);
    i = (int)(i * paramFloat) - j;
    if (!checkDrawerViewAbsoluteGravity(paramView, 3))
      i = -i; 
    paramView.offsetLeftAndRight(i);
    setDrawerViewOffset(paramView, paramFloat);
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    this.mFirstLayout = true;
  }
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
      byte b;
      if (Build.VERSION.SDK_INT >= 21) {
        Object object = this.mLastInsets;
        if (object != null) {
          b = ((WindowInsets)object).getSystemWindowInsetTop();
        } else {
          b = 0;
        } 
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
    float f2;
    float f1;
    int i = paramMotionEvent.getActionMasked();
    boolean bool1 = this.mLeftDragger.shouldInterceptTouchEvent(paramMotionEvent);
    boolean bool2 = this.mRightDragger.shouldInterceptTouchEvent(paramMotionEvent);
    int k = 0;
    int j = 0;
    byte b = 1;
    switch (i) {
      default:
        i = k;
        break;
      case 2:
        i = k;
        if (this.mLeftDragger.checkTouchSlop(3)) {
          this.mLeftCallback.removeCallbacks();
          this.mRightCallback.removeCallbacks();
          i = k;
        } 
        break;
      case 1:
      case 3:
        closeDrawers(true);
        this.mDisallowInterceptRequested = false;
        this.mChildrenCanceledTouch = false;
        i = k;
        break;
      case 0:
        f1 = paramMotionEvent.getX();
        f2 = paramMotionEvent.getY();
        this.mInitialMotionX = f1;
        this.mInitialMotionY = f2;
        i = j;
        if (this.mScrimOpacity > 0.0F) {
          View view = this.mLeftDragger.findTopChildUnder((int)f1, (int)f2);
          i = j;
          if (view != null) {
            i = j;
            if (isContentView(view))
              i = 1; 
          } 
        } 
        this.mDisallowInterceptRequested = false;
        this.mChildrenCanceledTouch = false;
        break;
    } 
    int m = b;
    if (!(bool1 | bool2)) {
      m = b;
      if (i == 0) {
        m = b;
        if (!hasPeekingDrawer()) {
          if (this.mChildrenCanceledTouch)
            return true; 
          m = 0;
        } 
      } 
    } 
    return m;
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    if (paramInt == 4 && hasVisibleDrawer()) {
      paramKeyEvent.startTracking();
      return true;
    } 
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    View view;
    if (paramInt == 4) {
      view = findVisibleDrawer();
      if (view != null && getDrawerLockMode(view) == 0)
        closeDrawers(); 
      return (view != null);
    } 
    return super.onKeyUp(paramInt, view);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mInLayout = true;
    int i = paramInt3 - paramInt1;
    int j = getChildCount();
    for (paramInt3 = 0; paramInt3 < j; paramInt3++) {
      View view = getChildAt(paramInt3);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (isContentView(view)) {
          view.layout(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.leftMargin + view.getMeasuredWidth(), layoutParams.topMargin + view.getMeasuredHeight());
        } else {
          boolean bool;
          int k;
          float f;
          int m = view.getMeasuredWidth();
          int n = view.getMeasuredHeight();
          if (checkDrawerViewAbsoluteGravity(view, 3)) {
            k = -m + (int)(m * layoutParams.onScreen);
            f = (m + k) / m;
          } else {
            k = i - (int)(m * layoutParams.onScreen);
            f = (i - k) / m;
          } 
          if (f != layoutParams.onScreen) {
            bool = true;
          } else {
            bool = false;
          } 
          paramInt1 = layoutParams.gravity & 0x70;
          if (paramInt1 != 16) {
            if (paramInt1 != 80) {
              view.layout(k, layoutParams.topMargin, k + m, layoutParams.topMargin + n);
            } else {
              paramInt1 = paramInt4 - paramInt2;
              view.layout(k, paramInt1 - layoutParams.bottomMargin - view.getMeasuredHeight(), k + m, paramInt1 - layoutParams.bottomMargin);
            } 
          } else {
            int i2 = paramInt4 - paramInt2;
            int i1 = (i2 - n) / 2;
            if (i1 < layoutParams.topMargin) {
              paramInt1 = layoutParams.topMargin;
            } else {
              paramInt1 = i1;
              if (i1 + n > i2 - layoutParams.bottomMargin)
                paramInt1 = i2 - layoutParams.bottomMargin - n; 
            } 
            view.layout(k, paramInt1, k + m, paramInt1 + n);
          } 
          if (bool)
            setDrawerViewOffset(view, f); 
          if (layoutParams.onScreen > 0.0F) {
            paramInt1 = 0;
          } else {
            paramInt1 = 4;
          } 
          if (view.getVisibility() != paramInt1)
            view.setVisibility(paramInt1); 
        } 
      } 
    } 
    this.mInLayout = false;
    this.mFirstLayout = false;
  }
  
  @SuppressLint({"WrongConstant"})
  protected void onMeasure(int paramInt1, int paramInt2) { // Byte code:
    //   0: aload_0
    //   1: astore #17
    //   3: iload_1
    //   4: invokestatic getMode : (I)I
    //   7: istore #9
    //   9: iload_2
    //   10: invokestatic getMode : (I)I
    //   13: istore #13
    //   15: iload_1
    //   16: invokestatic getSize : (I)I
    //   19: istore #8
    //   21: iload_2
    //   22: invokestatic getSize : (I)I
    //   25: istore #12
    //   27: iload #9
    //   29: ldc_w 1073741824
    //   32: if_icmpne -> 59
    //   35: iload #9
    //   37: istore #7
    //   39: iload #13
    //   41: istore #5
    //   43: iload #8
    //   45: istore #10
    //   47: iload #12
    //   49: istore #11
    //   51: iload #13
    //   53: ldc_w 1073741824
    //   56: if_icmpeq -> 168
    //   59: aload_0
    //   60: invokevirtual isInEditMode : ()Z
    //   63: ifeq -> 892
    //   66: iload #9
    //   68: ldc_w -2147483648
    //   71: if_icmpne -> 82
    //   74: ldc_w 1073741824
    //   77: istore #6
    //   79: goto -> 101
    //   82: iload #9
    //   84: istore #6
    //   86: iload #9
    //   88: ifne -> 101
    //   91: ldc_w 1073741824
    //   94: istore #6
    //   96: sipush #300
    //   99: istore #8
    //   101: iload #13
    //   103: ldc_w -2147483648
    //   106: if_icmpne -> 129
    //   109: ldc_w 1073741824
    //   112: istore #5
    //   114: iload #6
    //   116: istore #7
    //   118: iload #8
    //   120: istore #10
    //   122: iload #12
    //   124: istore #11
    //   126: goto -> 168
    //   129: iload #6
    //   131: istore #7
    //   133: iload #13
    //   135: istore #5
    //   137: iload #8
    //   139: istore #10
    //   141: iload #12
    //   143: istore #11
    //   145: iload #13
    //   147: ifne -> 168
    //   150: ldc_w 1073741824
    //   153: istore #5
    //   155: sipush #300
    //   158: istore #11
    //   160: iload #8
    //   162: istore #10
    //   164: iload #6
    //   166: istore #7
    //   168: aload #17
    //   170: iload #10
    //   172: iload #11
    //   174: invokevirtual setMeasuredDimension : (II)V
    //   177: aload #17
    //   179: getfield mLastInsets : Ljava/lang/Object;
    //   182: ifnull -> 198
    //   185: aload_0
    //   186: invokestatic getFitsSystemWindows : (Landroid/view/View;)Z
    //   189: ifeq -> 198
    //   192: iconst_1
    //   193: istore #9
    //   195: goto -> 201
    //   198: iconst_0
    //   199: istore #9
    //   201: aload_0
    //   202: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   205: istore #14
    //   207: iconst_0
    //   208: istore #8
    //   210: iconst_0
    //   211: istore #6
    //   213: aload_0
    //   214: invokevirtual getChildCount : ()I
    //   217: istore #15
    //   219: iconst_0
    //   220: istore #12
    //   222: aload_0
    //   223: astore #18
    //   225: iload #12
    //   227: iload #15
    //   229: if_icmpge -> 891
    //   232: aload #18
    //   234: iload #12
    //   236: invokevirtual getChildAt : (I)Landroid/view/View;
    //   239: astore #20
    //   241: aload #20
    //   243: invokevirtual getVisibility : ()I
    //   246: bipush #8
    //   248: if_icmpne -> 254
    //   251: goto -> 570
    //   254: aload #20
    //   256: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   259: checkcast android/support/v4/widget/DrawerLayout$LayoutParams
    //   262: astore #21
    //   264: iload #9
    //   266: ifeq -> 515
    //   269: aload #21
    //   271: getfield gravity : I
    //   274: iload #14
    //   276: invokestatic getAbsoluteGravity : (II)I
    //   279: istore #13
    //   281: aload #20
    //   283: invokestatic getFitsSystemWindows : (Landroid/view/View;)Z
    //   286: ifeq -> 386
    //   289: getstatic android/os/Build$VERSION.SDK_INT : I
    //   292: bipush #21
    //   294: if_icmplt -> 383
    //   297: aload #18
    //   299: getfield mLastInsets : Ljava/lang/Object;
    //   302: checkcast android/view/WindowInsets
    //   305: astore #19
    //   307: iload #13
    //   309: iconst_3
    //   310: if_icmpne -> 339
    //   313: aload #19
    //   315: aload #19
    //   317: invokevirtual getSystemWindowInsetLeft : ()I
    //   320: aload #19
    //   322: invokevirtual getSystemWindowInsetTop : ()I
    //   325: iconst_0
    //   326: aload #19
    //   328: invokevirtual getSystemWindowInsetBottom : ()I
    //   331: invokevirtual replaceSystemWindowInsets : (IIII)Landroid/view/WindowInsets;
    //   334: astore #17
    //   336: goto -> 372
    //   339: aload #19
    //   341: astore #17
    //   343: iload #13
    //   345: iconst_5
    //   346: if_icmpne -> 372
    //   349: aload #19
    //   351: iconst_0
    //   352: aload #19
    //   354: invokevirtual getSystemWindowInsetTop : ()I
    //   357: aload #19
    //   359: invokevirtual getSystemWindowInsetRight : ()I
    //   362: aload #19
    //   364: invokevirtual getSystemWindowInsetBottom : ()I
    //   367: invokevirtual replaceSystemWindowInsets : (IIII)Landroid/view/WindowInsets;
    //   370: astore #17
    //   372: aload #20
    //   374: aload #17
    //   376: invokevirtual dispatchApplyWindowInsets : (Landroid/view/WindowInsets;)Landroid/view/WindowInsets;
    //   379: pop
    //   380: goto -> 515
    //   383: goto -> 515
    //   386: getstatic android/os/Build$VERSION.SDK_INT : I
    //   389: bipush #21
    //   391: if_icmplt -> 512
    //   394: aload #18
    //   396: getfield mLastInsets : Ljava/lang/Object;
    //   399: checkcast android/view/WindowInsets
    //   402: astore #19
    //   404: iload #13
    //   406: iconst_3
    //   407: if_icmpne -> 436
    //   410: aload #19
    //   412: aload #19
    //   414: invokevirtual getSystemWindowInsetLeft : ()I
    //   417: aload #19
    //   419: invokevirtual getSystemWindowInsetTop : ()I
    //   422: iconst_0
    //   423: aload #19
    //   425: invokevirtual getSystemWindowInsetBottom : ()I
    //   428: invokevirtual replaceSystemWindowInsets : (IIII)Landroid/view/WindowInsets;
    //   431: astore #17
    //   433: goto -> 469
    //   436: aload #19
    //   438: astore #17
    //   440: iload #13
    //   442: iconst_5
    //   443: if_icmpne -> 469
    //   446: aload #19
    //   448: iconst_0
    //   449: aload #19
    //   451: invokevirtual getSystemWindowInsetTop : ()I
    //   454: aload #19
    //   456: invokevirtual getSystemWindowInsetRight : ()I
    //   459: aload #19
    //   461: invokevirtual getSystemWindowInsetBottom : ()I
    //   464: invokevirtual replaceSystemWindowInsets : (IIII)Landroid/view/WindowInsets;
    //   467: astore #17
    //   469: aload #21
    //   471: aload #17
    //   473: invokevirtual getSystemWindowInsetLeft : ()I
    //   476: putfield leftMargin : I
    //   479: aload #21
    //   481: aload #17
    //   483: invokevirtual getSystemWindowInsetTop : ()I
    //   486: putfield topMargin : I
    //   489: aload #21
    //   491: aload #17
    //   493: invokevirtual getSystemWindowInsetRight : ()I
    //   496: putfield rightMargin : I
    //   499: aload #21
    //   501: aload #17
    //   503: invokevirtual getSystemWindowInsetBottom : ()I
    //   506: putfield bottomMargin : I
    //   509: goto -> 515
    //   512: goto -> 515
    //   515: aload #18
    //   517: aload #20
    //   519: invokevirtual isContentView : (Landroid/view/View;)Z
    //   522: ifeq -> 573
    //   525: aload #20
    //   527: iload #10
    //   529: aload #21
    //   531: getfield leftMargin : I
    //   534: isub
    //   535: aload #21
    //   537: getfield rightMargin : I
    //   540: isub
    //   541: ldc_w 1073741824
    //   544: invokestatic makeMeasureSpec : (II)I
    //   547: iload #11
    //   549: aload #21
    //   551: getfield topMargin : I
    //   554: isub
    //   555: aload #21
    //   557: getfield bottomMargin : I
    //   560: isub
    //   561: ldc_w 1073741824
    //   564: invokestatic makeMeasureSpec : (II)I
    //   567: invokevirtual measure : (II)V
    //   570: goto -> 808
    //   573: aload #18
    //   575: aload #20
    //   577: invokevirtual isDrawerView : (Landroid/view/View;)Z
    //   580: ifeq -> 817
    //   583: getstatic android/support/v4/widget/DrawerLayout.SET_DRAWER_SHADOW_FROM_ELEVATION : Z
    //   586: ifeq -> 616
    //   589: aload #20
    //   591: invokestatic getElevation : (Landroid/view/View;)F
    //   594: fstore_3
    //   595: aload #18
    //   597: getfield mDrawerElevation : F
    //   600: fstore #4
    //   602: fload_3
    //   603: fload #4
    //   605: fcmpl
    //   606: ifeq -> 616
    //   609: aload #20
    //   611: fload #4
    //   613: invokestatic setElevation : (Landroid/view/View;F)V
    //   616: aload #18
    //   618: aload #20
    //   620: invokevirtual getDrawerViewAbsoluteGravity : (Landroid/view/View;)I
    //   623: bipush #7
    //   625: iand
    //   626: istore #16
    //   628: iload #16
    //   630: iconst_3
    //   631: if_icmpne -> 640
    //   634: iconst_1
    //   635: istore #13
    //   637: goto -> 643
    //   640: iconst_0
    //   641: istore #13
    //   643: iload #13
    //   645: ifeq -> 653
    //   648: iload #8
    //   650: ifne -> 666
    //   653: iload #13
    //   655: ifne -> 743
    //   658: iload #6
    //   660: ifne -> 666
    //   663: goto -> 743
    //   666: new java/lang/StringBuilder
    //   669: dup
    //   670: invokespecial <init> : ()V
    //   673: astore #17
    //   675: aload #17
    //   677: ldc_w 'Child drawer has absolute gravity '
    //   680: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   683: pop
    //   684: aload #17
    //   686: iload #16
    //   688: invokestatic gravityToString : (I)Ljava/lang/String;
    //   691: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   694: pop
    //   695: aload #17
    //   697: ldc_w ' but this '
    //   700: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   703: pop
    //   704: aload #17
    //   706: ldc 'DrawerLayout'
    //   708: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   711: pop
    //   712: aload #17
    //   714: ldc_w ' already has a '
    //   717: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   720: pop
    //   721: aload #17
    //   723: ldc_w 'drawer view along that edge'
    //   726: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   729: pop
    //   730: new java/lang/IllegalStateException
    //   733: dup
    //   734: aload #17
    //   736: invokevirtual toString : ()Ljava/lang/String;
    //   739: invokespecial <init> : (Ljava/lang/String;)V
    //   742: athrow
    //   743: iload #13
    //   745: ifeq -> 754
    //   748: iconst_1
    //   749: istore #8
    //   751: goto -> 757
    //   754: iconst_1
    //   755: istore #6
    //   757: aload #20
    //   759: iload_1
    //   760: aload #18
    //   762: getfield mMinDrawerMargin : I
    //   765: aload #21
    //   767: getfield leftMargin : I
    //   770: iadd
    //   771: aload #21
    //   773: getfield rightMargin : I
    //   776: iadd
    //   777: aload #21
    //   779: getfield width : I
    //   782: invokestatic getChildMeasureSpec : (III)I
    //   785: iload_2
    //   786: aload #21
    //   788: getfield topMargin : I
    //   791: aload #21
    //   793: getfield bottomMargin : I
    //   796: iadd
    //   797: aload #21
    //   799: getfield height : I
    //   802: invokestatic getChildMeasureSpec : (III)I
    //   805: invokevirtual measure : (II)V
    //   808: iload #12
    //   810: iconst_1
    //   811: iadd
    //   812: istore #12
    //   814: goto -> 222
    //   817: new java/lang/StringBuilder
    //   820: dup
    //   821: invokespecial <init> : ()V
    //   824: astore #17
    //   826: aload #17
    //   828: ldc_w 'Child '
    //   831: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   834: pop
    //   835: aload #17
    //   837: aload #20
    //   839: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   842: pop
    //   843: aload #17
    //   845: ldc_w ' at index '
    //   848: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   851: pop
    //   852: aload #17
    //   854: iload #12
    //   856: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   859: pop
    //   860: aload #17
    //   862: ldc_w ' does not have a valid layout_gravity - must be Gravity.LEFT, '
    //   865: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   868: pop
    //   869: aload #17
    //   871: ldc_w 'Gravity.RIGHT or Gravity.NO_GRAVITY'
    //   874: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   877: pop
    //   878: new java/lang/IllegalStateException
    //   881: dup
    //   882: aload #17
    //   884: invokevirtual toString : ()Ljava/lang/String;
    //   887: invokespecial <init> : (Ljava/lang/String;)V
    //   890: athrow
    //   891: return
    //   892: new java/lang/IllegalArgumentException
    //   895: dup
    //   896: ldc_w 'DrawerLayout must be measured with MeasureSpec.EXACTLY.'
    //   899: invokespecial <init> : (Ljava/lang/String;)V
    //   902: athrow }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (savedState.openDrawerGravity != 0) {
      View view = findDrawerWithGravity(savedState.openDrawerGravity);
      if (view != null)
        openDrawer(view); 
    } 
    if (savedState.lockModeLeft != 3)
      setDrawerLockMode(savedState.lockModeLeft, 3); 
    if (savedState.lockModeRight != 3)
      setDrawerLockMode(savedState.lockModeRight, 5); 
    if (savedState.lockModeStart != 3)
      setDrawerLockMode(savedState.lockModeStart, 8388611); 
    if (savedState.lockModeEnd != 3)
      setDrawerLockMode(savedState.lockModeEnd, 8388613); 
  }
  
  public void onRtlPropertiesChanged(int paramInt) { resolveShadowDrawables(); }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      LayoutParams layoutParams = (LayoutParams)getChildAt(b).getLayoutParams();
      int j = layoutParams.openState;
      boolean bool = false;
      if (j == 1) {
        j = 1;
      } else {
        j = 0;
      } 
      if (layoutParams.openState == 2)
        bool = true; 
      if (j != 0 || bool) {
        savedState.openDrawerGravity = layoutParams.gravity;
        break;
      } 
    } 
    savedState.lockModeLeft = this.mLockModeLeft;
    savedState.lockModeRight = this.mLockModeRight;
    savedState.lockModeStart = this.mLockModeStart;
    savedState.lockModeEnd = this.mLockModeEnd;
    return savedState;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    this.mLeftDragger.processTouchEvent(paramMotionEvent);
    this.mRightDragger.processTouchEvent(paramMotionEvent);
    int i = paramMotionEvent.getAction() & 0xFF;
    byte b = 1;
    if (i != 3) {
      byte b1;
      int j;
      View view;
      switch (i) {
        default:
          return true;
        case 1:
          f2 = paramMotionEvent.getX();
          f1 = paramMotionEvent.getY();
          b1 = 1;
          view = this.mLeftDragger.findTopChildUnder((int)f2, (int)f1);
          j = b1;
          if (view != null) {
            j = b1;
            if (isContentView(view)) {
              f2 -= this.mInitialMotionX;
              f1 -= this.mInitialMotionY;
              i = this.mLeftDragger.getTouchSlop();
              j = b1;
              if (f2 * f2 + f1 * f1 < (i * i)) {
                view = findOpenDrawer();
                j = b1;
                if (view != null)
                  if (getDrawerLockMode(view) == 2) {
                    j = b;
                  } else {
                    j = 0;
                  }  
              } 
            } 
          } 
          closeDrawers(j);
          this.mDisallowInterceptRequested = false;
          return true;
        case 0:
          break;
      } 
      float f1 = view.getX();
      float f2 = view.getY();
      this.mInitialMotionX = f1;
      this.mInitialMotionY = f2;
      this.mDisallowInterceptRequested = false;
      this.mChildrenCanceledTouch = false;
      return true;
    } 
    closeDrawers(true);
    this.mDisallowInterceptRequested = false;
    this.mChildrenCanceledTouch = false;
    return true;
  }
  
  public void openDrawer(int paramInt) { openDrawer(paramInt, true); }
  
  public void openDrawer(int paramInt, boolean paramBoolean) {
    View view = findDrawerWithGravity(paramInt);
    if (view != null) {
      openDrawer(view, paramBoolean);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No drawer view found with gravity ");
    stringBuilder.append(gravityToString(paramInt));
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void openDrawer(@NonNull View paramView) { openDrawer(paramView, true); }
  
  public void openDrawer(@NonNull View paramView, boolean paramBoolean) {
    if (isDrawerView(paramView)) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      if (this.mFirstLayout) {
        layoutParams.onScreen = 1.0F;
        layoutParams.openState = 1;
        updateChildrenImportantForAccessibility(paramView, true);
      } else if (paramBoolean) {
        layoutParams.openState |= 0x2;
        if (checkDrawerViewAbsoluteGravity(paramView, 3)) {
          this.mLeftDragger.smoothSlideViewTo(paramView, 0, paramView.getTop());
        } else {
          this.mRightDragger.smoothSlideViewTo(paramView, getWidth() - paramView.getWidth(), paramView.getTop());
        } 
      } else {
        moveDrawerToOffset(paramView, 1.0F);
        updateDrawerState(layoutParams.gravity, 0, paramView);
        paramView.setVisibility(0);
      } 
      invalidate();
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("View ");
    stringBuilder.append(paramView);
    stringBuilder.append(" is not a sliding drawer");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void removeDrawerListener(@NonNull DrawerListener paramDrawerListener) {
    if (paramDrawerListener == null)
      return; 
    List list = this.mListeners;
    if (list == null)
      return; 
    list.remove(paramDrawerListener);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    super.requestDisallowInterceptTouchEvent(paramBoolean);
    this.mDisallowInterceptRequested = paramBoolean;
    if (paramBoolean)
      closeDrawers(true); 
  }
  
  public void requestLayout() {
    if (!this.mInLayout)
      super.requestLayout(); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setChildInsets(Object paramObject, boolean paramBoolean) {
    this.mLastInsets = paramObject;
    this.mDrawStatusBarBackground = paramBoolean;
    if (!paramBoolean && getBackground() == null) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    } 
    setWillNotDraw(paramBoolean);
    requestLayout();
  }
  
  public void setDrawerElevation(float paramFloat) {
    this.mDrawerElevation = paramFloat;
    for (byte b = 0; b < getChildCount(); b++) {
      View view = getChildAt(b);
      if (isDrawerView(view))
        ViewCompat.setElevation(view, this.mDrawerElevation); 
    } 
  }
  
  @Deprecated
  public void setDrawerListener(DrawerListener paramDrawerListener) {
    DrawerListener drawerListener = this.mListener;
    if (drawerListener != null)
      removeDrawerListener(drawerListener); 
    if (paramDrawerListener != null)
      addDrawerListener(paramDrawerListener); 
    this.mListener = paramDrawerListener;
  }
  
  public void setDrawerLockMode(int paramInt) {
    setDrawerLockMode(paramInt, 3);
    setDrawerLockMode(paramInt, 5);
  }
  
  public void setDrawerLockMode(int paramInt1, int paramInt2) {
    int i = GravityCompat.getAbsoluteGravity(paramInt2, ViewCompat.getLayoutDirection(this));
    if (paramInt2 != 3) {
      if (paramInt2 != 5) {
        if (paramInt2 != 8388611) {
          if (paramInt2 == 8388613)
            this.mLockModeEnd = paramInt1; 
        } else {
          this.mLockModeStart = paramInt1;
        } 
      } else {
        this.mLockModeRight = paramInt1;
      } 
    } else {
      this.mLockModeLeft = paramInt1;
    } 
    if (paramInt1 != 0) {
      ViewDragHelper viewDragHelper;
      if (i == 3) {
        viewDragHelper = this.mLeftDragger;
      } else {
        viewDragHelper = this.mRightDragger;
      } 
      viewDragHelper.cancel();
    } 
    switch (paramInt1) {
      default:
        return;
      case 2:
        view = findDrawerWithGravity(i);
        if (view != null) {
          openDrawer(view);
          return;
        } 
        return;
      case 1:
        break;
    } 
    View view = findDrawerWithGravity(i);
    if (view != null)
      closeDrawer(view); 
  }
  
  public void setDrawerLockMode(int paramInt, @NonNull View paramView) {
    if (isDrawerView(paramView)) {
      setDrawerLockMode(paramInt, ((LayoutParams)paramView.getLayoutParams()).gravity);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("View ");
    stringBuilder.append(paramView);
    stringBuilder.append(" is not a ");
    stringBuilder.append("drawer with appropriate layout_gravity");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setDrawerShadow(@DrawableRes int paramInt1, int paramInt2) { setDrawerShadow(ContextCompat.getDrawable(getContext(), paramInt1), paramInt2); }
  
  public void setDrawerShadow(Drawable paramDrawable, int paramInt) {
    if (SET_DRAWER_SHADOW_FROM_ELEVATION)
      return; 
    if ((paramInt & 0x800003) == 8388611) {
      this.mShadowStart = paramDrawable;
    } else if ((paramInt & 0x800005) == 8388613) {
      this.mShadowEnd = paramDrawable;
    } else if ((paramInt & 0x3) == 3) {
      this.mShadowLeft = paramDrawable;
    } else if ((paramInt & 0x5) == 5) {
      this.mShadowRight = paramDrawable;
    } else {
      return;
    } 
    resolveShadowDrawables();
    invalidate();
  }
  
  public void setDrawerTitle(int paramInt, @Nullable CharSequence paramCharSequence) {
    paramInt = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection(this));
    if (paramInt == 3) {
      this.mTitleLeft = paramCharSequence;
      return;
    } 
    if (paramInt == 5)
      this.mTitleRight = paramCharSequence; 
  }
  
  void setDrawerViewOffset(View paramView, float paramFloat) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (paramFloat == layoutParams.onScreen)
      return; 
    layoutParams.onScreen = paramFloat;
    dispatchOnDrawerSlide(paramView, paramFloat);
  }
  
  public void setScrimColor(@ColorInt int paramInt) {
    this.mScrimColor = paramInt;
    invalidate();
  }
  
  public void setStatusBarBackground(int paramInt) {
    Object object;
    if (paramInt != 0) {
      object = ContextCompat.getDrawable(getContext(), paramInt);
    } else {
      object = null;
    } 
    this.mStatusBarBackground = object;
    invalidate();
  }
  
  public void setStatusBarBackground(@Nullable Drawable paramDrawable) {
    this.mStatusBarBackground = paramDrawable;
    invalidate();
  }
  
  public void setStatusBarBackgroundColor(@ColorInt int paramInt) {
    this.mStatusBarBackground = new ColorDrawable(paramInt);
    invalidate();
  }
  
  void updateDrawerState(int paramInt1, int paramInt2, View paramView) {
    paramInt1 = this.mLeftDragger.getViewDragState();
    int i = this.mRightDragger.getViewDragState();
    if (paramInt1 == 1 || i == 1) {
      paramInt1 = 1;
    } else if (paramInt1 == 2 || i == 2) {
      paramInt1 = 2;
    } else {
      paramInt1 = 0;
    } 
    if (paramView != null && paramInt2 == 0) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      if (layoutParams.onScreen == 0.0F) {
        dispatchOnDrawerClosed(paramView);
      } else if (layoutParams.onScreen == 1.0F) {
        dispatchOnDrawerOpened(paramView);
      } 
    } 
    if (paramInt1 != this.mDrawerState) {
      this.mDrawerState = paramInt1;
      List list = this.mListeners;
      if (list != null)
        for (paramInt2 = list.size() - 1; paramInt2 >= 0; paramInt2--)
          ((DrawerListener)this.mListeners.get(paramInt2)).onDrawerStateChanged(paramInt1);  
    } 
  }
  
  class AccessibilityDelegate extends AccessibilityDelegateCompat {
    private final Rect mTmpRect = new Rect();
    
    private void addChildrenForAccessibility(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat, ViewGroup param1ViewGroup) {
      int i = param1ViewGroup.getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = param1ViewGroup.getChildAt(b);
        if (DrawerLayout.includeChildForAccessibility(view))
          param1AccessibilityNodeInfoCompat.addChild(view); 
      } 
    }
    
    private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat1, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat2) {
      Rect rect = this.mTmpRect;
      param1AccessibilityNodeInfoCompat2.getBoundsInParent(rect);
      param1AccessibilityNodeInfoCompat1.setBoundsInParent(rect);
      param1AccessibilityNodeInfoCompat2.getBoundsInScreen(rect);
      param1AccessibilityNodeInfoCompat1.setBoundsInScreen(rect);
      param1AccessibilityNodeInfoCompat1.setVisibleToUser(param1AccessibilityNodeInfoCompat2.isVisibleToUser());
      param1AccessibilityNodeInfoCompat1.setPackageName(param1AccessibilityNodeInfoCompat2.getPackageName());
      param1AccessibilityNodeInfoCompat1.setClassName(param1AccessibilityNodeInfoCompat2.getClassName());
      param1AccessibilityNodeInfoCompat1.setContentDescription(param1AccessibilityNodeInfoCompat2.getContentDescription());
      param1AccessibilityNodeInfoCompat1.setEnabled(param1AccessibilityNodeInfoCompat2.isEnabled());
      param1AccessibilityNodeInfoCompat1.setClickable(param1AccessibilityNodeInfoCompat2.isClickable());
      param1AccessibilityNodeInfoCompat1.setFocusable(param1AccessibilityNodeInfoCompat2.isFocusable());
      param1AccessibilityNodeInfoCompat1.setFocused(param1AccessibilityNodeInfoCompat2.isFocused());
      param1AccessibilityNodeInfoCompat1.setAccessibilityFocused(param1AccessibilityNodeInfoCompat2.isAccessibilityFocused());
      param1AccessibilityNodeInfoCompat1.setSelected(param1AccessibilityNodeInfoCompat2.isSelected());
      param1AccessibilityNodeInfoCompat1.setLongClickable(param1AccessibilityNodeInfoCompat2.isLongClickable());
      param1AccessibilityNodeInfoCompat1.addAction(param1AccessibilityNodeInfoCompat2.getActions());
    }
    
    public boolean dispatchPopulateAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      CharSequence charSequence;
      List list;
      if (param1AccessibilityEvent.getEventType() == 32) {
        list = param1AccessibilityEvent.getText();
        charSequence = DrawerLayout.this.findVisibleDrawer();
        if (charSequence != null) {
          int i = DrawerLayout.this.getDrawerViewAbsoluteGravity(charSequence);
          charSequence = DrawerLayout.this.getDrawerTitle(i);
          if (charSequence != null)
            list.add(charSequence); 
        } 
        return true;
      } 
      return super.dispatchPopulateAccessibilityEvent(list, charSequence);
    }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(DrawerLayout.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      if (DrawerLayout.CAN_HIDE_DESCENDANTS) {
        super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      } else {
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(param1AccessibilityNodeInfoCompat);
        super.onInitializeAccessibilityNodeInfo(param1View, accessibilityNodeInfoCompat);
        param1AccessibilityNodeInfoCompat.setSource(param1View);
        ViewParent viewParent = ViewCompat.getParentForAccessibility(param1View);
        if (viewParent instanceof View)
          param1AccessibilityNodeInfoCompat.setParent((View)viewParent); 
        copyNodeInfoNoChildren(param1AccessibilityNodeInfoCompat, accessibilityNodeInfoCompat);
        accessibilityNodeInfoCompat.recycle();
        addChildrenForAccessibility(param1AccessibilityNodeInfoCompat, (ViewGroup)param1View);
      } 
      param1AccessibilityNodeInfoCompat.setClassName(DrawerLayout.class.getName());
      param1AccessibilityNodeInfoCompat.setFocusable(false);
      param1AccessibilityNodeInfoCompat.setFocused(false);
      param1AccessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
      param1AccessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
    }
    
    public boolean onRequestSendAccessibilityEvent(ViewGroup param1ViewGroup, View param1View, AccessibilityEvent param1AccessibilityEvent) { return (DrawerLayout.CAN_HIDE_DESCENDANTS || DrawerLayout.includeChildForAccessibility(param1View)) ? super.onRequestSendAccessibilityEvent(param1ViewGroup, param1View, param1AccessibilityEvent) : 0; }
  }
  
  static final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat {
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      if (!DrawerLayout.includeChildForAccessibility(param1View))
        param1AccessibilityNodeInfoCompat.setParent(null); 
    }
  }
  
  public static interface DrawerListener {
    void onDrawerClosed(@NonNull View param1View);
    
    void onDrawerOpened(@NonNull View param1View);
    
    void onDrawerSlide(@NonNull View param1View, float param1Float);
    
    void onDrawerStateChanged(int param1Int);
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    private static final int FLAG_IS_CLOSING = 4;
    
    private static final int FLAG_IS_OPENED = 1;
    
    private static final int FLAG_IS_OPENING = 2;
    
    public int gravity = 0;
    
    boolean isPeeking;
    
    float onScreen;
    
    int openState;
    
    public LayoutParams(int param1Int1, int param1Int2) { super(param1Int1, param1Int2); }
    
    public LayoutParams(int param1Int1, int param1Int2, int param1Int3) {
      this(param1Int1, param1Int2);
      this.gravity = param1Int3;
    }
    
    public LayoutParams(@NonNull Context param1Context, @Nullable AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, DrawerLayout.LAYOUT_ATTRS);
      this.gravity = typedArray.getInt(0, 0);
      typedArray.recycle();
    }
    
    public LayoutParams(@NonNull LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.gravity = param1LayoutParams.gravity;
    }
    
    public LayoutParams(@NonNull ViewGroup.LayoutParams param1LayoutParams) { super(param1LayoutParams); }
    
    public LayoutParams(@NonNull ViewGroup.MarginLayoutParams param1MarginLayoutParams) { super(param1MarginLayoutParams); }
  }
  
  protected static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
        public DrawerLayout.SavedState createFromParcel(Parcel param2Parcel) { return new DrawerLayout.SavedState(param2Parcel, null); }
        
        public DrawerLayout.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) { return new DrawerLayout.SavedState(param2Parcel, param2ClassLoader); }
        
        public DrawerLayout.SavedState[] newArray(int param2Int) { return new DrawerLayout.SavedState[param2Int]; }
      };
    
    int lockModeEnd;
    
    int lockModeLeft;
    
    int lockModeRight;
    
    int lockModeStart;
    
    int openDrawerGravity = 0;
    
    public SavedState(@NonNull Parcel param1Parcel, @Nullable ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.openDrawerGravity = param1Parcel.readInt();
      this.lockModeLeft = param1Parcel.readInt();
      this.lockModeRight = param1Parcel.readInt();
      this.lockModeStart = param1Parcel.readInt();
      this.lockModeEnd = param1Parcel.readInt();
    }
    
    public SavedState(@NonNull Parcelable param1Parcelable) { super(param1Parcelable); }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.openDrawerGravity);
      param1Parcel.writeInt(this.lockModeLeft);
      param1Parcel.writeInt(this.lockModeRight);
      param1Parcel.writeInt(this.lockModeStart);
      param1Parcel.writeInt(this.lockModeEnd);
    }
  }
  
  static final class null extends Object implements Parcelable.ClassLoaderCreator<SavedState> {
    public DrawerLayout.SavedState createFromParcel(Parcel param1Parcel) { return new DrawerLayout.SavedState(param1Parcel, null); }
    
    public DrawerLayout.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) { return new DrawerLayout.SavedState(param1Parcel, param1ClassLoader); }
    
    public DrawerLayout.SavedState[] newArray(int param1Int) { return new DrawerLayout.SavedState[param1Int]; }
  }
  
  public static abstract class SimpleDrawerListener implements DrawerListener {
    public void onDrawerClosed(View param1View) {}
    
    public void onDrawerOpened(View param1View) {}
    
    public void onDrawerSlide(View param1View, float param1Float) {}
    
    public void onDrawerStateChanged(int param1Int) {}
  }
  
  private class ViewDragCallback extends ViewDragHelper.Callback {
    private final int mAbsGravity;
    
    private ViewDragHelper mDragger;
    
    private final Runnable mPeekRunnable = new Runnable() {
        public void run() { DrawerLayout.ViewDragCallback.this.peekDrawer(); }
      };
    
    ViewDragCallback(int param1Int) { this.mAbsGravity = param1Int; }
    
    private void closeOtherDrawer() {
      int i = this.mAbsGravity;
      byte b = 3;
      if (i == 3)
        b = 5; 
      View view = DrawerLayout.this.findDrawerWithGravity(b);
      if (view != null)
        DrawerLayout.this.closeDrawer(view); 
    }
    
    public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, 3))
        return Math.max(-param1View.getWidth(), Math.min(param1Int1, 0)); 
      param1Int2 = DrawerLayout.this.getWidth();
      return Math.max(param1Int2 - param1View.getWidth(), Math.min(param1Int1, param1Int2));
    }
    
    public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) { return param1View.getTop(); }
    
    public int getViewHorizontalDragRange(View param1View) { return DrawerLayout.this.isDrawerView(param1View) ? param1View.getWidth() : 0; }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) {
      View view;
      if ((param1Int1 & true) == 1) {
        view = DrawerLayout.this.findDrawerWithGravity(3);
      } else {
        view = DrawerLayout.this.findDrawerWithGravity(5);
      } 
      if (view != null && DrawerLayout.this.getDrawerLockMode(view) == 0)
        this.mDragger.captureChildView(view, param1Int2); 
    }
    
    public boolean onEdgeLock(int param1Int) { return false; }
    
    public void onEdgeTouched(int param1Int1, int param1Int2) { DrawerLayout.this.postDelayed(this.mPeekRunnable, 160L); }
    
    public void onViewCaptured(View param1View, int param1Int) {
      ((DrawerLayout.LayoutParams)param1View.getLayoutParams()).isPeeking = false;
      closeOtherDrawer();
    }
    
    public void onViewDragStateChanged(int param1Int) { DrawerLayout.this.updateDrawerState(this.mAbsGravity, param1Int, this.mDragger.getCapturedView()); }
    
    public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      float f;
      param1Int2 = param1View.getWidth();
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, 3)) {
        f = (param1Int2 + param1Int1) / param1Int2;
      } else {
        f = (DrawerLayout.this.getWidth() - param1Int1) / param1Int2;
      } 
      DrawerLayout.this.setDrawerViewOffset(param1View, f);
      if (f == 0.0F) {
        param1Int1 = 4;
      } else {
        param1Int1 = 0;
      } 
      param1View.setVisibility(param1Int1);
      DrawerLayout.this.invalidate();
    }
    
    public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
      int i;
      param1Float2 = DrawerLayout.this.getDrawerViewOffset(param1View);
      int j = param1View.getWidth();
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, 3)) {
        if (param1Float1 > 0.0F || (param1Float1 == 0.0F && param1Float2 > 0.5F)) {
          i = 0;
        } else {
          i = -j;
        } 
      } else {
        i = DrawerLayout.this.getWidth();
        if (param1Float1 < 0.0F || (param1Float1 == 0.0F && param1Float2 > 0.5F))
          i -= j; 
      } 
      this.mDragger.settleCapturedViewAt(i, param1View.getTop());
      DrawerLayout.this.invalidate();
    }
    
    void peekDrawer() {
      View view;
      int k = this.mDragger.getEdgeSize();
      int i = this.mAbsGravity;
      int j = 0;
      if (i == 3) {
        i = 1;
      } else {
        i = 0;
      } 
      if (i != 0) {
        view = DrawerLayout.this.findDrawerWithGravity(3);
        if (view != null)
          j = -view.getWidth(); 
        j += k;
      } else {
        view = DrawerLayout.this.findDrawerWithGravity(5);
        j = DrawerLayout.this.getWidth() - k;
      } 
      if (view != null && ((i != 0 && view.getLeft() < j) || (i == 0 && view.getLeft() > j)) && DrawerLayout.this.getDrawerLockMode(view) == 0) {
        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams)view.getLayoutParams();
        this.mDragger.smoothSlideViewTo(view, j, view.getTop());
        layoutParams.isPeeking = true;
        DrawerLayout.this.invalidate();
        closeOtherDrawer();
        DrawerLayout.this.cancelChildViewTouch();
      } 
    }
    
    public void removeCallbacks() { DrawerLayout.this.removeCallbacks(this.mPeekRunnable); }
    
    public void setDragger(ViewDragHelper param1ViewDragHelper) { this.mDragger = param1ViewDragHelper; }
    
    public boolean tryCaptureView(View param1View, int param1Int) { return (DrawerLayout.this.isDrawerView(param1View) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(param1View) == 0); }
  }
  
  class null implements Runnable {
    null() {}
    
    public void run() { this.this$1.peekDrawer(); }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\widget\DrawerLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */