package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Toolbar extends ViewGroup {
  private static final String TAG = "Toolbar";
  
  private MenuPresenter.Callback mActionMenuPresenterCallback;
  
  int mButtonGravity;
  
  ImageButton mCollapseButtonView;
  
  private CharSequence mCollapseDescription;
  
  private Drawable mCollapseIcon;
  
  private boolean mCollapsible;
  
  private int mContentInsetEndWithActions;
  
  private int mContentInsetStartWithNavigation;
  
  private RtlSpacingHelper mContentInsets;
  
  private boolean mEatingHover;
  
  private boolean mEatingTouch;
  
  View mExpandedActionView;
  
  private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
  
  private int mGravity = 8388627;
  
  private final ArrayList<View> mHiddenViews = new ArrayList();
  
  private ImageView mLogoView;
  
  private int mMaxButtonHeight;
  
  private MenuBuilder.Callback mMenuBuilderCallback;
  
  private ActionMenuView mMenuView;
  
  private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem param1MenuItem) { return (Toolbar.this.mOnMenuItemClickListener != null) ? Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(param1MenuItem) : 0; }
    };
  
  private ImageButton mNavButtonView;
  
  OnMenuItemClickListener mOnMenuItemClickListener;
  
  private ActionMenuPresenter mOuterActionMenuPresenter;
  
  private Context mPopupContext;
  
  private int mPopupTheme;
  
  private final Runnable mShowOverflowMenuRunnable = new Runnable() {
      public void run() { Toolbar.this.showOverflowMenu(); }
    };
  
  private CharSequence mSubtitleText;
  
  private int mSubtitleTextAppearance;
  
  private int mSubtitleTextColor;
  
  private TextView mSubtitleTextView;
  
  private final int[] mTempMargins = new int[2];
  
  private final ArrayList<View> mTempViews = new ArrayList();
  
  private int mTitleMarginBottom;
  
  private int mTitleMarginEnd;
  
  private int mTitleMarginStart;
  
  private int mTitleMarginTop;
  
  private CharSequence mTitleText;
  
  private int mTitleTextAppearance;
  
  private int mTitleTextColor;
  
  private TextView mTitleTextView;
  
  private ToolbarWidgetWrapper mWrapper;
  
  public Toolbar(Context paramContext) { this(paramContext, null); }
  
  public Toolbar(Context paramContext, @Nullable AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, R.attr.toolbarStyle); }
  
  public Toolbar(Context paramContext, @Nullable AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(), paramAttributeSet, R.styleable.Toolbar, paramInt, 0);
    this.mTitleTextAppearance = tintTypedArray.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
    this.mSubtitleTextAppearance = tintTypedArray.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
    this.mGravity = tintTypedArray.getInteger(R.styleable.Toolbar_android_gravity, this.mGravity);
    this.mButtonGravity = tintTypedArray.getInteger(R.styleable.Toolbar_buttonGravity, 48);
    int i = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMargin, 0);
    paramInt = i;
    if (tintTypedArray.hasValue(R.styleable.Toolbar_titleMargins))
      paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMargins, i); 
    this.mTitleMarginBottom = paramInt;
    this.mTitleMarginTop = paramInt;
    this.mTitleMarginEnd = paramInt;
    this.mTitleMarginStart = paramInt;
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
    if (paramInt >= 0)
      this.mTitleMarginStart = paramInt; 
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1);
    if (paramInt >= 0)
      this.mTitleMarginEnd = paramInt; 
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1);
    if (paramInt >= 0)
      this.mTitleMarginTop = paramInt; 
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom, -1);
    if (paramInt >= 0)
      this.mTitleMarginBottom = paramInt; 
    this.mMaxButtonHeight = tintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStart, -2147483648);
    i = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEnd, -2147483648);
    int j = tintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_contentInsetLeft, 0);
    int k = tintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_contentInsetRight, 0);
    ensureContentInsets();
    this.mContentInsets.setAbsolute(j, k);
    if (paramInt != Integer.MIN_VALUE || i != Integer.MIN_VALUE)
      this.mContentInsets.setRelative(paramInt, i); 
    this.mContentInsetStartWithNavigation = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStartWithNavigation, -2147483648);
    this.mContentInsetEndWithActions = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEndWithActions, -2147483648);
    this.mCollapseIcon = tintTypedArray.getDrawable(R.styleable.Toolbar_collapseIcon);
    this.mCollapseDescription = tintTypedArray.getText(R.styleable.Toolbar_collapseContentDescription);
    CharSequence charSequence3 = tintTypedArray.getText(R.styleable.Toolbar_title);
    if (!TextUtils.isEmpty(charSequence3))
      setTitle(charSequence3); 
    charSequence3 = tintTypedArray.getText(R.styleable.Toolbar_subtitle);
    if (!TextUtils.isEmpty(charSequence3))
      setSubtitle(charSequence3); 
    this.mPopupContext = getContext();
    setPopupTheme(tintTypedArray.getResourceId(R.styleable.Toolbar_popupTheme, 0));
    Drawable drawable2 = tintTypedArray.getDrawable(R.styleable.Toolbar_navigationIcon);
    if (drawable2 != null)
      setNavigationIcon(drawable2); 
    CharSequence charSequence2 = tintTypedArray.getText(R.styleable.Toolbar_navigationContentDescription);
    if (!TextUtils.isEmpty(charSequence2))
      setNavigationContentDescription(charSequence2); 
    Drawable drawable1 = tintTypedArray.getDrawable(R.styleable.Toolbar_logo);
    if (drawable1 != null)
      setLogo(drawable1); 
    CharSequence charSequence1 = tintTypedArray.getText(R.styleable.Toolbar_logoDescription);
    if (!TextUtils.isEmpty(charSequence1))
      setLogoDescription(charSequence1); 
    if (tintTypedArray.hasValue(R.styleable.Toolbar_titleTextColor))
      setTitleTextColor(tintTypedArray.getColor(R.styleable.Toolbar_titleTextColor, -1)); 
    if (tintTypedArray.hasValue(R.styleable.Toolbar_subtitleTextColor))
      setSubtitleTextColor(tintTypedArray.getColor(R.styleable.Toolbar_subtitleTextColor, -1)); 
    tintTypedArray.recycle();
  }
  
  private void addCustomViewsWithGravity(List<View> paramList, int paramInt) {
    int i = ViewCompat.getLayoutDirection(this);
    boolean bool = true;
    if (i != 1)
      bool = false; 
    int j = getChildCount();
    i = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection(this));
    paramList.clear();
    if (bool) {
      for (paramInt = j - 1; paramInt >= 0; paramInt--) {
        View view = getChildAt(paramInt);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mViewType == 0 && shouldLayout(view) && getChildHorizontalGravity(layoutParams.gravity) == i)
          paramList.add(view); 
      } 
    } else {
      for (paramInt = 0; paramInt < j; paramInt++) {
        View view = getChildAt(paramInt);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mViewType == 0 && shouldLayout(view) && getChildHorizontalGravity(layoutParams.gravity) == i)
          paramList.add(view); 
      } 
    } 
  }
  
  private void addSystemView(View paramView, boolean paramBoolean) {
    LayoutParams layoutParams = paramView.getLayoutParams();
    if (layoutParams == null) {
      layoutParams = generateDefaultLayoutParams();
    } else if (!checkLayoutParams(layoutParams)) {
      layoutParams = generateLayoutParams(layoutParams);
    } else {
      layoutParams = (LayoutParams)layoutParams;
    } 
    layoutParams.mViewType = 1;
    if (paramBoolean && this.mExpandedActionView != null) {
      paramView.setLayoutParams(layoutParams);
      this.mHiddenViews.add(paramView);
      return;
    } 
    addView(paramView, layoutParams);
  }
  
  private void ensureContentInsets() {
    if (this.mContentInsets == null)
      this.mContentInsets = new RtlSpacingHelper(); 
  }
  
  private void ensureLogoView() {
    if (this.mLogoView == null)
      this.mLogoView = new AppCompatImageView(getContext()); 
  }
  
  private void ensureMenu() {
    ensureMenuView();
    if (this.mMenuView.peekMenu() == null) {
      MenuBuilder menuBuilder = (MenuBuilder)this.mMenuView.getMenu();
      if (this.mExpandedMenuPresenter == null)
        this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(); 
      this.mMenuView.setExpandedActionViewsExclusive(true);
      menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
    } 
  }
  
  private void ensureMenuView() {
    if (this.mMenuView == null) {
      this.mMenuView = new ActionMenuView(getContext());
      this.mMenuView.setPopupTheme(this.mPopupTheme);
      this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
      this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
      LayoutParams layoutParams = generateDefaultLayoutParams();
      layoutParams.gravity = 0x800005 | this.mButtonGravity & 0x70;
      this.mMenuView.setLayoutParams(layoutParams);
      addSystemView(this.mMenuView, false);
    } 
  }
  
  private void ensureNavButtonView() {
    if (this.mNavButtonView == null) {
      this.mNavButtonView = new AppCompatImageButton(getContext(), null, R.attr.toolbarNavigationButtonStyle);
      LayoutParams layoutParams = generateDefaultLayoutParams();
      layoutParams.gravity = 0x800003 | this.mButtonGravity & 0x70;
      this.mNavButtonView.setLayoutParams(layoutParams);
    } 
  }
  
  private int getChildHorizontalGravity(int paramInt) {
    int i = ViewCompat.getLayoutDirection(this);
    int j = GravityCompat.getAbsoluteGravity(paramInt, i) & 0x7;
    if (j != 1) {
      paramInt = 3;
      if (j != 3 && j != 5) {
        if (i == 1)
          paramInt = 5; 
        return paramInt;
      } 
    } 
    return j;
  }
  
  private int getChildTop(View paramView, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int j = paramView.getMeasuredHeight();
    if (paramInt > 0) {
      paramInt = (j - paramInt) / 2;
    } else {
      paramInt = 0;
    } 
    int i = getChildVerticalGravity(layoutParams.gravity);
    if (i != 48) {
      if (i != 80) {
        int k = getPaddingTop();
        paramInt = getPaddingBottom();
        int m = getHeight();
        i = (m - k - paramInt - j) / 2;
        if (i < layoutParams.topMargin) {
          paramInt = layoutParams.topMargin;
        } else {
          j = m - paramInt - j - i - k;
          paramInt = i;
          if (j < layoutParams.bottomMargin)
            paramInt = Math.max(0, i - layoutParams.bottomMargin - j); 
        } 
        return k + paramInt;
      } 
      return getHeight() - getPaddingBottom() - j - layoutParams.bottomMargin - paramInt;
    } 
    return getPaddingTop() - paramInt;
  }
  
  private int getChildVerticalGravity(int paramInt) {
    paramInt &= 0x70;
    return (paramInt != 16 && paramInt != 48 && paramInt != 80) ? (this.mGravity & 0x70) : paramInt;
  }
  
  private int getHorizontalMargins(View paramView) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    return MarginLayoutParamsCompat.getMarginStart(marginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
  }
  
  private MenuInflater getMenuInflater() { return new SupportMenuInflater(getContext()); }
  
  private int getVerticalMargins(View paramView) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
  }
  
  private int getViewListMeasuredWidth(List<View> paramList, int[] paramArrayOfInt) {
    int k = paramArrayOfInt[0];
    int j = paramArrayOfInt[1];
    int i = 0;
    int m = paramList.size();
    for (byte b = 0; b < m; b++) {
      View view = (View)paramList.get(b);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      k = layoutParams.leftMargin - k;
      j = layoutParams.rightMargin - j;
      int n = Math.max(0, k);
      int i1 = Math.max(0, j);
      k = Math.max(0, -k);
      j = Math.max(0, -j);
      i += view.getMeasuredWidth() + n + i1;
    } 
    return i;
  }
  
  private boolean isChildOrHidden(View paramView) { return (paramView.getParent() == this || this.mHiddenViews.contains(paramView)); }
  
  private static boolean isCustomView(View paramView) { return (((LayoutParams)paramView.getLayoutParams()).mViewType == 0); }
  
  private int layoutChildLeft(View paramView, int paramInt1, int[] paramArrayOfInt, int paramInt2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = layoutParams.leftMargin - paramArrayOfInt[0];
    paramInt1 += Math.max(0, i);
    paramArrayOfInt[0] = Math.max(0, -i);
    paramInt2 = getChildTop(paramView, paramInt2);
    i = paramView.getMeasuredWidth();
    paramView.layout(paramInt1, paramInt2, paramInt1 + i, paramView.getMeasuredHeight() + paramInt2);
    return paramInt1 + layoutParams.rightMargin + i;
  }
  
  private int layoutChildRight(View paramView, int paramInt1, int[] paramArrayOfInt, int paramInt2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = layoutParams.rightMargin - paramArrayOfInt[1];
    paramInt1 -= Math.max(0, i);
    paramArrayOfInt[1] = Math.max(0, -i);
    paramInt2 = getChildTop(paramView, paramInt2);
    i = paramView.getMeasuredWidth();
    paramView.layout(paramInt1 - i, paramInt2, paramInt1, paramView.getMeasuredHeight() + paramInt2);
    return paramInt1 - layoutParams.leftMargin + i;
  }
  
  private int measureChildCollapseMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    int i = marginLayoutParams.leftMargin - paramArrayOfInt[0];
    int j = marginLayoutParams.rightMargin - paramArrayOfInt[1];
    int k = Math.max(0, i) + Math.max(0, j);
    paramArrayOfInt[0] = Math.max(0, -i);
    paramArrayOfInt[1] = Math.max(0, -j);
    paramView.measure(getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + k + paramInt2, marginLayoutParams.width), getChildMeasureSpec(paramInt3, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + paramInt4, marginLayoutParams.height));
    return paramView.getMeasuredWidth() + k;
  }
  
  private void measureChildConstrained(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    int i = getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + paramInt2, marginLayoutParams.width);
    paramInt2 = getChildMeasureSpec(paramInt3, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + paramInt4, marginLayoutParams.height);
    paramInt3 = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = paramInt2;
    if (paramInt3 != 1073741824) {
      paramInt1 = paramInt2;
      if (paramInt5 >= 0) {
        if (paramInt3 != 0)
          paramInt5 = Math.min(View.MeasureSpec.getSize(paramInt2), paramInt5); 
        paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt5, 1073741824);
      } 
    } 
    paramView.measure(i, paramInt1);
  }
  
  private void postShowOverflowMenu() {
    removeCallbacks(this.mShowOverflowMenuRunnable);
    post(this.mShowOverflowMenuRunnable);
  }
  
  private boolean shouldCollapse() {
    if (!this.mCollapsible)
      return false; 
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (shouldLayout(view) && view.getMeasuredWidth() > 0 && view.getMeasuredHeight() > 0)
        return false; 
    } 
    return true;
  }
  
  private boolean shouldLayout(View paramView) { return (paramView != null && paramView.getParent() == this && paramView.getVisibility() != 8); }
  
  void addChildrenForExpandedActionView() {
    for (int i = this.mHiddenViews.size() - 1; i >= 0; i--)
      addView((View)this.mHiddenViews.get(i)); 
    this.mHiddenViews.clear();
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean canShowOverflowMenu() {
    if (getVisibility() == 0) {
      ActionMenuView actionMenuView = this.mMenuView;
      if (actionMenuView != null && actionMenuView.isOverflowReserved())
        return true; 
    } 
    return false;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return (super.checkLayoutParams(paramLayoutParams) && paramLayoutParams instanceof LayoutParams); }
  
  public void collapseActionView() {
    MenuItemImpl menuItemImpl = this.mExpandedMenuPresenter;
    if (menuItemImpl == null) {
      menuItemImpl = null;
    } else {
      menuItemImpl = menuItemImpl.mCurrentExpandedItem;
    } 
    if (menuItemImpl != null)
      menuItemImpl.collapseActionView(); 
  }
  
  public void dismissPopupMenus() {
    ActionMenuView actionMenuView = this.mMenuView;
    if (actionMenuView != null)
      actionMenuView.dismissPopupMenus(); 
  }
  
  void ensureCollapseButtonView() {
    if (this.mCollapseButtonView == null) {
      this.mCollapseButtonView = new AppCompatImageButton(getContext(), null, R.attr.toolbarNavigationButtonStyle);
      this.mCollapseButtonView.setImageDrawable(this.mCollapseIcon);
      this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
      LayoutParams layoutParams = generateDefaultLayoutParams();
      layoutParams.gravity = 0x800003 | this.mButtonGravity & 0x70;
      layoutParams.mViewType = 2;
      this.mCollapseButtonView.setLayoutParams(layoutParams);
      this.mCollapseButtonView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { Toolbar.this.collapseActionView(); }
          });
    } 
  }
  
  protected LayoutParams generateDefaultLayoutParams() { return new LayoutParams(-2, -2); }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) { return new LayoutParams(getContext(), paramAttributeSet); }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return (paramLayoutParams instanceof LayoutParams) ? new LayoutParams((LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ActionBar.LayoutParams) ? new LayoutParams((ActionBar.LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams))); }
  
  public int getContentInsetEnd() {
    RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
    return (rtlSpacingHelper != null) ? rtlSpacingHelper.getEnd() : 0;
  }
  
  public int getContentInsetEndWithActions() {
    int i = this.mContentInsetEndWithActions;
    return (i != Integer.MIN_VALUE) ? i : getContentInsetEnd();
  }
  
  public int getContentInsetLeft() {
    RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
    return (rtlSpacingHelper != null) ? rtlSpacingHelper.getLeft() : 0;
  }
  
  public int getContentInsetRight() {
    RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
    return (rtlSpacingHelper != null) ? rtlSpacingHelper.getRight() : 0;
  }
  
  public int getContentInsetStart() {
    RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
    return (rtlSpacingHelper != null) ? rtlSpacingHelper.getStart() : 0;
  }
  
  public int getContentInsetStartWithNavigation() {
    int i = this.mContentInsetStartWithNavigation;
    return (i != Integer.MIN_VALUE) ? i : getContentInsetStart();
  }
  
  public int getCurrentContentInsetEnd() {
    boolean bool = false;
    ActionMenuView actionMenuView = this.mMenuView;
    if (actionMenuView != null) {
      MenuBuilder menuBuilder = actionMenuView.peekMenu();
      if (menuBuilder != null && menuBuilder.hasVisibleItems()) {
        bool = true;
      } else {
        bool = false;
      } 
    } 
    return bool ? Math.max(getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0)) : getContentInsetEnd();
  }
  
  public int getCurrentContentInsetLeft() { return (ViewCompat.getLayoutDirection(this) == 1) ? getCurrentContentInsetEnd() : getCurrentContentInsetStart(); }
  
  public int getCurrentContentInsetRight() { return (ViewCompat.getLayoutDirection(this) == 1) ? getCurrentContentInsetStart() : getCurrentContentInsetEnd(); }
  
  public int getCurrentContentInsetStart() { return (getNavigationIcon() != null) ? Math.max(getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0)) : getContentInsetStart(); }
  
  public Drawable getLogo() {
    ImageView imageView = this.mLogoView;
    return (imageView != null) ? imageView.getDrawable() : null;
  }
  
  public CharSequence getLogoDescription() {
    ImageView imageView = this.mLogoView;
    return (imageView != null) ? imageView.getContentDescription() : null;
  }
  
  public Menu getMenu() {
    ensureMenu();
    return this.mMenuView.getMenu();
  }
  
  @Nullable
  public CharSequence getNavigationContentDescription() {
    ImageButton imageButton = this.mNavButtonView;
    return (imageButton != null) ? imageButton.getContentDescription() : null;
  }
  
  @Nullable
  public Drawable getNavigationIcon() {
    ImageButton imageButton = this.mNavButtonView;
    return (imageButton != null) ? imageButton.getDrawable() : null;
  }
  
  ActionMenuPresenter getOuterActionMenuPresenter() { return this.mOuterActionMenuPresenter; }
  
  @Nullable
  public Drawable getOverflowIcon() {
    ensureMenu();
    return this.mMenuView.getOverflowIcon();
  }
  
  Context getPopupContext() { return this.mPopupContext; }
  
  public int getPopupTheme() { return this.mPopupTheme; }
  
  public CharSequence getSubtitle() { return this.mSubtitleText; }
  
  public CharSequence getTitle() { return this.mTitleText; }
  
  public int getTitleMarginBottom() { return this.mTitleMarginBottom; }
  
  public int getTitleMarginEnd() { return this.mTitleMarginEnd; }
  
  public int getTitleMarginStart() { return this.mTitleMarginStart; }
  
  public int getTitleMarginTop() { return this.mTitleMarginTop; }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public DecorToolbar getWrapper() {
    if (this.mWrapper == null)
      this.mWrapper = new ToolbarWidgetWrapper(this, true); 
    return this.mWrapper;
  }
  
  public boolean hasExpandedActionView() {
    ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
    return (expandedActionViewMenuPresenter != null && expandedActionViewMenuPresenter.mCurrentExpandedItem != null);
  }
  
  public boolean hideOverflowMenu() {
    ActionMenuView actionMenuView = this.mMenuView;
    return (actionMenuView != null && actionMenuView.hideOverflowMenu());
  }
  
  public void inflateMenu(@MenuRes int paramInt) { getMenuInflater().inflate(paramInt, getMenu()); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean isOverflowMenuShowPending() {
    ActionMenuView actionMenuView = this.mMenuView;
    return (actionMenuView != null && actionMenuView.isOverflowMenuShowPending());
  }
  
  public boolean isOverflowMenuShowing() {
    ActionMenuView actionMenuView = this.mMenuView;
    return (actionMenuView != null && actionMenuView.isOverflowMenuShowing());
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean isTitleTruncated() {
    TextView textView = this.mTitleTextView;
    if (textView == null)
      return false; 
    Layout layout = textView.getLayout();
    if (layout == null)
      return false; 
    int i = layout.getLineCount();
    for (byte b = 0; b < i; b++) {
      if (layout.getEllipsisCount(b) > 0)
        return true; 
    } 
    return false;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    removeCallbacks(this.mShowOverflowMenuRunnable);
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
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int k;
    if (ViewCompat.getLayoutDirection(this) == 1) {
      k = 1;
    } else {
      k = 0;
    } 
    int i2 = getWidth();
    int i1 = getHeight();
    int j = getPaddingLeft();
    int i3 = getPaddingRight();
    int n = getPaddingTop();
    int i4 = getPaddingBottom();
    int i = j;
    int m = i2 - i3;
    int[] arrayOfInt = this.mTempMargins;
    arrayOfInt[1] = 0;
    arrayOfInt[0] = 0;
    paramInt1 = ViewCompat.getMinimumHeight(this);
    if (paramInt1 >= 0) {
      paramInt3 = Math.min(paramInt1, paramInt4 - paramInt2);
    } else {
      paramInt3 = 0;
    } 
    paramInt1 = i;
    paramInt2 = m;
    if (shouldLayout(this.mNavButtonView))
      if (k) {
        paramInt2 = layoutChildRight(this.mNavButtonView, m, arrayOfInt, paramInt3);
        paramInt1 = i;
      } else {
        paramInt1 = layoutChildLeft(this.mNavButtonView, i, arrayOfInt, paramInt3);
        paramInt2 = m;
      }  
    paramInt4 = paramInt1;
    i = paramInt2;
    if (shouldLayout(this.mCollapseButtonView))
      if (k) {
        i = layoutChildRight(this.mCollapseButtonView, paramInt2, arrayOfInt, paramInt3);
        paramInt4 = paramInt1;
      } else {
        paramInt4 = layoutChildLeft(this.mCollapseButtonView, paramInt1, arrayOfInt, paramInt3);
        i = paramInt2;
      }  
    paramInt2 = paramInt4;
    paramInt1 = i;
    if (shouldLayout(this.mMenuView))
      if (k) {
        paramInt2 = layoutChildLeft(this.mMenuView, paramInt4, arrayOfInt, paramInt3);
        paramInt1 = i;
      } else {
        paramInt1 = layoutChildRight(this.mMenuView, i, arrayOfInt, paramInt3);
        paramInt2 = paramInt4;
      }  
    i = getCurrentContentInsetLeft();
    paramInt4 = getCurrentContentInsetRight();
    arrayOfInt[0] = Math.max(0, i - paramInt2);
    arrayOfInt[1] = Math.max(0, paramInt4 - i2 - i3 - paramInt1);
    paramInt2 = Math.max(paramInt2, i);
    paramInt4 = Math.min(paramInt1, i2 - i3 - paramInt4);
    paramInt1 = paramInt2;
    i = paramInt4;
    if (shouldLayout(this.mExpandedActionView))
      if (k) {
        i = layoutChildRight(this.mExpandedActionView, paramInt4, arrayOfInt, paramInt3);
        paramInt1 = paramInt2;
      } else {
        paramInt1 = layoutChildLeft(this.mExpandedActionView, paramInt2, arrayOfInt, paramInt3);
        i = paramInt4;
      }  
    paramInt2 = paramInt1;
    paramInt4 = i;
    if (shouldLayout(this.mLogoView))
      if (k) {
        paramInt4 = layoutChildRight(this.mLogoView, i, arrayOfInt, paramInt3);
        paramInt2 = paramInt1;
      } else {
        paramInt2 = layoutChildLeft(this.mLogoView, paramInt1, arrayOfInt, paramInt3);
        paramInt4 = i;
      }  
    paramBoolean = shouldLayout(this.mTitleTextView);
    boolean bool = shouldLayout(this.mSubtitleTextView);
    paramInt1 = 0;
    if (paramBoolean) {
      LayoutParams layoutParams = (LayoutParams)this.mTitleTextView.getLayoutParams();
      paramInt1 = 0 + layoutParams.topMargin + this.mTitleTextView.getMeasuredHeight() + layoutParams.bottomMargin;
    } 
    m = paramInt1;
    if (bool) {
      LayoutParams layoutParams = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
      m = paramInt1 + layoutParams.topMargin + this.mSubtitleTextView.getMeasuredHeight() + layoutParams.bottomMargin;
    } 
    if (paramBoolean || bool) {
      TextView textView2;
      TextView textView1;
      if (paramBoolean) {
        textView1 = this.mTitleTextView;
      } else {
        textView1 = this.mSubtitleTextView;
      } 
      if (bool) {
        textView2 = this.mSubtitleTextView;
      } else {
        textView2 = this.mTitleTextView;
      } 
      LayoutParams layoutParams1 = (LayoutParams)textView1.getLayoutParams();
      LayoutParams layoutParams2 = (LayoutParams)textView2.getLayoutParams();
      if ((paramBoolean && this.mTitleTextView.getMeasuredWidth() > 0) || (bool && this.mSubtitleTextView.getMeasuredWidth() > 0)) {
        i = 1;
      } else {
        i = 0;
      } 
      paramInt1 = this.mGravity & 0x70;
      if (paramInt1 != 48) {
        if (paramInt1 != 80) {
          paramInt1 = (i1 - n - i4 - m) / 2;
          if (paramInt1 < layoutParams1.topMargin + this.mTitleMarginTop) {
            paramInt1 = layoutParams1.topMargin + this.mTitleMarginTop;
          } else {
            m = i1 - i4 - m - paramInt1 - n;
            if (m < layoutParams1.bottomMargin + this.mTitleMarginBottom)
              paramInt1 = Math.max(0, paramInt1 - layoutParams2.bottomMargin + this.mTitleMarginBottom - m); 
          } 
          paramInt1 = n + paramInt1;
        } else {
          paramInt1 = i1 - i4 - layoutParams2.bottomMargin - this.mTitleMarginBottom - m;
        } 
      } else {
        paramInt1 = getPaddingTop() + layoutParams1.topMargin + this.mTitleMarginTop;
      } 
      m = paramInt2;
      if (k) {
        if (i != 0) {
          paramInt2 = this.mTitleMarginStart;
        } else {
          paramInt2 = 0;
        } 
        k = paramInt2 - arrayOfInt[1];
        paramInt2 = paramInt4 - Math.max(0, k);
        arrayOfInt[1] = Math.max(0, -k);
        k = paramInt2;
        paramInt4 = paramInt2;
        if (paramBoolean) {
          layoutParams1 = (LayoutParams)this.mTitleTextView.getLayoutParams();
          i1 = k - this.mTitleTextView.getMeasuredWidth();
          n = this.mTitleTextView.getMeasuredHeight() + paramInt1;
          this.mTitleTextView.layout(i1, paramInt1, k, n);
          paramInt1 = i1 - this.mTitleMarginEnd;
          n += layoutParams1.bottomMargin;
        } else {
          n = paramInt1;
          paramInt1 = k;
        } 
        k = paramInt4;
        if (bool) {
          layoutParams1 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
          k = n + layoutParams1.topMargin;
          n = this.mSubtitleTextView.getMeasuredWidth();
          i1 = this.mSubtitleTextView.getMeasuredHeight() + k;
          this.mSubtitleTextView.layout(paramInt4 - n, k, paramInt4, i1);
          k = paramInt4 - this.mTitleMarginEnd;
          paramInt4 = layoutParams1.bottomMargin;
        } 
        if (i != 0)
          paramInt2 = Math.min(paramInt1, k); 
        paramInt1 = m;
      } else {
        if (i != 0) {
          paramInt2 = this.mTitleMarginStart;
        } else {
          paramInt2 = 0;
        } 
        k = paramInt2 - arrayOfInt[0];
        paramInt2 = m + Math.max(0, k);
        arrayOfInt[0] = Math.max(0, -k);
        m = paramInt2;
        k = paramInt2;
        if (paramBoolean) {
          layoutParams1 = (LayoutParams)this.mTitleTextView.getLayoutParams();
          i1 = this.mTitleTextView.getMeasuredWidth() + m;
          n = this.mTitleTextView.getMeasuredHeight() + paramInt1;
          this.mTitleTextView.layout(m, paramInt1, i1, n);
          m = i1 + this.mTitleMarginEnd;
          n += layoutParams1.bottomMargin;
        } else {
          n = paramInt1;
        } 
        paramInt1 = paramInt2;
        i1 = k;
        if (bool) {
          layoutParams1 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
          paramInt2 = n + layoutParams1.topMargin;
          n = this.mSubtitleTextView.getMeasuredWidth() + k;
          i1 = this.mSubtitleTextView.getMeasuredHeight() + paramInt2;
          this.mSubtitleTextView.layout(k, paramInt2, n, i1);
          i1 = n + this.mTitleMarginEnd;
          paramInt2 = layoutParams1.bottomMargin;
        } 
        paramInt2 = paramInt4;
        if (i != 0) {
          paramInt1 = Math.max(m, i1);
          paramInt2 = paramInt4;
        } 
      } 
    } else {
      paramInt1 = paramInt2;
      paramInt2 = paramInt4;
    } 
    paramInt4 = paramInt3;
    addCustomViewsWithGravity(this.mTempViews, 3);
    i = this.mTempViews.size();
    for (paramInt3 = 0; paramInt3 < i; paramInt3++)
      paramInt1 = layoutChildLeft((View)this.mTempViews.get(paramInt3), paramInt1, arrayOfInt, paramInt4); 
    addCustomViewsWithGravity(this.mTempViews, 5);
    i = this.mTempViews.size();
    for (paramInt3 = 0; paramInt3 < i; paramInt3++)
      paramInt2 = layoutChildRight((View)this.mTempViews.get(paramInt3), paramInt2, arrayOfInt, paramInt4); 
    addCustomViewsWithGravity(this.mTempViews, 1);
    i = getViewListMeasuredWidth(this.mTempViews, arrayOfInt);
    paramInt3 = j + (i2 - j - i3) / 2 - i / 2;
    i = paramInt3 + i;
    if (paramInt3 >= paramInt1) {
      paramInt1 = paramInt3;
      if (i > paramInt2)
        paramInt1 = paramInt3 - i - paramInt2; 
    } 
    paramInt2 = this.mTempViews.size();
    paramInt3 = paramInt1;
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
      paramInt3 = layoutChildLeft((View)this.mTempViews.get(paramInt1), paramInt3, arrayOfInt, paramInt4); 
    this.mTempViews.clear();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int m = 0;
    int k = 0;
    int[] arrayOfInt = this.mTempMargins;
    if (ViewUtils.isLayoutRtl(this)) {
      i2 = 1;
      i1 = 0;
    } else {
      i2 = 0;
      i1 = 1;
    } 
    int n = 0;
    if (shouldLayout(this.mNavButtonView)) {
      measureChildConstrained(this.mNavButtonView, paramInt1, 0, paramInt2, 0, this.mMaxButtonHeight);
      n = this.mNavButtonView.getMeasuredWidth() + getHorizontalMargins(this.mNavButtonView);
      m = Math.max(0, this.mNavButtonView.getMeasuredHeight() + getVerticalMargins(this.mNavButtonView));
      k = View.combineMeasuredStates(0, this.mNavButtonView.getMeasuredState());
    } 
    int j = m;
    int i = k;
    if (shouldLayout(this.mCollapseButtonView)) {
      measureChildConstrained(this.mCollapseButtonView, paramInt1, 0, paramInt2, 0, this.mMaxButtonHeight);
      n = this.mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins(this.mCollapseButtonView);
      j = Math.max(m, this.mCollapseButtonView.getMeasuredHeight() + getVerticalMargins(this.mCollapseButtonView));
      i = View.combineMeasuredStates(k, this.mCollapseButtonView.getMeasuredState());
    } 
    k = getCurrentContentInsetStart();
    m = 0 + Math.max(k, n);
    arrayOfInt[i2] = Math.max(0, k - n);
    if (shouldLayout(this.mMenuView)) {
      measureChildConstrained(this.mMenuView, paramInt1, m, paramInt2, 0, this.mMaxButtonHeight);
      n = this.mMenuView.getMeasuredWidth();
      i2 = getHorizontalMargins(this.mMenuView);
      j = Math.max(j, this.mMenuView.getMeasuredHeight() + getVerticalMargins(this.mMenuView));
      k = View.combineMeasuredStates(i, this.mMenuView.getMeasuredState());
      i = n + i2;
    } else {
      k = i;
      i = 0;
    } 
    int i2 = getCurrentContentInsetEnd();
    n = m + Math.max(i2, i);
    arrayOfInt[i1] = Math.max(0, i2 - i);
    if (shouldLayout(this.mExpandedActionView)) {
      n += measureChildCollapseMargins(this.mExpandedActionView, paramInt1, n, paramInt2, 0, arrayOfInt);
      m = Math.max(j, this.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(this.mExpandedActionView));
      k = View.combineMeasuredStates(k, this.mExpandedActionView.getMeasuredState());
    } else {
      m = j;
    } 
    int i1 = n;
    i2 = m;
    j = k;
    if (shouldLayout(this.mLogoView)) {
      i1 = n + measureChildCollapseMargins(this.mLogoView, paramInt1, n, paramInt2, 0, arrayOfInt);
      i2 = Math.max(m, this.mLogoView.getMeasuredHeight() + getVerticalMargins(this.mLogoView));
      j = View.combineMeasuredStates(k, this.mLogoView.getMeasuredState());
    } 
    m = getChildCount();
    n = 0;
    k = i;
    i = i2;
    while (n < m) {
      View view = getChildAt(n);
      if (((LayoutParams)view.getLayoutParams()).mViewType == 0 && shouldLayout(view)) {
        i1 += measureChildCollapseMargins(view, paramInt1, i1, paramInt2, 0, arrayOfInt);
        i = Math.max(i, view.getMeasuredHeight() + getVerticalMargins(view));
        j = View.combineMeasuredStates(j, view.getMeasuredState());
      } 
      n++;
    } 
    n = 0;
    m = 0;
    i2 = this.mTitleMarginTop + this.mTitleMarginBottom;
    int i3 = this.mTitleMarginStart + this.mTitleMarginEnd;
    k = j;
    if (shouldLayout(this.mTitleTextView)) {
      measureChildCollapseMargins(this.mTitleTextView, paramInt1, i1 + i3, paramInt2, i2, arrayOfInt);
      n = this.mTitleTextView.getMeasuredWidth() + getHorizontalMargins(this.mTitleTextView);
      m = this.mTitleTextView.getMeasuredHeight() + getVerticalMargins(this.mTitleTextView);
      k = View.combineMeasuredStates(j, this.mTitleTextView.getMeasuredState());
    } 
    if (shouldLayout(this.mSubtitleTextView)) {
      n = Math.max(n, measureChildCollapseMargins(this.mSubtitleTextView, paramInt1, i1 + i3, paramInt2, m + i2, arrayOfInt));
      j = this.mSubtitleTextView.getMeasuredHeight();
      i2 = getVerticalMargins(this.mSubtitleTextView);
      k = View.combineMeasuredStates(k, this.mSubtitleTextView.getMeasuredState());
      m += j + i2;
    } 
    i = Math.max(i, m);
    i2 = getPaddingLeft();
    i3 = getPaddingRight();
    j = getPaddingTop();
    m = getPaddingBottom();
    n = View.resolveSizeAndState(Math.max(i1 + n + i2 + i3, getSuggestedMinimumWidth()), paramInt1, 0xFF000000 & k);
    paramInt1 = View.resolveSizeAndState(Math.max(i + j + m, getSuggestedMinimumHeight()), paramInt2, k << 16);
    if (shouldCollapse())
      paramInt1 = 0; 
    setMeasuredDimension(n, paramInt1);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    ActionMenuView actionMenuView = this.mMenuView;
    if (actionMenuView != null) {
      MenuBuilder menuBuilder = actionMenuView.peekMenu();
    } else {
      actionMenuView = null;
    } 
    if (savedState.expandedMenuItemId != 0 && this.mExpandedMenuPresenter != null && actionMenuView != null) {
      MenuItem menuItem = actionMenuView.findItem(savedState.expandedMenuItemId);
      if (menuItem != null)
        menuItem.expandActionView(); 
    } 
    if (savedState.isOverflowOpen)
      postShowOverflowMenu(); 
  }
  
  public void onRtlPropertiesChanged(int paramInt) {
    if (Build.VERSION.SDK_INT >= 17)
      super.onRtlPropertiesChanged(paramInt); 
    ensureContentInsets();
    RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
    boolean bool = true;
    if (paramInt != 1)
      bool = false; 
    rtlSpacingHelper.setDirection(bool);
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
    if (expandedActionViewMenuPresenter != null && expandedActionViewMenuPresenter.mCurrentExpandedItem != null)
      savedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId(); 
    savedState.isOverflowOpen = isOverflowMenuShowing();
    return savedState;
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
  
  void removeChildrenForExpandedActionView() {
    for (int i = getChildCount() - 1; i >= 0; i--) {
      View view = getChildAt(i);
      if (((LayoutParams)view.getLayoutParams()).mViewType != 2 && view != this.mMenuView) {
        removeViewAt(i);
        this.mHiddenViews.add(view);
      } 
    } 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setCollapsible(boolean paramBoolean) {
    this.mCollapsible = paramBoolean;
    requestLayout();
  }
  
  public void setContentInsetEndWithActions(int paramInt) {
    int i = paramInt;
    if (paramInt < 0)
      i = Integer.MIN_VALUE; 
    if (i != this.mContentInsetEndWithActions) {
      this.mContentInsetEndWithActions = i;
      if (getNavigationIcon() != null)
        requestLayout(); 
    } 
  }
  
  public void setContentInsetStartWithNavigation(int paramInt) {
    int i = paramInt;
    if (paramInt < 0)
      i = Integer.MIN_VALUE; 
    if (i != this.mContentInsetStartWithNavigation) {
      this.mContentInsetStartWithNavigation = i;
      if (getNavigationIcon() != null)
        requestLayout(); 
    } 
  }
  
  public void setContentInsetsAbsolute(int paramInt1, int paramInt2) {
    ensureContentInsets();
    this.mContentInsets.setAbsolute(paramInt1, paramInt2);
  }
  
  public void setContentInsetsRelative(int paramInt1, int paramInt2) {
    ensureContentInsets();
    this.mContentInsets.setRelative(paramInt1, paramInt2);
  }
  
  public void setLogo(@DrawableRes int paramInt) { setLogo(AppCompatResources.getDrawable(getContext(), paramInt)); }
  
  public void setLogo(Drawable paramDrawable) {
    if (paramDrawable != null) {
      ensureLogoView();
      if (!isChildOrHidden(this.mLogoView))
        addSystemView(this.mLogoView, true); 
    } else {
      ImageView imageView1 = this.mLogoView;
      if (imageView1 != null && isChildOrHidden(imageView1)) {
        removeView(this.mLogoView);
        this.mHiddenViews.remove(this.mLogoView);
      } 
    } 
    ImageView imageView = this.mLogoView;
    if (imageView != null)
      imageView.setImageDrawable(paramDrawable); 
  }
  
  public void setLogoDescription(@StringRes int paramInt) { setLogoDescription(getContext().getText(paramInt)); }
  
  public void setLogoDescription(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence))
      ensureLogoView(); 
    ImageView imageView = this.mLogoView;
    if (imageView != null)
      imageView.setContentDescription(paramCharSequence); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setMenu(MenuBuilder paramMenuBuilder, ActionMenuPresenter paramActionMenuPresenter) {
    if (paramMenuBuilder == null && this.mMenuView == null)
      return; 
    ensureMenuView();
    MenuBuilder menuBuilder = this.mMenuView.peekMenu();
    if (menuBuilder == paramMenuBuilder)
      return; 
    if (menuBuilder != null) {
      menuBuilder.removeMenuPresenter(this.mOuterActionMenuPresenter);
      menuBuilder.removeMenuPresenter(this.mExpandedMenuPresenter);
    } 
    if (this.mExpandedMenuPresenter == null)
      this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(); 
    paramActionMenuPresenter.setExpandedActionViewsExclusive(true);
    if (paramMenuBuilder != null) {
      paramMenuBuilder.addMenuPresenter(paramActionMenuPresenter, this.mPopupContext);
      paramMenuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
    } else {
      paramActionMenuPresenter.initForMenu(this.mPopupContext, null);
      this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
      paramActionMenuPresenter.updateMenuView(true);
      this.mExpandedMenuPresenter.updateMenuView(true);
    } 
    this.mMenuView.setPopupTheme(this.mPopupTheme);
    this.mMenuView.setPresenter(paramActionMenuPresenter);
    this.mOuterActionMenuPresenter = paramActionMenuPresenter;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1) {
    this.mActionMenuPresenterCallback = paramCallback;
    this.mMenuBuilderCallback = paramCallback1;
    ActionMenuView actionMenuView = this.mMenuView;
    if (actionMenuView != null)
      actionMenuView.setMenuCallbacks(paramCallback, paramCallback1); 
  }
  
  public void setNavigationContentDescription(@StringRes int paramInt) {
    CharSequence charSequence;
    if (paramInt != 0) {
      charSequence = getContext().getText(paramInt);
    } else {
      charSequence = null;
    } 
    setNavigationContentDescription(charSequence);
  }
  
  public void setNavigationContentDescription(@Nullable CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence))
      ensureNavButtonView(); 
    ImageButton imageButton = this.mNavButtonView;
    if (imageButton != null)
      imageButton.setContentDescription(paramCharSequence); 
  }
  
  public void setNavigationIcon(@DrawableRes int paramInt) { setNavigationIcon(AppCompatResources.getDrawable(getContext(), paramInt)); }
  
  public void setNavigationIcon(@Nullable Drawable paramDrawable) {
    if (paramDrawable != null) {
      ensureNavButtonView();
      if (!isChildOrHidden(this.mNavButtonView))
        addSystemView(this.mNavButtonView, true); 
    } else {
      ImageButton imageButton1 = this.mNavButtonView;
      if (imageButton1 != null && isChildOrHidden(imageButton1)) {
        removeView(this.mNavButtonView);
        this.mHiddenViews.remove(this.mNavButtonView);
      } 
    } 
    ImageButton imageButton = this.mNavButtonView;
    if (imageButton != null)
      imageButton.setImageDrawable(paramDrawable); 
  }
  
  public void setNavigationOnClickListener(View.OnClickListener paramOnClickListener) {
    ensureNavButtonView();
    this.mNavButtonView.setOnClickListener(paramOnClickListener);
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener) { this.mOnMenuItemClickListener = paramOnMenuItemClickListener; }
  
  public void setOverflowIcon(@Nullable Drawable paramDrawable) {
    ensureMenu();
    this.mMenuView.setOverflowIcon(paramDrawable);
  }
  
  public void setPopupTheme(@StyleRes int paramInt) {
    if (this.mPopupTheme != paramInt) {
      this.mPopupTheme = paramInt;
      if (paramInt == 0) {
        this.mPopupContext = getContext();
        return;
      } 
      this.mPopupContext = new ContextThemeWrapper(getContext(), paramInt);
    } 
  }
  
  public void setSubtitle(@StringRes int paramInt) { setSubtitle(getContext().getText(paramInt)); }
  
  public void setSubtitle(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence)) {
      if (this.mSubtitleTextView == null) {
        Context context = getContext();
        this.mSubtitleTextView = new AppCompatTextView(context);
        this.mSubtitleTextView.setSingleLine();
        this.mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        int i = this.mSubtitleTextAppearance;
        if (i != 0)
          this.mSubtitleTextView.setTextAppearance(context, i); 
        i = this.mSubtitleTextColor;
        if (i != 0)
          this.mSubtitleTextView.setTextColor(i); 
      } 
      if (!isChildOrHidden(this.mSubtitleTextView))
        addSystemView(this.mSubtitleTextView, true); 
    } else {
      TextView textView1 = this.mSubtitleTextView;
      if (textView1 != null && isChildOrHidden(textView1)) {
        removeView(this.mSubtitleTextView);
        this.mHiddenViews.remove(this.mSubtitleTextView);
      } 
    } 
    TextView textView = this.mSubtitleTextView;
    if (textView != null)
      textView.setText(paramCharSequence); 
    this.mSubtitleText = paramCharSequence;
  }
  
  public void setSubtitleTextAppearance(Context paramContext, @StyleRes int paramInt) {
    this.mSubtitleTextAppearance = paramInt;
    TextView textView = this.mSubtitleTextView;
    if (textView != null)
      textView.setTextAppearance(paramContext, paramInt); 
  }
  
  public void setSubtitleTextColor(@ColorInt int paramInt) {
    this.mSubtitleTextColor = paramInt;
    TextView textView = this.mSubtitleTextView;
    if (textView != null)
      textView.setTextColor(paramInt); 
  }
  
  public void setTitle(@StringRes int paramInt) { setTitle(getContext().getText(paramInt)); }
  
  public void setTitle(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence)) {
      if (this.mTitleTextView == null) {
        Context context = getContext();
        this.mTitleTextView = new AppCompatTextView(context);
        this.mTitleTextView.setSingleLine();
        this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        int i = this.mTitleTextAppearance;
        if (i != 0)
          this.mTitleTextView.setTextAppearance(context, i); 
        i = this.mTitleTextColor;
        if (i != 0)
          this.mTitleTextView.setTextColor(i); 
      } 
      if (!isChildOrHidden(this.mTitleTextView))
        addSystemView(this.mTitleTextView, true); 
    } else {
      TextView textView1 = this.mTitleTextView;
      if (textView1 != null && isChildOrHidden(textView1)) {
        removeView(this.mTitleTextView);
        this.mHiddenViews.remove(this.mTitleTextView);
      } 
    } 
    TextView textView = this.mTitleTextView;
    if (textView != null)
      textView.setText(paramCharSequence); 
    this.mTitleText = paramCharSequence;
  }
  
  public void setTitleMargin(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mTitleMarginStart = paramInt1;
    this.mTitleMarginTop = paramInt2;
    this.mTitleMarginEnd = paramInt3;
    this.mTitleMarginBottom = paramInt4;
    requestLayout();
  }
  
  public void setTitleMarginBottom(int paramInt) {
    this.mTitleMarginBottom = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginEnd(int paramInt) {
    this.mTitleMarginEnd = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginStart(int paramInt) {
    this.mTitleMarginStart = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginTop(int paramInt) {
    this.mTitleMarginTop = paramInt;
    requestLayout();
  }
  
  public void setTitleTextAppearance(Context paramContext, @StyleRes int paramInt) {
    this.mTitleTextAppearance = paramInt;
    TextView textView = this.mTitleTextView;
    if (textView != null)
      textView.setTextAppearance(paramContext, paramInt); 
  }
  
  public void setTitleTextColor(@ColorInt int paramInt) {
    this.mTitleTextColor = paramInt;
    TextView textView = this.mTitleTextView;
    if (textView != null)
      textView.setTextColor(paramInt); 
  }
  
  public boolean showOverflowMenu() {
    ActionMenuView actionMenuView = this.mMenuView;
    return (actionMenuView != null && actionMenuView.showOverflowMenu());
  }
  
  private class ExpandedActionViewMenuPresenter implements MenuPresenter {
    MenuItemImpl mCurrentExpandedItem;
    
    MenuBuilder mMenu;
    
    public boolean collapseItemActionView(MenuBuilder param1MenuBuilder, MenuItemImpl param1MenuItemImpl) {
      if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView)
        ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewCollapsed(); 
      Toolbar toolbar = Toolbar.this;
      toolbar.removeView(toolbar.mExpandedActionView);
      toolbar = Toolbar.this;
      toolbar.removeView(toolbar.mCollapseButtonView);
      toolbar = Toolbar.this;
      toolbar.mExpandedActionView = null;
      toolbar.addChildrenForExpandedActionView();
      this.mCurrentExpandedItem = null;
      Toolbar.this.requestLayout();
      param1MenuItemImpl.setActionViewExpanded(false);
      return true;
    }
    
    public boolean expandItemActionView(MenuBuilder param1MenuBuilder, MenuItemImpl param1MenuItemImpl) {
      Toolbar.this.ensureCollapseButtonView();
      ViewParent viewParent = Toolbar.this.mCollapseButtonView.getParent();
      Toolbar toolbar = Toolbar.this;
      if (viewParent != toolbar) {
        if (viewParent instanceof ViewGroup)
          ((ViewGroup)viewParent).removeView(toolbar.mCollapseButtonView); 
        Toolbar toolbar1 = Toolbar.this;
        toolbar1.addView(toolbar1.mCollapseButtonView);
      } 
      Toolbar.this.mExpandedActionView = param1MenuItemImpl.getActionView();
      this.mCurrentExpandedItem = param1MenuItemImpl;
      viewParent = Toolbar.this.mExpandedActionView.getParent();
      toolbar = Toolbar.this;
      if (viewParent != toolbar) {
        if (viewParent instanceof ViewGroup)
          ((ViewGroup)viewParent).removeView(toolbar.mExpandedActionView); 
        Toolbar.LayoutParams layoutParams = Toolbar.this.generateDefaultLayoutParams();
        layoutParams.gravity = 0x800003 | Toolbar.this.mButtonGravity & 0x70;
        layoutParams.mViewType = 2;
        Toolbar.this.mExpandedActionView.setLayoutParams(layoutParams);
        Toolbar toolbar1 = Toolbar.this;
        toolbar1.addView(toolbar1.mExpandedActionView);
      } 
      Toolbar.this.removeChildrenForExpandedActionView();
      Toolbar.this.requestLayout();
      param1MenuItemImpl.setActionViewExpanded(true);
      if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView)
        ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewExpanded(); 
      return true;
    }
    
    public boolean flagActionItems() { return false; }
    
    public int getId() { return 0; }
    
    public MenuView getMenuView(ViewGroup param1ViewGroup) { return null; }
    
    public void initForMenu(Context param1Context, MenuBuilder param1MenuBuilder) {
      MenuBuilder menuBuilder = this.mMenu;
      if (menuBuilder != null) {
        MenuItemImpl menuItemImpl = this.mCurrentExpandedItem;
        if (menuItemImpl != null)
          menuBuilder.collapseItemActionView(menuItemImpl); 
      } 
      this.mMenu = param1MenuBuilder;
    }
    
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {}
    
    public void onRestoreInstanceState(Parcelable param1Parcelable) {}
    
    public Parcelable onSaveInstanceState() { return null; }
    
    public boolean onSubMenuSelected(SubMenuBuilder param1SubMenuBuilder) { return false; }
    
    public void setCallback(MenuPresenter.Callback param1Callback) {}
    
    public void updateMenuView(boolean param1Boolean) {
      if (this.mCurrentExpandedItem != null) {
        byte b2 = 0;
        MenuBuilder menuBuilder = this.mMenu;
        byte b1 = b2;
        if (menuBuilder != null) {
          int i = menuBuilder.size();
          byte b = 0;
          while (true) {
            b1 = b2;
            if (b < i) {
              if (this.mMenu.getItem(b) == this.mCurrentExpandedItem) {
                b1 = 1;
                break;
              } 
              b++;
              continue;
            } 
            break;
          } 
        } 
        if (b1 == 0)
          collapseItemActionView(this.mMenu, this.mCurrentExpandedItem); 
      } 
    }
  }
  
  public static class LayoutParams extends ActionBar.LayoutParams {
    static final int CUSTOM = 0;
    
    static final int EXPANDED = 2;
    
    static final int SYSTEM = 1;
    
    int mViewType = 0;
    
    public LayoutParams(int param1Int) { this(-2, -1, param1Int); }
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.gravity = 8388627;
    }
    
    public LayoutParams(int param1Int1, int param1Int2, int param1Int3) {
      super(param1Int1, param1Int2);
      this.gravity = param1Int3;
    }
    
    public LayoutParams(@NonNull Context param1Context, AttributeSet param1AttributeSet) { super(param1Context, param1AttributeSet); }
    
    public LayoutParams(ActionBar.LayoutParams param1LayoutParams) { super(param1LayoutParams); }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.mViewType = param1LayoutParams.mViewType;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) { super(param1LayoutParams); }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
      copyMarginsFromCompat(param1MarginLayoutParams);
    }
    
    void copyMarginsFromCompat(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      this.leftMargin = param1MarginLayoutParams.leftMargin;
      this.topMargin = param1MarginLayoutParams.topMargin;
      this.rightMargin = param1MarginLayoutParams.rightMargin;
      this.bottomMargin = param1MarginLayoutParams.bottomMargin;
    }
  }
  
  public static interface OnMenuItemClickListener {
    boolean onMenuItemClick(MenuItem param1MenuItem);
  }
  
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
        public Toolbar.SavedState createFromParcel(Parcel param2Parcel) { return new Toolbar.SavedState(param2Parcel, null); }
        
        public Toolbar.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) { return new Toolbar.SavedState(param2Parcel, param2ClassLoader); }
        
        public Toolbar.SavedState[] newArray(int param2Int) { return new Toolbar.SavedState[param2Int]; }
      };
    
    int expandedMenuItemId;
    
    boolean isOverflowOpen;
    
    public SavedState(Parcel param1Parcel) { this(param1Parcel, null); }
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.expandedMenuItemId = param1Parcel.readInt();
      if (param1Parcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.isOverflowOpen = bool;
    }
    
    public SavedState(Parcelable param1Parcelable) { super(param1Parcelable); }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) { throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n"); }
  }
  
  static final class null extends Object implements Parcelable.ClassLoaderCreator<SavedState> {
    public Toolbar.SavedState createFromParcel(Parcel param1Parcel) { return new Toolbar.SavedState(param1Parcel, null); }
    
    public Toolbar.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) { return new Toolbar.SavedState(param1Parcel, param1ClassLoader); }
    
    public Toolbar.SavedState[] newArray(int param1Int) { return new Toolbar.SavedState[param1Int]; }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\Toolbar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */