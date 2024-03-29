package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

public final class CompoundButtonCompat {
  private static final String TAG = "CompoundButtonCompat";
  
  private static Field sButtonDrawableField;
  
  private static boolean sButtonDrawableFieldFetched;
  
  @Nullable
  public static Drawable getButtonDrawable(@NonNull CompoundButton paramCompoundButton) {
    if (Build.VERSION.SDK_INT >= 23)
      return paramCompoundButton.getButtonDrawable(); 
    if (!sButtonDrawableFieldFetched) {
      try {
        sButtonDrawableField = CompoundButton.class.getDeclaredField("mButtonDrawable");
        sButtonDrawableField.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {
        Log.i("CompoundButtonCompat", "Failed to retrieve mButtonDrawable field", noSuchFieldException);
      } 
      sButtonDrawableFieldFetched = true;
    } 
    Field field = sButtonDrawableField;
    if (field != null)
      try {
        return (Drawable)field.get(paramCompoundButton);
      } catch (IllegalAccessException paramCompoundButton) {
        Log.i("CompoundButtonCompat", "Failed to get button drawable via reflection", paramCompoundButton);
        sButtonDrawableField = null;
        return null;
      }  
    return null;
  }
  
  @Nullable
  public static ColorStateList getButtonTintList(@NonNull CompoundButton paramCompoundButton) { return (Build.VERSION.SDK_INT >= 21) ? paramCompoundButton.getButtonTintList() : ((paramCompoundButton instanceof TintableCompoundButton) ? ((TintableCompoundButton)paramCompoundButton).getSupportButtonTintList() : null); }
  
  @Nullable
  public static PorterDuff.Mode getButtonTintMode(@NonNull CompoundButton paramCompoundButton) { return (Build.VERSION.SDK_INT >= 21) ? paramCompoundButton.getButtonTintMode() : ((paramCompoundButton instanceof TintableCompoundButton) ? ((TintableCompoundButton)paramCompoundButton).getSupportButtonTintMode() : null); }
  
  public static void setButtonTintList(@NonNull CompoundButton paramCompoundButton, @Nullable ColorStateList paramColorStateList) {
    if (Build.VERSION.SDK_INT >= 21) {
      paramCompoundButton.setButtonTintList(paramColorStateList);
      return;
    } 
    if (paramCompoundButton instanceof TintableCompoundButton)
      ((TintableCompoundButton)paramCompoundButton).setSupportButtonTintList(paramColorStateList); 
  }
  
  public static void setButtonTintMode(@NonNull CompoundButton paramCompoundButton, @Nullable PorterDuff.Mode paramMode) {
    if (Build.VERSION.SDK_INT >= 21) {
      paramCompoundButton.setButtonTintMode(paramMode);
      return;
    } 
    if (paramCompoundButton instanceof TintableCompoundButton)
      ((TintableCompoundButton)paramCompoundButton).setSupportButtonTintMode(paramMode); 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\widget\CompoundButtonCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */