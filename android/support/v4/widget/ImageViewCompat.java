package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

public class ImageViewCompat {
  @Nullable
  public static ColorStateList getImageTintList(@NonNull ImageView paramImageView) { return (Build.VERSION.SDK_INT >= 21) ? paramImageView.getImageTintList() : ((paramImageView instanceof TintableImageSourceView) ? ((TintableImageSourceView)paramImageView).getSupportImageTintList() : null); }
  
  @Nullable
  public static PorterDuff.Mode getImageTintMode(@NonNull ImageView paramImageView) { return (Build.VERSION.SDK_INT >= 21) ? paramImageView.getImageTintMode() : ((paramImageView instanceof TintableImageSourceView) ? ((TintableImageSourceView)paramImageView).getSupportImageTintMode() : null); }
  
  public static void setImageTintList(@NonNull ImageView paramImageView, @Nullable ColorStateList paramColorStateList) {
    Drawable drawable;
    if (Build.VERSION.SDK_INT >= 21) {
      paramImageView.setImageTintList(paramColorStateList);
      if (Build.VERSION.SDK_INT == 21) {
        boolean bool;
        drawable = paramImageView.getDrawable();
        if (paramImageView.getImageTintList() != null && paramImageView.getImageTintMode() != null) {
          bool = true;
        } else {
          bool = false;
        } 
        if (drawable != null && bool) {
          if (drawable.isStateful())
            drawable.setState(paramImageView.getDrawableState()); 
          paramImageView.setImageDrawable(drawable);
        } 
        return;
      } 
    } else if (paramImageView instanceof TintableImageSourceView) {
      ((TintableImageSourceView)paramImageView).setSupportImageTintList(drawable);
    } 
  }
  
  public static void setImageTintMode(@NonNull ImageView paramImageView, @Nullable PorterDuff.Mode paramMode) {
    Drawable drawable;
    if (Build.VERSION.SDK_INT >= 21) {
      paramImageView.setImageTintMode(paramMode);
      if (Build.VERSION.SDK_INT == 21) {
        boolean bool;
        drawable = paramImageView.getDrawable();
        if (paramImageView.getImageTintList() != null && paramImageView.getImageTintMode() != null) {
          bool = true;
        } else {
          bool = false;
        } 
        if (drawable != null && bool) {
          if (drawable.isStateful())
            drawable.setState(paramImageView.getDrawableState()); 
          paramImageView.setImageDrawable(drawable);
        } 
        return;
      } 
    } else if (paramImageView instanceof TintableImageSourceView) {
      ((TintableImageSourceView)paramImageView).setSupportImageTintMode(drawable);
    } 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\widget\ImageViewCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */