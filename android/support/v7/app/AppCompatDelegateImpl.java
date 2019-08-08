package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.NavUtils;
import android.support.v4.view.KeyEventDispatcher;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.StandaloneActionMode;
import android.support.v7.view.SupportActionModeWrapper;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.WindowCallbackWrapper;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.FitWindowsViewGroup;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.VectorEnabledTintResources;
import android.support.v7.widget.ViewStubCompat;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

class AppCompatDelegateImpl extends AppCompatDelegate implements MenuBuilder.Callback, LayoutInflater.Factory2 {
  private static final boolean DEBUG = false;
  
  static final String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.";
  
  private static final boolean IS_PRE_LOLLIPOP;
  
  private static final String KEY_LOCAL_NIGHT_MODE = "appcompat:local_night_mode";
  
  private static boolean sInstalledExceptionHandler;
  
  private static final int[] sWindowBackgroundStyleable;
  
  ActionBar mActionBar;
  
  private ActionMenuPresenterCallback mActionMenuPresenterCallback;
  
  ActionMode mActionMode;
  
  PopupWindow mActionModePopup;
  
  ActionBarContextView mActionModeView;
  
  final AppCompatCallback mAppCompatCallback;
  
  private AppCompatViewInflater mAppCompatViewInflater;
  
  final Window.Callback mAppCompatWindowCallback;
  
  private boolean mApplyDayNightCalled;
  
  private AutoNightModeManager mAutoNightModeManager;
  
  private boolean mClosingActionMenu;
  
  final Context mContext;
  
  private DecorContentParent mDecorContentParent;
  
  private boolean mEnableDefaultActionBarUp;
  
  ViewPropertyAnimatorCompat mFadeAnim = null;
  
  private boolean mFeatureIndeterminateProgress;
  
  private boolean mFeatureProgress;
  
  private boolean mHandleNativeActionModes = true;
  
  boolean mHasActionBar;
  
  int mInvalidatePanelMenuFeatures;
  
  boolean mInvalidatePanelMenuPosted;
  
  private final Runnable mInvalidatePanelMenuRunnable = new Runnable() {
      public void run() {
        if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & true) != 0)
          AppCompatDelegateImpl.this.doInvalidatePanelMenu(0); 
        if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 0x1000) != 0)
          AppCompatDelegateImpl.this.doInvalidatePanelMenu(108); 
        AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
        appCompatDelegateImpl.mInvalidatePanelMenuPosted = false;
        appCompatDelegateImpl.mInvalidatePanelMenuFeatures = 0;
      }
    };
  
  boolean mIsDestroyed;
  
  boolean mIsFloating;
  
  private int mLocalNightMode = -100;
  
  private boolean mLongPressBackDown;
  
  MenuInflater mMenuInflater;
  
  final Window.Callback mOriginalWindowCallback;
  
  boolean mOverlayActionBar;
  
  boolean mOverlayActionMode;
  
  private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
  
  private PanelFeatureState[] mPanels;
  
  private PanelFeatureState mPreparedPanel;
  
  Runnable mShowActionModePopup;
  
  private View mStatusGuard;
  
  private ViewGroup mSubDecor;
  
  private boolean mSubDecorInstalled;
  
  private Rect mTempRect1;
  
  private Rect mTempRect2;
  
  private CharSequence mTitle;
  
  private TextView mTitleView;
  
  final Window mWindow;
  
  boolean mWindowNoTitle;
  
  static  {
    boolean bool;
    if (Build.VERSION.SDK_INT < 21) {
      bool = true;
    } else {
      bool = false;
    } 
    IS_PRE_LOLLIPOP = bool;
    sWindowBackgroundStyleable = new int[] { 16842836 };
    if (IS_PRE_LOLLIPOP && !sInstalledExceptionHandler) {
      Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()) {
            private boolean shouldWrapException(Throwable param1Throwable) {
              if (param1Throwable instanceof Resources.NotFoundException) {
                String str = param1Throwable.getMessage();
                return (str != null && (str.contains("drawable") || str.contains("Drawable")));
              } 
              return false;
            }
            
            public void uncaughtException(Thread param1Thread, Throwable param1Throwable) {
              if (shouldWrapException(param1Throwable)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(param1Throwable.getMessage());
                stringBuilder.append(". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.");
                Resources.NotFoundException notFoundException = new Resources.NotFoundException(stringBuilder.toString());
                notFoundException.initCause(param1Throwable.getCause());
                notFoundException.setStackTrace(param1Throwable.getStackTrace());
                defHandler.uncaughtException(param1Thread, notFoundException);
                return;
              } 
              defHandler.uncaughtException(param1Thread, param1Throwable);
            }
          });
      sInstalledExceptionHandler = true;
    } 
  }
  
  AppCompatDelegateImpl(Context paramContext, Window paramWindow, AppCompatCallback paramAppCompatCallback) {
    this.mContext = paramContext;
    this.mWindow = paramWindow;
    this.mAppCompatCallback = paramAppCompatCallback;
    this.mOriginalWindowCallback = this.mWindow.getCallback();
    Window.Callback callback = this.mOriginalWindowCallback;
    if (!(callback instanceof AppCompatWindowCallback)) {
      this.mAppCompatWindowCallback = new AppCompatWindowCallback(callback);
      this.mWindow.setCallback(this.mAppCompatWindowCallback);
      TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, null, sWindowBackgroundStyleable);
      Drawable drawable = tintTypedArray.getDrawableIfKnown(0);
      if (drawable != null)
        this.mWindow.setBackgroundDrawable(drawable); 
      tintTypedArray.recycle();
      return;
    } 
    throw new IllegalStateException("AppCompat has already installed itself into the Window");
  }
  
  private void applyFixedSizeWindow() {
    ContentFrameLayout contentFrameLayout = (ContentFrameLayout)this.mSubDecor.findViewById(16908290);
    View view = this.mWindow.getDecorView();
    contentFrameLayout.setDecorPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    TypedArray typedArray = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
    typedArray.getValue(R.styleable.AppCompatTheme_windowMinWidthMajor, contentFrameLayout.getMinWidthMajor());
    typedArray.getValue(R.styleable.AppCompatTheme_windowMinWidthMinor, contentFrameLayout.getMinWidthMinor());
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMajor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedWidthMajor, contentFrameLayout.getFixedWidthMajor()); 
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMinor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedWidthMinor, contentFrameLayout.getFixedWidthMinor()); 
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMajor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedHeightMajor, contentFrameLayout.getFixedHeightMajor()); 
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMinor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedHeightMinor, contentFrameLayout.getFixedHeightMinor()); 
    typedArray.recycle();
    contentFrameLayout.requestLayout();
  }
  
  private ViewGroup createSubDecor() {
    StringBuilder stringBuilder = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
    if (stringBuilder.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
      ViewGroup viewGroup;
      if (stringBuilder.getBoolean(R.styleable.AppCompatTheme_windowNoTitle, false)) {
        requestWindowFeature(1);
      } else if (stringBuilder.getBoolean(R.styleable.AppCompatTheme_windowActionBar, false)) {
        requestWindowFeature(108);
      } 
      if (stringBuilder.getBoolean(R.styleable.AppCompatTheme_windowActionBarOverlay, false))
        requestWindowFeature(109); 
      if (stringBuilder.getBoolean(R.styleable.AppCompatTheme_windowActionModeOverlay, false))
        requestWindowFeature(10); 
      this.mIsFloating = stringBuilder.getBoolean(R.styleable.AppCompatTheme_android_windowIsFloating, false);
      stringBuilder.recycle();
      this.mWindow.getDecorView();
      LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
      stringBuilder = null;
      if (!this.mWindowNoTitle) {
        if (this.mIsFloating) {
          viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.abc_dialog_title_material, null);
          this.mOverlayActionBar = false;
          this.mHasActionBar = false;
        } else if (this.mHasActionBar) {
          Context context = new TypedValue();
          this.mContext.getTheme().resolveAttribute(R.attr.actionBarTheme, context, true);
          if (context.resourceId != 0) {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this.mContext, context.resourceId);
          } else {
            context = this.mContext;
          } 
          viewGroup = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.abc_screen_toolbar, null);
          this.mDecorContentParent = (DecorContentParent)viewGroup.findViewById(R.id.decor_content_parent);
          this.mDecorContentParent.setWindowCallback(getWindowCallback());
          if (this.mOverlayActionBar)
            this.mDecorContentParent.initFeature(109); 
          if (this.mFeatureProgress)
            this.mDecorContentParent.initFeature(2); 
          if (this.mFeatureIndeterminateProgress)
            this.mDecorContentParent.initFeature(5); 
        } 
      } else {
        if (this.mOverlayActionMode) {
          viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.abc_screen_simple_overlay_action_mode, null);
        } else {
          viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.abc_screen_simple, null);
        } 
        if (Build.VERSION.SDK_INT >= 21) {
          ViewCompat.setOnApplyWindowInsetsListener(viewGroup, new OnApplyWindowInsetsListener() {
                public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
                  int i = param1WindowInsetsCompat.getSystemWindowInsetTop();
                  int j = AppCompatDelegateImpl.this.updateStatusGuard(i);
                  WindowInsetsCompat windowInsetsCompat = param1WindowInsetsCompat;
                  if (i != j)
                    windowInsetsCompat = param1WindowInsetsCompat.replaceSystemWindowInsets(param1WindowInsetsCompat.getSystemWindowInsetLeft(), j, param1WindowInsetsCompat.getSystemWindowInsetRight(), param1WindowInsetsCompat.getSystemWindowInsetBottom()); 
                  return ViewCompat.onApplyWindowInsets(param1View, windowInsetsCompat);
                }
              });
        } else {
          ((FitWindowsViewGroup)viewGroup).setOnFitSystemWindowsListener(new FitWindowsViewGroup.OnFitSystemWindowsListener() {
                public void onFitSystemWindows(Rect param1Rect) { param1Rect.top = AppCompatDelegateImpl.this.updateStatusGuard(param1Rect.top); }
              });
        } 
      } 
      if (viewGroup != null) {
        if (this.mDecorContentParent == null)
          this.mTitleView = (TextView)viewGroup.findViewById(R.id.title); 
        ViewUtils.makeOptionalFitsSystemWindows(viewGroup);
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout)viewGroup.findViewById(R.id.action_bar_activity_content);
        ViewGroup viewGroup1 = (ViewGroup)this.mWindow.findViewById(16908290);
        if (viewGroup1 != null) {
          while (viewGroup1.getChildCount() > 0) {
            View view = viewGroup1.getChildAt(0);
            viewGroup1.removeViewAt(0);
            contentFrameLayout.addView(view);
          } 
          viewGroup1.setId(-1);
          contentFrameLayout.setId(16908290);
          if (viewGroup1 instanceof FrameLayout)
            ((FrameLayout)viewGroup1).setForeground(null); 
        } 
        this.mWindow.setContentView(viewGroup);
        contentFrameLayout.setAttachListener(new ContentFrameLayout.OnAttachListener() {
              public void onAttachedFromWindow() {}
              
              public void onDetachedFromWindow() { AppCompatDelegateImpl.this.dismissPopups(); }
            });
        return viewGroup;
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("AppCompat does not support the current theme features: { windowActionBar: ");
      stringBuilder.append(this.mHasActionBar);
      stringBuilder.append(", windowActionBarOverlay: ");
      stringBuilder.append(this.mOverlayActionBar);
      stringBuilder.append(", android:windowIsFloating: ");
      stringBuilder.append(this.mIsFloating);
      stringBuilder.append(", windowActionModeOverlay: ");
      stringBuilder.append(this.mOverlayActionMode);
      stringBuilder.append(", windowNoTitle: ");
      stringBuilder.append(this.mWindowNoTitle);
      stringBuilder.append(" }");
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    stringBuilder.recycle();
    throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
  }
  
  private void ensureAutoNightModeManager() {
    if (this.mAutoNightModeManager == null)
      this.mAutoNightModeManager = new AutoNightModeManager(TwilightManager.getInstance(this.mContext)); 
  }
  
  private void ensureSubDecor() {
    if (!this.mSubDecorInstalled) {
      this.mSubDecor = createSubDecor();
      CharSequence charSequence = getTitle();
      if (!TextUtils.isEmpty(charSequence)) {
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
          decorContentParent.setWindowTitle(charSequence);
        } else if (peekSupportActionBar() != null) {
          peekSupportActionBar().setWindowTitle(charSequence);
        } else {
          TextView textView = this.mTitleView;
          if (textView != null)
            textView.setText(charSequence); 
        } 
      } 
      applyFixedSizeWindow();
      onSubDecorInstalled(this.mSubDecor);
      this.mSubDecorInstalled = true;
      PanelFeatureState panelFeatureState = getPanelState(0, false);
      if (!this.mIsDestroyed && (panelFeatureState == null || panelFeatureState.menu == null))
        invalidatePanelMenu(108); 
    } 
  }
  
  private int getNightMode() {
    int i = this.mLocalNightMode;
    return (i != -100) ? i : getDefaultNightMode();
  }
  
  private void initWindowDecorActionBar() {
    ensureSubDecor();
    if (this.mHasActionBar) {
      if (this.mActionBar != null)
        return; 
      Window.Callback callback = this.mOriginalWindowCallback;
      if (callback instanceof Activity) {
        this.mActionBar = new WindowDecorActionBar((Activity)callback, this.mOverlayActionBar);
      } else if (callback instanceof Dialog) {
        this.mActionBar = new WindowDecorActionBar((Dialog)callback);
      } 
      ActionBar actionBar = this.mActionBar;
      if (actionBar != null)
        actionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp); 
      return;
    } 
  }
  
  private boolean initializePanelContent(PanelFeatureState paramPanelFeatureState) {
    if (paramPanelFeatureState.createdPanelView != null) {
      paramPanelFeatureState.shownPanelView = paramPanelFeatureState.createdPanelView;
      return true;
    } 
    if (paramPanelFeatureState.menu == null)
      return false; 
    if (this.mPanelMenuPresenterCallback == null)
      this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback(); 
    paramPanelFeatureState.shownPanelView = (View)paramPanelFeatureState.getListMenuView(this.mPanelMenuPresenterCallback);
    return (paramPanelFeatureState.shownPanelView != null);
  }
  
  private boolean initializePanelDecor(PanelFeatureState paramPanelFeatureState) {
    paramPanelFeatureState.setStyle(getActionBarThemedContext());
    paramPanelFeatureState.decorView = new ListMenuDecorView(paramPanelFeatureState.listPresenterContext);
    paramPanelFeatureState.gravity = 81;
    return true;
  }
  
  private boolean initializePanelMenu(PanelFeatureState paramPanelFeatureState) { // Byte code:
    //   0: aload_0
    //   1: getfield mContext : Landroid/content/Context;
    //   4: astore #4
    //   6: aload_1
    //   7: getfield featureId : I
    //   10: ifeq -> 25
    //   13: aload #4
    //   15: astore_2
    //   16: aload_1
    //   17: getfield featureId : I
    //   20: bipush #108
    //   22: if_icmpne -> 191
    //   25: aload #4
    //   27: astore_2
    //   28: aload_0
    //   29: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   32: ifnull -> 191
    //   35: new android/util/TypedValue
    //   38: dup
    //   39: invokespecial <init> : ()V
    //   42: astore #5
    //   44: aload #4
    //   46: invokevirtual getTheme : ()Landroid/content/res/Resources$Theme;
    //   49: astore #6
    //   51: aload #6
    //   53: getstatic android/support/v7/appcompat/R$attr.actionBarTheme : I
    //   56: aload #5
    //   58: iconst_1
    //   59: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   62: pop
    //   63: aconst_null
    //   64: astore_2
    //   65: aload #5
    //   67: getfield resourceId : I
    //   70: ifeq -> 112
    //   73: aload #4
    //   75: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   78: invokevirtual newTheme : ()Landroid/content/res/Resources$Theme;
    //   81: astore_2
    //   82: aload_2
    //   83: aload #6
    //   85: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   88: aload_2
    //   89: aload #5
    //   91: getfield resourceId : I
    //   94: iconst_1
    //   95: invokevirtual applyStyle : (IZ)V
    //   98: aload_2
    //   99: getstatic android/support/v7/appcompat/R$attr.actionBarWidgetTheme : I
    //   102: aload #5
    //   104: iconst_1
    //   105: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   108: pop
    //   109: goto -> 124
    //   112: aload #6
    //   114: getstatic android/support/v7/appcompat/R$attr.actionBarWidgetTheme : I
    //   117: aload #5
    //   119: iconst_1
    //   120: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   123: pop
    //   124: aload_2
    //   125: astore_3
    //   126: aload #5
    //   128: getfield resourceId : I
    //   131: ifeq -> 165
    //   134: aload_2
    //   135: astore_3
    //   136: aload_2
    //   137: ifnonnull -> 155
    //   140: aload #4
    //   142: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   145: invokevirtual newTheme : ()Landroid/content/res/Resources$Theme;
    //   148: astore_3
    //   149: aload_3
    //   150: aload #6
    //   152: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   155: aload_3
    //   156: aload #5
    //   158: getfield resourceId : I
    //   161: iconst_1
    //   162: invokevirtual applyStyle : (IZ)V
    //   165: aload #4
    //   167: astore_2
    //   168: aload_3
    //   169: ifnull -> 191
    //   172: new android/support/v7/view/ContextThemeWrapper
    //   175: dup
    //   176: aload #4
    //   178: iconst_0
    //   179: invokespecial <init> : (Landroid/content/Context;I)V
    //   182: astore_2
    //   183: aload_2
    //   184: invokevirtual getTheme : ()Landroid/content/res/Resources$Theme;
    //   187: aload_3
    //   188: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   191: new android/support/v7/view/menu/MenuBuilder
    //   194: dup
    //   195: aload_2
    //   196: invokespecial <init> : (Landroid/content/Context;)V
    //   199: astore_2
    //   200: aload_2
    //   201: aload_0
    //   202: invokevirtual setCallback : (Landroid/support/v7/view/menu/MenuBuilder$Callback;)V
    //   205: aload_1
    //   206: aload_2
    //   207: invokevirtual setMenu : (Landroid/support/v7/view/menu/MenuBuilder;)V
    //   210: iconst_1
    //   211: ireturn }
  
  private void invalidatePanelMenu(int paramInt) {
    this.mInvalidatePanelMenuFeatures |= 1 << paramInt;
    if (!this.mInvalidatePanelMenuPosted) {
      ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
      this.mInvalidatePanelMenuPosted = true;
    } 
  }
  
  private boolean onKeyDownPanel(int paramInt, KeyEvent paramKeyEvent) {
    if (paramKeyEvent.getRepeatCount() == 0) {
      PanelFeatureState panelFeatureState = getPanelState(paramInt, true);
      if (!panelFeatureState.isOpen)
        return preparePanel(panelFeatureState, paramKeyEvent); 
    } 
    return false;
  }
  
  private boolean onKeyUpPanel(int paramInt, KeyEvent paramKeyEvent) { // Byte code:
    //   0: aload_0
    //   1: getfield mActionMode : Landroid/support/v7/view/ActionMode;
    //   4: ifnull -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: iconst_0
    //   10: istore #5
    //   12: aload_0
    //   13: iload_1
    //   14: iconst_1
    //   15: invokevirtual getPanelState : (IZ)Landroid/support/v7/app/AppCompatDelegateImpl$PanelFeatureState;
    //   18: astore #6
    //   20: iload_1
    //   21: ifne -> 119
    //   24: aload_0
    //   25: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   28: astore #7
    //   30: aload #7
    //   32: ifnull -> 119
    //   35: aload #7
    //   37: invokeinterface canShowOverflowMenu : ()Z
    //   42: ifeq -> 119
    //   45: aload_0
    //   46: getfield mContext : Landroid/content/Context;
    //   49: invokestatic get : (Landroid/content/Context;)Landroid/view/ViewConfiguration;
    //   52: invokevirtual hasPermanentMenuKey : ()Z
    //   55: ifne -> 119
    //   58: aload_0
    //   59: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   62: invokeinterface isOverflowMenuShowing : ()Z
    //   67: ifne -> 106
    //   70: iload #5
    //   72: istore_3
    //   73: aload_0
    //   74: getfield mIsDestroyed : Z
    //   77: ifne -> 208
    //   80: iload #5
    //   82: istore_3
    //   83: aload_0
    //   84: aload #6
    //   86: aload_2
    //   87: invokespecial preparePanel : (Landroid/support/v7/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   90: ifeq -> 208
    //   93: aload_0
    //   94: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   97: invokeinterface showOverflowMenu : ()Z
    //   102: istore_3
    //   103: goto -> 208
    //   106: aload_0
    //   107: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   110: invokeinterface hideOverflowMenu : ()Z
    //   115: istore_3
    //   116: goto -> 208
    //   119: aload #6
    //   121: getfield isOpen : Z
    //   124: ifne -> 195
    //   127: aload #6
    //   129: getfield isHandled : Z
    //   132: ifeq -> 138
    //   135: goto -> 195
    //   138: iload #5
    //   140: istore_3
    //   141: aload #6
    //   143: getfield isPrepared : Z
    //   146: ifeq -> 208
    //   149: iconst_1
    //   150: istore #4
    //   152: aload #6
    //   154: getfield refreshMenuContent : Z
    //   157: ifeq -> 175
    //   160: aload #6
    //   162: iconst_0
    //   163: putfield isPrepared : Z
    //   166: aload_0
    //   167: aload #6
    //   169: aload_2
    //   170: invokespecial preparePanel : (Landroid/support/v7/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   173: istore #4
    //   175: iload #5
    //   177: istore_3
    //   178: iload #4
    //   180: ifeq -> 208
    //   183: aload_0
    //   184: aload #6
    //   186: aload_2
    //   187: invokespecial openPanel : (Landroid/support/v7/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)V
    //   190: iconst_1
    //   191: istore_3
    //   192: goto -> 208
    //   195: aload #6
    //   197: getfield isOpen : Z
    //   200: istore_3
    //   201: aload_0
    //   202: aload #6
    //   204: iconst_1
    //   205: invokevirtual closePanel : (Landroid/support/v7/app/AppCompatDelegateImpl$PanelFeatureState;Z)V
    //   208: iload_3
    //   209: ifeq -> 247
    //   212: aload_0
    //   213: getfield mContext : Landroid/content/Context;
    //   216: ldc_w 'audio'
    //   219: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   222: checkcast android/media/AudioManager
    //   225: astore_2
    //   226: aload_2
    //   227: ifnull -> 237
    //   230: aload_2
    //   231: iconst_0
    //   232: invokevirtual playSoundEffect : (I)V
    //   235: iload_3
    //   236: ireturn
    //   237: ldc_w 'AppCompatDelegate'
    //   240: ldc_w 'Couldn't get audio manager'
    //   243: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   246: pop
    //   247: iload_3
    //   248: ireturn }
  
  private void openPanel(PanelFeatureState paramPanelFeatureState, KeyEvent paramKeyEvent) { // Byte code:
    //   0: aload_1
    //   1: getfield isOpen : Z
    //   4: ifne -> 425
    //   7: aload_0
    //   8: getfield mIsDestroyed : Z
    //   11: ifeq -> 15
    //   14: return
    //   15: aload_1
    //   16: getfield featureId : I
    //   19: ifne -> 54
    //   22: aload_0
    //   23: getfield mContext : Landroid/content/Context;
    //   26: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   29: invokevirtual getConfiguration : ()Landroid/content/res/Configuration;
    //   32: getfield screenLayout : I
    //   35: bipush #15
    //   37: iand
    //   38: iconst_4
    //   39: if_icmpne -> 47
    //   42: iconst_1
    //   43: istore_3
    //   44: goto -> 49
    //   47: iconst_0
    //   48: istore_3
    //   49: iload_3
    //   50: ifeq -> 54
    //   53: return
    //   54: aload_0
    //   55: invokevirtual getWindowCallback : ()Landroid/view/Window$Callback;
    //   58: astore #5
    //   60: aload #5
    //   62: ifnull -> 90
    //   65: aload #5
    //   67: aload_1
    //   68: getfield featureId : I
    //   71: aload_1
    //   72: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   75: invokeinterface onMenuOpened : (ILandroid/view/Menu;)Z
    //   80: ifne -> 90
    //   83: aload_0
    //   84: aload_1
    //   85: iconst_1
    //   86: invokevirtual closePanel : (Landroid/support/v7/app/AppCompatDelegateImpl$PanelFeatureState;Z)V
    //   89: return
    //   90: aload_0
    //   91: getfield mContext : Landroid/content/Context;
    //   94: ldc_w 'window'
    //   97: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   100: checkcast android/view/WindowManager
    //   103: astore #6
    //   105: aload #6
    //   107: ifnonnull -> 111
    //   110: return
    //   111: aload_0
    //   112: aload_1
    //   113: aload_2
    //   114: invokespecial preparePanel : (Landroid/support/v7/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   117: ifne -> 121
    //   120: return
    //   121: bipush #-2
    //   123: istore #4
    //   125: aload_1
    //   126: getfield decorView : Landroid/view/ViewGroup;
    //   129: ifnull -> 186
    //   132: aload_1
    //   133: getfield refreshDecorView : Z
    //   136: ifeq -> 142
    //   139: goto -> 186
    //   142: aload_1
    //   143: getfield createdPanelView : Landroid/view/View;
    //   146: ifnull -> 180
    //   149: aload_1
    //   150: getfield createdPanelView : Landroid/view/View;
    //   153: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   156: astore_2
    //   157: iload #4
    //   159: istore_3
    //   160: aload_2
    //   161: ifnull -> 358
    //   164: iload #4
    //   166: istore_3
    //   167: aload_2
    //   168: getfield width : I
    //   171: iconst_m1
    //   172: if_icmpne -> 358
    //   175: iconst_m1
    //   176: istore_3
    //   177: goto -> 358
    //   180: iload #4
    //   182: istore_3
    //   183: goto -> 358
    //   186: aload_1
    //   187: getfield decorView : Landroid/view/ViewGroup;
    //   190: ifnonnull -> 209
    //   193: aload_0
    //   194: aload_1
    //   195: invokespecial initializePanelDecor : (Landroid/support/v7/app/AppCompatDelegateImpl$PanelFeatureState;)Z
    //   198: ifeq -> 208
    //   201: aload_1
    //   202: getfield decorView : Landroid/view/ViewGroup;
    //   205: ifnonnull -> 233
    //   208: return
    //   209: aload_1
    //   210: getfield refreshDecorView : Z
    //   213: ifeq -> 233
    //   216: aload_1
    //   217: getfield decorView : Landroid/view/ViewGroup;
    //   220: invokevirtual getChildCount : ()I
    //   223: ifle -> 233
    //   226: aload_1
    //   227: getfield decorView : Landroid/view/ViewGroup;
    //   230: invokevirtual removeAllViews : ()V
    //   233: aload_0
    //   234: aload_1
    //   235: invokespecial initializePanelContent : (Landroid/support/v7/app/AppCompatDelegateImpl$PanelFeatureState;)Z
    //   238: ifeq -> 424
    //   241: aload_1
    //   242: invokevirtual hasPanelItems : ()Z
    //   245: ifne -> 249
    //   248: return
    //   249: aload_1
    //   250: getfield shownPanelView : Landroid/view/View;
    //   253: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   256: astore #5
    //   258: aload #5
    //   260: astore_2
    //   261: aload #5
    //   263: ifnonnull -> 278
    //   266: new android/view/ViewGroup$LayoutParams
    //   269: dup
    //   270: bipush #-2
    //   272: bipush #-2
    //   274: invokespecial <init> : (II)V
    //   277: astore_2
    //   278: aload_1
    //   279: getfield background : I
    //   282: istore_3
    //   283: aload_1
    //   284: getfield decorView : Landroid/view/ViewGroup;
    //   287: iload_3
    //   288: invokevirtual setBackgroundResource : (I)V
    //   291: aload_1
    //   292: getfield shownPanelView : Landroid/view/View;
    //   295: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   298: astore #5
    //   300: aload #5
    //   302: ifnull -> 325
    //   305: aload #5
    //   307: instanceof android/view/ViewGroup
    //   310: ifeq -> 325
    //   313: aload #5
    //   315: checkcast android/view/ViewGroup
    //   318: aload_1
    //   319: getfield shownPanelView : Landroid/view/View;
    //   322: invokevirtual removeView : (Landroid/view/View;)V
    //   325: aload_1
    //   326: getfield decorView : Landroid/view/ViewGroup;
    //   329: aload_1
    //   330: getfield shownPanelView : Landroid/view/View;
    //   333: aload_2
    //   334: invokevirtual addView : (Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    //   337: aload_1
    //   338: getfield shownPanelView : Landroid/view/View;
    //   341: invokevirtual hasFocus : ()Z
    //   344: ifne -> 180
    //   347: aload_1
    //   348: getfield shownPanelView : Landroid/view/View;
    //   351: invokevirtual requestFocus : ()Z
    //   354: pop
    //   355: goto -> 180
    //   358: aload_1
    //   359: iconst_0
    //   360: putfield isHandled : Z
    //   363: new android/view/WindowManager$LayoutParams
    //   366: dup
    //   367: iload_3
    //   368: bipush #-2
    //   370: aload_1
    //   371: getfield x : I
    //   374: aload_1
    //   375: getfield y : I
    //   378: sipush #1002
    //   381: ldc_w 8519680
    //   384: bipush #-3
    //   386: invokespecial <init> : (IIIIIII)V
    //   389: astore_2
    //   390: aload_2
    //   391: aload_1
    //   392: getfield gravity : I
    //   395: putfield gravity : I
    //   398: aload_2
    //   399: aload_1
    //   400: getfield windowAnimations : I
    //   403: putfield windowAnimations : I
    //   406: aload #6
    //   408: aload_1
    //   409: getfield decorView : Landroid/view/ViewGroup;
    //   412: aload_2
    //   413: invokeinterface addView : (Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    //   418: aload_1
    //   419: iconst_1
    //   420: putfield isOpen : Z
    //   423: return
    //   424: return
    //   425: return }
  
  private boolean performPanelShortcut(PanelFeatureState paramPanelFeatureState, int paramInt1, KeyEvent paramKeyEvent, int paramInt2) { // Byte code:
    //   0: aload_3
    //   1: invokevirtual isSystem : ()Z
    //   4: ifeq -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: iconst_0
    //   10: istore #6
    //   12: aload_1
    //   13: getfield isPrepared : Z
    //   16: ifne -> 32
    //   19: iload #6
    //   21: istore #5
    //   23: aload_0
    //   24: aload_1
    //   25: aload_3
    //   26: invokespecial preparePanel : (Landroid/support/v7/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   29: ifeq -> 56
    //   32: iload #6
    //   34: istore #5
    //   36: aload_1
    //   37: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   40: ifnull -> 56
    //   43: aload_1
    //   44: getfield menu : Landroid/support/v7/view/menu/MenuBuilder;
    //   47: iload_2
    //   48: aload_3
    //   49: iload #4
    //   51: invokevirtual performShortcut : (ILandroid/view/KeyEvent;I)Z
    //   54: istore #5
    //   56: iload #5
    //   58: ifeq -> 81
    //   61: iload #4
    //   63: iconst_1
    //   64: iand
    //   65: ifne -> 81
    //   68: aload_0
    //   69: getfield mDecorContentParent : Landroid/support/v7/widget/DecorContentParent;
    //   72: ifnonnull -> 81
    //   75: aload_0
    //   76: aload_1
    //   77: iconst_1
    //   78: invokevirtual closePanel : (Landroid/support/v7/app/AppCompatDelegateImpl$PanelFeatureState;Z)V
    //   81: iload #5
    //   83: ireturn }
  
  private boolean preparePanel(PanelFeatureState paramPanelFeatureState, KeyEvent paramKeyEvent) {
    int i;
    DecorContentParent decorContentParent;
    if (this.mIsDestroyed)
      return false; 
    if (paramPanelFeatureState.isPrepared)
      return true; 
    PanelFeatureState panelFeatureState = this.mPreparedPanel;
    if (panelFeatureState != null && panelFeatureState != paramPanelFeatureState)
      closePanel(panelFeatureState, false); 
    Window.Callback callback = getWindowCallback();
    if (callback != null)
      paramPanelFeatureState.createdPanelView = callback.onCreatePanelView(paramPanelFeatureState.featureId); 
    if (paramPanelFeatureState.featureId == 0 || paramPanelFeatureState.featureId == 108) {
      i = 1;
    } else {
      i = 0;
    } 
    if (i) {
      DecorContentParent decorContentParent1 = this.mDecorContentParent;
      if (decorContentParent1 != null)
        decorContentParent1.setMenuPrepared(); 
    } 
    if (paramPanelFeatureState.createdPanelView == null && (!i || !(peekSupportActionBar() instanceof ToolbarActionBar))) {
      boolean bool;
      DecorContentParent decorContentParent1;
      if (paramPanelFeatureState.menu == null || paramPanelFeatureState.refreshMenuContent) {
        if (paramPanelFeatureState.menu == null && (!initializePanelMenu(paramPanelFeatureState) || paramPanelFeatureState.menu == null))
          return false; 
        if (i && this.mDecorContentParent != null) {
          if (this.mActionMenuPresenterCallback == null)
            this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback(); 
          this.mDecorContentParent.setMenu(paramPanelFeatureState.menu, this.mActionMenuPresenterCallback);
        } 
        paramPanelFeatureState.menu.stopDispatchingItemsChanged();
        if (!callback.onCreatePanelMenu(paramPanelFeatureState.featureId, paramPanelFeatureState.menu)) {
          paramPanelFeatureState.setMenu(null);
          if (i) {
            decorContentParent = this.mDecorContentParent;
            if (decorContentParent != null)
              decorContentParent.setMenu(null, this.mActionMenuPresenterCallback); 
          } 
          return false;
        } 
        decorContentParent.refreshMenuContent = false;
      } 
      decorContentParent.menu.stopDispatchingItemsChanged();
      if (decorContentParent.frozenActionViewState != null) {
        decorContentParent.menu.restoreActionViewStates(decorContentParent.frozenActionViewState);
        decorContentParent.frozenActionViewState = null;
      } 
      if (!callback.onPreparePanel(0, decorContentParent.createdPanelView, decorContentParent.menu)) {
        if (i) {
          decorContentParent1 = this.mDecorContentParent;
          if (decorContentParent1 != null)
            decorContentParent1.setMenu(null, this.mActionMenuPresenterCallback); 
        } 
        decorContentParent.menu.startDispatchingItemsChanged();
        return false;
      } 
      if (decorContentParent1 != null) {
        i = decorContentParent1.getDeviceId();
      } else {
        i = -1;
      } 
      if (KeyCharacterMap.load(i).getKeyboardType() != 1) {
        bool = true;
      } else {
        bool = false;
      } 
      decorContentParent.qwertyMode = bool;
      decorContentParent.menu.setQwertyMode(decorContentParent.qwertyMode);
      decorContentParent.menu.startDispatchingItemsChanged();
    } 
    decorContentParent.isPrepared = true;
    decorContentParent.isHandled = false;
    this.mPreparedPanel = decorContentParent;
    return true;
  }
  
  private void reopenMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    DecorContentParent decorContentParent = this.mDecorContentParent;
    if (decorContentParent != null && decorContentParent.canShowOverflowMenu() && (!ViewConfiguration.get(this.mContext).hasPermanentMenuKey() || this.mDecorContentParent.isOverflowMenuShowPending())) {
      Window.Callback callback = getWindowCallback();
      if (!this.mDecorContentParent.isOverflowMenuShowing() || !paramBoolean) {
        if (callback != null && !this.mIsDestroyed) {
          if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & true) != 0) {
            this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuRunnable.run();
          } 
          PanelFeatureState panelFeatureState1 = getPanelState(0, true);
          if (panelFeatureState1.menu != null && !panelFeatureState1.refreshMenuContent && callback.onPreparePanel(0, panelFeatureState1.createdPanelView, panelFeatureState1.menu)) {
            callback.onMenuOpened(108, panelFeatureState1.menu);
            this.mDecorContentParent.showOverflowMenu();
          } 
        } 
        return;
      } 
      this.mDecorContentParent.hideOverflowMenu();
      if (!this.mIsDestroyed) {
        callback.onPanelClosed(108, (getPanelState(0, true)).menu);
        return;
      } 
      return;
    } 
    PanelFeatureState panelFeatureState = getPanelState(0, true);
    panelFeatureState.refreshDecorView = true;
    closePanel(panelFeatureState, false);
    openPanel(panelFeatureState, null);
  }
  
  private int sanitizeWindowFeatureId(int paramInt) {
    if (paramInt == 8) {
      Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
      return 108;
    } 
    if (paramInt == 9) {
      Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
      return 109;
    } 
    return paramInt;
  }
  
  private boolean shouldInheritContext(ViewParent paramViewParent) {
    if (paramViewParent == null)
      return false; 
    View view = this.mWindow.getDecorView();
    while (true) {
      if (paramViewParent == null)
        return true; 
      if (paramViewParent != view && paramViewParent instanceof View) {
        if (ViewCompat.isAttachedToWindow((View)paramViewParent))
          return false; 
        paramViewParent = paramViewParent.getParent();
        continue;
      } 
      break;
    } 
    return false;
  }
  
  private boolean shouldRecreateOnNightModeChange() {
    boolean bool2 = this.mApplyDayNightCalled;
    boolean bool1 = false;
    if (bool2) {
      Context context = this.mContext;
      if (context instanceof Activity) {
        packageManager = context.getPackageManager();
        try {
          int i = (packageManager.getActivityInfo(new ComponentName(this.mContext, this.mContext.getClass()), 0)).configChanges;
          if ((i & 0x200) == 0)
            bool1 = true; 
          return bool1;
        } catch (android.content.pm.PackageManager.NameNotFoundException packageManager) {
          Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", packageManager);
          return true;
        } 
      } 
    } 
    return false;
  }
  
  private void throwFeatureRequestIfSubDecorInstalled() {
    if (!this.mSubDecorInstalled)
      return; 
    throw new AndroidRuntimeException("Window feature must be requested before adding content");
  }
  
  private boolean updateForNightMode(int paramInt) {
    Resources resources = this.mContext.getResources();
    Configuration configuration = resources.getConfiguration();
    int i = configuration.uiMode;
    if (paramInt == 2) {
      paramInt = 32;
    } else {
      paramInt = 16;
    } 
    if ((i & 0x30) != paramInt) {
      if (shouldRecreateOnNightModeChange()) {
        ((Activity)this.mContext).recreate();
      } else {
        configuration = new Configuration(configuration);
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.uiMode = configuration.uiMode & 0xFFFFFFCF | paramInt;
        resources.updateConfiguration(configuration, displayMetrics);
        if (Build.VERSION.SDK_INT < 26)
          ResourcesFlusher.flush(resources); 
      } 
      return true;
    } 
    return false;
  }
  
  public void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    ensureSubDecor();
    ((ViewGroup)this.mSubDecor.findViewById(16908290)).addView(paramView, paramLayoutParams);
    this.mOriginalWindowCallback.onContentChanged();
  }
  
  public boolean applyDayNight() {
    boolean bool = false;
    int i = getNightMode();
    int j = mapNightMode(i);
    if (j != -1)
      bool = updateForNightMode(j); 
    if (i == 0) {
      ensureAutoNightModeManager();
      this.mAutoNightModeManager.setup();
    } 
    this.mApplyDayNightCalled = true;
    return bool;
  }
  
  void callOnPanelClosed(int paramInt, PanelFeatureState paramPanelFeatureState, Menu paramMenu) {
    PanelFeatureState panelFeatureState = paramPanelFeatureState;
    MenuBuilder menuBuilder = paramMenu;
    if (paramMenu == null) {
      PanelFeatureState panelFeatureState1 = paramPanelFeatureState;
      if (paramPanelFeatureState == null) {
        panelFeatureState1 = paramPanelFeatureState;
        if (paramInt >= 0) {
          PanelFeatureState[] arrayOfPanelFeatureState = this.mPanels;
          panelFeatureState1 = paramPanelFeatureState;
          if (paramInt < arrayOfPanelFeatureState.length)
            panelFeatureState1 = arrayOfPanelFeatureState[paramInt]; 
        } 
      } 
      panelFeatureState = panelFeatureState1;
      menuBuilder = paramMenu;
      if (panelFeatureState1 != null) {
        menuBuilder = panelFeatureState1.menu;
        panelFeatureState = panelFeatureState1;
      } 
    } 
    if (panelFeatureState != null && !panelFeatureState.isOpen)
      return; 
    if (!this.mIsDestroyed)
      this.mOriginalWindowCallback.onPanelClosed(paramInt, menuBuilder); 
  }
  
  void checkCloseActionMenu(MenuBuilder paramMenuBuilder) {
    if (this.mClosingActionMenu)
      return; 
    this.mClosingActionMenu = true;
    this.mDecorContentParent.dismissPopups();
    Window.Callback callback = getWindowCallback();
    if (callback != null && !this.mIsDestroyed)
      callback.onPanelClosed(108, paramMenuBuilder); 
    this.mClosingActionMenu = false;
  }
  
  void closePanel(int paramInt) { closePanel(getPanelState(paramInt, true), true); }
  
  void closePanel(PanelFeatureState paramPanelFeatureState, boolean paramBoolean) {
    if (paramBoolean && paramPanelFeatureState.featureId == 0) {
      DecorContentParent decorContentParent = this.mDecorContentParent;
      if (decorContentParent != null && decorContentParent.isOverflowMenuShowing()) {
        checkCloseActionMenu(paramPanelFeatureState.menu);
        return;
      } 
    } 
    WindowManager windowManager = (WindowManager)this.mContext.getSystemService("window");
    if (windowManager != null && paramPanelFeatureState.isOpen && paramPanelFeatureState.decorView != null) {
      windowManager.removeView(paramPanelFeatureState.decorView);
      if (paramBoolean)
        callOnPanelClosed(paramPanelFeatureState.featureId, paramPanelFeatureState, null); 
    } 
    paramPanelFeatureState.isPrepared = false;
    paramPanelFeatureState.isHandled = false;
    paramPanelFeatureState.isOpen = false;
    paramPanelFeatureState.shownPanelView = null;
    paramPanelFeatureState.refreshDecorView = true;
    if (this.mPreparedPanel == paramPanelFeatureState)
      this.mPreparedPanel = null; 
  }
  
  public View createView(View paramView, String paramString, @NonNull Context paramContext, @NonNull AttributeSet paramAttributeSet) {
    AppCompatViewInflater appCompatViewInflater = this.mAppCompatViewInflater;
    byte b = 0;
    if (appCompatViewInflater == null) {
      String str = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme).getString(R.styleable.AppCompatTheme_viewInflaterClass);
      if (str == null || AppCompatViewInflater.class.getName().equals(str)) {
        this.mAppCompatViewInflater = new AppCompatViewInflater();
      } else {
        try {
          this.mAppCompatViewInflater = (AppCompatViewInflater)Class.forName(str).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Throwable throwable) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Failed to instantiate custom view inflater ");
          stringBuilder.append(str);
          stringBuilder.append(". Falling back to default.");
          Log.i("AppCompatDelegate", stringBuilder.toString(), throwable);
          this.mAppCompatViewInflater = new AppCompatViewInflater();
        } 
      } 
    } 
    boolean bool = false;
    if (IS_PRE_LOLLIPOP)
      if (paramAttributeSet instanceof XmlPullParser) {
        bool = b;
        if (((XmlPullParser)paramAttributeSet).getDepth() > 1)
          bool = true; 
      } else {
        bool = shouldInheritContext((ViewParent)paramView);
      }  
    return this.mAppCompatViewInflater.createView(paramView, paramString, paramContext, paramAttributeSet, bool, IS_PRE_LOLLIPOP, true, VectorEnabledTintResources.shouldBeUsed());
  }
  
  void dismissPopups() {
    decorContentParent = this.mDecorContentParent;
    if (decorContentParent != null)
      decorContentParent.dismissPopups(); 
    if (this.mActionModePopup != null) {
      this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
      if (this.mActionModePopup.isShowing())
        try {
          this.mActionModePopup.dismiss();
        } catch (IllegalArgumentException decorContentParent) {} 
      this.mActionModePopup = null;
    } 
    endOnGoingFadeAnimation();
    PanelFeatureState panelFeatureState = getPanelState(0, false);
    if (panelFeatureState != null && panelFeatureState.menu != null)
      panelFeatureState.menu.close(); 
  }
  
  boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    Window.Callback callback = this.mOriginalWindowCallback;
    boolean bool1 = callback instanceof KeyEventDispatcher.Component;
    boolean bool = true;
    if (bool1 || callback instanceof AppCompatDialog) {
      View view = this.mWindow.getDecorView();
      if (view != null && KeyEventDispatcher.dispatchBeforeHierarchy(view, paramKeyEvent))
        return true; 
    } 
    if (paramKeyEvent.getKeyCode() == 82 && this.mOriginalWindowCallback.dispatchKeyEvent(paramKeyEvent))
      return true; 
    int i = paramKeyEvent.getKeyCode();
    if (paramKeyEvent.getAction() != 0)
      bool = false; 
    return bool ? onKeyDown(i, paramKeyEvent) : onKeyUp(i, paramKeyEvent);
  }
  
  void doInvalidatePanelMenu(int paramInt) {
    PanelFeatureState panelFeatureState = getPanelState(paramInt, true);
    if (panelFeatureState.menu != null) {
      Bundle bundle = new Bundle();
      panelFeatureState.menu.saveActionViewStates(bundle);
      if (bundle.size() > 0)
        panelFeatureState.frozenActionViewState = bundle; 
      panelFeatureState.menu.stopDispatchingItemsChanged();
      panelFeatureState.menu.clear();
    } 
    panelFeatureState.refreshMenuContent = true;
    panelFeatureState.refreshDecorView = true;
    if ((paramInt == 108 || paramInt == 0) && this.mDecorContentParent != null) {
      panelFeatureState = getPanelState(0, false);
      if (panelFeatureState != null) {
        panelFeatureState.isPrepared = false;
        preparePanel(panelFeatureState, null);
      } 
    } 
  }
  
  void endOnGoingFadeAnimation() {
    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mFadeAnim;
    if (viewPropertyAnimatorCompat != null)
      viewPropertyAnimatorCompat.cancel(); 
  }
  
  PanelFeatureState findMenuPanel(Menu paramMenu) {
    boolean bool;
    PanelFeatureState[] arrayOfPanelFeatureState = this.mPanels;
    if (arrayOfPanelFeatureState != null) {
      bool = arrayOfPanelFeatureState.length;
    } else {
      bool = false;
    } 
    for (byte b = 0; b < bool; b++) {
      PanelFeatureState panelFeatureState = arrayOfPanelFeatureState[b];
      if (panelFeatureState != null && panelFeatureState.menu == paramMenu)
        return panelFeatureState; 
    } 
    return null;
  }
  
  @Nullable
  public <T extends View> T findViewById(@IdRes int paramInt) {
    ensureSubDecor();
    return (T)this.mWindow.findViewById(paramInt);
  }
  
  final Context getActionBarThemedContext() {
    Context context1 = null;
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
      context1 = actionBar.getThemedContext(); 
    Context context2 = context1;
    if (context1 == null)
      context2 = this.mContext; 
    return context2;
  }
  
  @VisibleForTesting
  final AutoNightModeManager getAutoNightModeManager() {
    ensureAutoNightModeManager();
    return this.mAutoNightModeManager;
  }
  
  public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() { return new ActionBarDrawableToggleImpl(); }
  
  public MenuInflater getMenuInflater() {
    if (this.mMenuInflater == null) {
      initWindowDecorActionBar();
      Context context = this.mActionBar;
      if (context != null) {
        Context context1 = context.getThemedContext();
      } else {
        context = this.mContext;
      } 
      this.mMenuInflater = new SupportMenuInflater(context);
    } 
    return this.mMenuInflater;
  }
  
  protected PanelFeatureState getPanelState(int paramInt, boolean paramBoolean) { // Byte code:
    //   0: aload_0
    //   1: getfield mPanels : [Landroid/support/v7/app/AppCompatDelegateImpl$PanelFeatureState;
    //   4: astore_3
    //   5: aload_3
    //   6: astore #4
    //   8: aload_3
    //   9: ifnull -> 22
    //   12: aload #4
    //   14: astore_3
    //   15: aload #4
    //   17: arraylength
    //   18: iload_1
    //   19: if_icmpgt -> 56
    //   22: iload_1
    //   23: iconst_1
    //   24: iadd
    //   25: anewarray android/support/v7/app/AppCompatDelegateImpl$PanelFeatureState
    //   28: astore #5
    //   30: aload #4
    //   32: ifnull -> 47
    //   35: aload #4
    //   37: iconst_0
    //   38: aload #5
    //   40: iconst_0
    //   41: aload #4
    //   43: arraylength
    //   44: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   47: aload #5
    //   49: astore_3
    //   50: aload_0
    //   51: aload #5
    //   53: putfield mPanels : [Landroid/support/v7/app/AppCompatDelegateImpl$PanelFeatureState;
    //   56: aload_3
    //   57: iload_1
    //   58: aaload
    //   59: astore #5
    //   61: aload #5
    //   63: astore #4
    //   65: aload #5
    //   67: ifnonnull -> 89
    //   70: new android/support/v7/app/AppCompatDelegateImpl$PanelFeatureState
    //   73: dup
    //   74: iload_1
    //   75: invokespecial <init> : (I)V
    //   78: astore #5
    //   80: aload #5
    //   82: astore #4
    //   84: aload_3
    //   85: iload_1
    //   86: aload #5
    //   88: aastore
    //   89: aload #4
    //   91: areturn }
  
  ViewGroup getSubDecor() { return this.mSubDecor; }
  
  public ActionBar getSupportActionBar() {
    initWindowDecorActionBar();
    return this.mActionBar;
  }
  
  final CharSequence getTitle() {
    Window.Callback callback = this.mOriginalWindowCallback;
    return (callback instanceof Activity) ? ((Activity)callback).getTitle() : this.mTitle;
  }
  
  final Window.Callback getWindowCallback() { return this.mWindow.getCallback(); }
  
  public boolean hasWindowFeature(int paramInt) {
    boolean bool = false;
    switch (sanitizeWindowFeatureId(paramInt)) {
      case 109:
        bool = this.mOverlayActionBar;
        break;
      case 108:
        bool = this.mHasActionBar;
        break;
      case 10:
        bool = this.mOverlayActionMode;
        break;
      case 5:
        bool = this.mFeatureIndeterminateProgress;
        break;
      case 2:
        bool = this.mFeatureProgress;
        break;
      case 1:
        bool = this.mWindowNoTitle;
        break;
    } 
    return (bool || this.mWindow.hasFeature(paramInt));
  }
  
  public void installViewFactory() {
    LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
    if (layoutInflater.getFactory() == null) {
      LayoutInflaterCompat.setFactory2(layoutInflater, this);
      return;
    } 
    if (!(layoutInflater.getFactory2() instanceof AppCompatDelegateImpl))
      Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's"); 
  }
  
  public void invalidateOptionsMenu() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null && actionBar.invalidateOptionsMenu())
      return; 
    invalidatePanelMenu(0);
  }
  
  public boolean isHandleNativeActionModesEnabled() { return this.mHandleNativeActionModes; }
  
  int mapNightMode(int paramInt) {
    if (paramInt != -100) {
      if (paramInt != 0)
        return paramInt; 
      if (Build.VERSION.SDK_INT >= 23 && ((UiModeManager)this.mContext.getSystemService(UiModeManager.class)).getNightMode() == 0)
        return -1; 
      ensureAutoNightModeManager();
      return this.mAutoNightModeManager.getApplyableNightMode();
    } 
    return -1;
  }
  
  boolean onBackPressed() {
    ActionMode actionMode = this.mActionMode;
    if (actionMode != null) {
      actionMode.finish();
      return true;
    } 
    ActionBar actionBar = getSupportActionBar();
    return (actionBar != null && actionBar.collapseActionView());
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (this.mHasActionBar && this.mSubDecorInstalled) {
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null)
        actionBar.onConfigurationChanged(paramConfiguration); 
    } 
    AppCompatDrawableManager.get().onConfigurationChanged(this.mContext);
    applyDayNight();
  }
  
  public void onCreate(Bundle paramBundle) {
    callback = this.mOriginalWindowCallback;
    if (callback instanceof Activity) {
      String str = null;
      try {
        String str1 = NavUtils.getParentActivityName((Activity)callback);
        str = str1;
      } catch (IllegalArgumentException callback) {}
      if (str != null) {
        ActionBar actionBar = peekSupportActionBar();
        if (actionBar == null) {
          this.mEnableDefaultActionBarUp = true;
        } else {
          actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        } 
      } 
    } 
    if (paramBundle != null && this.mLocalNightMode == -100) {
      this.mLocalNightMode = paramBundle.getInt("appcompat:local_night_mode", -100);
      return;
    } 
  }
  
  public final View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) { return createView(paramView, paramString, paramContext, paramAttributeSet); }
  
  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet) { return onCreateView(null, paramString, paramContext, paramAttributeSet); }
  
  public void onDestroy() {
    if (this.mInvalidatePanelMenuPosted)
      this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable); 
    this.mIsDestroyed = true;
    ActionBar actionBar = this.mActionBar;
    if (actionBar != null)
      actionBar.onDestroy(); 
    AutoNightModeManager autoNightModeManager = this.mAutoNightModeManager;
    if (autoNightModeManager != null)
      autoNightModeManager.cleanup(); 
  }
  
  boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool = true;
    if (paramInt != 4) {
      if (paramInt != 82)
        return false; 
      onKeyDownPanel(0, paramKeyEvent);
      return true;
    } 
    if ((paramKeyEvent.getFlags() & 0x80) == 0)
      bool = false; 
    this.mLongPressBackDown = bool;
    return false;
  }
  
  boolean onKeyShortcut(int paramInt, KeyEvent paramKeyEvent) {
    PanelFeatureState panelFeatureState1;
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null && actionBar.onKeyShortcut(paramInt, paramKeyEvent))
      return true; 
    PanelFeatureState panelFeatureState2 = this.mPreparedPanel;
    if (panelFeatureState2 != null && performPanelShortcut(panelFeatureState2, paramKeyEvent.getKeyCode(), paramKeyEvent, 1)) {
      panelFeatureState1 = this.mPreparedPanel;
      if (panelFeatureState1 != null)
        panelFeatureState1.isHandled = true; 
      return true;
    } 
    if (this.mPreparedPanel == null) {
      panelFeatureState2 = getPanelState(0, true);
      preparePanel(panelFeatureState2, panelFeatureState1);
      boolean bool = performPanelShortcut(panelFeatureState2, panelFeatureState1.getKeyCode(), panelFeatureState1, 1);
      panelFeatureState2.isPrepared = false;
      if (bool)
        return true; 
    } 
    return false;
  }
  
  boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    if (paramInt != 4) {
      if (paramInt != 82)
        return false; 
      onKeyUpPanel(0, paramKeyEvent);
      return true;
    } 
    boolean bool = this.mLongPressBackDown;
    this.mLongPressBackDown = false;
    PanelFeatureState panelFeatureState = getPanelState(0, false);
    if (panelFeatureState != null && panelFeatureState.isOpen) {
      if (!bool)
        closePanel(panelFeatureState, true); 
      return true;
    } 
    return onBackPressed();
  }
  
  public boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem) {
    Window.Callback callback = getWindowCallback();
    if (callback != null && !this.mIsDestroyed) {
      PanelFeatureState panelFeatureState = findMenuPanel(paramMenuBuilder.getRootMenu());
      if (panelFeatureState != null)
        return callback.onMenuItemSelected(panelFeatureState.featureId, paramMenuItem); 
    } 
    return false;
  }
  
  public void onMenuModeChange(MenuBuilder paramMenuBuilder) { reopenMenu(paramMenuBuilder, true); }
  
  void onMenuOpened(int paramInt) {
    if (paramInt == 108) {
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null)
        actionBar.dispatchMenuVisibilityChanged(true); 
    } 
  }
  
  void onPanelClosed(int paramInt) {
    if (paramInt == 108) {
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null)
        actionBar.dispatchMenuVisibilityChanged(false); 
    } else if (paramInt == 0) {
      PanelFeatureState panelFeatureState = getPanelState(paramInt, true);
      if (panelFeatureState.isOpen) {
        closePanel(panelFeatureState, false);
        return;
      } 
    } 
  }
  
  public void onPostCreate(Bundle paramBundle) { ensureSubDecor(); }
  
  public void onPostResume() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
      actionBar.setShowHideAnimationEnabled(true); 
  }
  
  public void onSaveInstanceState(Bundle paramBundle) {
    int i = this.mLocalNightMode;
    if (i != -100)
      paramBundle.putInt("appcompat:local_night_mode", i); 
  }
  
  public void onStart() { applyDayNight(); }
  
  public void onStop() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
      actionBar.setShowHideAnimationEnabled(false); 
    AutoNightModeManager autoNightModeManager = this.mAutoNightModeManager;
    if (autoNightModeManager != null)
      autoNightModeManager.cleanup(); 
  }
  
  void onSubDecorInstalled(ViewGroup paramViewGroup) {}
  
  final ActionBar peekSupportActionBar() { return this.mActionBar; }
  
  public boolean requestWindowFeature(int paramInt) {
    paramInt = sanitizeWindowFeatureId(paramInt);
    if (this.mWindowNoTitle && paramInt == 108)
      return false; 
    if (this.mHasActionBar && paramInt == 1)
      this.mHasActionBar = false; 
    switch (paramInt) {
      default:
        return this.mWindow.requestFeature(paramInt);
      case 109:
        throwFeatureRequestIfSubDecorInstalled();
        this.mOverlayActionBar = true;
        return true;
      case 108:
        throwFeatureRequestIfSubDecorInstalled();
        this.mHasActionBar = true;
        return true;
      case 10:
        throwFeatureRequestIfSubDecorInstalled();
        this.mOverlayActionMode = true;
        return true;
      case 5:
        throwFeatureRequestIfSubDecorInstalled();
        this.mFeatureIndeterminateProgress = true;
        return true;
      case 2:
        throwFeatureRequestIfSubDecorInstalled();
        this.mFeatureProgress = true;
        return true;
      case 1:
        break;
    } 
    throwFeatureRequestIfSubDecorInstalled();
    this.mWindowNoTitle = true;
    return true;
  }
  
  public void setContentView(int paramInt) {
    ensureSubDecor();
    ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
    viewGroup.removeAllViews();
    LayoutInflater.from(this.mContext).inflate(paramInt, viewGroup);
    this.mOriginalWindowCallback.onContentChanged();
  }
  
  public void setContentView(View paramView) {
    ensureSubDecor();
    ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
    viewGroup.removeAllViews();
    viewGroup.addView(paramView);
    this.mOriginalWindowCallback.onContentChanged();
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    ensureSubDecor();
    ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
    viewGroup.removeAllViews();
    viewGroup.addView(paramView, paramLayoutParams);
    this.mOriginalWindowCallback.onContentChanged();
  }
  
  public void setHandleNativeActionModesEnabled(boolean paramBoolean) { this.mHandleNativeActionModes = paramBoolean; }
  
  public void setLocalNightMode(int paramInt) {
    switch (paramInt) {
      default:
        Log.i("AppCompatDelegate", "setLocalNightMode() called with an unknown mode");
        return;
      case -1:
      case 0:
      case 1:
      case 2:
        break;
    } 
    if (this.mLocalNightMode != paramInt) {
      this.mLocalNightMode = paramInt;
      if (this.mApplyDayNightCalled)
        applyDayNight(); 
    } 
  }
  
  public void setSupportActionBar(Toolbar paramToolbar) {
    if (!(this.mOriginalWindowCallback instanceof Activity))
      return; 
    ActionBar actionBar = getSupportActionBar();
    if (!(actionBar instanceof WindowDecorActionBar)) {
      this.mMenuInflater = null;
      if (actionBar != null)
        actionBar.onDestroy(); 
      if (paramToolbar != null) {
        ToolbarActionBar toolbarActionBar = new ToolbarActionBar(paramToolbar, ((Activity)this.mOriginalWindowCallback).getTitle(), this.mAppCompatWindowCallback);
        this.mActionBar = toolbarActionBar;
        this.mWindow.setCallback(toolbarActionBar.getWrappedWindowCallback());
      } else {
        this.mActionBar = null;
        this.mWindow.setCallback(this.mAppCompatWindowCallback);
      } 
      invalidateOptionsMenu();
      return;
    } 
    throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
  }
  
  public final void setTitle(CharSequence paramCharSequence) {
    this.mTitle = paramCharSequence;
    DecorContentParent decorContentParent = this.mDecorContentParent;
    if (decorContentParent != null) {
      decorContentParent.setWindowTitle(paramCharSequence);
      return;
    } 
    if (peekSupportActionBar() != null) {
      peekSupportActionBar().setWindowTitle(paramCharSequence);
      return;
    } 
    TextView textView = this.mTitleView;
    if (textView != null)
      textView.setText(paramCharSequence); 
  }
  
  final boolean shouldAnimateActionModeView() {
    if (this.mSubDecorInstalled) {
      ViewGroup viewGroup = this.mSubDecor;
      if (viewGroup != null && ViewCompat.isLaidOut(viewGroup))
        return true; 
    } 
    return false;
  }
  
  public ActionMode startSupportActionMode(@NonNull ActionMode.Callback paramCallback) {
    if (paramCallback != null) {
      ActionMode actionMode = this.mActionMode;
      if (actionMode != null)
        actionMode.finish(); 
      paramCallback = new ActionModeCallbackWrapperV9(paramCallback);
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        this.mActionMode = actionBar.startActionMode(paramCallback);
        ActionMode actionMode1 = this.mActionMode;
        if (actionMode1 != null) {
          AppCompatCallback appCompatCallback = this.mAppCompatCallback;
          if (appCompatCallback != null)
            appCompatCallback.onSupportActionModeStarted(actionMode1); 
        } 
      } 
      if (this.mActionMode == null)
        this.mActionMode = startSupportActionModeFromWindow(paramCallback); 
      return this.mActionMode;
    } 
    throw new IllegalArgumentException("ActionMode callback can not be null.");
  }
  
  ActionMode startSupportActionModeFromWindow(@NonNull ActionMode.Callback paramCallback) {
    endOnGoingFadeAnimation();
    ActionMode actionMode2 = this.mActionMode;
    if (actionMode2 != null)
      actionMode2.finish(); 
    ActionMode.Callback callback = paramCallback;
    if (!(paramCallback instanceof ActionModeCallbackWrapperV9))
      callback = new ActionModeCallbackWrapperV9(paramCallback); 
    paramCallback = null;
    appCompatCallback = this.mAppCompatCallback;
    if (appCompatCallback != null && !this.mIsDestroyed)
      try {
        ActionMode actionMode = appCompatCallback.onWindowStartingSupportActionMode(callback);
        actionMode1 = actionMode;
      } catch (AbstractMethodError appCompatCallback) {} 
    if (actionMode1 != null) {
      this.mActionMode = actionMode1;
    } else {
      ActionBarContextView actionBarContextView = this.mActionModeView;
      boolean bool = true;
      if (actionBarContextView == null)
        if (this.mIsFloating) {
          TypedValue typedValue = new TypedValue();
          Context context = this.mContext.getTheme();
          context.resolveAttribute(R.attr.actionBarTheme, typedValue, true);
          if (typedValue.resourceId != 0) {
            Resources.Theme theme = this.mContext.getResources().newTheme();
            theme.setTo(context);
            theme.applyStyle(typedValue.resourceId, true);
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this.mContext, 0);
            contextThemeWrapper.getTheme().setTo(theme);
          } else {
            context = this.mContext;
          } 
          this.mActionModeView = new ActionBarContextView(context);
          this.mActionModePopup = new PopupWindow(context, null, R.attr.actionModePopupWindowStyle);
          PopupWindowCompat.setWindowLayoutType(this.mActionModePopup, 2);
          this.mActionModePopup.setContentView(this.mActionModeView);
          this.mActionModePopup.setWidth(-1);
          context.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true);
          int i = TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
          this.mActionModeView.setContentHeight(i);
          this.mActionModePopup.setHeight(-2);
          this.mShowActionModePopup = new Runnable() {
              public void run() {
                AppCompatDelegateImpl.this.mActionModePopup.showAtLocation(AppCompatDelegateImpl.this.mActionModeView, 55, 0, 0);
                AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
                if (AppCompatDelegateImpl.this.shouldAnimateActionModeView()) {
                  AppCompatDelegateImpl.this.mActionModeView.setAlpha(0.0F);
                  AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
                  appCompatDelegateImpl.mFadeAnim = ViewCompat.animate(appCompatDelegateImpl.mActionModeView).alpha(1.0F);
                  AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
                        public void onAnimationEnd(View param2View) {
                          AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0F);
                          AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                          AppCompatDelegateImpl.this.mFadeAnim = null;
                        }
                        
                        public void onAnimationStart(View param2View) { AppCompatDelegateImpl.this.mActionModeView.setVisibility(0); }
                      });
                  return;
                } 
                AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0F);
                AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
              }
            };
        } else {
          ViewStubCompat viewStubCompat = (ViewStubCompat)this.mSubDecor.findViewById(R.id.action_mode_bar_stub);
          if (viewStubCompat != null) {
            viewStubCompat.setLayoutInflater(LayoutInflater.from(getActionBarThemedContext()));
            this.mActionModeView = (ActionBarContextView)viewStubCompat.inflate();
          } 
        }  
      if (this.mActionModeView != null) {
        endOnGoingFadeAnimation();
        this.mActionModeView.killMode();
        Context context = this.mActionModeView.getContext();
        ActionBarContextView actionBarContextView1 = this.mActionModeView;
        if (this.mActionModePopup != null)
          bool = false; 
        StandaloneActionMode standaloneActionMode = new StandaloneActionMode(context, actionBarContextView1, callback, bool);
        if (callback.onCreateActionMode(standaloneActionMode, standaloneActionMode.getMenu())) {
          standaloneActionMode.invalidate();
          this.mActionModeView.initForMode(standaloneActionMode);
          this.mActionMode = standaloneActionMode;
          if (shouldAnimateActionModeView()) {
            this.mActionModeView.setAlpha(0.0F);
            this.mFadeAnim = ViewCompat.animate(this.mActionModeView).alpha(1.0F);
            this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
                  public void onAnimationEnd(View param1View) {
                    AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0F);
                    AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                    AppCompatDelegateImpl.this.mFadeAnim = null;
                  }
                  
                  public void onAnimationStart(View param1View) {
                    AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                    AppCompatDelegateImpl.this.mActionModeView.sendAccessibilityEvent(32);
                    if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View)
                      ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent()); 
                  }
                });
          } else {
            this.mActionModeView.setAlpha(1.0F);
            this.mActionModeView.setVisibility(0);
            this.mActionModeView.sendAccessibilityEvent(32);
            if (this.mActionModeView.getParent() instanceof View)
              ViewCompat.requestApplyInsets((View)this.mActionModeView.getParent()); 
          } 
          if (this.mActionModePopup != null)
            this.mWindow.getDecorView().post(this.mShowActionModePopup); 
        } else {
          this.mActionMode = null;
        } 
      } 
    } 
    ActionMode actionMode1 = this.mActionMode;
    if (actionMode1 != null) {
      AppCompatCallback appCompatCallback1 = this.mAppCompatCallback;
      if (appCompatCallback1 != null)
        appCompatCallback1.onSupportActionModeStarted(actionMode1); 
    } 
    return this.mActionMode;
  }
  
  int updateStatusGuard(int paramInt) {
    int i = 0;
    byte b2 = 0;
    ActionBarContextView actionBarContextView = this.mActionModeView;
    int k = 0;
    byte b1 = i;
    int j = paramInt;
    if (actionBarContextView != null) {
      b1 = i;
      j = paramInt;
      if (actionBarContextView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
        byte b;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mActionModeView.getLayoutParams();
        int m = 0;
        i = 0;
        if (this.mActionModeView.isShown()) {
          if (this.mTempRect1 == null) {
            this.mTempRect1 = new Rect();
            this.mTempRect2 = new Rect();
          } 
          Rect rect1 = this.mTempRect1;
          Rect rect2 = this.mTempRect2;
          rect1.set(0, paramInt, 0, 0);
          ViewUtils.computeFitSystemWindows(this.mSubDecor, rect1, rect2);
          if (rect2.top == 0) {
            b = paramInt;
          } else {
            b = 0;
          } 
          if (marginLayoutParams.topMargin != b) {
            b = 1;
            marginLayoutParams.topMargin = paramInt;
            View view1 = this.mStatusGuard;
            if (view1 == null) {
              this.mStatusGuard = new View(this.mContext);
              this.mStatusGuard.setBackgroundColor(this.mContext.getResources().getColor(R.color.abc_input_method_navigation_guard));
              this.mSubDecor.addView(this.mStatusGuard, -1, new ViewGroup.LayoutParams(-1, paramInt));
              i = b;
            } else {
              ViewGroup.LayoutParams layoutParams = view1.getLayoutParams();
              i = b;
              if (layoutParams.height != paramInt) {
                layoutParams.height = paramInt;
                this.mStatusGuard.setLayoutParams(layoutParams);
                i = b;
              } 
            } 
          } 
          if (this.mStatusGuard != null) {
            b = 1;
          } else {
            b = 0;
          } 
          m = paramInt;
          if (!this.mOverlayActionMode) {
            m = paramInt;
            if (b != 0)
              m = 0; 
          } 
        } else {
          b = b2;
          i = m;
          m = paramInt;
          if (marginLayoutParams.topMargin != 0) {
            i = 1;
            marginLayoutParams.topMargin = 0;
            m = paramInt;
            b = b2;
          } 
        } 
        b1 = b;
        j = m;
        if (i != 0) {
          this.mActionModeView.setLayoutParams(marginLayoutParams);
          j = m;
          b1 = b;
        } 
      } 
    } 
    View view = this.mStatusGuard;
    if (view != null) {
      if (b1 != 0) {
        paramInt = k;
      } else {
        paramInt = 8;
      } 
      view.setVisibility(paramInt);
    } 
    return j;
  }
  
  private class ActionBarDrawableToggleImpl implements ActionBarDrawerToggle.Delegate {
    public Context getActionBarThemedContext() { return AppCompatDelegateImpl.this.getActionBarThemedContext(); }
    
    public Drawable getThemeUpIndicator() {
      TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getActionBarThemedContext(), null, new int[] { R.attr.homeAsUpIndicator });
      Drawable drawable = tintTypedArray.getDrawable(0);
      tintTypedArray.recycle();
      return drawable;
    }
    
    public boolean isNavigationVisible() {
      ActionBar actionBar = AppCompatDelegateImpl.this.getSupportActionBar();
      return (actionBar != null && (actionBar.getDisplayOptions() & 0x4) != 0);
    }
    
    public void setActionBarDescription(int param1Int) {
      ActionBar actionBar = AppCompatDelegateImpl.this.getSupportActionBar();
      if (actionBar != null)
        actionBar.setHomeActionContentDescription(param1Int); 
    }
    
    public void setActionBarUpIndicator(Drawable param1Drawable, int param1Int) {
      ActionBar actionBar = AppCompatDelegateImpl.this.getSupportActionBar();
      if (actionBar != null) {
        actionBar.setHomeAsUpIndicator(param1Drawable);
        actionBar.setHomeActionContentDescription(param1Int);
      } 
    }
  }
  
  private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) { AppCompatDelegateImpl.this.checkCloseActionMenu(param1MenuBuilder); }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      Window.Callback callback = AppCompatDelegateImpl.this.getWindowCallback();
      if (callback != null)
        callback.onMenuOpened(108, param1MenuBuilder); 
      return true;
    }
  }
  
  class ActionModeCallbackWrapperV9 implements ActionMode.Callback {
    private ActionMode.Callback mWrapped;
    
    public ActionModeCallbackWrapperV9(ActionMode.Callback param1Callback) { this.mWrapped = param1Callback; }
    
    public boolean onActionItemClicked(ActionMode param1ActionMode, MenuItem param1MenuItem) { return this.mWrapped.onActionItemClicked(param1ActionMode, param1MenuItem); }
    
    public boolean onCreateActionMode(ActionMode param1ActionMode, Menu param1Menu) { return this.mWrapped.onCreateActionMode(param1ActionMode, param1Menu); }
    
    public void onDestroyActionMode(ActionMode param1ActionMode) {
      this.mWrapped.onDestroyActionMode(param1ActionMode);
      if (AppCompatDelegateImpl.this.mActionModePopup != null)
        AppCompatDelegateImpl.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImpl.this.mShowActionModePopup); 
      if (AppCompatDelegateImpl.this.mActionModeView != null) {
        AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
        AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
        appCompatDelegateImpl.mFadeAnim = ViewCompat.animate(appCompatDelegateImpl.mActionModeView).alpha(0.0F);
        AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
              public void onAnimationEnd(View param2View) {
                AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
                if (AppCompatDelegateImpl.this.mActionModePopup != null) {
                  AppCompatDelegateImpl.this.mActionModePopup.dismiss();
                } else if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                  ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
                } 
                AppCompatDelegateImpl.this.mActionModeView.removeAllViews();
                AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                AppCompatDelegateImpl.this.mFadeAnim = null;
              }
            });
      } 
      if (AppCompatDelegateImpl.this.mAppCompatCallback != null)
        AppCompatDelegateImpl.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImpl.this.mActionMode); 
      AppCompatDelegateImpl.this.mActionMode = null;
    }
    
    public boolean onPrepareActionMode(ActionMode param1ActionMode, Menu param1Menu) { return this.mWrapped.onPrepareActionMode(param1ActionMode, param1Menu); }
  }
  
  class null extends ViewPropertyAnimatorListenerAdapter {
    null() {}
    
    public void onAnimationEnd(View param1View) {
      AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
      if (AppCompatDelegateImpl.this.mActionModePopup != null) {
        AppCompatDelegateImpl.this.mActionModePopup.dismiss();
      } else if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
        ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
      } 
      AppCompatDelegateImpl.this.mActionModeView.removeAllViews();
      AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
      AppCompatDelegateImpl.this.mFadeAnim = null;
    }
  }
  
  class AppCompatWindowCallback extends WindowCallbackWrapper {
    AppCompatWindowCallback(Window.Callback param1Callback) { super(param1Callback); }
    
    public boolean dispatchKeyEvent(KeyEvent param1KeyEvent) { return (AppCompatDelegateImpl.this.dispatchKeyEvent(param1KeyEvent) || super.dispatchKeyEvent(param1KeyEvent)); }
    
    public boolean dispatchKeyShortcutEvent(KeyEvent param1KeyEvent) { return (super.dispatchKeyShortcutEvent(param1KeyEvent) || AppCompatDelegateImpl.this.onKeyShortcut(param1KeyEvent.getKeyCode(), param1KeyEvent)); }
    
    public void onContentChanged() {}
    
    public boolean onCreatePanelMenu(int param1Int, Menu param1Menu) { return (param1Int == 0 && !(param1Menu instanceof MenuBuilder)) ? false : super.onCreatePanelMenu(param1Int, param1Menu); }
    
    public boolean onMenuOpened(int param1Int, Menu param1Menu) {
      super.onMenuOpened(param1Int, param1Menu);
      AppCompatDelegateImpl.this.onMenuOpened(param1Int);
      return true;
    }
    
    public void onPanelClosed(int param1Int, Menu param1Menu) {
      super.onPanelClosed(param1Int, param1Menu);
      AppCompatDelegateImpl.this.onPanelClosed(param1Int);
    }
    
    public boolean onPreparePanel(int param1Int, View param1View, Menu param1Menu) {
      Object object;
      if (param1Menu instanceof MenuBuilder) {
        object = (MenuBuilder)param1Menu;
      } else {
        object = null;
      } 
      if (param1Int == 0 && object == null)
        return false; 
      if (object != null)
        object.setOverrideVisibleItems(true); 
      boolean bool = super.onPreparePanel(param1Int, param1View, param1Menu);
      if (object != null)
        object.setOverrideVisibleItems(false); 
      return bool;
    }
    
    @RequiresApi(24)
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> param1List, Menu param1Menu, int param1Int) {
      AppCompatDelegateImpl.PanelFeatureState panelFeatureState = AppCompatDelegateImpl.this.getPanelState(0, true);
      if (panelFeatureState != null && panelFeatureState.menu != null) {
        super.onProvideKeyboardShortcuts(param1List, panelFeatureState.menu, param1Int);
        return;
      } 
      super.onProvideKeyboardShortcuts(param1List, param1Menu, param1Int);
    }
    
    public ActionMode onWindowStartingActionMode(ActionMode.Callback param1Callback) { return (Build.VERSION.SDK_INT >= 23) ? null : (AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() ? startAsSupportActionMode(param1Callback) : super.onWindowStartingActionMode(param1Callback)); }
    
    @RequiresApi(23)
    public ActionMode onWindowStartingActionMode(ActionMode.Callback param1Callback, int param1Int) { return (!AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() || param1Int != 0) ? super.onWindowStartingActionMode(param1Callback, param1Int) : startAsSupportActionMode(param1Callback); }
    
    final ActionMode startAsSupportActionMode(ActionMode.Callback param1Callback) {
      SupportActionModeWrapper.CallbackWrapper callbackWrapper = new SupportActionModeWrapper.CallbackWrapper(AppCompatDelegateImpl.this.mContext, param1Callback);
      ActionMode actionMode = AppCompatDelegateImpl.this.startSupportActionMode(callbackWrapper);
      return (actionMode != null) ? callbackWrapper.getActionModeWrapper(actionMode) : null;
    }
  }
  
  @VisibleForTesting
  final class AutoNightModeManager {
    private BroadcastReceiver mAutoTimeChangeReceiver;
    
    private IntentFilter mAutoTimeChangeReceiverFilter;
    
    private boolean mIsNight;
    
    private TwilightManager mTwilightManager;
    
    AutoNightModeManager(TwilightManager param1TwilightManager) {
      this.mTwilightManager = param1TwilightManager;
      this.mIsNight = param1TwilightManager.isNight();
    }
    
    void cleanup() {
      if (this.mAutoTimeChangeReceiver != null) {
        AppCompatDelegateImpl.this.mContext.unregisterReceiver(this.mAutoTimeChangeReceiver);
        this.mAutoTimeChangeReceiver = null;
      } 
    }
    
    void dispatchTimeChanged() {
      boolean bool = this.mTwilightManager.isNight();
      if (bool != this.mIsNight) {
        this.mIsNight = bool;
        AppCompatDelegateImpl.this.applyDayNight();
      } 
    }
    
    int getApplyableNightMode() {
      this.mIsNight = this.mTwilightManager.isNight();
      return this.mIsNight ? 2 : 1;
    }
    
    void setup() {
      cleanup();
      if (this.mAutoTimeChangeReceiver == null)
        this.mAutoTimeChangeReceiver = new BroadcastReceiver() {
            public void onReceive(Context param2Context, Intent param2Intent) { AppCompatDelegateImpl.AutoNightModeManager.this.dispatchTimeChanged(); }
          }; 
      if (this.mAutoTimeChangeReceiverFilter == null) {
        this.mAutoTimeChangeReceiverFilter = new IntentFilter();
        this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_SET");
        this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_TICK");
      } 
      AppCompatDelegateImpl.this.mContext.registerReceiver(this.mAutoTimeChangeReceiver, this.mAutoTimeChangeReceiverFilter);
    }
  }
  
  class null extends BroadcastReceiver {
    null() {}
    
    public void onReceive(Context param1Context, Intent param1Intent) { this.this$1.dispatchTimeChanged(); }
  }
  
  private class ListMenuDecorView extends ContentFrameLayout {
    public ListMenuDecorView(Context param1Context) { super(param1Context); }
    
    private boolean isOutOfBounds(int param1Int1, int param1Int2) { return (param1Int1 < -5 || param1Int2 < -5 || param1Int1 > getWidth() + 5 || param1Int2 > getHeight() + 5); }
    
    public boolean dispatchKeyEvent(KeyEvent param1KeyEvent) { return (AppCompatDelegateImpl.this.dispatchKeyEvent(param1KeyEvent) || super.dispatchKeyEvent(param1KeyEvent)); }
    
    public boolean onInterceptTouchEvent(MotionEvent param1MotionEvent) {
      if (param1MotionEvent.getAction() == 0 && isOutOfBounds((int)param1MotionEvent.getX(), (int)param1MotionEvent.getY())) {
        AppCompatDelegateImpl.this.closePanel(0);
        return true;
      } 
      return super.onInterceptTouchEvent(param1MotionEvent);
    }
    
    public void setBackgroundResource(int param1Int) { setBackgroundDrawable(AppCompatResources.getDrawable(getContext(), param1Int)); }
  }
  
  protected static final class PanelFeatureState {
    int background;
    
    View createdPanelView;
    
    ViewGroup decorView;
    
    int featureId;
    
    Bundle frozenActionViewState;
    
    Bundle frozenMenuState;
    
    int gravity;
    
    boolean isHandled;
    
    boolean isOpen;
    
    boolean isPrepared;
    
    ListMenuPresenter listMenuPresenter;
    
    Context listPresenterContext;
    
    MenuBuilder menu;
    
    public boolean qwertyMode;
    
    boolean refreshDecorView;
    
    boolean refreshMenuContent;
    
    View shownPanelView;
    
    boolean wasLastOpen;
    
    int windowAnimations;
    
    int x;
    
    int y;
    
    PanelFeatureState(int param1Int) {
      this.featureId = param1Int;
      this.refreshDecorView = false;
    }
    
    void applyFrozenState() {
      MenuBuilder menuBuilder = this.menu;
      if (menuBuilder != null) {
        Bundle bundle = this.frozenMenuState;
        if (bundle != null) {
          menuBuilder.restorePresenterStates(bundle);
          this.frozenMenuState = null;
        } 
      } 
    }
    
    public void clearMenuPresenters() {
      MenuBuilder menuBuilder = this.menu;
      if (menuBuilder != null)
        menuBuilder.removeMenuPresenter(this.listMenuPresenter); 
      this.listMenuPresenter = null;
    }
    
    MenuView getListMenuView(MenuPresenter.Callback param1Callback) {
      if (this.menu == null)
        return null; 
      if (this.listMenuPresenter == null) {
        this.listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, R.layout.abc_list_menu_item_layout);
        this.listMenuPresenter.setCallback(param1Callback);
        this.menu.addMenuPresenter(this.listMenuPresenter);
      } 
      return this.listMenuPresenter.getMenuView(this.decorView);
    }
    
    public boolean hasPanelItems() {
      View view = this.shownPanelView;
      boolean bool = false;
      if (view == null)
        return false; 
      if (this.createdPanelView != null)
        return true; 
      if (this.listMenuPresenter.getAdapter().getCount() > 0)
        bool = true; 
      return bool;
    }
    
    void onRestoreInstanceState(Parcelable param1Parcelable) {
      param1Parcelable = (SavedState)param1Parcelable;
      this.featureId = param1Parcelable.featureId;
      this.wasLastOpen = param1Parcelable.isOpen;
      this.frozenMenuState = param1Parcelable.menuState;
      this.shownPanelView = null;
      this.decorView = null;
    }
    
    Parcelable onSaveInstanceState() {
      SavedState savedState = new SavedState();
      savedState.featureId = this.featureId;
      savedState.isOpen = this.isOpen;
      if (this.menu != null) {
        savedState.menuState = new Bundle();
        this.menu.savePresenterStates(savedState.menuState);
      } 
      return savedState;
    }
    
    void setMenu(MenuBuilder param1MenuBuilder) {
      MenuBuilder menuBuilder = this.menu;
      if (param1MenuBuilder == menuBuilder)
        return; 
      if (menuBuilder != null)
        menuBuilder.removeMenuPresenter(this.listMenuPresenter); 
      this.menu = param1MenuBuilder;
      if (param1MenuBuilder != null) {
        ListMenuPresenter listMenuPresenter1 = this.listMenuPresenter;
        if (listMenuPresenter1 != null)
          param1MenuBuilder.addMenuPresenter(listMenuPresenter1); 
      } 
    }
    
    void setStyle(Context param1Context) {
      TypedValue typedValue = new TypedValue();
      Resources.Theme theme = param1Context.getResources().newTheme();
      theme.setTo(param1Context.getTheme());
      theme.resolveAttribute(R.attr.actionBarPopupTheme, typedValue, true);
      if (typedValue.resourceId != 0)
        theme.applyStyle(typedValue.resourceId, true); 
      theme.resolveAttribute(R.attr.panelMenuListTheme, typedValue, true);
      if (typedValue.resourceId != 0) {
        theme.applyStyle(typedValue.resourceId, true);
      } else {
        theme.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
      } 
      ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(param1Context, 0);
      contextThemeWrapper.getTheme().setTo(theme);
      this.listPresenterContext = contextThemeWrapper;
      TypedArray typedArray = contextThemeWrapper.obtainStyledAttributes(R.styleable.AppCompatTheme);
      this.background = typedArray.getResourceId(R.styleable.AppCompatTheme_panelBackground, 0);
      this.windowAnimations = typedArray.getResourceId(R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
      typedArray.recycle();
    }
    
    private static class SavedState implements Parcelable {
      public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
          public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel) { return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param3Parcel, null); }
          
          public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel, ClassLoader param3ClassLoader) { return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param3Parcel, param3ClassLoader); }
          
          public AppCompatDelegateImpl.PanelFeatureState.SavedState[] newArray(int param3Int) { return new AppCompatDelegateImpl.PanelFeatureState.SavedState[param3Int]; }
        };
      
      int featureId;
      
      boolean isOpen;
      
      Bundle menuState;
      
      static SavedState readFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
        SavedState savedState = new SavedState();
        savedState.featureId = param2Parcel.readInt();
        int i = param2Parcel.readInt();
        boolean bool = true;
        if (i != 1)
          bool = false; 
        savedState.isOpen = bool;
        if (savedState.isOpen)
          savedState.menuState = param2Parcel.readBundle(param2ClassLoader); 
        return savedState;
      }
      
      public int describeContents() { return 0; }
      
      public void writeToParcel(Parcel param2Parcel, int param2Int) { throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n"); }
    }
    
    static final class null extends Object implements Parcelable.ClassLoaderCreator<SavedState> {
      public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param2Parcel) { return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param2Parcel, null); }
      
      public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) { return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param2Parcel, param2ClassLoader); }
      
      public AppCompatDelegateImpl.PanelFeatureState.SavedState[] newArray(int param2Int) { return new AppCompatDelegateImpl.PanelFeatureState.SavedState[param2Int]; }
    }
  }
  
  private static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
        public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel) { return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param3Parcel, null); }
        
        public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel, ClassLoader param3ClassLoader) { return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param3Parcel, param3ClassLoader); }
        
        public AppCompatDelegateImpl.PanelFeatureState.SavedState[] newArray(int param3Int) { return new AppCompatDelegateImpl.PanelFeatureState.SavedState[param3Int]; }
      };
    
    int featureId;
    
    boolean isOpen;
    
    Bundle menuState;
    
    static SavedState readFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      SavedState savedState = new SavedState();
      savedState.featureId = param1Parcel.readInt();
      int i = param1Parcel.readInt();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      savedState.isOpen = bool;
      if (savedState.isOpen)
        savedState.menuState = param1Parcel.readBundle(param1ClassLoader); 
      return savedState;
    }
    
    public int describeContents() { return 0; }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) { throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n"); }
  }
  
  static final class null extends Object implements Parcelable.ClassLoaderCreator<PanelFeatureState.SavedState> {
    public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param1Parcel) { return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param1Parcel, null); }
    
    public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) { return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param1Parcel, param1ClassLoader); }
    
    public AppCompatDelegateImpl.PanelFeatureState.SavedState[] newArray(int param1Int) { return new AppCompatDelegateImpl.PanelFeatureState.SavedState[param1Int]; }
  }
  
  private final class PanelMenuPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      boolean bool;
      MenuBuilder menuBuilder = param1MenuBuilder.getRootMenu();
      if (menuBuilder != param1MenuBuilder) {
        bool = true;
      } else {
        bool = false;
      } 
      AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
      if (bool)
        param1MenuBuilder = menuBuilder; 
      AppCompatDelegateImpl.PanelFeatureState panelFeatureState = appCompatDelegateImpl.findMenuPanel(param1MenuBuilder);
      if (panelFeatureState != null) {
        if (bool) {
          AppCompatDelegateImpl.this.callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, menuBuilder);
          AppCompatDelegateImpl.this.closePanel(panelFeatureState, true);
          return;
        } 
        AppCompatDelegateImpl.this.closePanel(panelFeatureState, param1Boolean);
      } 
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      if (param1MenuBuilder == null && AppCompatDelegateImpl.this.mHasActionBar) {
        Window.Callback callback = AppCompatDelegateImpl.this.getWindowCallback();
        if (callback != null && !AppCompatDelegateImpl.this.mIsDestroyed)
          callback.onMenuOpened(108, param1MenuBuilder); 
      } 
      return true;
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\app\AppCompatDelegateImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */