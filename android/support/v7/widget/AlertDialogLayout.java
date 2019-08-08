package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class AlertDialogLayout extends LinearLayoutCompat {
  public AlertDialogLayout(@Nullable Context paramContext) { super(paramContext); }
  
  public AlertDialogLayout(@Nullable Context paramContext, @Nullable AttributeSet paramAttributeSet) { super(paramContext, paramAttributeSet); }
  
  private void forceUniformWidth(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)view.getLayoutParams();
        if (layoutParams.width == -1) {
          int j = layoutParams.height;
          layoutParams.height = view.getMeasuredHeight();
          measureChildWithMargins(view, i, 0, paramInt2, 0);
          layoutParams.height = j;
        } 
      } 
    } 
  }
  
  private static int resolveMinimumHeight(View paramView) {
    int i = ViewCompat.getMinimumHeight(paramView);
    if (i > 0)
      return i; 
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      if (viewGroup.getChildCount() == 1)
        return resolveMinimumHeight(viewGroup.getChildAt(0)); 
    } 
    return 0;
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) { paramView.layout(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4); }
  
  private boolean tryOnMeasure(int paramInt1, int paramInt2) {
    View view3 = null;
    View view1 = null;
    View view2 = null;
    int i5 = getChildCount();
    int i;
    for (i = 0; i < i5; i++) {
      View view = getChildAt(i);
      if (view.getVisibility() != 8) {
        int i8 = view.getId();
        if (i8 == R.id.topPanel) {
          view3 = view;
        } else if (i8 == R.id.buttonPanel) {
          view1 = view;
        } else if (i8 == R.id.contentPanel || i8 == R.id.customPanel) {
          if (view2 != null)
            return false; 
          view2 = view;
        } else {
          return false;
        } 
      } 
    } 
    int i7 = View.MeasureSpec.getMode(paramInt2);
    int i1 = View.MeasureSpec.getSize(paramInt2);
    int i6 = View.MeasureSpec.getMode(paramInt1);
    int m = 0;
    i = getPaddingTop() + getPaddingBottom();
    int n = i;
    if (view3 != null) {
      view3.measure(paramInt1, 0);
      n = i + view3.getMeasuredHeight();
      m = View.combineMeasuredStates(0, view3.getMeasuredState());
    } 
    i = 0;
    int i2 = 0;
    int j = m;
    int k = n;
    if (view1 != null) {
      view1.measure(paramInt1, 0);
      i = resolveMinimumHeight(view1);
      i2 = view1.getMeasuredHeight() - i;
      k = n + i;
      j = View.combineMeasuredStates(m, view1.getMeasuredState());
    } 
    int i3 = 0;
    if (view2 != null) {
      if (i7 == 0) {
        m = 0;
      } else {
        m = View.MeasureSpec.makeMeasureSpec(Math.max(0, i1 - k), i7);
      } 
      view2.measure(paramInt1, m);
      i3 = view2.getMeasuredHeight();
      k += i3;
      j = View.combineMeasuredStates(j, view2.getMeasuredState());
    } 
    int i4 = i1 - k;
    m = i4;
    n = j;
    i1 = k;
    if (view1 != null) {
      i1 = Math.min(i4, i2);
      m = i4;
      n = i;
      if (i1 > 0) {
        m = i4 - i1;
        n = i + i1;
      } 
      view1.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(n, 1073741824));
      i1 = k - i + view1.getMeasuredHeight();
      n = View.combineMeasuredStates(j, view1.getMeasuredState());
    } 
    k = m;
    j = n;
    i = i1;
    if (view2 != null) {
      k = m;
      j = n;
      i = i1;
      if (m > 0) {
        view2.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(i3 + m, i7));
        i = i1 - i3 + view2.getMeasuredHeight();
        j = View.combineMeasuredStates(n, view2.getMeasuredState());
        k = m - m;
      } 
    } 
    m = 0;
    k = 0;
    while (k < i5) {
      View view = getChildAt(k);
      n = m;
      if (view.getVisibility() != 8)
        n = Math.max(m, view.getMeasuredWidth()); 
      k++;
      m = n;
    } 
    setMeasuredDimension(View.resolveSizeAndState(m + getPaddingLeft() + getPaddingRight(), paramInt1, j), View.resolveSizeAndState(i, paramInt2, 0));
    if (i6 != 1073741824)
      forceUniformWidth(i5, paramInt2); 
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getPaddingLeft();
    int j = paramInt3 - paramInt1;
    int k = getPaddingRight();
    int m = getPaddingRight();
    paramInt1 = getMeasuredHeight();
    int n = getChildCount();
    int i1 = getGravity();
    paramInt3 = i1 & 0x70;
    if (paramInt3 != 16) {
      if (paramInt3 != 80) {
        paramInt1 = getPaddingTop();
      } else {
        paramInt1 = getPaddingTop() + paramInt4 - paramInt2 - paramInt1;
      } 
    } else {
      paramInt1 = getPaddingTop() + (paramInt4 - paramInt2 - paramInt1) / 2;
    } 
    Drawable drawable = getDividerDrawable();
    if (drawable == null) {
      paramInt3 = 0;
    } else {
      paramInt3 = drawable.getIntrinsicHeight();
    } 
    paramInt4 = 0;
    while (true) {
      AlertDialogLayout alertDialogLayout = this;
      if (paramInt4 < n) {
        View view = alertDialogLayout.getChildAt(paramInt4);
        if (view != null && view.getVisibility() != 8) {
          int i3 = view.getMeasuredWidth();
          int i4 = view.getMeasuredHeight();
          LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)view.getLayoutParams();
          paramInt2 = layoutParams.gravity;
          if (paramInt2 < 0)
            paramInt2 = i1 & 0x800007; 
          paramInt2 = GravityCompat.getAbsoluteGravity(paramInt2, ViewCompat.getLayoutDirection(this)) & 0x7;
          if (paramInt2 != 1) {
            if (paramInt2 != 5) {
              paramInt2 = layoutParams.leftMargin + i;
            } else {
              paramInt2 = j - k - i3 - layoutParams.rightMargin;
            } 
          } else {
            paramInt2 = (j - i - m - i3) / 2 + i + layoutParams.leftMargin - layoutParams.rightMargin;
          } 
          int i2 = paramInt1;
          if (alertDialogLayout.hasDividerBeforeChildAt(paramInt4))
            i2 = paramInt1 + paramInt3; 
          paramInt1 = i2 + layoutParams.topMargin;
          setChildFrame(view, paramInt2, paramInt1, i3, i4);
          paramInt1 += i4 + layoutParams.bottomMargin;
        } 
        paramInt4++;
        continue;
      } 
      break;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (!tryOnMeasure(paramInt1, paramInt2))
      super.onMeasure(paramInt1, paramInt2); 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\AlertDialogLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */