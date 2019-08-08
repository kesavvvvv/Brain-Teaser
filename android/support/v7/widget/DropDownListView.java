package android.support.v7.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.appcompat.R;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.lang.reflect.Field;

class DropDownListView extends ListView {
  public static final int INVALID_POSITION = -1;
  
  public static final int NO_POSITION = -1;
  
  private ViewPropertyAnimatorCompat mClickAnimation;
  
  private boolean mDrawsInPressedState;
  
  private boolean mHijackFocus;
  
  private Field mIsChildViewEnabled;
  
  private boolean mListSelectionHidden;
  
  private int mMotionPosition;
  
  ResolveHoverRunnable mResolveHoverRunnable;
  
  private ListViewAutoScrollHelper mScrollHelper;
  
  private int mSelectionBottomPadding = 0;
  
  private int mSelectionLeftPadding = 0;
  
  private int mSelectionRightPadding = 0;
  
  private int mSelectionTopPadding = 0;
  
  private GateKeeperDrawable mSelector;
  
  private final Rect mSelectorRect = new Rect();
  
  DropDownListView(Context paramContext, boolean paramBoolean) {
    super(paramContext, null, R.attr.dropDownListViewStyle);
    this.mHijackFocus = paramBoolean;
    setCacheColorHint(0);
    try {
      this.mIsChildViewEnabled = android.widget.AbsListView.class.getDeclaredField("mIsChildViewEnabled");
      this.mIsChildViewEnabled.setAccessible(true);
      return;
    } catch (NoSuchFieldException paramContext) {
      paramContext.printStackTrace();
      return;
    } 
  }
  
  private void clearPressedItem() {
    this.mDrawsInPressedState = false;
    setPressed(false);
    drawableStateChanged();
    View view = getChildAt(this.mMotionPosition - getFirstVisiblePosition());
    if (view != null)
      view.setPressed(false); 
    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mClickAnimation;
    if (viewPropertyAnimatorCompat != null) {
      viewPropertyAnimatorCompat.cancel();
      this.mClickAnimation = null;
    } 
  }
  
  private void clickPressedItem(View paramView, int paramInt) { performItemClick(paramView, paramInt, getItemIdAtPosition(paramInt)); }
  
  private void drawSelectorCompat(Canvas paramCanvas) {
    if (!this.mSelectorRect.isEmpty()) {
      Drawable drawable = getSelector();
      if (drawable != null) {
        drawable.setBounds(this.mSelectorRect);
        drawable.draw(paramCanvas);
      } 
    } 
  }
  
  private void positionSelectorCompat(int paramInt, View paramView) {
    Rect rect = this.mSelectorRect;
    rect.set(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
    rect.left -= this.mSelectionLeftPadding;
    rect.top -= this.mSelectionTopPadding;
    rect.right += this.mSelectionRightPadding;
    rect.bottom += this.mSelectionBottomPadding;
    try {
      boolean bool = this.mIsChildViewEnabled.getBoolean(this);
      if (paramView.isEnabled() != bool) {
        Field field = this.mIsChildViewEnabled;
        if (!bool) {
          bool = true;
        } else {
          bool = false;
        } 
        field.set(this, Boolean.valueOf(bool));
        if (paramInt != -1)
          refreshDrawableState(); 
      } 
      return;
    } catch (IllegalAccessException paramView) {
      paramView.printStackTrace();
      return;
    } 
  }
  
  private void positionSelectorLikeFocusCompat(int paramInt, View paramView) {
    boolean bool;
    Drawable drawable = getSelector();
    boolean bool1 = true;
    if (drawable != null && paramInt != -1) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool)
      drawable.setVisible(false, false); 
    positionSelectorCompat(paramInt, paramView);
    if (bool) {
      Rect rect = this.mSelectorRect;
      float f1 = rect.exactCenterX();
      float f2 = rect.exactCenterY();
      if (getVisibility() != 0)
        bool1 = false; 
      drawable.setVisible(bool1, false);
      DrawableCompat.setHotspot(drawable, f1, f2);
    } 
  }
  
  private void positionSelectorLikeTouchCompat(int paramInt, View paramView, float paramFloat1, float paramFloat2) {
    positionSelectorLikeFocusCompat(paramInt, paramView);
    Drawable drawable = getSelector();
    if (drawable != null && paramInt != -1)
      DrawableCompat.setHotspot(drawable, paramFloat1, paramFloat2); 
  }
  
  private void setPressedItem(View paramView, int paramInt, float paramFloat1, float paramFloat2) {
    this.mDrawsInPressedState = true;
    if (Build.VERSION.SDK_INT >= 21)
      drawableHotspotChanged(paramFloat1, paramFloat2); 
    if (!isPressed())
      setPressed(true); 
    layoutChildren();
    int i = this.mMotionPosition;
    if (i != -1) {
      View view = getChildAt(i - getFirstVisiblePosition());
      if (view != null && view != paramView && view.isPressed())
        view.setPressed(false); 
    } 
    this.mMotionPosition = paramInt;
    float f1 = paramView.getLeft();
    float f2 = paramView.getTop();
    if (Build.VERSION.SDK_INT >= 21)
      paramView.drawableHotspotChanged(paramFloat1 - f1, paramFloat2 - f2); 
    if (!paramView.isPressed())
      paramView.setPressed(true); 
    positionSelectorLikeTouchCompat(paramInt, paramView, paramFloat1, paramFloat2);
    setSelectorEnabled(false);
    refreshDrawableState();
  }
  
  private void setSelectorEnabled(boolean paramBoolean) {
    GateKeeperDrawable gateKeeperDrawable = this.mSelector;
    if (gateKeeperDrawable != null)
      gateKeeperDrawable.setEnabled(paramBoolean); 
  }
  
  private boolean touchModeDrawsInPressedStateCompat() { return this.mDrawsInPressedState; }
  
  private void updateSelectorStateCompat() {
    Drawable drawable = getSelector();
    if (drawable != null && touchModeDrawsInPressedStateCompat() && isPressed())
      drawable.setState(getDrawableState()); 
  }
  
  protected void dispatchDraw(Canvas paramCanvas) {
    drawSelectorCompat(paramCanvas);
    super.dispatchDraw(paramCanvas);
  }
  
  protected void drawableStateChanged() {
    if (this.mResolveHoverRunnable != null)
      return; 
    super.drawableStateChanged();
    setSelectorEnabled(true);
    updateSelectorStateCompat();
  }
  
  public boolean hasFocus() { return (this.mHijackFocus || super.hasFocus()); }
  
  public boolean hasWindowFocus() { return (this.mHijackFocus || super.hasWindowFocus()); }
  
  public boolean isFocused() { return (this.mHijackFocus || super.isFocused()); }
  
  public boolean isInTouchMode() { return ((this.mHijackFocus && this.mListSelectionHidden) || super.isInTouchMode()); }
  
  public int lookForSelectablePosition(int paramInt, boolean paramBoolean) {
    ListAdapter listAdapter = getAdapter();
    if (listAdapter != null) {
      if (isInTouchMode())
        return -1; 
      int i = listAdapter.getCount();
      if (!getAdapter().areAllItemsEnabled()) {
        int j;
        if (paramBoolean) {
          paramInt = Math.max(0, paramInt);
          while (true) {
            j = paramInt;
            if (paramInt < i) {
              j = paramInt;
              if (!listAdapter.isEnabled(paramInt)) {
                paramInt++;
                continue;
              } 
            } 
            break;
          } 
        } else {
          paramInt = Math.min(paramInt, i - 1);
          while (true) {
            j = paramInt;
            if (paramInt >= 0) {
              j = paramInt;
              if (!listAdapter.isEnabled(paramInt)) {
                paramInt--;
                continue;
              } 
            } 
            break;
          } 
        } 
        return (j >= 0) ? ((j >= i) ? -1 : j) : -1;
      } 
      return (paramInt >= 0) ? ((paramInt >= i) ? -1 : paramInt) : -1;
    } 
    return -1;
  }
  
  public int measureHeightOfChildrenCompat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    int k = getListPaddingTop();
    int j = getListPaddingBottom();
    getListPaddingLeft();
    getListPaddingRight();
    paramInt3 = getDividerHeight();
    Drawable drawable = getDivider();
    ListAdapter listAdapter = getAdapter();
    if (listAdapter == null)
      return k + j; 
    if (paramInt3 <= 0 || drawable == null)
      paramInt3 = 0; 
    drawable = null;
    int n = listAdapter.getCount();
    int m = 0;
    int i = 0;
    paramInt2 = k + j;
    byte b = 0;
    while (b < n) {
      int i2 = listAdapter.getItemViewType(b);
      int i1 = m;
      if (i2 != m) {
        drawable = null;
        i1 = i2;
      } 
      View view2 = listAdapter.getView(b, drawable, this);
      ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
      if (layoutParams == null) {
        layoutParams = generateDefaultLayoutParams();
        view2.setLayoutParams(layoutParams);
      } 
      if (layoutParams.height > 0) {
        m = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
      } else {
        m = View.MeasureSpec.makeMeasureSpec(0, 0);
      } 
      view2.measure(paramInt1, m);
      view2.forceLayout();
      m = paramInt2;
      if (b > 0)
        m = paramInt2 + paramInt3; 
      paramInt2 = m + view2.getMeasuredHeight();
      if (paramInt2 >= paramInt4)
        return (paramInt5 >= 0 && b > paramInt5 && i && paramInt2 != paramInt4) ? i : paramInt4; 
      i2 = i;
      if (paramInt5 >= 0) {
        i2 = i;
        if (b >= paramInt5)
          i2 = paramInt2; 
      } 
      b++;
      m = i1;
      View view1 = view2;
      i = i2;
    } 
    return paramInt2;
  }
  
  protected void onDetachedFromWindow() {
    this.mResolveHoverRunnable = null;
    super.onDetachedFromWindow();
  }
  
  public boolean onForwardedEvent(MotionEvent paramMotionEvent, int paramInt) {
    View view;
    int m;
    int k;
    byte b = 1;
    int n = 1;
    int i = 0;
    int j = paramMotionEvent.getActionMasked();
    switch (j) {
      default:
        n = b;
        paramInt = i;
        break;
      case 3:
        n = 0;
        paramInt = i;
        break;
      case 1:
        n = 0;
      case 2:
        k = paramMotionEvent.findPointerIndex(paramInt);
        if (k < 0) {
          n = 0;
          paramInt = i;
          break;
        } 
        paramInt = (int)paramMotionEvent.getX(k);
        m = (int)paramMotionEvent.getY(k);
        k = pointToPosition(paramInt, m);
        if (k == -1) {
          paramInt = 1;
          break;
        } 
        view = getChildAt(k - getFirstVisiblePosition());
        setPressedItem(view, k, paramInt, m);
        b = 1;
        n = b;
        paramInt = i;
        if (j == 1) {
          clickPressedItem(view, k);
          paramInt = i;
          n = b;
        } 
        break;
    } 
    if (n == 0 || paramInt != 0)
      clearPressedItem(); 
    if (n != 0) {
      if (this.mScrollHelper == null)
        this.mScrollHelper = new ListViewAutoScrollHelper(this); 
      this.mScrollHelper.setEnabled(true);
      this.mScrollHelper.onTouch(this, paramMotionEvent);
      return n;
    } 
    ListViewAutoScrollHelper listViewAutoScrollHelper = this.mScrollHelper;
    if (listViewAutoScrollHelper != null)
      listViewAutoScrollHelper.setEnabled(false); 
    return n;
  }
  
  public boolean onHoverEvent(@NonNull MotionEvent paramMotionEvent) {
    if (Build.VERSION.SDK_INT < 26)
      return super.onHoverEvent(paramMotionEvent); 
    int i = paramMotionEvent.getActionMasked();
    if (i == 10 && this.mResolveHoverRunnable == null) {
      this.mResolveHoverRunnable = new ResolveHoverRunnable();
      this.mResolveHoverRunnable.post();
    } 
    boolean bool = super.onHoverEvent(paramMotionEvent);
    if (i == 9 || i == 7) {
      i = pointToPosition((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
      if (i != -1 && i != getSelectedItemPosition()) {
        View view = getChildAt(i - getFirstVisiblePosition());
        if (view.isEnabled())
          setSelectionFromTop(i, view.getTop() - getTop()); 
        updateSelectorStateCompat();
      } 
      return bool;
    } 
    setSelection(-1);
    return bool;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (paramMotionEvent.getAction() == 0)
      this.mMotionPosition = pointToPosition((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY()); 
    ResolveHoverRunnable resolveHoverRunnable = this.mResolveHoverRunnable;
    if (resolveHoverRunnable != null)
      resolveHoverRunnable.cancel(); 
    return super.onTouchEvent(paramMotionEvent);
  }
  
  void setListSelectionHidden(boolean paramBoolean) { this.mListSelectionHidden = paramBoolean; }
  
  public void setSelector(Drawable paramDrawable) {
    if (paramDrawable != null) {
      rect = new GateKeeperDrawable(paramDrawable);
    } else {
      rect = null;
    } 
    this.mSelector = rect;
    super.setSelector(this.mSelector);
    Rect rect = new Rect();
    if (paramDrawable != null)
      paramDrawable.getPadding(rect); 
    this.mSelectionLeftPadding = rect.left;
    this.mSelectionTopPadding = rect.top;
    this.mSelectionRightPadding = rect.right;
    this.mSelectionBottomPadding = rect.bottom;
  }
  
  private static class GateKeeperDrawable extends DrawableWrapper {
    private boolean mEnabled = true;
    
    GateKeeperDrawable(Drawable param1Drawable) { super(param1Drawable); }
    
    public void draw(Canvas param1Canvas) {
      if (this.mEnabled)
        super.draw(param1Canvas); 
    }
    
    void setEnabled(boolean param1Boolean) { this.mEnabled = param1Boolean; }
    
    public void setHotspot(float param1Float1, float param1Float2) {
      if (this.mEnabled)
        super.setHotspot(param1Float1, param1Float2); 
    }
    
    public void setHotspotBounds(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      if (this.mEnabled)
        super.setHotspotBounds(param1Int1, param1Int2, param1Int3, param1Int4); 
    }
    
    public boolean setState(int[] param1ArrayOfInt) { return this.mEnabled ? super.setState(param1ArrayOfInt) : 0; }
    
    public boolean setVisible(boolean param1Boolean1, boolean param1Boolean2) { return this.mEnabled ? super.setVisible(param1Boolean1, param1Boolean2) : 0; }
  }
  
  private class ResolveHoverRunnable implements Runnable {
    public void cancel() {
      DropDownListView dropDownListView = DropDownListView.this;
      dropDownListView.mResolveHoverRunnable = null;
      dropDownListView.removeCallbacks(this);
    }
    
    public void post() { DropDownListView.this.post(this); }
    
    public void run() {
      DropDownListView dropDownListView = DropDownListView.this;
      dropDownListView.mResolveHoverRunnable = null;
      dropDownListView.drawableStateChanged();
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\DropDownListView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */