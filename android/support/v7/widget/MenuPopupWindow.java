package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.transition.Transition;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.HeaderViewListAdapter;
import java.lang.reflect.Method;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class MenuPopupWindow extends ListPopupWindow implements MenuItemHoverListener {
  private static final String TAG = "MenuPopupWindow";
  
  private static Method sSetTouchModalMethod;
  
  private MenuItemHoverListener mHoverListener;
  
  static  {
    try {
      sSetTouchModalMethod = android.widget.PopupWindow.class.getDeclaredMethod("setTouchModal", new Class[] { boolean.class });
      return;
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.i("MenuPopupWindow", "Could not find method setTouchModal() on PopupWindow. Oh well.");
      return;
    } 
  }
  
  public MenuPopupWindow(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) { super(paramContext, paramAttributeSet, paramInt1, paramInt2); }
  
  DropDownListView createDropDownListView(Context paramContext, boolean paramBoolean) {
    MenuDropDownListView menuDropDownListView = new MenuDropDownListView(paramContext, paramBoolean);
    menuDropDownListView.setHoverListener(this);
    return menuDropDownListView;
  }
  
  public void onItemHoverEnter(@NonNull MenuBuilder paramMenuBuilder, @NonNull MenuItem paramMenuItem) {
    MenuItemHoverListener menuItemHoverListener = this.mHoverListener;
    if (menuItemHoverListener != null)
      menuItemHoverListener.onItemHoverEnter(paramMenuBuilder, paramMenuItem); 
  }
  
  public void onItemHoverExit(@NonNull MenuBuilder paramMenuBuilder, @NonNull MenuItem paramMenuItem) {
    MenuItemHoverListener menuItemHoverListener = this.mHoverListener;
    if (menuItemHoverListener != null)
      menuItemHoverListener.onItemHoverExit(paramMenuBuilder, paramMenuItem); 
  }
  
  public void setEnterTransition(Object paramObject) {
    if (Build.VERSION.SDK_INT >= 23)
      this.mPopup.setEnterTransition((Transition)paramObject); 
  }
  
  public void setExitTransition(Object paramObject) {
    if (Build.VERSION.SDK_INT >= 23)
      this.mPopup.setExitTransition((Transition)paramObject); 
  }
  
  public void setHoverListener(MenuItemHoverListener paramMenuItemHoverListener) { this.mHoverListener = paramMenuItemHoverListener; }
  
  public void setTouchModal(boolean paramBoolean) {
    method = sSetTouchModalMethod;
    if (method != null)
      try {
        method.invoke(this.mPopup, new Object[] { Boolean.valueOf(paramBoolean) });
        return;
      } catch (Exception method) {
        Log.i("MenuPopupWindow", "Could not invoke setTouchModal() on PopupWindow. Oh well.");
        return;
      }  
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static class MenuDropDownListView extends DropDownListView {
    final int mAdvanceKey;
    
    private MenuItemHoverListener mHoverListener;
    
    private MenuItem mHoveredMenuItem;
    
    final int mRetreatKey;
    
    public MenuDropDownListView(Context param1Context, boolean param1Boolean) {
      super(param1Context, param1Boolean);
      Configuration configuration = param1Context.getResources().getConfiguration();
      if (Build.VERSION.SDK_INT >= 17 && 1 == configuration.getLayoutDirection()) {
        this.mAdvanceKey = 21;
        this.mRetreatKey = 22;
        return;
      } 
      this.mAdvanceKey = 22;
      this.mRetreatKey = 21;
    }
    
    public void clearSelection() { setSelection(-1); }
    
    public boolean onHoverEvent(MotionEvent param1MotionEvent) {
      if (this.mHoverListener != null) {
        int i;
        MenuAdapter menuAdapter = getAdapter();
        if (menuAdapter instanceof HeaderViewListAdapter) {
          HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter)menuAdapter;
          i = headerViewListAdapter.getHeadersCount();
          menuAdapter = (MenuAdapter)headerViewListAdapter.getWrappedAdapter();
        } else {
          i = 0;
          menuAdapter = (MenuAdapter)menuAdapter;
        } 
        MenuItem menuItem = null;
        MenuItemImpl menuItemImpl = menuItem;
        if (param1MotionEvent.getAction() != 10) {
          int j = pointToPosition((int)param1MotionEvent.getX(), (int)param1MotionEvent.getY());
          menuItemImpl = menuItem;
          if (j != -1) {
            i = j - i;
            menuItemImpl = menuItem;
            if (i >= 0) {
              menuItemImpl = menuItem;
              if (i < menuAdapter.getCount())
                menuItemImpl = menuAdapter.getItem(i); 
            } 
          } 
        } 
        menuItem = this.mHoveredMenuItem;
        if (menuItem != menuItemImpl) {
          MenuBuilder menuBuilder = menuAdapter.getAdapterMenu();
          if (menuItem != null)
            this.mHoverListener.onItemHoverExit(menuBuilder, menuItem); 
          this.mHoveredMenuItem = menuItemImpl;
          if (menuItemImpl != null)
            this.mHoverListener.onItemHoverEnter(menuBuilder, menuItemImpl); 
        } 
      } 
      return super.onHoverEvent(param1MotionEvent);
    }
    
    public boolean onKeyDown(int param1Int, KeyEvent param1KeyEvent) {
      ListMenuItemView listMenuItemView = (ListMenuItemView)getSelectedView();
      if (listMenuItemView != null && param1Int == this.mAdvanceKey) {
        if (listMenuItemView.isEnabled() && listMenuItemView.getItemData().hasSubMenu())
          performItemClick(listMenuItemView, getSelectedItemPosition(), getSelectedItemId()); 
        return true;
      } 
      if (listMenuItemView != null && param1Int == this.mRetreatKey) {
        setSelection(-1);
        ((MenuAdapter)getAdapter()).getAdapterMenu().close(false);
        return true;
      } 
      return super.onKeyDown(param1Int, param1KeyEvent);
    }
    
    public void setHoverListener(MenuItemHoverListener param1MenuItemHoverListener) { this.mHoverListener = param1MenuItemHoverListener; }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\MenuPopupWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */