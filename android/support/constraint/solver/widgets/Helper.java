package android.support.constraint.solver.widgets;

import java.util.Arrays;

public class Helper extends ConstraintWidget {
  protected ConstraintWidget[] mWidgets = new ConstraintWidget[4];
  
  protected int mWidgetsCount = 0;
  
  public void add(ConstraintWidget paramConstraintWidget) {
    int i = this.mWidgetsCount;
    ConstraintWidget[] arrayOfConstraintWidget = this.mWidgets;
    if (i + 1 > arrayOfConstraintWidget.length)
      this.mWidgets = (ConstraintWidget[])Arrays.copyOf(arrayOfConstraintWidget, arrayOfConstraintWidget.length * 2); 
    arrayOfConstraintWidget = this.mWidgets;
    i = this.mWidgetsCount;
    arrayOfConstraintWidget[i] = paramConstraintWidget;
    this.mWidgetsCount = i + 1;
  }
  
  public void removeAllIds() { this.mWidgetsCount = 0; }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\constraint\solver\widgets\Helper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */