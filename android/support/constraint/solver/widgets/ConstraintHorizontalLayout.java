package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;

public class ConstraintHorizontalLayout extends ConstraintWidgetContainer {
  private ContentAlignment mAlignment = ContentAlignment.MIDDLE;
  
  public ConstraintHorizontalLayout() {}
  
  public ConstraintHorizontalLayout(int paramInt1, int paramInt2) { super(paramInt1, paramInt2); }
  
  public ConstraintHorizontalLayout(int paramInt1, int paramInt2, int paramInt3, int paramInt4) { super(paramInt1, paramInt2, paramInt3, paramInt4); }
  
  public void addToSolver(LinearSystem paramLinearSystem) {
    if (this.mChildren.size() != 0) {
      ConstraintWidget constraintWidget = this;
      byte b = 0;
      int i = this.mChildren.size();
      while (b < i) {
        ConstraintWidget constraintWidget1 = (ConstraintWidget)this.mChildren.get(b);
        if (constraintWidget != this) {
          constraintWidget1.connect(ConstraintAnchor.Type.LEFT, constraintWidget, ConstraintAnchor.Type.RIGHT);
          constraintWidget.connect(ConstraintAnchor.Type.RIGHT, constraintWidget1, ConstraintAnchor.Type.LEFT);
        } else {
          ConstraintAnchor.Strength strength = ConstraintAnchor.Strength.STRONG;
          if (this.mAlignment == ContentAlignment.END)
            strength = ConstraintAnchor.Strength.WEAK; 
          constraintWidget1.connect(ConstraintAnchor.Type.LEFT, constraintWidget, ConstraintAnchor.Type.LEFT, 0, strength);
        } 
        constraintWidget1.connect(ConstraintAnchor.Type.TOP, this, ConstraintAnchor.Type.TOP);
        constraintWidget1.connect(ConstraintAnchor.Type.BOTTOM, this, ConstraintAnchor.Type.BOTTOM);
        constraintWidget = constraintWidget1;
        b++;
      } 
      if (constraintWidget != this) {
        ConstraintAnchor.Strength strength = ConstraintAnchor.Strength.STRONG;
        if (this.mAlignment == ContentAlignment.BEGIN)
          strength = ConstraintAnchor.Strength.WEAK; 
        constraintWidget.connect(ConstraintAnchor.Type.RIGHT, this, ConstraintAnchor.Type.RIGHT, 0, strength);
      } 
    } 
    super.addToSolver(paramLinearSystem);
  }
  
  public enum ContentAlignment {
    BEGIN, BOTTOM, END, LEFT, MIDDLE, RIGHT, TOP, VERTICAL_MIDDLE;
    
    static  {
      END = new ContentAlignment("END", 2);
      TOP = new ContentAlignment("TOP", 3);
      VERTICAL_MIDDLE = new ContentAlignment("VERTICAL_MIDDLE", 4);
      BOTTOM = new ContentAlignment("BOTTOM", 5);
      LEFT = new ContentAlignment("LEFT", 6);
      RIGHT = new ContentAlignment("RIGHT", 7);
      $VALUES = new ContentAlignment[] { BEGIN, MIDDLE, END, TOP, VERTICAL_MIDDLE, BOTTOM, LEFT, RIGHT };
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\constraint\solver\widgets\ConstraintHorizontalLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */