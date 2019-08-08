package android.support.v4.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormatSymbols;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v4.text.PrecomputedTextCompat;
import android.support.v4.util.Preconditions;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class TextViewCompat {
  public static final int AUTO_SIZE_TEXT_TYPE_NONE = 0;
  
  public static final int AUTO_SIZE_TEXT_TYPE_UNIFORM = 1;
  
  private static final int LINES = 1;
  
  private static final String LOG_TAG = "TextViewCompat";
  
  private static Field sMaxModeField;
  
  private static boolean sMaxModeFieldFetched;
  
  private static Field sMaximumField;
  
  private static boolean sMaximumFieldFetched;
  
  private static Field sMinModeField;
  
  private static boolean sMinModeFieldFetched;
  
  private static Field sMinimumField;
  
  private static boolean sMinimumFieldFetched;
  
  public static int getAutoSizeMaxTextSize(@NonNull TextView paramTextView) { return (Build.VERSION.SDK_INT >= 27) ? paramTextView.getAutoSizeMaxTextSize() : ((paramTextView instanceof AutoSizeableTextView) ? ((AutoSizeableTextView)paramTextView).getAutoSizeMaxTextSize() : -1); }
  
  public static int getAutoSizeMinTextSize(@NonNull TextView paramTextView) { return (Build.VERSION.SDK_INT >= 27) ? paramTextView.getAutoSizeMinTextSize() : ((paramTextView instanceof AutoSizeableTextView) ? ((AutoSizeableTextView)paramTextView).getAutoSizeMinTextSize() : -1); }
  
  public static int getAutoSizeStepGranularity(@NonNull TextView paramTextView) { return (Build.VERSION.SDK_INT >= 27) ? paramTextView.getAutoSizeStepGranularity() : ((paramTextView instanceof AutoSizeableTextView) ? ((AutoSizeableTextView)paramTextView).getAutoSizeStepGranularity() : -1); }
  
  @NonNull
  public static int[] getAutoSizeTextAvailableSizes(@NonNull TextView paramTextView) { return (Build.VERSION.SDK_INT >= 27) ? paramTextView.getAutoSizeTextAvailableSizes() : ((paramTextView instanceof AutoSizeableTextView) ? ((AutoSizeableTextView)paramTextView).getAutoSizeTextAvailableSizes() : new int[0]); }
  
  public static int getAutoSizeTextType(@NonNull TextView paramTextView) { return (Build.VERSION.SDK_INT >= 27) ? paramTextView.getAutoSizeTextType() : ((paramTextView instanceof AutoSizeableTextView) ? ((AutoSizeableTextView)paramTextView).getAutoSizeTextType() : 0); }
  
  @NonNull
  public static Drawable[] getCompoundDrawablesRelative(@NonNull TextView paramTextView) {
    Drawable[] arrayOfDrawable;
    if (Build.VERSION.SDK_INT >= 18)
      return paramTextView.getCompoundDrawablesRelative(); 
    if (Build.VERSION.SDK_INT >= 17) {
      int i = paramTextView.getLayoutDirection();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      arrayOfDrawable = paramTextView.getCompoundDrawables();
      if (bool) {
        Drawable drawable1 = arrayOfDrawable[2];
        Drawable drawable2 = arrayOfDrawable[0];
        arrayOfDrawable[0] = drawable1;
        arrayOfDrawable[2] = drawable2;
      } 
      return arrayOfDrawable;
    } 
    return arrayOfDrawable.getCompoundDrawables();
  }
  
  public static int getFirstBaselineToTopHeight(@NonNull TextView paramTextView) { return paramTextView.getPaddingTop() - (paramTextView.getPaint().getFontMetricsInt()).top; }
  
  public static int getLastBaselineToBottomHeight(@NonNull TextView paramTextView) { return paramTextView.getPaddingBottom() + (paramTextView.getPaint().getFontMetricsInt()).bottom; }
  
  public static int getMaxLines(@NonNull TextView paramTextView) {
    if (Build.VERSION.SDK_INT >= 16)
      return paramTextView.getMaxLines(); 
    if (!sMaxModeFieldFetched) {
      sMaxModeField = retrieveField("mMaxMode");
      sMaxModeFieldFetched = true;
    } 
    Field field = sMaxModeField;
    if (field != null && retrieveIntFromField(field, paramTextView) == 1) {
      if (!sMaximumFieldFetched) {
        sMaximumField = retrieveField("mMaximum");
        sMaximumFieldFetched = true;
      } 
      field = sMaximumField;
      if (field != null)
        return retrieveIntFromField(field, paramTextView); 
    } 
    return -1;
  }
  
  public static int getMinLines(@NonNull TextView paramTextView) {
    if (Build.VERSION.SDK_INT >= 16)
      return paramTextView.getMinLines(); 
    if (!sMinModeFieldFetched) {
      sMinModeField = retrieveField("mMinMode");
      sMinModeFieldFetched = true;
    } 
    Field field = sMinModeField;
    if (field != null && retrieveIntFromField(field, paramTextView) == 1) {
      if (!sMinimumFieldFetched) {
        sMinimumField = retrieveField("mMinimum");
        sMinimumFieldFetched = true;
      } 
      field = sMinimumField;
      if (field != null)
        return retrieveIntFromField(field, paramTextView); 
    } 
    return -1;
  }
  
  @RequiresApi(18)
  private static int getTextDirection(@NonNull TextDirectionHeuristic paramTextDirectionHeuristic) { return (paramTextDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_RTL) ? 1 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_LTR) ? 1 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.ANYRTL_LTR) ? 2 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.LTR) ? 3 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.RTL) ? 4 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.LOCALE) ? 5 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_LTR) ? 6 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_RTL) ? 7 : 1))))))); }
  
  @RequiresApi(18)
  private static TextDirectionHeuristic getTextDirectionHeuristic(@NonNull TextView paramTextView) {
    if (paramTextView.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod)
      return TextDirectionHeuristics.LTR; 
    int i = Build.VERSION.SDK_INT;
    byte b = 0;
    if (i >= 28 && (paramTextView.getInputType() & 0xF) == 3) {
      b = Character.getDirectionality(DecimalFormatSymbols.getInstance(paramTextView.getTextLocale()).getDigitStrings()[0].codePointAt(0));
      return (b == 1 || b == 2) ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR;
    } 
    if (paramTextView.getLayoutDirection() == 1)
      b = 1; 
    switch (paramTextView.getTextDirection()) {
      default:
        return (b != 0) ? TextDirectionHeuristics.FIRSTSTRONG_RTL : TextDirectionHeuristics.FIRSTSTRONG_LTR;
      case 7:
        return TextDirectionHeuristics.FIRSTSTRONG_RTL;
      case 6:
        return TextDirectionHeuristics.FIRSTSTRONG_LTR;
      case 5:
        return TextDirectionHeuristics.LOCALE;
      case 4:
        return TextDirectionHeuristics.RTL;
      case 3:
        return TextDirectionHeuristics.LTR;
      case 2:
        break;
    } 
    return TextDirectionHeuristics.ANYRTL_LTR;
  }
  
  @NonNull
  public static PrecomputedTextCompat.Params getTextMetricsParams(@NonNull TextView paramTextView) {
    if (Build.VERSION.SDK_INT >= 28)
      return new PrecomputedTextCompat.Params(paramTextView.getTextMetricsParams()); 
    PrecomputedTextCompat.Params.Builder builder = new PrecomputedTextCompat.Params.Builder(new TextPaint(paramTextView.getPaint()));
    if (Build.VERSION.SDK_INT >= 23) {
      builder.setBreakStrategy(paramTextView.getBreakStrategy());
      builder.setHyphenationFrequency(paramTextView.getHyphenationFrequency());
    } 
    if (Build.VERSION.SDK_INT >= 18)
      builder.setTextDirection(getTextDirectionHeuristic(paramTextView)); 
    return builder.build();
  }
  
  private static Field retrieveField(String paramString) {
    Field field = null;
    try {
      Field field1 = TextView.class.getDeclaredField(paramString);
      field = field1;
      field1.setAccessible(true);
      return field1;
    } catch (NoSuchFieldException noSuchFieldException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Could not retrieve ");
      stringBuilder.append(paramString);
      stringBuilder.append(" field.");
      Log.e("TextViewCompat", stringBuilder.toString());
      return field;
    } 
  }
  
  private static int retrieveIntFromField(Field paramField, TextView paramTextView) {
    try {
      return paramField.getInt(paramTextView);
    } catch (IllegalAccessException paramTextView) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Could not retrieve value of ");
      stringBuilder.append(paramField.getName());
      stringBuilder.append(" field.");
      Log.d("TextViewCompat", stringBuilder.toString());
      return -1;
    } 
  }
  
  public static void setAutoSizeTextTypeUniformWithConfiguration(@NonNull TextView paramTextView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws IllegalArgumentException {
    if (Build.VERSION.SDK_INT >= 27) {
      paramTextView.setAutoSizeTextTypeUniformWithConfiguration(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    if (paramTextView instanceof AutoSizeableTextView)
      ((AutoSizeableTextView)paramTextView).setAutoSizeTextTypeUniformWithConfiguration(paramInt1, paramInt2, paramInt3, paramInt4); 
  }
  
  public static void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull TextView paramTextView, @NonNull int[] paramArrayOfInt, int paramInt) throws IllegalArgumentException {
    if (Build.VERSION.SDK_INT >= 27) {
      paramTextView.setAutoSizeTextTypeUniformWithPresetSizes(paramArrayOfInt, paramInt);
      return;
    } 
    if (paramTextView instanceof AutoSizeableTextView)
      ((AutoSizeableTextView)paramTextView).setAutoSizeTextTypeUniformWithPresetSizes(paramArrayOfInt, paramInt); 
  }
  
  public static void setAutoSizeTextTypeWithDefaults(@NonNull TextView paramTextView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 27) {
      paramTextView.setAutoSizeTextTypeWithDefaults(paramInt);
      return;
    } 
    if (paramTextView instanceof AutoSizeableTextView)
      ((AutoSizeableTextView)paramTextView).setAutoSizeTextTypeWithDefaults(paramInt); 
  }
  
  public static void setCompoundDrawablesRelative(@NonNull TextView paramTextView, @Nullable Drawable paramDrawable1, @Nullable Drawable paramDrawable2, @Nullable Drawable paramDrawable3, @Nullable Drawable paramDrawable4) {
    if (Build.VERSION.SDK_INT >= 18) {
      paramTextView.setCompoundDrawablesRelative(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 17) {
      Drawable drawable;
      int i = paramTextView.getLayoutDirection();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      if (bool) {
        drawable = paramDrawable3;
      } else {
        drawable = paramDrawable1;
      } 
      if (!bool)
        paramDrawable1 = paramDrawable3; 
      paramTextView.setCompoundDrawables(drawable, paramDrawable2, paramDrawable1, paramDrawable4);
      return;
    } 
    paramTextView.setCompoundDrawables(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
  }
  
  public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView paramTextView, @DrawableRes int paramInt1, @DrawableRes int paramInt2, @DrawableRes int paramInt3, @DrawableRes int paramInt4) throws IllegalArgumentException {
    if (Build.VERSION.SDK_INT >= 18) {
      paramTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 17) {
      int i = paramTextView.getLayoutDirection();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      if (bool) {
        i = paramInt3;
      } else {
        i = paramInt1;
      } 
      if (!bool)
        paramInt1 = paramInt3; 
      paramTextView.setCompoundDrawablesWithIntrinsicBounds(i, paramInt2, paramInt1, paramInt4);
      return;
    } 
    paramTextView.setCompoundDrawablesWithIntrinsicBounds(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView paramTextView, @Nullable Drawable paramDrawable1, @Nullable Drawable paramDrawable2, @Nullable Drawable paramDrawable3, @Nullable Drawable paramDrawable4) {
    if (Build.VERSION.SDK_INT >= 18) {
      paramTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 17) {
      Drawable drawable;
      int i = paramTextView.getLayoutDirection();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      if (bool) {
        drawable = paramDrawable3;
      } else {
        drawable = paramDrawable1;
      } 
      if (!bool)
        paramDrawable1 = paramDrawable3; 
      paramTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, paramDrawable2, paramDrawable1, paramDrawable4);
      return;
    } 
    paramTextView.setCompoundDrawablesWithIntrinsicBounds(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
  }
  
  public static void setCustomSelectionActionModeCallback(@NonNull TextView paramTextView, @NonNull ActionMode.Callback paramCallback) { paramTextView.setCustomSelectionActionModeCallback(wrapCustomSelectionActionModeCallback(paramTextView, paramCallback)); }
  
  public static void setFirstBaselineToTopHeight(@NonNull TextView paramTextView, @IntRange(from = 0L) @Px int paramInt) {
    int i;
    Preconditions.checkArgumentNonnegative(paramInt);
    if (Build.VERSION.SDK_INT >= 28) {
      paramTextView.setFirstBaselineToTopHeight(paramInt);
      return;
    } 
    Paint.FontMetricsInt fontMetricsInt = paramTextView.getPaint().getFontMetricsInt();
    if (Build.VERSION.SDK_INT < 16 || paramTextView.getIncludeFontPadding()) {
      i = fontMetricsInt.top;
    } else {
      i = fontMetricsInt.ascent;
    } 
    if (paramInt > Math.abs(i)) {
      i = -i;
      paramTextView.setPadding(paramTextView.getPaddingLeft(), paramInt - i, paramTextView.getPaddingRight(), paramTextView.getPaddingBottom());
    } 
  }
  
  public static void setLastBaselineToBottomHeight(@NonNull TextView paramTextView, @IntRange(from = 0L) @Px int paramInt) {
    int i;
    Preconditions.checkArgumentNonnegative(paramInt);
    Paint.FontMetricsInt fontMetricsInt = paramTextView.getPaint().getFontMetricsInt();
    if (Build.VERSION.SDK_INT < 16 || paramTextView.getIncludeFontPadding()) {
      i = fontMetricsInt.bottom;
    } else {
      i = fontMetricsInt.descent;
    } 
    if (paramInt > Math.abs(i))
      paramTextView.setPadding(paramTextView.getPaddingLeft(), paramTextView.getPaddingTop(), paramTextView.getPaddingRight(), paramInt - i); 
  }
  
  public static void setLineHeight(@NonNull TextView paramTextView, @IntRange(from = 0L) @Px int paramInt) {
    Preconditions.checkArgumentNonnegative(paramInt);
    int i = paramTextView.getPaint().getFontMetricsInt(null);
    if (paramInt != i)
      paramTextView.setLineSpacing((paramInt - i), 1.0F); 
  }
  
  public static void setPrecomputedText(@NonNull TextView paramTextView, @NonNull PrecomputedTextCompat paramPrecomputedTextCompat) {
    if (Build.VERSION.SDK_INT >= 28) {
      paramTextView.setText(paramPrecomputedTextCompat.getPrecomputedText());
      return;
    } 
    if (getTextMetricsParams(paramTextView).equals(paramPrecomputedTextCompat.getParams())) {
      paramTextView.setText(paramPrecomputedTextCompat);
      return;
    } 
    throw new IllegalArgumentException("Given text can not be applied to TextView.");
  }
  
  public static void setTextAppearance(@NonNull TextView paramTextView, @StyleRes int paramInt) {
    if (Build.VERSION.SDK_INT >= 23) {
      paramTextView.setTextAppearance(paramInt);
      return;
    } 
    paramTextView.setTextAppearance(paramTextView.getContext(), paramInt);
  }
  
  public static void setTextMetricsParams(@NonNull TextView paramTextView, @NonNull PrecomputedTextCompat.Params paramParams) {
    if (Build.VERSION.SDK_INT >= 18)
      paramTextView.setTextDirection(getTextDirection(paramParams.getTextDirection())); 
    if (Build.VERSION.SDK_INT < 23) {
      float f = paramParams.getTextPaint().getTextScaleX();
      paramTextView.getPaint().set(paramParams.getTextPaint());
      if (f == paramTextView.getTextScaleX())
        paramTextView.setTextScaleX(f / 2.0F + 1.0F); 
      paramTextView.setTextScaleX(f);
      return;
    } 
    paramTextView.getPaint().set(paramParams.getTextPaint());
    paramTextView.setBreakStrategy(paramParams.getBreakStrategy());
    paramTextView.setHyphenationFrequency(paramParams.getHyphenationFrequency());
  }
  
  @NonNull
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static ActionMode.Callback wrapCustomSelectionActionModeCallback(@NonNull TextView paramTextView, @NonNull ActionMode.Callback paramCallback) { return (Build.VERSION.SDK_INT >= 26 && Build.VERSION.SDK_INT <= 27) ? ((paramCallback instanceof OreoCallback) ? paramCallback : new OreoCallback(paramCallback, paramTextView)) : paramCallback; }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface AutoSizeTextType {}
  
  @RequiresApi(26)
  private static class OreoCallback implements ActionMode.Callback {
    private static final int MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START = 100;
    
    private final ActionMode.Callback mCallback;
    
    private boolean mCanUseMenuBuilderReferences;
    
    private boolean mInitializedMenuBuilderReferences;
    
    private Class mMenuBuilderClass;
    
    private Method mMenuBuilderRemoveItemAtMethod;
    
    private final TextView mTextView;
    
    OreoCallback(ActionMode.Callback param1Callback, TextView param1TextView) {
      this.mCallback = param1Callback;
      this.mTextView = param1TextView;
      this.mInitializedMenuBuilderReferences = false;
    }
    
    private Intent createProcessTextIntent() { return (new Intent()).setAction("android.intent.action.PROCESS_TEXT").setType("text/plain"); }
    
    private Intent createProcessTextIntentForResolveInfo(ResolveInfo param1ResolveInfo, TextView param1TextView) { return createProcessTextIntent().putExtra("android.intent.extra.PROCESS_TEXT_READONLY", isEditable(param1TextView) ^ true).setClassName(param1ResolveInfo.activityInfo.packageName, param1ResolveInfo.activityInfo.name); }
    
    private List<ResolveInfo> getSupportedActivities(Context param1Context, PackageManager param1PackageManager) {
      ArrayList arrayList = new ArrayList();
      if (!(param1Context instanceof android.app.Activity))
        return arrayList; 
      for (ResolveInfo resolveInfo : param1PackageManager.queryIntentActivities(createProcessTextIntent(), 0)) {
        if (isSupportedActivity(resolveInfo, param1Context))
          arrayList.add(resolveInfo); 
      } 
      return arrayList;
    }
    
    private boolean isEditable(TextView param1TextView) { return (param1TextView instanceof android.text.Editable && param1TextView.onCheckIsTextEditor() && param1TextView.isEnabled()); }
    
    private boolean isSupportedActivity(ResolveInfo param1ResolveInfo, Context param1Context) { return param1Context.getPackageName().equals(param1ResolveInfo.activityInfo.packageName) ? true : (!param1ResolveInfo.activityInfo.exported ? false : ((param1ResolveInfo.activityInfo.permission == null || param1Context.checkSelfPermission(param1ResolveInfo.activityInfo.permission) == 0))); }
    
    private void recomputeProcessTextMenuItems(Menu param1Menu) {
      Context context = this.mTextView.getContext();
      PackageManager packageManager = context.getPackageManager();
      if (!this.mInitializedMenuBuilderReferences) {
        this.mInitializedMenuBuilderReferences = true;
        try {
          this.mMenuBuilderClass = Class.forName("com.android.internal.view.menu.MenuBuilder");
          this.mMenuBuilderRemoveItemAtMethod = this.mMenuBuilderClass.getDeclaredMethod("removeItemAt", new Class[] { int.class });
          this.mCanUseMenuBuilderReferences = true;
        } catch (ClassNotFoundException classNotFoundException) {
          this.mMenuBuilderClass = null;
          this.mMenuBuilderRemoveItemAtMethod = null;
          this.mCanUseMenuBuilderReferences = false;
        } catch (NoSuchMethodException noSuchMethodException) {}
      } 
      try {
        Method method;
        if (this.mCanUseMenuBuilderReferences && this.mMenuBuilderClass.isInstance(param1Menu)) {
          method = this.mMenuBuilderRemoveItemAtMethod;
        } else {
          method = param1Menu.getClass().getDeclaredMethod("removeItemAt", new Class[] { int.class });
        } 
        int i;
        for (i = param1Menu.size() - 1; i >= 0; i--) {
          MenuItem menuItem = param1Menu.getItem(i);
          if (menuItem.getIntent() != null && "android.intent.action.PROCESS_TEXT".equals(menuItem.getIntent().getAction()))
            method.invoke(param1Menu, new Object[] { Integer.valueOf(i) }); 
        } 
        List list = getSupportedActivities(context, packageManager);
        for (i = 0; i < list.size(); i++) {
          ResolveInfo resolveInfo = (ResolveInfo)list.get(i);
          param1Menu.add(0, 0, i + 100, resolveInfo.loadLabel(packageManager)).setIntent(createProcessTextIntentForResolveInfo(resolveInfo, this.mTextView)).setShowAsAction(1);
        } 
        return;
      } catch (NoSuchMethodException param1Menu) {
        return;
      } catch (IllegalAccessException param1Menu) {
        return;
      } catch (InvocationTargetException param1Menu) {
        return;
      } 
    }
    
    public boolean onActionItemClicked(ActionMode param1ActionMode, MenuItem param1MenuItem) { return this.mCallback.onActionItemClicked(param1ActionMode, param1MenuItem); }
    
    public boolean onCreateActionMode(ActionMode param1ActionMode, Menu param1Menu) { return this.mCallback.onCreateActionMode(param1ActionMode, param1Menu); }
    
    public void onDestroyActionMode(ActionMode param1ActionMode) { this.mCallback.onDestroyActionMode(param1ActionMode); }
    
    public boolean onPrepareActionMode(ActionMode param1ActionMode, Menu param1Menu) {
      recomputeProcessTextMenuItems(param1Menu);
      return this.mCallback.onPrepareActionMode(param1ActionMode, param1Menu);
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\widget\TextViewCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */