package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.CheckedTextView;

public class AppCompatCheckedTextView extends CheckedTextView {
  private static final int[] TINT_ATTRS = { 16843016 };
  
  private final AppCompatTextHelper mTextHelper = new AppCompatTextHelper(this);
  
  public AppCompatCheckedTextView(Context paramContext) { this(paramContext, null); }
  
  public AppCompatCheckedTextView(Context paramContext, AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, 16843720); }
  
  public AppCompatCheckedTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(TintContextWrapper.wrap(paramContext), paramAttributeSet, paramInt);
    this.mTextHelper.loadFromAttributes(paramAttributeSet, paramInt);
    this.mTextHelper.applyCompoundDrawablesTints();
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(), paramAttributeSet, TINT_ATTRS, paramInt, 0);
    setCheckMarkDrawable(tintTypedArray.getDrawable(0));
    tintTypedArray.recycle();
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.applyCompoundDrawablesTints(); 
  }
  
  public InputConnection onCreateInputConnection(EditorInfo paramEditorInfo) { return AppCompatHintHelper.onCreateInputConnection(super.onCreateInputConnection(paramEditorInfo), paramEditorInfo, this); }
  
  public void setCheckMarkDrawable(@DrawableRes int paramInt) { setCheckMarkDrawable(AppCompatResources.getDrawable(getContext(), paramInt)); }
  
  public void setCustomSelectionActionModeCallback(ActionMode.Callback paramCallback) { super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, paramCallback)); }
  
  public void setTextAppearance(Context paramContext, int paramInt) {
    super.setTextAppearance(paramContext, paramInt);
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.onSetTextAppearance(paramContext, paramInt); 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\AppCompatCheckedTextView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */