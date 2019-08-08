package android.support.v4.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import java.util.Arrays;

public class ViewDragHelper {
  private static final int BASE_SETTLE_DURATION = 256;
  
  public static final int DIRECTION_ALL = 3;
  
  public static final int DIRECTION_HORIZONTAL = 1;
  
  public static final int DIRECTION_VERTICAL = 2;
  
  public static final int EDGE_ALL = 15;
  
  public static final int EDGE_BOTTOM = 8;
  
  public static final int EDGE_LEFT = 1;
  
  public static final int EDGE_RIGHT = 2;
  
  private static final int EDGE_SIZE = 20;
  
  public static final int EDGE_TOP = 4;
  
  public static final int INVALID_POINTER = -1;
  
  private static final int MAX_SETTLE_DURATION = 600;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_IDLE = 0;
  
  public static final int STATE_SETTLING = 2;
  
  private static final String TAG = "ViewDragHelper";
  
  private static final Interpolator sInterpolator = new Interpolator() {
      public float getInterpolation(float param1Float) {
        param1Float--;
        return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
      }
    };
  
  private int mActivePointerId = -1;
  
  private final Callback mCallback;
  
  private View mCapturedView;
  
  private int mDragState;
  
  private int[] mEdgeDragsInProgress;
  
  private int[] mEdgeDragsLocked;
  
  private int mEdgeSize;
  
  private int[] mInitialEdgesTouched;
  
  private float[] mInitialMotionX;
  
  private float[] mInitialMotionY;
  
  private float[] mLastMotionX;
  
  private float[] mLastMotionY;
  
  private float mMaxVelocity;
  
  private float mMinVelocity;
  
  private final ViewGroup mParentView;
  
  private int mPointersDown;
  
  private boolean mReleaseInProgress;
  
  private OverScroller mScroller;
  
  private final Runnable mSetIdleRunnable = new Runnable() {
      public void run() { ViewDragHelper.this.setDragState(0); }
    };
  
  private int mTouchSlop;
  
  private int mTrackingEdges;
  
  private VelocityTracker mVelocityTracker;
  
  private ViewDragHelper(@NonNull Context paramContext, @NonNull ViewGroup paramViewGroup, @NonNull Callback paramCallback) {
    if (paramViewGroup != null) {
      if (paramCallback != null) {
        this.mParentView = paramViewGroup;
        this.mCallback = paramCallback;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(paramContext);
        this.mEdgeSize = (int)(20.0F * (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mScroller = new OverScroller(paramContext, sInterpolator);
        return;
      } 
      throw new IllegalArgumentException("Callback may not be null");
    } 
    throw new IllegalArgumentException("Parent view may not be null");
  }
  
  private boolean checkNewEdgeDrag(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2) {
    paramFloat1 = Math.abs(paramFloat1);
    paramFloat2 = Math.abs(paramFloat2);
    int i = this.mInitialEdgesTouched[paramInt1];
    byte b = 0;
    if ((i & paramInt2) == paramInt2 && (this.mTrackingEdges & paramInt2) != 0 && (this.mEdgeDragsLocked[paramInt1] & paramInt2) != paramInt2 && (this.mEdgeDragsInProgress[paramInt1] & paramInt2) != paramInt2) {
      i = this.mTouchSlop;
      if (paramFloat1 <= i && paramFloat2 <= i)
        return false; 
      if (paramFloat1 < 0.5F * paramFloat2 && this.mCallback.onEdgeLock(paramInt2)) {
        int[] arrayOfInt = this.mEdgeDragsLocked;
        arrayOfInt[paramInt1] = arrayOfInt[paramInt1] | paramInt2;
        return false;
      } 
      int j = b;
      if ((this.mEdgeDragsInProgress[paramInt1] & paramInt2) == 0) {
        j = b;
        if (paramFloat1 > this.mTouchSlop)
          j = 1; 
      } 
      return j;
    } 
    return false;
  }
  
  private boolean checkTouchSlop(View paramView, float paramFloat1, float paramFloat2) {
    boolean bool1;
    int i;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool = false;
    if (paramView == null)
      return false; 
    if (this.mCallback.getViewHorizontalDragRange(paramView) > 0) {
      i = 1;
    } else {
      i = 0;
    } 
    if (this.mCallback.getViewVerticalDragRange(paramView) > 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (i && bool1) {
      i = this.mTouchSlop;
      if (paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2 > (i * i))
        bool = true; 
      return bool;
    } 
    if (i != 0) {
      bool = bool2;
      if (Math.abs(paramFloat1) > this.mTouchSlop)
        bool = true; 
      return bool;
    } 
    if (bool1) {
      bool = bool3;
      if (Math.abs(paramFloat2) > this.mTouchSlop)
        bool = true; 
      return bool;
    } 
    return false;
  }
  
  private float clampMag(float paramFloat1, float paramFloat2, float paramFloat3) {
    float f = Math.abs(paramFloat1);
    return (f < paramFloat2) ? 0.0F : ((f > paramFloat3) ? ((paramFloat1 > 0.0F) ? paramFloat3 : -paramFloat3) : paramFloat1);
  }
  
  private int clampMag(int paramInt1, int paramInt2, int paramInt3) {
    int i = Math.abs(paramInt1);
    return (i < paramInt2) ? 0 : ((i > paramInt3) ? ((paramInt1 > 0) ? paramInt3 : -paramInt3) : paramInt1);
  }
  
  private void clearMotionHistory() {
    float[] arrayOfFloat = this.mInitialMotionX;
    if (arrayOfFloat == null)
      return; 
    Arrays.fill(arrayOfFloat, 0.0F);
    Arrays.fill(this.mInitialMotionY, 0.0F);
    Arrays.fill(this.mLastMotionX, 0.0F);
    Arrays.fill(this.mLastMotionY, 0.0F);
    Arrays.fill(this.mInitialEdgesTouched, 0);
    Arrays.fill(this.mEdgeDragsInProgress, 0);
    Arrays.fill(this.mEdgeDragsLocked, 0);
    this.mPointersDown = 0;
  }
  
  private void clearMotionHistory(int paramInt) {
    if (this.mInitialMotionX != null) {
      if (!isPointerDown(paramInt))
        return; 
      this.mInitialMotionX[paramInt] = 0.0F;
      this.mInitialMotionY[paramInt] = 0.0F;
      this.mLastMotionX[paramInt] = 0.0F;
      this.mLastMotionY[paramInt] = 0.0F;
      this.mInitialEdgesTouched[paramInt] = 0;
      this.mEdgeDragsInProgress[paramInt] = 0;
      this.mEdgeDragsLocked[paramInt] = 0;
      this.mPointersDown &= (1 << paramInt ^ 0xFFFFFFFF);
      return;
    } 
  }
  
  private int computeAxisDuration(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 == 0)
      return 0; 
    int i = this.mParentView.getWidth();
    int j = i / 2;
    float f3 = Math.min(1.0F, Math.abs(paramInt1) / i);
    float f1 = j;
    float f2 = j;
    f3 = distanceInfluenceForSnapDuration(f3);
    paramInt2 = Math.abs(paramInt2);
    if (paramInt2 > 0) {
      paramInt1 = Math.round(Math.abs((f1 + f2 * f3) / paramInt2) * 1000.0F) * 4;
    } else {
      paramInt1 = (int)((1.0F + Math.abs(paramInt1) / paramInt3) * 256.0F);
    } 
    return Math.min(paramInt1, 600);
  }
  
  private int computeSettleDuration(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    float f2;
    float f1;
    paramInt3 = clampMag(paramInt3, (int)this.mMinVelocity, (int)this.mMaxVelocity);
    paramInt4 = clampMag(paramInt4, (int)this.mMinVelocity, (int)this.mMaxVelocity);
    int i = Math.abs(paramInt1);
    int j = Math.abs(paramInt2);
    int k = Math.abs(paramInt3);
    int m = Math.abs(paramInt4);
    int n = k + m;
    int i1 = i + j;
    if (paramInt3 != 0) {
      f1 = k;
      f2 = n;
    } else {
      f1 = i;
      f2 = i1;
    } 
    float f3 = f1 / f2;
    if (paramInt4 != 0) {
      f1 = m;
      f2 = n;
    } else {
      f1 = j;
      f2 = i1;
    } 
    f1 /= f2;
    paramInt1 = computeAxisDuration(paramInt1, paramInt3, this.mCallback.getViewHorizontalDragRange(paramView));
    paramInt2 = computeAxisDuration(paramInt2, paramInt4, this.mCallback.getViewVerticalDragRange(paramView));
    return (int)(paramInt1 * f3 + paramInt2 * f1);
  }
  
  public static ViewDragHelper create(@NonNull ViewGroup paramViewGroup, float paramFloat, @NonNull Callback paramCallback) {
    ViewDragHelper viewDragHelper = create(paramViewGroup, paramCallback);
    viewDragHelper.mTouchSlop = (int)(viewDragHelper.mTouchSlop * 1.0F / paramFloat);
    return viewDragHelper;
  }
  
  public static ViewDragHelper create(@NonNull ViewGroup paramViewGroup, @NonNull Callback paramCallback) { return new ViewDragHelper(paramViewGroup.getContext(), paramViewGroup, paramCallback); }
  
  private void dispatchViewReleased(float paramFloat1, float paramFloat2) {
    this.mReleaseInProgress = true;
    this.mCallback.onViewReleased(this.mCapturedView, paramFloat1, paramFloat2);
    this.mReleaseInProgress = false;
    if (this.mDragState == 1)
      setDragState(0); 
  }
  
  private float distanceInfluenceForSnapDuration(float paramFloat) { return (float)Math.sin(((paramFloat - 0.5F) * 0.47123894F)); }
  
  private void dragTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int j = paramInt1;
    int i = paramInt2;
    int k = this.mCapturedView.getLeft();
    int m = this.mCapturedView.getTop();
    if (paramInt3 != 0) {
      paramInt1 = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, paramInt1, paramInt3);
      ViewCompat.offsetLeftAndRight(this.mCapturedView, paramInt1 - k);
    } else {
      paramInt1 = j;
    } 
    if (paramInt4 != 0) {
      i = this.mCallback.clampViewPositionVertical(this.mCapturedView, paramInt2, paramInt4);
      ViewCompat.offsetTopAndBottom(this.mCapturedView, i - m);
    } 
    if (paramInt3 != 0 || paramInt4 != 0)
      this.mCallback.onViewPositionChanged(this.mCapturedView, paramInt1, i, paramInt1 - k, i - m); 
  }
  
  private void ensureMotionHistorySizeForId(int paramInt) {
    float[] arrayOfFloat = this.mInitialMotionX;
    if (arrayOfFloat == null || arrayOfFloat.length <= paramInt) {
      arrayOfFloat = new float[paramInt + 1];
      float[] arrayOfFloat1 = new float[paramInt + 1];
      float[] arrayOfFloat2 = new float[paramInt + 1];
      float[] arrayOfFloat3 = new float[paramInt + 1];
      int[] arrayOfInt1 = new int[paramInt + 1];
      int[] arrayOfInt2 = new int[paramInt + 1];
      int[] arrayOfInt3 = new int[paramInt + 1];
      float[] arrayOfFloat4 = this.mInitialMotionX;
      if (arrayOfFloat4 != null) {
        System.arraycopy(arrayOfFloat4, 0, arrayOfFloat, 0, arrayOfFloat4.length);
        arrayOfFloat4 = this.mInitialMotionY;
        System.arraycopy(arrayOfFloat4, 0, arrayOfFloat1, 0, arrayOfFloat4.length);
        arrayOfFloat4 = this.mLastMotionX;
        System.arraycopy(arrayOfFloat4, 0, arrayOfFloat2, 0, arrayOfFloat4.length);
        arrayOfFloat4 = this.mLastMotionY;
        System.arraycopy(arrayOfFloat4, 0, arrayOfFloat3, 0, arrayOfFloat4.length);
        int[] arrayOfInt = this.mInitialEdgesTouched;
        System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, arrayOfInt.length);
        arrayOfInt = this.mEdgeDragsInProgress;
        System.arraycopy(arrayOfInt, 0, arrayOfInt2, 0, arrayOfInt.length);
        arrayOfInt = this.mEdgeDragsLocked;
        System.arraycopy(arrayOfInt, 0, arrayOfInt3, 0, arrayOfInt.length);
      } 
      this.mInitialMotionX = arrayOfFloat;
      this.mInitialMotionY = arrayOfFloat1;
      this.mLastMotionX = arrayOfFloat2;
      this.mLastMotionY = arrayOfFloat3;
      this.mInitialEdgesTouched = arrayOfInt1;
      this.mEdgeDragsInProgress = arrayOfInt2;
      this.mEdgeDragsLocked = arrayOfInt3;
    } 
  }
  
  private boolean forceSettleCapturedViewAt(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = this.mCapturedView.getLeft();
    int j = this.mCapturedView.getTop();
    paramInt1 -= i;
    paramInt2 -= j;
    if (paramInt1 == 0 && paramInt2 == 0) {
      this.mScroller.abortAnimation();
      setDragState(0);
      return false;
    } 
    paramInt3 = computeSettleDuration(this.mCapturedView, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mScroller.startScroll(i, j, paramInt1, paramInt2, paramInt3);
    setDragState(2);
    return true;
  }
  
  private int getEdgesTouched(int paramInt1, int paramInt2) {
    int i = 0;
    if (paramInt1 < this.mParentView.getLeft() + this.mEdgeSize)
      i = false | true; 
    byte b = i;
    if (paramInt2 < this.mParentView.getTop() + this.mEdgeSize)
      b = i | 0x4; 
    i = b;
    if (paramInt1 > this.mParentView.getRight() - this.mEdgeSize)
      i = b | 0x2; 
    paramInt1 = i;
    if (paramInt2 > this.mParentView.getBottom() - this.mEdgeSize)
      paramInt1 = i | 0x8; 
    return paramInt1;
  }
  
  private boolean isValidPointerForActionMove(int paramInt) {
    if (!isPointerDown(paramInt)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Ignoring pointerId=");
      stringBuilder.append(paramInt);
      stringBuilder.append(" because ACTION_DOWN was not received ");
      stringBuilder.append("for this pointer before ACTION_MOVE. It likely happened because ");
      stringBuilder.append(" ViewDragHelper did not receive all the events in the event stream.");
      Log.e("ViewDragHelper", stringBuilder.toString());
      return false;
    } 
    return true;
  }
  
  private void releaseViewForPointerUp() {
    this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
    dispatchViewReleased(clampMag(this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), clampMag(this.mVelocityTracker.getYVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
  }
  
  private void reportNewEdgeDrags(float paramFloat1, float paramFloat2, int paramInt) {
    byte b = 0;
    if (checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 1))
      b = false | true; 
    int i = b;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 4))
      i = b | 0x4; 
    b = i;
    if (checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 2))
      b = i | 0x2; 
    i = b;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 8))
      i = b | 0x8; 
    if (i != 0) {
      int[] arrayOfInt = this.mEdgeDragsInProgress;
      arrayOfInt[paramInt] = arrayOfInt[paramInt] | i;
      this.mCallback.onEdgeDragStarted(i, paramInt);
    } 
  }
  
  private void saveInitialMotion(float paramFloat1, float paramFloat2, int paramInt) {
    ensureMotionHistorySizeForId(paramInt);
    float[] arrayOfFloat = this.mInitialMotionX;
    this.mLastMotionX[paramInt] = paramFloat1;
    arrayOfFloat[paramInt] = paramFloat1;
    arrayOfFloat = this.mInitialMotionY;
    this.mLastMotionY[paramInt] = paramFloat2;
    arrayOfFloat[paramInt] = paramFloat2;
    this.mInitialEdgesTouched[paramInt] = getEdgesTouched((int)paramFloat1, (int)paramFloat2);
    this.mPointersDown |= 1 << paramInt;
  }
  
  private void saveLastMotion(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getPointerCount();
    byte b;
    for (b = 0; b < i; b++) {
      int j = paramMotionEvent.getPointerId(b);
      if (isValidPointerForActionMove(j)) {
        float f1 = paramMotionEvent.getX(b);
        float f2 = paramMotionEvent.getY(b);
        this.mLastMotionX[j] = f1;
        this.mLastMotionY[j] = f2;
      } 
    } 
  }
  
  public void abort() {
    cancel();
    if (this.mDragState == 2) {
      int i = this.mScroller.getCurrX();
      int j = this.mScroller.getCurrY();
      this.mScroller.abortAnimation();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      this.mCallback.onViewPositionChanged(this.mCapturedView, k, m, k - i, m - j);
    } 
    setDragState(0);
  }
  
  protected boolean canScroll(@NonNull View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int j = paramView.getScrollX();
      int k = paramView.getScrollY();
      int i;
      for (i = viewGroup.getChildCount() - 1; i >= 0; i--) {
        View view = viewGroup.getChildAt(i);
        if (paramInt3 + j >= view.getLeft() && paramInt3 + j < view.getRight() && paramInt4 + k >= view.getTop() && paramInt4 + k < view.getBottom() && canScroll(view, true, paramInt1, paramInt2, paramInt3 + j - view.getLeft(), paramInt4 + k - view.getTop()))
          return true; 
      } 
    } 
    if (paramBoolean)
      if (!paramView.canScrollHorizontally(-paramInt1)) {
        if (paramView.canScrollVertically(-paramInt2))
          return true; 
      } else {
        return true;
      }  
    return false;
  }
  
  public void cancel() {
    this.mActivePointerId = -1;
    clearMotionHistory();
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  public void captureChildView(@NonNull View paramView, int paramInt) {
    if (paramView.getParent() == this.mParentView) {
      this.mCapturedView = paramView;
      this.mActivePointerId = paramInt;
      this.mCallback.onViewCaptured(paramView, paramInt);
      setDragState(1);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (");
    stringBuilder.append(this.mParentView);
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public boolean checkTouchSlop(int paramInt) {
    int i = this.mInitialMotionX.length;
    for (byte b = 0; b < i; b++) {
      if (checkTouchSlop(paramInt, b))
        return true; 
    } 
    return false;
  }
  
  public boolean checkTouchSlop(int paramInt1, int paramInt2) {
    boolean bool1;
    boolean bool5 = isPointerDown(paramInt2);
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool2 = false;
    if (!bool5)
      return false; 
    if ((paramInt1 & true) == 1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if ((paramInt1 & 0x2) == 2) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    float f1 = this.mLastMotionX[paramInt2] - this.mInitialMotionX[paramInt2];
    float f2 = this.mLastMotionY[paramInt2] - this.mInitialMotionY[paramInt2];
    if (bool1 && paramInt1 != 0) {
      paramInt1 = this.mTouchSlop;
      if (f1 * f1 + f2 * f2 > (paramInt1 * paramInt1))
        bool2 = true; 
      return bool2;
    } 
    if (bool1) {
      bool2 = bool3;
      if (Math.abs(f1) > this.mTouchSlop)
        bool2 = true; 
      return bool2;
    } 
    if (paramInt1 != 0) {
      bool2 = bool4;
      if (Math.abs(f2) > this.mTouchSlop)
        bool2 = true; 
      return bool2;
    } 
    return false;
  }
  
  public boolean continueSettling(boolean paramBoolean) {
    int i = this.mDragState;
    boolean bool = false;
    if (i == 2) {
      boolean bool2 = this.mScroller.computeScrollOffset();
      i = this.mScroller.getCurrX();
      int j = this.mScroller.getCurrY();
      int k = i - this.mCapturedView.getLeft();
      int m = j - this.mCapturedView.getTop();
      if (k != 0)
        ViewCompat.offsetLeftAndRight(this.mCapturedView, k); 
      if (m != 0)
        ViewCompat.offsetTopAndBottom(this.mCapturedView, m); 
      if (k != 0 || m != 0)
        this.mCallback.onViewPositionChanged(this.mCapturedView, i, j, k, m); 
      boolean bool1 = bool2;
      if (bool2) {
        bool1 = bool2;
        if (i == this.mScroller.getFinalX()) {
          bool1 = bool2;
          if (j == this.mScroller.getFinalY()) {
            this.mScroller.abortAnimation();
            bool1 = false;
          } 
        } 
      } 
      if (!bool1)
        if (paramBoolean) {
          this.mParentView.post(this.mSetIdleRunnable);
        } else {
          setDragState(0);
        }  
    } 
    paramBoolean = bool;
    if (this.mDragState == 2)
      paramBoolean = true; 
    return paramBoolean;
  }
  
  @Nullable
  public View findTopChildUnder(int paramInt1, int paramInt2) {
    for (int i = this.mParentView.getChildCount() - 1; i >= 0; i--) {
      View view = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(i));
      if (paramInt1 >= view.getLeft() && paramInt1 < view.getRight() && paramInt2 >= view.getTop() && paramInt2 < view.getBottom())
        return view; 
    } 
    return null;
  }
  
  public void flingCapturedView(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mReleaseInProgress) {
      this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId), paramInt1, paramInt3, paramInt2, paramInt4);
      setDragState(2);
      return;
    } 
    throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
  }
  
  public int getActivePointerId() { return this.mActivePointerId; }
  
  @Nullable
  public View getCapturedView() { return this.mCapturedView; }
  
  @Px
  public int getEdgeSize() { return this.mEdgeSize; }
  
  public float getMinVelocity() { return this.mMinVelocity; }
  
  @Px
  public int getTouchSlop() { return this.mTouchSlop; }
  
  public int getViewDragState() { return this.mDragState; }
  
  public boolean isCapturedViewUnder(int paramInt1, int paramInt2) { return isViewUnder(this.mCapturedView, paramInt1, paramInt2); }
  
  public boolean isEdgeTouched(int paramInt) {
    int i = this.mInitialEdgesTouched.length;
    for (byte b = 0; b < i; b++) {
      if (isEdgeTouched(paramInt, b))
        return true; 
    } 
    return false;
  }
  
  public boolean isEdgeTouched(int paramInt1, int paramInt2) { return (isPointerDown(paramInt2) && (this.mInitialEdgesTouched[paramInt2] & paramInt1) != 0); }
  
  public boolean isPointerDown(int paramInt) { return ((this.mPointersDown & 1 << paramInt) != 0); }
  
  public boolean isViewUnder(@Nullable View paramView, int paramInt1, int paramInt2) { return (paramView == null) ? false : ((paramInt1 >= paramView.getLeft() && paramInt1 < paramView.getRight() && paramInt2 >= paramView.getTop() && paramInt2 < paramView.getBottom())); }
  
  public void processTouchEvent(@NonNull MotionEvent paramMotionEvent) {
    int m;
    int i = paramMotionEvent.getActionMasked();
    int j = paramMotionEvent.getActionIndex();
    if (i == 0)
      cancel(); 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (i) {
      default:
        return;
      case 6:
        m = paramMotionEvent.getPointerId(j);
        if (this.mDragState == 1 && m == this.mActivePointerId) {
          int n = -1;
          int i1 = paramMotionEvent.getPointerCount();
          i = 0;
          while (true) {
            j = n;
            if (i < i1) {
              j = paramMotionEvent.getPointerId(i);
              if (j != this.mActivePointerId) {
                float f3 = paramMotionEvent.getX(i);
                float f4 = paramMotionEvent.getY(i);
                View view1 = findTopChildUnder((int)f3, (int)f4);
                View view2 = this.mCapturedView;
                if (view1 == view2 && tryCaptureViewForDrag(view2, j)) {
                  j = this.mActivePointerId;
                  break;
                } 
              } 
              i++;
              continue;
            } 
            break;
          } 
          if (j == -1)
            releaseViewForPointerUp(); 
        } 
        clearMotionHistory(m);
        return;
      case 5:
        i = paramMotionEvent.getPointerId(j);
        f1 = paramMotionEvent.getX(j);
        f2 = paramMotionEvent.getY(j);
        saveInitialMotion(f1, f2, i);
        if (this.mDragState == 0) {
          tryCaptureViewForDrag(findTopChildUnder((int)f1, (int)f2), i);
          j = this.mInitialEdgesTouched[i];
          int n = this.mTrackingEdges;
          if ((j & n) != 0)
            this.mCallback.onEdgeTouched(n & j, i); 
        } else if (isCapturedViewUnder((int)f1, (int)f2)) {
          tryCaptureViewForDrag(this.mCapturedView, i);
          return;
        } 
        return;
      case 3:
        if (this.mDragState == 1)
          dispatchViewReleased(0.0F, 0.0F); 
        cancel();
        return;
      case 2:
        if (this.mDragState == 1) {
          if (!isValidPointerForActionMove(this.mActivePointerId))
            return; 
          i = paramMotionEvent.findPointerIndex(this.mActivePointerId);
          f1 = paramMotionEvent.getX(i);
          f2 = paramMotionEvent.getY(i);
          float[] arrayOfFloat = this.mLastMotionX;
          j = this.mActivePointerId;
          i = (int)(f1 - arrayOfFloat[j]);
          j = (int)(f2 - this.mLastMotionY[j]);
          dragTo(this.mCapturedView.getLeft() + i, this.mCapturedView.getTop() + j, i, j);
          saveLastMotion(paramMotionEvent);
          return;
        } 
        j = paramMotionEvent.getPointerCount();
        for (i = 0; i < j; i++) {
          int n = paramMotionEvent.getPointerId(i);
          if (isValidPointerForActionMove(n)) {
            f1 = paramMotionEvent.getX(i);
            f2 = paramMotionEvent.getY(i);
            float f3 = f1 - this.mInitialMotionX[n];
            float f4 = f2 - this.mInitialMotionY[n];
            reportNewEdgeDrags(f3, f4, n);
            if (this.mDragState == 1)
              break; 
            View view1 = findTopChildUnder((int)f1, (int)f2);
            if (checkTouchSlop(view1, f3, f4) && tryCaptureViewForDrag(view1, n))
              break; 
          } 
        } 
        saveLastMotion(paramMotionEvent);
        return;
      case 1:
        if (this.mDragState == 1)
          releaseViewForPointerUp(); 
        cancel();
        return;
      case 0:
        break;
    } 
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    i = paramMotionEvent.getPointerId(0);
    View view = findTopChildUnder((int)f1, (int)f2);
    saveInitialMotion(f1, f2, i);
    tryCaptureViewForDrag(view, i);
    j = this.mInitialEdgesTouched[i];
    int k = this.mTrackingEdges;
    if ((j & k) != 0)
      this.mCallback.onEdgeTouched(k & j, i); 
  }
  
  void setDragState(int paramInt) {
    this.mParentView.removeCallbacks(this.mSetIdleRunnable);
    if (this.mDragState != paramInt) {
      this.mDragState = paramInt;
      this.mCallback.onViewDragStateChanged(paramInt);
      if (this.mDragState == 0)
        this.mCapturedView = null; 
    } 
  }
  
  public void setEdgeTrackingEnabled(int paramInt) { this.mTrackingEdges = paramInt; }
  
  public void setMinVelocity(float paramFloat) { this.mMinVelocity = paramFloat; }
  
  public boolean settleCapturedViewAt(int paramInt1, int paramInt2) {
    if (this.mReleaseInProgress)
      return forceSettleCapturedViewAt(paramInt1, paramInt2, (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId)); 
    throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
  }
  
  public boolean shouldInterceptTouchEvent(@NonNull MotionEvent paramMotionEvent) {
    byte b;
    int i;
    float f2;
    float f1;
    View view;
    int k = paramMotionEvent.getActionMasked();
    int j = paramMotionEvent.getActionIndex();
    if (k == 0)
      cancel(); 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (k) {
      case 6:
        clearMotionHistory(paramMotionEvent.getPointerId(j));
        break;
      case 5:
        i = paramMotionEvent.getPointerId(j);
        f1 = paramMotionEvent.getX(j);
        f2 = paramMotionEvent.getY(j);
        saveInitialMotion(f1, f2, i);
        j = this.mDragState;
        if (j == 0) {
          j = this.mInitialEdgesTouched[i];
          k = this.mTrackingEdges;
          if ((j & k) != 0)
            this.mCallback.onEdgeTouched(k & j, i); 
          break;
        } 
        if (j == 2) {
          view = findTopChildUnder((int)f1, (int)f2);
          if (view == this.mCapturedView)
            tryCaptureViewForDrag(view, i); 
        } 
        break;
      case 2:
        if (this.mInitialMotionX == null || this.mInitialMotionY == null)
          break; 
        i = view.getPointerCount();
        for (b = 0; b < i; b++) {
          int m = view.getPointerId(b);
          if (isValidPointerForActionMove(m)) {
            boolean bool;
            f1 = view.getX(b);
            f2 = view.getY(b);
            float f3 = f1 - this.mInitialMotionX[m];
            float f4 = f2 - this.mInitialMotionY[m];
            View view1 = findTopChildUnder((int)f1, (int)f2);
            if (view1 != null && checkTouchSlop(view1, f3, f4)) {
              bool = true;
            } else {
              bool = false;
            } 
            if (bool) {
              int n = view1.getLeft();
              int i1 = (int)f3;
              i1 = this.mCallback.clampViewPositionHorizontal(view1, i1 + n, (int)f3);
              int i2 = view1.getTop();
              int i3 = (int)f4;
              i3 = this.mCallback.clampViewPositionVertical(view1, i3 + i2, (int)f4);
              int i4 = this.mCallback.getViewHorizontalDragRange(view1);
              int i5 = this.mCallback.getViewVerticalDragRange(view1);
              if ((i4 == 0 || (i4 > 0 && i1 == n)) && (i5 == 0 || (i5 > 0 && i3 == i2)))
                break; 
            } 
            reportNewEdgeDrags(f3, f4, m);
            if (this.mDragState == 1 || (bool && tryCaptureViewForDrag(view1, m)))
              break; 
          } 
        } 
        saveLastMotion(view);
        break;
      case 1:
      case 3:
        cancel();
        break;
      case 0:
        f1 = view.getX();
        f2 = view.getY();
        i = view.getPointerId(0);
        saveInitialMotion(f1, f2, i);
        view = findTopChildUnder((int)f1, (int)f2);
        if (view == this.mCapturedView && this.mDragState == 2)
          tryCaptureViewForDrag(view, i); 
        j = this.mInitialEdgesTouched[i];
        k = this.mTrackingEdges;
        if ((j & k) != 0)
          this.mCallback.onEdgeTouched(k & j, i); 
        break;
    } 
    return (this.mDragState == 1);
  }
  
  public boolean smoothSlideViewTo(@NonNull View paramView, int paramInt1, int paramInt2) {
    this.mCapturedView = paramView;
    this.mActivePointerId = -1;
    boolean bool = forceSettleCapturedViewAt(paramInt1, paramInt2, 0, 0);
    if (!bool && this.mDragState == 0 && this.mCapturedView != null)
      this.mCapturedView = null; 
    return bool;
  }
  
  boolean tryCaptureViewForDrag(View paramView, int paramInt) {
    if (paramView == this.mCapturedView && this.mActivePointerId == paramInt)
      return true; 
    if (paramView != null && this.mCallback.tryCaptureView(paramView, paramInt)) {
      this.mActivePointerId = paramInt;
      captureChildView(paramView, paramInt);
      return true;
    } 
    return false;
  }
  
  public static abstract class Callback {
    public int clampViewPositionHorizontal(@NonNull View param1View, int param1Int1, int param1Int2) { return 0; }
    
    public int clampViewPositionVertical(@NonNull View param1View, int param1Int1, int param1Int2) { return 0; }
    
    public int getOrderedChildIndex(int param1Int) { return param1Int; }
    
    public int getViewHorizontalDragRange(@NonNull View param1View) { return 0; }
    
    public int getViewVerticalDragRange(@NonNull View param1View) { return 0; }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) {}
    
    public boolean onEdgeLock(int param1Int) { return false; }
    
    public void onEdgeTouched(int param1Int1, int param1Int2) {}
    
    public void onViewCaptured(@NonNull View param1View, int param1Int) {}
    
    public void onViewDragStateChanged(int param1Int) {}
    
    public void onViewPositionChanged(@NonNull View param1View, int param1Int1, int param1Int2, @Px int param1Int3, @Px int param1Int4) {}
    
    public void onViewReleased(@NonNull View param1View, float param1Float1, float param1Float2) {}
    
    public abstract boolean tryCaptureView(@NonNull View param1View, int param1Int);
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\widget\ViewDragHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */