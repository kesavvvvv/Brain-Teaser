package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

abstract class AbsActionBarView extends ViewGroup {
  private static final int FADE_DURATION = 200;
  
  protected ActionMenuPresenter mActionMenuPresenter;
  
  protected int mContentHeight;
  
  private boolean mEatingHover;
  
  private boolean mEatingTouch;
  
  protected ActionMenuView mMenuView;
  
  protected final Context mPopupContext;
  
  protected final VisibilityAnimListener mVisAnimListener = new VisibilityAnimListener();
  
  protected ViewPropertyAnimatorCompat mVisibilityAnim;
  
  AbsActionBarView(Context paramContext) { this(paramContext, null); }
  
  AbsActionBarView(Context paramContext, AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, 0); }
  
  AbsActionBarView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TypedValue typedValue = new TypedValue();
    if (paramContext.getTheme().resolveAttribute(R.attr.actionBarPopupTheme, typedValue, true) && typedValue.resourceId != 0) {
      this.mPopupContext = new ContextThemeWrapper(paramContext, typedValue.resourceId);
      return;
    } 
    this.mPopupContext = paramContext;
  }
  
  protected static int next(int paramInt1, int paramInt2, boolean paramBoolean) { return paramBoolean ? (paramInt1 - paramInt2) : (paramInt1 + paramInt2); }
  
  public void animateToVisibility(int paramInt) { setupAnimatorToVisibility(paramInt, 200L).start(); }
  
  public boolean canShowOverflowMenu() { return (isOverflowReserved() && getVisibility() == 0); }
  
  public void dismissPopupMenus() {
    ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
    if (actionMenuPresenter != null)
      actionMenuPresenter.dismissPopupMenus(); 
  }
  
  public int getAnimatedVisibility() { return (this.mVisibilityAnim != null) ? this.mVisAnimListener.mFinalVisibility : getVisibility(); }
  
  public int getContentHeight() { return this.mContentHeight; }
  
  public boolean hideOverflowMenu() {
    ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
    return (actionMenuPresenter != null) ? actionMenuPresenter.hideOverflowMenu() : 0;
  }
  
  public boolean isOverflowMenuShowPending() {
    ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
    return (actionMenuPresenter != null) ? actionMenuPresenter.isOverflowMenuShowPending() : 0;
  }
  
  public boolean isOverflowMenuShowing() {
    ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
    return (actionMenuPresenter != null) ? actionMenuPresenter.isOverflowMenuShowing() : 0;
  }
  
  public boolean isOverflowReserved() {
    ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
    return (actionMenuPresenter != null && actionMenuPresenter.isOverflowReserved());
  }
  
  protected int measureChildView(View paramView, int paramInt1, int paramInt2, int paramInt3) {
    paramView.measure(View.MeasureSpec.makeMeasureSpec(paramInt1, -2147483648), paramInt2);
    return Math.max(0, paramInt1 - paramView.getMeasuredWidth() - paramInt3);
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    TypedArray typedArray = getContext().obtainStyledAttributes(null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
    setContentHeight(typedArray.getLayoutDimension(R.styleable.ActionBar_height, 0));
    typedArray.recycle();
    ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
    if (actionMenuPresenter != null)
      actionMenuPresenter.onConfigurationChanged(paramConfiguration); 
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionMasked();
    if (i == 9)
      this.mEatingHover = false; 
    if (!this.mEatingHover) {
      boolean bool = super.onHoverEvent(paramMotionEvent);
      if (i == 9 && !bool)
        this.mEatingHover = true; 
    } 
    if (i == 10 || i == 3)
      this.mEatingHover = false; 
    return true;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionMasked();
    if (i == 0)
      this.mEatingTouch = false; 
    if (!this.mEatingTouch) {
      boolean bool = super.onTouchEvent(paramMotionEvent);
      if (i == 0 && !bool)
        this.mEatingTouch = true; 
    } 
    if (i == 1 || i == 3)
      this.mEatingTouch = false; 
    return true;
  }
  
  protected int positionChild(View paramView, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    int i = paramView.getMeasuredWidth();
    int j = paramView.getMeasuredHeight();
    paramInt2 = (paramInt3 - j) / 2 + paramInt2;
    if (paramBoolean) {
      paramView.layout(paramInt1 - i, paramInt2, paramInt1, paramInt2 + j);
    } else {
      paramView.layout(paramInt1, paramInt2, paramInt1 + i, paramInt2 + j);
    } 
    return paramBoolean ? -i : i;
  }
  
  public void postShowOverflowMenu() { post(new Runnable() {
          public void run() { AbsActionBarView.this.showOverflowMenu(); }
        }); }
  
  public void setContentHeight(int paramInt) {
    this.mContentHeight = paramInt;
    requestLayout();
  }
  
  public void setVisibility(int paramInt) {
    if (paramInt != getVisibility()) {
      ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mVisibilityAnim;
      if (viewPropertyAnimatorCompat != null)
        viewPropertyAnimatorCompat.cancel(); 
      super.setVisibility(paramInt);
    } 
  }
  
  public ViewPropertyAnimatorCompat setupAnimatorToVisibility(int paramInt, long paramLong) {
    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mVisibilityAnim;
    if (viewPropertyAnimatorCompat != null)
      viewPropertyAnimatorCompat.cancel(); 
    if (paramInt == 0) {
      if (getVisibility() != 0)
        setAlpha(0.0F); 
      viewPropertyAnimatorCompat = ViewCompat.animate(this).alpha(1.0F);
      viewPropertyAnimatorCompat.setDuration(paramLong);
      viewPropertyAnimatorCompat.setListener(this.mVisAnimListener.withFinalVisibility(viewPropertyAnimatorCompat, paramInt));
      return viewPropertyAnimatorCompat;
    } 
    viewPropertyAnimatorCompat = ViewCompat.animate(this).alpha(0.0F);
    viewPropertyAnimatorCompat.setDuration(paramLong);
    viewPropertyAnimatorCompat.setListener(this.mVisAnimListener.withFinalVisibility(viewPropertyAnimatorCompat, paramInt));
    return viewPropertyAnimatorCompat;
  }
  
  public boolean showOverflowMenu() {
    ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
    return (actionMenuPresenter != null) ? actionMenuPresenter.showOverflowMenu() : 0;
  }
  
  protected class VisibilityAnimListener implements ViewPropertyAnimatorListener {
    private boolean mCanceled = false;
    
    int mFinalVisibility;
    
    public void onAnimationCancel(View param1View) { this.mCanceled = true; }
    
    public void onAnimationEnd(View param1View) {
      if (this.mCanceled)
        return; 
      AbsActionBarView absActionBarView = AbsActionBarView.this;
      absActionBarView.mVisibilityAnim = null;
      absActionBarView.setVisibility(this.mFinalVisibility);
    }
    
    public void onAnimationStart(View param1View) {
      AbsActionBarView.this.setVisibility(0);
      this.mCanceled = false;
    }
    
    public VisibilityAnimListener withFinalVisibility(ViewPropertyAnimatorCompat param1ViewPropertyAnimatorCompat, int param1Int) {
      AbsActionBarView.this.mVisibilityAnim = param1ViewPropertyAnimatorCompat;
      this.mFinalVisibility = param1Int;
      return this;
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\AbsActionBarView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */