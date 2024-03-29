package android.support.v7.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.StateSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class StateListDrawable extends DrawableContainer {
  private static final boolean DEBUG = false;
  
  private static final String TAG = "StateListDrawable";
  
  private boolean mMutated;
  
  private StateListState mStateListState;
  
  StateListDrawable() { this(null, null); }
  
  StateListDrawable(@Nullable StateListState paramStateListState) {
    if (paramStateListState != null)
      setConstantState(paramStateListState); 
  }
  
  StateListDrawable(StateListState paramStateListState, Resources paramResources) {
    setConstantState(new StateListState(paramStateListState, this, paramResources));
    onStateChange(getState());
  }
  
  private void inflateChildElements(Context paramContext, Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    StateListState stateListState = this.mStateListState;
    int i = paramXmlPullParser.getDepth() + 1;
    while (true) {
      int j = paramXmlPullParser.next();
      if (j != 1) {
        int k = paramXmlPullParser.getDepth();
        if (k >= i || j != 3) {
          if (j != 2 || k > i || !paramXmlPullParser.getName().equals("item"))
            continue; 
          TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.StateListDrawableItem);
          Drawable drawable1 = null;
          j = typedArray.getResourceId(R.styleable.StateListDrawableItem_android_drawable, -1);
          if (j > 0)
            drawable1 = AppCompatResources.getDrawable(paramContext, j); 
          typedArray.recycle();
          int[] arrayOfInt = extractStateSet(paramAttributeSet);
          Drawable drawable2 = drawable1;
          if (drawable1 == null) {
            while (true) {
              j = paramXmlPullParser.next();
              if (j == 4)
                continue; 
              break;
            } 
            if (j == 2) {
              if (Build.VERSION.SDK_INT >= 21) {
                drawable2 = Drawable.createFromXmlInner(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
              } else {
                drawable2 = Drawable.createFromXmlInner(paramResources, paramXmlPullParser, paramAttributeSet);
              } 
            } else {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(paramXmlPullParser.getPositionDescription());
              stringBuilder.append(": <item> tag requires a 'drawable' attribute or ");
              stringBuilder.append("child tag defining a drawable");
              throw new XmlPullParserException(stringBuilder.toString());
            } 
          } 
          stateListState.addStateSet(arrayOfInt, drawable2);
          continue;
        } 
        return;
      } 
      break;
    } 
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray) {
    StateListState stateListState = this.mStateListState;
    if (Build.VERSION.SDK_INT >= 21)
      stateListState.mChangingConfigurations |= paramTypedArray.getChangingConfigurations(); 
    stateListState.mVariablePadding = paramTypedArray.getBoolean(R.styleable.StateListDrawable_android_variablePadding, stateListState.mVariablePadding);
    stateListState.mConstantSize = paramTypedArray.getBoolean(R.styleable.StateListDrawable_android_constantSize, stateListState.mConstantSize);
    stateListState.mEnterFadeDuration = paramTypedArray.getInt(R.styleable.StateListDrawable_android_enterFadeDuration, stateListState.mEnterFadeDuration);
    stateListState.mExitFadeDuration = paramTypedArray.getInt(R.styleable.StateListDrawable_android_exitFadeDuration, stateListState.mExitFadeDuration);
    stateListState.mDither = paramTypedArray.getBoolean(R.styleable.StateListDrawable_android_dither, stateListState.mDither);
  }
  
  public void addState(int[] paramArrayOfInt, Drawable paramDrawable) {
    if (paramDrawable != null) {
      this.mStateListState.addStateSet(paramArrayOfInt, paramDrawable);
      onStateChange(getState());
    } 
  }
  
  @RequiresApi(21)
  public void applyTheme(@NonNull Resources.Theme paramTheme) {
    super.applyTheme(paramTheme);
    onStateChange(getState());
  }
  
  void clearMutated() {
    super.clearMutated();
    this.mMutated = false;
  }
  
  StateListState cloneConstantState() { return new StateListState(this.mStateListState, this, null); }
  
  int[] extractStateSet(AttributeSet paramAttributeSet) {
    byte b2 = 0;
    int i = paramAttributeSet.getAttributeCount();
    int[] arrayOfInt = new int[i];
    for (byte b1 = 0; b1 < i; b1++) {
      int j = paramAttributeSet.getAttributeNameResource(b1);
      if (j != 0 && j != 16842960 && j != 16843161) {
        if (!paramAttributeSet.getAttributeBooleanValue(b1, false))
          j = -j; 
        arrayOfInt[b2] = j;
        b2++;
      } 
    } 
    return StateSet.trimStateSet(arrayOfInt, b2);
  }
  
  int getStateCount() { return this.mStateListState.getChildCount(); }
  
  Drawable getStateDrawable(int paramInt) { return this.mStateListState.getChild(paramInt); }
  
  int getStateDrawableIndex(int[] paramArrayOfInt) { return this.mStateListState.indexOfStateSet(paramArrayOfInt); }
  
  StateListState getStateListState() { return this.mStateListState; }
  
  int[] getStateSet(int paramInt) { return this.mStateListState.mStateSets[paramInt]; }
  
  public void inflate(@NonNull Context paramContext, @NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.StateListDrawable);
    setVisible(typedArray.getBoolean(R.styleable.StateListDrawable_android_visible, true), true);
    updateStateFromTypedArray(typedArray);
    updateDensity(paramResources);
    typedArray.recycle();
    inflateChildElements(paramContext, paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    onStateChange(getState());
  }
  
  public boolean isStateful() { return true; }
  
  @NonNull
  public Drawable mutate() {
    if (!this.mMutated && super.mutate() == this) {
      this.mStateListState.mutate();
      this.mMutated = true;
    } 
    return this;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt) {
    boolean bool = super.onStateChange(paramArrayOfInt);
    int j = this.mStateListState.indexOfStateSet(paramArrayOfInt);
    int i = j;
    if (j < 0)
      i = this.mStateListState.indexOfStateSet(StateSet.WILD_CARD); 
    return (selectDrawable(i) || bool);
  }
  
  protected void setConstantState(@NonNull DrawableContainer.DrawableContainerState paramDrawableContainerState) {
    super.setConstantState(paramDrawableContainerState);
    if (paramDrawableContainerState instanceof StateListState)
      this.mStateListState = (StateListState)paramDrawableContainerState; 
  }
  
  static class StateListState extends DrawableContainer.DrawableContainerState {
    int[][] mStateSets;
    
    StateListState(StateListState param1StateListState, StateListDrawable param1StateListDrawable, Resources param1Resources) {
      super(param1StateListState, param1StateListDrawable, param1Resources);
      if (param1StateListState != null) {
        this.mStateSets = param1StateListState.mStateSets;
        return;
      } 
      this.mStateSets = new int[getCapacity()][];
    }
    
    int addStateSet(int[] param1ArrayOfInt, Drawable param1Drawable) {
      int i = addChild(param1Drawable);
      this.mStateSets[i] = param1ArrayOfInt;
      return i;
    }
    
    public void growArray(int param1Int1, int param1Int2) {
      super.growArray(param1Int1, param1Int2);
      int[][] arrayOfInt = new int[param1Int2][];
      System.arraycopy(this.mStateSets, 0, arrayOfInt, 0, param1Int1);
      this.mStateSets = arrayOfInt;
    }
    
    int indexOfStateSet(int[] param1ArrayOfInt) {
      int[][] arrayOfInt = this.mStateSets;
      int i = getChildCount();
      for (byte b = 0; b < i; b++) {
        if (StateSet.stateSetMatches(arrayOfInt[b], param1ArrayOfInt))
          return b; 
      } 
      return -1;
    }
    
    void mutate() {
      int[][] arrayOfInt1 = this.mStateSets;
      int[][] arrayOfInt2 = new int[arrayOfInt1.length][];
      for (int i = arrayOfInt1.length - 1; i >= 0; i--) {
        arrayOfInt1 = this.mStateSets;
        if (arrayOfInt1[i] != null) {
          int[] arrayOfInt = (int[])arrayOfInt1[i].clone();
        } else {
          arrayOfInt1 = null;
        } 
        arrayOfInt2[i] = arrayOfInt1;
      } 
      this.mStateSets = arrayOfInt2;
    }
    
    @NonNull
    public Drawable newDrawable() { return new StateListDrawable(this, null); }
    
    @NonNull
    public Drawable newDrawable(Resources param1Resources) { return new StateListDrawable(this, param1Resources); }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\graphics\drawable\StateListDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */