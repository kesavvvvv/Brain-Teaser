package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedVectorDrawableCompat extends VectorDrawableCommon implements Animatable2Compat {
  private static final String ANIMATED_VECTOR = "animated-vector";
  
  private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;
  
  private static final String LOGTAG = "AnimatedVDCompat";
  
  private static final String TARGET = "target";
  
  private AnimatedVectorDrawableCompatState mAnimatedVectorState;
  
  ArrayList<Animatable2Compat.AnimationCallback> mAnimationCallbacks = null;
  
  private Animator.AnimatorListener mAnimatorListener = null;
  
  private ArgbEvaluator mArgbEvaluator = null;
  
  AnimatedVectorDrawableDelegateState mCachedConstantStateDelegate;
  
  final Drawable.Callback mCallback = new Drawable.Callback() {
      public void invalidateDrawable(Drawable param1Drawable) { AnimatedVectorDrawableCompat.this.invalidateSelf(); }
      
      public void scheduleDrawable(Drawable param1Drawable, Runnable param1Runnable, long param1Long) { AnimatedVectorDrawableCompat.this.scheduleSelf(param1Runnable, param1Long); }
      
      public void unscheduleDrawable(Drawable param1Drawable, Runnable param1Runnable) { AnimatedVectorDrawableCompat.this.unscheduleSelf(param1Runnable); }
    };
  
  private Context mContext;
  
  AnimatedVectorDrawableCompat() { this(null, null, null); }
  
  private AnimatedVectorDrawableCompat(@Nullable Context paramContext) { this(paramContext, null, null); }
  
  private AnimatedVectorDrawableCompat(@Nullable Context paramContext, @Nullable AnimatedVectorDrawableCompatState paramAnimatedVectorDrawableCompatState, @Nullable Resources paramResources) {
    this.mContext = paramContext;
    if (paramAnimatedVectorDrawableCompatState != null) {
      this.mAnimatedVectorState = paramAnimatedVectorDrawableCompatState;
      return;
    } 
    this.mAnimatedVectorState = new AnimatedVectorDrawableCompatState(paramContext, paramAnimatedVectorDrawableCompatState, this.mCallback, paramResources);
  }
  
  public static void clearAnimationCallbacks(Drawable paramDrawable) {
    if (paramDrawable != null) {
      if (!(paramDrawable instanceof android.graphics.drawable.Animatable))
        return; 
      if (Build.VERSION.SDK_INT >= 24) {
        ((AnimatedVectorDrawable)paramDrawable).clearAnimationCallbacks();
        return;
      } 
      ((AnimatedVectorDrawableCompat)paramDrawable).clearAnimationCallbacks();
      return;
    } 
  }
  
  @Nullable
  public static AnimatedVectorDrawableCompat create(@NonNull Context paramContext, @DrawableRes int paramInt) {
    if (Build.VERSION.SDK_INT >= 24) {
      AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat(paramContext);
      animatedVectorDrawableCompat.mDelegateDrawable = ResourcesCompat.getDrawable(paramContext.getResources(), paramInt, paramContext.getTheme());
      animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
      animatedVectorDrawableCompat.mCachedConstantStateDelegate = new AnimatedVectorDrawableDelegateState(animatedVectorDrawableCompat.mDelegateDrawable.getConstantState());
      return animatedVectorDrawableCompat;
    } 
    Resources resources = paramContext.getResources();
    try {
      XmlResourceParser xmlResourceParser = resources.getXml(paramInt);
      AttributeSet attributeSet = Xml.asAttributeSet(xmlResourceParser);
      while (true) {
        paramInt = xmlResourceParser.next();
        if (paramInt != 2 && paramInt != 1)
          continue; 
        break;
      } 
      if (paramInt == 2)
        return createFromXmlInner(paramContext, paramContext.getResources(), xmlResourceParser, attributeSet, paramContext.getTheme()); 
      throw new XmlPullParserException("No start tag found");
    } catch (XmlPullParserException paramContext) {
      Log.e("AnimatedVDCompat", "parser error", paramContext);
    } catch (IOException paramContext) {
      Log.e("AnimatedVDCompat", "parser error", paramContext);
    } 
    return null;
  }
  
  public static AnimatedVectorDrawableCompat createFromXmlInner(Context paramContext, Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat(paramContext);
    animatedVectorDrawableCompat.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    return animatedVectorDrawableCompat;
  }
  
  public static void registerAnimationCallback(Drawable paramDrawable, Animatable2Compat.AnimationCallback paramAnimationCallback) {
    if (paramDrawable != null) {
      if (paramAnimationCallback == null)
        return; 
      if (!(paramDrawable instanceof android.graphics.drawable.Animatable))
        return; 
      if (Build.VERSION.SDK_INT >= 24) {
        registerPlatformCallback((AnimatedVectorDrawable)paramDrawable, paramAnimationCallback);
        return;
      } 
      ((AnimatedVectorDrawableCompat)paramDrawable).registerAnimationCallback(paramAnimationCallback);
      return;
    } 
  }
  
  @RequiresApi(23)
  private static void registerPlatformCallback(@NonNull AnimatedVectorDrawable paramAnimatedVectorDrawable, @NonNull Animatable2Compat.AnimationCallback paramAnimationCallback) { paramAnimatedVectorDrawable.registerAnimationCallback(paramAnimationCallback.getPlatformCallback()); }
  
  private void removeAnimatorSetListener() {
    if (this.mAnimatorListener != null) {
      this.mAnimatedVectorState.mAnimatorSet.removeListener(this.mAnimatorListener);
      this.mAnimatorListener = null;
    } 
  }
  
  private void setupAnimatorsForTarget(String paramString, Animator paramAnimator) {
    paramAnimator.setTarget(this.mAnimatedVectorState.mVectorDrawable.getTargetByName(paramString));
    if (Build.VERSION.SDK_INT < 21)
      setupColorAnimator(paramAnimator); 
    if (this.mAnimatedVectorState.mAnimators == null) {
      this.mAnimatedVectorState.mAnimators = new ArrayList();
      this.mAnimatedVectorState.mTargetNameMap = new ArrayMap();
    } 
    this.mAnimatedVectorState.mAnimators.add(paramAnimator);
    this.mAnimatedVectorState.mTargetNameMap.put(paramAnimator, paramString);
  }
  
  private void setupColorAnimator(Animator paramAnimator) {
    if (paramAnimator instanceof AnimatorSet) {
      ArrayList arrayList = ((AnimatorSet)paramAnimator).getChildAnimations();
      if (arrayList != null)
        for (byte b = 0; b < arrayList.size(); b++)
          setupColorAnimator((Animator)arrayList.get(b));  
    } 
    if (paramAnimator instanceof ObjectAnimator) {
      ObjectAnimator objectAnimator = (ObjectAnimator)paramAnimator;
      String str = objectAnimator.getPropertyName();
      if ("fillColor".equals(str) || "strokeColor".equals(str)) {
        if (this.mArgbEvaluator == null)
          this.mArgbEvaluator = new ArgbEvaluator(); 
        objectAnimator.setEvaluator(this.mArgbEvaluator);
      } 
    } 
  }
  
  public static boolean unregisterAnimationCallback(Drawable paramDrawable, Animatable2Compat.AnimationCallback paramAnimationCallback) { return (paramDrawable != null) ? ((paramAnimationCallback == null) ? false : (!(paramDrawable instanceof android.graphics.drawable.Animatable) ? false : ((Build.VERSION.SDK_INT >= 24) ? unregisterPlatformCallback((AnimatedVectorDrawable)paramDrawable, paramAnimationCallback) : ((AnimatedVectorDrawableCompat)paramDrawable).unregisterAnimationCallback(paramAnimationCallback)))) : false; }
  
  @RequiresApi(23)
  private static boolean unregisterPlatformCallback(AnimatedVectorDrawable paramAnimatedVectorDrawable, Animatable2Compat.AnimationCallback paramAnimationCallback) { return paramAnimatedVectorDrawable.unregisterAnimationCallback(paramAnimationCallback.getPlatformCallback()); }
  
  public void applyTheme(Resources.Theme paramTheme) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.applyTheme(this.mDelegateDrawable, paramTheme);
      return;
    } 
  }
  
  public boolean canApplyTheme() { return (this.mDelegateDrawable != null) ? DrawableCompat.canApplyTheme(this.mDelegateDrawable) : 0; }
  
  public void clearAnimationCallbacks() {
    if (this.mDelegateDrawable != null) {
      ((AnimatedVectorDrawable)this.mDelegateDrawable).clearAnimationCallbacks();
      return;
    } 
    removeAnimatorSetListener();
    ArrayList arrayList = this.mAnimationCallbacks;
    if (arrayList == null)
      return; 
    arrayList.clear();
  }
  
  public void draw(Canvas paramCanvas) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.draw(paramCanvas);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.draw(paramCanvas);
    if (this.mAnimatedVectorState.mAnimatorSet.isStarted())
      invalidateSelf(); 
  }
  
  public int getAlpha() { return (this.mDelegateDrawable != null) ? DrawableCompat.getAlpha(this.mDelegateDrawable) : this.mAnimatedVectorState.mVectorDrawable.getAlpha(); }
  
  public int getChangingConfigurations() { return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getChangingConfigurations() : (super.getChangingConfigurations() | this.mAnimatedVectorState.mChangingConfigurations); }
  
  public Drawable.ConstantState getConstantState() { return (this.mDelegateDrawable != null && Build.VERSION.SDK_INT >= 24) ? new AnimatedVectorDrawableDelegateState(this.mDelegateDrawable.getConstantState()) : null; }
  
  public int getIntrinsicHeight() { return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getIntrinsicHeight() : this.mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight(); }
  
  public int getIntrinsicWidth() { return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getIntrinsicWidth() : this.mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth(); }
  
  public int getOpacity() { return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getOpacity() : this.mAnimatedVectorState.mVectorDrawable.getOpacity(); }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet) throws XmlPullParserException, IOException { inflate(paramResources, paramXmlPullParser, paramAttributeSet, null); }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.inflate(this.mDelegateDrawable, paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
      return;
    } 
    int i = paramXmlPullParser.getEventType();
    int j = paramXmlPullParser.getDepth();
    while (i != 1 && (paramXmlPullParser.getDepth() >= j + 1 || i != 3)) {
      if (i == 2) {
        TypedArray typedArray = paramXmlPullParser.getName();
        if ("animated-vector".equals(typedArray)) {
          typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE);
          i = typedArray.getResourceId(0, 0);
          if (i != 0) {
            VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(paramResources, i, paramTheme);
            vectorDrawableCompat.setAllowCaching(false);
            vectorDrawableCompat.setCallback(this.mCallback);
            if (this.mAnimatedVectorState.mVectorDrawable != null)
              this.mAnimatedVectorState.mVectorDrawable.setCallback(null); 
            this.mAnimatedVectorState.mVectorDrawable = vectorDrawableCompat;
          } 
          typedArray.recycle();
        } else if ("target".equals(typedArray)) {
          typedArray = paramResources.obtainAttributes(paramAttributeSet, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE_TARGET);
          String str = typedArray.getString(0);
          i = typedArray.getResourceId(1, 0);
          if (i != 0) {
            Context context = this.mContext;
            if (context != null) {
              setupAnimatorsForTarget(str, AnimatorInflaterCompat.loadAnimator(context, i));
            } else {
              typedArray.recycle();
              throw new IllegalStateException("Context can't be null when inflating animators");
            } 
          } 
          typedArray.recycle();
        } 
      } 
      i = paramXmlPullParser.next();
    } 
    this.mAnimatedVectorState.setupAnimatorSet();
  }
  
  public boolean isAutoMirrored() { return (this.mDelegateDrawable != null) ? DrawableCompat.isAutoMirrored(this.mDelegateDrawable) : this.mAnimatedVectorState.mVectorDrawable.isAutoMirrored(); }
  
  public boolean isRunning() { return (this.mDelegateDrawable != null) ? ((AnimatedVectorDrawable)this.mDelegateDrawable).isRunning() : this.mAnimatedVectorState.mAnimatorSet.isRunning(); }
  
  public boolean isStateful() { return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.isStateful() : this.mAnimatedVectorState.mVectorDrawable.isStateful(); }
  
  public Drawable mutate() {
    if (this.mDelegateDrawable != null)
      this.mDelegateDrawable.mutate(); 
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setBounds(paramRect);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setBounds(paramRect);
  }
  
  protected boolean onLevelChange(int paramInt) { return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.setLevel(paramInt) : this.mAnimatedVectorState.mVectorDrawable.setLevel(paramInt); }
  
  protected boolean onStateChange(int[] paramArrayOfInt) { return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.setState(paramArrayOfInt) : this.mAnimatedVectorState.mVectorDrawable.setState(paramArrayOfInt); }
  
  public void registerAnimationCallback(@NonNull Animatable2Compat.AnimationCallback paramAnimationCallback) {
    if (this.mDelegateDrawable != null) {
      registerPlatformCallback((AnimatedVectorDrawable)this.mDelegateDrawable, paramAnimationCallback);
      return;
    } 
    if (paramAnimationCallback == null)
      return; 
    if (this.mAnimationCallbacks == null)
      this.mAnimationCallbacks = new ArrayList(); 
    if (this.mAnimationCallbacks.contains(paramAnimationCallback))
      return; 
    this.mAnimationCallbacks.add(paramAnimationCallback);
    if (this.mAnimatorListener == null)
      this.mAnimatorListener = new AnimatorListenerAdapter() {
          public void onAnimationEnd(Animator param1Animator) {
            ArrayList arrayList = new ArrayList(AnimatedVectorDrawableCompat.this.mAnimationCallbacks);
            int i = arrayList.size();
            for (byte b = 0; b < i; b++)
              ((Animatable2Compat.AnimationCallback)arrayList.get(b)).onAnimationEnd(AnimatedVectorDrawableCompat.this); 
          }
          
          public void onAnimationStart(Animator param1Animator) {
            ArrayList arrayList = new ArrayList(AnimatedVectorDrawableCompat.this.mAnimationCallbacks);
            int i = arrayList.size();
            for (byte b = 0; b < i; b++)
              ((Animatable2Compat.AnimationCallback)arrayList.get(b)).onAnimationStart(AnimatedVectorDrawableCompat.this); 
          }
        }; 
    this.mAnimatedVectorState.mAnimatorSet.addListener(this.mAnimatorListener);
  }
  
  public void setAlpha(int paramInt) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setAlpha(paramInt);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setAlpha(paramInt);
  }
  
  public void setAutoMirrored(boolean paramBoolean) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setAutoMirrored(this.mDelegateDrawable, paramBoolean);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setAutoMirrored(paramBoolean);
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setColorFilter(paramColorFilter);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setColorFilter(paramColorFilter);
  }
  
  public void setTint(int paramInt) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setTint(this.mDelegateDrawable, paramInt);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setTint(paramInt);
  }
  
  public void setTintList(ColorStateList paramColorStateList) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setTintList(this.mDelegateDrawable, paramColorStateList);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setTintList(paramColorStateList);
  }
  
  public void setTintMode(PorterDuff.Mode paramMode) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setTintMode(this.mDelegateDrawable, paramMode);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setTintMode(paramMode);
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2) {
    if (this.mDelegateDrawable != null)
      return this.mDelegateDrawable.setVisible(paramBoolean1, paramBoolean2); 
    this.mAnimatedVectorState.mVectorDrawable.setVisible(paramBoolean1, paramBoolean2);
    return super.setVisible(paramBoolean1, paramBoolean2);
  }
  
  public void start() {
    if (this.mDelegateDrawable != null) {
      ((AnimatedVectorDrawable)this.mDelegateDrawable).start();
      return;
    } 
    if (this.mAnimatedVectorState.mAnimatorSet.isStarted())
      return; 
    this.mAnimatedVectorState.mAnimatorSet.start();
    invalidateSelf();
  }
  
  public void stop() {
    if (this.mDelegateDrawable != null) {
      ((AnimatedVectorDrawable)this.mDelegateDrawable).stop();
      return;
    } 
    this.mAnimatedVectorState.mAnimatorSet.end();
  }
  
  public boolean unregisterAnimationCallback(@NonNull Animatable2Compat.AnimationCallback paramAnimationCallback) {
    if (this.mDelegateDrawable != null)
      unregisterPlatformCallback((AnimatedVectorDrawable)this.mDelegateDrawable, paramAnimationCallback); 
    ArrayList arrayList = this.mAnimationCallbacks;
    if (arrayList == null || paramAnimationCallback == null)
      return false; 
    boolean bool = arrayList.remove(paramAnimationCallback);
    if (this.mAnimationCallbacks.size() == 0)
      removeAnimatorSetListener(); 
    return bool;
  }
  
  private static class AnimatedVectorDrawableCompatState extends Drawable.ConstantState {
    AnimatorSet mAnimatorSet;
    
    ArrayList<Animator> mAnimators;
    
    int mChangingConfigurations;
    
    ArrayMap<Animator, String> mTargetNameMap;
    
    VectorDrawableCompat mVectorDrawable;
    
    public AnimatedVectorDrawableCompatState(Context param1Context, AnimatedVectorDrawableCompatState param1AnimatedVectorDrawableCompatState, Drawable.Callback param1Callback, Resources param1Resources) {
      if (param1AnimatedVectorDrawableCompatState != null) {
        this.mChangingConfigurations = param1AnimatedVectorDrawableCompatState.mChangingConfigurations;
        VectorDrawableCompat vectorDrawableCompat = param1AnimatedVectorDrawableCompatState.mVectorDrawable;
        if (vectorDrawableCompat != null) {
          Drawable.ConstantState constantState = vectorDrawableCompat.getConstantState();
          if (param1Resources != null) {
            this.mVectorDrawable = (VectorDrawableCompat)constantState.newDrawable(param1Resources);
          } else {
            this.mVectorDrawable = (VectorDrawableCompat)constantState.newDrawable();
          } 
          this.mVectorDrawable = (VectorDrawableCompat)this.mVectorDrawable.mutate();
          this.mVectorDrawable.setCallback(param1Callback);
          this.mVectorDrawable.setBounds(param1AnimatedVectorDrawableCompatState.mVectorDrawable.getBounds());
          this.mVectorDrawable.setAllowCaching(false);
        } 
        ArrayList arrayList = param1AnimatedVectorDrawableCompatState.mAnimators;
        if (arrayList != null) {
          int i = arrayList.size();
          this.mAnimators = new ArrayList(i);
          this.mTargetNameMap = new ArrayMap(i);
          byte b;
          for (b = 0; b < i; b++) {
            Animator animator2 = (Animator)param1AnimatedVectorDrawableCompatState.mAnimators.get(b);
            Animator animator1 = animator2.clone();
            String str = (String)param1AnimatedVectorDrawableCompatState.mTargetNameMap.get(animator2);
            animator1.setTarget(this.mVectorDrawable.getTargetByName(str));
            this.mAnimators.add(animator1);
            this.mTargetNameMap.put(animator1, str);
          } 
          setupAnimatorSet();
        } 
      } 
    }
    
    public int getChangingConfigurations() { return this.mChangingConfigurations; }
    
    public Drawable newDrawable() { throw new IllegalStateException("No constant state support for SDK < 24."); }
    
    public Drawable newDrawable(Resources param1Resources) { throw new IllegalStateException("No constant state support for SDK < 24."); }
    
    public void setupAnimatorSet() {
      if (this.mAnimatorSet == null)
        this.mAnimatorSet = new AnimatorSet(); 
      this.mAnimatorSet.playTogether(this.mAnimators);
    }
  }
  
  @RequiresApi(24)
  private static class AnimatedVectorDrawableDelegateState extends Drawable.ConstantState {
    private final Drawable.ConstantState mDelegateState;
    
    public AnimatedVectorDrawableDelegateState(Drawable.ConstantState param1ConstantState) { this.mDelegateState = param1ConstantState; }
    
    public boolean canApplyTheme() { return this.mDelegateState.canApplyTheme(); }
    
    public int getChangingConfigurations() { return this.mDelegateState.getChangingConfigurations(); }
    
    public Drawable newDrawable() {
      AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
      animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable();
      animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
      return animatedVectorDrawableCompat;
    }
    
    public Drawable newDrawable(Resources param1Resources) {
      AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
      animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(param1Resources);
      animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
      return animatedVectorDrawableCompat;
    }
    
    public Drawable newDrawable(Resources param1Resources, Resources.Theme param1Theme) {
      AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
      animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(param1Resources, param1Theme);
      animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
      return animatedVectorDrawableCompat;
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\graphics\drawable\AnimatedVectorDrawableCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */