package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.constraint.solver.widgets.Helper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.Arrays;

public abstract class ConstraintHelper extends View {
  protected int mCount;
  
  protected Helper mHelperWidget;
  
  protected int[] mIds = new int[32];
  
  private String mReferenceIds;
  
  protected boolean mUseViewMeasure = false;
  
  protected Context myContext;
  
  public ConstraintHelper(Context paramContext) {
    super(paramContext);
    this.myContext = paramContext;
    init(null);
  }
  
  public ConstraintHelper(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    this.myContext = paramContext;
    init(paramAttributeSet);
  }
  
  public ConstraintHelper(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    this.myContext = paramContext;
    init(paramAttributeSet);
  }
  
  private void addID(String paramString) {
    if (paramString == null)
      return; 
    if (this.myContext == null)
      return; 
    paramString = paramString.trim();
    int j = 0;
    try {
      int k = R.id.class.getField(paramString).getInt(null);
      j = k;
    } catch (Exception exception) {}
    int i = j;
    if (j == 0)
      i = this.myContext.getResources().getIdentifier(paramString, "id", this.myContext.getPackageName()); 
    j = i;
    if (i == 0) {
      j = i;
      if (isInEditMode()) {
        j = i;
        if (getParent() instanceof ConstraintLayout) {
          Object object = ((ConstraintLayout)getParent()).getDesignInformation(0, paramString);
          j = i;
          if (object != null) {
            j = i;
            if (object instanceof Integer)
              j = ((Integer)object).intValue(); 
          } 
        } 
      } 
    } 
    if (j != 0) {
      setTag(j, null);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Could not find id of \"");
    stringBuilder.append(paramString);
    stringBuilder.append("\"");
    Log.w("ConstraintHelper", stringBuilder.toString());
  }
  
  private void setIds(String paramString) {
    if (paramString == null)
      return; 
    for (int i = 0;; i = j + 1) {
      int j = paramString.indexOf(',', i);
      if (j == -1) {
        addID(paramString.substring(i));
        return;
      } 
      addID(paramString.substring(i, j));
    } 
  }
  
  public int[] getReferencedIds() { return Arrays.copyOf(this.mIds, this.mCount); }
  
  protected void init(AttributeSet paramAttributeSet) {
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_Layout_constraint_referenced_ids) {
          this.mReferenceIds = typedArray.getString(j);
          setIds(this.mReferenceIds);
        } 
      } 
    } 
  }
  
  public void onDraw(Canvas paramCanvas) {}
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mUseViewMeasure) {
      super.onMeasure(paramInt1, paramInt2);
      return;
    } 
    setMeasuredDimension(0, 0);
  }
  
  public void setReferencedIds(int[] paramArrayOfInt) {
    this.mCount = 0;
    for (byte b = 0; b < paramArrayOfInt.length; b++)
      setTag(paramArrayOfInt[b], null); 
  }
  
  public void setTag(int paramInt, Object paramObject) {
    int i = this.mCount;
    int[] arrayOfInt = this.mIds;
    if (i + 1 > arrayOfInt.length)
      this.mIds = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2); 
    arrayOfInt = this.mIds;
    i = this.mCount;
    arrayOfInt[i] = paramInt;
    this.mCount = i + 1;
  }
  
  public void updatePostLayout(ConstraintLayout paramConstraintLayout) {}
  
  public void updatePostMeasure(ConstraintLayout paramConstraintLayout) {}
  
  public void updatePreLayout(ConstraintLayout paramConstraintLayout) {
    if (isInEditMode())
      setIds(this.mReferenceIds); 
    Helper helper = this.mHelperWidget;
    if (helper == null)
      return; 
    helper.removeAllIds();
    for (byte b = 0; b < this.mCount; b++) {
      View view = paramConstraintLayout.getViewById(this.mIds[b]);
      if (view != null)
        this.mHelperWidget.add(paramConstraintLayout.getViewWidget(view)); 
    } 
  }
  
  public void validateParams() {
    if (this.mHelperWidget == null)
      return; 
    ViewGroup.LayoutParams layoutParams = getLayoutParams();
    if (layoutParams instanceof ConstraintLayout.LayoutParams)
      ((ConstraintLayout.LayoutParams)layoutParams).widget = this.mHelperWidget; 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\constraint\ConstraintHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */