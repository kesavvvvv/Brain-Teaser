package android.support.v4.content.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.IconCompat;
import android.text.TextUtils;
import java.util.Arrays;

public class ShortcutInfoCompat {
  ComponentName mActivity;
  
  Context mContext;
  
  CharSequence mDisabledMessage;
  
  IconCompat mIcon;
  
  String mId;
  
  Intent[] mIntents;
  
  boolean mIsAlwaysBadged;
  
  CharSequence mLabel;
  
  CharSequence mLongLabel;
  
  Intent addToIntent(Intent paramIntent) {
    Intent[] arrayOfIntent = this.mIntents;
    paramIntent.putExtra("android.intent.extra.shortcut.INTENT", arrayOfIntent[arrayOfIntent.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
    if (this.mIcon != null) {
      ComponentName componentName1;
      componentName2 = null;
      arrayOfIntent = null;
      if (this.mIsAlwaysBadged) {
        PackageManager packageManager = this.mContext.getPackageManager();
        componentName2 = this.mActivity;
        if (componentName2 != null)
          try {
            Drawable drawable = packageManager.getActivityIcon(componentName2);
            componentName1 = drawable;
          } catch (android.content.pm.PackageManager.NameNotFoundException componentName2) {} 
        if (componentName1 == null)
          componentName1 = this.mContext.getApplicationInfo().loadIcon(packageManager); 
      } else {
        componentName1 = componentName2;
      } 
      this.mIcon.addToShortcutIntent(paramIntent, componentName1, this.mContext);
      return paramIntent;
    } 
    return paramIntent;
  }
  
  @Nullable
  public ComponentName getActivity() { return this.mActivity; }
  
  @Nullable
  public CharSequence getDisabledMessage() { return this.mDisabledMessage; }
  
  @NonNull
  public String getId() { return this.mId; }
  
  @NonNull
  public Intent getIntent() {
    Intent[] arrayOfIntent = this.mIntents;
    return arrayOfIntent[arrayOfIntent.length - 1];
  }
  
  @NonNull
  public Intent[] getIntents() {
    Intent[] arrayOfIntent = this.mIntents;
    return (Intent[])Arrays.copyOf(arrayOfIntent, arrayOfIntent.length);
  }
  
  @Nullable
  public CharSequence getLongLabel() { return this.mLongLabel; }
  
  @NonNull
  public CharSequence getShortLabel() { return this.mLabel; }
  
  @RequiresApi(25)
  public ShortcutInfo toShortcutInfo() {
    ShortcutInfo.Builder builder = (new ShortcutInfo.Builder(this.mContext, this.mId)).setShortLabel(this.mLabel).setIntents(this.mIntents);
    IconCompat iconCompat = this.mIcon;
    if (iconCompat != null)
      builder.setIcon(iconCompat.toIcon()); 
    if (!TextUtils.isEmpty(this.mLongLabel))
      builder.setLongLabel(this.mLongLabel); 
    if (!TextUtils.isEmpty(this.mDisabledMessage))
      builder.setDisabledMessage(this.mDisabledMessage); 
    ComponentName componentName = this.mActivity;
    if (componentName != null)
      builder.setActivity(componentName); 
    return builder.build();
  }
  
  public static class Builder {
    private final ShortcutInfoCompat mInfo = new ShortcutInfoCompat();
    
    public Builder(@NonNull Context param1Context, @NonNull String param1String) {
      ShortcutInfoCompat shortcutInfoCompat = this.mInfo;
      shortcutInfoCompat.mContext = param1Context;
      shortcutInfoCompat.mId = param1String;
    }
    
    @NonNull
    public ShortcutInfoCompat build() {
      if (!TextUtils.isEmpty(this.mInfo.mLabel)) {
        if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0)
          return this.mInfo; 
        throw new IllegalArgumentException("Shortcut must have an intent");
      } 
      throw new IllegalArgumentException("Shortcut must have a non-empty label");
    }
    
    @NonNull
    public Builder setActivity(@NonNull ComponentName param1ComponentName) {
      this.mInfo.mActivity = param1ComponentName;
      return this;
    }
    
    public Builder setAlwaysBadged() {
      this.mInfo.mIsAlwaysBadged = true;
      return this;
    }
    
    @NonNull
    public Builder setDisabledMessage(@NonNull CharSequence param1CharSequence) {
      this.mInfo.mDisabledMessage = param1CharSequence;
      return this;
    }
    
    @NonNull
    public Builder setIcon(IconCompat param1IconCompat) {
      this.mInfo.mIcon = param1IconCompat;
      return this;
    }
    
    @NonNull
    public Builder setIntent(@NonNull Intent param1Intent) { return setIntents(new Intent[] { param1Intent }); }
    
    @NonNull
    public Builder setIntents(@NonNull Intent[] param1ArrayOfIntent) {
      this.mInfo.mIntents = param1ArrayOfIntent;
      return this;
    }
    
    @NonNull
    public Builder setLongLabel(@NonNull CharSequence param1CharSequence) {
      this.mInfo.mLongLabel = param1CharSequence;
      return this;
    }
    
    @NonNull
    public Builder setShortLabel(@NonNull CharSequence param1CharSequence) {
      this.mInfo.mLabel = param1CharSequence;
      return this;
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\content\pm\ShortcutInfoCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */