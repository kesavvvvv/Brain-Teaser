package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionMode;
import android.support.v7.view.menu.MenuBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ActionBarContextView extends AbsActionBarView {
  private static final String TAG = "ActionBarContextView";
  
  private View mClose;
  
  private int mCloseItemLayout;
  
  private View mCustomView;
  
  private CharSequence mSubtitle;
  
  private int mSubtitleStyleRes;
  
  private TextView mSubtitleView;
  
  private CharSequence mTitle;
  
  private LinearLayout mTitleLayout;
  
  private boolean mTitleOptional;
  
  private int mTitleStyleRes;
  
  private TextView mTitleView;
  
  public ActionBarContextView(Context paramContext) { this(paramContext, null); }
  
  public ActionBarContextView(Context paramContext, AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, R.attr.actionModeStyle); }
  
  public ActionBarContextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.ActionMode, paramInt, 0);
    ViewCompat.setBackground(this, tintTypedArray.getDrawable(R.styleable.ActionMode_background));
    this.mTitleStyleRes = tintTypedArray.getResourceId(R.styleable.ActionMode_titleTextStyle, 0);
    this.mSubtitleStyleRes = tintTypedArray.getResourceId(R.styleable.ActionMode_subtitleTextStyle, 0);
    this.mContentHeight = tintTypedArray.getLayoutDimension(R.styleable.ActionMode_height, 0);
    this.mCloseItemLayout = tintTypedArray.getResourceId(R.styleable.ActionMode_closeItemLayout, R.layout.abc_action_mode_close_item_material);
    tintTypedArray.recycle();
  }
  
  private void initTitle() {
    if (this.mTitleLayout == null) {
      LayoutInflater.from(getContext()).inflate(R.layout.abc_action_bar_title_item, this);
      this.mTitleLayout = (LinearLayout)getChildAt(getChildCount() - 1);
      this.mTitleView = (TextView)this.mTitleLayout.findViewById(R.id.action_bar_title);
      this.mSubtitleView = (TextView)this.mTitleLayout.findViewById(R.id.action_bar_subtitle);
      if (this.mTitleStyleRes != 0)
        this.mTitleView.setTextAppearance(getContext(), this.mTitleStyleRes); 
      if (this.mSubtitleStyleRes != 0)
        this.mSubtitleView.setTextAppearance(getContext(), this.mSubtitleStyleRes); 
    } 
    this.mTitleView.setText(this.mTitle);
    this.mSubtitleView.setText(this.mSubtitle);
    boolean bool2 = TextUtils.isEmpty(this.mTitle);
    boolean bool1 = TextUtils.isEmpty(this.mSubtitle) ^ true;
    TextView textView = this.mSubtitleView;
    byte b2 = 0;
    if (bool1) {
      b1 = 0;
    } else {
      b1 = 8;
    } 
    textView.setVisibility(b1);
    LinearLayout linearLayout = this.mTitleLayout;
    byte b1 = b2;
    if (!(bool2 ^ true))
      if (bool1) {
        b1 = b2;
      } else {
        b1 = 8;
      }  
    linearLayout.setVisibility(b1);
    if (this.mTitleLayout.getParent() == null)
      addView(this.mTitleLayout); 
  }
  
  public void closeMode() {
    if (this.mClose == null) {
      killMode();
      return;
    } 
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() { return new ViewGroup.MarginLayoutParams(-1, -2); }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) { return new ViewGroup.MarginLayoutParams(getContext(), paramAttributeSet); }
  
  public CharSequence getSubtitle() { return this.mSubtitle; }
  
  public CharSequence getTitle() { return this.mTitle; }
  
  public boolean hideOverflowMenu() { return (this.mActionMenuPresenter != null) ? this.mActionMenuPresenter.hideOverflowMenu() : 0; }
  
  public void initForMode(final ActionMode mode) {
    View view = this.mClose;
    if (view == null) {
      this.mClose = LayoutInflater.from(getContext()).inflate(this.mCloseItemLayout, this, false);
      addView(this.mClose);
    } else if (view.getParent() == null) {
      addView(this.mClose);
    } 
    this.mClose.findViewById(R.id.action_mode_close_button).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) { mode.finish(); }
        });
    MenuBuilder menuBuilder = (MenuBuilder)paramActionMode.getMenu();
    if (this.mActionMenuPresenter != null)
      this.mActionMenuPresenter.dismissPopupMenus(); 
    this.mActionMenuPresenter = new ActionMenuPresenter(getContext());
    this.mActionMenuPresenter.setReserveOverflow(true);
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -1);
    menuBuilder.addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
    this.mMenuView = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
    ViewCompat.setBackground(this.mMenuView, null);
    addView(this.mMenuView, layoutParams);
  }
  
  public boolean isOverflowMenuShowing() { return (this.mActionMenuPresenter != null) ? this.mActionMenuPresenter.isOverflowMenuShowing() : 0; }
  
  public boolean isTitleOptional() { return this.mTitleOptional; }
  
  public void killMode() {
    removeAllViews();
    this.mCustomView = null;
    this.mMenuView = null;
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (this.mActionMenuPresenter != null) {
      this.mActionMenuPresenter.hideOverflowMenu();
      this.mActionMenuPresenter.hideSubMenus();
    } 
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    if (paramAccessibilityEvent.getEventType() == 32) {
      paramAccessibilityEvent.setSource(this);
      paramAccessibilityEvent.setClassName(getClass().getName());
      paramAccessibilityEvent.setPackageName(getContext().getPackageName());
      paramAccessibilityEvent.setContentDescription(this.mTitle);
      return;
    } 
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i;
    paramBoolean = ViewUtils.isLayoutRtl(this);
    if (paramBoolean) {
      i = paramInt3 - paramInt1 - getPaddingRight();
    } else {
      i = getPaddingLeft();
    } 
    int j = getPaddingTop();
    int k = paramInt4 - paramInt2 - getPaddingTop() - getPaddingBottom();
    View view2 = this.mClose;
    if (view2 != null && view2.getVisibility() != 8) {
      ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
      if (paramBoolean) {
        paramInt2 = marginLayoutParams.rightMargin;
      } else {
        paramInt2 = marginLayoutParams.leftMargin;
      } 
      if (paramBoolean) {
        paramInt4 = marginLayoutParams.leftMargin;
      } else {
        paramInt4 = marginLayoutParams.rightMargin;
      } 
      paramInt2 = next(i, paramInt2, paramBoolean);
      paramInt2 = next(paramInt2 + positionChild(this.mClose, paramInt2, j, k, paramBoolean), paramInt4, paramBoolean);
    } else {
      paramInt2 = i;
    } 
    LinearLayout linearLayout = this.mTitleLayout;
    paramInt4 = paramInt2;
    if (linearLayout != null) {
      paramInt4 = paramInt2;
      if (this.mCustomView == null) {
        paramInt4 = paramInt2;
        if (linearLayout.getVisibility() != 8)
          paramInt4 = paramInt2 + positionChild(this.mTitleLayout, paramInt2, j, k, paramBoolean); 
      } 
    } 
    View view1 = this.mCustomView;
    if (view1 != null)
      positionChild(view1, paramInt4, j, k, paramBoolean); 
    if (paramBoolean) {
      paramInt1 = getPaddingLeft();
    } else {
      paramInt1 = paramInt3 - paramInt1 - getPaddingRight();
    } 
    if (this.mMenuView != null)
      positionChild(this.mMenuView, paramInt1, j, k, paramBoolean ^ true); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = 1073741824;
    if (i == 1073741824) {
      if (View.MeasureSpec.getMode(paramInt2) != 0) {
        int i1 = View.MeasureSpec.getSize(paramInt1);
        if (this.mContentHeight > 0) {
          i = this.mContentHeight;
        } else {
          i = View.MeasureSpec.getSize(paramInt2);
        } 
        int i2 = getPaddingTop() + getPaddingBottom();
        paramInt1 = i1 - getPaddingLeft() - getPaddingRight();
        int m = i - i2;
        int k = View.MeasureSpec.makeMeasureSpec(m, -2147483648);
        View view2 = this.mClose;
        int n = 0;
        paramInt2 = paramInt1;
        if (view2 != null) {
          paramInt1 = measureChildView(view2, paramInt1, k, 0);
          ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
          paramInt2 = paramInt1 - marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
        } 
        paramInt1 = paramInt2;
        if (this.mMenuView != null) {
          paramInt1 = paramInt2;
          if (this.mMenuView.getParent() == this)
            paramInt1 = measureChildView(this.mMenuView, paramInt2, k, 0); 
        } 
        LinearLayout linearLayout = this.mTitleLayout;
        paramInt2 = paramInt1;
        if (linearLayout != null) {
          paramInt2 = paramInt1;
          if (this.mCustomView == null)
            if (this.mTitleOptional) {
              paramInt2 = View.MeasureSpec.makeMeasureSpec(0, 0);
              this.mTitleLayout.measure(paramInt2, k);
              int i3 = this.mTitleLayout.getMeasuredWidth();
              if (i3 <= paramInt1) {
                k = 1;
              } else {
                k = 0;
              } 
              paramInt2 = paramInt1;
              if (k != 0)
                paramInt2 = paramInt1 - i3; 
              linearLayout = this.mTitleLayout;
              if (k != 0) {
                paramInt1 = n;
              } else {
                paramInt1 = 8;
              } 
              linearLayout.setVisibility(paramInt1);
            } else {
              paramInt2 = measureChildView(linearLayout, paramInt1, k, 0);
            }  
        } 
        View view1 = this.mCustomView;
        if (view1 != null) {
          ViewGroup.LayoutParams layoutParams = view1.getLayoutParams();
          if (layoutParams.width != -2) {
            paramInt1 = 1073741824;
          } else {
            paramInt1 = Integer.MIN_VALUE;
          } 
          if (layoutParams.width >= 0)
            paramInt2 = Math.min(layoutParams.width, paramInt2); 
          if (layoutParams.height != -2) {
            k = j;
          } else {
            k = Integer.MIN_VALUE;
          } 
          if (layoutParams.height >= 0) {
            j = Math.min(layoutParams.height, m);
          } else {
            j = m;
          } 
          this.mCustomView.measure(View.MeasureSpec.makeMeasureSpec(paramInt2, paramInt1), View.MeasureSpec.makeMeasureSpec(j, k));
        } 
        if (this.mContentHeight <= 0) {
          paramInt2 = 0;
          j = getChildCount();
          paramInt1 = 0;
          while (paramInt1 < j) {
            k = getChildAt(paramInt1).getMeasuredHeight() + i2;
            i = paramInt2;
            if (k > paramInt2)
              i = k; 
            paramInt1++;
            paramInt2 = i;
          } 
          setMeasuredDimension(i1, paramInt2);
          return;
        } 
        setMeasuredDimension(i1, i);
        return;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(getClass().getSimpleName());
      stringBuilder1.append(" can only be used ");
      stringBuilder1.append("with android:layout_height=\"wrap_content\"");
      throw new IllegalStateException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getClass().getSimpleName());
    stringBuilder.append(" can only be used ");
    stringBuilder.append("with android:layout_width=\"match_parent\" (or fill_parent)");
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public void setContentHeight(int paramInt) { this.mContentHeight = paramInt; }
  
  public void setCustomView(View paramView) {
    View view = this.mCustomView;
    if (view != null)
      removeView(view); 
    this.mCustomView = paramView;
    if (paramView != null) {
      LinearLayout linearLayout = this.mTitleLayout;
      if (linearLayout != null) {
        removeView(linearLayout);
        this.mTitleLayout = null;
      } 
    } 
    if (paramView != null)
      addView(paramView); 
    requestLayout();
  }
  
  public void setSubtitle(CharSequence paramCharSequence) {
    this.mSubtitle = paramCharSequence;
    initTitle();
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    this.mTitle = paramCharSequence;
    initTitle();
  }
  
  public void setTitleOptional(boolean paramBoolean) {
    if (paramBoolean != this.mTitleOptional)
      requestLayout(); 
    this.mTitleOptional = paramBoolean;
  }
  
  public boolean shouldDelayChildPressedState() { return false; }
  
  public boolean showOverflowMenu() { return (this.mActionMenuPresenter != null) ? this.mActionMenuPresenter.showOverflowMenu() : 0; }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\ActionBarContextView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */