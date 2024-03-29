package android.support.v4.hardware.display;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.WindowManager;
import java.util.WeakHashMap;

public final class DisplayManagerCompat {
  public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
  
  private static final WeakHashMap<Context, DisplayManagerCompat> sInstances = new WeakHashMap();
  
  private final Context mContext;
  
  private DisplayManagerCompat(Context paramContext) { this.mContext = paramContext; }
  
  @NonNull
  public static DisplayManagerCompat getInstance(@NonNull Context paramContext) {
    synchronized (sInstances) {
      DisplayManagerCompat displayManagerCompat2 = (DisplayManagerCompat)sInstances.get(paramContext);
      DisplayManagerCompat displayManagerCompat1 = displayManagerCompat2;
      if (displayManagerCompat2 == null) {
        displayManagerCompat1 = new DisplayManagerCompat(paramContext);
        sInstances.put(paramContext, displayManagerCompat1);
      } 
      return displayManagerCompat1;
    } 
  }
  
  @Nullable
  public Display getDisplay(int paramInt) {
    if (Build.VERSION.SDK_INT >= 17)
      return ((DisplayManager)this.mContext.getSystemService("display")).getDisplay(paramInt); 
    Display display = ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay();
    return (display.getDisplayId() == paramInt) ? display : null;
  }
  
  @NonNull
  public Display[] getDisplays() { return (Build.VERSION.SDK_INT >= 17) ? ((DisplayManager)this.mContext.getSystemService("display")).getDisplays() : new Display[] { ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay() }; }
  
  @NonNull
  public Display[] getDisplays(@Nullable String paramString) { return (Build.VERSION.SDK_INT >= 17) ? ((DisplayManager)this.mContext.getSystemService("display")).getDisplays(paramString) : ((paramString == null) ? new Display[0] : new Display[] { ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay() }); }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\hardware\display\DisplayManagerCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */