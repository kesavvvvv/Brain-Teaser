package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.widget.TintableImageSourceView;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class AppCompatImageButton extends ImageButton implements TintableBackgroundView, TintableImageSourceView {
  private final AppCompatBackgroundHelper mBackgroundTintHelper = new AppCompatBackgroundHelper(this);
  
  private final AppCompatImageHelper mImageHelper;
  
  public AppCompatImageButton(Context paramContext) { this(paramContext, null); }
  
  public AppCompatImageButton(Context paramContext, AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, R.attr.imageButtonStyle); }
  
  public AppCompatImageButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(TintContextWrapper.wrap(paramContext), paramAttributeSet, paramInt);
    this.mBackgroundTintHelper.loadFromAttributes(paramAttributeSet, paramInt);
    this.mImageHelper = new AppCompatImageHelper(this);
    this.mImageHelper.loadFromAttributes(paramAttributeSet, paramInt);
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.applySupportBackgroundTint(); 
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    if (appCompatImageHelper != null)
      appCompatImageHelper.applySupportImageTint(); 
  }
  
  @Nullable
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public ColorStateList getSupportBackgroundTintList() {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    return (appCompatBackgroundHelper != null) ? appCompatBackgroundHelper.getSupportBackgroundTintList() : null;
  }
  
  @Nullable
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public PorterDuff.Mode getSupportBackgroundTintMode() {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    return (appCompatBackgroundHelper != null) ? appCompatBackgroundHelper.getSupportBackgroundTintMode() : null;
  }
  
  @Nullable
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public ColorStateList getSupportImageTintList() {
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    return (appCompatImageHelper != null) ? appCompatImageHelper.getSupportImageTintList() : null;
  }
  
  @Nullable
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public PorterDuff.Mode getSupportImageTintMode() {
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    return (appCompatImageHelper != null) ? appCompatImageHelper.getSupportImageTintMode() : null;
  }
  
  public boolean hasOverlappingRendering() { return (this.mImageHelper.hasOverlappingRendering() && super.hasOverlappingRendering()); }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    super.setBackgroundDrawable(paramDrawable);
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.onSetBackgroundDrawable(paramDrawable); 
  }
  
  public void setBackgroundResource(@DrawableRes int paramInt) {
    super.setBackgroundResource(paramInt);
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.onSetBackgroundResource(paramInt); 
  }
  
  public void setImageBitmap(Bitmap paramBitmap) {
    super.setImageBitmap(paramBitmap);
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    if (appCompatImageHelper != null)
      appCompatImageHelper.applySupportImageTint(); 
  }
  
  public void setImageDrawable(@Nullable Drawable paramDrawable) {
    super.setImageDrawable(paramDrawable);
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    if (appCompatImageHelper != null)
      appCompatImageHelper.applySupportImageTint(); 
  }
  
  public void setImageResource(@DrawableRes int paramInt) { this.mImageHelper.setImageResource(paramInt); }
  
  public void setImageURI(@Nullable Uri paramUri) {
    super.setImageURI(paramUri);
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    if (appCompatImageHelper != null)
      appCompatImageHelper.applySupportImageTint(); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setSupportBackgroundTintList(@Nullable ColorStateList paramColorStateList) {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.setSupportBackgroundTintList(paramColorStateList); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setSupportBackgroundTintMode(@Nullable PorterDuff.Mode paramMode) {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.setSupportBackgroundTintMode(paramMode); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setSupportImageTintList(@Nullable ColorStateList paramColorStateList) {
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    if (appCompatImageHelper != null)
      appCompatImageHelper.setSupportImageTintList(paramColorStateList); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setSupportImageTintMode(@Nullable PorterDuff.Mode paramMode) {
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    if (appCompatImageHelper != null)
      appCompatImageHelper.setSupportImageTintMode(paramMode); 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\AppCompatImageButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */