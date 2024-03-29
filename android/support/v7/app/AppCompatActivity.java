package android.support.v7.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class AppCompatActivity extends FragmentActivity implements AppCompatCallback, TaskStackBuilder.SupportParentable, ActionBarDrawerToggle.DelegateProvider {
  private AppCompatDelegate mDelegate;
  
  private Resources mResources;
  
  private int mThemeId = 0;
  
  private boolean performMenuItemShortcut(int paramInt, KeyEvent paramKeyEvent) {
    if (Build.VERSION.SDK_INT < 26 && !paramKeyEvent.isCtrlPressed() && !KeyEvent.metaStateHasNoModifiers(paramKeyEvent.getMetaState()) && paramKeyEvent.getRepeatCount() == 0 && !KeyEvent.isModifierKey(paramKeyEvent.getKeyCode())) {
      Window window = getWindow();
      if (window != null && window.getDecorView() != null && window.getDecorView().dispatchKeyShortcutEvent(paramKeyEvent))
        return true; 
    } 
    return false;
  }
  
  public void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) { getDelegate().addContentView(paramView, paramLayoutParams); }
  
  public void closeOptionsMenu() {
    ActionBar actionBar = getSupportActionBar();
    if (getWindow().hasFeature(0) && (actionBar == null || !actionBar.closeOptionsMenu()))
      super.closeOptionsMenu(); 
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    int i = paramKeyEvent.getKeyCode();
    ActionBar actionBar = getSupportActionBar();
    return (i == 82 && actionBar != null && actionBar.onMenuKeyEvent(paramKeyEvent)) ? true : super.dispatchKeyEvent(paramKeyEvent);
  }
  
  public <T extends View> T findViewById(@IdRes int paramInt) { return (T)getDelegate().findViewById(paramInt); }
  
  @NonNull
  public AppCompatDelegate getDelegate() {
    if (this.mDelegate == null)
      this.mDelegate = AppCompatDelegate.create(this, this); 
    return this.mDelegate;
  }
  
  @Nullable
  public ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() { return getDelegate().getDrawerToggleDelegate(); }
  
  public MenuInflater getMenuInflater() { return getDelegate().getMenuInflater(); }
  
  public Resources getResources() {
    if (this.mResources == null && VectorEnabledTintResources.shouldBeUsed())
      this.mResources = new VectorEnabledTintResources(this, super.getResources()); 
    Resources resources2 = this.mResources;
    Resources resources1 = resources2;
    if (resources2 == null)
      resources1 = super.getResources(); 
    return resources1;
  }
  
  @Nullable
  public ActionBar getSupportActionBar() { return getDelegate().getSupportActionBar(); }
  
  @Nullable
  public Intent getSupportParentActivityIntent() { return NavUtils.getParentActivityIntent(this); }
  
  public void invalidateOptionsMenu() { getDelegate().invalidateOptionsMenu(); }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    getDelegate().onConfigurationChanged(paramConfiguration);
    if (this.mResources != null) {
      DisplayMetrics displayMetrics = super.getResources().getDisplayMetrics();
      this.mResources.updateConfiguration(paramConfiguration, displayMetrics);
    } 
  }
  
  public void onContentChanged() { onSupportContentChanged(); }
  
  protected void onCreate(@Nullable Bundle paramBundle) {
    AppCompatDelegate appCompatDelegate = getDelegate();
    appCompatDelegate.installViewFactory();
    appCompatDelegate.onCreate(paramBundle);
    if (appCompatDelegate.applyDayNight() && this.mThemeId != 0)
      if (Build.VERSION.SDK_INT >= 23) {
        onApplyThemeResource(getTheme(), this.mThemeId, false);
      } else {
        setTheme(this.mThemeId);
      }  
    super.onCreate(paramBundle);
  }
  
  public void onCreateSupportNavigateUpTaskStack(@NonNull TaskStackBuilder paramTaskStackBuilder) { paramTaskStackBuilder.addParentStack(this); }
  
  protected void onDestroy() {
    super.onDestroy();
    getDelegate().onDestroy();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) { return performMenuItemShortcut(paramInt, paramKeyEvent) ? true : super.onKeyDown(paramInt, paramKeyEvent); }
  
  public final boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem) {
    if (super.onMenuItemSelected(paramInt, paramMenuItem))
      return true; 
    ActionBar actionBar = getSupportActionBar();
    return (paramMenuItem.getItemId() == 16908332 && actionBar != null && (actionBar.getDisplayOptions() & 0x4) != 0) ? onSupportNavigateUp() : 0;
  }
  
  public boolean onMenuOpened(int paramInt, Menu paramMenu) { return super.onMenuOpened(paramInt, paramMenu); }
  
  public void onPanelClosed(int paramInt, Menu paramMenu) { super.onPanelClosed(paramInt, paramMenu); }
  
  protected void onPostCreate(@Nullable Bundle paramBundle) {
    super.onPostCreate(paramBundle);
    getDelegate().onPostCreate(paramBundle);
  }
  
  protected void onPostResume() {
    super.onPostResume();
    getDelegate().onPostResume();
  }
  
  public void onPrepareSupportNavigateUpTaskStack(@NonNull TaskStackBuilder paramTaskStackBuilder) {}
  
  protected void onSaveInstanceState(Bundle paramBundle) {
    super.onSaveInstanceState(paramBundle);
    getDelegate().onSaveInstanceState(paramBundle);
  }
  
  protected void onStart() {
    super.onStart();
    getDelegate().onStart();
  }
  
  protected void onStop() {
    super.onStop();
    getDelegate().onStop();
  }
  
  @CallSuper
  public void onSupportActionModeFinished(@NonNull ActionMode paramActionMode) {}
  
  @CallSuper
  public void onSupportActionModeStarted(@NonNull ActionMode paramActionMode) {}
  
  @Deprecated
  public void onSupportContentChanged() {}
  
  public boolean onSupportNavigateUp() {
    Intent intent = getSupportParentActivityIntent();
    if (intent != null) {
      if (supportShouldUpRecreateTask(intent)) {
        taskStackBuilder = TaskStackBuilder.create(this);
        onCreateSupportNavigateUpTaskStack(taskStackBuilder);
        onPrepareSupportNavigateUpTaskStack(taskStackBuilder);
        taskStackBuilder.startActivities();
        try {
          ActivityCompat.finishAffinity(this);
        } catch (IllegalStateException taskStackBuilder) {
          finish();
        } 
      } else {
        supportNavigateUpTo(taskStackBuilder);
      } 
      return true;
    } 
    return false;
  }
  
  protected void onTitleChanged(CharSequence paramCharSequence, int paramInt) {
    super.onTitleChanged(paramCharSequence, paramInt);
    getDelegate().setTitle(paramCharSequence);
  }
  
  @Nullable
  public ActionMode onWindowStartingSupportActionMode(@NonNull ActionMode.Callback paramCallback) { return null; }
  
  public void openOptionsMenu() {
    ActionBar actionBar = getSupportActionBar();
    if (getWindow().hasFeature(0) && (actionBar == null || !actionBar.openOptionsMenu()))
      super.openOptionsMenu(); 
  }
  
  public void setContentView(@LayoutRes int paramInt) { getDelegate().setContentView(paramInt); }
  
  public void setContentView(View paramView) { getDelegate().setContentView(paramView); }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) { getDelegate().setContentView(paramView, paramLayoutParams); }
  
  public void setSupportActionBar(@Nullable Toolbar paramToolbar) { getDelegate().setSupportActionBar(paramToolbar); }
  
  @Deprecated
  public void setSupportProgress(int paramInt) {}
  
  @Deprecated
  public void setSupportProgressBarIndeterminate(boolean paramBoolean) {}
  
  @Deprecated
  public void setSupportProgressBarIndeterminateVisibility(boolean paramBoolean) {}
  
  @Deprecated
  public void setSupportProgressBarVisibility(boolean paramBoolean) {}
  
  public void setTheme(@StyleRes int paramInt) {
    super.setTheme(paramInt);
    this.mThemeId = paramInt;
  }
  
  @Nullable
  public ActionMode startSupportActionMode(@NonNull ActionMode.Callback paramCallback) { return getDelegate().startSupportActionMode(paramCallback); }
  
  public void supportInvalidateOptionsMenu() { getDelegate().invalidateOptionsMenu(); }
  
  public void supportNavigateUpTo(@NonNull Intent paramIntent) { NavUtils.navigateUpTo(this, paramIntent); }
  
  public boolean supportRequestWindowFeature(int paramInt) { return getDelegate().requestWindowFeature(paramInt); }
  
  public boolean supportShouldUpRecreateTask(@NonNull Intent paramIntent) { return NavUtils.shouldUpRecreateTask(this, paramIntent); }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\app\AppCompatActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */