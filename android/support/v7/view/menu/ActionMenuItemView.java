package android.support.v7.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ForwardingListener;
import android.support.v7.widget.TooltipCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ActionMenuItemView extends AppCompatTextView implements MenuView.ItemView, View.OnClickListener, ActionMenuView.ActionMenuChildView {
  private static final int MAX_ICON_SIZE = 32;
  
  private static final String TAG = "ActionMenuItemView";
  
  private boolean mAllowTextWithIcon;
  
  private boolean mExpandedFormat;
  
  private ForwardingListener mForwardingListener;
  
  private Drawable mIcon;
  
  MenuItemImpl mItemData;
  
  MenuBuilder.ItemInvoker mItemInvoker;
  
  private int mMaxIconSize;
  
  private int mMinWidth;
  
  PopupCallback mPopupCallback;
  
  private int mSavedPaddingLeft;
  
  private CharSequence mTitle;
  
  public ActionMenuItemView(Context paramContext) { this(paramContext, null); }
  
  public ActionMenuItemView(Context paramContext, AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, 0); }
  
  public ActionMenuItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    Resources resources = paramContext.getResources();
    this.mAllowTextWithIcon = shouldAllowTextWithIcon();
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ActionMenuItemView, paramInt, 0);
    this.mMinWidth = typedArray.getDimensionPixelSize(R.styleable.ActionMenuItemView_android_minWidth, 0);
    typedArray.recycle();
    this.mMaxIconSize = (int)(32.0F * (resources.getDisplayMetrics()).density + 0.5F);
    setOnClickListener(this);
    this.mSavedPaddingLeft = -1;
    setSaveEnabled(false);
  }
  
  private boolean shouldAllowTextWithIcon() {
    Configuration configuration = getContext().getResources().getConfiguration();
    int i = configuration.screenWidthDp;
    int j = configuration.screenHeightDp;
    return (i >= 480 || (i >= 640 && j >= 480) || configuration.orientation == 2);
  }
  
  private void updateTextButtonVisibility() {
    boolean bool2 = TextUtils.isEmpty(this.mTitle);
    boolean bool1 = true;
    if (this.mIcon != null && (!this.mItemData.showsTextAsAction() || (!this.mAllowTextWithIcon && !this.mExpandedFormat)))
      bool1 = false; 
    bool1 = (bool2 ^ true) & bool1;
    CharSequence charSequence2 = null;
    if (bool1) {
      charSequence1 = this.mTitle;
    } else {
      charSequence1 = null;
    } 
    setText(charSequence1);
    CharSequence charSequence1 = this.mItemData.getContentDescription();
    if (TextUtils.isEmpty(charSequence1)) {
      if (bool1) {
        charSequence1 = null;
      } else {
        charSequence1 = this.mItemData.getTitle();
      } 
      setContentDescription(charSequence1);
    } else {
      setContentDescription(charSequence1);
    } 
    charSequence1 = this.mItemData.getTooltipText();
    if (TextUtils.isEmpty(charSequence1)) {
      if (bool1) {
        charSequence1 = charSequence2;
      } else {
        charSequence1 = this.mItemData.getTitle();
      } 
      TooltipCompat.setTooltipText(this, charSequence1);
      return;
    } 
    TooltipCompat.setTooltipText(this, charSequence1);
  }
  
  public MenuItemImpl getItemData() { return this.mItemData; }
  
  public boolean hasText() { return TextUtils.isEmpty(getText()) ^ true; }
  
  public void initialize(MenuItemImpl paramMenuItemImpl, int paramInt) {
    this.mItemData = paramMenuItemImpl;
    setIcon(paramMenuItemImpl.getIcon());
    setTitle(paramMenuItemImpl.getTitleForItemView(this));
    setId(paramMenuItemImpl.getItemId());
    if (paramMenuItemImpl.isVisible()) {
      paramInt = 0;
    } else {
      paramInt = 8;
    } 
    setVisibility(paramInt);
    setEnabled(paramMenuItemImpl.isEnabled());
    if (paramMenuItemImpl.hasSubMenu() && this.mForwardingListener == null)
      this.mForwardingListener = new ActionMenuItemForwardingListener(); 
  }
  
  public boolean needsDividerAfter() { return hasText(); }
  
  public boolean needsDividerBefore() { return (hasText() && this.mItemData.getIcon() == null); }
  
  public void onClick(View paramView) {
    MenuBuilder.ItemInvoker itemInvoker = this.mItemInvoker;
    if (itemInvoker != null)
      itemInvoker.invokeItem(this.mItemData); 
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    this.mAllowTextWithIcon = shouldAllowTextWithIcon();
    updateTextButtonVisibility();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    boolean bool = hasText();
    if (bool) {
      int k = this.mSavedPaddingLeft;
      if (k >= 0)
        super.setPadding(k, getPaddingTop(), getPaddingRight(), getPaddingBottom()); 
    } 
    super.onMeasure(paramInt1, paramInt2);
    int i = View.MeasureSpec.getMode(paramInt1);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    int j = getMeasuredWidth();
    if (i == Integer.MIN_VALUE) {
      paramInt1 = Math.min(paramInt1, this.mMinWidth);
    } else {
      paramInt1 = this.mMinWidth;
    } 
    if (i != 1073741824 && this.mMinWidth > 0 && j < paramInt1)
      super.onMeasure(View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824), paramInt2); 
    if (!bool && this.mIcon != null)
      super.setPadding((getMeasuredWidth() - this.mIcon.getBounds().width()) / 2, getPaddingTop(), getPaddingRight(), getPaddingBottom()); 
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) { super.onRestoreInstanceState(null); }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (this.mItemData.hasSubMenu()) {
      ForwardingListener forwardingListener = this.mForwardingListener;
      if (forwardingListener != null && forwardingListener.onTouch(this, paramMotionEvent))
        return true; 
    } 
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public boolean prefersCondensedTitle() { return true; }
  
  public void setCheckable(boolean paramBoolean) {}
  
  public void setChecked(boolean paramBoolean) {}
  
  public void setExpandedFormat(boolean paramBoolean) {
    if (this.mExpandedFormat != paramBoolean) {
      this.mExpandedFormat = paramBoolean;
      MenuItemImpl menuItemImpl = this.mItemData;
      if (menuItemImpl != null)
        menuItemImpl.actionFormatChanged(); 
    } 
  }
  
  public void setIcon(Drawable paramDrawable) {
    this.mIcon = paramDrawable;
    if (paramDrawable != null) {
      int m = paramDrawable.getIntrinsicWidth();
      int k = paramDrawable.getIntrinsicHeight();
      int n = this.mMaxIconSize;
      int i = m;
      int j = k;
      if (m > n) {
        float f = n / m;
        i = this.mMaxIconSize;
        j = (int)(k * f);
      } 
      n = this.mMaxIconSize;
      m = i;
      k = j;
      if (j > n) {
        float f = n / j;
        k = this.mMaxIconSize;
        m = (int)(i * f);
      } 
      paramDrawable.setBounds(0, 0, m, k);
    } 
    setCompoundDrawables(paramDrawable, null, null, null);
    updateTextButtonVisibility();
  }
  
  public void setItemInvoker(MenuBuilder.ItemInvoker paramItemInvoker) { this.mItemInvoker = paramItemInvoker; }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mSavedPaddingLeft = paramInt1;
    super.setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setPopupCallback(PopupCallback paramPopupCallback) { this.mPopupCallback = paramPopupCallback; }
  
  public void setShortcut(boolean paramBoolean, char paramChar) {}
  
  public void setTitle(CharSequence paramCharSequence) {
    this.mTitle = paramCharSequence;
    updateTextButtonVisibility();
  }
  
  public boolean showsIcon() { return true; }
  
  private class ActionMenuItemForwardingListener extends ForwardingListener {
    public ActionMenuItemForwardingListener() { super(ActionMenuItemView.this); }
    
    public ShowableListMenu getPopup() { return (ActionMenuItemView.this.mPopupCallback != null) ? ActionMenuItemView.this.mPopupCallback.getPopup() : null; }
    
    protected boolean onForwardingStarted() {
      MenuBuilder.ItemInvoker itemInvoker = ActionMenuItemView.this.mItemInvoker;
      byte b = 0;
      if (itemInvoker != null && ActionMenuItemView.this.mItemInvoker.invokeItem(ActionMenuItemView.this.mItemData)) {
        ShowableListMenu showableListMenu = getPopup();
        int i = b;
        if (showableListMenu != null) {
          i = b;
          if (showableListMenu.isShowing())
            i = 1; 
        } 
        return i;
      } 
      return false;
    }
  }
  
  public static abstract class PopupCallback {
    public abstract ShowableListMenu getPopup();
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\view\menu\ActionMenuItemView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */