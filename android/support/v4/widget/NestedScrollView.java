package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationUtils;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import java.util.ArrayList;

public class NestedScrollView extends FrameLayout implements NestedScrollingParent2, NestedScrollingChild2, ScrollingView {
  private static final AccessibilityDelegate ACCESSIBILITY_DELEGATE = new AccessibilityDelegate();
  
  static final int ANIMATED_SCROLL_GAP = 250;
  
  private static final int INVALID_POINTER = -1;
  
  static final float MAX_SCROLL_FACTOR = 0.5F;
  
  private static final int[] SCROLLVIEW_STYLEABLE = { 16843130 };
  
  private static final String TAG = "NestedScrollView";
  
  private int mActivePointerId = -1;
  
  private final NestedScrollingChildHelper mChildHelper;
  
  private View mChildToScrollTo = null;
  
  private EdgeEffect mEdgeGlowBottom;
  
  private EdgeEffect mEdgeGlowTop;
  
  private boolean mFillViewport;
  
  private boolean mIsBeingDragged = false;
  
  private boolean mIsLaidOut = false;
  
  private boolean mIsLayoutDirty = true;
  
  private int mLastMotionY;
  
  private long mLastScroll;
  
  private int mLastScrollerY;
  
  private int mMaximumVelocity;
  
  private int mMinimumVelocity;
  
  private int mNestedYOffset;
  
  private OnScrollChangeListener mOnScrollChangeListener;
  
  private final NestedScrollingParentHelper mParentHelper;
  
  private SavedState mSavedState;
  
  private final int[] mScrollConsumed = new int[2];
  
  private final int[] mScrollOffset = new int[2];
  
  private OverScroller mScroller;
  
  private boolean mSmoothScrollingEnabled = true;
  
  private final Rect mTempRect = new Rect();
  
  private int mTouchSlop;
  
  private VelocityTracker mVelocityTracker;
  
  private float mVerticalScrollFactor;
  
  public NestedScrollView(@NonNull Context paramContext) { this(paramContext, null); }
  
  public NestedScrollView(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, 0); }
  
  public NestedScrollView(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initScrollView();
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, SCROLLVIEW_STYLEABLE, paramInt, 0);
    setFillViewport(typedArray.getBoolean(0, false));
    typedArray.recycle();
    this.mParentHelper = new NestedScrollingParentHelper(this);
    this.mChildHelper = new NestedScrollingChildHelper(this);
    setNestedScrollingEnabled(true);
    ViewCompat.setAccessibilityDelegate(this, ACCESSIBILITY_DELEGATE);
  }
  
  private boolean canScroll() {
    int i = getChildCount();
    boolean bool = false;
    if (i > 0) {
      View view = getChildAt(0);
      FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
      if (view.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin > getHeight() - getPaddingTop() - getPaddingBottom())
        bool = true; 
      return bool;
    } 
    return false;
  }
  
  private static int clamp(int paramInt1, int paramInt2, int paramInt3) { return (paramInt2 >= paramInt3 || paramInt1 < 0) ? 0 : ((paramInt2 + paramInt1 > paramInt3) ? (paramInt3 - paramInt2) : paramInt1); }
  
  private void doScrollY(int paramInt) {
    if (paramInt != 0) {
      if (this.mSmoothScrollingEnabled) {
        smoothScrollBy(0, paramInt);
        return;
      } 
      scrollBy(0, paramInt);
    } 
  }
  
  private void endDrag() {
    this.mIsBeingDragged = false;
    recycleVelocityTracker();
    stopNestedScroll(0);
    EdgeEffect edgeEffect = this.mEdgeGlowTop;
    if (edgeEffect != null) {
      edgeEffect.onRelease();
      this.mEdgeGlowBottom.onRelease();
    } 
  }
  
  private void ensureGlows() {
    if (getOverScrollMode() != 2) {
      if (this.mEdgeGlowTop == null) {
        Context context = getContext();
        this.mEdgeGlowTop = new EdgeEffect(context);
        this.mEdgeGlowBottom = new EdgeEffect(context);
        return;
      } 
    } else {
      this.mEdgeGlowTop = null;
      this.mEdgeGlowBottom = null;
    } 
  }
  
  private View findFocusableViewInBounds(boolean paramBoolean, int paramInt1, int paramInt2) {
    ArrayList arrayList = getFocusables(2);
    View view = null;
    byte b2 = 0;
    int i = arrayList.size();
    byte b1 = 0;
    while (b1 < i) {
      View view2 = (View)arrayList.get(b1);
      int j = view2.getTop();
      int k = view2.getBottom();
      View view1 = view;
      byte b = b2;
      if (paramInt1 < k) {
        view1 = view;
        b = b2;
        if (j < paramInt2) {
          byte b3;
          boolean bool = false;
          if (paramInt1 < j && k < paramInt2) {
            b3 = 1;
          } else {
            b3 = 0;
          } 
          if (view == null) {
            view1 = view2;
            b = b3;
          } else {
            if ((paramBoolean && j < view.getTop()) || (!paramBoolean && k > view.getBottom()))
              bool = true; 
            if (b2 != 0) {
              view1 = view;
              b = b2;
              if (b3 != 0) {
                view1 = view;
                b = b2;
                if (bool) {
                  view1 = view2;
                  b = b2;
                } 
              } 
            } else if (b3 != 0) {
              view1 = view2;
              b = 1;
            } else {
              view1 = view;
              b = b2;
              if (bool) {
                view1 = view2;
                b = b2;
              } 
            } 
          } 
        } 
      } 
      b1++;
      view = view1;
      b2 = b;
    } 
    return view;
  }
  
  private void flingWithNestedDispatch(int paramInt) {
    boolean bool;
    int i = getScrollY();
    if ((i > 0 || paramInt > 0) && (i < getScrollRange() || paramInt < 0)) {
      bool = true;
    } else {
      bool = false;
    } 
    if (!dispatchNestedPreFling(0.0F, paramInt)) {
      dispatchNestedFling(0.0F, paramInt, bool);
      fling(paramInt);
    } 
  }
  
  private float getVerticalScrollFactorCompat() {
    if (this.mVerticalScrollFactor == 0.0F) {
      TypedValue typedValue = new TypedValue();
      Context context = getContext();
      if (context.getTheme().resolveAttribute(16842829, typedValue, true)) {
        this.mVerticalScrollFactor = typedValue.getDimension(context.getResources().getDisplayMetrics());
      } else {
        throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
      } 
    } 
    return this.mVerticalScrollFactor;
  }
  
  private boolean inChild(int paramInt1, int paramInt2) {
    if (getChildCount() > 0) {
      int i = getScrollY();
      View view = getChildAt(0);
      return (paramInt2 >= view.getTop() - i && paramInt2 < view.getBottom() - i && paramInt1 >= view.getLeft() && paramInt1 < view.getRight());
    } 
    return false;
  }
  
  private void initOrResetVelocityTracker() {
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker == null) {
      this.mVelocityTracker = VelocityTracker.obtain();
      return;
    } 
    velocityTracker.clear();
  }
  
  private void initScrollView() {
    this.mScroller = new OverScroller(getContext());
    setFocusable(true);
    setDescendantFocusability(262144);
    setWillNotDraw(false);
    ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
    this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
  }
  
  private void initVelocityTrackerIfNotExists() {
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
  }
  
  private boolean isOffScreen(View paramView) { return isWithinDeltaOfScreen(paramView, 0, getHeight()) ^ true; }
  
  private static boolean isViewDescendantOf(View paramView1, View paramView2) {
    if (paramView1 == paramView2)
      return true; 
    ViewParent viewParent = paramView1.getParent();
    return (viewParent instanceof ViewGroup && isViewDescendantOf((View)viewParent, paramView2));
  }
  
  private boolean isWithinDeltaOfScreen(View paramView, int paramInt1, int paramInt2) {
    paramView.getDrawingRect(this.mTempRect);
    offsetDescendantRectToMyCoords(paramView, this.mTempRect);
    return (this.mTempRect.bottom + paramInt1 >= getScrollY() && this.mTempRect.top - paramInt1 <= getScrollY() + paramInt2);
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionIndex();
    if (paramMotionEvent.getPointerId(i) == this.mActivePointerId) {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mLastMotionY = (int)paramMotionEvent.getY(i);
      this.mActivePointerId = paramMotionEvent.getPointerId(i);
      VelocityTracker velocityTracker = this.mVelocityTracker;
      if (velocityTracker != null)
        velocityTracker.clear(); 
    } 
  }
  
  private void recycleVelocityTracker() {
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  private boolean scrollAndFocus(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool;
    boolean bool1 = true;
    int j = getHeight();
    int i = getScrollY();
    j = i + j;
    if (paramInt1 == 33) {
      bool = true;
    } else {
      bool = false;
    } 
    View view = findFocusableViewInBounds(bool, paramInt2, paramInt3);
    NestedScrollView nestedScrollView = view;
    if (view == null)
      nestedScrollView = this; 
    if (paramInt2 >= i && paramInt3 <= j) {
      bool = false;
    } else {
      if (bool) {
        paramInt2 -= i;
      } else {
        paramInt2 = paramInt3 - j;
      } 
      doScrollY(paramInt2);
      bool = bool1;
    } 
    if (nestedScrollView != findFocus())
      nestedScrollView.requestFocus(paramInt1); 
    return bool;
  }
  
  private void scrollToChild(View paramView) {
    paramView.getDrawingRect(this.mTempRect);
    offsetDescendantRectToMyCoords(paramView, this.mTempRect);
    int i = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
    if (i != 0)
      scrollBy(0, i); 
  }
  
  private boolean scrollToChildRect(Rect paramRect, boolean paramBoolean) {
    boolean bool;
    int i = computeScrollDeltaToGetChildRectOnScreen(paramRect);
    if (i != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      if (paramBoolean) {
        scrollBy(0, i);
        return bool;
      } 
      smoothScrollBy(0, i);
    } 
    return bool;
  }
  
  public void addView(View paramView) {
    if (getChildCount() <= 0) {
      super.addView(paramView);
      return;
    } 
    throw new IllegalStateException("ScrollView can host only one direct child");
  }
  
  public void addView(View paramView, int paramInt) {
    if (getChildCount() <= 0) {
      super.addView(paramView, paramInt);
      return;
    } 
    throw new IllegalStateException("ScrollView can host only one direct child");
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    if (getChildCount() <= 0) {
      super.addView(paramView, paramInt, paramLayoutParams);
      return;
    } 
    throw new IllegalStateException("ScrollView can host only one direct child");
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    if (getChildCount() <= 0) {
      super.addView(paramView, paramLayoutParams);
      return;
    } 
    throw new IllegalStateException("ScrollView can host only one direct child");
  }
  
  public boolean arrowScroll(int paramInt) {
    View view2 = findFocus();
    View view1 = view2;
    if (view2 == this)
      view1 = null; 
    view2 = FocusFinder.getInstance().findNextFocus(this, view1, paramInt);
    int i = getMaxScrollAmount();
    if (view2 != null && isWithinDeltaOfScreen(view2, i, getHeight())) {
      view2.getDrawingRect(this.mTempRect);
      offsetDescendantRectToMyCoords(view2, this.mTempRect);
      doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
      view2.requestFocus(paramInt);
    } else {
      int j;
      int k = i;
      if (paramInt == 33 && getScrollY() < k) {
        j = getScrollY();
      } else {
        j = k;
        if (paramInt == 130) {
          j = k;
          if (getChildCount() > 0) {
            view2 = getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view2.getLayoutParams();
            j = Math.min(view2.getBottom() + layoutParams.bottomMargin - getScrollY() + getHeight() - getPaddingBottom(), i);
          } 
        } 
      } 
      if (j == 0)
        return false; 
      if (paramInt != 130)
        j = -j; 
      doScrollY(j);
    } 
    if (view1 != null && view1.isFocused() && isOffScreen(view1)) {
      paramInt = getDescendantFocusability();
      setDescendantFocusability(131072);
      requestFocus();
      setDescendantFocusability(paramInt);
    } 
    return true;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int computeHorizontalScrollExtent() { return super.computeHorizontalScrollExtent(); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int computeHorizontalScrollOffset() { return super.computeHorizontalScrollOffset(); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int computeHorizontalScrollRange() { return super.computeHorizontalScrollRange(); }
  
  public void computeScroll() {
    if (this.mScroller.computeScrollOffset()) {
      this.mScroller.getCurrX();
      int j = this.mScroller.getCurrY();
      int i = j - this.mLastScrollerY;
      if (dispatchNestedPreScroll(0, i, this.mScrollConsumed, null, 1))
        i -= this.mScrollConsumed[1]; 
      if (i != 0) {
        int k = getScrollRange();
        int m = getScrollY();
        overScrollByCompat(0, i, getScrollX(), m, 0, k, 0, 0, false);
        int n = getScrollY() - m;
        if (!dispatchNestedScroll(0, n, 0, i - n, null, 1)) {
          i = getOverScrollMode();
          if (i == 0 || (i == 1 && k > 0)) {
            i = 1;
          } else {
            i = 0;
          } 
          if (i != 0) {
            ensureGlows();
            if (j <= 0 && m > 0) {
              this.mEdgeGlowTop.onAbsorb((int)this.mScroller.getCurrVelocity());
            } else if (j >= k && m < k) {
              this.mEdgeGlowBottom.onAbsorb((int)this.mScroller.getCurrVelocity());
            } 
          } 
        } 
      } 
      this.mLastScrollerY = j;
      ViewCompat.postInvalidateOnAnimation(this);
      return;
    } 
    if (hasNestedScrollingParent(1))
      stopNestedScroll(1); 
    this.mLastScrollerY = 0;
  }
  
  protected int computeScrollDeltaToGetChildRectOnScreen(Rect paramRect) {
    if (getChildCount() == 0)
      return 0; 
    int i1 = getHeight();
    int i = getScrollY();
    int k = i + i1;
    int m = getVerticalFadingEdgeLength();
    int j = i;
    if (paramRect.top > 0)
      j = i + m; 
    View view = getChildAt(0);
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
    i = k;
    if (paramRect.bottom < view.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin)
      i = k - m; 
    m = i;
    int n = 0;
    if (paramRect.bottom > m && paramRect.top > j) {
      if (paramRect.height() > i1) {
        i = 0 + paramRect.top - j;
      } else {
        i = 0 + paramRect.bottom - m;
      } 
      return Math.min(i, view.getBottom() + layoutParams.bottomMargin - k);
    } 
    i = n;
    if (paramRect.top < j) {
      i = n;
      if (paramRect.bottom < m) {
        if (paramRect.height() > i1) {
          i = 0 - m - paramRect.bottom;
        } else {
          i = 0 - j - paramRect.top;
        } 
        return Math.max(i, -getScrollY());
      } 
    } 
    return i;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int computeVerticalScrollExtent() { return super.computeVerticalScrollExtent(); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int computeVerticalScrollOffset() { return Math.max(0, super.computeVerticalScrollOffset()); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int computeVerticalScrollRange() {
    int j = getChildCount();
    int i = getHeight() - getPaddingBottom() - getPaddingTop();
    if (j == 0)
      return i; 
    View view = getChildAt(0);
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
    j = view.getBottom() + layoutParams.bottomMargin;
    int k = getScrollY();
    int m = Math.max(0, j - i);
    if (k < 0)
      return j - k; 
    i = j;
    if (k > m)
      i = j + k - m; 
    return i;
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) { return (super.dispatchKeyEvent(paramKeyEvent) || executeKeyEvent(paramKeyEvent)); }
  
  public boolean dispatchNestedFling(float paramFloat1, float paramFloat2, boolean paramBoolean) { return this.mChildHelper.dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean); }
  
  public boolean dispatchNestedPreFling(float paramFloat1, float paramFloat2) { return this.mChildHelper.dispatchNestedPreFling(paramFloat1, paramFloat2); }
  
  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) { return dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2, 0); }
  
  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt3) { return this.mChildHelper.dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2, paramInt3); }
  
  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) { return dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, 0); }
  
  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5) { return this.mChildHelper.dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5); }
  
  public void draw(Canvas paramCanvas) { // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial draw : (Landroid/graphics/Canvas;)V
    //   5: aload_0
    //   6: getfield mEdgeGlowTop : Landroid/widget/EdgeEffect;
    //   9: ifnull -> 376
    //   12: aload_0
    //   13: invokevirtual getScrollY : ()I
    //   16: istore #8
    //   18: aload_0
    //   19: getfield mEdgeGlowTop : Landroid/widget/EdgeEffect;
    //   22: invokevirtual isFinished : ()Z
    //   25: ifne -> 188
    //   28: aload_1
    //   29: invokevirtual save : ()I
    //   32: istore #9
    //   34: aload_0
    //   35: invokevirtual getWidth : ()I
    //   38: istore #4
    //   40: aload_0
    //   41: invokevirtual getHeight : ()I
    //   44: istore #7
    //   46: iconst_0
    //   47: istore_3
    //   48: iconst_0
    //   49: iload #8
    //   51: invokestatic min : (II)I
    //   54: istore #6
    //   56: getstatic android/os/Build$VERSION.SDK_INT : I
    //   59: bipush #21
    //   61: if_icmplt -> 74
    //   64: iload #4
    //   66: istore_2
    //   67: aload_0
    //   68: invokevirtual getClipToPadding : ()Z
    //   71: ifeq -> 94
    //   74: iload #4
    //   76: aload_0
    //   77: invokevirtual getPaddingLeft : ()I
    //   80: aload_0
    //   81: invokevirtual getPaddingRight : ()I
    //   84: iadd
    //   85: isub
    //   86: istore_2
    //   87: iconst_0
    //   88: aload_0
    //   89: invokevirtual getPaddingLeft : ()I
    //   92: iadd
    //   93: istore_3
    //   94: iload #7
    //   96: istore #5
    //   98: iload #6
    //   100: istore #4
    //   102: getstatic android/os/Build$VERSION.SDK_INT : I
    //   105: bipush #21
    //   107: if_icmplt -> 148
    //   110: iload #7
    //   112: istore #5
    //   114: iload #6
    //   116: istore #4
    //   118: aload_0
    //   119: invokevirtual getClipToPadding : ()Z
    //   122: ifeq -> 148
    //   125: iload #7
    //   127: aload_0
    //   128: invokevirtual getPaddingTop : ()I
    //   131: aload_0
    //   132: invokevirtual getPaddingBottom : ()I
    //   135: iadd
    //   136: isub
    //   137: istore #5
    //   139: iload #6
    //   141: aload_0
    //   142: invokevirtual getPaddingTop : ()I
    //   145: iadd
    //   146: istore #4
    //   148: aload_1
    //   149: iload_3
    //   150: i2f
    //   151: iload #4
    //   153: i2f
    //   154: invokevirtual translate : (FF)V
    //   157: aload_0
    //   158: getfield mEdgeGlowTop : Landroid/widget/EdgeEffect;
    //   161: iload_2
    //   162: iload #5
    //   164: invokevirtual setSize : (II)V
    //   167: aload_0
    //   168: getfield mEdgeGlowTop : Landroid/widget/EdgeEffect;
    //   171: aload_1
    //   172: invokevirtual draw : (Landroid/graphics/Canvas;)Z
    //   175: ifeq -> 182
    //   178: aload_0
    //   179: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
    //   182: aload_1
    //   183: iload #9
    //   185: invokevirtual restoreToCount : (I)V
    //   188: aload_0
    //   189: getfield mEdgeGlowBottom : Landroid/widget/EdgeEffect;
    //   192: invokevirtual isFinished : ()Z
    //   195: ifne -> 376
    //   198: aload_1
    //   199: invokevirtual save : ()I
    //   202: istore #9
    //   204: aload_0
    //   205: invokevirtual getWidth : ()I
    //   208: istore #4
    //   210: aload_0
    //   211: invokevirtual getHeight : ()I
    //   214: istore #6
    //   216: iconst_0
    //   217: istore_3
    //   218: aload_0
    //   219: invokevirtual getScrollRange : ()I
    //   222: iload #8
    //   224: invokestatic max : (II)I
    //   227: iload #6
    //   229: iadd
    //   230: istore #7
    //   232: getstatic android/os/Build$VERSION.SDK_INT : I
    //   235: bipush #21
    //   237: if_icmplt -> 250
    //   240: iload #4
    //   242: istore_2
    //   243: aload_0
    //   244: invokevirtual getClipToPadding : ()Z
    //   247: ifeq -> 270
    //   250: iload #4
    //   252: aload_0
    //   253: invokevirtual getPaddingLeft : ()I
    //   256: aload_0
    //   257: invokevirtual getPaddingRight : ()I
    //   260: iadd
    //   261: isub
    //   262: istore_2
    //   263: iconst_0
    //   264: aload_0
    //   265: invokevirtual getPaddingLeft : ()I
    //   268: iadd
    //   269: istore_3
    //   270: iload #6
    //   272: istore #5
    //   274: iload #7
    //   276: istore #4
    //   278: getstatic android/os/Build$VERSION.SDK_INT : I
    //   281: bipush #21
    //   283: if_icmplt -> 324
    //   286: iload #6
    //   288: istore #5
    //   290: iload #7
    //   292: istore #4
    //   294: aload_0
    //   295: invokevirtual getClipToPadding : ()Z
    //   298: ifeq -> 324
    //   301: iload #6
    //   303: aload_0
    //   304: invokevirtual getPaddingTop : ()I
    //   307: aload_0
    //   308: invokevirtual getPaddingBottom : ()I
    //   311: iadd
    //   312: isub
    //   313: istore #5
    //   315: iload #7
    //   317: aload_0
    //   318: invokevirtual getPaddingBottom : ()I
    //   321: isub
    //   322: istore #4
    //   324: aload_1
    //   325: iload_3
    //   326: iload_2
    //   327: isub
    //   328: i2f
    //   329: iload #4
    //   331: i2f
    //   332: invokevirtual translate : (FF)V
    //   335: aload_1
    //   336: ldc_w 180.0
    //   339: iload_2
    //   340: i2f
    //   341: fconst_0
    //   342: invokevirtual rotate : (FFF)V
    //   345: aload_0
    //   346: getfield mEdgeGlowBottom : Landroid/widget/EdgeEffect;
    //   349: iload_2
    //   350: iload #5
    //   352: invokevirtual setSize : (II)V
    //   355: aload_0
    //   356: getfield mEdgeGlowBottom : Landroid/widget/EdgeEffect;
    //   359: aload_1
    //   360: invokevirtual draw : (Landroid/graphics/Canvas;)Z
    //   363: ifeq -> 370
    //   366: aload_0
    //   367: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
    //   370: aload_1
    //   371: iload #9
    //   373: invokevirtual restoreToCount : (I)V
    //   376: return }
  
  public boolean executeKeyEvent(@NonNull KeyEvent paramKeyEvent) {
    View view;
    this.mTempRect.setEmpty();
    boolean bool = canScroll();
    char c = '';
    if (!bool) {
      if (isFocused() && paramKeyEvent.getKeyCode() != 4) {
        View view1 = findFocus();
        view = view1;
        if (view1 == this)
          view = null; 
        view = FocusFinder.getInstance().findNextFocus(this, view, 130);
        return (view != null && view != this && view.requestFocus(130));
      } 
      return false;
    } 
    if (view.getAction() == 0) {
      int i = view.getKeyCode();
      if (i != 62) {
        switch (i) {
          default:
            return false;
          case 20:
            return !view.isAltPressed() ? arrowScroll(130) : fullScroll(130);
          case 19:
            break;
        } 
        return !view.isAltPressed() ? arrowScroll(33) : fullScroll(33);
      } 
      if (view.isShiftPressed())
        c = '!'; 
      pageScroll(c);
    } 
    return false;
  }
  
  public void fling(int paramInt) {
    if (getChildCount() > 0) {
      startNestedScroll(2, 1);
      this.mScroller.fling(getScrollX(), getScrollY(), 0, paramInt, 0, 0, -2147483648, 2147483647, 0, 0);
      this.mLastScrollerY = getScrollY();
      ViewCompat.postInvalidateOnAnimation(this);
    } 
  }
  
  public boolean fullScroll(int paramInt) {
    int i;
    if (paramInt == 130) {
      i = 1;
    } else {
      i = 0;
    } 
    int j = getHeight();
    Rect rect = this.mTempRect;
    rect.top = 0;
    rect.bottom = j;
    if (i) {
      i = getChildCount();
      if (i > 0) {
        View view = getChildAt(i - 1);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        this.mTempRect.bottom = view.getBottom() + layoutParams.bottomMargin + getPaddingBottom();
        Rect rect1 = this.mTempRect;
        rect1.top = rect1.bottom - j;
      } 
    } 
    return scrollAndFocus(paramInt, this.mTempRect.top, this.mTempRect.bottom);
  }
  
  protected float getBottomFadingEdgeStrength() {
    if (getChildCount() == 0)
      return 0.0F; 
    View view = getChildAt(0);
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
    int i = getVerticalFadingEdgeLength();
    int j = getHeight();
    int k = getPaddingBottom();
    j = view.getBottom() + layoutParams.bottomMargin - getScrollY() - j - k;
    return (j < i) ? (j / i) : 1.0F;
  }
  
  public int getMaxScrollAmount() { return (int)(getHeight() * 0.5F); }
  
  public int getNestedScrollAxes() { return this.mParentHelper.getNestedScrollAxes(); }
  
  int getScrollRange() {
    int i = 0;
    if (getChildCount() > 0) {
      View view = getChildAt(0);
      FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
      i = Math.max(0, view.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin - getHeight() - getPaddingTop() - getPaddingBottom());
    } 
    return i;
  }
  
  protected float getTopFadingEdgeStrength() {
    if (getChildCount() == 0)
      return 0.0F; 
    int i = getVerticalFadingEdgeLength();
    int j = getScrollY();
    return (j < i) ? (j / i) : 1.0F;
  }
  
  public boolean hasNestedScrollingParent() { return hasNestedScrollingParent(0); }
  
  public boolean hasNestedScrollingParent(int paramInt) { return this.mChildHelper.hasNestedScrollingParent(paramInt); }
  
  public boolean isFillViewport() { return this.mFillViewport; }
  
  public boolean isNestedScrollingEnabled() { return this.mChildHelper.isNestedScrollingEnabled(); }
  
  public boolean isSmoothScrollingEnabled() { return this.mSmoothScrollingEnabled; }
  
  protected void measureChild(View paramView, int paramInt1, int paramInt2) {
    ViewGroup.LayoutParams layoutParams = paramView.getLayoutParams();
    paramView.measure(getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight(), layoutParams.width), View.MeasureSpec.makeMeasureSpec(0, 0));
  }
  
  protected void measureChildWithMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    paramView.measure(getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + paramInt2, marginLayoutParams.width), View.MeasureSpec.makeMeasureSpec(marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, 0));
  }
  
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mIsLaidOut = false;
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent) {
    if ((paramMotionEvent.getSource() & 0x2) != 0 && paramMotionEvent.getAction() == 8 && !this.mIsBeingDragged) {
      float f = paramMotionEvent.getAxisValue(9);
      if (f != 0.0F) {
        int i = (int)(getVerticalScrollFactorCompat() * f);
        int j = getScrollRange();
        int m = getScrollY();
        int k = m - i;
        if (k < 0) {
          i = 0;
        } else {
          i = k;
          if (k > j)
            i = j; 
        } 
        if (i != m) {
          super.scrollTo(getScrollX(), i);
          return true;
        } 
      } 
    } 
    return false;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    ViewParent viewParent;
    int i = paramMotionEvent.getAction();
    if (i == 2 && this.mIsBeingDragged)
      return true; 
    i &= 0xFF;
    if (i != 6) {
      switch (i) {
        default:
          return this.mIsBeingDragged;
        case 2:
          i = this.mActivePointerId;
          if (i != -1) {
            int j = paramMotionEvent.findPointerIndex(i);
            if (j == -1) {
              viewParent = new StringBuilder();
              viewParent.append("Invalid pointerId=");
              viewParent.append(i);
              viewParent.append(" in onInterceptTouchEvent");
              Log.e("NestedScrollView", viewParent.toString());
            } else {
              i = (int)viewParent.getY(j);
              if (Math.abs(i - this.mLastMotionY) > this.mTouchSlop && (0x2 & getNestedScrollAxes()) == 0) {
                this.mIsBeingDragged = true;
                this.mLastMotionY = i;
                initVelocityTrackerIfNotExists();
                this.mVelocityTracker.addMovement(viewParent);
                this.mNestedYOffset = 0;
                viewParent = getParent();
                if (viewParent != null)
                  viewParent.requestDisallowInterceptTouchEvent(true); 
              } 
            } 
          } 
        case 1:
        case 3:
          this.mIsBeingDragged = false;
          this.mActivePointerId = -1;
          recycleVelocityTracker();
          if (this.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()))
            ViewCompat.postInvalidateOnAnimation(this); 
          stopNestedScroll(0);
        case 0:
          break;
      } 
      i = (int)viewParent.getY();
      if (!inChild((int)viewParent.getX(), i)) {
        this.mIsBeingDragged = false;
        recycleVelocityTracker();
      } 
      this.mLastMotionY = i;
      this.mActivePointerId = viewParent.getPointerId(0);
      initOrResetVelocityTracker();
      this.mVelocityTracker.addMovement(viewParent);
      this.mScroller.computeScrollOffset();
      this.mIsBeingDragged = true ^ this.mScroller.isFinished();
      startNestedScroll(2, 0);
    } 
    onSecondaryPointerUp(viewParent);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mIsLayoutDirty = false;
    View view = this.mChildToScrollTo;
    if (view != null && isViewDescendantOf(view, this))
      scrollToChild(this.mChildToScrollTo); 
    this.mChildToScrollTo = null;
    if (!this.mIsLaidOut) {
      if (this.mSavedState != null) {
        scrollTo(getScrollX(), this.mSavedState.scrollPosition);
        this.mSavedState = null;
      } 
      paramInt1 = 0;
      if (getChildCount() > 0) {
        view = getChildAt(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        paramInt1 = view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
      } 
      int i = getPaddingTop();
      int j = getPaddingBottom();
      paramInt3 = getScrollY();
      paramInt1 = clamp(paramInt3, paramInt4 - paramInt2 - i - j, paramInt1);
      if (paramInt1 != paramInt3)
        scrollTo(getScrollX(), paramInt1); 
    } 
    scrollTo(getScrollX(), getScrollY());
    this.mIsLaidOut = true;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    if (!this.mFillViewport)
      return; 
    if (View.MeasureSpec.getMode(paramInt2) == 0)
      return; 
    if (getChildCount() > 0) {
      View view = getChildAt(0);
      FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
      paramInt2 = view.getMeasuredHeight();
      int i = getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - layoutParams.topMargin - layoutParams.bottomMargin;
      if (paramInt2 < i)
        view.measure(getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width), View.MeasureSpec.makeMeasureSpec(i, 1073741824)); 
    } 
  }
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    if (!paramBoolean) {
      flingWithNestedDispatch((int)paramFloat2);
      return true;
    } 
    return false;
  }
  
  public boolean onNestedPreFling(View paramView, float paramFloat1, float paramFloat2) { return dispatchNestedPreFling(paramFloat1, paramFloat2); }
  
  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfInt) { onNestedPreScroll(paramView, paramInt1, paramInt2, paramArrayOfInt, 0); }
  
  public void onNestedPreScroll(@NonNull View paramView, int paramInt1, int paramInt2, @NonNull int[] paramArrayOfInt, int paramInt3) { dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfInt, null, paramInt3); }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) { onNestedScroll(paramView, paramInt1, paramInt2, paramInt3, paramInt4, 0); }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    paramInt1 = getScrollY();
    scrollBy(0, paramInt4);
    paramInt1 = getScrollY() - paramInt1;
    dispatchNestedScroll(0, paramInt1, 0, paramInt4 - paramInt1, null, paramInt5);
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt) { onNestedScrollAccepted(paramView1, paramView2, paramInt, 0); }
  
  public void onNestedScrollAccepted(@NonNull View paramView1, @NonNull View paramView2, int paramInt1, int paramInt2) {
    this.mParentHelper.onNestedScrollAccepted(paramView1, paramView2, paramInt1, paramInt2);
    startNestedScroll(2, paramInt2);
  }
  
  protected void onOverScrolled(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2) { super.scrollTo(paramInt1, paramInt2); }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect) {
    View view;
    int i;
    if (paramInt == 2) {
      i = 130;
    } else {
      i = paramInt;
      if (paramInt == 1)
        i = 33; 
    } 
    if (paramRect == null) {
      view = FocusFinder.getInstance().findNextFocus(this, null, i);
    } else {
      view = FocusFinder.getInstance().findNextFocusFromRect(this, paramRect, i);
    } 
    return (view == null) ? false : (isOffScreen(view) ? false : view.requestFocus(i, paramRect));
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.mSavedState = savedState;
    requestLayout();
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.scrollPosition = getScrollY();
    return savedState;
  }
  
  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    OnScrollChangeListener onScrollChangeListener = this.mOnScrollChangeListener;
    if (onScrollChangeListener != null)
      onScrollChangeListener.onScrollChange(this, paramInt1, paramInt2, paramInt3, paramInt4); 
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    View view = findFocus();
    if (view != null) {
      if (this == view)
        return; 
      if (isWithinDeltaOfScreen(view, 0, paramInt4)) {
        view.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(view, this.mTempRect);
        doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
      } 
      return;
    } 
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt) { return onStartNestedScroll(paramView1, paramView2, paramInt, 0); }
  
  public boolean onStartNestedScroll(@NonNull View paramView1, @NonNull View paramView2, int paramInt1, int paramInt2) { return ((paramInt1 & 0x2) != 0); }
  
  public void onStopNestedScroll(View paramView) { onStopNestedScroll(paramView, 0); }
  
  public void onStopNestedScroll(@NonNull View paramView, int paramInt) {
    this.mParentHelper.onStopNestedScroll(paramView, paramInt);
    stopNestedScroll(paramInt);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    boolean bool;
    int m;
    int k;
    int j;
    StringBuilder stringBuilder;
    initVelocityTrackerIfNotExists();
    MotionEvent motionEvent = MotionEvent.obtain(paramMotionEvent);
    int i = paramMotionEvent.getActionMasked();
    if (i == 0)
      this.mNestedYOffset = 0; 
    motionEvent.offsetLocation(0.0F, this.mNestedYOffset);
    switch (i) {
      case 6:
        onSecondaryPointerUp(paramMotionEvent);
        this.mLastMotionY = (int)paramMotionEvent.getY(paramMotionEvent.findPointerIndex(this.mActivePointerId));
        break;
      case 5:
        i = paramMotionEvent.getActionIndex();
        this.mLastMotionY = (int)paramMotionEvent.getY(i);
        this.mActivePointerId = paramMotionEvent.getPointerId(i);
        break;
      case 3:
        if (this.mIsBeingDragged && getChildCount() > 0 && this.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()))
          ViewCompat.postInvalidateOnAnimation(this); 
        this.mActivePointerId = -1;
        endDrag();
        break;
      case 2:
        k = paramMotionEvent.findPointerIndex(this.mActivePointerId);
        if (k == -1) {
          stringBuilder = new StringBuilder();
          stringBuilder.append("Invalid pointerId=");
          stringBuilder.append(this.mActivePointerId);
          stringBuilder.append(" in onTouchEvent");
          Log.e("NestedScrollView", stringBuilder.toString());
          break;
        } 
        m = (int)stringBuilder.getY(k);
        i = this.mLastMotionY - m;
        j = i;
        if (dispatchNestedPreScroll(0, i, this.mScrollConsumed, this.mScrollOffset, 0)) {
          j = i - this.mScrollConsumed[1];
          motionEvent.offsetLocation(0.0F, this.mScrollOffset[1]);
          this.mNestedYOffset += this.mScrollOffset[1];
        } 
        i = j;
        if (!this.mIsBeingDragged) {
          i = j;
          if (Math.abs(j) > this.mTouchSlop) {
            ViewParent viewParent = getParent();
            if (viewParent != null)
              viewParent.requestDisallowInterceptTouchEvent(true); 
            this.mIsBeingDragged = true;
            if (j > 0) {
              i = j - this.mTouchSlop;
            } else {
              i = j + this.mTouchSlop;
            } 
          } 
        } 
        if (this.mIsBeingDragged) {
          int[] arrayOfInt;
          this.mLastMotionY = m - this.mScrollOffset[1];
          int n = getScrollY();
          m = getScrollRange();
          j = getOverScrollMode();
          if (j == 0 || (j == 1 && m > 0)) {
            j = 1;
          } else {
            j = 0;
          } 
          if (overScrollByCompat(0, i, 0, getScrollY(), 0, m, 0, 0, true) && !hasNestedScrollingParent(0))
            this.mVelocityTracker.clear(); 
          int i1 = getScrollY() - n;
          if (dispatchNestedScroll(0, i1, 0, i - i1, this.mScrollOffset, 0)) {
            i = this.mLastMotionY;
            arrayOfInt = this.mScrollOffset;
            this.mLastMotionY = i - arrayOfInt[1];
            motionEvent.offsetLocation(0.0F, arrayOfInt[1]);
            this.mNestedYOffset += this.mScrollOffset[1];
            break;
          } 
          if (j != 0) {
            ensureGlows();
            j = n + i;
            if (j < 0) {
              EdgeEffectCompat.onPull(this.mEdgeGlowTop, i / getHeight(), arrayOfInt.getX(k) / getWidth());
              if (!this.mEdgeGlowBottom.isFinished())
                this.mEdgeGlowBottom.onRelease(); 
            } else if (j > m) {
              EdgeEffectCompat.onPull(this.mEdgeGlowBottom, i / getHeight(), 1.0F - arrayOfInt.getX(k) / getWidth());
              if (!this.mEdgeGlowTop.isFinished())
                this.mEdgeGlowTop.onRelease(); 
            } 
            EdgeEffect edgeEffect = this.mEdgeGlowTop;
            if (edgeEffect != null && (!edgeEffect.isFinished() || !this.mEdgeGlowBottom.isFinished()))
              ViewCompat.postInvalidateOnAnimation(this); 
          } 
        } 
        break;
      case 1:
        velocityTracker = this.mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        i = (int)velocityTracker.getYVelocity(this.mActivePointerId);
        if (Math.abs(i) > this.mMinimumVelocity) {
          flingWithNestedDispatch(-i);
        } else if (this.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
          ViewCompat.postInvalidateOnAnimation(this);
        } 
        this.mActivePointerId = -1;
        endDrag();
        break;
      case 0:
        if (getChildCount() == 0)
          return false; 
        bool = this.mScroller.isFinished() ^ true;
        this.mIsBeingDragged = bool;
        if (bool) {
          ViewParent viewParent = getParent();
          if (viewParent != null)
            viewParent.requestDisallowInterceptTouchEvent(true); 
        } 
        if (!this.mScroller.isFinished())
          this.mScroller.abortAnimation(); 
        this.mLastMotionY = (int)velocityTracker.getY();
        this.mActivePointerId = velocityTracker.getPointerId(0);
        startNestedScroll(2, 0);
        break;
    } 
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null)
      velocityTracker.addMovement(motionEvent); 
    motionEvent.recycle();
    return true;
  }
  
  boolean overScrollByCompat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean) {
    boolean bool;
    boolean bool2;
    boolean bool1;
    int i = getOverScrollMode();
    if (computeHorizontalScrollRange() > computeHorizontalScrollExtent()) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (computeVerticalScrollRange() > computeVerticalScrollExtent()) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (i == 0 || (i == 1 && bool1)) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (i == 0 || (i == 1 && bool2)) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    paramInt3 += paramInt1;
    if (!bool1) {
      paramInt1 = 0;
    } else {
      paramInt1 = paramInt7;
    } 
    paramInt4 += paramInt2;
    if (!bool2) {
      paramInt2 = 0;
    } else {
      paramInt2 = paramInt8;
    } 
    paramInt7 = -paramInt1;
    paramInt1 += paramInt5;
    paramInt5 = -paramInt2;
    paramInt2 += paramInt6;
    if (paramInt3 > paramInt1) {
      paramBoolean = true;
    } else if (paramInt3 < paramInt7) {
      paramInt1 = paramInt7;
      paramBoolean = true;
    } else {
      paramBoolean = false;
      paramInt1 = paramInt3;
    } 
    if (paramInt4 > paramInt2) {
      bool = true;
    } else if (paramInt4 < paramInt5) {
      paramInt2 = paramInt5;
      bool = true;
    } else {
      paramInt2 = paramInt4;
      bool = false;
    } 
    if (bool && !hasNestedScrollingParent(1))
      this.mScroller.springBack(paramInt1, paramInt2, 0, 0, 0, getScrollRange()); 
    onOverScrolled(paramInt1, paramInt2, paramBoolean, bool);
    return (paramBoolean || bool);
  }
  
  public boolean pageScroll(int paramInt) {
    int i;
    if (paramInt == 130) {
      i = 1;
    } else {
      i = 0;
    } 
    int j = getHeight();
    if (i) {
      this.mTempRect.top = getScrollY() + j;
      i = getChildCount();
      if (i > 0) {
        View view = getChildAt(i - 1);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        i = view.getBottom() + layoutParams.bottomMargin + getPaddingBottom();
        if (this.mTempRect.top + j > i)
          this.mTempRect.top = i - j; 
      } 
    } else {
      this.mTempRect.top = getScrollY() - j;
      if (this.mTempRect.top < 0)
        this.mTempRect.top = 0; 
    } 
    Rect rect = this.mTempRect;
    rect.bottom = rect.top + j;
    return scrollAndFocus(paramInt, this.mTempRect.top, this.mTempRect.bottom);
  }
  
  public void requestChildFocus(View paramView1, View paramView2) {
    if (!this.mIsLayoutDirty) {
      scrollToChild(paramView2);
    } else {
      this.mChildToScrollTo = paramView2;
    } 
    super.requestChildFocus(paramView1, paramView2);
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean) {
    paramRect.offset(paramView.getLeft() - paramView.getScrollX(), paramView.getTop() - paramView.getScrollY());
    return scrollToChildRect(paramRect, paramBoolean);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    if (paramBoolean)
      recycleVelocityTracker(); 
    super.requestDisallowInterceptTouchEvent(paramBoolean);
  }
  
  public void requestLayout() {
    this.mIsLayoutDirty = true;
    super.requestLayout();
  }
  
  public void scrollTo(int paramInt1, int paramInt2) {
    if (getChildCount() > 0) {
      View view = getChildAt(0);
      FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
      int i2 = getWidth();
      int i3 = getPaddingLeft();
      int i4 = getPaddingRight();
      int i5 = view.getWidth();
      int i6 = layoutParams.leftMargin;
      int i7 = layoutParams.rightMargin;
      int i = getHeight();
      int j = getPaddingTop();
      int k = getPaddingBottom();
      int m = view.getHeight();
      int n = layoutParams.topMargin;
      int i1 = layoutParams.bottomMargin;
      paramInt1 = clamp(paramInt1, i2 - i3 - i4, i5 + i6 + i7);
      paramInt2 = clamp(paramInt2, i - j - k, m + n + i1);
      if (paramInt1 != getScrollX() || paramInt2 != getScrollY())
        super.scrollTo(paramInt1, paramInt2); 
    } 
  }
  
  public void setFillViewport(boolean paramBoolean) {
    if (paramBoolean != this.mFillViewport) {
      this.mFillViewport = paramBoolean;
      requestLayout();
    } 
  }
  
  public void setNestedScrollingEnabled(boolean paramBoolean) { this.mChildHelper.setNestedScrollingEnabled(paramBoolean); }
  
  public void setOnScrollChangeListener(@Nullable OnScrollChangeListener paramOnScrollChangeListener) { this.mOnScrollChangeListener = paramOnScrollChangeListener; }
  
  public void setSmoothScrollingEnabled(boolean paramBoolean) { this.mSmoothScrollingEnabled = paramBoolean; }
  
  public boolean shouldDelayChildPressedState() { return true; }
  
  public final void smoothScrollBy(int paramInt1, int paramInt2) {
    if (getChildCount() == 0)
      return; 
    if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250L) {
      View view = getChildAt(0);
      FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
      int i = view.getHeight();
      int j = layoutParams.topMargin;
      int k = layoutParams.bottomMargin;
      int m = getHeight();
      int n = getPaddingTop();
      int i1 = getPaddingBottom();
      paramInt1 = getScrollY();
      paramInt2 = Math.max(0, Math.min(paramInt1 + paramInt2, Math.max(0, i + j + k - m - n - i1)));
      this.mLastScrollerY = getScrollY();
      this.mScroller.startScroll(getScrollX(), paramInt1, 0, paramInt2 - paramInt1);
      ViewCompat.postInvalidateOnAnimation(this);
    } else {
      if (!this.mScroller.isFinished())
        this.mScroller.abortAnimation(); 
      scrollBy(paramInt1, paramInt2);
    } 
    this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
  }
  
  public final void smoothScrollTo(int paramInt1, int paramInt2) { smoothScrollBy(paramInt1 - getScrollX(), paramInt2 - getScrollY()); }
  
  public boolean startNestedScroll(int paramInt) { return startNestedScroll(paramInt, 0); }
  
  public boolean startNestedScroll(int paramInt1, int paramInt2) { return this.mChildHelper.startNestedScroll(paramInt1, paramInt2); }
  
  public void stopNestedScroll() { stopNestedScroll(0); }
  
  public void stopNestedScroll(int paramInt) { this.mChildHelper.stopNestedScroll(paramInt); }
  
  static class AccessibilityDelegate extends AccessibilityDelegateCompat {
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      boolean bool;
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      NestedScrollView nestedScrollView = (NestedScrollView)param1View;
      param1AccessibilityEvent.setClassName(android.widget.ScrollView.class.getName());
      if (nestedScrollView.getScrollRange() > 0) {
        bool = true;
      } else {
        bool = false;
      } 
      param1AccessibilityEvent.setScrollable(bool);
      param1AccessibilityEvent.setScrollX(nestedScrollView.getScrollX());
      param1AccessibilityEvent.setScrollY(nestedScrollView.getScrollY());
      AccessibilityRecordCompat.setMaxScrollX(param1AccessibilityEvent, nestedScrollView.getScrollX());
      AccessibilityRecordCompat.setMaxScrollY(param1AccessibilityEvent, nestedScrollView.getScrollRange());
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      NestedScrollView nestedScrollView = (NestedScrollView)param1View;
      param1AccessibilityNodeInfoCompat.setClassName(android.widget.ScrollView.class.getName());
      if (nestedScrollView.isEnabled()) {
        int i = nestedScrollView.getScrollRange();
        if (i > 0) {
          param1AccessibilityNodeInfoCompat.setScrollable(true);
          if (nestedScrollView.getScrollY() > 0)
            param1AccessibilityNodeInfoCompat.addAction(8192); 
          if (nestedScrollView.getScrollY() < i)
            param1AccessibilityNodeInfoCompat.addAction(4096); 
        } 
      } 
    }
    
    public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
      if (super.performAccessibilityAction(param1View, param1Int, param1Bundle))
        return true; 
      NestedScrollView nestedScrollView = (NestedScrollView)param1View;
      if (!nestedScrollView.isEnabled())
        return false; 
      if (param1Int != 4096) {
        if (param1Int != 8192)
          return false; 
        param1Int = nestedScrollView.getHeight();
        int k = nestedScrollView.getPaddingBottom();
        int m = nestedScrollView.getPaddingTop();
        param1Int = Math.max(nestedScrollView.getScrollY() - param1Int - k - m, 0);
        if (param1Int != nestedScrollView.getScrollY()) {
          nestedScrollView.smoothScrollTo(0, param1Int);
          return true;
        } 
        return false;
      } 
      param1Int = nestedScrollView.getHeight();
      int i = nestedScrollView.getPaddingBottom();
      int j = nestedScrollView.getPaddingTop();
      param1Int = Math.min(nestedScrollView.getScrollY() + param1Int - i - j, nestedScrollView.getScrollRange());
      if (param1Int != nestedScrollView.getScrollY()) {
        nestedScrollView.smoothScrollTo(0, param1Int);
        return true;
      } 
      return false;
    }
  }
  
  public static interface OnScrollChangeListener {
    void onScrollChange(NestedScrollView param1NestedScrollView, int param1Int1, int param1Int2, int param1Int3, int param1Int4);
  }
  
  static class SavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public NestedScrollView.SavedState createFromParcel(Parcel param2Parcel) { return new NestedScrollView.SavedState(param2Parcel); }
        
        public NestedScrollView.SavedState[] newArray(int param2Int) { return new NestedScrollView.SavedState[param2Int]; }
      };
    
    public int scrollPosition;
    
    SavedState(Parcel param1Parcel) {
      super(param1Parcel);
      this.scrollPosition = param1Parcel.readInt();
    }
    
    SavedState(Parcelable param1Parcelable) { super(param1Parcelable); }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("HorizontalScrollView.SavedState{");
      stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      stringBuilder.append(" scrollPosition=");
      stringBuilder.append(this.scrollPosition);
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.scrollPosition);
    }
  }
  
  static final class null extends Object implements Parcelable.Creator<SavedState> {
    public NestedScrollView.SavedState createFromParcel(Parcel param1Parcel) { return new NestedScrollView.SavedState(param1Parcel); }
    
    public NestedScrollView.SavedState[] newArray(int param1Int) { return new NestedScrollView.SavedState[param1Int]; }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\widget\NestedScrollView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */