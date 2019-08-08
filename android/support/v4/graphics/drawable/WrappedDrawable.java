package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public interface WrappedDrawable {
  Drawable getWrappedDrawable();
  
  void setWrappedDrawable(Drawable paramDrawable);
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\graphics\drawable\WrappedDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */