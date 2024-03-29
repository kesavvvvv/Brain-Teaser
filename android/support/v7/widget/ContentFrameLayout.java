package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

@RestrictTo({RestrictTo.Scope.LIBRARY})
public class ContentFrameLayout extends FrameLayout {
  private OnAttachListener mAttachListener;
  
  private final Rect mDecorPadding = new Rect();
  
  private TypedValue mFixedHeightMajor;
  
  private TypedValue mFixedHeightMinor;
  
  private TypedValue mFixedWidthMajor;
  
  private TypedValue mFixedWidthMinor;
  
  private TypedValue mMinWidthMajor;
  
  private TypedValue mMinWidthMinor;
  
  public ContentFrameLayout(Context paramContext) { this(paramContext, null); }
  
  public ContentFrameLayout(Context paramContext, AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, 0); }
  
  public ContentFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) { super(paramContext, paramAttributeSet, paramInt); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void dispatchFitSystemWindows(Rect paramRect) { fitSystemWindows(paramRect); }
  
  public TypedValue getFixedHeightMajor() {
    if (this.mFixedHeightMajor == null)
      this.mFixedHeightMajor = new TypedValue(); 
    return this.mFixedHeightMajor;
  }
  
  public TypedValue getFixedHeightMinor() {
    if (this.mFixedHeightMinor == null)
      this.mFixedHeightMinor = new TypedValue(); 
    return this.mFixedHeightMinor;
  }
  
  public TypedValue getFixedWidthMajor() {
    if (this.mFixedWidthMajor == null)
      this.mFixedWidthMajor = new TypedValue(); 
    return this.mFixedWidthMajor;
  }
  
  public TypedValue getFixedWidthMinor() {
    if (this.mFixedWidthMinor == null)
      this.mFixedWidthMinor = new TypedValue(); 
    return this.mFixedWidthMinor;
  }
  
  public TypedValue getMinWidthMajor() {
    if (this.mMinWidthMajor == null)
      this.mMinWidthMajor = new TypedValue(); 
    return this.mMinWidthMajor;
  }
  
  public TypedValue getMinWidthMinor() {
    if (this.mMinWidthMinor == null)
      this.mMinWidthMinor = new TypedValue(); 
    return this.mMinWidthMinor;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    OnAttachListener onAttachListener = this.mAttachListener;
    if (onAttachListener != null)
      onAttachListener.onAttachedFromWindow(); 
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    OnAttachListener onAttachListener = this.mAttachListener;
    if (onAttachListener != null)
      onAttachListener.onDetachedFromWindow(); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int j;
    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    if (displayMetrics.widthPixels < displayMetrics.heightPixels) {
      j = 1;
    } else {
      j = 0;
    } 
    int n = View.MeasureSpec.getMode(paramInt1);
    int i1 = View.MeasureSpec.getMode(paramInt2);
    int m = 0;
    byte b = m;
    int k = paramInt1;
    if (n == Integer.MIN_VALUE) {
      TypedValue typedValue;
      if (j) {
        typedValue = this.mFixedWidthMinor;
      } else {
        typedValue = this.mFixedWidthMajor;
      } 
      b = m;
      k = paramInt1;
      if (typedValue != null) {
        b = m;
        k = paramInt1;
        if (typedValue.type != 0) {
          int i2 = 0;
          if (typedValue.type == 5) {
            i2 = (int)typedValue.getDimension(displayMetrics);
          } else if (typedValue.type == 6) {
            i2 = (int)typedValue.getFraction(displayMetrics.widthPixels, displayMetrics.widthPixels);
          } 
          b = m;
          k = paramInt1;
          if (i2 > 0) {
            k = View.MeasureSpec.makeMeasureSpec(Math.min(i2 - this.mDecorPadding.left + this.mDecorPadding.right, View.MeasureSpec.getSize(paramInt1)), 1073741824);
            b = 1;
          } 
        } 
      } 
    } 
    int i = paramInt2;
    if (i1 == Integer.MIN_VALUE) {
      TypedValue typedValue;
      if (j) {
        typedValue = this.mFixedHeightMajor;
      } else {
        typedValue = this.mFixedHeightMinor;
      } 
      i = paramInt2;
      if (typedValue != null) {
        i = paramInt2;
        if (typedValue.type != 0) {
          paramInt1 = 0;
          if (typedValue.type == 5) {
            paramInt1 = (int)typedValue.getDimension(displayMetrics);
          } else if (typedValue.type == 6) {
            paramInt1 = (int)typedValue.getFraction(displayMetrics.heightPixels, displayMetrics.heightPixels);
          } 
          i = paramInt2;
          if (paramInt1 > 0)
            i = View.MeasureSpec.makeMeasureSpec(Math.min(paramInt1 - this.mDecorPadding.top + this.mDecorPadding.bottom, View.MeasureSpec.getSize(paramInt2)), 1073741824); 
        } 
      } 
    } 
    super.onMeasure(k, i);
    i1 = getMeasuredWidth();
    k = 0;
    m = View.MeasureSpec.makeMeasureSpec(i1, 1073741824);
    paramInt2 = k;
    paramInt1 = m;
    if (b == 0) {
      paramInt2 = k;
      paramInt1 = m;
      if (n == Integer.MIN_VALUE) {
        TypedValue typedValue;
        if (j) {
          typedValue = this.mMinWidthMinor;
        } else {
          typedValue = this.mMinWidthMajor;
        } 
        paramInt2 = k;
        paramInt1 = m;
        if (typedValue != null) {
          paramInt2 = k;
          paramInt1 = m;
          if (typedValue.type != 0) {
            paramInt1 = 0;
            if (typedValue.type == 5) {
              paramInt1 = (int)typedValue.getDimension(displayMetrics);
            } else if (typedValue.type == 6) {
              paramInt1 = (int)typedValue.getFraction(displayMetrics.widthPixels, displayMetrics.widthPixels);
            } 
            j = paramInt1;
            if (paramInt1 > 0)
              j = paramInt1 - this.mDecorPadding.left + this.mDecorPadding.right; 
            paramInt2 = k;
            paramInt1 = m;
            if (i1 < j) {
              paramInt1 = View.MeasureSpec.makeMeasureSpec(j, 1073741824);
              paramInt2 = 1;
            } 
          } 
        } 
      } 
    } 
    if (paramInt2 != 0)
      super.onMeasure(paramInt1, i); 
  }
  
  public void setAttachListener(OnAttachListener paramOnAttachListener) { this.mAttachListener = paramOnAttachListener; }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setDecorPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mDecorPadding.set(paramInt1, paramInt2, paramInt3, paramInt4);
    if (ViewCompat.isLaidOut(this))
      requestLayout(); 
  }
  
  public static interface OnAttachListener {
    void onAttachedFromWindow();
    
    void onDetachedFromWindow();
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\ContentFrameLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */