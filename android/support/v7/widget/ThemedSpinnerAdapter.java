package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.widget.SpinnerAdapter;

public interface ThemedSpinnerAdapter extends SpinnerAdapter {
  @Nullable
  Resources.Theme getDropDownViewTheme();
  
  void setDropDownViewTheme(@Nullable Resources.Theme paramTheme);
  
  public static final class Helper {
    private final Context mContext;
    
    private LayoutInflater mDropDownInflater;
    
    private final LayoutInflater mInflater;
    
    public Helper(@NonNull Context param1Context) {
      this.mContext = param1Context;
      this.mInflater = LayoutInflater.from(param1Context);
    }
    
    @NonNull
    public LayoutInflater getDropDownViewInflater() {
      LayoutInflater layoutInflater = this.mDropDownInflater;
      return (layoutInflater != null) ? layoutInflater : this.mInflater;
    }
    
    @Nullable
    public Resources.Theme getDropDownViewTheme() {
      LayoutInflater layoutInflater = this.mDropDownInflater;
      return (layoutInflater == null) ? null : layoutInflater.getContext().getTheme();
    }
    
    public void setDropDownViewTheme(@Nullable Resources.Theme param1Theme) {
      if (param1Theme == null) {
        this.mDropDownInflater = null;
        return;
      } 
      if (param1Theme == this.mContext.getTheme()) {
        this.mDropDownInflater = this.mInflater;
        return;
      } 
      this.mDropDownInflater = LayoutInflater.from(new ContextThemeWrapper(this.mContext, param1Theme));
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\ThemedSpinnerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */