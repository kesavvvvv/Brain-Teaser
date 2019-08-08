package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.menu.ShowableListMenu;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ThemedSpinnerAdapter;

public class AppCompatSpinner extends Spinner implements TintableBackgroundView {
  private static final int[] ATTRS_ANDROID_SPINNERMODE = { 16843505 };
  
  private static final int MAX_ITEMS_MEASURED = 15;
  
  private static final int MODE_DIALOG = 0;
  
  private static final int MODE_DROPDOWN = 1;
  
  private static final int MODE_THEME = -1;
  
  private static final String TAG = "AppCompatSpinner";
  
  private final AppCompatBackgroundHelper mBackgroundTintHelper;
  
  int mDropDownWidth;
  
  private ForwardingListener mForwardingListener;
  
  DropdownPopup mPopup;
  
  private final Context mPopupContext;
  
  private final boolean mPopupSet;
  
  private SpinnerAdapter mTempAdapter;
  
  final Rect mTempRect;
  
  public AppCompatSpinner(Context paramContext) { this(paramContext, null); }
  
  public AppCompatSpinner(Context paramContext, int paramInt) { this(paramContext, null, R.attr.spinnerStyle, paramInt); }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, R.attr.spinnerStyle); }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt) { this(paramContext, paramAttributeSet, paramInt, -1); }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) { this(paramContext, paramAttributeSet, paramInt1, paramInt2, null); }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2, Resources.Theme paramTheme) { // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: aload_2
    //   3: iload_3
    //   4: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;I)V
    //   7: aload_0
    //   8: new android/graphics/Rect
    //   11: dup
    //   12: invokespecial <init> : ()V
    //   15: putfield mTempRect : Landroid/graphics/Rect;
    //   18: aload_1
    //   19: aload_2
    //   20: getstatic android/support/v7/appcompat/R$styleable.Spinner : [I
    //   23: iload_3
    //   24: iconst_0
    //   25: invokestatic obtainStyledAttributes : (Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroid/support/v7/widget/TintTypedArray;
    //   28: astore #10
    //   30: aload_0
    //   31: new android/support/v7/widget/AppCompatBackgroundHelper
    //   34: dup
    //   35: aload_0
    //   36: invokespecial <init> : (Landroid/view/View;)V
    //   39: putfield mBackgroundTintHelper : Landroid/support/v7/widget/AppCompatBackgroundHelper;
    //   42: aload #5
    //   44: ifnull -> 64
    //   47: aload_0
    //   48: new android/support/v7/view/ContextThemeWrapper
    //   51: dup
    //   52: aload_1
    //   53: aload #5
    //   55: invokespecial <init> : (Landroid/content/Context;Landroid/content/res/Resources$Theme;)V
    //   58: putfield mPopupContext : Landroid/content/Context;
    //   61: goto -> 120
    //   64: aload #10
    //   66: getstatic android/support/v7/appcompat/R$styleable.Spinner_popupTheme : I
    //   69: iconst_0
    //   70: invokevirtual getResourceId : (II)I
    //   73: istore #6
    //   75: iload #6
    //   77: ifeq -> 97
    //   80: aload_0
    //   81: new android/support/v7/view/ContextThemeWrapper
    //   84: dup
    //   85: aload_1
    //   86: iload #6
    //   88: invokespecial <init> : (Landroid/content/Context;I)V
    //   91: putfield mPopupContext : Landroid/content/Context;
    //   94: goto -> 120
    //   97: getstatic android/os/Build$VERSION.SDK_INT : I
    //   100: bipush #23
    //   102: if_icmpge -> 111
    //   105: aload_1
    //   106: astore #5
    //   108: goto -> 114
    //   111: aconst_null
    //   112: astore #5
    //   114: aload_0
    //   115: aload #5
    //   117: putfield mPopupContext : Landroid/content/Context;
    //   120: aload_0
    //   121: getfield mPopupContext : Landroid/content/Context;
    //   124: ifnull -> 369
    //   127: iload #4
    //   129: istore #7
    //   131: iload #4
    //   133: iconst_m1
    //   134: if_icmpne -> 266
    //   137: aconst_null
    //   138: astore #5
    //   140: aconst_null
    //   141: astore #8
    //   143: aload_1
    //   144: aload_2
    //   145: getstatic android/support/v7/widget/AppCompatSpinner.ATTRS_ANDROID_SPINNERMODE : [I
    //   148: iload_3
    //   149: iconst_0
    //   150: invokevirtual obtainStyledAttributes : (Landroid/util/AttributeSet;[III)Landroid/content/res/TypedArray;
    //   153: astore #9
    //   155: iload #4
    //   157: istore #6
    //   159: aload #9
    //   161: astore #8
    //   163: aload #9
    //   165: astore #5
    //   167: aload #9
    //   169: iconst_0
    //   170: invokevirtual hasValue : (I)Z
    //   173: ifeq -> 193
    //   176: aload #9
    //   178: astore #8
    //   180: aload #9
    //   182: astore #5
    //   184: aload #9
    //   186: iconst_0
    //   187: iconst_0
    //   188: invokevirtual getInt : (II)I
    //   191: istore #6
    //   193: iload #6
    //   195: istore #7
    //   197: aload #9
    //   199: ifnull -> 266
    //   202: iload #6
    //   204: istore #4
    //   206: aload #9
    //   208: astore #5
    //   210: aload #5
    //   212: invokevirtual recycle : ()V
    //   215: iload #4
    //   217: istore #7
    //   219: goto -> 266
    //   222: astore_1
    //   223: goto -> 254
    //   226: astore #9
    //   228: aload #5
    //   230: astore #8
    //   232: ldc 'AppCompatSpinner'
    //   234: ldc 'Could not read android:spinnerMode'
    //   236: aload #9
    //   238: invokestatic i : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   241: pop
    //   242: iload #4
    //   244: istore #7
    //   246: aload #5
    //   248: ifnull -> 266
    //   251: goto -> 210
    //   254: aload #8
    //   256: ifnull -> 264
    //   259: aload #8
    //   261: invokevirtual recycle : ()V
    //   264: aload_1
    //   265: athrow
    //   266: iload #7
    //   268: iconst_1
    //   269: if_icmpne -> 369
    //   272: new android/support/v7/widget/AppCompatSpinner$DropdownPopup
    //   275: dup
    //   276: aload_0
    //   277: aload_0
    //   278: getfield mPopupContext : Landroid/content/Context;
    //   281: aload_2
    //   282: iload_3
    //   283: invokespecial <init> : (Landroid/support/v7/widget/AppCompatSpinner;Landroid/content/Context;Landroid/util/AttributeSet;I)V
    //   286: astore #5
    //   288: aload_0
    //   289: getfield mPopupContext : Landroid/content/Context;
    //   292: aload_2
    //   293: getstatic android/support/v7/appcompat/R$styleable.Spinner : [I
    //   296: iload_3
    //   297: iconst_0
    //   298: invokestatic obtainStyledAttributes : (Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroid/support/v7/widget/TintTypedArray;
    //   301: astore #8
    //   303: aload_0
    //   304: aload #8
    //   306: getstatic android/support/v7/appcompat/R$styleable.Spinner_android_dropDownWidth : I
    //   309: bipush #-2
    //   311: invokevirtual getLayoutDimension : (II)I
    //   314: putfield mDropDownWidth : I
    //   317: aload #5
    //   319: aload #8
    //   321: getstatic android/support/v7/appcompat/R$styleable.Spinner_android_popupBackground : I
    //   324: invokevirtual getDrawable : (I)Landroid/graphics/drawable/Drawable;
    //   327: invokevirtual setBackgroundDrawable : (Landroid/graphics/drawable/Drawable;)V
    //   330: aload #5
    //   332: aload #10
    //   334: getstatic android/support/v7/appcompat/R$styleable.Spinner_android_prompt : I
    //   337: invokevirtual getString : (I)Ljava/lang/String;
    //   340: invokevirtual setPromptText : (Ljava/lang/CharSequence;)V
    //   343: aload #8
    //   345: invokevirtual recycle : ()V
    //   348: aload_0
    //   349: aload #5
    //   351: putfield mPopup : Landroid/support/v7/widget/AppCompatSpinner$DropdownPopup;
    //   354: aload_0
    //   355: new android/support/v7/widget/AppCompatSpinner$1
    //   358: dup
    //   359: aload_0
    //   360: aload_0
    //   361: aload #5
    //   363: invokespecial <init> : (Landroid/support/v7/widget/AppCompatSpinner;Landroid/view/View;Landroid/support/v7/widget/AppCompatSpinner$DropdownPopup;)V
    //   366: putfield mForwardingListener : Landroid/support/v7/widget/ForwardingListener;
    //   369: aload #10
    //   371: getstatic android/support/v7/appcompat/R$styleable.Spinner_android_entries : I
    //   374: invokevirtual getTextArray : (I)[Ljava/lang/CharSequence;
    //   377: astore #5
    //   379: aload #5
    //   381: ifnull -> 409
    //   384: new android/widget/ArrayAdapter
    //   387: dup
    //   388: aload_1
    //   389: ldc 17367048
    //   391: aload #5
    //   393: invokespecial <init> : (Landroid/content/Context;I[Ljava/lang/Object;)V
    //   396: astore_1
    //   397: aload_1
    //   398: getstatic android/support/v7/appcompat/R$layout.support_simple_spinner_dropdown_item : I
    //   401: invokevirtual setDropDownViewResource : (I)V
    //   404: aload_0
    //   405: aload_1
    //   406: invokevirtual setAdapter : (Landroid/widget/SpinnerAdapter;)V
    //   409: aload #10
    //   411: invokevirtual recycle : ()V
    //   414: aload_0
    //   415: iconst_1
    //   416: putfield mPopupSet : Z
    //   419: aload_0
    //   420: getfield mTempAdapter : Landroid/widget/SpinnerAdapter;
    //   423: astore_1
    //   424: aload_1
    //   425: ifnull -> 438
    //   428: aload_0
    //   429: aload_1
    //   430: invokevirtual setAdapter : (Landroid/widget/SpinnerAdapter;)V
    //   433: aload_0
    //   434: aconst_null
    //   435: putfield mTempAdapter : Landroid/widget/SpinnerAdapter;
    //   438: aload_0
    //   439: getfield mBackgroundTintHelper : Landroid/support/v7/widget/AppCompatBackgroundHelper;
    //   442: aload_2
    //   443: iload_3
    //   444: invokevirtual loadFromAttributes : (Landroid/util/AttributeSet;I)V
    //   447: return
    // Exception table:
    //   from	to	target	type
    //   143	155	226	java/lang/Exception
    //   143	155	222	finally
    //   167	176	226	java/lang/Exception
    //   167	176	222	finally
    //   184	193	226	java/lang/Exception
    //   184	193	222	finally
    //   232	242	222	finally }
  
  int compatMeasureContentWidth(SpinnerAdapter paramSpinnerAdapter, Drawable paramDrawable) {
    if (paramSpinnerAdapter == null)
      return 0; 
    int i = 0;
    View view = null;
    int k = 0;
    int m = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
    int n = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
    int j = Math.max(0, getSelectedItemPosition());
    int i1 = Math.min(paramSpinnerAdapter.getCount(), j + 15);
    j = Math.max(0, j - 15 - i1 - j);
    while (j < i1) {
      int i3 = paramSpinnerAdapter.getItemViewType(j);
      int i2 = k;
      if (i3 != k) {
        i2 = i3;
        view = null;
      } 
      view = paramSpinnerAdapter.getView(j, view, this);
      if (view.getLayoutParams() == null)
        view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2)); 
      view.measure(m, n);
      i = Math.max(i, view.getMeasuredWidth());
      j++;
      k = i2;
    } 
    j = i;
    if (paramDrawable != null) {
      paramDrawable.getPadding(this.mTempRect);
      j = i + this.mTempRect.left + this.mTempRect.right;
    } 
    return j;
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.applySupportBackgroundTint(); 
  }
  
  public int getDropDownHorizontalOffset() {
    DropdownPopup dropdownPopup = this.mPopup;
    return (dropdownPopup != null) ? dropdownPopup.getHorizontalOffset() : ((Build.VERSION.SDK_INT >= 16) ? super.getDropDownHorizontalOffset() : 0);
  }
  
  public int getDropDownVerticalOffset() {
    DropdownPopup dropdownPopup = this.mPopup;
    return (dropdownPopup != null) ? dropdownPopup.getVerticalOffset() : ((Build.VERSION.SDK_INT >= 16) ? super.getDropDownVerticalOffset() : 0);
  }
  
  public int getDropDownWidth() { return (this.mPopup != null) ? this.mDropDownWidth : ((Build.VERSION.SDK_INT >= 16) ? super.getDropDownWidth() : 0); }
  
  public Drawable getPopupBackground() {
    DropdownPopup dropdownPopup = this.mPopup;
    return (dropdownPopup != null) ? dropdownPopup.getBackground() : ((Build.VERSION.SDK_INT >= 16) ? super.getPopupBackground() : null);
  }
  
  public Context getPopupContext() { return (this.mPopup != null) ? this.mPopupContext : ((Build.VERSION.SDK_INT >= 23) ? super.getPopupContext() : null); }
  
  public CharSequence getPrompt() {
    DropdownPopup dropdownPopup = this.mPopup;
    return (dropdownPopup != null) ? dropdownPopup.getHintText() : super.getPrompt();
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
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    DropdownPopup dropdownPopup = this.mPopup;
    if (dropdownPopup != null && dropdownPopup.isShowing())
      this.mPopup.dismiss(); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    if (this.mPopup != null && View.MeasureSpec.getMode(paramInt1) == Integer.MIN_VALUE)
      setMeasuredDimension(Math.min(Math.max(getMeasuredWidth(), compatMeasureContentWidth(getAdapter(), getBackground())), View.MeasureSpec.getSize(paramInt1)), getMeasuredHeight()); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    ForwardingListener forwardingListener = this.mForwardingListener;
    return (forwardingListener != null && forwardingListener.onTouch(this, paramMotionEvent)) ? true : super.onTouchEvent(paramMotionEvent);
  }
  
  public boolean performClick() {
    DropdownPopup dropdownPopup = this.mPopup;
    if (dropdownPopup != null) {
      if (!dropdownPopup.isShowing())
        this.mPopup.show(); 
      return true;
    } 
    return super.performClick();
  }
  
  public void setAdapter(SpinnerAdapter paramSpinnerAdapter) {
    if (!this.mPopupSet) {
      this.mTempAdapter = paramSpinnerAdapter;
      return;
    } 
    super.setAdapter(paramSpinnerAdapter);
    if (this.mPopup != null) {
      Context context2 = this.mPopupContext;
      Context context1 = context2;
      if (context2 == null)
        context1 = getContext(); 
      this.mPopup.setAdapter(new DropDownAdapter(paramSpinnerAdapter, context1.getTheme()));
    } 
  }
  
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
  
  public void setDropDownHorizontalOffset(int paramInt) {
    DropdownPopup dropdownPopup = this.mPopup;
    if (dropdownPopup != null) {
      dropdownPopup.setHorizontalOffset(paramInt);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 16)
      super.setDropDownHorizontalOffset(paramInt); 
  }
  
  public void setDropDownVerticalOffset(int paramInt) {
    DropdownPopup dropdownPopup = this.mPopup;
    if (dropdownPopup != null) {
      dropdownPopup.setVerticalOffset(paramInt);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 16)
      super.setDropDownVerticalOffset(paramInt); 
  }
  
  public void setDropDownWidth(int paramInt) {
    if (this.mPopup != null) {
      this.mDropDownWidth = paramInt;
      return;
    } 
    if (Build.VERSION.SDK_INT >= 16)
      super.setDropDownWidth(paramInt); 
  }
  
  public void setPopupBackgroundDrawable(Drawable paramDrawable) {
    DropdownPopup dropdownPopup = this.mPopup;
    if (dropdownPopup != null) {
      dropdownPopup.setBackgroundDrawable(paramDrawable);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 16)
      super.setPopupBackgroundDrawable(paramDrawable); 
  }
  
  public void setPopupBackgroundResource(@DrawableRes int paramInt) { setPopupBackgroundDrawable(AppCompatResources.getDrawable(getPopupContext(), paramInt)); }
  
  public void setPrompt(CharSequence paramCharSequence) {
    DropdownPopup dropdownPopup = this.mPopup;
    if (dropdownPopup != null) {
      dropdownPopup.setPromptText(paramCharSequence);
      return;
    } 
    super.setPrompt(paramCharSequence);
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
  
  private static class DropDownAdapter implements ListAdapter, SpinnerAdapter {
    private SpinnerAdapter mAdapter;
    
    private ListAdapter mListAdapter;
    
    public DropDownAdapter(@Nullable SpinnerAdapter param1SpinnerAdapter, @Nullable Resources.Theme param1Theme) {
      this.mAdapter = param1SpinnerAdapter;
      if (param1SpinnerAdapter instanceof ListAdapter)
        this.mListAdapter = (ListAdapter)param1SpinnerAdapter; 
      if (param1Theme != null) {
        ThemedSpinnerAdapter themedSpinnerAdapter;
        if (Build.VERSION.SDK_INT >= 23 && param1SpinnerAdapter instanceof ThemedSpinnerAdapter) {
          themedSpinnerAdapter = (ThemedSpinnerAdapter)param1SpinnerAdapter;
          if (themedSpinnerAdapter.getDropDownViewTheme() != param1Theme) {
            themedSpinnerAdapter.setDropDownViewTheme(param1Theme);
            return;
          } 
        } else if (themedSpinnerAdapter instanceof ThemedSpinnerAdapter) {
          ThemedSpinnerAdapter themedSpinnerAdapter1 = (ThemedSpinnerAdapter)themedSpinnerAdapter;
          if (themedSpinnerAdapter1.getDropDownViewTheme() == null)
            themedSpinnerAdapter1.setDropDownViewTheme(param1Theme); 
        } 
      } 
    }
    
    public boolean areAllItemsEnabled() {
      ListAdapter listAdapter = this.mListAdapter;
      return (listAdapter != null) ? listAdapter.areAllItemsEnabled() : 1;
    }
    
    public int getCount() {
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      return (spinnerAdapter == null) ? 0 : spinnerAdapter.getCount();
    }
    
    public View getDropDownView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      return (spinnerAdapter == null) ? null : spinnerAdapter.getDropDownView(param1Int, param1View, param1ViewGroup);
    }
    
    public Object getItem(int param1Int) {
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      return (spinnerAdapter == null) ? null : spinnerAdapter.getItem(param1Int);
    }
    
    public long getItemId(int param1Int) {
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      return (spinnerAdapter == null) ? -1L : spinnerAdapter.getItemId(param1Int);
    }
    
    public int getItemViewType(int param1Int) { return 0; }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) { return getDropDownView(param1Int, param1View, param1ViewGroup); }
    
    public int getViewTypeCount() { return 1; }
    
    public boolean hasStableIds() {
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      return (spinnerAdapter != null && spinnerAdapter.hasStableIds());
    }
    
    public boolean isEmpty() { return (getCount() == 0); }
    
    public boolean isEnabled(int param1Int) {
      ListAdapter listAdapter = this.mListAdapter;
      return (listAdapter != null) ? listAdapter.isEnabled(param1Int) : 1;
    }
    
    public void registerDataSetObserver(DataSetObserver param1DataSetObserver) {
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter != null)
        spinnerAdapter.registerDataSetObserver(param1DataSetObserver); 
    }
    
    public void unregisterDataSetObserver(DataSetObserver param1DataSetObserver) {
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter != null)
        spinnerAdapter.unregisterDataSetObserver(param1DataSetObserver); 
    }
  }
  
  private class DropdownPopup extends ListPopupWindow {
    ListAdapter mAdapter;
    
    private CharSequence mHintText;
    
    private final Rect mVisibleRect = new Rect();
    
    public DropdownPopup(Context param1Context, AttributeSet param1AttributeSet, int param1Int) {
      super(param1Context, param1AttributeSet, param1Int);
      setAnchorView(this$0);
      setModal(true);
      setPromptPosition(0);
      setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param2AdapterView, View param2View, int param2Int, long param2Long) {
              AppCompatSpinner.DropdownPopup.this.this$0.setSelection(param2Int);
              if (AppCompatSpinner.DropdownPopup.this.this$0.getOnItemClickListener() != null)
                AppCompatSpinner.DropdownPopup.this.this$0.performItemClick(param2View, param2Int, AppCompatSpinner.DropdownPopup.this.mAdapter.getItemId(param2Int)); 
              AppCompatSpinner.DropdownPopup.this.dismiss();
            }
          });
    }
    
    void computeContentWidth() {
      Drawable drawable = getBackground();
      int i = 0;
      if (drawable != null) {
        drawable.getPadding(AppCompatSpinner.this.mTempRect);
        if (ViewUtils.isLayoutRtl(AppCompatSpinner.this)) {
          i = this.this$0.mTempRect.right;
        } else {
          i = -this.this$0.mTempRect.left;
        } 
      } else {
        Rect rect = AppCompatSpinner.this.mTempRect;
        this.this$0.mTempRect.right = 0;
        rect.left = 0;
      } 
      int j = AppCompatSpinner.this.getPaddingLeft();
      int k = AppCompatSpinner.this.getPaddingRight();
      int m = AppCompatSpinner.this.getWidth();
      if (AppCompatSpinner.this.mDropDownWidth == -2) {
        int i1 = AppCompatSpinner.this.compatMeasureContentWidth((SpinnerAdapter)this.mAdapter, getBackground());
        int i2 = (this.this$0.getContext().getResources().getDisplayMetrics()).widthPixels - this.this$0.mTempRect.left - this.this$0.mTempRect.right;
        int n = i1;
        if (i1 > i2)
          n = i2; 
        setContentWidth(Math.max(n, m - j - k));
      } else if (AppCompatSpinner.this.mDropDownWidth == -1) {
        setContentWidth(m - j - k);
      } else {
        setContentWidth(AppCompatSpinner.this.mDropDownWidth);
      } 
      if (ViewUtils.isLayoutRtl(AppCompatSpinner.this)) {
        i += m - k - getWidth();
      } else {
        i += j;
      } 
      setHorizontalOffset(i);
    }
    
    public CharSequence getHintText() { return this.mHintText; }
    
    boolean isVisibleToUser(View param1View) { return (ViewCompat.isAttachedToWindow(param1View) && param1View.getGlobalVisibleRect(this.mVisibleRect)); }
    
    public void setAdapter(ListAdapter param1ListAdapter) {
      super.setAdapter(param1ListAdapter);
      this.mAdapter = param1ListAdapter;
    }
    
    public void setPromptText(CharSequence param1CharSequence) { this.mHintText = param1CharSequence; }
    
    public void show() {
      boolean bool = isShowing();
      computeContentWidth();
      setInputMethodMode(2);
      super.show();
      getListView().setChoiceMode(1);
      setSelection(AppCompatSpinner.this.getSelectedItemPosition());
      if (bool)
        return; 
      ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
      if (viewTreeObserver != null) {
        final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
              AppCompatSpinner.DropdownPopup dropdownPopup = AppCompatSpinner.DropdownPopup.this;
              if (!dropdownPopup.isVisibleToUser(dropdownPopup.this$0)) {
                AppCompatSpinner.DropdownPopup.this.dismiss();
                return;
              } 
              AppCompatSpinner.DropdownPopup.this.computeContentWidth();
              AppCompatSpinner.DropdownPopup.this.show();
            }
          };
        viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener);
        setOnDismissListener(new PopupWindow.OnDismissListener() {
              public void onDismiss() {
                ViewTreeObserver viewTreeObserver = AppCompatSpinner.DropdownPopup.this.this$0.getViewTreeObserver();
                if (viewTreeObserver != null)
                  viewTreeObserver.removeGlobalOnLayoutListener(layoutListener); 
              }
            });
      } 
    }
  }
  
  class null implements AdapterView.OnItemClickListener {
    public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
      this.this$1.this$0.setSelection(param1Int);
      if (this.this$1.this$0.getOnItemClickListener() != null)
        this.this$1.this$0.performItemClick(param1View, param1Int, this.this$1.mAdapter.getItemId(param1Int)); 
      this.this$1.dismiss();
    }
  }
  
  class null implements ViewTreeObserver.OnGlobalLayoutListener {
    null() {}
    
    public void onGlobalLayout() {
      AppCompatSpinner.DropdownPopup dropdownPopup = this.this$1;
      if (!dropdownPopup.isVisibleToUser(dropdownPopup.this$0)) {
        this.this$1.dismiss();
        return;
      } 
      this.this$1.computeContentWidth();
      this.this$1.show();
    }
  }
  
  class null implements PopupWindow.OnDismissListener {
    public void onDismiss() {
      ViewTreeObserver viewTreeObserver = this.this$1.this$0.getViewTreeObserver();
      if (viewTreeObserver != null)
        viewTreeObserver.removeGlobalOnLayoutListener(layoutListener); 
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\AppCompatSpinner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */