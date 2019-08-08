package android.support.v7.view.menu;

import android.support.annotation.RestrictTo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class MenuAdapter extends BaseAdapter {
  MenuBuilder mAdapterMenu;
  
  private int mExpandedIndex = -1;
  
  private boolean mForceShowIcon;
  
  private final LayoutInflater mInflater;
  
  private final int mItemLayoutRes;
  
  private final boolean mOverflowOnly;
  
  public MenuAdapter(MenuBuilder paramMenuBuilder, LayoutInflater paramLayoutInflater, boolean paramBoolean, int paramInt) {
    this.mOverflowOnly = paramBoolean;
    this.mInflater = paramLayoutInflater;
    this.mAdapterMenu = paramMenuBuilder;
    this.mItemLayoutRes = paramInt;
    findExpandedIndex();
  }
  
  void findExpandedIndex() {
    MenuItemImpl menuItemImpl = this.mAdapterMenu.getExpandedItem();
    if (menuItemImpl != null) {
      ArrayList arrayList = this.mAdapterMenu.getNonActionItems();
      int i = arrayList.size();
      for (byte b = 0; b < i; b++) {
        if ((MenuItemImpl)arrayList.get(b) == menuItemImpl) {
          this.mExpandedIndex = b;
          return;
        } 
      } 
    } 
    this.mExpandedIndex = -1;
  }
  
  public MenuBuilder getAdapterMenu() { return this.mAdapterMenu; }
  
  public int getCount() {
    ArrayList arrayList;
    if (this.mOverflowOnly) {
      arrayList = this.mAdapterMenu.getNonActionItems();
    } else {
      arrayList = this.mAdapterMenu.getVisibleItems();
    } 
    return (this.mExpandedIndex < 0) ? arrayList.size() : (arrayList.size() - 1);
  }
  
  public boolean getForceShowIcon() { return this.mForceShowIcon; }
  
  public MenuItemImpl getItem(int paramInt) {
    ArrayList arrayList;
    if (this.mOverflowOnly) {
      arrayList = this.mAdapterMenu.getNonActionItems();
    } else {
      arrayList = this.mAdapterMenu.getVisibleItems();
    } 
    int j = this.mExpandedIndex;
    int i = paramInt;
    if (j >= 0) {
      i = paramInt;
      if (paramInt >= j)
        i = paramInt + 1; 
    } 
    return (MenuItemImpl)arrayList.get(i);
  }
  
  public long getItemId(int paramInt) { return paramInt; }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    boolean bool;
    int i;
    View view = paramView;
    if (paramView == null)
      view = this.mInflater.inflate(this.mItemLayoutRes, paramViewGroup, false); 
    int j = getItem(paramInt).getGroupId();
    if (paramInt - 1 >= 0) {
      i = getItem(paramInt - 1).getGroupId();
    } else {
      i = j;
    } 
    ListMenuItemView listMenuItemView = (ListMenuItemView)view;
    if (this.mAdapterMenu.isGroupDividerEnabled() && j != i) {
      bool = true;
    } else {
      bool = false;
    } 
    listMenuItemView.setGroupDividerEnabled(bool);
    MenuView.ItemView itemView = (MenuView.ItemView)view;
    if (this.mForceShowIcon)
      ((ListMenuItemView)view).setForceShowIcon(true); 
    itemView.initialize(getItem(paramInt), 0);
    return view;
  }
  
  public void notifyDataSetChanged() {
    findExpandedIndex();
    super.notifyDataSetChanged();
  }
  
  public void setForceShowIcon(boolean paramBoolean) { this.mForceShowIcon = paramBoolean; }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\view\menu\MenuAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */