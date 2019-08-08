package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.widget.AutoSizeableTextView;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

public class AppCompatButton extends Button implements TintableBackgroundView, AutoSizeableTextView {
  private final AppCompatBackgroundHelper mBackgroundTintHelper = new AppCompatBackgroundHelper(this);
  
  private final AppCompatTextHelper mTextHelper;
  
  public AppCompatButton(Context paramContext) { this(paramContext, null); }
  
  public AppCompatButton(Context paramContext, AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, R.attr.buttonStyle); }
  
  public AppCompatButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(TintContextWrapper.wrap(paramContext), paramAttributeSet, paramInt);
    this.mBackgroundTintHelper.loadFromAttributes(paramAttributeSet, paramInt);
    this.mTextHelper = new AppCompatTextHelper(this);
    this.mTextHelper.loadFromAttributes(paramAttributeSet, paramInt);
    this.mTextHelper.applyCompoundDrawablesTints();
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.applySupportBackgroundTint(); 
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.applyCompoundDrawablesTints(); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int getAutoSizeMaxTextSize() {
    if (PLATFORM_SUPPORTS_AUTOSIZE)
      return super.getAutoSizeMaxTextSize(); 
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    return (appCompatTextHelper != null) ? appCompatTextHelper.getAutoSizeMaxTextSize() : -1;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int getAutoSizeMinTextSize() {
    if (PLATFORM_SUPPORTS_AUTOSIZE)
      return super.getAutoSizeMinTextSize(); 
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    return (appCompatTextHelper != null) ? appCompatTextHelper.getAutoSizeMinTextSize() : -1;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int getAutoSizeStepGranularity() {
    if (PLATFORM_SUPPORTS_AUTOSIZE)
      return super.getAutoSizeStepGranularity(); 
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    return (appCompatTextHelper != null) ? appCompatTextHelper.getAutoSizeStepGranularity() : -1;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int[] getAutoSizeTextAvailableSizes() {
    if (PLATFORM_SUPPORTS_AUTOSIZE)
      return super.getAutoSizeTextAvailableSizes(); 
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    return (appCompatTextHelper != null) ? appCompatTextHelper.getAutoSizeTextAvailableSizes() : new int[0];
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int getAutoSizeTextType() {
    boolean bool = PLATFORM_SUPPORTS_AUTOSIZE;
    byte b = 0;
    if (bool) {
      if (super.getAutoSizeTextType() == 1)
        b = 1; 
      return b;
    } 
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    return (appCompatTextHelper != null) ? appCompatTextHelper.getAutoSizeTextType() : 0;
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
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName(Button.class.getName());
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(Button.class.getName());
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4); 
  }
  
  protected void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
    super.onTextChanged(paramCharSequence, paramInt1, paramInt2, paramInt3);
    if (this.mTextHelper != null && !PLATFORM_SUPPORTS_AUTOSIZE && this.mTextHelper.isAutoSizeEnabled())
      this.mTextHelper.autoSizeText(); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setAutoSizeTextTypeUniformWithConfiguration(int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws IllegalArgumentException {
    if (PLATFORM_SUPPORTS_AUTOSIZE) {
      super.setAutoSizeTextTypeUniformWithConfiguration(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.setAutoSizeTextTypeUniformWithConfiguration(paramInt1, paramInt2, paramInt3, paramInt4); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] paramArrayOfInt, int paramInt) throws IllegalArgumentException {
    if (PLATFORM_SUPPORTS_AUTOSIZE) {
      super.setAutoSizeTextTypeUniformWithPresetSizes(paramArrayOfInt, paramInt);
      return;
    } 
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(paramArrayOfInt, paramInt); 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setAutoSizeTextTypeWithDefaults(int paramInt) {
    if (PLATFORM_SUPPORTS_AUTOSIZE) {
      super.setAutoSizeTextTypeWithDefaults(paramInt);
      return;
    } 
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.setAutoSizeTextTypeWithDefaults(paramInt); 
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
  
  public void setCustomSelectionActionModeCallback(ActionMode.Callback paramCallback) { super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, paramCallback)); }
  
  public void setSupportAllCaps(boolean paramBoolean) {
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.setAllCaps(paramBoolean); 
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
  
  public void setTextAppearance(Context paramContext, int paramInt) {
    super.setTextAppearance(paramContext, paramInt);
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.onSetTextAppearance(paramContext, paramInt); 
  }
  
  public void setTextSize(int paramInt, float paramFloat) {
    if (PLATFORM_SUPPORTS_AUTOSIZE) {
      super.setTextSize(paramInt, paramFloat);
      return;
    } 
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.setTextSize(paramInt, paramFloat); 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\AppCompatButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */