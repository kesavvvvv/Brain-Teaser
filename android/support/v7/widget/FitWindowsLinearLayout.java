package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.util.AttributeSet;
import android.widget.LinearLayout;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class FitWindowsLinearLayout extends LinearLayout implements FitWindowsViewGroup {
  private FitWindowsViewGroup.OnFitSystemWindowsListener mListener;
  
  public FitWindowsLinearLayout(Context paramContext) { super(paramContext); }
  
  public FitWindowsLinearLayout(Context paramContext, AttributeSet paramAttributeSet) { super(paramContext, paramAttributeSet); }
  
  protected boolean fitSystemWindows(Rect paramRect) {
    FitWindowsViewGroup.OnFitSystemWindowsListener onFitSystemWindowsListener = this.mListener;
    if (onFitSystemWindowsListener != null)
      onFitSystemWindowsListener.onFitSystemWindows(paramRect); 
    return super.fitSystemWindows(paramRect);
  }
  
  public void setOnFitSystemWindowsListener(FitWindowsViewGroup.OnFitSystemWindowsListener paramOnFitSystemWindowsListener) { this.mListener = paramOnFitSystemWindowsListener; }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\FitWindowsLinearLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */