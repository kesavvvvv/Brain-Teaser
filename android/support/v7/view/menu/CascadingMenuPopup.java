package android.support.v7.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.MenuPopupWindow;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class CascadingMenuPopup extends MenuPopup implements MenuPresenter, View.OnKeyListener, PopupWindow.OnDismissListener {
  static final int HORIZ_POSITION_LEFT = 0;
  
  static final int HORIZ_POSITION_RIGHT = 1;
  
  private static final int ITEM_LAYOUT = R.layout.abc_cascading_menu_item_layout;
  
  static final int SUBMENU_TIMEOUT_MS = 200;
  
  private View mAnchorView;
  
  private final View.OnAttachStateChangeListener mAttachStateChangeListener = new View.OnAttachStateChangeListener() {
      public void onViewAttachedToWindow(View param1View) {}
      
      public void onViewDetachedFromWindow(View param1View) {
        if (CascadingMenuPopup.this.mTreeObserver != null) {
          if (!CascadingMenuPopup.this.mTreeObserver.isAlive())
            CascadingMenuPopup.this.mTreeObserver = param1View.getViewTreeObserver(); 
          CascadingMenuPopup.this.mTreeObserver.removeGlobalOnLayoutListener(CascadingMenuPopup.this.mGlobalLayoutListener);
        } 
        param1View.removeOnAttachStateChangeListener(this);
      }
    };
  
  private final Context mContext;
  
  private int mDropDownGravity = 0;
  
  private boolean mForceShowIcon;
  
  final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
      public void onGlobalLayout() {
        if (CascadingMenuPopup.this.isShowing() && CascadingMenuPopup.this.mShowingMenus.size() > 0 && !((CascadingMenuPopup.CascadingMenuInfo)this.this$0.mShowingMenus.get(0)).window.isModal()) {
          View view = CascadingMenuPopup.this.mShownAnchorView;
          if (view == null || !view.isShown()) {
            CascadingMenuPopup.this.dismiss();
            return;
          } 
          Iterator iterator = CascadingMenuPopup.this.mShowingMenus.iterator();
          while (iterator.hasNext())
            ((CascadingMenuPopup.CascadingMenuInfo)iterator.next()).window.show(); 
        } 
      }
    };
  
  private boolean mHasXOffset;
  
  private boolean mHasYOffset;
  
  private int mLastPosition;
  
  private final MenuItemHoverListener mMenuItemHoverListener = new MenuItemHoverListener() {
      public void onItemHoverEnter(@NonNull final MenuBuilder menu, @NonNull final MenuItem item) {
        final CascadingMenuPopup.CascadingMenuInfo nextInfo;
        byte b2;
        CascadingMenuPopup.this.mSubMenuHoverHandler.removeCallbacksAndMessages(null);
        byte b3 = -1;
        byte b1 = 0;
        int i = CascadingMenuPopup.this.mShowingMenus.size();
        while (true) {
          b2 = b3;
          if (b1 < i) {
            if (param1MenuBuilder == ((CascadingMenuPopup.CascadingMenuInfo)this.this$0.mShowingMenus.get(b1)).menu) {
              b2 = b1;
              break;
            } 
            b1++;
            continue;
          } 
          break;
        } 
        if (b2 == -1)
          return; 
        b1 = b2 + 1;
        if (b1 < CascadingMenuPopup.this.mShowingMenus.size()) {
          cascadingMenuInfo = (CascadingMenuPopup.CascadingMenuInfo)CascadingMenuPopup.this.mShowingMenus.get(b1);
        } else {
          cascadingMenuInfo = null;
        } 
        Runnable runnable = new Runnable() {
            public void run() {
              if (nextInfo != null) {
                CascadingMenuPopup.this.mShouldCloseImmediately = true;
                this.val$nextInfo.menu.close(false);
                CascadingMenuPopup.this.mShouldCloseImmediately = false;
              } 
              if (item.isEnabled() && item.hasSubMenu())
                menu.performItemAction(item, 4); 
            }
          };
        long l = SystemClock.uptimeMillis();
        CascadingMenuPopup.this.mSubMenuHoverHandler.postAtTime(runnable, param1MenuBuilder, l + 200L);
      }
      
      public void onItemHoverExit(@NonNull MenuBuilder param1MenuBuilder, @NonNull MenuItem param1MenuItem) { CascadingMenuPopup.this.mSubMenuHoverHandler.removeCallbacksAndMessages(param1MenuBuilder); }
    };
  
  private final int mMenuMaxWidth;
  
  private PopupWindow.OnDismissListener mOnDismissListener;
  
  private final boolean mOverflowOnly;
  
  private final List<MenuBuilder> mPendingMenus = new ArrayList();
  
  private final int mPopupStyleAttr;
  
  private final int mPopupStyleRes;
  
  private MenuPresenter.Callback mPresenterCallback;
  
  private int mRawDropDownGravity = 0;
  
  boolean mShouldCloseImmediately;
  
  private boolean mShowTitle;
  
  final List<CascadingMenuInfo> mShowingMenus = new ArrayList();
  
  View mShownAnchorView;
  
  final Handler mSubMenuHoverHandler;
  
  ViewTreeObserver mTreeObserver;
  
  private int mXOffset;
  
  private int mYOffset;
  
  public CascadingMenuPopup(@NonNull Context paramContext, @NonNull View paramView, @AttrRes int paramInt1, @StyleRes int paramInt2, boolean paramBoolean) {
    this.mContext = paramContext;
    this.mAnchorView = paramView;
    this.mPopupStyleAttr = paramInt1;
    this.mPopupStyleRes = paramInt2;
    this.mOverflowOnly = paramBoolean;
    this.mForceShowIcon = false;
    this.mLastPosition = getInitialMenuPosition();
    Resources resources = paramContext.getResources();
    this.mMenuMaxWidth = Math.max((resources.getDisplayMetrics()).widthPixels / 2, resources.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
    this.mSubMenuHoverHandler = new Handler();
  }
  
  private MenuPopupWindow createPopupWindow() {
    MenuPopupWindow menuPopupWindow = new MenuPopupWindow(this.mContext, null, this.mPopupStyleAttr, this.mPopupStyleRes);
    menuPopupWindow.setHoverListener(this.mMenuItemHoverListener);
    menuPopupWindow.setOnItemClickListener(this);
    menuPopupWindow.setOnDismissListener(this);
    menuPopupWindow.setAnchorView(this.mAnchorView);
    menuPopupWindow.setDropDownGravity(this.mDropDownGravity);
    menuPopupWindow.setModal(true);
    menuPopupWindow.setInputMethodMode(2);
    return menuPopupWindow;
  }
  
  private int findIndexOfAddedMenu(@NonNull MenuBuilder paramMenuBuilder) {
    byte b = 0;
    int i = this.mShowingMenus.size();
    while (b < i) {
      if (paramMenuBuilder == ((CascadingMenuInfo)this.mShowingMenus.get(b)).menu)
        return b; 
      b++;
    } 
    return -1;
  }
  
  private MenuItem findMenuItemForSubmenu(@NonNull MenuBuilder paramMenuBuilder1, @NonNull MenuBuilder paramMenuBuilder2) {
    byte b = 0;
    int i = paramMenuBuilder1.size();
    while (b < i) {
      MenuItem menuItem = paramMenuBuilder1.getItem(b);
      if (menuItem.hasSubMenu() && paramMenuBuilder2 == menuItem.getSubMenu())
        return menuItem; 
      b++;
    } 
    return null;
  }
  
  @Nullable
  private View findParentViewForSubmenu(@NonNull CascadingMenuInfo paramCascadingMenuInfo, @NonNull MenuBuilder paramMenuBuilder) {
    byte b2;
    byte b1;
    MenuItem menuItem = findMenuItemForSubmenu(paramCascadingMenuInfo.menu, paramMenuBuilder);
    if (menuItem == null)
      return null; 
    ListView listView = paramCascadingMenuInfo.getListView();
    MenuAdapter menuAdapter = listView.getAdapter();
    if (menuAdapter instanceof HeaderViewListAdapter) {
      HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter)menuAdapter;
      b1 = headerViewListAdapter.getHeadersCount();
      menuAdapter = (MenuAdapter)headerViewListAdapter.getWrappedAdapter();
    } else {
      b1 = 0;
      menuAdapter = (MenuAdapter)menuAdapter;
    } 
    byte b3 = -1;
    int i = 0;
    int j = menuAdapter.getCount();
    while (true) {
      b2 = b3;
      if (i < j) {
        if (menuItem == menuAdapter.getItem(i)) {
          b2 = i;
          break;
        } 
        i++;
        continue;
      } 
      break;
    } 
    if (b2 == -1)
      return null; 
    i = b2 + b1 - listView.getFirstVisiblePosition();
    return (i >= 0) ? ((i >= listView.getChildCount()) ? null : listView.getChildAt(i)) : null;
  }
  
  private int getInitialMenuPosition() {
    int i = ViewCompat.getLayoutDirection(this.mAnchorView);
    byte b = 1;
    if (i == 1)
      b = 0; 
    return b;
  }
  
  private int getNextMenuPosition(int paramInt) {
    List list = this.mShowingMenus;
    ListView listView = ((CascadingMenuInfo)list.get(list.size() - 1)).getListView();
    int[] arrayOfInt = new int[2];
    listView.getLocationOnScreen(arrayOfInt);
    Rect rect = new Rect();
    this.mShownAnchorView.getWindowVisibleDisplayFrame(rect);
    return (this.mLastPosition == 1) ? ((arrayOfInt[0] + listView.getWidth() + paramInt > rect.right) ? 0 : 1) : ((arrayOfInt[0] - paramInt < 0) ? 1 : 0);
  }
  
  private void showMenu(@NonNull MenuBuilder paramMenuBuilder) {
    View view;
    LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
    MenuAdapter menuAdapter = new MenuAdapter(paramMenuBuilder, layoutInflater, this.mOverflowOnly, ITEM_LAYOUT);
    if (!isShowing() && this.mForceShowIcon) {
      menuAdapter.setForceShowIcon(true);
    } else if (isShowing()) {
      menuAdapter.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(paramMenuBuilder));
    } 
    int i = measureIndividualMenuWidth(menuAdapter, null, this.mContext, this.mMenuMaxWidth);
    MenuPopupWindow menuPopupWindow = createPopupWindow();
    menuPopupWindow.setAdapter(menuAdapter);
    menuPopupWindow.setContentWidth(i);
    menuPopupWindow.setDropDownGravity(this.mDropDownGravity);
    if (this.mShowingMenus.size() > 0) {
      List list = this.mShowingMenus;
      CascadingMenuInfo cascadingMenuInfo1 = (CascadingMenuInfo)list.get(list.size() - 1);
      view = findParentViewForSubmenu(cascadingMenuInfo1, paramMenuBuilder);
    } else {
      menuAdapter = null;
      view = null;
    } 
    if (view != null) {
      int m;
      int j;
      menuPopupWindow.setTouchModal(false);
      menuPopupWindow.setEnterTransition(null);
      int k = getNextMenuPosition(i);
      if (k == 1) {
        j = 1;
      } else {
        j = 0;
      } 
      this.mLastPosition = k;
      if (Build.VERSION.SDK_INT >= 26) {
        menuPopupWindow.setAnchorView(view);
        m = 0;
        k = 0;
      } else {
        int[] arrayOfInt1 = new int[2];
        this.mAnchorView.getLocationOnScreen(arrayOfInt1);
        int[] arrayOfInt2 = new int[2];
        view.getLocationOnScreen(arrayOfInt2);
        if ((this.mDropDownGravity & 0x7) == 5) {
          arrayOfInt1[0] = arrayOfInt1[0] + this.mAnchorView.getWidth();
          arrayOfInt2[0] = arrayOfInt2[0] + view.getWidth();
        } 
        m = arrayOfInt2[0] - arrayOfInt1[0];
        k = arrayOfInt2[1] - arrayOfInt1[1];
      } 
      if ((this.mDropDownGravity & 0x5) == 5) {
        if (j) {
          j = m + i;
        } else {
          j = m - view.getWidth();
        } 
      } else if (j != 0) {
        j = view.getWidth() + m;
      } else {
        j = m - i;
      } 
      menuPopupWindow.setHorizontalOffset(j);
      menuPopupWindow.setOverlapAnchor(true);
      menuPopupWindow.setVerticalOffset(k);
    } else {
      if (this.mHasXOffset)
        menuPopupWindow.setHorizontalOffset(this.mXOffset); 
      if (this.mHasYOffset)
        menuPopupWindow.setVerticalOffset(this.mYOffset); 
      menuPopupWindow.setEpicenterBounds(getEpicenterBounds());
    } 
    CascadingMenuInfo cascadingMenuInfo = new CascadingMenuInfo(menuPopupWindow, paramMenuBuilder, this.mLastPosition);
    this.mShowingMenus.add(cascadingMenuInfo);
    menuPopupWindow.show();
    ListView listView = menuPopupWindow.getListView();
    listView.setOnKeyListener(this);
    if (menuAdapter == null && this.mShowTitle && paramMenuBuilder.getHeaderTitle() != null) {
      FrameLayout frameLayout = (FrameLayout)layoutInflater.inflate(R.layout.abc_popup_menu_header_item_layout, listView, false);
      TextView textView = (TextView)frameLayout.findViewById(16908310);
      frameLayout.setEnabled(false);
      textView.setText(paramMenuBuilder.getHeaderTitle());
      listView.addHeaderView(frameLayout, null, false);
      menuPopupWindow.show();
    } 
  }
  
  public void addMenu(MenuBuilder paramMenuBuilder) {
    paramMenuBuilder.addMenuPresenter(this, this.mContext);
    if (isShowing()) {
      showMenu(paramMenuBuilder);
      return;
    } 
    this.mPendingMenus.add(paramMenuBuilder);
  }
  
  protected boolean closeMenuOnSubMenuOpened() { return false; }
  
  public void dismiss() {
    int i = this.mShowingMenus.size();
    if (i > 0) {
      CascadingMenuInfo[] arrayOfCascadingMenuInfo = (CascadingMenuInfo[])this.mShowingMenus.toArray(new CascadingMenuInfo[i]);
      while (--i >= 0) {
        CascadingMenuInfo cascadingMenuInfo = arrayOfCascadingMenuInfo[i];
        if (cascadingMenuInfo.window.isShowing())
          cascadingMenuInfo.window.dismiss(); 
        i--;
      } 
    } 
  }
  
  public boolean flagActionItems() { return false; }
  
  public ListView getListView() {
    if (this.mShowingMenus.isEmpty())
      return null; 
    List list = this.mShowingMenus;
    return ((CascadingMenuInfo)list.get(list.size() - 1)).getListView();
  }
  
  public boolean isShowing() {
    int i = this.mShowingMenus.size();
    byte b = 0;
    int j = b;
    if (i > 0) {
      j = b;
      if (((CascadingMenuInfo)this.mShowingMenus.get(0)).window.isShowing())
        j = 1; 
    } 
    return j;
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    int i = findIndexOfAddedMenu(paramMenuBuilder);
    if (i < 0)
      return; 
    int j = i + 1;
    if (j < this.mShowingMenus.size())
      ((CascadingMenuInfo)this.mShowingMenus.get(j)).menu.close(false); 
    CascadingMenuInfo cascadingMenuInfo = (CascadingMenuInfo)this.mShowingMenus.remove(i);
    cascadingMenuInfo.menu.removeMenuPresenter(this);
    if (this.mShouldCloseImmediately) {
      cascadingMenuInfo.window.setExitTransition(null);
      cascadingMenuInfo.window.setAnimationStyle(0);
    } 
    cascadingMenuInfo.window.dismiss();
    i = this.mShowingMenus.size();
    if (i > 0) {
      this.mLastPosition = ((CascadingMenuInfo)this.mShowingMenus.get(i - 1)).position;
    } else {
      this.mLastPosition = getInitialMenuPosition();
    } 
    if (i == 0) {
      dismiss();
      MenuPresenter.Callback callback = this.mPresenterCallback;
      if (callback != null)
        callback.onCloseMenu(paramMenuBuilder, true); 
      ViewTreeObserver viewTreeObserver = this.mTreeObserver;
      if (viewTreeObserver != null) {
        if (viewTreeObserver.isAlive())
          this.mTreeObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener); 
        this.mTreeObserver = null;
      } 
      this.mShownAnchorView.removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
      this.mOnDismissListener.onDismiss();
      return;
    } 
    if (paramBoolean)
      ((CascadingMenuInfo)this.mShowingMenus.get(0)).menu.close(false); 
  }
  
  public void onDismiss() {
    CascadingMenuInfo cascadingMenuInfo;
    Object object = null;
    byte b = 0;
    int i = this.mShowingMenus.size();
    while (true) {
      cascadingMenuInfo = object;
      if (b < i) {
        cascadingMenuInfo = (CascadingMenuInfo)this.mShowingMenus.get(b);
        if (!cascadingMenuInfo.window.isShowing())
          break; 
        b++;
        continue;
      } 
      break;
    } 
    if (cascadingMenuInfo != null)
      cascadingMenuInfo.menu.close(false); 
  }
  
  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
    if (paramKeyEvent.getAction() == 1 && paramInt == 82) {
      dismiss();
      return true;
    } 
    return false;
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {}
  
  public Parcelable onSaveInstanceState() { return null; }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    for (CascadingMenuInfo cascadingMenuInfo : this.mShowingMenus) {
      if (paramSubMenuBuilder == cascadingMenuInfo.menu) {
        cascadingMenuInfo.getListView().requestFocus();
        return true;
      } 
    } 
    if (paramSubMenuBuilder.hasVisibleItems()) {
      addMenu(paramSubMenuBuilder);
      MenuPresenter.Callback callback = this.mPresenterCallback;
      if (callback != null)
        callback.onOpenSubMenu(paramSubMenuBuilder); 
      return true;
    } 
    return false;
  }
  
  public void setAnchorView(@NonNull View paramView) {
    if (this.mAnchorView != paramView) {
      this.mAnchorView = paramView;
      this.mDropDownGravity = GravityCompat.getAbsoluteGravity(this.mRawDropDownGravity, ViewCompat.getLayoutDirection(this.mAnchorView));
    } 
  }
  
  public void setCallback(MenuPresenter.Callback paramCallback) { this.mPresenterCallback = paramCallback; }
  
  public void setForceShowIcon(boolean paramBoolean) { this.mForceShowIcon = paramBoolean; }
  
  public void setGravity(int paramInt) {
    if (this.mRawDropDownGravity != paramInt) {
      this.mRawDropDownGravity = paramInt;
      this.mDropDownGravity = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection(this.mAnchorView));
    } 
  }
  
  public void setHorizontalOffset(int paramInt) {
    this.mHasXOffset = true;
    this.mXOffset = paramInt;
  }
  
  public void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener) { this.mOnDismissListener = paramOnDismissListener; }
  
  public void setShowTitle(boolean paramBoolean) { this.mShowTitle = paramBoolean; }
  
  public void setVerticalOffset(int paramInt) {
    this.mHasYOffset = true;
    this.mYOffset = paramInt;
  }
  
  public void show() {
    if (isShowing())
      return; 
    Iterator iterator = this.mPendingMenus.iterator();
    while (iterator.hasNext())
      showMenu((MenuBuilder)iterator.next()); 
    this.mPendingMenus.clear();
    this.mShownAnchorView = this.mAnchorView;
    if (this.mShownAnchorView != null) {
      boolean bool;
      if (this.mTreeObserver == null) {
        bool = true;
      } else {
        bool = false;
      } 
      this.mTreeObserver = this.mShownAnchorView.getViewTreeObserver();
      if (bool)
        this.mTreeObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener); 
      this.mShownAnchorView.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
    } 
  }
  
  public void updateMenuView(boolean paramBoolean) {
    Iterator iterator = this.mShowingMenus.iterator();
    while (iterator.hasNext())
      toMenuAdapter(((CascadingMenuInfo)iterator.next()).getListView().getAdapter()).notifyDataSetChanged(); 
  }
  
  private static class CascadingMenuInfo {
    public final MenuBuilder menu;
    
    public final int position;
    
    public final MenuPopupWindow window;
    
    public CascadingMenuInfo(@NonNull MenuPopupWindow param1MenuPopupWindow, @NonNull MenuBuilder param1MenuBuilder, int param1Int) {
      this.window = param1MenuPopupWindow;
      this.menu = param1MenuBuilder;
      this.position = param1Int;
    }
    
    public ListView getListView() { return this.window.getListView(); }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface HorizPosition {}
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\view\menu\CascadingMenuPopup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */