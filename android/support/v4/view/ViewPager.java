package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewPager extends ViewGroup {
  private static final int CLOSE_ENOUGH = 2;
  
  private static final Comparator<ItemInfo> COMPARATOR;
  
  private static final boolean DEBUG = false;
  
  private static final int DEFAULT_GUTTER_SIZE = 16;
  
  private static final int DEFAULT_OFFSCREEN_PAGES = 1;
  
  private static final int DRAW_ORDER_DEFAULT = 0;
  
  private static final int DRAW_ORDER_FORWARD = 1;
  
  private static final int DRAW_ORDER_REVERSE = 2;
  
  private static final int INVALID_POINTER = -1;
  
  static final int[] LAYOUT_ATTRS = { 16842931 };
  
  private static final int MAX_SETTLE_DURATION = 600;
  
  private static final int MIN_DISTANCE_FOR_FLING = 25;
  
  private static final int MIN_FLING_VELOCITY = 400;
  
  public static final int SCROLL_STATE_DRAGGING = 1;
  
  public static final int SCROLL_STATE_IDLE = 0;
  
  public static final int SCROLL_STATE_SETTLING = 2;
  
  private static final String TAG = "ViewPager";
  
  private static final boolean USE_CACHE = false;
  
  private static final Interpolator sInterpolator;
  
  private static final ViewPositionComparator sPositionComparator;
  
  private int mActivePointerId = -1;
  
  PagerAdapter mAdapter;
  
  private List<OnAdapterChangeListener> mAdapterChangeListeners;
  
  private int mBottomPageBounds;
  
  private boolean mCalledSuper;
  
  private int mChildHeightMeasureSpec;
  
  private int mChildWidthMeasureSpec;
  
  private int mCloseEnough;
  
  int mCurItem;
  
  private int mDecorChildCount;
  
  private int mDefaultGutterSize;
  
  private int mDrawingOrder;
  
  private ArrayList<View> mDrawingOrderedChildren;
  
  private final Runnable mEndScrollRunnable = new Runnable() {
      public void run() {
        ViewPager.this.setScrollState(0);
        ViewPager.this.populate();
      }
    };
  
  private int mExpectedAdapterCount;
  
  private long mFakeDragBeginTime;
  
  private boolean mFakeDragging;
  
  private boolean mFirstLayout = true;
  
  private float mFirstOffset = -3.4028235E38F;
  
  private int mFlingDistance;
  
  private int mGutterSize;
  
  private boolean mInLayout;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  private OnPageChangeListener mInternalPageChangeListener;
  
  private boolean mIsBeingDragged;
  
  private boolean mIsScrollStarted;
  
  private boolean mIsUnableToDrag;
  
  private final ArrayList<ItemInfo> mItems = new ArrayList();
  
  private float mLastMotionX;
  
  private float mLastMotionY;
  
  private float mLastOffset = Float.MAX_VALUE;
  
  private EdgeEffect mLeftEdge;
  
  private Drawable mMarginDrawable;
  
  private int mMaximumVelocity;
  
  private int mMinimumVelocity;
  
  private boolean mNeedCalculatePageOffsets = false;
  
  private PagerObserver mObserver;
  
  private int mOffscreenPageLimit = 1;
  
  private OnPageChangeListener mOnPageChangeListener;
  
  private List<OnPageChangeListener> mOnPageChangeListeners;
  
  private int mPageMargin;
  
  private PageTransformer mPageTransformer;
  
  private int mPageTransformerLayerType;
  
  private boolean mPopulatePending;
  
  private Parcelable mRestoredAdapterState = null;
  
  private ClassLoader mRestoredClassLoader = null;
  
  private int mRestoredCurItem = -1;
  
  private EdgeEffect mRightEdge;
  
  private int mScrollState = 0;
  
  private Scroller mScroller;
  
  private boolean mScrollingCacheEnabled;
  
  private final ItemInfo mTempItem = new ItemInfo();
  
  private final Rect mTempRect = new Rect();
  
  private int mTopPageBounds;
  
  private int mTouchSlop;
  
  private VelocityTracker mVelocityTracker;
  
  static  {
    COMPARATOR = new Comparator<ItemInfo>() {
        public int compare(ViewPager.ItemInfo param1ItemInfo1, ViewPager.ItemInfo param1ItemInfo2) { return param1ItemInfo1.position - param1ItemInfo2.position; }
      };
    sInterpolator = new Interpolator() {
        public float getInterpolation(float param1Float) {
          param1Float--;
          return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
        }
      };
    sPositionComparator = new ViewPositionComparator();
  }
  
  public ViewPager(@NonNull Context paramContext) {
    super(paramContext);
    initViewPager();
  }
  
  public ViewPager(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initViewPager();
  }
  
  private void calculatePageOffsets(ItemInfo paramItemInfo1, int paramInt, ItemInfo paramItemInfo2) {
    float f2;
    int m = this.mAdapter.getCount();
    int i = getClientWidth();
    if (i > 0) {
      f2 = this.mPageMargin / i;
    } else {
      f2 = 0.0F;
    } 
    if (paramItemInfo2 != null) {
      i = paramItemInfo2.position;
      if (i < paramItemInfo1.position) {
        byte b = 0;
        f1 = paramItemInfo2.offset + paramItemInfo2.widthFactor + f2;
        while (++i <= paramItemInfo1.position && b < this.mItems.size()) {
          int n;
          float f;
          paramItemInfo2 = (ItemInfo)this.mItems.get(b);
          while (true) {
            f = f1;
            n = i;
            if (i > paramItemInfo2.position) {
              f = f1;
              n = i;
              if (b < this.mItems.size() - 1) {
                paramItemInfo2 = (ItemInfo)this.mItems.get(++b);
                continue;
              } 
            } 
            break;
          } 
          while (n < paramItemInfo2.position) {
            f += this.mAdapter.getPageWidth(n) + f2;
            n++;
          } 
          paramItemInfo2.offset = f;
          f1 = f + paramItemInfo2.widthFactor + f2;
          i = n + 1;
        } 
      } else if (i > paramItemInfo1.position) {
        int n = this.mItems.size() - 1;
        f1 = paramItemInfo2.offset;
        while (--i >= paramItemInfo1.position && n >= 0) {
          int i1;
          float f;
          paramItemInfo2 = (ItemInfo)this.mItems.get(n);
          while (true) {
            f = f1;
            i1 = i;
            if (i < paramItemInfo2.position) {
              f = f1;
              i1 = i;
              if (n > 0) {
                paramItemInfo2 = (ItemInfo)this.mItems.get(--n);
                continue;
              } 
            } 
            break;
          } 
          while (i1 > paramItemInfo2.position) {
            f -= this.mAdapter.getPageWidth(i1) + f2;
            i1--;
          } 
          f1 = f - paramItemInfo2.widthFactor + f2;
          paramItemInfo2.offset = f1;
          i = i1 - 1;
        } 
      } 
    } 
    int k = this.mItems.size();
    float f3 = paramItemInfo1.offset;
    i = paramItemInfo1.position - 1;
    if (paramItemInfo1.position == 0) {
      f1 = paramItemInfo1.offset;
    } else {
      f1 = -3.4028235E38F;
    } 
    this.mFirstOffset = f1;
    if (paramItemInfo1.position == m - 1) {
      f1 = paramItemInfo1.offset + paramItemInfo1.widthFactor - 1.0F;
    } else {
      f1 = Float.MAX_VALUE;
    } 
    this.mLastOffset = f1;
    int j = paramInt - 1;
    float f1 = f3;
    while (j >= 0) {
      paramItemInfo2 = (ItemInfo)this.mItems.get(j);
      while (i > paramItemInfo2.position) {
        f1 -= this.mAdapter.getPageWidth(i) + f2;
        i--;
      } 
      f1 -= paramItemInfo2.widthFactor + f2;
      paramItemInfo2.offset = f1;
      if (paramItemInfo2.position == 0)
        this.mFirstOffset = f1; 
      j--;
      i--;
    } 
    f1 = paramItemInfo1.offset + paramItemInfo1.widthFactor + f2;
    j = paramItemInfo1.position + 1;
    i = paramInt + 1;
    for (paramInt = j; i < k; paramInt++) {
      paramItemInfo1 = (ItemInfo)this.mItems.get(i);
      while (paramInt < paramItemInfo1.position) {
        f1 += this.mAdapter.getPageWidth(paramInt) + f2;
        paramInt++;
      } 
      if (paramItemInfo1.position == m - 1)
        this.mLastOffset = paramItemInfo1.widthFactor + f1 - 1.0F; 
      paramItemInfo1.offset = f1;
      f1 += paramItemInfo1.widthFactor + f2;
      i++;
    } 
    this.mNeedCalculatePageOffsets = false;
  }
  
  private void completeScroll(boolean paramBoolean) {
    boolean bool;
    if (this.mScrollState == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      setScrollingCacheEnabled(false);
      if (true ^ this.mScroller.isFinished()) {
        this.mScroller.abortAnimation();
        int i = getScrollX();
        int j = getScrollY();
        int k = this.mScroller.getCurrX();
        int m = this.mScroller.getCurrY();
        if (i != k || j != m) {
          scrollTo(k, m);
          if (k != i)
            pageScrolled(k); 
        } 
      } 
    } 
    this.mPopulatePending = false;
    for (byte b = 0; b < this.mItems.size(); b++) {
      ItemInfo itemInfo = (ItemInfo)this.mItems.get(b);
      if (itemInfo.scrolling) {
        bool = true;
        itemInfo.scrolling = false;
      } 
    } 
    if (bool) {
      if (paramBoolean) {
        ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
        return;
      } 
      this.mEndScrollRunnable.run();
    } 
  }
  
  private int determineTargetPage(int paramInt1, float paramFloat, int paramInt2, int paramInt3) {
    if (Math.abs(paramInt3) > this.mFlingDistance && Math.abs(paramInt2) > this.mMinimumVelocity) {
      if (paramInt2 <= 0)
        paramInt1++; 
    } else {
      float f;
      if (paramInt1 >= this.mCurItem) {
        f = 0.4F;
      } else {
        f = 0.6F;
      } 
      paramInt1 += (int)(paramFloat + f);
    } 
    paramInt2 = paramInt1;
    if (this.mItems.size() > 0) {
      ItemInfo itemInfo1 = (ItemInfo)this.mItems.get(0);
      ArrayList arrayList = this.mItems;
      ItemInfo itemInfo2 = (ItemInfo)arrayList.get(arrayList.size() - 1);
      paramInt2 = Math.max(itemInfo1.position, Math.min(paramInt1, itemInfo2.position));
    } 
    return paramInt2;
  }
  
  private void dispatchOnPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListener;
    if (onPageChangeListener2 != null)
      onPageChangeListener2.onPageScrolled(paramInt1, paramFloat, paramInt2); 
    List list = this.mOnPageChangeListeners;
    if (list != null) {
      byte b = 0;
      int i = list.size();
      while (b < i) {
        OnPageChangeListener onPageChangeListener = (OnPageChangeListener)this.mOnPageChangeListeners.get(b);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2); 
        b++;
      } 
    } 
    OnPageChangeListener onPageChangeListener1 = this.mInternalPageChangeListener;
    if (onPageChangeListener1 != null)
      onPageChangeListener1.onPageScrolled(paramInt1, paramFloat, paramInt2); 
  }
  
  private void dispatchOnPageSelected(int paramInt) {
    OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListener;
    if (onPageChangeListener2 != null)
      onPageChangeListener2.onPageSelected(paramInt); 
    List list = this.mOnPageChangeListeners;
    if (list != null) {
      byte b = 0;
      int i = list.size();
      while (b < i) {
        OnPageChangeListener onPageChangeListener = (OnPageChangeListener)this.mOnPageChangeListeners.get(b);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageSelected(paramInt); 
        b++;
      } 
    } 
    OnPageChangeListener onPageChangeListener1 = this.mInternalPageChangeListener;
    if (onPageChangeListener1 != null)
      onPageChangeListener1.onPageSelected(paramInt); 
  }
  
  private void dispatchOnScrollStateChanged(int paramInt) {
    OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListener;
    if (onPageChangeListener2 != null)
      onPageChangeListener2.onPageScrollStateChanged(paramInt); 
    List list = this.mOnPageChangeListeners;
    if (list != null) {
      byte b = 0;
      int i = list.size();
      while (b < i) {
        OnPageChangeListener onPageChangeListener = (OnPageChangeListener)this.mOnPageChangeListeners.get(b);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageScrollStateChanged(paramInt); 
        b++;
      } 
    } 
    OnPageChangeListener onPageChangeListener1 = this.mInternalPageChangeListener;
    if (onPageChangeListener1 != null)
      onPageChangeListener1.onPageScrollStateChanged(paramInt); 
  }
  
  private void enableLayers(boolean paramBoolean) {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      byte b1;
      if (paramBoolean) {
        b1 = this.mPageTransformerLayerType;
      } else {
        b1 = 0;
      } 
      getChildAt(b).setLayerType(b1, null);
    } 
  }
  
  private void endDrag() {
    this.mIsBeingDragged = false;
    this.mIsUnableToDrag = false;
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  private Rect getChildRectInPagerCoordinates(Rect paramRect, View paramView) {
    Rect rect = paramRect;
    if (paramRect == null)
      rect = new Rect(); 
    if (paramView == null) {
      rect.set(0, 0, 0, 0);
      return rect;
    } 
    rect.left = paramView.getLeft();
    rect.right = paramView.getRight();
    rect.top = paramView.getTop();
    rect.bottom = paramView.getBottom();
    ViewParent viewParent = paramView.getParent();
    while (viewParent instanceof ViewGroup && viewParent != this) {
      ViewGroup viewGroup = (ViewGroup)viewParent;
      rect.left += viewGroup.getLeft();
      rect.right += viewGroup.getRight();
      rect.top += viewGroup.getTop();
      rect.bottom += viewGroup.getBottom();
      ViewParent viewParent1 = viewGroup.getParent();
    } 
    return rect;
  }
  
  private int getClientWidth() { return getMeasuredWidth() - getPaddingLeft() - getPaddingRight(); }
  
  private ItemInfo infoForCurrentScrollPosition() {
    float f1;
    int i = getClientWidth();
    float f2 = 0.0F;
    if (i > 0) {
      f1 = getScrollX() / i;
    } else {
      f1 = 0.0F;
    } 
    if (i > 0)
      f2 = this.mPageMargin / i; 
    int j = -1;
    float f3 = 0.0F;
    float f4 = 0.0F;
    boolean bool = true;
    ItemInfo itemInfo = null;
    i = 0;
    while (i < this.mItems.size()) {
      ItemInfo itemInfo2 = (ItemInfo)this.mItems.get(i);
      int k = i;
      ItemInfo itemInfo1 = itemInfo2;
      if (!bool) {
        k = i;
        itemInfo1 = itemInfo2;
        if (itemInfo2.position != j + 1) {
          itemInfo1 = this.mTempItem;
          itemInfo1.offset = f3 + f4 + f2;
          itemInfo1.position = j + 1;
          itemInfo1.widthFactor = this.mAdapter.getPageWidth(itemInfo1.position);
          k = i - 1;
        } 
      } 
      f3 = itemInfo1.offset;
      f4 = itemInfo1.widthFactor;
      if (bool || f1 >= f3) {
        if (f1 >= f4 + f3 + f2) {
          if (k == this.mItems.size() - 1)
            return itemInfo1; 
          bool = false;
          j = itemInfo1.position;
          f4 = itemInfo1.widthFactor;
          i = k + 1;
          itemInfo = itemInfo1;
          continue;
        } 
        return itemInfo1;
      } 
      return itemInfo;
    } 
    return itemInfo;
  }
  
  private static boolean isDecorView(@NonNull View paramView) { return (paramView.getClass().getAnnotation(DecorView.class) != null); }
  
  private boolean isGutterDrag(float paramFloat1, float paramFloat2) { return ((paramFloat1 < this.mGutterSize && paramFloat2 > 0.0F) || (paramFloat1 > (getWidth() - this.mGutterSize) && paramFloat2 < 0.0F)); }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionIndex();
    if (paramMotionEvent.getPointerId(i) == this.mActivePointerId) {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mLastMotionX = paramMotionEvent.getX(i);
      this.mActivePointerId = paramMotionEvent.getPointerId(i);
      VelocityTracker velocityTracker = this.mVelocityTracker;
      if (velocityTracker != null)
        velocityTracker.clear(); 
    } 
  }
  
  private boolean pageScrolled(int paramInt) {
    if (this.mItems.size() == 0) {
      if (this.mFirstLayout)
        return false; 
      this.mCalledSuper = false;
      onPageScrolled(0, 0.0F, 0);
      if (this.mCalledSuper)
        return false; 
      throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    } 
    ItemInfo itemInfo = infoForCurrentScrollPosition();
    int j = getClientWidth();
    int k = this.mPageMargin;
    float f = k / j;
    int i = itemInfo.position;
    f = (paramInt / j - itemInfo.offset) / (itemInfo.widthFactor + f);
    paramInt = (int)((j + k) * f);
    this.mCalledSuper = false;
    onPageScrolled(i, f, paramInt);
    if (this.mCalledSuper)
      return true; 
    throw new IllegalStateException("onPageScrolled did not call superclass implementation");
  }
  
  private boolean performDrag(float paramFloat) {
    byte b1 = 0;
    byte b2 = 0;
    int j = 0;
    float f1 = this.mLastMotionX;
    this.mLastMotionX = paramFloat;
    float f2 = getScrollX() + f1 - paramFloat;
    int i = getClientWidth();
    paramFloat = i * this.mFirstOffset;
    f1 = i * this.mLastOffset;
    boolean bool1 = true;
    boolean bool2 = true;
    ItemInfo itemInfo1 = (ItemInfo)this.mItems.get(0);
    ArrayList arrayList = this.mItems;
    ItemInfo itemInfo2 = (ItemInfo)arrayList.get(arrayList.size() - 1);
    if (itemInfo1.position != 0) {
      bool1 = false;
      paramFloat = itemInfo1.offset * i;
    } 
    if (itemInfo2.position != this.mAdapter.getCount() - 1) {
      bool2 = false;
      f1 = itemInfo2.offset * i;
    } 
    if (f2 < paramFloat) {
      if (bool1) {
        this.mLeftEdge.onPull(Math.abs(paramFloat - f2) / i);
        j = 1;
      } 
    } else {
      j = b2;
      paramFloat = f2;
      if (f2 > f1) {
        j = b1;
        if (bool2) {
          this.mRightEdge.onPull(Math.abs(f2 - f1) / i);
          j = 1;
        } 
        paramFloat = f1;
      } 
    } 
    this.mLastMotionX += paramFloat - (int)paramFloat;
    scrollTo((int)paramFloat, getScrollY());
    pageScrolled((int)paramFloat);
    return j;
  }
  
  private void recomputeScrollPosition(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    float f;
    if (paramInt2 > 0 && !this.mItems.isEmpty()) {
      if (!this.mScroller.isFinished()) {
        this.mScroller.setFinalX(getCurrentItem() * getClientWidth());
        return;
      } 
      int i = getPaddingLeft();
      int j = getPaddingRight();
      int k = getPaddingLeft();
      int m = getPaddingRight();
      f = getScrollX() / (paramInt2 - k - m + paramInt4);
      scrollTo((int)((paramInt1 - i - j + paramInt3) * f), getScrollY());
      return;
    } 
    ItemInfo itemInfo = infoForPosition(this.mCurItem);
    if (itemInfo != null) {
      f = Math.min(itemInfo.offset, this.mLastOffset);
    } else {
      f = 0.0F;
    } 
    paramInt1 = (int)((paramInt1 - getPaddingLeft() - getPaddingRight()) * f);
    if (paramInt1 != getScrollX()) {
      completeScroll(false);
      scrollTo(paramInt1, getScrollY());
    } 
  }
  
  private void removeNonDecorViews() {
    for (byte b = 0; b < getChildCount(); b = b1 + 1) {
      byte b1 = b;
      if (!((LayoutParams)getChildAt(b).getLayoutParams()).isDecor) {
        removeViewAt(b);
        b1 = b - 1;
      } 
    } 
  }
  
  private void requestParentDisallowInterceptTouchEvent(boolean paramBoolean) {
    ViewParent viewParent = getParent();
    if (viewParent != null)
      viewParent.requestDisallowInterceptTouchEvent(paramBoolean); 
  }
  
  private boolean resetTouch() {
    this.mActivePointerId = -1;
    endDrag();
    this.mLeftEdge.onRelease();
    this.mRightEdge.onRelease();
    return (this.mLeftEdge.isFinished() || this.mRightEdge.isFinished());
  }
  
  private void scrollToItem(int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2) {
    ItemInfo itemInfo = infoForPosition(paramInt1);
    int i = 0;
    if (itemInfo != null)
      i = (int)(getClientWidth() * Math.max(this.mFirstOffset, Math.min(itemInfo.offset, this.mLastOffset))); 
    if (paramBoolean1) {
      smoothScrollTo(i, 0, paramInt2);
      if (paramBoolean2) {
        dispatchOnPageSelected(paramInt1);
        return;
      } 
    } else {
      if (paramBoolean2)
        dispatchOnPageSelected(paramInt1); 
      completeScroll(false);
      scrollTo(i, 0);
      pageScrolled(i);
    } 
  }
  
  private void setScrollingCacheEnabled(boolean paramBoolean) {
    if (this.mScrollingCacheEnabled != paramBoolean)
      this.mScrollingCacheEnabled = paramBoolean; 
  }
  
  private void sortChildDrawingOrder() {
    if (this.mDrawingOrder != 0) {
      ArrayList arrayList = this.mDrawingOrderedChildren;
      if (arrayList == null) {
        this.mDrawingOrderedChildren = new ArrayList();
      } else {
        arrayList.clear();
      } 
      int i = getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = getChildAt(b);
        this.mDrawingOrderedChildren.add(view);
      } 
      Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
    } 
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2) {
    int i = paramArrayList.size();
    int j = getDescendantFocusability();
    if (j != 393216) {
      byte b;
      for (b = 0; b < getChildCount(); b++) {
        View view = getChildAt(b);
        if (view.getVisibility() == 0) {
          ItemInfo itemInfo = infoForChild(view);
          if (itemInfo != null && itemInfo.position == this.mCurItem)
            view.addFocusables(paramArrayList, paramInt1, paramInt2); 
        } 
      } 
    } 
    if (j != 262144 || i == paramArrayList.size()) {
      if (!isFocusable())
        return; 
      if ((paramInt2 & true) == 1 && isInTouchMode() && !isFocusableInTouchMode())
        return; 
      if (paramArrayList != null)
        paramArrayList.add(this); 
    } 
  }
  
  ItemInfo addNewItem(int paramInt1, int paramInt2) {
    ItemInfo itemInfo = new ItemInfo();
    itemInfo.position = paramInt1;
    itemInfo.object = this.mAdapter.instantiateItem(this, paramInt1);
    itemInfo.widthFactor = this.mAdapter.getPageWidth(paramInt1);
    if (paramInt2 < 0 || paramInt2 >= this.mItems.size()) {
      this.mItems.add(itemInfo);
      return itemInfo;
    } 
    this.mItems.add(paramInt2, itemInfo);
    return itemInfo;
  }
  
  public void addOnAdapterChangeListener(@NonNull OnAdapterChangeListener paramOnAdapterChangeListener) {
    if (this.mAdapterChangeListeners == null)
      this.mAdapterChangeListeners = new ArrayList(); 
    this.mAdapterChangeListeners.add(paramOnAdapterChangeListener);
  }
  
  public void addOnPageChangeListener(@NonNull OnPageChangeListener paramOnPageChangeListener) {
    if (this.mOnPageChangeListeners == null)
      this.mOnPageChangeListeners = new ArrayList(); 
    this.mOnPageChangeListeners.add(paramOnPageChangeListener);
  }
  
  public void addTouchables(ArrayList<View> paramArrayList) {
    for (byte b = 0; b < getChildCount(); b++) {
      View view = getChildAt(b);
      if (view.getVisibility() == 0) {
        ItemInfo itemInfo = infoForChild(view);
        if (itemInfo != null && itemInfo.position == this.mCurItem)
          view.addTouchables(paramArrayList); 
      } 
    } 
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    ViewGroup.LayoutParams layoutParams = paramLayoutParams;
    if (!checkLayoutParams(paramLayoutParams))
      layoutParams = generateLayoutParams(paramLayoutParams); 
    paramLayoutParams = (LayoutParams)layoutParams;
    paramLayoutParams.isDecor |= isDecorView(paramView);
    if (this.mInLayout) {
      if (paramLayoutParams == null || !paramLayoutParams.isDecor) {
        paramLayoutParams.needsMeasure = true;
        addViewInLayout(paramView, paramInt, layoutParams);
        return;
      } 
      throw new IllegalStateException("Cannot add pager decor view during layout");
    } 
    super.addView(paramView, paramInt, layoutParams);
  }
  
  public boolean arrowScroll(int paramInt) { // Byte code:
    //   0: aload_0
    //   1: invokevirtual findFocus : ()Landroid/view/View;
    //   4: astore #7
    //   6: aload #7
    //   8: aload_0
    //   9: if_acmpne -> 18
    //   12: aconst_null
    //   13: astore #6
    //   15: goto -> 193
    //   18: aload #7
    //   20: astore #6
    //   22: aload #7
    //   24: ifnull -> 193
    //   27: iconst_0
    //   28: istore_3
    //   29: aload #7
    //   31: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   34: astore #6
    //   36: iload_3
    //   37: istore_2
    //   38: aload #6
    //   40: instanceof android/view/ViewGroup
    //   43: ifeq -> 69
    //   46: aload #6
    //   48: aload_0
    //   49: if_acmpne -> 57
    //   52: iconst_1
    //   53: istore_2
    //   54: goto -> 69
    //   57: aload #6
    //   59: invokeinterface getParent : ()Landroid/view/ViewParent;
    //   64: astore #6
    //   66: goto -> 36
    //   69: aload #7
    //   71: astore #6
    //   73: iload_2
    //   74: ifne -> 193
    //   77: new java/lang/StringBuilder
    //   80: dup
    //   81: invokespecial <init> : ()V
    //   84: astore #8
    //   86: aload #8
    //   88: aload #7
    //   90: invokevirtual getClass : ()Ljava/lang/Class;
    //   93: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   96: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: aload #7
    //   102: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   105: astore #6
    //   107: aload #6
    //   109: instanceof android/view/ViewGroup
    //   112: ifeq -> 150
    //   115: aload #8
    //   117: ldc_w ' => '
    //   120: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   123: pop
    //   124: aload #8
    //   126: aload #6
    //   128: invokevirtual getClass : ()Ljava/lang/Class;
    //   131: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   134: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: pop
    //   138: aload #6
    //   140: invokeinterface getParent : ()Landroid/view/ViewParent;
    //   145: astore #6
    //   147: goto -> 107
    //   150: new java/lang/StringBuilder
    //   153: dup
    //   154: invokespecial <init> : ()V
    //   157: astore #6
    //   159: aload #6
    //   161: ldc_w 'arrowScroll tried to find focus based on non-child current focused view '
    //   164: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   167: pop
    //   168: aload #6
    //   170: aload #8
    //   172: invokevirtual toString : ()Ljava/lang/String;
    //   175: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   178: pop
    //   179: ldc 'ViewPager'
    //   181: aload #6
    //   183: invokevirtual toString : ()Ljava/lang/String;
    //   186: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   189: pop
    //   190: aconst_null
    //   191: astore #6
    //   193: iconst_0
    //   194: istore #5
    //   196: iconst_0
    //   197: istore #4
    //   199: invokestatic getInstance : ()Landroid/view/FocusFinder;
    //   202: aload_0
    //   203: aload #6
    //   205: iload_1
    //   206: invokevirtual findNextFocus : (Landroid/view/ViewGroup;Landroid/view/View;I)Landroid/view/View;
    //   209: astore #7
    //   211: aload #7
    //   213: ifnull -> 349
    //   216: aload #7
    //   218: aload #6
    //   220: if_acmpeq -> 349
    //   223: iload_1
    //   224: bipush #17
    //   226: if_icmpne -> 286
    //   229: aload_0
    //   230: aload_0
    //   231: getfield mTempRect : Landroid/graphics/Rect;
    //   234: aload #7
    //   236: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   239: getfield left : I
    //   242: istore_2
    //   243: aload_0
    //   244: aload_0
    //   245: getfield mTempRect : Landroid/graphics/Rect;
    //   248: aload #6
    //   250: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   253: getfield left : I
    //   256: istore_3
    //   257: aload #6
    //   259: ifnull -> 276
    //   262: iload_2
    //   263: iload_3
    //   264: if_icmplt -> 276
    //   267: aload_0
    //   268: invokevirtual pageLeft : ()Z
    //   271: istore #4
    //   273: goto -> 283
    //   276: aload #7
    //   278: invokevirtual requestFocus : ()Z
    //   281: istore #4
    //   283: goto -> 393
    //   286: iload_1
    //   287: bipush #66
    //   289: if_icmpne -> 283
    //   292: aload_0
    //   293: aload_0
    //   294: getfield mTempRect : Landroid/graphics/Rect;
    //   297: aload #7
    //   299: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   302: getfield left : I
    //   305: istore_2
    //   306: aload_0
    //   307: aload_0
    //   308: getfield mTempRect : Landroid/graphics/Rect;
    //   311: aload #6
    //   313: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   316: getfield left : I
    //   319: istore_3
    //   320: aload #6
    //   322: ifnull -> 339
    //   325: iload_2
    //   326: iload_3
    //   327: if_icmpgt -> 339
    //   330: aload_0
    //   331: invokevirtual pageRight : ()Z
    //   334: istore #4
    //   336: goto -> 346
    //   339: aload #7
    //   341: invokevirtual requestFocus : ()Z
    //   344: istore #4
    //   346: goto -> 393
    //   349: iload_1
    //   350: bipush #17
    //   352: if_icmpeq -> 387
    //   355: iload_1
    //   356: iconst_1
    //   357: if_icmpne -> 363
    //   360: goto -> 387
    //   363: iload_1
    //   364: bipush #66
    //   366: if_icmpeq -> 378
    //   369: iload #5
    //   371: istore #4
    //   373: iload_1
    //   374: iconst_2
    //   375: if_icmpne -> 393
    //   378: aload_0
    //   379: invokevirtual pageRight : ()Z
    //   382: istore #4
    //   384: goto -> 393
    //   387: aload_0
    //   388: invokevirtual pageLeft : ()Z
    //   391: istore #4
    //   393: iload #4
    //   395: ifeq -> 406
    //   398: aload_0
    //   399: iload_1
    //   400: invokestatic getContantForFocusDirection : (I)I
    //   403: invokevirtual playSoundEffect : (I)V
    //   406: iload #4
    //   408: ireturn }
  
  public boolean beginFakeDrag() {
    if (this.mIsBeingDragged)
      return false; 
    this.mFakeDragging = true;
    setScrollState(1);
    this.mLastMotionX = 0.0F;
    this.mInitialMotionX = 0.0F;
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker == null) {
      this.mVelocityTracker = VelocityTracker.obtain();
    } else {
      velocityTracker.clear();
    } 
    long l = SystemClock.uptimeMillis();
    MotionEvent motionEvent = MotionEvent.obtain(l, l, 0, 0.0F, 0.0F, 0);
    this.mVelocityTracker.addMovement(motionEvent);
    motionEvent.recycle();
    this.mFakeDragBeginTime = l;
    return true;
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
    return (paramBoolean && paramView.canScrollHorizontally(-paramInt1));
  }
  
  public boolean canScrollHorizontally(int paramInt) {
    PagerAdapter pagerAdapter = this.mAdapter;
    boolean bool1 = false;
    boolean bool = false;
    if (pagerAdapter == null)
      return false; 
    int i = getClientWidth();
    int j = getScrollX();
    if (paramInt < 0) {
      if (j > (int)(i * this.mFirstOffset))
        bool = true; 
      return bool;
    } 
    if (paramInt > 0) {
      bool = bool1;
      if (j < (int)(i * this.mLastOffset))
        bool = true; 
      return bool;
    } 
    return false;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams)); }
  
  public void clearOnPageChangeListeners() {
    List list = this.mOnPageChangeListeners;
    if (list != null)
      list.clear(); 
  }
  
  public void computeScroll() {
    this.mIsScrollStarted = true;
    if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
      int i = getScrollX();
      int j = getScrollY();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      if (i != k || j != m) {
        scrollTo(k, m);
        if (!pageScrolled(k)) {
          this.mScroller.abortAnimation();
          scrollTo(0, m);
        } 
      } 
      ViewCompat.postInvalidateOnAnimation(this);
      return;
    } 
    completeScroll(true);
  }
  
  void dataSetChanged() {
    byte b1;
    int k = this.mAdapter.getCount();
    this.mExpectedAdapterCount = k;
    if (this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < k) {
      b1 = 1;
    } else {
      b1 = 0;
    } 
    int i = this.mCurItem;
    int j = 0;
    byte b2 = 0;
    while (b2 < this.mItems.size()) {
      byte b4;
      byte b3;
      int m;
      ItemInfo itemInfo = (ItemInfo)this.mItems.get(b2);
      int n = this.mAdapter.getItemPosition(itemInfo.object);
      if (n == -1) {
        m = i;
        b3 = j;
        b4 = b2;
      } else if (n == -2) {
        this.mItems.remove(b2);
        n = b2 - 1;
        b2 = j;
        if (j == 0) {
          this.mAdapter.startUpdate(this);
          b2 = 1;
        } 
        this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
        b1 = 1;
        m = i;
        b3 = b2;
        b4 = n;
        if (this.mCurItem == itemInfo.position) {
          m = Math.max(0, Math.min(this.mCurItem, k - 1));
          b1 = 1;
          b3 = b2;
          b4 = n;
        } 
      } else {
        m = i;
        b3 = j;
        b4 = b2;
        if (itemInfo.position != n) {
          if (itemInfo.position == this.mCurItem)
            i = n; 
          itemInfo.position = n;
          b1 = 1;
          b4 = b2;
          b3 = j;
          m = i;
        } 
      } 
      b2 = b4 + 1;
      i = m;
      j = b3;
    } 
    if (j != 0)
      this.mAdapter.finishUpdate(this); 
    Collections.sort(this.mItems, COMPARATOR);
    if (b1) {
      j = getChildCount();
      for (b1 = 0; b1 < j; b1++) {
        LayoutParams layoutParams = (LayoutParams)getChildAt(b1).getLayoutParams();
        if (!layoutParams.isDecor)
          layoutParams.widthFactor = 0.0F; 
      } 
      setCurrentItemInternal(i, false, true);
      requestLayout();
    } 
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) { return (super.dispatchKeyEvent(paramKeyEvent) || executeKeyEvent(paramKeyEvent)); }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    if (paramAccessibilityEvent.getEventType() == 4096)
      return super.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent); 
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() == 0) {
        ItemInfo itemInfo = infoForChild(view);
        if (itemInfo != null && itemInfo.position == this.mCurItem && view.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent))
          return true; 
      } 
    } 
    return false;
  }
  
  float distanceInfluenceForSnapDuration(float paramFloat) { return (float)Math.sin(((paramFloat - 0.5F) * 0.47123894F)); }
  
  public void draw(Canvas paramCanvas) { // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial draw : (Landroid/graphics/Canvas;)V
    //   5: iconst_0
    //   6: istore_3
    //   7: iconst_0
    //   8: istore_2
    //   9: aload_0
    //   10: invokevirtual getOverScrollMode : ()I
    //   13: istore #4
    //   15: iload #4
    //   17: ifeq -> 66
    //   20: iload #4
    //   22: iconst_1
    //   23: if_icmpne -> 49
    //   26: aload_0
    //   27: getfield mAdapter : Landroid/support/v4/view/PagerAdapter;
    //   30: astore #8
    //   32: aload #8
    //   34: ifnull -> 49
    //   37: aload #8
    //   39: invokevirtual getCount : ()I
    //   42: iconst_1
    //   43: if_icmple -> 49
    //   46: goto -> 66
    //   49: aload_0
    //   50: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   53: invokevirtual finish : ()V
    //   56: aload_0
    //   57: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   60: invokevirtual finish : ()V
    //   63: goto -> 256
    //   66: aload_0
    //   67: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   70: invokevirtual isFinished : ()Z
    //   73: ifne -> 155
    //   76: aload_1
    //   77: invokevirtual save : ()I
    //   80: istore_3
    //   81: aload_0
    //   82: invokevirtual getHeight : ()I
    //   85: aload_0
    //   86: invokevirtual getPaddingTop : ()I
    //   89: isub
    //   90: aload_0
    //   91: invokevirtual getPaddingBottom : ()I
    //   94: isub
    //   95: istore_2
    //   96: aload_0
    //   97: invokevirtual getWidth : ()I
    //   100: istore #4
    //   102: aload_1
    //   103: ldc_w 270.0
    //   106: invokevirtual rotate : (F)V
    //   109: aload_1
    //   110: iload_2
    //   111: ineg
    //   112: aload_0
    //   113: invokevirtual getPaddingTop : ()I
    //   116: iadd
    //   117: i2f
    //   118: aload_0
    //   119: getfield mFirstOffset : F
    //   122: iload #4
    //   124: i2f
    //   125: fmul
    //   126: invokevirtual translate : (FF)V
    //   129: aload_0
    //   130: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   133: iload_2
    //   134: iload #4
    //   136: invokevirtual setSize : (II)V
    //   139: iconst_0
    //   140: aload_0
    //   141: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   144: aload_1
    //   145: invokevirtual draw : (Landroid/graphics/Canvas;)Z
    //   148: ior
    //   149: istore_2
    //   150: aload_1
    //   151: iload_3
    //   152: invokevirtual restoreToCount : (I)V
    //   155: iload_2
    //   156: istore_3
    //   157: aload_0
    //   158: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   161: invokevirtual isFinished : ()Z
    //   164: ifne -> 256
    //   167: aload_1
    //   168: invokevirtual save : ()I
    //   171: istore #4
    //   173: aload_0
    //   174: invokevirtual getWidth : ()I
    //   177: istore_3
    //   178: aload_0
    //   179: invokevirtual getHeight : ()I
    //   182: istore #5
    //   184: aload_0
    //   185: invokevirtual getPaddingTop : ()I
    //   188: istore #6
    //   190: aload_0
    //   191: invokevirtual getPaddingBottom : ()I
    //   194: istore #7
    //   196: aload_1
    //   197: ldc_w 90.0
    //   200: invokevirtual rotate : (F)V
    //   203: aload_1
    //   204: aload_0
    //   205: invokevirtual getPaddingTop : ()I
    //   208: ineg
    //   209: i2f
    //   210: aload_0
    //   211: getfield mLastOffset : F
    //   214: fconst_1
    //   215: fadd
    //   216: fneg
    //   217: iload_3
    //   218: i2f
    //   219: fmul
    //   220: invokevirtual translate : (FF)V
    //   223: aload_0
    //   224: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   227: iload #5
    //   229: iload #6
    //   231: isub
    //   232: iload #7
    //   234: isub
    //   235: iload_3
    //   236: invokevirtual setSize : (II)V
    //   239: iload_2
    //   240: aload_0
    //   241: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   244: aload_1
    //   245: invokevirtual draw : (Landroid/graphics/Canvas;)Z
    //   248: ior
    //   249: istore_3
    //   250: aload_1
    //   251: iload #4
    //   253: invokevirtual restoreToCount : (I)V
    //   256: iload_3
    //   257: ifeq -> 264
    //   260: aload_0
    //   261: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
    //   264: return }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    Drawable drawable = this.mMarginDrawable;
    if (drawable != null && drawable.isStateful())
      drawable.setState(getDrawableState()); 
  }
  
  public void endFakeDrag() {
    if (this.mFakeDragging) {
      if (this.mAdapter != null) {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        int i = (int)velocityTracker.getXVelocity(this.mActivePointerId);
        this.mPopulatePending = true;
        int j = getClientWidth();
        int k = getScrollX();
        ItemInfo itemInfo = infoForCurrentScrollPosition();
        setCurrentItemInternal(determineTargetPage(itemInfo.position, (k / j - itemInfo.offset) / itemInfo.widthFactor, i, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, i);
      } 
      endDrag();
      this.mFakeDragging = false;
      return;
    } 
    throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
  }
  
  public boolean executeKeyEvent(@NonNull KeyEvent paramKeyEvent) {
    byte b = 0;
    boolean bool = b;
    if (paramKeyEvent.getAction() == 0) {
      int i = paramKeyEvent.getKeyCode();
      if (i != 61) {
        switch (i) {
          default:
            return false;
          case 22:
            return paramKeyEvent.hasModifiers(2) ? pageRight() : arrowScroll(66);
          case 21:
            break;
        } 
        return paramKeyEvent.hasModifiers(2) ? pageLeft() : arrowScroll(17);
      } 
      if (paramKeyEvent.hasNoModifiers())
        return arrowScroll(2); 
      bool = b;
      if (paramKeyEvent.hasModifiers(1))
        bool = arrowScroll(1); 
    } 
    return bool;
  }
  
  public void fakeDragBy(float paramFloat) {
    if (this.mFakeDragging) {
      if (this.mAdapter == null)
        return; 
      this.mLastMotionX += paramFloat;
      float f2 = getScrollX() - paramFloat;
      int i = getClientWidth();
      paramFloat = i * this.mFirstOffset;
      float f1 = i * this.mLastOffset;
      ItemInfo itemInfo1 = (ItemInfo)this.mItems.get(0);
      ArrayList arrayList = this.mItems;
      ItemInfo itemInfo2 = (ItemInfo)arrayList.get(arrayList.size() - 1);
      if (itemInfo1.position != 0)
        paramFloat = itemInfo1.offset * i; 
      if (itemInfo2.position != this.mAdapter.getCount() - 1)
        f1 = itemInfo2.offset * i; 
      if (f2 >= paramFloat) {
        paramFloat = f2;
        if (f2 > f1)
          paramFloat = f1; 
      } 
      this.mLastMotionX += paramFloat - (int)paramFloat;
      scrollTo((int)paramFloat, getScrollY());
      pageScrolled((int)paramFloat);
      long l = SystemClock.uptimeMillis();
      MotionEvent motionEvent = MotionEvent.obtain(this.mFakeDragBeginTime, l, 2, this.mLastMotionX, 0.0F, 0);
      this.mVelocityTracker.addMovement(motionEvent);
      motionEvent.recycle();
      return;
    } 
    throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() { return new LayoutParams(); }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) { return new LayoutParams(getContext(), paramAttributeSet); }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return generateDefaultLayoutParams(); }
  
  @Nullable
  public PagerAdapter getAdapter() { return this.mAdapter; }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2) {
    if (this.mDrawingOrder == 2)
      paramInt2 = paramInt1 - 1 - paramInt2; 
    return ((LayoutParams)((View)this.mDrawingOrderedChildren.get(paramInt2)).getLayoutParams()).childIndex;
  }
  
  public int getCurrentItem() { return this.mCurItem; }
  
  public int getOffscreenPageLimit() { return this.mOffscreenPageLimit; }
  
  public int getPageMargin() { return this.mPageMargin; }
  
  ItemInfo infoForAnyChild(View paramView) {
    while (true) {
      ViewParent viewParent = paramView.getParent();
      if (viewParent != this) {
        if (viewParent == null || !(viewParent instanceof View))
          return null; 
        paramView = (View)viewParent;
        continue;
      } 
      break;
    } 
    return infoForChild(paramView);
  }
  
  ItemInfo infoForChild(View paramView) {
    for (byte b = 0; b < this.mItems.size(); b++) {
      ItemInfo itemInfo = (ItemInfo)this.mItems.get(b);
      if (this.mAdapter.isViewFromObject(paramView, itemInfo.object))
        return itemInfo; 
    } 
    return null;
  }
  
  ItemInfo infoForPosition(int paramInt) {
    for (byte b = 0; b < this.mItems.size(); b++) {
      ItemInfo itemInfo = (ItemInfo)this.mItems.get(b);
      if (itemInfo.position == paramInt)
        return itemInfo; 
    } 
    return null;
  }
  
  void initViewPager() {
    setWillNotDraw(false);
    setDescendantFocusability(262144);
    setFocusable(true);
    Context context = getContext();
    this.mScroller = new Scroller(context, sInterpolator);
    ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
    float f = (context.getResources().getDisplayMetrics()).density;
    this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
    this.mMinimumVelocity = (int)(400.0F * f);
    this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    this.mLeftEdge = new EdgeEffect(context);
    this.mRightEdge = new EdgeEffect(context);
    this.mFlingDistance = (int)(25.0F * f);
    this.mCloseEnough = (int)(2.0F * f);
    this.mDefaultGutterSize = (int)(16.0F * f);
    ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());
    if (ViewCompat.getImportantForAccessibility(this) == 0)
      ViewCompat.setImportantForAccessibility(this, 1); 
    ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
          private final Rect mTempRect = new Rect();
          
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
            WindowInsetsCompat windowInsetsCompat = ViewCompat.onApplyWindowInsets(param1View, param1WindowInsetsCompat);
            if (windowInsetsCompat.isConsumed())
              return windowInsetsCompat; 
            Rect rect = this.mTempRect;
            rect.left = windowInsetsCompat.getSystemWindowInsetLeft();
            rect.top = windowInsetsCompat.getSystemWindowInsetTop();
            rect.right = windowInsetsCompat.getSystemWindowInsetRight();
            rect.bottom = windowInsetsCompat.getSystemWindowInsetBottom();
            byte b = 0;
            int i = ViewPager.this.getChildCount();
            while (b < i) {
              WindowInsetsCompat windowInsetsCompat1 = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(b), windowInsetsCompat);
              rect.left = Math.min(windowInsetsCompat1.getSystemWindowInsetLeft(), rect.left);
              rect.top = Math.min(windowInsetsCompat1.getSystemWindowInsetTop(), rect.top);
              rect.right = Math.min(windowInsetsCompat1.getSystemWindowInsetRight(), rect.right);
              rect.bottom = Math.min(windowInsetsCompat1.getSystemWindowInsetBottom(), rect.bottom);
              b++;
            } 
            return windowInsetsCompat.replaceSystemWindowInsets(rect.left, rect.top, rect.right, rect.bottom);
          }
        });
  }
  
  public boolean isFakeDragging() { return this.mFakeDragging; }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow() {
    removeCallbacks(this.mEndScrollRunnable);
    Scroller scroller = this.mScroller;
    if (scroller != null && !scroller.isFinished())
      this.mScroller.abortAnimation(); 
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
      int j = getScrollX();
      int k = getWidth();
      float f2 = this.mPageMargin / k;
      byte b = 0;
      ItemInfo itemInfo = (ItemInfo)this.mItems.get(0);
      float f1 = itemInfo.offset;
      int m = this.mItems.size();
      int i = itemInfo.position;
      int n = ((ItemInfo)this.mItems.get(m - 1)).position;
      while (i < n) {
        ItemInfo itemInfo1;
        float f;
        while (i > itemInfo.position && b < m) {
          ArrayList arrayList = this.mItems;
          itemInfo1 = (ItemInfo)arrayList.get(++b);
        } 
        if (i == itemInfo1.position) {
          f = (itemInfo1.offset + itemInfo1.widthFactor) * k;
          f1 = itemInfo1.offset + itemInfo1.widthFactor + f2;
        } else {
          f = this.mAdapter.getPageWidth(i);
          float f4 = k;
          float f3 = f1 + f + f2;
          f = (f1 + f) * f4;
          f1 = f3;
        } 
        if (this.mPageMargin + f > j) {
          this.mMarginDrawable.setBounds(Math.round(f), this.mTopPageBounds, Math.round(this.mPageMargin + f), this.mBottomPageBounds);
          this.mMarginDrawable.draw(paramCanvas);
        } 
        if (f > (j + k))
          return; 
        i++;
      } 
      return;
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getAction() & 0xFF;
    if (i == 3 || i == 1) {
      resetTouch();
      return false;
    } 
    if (i != 0) {
      if (this.mIsBeingDragged)
        return true; 
      if (this.mIsUnableToDrag)
        return false; 
    } 
    if (i != 0) {
      if (i != 2) {
        if (i == 6)
          onSecondaryPointerUp(paramMotionEvent); 
      } else {
        i = this.mActivePointerId;
        if (i != -1) {
          i = paramMotionEvent.findPointerIndex(i);
          float f2 = paramMotionEvent.getX(i);
          float f1 = f2 - this.mLastMotionX;
          float f4 = Math.abs(f1);
          float f3 = paramMotionEvent.getY(i);
          float f5 = Math.abs(f3 - this.mInitialMotionY);
          if (f1 != 0.0F && !isGutterDrag(this.mLastMotionX, f1) && canScroll(this, false, (int)f1, (int)f2, (int)f3)) {
            this.mLastMotionX = f2;
            this.mLastMotionY = f3;
            this.mIsUnableToDrag = true;
            return false;
          } 
          if (f4 > this.mTouchSlop && 0.5F * f4 > f5) {
            this.mIsBeingDragged = true;
            requestParentDisallowInterceptTouchEvent(true);
            setScrollState(1);
            if (f1 > 0.0F) {
              f1 = this.mInitialMotionX + this.mTouchSlop;
            } else {
              f1 = this.mInitialMotionX - this.mTouchSlop;
            } 
            this.mLastMotionX = f1;
            this.mLastMotionY = f3;
            setScrollingCacheEnabled(true);
          } else if (f5 > this.mTouchSlop) {
            this.mIsUnableToDrag = true;
          } 
          if (this.mIsBeingDragged && performDrag(f2))
            ViewCompat.postInvalidateOnAnimation(this); 
        } 
      } 
    } else {
      float f = paramMotionEvent.getX();
      this.mInitialMotionX = f;
      this.mLastMotionX = f;
      f = paramMotionEvent.getY();
      this.mInitialMotionY = f;
      this.mLastMotionY = f;
      this.mActivePointerId = paramMotionEvent.getPointerId(0);
      this.mIsUnableToDrag = false;
      this.mIsScrollStarted = true;
      this.mScroller.computeScrollOffset();
      if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
        this.mScroller.abortAnimation();
        this.mPopulatePending = false;
        populate();
        this.mIsBeingDragged = true;
        requestParentDisallowInterceptTouchEvent(true);
        setScrollState(1);
      } else {
        completeScroll(false);
        this.mIsBeingDragged = false;
      } 
    } 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    return this.mIsBeingDragged;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int k = getChildCount();
    int m = paramInt3 - paramInt1;
    int n = paramInt4 - paramInt2;
    paramInt1 = getPaddingLeft();
    paramInt2 = getPaddingTop();
    int i = getPaddingRight();
    paramInt4 = getPaddingBottom();
    int i1 = getScrollX();
    byte b2 = 0;
    int j = 0;
    while (j < k) {
      View view = getChildAt(j);
      paramInt3 = paramInt1;
      int i2 = paramInt2;
      int i3 = i;
      int i4 = paramInt4;
      byte b = b2;
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.isDecor) {
          paramInt3 = layoutParams.gravity & 0x7;
          i3 = layoutParams.gravity & 0x70;
          if (paramInt3 != 1) {
            if (paramInt3 != 3) {
              if (paramInt3 != 5) {
                paramInt3 = paramInt1;
                i2 = paramInt1;
              } else {
                paramInt3 = m - i - view.getMeasuredWidth();
                i += view.getMeasuredWidth();
                i2 = paramInt1;
              } 
            } else {
              paramInt3 = paramInt1;
              i2 = paramInt1 + view.getMeasuredWidth();
            } 
          } else {
            paramInt3 = Math.max((m - view.getMeasuredWidth()) / 2, paramInt1);
            i2 = paramInt1;
          } 
          if (i3 != 16) {
            if (i3 != 48) {
              if (i3 != 80) {
                paramInt1 = paramInt2;
              } else {
                paramInt1 = n - paramInt4 - view.getMeasuredHeight();
                paramInt4 += view.getMeasuredHeight();
              } 
            } else {
              paramInt1 = paramInt2;
              paramInt2 += view.getMeasuredHeight();
            } 
          } else {
            paramInt1 = Math.max((n - view.getMeasuredHeight()) / 2, paramInt2);
          } 
          paramInt3 += i1;
          view.layout(paramInt3, paramInt1, view.getMeasuredWidth() + paramInt3, paramInt1 + view.getMeasuredHeight());
          b = b2 + true;
          paramInt3 = i2;
          i2 = paramInt2;
          i3 = i;
          i4 = paramInt4;
        } else {
          b = b2;
          i4 = paramInt4;
          i3 = i;
          i2 = paramInt2;
          paramInt3 = paramInt1;
        } 
      } 
      j++;
      paramInt1 = paramInt3;
      paramInt2 = i2;
      i = i3;
      paramInt4 = i4;
      b2 = b;
    } 
    j = m - paramInt1 - i;
    byte b1 = 0;
    paramInt3 = m;
    i = k;
    while (b1 < i) {
      View view = getChildAt(b1);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.isDecor) {
          ItemInfo itemInfo = infoForChild(view);
          if (itemInfo != null) {
            int i2 = paramInt1 + (int)(j * itemInfo.offset);
            if (layoutParams.needsMeasure) {
              layoutParams.needsMeasure = false;
              view.measure(View.MeasureSpec.makeMeasureSpec((int)(j * layoutParams.widthFactor), 1073741824), View.MeasureSpec.makeMeasureSpec(n - paramInt2 - paramInt4, 1073741824));
            } 
            view.layout(i2, paramInt2, view.getMeasuredWidth() + i2, view.getMeasuredHeight() + paramInt2);
          } 
        } 
      } 
      b1++;
    } 
    this.mTopPageBounds = paramInt2;
    this.mBottomPageBounds = n - paramInt4;
    this.mDecorChildCount = b2;
    if (this.mFirstLayout)
      scrollToItem(this.mCurItem, false, 0, false); 
    this.mFirstLayout = false;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    setMeasuredDimension(getDefaultSize(0, paramInt1), getDefaultSize(0, paramInt2));
    int j = getMeasuredWidth();
    int k = j / 10;
    this.mGutterSize = Math.min(k, this.mDefaultGutterSize);
    paramInt1 = j - getPaddingLeft() - getPaddingRight();
    paramInt2 = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    int m = getChildCount();
    byte b = 0;
    while (b < m) {
      int i1;
      int n;
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams != null && layoutParams.isDecor) {
          int i4;
          boolean bool;
          n = layoutParams.gravity & 0x7;
          int i3 = layoutParams.gravity & 0x70;
          int i2 = Integer.MIN_VALUE;
          i1 = Integer.MIN_VALUE;
          if (i3 == 48 || i3 == 80) {
            i3 = 1;
          } else {
            i3 = 0;
          } 
          if (n == 3 || n == 5) {
            bool = true;
          } else {
            bool = false;
          } 
          if (i3 != 0) {
            n = 1073741824;
          } else {
            n = i2;
            if (bool) {
              i1 = 1073741824;
              n = i2;
            } 
          } 
          if (layoutParams.width != -2) {
            i4 = 1073741824;
            if (layoutParams.width != -1) {
              n = layoutParams.width;
            } else {
              n = paramInt1;
            } 
          } else {
            i2 = paramInt1;
            i4 = n;
            n = i2;
          } 
          if (layoutParams.height != -2) {
            if (layoutParams.height != -1) {
              i1 = layoutParams.height;
              i2 = 1073741824;
            } else {
              i2 = 1073741824;
              i1 = paramInt2;
            } 
          } else {
            i2 = i1;
            i1 = paramInt2;
          } 
          view.measure(View.MeasureSpec.makeMeasureSpec(n, i4), View.MeasureSpec.makeMeasureSpec(i1, i2));
          if (i3 != 0) {
            i1 = paramInt2 - view.getMeasuredHeight();
            n = paramInt1;
          } else {
            n = paramInt1;
            i1 = paramInt2;
            if (bool) {
              n = paramInt1 - view.getMeasuredWidth();
              i1 = paramInt2;
            } 
          } 
        } else {
          n = paramInt1;
          i1 = paramInt2;
        } 
      } else {
        i1 = paramInt2;
        n = paramInt1;
      } 
      b++;
      paramInt1 = n;
      paramInt2 = i1;
    } 
    this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
    this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
    this.mInLayout = true;
    populate();
    this.mInLayout = false;
    int i = getChildCount();
    for (paramInt2 = 0; paramInt2 < i; paramInt2++) {
      View view = getChildAt(paramInt2);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams == null || !layoutParams.isDecor)
          view.measure(View.MeasureSpec.makeMeasureSpec((int)(paramInt1 * layoutParams.widthFactor), 1073741824), this.mChildHeightMeasureSpec); 
      } 
    } 
  }
  
  @CallSuper
  protected void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    if (this.mDecorChildCount > 0) {
      int k = getScrollX();
      int i = getPaddingLeft();
      int j = getPaddingRight();
      int m = getWidth();
      int n = getChildCount();
      byte b = 0;
      while (b < n) {
        int i2;
        int i1;
        View view = getChildAt(b);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.isDecor) {
          i1 = i;
          i2 = j;
        } else {
          i1 = layoutParams.gravity & 0x7;
          if (i1 != 1) {
            if (i1 != 3) {
              if (i1 != 5) {
                i1 = i;
              } else {
                i1 = m - j - view.getMeasuredWidth();
                j += view.getMeasuredWidth();
              } 
            } else {
              i1 = i;
              i += view.getWidth();
            } 
          } else {
            i1 = Math.max((m - view.getMeasuredWidth()) / 2, i);
          } 
          int i3 = i1 + k - view.getLeft();
          i1 = i;
          i2 = j;
          if (i3 != 0) {
            view.offsetLeftAndRight(i3);
            i2 = j;
            i1 = i;
          } 
        } 
        b++;
        i = i1;
        j = i2;
      } 
    } 
    dispatchOnPageScrolled(paramInt1, paramFloat, paramInt2);
    if (this.mPageTransformer != null) {
      paramInt2 = getScrollX();
      int i = getChildCount();
      for (paramInt1 = 0; paramInt1 < i; paramInt1++) {
        View view = getChildAt(paramInt1);
        if (!((LayoutParams)view.getLayoutParams()).isDecor) {
          paramFloat = (view.getLeft() - paramInt2) / getClientWidth();
          this.mPageTransformer.transformPage(view, paramFloat);
        } 
      } 
    } 
    this.mCalledSuper = true;
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect) {
    int j;
    int i;
    int k = getChildCount();
    if ((paramInt & 0x2) != 0) {
      i = 0;
      j = 1;
    } else {
      i = k - 1;
      j = -1;
      k = -1;
    } 
    while (i != k) {
      View view = getChildAt(i);
      if (view.getVisibility() == 0) {
        ItemInfo itemInfo = infoForChild(view);
        if (itemInfo != null && itemInfo.position == this.mCurItem && view.requestFocus(paramInt, paramRect))
          return true; 
      } 
      i += j;
    } 
    return false;
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null) {
      pagerAdapter.restoreState(paramParcelable.adapterState, paramParcelable.loader);
      setCurrentItemInternal(paramParcelable.position, false, true);
      return;
    } 
    this.mRestoredCurItem = paramParcelable.position;
    this.mRestoredAdapterState = paramParcelable.adapterState;
    this.mRestoredClassLoader = paramParcelable.loader;
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.position = this.mCurItem;
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null)
      savedState.adapterState = pagerAdapter.saveState(); 
    return savedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3) {
      paramInt2 = this.mPageMargin;
      recomputeScrollPosition(paramInt1, paramInt3, paramInt2, paramInt2);
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (this.mFakeDragging)
      return true; 
    if (paramMotionEvent.getAction() == 0 && paramMotionEvent.getEdgeFlags() != 0)
      return false; 
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null) {
      float f;
      if (pagerAdapter.getCount() == 0)
        return false; 
      if (this.mVelocityTracker == null)
        this.mVelocityTracker = VelocityTracker.obtain(); 
      this.mVelocityTracker.addMovement(paramMotionEvent);
      int i = paramMotionEvent.getAction();
      boolean bool = false;
      switch (i & 0xFF) {
        case 6:
          onSecondaryPointerUp(paramMotionEvent);
          this.mLastMotionX = paramMotionEvent.getX(paramMotionEvent.findPointerIndex(this.mActivePointerId));
          break;
        case 5:
          i = paramMotionEvent.getActionIndex();
          this.mLastMotionX = paramMotionEvent.getX(i);
          this.mActivePointerId = paramMotionEvent.getPointerId(i);
          break;
        case 3:
          if (this.mIsBeingDragged) {
            scrollToItem(this.mCurItem, true, 0, false);
            bool = resetTouch();
          } 
          break;
        case 2:
          if (!this.mIsBeingDragged) {
            i = paramMotionEvent.findPointerIndex(this.mActivePointerId);
            if (i == -1) {
              bool = resetTouch();
              break;
            } 
            float f1 = paramMotionEvent.getX(i);
            float f3 = Math.abs(f1 - this.mLastMotionX);
            float f2 = paramMotionEvent.getY(i);
            float f4 = Math.abs(f2 - this.mLastMotionY);
            if (f3 > this.mTouchSlop && f3 > f4) {
              this.mIsBeingDragged = true;
              requestParentDisallowInterceptTouchEvent(true);
              f3 = this.mInitialMotionX;
              if (f1 - f3 > 0.0F) {
                f1 = f3 + this.mTouchSlop;
              } else {
                f1 = f3 - this.mTouchSlop;
              } 
              this.mLastMotionX = f1;
              this.mLastMotionY = f2;
              setScrollState(1);
              setScrollingCacheEnabled(true);
              ViewParent viewParent = getParent();
              if (viewParent != null)
                viewParent.requestDisallowInterceptTouchEvent(true); 
            } 
          } 
          if (this.mIsBeingDragged)
            bool = false | performDrag(paramMotionEvent.getX(paramMotionEvent.findPointerIndex(this.mActivePointerId))); 
          break;
        case 1:
          if (this.mIsBeingDragged) {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
            i = (int)velocityTracker.getXVelocity(this.mActivePointerId);
            this.mPopulatePending = true;
            int j = getClientWidth();
            int k = getScrollX();
            ItemInfo itemInfo = infoForCurrentScrollPosition();
            float f1 = this.mPageMargin / j;
            setCurrentItemInternal(determineTargetPage(itemInfo.position, (k / j - itemInfo.offset) / (itemInfo.widthFactor + f1), i, (int)(paramMotionEvent.getX(paramMotionEvent.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, i);
            bool = resetTouch();
          } 
          break;
        case 0:
          this.mScroller.abortAnimation();
          this.mPopulatePending = false;
          populate();
          f = paramMotionEvent.getX();
          this.mInitialMotionX = f;
          this.mLastMotionX = f;
          f = paramMotionEvent.getY();
          this.mInitialMotionY = f;
          this.mLastMotionY = f;
          this.mActivePointerId = paramMotionEvent.getPointerId(0);
          break;
      } 
      if (bool)
        ViewCompat.postInvalidateOnAnimation(this); 
      return true;
    } 
    return false;
  }
  
  boolean pageLeft() {
    int i = this.mCurItem;
    if (i > 0) {
      setCurrentItem(i - 1, true);
      return true;
    } 
    return false;
  }
  
  boolean pageRight() {
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null && this.mCurItem < pagerAdapter.getCount() - 1) {
      setCurrentItem(this.mCurItem + 1, true);
      return true;
    } 
    return false;
  }
  
  void populate() { populate(this.mCurItem); }
  
  void populate(int paramInt) {
    ItemInfo itemInfo;
    String str;
    int i = this.mCurItem;
    if (i != paramInt) {
      itemInfo = infoForPosition(i);
      this.mCurItem = paramInt;
    } else {
      itemInfo = null;
    } 
    if (this.mAdapter == null) {
      sortChildDrawingOrder();
      return;
    } 
    if (this.mPopulatePending) {
      sortChildDrawingOrder();
      return;
    } 
    if (getWindowToken() == null)
      return; 
    this.mAdapter.startUpdate(this);
    int j = this.mOffscreenPageLimit;
    int k = Math.max(0, this.mCurItem - j);
    int m = this.mAdapter.getCount();
    int n = Math.min(m - 1, this.mCurItem + j);
    if (m == this.mExpectedAdapterCount) {
      ItemInfo itemInfo1 = null;
      paramInt = 0;
      while (true) {
        str = itemInfo1;
        if (paramInt < this.mItems.size()) {
          ItemInfo itemInfo2 = (ItemInfo)this.mItems.get(paramInt);
          if (itemInfo2.position >= this.mCurItem) {
            str = itemInfo1;
            if (itemInfo2.position == this.mCurItem)
              str = itemInfo2; 
            break;
          } 
          paramInt++;
          continue;
        } 
        break;
      } 
      itemInfo1 = str;
      if (str == null) {
        itemInfo1 = str;
        if (m > 0)
          itemInfo1 = addNewItem(this.mCurItem, paramInt); 
      } 
      if (itemInfo1 != null) {
        float f2;
        float f3 = 0.0F;
        int i3 = paramInt - 1;
        if (i3 >= 0) {
          str = (ItemInfo)this.mItems.get(i3);
        } else {
          str = null;
        } 
        int i4 = getClientWidth();
        if (i4 <= 0) {
          f2 = 0.0F;
        } else {
          float f = itemInfo1.widthFactor;
          f2 = getPaddingLeft() / i4 + 2.0F - f;
        } 
        int i2 = this.mCurItem - 1;
        ItemInfo itemInfo2 = str;
        int i1 = paramInt;
        while (i2 >= 0) {
          float f;
          if (f3 >= f2 && i2 < k) {
            if (itemInfo2 == null)
              break; 
            paramInt = i1;
            f = f3;
            i = i3;
            str = itemInfo2;
            if (i2 == itemInfo2.position) {
              paramInt = i1;
              f = f3;
              i = i3;
              str = itemInfo2;
              if (!itemInfo2.scrolling) {
                this.mItems.remove(i3);
                this.mAdapter.destroyItem(this, i2, itemInfo2.object);
                i = i3 - 1;
                paramInt = i1 - 1;
                if (i >= 0) {
                  str = (ItemInfo)this.mItems.get(i);
                } else {
                  str = null;
                } 
                f = f3;
              } 
            } 
          } else if (itemInfo2 != null && i2 == itemInfo2.position) {
            f = f3 + itemInfo2.widthFactor;
            i = i3 - 1;
            if (i >= 0) {
              str = (ItemInfo)this.mItems.get(i);
            } else {
              str = null;
            } 
            paramInt = i1;
          } else {
            f = f3 + (addNewItem(i2, i3 + 1)).widthFactor;
            paramInt = i1 + 1;
            if (i3 >= 0) {
              str = (ItemInfo)this.mItems.get(i3);
            } else {
              str = null;
            } 
            i = i3;
          } 
          i2--;
          i1 = paramInt;
          f3 = f;
          i3 = i;
          itemInfo2 = str;
        } 
        float f1 = itemInfo1.widthFactor;
        paramInt = i1 + 1;
        if (f1 < 2.0F) {
          if (paramInt < this.mItems.size()) {
            str = (ItemInfo)this.mItems.get(paramInt);
          } else {
            str = null;
          } 
          if (i4 <= 0) {
            f2 = 0.0F;
          } else {
            f2 = getPaddingRight() / i4 + 2.0F;
          } 
          i2 = this.mCurItem + 1;
          i = k;
          i3 = j;
          while (i2 < m) {
            if (f1 >= f2 && i2 > n) {
              if (str == null)
                break; 
              if (i2 == str.position && !str.scrolling) {
                this.mItems.remove(paramInt);
                this.mAdapter.destroyItem(this, i2, str.object);
                if (paramInt < this.mItems.size()) {
                  str = (ItemInfo)this.mItems.get(paramInt);
                } else {
                  str = null;
                } 
              } 
            } else if (str != null && i2 == str.position) {
              f1 += str.widthFactor;
              if (++paramInt < this.mItems.size()) {
                str = (ItemInfo)this.mItems.get(paramInt);
              } else {
                str = null;
              } 
            } else {
              str = addNewItem(i2, paramInt);
              paramInt++;
              f1 += str.widthFactor;
              if (paramInt < this.mItems.size()) {
                str = (ItemInfo)this.mItems.get(paramInt);
              } else {
                str = null;
              } 
            } 
            i2++;
          } 
        } 
        calculatePageOffsets(itemInfo1, i1, itemInfo);
        this.mAdapter.setPrimaryItem(this, this.mCurItem, itemInfo1.object);
      } 
      this.mAdapter.finishUpdate(this);
      i = getChildCount();
      for (paramInt = 0; paramInt < i; paramInt++) {
        View view = getChildAt(paramInt);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        layoutParams.childIndex = paramInt;
        if (!layoutParams.isDecor && layoutParams.widthFactor == 0.0F) {
          ItemInfo itemInfo2 = infoForChild(view);
          if (itemInfo2 != null) {
            layoutParams.widthFactor = itemInfo2.widthFactor;
            layoutParams.position = itemInfo2.position;
          } 
        } 
      } 
      sortChildDrawingOrder();
      if (hasFocus()) {
        View view = findFocus();
        if (view != null) {
          ItemInfo itemInfo2 = infoForAnyChild(view);
        } else {
          view = null;
        } 
        if (view == null || view.position != this.mCurItem)
          for (paramInt = 0; paramInt < getChildCount(); paramInt++) {
            view = getChildAt(paramInt);
            itemInfo = infoForChild(view);
            if (itemInfo != null && itemInfo.position == this.mCurItem && view.requestFocus(2))
              return; 
          }  
      } 
      return;
    } 
    try {
      str = getResources().getResourceName(getId());
    } catch (android.content.res.Resources.NotFoundException notFoundException) {
      str = Integer.toHexString(getId());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ");
    stringBuilder.append(this.mExpectedAdapterCount);
    stringBuilder.append(", found: ");
    stringBuilder.append(m);
    stringBuilder.append(" Pager id: ");
    stringBuilder.append(str);
    stringBuilder.append(" Pager class: ");
    stringBuilder.append(getClass());
    stringBuilder.append(" Problematic adapter: ");
    stringBuilder.append(this.mAdapter.getClass());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public void removeOnAdapterChangeListener(@NonNull OnAdapterChangeListener paramOnAdapterChangeListener) {
    List list = this.mAdapterChangeListeners;
    if (list != null)
      list.remove(paramOnAdapterChangeListener); 
  }
  
  public void removeOnPageChangeListener(@NonNull OnPageChangeListener paramOnPageChangeListener) {
    List list = this.mOnPageChangeListeners;
    if (list != null)
      list.remove(paramOnPageChangeListener); 
  }
  
  public void removeView(View paramView) {
    if (this.mInLayout) {
      removeViewInLayout(paramView);
      return;
    } 
    super.removeView(paramView);
  }
  
  public void setAdapter(@Nullable PagerAdapter paramPagerAdapter) {
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null) {
      pagerAdapter.setViewPagerObserver(null);
      this.mAdapter.startUpdate(this);
      for (byte b = 0; b < this.mItems.size(); b++) {
        ItemInfo itemInfo = (ItemInfo)this.mItems.get(b);
        this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
      } 
      this.mAdapter.finishUpdate(this);
      this.mItems.clear();
      removeNonDecorViews();
      this.mCurItem = 0;
      scrollTo(0, 0);
    } 
    pagerAdapter = this.mAdapter;
    this.mAdapter = paramPagerAdapter;
    this.mExpectedAdapterCount = 0;
    if (this.mAdapter != null) {
      if (this.mObserver == null)
        this.mObserver = new PagerObserver(); 
      this.mAdapter.setViewPagerObserver(this.mObserver);
      this.mPopulatePending = false;
      boolean bool = this.mFirstLayout;
      this.mFirstLayout = true;
      this.mExpectedAdapterCount = this.mAdapter.getCount();
      if (this.mRestoredCurItem >= 0) {
        this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
        setCurrentItemInternal(this.mRestoredCurItem, false, true);
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
      } else if (!bool) {
        populate();
      } else {
        requestLayout();
      } 
    } 
    List list = this.mAdapterChangeListeners;
    if (list != null && !list.isEmpty()) {
      byte b = 0;
      int i = this.mAdapterChangeListeners.size();
      while (b < i) {
        ((OnAdapterChangeListener)this.mAdapterChangeListeners.get(b)).onAdapterChanged(this, pagerAdapter, paramPagerAdapter);
        b++;
      } 
    } 
  }
  
  public void setCurrentItem(int paramInt) {
    this.mPopulatePending = false;
    setCurrentItemInternal(paramInt, this.mFirstLayout ^ true, false);
  }
  
  public void setCurrentItem(int paramInt, boolean paramBoolean) {
    this.mPopulatePending = false;
    setCurrentItemInternal(paramInt, paramBoolean, false);
  }
  
  void setCurrentItemInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2) { setCurrentItemInternal(paramInt, paramBoolean1, paramBoolean2, 0); }
  
  void setCurrentItemInternal(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2) {
    int i;
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter == null || pagerAdapter.getCount() <= 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    if (!paramBoolean2 && this.mCurItem == paramInt1 && this.mItems.size() != 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    paramBoolean2 = true;
    if (paramInt1 < 0) {
      i = 0;
    } else {
      i = paramInt1;
      if (paramInt1 >= this.mAdapter.getCount())
        i = this.mAdapter.getCount() - 1; 
    } 
    paramInt1 = this.mOffscreenPageLimit;
    int j = this.mCurItem;
    if (i > j + paramInt1 || i < j - paramInt1)
      for (paramInt1 = 0; paramInt1 < this.mItems.size(); paramInt1++)
        ((ItemInfo)this.mItems.get(paramInt1)).scrolling = true;  
    if (this.mCurItem == i)
      paramBoolean2 = false; 
    if (this.mFirstLayout) {
      this.mCurItem = i;
      if (paramBoolean2)
        dispatchOnPageSelected(i); 
      requestLayout();
      return;
    } 
    populate(i);
    scrollToItem(i, paramBoolean1, paramInt2, paramBoolean2);
  }
  
  OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    OnPageChangeListener onPageChangeListener = this.mInternalPageChangeListener;
    this.mInternalPageChangeListener = paramOnPageChangeListener;
    return onPageChangeListener;
  }
  
  public void setOffscreenPageLimit(int paramInt) {
    int i = paramInt;
    if (paramInt < 1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Requested offscreen page limit ");
      stringBuilder.append(paramInt);
      stringBuilder.append(" too small; defaulting to ");
      stringBuilder.append(1);
      Log.w("ViewPager", stringBuilder.toString());
      i = 1;
    } 
    if (i != this.mOffscreenPageLimit) {
      this.mOffscreenPageLimit = i;
      populate();
    } 
  }
  
  @Deprecated
  public void setOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) { this.mOnPageChangeListener = paramOnPageChangeListener; }
  
  public void setPageMargin(int paramInt) {
    int i = this.mPageMargin;
    this.mPageMargin = paramInt;
    int j = getWidth();
    recomputeScrollPosition(j, j, paramInt, i);
    requestLayout();
  }
  
  public void setPageMarginDrawable(@DrawableRes int paramInt) { setPageMarginDrawable(ContextCompat.getDrawable(getContext(), paramInt)); }
  
  public void setPageMarginDrawable(@Nullable Drawable paramDrawable) {
    boolean bool;
    this.mMarginDrawable = paramDrawable;
    if (paramDrawable != null)
      refreshDrawableState(); 
    if (paramDrawable == null) {
      bool = true;
    } else {
      bool = false;
    } 
    setWillNotDraw(bool);
    invalidate();
  }
  
  public void setPageTransformer(boolean paramBoolean, @Nullable PageTransformer paramPageTransformer) { setPageTransformer(paramBoolean, paramPageTransformer, 2); }
  
  public void setPageTransformer(boolean paramBoolean, @Nullable PageTransformer paramPageTransformer, int paramInt) {
    boolean bool2;
    boolean bool;
    boolean bool1;
    byte b = 1;
    if (paramPageTransformer != null) {
      bool = true;
    } else {
      bool = false;
    } 
    if (this.mPageTransformer != null) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (bool != bool2) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.mPageTransformer = paramPageTransformer;
    setChildrenDrawingOrderEnabled(bool);
    if (bool) {
      if (paramBoolean)
        b = 2; 
      this.mDrawingOrder = b;
      this.mPageTransformerLayerType = paramInt;
    } else {
      this.mDrawingOrder = 0;
    } 
    if (bool1)
      populate(); 
  }
  
  void setScrollState(int paramInt) {
    if (this.mScrollState == paramInt)
      return; 
    this.mScrollState = paramInt;
    if (this.mPageTransformer != null) {
      boolean bool;
      if (paramInt != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      enableLayers(bool);
    } 
    dispatchOnScrollStateChanged(paramInt);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2) { smoothScrollTo(paramInt1, paramInt2, 0); }
  
  void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3) {
    int i;
    if (getChildCount() == 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    Scroller scroller = this.mScroller;
    if (scroller != null && !scroller.isFinished()) {
      i = 1;
    } else {
      i = 0;
    } 
    if (i) {
      if (this.mIsScrollStarted) {
        i = this.mScroller.getCurrX();
      } else {
        i = this.mScroller.getStartX();
      } 
      this.mScroller.abortAnimation();
      setScrollingCacheEnabled(false);
    } else {
      i = getScrollX();
    } 
    int j = getScrollY();
    int k = paramInt1 - i;
    paramInt2 -= j;
    if (k == 0 && paramInt2 == 0) {
      completeScroll(false);
      populate();
      setScrollState(0);
      return;
    } 
    setScrollingCacheEnabled(true);
    setScrollState(2);
    paramInt1 = getClientWidth();
    int m = paramInt1 / 2;
    float f3 = Math.min(1.0F, Math.abs(k) * 1.0F / paramInt1);
    float f1 = m;
    float f2 = m;
    f3 = distanceInfluenceForSnapDuration(f3);
    paramInt3 = Math.abs(paramInt3);
    if (paramInt3 > 0) {
      paramInt1 = Math.round(Math.abs((f1 + f2 * f3) / paramInt3) * 1000.0F) * 4;
    } else {
      f1 = paramInt1;
      f2 = this.mAdapter.getPageWidth(this.mCurItem);
      paramInt1 = (int)((1.0F + Math.abs(k) / (this.mPageMargin + f1 * f2)) * 100.0F);
    } 
    paramInt1 = Math.min(paramInt1, 600);
    this.mIsScrollStarted = false;
    this.mScroller.startScroll(i, j, k, paramInt2, paramInt1);
    ViewCompat.postInvalidateOnAnimation(this);
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) { return (super.verifyDrawable(paramDrawable) || paramDrawable == this.mMarginDrawable); }
  
  @Inherited
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE})
  public static @interface DecorView {}
  
  static class ItemInfo {
    Object object;
    
    float offset;
    
    int position;
    
    boolean scrolling;
    
    float widthFactor;
  }
  
  public static class LayoutParams extends ViewGroup.LayoutParams {
    int childIndex;
    
    public int gravity;
    
    public boolean isDecor;
    
    boolean needsMeasure;
    
    int position;
    
    float widthFactor = 0.0F;
    
    public LayoutParams() { super(-1, -1); }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, ViewPager.LAYOUT_ATTRS);
      this.gravity = typedArray.getInteger(0, 48);
      typedArray.recycle();
    }
  }
  
  class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
    private boolean canScroll() { return (ViewPager.this.mAdapter != null && ViewPager.this.mAdapter.getCount() > 1); }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(ViewPager.class.getName());
      param1AccessibilityEvent.setScrollable(canScroll());
      if (param1AccessibilityEvent.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
        param1AccessibilityEvent.setItemCount(ViewPager.this.mAdapter.getCount());
        param1AccessibilityEvent.setFromIndex(ViewPager.this.mCurItem);
        param1AccessibilityEvent.setToIndex(ViewPager.this.mCurItem);
      } 
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      param1AccessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
      param1AccessibilityNodeInfoCompat.setScrollable(canScroll());
      if (ViewPager.this.canScrollHorizontally(1))
        param1AccessibilityNodeInfoCompat.addAction(4096); 
      if (ViewPager.this.canScrollHorizontally(-1))
        param1AccessibilityNodeInfoCompat.addAction(8192); 
    }
    
    public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
      if (super.performAccessibilityAction(param1View, param1Int, param1Bundle))
        return true; 
      if (param1Int != 4096) {
        if (param1Int != 8192)
          return false; 
        if (ViewPager.this.canScrollHorizontally(-1)) {
          ViewPager viewPager = ViewPager.this;
          viewPager.setCurrentItem(viewPager.mCurItem - 1);
          return true;
        } 
        return false;
      } 
      if (ViewPager.this.canScrollHorizontally(1)) {
        ViewPager viewPager = ViewPager.this;
        viewPager.setCurrentItem(viewPager.mCurItem + 1);
        return true;
      } 
      return false;
    }
  }
  
  public static interface OnAdapterChangeListener {
    void onAdapterChanged(@NonNull ViewPager param1ViewPager, @Nullable PagerAdapter param1PagerAdapter1, @Nullable PagerAdapter param1PagerAdapter2);
  }
  
  public static interface OnPageChangeListener {
    void onPageScrollStateChanged(int param1Int);
    
    void onPageScrolled(int param1Int1, float param1Float, @Px int param1Int2);
    
    void onPageSelected(int param1Int);
  }
  
  public static interface PageTransformer {
    void transformPage(@NonNull View param1View, float param1Float);
  }
  
  private class PagerObserver extends DataSetObserver {
    public void onChanged() { ViewPager.this.dataSetChanged(); }
    
    public void onInvalidated() { ViewPager.this.dataSetChanged(); }
  }
  
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
        public ViewPager.SavedState createFromParcel(Parcel param2Parcel) { return new ViewPager.SavedState(param2Parcel, null); }
        
        public ViewPager.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) { return new ViewPager.SavedState(param2Parcel, param2ClassLoader); }
        
        public ViewPager.SavedState[] newArray(int param2Int) { return new ViewPager.SavedState[param2Int]; }
      };
    
    Parcelable adapterState;
    
    ClassLoader loader;
    
    int position;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      ClassLoader classLoader = param1ClassLoader;
      if (param1ClassLoader == null)
        classLoader = getClass().getClassLoader(); 
      this.position = param1Parcel.readInt();
      this.adapterState = param1Parcel.readParcelable(classLoader);
      this.loader = classLoader;
    }
    
    public SavedState(@NonNull Parcelable param1Parcelable) { super(param1Parcelable); }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("FragmentPager.SavedState{");
      stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      stringBuilder.append(" position=");
      stringBuilder.append(this.position);
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.position);
      param1Parcel.writeParcelable(this.adapterState, param1Int);
    }
  }
  
  static final class null extends Object implements Parcelable.ClassLoaderCreator<SavedState> {
    public ViewPager.SavedState createFromParcel(Parcel param1Parcel) { return new ViewPager.SavedState(param1Parcel, null); }
    
    public ViewPager.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) { return new ViewPager.SavedState(param1Parcel, param1ClassLoader); }
    
    public ViewPager.SavedState[] newArray(int param1Int) { return new ViewPager.SavedState[param1Int]; }
  }
  
  public static class SimpleOnPageChangeListener implements OnPageChangeListener {
    public void onPageScrollStateChanged(int param1Int) {}
    
    public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}
    
    public void onPageSelected(int param1Int) {}
  }
  
  static class ViewPositionComparator extends Object implements Comparator<View> {
    public int compare(View param1View1, View param1View2) {
      ViewPager.LayoutParams layoutParams1 = (ViewPager.LayoutParams)param1View1.getLayoutParams();
      ViewPager.LayoutParams layoutParams2 = (ViewPager.LayoutParams)param1View2.getLayoutParams();
      return (layoutParams1.isDecor != layoutParams2.isDecor) ? (layoutParams1.isDecor ? 1 : -1) : (layoutParams1.position - layoutParams2.position);
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\view\ViewPager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */