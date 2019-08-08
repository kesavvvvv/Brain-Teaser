package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.widget.TintableCompoundButton;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class AppCompatCheckBox extends CheckBox implements TintableCompoundButton {
  private final AppCompatCompoundButtonHelper mCompoundButtonHelper = new AppCompatCompoundButtonHelper(this);
  
  public AppCompatCheckBox(Context paramContext) { this(paramContext, null); }
  
  public AppCompatCheckBox(Context paramContext, AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, R.attr.checkboxStyle); }
  
  public AppCompatCheckBox(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(TintContextWrapper.wrap(paramContext), paramAttributeSet, paramInt);
    this.mCompoundButtonHelper.loadFromAttributes(paramAttributeSet, paramInt);
  }
  
  public int getCompoundPaddingLeft() {
    int i = super.getCompoundPaddingLeft();
    AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
    return (appCompatCompoundButtonHelper != null) ? appCompatCompoundButtonHelper.getCompoundPaddingLeft(i) : i;
  }
  
  @Nullable
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public ColorStateList getSupportButtonTintList() {
    AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
    return (appCompatCompoundButtonHelper != null) ? appCompatCompoundButtonHelper.getSupportButtonTintList() : null;
  }
  
  @Nullable
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public PorterDuff.Mode getSupportButtonTintMode() {
    AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
    return (appCompatCompoundButtonHelper != null) ? appCompatCompoundButtonHelper.getSupportButtonTintMode() : null;
  }
  
  public void setButtonDrawable(@DrawableRes int paramInt) { setButtonDrawable(AppCompatResources.getDrawable(getContext(), paramInt)); }
  
  public void setButtonDrawable(Drawable paramDrawable) {
    super.setButtonDrawable(paramDrawable);
    AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
    if (appCompatCompoundButtonHelper != null)
      appCompatCompoundButtonHelper.onSetButtonDrawable(); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setSupportButtonTintList(@Nullable ColorStateList paramColorStateList) {
    AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
    if (appCompatCompoundButtonHelper != null)
      appCompatCompoundButtonHelper.setSupportButtonTintList(paramColorStateList); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setSupportButtonTintMode(@Nullable PorterDuff.Mode paramMode) {
    AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
    if (appCompatCompoundButtonHelper != null)
      appCompatCompoundButtonHelper.setSupportButtonTintMode(paramMode); 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\AppCompatCheckBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */