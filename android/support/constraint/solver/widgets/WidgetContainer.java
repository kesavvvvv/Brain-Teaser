package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import java.util.ArrayList;

public class WidgetContainer extends ConstraintWidget {
  protected ArrayList<ConstraintWidget> mChildren = new ArrayList();
  
  public WidgetContainer() {}
  
  public WidgetContainer(int paramInt1, int paramInt2) { super(paramInt1, paramInt2); }
  
  public WidgetContainer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) { super(paramInt1, paramInt2, paramInt3, paramInt4); }
  
  public static Rectangle getBounds(ArrayList<ConstraintWidget> paramArrayList) {
    Rectangle rectangle = new Rectangle();
    if (paramArrayList.size() == 0)
      return rectangle; 
    int m = Integer.MAX_VALUE;
    int j = 0;
    int k = Integer.MAX_VALUE;
    int i = 0;
    byte b = 0;
    int n = paramArrayList.size();
    while (b < n) {
      ConstraintWidget constraintWidget = (ConstraintWidget)paramArrayList.get(b);
      int i1 = m;
      if (constraintWidget.getX() < m)
        i1 = constraintWidget.getX(); 
      int i2 = k;
      if (constraintWidget.getY() < k)
        i2 = constraintWidget.getY(); 
      k = j;
      if (constraintWidget.getRight() > j)
        k = constraintWidget.getRight(); 
      int i3 = i;
      if (constraintWidget.getBottom() > i)
        i3 = constraintWidget.getBottom(); 
      b++;
      m = i1;
      j = k;
      k = i2;
      i = i3;
    } 
    rectangle.setBounds(m, k, j - m, i - k);
    return rectangle;
  }
  
  public void add(ConstraintWidget paramConstraintWidget) {
    this.mChildren.add(paramConstraintWidget);
    if (paramConstraintWidget.getParent() != null)
      ((WidgetContainer)paramConstraintWidget.getParent()).remove(paramConstraintWidget); 
    paramConstraintWidget.setParent(this);
  }
  
  public void add(ConstraintWidget... paramVarArgs) {
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++)
      add(paramVarArgs[b]); 
  }
  
  public ConstraintWidget findWidget(float paramFloat1, float paramFloat2) {
    ConstraintWidget constraintWidget2 = null;
    int i = getDrawX();
    int j = getDrawY();
    int k = getWidth();
    int m = getHeight();
    ConstraintWidget constraintWidget1 = constraintWidget2;
    if (paramFloat1 >= i) {
      constraintWidget1 = constraintWidget2;
      if (paramFloat1 <= (k + i)) {
        constraintWidget1 = constraintWidget2;
        if (paramFloat2 >= j) {
          constraintWidget1 = constraintWidget2;
          if (paramFloat2 <= (m + j))
            constraintWidget1 = this; 
        } 
      } 
    } 
    i = 0;
    j = this.mChildren.size();
    while (i < j) {
      constraintWidget2 = (ConstraintWidget)this.mChildren.get(i);
      if (constraintWidget2 instanceof WidgetContainer) {
        constraintWidget2 = ((WidgetContainer)constraintWidget2).findWidget(paramFloat1, paramFloat2);
        if (constraintWidget2 != null)
          ConstraintWidget constraintWidget = constraintWidget2; 
      } else {
        k = constraintWidget2.getDrawX();
        m = constraintWidget2.getDrawY();
        int n = constraintWidget2.getWidth();
        int i1 = constraintWidget2.getHeight();
        if (paramFloat1 >= k && paramFloat1 <= (n + k) && paramFloat2 >= m && paramFloat2 <= (i1 + m))
          constraintWidget1 = constraintWidget2; 
      } 
      i++;
    } 
    return constraintWidget1;
  }
  
  public ArrayList<ConstraintWidget> findWidgets(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ArrayList arrayList = new ArrayList();
    Rectangle rectangle = new Rectangle();
    rectangle.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    paramInt1 = 0;
    paramInt2 = this.mChildren.size();
    while (paramInt1 < paramInt2) {
      ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(paramInt1);
      Rectangle rectangle1 = new Rectangle();
      rectangle1.setBounds(constraintWidget.getDrawX(), constraintWidget.getDrawY(), constraintWidget.getWidth(), constraintWidget.getHeight());
      if (rectangle.intersects(rectangle1))
        arrayList.add(constraintWidget); 
      paramInt1++;
    } 
    return arrayList;
  }
  
  public ArrayList<ConstraintWidget> getChildren() { return this.mChildren; }
  
  public ConstraintWidgetContainer getRootConstraintContainer() {
    ConstraintWidget constraintWidget2 = getParent();
    ConstraintWidgetContainer constraintWidgetContainer = null;
    ConstraintWidget constraintWidget1 = constraintWidget2;
    if (this instanceof ConstraintWidgetContainer) {
      constraintWidgetContainer = (ConstraintWidgetContainer)this;
      constraintWidget1 = constraintWidget2;
    } 
    while (true) {
      ConstraintWidget constraintWidget = constraintWidget1;
      if (constraintWidget != null) {
        constraintWidget2 = constraintWidget.getParent();
        constraintWidget1 = constraintWidget2;
        if (constraintWidget instanceof ConstraintWidgetContainer) {
          constraintWidgetContainer = (ConstraintWidgetContainer)constraintWidget;
          constraintWidget1 = constraintWidget2;
        } 
        continue;
      } 
      break;
    } 
    return constraintWidgetContainer;
  }
  
  public void layout() {
    updateDrawPosition();
    ArrayList arrayList = this.mChildren;
    if (arrayList == null)
      return; 
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(b);
      if (constraintWidget instanceof WidgetContainer)
        ((WidgetContainer)constraintWidget).layout(); 
    } 
  }
  
  public void remove(ConstraintWidget paramConstraintWidget) {
    this.mChildren.remove(paramConstraintWidget);
    paramConstraintWidget.setParent(null);
  }
  
  public void removeAllChildren() { this.mChildren.clear(); }
  
  public void reset() {
    this.mChildren.clear();
    super.reset();
  }
  
  public void resetSolverVariables(Cache paramCache) {
    super.resetSolverVariables(paramCache);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++)
      ((ConstraintWidget)this.mChildren.get(b)).resetSolverVariables(paramCache); 
  }
  
  public void setOffset(int paramInt1, int paramInt2) {
    super.setOffset(paramInt1, paramInt2);
    paramInt2 = this.mChildren.size();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
      ((ConstraintWidget)this.mChildren.get(paramInt1)).setOffset(getRootX(), getRootY()); 
  }
  
  public void updateDrawPosition() {
    super.updateDrawPosition();
    ArrayList arrayList = this.mChildren;
    if (arrayList == null)
      return; 
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(b);
      constraintWidget.setOffset(getDrawX(), getDrawY());
      if (!(constraintWidget instanceof ConstraintWidgetContainer))
        constraintWidget.updateDrawPosition(); 
    } 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\constraint\solver\widgets\WidgetContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */