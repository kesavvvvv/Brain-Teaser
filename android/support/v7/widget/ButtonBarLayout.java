package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ButtonBarLayout extends LinearLayout {
  private static final int PEEK_BUTTON_DP = 16;
  
  private boolean mAllowStacking;
  
  private int mLastWidthSize = -1;
  
  private int mMinimumHeight = 0;
  
  public ButtonBarLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ButtonBarLayout);
    this.mAllowStacking = typedArray.getBoolean(R.styleable.ButtonBarLayout_allowStacking, true);
    typedArray.recycle();
  }
  
  private int getNextVisibleChildIndex(int paramInt) {
    int i = getChildCount();
    while (paramInt < i) {
      if (getChildAt(paramInt).getVisibility() == 0)
        return paramInt; 
      paramInt++;
    } 
    return -1;
  }
  
  private boolean isStacked() { return (getOrientation() == 1); }
  
  private void setStacked(boolean paramBoolean) { throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n"); }
  
  public int getMinimumHeight() { return Math.max(this.mMinimumHeight, super.getMinimumHeight()); }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int j = View.MeasureSpec.getSize(paramInt1);
    if (this.mAllowStacking) {
      if (j > this.mLastWidthSize && isStacked())
        setStacked(false); 
      this.mLastWidthSize = j;
    } 
    int i = 0;
    if (!isStacked() && View.MeasureSpec.getMode(paramInt1) == 1073741824) {
      j = View.MeasureSpec.makeMeasureSpec(j, -2147483648);
      i = 1;
    } else {
      j = paramInt1;
    } 
    super.onMeasure(j, paramInt2);
    byte b = i;
    if (this.mAllowStacking) {
      b = i;
      if (!isStacked()) {
        if ((0xFF000000 & getMeasuredWidthAndState()) == 16777216) {
          j = 1;
        } else {
          j = 0;
        } 
        b = i;
        if (j != 0) {
          setStacked(true);
          b = 1;
        } 
      } 
    } 
    if (b != 0)
      super.onMeasure(paramInt1, paramInt2); 
    paramInt1 = 0;
    i = getNextVisibleChildIndex(0);
    if (i >= 0) {
      View view = getChildAt(i);
      LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)view.getLayoutParams();
      paramInt2 = 0 + getPaddingTop() + view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
      if (isStacked()) {
        i = getNextVisibleChildIndex(i + 1);
        paramInt1 = paramInt2;
        if (i >= 0)
          paramInt1 = paramInt2 + getChildAt(i).getPaddingTop() + (int)((getResources().getDisplayMetrics()).density * 16.0F); 
      } else {
        paramInt1 = paramInt2 + getPaddingBottom();
      } 
    } 
    if (ViewCompat.getMinimumHeight(this) != paramInt1)
      setMinimumHeight(paramInt1); 
  }
  
  public void setAllowStacking(boolean paramBoolean) {
    if (this.mAllowStacking != paramBoolean) {
      this.mAllowStacking = paramBoolean;
      if (!this.mAllowStacking && getOrientation() == 1)
        setStacked(false); 
      requestLayout();
    } 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\ButtonBarLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */