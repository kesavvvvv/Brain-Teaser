package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public class ActionMenuView extends LinearLayoutCompat implements MenuBuilder.ItemInvoker, MenuView {
  static final int GENERATED_ITEM_PADDING = 4;
  
  static final int MIN_CELL_SIZE = 56;
  
  private static final String TAG = "ActionMenuView";
  
  private MenuPresenter.Callback mActionMenuPresenterCallback;
  
  private boolean mFormatItems;
  
  private int mFormatItemsWidth;
  
  private int mGeneratedItemPadding;
  
  private MenuBuilder mMenu;
  
  MenuBuilder.Callback mMenuBuilderCallback;
  
  private int mMinCellSize;
  
  OnMenuItemClickListener mOnMenuItemClickListener;
  
  private Context mPopupContext;
  
  private int mPopupTheme;
  
  private ActionMenuPresenter mPresenter;
  
  private boolean mReserveOverflow;
  
  public ActionMenuView(Context paramContext) { this(paramContext, null); }
  
  public ActionMenuView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    setBaselineAligned(false);
    float f = (paramContext.getResources().getDisplayMetrics()).density;
    this.mMinCellSize = (int)(56.0F * f);
    this.mGeneratedItemPadding = (int)(4.0F * f);
    this.mPopupContext = paramContext;
    this.mPopupTheme = 0;
  }
  
  static int measureChildForCells(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) { // Byte code:
    //   0: aload_0
    //   1: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   4: checkcast android/support/v7/widget/ActionMenuView$LayoutParams
    //   7: astore #10
    //   9: iload_3
    //   10: invokestatic getSize : (I)I
    //   13: iload #4
    //   15: isub
    //   16: iload_3
    //   17: invokestatic getMode : (I)I
    //   20: invokestatic makeMeasureSpec : (II)I
    //   23: istore #6
    //   25: aload_0
    //   26: instanceof android/support/v7/view/menu/ActionMenuItemView
    //   29: ifeq -> 41
    //   32: aload_0
    //   33: checkcast android/support/v7/view/menu/ActionMenuItemView
    //   36: astore #9
    //   38: goto -> 44
    //   41: aconst_null
    //   42: astore #9
    //   44: iconst_0
    //   45: istore #8
    //   47: aload #9
    //   49: ifnull -> 66
    //   52: aload #9
    //   54: invokevirtual hasText : ()Z
    //   57: ifeq -> 66
    //   60: iconst_1
    //   61: istore #4
    //   63: goto -> 69
    //   66: iconst_0
    //   67: istore #4
    //   69: iconst_0
    //   70: istore #5
    //   72: iload #5
    //   74: istore_3
    //   75: iload_2
    //   76: ifle -> 146
    //   79: iload #4
    //   81: ifeq -> 92
    //   84: iload #5
    //   86: istore_3
    //   87: iload_2
    //   88: iconst_2
    //   89: if_icmplt -> 146
    //   92: aload_0
    //   93: iload_1
    //   94: iload_2
    //   95: imul
    //   96: ldc -2147483648
    //   98: invokestatic makeMeasureSpec : (II)I
    //   101: iload #6
    //   103: invokevirtual measure : (II)V
    //   106: aload_0
    //   107: invokevirtual getMeasuredWidth : ()I
    //   110: istore #5
    //   112: iload #5
    //   114: iload_1
    //   115: idiv
    //   116: istore_3
    //   117: iload_3
    //   118: istore_2
    //   119: iload #5
    //   121: iload_1
    //   122: irem
    //   123: ifeq -> 130
    //   126: iload_3
    //   127: iconst_1
    //   128: iadd
    //   129: istore_2
    //   130: iload_2
    //   131: istore_3
    //   132: iload #4
    //   134: ifeq -> 146
    //   137: iload_2
    //   138: istore_3
    //   139: iload_2
    //   140: iconst_2
    //   141: if_icmpge -> 146
    //   144: iconst_2
    //   145: istore_3
    //   146: iload #8
    //   148: istore #7
    //   150: aload #10
    //   152: getfield isOverflowButton : Z
    //   155: ifne -> 170
    //   158: iload #8
    //   160: istore #7
    //   162: iload #4
    //   164: ifeq -> 170
    //   167: iconst_1
    //   168: istore #7
    //   170: aload #10
    //   172: iload #7
    //   174: putfield expandable : Z
    //   177: aload #10
    //   179: iload_3
    //   180: putfield cellsUsed : I
    //   183: aload_0
    //   184: iload_3
    //   185: iload_1
    //   186: imul
    //   187: ldc 1073741824
    //   189: invokestatic makeMeasureSpec : (II)I
    //   192: iload #6
    //   194: invokevirtual measure : (II)V
    //   197: iload_3
    //   198: ireturn }
  
  private void onMeasureExactFormat(int paramInt1, int paramInt2) {
    int i7 = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    int i6 = View.MeasureSpec.getSize(paramInt2);
    int i3 = getPaddingLeft() + getPaddingRight();
    int i1 = getPaddingTop() + getPaddingBottom();
    int i8 = getChildMeasureSpec(paramInt2, i1, -2);
    int i5 = paramInt1 - i3;
    paramInt1 = this.mMinCellSize;
    int i2 = i5 / paramInt1;
    int m = i5 % paramInt1;
    if (i2 == 0) {
      setMeasuredDimension(i5, 0);
      return;
    } 
    int i9 = paramInt1 + m / i2;
    paramInt2 = 0;
    int i10 = getChildCount();
    int j = 0;
    byte b = 0;
    int i = 0;
    int n = 0;
    paramInt1 = i2;
    int i4 = 0;
    long l = 0L;
    while (i4 < i10) {
      View view = getChildAt(i4);
      if (view.getVisibility() != 8) {
        boolean bool = view instanceof ActionMenuItemView;
        b++;
        if (bool) {
          i11 = this.mGeneratedItemPadding;
          view.setPadding(i11, 0, i11, 0);
        } 
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        layoutParams.expanded = false;
        layoutParams.extraPixels = 0;
        layoutParams.cellsUsed = 0;
        layoutParams.expandable = false;
        layoutParams.leftMargin = 0;
        layoutParams.rightMargin = 0;
        if (bool && ((ActionMenuItemView)view).hasText()) {
          bool = true;
        } else {
          bool = false;
        } 
        layoutParams.preventEdgeOffset = bool;
        if (layoutParams.isOverflowButton) {
          i11 = 1;
        } else {
          i11 = paramInt1;
        } 
        int i12 = measureChildForCells(view, i9, i11, i8, i1);
        n = Math.max(n, i12);
        int i11 = i;
        if (layoutParams.expandable)
          i11 = i + 1; 
        if (layoutParams.isOverflowButton)
          paramInt2 = 1; 
        paramInt1 -= i12;
        j = Math.max(j, view.getMeasuredHeight());
        if (i12 == 1) {
          l |= (1 << i4);
          i = i11;
        } else {
          i = i11;
        } 
      } 
      i4++;
    } 
    if (paramInt2 != 0 && b == 2) {
      i2 = 1;
    } else {
      i2 = 0;
    } 
    int k = 0;
    i3 = paramInt1;
    paramInt1 = k;
    i1 = i5;
    while (i > 0 && i3 > 0) {
      long l1 = 0L;
      i4 = Integer.MAX_VALUE;
      m = 0;
      i5 = 0;
      k = paramInt1;
      paramInt1 = m;
      while (i5 < i10) {
        long l2;
        int i11;
        LayoutParams layoutParams = (LayoutParams)getChildAt(i5).getLayoutParams();
        if (!layoutParams.expandable) {
          m = paramInt1;
          i11 = i4;
          l2 = l1;
        } else if (layoutParams.cellsUsed < i4) {
          i11 = layoutParams.cellsUsed;
          l2 = 1L << i5;
          m = 1;
        } else {
          m = paramInt1;
          i11 = i4;
          l2 = l1;
          if (layoutParams.cellsUsed == i4) {
            l2 = l1 | 1L << i5;
            m = paramInt1 + 1;
            i11 = i4;
          } 
        } 
        i5++;
        paramInt1 = m;
        i4 = i11;
        l1 = l2;
      } 
      l |= l1;
      if (paramInt1 > i3) {
        paramInt1 = k;
        break;
      } 
      k = 0;
      while (k < i10) {
        long l2;
        View view = getChildAt(k);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if ((l1 & (1 << k)) == 0L) {
          m = i3;
          l2 = l;
          if (layoutParams.cellsUsed == i4 + 1) {
            l2 = l | (1 << k);
            m = i3;
          } 
        } else {
          if (i2 != 0 && layoutParams.preventEdgeOffset && i3 == 1) {
            m = this.mGeneratedItemPadding;
            view.setPadding(m + i9, 0, m, 0);
          } 
          layoutParams.cellsUsed++;
          layoutParams.expanded = true;
          m = i3 - 1;
          l2 = l;
        } 
        k++;
        i3 = m;
        l = l2;
      } 
      paramInt1 = 1;
    } 
    if (paramInt2 == 0 && b == 1) {
      paramInt2 = 1;
    } else {
      paramInt2 = 0;
    } 
    if (i3 > 0 && l != 0L && (i3 < b - 1 || paramInt2 != 0 || n > 1)) {
      float f = Long.bitCount(l);
      if (paramInt2 == 0) {
        float f1;
        if ((l & 0x1L) != 0L) {
          f1 = f;
          if (!((LayoutParams)getChildAt(0).getLayoutParams()).preventEdgeOffset)
            f1 = f - 0.5F; 
        } else {
          f1 = f;
        } 
        f = f1;
        if ((l & (1 << i10 - 1)) != 0L) {
          f = f1;
          if (!((LayoutParams)getChildAt(i10 - 1).getLayoutParams()).preventEdgeOffset)
            f = f1 - 0.5F; 
        } 
      } 
      if (f > 0.0F) {
        i = (int)((i3 * i9) / f);
      } else {
        i = 0;
      } 
      m = 0;
      k = paramInt2;
      while (m < i10) {
        if ((l & (1 << m)) == 0L) {
          paramInt2 = paramInt1;
        } else {
          View view = getChildAt(m);
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          if (view instanceof ActionMenuItemView) {
            layoutParams.extraPixels = i;
            layoutParams.expanded = true;
            if (m == 0 && !layoutParams.preventEdgeOffset)
              layoutParams.leftMargin = -i / 2; 
            paramInt2 = 1;
          } else if (layoutParams.isOverflowButton) {
            layoutParams.extraPixels = i;
            layoutParams.expanded = true;
            layoutParams.rightMargin = -i / 2;
            paramInt2 = 1;
          } else {
            if (m != 0)
              layoutParams.leftMargin = i / 2; 
            paramInt2 = paramInt1;
            if (m != i10 - 1) {
              layoutParams.rightMargin = i / 2;
              paramInt2 = paramInt1;
            } 
          } 
        } 
        m++;
        paramInt1 = paramInt2;
      } 
    } 
    if (paramInt1 != 0)
      for (paramInt1 = 0; paramInt1 < i10; paramInt1++) {
        View view = getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.expanded)
          view.measure(View.MeasureSpec.makeMeasureSpec(layoutParams.cellsUsed * i9 + layoutParams.extraPixels, 1073741824), i8); 
      }  
    if (i7 != 1073741824) {
      paramInt1 = j;
    } else {
      paramInt1 = i6;
    } 
    setMeasuredDimension(i1, paramInt1);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return (paramLayoutParams != null && paramLayoutParams instanceof LayoutParams); }
  
  public void dismissPopupMenus() {
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null)
      actionMenuPresenter.dismissPopupMenus(); 
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) { return false; }
  
  protected LayoutParams generateDefaultLayoutParams() {
    LayoutParams layoutParams = new LayoutParams(-2, -2);
    layoutParams.gravity = 16;
    return layoutParams;
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) { return new LayoutParams(getContext(), paramAttributeSet); }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    if (paramLayoutParams != null) {
      LayoutParams layoutParams;
      if (paramLayoutParams instanceof LayoutParams) {
        layoutParams = new LayoutParams((LayoutParams)paramLayoutParams);
      } else {
        layoutParams = new LayoutParams(layoutParams);
      } 
      if (layoutParams.gravity <= 0)
        layoutParams.gravity = 16; 
      return layoutParams;
    } 
    return generateDefaultLayoutParams();
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public LayoutParams generateOverflowButtonLayoutParams() {
    LayoutParams layoutParams = generateDefaultLayoutParams();
    layoutParams.isOverflowButton = true;
    return layoutParams;
  }
  
  public Menu getMenu() {
    if (this.mMenu == null) {
      Context context = getContext();
      this.mMenu = new MenuBuilder(context);
      this.mMenu.setCallback(new MenuBuilderCallback());
      this.mPresenter = new ActionMenuPresenter(context);
      this.mPresenter.setReserveOverflow(true);
      ActionMenuPresenter actionMenuPresenter = this.mPresenter;
      MenuPresenter.Callback callback = this.mActionMenuPresenterCallback;
      if (callback == null)
        callback = new ActionMenuPresenterCallback(); 
      actionMenuPresenter.setCallback(callback);
      this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
      this.mPresenter.setMenuView(this);
    } 
    return this.mMenu;
  }
  
  @Nullable
  public Drawable getOverflowIcon() {
    getMenu();
    return this.mPresenter.getOverflowIcon();
  }
  
  public int getPopupTheme() { return this.mPopupTheme; }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int getWindowAnimations() { return 0; }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected boolean hasSupportDividerBeforeChildAt(int paramInt) {
    if (paramInt == 0)
      return false; 
    View view1 = getChildAt(paramInt - 1);
    View view2 = getChildAt(paramInt);
    byte b = 0;
    boolean bool1 = b;
    if (paramInt < getChildCount()) {
      bool1 = b;
      if (view1 instanceof ActionMenuChildView)
        bool1 = false | ((ActionMenuChildView)view1).needsDividerAfter(); 
    } 
    boolean bool2 = bool1;
    if (paramInt > 0) {
      bool2 = bool1;
      if (view2 instanceof ActionMenuChildView)
        bool2 = bool1 | ((ActionMenuChildView)view2).needsDividerBefore(); 
    } 
    return bool2;
  }
  
  public boolean hideOverflowMenu() {
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    return (actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu());
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void initialize(MenuBuilder paramMenuBuilder) { this.mMenu = paramMenuBuilder; }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean invokeItem(MenuItemImpl paramMenuItemImpl) { return this.mMenu.performItemAction(paramMenuItemImpl, 0); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean isOverflowMenuShowPending() {
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    return (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowPending());
  }
  
  public boolean isOverflowMenuShowing() {
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    return (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing());
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean isOverflowReserved() { return this.mReserveOverflow; }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null) {
      actionMenuPresenter.updateMenuView(false);
      if (this.mPresenter.isOverflowMenuShowing()) {
        this.mPresenter.hideOverflowMenu();
        this.mPresenter.showOverflowMenu();
      } 
    } 
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    dismissPopupMenus();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    View view = this;
    if (!view.mFormatItems) {
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    int i2 = getChildCount();
    int j = (paramInt4 - paramInt2) / 2;
    int i1 = getDividerWidth();
    paramInt2 = 0;
    int i = 0;
    int m = 0;
    paramInt4 = paramInt3 - paramInt1 - getPaddingRight() - getPaddingLeft();
    int n = 0;
    paramBoolean = ViewUtils.isLayoutRtl(this);
    int k;
    for (k = 0; k < i2; k++) {
      View view1 = view.getChildAt(k);
      if (view1.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view1.getLayoutParams();
        if (layoutParams.isOverflowButton) {
          int i3;
          n = view1.getMeasuredWidth();
          paramInt2 = n;
          if (view.hasSupportDividerBeforeChildAt(k))
            paramInt2 = n + i1; 
          int i4 = view1.getMeasuredHeight();
          if (paramBoolean) {
            n = getPaddingLeft() + layoutParams.leftMargin;
            i3 = n + paramInt2;
          } else {
            i3 = getWidth() - getPaddingRight() - layoutParams.rightMargin;
            n = i3 - paramInt2;
          } 
          int i5 = j - i4 / 2;
          view1.layout(n, i5, i3, i5 + i4);
          paramInt4 -= paramInt2;
          n = 1;
        } else {
          int i4 = view1.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
          int i3 = i + i4;
          paramInt4 -= i4;
          i = i3;
          if (view.hasSupportDividerBeforeChildAt(k))
            i = i3 + i1; 
          m++;
        } 
      } 
    } 
    i = 1;
    if (i2 == 1 && n == 0) {
      view = view.getChildAt(0);
      paramInt2 = view.getMeasuredWidth();
      paramInt4 = view.getMeasuredHeight();
      paramInt1 = (paramInt3 - paramInt1) / 2 - paramInt2 / 2;
      paramInt3 = j - paramInt4 / 2;
      view.layout(paramInt1, paramInt3, paramInt1 + paramInt2, paramInt3 + paramInt4);
      return;
    } 
    paramInt1 = i;
    if (n != 0)
      paramInt1 = 0; 
    paramInt1 = m - paramInt1;
    if (paramInt1 > 0) {
      paramInt1 = paramInt4 / paramInt1;
    } else {
      paramInt1 = 0;
    } 
    i = Math.max(0, paramInt1);
    if (paramBoolean) {
      paramInt4 = getWidth() - getPaddingRight();
      paramInt1 = 0;
      paramInt3 = i1;
      while (paramInt1 < i2) {
        View view1 = view.getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view1.getLayoutParams();
        if (view1.getVisibility() != 8 && !layoutParams.isOverflowButton) {
          paramInt4 -= layoutParams.rightMargin;
          k = view1.getMeasuredWidth();
          m = view1.getMeasuredHeight();
          n = j - m / 2;
          view1.layout(paramInt4 - k, n, paramInt4, n + m);
          paramInt4 -= layoutParams.leftMargin + k + i;
        } 
        paramInt1++;
      } 
      return;
    } 
    paramInt2 = getPaddingLeft();
    paramInt1 = 0;
    while (paramInt1 < i2) {
      view = getChildAt(paramInt1);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      paramInt3 = paramInt2;
      if (view.getVisibility() != 8)
        if (layoutParams.isOverflowButton) {
          paramInt3 = paramInt2;
        } else {
          paramInt2 += layoutParams.leftMargin;
          paramInt3 = view.getMeasuredWidth();
          paramInt4 = view.getMeasuredHeight();
          k = j - paramInt4 / 2;
          view.layout(paramInt2, k, paramInt2 + paramInt3, k + paramInt4);
          paramInt3 = paramInt2 + layoutParams.rightMargin + paramInt3 + i;
        }  
      paramInt1++;
      paramInt2 = paramInt3;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    boolean bool;
    boolean bool1 = this.mFormatItems;
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mFormatItems = bool;
    if (bool1 != this.mFormatItems)
      this.mFormatItemsWidth = 0; 
    int i = View.MeasureSpec.getSize(paramInt1);
    if (this.mFormatItems) {
      MenuBuilder menuBuilder = this.mMenu;
      if (menuBuilder != null && i != this.mFormatItemsWidth) {
        this.mFormatItemsWidth = i;
        menuBuilder.onItemsChanged(true);
      } 
    } 
    int j = getChildCount();
    if (this.mFormatItems && j > 0) {
      onMeasureExactFormat(paramInt1, paramInt2);
      return;
    } 
    for (i = 0; i < j; i++) {
      LayoutParams layoutParams = (LayoutParams)getChildAt(i).getLayoutParams();
      layoutParams.rightMargin = 0;
      layoutParams.leftMargin = 0;
    } 
    super.onMeasure(paramInt1, paramInt2);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public MenuBuilder peekMenu() { return this.mMenu; }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setExpandedActionViewsExclusive(boolean paramBoolean) { this.mPresenter.setExpandedActionViewsExclusive(paramBoolean); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1) {
    this.mActionMenuPresenterCallback = paramCallback;
    this.mMenuBuilderCallback = paramCallback1;
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener) { this.mOnMenuItemClickListener = paramOnMenuItemClickListener; }
  
  public void setOverflowIcon(@Nullable Drawable paramDrawable) {
    getMenu();
    this.mPresenter.setOverflowIcon(paramDrawable);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setOverflowReserved(boolean paramBoolean) { this.mReserveOverflow = paramBoolean; }
  
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
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setPresenter(ActionMenuPresenter paramActionMenuPresenter) {
    this.mPresenter = paramActionMenuPresenter;
    this.mPresenter.setMenuView(this);
  }
  
  public boolean showOverflowMenu() {
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    return (actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu());
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static interface ActionMenuChildView {
    boolean needsDividerAfter();
    
    boolean needsDividerBefore();
  }
  
  private static class ActionMenuPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {}
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) { return false; }
  }
  
  public static class LayoutParams extends LinearLayoutCompat.LayoutParams {
    @ExportedProperty
    public int cellsUsed;
    
    @ExportedProperty
    public boolean expandable;
    
    boolean expanded;
    
    @ExportedProperty
    public int extraPixels;
    
    @ExportedProperty
    public boolean isOverflowButton;
    
    @ExportedProperty
    public boolean preventEdgeOffset;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.isOverflowButton = false;
    }
    
    LayoutParams(int param1Int1, int param1Int2, boolean param1Boolean) {
      super(param1Int1, param1Int2);
      this.isOverflowButton = param1Boolean;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) { super(param1Context, param1AttributeSet); }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.isOverflowButton = param1LayoutParams.isOverflowButton;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) { super(param1LayoutParams); }
  }
  
  private class MenuBuilderCallback implements MenuBuilder.Callback {
    public boolean onMenuItemSelected(MenuBuilder param1MenuBuilder, MenuItem param1MenuItem) { return (ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(param1MenuItem)); }
    
    public void onMenuModeChange(MenuBuilder param1MenuBuilder) {
      if (ActionMenuView.this.mMenuBuilderCallback != null)
        ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(param1MenuBuilder); 
    }
  }
  
  public static interface OnMenuItemClickListener {
    boolean onMenuItemClick(MenuItem param1MenuItem);
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\ActionMenuView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */