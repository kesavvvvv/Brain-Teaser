package android.support.v7.app;

import android.view.View;
import android.widget.AdapterView;

class NavItemSelectedListener implements AdapterView.OnItemSelectedListener {
  private final ActionBar.OnNavigationListener mListener;
  
  public NavItemSelectedListener(ActionBar.OnNavigationListener paramOnNavigationListener) { this.mListener = paramOnNavigationListener; }
  
  public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
    ActionBar.OnNavigationListener onNavigationListener = this.mListener;
    if (onNavigationListener != null)
      onNavigationListener.onNavigationItemSelected(paramInt, paramLong); 
  }
  
  public void onNothingSelected(AdapterView<?> paramAdapterView) {}
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\app\NavItemSelectedListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */