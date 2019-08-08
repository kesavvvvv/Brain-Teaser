package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup {
  private static final int DEFAULT_FADE_COLOR = -858993460;
  
  private static final int DEFAULT_OVERHANG_SIZE = 32;
  
  private static final int MIN_FLING_VELOCITY = 400;
  
  private static final String TAG = "SlidingPaneLayout";
  
  private boolean mCanSlide;
  
  private int mCoveredFadeColor;
  
  private boolean mDisplayListReflectionLoaded;
  
  final ViewDragHelper mDragHelper;
  
  private boolean mFirstLayout = true;
  
  private Method mGetDisplayList;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  boolean mIsUnableToDrag;
  
  private final int mOverhangSize;
  
  private PanelSlideListener mPanelSlideListener;
  
  private int mParallaxBy;
  
  private float mParallaxOffset;
  
  final ArrayList<DisableLayerRunnable> mPostedRunnables = new ArrayList();
  
  boolean mPreservedOpenState;
  
  private Field mRecreateDisplayList;
  
  private Drawable mShadowDrawableLeft;
  
  private Drawable mShadowDrawableRight;
  
  float mSlideOffset;
  
  int mSlideRange;
  
  View mSlideableView;
  
  private int mSliderFadeColor = -858993460;
  
  private final Rect mTmpRect = new Rect();
  
  public SlidingPaneLayout(@NonNull Context paramContext) { this(paramContext, null); }
  
  public SlidingPaneLayout(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, 0); }
  
  public SlidingPaneLayout(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    float f = (paramContext.getResources().getDisplayMetrics()).density;
    this.mOverhangSize = (int)(32.0F * f + 0.5F);
    setWillNotDraw(false);
    ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
    ViewCompat.setImportantForAccessibility(this, 1);
    this.mDragHelper = ViewDragHelper.create(this, 0.5F, new DragHelperCallback());
    this.mDragHelper.setMinVelocity(400.0F * f);
  }
  
  private boolean closePane(View paramView, int paramInt) {
    if (this.mFirstLayout || smoothSlideTo(0.0F, paramInt)) {
      this.mPreservedOpenState = false;
      return true;
    } 
    return false;
  }
  
  private void dimChildView(View paramView, float paramFloat, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (paramFloat > 0.0F && paramInt != 0) {
      int i = (int)(((0xFF000000 & paramInt) >>> 24) * paramFloat);
      if (layoutParams.dimPaint == null)
        layoutParams.dimPaint = new Paint(); 
      layoutParams.dimPaint.setColorFilter(new PorterDuffColorFilter(i << 24 | 0xFFFFFF & paramInt, PorterDuff.Mode.SRC_OVER));
      if (paramView.getLayerType() != 2)
        paramView.setLayerType(2, layoutParams.dimPaint); 
      invalidateChildRegion(paramView);
    } else if (paramView.getLayerType() != 0) {
      if (layoutParams.dimPaint != null)
        layoutParams.dimPaint.setColorFilter(null); 
      DisableLayerRunnable disableLayerRunnable = new DisableLayerRunnable(paramView);
      this.mPostedRunnables.add(disableLayerRunnable);
      ViewCompat.postOnAnimation(this, disableLayerRunnable);
      return;
    } 
  }
  
  private boolean openPane(View paramView, int paramInt) {
    if (this.mFirstLayout || smoothSlideTo(1.0F, paramInt)) {
      this.mPreservedOpenState = true;
      return true;
    } 
    return false;
  }
  
  private void parallaxOtherViews(float paramFloat) { // Byte code:
    //   0: aload_0
    //   1: invokevirtual isLayoutRtlSupport : ()Z
    //   4: istore #8
    //   6: aload_0
    //   7: getfield mSlideableView : Landroid/view/View;
    //   10: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   13: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
    //   16: astore #9
    //   18: aload #9
    //   20: getfield dimWhenOffset : Z
    //   23: ifeq -> 55
    //   26: iload #8
    //   28: ifeq -> 40
    //   31: aload #9
    //   33: getfield rightMargin : I
    //   36: istore_3
    //   37: goto -> 46
    //   40: aload #9
    //   42: getfield leftMargin : I
    //   45: istore_3
    //   46: iload_3
    //   47: ifgt -> 55
    //   50: iconst_1
    //   51: istore_3
    //   52: goto -> 57
    //   55: iconst_0
    //   56: istore_3
    //   57: aload_0
    //   58: invokevirtual getChildCount : ()I
    //   61: istore #6
    //   63: iconst_0
    //   64: istore #4
    //   66: iload #4
    //   68: iload #6
    //   70: if_icmpge -> 198
    //   73: aload_0
    //   74: iload #4
    //   76: invokevirtual getChildAt : (I)Landroid/view/View;
    //   79: astore #9
    //   81: aload #9
    //   83: aload_0
    //   84: getfield mSlideableView : Landroid/view/View;
    //   87: if_acmpne -> 93
    //   90: goto -> 189
    //   93: aload_0
    //   94: getfield mParallaxOffset : F
    //   97: fstore_2
    //   98: aload_0
    //   99: getfield mParallaxBy : I
    //   102: istore #5
    //   104: fconst_1
    //   105: fload_2
    //   106: fsub
    //   107: iload #5
    //   109: i2f
    //   110: fmul
    //   111: f2i
    //   112: istore #7
    //   114: aload_0
    //   115: fload_1
    //   116: putfield mParallaxOffset : F
    //   119: iload #7
    //   121: fconst_1
    //   122: fload_1
    //   123: fsub
    //   124: iload #5
    //   126: i2f
    //   127: fmul
    //   128: f2i
    //   129: isub
    //   130: istore #5
    //   132: iload #8
    //   134: ifeq -> 145
    //   137: iload #5
    //   139: ineg
    //   140: istore #5
    //   142: goto -> 145
    //   145: aload #9
    //   147: iload #5
    //   149: invokevirtual offsetLeftAndRight : (I)V
    //   152: iload_3
    //   153: ifeq -> 189
    //   156: iload #8
    //   158: ifeq -> 171
    //   161: aload_0
    //   162: getfield mParallaxOffset : F
    //   165: fconst_1
    //   166: fsub
    //   167: fstore_2
    //   168: goto -> 178
    //   171: fconst_1
    //   172: aload_0
    //   173: getfield mParallaxOffset : F
    //   176: fsub
    //   177: fstore_2
    //   178: aload_0
    //   179: aload #9
    //   181: fload_2
    //   182: aload_0
    //   183: getfield mCoveredFadeColor : I
    //   186: invokespecial dimChildView : (Landroid/view/View;FI)V
    //   189: iload #4
    //   191: iconst_1
    //   192: iadd
    //   193: istore #4
    //   195: goto -> 66
    //   198: return }
  
  private static boolean viewIsOpaque(View paramView) {
    if (paramView.isOpaque())
      return true; 
    if (Build.VERSION.SDK_INT >= 18)
      return false; 
    Drawable drawable = paramView.getBackground();
    return (drawable != null) ? ((drawable.getOpacity() == -1)) : false;
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3) {
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int j = paramView.getScrollX();
      int k = paramView.getScrollY();
      int i;
      for (i = viewGroup.getChildCount() - 1; i >= 0; i--) {
        View view = viewGroup.getChildAt(i);
        if (paramInt2 + j >= view.getLeft() && paramInt2 + j < view.getRight() && paramInt3 + k >= view.getTop() && paramInt3 + k < view.getBottom() && canScroll(view, true, paramInt1, paramInt2 + j - view.getLeft(), paramInt3 + k - view.getTop()))
          return true; 
      } 
    } 
    if (paramBoolean) {
      if (!isLayoutRtlSupport())
        paramInt1 = -paramInt1; 
      if (paramView.canScrollHorizontally(paramInt1))
        return true; 
    } 
    return false;
  }
  
  @Deprecated
  public boolean canSlide() { return this.mCanSlide; }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams)); }
  
  public boolean closePane() { return closePane(this.mSlideableView, 0); }
  
  public void computeScroll() {
    if (this.mDragHelper.continueSettling(true)) {
      if (!this.mCanSlide) {
        this.mDragHelper.abort();
        return;
      } 
      ViewCompat.postInvalidateOnAnimation(this);
    } 
  }
  
  void dispatchOnPanelClosed(View paramView) {
    PanelSlideListener panelSlideListener = this.mPanelSlideListener;
    if (panelSlideListener != null)
      panelSlideListener.onPanelClosed(paramView); 
    sendAccessibilityEvent(32);
  }
  
  void dispatchOnPanelOpened(View paramView) {
    PanelSlideListener panelSlideListener = this.mPanelSlideListener;
    if (panelSlideListener != null)
      panelSlideListener.onPanelOpened(paramView); 
    sendAccessibilityEvent(32);
  }
  
  void dispatchOnPanelSlide(View paramView) {
    PanelSlideListener panelSlideListener = this.mPanelSlideListener;
    if (panelSlideListener != null)
      panelSlideListener.onPanelSlide(paramView, this.mSlideOffset); 
  }
  
  public void draw(Canvas paramCanvas) {
    Object object;
    Drawable drawable;
    super.draw(paramCanvas);
    if (isLayoutRtlSupport()) {
      drawable = this.mShadowDrawableRight;
    } else {
      drawable = this.mShadowDrawableLeft;
    } 
    if (getChildCount() > 1) {
      object = getChildAt(1);
    } else {
      object = null;
    } 
    if (object != null) {
      int j;
      int i;
      if (drawable == null)
        return; 
      int k = object.getTop();
      int m = object.getBottom();
      int n = drawable.getIntrinsicWidth();
      if (isLayoutRtlSupport()) {
        i = object.getRight();
        j = i + n;
      } else {
        j = object.getLeft();
        i = j - n;
      } 
      drawable.setBounds(i, k, j, m);
      drawable.draw(paramCanvas);
      return;
    } 
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = paramCanvas.save();
    if (this.mCanSlide && !layoutParams.slideable && this.mSlideableView != null) {
      paramCanvas.getClipBounds(this.mTmpRect);
      if (isLayoutRtlSupport()) {
        Rect rect = this.mTmpRect;
        rect.left = Math.max(rect.left, this.mSlideableView.getRight());
      } else {
        Rect rect = this.mTmpRect;
        rect.right = Math.min(rect.right, this.mSlideableView.getLeft());
      } 
      paramCanvas.clipRect(this.mTmpRect);
    } 
    boolean bool = super.drawChild(paramCanvas, paramView, paramLong);
    paramCanvas.restoreToCount(i);
    return bool;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() { return new LayoutParams(); }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) { return new LayoutParams(getContext(), paramAttributeSet); }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return (paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams); }
  
  @ColorInt
  public int getCoveredFadeColor() { return this.mCoveredFadeColor; }
  
  @Px
  public int getParallaxDistance() { return this.mParallaxBy; }
  
  @ColorInt
  public int getSliderFadeColor() { return this.mSliderFadeColor; }
  
  void invalidateChildRegion(View paramView) { // Byte code:
    //   0: getstatic android/os/Build$VERSION.SDK_INT : I
    //   3: bipush #17
    //   5: if_icmplt -> 23
    //   8: aload_1
    //   9: aload_1
    //   10: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   13: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
    //   16: getfield dimPaint : Landroid/graphics/Paint;
    //   19: invokestatic setLayerPaint : (Landroid/view/View;Landroid/graphics/Paint;)V
    //   22: return
    //   23: getstatic android/os/Build$VERSION.SDK_INT : I
    //   26: bipush #16
    //   28: if_icmplt -> 167
    //   31: aload_0
    //   32: getfield mDisplayListReflectionLoaded : Z
    //   35: ifne -> 107
    //   38: aload_0
    //   39: ldc android/view/View
    //   41: ldc_w 'getDisplayList'
    //   44: aconst_null
    //   45: checkcast [Ljava/lang/Class;
    //   48: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   51: putfield mGetDisplayList : Ljava/lang/reflect/Method;
    //   54: goto -> 68
    //   57: astore_2
    //   58: ldc 'SlidingPaneLayout'
    //   60: ldc_w 'Couldn't fetch getDisplayList method; dimming won't work right.'
    //   63: aload_2
    //   64: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   67: pop
    //   68: aload_0
    //   69: ldc android/view/View
    //   71: ldc_w 'mRecreateDisplayList'
    //   74: invokevirtual getDeclaredField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   77: putfield mRecreateDisplayList : Ljava/lang/reflect/Field;
    //   80: aload_0
    //   81: getfield mRecreateDisplayList : Ljava/lang/reflect/Field;
    //   84: iconst_1
    //   85: invokevirtual setAccessible : (Z)V
    //   88: goto -> 102
    //   91: astore_2
    //   92: ldc 'SlidingPaneLayout'
    //   94: ldc_w 'Couldn't fetch mRecreateDisplayList field; dimming will be slow.'
    //   97: aload_2
    //   98: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   101: pop
    //   102: aload_0
    //   103: iconst_1
    //   104: putfield mDisplayListReflectionLoaded : Z
    //   107: aload_0
    //   108: getfield mGetDisplayList : Ljava/lang/reflect/Method;
    //   111: ifnull -> 162
    //   114: aload_0
    //   115: getfield mRecreateDisplayList : Ljava/lang/reflect/Field;
    //   118: astore_2
    //   119: aload_2
    //   120: ifnonnull -> 126
    //   123: goto -> 162
    //   126: aload_2
    //   127: aload_1
    //   128: iconst_1
    //   129: invokevirtual setBoolean : (Ljava/lang/Object;Z)V
    //   132: aload_0
    //   133: getfield mGetDisplayList : Ljava/lang/reflect/Method;
    //   136: aload_1
    //   137: aconst_null
    //   138: checkcast [Ljava/lang/Object;
    //   141: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   144: pop
    //   145: goto -> 167
    //   148: astore_2
    //   149: ldc 'SlidingPaneLayout'
    //   151: ldc_w 'Error refreshing display list state'
    //   154: aload_2
    //   155: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   158: pop
    //   159: goto -> 167
    //   162: aload_1
    //   163: invokevirtual invalidate : ()V
    //   166: return
    //   167: aload_0
    //   168: aload_1
    //   169: invokevirtual getLeft : ()I
    //   172: aload_1
    //   173: invokevirtual getTop : ()I
    //   176: aload_1
    //   177: invokevirtual getRight : ()I
    //   180: aload_1
    //   181: invokevirtual getBottom : ()I
    //   184: invokestatic postInvalidateOnAnimation : (Landroid/view/View;IIII)V
    //   187: return
    // Exception table:
    //   from	to	target	type
    //   38	54	57	java/lang/NoSuchMethodException
    //   68	88	91	java/lang/NoSuchFieldException
    //   126	145	148	java/lang/Exception }
  
  boolean isDimmed(View paramView) {
    byte b = 0;
    if (paramView == null)
      return false; 
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = b;
    if (this.mCanSlide) {
      i = b;
      if (layoutParams.dimWhenOffset) {
        i = b;
        if (this.mSlideOffset > 0.0F)
          i = 1; 
      } 
    } 
    return i;
  }
  
  boolean isLayoutRtlSupport() { return (ViewCompat.getLayoutDirection(this) == 1); }
  
  public boolean isOpen() { return (!this.mCanSlide || this.mSlideOffset == 1.0F); }
  
  public boolean isSlideable() { return this.mCanSlide; }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    this.mFirstLayout = true;
    byte b = 0;
    int i = this.mPostedRunnables.size();
    while (b < i) {
      ((DisableLayerRunnable)this.mPostedRunnables.get(b)).run();
      b++;
    } 
    this.mPostedRunnables.clear();
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionMasked();
    boolean bool2 = this.mCanSlide;
    boolean bool1 = true;
    if (!bool2 && i == 0 && getChildCount() > 1) {
      View view = getChildAt(1);
      if (view != null)
        this.mPreservedOpenState = this.mDragHelper.isViewUnder(view, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY()) ^ true; 
    } 
    if (!this.mCanSlide || (this.mIsUnableToDrag && i != 0)) {
      this.mDragHelper.cancel();
      return super.onInterceptTouchEvent(paramMotionEvent);
    } 
    if (i == 3 || i == 1) {
      this.mDragHelper.cancel();
      return false;
    } 
    int j = 0;
    if (i != 0) {
      if (i != 2) {
        i = j;
      } else {
        float f2 = paramMotionEvent.getX();
        float f1 = paramMotionEvent.getY();
        f2 = Math.abs(f2 - this.mInitialMotionX);
        f1 = Math.abs(f1 - this.mInitialMotionY);
        i = j;
        if (f2 > this.mDragHelper.getTouchSlop()) {
          i = j;
          if (f1 > f2) {
            this.mDragHelper.cancel();
            this.mIsUnableToDrag = true;
            return false;
          } 
        } 
      } 
    } else {
      this.mIsUnableToDrag = false;
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      this.mInitialMotionX = f1;
      this.mInitialMotionY = f2;
      i = j;
      if (this.mDragHelper.isViewUnder(this.mSlideableView, (int)f1, (int)f2)) {
        i = j;
        if (isDimmed(this.mSlideableView))
          i = 1; 
      } 
    } 
    if (!this.mDragHelper.shouldInterceptTouchEvent(paramMotionEvent)) {
      if (i != 0)
        return true; 
      bool1 = false;
    } 
    return bool1;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) { // Byte code:
    //   0: aload_0
    //   1: invokevirtual isLayoutRtlSupport : ()Z
    //   4: istore #15
    //   6: iload #15
    //   8: ifeq -> 22
    //   11: aload_0
    //   12: getfield mDragHelper : Landroid/support/v4/widget/ViewDragHelper;
    //   15: iconst_2
    //   16: invokevirtual setEdgeTrackingEnabled : (I)V
    //   19: goto -> 30
    //   22: aload_0
    //   23: getfield mDragHelper : Landroid/support/v4/widget/ViewDragHelper;
    //   26: iconst_1
    //   27: invokevirtual setEdgeTrackingEnabled : (I)V
    //   30: iload #4
    //   32: iload_2
    //   33: isub
    //   34: istore #10
    //   36: iload #15
    //   38: ifeq -> 49
    //   41: aload_0
    //   42: invokevirtual getPaddingRight : ()I
    //   45: istore_3
    //   46: goto -> 54
    //   49: aload_0
    //   50: invokevirtual getPaddingLeft : ()I
    //   53: istore_3
    //   54: iload #15
    //   56: ifeq -> 68
    //   59: aload_0
    //   60: invokevirtual getPaddingLeft : ()I
    //   63: istore #5
    //   65: goto -> 74
    //   68: aload_0
    //   69: invokevirtual getPaddingRight : ()I
    //   72: istore #5
    //   74: aload_0
    //   75: invokevirtual getPaddingTop : ()I
    //   78: istore #12
    //   80: aload_0
    //   81: invokevirtual getChildCount : ()I
    //   84: istore #11
    //   86: iload_3
    //   87: istore #4
    //   89: iload #4
    //   91: istore_2
    //   92: aload_0
    //   93: getfield mFirstLayout : Z
    //   96: ifeq -> 128
    //   99: aload_0
    //   100: getfield mCanSlide : Z
    //   103: ifeq -> 119
    //   106: aload_0
    //   107: getfield mPreservedOpenState : Z
    //   110: ifeq -> 119
    //   113: fconst_1
    //   114: fstore #6
    //   116: goto -> 122
    //   119: fconst_0
    //   120: fstore #6
    //   122: aload_0
    //   123: fload #6
    //   125: putfield mSlideOffset : F
    //   128: iconst_0
    //   129: istore #7
    //   131: iload #4
    //   133: istore #8
    //   135: iload_3
    //   136: istore #4
    //   138: iload #7
    //   140: iload #11
    //   142: if_icmpge -> 447
    //   145: aload_0
    //   146: iload #7
    //   148: invokevirtual getChildAt : (I)Landroid/view/View;
    //   151: astore #16
    //   153: aload #16
    //   155: invokevirtual getVisibility : ()I
    //   158: bipush #8
    //   160: if_icmpne -> 169
    //   163: iload #8
    //   165: istore_3
    //   166: goto -> 435
    //   169: aload #16
    //   171: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   174: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
    //   177: astore #17
    //   179: aload #16
    //   181: invokevirtual getMeasuredWidth : ()I
    //   184: istore #13
    //   186: iconst_0
    //   187: istore #9
    //   189: aload #17
    //   191: getfield slideable : Z
    //   194: ifeq -> 332
    //   197: aload #17
    //   199: getfield leftMargin : I
    //   202: istore_3
    //   203: aload #17
    //   205: getfield rightMargin : I
    //   208: istore #14
    //   210: iload_2
    //   211: iload #10
    //   213: iload #5
    //   215: isub
    //   216: aload_0
    //   217: getfield mOverhangSize : I
    //   220: isub
    //   221: invokestatic min : (II)I
    //   224: iload #8
    //   226: isub
    //   227: iload_3
    //   228: iload #14
    //   230: iadd
    //   231: isub
    //   232: istore #14
    //   234: aload_0
    //   235: iload #14
    //   237: putfield mSlideRange : I
    //   240: iload #15
    //   242: ifeq -> 254
    //   245: aload #17
    //   247: getfield rightMargin : I
    //   250: istore_3
    //   251: goto -> 260
    //   254: aload #17
    //   256: getfield leftMargin : I
    //   259: istore_3
    //   260: iload #8
    //   262: iload_3
    //   263: iadd
    //   264: iload #14
    //   266: iadd
    //   267: iload #13
    //   269: iconst_2
    //   270: idiv
    //   271: iadd
    //   272: iload #10
    //   274: iload #5
    //   276: isub
    //   277: if_icmple -> 285
    //   280: iconst_1
    //   281: istore_1
    //   282: goto -> 287
    //   285: iconst_0
    //   286: istore_1
    //   287: aload #17
    //   289: iload_1
    //   290: putfield dimWhenOffset : Z
    //   293: iload #14
    //   295: i2f
    //   296: aload_0
    //   297: getfield mSlideOffset : F
    //   300: fmul
    //   301: f2i
    //   302: istore #14
    //   304: iload #8
    //   306: iload #14
    //   308: iload_3
    //   309: iadd
    //   310: iadd
    //   311: istore_3
    //   312: aload_0
    //   313: iload #14
    //   315: i2f
    //   316: aload_0
    //   317: getfield mSlideRange : I
    //   320: i2f
    //   321: fdiv
    //   322: putfield mSlideOffset : F
    //   325: iload #9
    //   327: istore #8
    //   329: goto -> 371
    //   332: aload_0
    //   333: getfield mCanSlide : Z
    //   336: ifeq -> 365
    //   339: aload_0
    //   340: getfield mParallaxBy : I
    //   343: istore_3
    //   344: iload_3
    //   345: ifeq -> 365
    //   348: fconst_1
    //   349: aload_0
    //   350: getfield mSlideOffset : F
    //   353: fsub
    //   354: iload_3
    //   355: i2f
    //   356: fmul
    //   357: f2i
    //   358: istore #8
    //   360: iload_2
    //   361: istore_3
    //   362: goto -> 371
    //   365: iload_2
    //   366: istore_3
    //   367: iload #9
    //   369: istore #8
    //   371: iload #15
    //   373: ifeq -> 395
    //   376: iload #10
    //   378: iload_3
    //   379: isub
    //   380: iload #8
    //   382: iadd
    //   383: istore #8
    //   385: iload #8
    //   387: iload #13
    //   389: isub
    //   390: istore #9
    //   392: goto -> 408
    //   395: iload_3
    //   396: iload #8
    //   398: isub
    //   399: istore #9
    //   401: iload #9
    //   403: iload #13
    //   405: iadd
    //   406: istore #8
    //   408: aload #16
    //   410: iload #9
    //   412: iload #12
    //   414: iload #8
    //   416: aload #16
    //   418: invokevirtual getMeasuredHeight : ()I
    //   421: iload #12
    //   423: iadd
    //   424: invokevirtual layout : (IIII)V
    //   427: iload_2
    //   428: aload #16
    //   430: invokevirtual getWidth : ()I
    //   433: iadd
    //   434: istore_2
    //   435: iload #7
    //   437: iconst_1
    //   438: iadd
    //   439: istore #7
    //   441: iload_3
    //   442: istore #8
    //   444: goto -> 138
    //   447: aload_0
    //   448: getfield mFirstLayout : Z
    //   451: ifeq -> 548
    //   454: aload_0
    //   455: getfield mCanSlide : Z
    //   458: ifeq -> 511
    //   461: aload_0
    //   462: getfield mParallaxBy : I
    //   465: ifeq -> 476
    //   468: aload_0
    //   469: aload_0
    //   470: getfield mSlideOffset : F
    //   473: invokespecial parallaxOtherViews : (F)V
    //   476: aload_0
    //   477: getfield mSlideableView : Landroid/view/View;
    //   480: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   483: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
    //   486: getfield dimWhenOffset : Z
    //   489: ifeq -> 540
    //   492: aload_0
    //   493: aload_0
    //   494: getfield mSlideableView : Landroid/view/View;
    //   497: aload_0
    //   498: getfield mSlideOffset : F
    //   501: aload_0
    //   502: getfield mSliderFadeColor : I
    //   505: invokespecial dimChildView : (Landroid/view/View;FI)V
    //   508: goto -> 540
    //   511: iconst_0
    //   512: istore_2
    //   513: iload_2
    //   514: iload #11
    //   516: if_icmpge -> 540
    //   519: aload_0
    //   520: aload_0
    //   521: iload_2
    //   522: invokevirtual getChildAt : (I)Landroid/view/View;
    //   525: fconst_0
    //   526: aload_0
    //   527: getfield mSliderFadeColor : I
    //   530: invokespecial dimChildView : (Landroid/view/View;FI)V
    //   533: iload_2
    //   534: iconst_1
    //   535: iadd
    //   536: istore_2
    //   537: goto -> 513
    //   540: aload_0
    //   541: aload_0
    //   542: getfield mSlideableView : Landroid/view/View;
    //   545: invokevirtual updateObscuredViewsVisibility : (Landroid/view/View;)V
    //   548: aload_0
    //   549: iconst_0
    //   550: putfield mFirstLayout : Z
    //   553: return }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int m;
    int k;
    int j;
    int i = View.MeasureSpec.getMode(paramInt1);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    int n = View.MeasureSpec.getMode(paramInt2);
    int i1 = View.MeasureSpec.getSize(paramInt2);
    if (i != 1073741824) {
      if (isInEditMode()) {
        if (i == Integer.MIN_VALUE) {
          paramInt2 = 1073741824;
          m = paramInt1;
          j = n;
          k = i1;
        } else {
          paramInt2 = i;
          m = paramInt1;
          j = n;
          k = i1;
          if (i == 0) {
            paramInt2 = 1073741824;
            m = 300;
            j = n;
            k = i1;
          } 
        } 
      } else {
        throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
      } 
    } else {
      paramInt2 = i;
      m = paramInt1;
      j = n;
      k = i1;
      if (n == 0)
        if (isInEditMode()) {
          paramInt2 = i;
          m = paramInt1;
          j = n;
          k = i1;
          if (n == 0) {
            j = Integer.MIN_VALUE;
            k = 300;
            paramInt2 = i;
            m = paramInt1;
          } 
        } else {
          throw new IllegalStateException("Height must not be UNSPECIFIED");
        }  
    } 
    i = 0;
    paramInt1 = 0;
    if (j != Integer.MIN_VALUE) {
      if (j == 1073741824) {
        i = k - getPaddingTop() - getPaddingBottom();
        paramInt1 = i;
      } 
    } else {
      paramInt1 = k - getPaddingTop() - getPaddingBottom();
    } 
    float f = 0.0F;
    byte b = 0;
    int i4 = m - getPaddingLeft() - getPaddingRight();
    n = i4;
    int i5 = getChildCount();
    if (i5 > 2)
      Log.e("SlidingPaneLayout", "onMeasure: More than two child views are not supported."); 
    this.mSlideableView = null;
    i1 = 0;
    int i2 = k;
    int i3 = paramInt2;
    while (i1 < i5) {
      byte b1;
      View view = getChildAt(i1);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (view.getVisibility() == 8) {
        layoutParams.dimWhenOffset = false;
        paramInt2 = i;
        continue;
      } 
      float f1 = f;
      if (layoutParams.weight > 0.0F) {
        f += layoutParams.weight;
        f1 = f;
        if (layoutParams.width == 0) {
          paramInt2 = i;
          continue;
        } 
      } 
      paramInt2 = layoutParams.leftMargin + layoutParams.rightMargin;
      if (layoutParams.width == -2) {
        paramInt2 = View.MeasureSpec.makeMeasureSpec(i4 - paramInt2, -2147483648);
      } else if (layoutParams.width == -1) {
        paramInt2 = View.MeasureSpec.makeMeasureSpec(i4 - paramInt2, 1073741824);
      } else {
        paramInt2 = View.MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824);
      } 
      if (layoutParams.height == -2) {
        k = View.MeasureSpec.makeMeasureSpec(paramInt1, -2147483648);
      } else if (layoutParams.height == -1) {
        k = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
      } else {
        k = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
      } 
      view.measure(paramInt2, k);
      k = view.getMeasuredWidth();
      int i6 = view.getMeasuredHeight();
      paramInt2 = i;
      if (j == Integer.MIN_VALUE) {
        paramInt2 = i;
        if (i6 > i)
          paramInt2 = Math.min(i6, paramInt1); 
      } 
      n -= k;
      if (n < 0) {
        b1 = 1;
      } else {
        b1 = 0;
      } 
      layoutParams.slideable = b1;
      if (layoutParams.slideable)
        this.mSlideableView = view; 
      b = b1 | b;
      f = f1;
      continue;
      i1++;
      i = paramInt2;
    } 
    if (b != 0 || f > 0.0F) {
      i3 = i4 - this.mOverhangSize;
      i2 = 0;
      k = i5;
      paramInt2 = paramInt1;
      i1 = j;
      j = i3;
      while (i2 < k) {
        View view = getChildAt(i2);
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          if (view.getVisibility() != 8) {
            if (layoutParams.width == 0 && layoutParams.weight > 0.0F) {
              paramInt1 = 1;
            } else {
              paramInt1 = 0;
            } 
            if (paramInt1 != 0) {
              i3 = 0;
            } else {
              i3 = view.getMeasuredWidth();
            } 
            if (b != 0 && view != this.mSlideableView) {
              if (layoutParams.width < 0 && (i3 > j || layoutParams.weight > 0.0F)) {
                if (paramInt1 != 0) {
                  if (layoutParams.height == -2) {
                    paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt2, -2147483648);
                  } else if (layoutParams.height == -1) {
                    paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
                  } else {
                    paramInt1 = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
                  } 
                } else {
                  paramInt1 = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 1073741824);
                } 
                view.measure(View.MeasureSpec.makeMeasureSpec(j, 1073741824), paramInt1);
              } 
            } else if (layoutParams.weight > 0.0F) {
              if (layoutParams.width == 0) {
                if (layoutParams.height == -2) {
                  paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt2, -2147483648);
                } else if (layoutParams.height == -1) {
                  paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
                } else {
                  paramInt1 = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
                } 
              } else {
                paramInt1 = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 1073741824);
              } 
              if (b != 0) {
                i5 = i4 - layoutParams.leftMargin + layoutParams.rightMargin;
                int i6 = View.MeasureSpec.makeMeasureSpec(i5, 1073741824);
                if (i3 != i5)
                  view.measure(i6, paramInt1); 
              } else {
                i5 = Math.max(0, n);
                view.measure(View.MeasureSpec.makeMeasureSpec(i3 + (int)(layoutParams.weight * i5 / f), 1073741824), paramInt1);
              } 
            } 
          } 
        } 
        i2++;
      } 
    } 
    setMeasuredDimension(m, getPaddingTop() + i + getPaddingBottom());
    this.mCanSlide = b;
    if (this.mDragHelper.getViewDragState() != 0 && b == 0)
      this.mDragHelper.abort(); 
  }
  
  void onPanelDragged(int paramInt) {
    int j;
    if (this.mSlideableView == null) {
      this.mSlideOffset = 0.0F;
      return;
    } 
    boolean bool = isLayoutRtlSupport();
    LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
    int i = this.mSlideableView.getWidth();
    if (bool)
      paramInt = getWidth() - paramInt - i; 
    if (bool) {
      i = getPaddingRight();
    } else {
      i = getPaddingLeft();
    } 
    if (bool) {
      j = layoutParams.rightMargin;
    } else {
      j = layoutParams.leftMargin;
    } 
    this.mSlideOffset = (paramInt - i + j) / this.mSlideRange;
    if (this.mParallaxBy != 0)
      parallaxOtherViews(this.mSlideOffset); 
    if (layoutParams.dimWhenOffset)
      dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor); 
    dispatchOnPanelSlide(this.mSlideableView);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (savedState.isOpen) {
      openPane();
    } else {
      closePane();
    } 
    this.mPreservedOpenState = savedState.isOpen;
  }
  
  protected Parcelable onSaveInstanceState() {
    boolean bool;
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    if (isSlideable()) {
      bool = isOpen();
    } else {
      bool = this.mPreservedOpenState;
    } 
    savedState.isOpen = bool;
    return savedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3)
      this.mFirstLayout = true; 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (!this.mCanSlide)
      return super.onTouchEvent(paramMotionEvent); 
    this.mDragHelper.processTouchEvent(paramMotionEvent);
    switch (paramMotionEvent.getActionMasked()) {
      default:
        return true;
      case 1:
        if (isDimmed(this.mSlideableView)) {
          float f3 = paramMotionEvent.getX();
          float f4 = paramMotionEvent.getY();
          float f5 = f3 - this.mInitialMotionX;
          float f6 = f4 - this.mInitialMotionY;
          int i = this.mDragHelper.getTouchSlop();
          if (f5 * f5 + f6 * f6 < (i * i) && this.mDragHelper.isViewUnder(this.mSlideableView, (int)f3, (int)f4)) {
            closePane(this.mSlideableView, 0);
            return true;
          } 
        } 
        return true;
      case 0:
        break;
    } 
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    this.mInitialMotionX = f1;
    this.mInitialMotionY = f2;
    return true;
  }
  
  public boolean openPane() { return openPane(this.mSlideableView, 0); }
  
  public void requestChildFocus(View paramView1, View paramView2) {
    super.requestChildFocus(paramView1, paramView2);
    if (!isInTouchMode() && !this.mCanSlide) {
      boolean bool;
      if (paramView1 == this.mSlideableView) {
        bool = true;
      } else {
        bool = false;
      } 
      this.mPreservedOpenState = bool;
    } 
  }
  
  void setAllChildrenVisible() {
    byte b = 0;
    int i = getChildCount();
    while (b < i) {
      View view = getChildAt(b);
      if (view.getVisibility() == 4)
        view.setVisibility(0); 
      b++;
    } 
  }
  
  public void setCoveredFadeColor(@ColorInt int paramInt) { this.mCoveredFadeColor = paramInt; }
  
  public void setPanelSlideListener(@Nullable PanelSlideListener paramPanelSlideListener) { this.mPanelSlideListener = paramPanelSlideListener; }
  
  public void setParallaxDistance(@Px int paramInt) {
    this.mParallaxBy = paramInt;
    requestLayout();
  }
  
  @Deprecated
  public void setShadowDrawable(Drawable paramDrawable) { setShadowDrawableLeft(paramDrawable); }
  
  public void setShadowDrawableLeft(@Nullable Drawable paramDrawable) { this.mShadowDrawableLeft = paramDrawable; }
  
  public void setShadowDrawableRight(@Nullable Drawable paramDrawable) { this.mShadowDrawableRight = paramDrawable; }
  
  @Deprecated
  public void setShadowResource(@DrawableRes int paramInt) { setShadowDrawable(getResources().getDrawable(paramInt)); }
  
  public void setShadowResourceLeft(int paramInt) { setShadowDrawableLeft(ContextCompat.getDrawable(getContext(), paramInt)); }
  
  public void setShadowResourceRight(int paramInt) { setShadowDrawableRight(ContextCompat.getDrawable(getContext(), paramInt)); }
  
  public void setSliderFadeColor(@ColorInt int paramInt) { this.mSliderFadeColor = paramInt; }
  
  @Deprecated
  public void smoothSlideClosed() { closePane(); }
  
  @Deprecated
  public void smoothSlideOpen() { openPane(); }
  
  boolean smoothSlideTo(float paramFloat, int paramInt) {
    if (!this.mCanSlide)
      return false; 
    boolean bool = isLayoutRtlSupport();
    LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
    if (bool) {
      paramInt = getPaddingRight();
      int i = layoutParams.rightMargin;
      int j = this.mSlideableView.getWidth();
      paramInt = (int)(getWidth() - (paramInt + i) + this.mSlideRange * paramFloat + j);
    } else {
      paramInt = (int)((getPaddingLeft() + layoutParams.leftMargin) + this.mSlideRange * paramFloat);
    } 
    ViewDragHelper viewDragHelper = this.mDragHelper;
    View view = this.mSlideableView;
    if (viewDragHelper.smoothSlideViewTo(view, paramInt, view.getTop())) {
      setAllChildrenVisible();
      ViewCompat.postInvalidateOnAnimation(this);
      return true;
    } 
    return false;
  }
  
  void updateObscuredViewsVisibility(View paramView) {
    boolean bool4;
    boolean bool3;
    boolean bool2;
    boolean bool1;
    int j;
    int i;
    boolean bool = isLayoutRtlSupport();
    if (bool) {
      i = getWidth() - getPaddingRight();
    } else {
      i = getPaddingLeft();
    } 
    if (bool) {
      j = getPaddingLeft();
    } else {
      j = getWidth() - getPaddingRight();
    } 
    int k = getPaddingTop();
    int m = getHeight();
    int n = getPaddingBottom();
    if (paramView != null && viewIsOpaque(paramView)) {
      bool1 = paramView.getLeft();
      bool2 = paramView.getRight();
      bool3 = paramView.getTop();
      bool4 = paramView.getBottom();
    } else {
      bool1 = false;
      bool4 = false;
      bool3 = false;
      bool2 = false;
    } 
    byte b = 0;
    int i1 = getChildCount();
    while (b < i1) {
      View view = getChildAt(b);
      if (view == paramView)
        return; 
      if (view.getVisibility() != 8) {
        if (bool) {
          i2 = j;
        } else {
          i2 = i;
        } 
        int i3 = Math.max(i2, view.getLeft());
        int i4 = Math.max(k, view.getTop());
        if (bool) {
          i2 = i;
        } else {
          i2 = j;
        } 
        int i2 = Math.min(i2, view.getRight());
        int i5 = Math.min(m - n, view.getBottom());
        if (i3 >= bool1 && i4 >= bool3 && i2 <= bool2 && i5 <= bool4) {
          i2 = 4;
        } else {
          i2 = 0;
        } 
        view.setVisibility(i2);
      } 
      b++;
    } 
  }
  
  class AccessibilityDelegate extends AccessibilityDelegateCompat {
    private final Rect mTmpRect = new Rect();
    
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
      param1AccessibilityNodeInfoCompat1.setMovementGranularities(param1AccessibilityNodeInfoCompat2.getMovementGranularities());
    }
    
    public boolean filter(View param1View) { return SlidingPaneLayout.this.isDimmed(param1View); }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(SlidingPaneLayout.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(param1AccessibilityNodeInfoCompat);
      super.onInitializeAccessibilityNodeInfo(param1View, accessibilityNodeInfoCompat);
      copyNodeInfoNoChildren(param1AccessibilityNodeInfoCompat, accessibilityNodeInfoCompat);
      accessibilityNodeInfoCompat.recycle();
      param1AccessibilityNodeInfoCompat.setClassName(SlidingPaneLayout.class.getName());
      param1AccessibilityNodeInfoCompat.setSource(param1View);
      ViewParent viewParent = ViewCompat.getParentForAccessibility(param1View);
      if (viewParent instanceof View)
        param1AccessibilityNodeInfoCompat.setParent((View)viewParent); 
      int i = SlidingPaneLayout.this.getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = SlidingPaneLayout.this.getChildAt(b);
        if (!filter(view) && view.getVisibility() == 0) {
          ViewCompat.setImportantForAccessibility(view, 1);
          param1AccessibilityNodeInfoCompat.addChild(view);
        } 
      } 
    }
    
    public boolean onRequestSendAccessibilityEvent(ViewGroup param1ViewGroup, View param1View, AccessibilityEvent param1AccessibilityEvent) { return !filter(param1View) ? super.onRequestSendAccessibilityEvent(param1ViewGroup, param1View, param1AccessibilityEvent) : 0; }
  }
  
  private class DisableLayerRunnable implements Runnable {
    final View mChildView;
    
    DisableLayerRunnable(View param1View) { this.mChildView = param1View; }
    
    public void run() {
      if (this.mChildView.getParent() == SlidingPaneLayout.this) {
        this.mChildView.setLayerType(0, null);
        SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
      } 
      SlidingPaneLayout.this.mPostedRunnables.remove(this);
    }
  }
  
  private class DragHelperCallback extends ViewDragHelper.Callback {
    public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
      SlidingPaneLayout.LayoutParams layoutParams = (SlidingPaneLayout.LayoutParams)SlidingPaneLayout.this.mSlideableView.getLayoutParams();
      if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
        param1Int2 = SlidingPaneLayout.this.getWidth() - SlidingPaneLayout.this.getPaddingRight() + layoutParams.rightMargin + SlidingPaneLayout.this.mSlideableView.getWidth();
        int j = SlidingPaneLayout.this.mSlideRange;
        return Math.max(Math.min(param1Int1, param1Int2), param1Int2 - j);
      } 
      param1Int2 = SlidingPaneLayout.this.getPaddingLeft() + layoutParams.leftMargin;
      int i = SlidingPaneLayout.this.mSlideRange;
      return Math.min(Math.max(param1Int1, param1Int2), i + param1Int2);
    }
    
    public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) { return param1View.getTop(); }
    
    public int getViewHorizontalDragRange(View param1View) { return SlidingPaneLayout.this.mSlideRange; }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) { SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, param1Int2); }
    
    public void onViewCaptured(View param1View, int param1Int) { SlidingPaneLayout.this.setAllChildrenVisible(); }
    
    public void onViewDragStateChanged(int param1Int) {
      if (SlidingPaneLayout.this.mDragHelper.getViewDragState() == 0) {
        if (SlidingPaneLayout.this.mSlideOffset == 0.0F) {
          SlidingPaneLayout slidingPaneLayout1 = SlidingPaneLayout.this;
          slidingPaneLayout1.updateObscuredViewsVisibility(slidingPaneLayout1.mSlideableView);
          slidingPaneLayout1 = SlidingPaneLayout.this;
          slidingPaneLayout1.dispatchOnPanelClosed(slidingPaneLayout1.mSlideableView);
          SlidingPaneLayout.this.mPreservedOpenState = false;
          return;
        } 
        SlidingPaneLayout slidingPaneLayout = SlidingPaneLayout.this;
        slidingPaneLayout.dispatchOnPanelOpened(slidingPaneLayout.mSlideableView);
        SlidingPaneLayout.this.mPreservedOpenState = true;
      } 
    }
    
    public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      SlidingPaneLayout.this.onPanelDragged(param1Int1);
      SlidingPaneLayout.this.invalidate();
    }
    
    public void onViewReleased(View param1View, float param1Float1, float param1Float2) { // Byte code:
      //   0: aload_1
      //   1: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
      //   4: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
      //   7: astore #6
      //   9: aload_0
      //   10: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   13: invokevirtual isLayoutRtlSupport : ()Z
      //   16: ifeq -> 109
      //   19: aload_0
      //   20: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   23: invokevirtual getPaddingRight : ()I
      //   26: aload #6
      //   28: getfield rightMargin : I
      //   31: iadd
      //   32: istore #5
      //   34: fload_2
      //   35: fconst_0
      //   36: fcmpg
      //   37: iflt -> 67
      //   40: iload #5
      //   42: istore #4
      //   44: fload_2
      //   45: fconst_0
      //   46: fcmpl
      //   47: ifne -> 79
      //   50: iload #5
      //   52: istore #4
      //   54: aload_0
      //   55: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   58: getfield mSlideOffset : F
      //   61: ldc 0.5
      //   63: fcmpl
      //   64: ifle -> 79
      //   67: iload #5
      //   69: aload_0
      //   70: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   73: getfield mSlideRange : I
      //   76: iadd
      //   77: istore #4
      //   79: aload_0
      //   80: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   83: getfield mSlideableView : Landroid/view/View;
      //   86: invokevirtual getWidth : ()I
      //   89: istore #5
      //   91: aload_0
      //   92: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   95: invokevirtual getWidth : ()I
      //   98: iload #4
      //   100: isub
      //   101: iload #5
      //   103: isub
      //   104: istore #4
      //   106: goto -> 167
      //   109: aload_0
      //   110: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   113: invokevirtual getPaddingLeft : ()I
      //   116: aload #6
      //   118: getfield leftMargin : I
      //   121: iadd
      //   122: istore #4
      //   124: fload_2
      //   125: fconst_0
      //   126: fcmpl
      //   127: ifgt -> 155
      //   130: fload_2
      //   131: fconst_0
      //   132: fcmpl
      //   133: ifne -> 152
      //   136: aload_0
      //   137: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   140: getfield mSlideOffset : F
      //   143: ldc 0.5
      //   145: fcmpl
      //   146: ifle -> 152
      //   149: goto -> 155
      //   152: goto -> 167
      //   155: iload #4
      //   157: aload_0
      //   158: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   161: getfield mSlideRange : I
      //   164: iadd
      //   165: istore #4
      //   167: aload_0
      //   168: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   171: getfield mDragHelper : Landroid/support/v4/widget/ViewDragHelper;
      //   174: iload #4
      //   176: aload_1
      //   177: invokevirtual getTop : ()I
      //   180: invokevirtual settleCapturedViewAt : (II)Z
      //   183: pop
      //   184: aload_0
      //   185: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   188: invokevirtual invalidate : ()V
      //   191: return }
    
    public boolean tryCaptureView(View param1View, int param1Int) { return SlidingPaneLayout.this.mIsUnableToDrag ? false : ((SlidingPaneLayout.LayoutParams)param1View.getLayoutParams()).slideable; }
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    private static final int[] ATTRS = { 16843137 };
    
    Paint dimPaint;
    
    boolean dimWhenOffset;
    
    boolean slideable;
    
    public float weight = 0.0F;
    
    public LayoutParams() { super(-1, -1); }
    
    public LayoutParams(int param1Int1, int param1Int2) { super(param1Int1, param1Int2); }
    
    public LayoutParams(@NonNull Context param1Context, @Nullable AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, ATTRS);
      this.weight = typedArray.getFloat(0, 0.0F);
      typedArray.recycle();
    }
    
    public LayoutParams(@NonNull LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.weight = param1LayoutParams.weight;
    }
    
    public LayoutParams(@NonNull ViewGroup.LayoutParams param1LayoutParams) { super(param1LayoutParams); }
    
    public LayoutParams(@NonNull ViewGroup.MarginLayoutParams param1MarginLayoutParams) { super(param1MarginLayoutParams); }
  }
  
  public static interface PanelSlideListener {
    void onPanelClosed(@NonNull View param1View);
    
    void onPanelOpened(@NonNull View param1View);
    
    void onPanelSlide(@NonNull View param1View, float param1Float);
  }
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
        public SlidingPaneLayout.SavedState createFromParcel(Parcel param2Parcel) { return new SlidingPaneLayout.SavedState(param2Parcel, null); }
        
        public SlidingPaneLayout.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) { return new SlidingPaneLayout.SavedState(param2Parcel, null); }
        
        public SlidingPaneLayout.SavedState[] newArray(int param2Int) { return new SlidingPaneLayout.SavedState[param2Int]; }
      };
    
    boolean isOpen;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      if (param1Parcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.isOpen = bool;
    }
    
    SavedState(Parcelable param1Parcelable) { super(param1Parcelable); }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) { throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n"); }
  }
  
  static final class null extends Object implements Parcelable.ClassLoaderCreator<SavedState> {
    public SlidingPaneLayout.SavedState createFromParcel(Parcel param1Parcel) { return new SlidingPaneLayout.SavedState(param1Parcel, null); }
    
    public SlidingPaneLayout.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) { return new SlidingPaneLayout.SavedState(param1Parcel, null); }
    
    public SlidingPaneLayout.SavedState[] newArray(int param1Int) { return new SlidingPaneLayout.SavedState[param1Int]; }
  }
  
  public static class SimplePanelSlideListener implements PanelSlideListener {
    public void onPanelClosed(View param1View) {}
    
    public void onPanelOpened(View param1View) {}
    
    public void onPanelSlide(View param1View, float param1Float) {}
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\widget\SlidingPaneLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */