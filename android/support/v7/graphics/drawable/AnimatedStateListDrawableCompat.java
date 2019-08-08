package android.support.v7.graphics.drawable;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedStateListDrawableCompat extends StateListDrawable {
  private static final String ELEMENT_ITEM = "item";
  
  private static final String ELEMENT_TRANSITION = "transition";
  
  private static final String ITEM_MISSING_DRAWABLE_ERROR = ": <item> tag requires a 'drawable' attribute or child tag defining a drawable";
  
  private static final String LOGTAG = AnimatedStateListDrawableCompat.class.getSimpleName();
  
  private static final String TRANSITION_MISSING_DRAWABLE_ERROR = ": <transition> tag requires a 'drawable' attribute or child tag defining a drawable";
  
  private static final String TRANSITION_MISSING_FROM_TO_ID = ": <transition> tag requires 'fromId' & 'toId' attributes";
  
  private boolean mMutated;
  
  private AnimatedStateListState mState;
  
  private Transition mTransition;
  
  private int mTransitionFromIndex = -1;
  
  private int mTransitionToIndex = -1;
  
  public AnimatedStateListDrawableCompat() { this(null, null); }
  
  AnimatedStateListDrawableCompat(@Nullable AnimatedStateListState paramAnimatedStateListState, @Nullable Resources paramResources) {
    super(null);
    setConstantState(new AnimatedStateListState(paramAnimatedStateListState, this, paramResources));
    onStateChange(getState());
    jumpToCurrentState();
  }
  
  @Nullable
  public static AnimatedStateListDrawableCompat create(@NonNull Context paramContext, @DrawableRes int paramInt, @Nullable Resources.Theme paramTheme) {
    try {
      Resources resources = paramContext.getResources();
      XmlResourceParser xmlResourceParser = resources.getXml(paramInt);
      AttributeSet attributeSet = Xml.asAttributeSet(xmlResourceParser);
      while (true) {
        paramInt = xmlResourceParser.next();
        if (paramInt != 2 && paramInt != 1)
          continue; 
        break;
      } 
      if (paramInt == 2)
        return createFromXmlInner(paramContext, resources, xmlResourceParser, attributeSet, paramTheme); 
      throw new XmlPullParserException("No start tag found");
    } catch (XmlPullParserException paramContext) {
      Log.e(LOGTAG, "parser error", paramContext);
    } catch (IOException paramContext) {
      Log.e(LOGTAG, "parser error", paramContext);
    } 
    return null;
  }
  
  public static AnimatedStateListDrawableCompat createFromXmlInner(@NonNull Context paramContext, @NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme) throws IOException, XmlPullParserException {
    AnimatedStateListDrawableCompat animatedStateListDrawableCompat = paramXmlPullParser.getName();
    if (animatedStateListDrawableCompat.equals("animated-selector")) {
      animatedStateListDrawableCompat = new AnimatedStateListDrawableCompat();
      animatedStateListDrawableCompat.inflate(paramContext, paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
      return animatedStateListDrawableCompat;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramXmlPullParser.getPositionDescription());
    stringBuilder.append(": invalid animated-selector tag ");
    stringBuilder.append(animatedStateListDrawableCompat);
    throw new XmlPullParserException(stringBuilder.toString());
  }
  
  private void inflateChildElements(@NonNull Context paramContext, @NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    int i = paramXmlPullParser.getDepth() + 1;
    while (true) {
      int j = paramXmlPullParser.next();
      if (j != 1) {
        int k = paramXmlPullParser.getDepth();
        if (k >= i || j != 3) {
          if (j != 2 || k > i)
            continue; 
          if (paramXmlPullParser.getName().equals("item")) {
            parseItem(paramContext, paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
            continue;
          } 
          if (paramXmlPullParser.getName().equals("transition"))
            parseTransition(paramContext, paramResources, paramXmlPullParser, paramAttributeSet, paramTheme); 
          continue;
        } 
      } 
      break;
    } 
  }
  
  private void init() { onStateChange(getState()); }
  
  private int parseItem(@NonNull Context paramContext, @NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimatedStateListDrawableItem);
    int i = typedArray.getResourceId(R.styleable.AnimatedStateListDrawableItem_android_id, 0);
    Drawable drawable = null;
    int j = typedArray.getResourceId(R.styleable.AnimatedStateListDrawableItem_android_drawable, -1);
    if (j > 0)
      drawable = AppCompatResources.getDrawable(paramContext, j); 
    typedArray.recycle();
    int[] arrayOfInt = extractStateSet(paramAttributeSet);
    StringBuilder stringBuilder = drawable;
    if (drawable == null) {
      while (true) {
        j = paramXmlPullParser.next();
        if (j == 4)
          continue; 
        break;
      } 
      if (j == 2) {
        if (paramXmlPullParser.getName().equals("vector")) {
          VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.createFromXmlInner(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
        } else if (Build.VERSION.SDK_INT >= 21) {
          stringBuilder = Drawable.createFromXmlInner(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
        } else {
          stringBuilder = Drawable.createFromXmlInner(paramResources, paramXmlPullParser, paramAttributeSet);
        } 
      } else {
        stringBuilder = new StringBuilder();
        stringBuilder.append(paramXmlPullParser.getPositionDescription());
        stringBuilder.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
        throw new XmlPullParserException(stringBuilder.toString());
      } 
    } 
    if (stringBuilder != null)
      return this.mState.addStateSet(arrayOfInt, stringBuilder, i); 
    stringBuilder = new StringBuilder();
    stringBuilder.append(paramXmlPullParser.getPositionDescription());
    stringBuilder.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
    throw new XmlPullParserException(stringBuilder.toString());
  }
  
  private int parseTransition(@NonNull Context paramContext, @NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimatedStateListDrawableTransition);
    int i = typedArray.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_fromId, -1);
    int j = typedArray.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_toId, -1);
    Drawable drawable2 = null;
    int k = typedArray.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_drawable, -1);
    if (k > 0)
      drawable2 = AppCompatResources.getDrawable(paramContext, k); 
    boolean bool = typedArray.getBoolean(R.styleable.AnimatedStateListDrawableTransition_android_reversible, false);
    typedArray.recycle();
    Drawable drawable1 = drawable2;
    if (drawable2 == null) {
      while (true) {
        k = paramXmlPullParser.next();
        if (k == 4)
          continue; 
        break;
      } 
      if (k == 2) {
        if (paramXmlPullParser.getName().equals("animated-vector")) {
          AnimatedVectorDrawableCompat animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.createFromXmlInner(paramContext, paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
        } else if (Build.VERSION.SDK_INT >= 21) {
          drawable1 = Drawable.createFromXmlInner(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
        } else {
          drawable1 = Drawable.createFromXmlInner(paramResources, paramXmlPullParser, paramAttributeSet);
        } 
      } else {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(paramXmlPullParser.getPositionDescription());
        stringBuilder1.append(": <transition> tag requires a 'drawable' attribute or child tag defining a drawable");
        throw new XmlPullParserException(stringBuilder1.toString());
      } 
    } 
    if (drawable1 != null) {
      if (i != -1 && j != -1)
        return this.mState.addTransition(i, j, drawable1, bool); 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(paramXmlPullParser.getPositionDescription());
      stringBuilder1.append(": <transition> tag requires 'fromId' & 'toId' attributes");
      throw new XmlPullParserException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramXmlPullParser.getPositionDescription());
    stringBuilder.append(": <transition> tag requires a 'drawable' attribute or child tag defining a drawable");
    throw new XmlPullParserException(stringBuilder.toString());
  }
  
  private boolean selectTransition(int paramInt) {
    int i;
    Transition transition = this.mTransition;
    if (transition != null) {
      if (paramInt == this.mTransitionToIndex)
        return true; 
      if (paramInt == this.mTransitionFromIndex && transition.canReverse()) {
        transition.reverse();
        this.mTransitionToIndex = this.mTransitionFromIndex;
        this.mTransitionFromIndex = paramInt;
        return true;
      } 
      i = this.mTransitionToIndex;
      transition.stop();
    } else {
      i = getCurrentIndex();
    } 
    this.mTransition = null;
    this.mTransitionFromIndex = -1;
    this.mTransitionToIndex = -1;
    AnimatedStateListState animatedStateListState = this.mState;
    int j = animatedStateListState.getKeyframeIdAt(i);
    int k = animatedStateListState.getKeyframeIdAt(paramInt);
    if (k != 0) {
      AnimatableTransition animatableTransition;
      if (j == 0)
        return false; 
      int m = animatedStateListState.indexOfTransition(j, k);
      if (m < 0)
        return false; 
      boolean bool = animatedStateListState.transitionHasReversibleFlag(j, k);
      selectDrawable(m);
      Drawable drawable = getCurrent();
      if (drawable instanceof AnimationDrawable) {
        boolean bool1 = animatedStateListState.isTransitionReversed(j, k);
        animatableTransition = new AnimationDrawableTransition((AnimationDrawable)drawable, bool1, bool);
      } else if (drawable instanceof AnimatedVectorDrawableCompat) {
        animatableTransition = new AnimatedVectorDrawableTransition((AnimatedVectorDrawableCompat)drawable);
      } else {
        if (drawable instanceof Animatable) {
          animatableTransition = new AnimatableTransition((Animatable)drawable);
          animatableTransition.start();
          this.mTransition = animatableTransition;
          this.mTransitionFromIndex = i;
          this.mTransitionToIndex = paramInt;
          return true;
        } 
        return false;
      } 
      animatableTransition.start();
      this.mTransition = animatableTransition;
      this.mTransitionFromIndex = i;
      this.mTransitionToIndex = paramInt;
      return true;
    } 
    return false;
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray) {
    AnimatedStateListState animatedStateListState = this.mState;
    if (Build.VERSION.SDK_INT >= 21)
      animatedStateListState.mChangingConfigurations |= paramTypedArray.getChangingConfigurations(); 
    animatedStateListState.setVariablePadding(paramTypedArray.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_variablePadding, animatedStateListState.mVariablePadding));
    animatedStateListState.setConstantSize(paramTypedArray.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_constantSize, animatedStateListState.mConstantSize));
    animatedStateListState.setEnterFadeDuration(paramTypedArray.getInt(R.styleable.AnimatedStateListDrawableCompat_android_enterFadeDuration, animatedStateListState.mEnterFadeDuration));
    animatedStateListState.setExitFadeDuration(paramTypedArray.getInt(R.styleable.AnimatedStateListDrawableCompat_android_exitFadeDuration, animatedStateListState.mExitFadeDuration));
    setDither(paramTypedArray.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_dither, animatedStateListState.mDither));
  }
  
  public void addState(@NonNull int[] paramArrayOfInt, @NonNull Drawable paramDrawable, int paramInt) {
    if (paramDrawable != null) {
      this.mState.addStateSet(paramArrayOfInt, paramDrawable, paramInt);
      onStateChange(getState());
      return;
    } 
    throw new IllegalArgumentException("Drawable must not be null");
  }
  
  public <T extends Drawable & Animatable> void addTransition(int paramInt1, int paramInt2, @NonNull T paramT, boolean paramBoolean) {
    if (paramT != null) {
      this.mState.addTransition(paramInt1, paramInt2, paramT, paramBoolean);
      return;
    } 
    throw new IllegalArgumentException("Transition drawable must not be null");
  }
  
  void clearMutated() {
    super.clearMutated();
    this.mMutated = false;
  }
  
  AnimatedStateListState cloneConstantState() { return new AnimatedStateListState(this.mState, this, null); }
  
  public void inflate(@NonNull Context paramContext, @NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimatedStateListDrawableCompat);
    setVisible(typedArray.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_visible, true), true);
    updateStateFromTypedArray(typedArray);
    updateDensity(paramResources);
    typedArray.recycle();
    inflateChildElements(paramContext, paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    init();
  }
  
  public boolean isStateful() { return true; }
  
  public void jumpToCurrentState() {
    super.jumpToCurrentState();
    Transition transition = this.mTransition;
    if (transition != null) {
      transition.stop();
      this.mTransition = null;
      selectDrawable(this.mTransitionToIndex);
      this.mTransitionToIndex = -1;
      this.mTransitionFromIndex = -1;
    } 
  }
  
  public Drawable mutate() {
    if (!this.mMutated && super.mutate() == this) {
      this.mState.mutate();
      this.mMutated = true;
    } 
    return this;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt) {
    boolean bool1;
    int i = this.mState.indexOfKeyframe(paramArrayOfInt);
    if (i != getCurrentIndex() && (selectTransition(i) || selectDrawable(i))) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    Drawable drawable = getCurrent();
    boolean bool2 = bool1;
    if (drawable != null)
      bool2 = bool1 | drawable.setState(paramArrayOfInt); 
    return bool2;
  }
  
  protected void setConstantState(@NonNull DrawableContainer.DrawableContainerState paramDrawableContainerState) {
    super.setConstantState(paramDrawableContainerState);
    if (paramDrawableContainerState instanceof AnimatedStateListState)
      this.mState = (AnimatedStateListState)paramDrawableContainerState; 
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2) {
    boolean bool = super.setVisible(paramBoolean1, paramBoolean2);
    if (this.mTransition != null && (bool || paramBoolean2)) {
      if (paramBoolean1) {
        this.mTransition.start();
        return bool;
      } 
      jumpToCurrentState();
    } 
    return bool;
  }
  
  private static class AnimatableTransition extends Transition {
    private final Animatable mA;
    
    AnimatableTransition(Animatable param1Animatable) {
      super(null);
      this.mA = param1Animatable;
    }
    
    public void start() { this.mA.start(); }
    
    public void stop() { this.mA.stop(); }
  }
  
  static class AnimatedStateListState extends StateListDrawable.StateListState {
    private static final long REVERSED_BIT = 4294967296L;
    
    private static final long REVERSIBLE_FLAG_BIT = 8589934592L;
    
    SparseArrayCompat<Integer> mStateIds;
    
    LongSparseArray<Long> mTransitions;
    
    AnimatedStateListState(@Nullable AnimatedStateListState param1AnimatedStateListState, @NonNull AnimatedStateListDrawableCompat param1AnimatedStateListDrawableCompat, @Nullable Resources param1Resources) {
      super(param1AnimatedStateListState, param1AnimatedStateListDrawableCompat, param1Resources);
      if (param1AnimatedStateListState != null) {
        this.mTransitions = param1AnimatedStateListState.mTransitions;
        this.mStateIds = param1AnimatedStateListState.mStateIds;
        return;
      } 
      this.mTransitions = new LongSparseArray();
      this.mStateIds = new SparseArrayCompat();
    }
    
    private static long generateTransitionKey(int param1Int1, int param1Int2) { return param1Int1 << 32 | param1Int2; }
    
    int addStateSet(@NonNull int[] param1ArrayOfInt, @NonNull Drawable param1Drawable, int param1Int) {
      int i = addStateSet(param1ArrayOfInt, param1Drawable);
      this.mStateIds.put(i, Integer.valueOf(param1Int));
      return i;
    }
    
    int addTransition(int param1Int1, int param1Int2, @NonNull Drawable param1Drawable, boolean param1Boolean) {
      int i = addChild(param1Drawable);
      long l2 = generateTransitionKey(param1Int1, param1Int2);
      long l1 = 0L;
      if (param1Boolean)
        l1 = 8589934592L; 
      this.mTransitions.append(l2, Long.valueOf(i | l1));
      if (param1Boolean) {
        l2 = generateTransitionKey(param1Int2, param1Int1);
        this.mTransitions.append(l2, Long.valueOf(i | 0x100000000L | l1));
        return i;
      } 
      return i;
    }
    
    int getKeyframeIdAt(int param1Int) { return (param1Int < 0) ? 0 : ((Integer)this.mStateIds.get(param1Int, Integer.valueOf(0))).intValue(); }
    
    int indexOfKeyframe(@NonNull int[] param1ArrayOfInt) {
      int i = indexOfStateSet(param1ArrayOfInt);
      return (i >= 0) ? i : indexOfStateSet(StateSet.WILD_CARD);
    }
    
    int indexOfTransition(int param1Int1, int param1Int2) {
      long l = generateTransitionKey(param1Int1, param1Int2);
      return (int)((Long)this.mTransitions.get(l, Long.valueOf(-1L))).longValue();
    }
    
    boolean isTransitionReversed(int param1Int1, int param1Int2) {
      long l = generateTransitionKey(param1Int1, param1Int2);
      return ((((Long)this.mTransitions.get(l, Long.valueOf(-1L))).longValue() & 0x100000000L) != 0L);
    }
    
    void mutate() {
      this.mTransitions = this.mTransitions.clone();
      this.mStateIds = this.mStateIds.clone();
    }
    
    @NonNull
    public Drawable newDrawable() { return new AnimatedStateListDrawableCompat(this, null); }
    
    @NonNull
    public Drawable newDrawable(Resources param1Resources) { return new AnimatedStateListDrawableCompat(this, param1Resources); }
    
    boolean transitionHasReversibleFlag(int param1Int1, int param1Int2) {
      long l = generateTransitionKey(param1Int1, param1Int2);
      return ((((Long)this.mTransitions.get(l, Long.valueOf(-1L))).longValue() & 0x200000000L) != 0L);
    }
  }
  
  private static class AnimatedVectorDrawableTransition extends Transition {
    private final AnimatedVectorDrawableCompat mAvd;
    
    AnimatedVectorDrawableTransition(AnimatedVectorDrawableCompat param1AnimatedVectorDrawableCompat) {
      super(null);
      this.mAvd = param1AnimatedVectorDrawableCompat;
    }
    
    public void start() { this.mAvd.start(); }
    
    public void stop() { this.mAvd.stop(); }
  }
  
  private static class AnimationDrawableTransition extends Transition {
    private final ObjectAnimator mAnim;
    
    private final boolean mHasReversibleFlag;
    
    AnimationDrawableTransition(AnimationDrawable param1AnimationDrawable, boolean param1Boolean1, boolean param1Boolean2) {
      super(null);
      int i = param1AnimationDrawable.getNumberOfFrames();
      if (param1Boolean1) {
        bool = i - 1;
      } else {
        bool = false;
      } 
      if (param1Boolean1) {
        i = 0;
      } else {
        i--;
      } 
      AnimatedStateListDrawableCompat.FrameInterpolator frameInterpolator = new AnimatedStateListDrawableCompat.FrameInterpolator(param1AnimationDrawable, param1Boolean1);
      ObjectAnimator objectAnimator = ObjectAnimator.ofInt(param1AnimationDrawable, "currentIndex", new int[] { bool, i });
      if (Build.VERSION.SDK_INT >= 18)
        objectAnimator.setAutoCancel(true); 
      objectAnimator.setDuration(frameInterpolator.getTotalDuration());
      objectAnimator.setInterpolator(frameInterpolator);
      this.mHasReversibleFlag = param1Boolean2;
      this.mAnim = objectAnimator;
    }
    
    public boolean canReverse() { return this.mHasReversibleFlag; }
    
    public void reverse() { this.mAnim.reverse(); }
    
    public void start() { this.mAnim.start(); }
    
    public void stop() { this.mAnim.cancel(); }
  }
  
  private static class FrameInterpolator implements TimeInterpolator {
    private int[] mFrameTimes;
    
    private int mFrames;
    
    private int mTotalDuration;
    
    FrameInterpolator(AnimationDrawable param1AnimationDrawable, boolean param1Boolean) { updateFrames(param1AnimationDrawable, param1Boolean); }
    
    public float getInterpolation(float param1Float) {
      int i = (int)(this.mTotalDuration * param1Float + 0.5F);
      int j = this.mFrames;
      int[] arrayOfInt = this.mFrameTimes;
      byte b;
      for (b = 0; b < j && i >= arrayOfInt[b]; b++)
        i -= arrayOfInt[b]; 
      if (b < j) {
        param1Float = i / this.mTotalDuration;
      } else {
        param1Float = 0.0F;
      } 
      return b / j + param1Float;
    }
    
    int getTotalDuration() { return this.mTotalDuration; }
    
    int updateFrames(AnimationDrawable param1AnimationDrawable, boolean param1Boolean) {
      int k = param1AnimationDrawable.getNumberOfFrames();
      this.mFrames = k;
      int[] arrayOfInt = this.mFrameTimes;
      if (arrayOfInt == null || arrayOfInt.length < k)
        this.mFrameTimes = new int[k]; 
      arrayOfInt = this.mFrameTimes;
      int j = 0;
      for (int i = 0; i < k; i++) {
        if (param1Boolean) {
          m = k - i - 1;
        } else {
          m = i;
        } 
        int m = param1AnimationDrawable.getDuration(m);
        arrayOfInt[i] = m;
        j += m;
      } 
      this.mTotalDuration = j;
      return j;
    }
  }
  
  private static abstract class Transition {
    private Transition() {}
    
    public boolean canReverse() { return false; }
    
    public void reverse() {}
    
    public abstract void start();
    
    public abstract void stop();
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\graphics\drawable\AnimatedStateListDrawableCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */