package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.BaseMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.view.menu.SubMenuBuilder;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

class ActionMenuPresenter extends BaseMenuPresenter implements ActionProvider.SubUiVisibilityListener {
  private static final String TAG = "ActionMenuPresenter";
  
  private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
  
  ActionButtonSubmenu mActionButtonPopup;
  
  private int mActionItemWidthLimit;
  
  private boolean mExpandedActionViewsExclusive;
  
  private int mMaxItems;
  
  private boolean mMaxItemsSet;
  
  private int mMinCellSize;
  
  int mOpenSubMenuId;
  
  OverflowMenuButton mOverflowButton;
  
  OverflowPopup mOverflowPopup;
  
  private Drawable mPendingOverflowIcon;
  
  private boolean mPendingOverflowIconSet;
  
  private ActionMenuPopupCallback mPopupCallback;
  
  final PopupPresenterCallback mPopupPresenterCallback = new PopupPresenterCallback();
  
  OpenOverflowRunnable mPostedOpenRunnable;
  
  private boolean mReserveOverflow;
  
  private boolean mReserveOverflowSet;
  
  private View mScrapActionButtonView;
  
  private boolean mStrictWidthLimit;
  
  private int mWidthLimit;
  
  private boolean mWidthLimitSet;
  
  public ActionMenuPresenter(Context paramContext) { super(paramContext, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout); }
  
  private View findViewForItem(MenuItem paramMenuItem) {
    ViewGroup viewGroup = (ViewGroup)this.mMenuView;
    if (viewGroup == null)
      return null; 
    int i = viewGroup.getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = viewGroup.getChildAt(b);
      if (view instanceof MenuView.ItemView && ((MenuView.ItemView)view).getItemData() == paramMenuItem)
        return view; 
    } 
    return null;
  }
  
  public void bindItemView(MenuItemImpl paramMenuItemImpl, MenuView.ItemView paramItemView) {
    paramItemView.initialize(paramMenuItemImpl, 0);
    ActionMenuView actionMenuView = (ActionMenuView)this.mMenuView;
    ActionMenuItemView actionMenuItemView = (ActionMenuItemView)paramItemView;
    actionMenuItemView.setItemInvoker(actionMenuView);
    if (this.mPopupCallback == null)
      this.mPopupCallback = new ActionMenuPopupCallback(); 
    actionMenuItemView.setPopupCallback(this.mPopupCallback);
  }
  
  public boolean dismissPopupMenus() { return hideOverflowMenu() | hideSubMenus(); }
  
  public boolean filterLeftoverView(ViewGroup paramViewGroup, int paramInt) { return (paramViewGroup.getChildAt(paramInt) == this.mOverflowButton) ? false : super.filterLeftoverView(paramViewGroup, paramInt); }
  
  public boolean flagActionItems() { // Byte code:
    //   0: aload_0
    //   1: astore #16
    //   3: aload #16
    //   5: getfield mMenu : Landroid/support/v7/view/menu/MenuBuilder;
    //   8: ifnull -> 30
    //   11: aload #16
    //   13: getfield mMenu : Landroid/support/v7/view/menu/MenuBuilder;
    //   16: invokevirtual getVisibleItems : ()Ljava/util/ArrayList;
    //   19: astore #15
    //   21: aload #15
    //   23: invokevirtual size : ()I
    //   26: istore_3
    //   27: goto -> 35
    //   30: aconst_null
    //   31: astore #15
    //   33: iconst_0
    //   34: istore_3
    //   35: aload #16
    //   37: getfield mMaxItems : I
    //   40: istore_1
    //   41: aload #16
    //   43: getfield mActionItemWidthLimit : I
    //   46: istore #9
    //   48: iconst_0
    //   49: iconst_0
    //   50: invokestatic makeMeasureSpec : (II)I
    //   53: istore #11
    //   55: aload #16
    //   57: getfield mMenuView : Landroid/support/v7/view/menu/MenuView;
    //   60: checkcast android/view/ViewGroup
    //   63: astore #17
    //   65: iconst_0
    //   66: istore #4
    //   68: iconst_0
    //   69: istore #6
    //   71: iconst_0
    //   72: istore #8
    //   74: iconst_0
    //   75: istore #5
    //   77: iconst_0
    //   78: istore_2
    //   79: iload_2
    //   80: iload_3
    //   81: if_icmpge -> 167
    //   84: aload #15
    //   86: iload_2
    //   87: invokevirtual get : (I)Ljava/lang/Object;
    //   90: checkcast android/support/v7/view/menu/MenuItemImpl
    //   93: astore #18
    //   95: aload #18
    //   97: invokevirtual requiresActionButton : ()Z
    //   100: ifeq -> 112
    //   103: iload #4
    //   105: iconst_1
    //   106: iadd
    //   107: istore #4
    //   109: goto -> 132
    //   112: aload #18
    //   114: invokevirtual requestsActionButton : ()Z
    //   117: ifeq -> 129
    //   120: iload #6
    //   122: iconst_1
    //   123: iadd
    //   124: istore #6
    //   126: goto -> 132
    //   129: iconst_1
    //   130: istore #5
    //   132: iload_1
    //   133: istore #7
    //   135: aload #16
    //   137: getfield mExpandedActionViewsExclusive : Z
    //   140: ifeq -> 157
    //   143: iload_1
    //   144: istore #7
    //   146: aload #18
    //   148: invokevirtual isActionViewExpanded : ()Z
    //   151: ifeq -> 157
    //   154: iconst_0
    //   155: istore #7
    //   157: iload_2
    //   158: iconst_1
    //   159: iadd
    //   160: istore_2
    //   161: iload #7
    //   163: istore_1
    //   164: goto -> 79
    //   167: iload_1
    //   168: istore_2
    //   169: aload #16
    //   171: getfield mReserveOverflow : Z
    //   174: ifeq -> 197
    //   177: iload #5
    //   179: ifne -> 193
    //   182: iload_1
    //   183: istore_2
    //   184: iload #4
    //   186: iload #6
    //   188: iadd
    //   189: iload_1
    //   190: if_icmple -> 197
    //   193: iload_1
    //   194: iconst_1
    //   195: isub
    //   196: istore_2
    //   197: iload_2
    //   198: iload #4
    //   200: isub
    //   201: istore #5
    //   203: aload #16
    //   205: getfield mActionButtonGroups : Landroid/util/SparseBooleanArray;
    //   208: astore #18
    //   210: aload #18
    //   212: invokevirtual clear : ()V
    //   215: iconst_0
    //   216: istore #6
    //   218: iconst_0
    //   219: istore_2
    //   220: aload #16
    //   222: getfield mStrictWidthLimit : Z
    //   225: ifeq -> 249
    //   228: aload #16
    //   230: getfield mMinCellSize : I
    //   233: istore_1
    //   234: iload #9
    //   236: iload_1
    //   237: idiv
    //   238: istore_2
    //   239: iload_1
    //   240: iload #9
    //   242: iload_1
    //   243: irem
    //   244: iload_2
    //   245: idiv
    //   246: iadd
    //   247: istore #6
    //   249: iconst_0
    //   250: istore #7
    //   252: iload #8
    //   254: istore_1
    //   255: iload #4
    //   257: istore #8
    //   259: aload #17
    //   261: astore #16
    //   263: iload #9
    //   265: istore #4
    //   267: iload_3
    //   268: istore #9
    //   270: aload_0
    //   271: astore #17
    //   273: iload #7
    //   275: iload #9
    //   277: if_icmpge -> 813
    //   280: aload #15
    //   282: iload #7
    //   284: invokevirtual get : (I)Ljava/lang/Object;
    //   287: checkcast android/support/v7/view/menu/MenuItemImpl
    //   290: astore #19
    //   292: aload #19
    //   294: invokevirtual requiresActionButton : ()Z
    //   297: ifeq -> 423
    //   300: aload #17
    //   302: aload #19
    //   304: aload #17
    //   306: getfield mScrapActionButtonView : Landroid/view/View;
    //   309: aload #16
    //   311: invokevirtual getItemView : (Landroid/support/v7/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   314: astore #20
    //   316: aload #17
    //   318: getfield mScrapActionButtonView : Landroid/view/View;
    //   321: ifnonnull -> 331
    //   324: aload #17
    //   326: aload #20
    //   328: putfield mScrapActionButtonView : Landroid/view/View;
    //   331: aload #17
    //   333: getfield mStrictWidthLimit : Z
    //   336: ifeq -> 356
    //   339: iload_2
    //   340: aload #20
    //   342: iload #6
    //   344: iload_2
    //   345: iload #11
    //   347: iconst_0
    //   348: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   351: isub
    //   352: istore_2
    //   353: goto -> 365
    //   356: aload #20
    //   358: iload #11
    //   360: iload #11
    //   362: invokevirtual measure : (II)V
    //   365: aload #20
    //   367: invokevirtual getMeasuredWidth : ()I
    //   370: istore #10
    //   372: iload #4
    //   374: iload #10
    //   376: isub
    //   377: istore #4
    //   379: iload_1
    //   380: istore_3
    //   381: iload_1
    //   382: ifne -> 388
    //   385: iload #10
    //   387: istore_3
    //   388: aload #19
    //   390: invokevirtual getGroupId : ()I
    //   393: istore_1
    //   394: iload_1
    //   395: ifeq -> 408
    //   398: aload #18
    //   400: iload_1
    //   401: iconst_1
    //   402: invokevirtual put : (IZ)V
    //   405: goto -> 408
    //   408: aload #19
    //   410: iconst_1
    //   411: invokevirtual setIsActionButton : (Z)V
    //   414: iload #5
    //   416: istore #10
    //   418: iload_3
    //   419: istore_1
    //   420: goto -> 800
    //   423: aload #19
    //   425: invokevirtual requestsActionButton : ()Z
    //   428: ifeq -> 790
    //   431: aload #19
    //   433: invokevirtual getGroupId : ()I
    //   436: istore #12
    //   438: aload #18
    //   440: iload #12
    //   442: invokevirtual get : (I)Z
    //   445: istore #14
    //   447: iload #5
    //   449: ifgt -> 463
    //   452: iload #14
    //   454: ifeq -> 460
    //   457: goto -> 463
    //   460: goto -> 486
    //   463: iload #4
    //   465: ifle -> 486
    //   468: aload #17
    //   470: getfield mStrictWidthLimit : Z
    //   473: ifeq -> 480
    //   476: iload_2
    //   477: ifle -> 486
    //   480: iconst_1
    //   481: istore #13
    //   483: goto -> 489
    //   486: iconst_0
    //   487: istore #13
    //   489: iload #5
    //   491: istore_3
    //   492: iload #13
    //   494: ifeq -> 652
    //   497: aload #17
    //   499: aload #19
    //   501: aload #17
    //   503: getfield mScrapActionButtonView : Landroid/view/View;
    //   506: aload #16
    //   508: invokevirtual getItemView : (Landroid/support/v7/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   511: astore #20
    //   513: aload #17
    //   515: getfield mScrapActionButtonView : Landroid/view/View;
    //   518: ifnonnull -> 528
    //   521: aload #17
    //   523: aload #20
    //   525: putfield mScrapActionButtonView : Landroid/view/View;
    //   528: aload #17
    //   530: getfield mStrictWidthLimit : Z
    //   533: ifeq -> 565
    //   536: aload #20
    //   538: iload #6
    //   540: iload_2
    //   541: iload #11
    //   543: iconst_0
    //   544: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   547: istore #5
    //   549: iload_2
    //   550: iload #5
    //   552: isub
    //   553: istore_2
    //   554: iload #5
    //   556: ifne -> 562
    //   559: iconst_0
    //   560: istore #13
    //   562: goto -> 574
    //   565: aload #20
    //   567: iload #11
    //   569: iload #11
    //   571: invokevirtual measure : (II)V
    //   574: aload #20
    //   576: invokevirtual getMeasuredWidth : ()I
    //   579: istore #10
    //   581: iload #4
    //   583: iload #10
    //   585: isub
    //   586: istore #4
    //   588: iload_1
    //   589: istore #5
    //   591: iload_1
    //   592: ifne -> 599
    //   595: iload #10
    //   597: istore #5
    //   599: aload #17
    //   601: getfield mStrictWidthLimit : Z
    //   604: ifeq -> 628
    //   607: iload #4
    //   609: iflt -> 617
    //   612: iconst_1
    //   613: istore_1
    //   614: goto -> 619
    //   617: iconst_0
    //   618: istore_1
    //   619: iload #13
    //   621: iload_1
    //   622: iand
    //   623: istore #13
    //   625: goto -> 655
    //   628: iload #4
    //   630: iload #5
    //   632: iadd
    //   633: ifle -> 641
    //   636: iconst_1
    //   637: istore_1
    //   638: goto -> 643
    //   641: iconst_0
    //   642: istore_1
    //   643: iload #13
    //   645: iload_1
    //   646: iand
    //   647: istore #13
    //   649: goto -> 655
    //   652: iload_1
    //   653: istore #5
    //   655: iload #13
    //   657: ifeq -> 678
    //   660: iload #12
    //   662: ifeq -> 678
    //   665: aload #18
    //   667: iload #12
    //   669: iconst_1
    //   670: invokevirtual put : (IZ)V
    //   673: iload_3
    //   674: istore_1
    //   675: goto -> 763
    //   678: iload #14
    //   680: ifeq -> 761
    //   683: aload #18
    //   685: iload #12
    //   687: iconst_0
    //   688: invokevirtual put : (IZ)V
    //   691: iconst_0
    //   692: istore #10
    //   694: iload_3
    //   695: istore_1
    //   696: iload #10
    //   698: iload #7
    //   700: if_icmpge -> 758
    //   703: aload #15
    //   705: iload #10
    //   707: invokevirtual get : (I)Ljava/lang/Object;
    //   710: checkcast android/support/v7/view/menu/MenuItemImpl
    //   713: astore #17
    //   715: iload_1
    //   716: istore_3
    //   717: aload #17
    //   719: invokevirtual getGroupId : ()I
    //   722: iload #12
    //   724: if_icmpne -> 747
    //   727: iload_1
    //   728: istore_3
    //   729: aload #17
    //   731: invokevirtual isActionButton : ()Z
    //   734: ifeq -> 741
    //   737: iload_1
    //   738: iconst_1
    //   739: iadd
    //   740: istore_3
    //   741: aload #17
    //   743: iconst_0
    //   744: invokevirtual setIsActionButton : (Z)V
    //   747: iload #10
    //   749: iconst_1
    //   750: iadd
    //   751: istore #10
    //   753: iload_3
    //   754: istore_1
    //   755: goto -> 696
    //   758: goto -> 763
    //   761: iload_3
    //   762: istore_1
    //   763: iload_1
    //   764: istore_3
    //   765: iload #13
    //   767: ifeq -> 774
    //   770: iload_1
    //   771: iconst_1
    //   772: isub
    //   773: istore_3
    //   774: aload #19
    //   776: iload #13
    //   778: invokevirtual setIsActionButton : (Z)V
    //   781: iload_3
    //   782: istore #10
    //   784: iload #5
    //   786: istore_1
    //   787: goto -> 800
    //   790: aload #19
    //   792: iconst_0
    //   793: invokevirtual setIsActionButton : (Z)V
    //   796: iload #5
    //   798: istore #10
    //   800: iload #7
    //   802: iconst_1
    //   803: iadd
    //   804: istore #7
    //   806: iload #10
    //   808: istore #5
    //   810: goto -> 270
    //   813: iconst_1
    //   814: ireturn }
  
  public View getItemView(MenuItemImpl paramMenuItemImpl, View paramView, ViewGroup paramViewGroup) {
    byte b;
    View view = paramMenuItemImpl.getActionView();
    if (view == null || paramMenuItemImpl.hasCollapsibleActionView())
      view = super.getItemView(paramMenuItemImpl, paramView, paramViewGroup); 
    if (paramMenuItemImpl.isActionViewExpanded()) {
      b = 8;
    } else {
      b = 0;
    } 
    view.setVisibility(b);
    ActionMenuView actionMenuView = (ActionMenuView)paramViewGroup;
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (!actionMenuView.checkLayoutParams(layoutParams))
      view.setLayoutParams(actionMenuView.generateLayoutParams(layoutParams)); 
    return view;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    MenuView menuView2 = this.mMenuView;
    MenuView menuView1 = super.getMenuView(paramViewGroup);
    if (menuView2 != menuView1)
      ((ActionMenuView)menuView1).setPresenter(this); 
    return menuView1;
  }
  
  public Drawable getOverflowIcon() {
    OverflowMenuButton overflowMenuButton = this.mOverflowButton;
    return (overflowMenuButton != null) ? overflowMenuButton.getDrawable() : (this.mPendingOverflowIconSet ? this.mPendingOverflowIcon : null);
  }
  
  public boolean hideOverflowMenu() {
    if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
      ((View)this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
      this.mPostedOpenRunnable = null;
      return true;
    } 
    OverflowPopup overflowPopup = this.mOverflowPopup;
    if (overflowPopup != null) {
      overflowPopup.dismiss();
      return true;
    } 
    return false;
  }
  
  public boolean hideSubMenus() {
    ActionButtonSubmenu actionButtonSubmenu = this.mActionButtonPopup;
    if (actionButtonSubmenu != null) {
      actionButtonSubmenu.dismiss();
      return true;
    } 
    return false;
  }
  
  public void initForMenu(@NonNull Context paramContext, @Nullable MenuBuilder paramMenuBuilder) {
    super.initForMenu(paramContext, paramMenuBuilder);
    Resources resources = paramContext.getResources();
    ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(paramContext);
    if (!this.mReserveOverflowSet)
      this.mReserveOverflow = actionBarPolicy.showsOverflowMenuButton(); 
    if (!this.mWidthLimitSet)
      this.mWidthLimit = actionBarPolicy.getEmbeddedMenuWidthLimit(); 
    if (!this.mMaxItemsSet)
      this.mMaxItems = actionBarPolicy.getMaxActionButtons(); 
    int i = this.mWidthLimit;
    if (this.mReserveOverflow) {
      if (this.mOverflowButton == null) {
        this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
        if (this.mPendingOverflowIconSet) {
          this.mOverflowButton.setImageDrawable(this.mPendingOverflowIcon);
          this.mPendingOverflowIcon = null;
          this.mPendingOverflowIconSet = false;
        } 
        int j = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.mOverflowButton.measure(j, j);
      } 
      i -= this.mOverflowButton.getMeasuredWidth();
    } else {
      this.mOverflowButton = null;
    } 
    this.mActionItemWidthLimit = i;
    this.mMinCellSize = (int)((resources.getDisplayMetrics()).density * 56.0F);
    this.mScrapActionButtonView = null;
  }
  
  public boolean isOverflowMenuShowPending() { return (this.mPostedOpenRunnable != null || isOverflowMenuShowing()); }
  
  public boolean isOverflowMenuShowing() {
    OverflowPopup overflowPopup = this.mOverflowPopup;
    return (overflowPopup != null && overflowPopup.isShowing());
  }
  
  public boolean isOverflowReserved() { return this.mReserveOverflow; }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    dismissPopupMenus();
    super.onCloseMenu(paramMenuBuilder, paramBoolean);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (!this.mMaxItemsSet)
      this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons(); 
    if (this.mMenu != null)
      this.mMenu.onItemsChanged(true); 
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState))
      return; 
    paramParcelable = (SavedState)paramParcelable;
    if (paramParcelable.openSubMenuId > 0) {
      MenuItem menuItem = this.mMenu.findItem(paramParcelable.openSubMenuId);
      if (menuItem != null)
        onSubMenuSelected((SubMenuBuilder)menuItem.getSubMenu()); 
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState();
    savedState.openSubMenuId = this.mOpenSubMenuId;
    return savedState;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    boolean bool;
    if (!paramSubMenuBuilder.hasVisibleItems())
      return false; 
    SubMenuBuilder subMenuBuilder;
    for (subMenuBuilder = paramSubMenuBuilder; subMenuBuilder.getParentMenu() != this.mMenu; subMenuBuilder = (SubMenuBuilder)subMenuBuilder.getParentMenu());
    View view = findViewForItem(subMenuBuilder.getItem());
    if (view == null)
      return false; 
    this.mOpenSubMenuId = paramSubMenuBuilder.getItem().getItemId();
    boolean bool1 = false;
    int i = paramSubMenuBuilder.size();
    byte b = 0;
    while (true) {
      bool = bool1;
      if (b < i) {
        MenuItem menuItem = paramSubMenuBuilder.getItem(b);
        if (menuItem.isVisible() && menuItem.getIcon() != null) {
          bool = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    this.mActionButtonPopup = new ActionButtonSubmenu(this.mContext, paramSubMenuBuilder, view);
    this.mActionButtonPopup.setForceShowIcon(bool);
    this.mActionButtonPopup.show();
    super.onSubMenuSelected(paramSubMenuBuilder);
    return true;
  }
  
  public void onSubUiVisibilityChanged(boolean paramBoolean) {
    if (paramBoolean) {
      super.onSubMenuSelected(null);
      return;
    } 
    if (this.mMenu != null)
      this.mMenu.close(false); 
  }
  
  public void setExpandedActionViewsExclusive(boolean paramBoolean) { this.mExpandedActionViewsExclusive = paramBoolean; }
  
  public void setItemLimit(int paramInt) {
    this.mMaxItems = paramInt;
    this.mMaxItemsSet = true;
  }
  
  public void setMenuView(ActionMenuView paramActionMenuView) {
    this.mMenuView = paramActionMenuView;
    paramActionMenuView.initialize(this.mMenu);
  }
  
  public void setOverflowIcon(Drawable paramDrawable) {
    OverflowMenuButton overflowMenuButton = this.mOverflowButton;
    if (overflowMenuButton != null) {
      overflowMenuButton.setImageDrawable(paramDrawable);
      return;
    } 
    this.mPendingOverflowIconSet = true;
    this.mPendingOverflowIcon = paramDrawable;
  }
  
  public void setReserveOverflow(boolean paramBoolean) {
    this.mReserveOverflow = paramBoolean;
    this.mReserveOverflowSet = true;
  }
  
  public void setWidthLimit(int paramInt, boolean paramBoolean) {
    this.mWidthLimit = paramInt;
    this.mStrictWidthLimit = paramBoolean;
    this.mWidthLimitSet = true;
  }
  
  public boolean shouldIncludeItem(int paramInt, MenuItemImpl paramMenuItemImpl) { return paramMenuItemImpl.isActionButton(); }
  
  public boolean showOverflowMenu() {
    if (this.mReserveOverflow && !isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
      this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this, this.mContext, this.mMenu, this.mOverflowButton, true));
      ((View)this.mMenuView).post(this.mPostedOpenRunnable);
      super.onSubMenuSelected(null);
      return true;
    } 
    return false;
  }
  
  public void updateMenuView(boolean paramBoolean) {
    ViewGroup viewGroup;
    super.updateMenuView(paramBoolean);
    ((View)this.mMenuView).requestLayout();
    if (this.mMenu != null) {
      viewGroup = this.mMenu.getActionItems();
      int j = viewGroup.size();
      for (byte b1 = 0; b1 < j; b1++) {
        ActionProvider actionProvider = ((MenuItemImpl)viewGroup.get(b1)).getSupportActionProvider();
        if (actionProvider != null)
          actionProvider.setSubUiVisibilityListener(this); 
      } 
    } 
    if (this.mMenu != null) {
      viewGroup = this.mMenu.getNonActionItems();
    } else {
      viewGroup = null;
    } 
    int i = 0;
    byte b = i;
    if (this.mReserveOverflow) {
      b = i;
      if (viewGroup != null) {
        i = viewGroup.size();
        b = 0;
        if (i == 1) {
          boolean bool = ((MenuItemImpl)viewGroup.get(0)).isActionViewExpanded() ^ true;
        } else if (i > 0) {
          b = 1;
        } 
      } 
    } 
    if (b != 0) {
      if (this.mOverflowButton == null)
        this.mOverflowButton = new OverflowMenuButton(this.mSystemContext); 
      viewGroup = (ViewGroup)this.mOverflowButton.getParent();
      if (viewGroup != this.mMenuView) {
        if (viewGroup != null)
          viewGroup.removeView(this.mOverflowButton); 
        viewGroup = (ActionMenuView)this.mMenuView;
        viewGroup.addView(this.mOverflowButton, viewGroup.generateOverflowButtonLayoutParams());
      } 
    } else {
      OverflowMenuButton overflowMenuButton = this.mOverflowButton;
      if (overflowMenuButton != null && overflowMenuButton.getParent() == this.mMenuView)
        ((ViewGroup)this.mMenuView).removeView(this.mOverflowButton); 
    } 
    ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
  }
  
  private class ActionButtonSubmenu extends MenuPopupHelper {
    public ActionButtonSubmenu(Context param1Context, SubMenuBuilder param1SubMenuBuilder, View param1View) {
      super(param1Context, param1SubMenuBuilder, param1View, false, R.attr.actionOverflowMenuStyle);
      if (!((MenuItemImpl)param1SubMenuBuilder.getItem()).isActionButton()) {
        ActionMenuPresenter.OverflowMenuButton overflowMenuButton;
        if (ActionMenuPresenter.this.mOverflowButton == null) {
          overflowMenuButton = (View)this$0.mMenuView;
        } else {
          overflowMenuButton = ActionMenuPresenter.this.mOverflowButton;
        } 
        setAnchorView(overflowMenuButton);
      } 
      setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
    }
    
    protected void onDismiss() {
      ActionMenuPresenter actionMenuPresenter = ActionMenuPresenter.this;
      actionMenuPresenter.mActionButtonPopup = null;
      actionMenuPresenter.mOpenSubMenuId = 0;
      super.onDismiss();
    }
  }
  
  private class ActionMenuPopupCallback extends ActionMenuItemView.PopupCallback {
    public ShowableListMenu getPopup() { return (ActionMenuPresenter.this.mActionButtonPopup != null) ? ActionMenuPresenter.this.mActionButtonPopup.getPopup() : null; }
  }
  
  private class OpenOverflowRunnable implements Runnable {
    private ActionMenuPresenter.OverflowPopup mPopup;
    
    public OpenOverflowRunnable(ActionMenuPresenter.OverflowPopup param1OverflowPopup) { this.mPopup = param1OverflowPopup; }
    
    public void run() {
      if (ActionMenuPresenter.this.mMenu != null)
        ActionMenuPresenter.this.mMenu.changeMenuMode(); 
      View view = (View)ActionMenuPresenter.this.mMenuView;
      if (view != null && view.getWindowToken() != null && this.mPopup.tryShow())
        ActionMenuPresenter.this.mOverflowPopup = this.mPopup; 
      ActionMenuPresenter.this.mPostedOpenRunnable = null;
    }
  }
  
  private class OverflowMenuButton extends AppCompatImageView implements ActionMenuView.ActionMenuChildView {
    private final float[] mTempPts = new float[2];
    
    public OverflowMenuButton(Context param1Context) {
      super(param1Context, null, R.attr.actionOverflowButtonStyle);
      setClickable(true);
      setFocusable(true);
      setVisibility(0);
      setEnabled(true);
      TooltipCompat.setTooltipText(this, getContentDescription());
      setOnTouchListener(new ForwardingListener(this) {
            public ShowableListMenu getPopup() { return (ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup(); }
            
            public boolean onForwardingStarted() {
              ActionMenuPresenter.OverflowMenuButton.this.this$0.showOverflowMenu();
              return true;
            }
            
            public boolean onForwardingStopped() {
              if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
                return false; 
              ActionMenuPresenter.OverflowMenuButton.this.this$0.hideOverflowMenu();
              return true;
            }
          });
    }
    
    public boolean needsDividerAfter() { return false; }
    
    public boolean needsDividerBefore() { return false; }
    
    public boolean performClick() {
      if (super.performClick())
        return true; 
      playSoundEffect(0);
      ActionMenuPresenter.this.showOverflowMenu();
      return true;
    }
    
    protected boolean setFrame(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      boolean bool = super.setFrame(param1Int1, param1Int2, param1Int3, param1Int4);
      Drawable drawable1 = getDrawable();
      Drawable drawable2 = getBackground();
      if (drawable1 != null && drawable2 != null) {
        int i = getWidth();
        param1Int2 = getHeight();
        param1Int1 = Math.max(i, param1Int2) / 2;
        int j = getPaddingLeft();
        int k = getPaddingRight();
        param1Int3 = getPaddingTop();
        param1Int4 = getPaddingBottom();
        i = (i + j - k) / 2;
        param1Int2 = (param1Int2 + param1Int3 - param1Int4) / 2;
        DrawableCompat.setHotspotBounds(drawable2, i - param1Int1, param1Int2 - param1Int1, i + param1Int1, param1Int2 + param1Int1);
      } 
      return bool;
    }
  }
  
  class null extends ForwardingListener {
    null(View param1View) { super(param1View); }
    
    public ShowableListMenu getPopup() { return (ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup(); }
    
    public boolean onForwardingStarted() {
      this.this$1.this$0.showOverflowMenu();
      return true;
    }
    
    public boolean onForwardingStopped() {
      if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
        return false; 
      this.this$1.this$0.hideOverflowMenu();
      return true;
    }
  }
  
  private class OverflowPopup extends MenuPopupHelper {
    public OverflowPopup(Context param1Context, MenuBuilder param1MenuBuilder, View param1View, boolean param1Boolean) {
      super(param1Context, param1MenuBuilder, param1View, param1Boolean, R.attr.actionOverflowMenuStyle);
      setGravity(8388613);
      setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
    }
    
    protected void onDismiss() {
      if (ActionMenuPresenter.this.mMenu != null)
        ActionMenuPresenter.this.mMenu.close(); 
      ActionMenuPresenter.this.mOverflowPopup = null;
      super.onDismiss();
    }
  }
  
  private class PopupPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      if (param1MenuBuilder instanceof SubMenuBuilder)
        param1MenuBuilder.getRootMenu().close(false); 
      MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
      if (callback != null)
        callback.onCloseMenu(param1MenuBuilder, param1Boolean); 
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      boolean bool = false;
      if (param1MenuBuilder == null)
        return false; 
      ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)param1MenuBuilder).getItem().getItemId();
      MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
      if (callback != null)
        bool = callback.onOpenSubMenu(param1MenuBuilder); 
      return bool;
    }
  }
  
  private static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public ActionMenuPresenter.SavedState createFromParcel(Parcel param2Parcel) { return new ActionMenuPresenter.SavedState(param2Parcel); }
        
        public ActionMenuPresenter.SavedState[] newArray(int param2Int) { return new ActionMenuPresenter.SavedState[param2Int]; }
      };
    
    public int openSubMenuId;
    
    SavedState() {}
    
    SavedState(Parcel param1Parcel) { this.openSubMenuId = param1Parcel.readInt(); }
    
    public int describeContents() { return 0; }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) { param1Parcel.writeInt(this.openSubMenuId); }
  }
  
  static final class null extends Object implements Parcelable.Creator<SavedState> {
    public ActionMenuPresenter.SavedState createFromParcel(Parcel param1Parcel) { return new ActionMenuPresenter.SavedState(param1Parcel); }
    
    public ActionMenuPresenter.SavedState[] newArray(int param1Int) { return new ActionMenuPresenter.SavedState[param1Int]; }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\ActionMenuPresenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */