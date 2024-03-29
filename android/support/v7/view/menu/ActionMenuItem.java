package android.support.v7.view.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.ActionProvider;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ActionMenuItem implements SupportMenuItem {
  private static final int CHECKABLE = 1;
  
  private static final int CHECKED = 2;
  
  private static final int ENABLED = 16;
  
  private static final int EXCLUSIVE = 4;
  
  private static final int HIDDEN = 8;
  
  private static final int NO_ICON = 0;
  
  private final int mCategoryOrder;
  
  private MenuItem.OnMenuItemClickListener mClickListener;
  
  private CharSequence mContentDescription;
  
  private Context mContext;
  
  private int mFlags = 16;
  
  private final int mGroup;
  
  private boolean mHasIconTint = false;
  
  private boolean mHasIconTintMode = false;
  
  private Drawable mIconDrawable;
  
  private int mIconResId = 0;
  
  private ColorStateList mIconTintList = null;
  
  private PorterDuff.Mode mIconTintMode = null;
  
  private final int mId;
  
  private Intent mIntent;
  
  private final int mOrdering;
  
  private char mShortcutAlphabeticChar;
  
  private int mShortcutAlphabeticModifiers = 4096;
  
  private char mShortcutNumericChar;
  
  private int mShortcutNumericModifiers = 4096;
  
  private CharSequence mTitle;
  
  private CharSequence mTitleCondensed;
  
  private CharSequence mTooltipText;
  
  public ActionMenuItem(Context paramContext, int paramInt1, int paramInt2, int paramInt3, int paramInt4, CharSequence paramCharSequence) {
    this.mContext = paramContext;
    this.mId = paramInt2;
    this.mGroup = paramInt1;
    this.mCategoryOrder = paramInt3;
    this.mOrdering = paramInt4;
    this.mTitle = paramCharSequence;
  }
  
  private void applyIconTint() {
    if (this.mIconDrawable != null && (this.mHasIconTint || this.mHasIconTintMode)) {
      this.mIconDrawable = DrawableCompat.wrap(this.mIconDrawable);
      this.mIconDrawable = this.mIconDrawable.mutate();
      if (this.mHasIconTint)
        DrawableCompat.setTintList(this.mIconDrawable, this.mIconTintList); 
      if (this.mHasIconTintMode)
        DrawableCompat.setTintMode(this.mIconDrawable, this.mIconTintMode); 
    } 
  }
  
  public boolean collapseActionView() { return false; }
  
  public boolean expandActionView() { return false; }
  
  public ActionProvider getActionProvider() { throw new UnsupportedOperationException(); }
  
  public View getActionView() { return null; }
  
  public int getAlphabeticModifiers() { return this.mShortcutAlphabeticModifiers; }
  
  public char getAlphabeticShortcut() { return this.mShortcutAlphabeticChar; }
  
  public CharSequence getContentDescription() { return this.mContentDescription; }
  
  public int getGroupId() { return this.mGroup; }
  
  public Drawable getIcon() { return this.mIconDrawable; }
  
  public ColorStateList getIconTintList() { return this.mIconTintList; }
  
  public PorterDuff.Mode getIconTintMode() { return this.mIconTintMode; }
  
  public Intent getIntent() { return this.mIntent; }
  
  public int getItemId() { return this.mId; }
  
  public ContextMenu.ContextMenuInfo getMenuInfo() { return null; }
  
  public int getNumericModifiers() { return this.mShortcutNumericModifiers; }
  
  public char getNumericShortcut() { return this.mShortcutNumericChar; }
  
  public int getOrder() { return this.mOrdering; }
  
  public SubMenu getSubMenu() { return null; }
  
  public ActionProvider getSupportActionProvider() { return null; }
  
  public CharSequence getTitle() { return this.mTitle; }
  
  public CharSequence getTitleCondensed() {
    CharSequence charSequence = this.mTitleCondensed;
    return (charSequence != null) ? charSequence : this.mTitle;
  }
  
  public CharSequence getTooltipText() { return this.mTooltipText; }
  
  public boolean hasSubMenu() { return false; }
  
  public boolean invoke() {
    MenuItem.OnMenuItemClickListener onMenuItemClickListener = this.mClickListener;
    if (onMenuItemClickListener != null && onMenuItemClickListener.onMenuItemClick(this))
      return true; 
    Intent intent = this.mIntent;
    if (intent != null) {
      this.mContext.startActivity(intent);
      return true;
    } 
    return false;
  }
  
  public boolean isActionViewExpanded() { return false; }
  
  public boolean isCheckable() { return ((this.mFlags & true) != 0); }
  
  public boolean isChecked() { return ((this.mFlags & 0x2) != 0); }
  
  public boolean isEnabled() { return ((this.mFlags & 0x10) != 0); }
  
  public boolean isVisible() { return ((this.mFlags & 0x8) == 0); }
  
  public MenuItem setActionProvider(ActionProvider paramActionProvider) { throw new UnsupportedOperationException(); }
  
  public SupportMenuItem setActionView(int paramInt) { throw new UnsupportedOperationException(); }
  
  public SupportMenuItem setActionView(View paramView) { throw new UnsupportedOperationException(); }
  
  public MenuItem setAlphabeticShortcut(char paramChar) {
    this.mShortcutAlphabeticChar = Character.toLowerCase(paramChar);
    return this;
  }
  
  public MenuItem setAlphabeticShortcut(char paramChar, int paramInt) {
    this.mShortcutAlphabeticChar = Character.toLowerCase(paramChar);
    this.mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(paramInt);
    return this;
  }
  
  public MenuItem setCheckable(boolean paramBoolean) {
    this.mFlags = this.mFlags & 0xFFFFFFFE | paramBoolean;
    return this;
  }
  
  public MenuItem setChecked(boolean paramBoolean) {
    int i;
    int j = this.mFlags;
    if (paramBoolean) {
      i = 2;
    } else {
      i = 0;
    } 
    this.mFlags = j & 0xFFFFFFFD | i;
    return this;
  }
  
  public SupportMenuItem setContentDescription(CharSequence paramCharSequence) {
    this.mContentDescription = paramCharSequence;
    return this;
  }
  
  public MenuItem setEnabled(boolean paramBoolean) {
    int i;
    int j = this.mFlags;
    if (paramBoolean) {
      i = 16;
    } else {
      i = 0;
    } 
    this.mFlags = j & 0xFFFFFFEF | i;
    return this;
  }
  
  public ActionMenuItem setExclusiveCheckable(boolean paramBoolean) {
    int i;
    int j = this.mFlags;
    if (paramBoolean) {
      i = 4;
    } else {
      i = 0;
    } 
    this.mFlags = j & 0xFFFFFFFB | i;
    return this;
  }
  
  public MenuItem setIcon(int paramInt) {
    this.mIconResId = paramInt;
    this.mIconDrawable = ContextCompat.getDrawable(this.mContext, paramInt);
    applyIconTint();
    return this;
  }
  
  public MenuItem setIcon(Drawable paramDrawable) {
    this.mIconDrawable = paramDrawable;
    this.mIconResId = 0;
    applyIconTint();
    return this;
  }
  
  public MenuItem setIconTintList(@Nullable ColorStateList paramColorStateList) {
    this.mIconTintList = paramColorStateList;
    this.mHasIconTint = true;
    applyIconTint();
    return this;
  }
  
  public MenuItem setIconTintMode(PorterDuff.Mode paramMode) {
    this.mIconTintMode = paramMode;
    this.mHasIconTintMode = true;
    applyIconTint();
    return this;
  }
  
  public MenuItem setIntent(Intent paramIntent) {
    this.mIntent = paramIntent;
    return this;
  }
  
  public MenuItem setNumericShortcut(char paramChar) {
    this.mShortcutNumericChar = paramChar;
    return this;
  }
  
  public MenuItem setNumericShortcut(char paramChar, int paramInt) {
    this.mShortcutNumericChar = paramChar;
    this.mShortcutNumericModifiers = KeyEvent.normalizeMetaState(paramInt);
    return this;
  }
  
  public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener paramOnActionExpandListener) { throw new UnsupportedOperationException(); }
  
  public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener paramOnMenuItemClickListener) {
    this.mClickListener = paramOnMenuItemClickListener;
    return this;
  }
  
  public MenuItem setShortcut(char paramChar1, char paramChar2) {
    this.mShortcutNumericChar = paramChar1;
    this.mShortcutAlphabeticChar = Character.toLowerCase(paramChar2);
    return this;
  }
  
  public MenuItem setShortcut(char paramChar1, char paramChar2, int paramInt1, int paramInt2) {
    this.mShortcutNumericChar = paramChar1;
    this.mShortcutNumericModifiers = KeyEvent.normalizeMetaState(paramInt1);
    this.mShortcutAlphabeticChar = Character.toLowerCase(paramChar2);
    this.mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(paramInt2);
    return this;
  }
  
  public void setShowAsAction(int paramInt) {}
  
  public SupportMenuItem setShowAsActionFlags(int paramInt) {
    setShowAsAction(paramInt);
    return this;
  }
  
  public SupportMenuItem setSupportActionProvider(ActionProvider paramActionProvider) { throw new UnsupportedOperationException(); }
  
  public MenuItem setTitle(int paramInt) {
    this.mTitle = this.mContext.getResources().getString(paramInt);
    return this;
  }
  
  public MenuItem setTitle(CharSequence paramCharSequence) {
    this.mTitle = paramCharSequence;
    return this;
  }
  
  public MenuItem setTitleCondensed(CharSequence paramCharSequence) {
    this.mTitleCondensed = paramCharSequence;
    return this;
  }
  
  public SupportMenuItem setTooltipText(CharSequence paramCharSequence) {
    this.mTooltipText = paramCharSequence;
    return this;
  }
  
  public MenuItem setVisible(boolean paramBoolean) {
    int j = this.mFlags;
    int i = 8;
    if (paramBoolean)
      i = 0; 
    this.mFlags = j & 0x8 | i;
    return this;
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\view\menu\ActionMenuItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */