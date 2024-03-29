package android.support.v4.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v4.internal.view.SupportMenu;
import android.view.Menu;
import android.view.MenuItem;

public final class MenuCompat {
  @SuppressLint({"NewApi"})
  public static void setGroupDividerEnabled(Menu paramMenu, boolean paramBoolean) {
    if (paramMenu instanceof SupportMenu) {
      ((SupportMenu)paramMenu).setGroupDividerEnabled(paramBoolean);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 28)
      paramMenu.setGroupDividerEnabled(paramBoolean); 
  }
  
  @Deprecated
  public static void setShowAsAction(MenuItem paramMenuItem, int paramInt) { paramMenuItem.setShowAsAction(paramInt); }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\view\MenuCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */