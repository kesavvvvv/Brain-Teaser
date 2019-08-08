package android.support.v7.view.menu;

import android.content.Context;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.internal.view.SupportSubMenu;
import android.support.v4.util.ArrayMap;
import android.view.MenuItem;
import android.view.SubMenu;
import java.util.Iterator;
import java.util.Map;

abstract class BaseMenuWrapper<T> extends BaseWrapper<T> {
  final Context mContext;
  
  private Map<SupportMenuItem, MenuItem> mMenuItems;
  
  private Map<SupportSubMenu, SubMenu> mSubMenus;
  
  BaseMenuWrapper(Context paramContext, T paramT) {
    super(paramT);
    this.mContext = paramContext;
  }
  
  final MenuItem getMenuItemWrapper(MenuItem paramMenuItem) {
    if (paramMenuItem instanceof SupportMenuItem) {
      SupportMenuItem supportMenuItem = (SupportMenuItem)paramMenuItem;
      if (this.mMenuItems == null)
        this.mMenuItems = new ArrayMap(); 
      MenuItem menuItem = (MenuItem)this.mMenuItems.get(paramMenuItem);
      paramMenuItem = menuItem;
      if (menuItem == null) {
        paramMenuItem = MenuWrapperFactory.wrapSupportMenuItem(this.mContext, supportMenuItem);
        this.mMenuItems.put(supportMenuItem, paramMenuItem);
      } 
      return paramMenuItem;
    } 
    return paramMenuItem;
  }
  
  final SubMenu getSubMenuWrapper(SubMenu paramSubMenu) {
    if (paramSubMenu instanceof SupportSubMenu) {
      SupportSubMenu supportSubMenu = (SupportSubMenu)paramSubMenu;
      if (this.mSubMenus == null)
        this.mSubMenus = new ArrayMap(); 
      SubMenu subMenu = (SubMenu)this.mSubMenus.get(supportSubMenu);
      paramSubMenu = subMenu;
      if (subMenu == null) {
        paramSubMenu = MenuWrapperFactory.wrapSupportSubMenu(this.mContext, supportSubMenu);
        this.mSubMenus.put(supportSubMenu, paramSubMenu);
      } 
      return paramSubMenu;
    } 
    return paramSubMenu;
  }
  
  final void internalClear() {
    Map map = this.mMenuItems;
    if (map != null)
      map.clear(); 
    map = this.mSubMenus;
    if (map != null)
      map.clear(); 
  }
  
  final void internalRemoveGroup(int paramInt) {
    Map map = this.mMenuItems;
    if (map == null)
      return; 
    Iterator iterator = map.keySet().iterator();
    while (iterator.hasNext()) {
      if (paramInt == ((MenuItem)iterator.next()).getGroupId())
        iterator.remove(); 
    } 
  }
  
  final void internalRemoveItem(int paramInt) {
    Map map = this.mMenuItems;
    if (map == null)
      return; 
    Iterator iterator = map.keySet().iterator();
    while (iterator.hasNext()) {
      if (paramInt == ((MenuItem)iterator.next()).getItemId()) {
        iterator.remove();
        break;
      } 
    } 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\view\menu\BaseMenuWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */