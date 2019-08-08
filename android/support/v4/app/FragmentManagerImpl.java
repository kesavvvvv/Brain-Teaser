package android.support.v4.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelStore;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflater.Factory2 {
  static final Interpolator ACCELERATE_CUBIC;
  
  static final Interpolator ACCELERATE_QUINT;
  
  static final int ANIM_DUR = 220;
  
  public static final int ANIM_STYLE_CLOSE_ENTER = 3;
  
  public static final int ANIM_STYLE_CLOSE_EXIT = 4;
  
  public static final int ANIM_STYLE_FADE_ENTER = 5;
  
  public static final int ANIM_STYLE_FADE_EXIT = 6;
  
  public static final int ANIM_STYLE_OPEN_ENTER = 1;
  
  public static final int ANIM_STYLE_OPEN_EXIT = 2;
  
  static boolean DEBUG = false;
  
  static final Interpolator DECELERATE_CUBIC;
  
  static final Interpolator DECELERATE_QUINT;
  
  static final String TAG = "FragmentManager";
  
  static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
  
  static final String TARGET_STATE_TAG = "android:target_state";
  
  static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
  
  static final String VIEW_STATE_TAG = "android:view_state";
  
  static Field sAnimationListenerField = null;
  
  SparseArray<Fragment> mActive;
  
  final ArrayList<Fragment> mAdded = new ArrayList();
  
  ArrayList<Integer> mAvailBackStackIndices;
  
  ArrayList<BackStackRecord> mBackStack;
  
  ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
  
  ArrayList<BackStackRecord> mBackStackIndices;
  
  FragmentContainer mContainer;
  
  ArrayList<Fragment> mCreatedMenus;
  
  int mCurState = 0;
  
  boolean mDestroyed;
  
  Runnable mExecCommit = new Runnable() {
      public void run() { FragmentManagerImpl.this.execPendingActions(); }
    };
  
  boolean mExecutingActions;
  
  boolean mHavePendingDeferredStart;
  
  FragmentHostCallback mHost;
  
  private final CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> mLifecycleCallbacks = new CopyOnWriteArrayList();
  
  boolean mNeedMenuInvalidate;
  
  int mNextFragmentIndex = 0;
  
  String mNoTransactionsBecause;
  
  Fragment mParent;
  
  ArrayList<OpGenerator> mPendingActions;
  
  ArrayList<StartEnterTransitionListener> mPostponedTransactions;
  
  @Nullable
  Fragment mPrimaryNav;
  
  FragmentManagerNonConfig mSavedNonConfig;
  
  SparseArray<Parcelable> mStateArray = null;
  
  Bundle mStateBundle = null;
  
  boolean mStateSaved;
  
  boolean mStopped;
  
  ArrayList<Fragment> mTmpAddedFragments;
  
  ArrayList<Boolean> mTmpIsPop;
  
  ArrayList<BackStackRecord> mTmpRecords;
  
  static  {
    DECELERATE_QUINT = new DecelerateInterpolator(2.5F);
    DECELERATE_CUBIC = new DecelerateInterpolator(1.5F);
    ACCELERATE_QUINT = new AccelerateInterpolator(2.5F);
    ACCELERATE_CUBIC = new AccelerateInterpolator(1.5F);
  }
  
  private void addAddedFragments(ArraySet<Fragment> paramArraySet) {
    int i = this.mCurState;
    if (i < 1)
      return; 
    int j = Math.min(i, 3);
    int k = this.mAdded.size();
    for (i = 0; i < k; i++) {
      Fragment fragment = (Fragment)this.mAdded.get(i);
      if (fragment.mState < j) {
        moveToState(fragment, j, fragment.getNextAnim(), fragment.getNextTransition(), false);
        if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded)
          paramArraySet.add(fragment); 
      } 
    } 
  }
  
  private void animateRemoveFragment(@NonNull final Fragment fragment, @NonNull AnimationOrAnimator paramAnimationOrAnimator, int paramInt) {
    final View viewToAnimate = paramFragment.mView;
    final ViewGroup container = paramFragment.mContainer;
    viewGroup.startViewTransition(view);
    paramFragment.setStateAfterAnimating(paramInt);
    if (paramAnimationOrAnimator.animation != null) {
      EndViewTransitionAnimator endViewTransitionAnimator = new EndViewTransitionAnimator(paramAnimationOrAnimator.animation, viewGroup, view);
      paramFragment.setAnimatingAway(paramFragment.mView);
      endViewTransitionAnimator.setAnimationListener(new AnimationListenerWrapper(getAnimationListener(endViewTransitionAnimator)) {
            public void onAnimationEnd(Animation param1Animation) {
              super.onAnimationEnd(param1Animation);
              container.post(new Runnable() {
                    public void run() {
                      if (fragment.getAnimatingAway() != null) {
                        fragment.setAnimatingAway(null);
                        FragmentManagerImpl.null.this.this$0.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                      } 
                    }
                  });
            }
          });
      setHWLayerAnimListenerIfAlpha(view, paramAnimationOrAnimator);
      paramFragment.mView.startAnimation(endViewTransitionAnimator);
      return;
    } 
    Animator animator = paramAnimationOrAnimator.animator;
    paramFragment.setAnimator(paramAnimationOrAnimator.animator);
    animator.addListener(new AnimatorListenerAdapter() {
          public void onAnimationEnd(Animator param1Animator) {
            container.endViewTransition(viewToAnimate);
            param1Animator = fragment.getAnimator();
            fragment.setAnimator(null);
            if (param1Animator != null && container.indexOfChild(viewToAnimate) < 0) {
              FragmentManagerImpl fragmentManagerImpl = FragmentManagerImpl.this;
              Fragment fragment = fragment;
              fragmentManagerImpl.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
            } 
          }
        });
    animator.setTarget(paramFragment.mView);
    setHWLayerAnimListenerIfAlpha(paramFragment.mView, paramAnimationOrAnimator);
    animator.start();
  }
  
  private void burpActive() {
    SparseArray sparseArray = this.mActive;
    if (sparseArray != null)
      for (int i = sparseArray.size() - 1; i >= 0; i--) {
        if (this.mActive.valueAt(i) == null) {
          sparseArray = this.mActive;
          sparseArray.delete(sparseArray.keyAt(i));
        } 
      }  
  }
  
  private void checkStateLoss() {
    if (!isStateSaved()) {
      if (this.mNoTransactionsBecause == null)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Can not perform this action inside of ");
      stringBuilder.append(this.mNoTransactionsBecause);
      throw new IllegalStateException(stringBuilder.toString());
    } 
    throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
  }
  
  private void cleanupExec() {
    this.mExecutingActions = false;
    this.mTmpIsPop.clear();
    this.mTmpRecords.clear();
  }
  
  private void dispatchStateChange(int paramInt) {
    try {
      this.mExecutingActions = true;
      moveToState(paramInt, false);
      this.mExecutingActions = false;
      return;
    } finally {
      this.mExecutingActions = false;
    } 
  }
  
  private void endAnimatingAwayFragments() {
    int i;
    SparseArray sparseArray = this.mActive;
    if (sparseArray == null) {
      i = 0;
    } else {
      i = sparseArray.size();
    } 
    for (byte b = 0; b < i; b++) {
      Fragment fragment = (Fragment)this.mActive.valueAt(b);
      if (fragment != null)
        if (fragment.getAnimatingAway() != null) {
          int j = fragment.getStateAfterAnimating();
          View view = fragment.getAnimatingAway();
          Animation animation = view.getAnimation();
          if (animation != null) {
            animation.cancel();
            view.clearAnimation();
          } 
          fragment.setAnimatingAway(null);
          moveToState(fragment, j, 0, 0, false);
        } else if (fragment.getAnimator() != null) {
          fragment.getAnimator().end();
        }  
    } 
  }
  
  private void ensureExecReady(boolean paramBoolean) {
    if (!this.mExecutingActions) {
      if (this.mHost != null) {
        if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
          if (!paramBoolean)
            checkStateLoss(); 
          if (this.mTmpRecords == null) {
            this.mTmpRecords = new ArrayList();
            this.mTmpIsPop = new ArrayList();
          } 
          this.mExecutingActions = true;
          try {
            executePostponedTransaction(null, null);
            return;
          } finally {
            this.mExecutingActions = false;
          } 
        } 
        throw new IllegalStateException("Must be called from main thread of fragment host");
      } 
      throw new IllegalStateException("Fragment host has been destroyed");
    } 
    throw new IllegalStateException("FragmentManager is already executing transactions");
  }
  
  private static void executeOps(ArrayList<BackStackRecord> paramArrayList1, ArrayList<Boolean> paramArrayList2, int paramInt1, int paramInt2) {
    while (paramInt1 < paramInt2) {
      BackStackRecord backStackRecord = (BackStackRecord)paramArrayList1.get(paramInt1);
      boolean bool2 = ((Boolean)paramArrayList2.get(paramInt1)).booleanValue();
      boolean bool1 = true;
      if (bool2) {
        backStackRecord.bumpBackStackNesting(-1);
        if (paramInt1 != paramInt2 - 1)
          bool1 = false; 
        backStackRecord.executePopOps(bool1);
      } else {
        backStackRecord.bumpBackStackNesting(1);
        backStackRecord.executeOps();
      } 
      paramInt1++;
    } 
  }
  
  private void executeOpsTogether(ArrayList<BackStackRecord> paramArrayList1, ArrayList<Boolean> paramArrayList2, int paramInt1, int paramInt2) {
    boolean bool = ((BackStackRecord)paramArrayList1.get(paramInt1)).mReorderingAllowed;
    ArrayList arrayList = this.mTmpAddedFragments;
    if (arrayList == null) {
      this.mTmpAddedFragments = new ArrayList();
    } else {
      arrayList.clear();
    } 
    this.mTmpAddedFragments.addAll(this.mAdded);
    Fragment fragment = getPrimaryNavigationFragment();
    int j = paramInt1;
    byte b = 0;
    while (true) {
      byte b1 = 1;
      if (j < paramInt2) {
        BackStackRecord backStackRecord = (BackStackRecord)paramArrayList1.get(j);
        if (!((Boolean)paramArrayList2.get(j)).booleanValue()) {
          fragment = backStackRecord.expandOps(this.mTmpAddedFragments, fragment);
        } else {
          fragment = backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, fragment);
        } 
        byte b2 = b1;
        if (!b)
          if (backStackRecord.mAddToBackStack) {
            b2 = b1;
          } else {
            b2 = 0;
          }  
        j++;
        b = b2;
        continue;
      } 
      break;
    } 
    this.mTmpAddedFragments.clear();
    if (!bool)
      FragmentTransition.startTransitions(this, paramArrayList1, paramArrayList2, paramInt1, paramInt2, false); 
    executeOps(paramArrayList1, paramArrayList2, paramInt1, paramInt2);
    int i = paramInt2;
    if (bool) {
      ArraySet arraySet = new ArraySet();
      addAddedFragments(arraySet);
      i = postponePostponableTransactions(paramArrayList1, paramArrayList2, paramInt1, paramInt2, arraySet);
      makeRemovedFragmentsInvisible(arraySet);
    } 
    if (i != paramInt1 && bool) {
      FragmentTransition.startTransitions(this, paramArrayList1, paramArrayList2, paramInt1, i, true);
      moveToState(this.mCurState, true);
    } 
    while (paramInt1 < paramInt2) {
      BackStackRecord backStackRecord = (BackStackRecord)paramArrayList1.get(paramInt1);
      if (((Boolean)paramArrayList2.get(paramInt1)).booleanValue() && backStackRecord.mIndex >= 0) {
        freeBackStackIndex(backStackRecord.mIndex);
        backStackRecord.mIndex = -1;
      } 
      backStackRecord.runOnCommitRunnables();
      paramInt1++;
    } 
    if (b != 0)
      reportBackStackChanged(); 
  }
  
  private void executePostponedTransaction(ArrayList<BackStackRecord> paramArrayList1, ArrayList<Boolean> paramArrayList2) { // Byte code:
    //   0: aload_0
    //   1: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   4: astore #7
    //   6: aload #7
    //   8: ifnonnull -> 17
    //   11: iconst_0
    //   12: istore #4
    //   14: goto -> 24
    //   17: aload #7
    //   19: invokevirtual size : ()I
    //   22: istore #4
    //   24: iconst_0
    //   25: istore_3
    //   26: iload_3
    //   27: iload #4
    //   29: if_icmpge -> 233
    //   32: aload_0
    //   33: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   36: iload_3
    //   37: invokevirtual get : (I)Ljava/lang/Object;
    //   40: checkcast android/support/v4/app/FragmentManagerImpl$StartEnterTransitionListener
    //   43: astore #7
    //   45: aload_1
    //   46: ifnull -> 104
    //   49: aload #7
    //   51: getfield mIsBack : Z
    //   54: ifne -> 104
    //   57: aload_1
    //   58: aload #7
    //   60: getfield mRecord : Landroid/support/v4/app/BackStackRecord;
    //   63: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   66: istore #5
    //   68: iload #5
    //   70: iconst_m1
    //   71: if_icmpeq -> 104
    //   74: aload_2
    //   75: iload #5
    //   77: invokevirtual get : (I)Ljava/lang/Object;
    //   80: checkcast java/lang/Boolean
    //   83: invokevirtual booleanValue : ()Z
    //   86: ifeq -> 104
    //   89: aload #7
    //   91: invokevirtual cancelTransaction : ()V
    //   94: iload #4
    //   96: istore #5
    //   98: iload_3
    //   99: istore #6
    //   101: goto -> 221
    //   104: aload #7
    //   106: invokevirtual isReady : ()Z
    //   109: ifne -> 147
    //   112: iload #4
    //   114: istore #5
    //   116: iload_3
    //   117: istore #6
    //   119: aload_1
    //   120: ifnull -> 221
    //   123: iload #4
    //   125: istore #5
    //   127: iload_3
    //   128: istore #6
    //   130: aload #7
    //   132: getfield mRecord : Landroid/support/v4/app/BackStackRecord;
    //   135: aload_1
    //   136: iconst_0
    //   137: aload_1
    //   138: invokevirtual size : ()I
    //   141: invokevirtual interactsWith : (Ljava/util/ArrayList;II)Z
    //   144: ifeq -> 221
    //   147: aload_0
    //   148: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   151: iload_3
    //   152: invokevirtual remove : (I)Ljava/lang/Object;
    //   155: pop
    //   156: iload_3
    //   157: iconst_1
    //   158: isub
    //   159: istore #6
    //   161: iload #4
    //   163: iconst_1
    //   164: isub
    //   165: istore #5
    //   167: aload_1
    //   168: ifnull -> 216
    //   171: aload #7
    //   173: getfield mIsBack : Z
    //   176: ifne -> 216
    //   179: aload_1
    //   180: aload #7
    //   182: getfield mRecord : Landroid/support/v4/app/BackStackRecord;
    //   185: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   188: istore_3
    //   189: iload_3
    //   190: iconst_m1
    //   191: if_icmpeq -> 216
    //   194: aload_2
    //   195: iload_3
    //   196: invokevirtual get : (I)Ljava/lang/Object;
    //   199: checkcast java/lang/Boolean
    //   202: invokevirtual booleanValue : ()Z
    //   205: ifeq -> 216
    //   208: aload #7
    //   210: invokevirtual cancelTransaction : ()V
    //   213: goto -> 221
    //   216: aload #7
    //   218: invokevirtual completeTransaction : ()V
    //   221: iload #6
    //   223: iconst_1
    //   224: iadd
    //   225: istore_3
    //   226: iload #5
    //   228: istore #4
    //   230: goto -> 26
    //   233: return }
  
  private Fragment findFragmentUnder(Fragment paramFragment) {
    ViewGroup viewGroup = paramFragment.mContainer;
    View view = paramFragment.mView;
    if (viewGroup != null) {
      if (view == null)
        return null; 
      for (int i = this.mAdded.indexOf(paramFragment) - 1; i >= 0; i--) {
        paramFragment = (Fragment)this.mAdded.get(i);
        if (paramFragment.mContainer == viewGroup && paramFragment.mView != null)
          return paramFragment; 
      } 
      return null;
    } 
    return null;
  }
  
  private void forcePostponedTransactions() {
    if (this.mPostponedTransactions != null)
      while (!this.mPostponedTransactions.isEmpty())
        ((StartEnterTransitionListener)this.mPostponedTransactions.remove(0)).completeTransaction();  
  }
  
  private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> paramArrayList1, ArrayList<Boolean> paramArrayList2) { // Byte code:
    //   0: iconst_0
    //   1: istore #5
    //   3: aload_0
    //   4: monitorenter
    //   5: aload_0
    //   6: getfield mPendingActions : Ljava/util/ArrayList;
    //   9: ifnull -> 98
    //   12: aload_0
    //   13: getfield mPendingActions : Ljava/util/ArrayList;
    //   16: invokevirtual size : ()I
    //   19: ifne -> 25
    //   22: goto -> 98
    //   25: aload_0
    //   26: getfield mPendingActions : Ljava/util/ArrayList;
    //   29: invokevirtual size : ()I
    //   32: istore #4
    //   34: iconst_0
    //   35: istore_3
    //   36: iload_3
    //   37: iload #4
    //   39: if_icmpge -> 72
    //   42: iload #5
    //   44: aload_0
    //   45: getfield mPendingActions : Ljava/util/ArrayList;
    //   48: iload_3
    //   49: invokevirtual get : (I)Ljava/lang/Object;
    //   52: checkcast android/support/v4/app/FragmentManagerImpl$OpGenerator
    //   55: aload_1
    //   56: aload_2
    //   57: invokeinterface generateOps : (Ljava/util/ArrayList;Ljava/util/ArrayList;)Z
    //   62: ior
    //   63: istore #5
    //   65: iload_3
    //   66: iconst_1
    //   67: iadd
    //   68: istore_3
    //   69: goto -> 36
    //   72: aload_0
    //   73: getfield mPendingActions : Ljava/util/ArrayList;
    //   76: invokevirtual clear : ()V
    //   79: aload_0
    //   80: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   83: invokevirtual getHandler : ()Landroid/os/Handler;
    //   86: aload_0
    //   87: getfield mExecCommit : Ljava/lang/Runnable;
    //   90: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)V
    //   93: aload_0
    //   94: monitorexit
    //   95: iload #5
    //   97: ireturn
    //   98: aload_0
    //   99: monitorexit
    //   100: iconst_0
    //   101: ireturn
    //   102: astore_1
    //   103: aload_0
    //   104: monitorexit
    //   105: aload_1
    //   106: athrow
    // Exception table:
    //   from	to	target	type
    //   5	22	102	finally
    //   25	34	102	finally
    //   42	65	102	finally
    //   72	95	102	finally
    //   98	100	102	finally
    //   103	105	102	finally }
  
  private static Animation.AnimationListener getAnimationListener(Animation paramAnimation) {
    try {
      if (sAnimationListenerField == null) {
        sAnimationListenerField = Animation.class.getDeclaredField("mListener");
        sAnimationListenerField.setAccessible(true);
      } 
      return (Animation.AnimationListener)sAnimationListenerField.get(paramAnimation);
    } catch (NoSuchFieldException paramAnimation) {
      Log.e("FragmentManager", "No field with the name mListener is found in Animation class", paramAnimation);
      return null;
    } catch (IllegalAccessException paramAnimation) {
      Log.e("FragmentManager", "Cannot access Animation's mListener field", paramAnimation);
      return null;
    } 
  }
  
  static AnimationOrAnimator makeFadeAnimation(Context paramContext, float paramFloat1, float paramFloat2) {
    AlphaAnimation alphaAnimation = new AlphaAnimation(paramFloat1, paramFloat2);
    alphaAnimation.setInterpolator(DECELERATE_CUBIC);
    alphaAnimation.setDuration(220L);
    return new AnimationOrAnimator(alphaAnimation);
  }
  
  static AnimationOrAnimator makeOpenCloseAnimation(Context paramContext, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    AnimationSet animationSet = new AnimationSet(false);
    ScaleAnimation scaleAnimation = new ScaleAnimation(paramFloat1, paramFloat2, paramFloat1, paramFloat2, 1, 0.5F, 1, 0.5F);
    scaleAnimation.setInterpolator(DECELERATE_QUINT);
    scaleAnimation.setDuration(220L);
    animationSet.addAnimation(scaleAnimation);
    AlphaAnimation alphaAnimation = new AlphaAnimation(paramFloat3, paramFloat4);
    alphaAnimation.setInterpolator(DECELERATE_CUBIC);
    alphaAnimation.setDuration(220L);
    animationSet.addAnimation(alphaAnimation);
    return new AnimationOrAnimator(animationSet);
  }
  
  private void makeRemovedFragmentsInvisible(ArraySet<Fragment> paramArraySet) {
    int i = paramArraySet.size();
    for (byte b = 0; b < i; b++) {
      Fragment fragment = (Fragment)paramArraySet.valueAt(b);
      if (!fragment.mAdded) {
        View view = fragment.getView();
        fragment.mPostponedAlpha = view.getAlpha();
        view.setAlpha(0.0F);
      } 
    } 
  }
  
  static boolean modifiesAlpha(Animator paramAnimator) {
    PropertyValuesHolder[] arrayOfPropertyValuesHolder;
    if (paramAnimator == null)
      return false; 
    if (paramAnimator instanceof ValueAnimator) {
      arrayOfPropertyValuesHolder = ((ValueAnimator)paramAnimator).getValues();
      for (byte b = 0; b < arrayOfPropertyValuesHolder.length; b++) {
        if ("alpha".equals(arrayOfPropertyValuesHolder[b].getPropertyName()))
          return true; 
      } 
    } else if (arrayOfPropertyValuesHolder instanceof AnimatorSet) {
      ArrayList arrayList = ((AnimatorSet)arrayOfPropertyValuesHolder).getChildAnimations();
      for (byte b = 0; b < arrayList.size(); b++) {
        if (modifiesAlpha((Animator)arrayList.get(b)))
          return true; 
      } 
    } 
    return false;
  }
  
  static boolean modifiesAlpha(AnimationOrAnimator paramAnimationOrAnimator) {
    List list;
    if (paramAnimationOrAnimator.animation instanceof AlphaAnimation)
      return true; 
    if (paramAnimationOrAnimator.animation instanceof AnimationSet) {
      list = ((AnimationSet)paramAnimationOrAnimator.animation).getAnimations();
      for (byte b = 0; b < list.size(); b++) {
        if (list.get(b) instanceof AlphaAnimation)
          return true; 
      } 
      return false;
    } 
    return modifiesAlpha(list.animator);
  }
  
  private boolean popBackStackImmediate(String paramString, int paramInt1, int paramInt2) {
    execPendingActions();
    ensureExecReady(true);
    Fragment fragment = this.mPrimaryNav;
    if (fragment != null && paramInt1 < 0 && paramString == null) {
      FragmentManager fragmentManager = fragment.peekChildFragmentManager();
      if (fragmentManager != null && fragmentManager.popBackStackImmediate())
        return true; 
    } 
    boolean bool = popBackStackState(this.mTmpRecords, this.mTmpIsPop, paramString, paramInt1, paramInt2);
    if (bool) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
      } finally {
        cleanupExec();
      } 
    } 
    doPendingDeferredStart();
    burpActive();
    return bool;
  }
  
  private int postponePostponableTransactions(ArrayList<BackStackRecord> paramArrayList1, ArrayList<Boolean> paramArrayList2, int paramInt1, int paramInt2, ArraySet<Fragment> paramArraySet) {
    int j = paramInt2;
    int i = paramInt2 - 1;
    while (i >= paramInt1) {
      boolean bool;
      BackStackRecord backStackRecord = (BackStackRecord)paramArrayList1.get(i);
      boolean bool1 = ((Boolean)paramArrayList2.get(i)).booleanValue();
      if (backStackRecord.isPostponed() && !backStackRecord.interactsWith(paramArrayList1, i + 1, paramInt2)) {
        bool = true;
      } else {
        bool = false;
      } 
      int k = j;
      if (bool) {
        if (this.mPostponedTransactions == null)
          this.mPostponedTransactions = new ArrayList(); 
        StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(backStackRecord, bool1);
        this.mPostponedTransactions.add(startEnterTransitionListener);
        backStackRecord.setOnStartPostponedListener(startEnterTransitionListener);
        if (bool1) {
          backStackRecord.executeOps();
        } else {
          backStackRecord.executePopOps(false);
        } 
        k = j - 1;
        if (i != k) {
          paramArrayList1.remove(i);
          paramArrayList1.add(k, backStackRecord);
        } 
        addAddedFragments(paramArraySet);
      } 
      i--;
      j = k;
    } 
    return j;
  }
  
  private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> paramArrayList1, ArrayList<Boolean> paramArrayList2) {
    if (paramArrayList1 != null) {
      if (paramArrayList1.isEmpty())
        return; 
      if (paramArrayList2 != null && paramArrayList1.size() == paramArrayList2.size()) {
        executePostponedTransaction(paramArrayList1, paramArrayList2);
        int i = paramArrayList1.size();
        byte b2 = 0;
        byte b1 = 0;
        while (b1 < i) {
          byte b3 = b2;
          byte b4 = b1;
          if (!((BackStackRecord)paramArrayList1.get(b1)).mReorderingAllowed) {
            if (b2 != b1)
              executeOpsTogether(paramArrayList1, paramArrayList2, b2, b1); 
            b3 = b1 + 1;
            b2 = b3;
            if (((Boolean)paramArrayList2.get(b1)).booleanValue())
              while (true) {
                b2 = b3;
                if (b3 < i) {
                  b2 = b3;
                  if (((Boolean)paramArrayList2.get(b3)).booleanValue()) {
                    b2 = b3;
                    if (!((BackStackRecord)paramArrayList1.get(b3)).mReorderingAllowed) {
                      b3++;
                      continue;
                    } 
                  } 
                } 
                break;
              }  
            executeOpsTogether(paramArrayList1, paramArrayList2, b1, b2);
            b3 = b2;
            b4 = b2 - 1;
          } 
          b1 = b4 + 1;
          b2 = b3;
        } 
        if (b2 != i)
          executeOpsTogether(paramArrayList1, paramArrayList2, b2, i); 
        return;
      } 
      throw new IllegalStateException("Internal error with the back stack records");
    } 
  }
  
  public static int reverseTransit(int paramInt) { return (paramInt != 4097) ? ((paramInt != 4099) ? ((paramInt != 8194) ? 0 : 4097) : 4099) : 8194; }
  
  private static void setHWLayerAnimListenerIfAlpha(View paramView, AnimationOrAnimator paramAnimationOrAnimator) {
    if (paramView != null) {
      if (paramAnimationOrAnimator == null)
        return; 
      if (shouldRunOnHWLayer(paramView, paramAnimationOrAnimator)) {
        if (paramAnimationOrAnimator.animator != null) {
          paramAnimationOrAnimator.animator.addListener(new AnimatorOnHWLayerIfNeededListener(paramView));
          return;
        } 
        Animation.AnimationListener animationListener = getAnimationListener(paramAnimationOrAnimator.animation);
        paramView.setLayerType(2, null);
        paramAnimationOrAnimator.animation.setAnimationListener(new AnimateOnHWLayerIfNeededListener(paramView, animationListener));
      } 
      return;
    } 
  }
  
  private static void setRetaining(FragmentManagerNonConfig paramFragmentManagerNonConfig) {
    if (paramFragmentManagerNonConfig == null)
      return; 
    List list2 = paramFragmentManagerNonConfig.getFragments();
    if (list2 != null) {
      Iterator iterator = list2.iterator();
      while (iterator.hasNext())
        ((Fragment)iterator.next()).mRetaining = true; 
    } 
    List list1 = paramFragmentManagerNonConfig.getChildNonConfigs();
    if (list1 != null) {
      Iterator iterator = list1.iterator();
      while (iterator.hasNext())
        setRetaining((FragmentManagerNonConfig)iterator.next()); 
    } 
  }
  
  static boolean shouldRunOnHWLayer(View paramView, AnimationOrAnimator paramAnimationOrAnimator) { return (paramView != null) ? ((paramAnimationOrAnimator == null) ? false : ((Build.VERSION.SDK_INT >= 19 && paramView.getLayerType() == 0 && ViewCompat.hasOverlappingRendering(paramView) && modifiesAlpha(paramAnimationOrAnimator)))) : false; }
  
  private void throwException(RuntimeException paramRuntimeException) {
    Log.e("FragmentManager", paramRuntimeException.getMessage());
    Log.e("FragmentManager", "Activity state:");
    printWriter = new PrintWriter(new LogWriter("FragmentManager"));
    FragmentHostCallback fragmentHostCallback = this.mHost;
    if (fragmentHostCallback != null) {
      try {
        fragmentHostCallback.onDump("  ", null, printWriter, new String[0]);
      } catch (Exception printWriter) {
        Log.e("FragmentManager", "Failed dumping state", printWriter);
      } 
    } else {
      try {
        dump("  ", null, printWriter, new String[0]);
      } catch (Exception printWriter) {
        Log.e("FragmentManager", "Failed dumping state", printWriter);
      } 
    } 
    throw paramRuntimeException;
  }
  
  public static int transitToStyleIndex(int paramInt, boolean paramBoolean) {
    if (paramInt != 4097) {
      if (paramInt != 4099) {
        if (paramInt != 8194)
          return -1; 
        if (paramBoolean) {
          paramInt = 3;
        } else {
          paramInt = 4;
        } 
        return paramInt;
      } 
      if (paramBoolean) {
        paramInt = 5;
      } else {
        paramInt = 6;
      } 
      return paramInt;
    } 
    if (paramBoolean) {
      paramInt = 1;
    } else {
      paramInt = 2;
    } 
    return paramInt;
  }
  
  void addBackStackState(BackStackRecord paramBackStackRecord) {
    if (this.mBackStack == null)
      this.mBackStack = new ArrayList(); 
    this.mBackStack.add(paramBackStackRecord);
  }
  
  public void addFragment(Fragment paramFragment, boolean paramBoolean) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("add: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    makeActive(paramFragment);
    if (!paramFragment.mDetached)
      if (!this.mAdded.contains(paramFragment)) {
        synchronized (this.mAdded) {
          this.mAdded.add(paramFragment);
          paramFragment.mAdded = true;
          paramFragment.mRemoving = false;
          if (paramFragment.mView == null)
            paramFragment.mHiddenChanged = false; 
          if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
            this.mNeedMenuInvalidate = true; 
          if (paramBoolean) {
            moveToState(paramFragment);
            return;
          } 
        } 
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment already added: ");
        stringBuilder.append(paramFragment);
        throw new IllegalStateException(stringBuilder.toString());
      }  
  }
  
  public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener) {
    if (this.mBackStackChangeListeners == null)
      this.mBackStackChangeListeners = new ArrayList(); 
    this.mBackStackChangeListeners.add(paramOnBackStackChangedListener);
  }
  
  public int allocBackStackIndex(BackStackRecord paramBackStackRecord) { // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   6: ifnull -> 111
    //   9: aload_0
    //   10: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   13: invokevirtual size : ()I
    //   16: ifgt -> 22
    //   19: goto -> 111
    //   22: aload_0
    //   23: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   26: aload_0
    //   27: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   30: invokevirtual size : ()I
    //   33: iconst_1
    //   34: isub
    //   35: invokevirtual remove : (I)Ljava/lang/Object;
    //   38: checkcast java/lang/Integer
    //   41: invokevirtual intValue : ()I
    //   44: istore_2
    //   45: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   48: ifeq -> 97
    //   51: new java/lang/StringBuilder
    //   54: dup
    //   55: invokespecial <init> : ()V
    //   58: astore_3
    //   59: aload_3
    //   60: ldc_w 'Adding back stack index '
    //   63: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: pop
    //   67: aload_3
    //   68: iload_2
    //   69: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   72: pop
    //   73: aload_3
    //   74: ldc_w ' with '
    //   77: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   80: pop
    //   81: aload_3
    //   82: aload_1
    //   83: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   86: pop
    //   87: ldc 'FragmentManager'
    //   89: aload_3
    //   90: invokevirtual toString : ()Ljava/lang/String;
    //   93: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   96: pop
    //   97: aload_0
    //   98: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   101: iload_2
    //   102: aload_1
    //   103: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   106: pop
    //   107: aload_0
    //   108: monitorexit
    //   109: iload_2
    //   110: ireturn
    //   111: aload_0
    //   112: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   115: ifnonnull -> 129
    //   118: aload_0
    //   119: new java/util/ArrayList
    //   122: dup
    //   123: invokespecial <init> : ()V
    //   126: putfield mBackStackIndices : Ljava/util/ArrayList;
    //   129: aload_0
    //   130: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   133: invokevirtual size : ()I
    //   136: istore_2
    //   137: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   140: ifeq -> 189
    //   143: new java/lang/StringBuilder
    //   146: dup
    //   147: invokespecial <init> : ()V
    //   150: astore_3
    //   151: aload_3
    //   152: ldc_w 'Setting back stack index '
    //   155: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   158: pop
    //   159: aload_3
    //   160: iload_2
    //   161: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   164: pop
    //   165: aload_3
    //   166: ldc_w ' to '
    //   169: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: pop
    //   173: aload_3
    //   174: aload_1
    //   175: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   178: pop
    //   179: ldc 'FragmentManager'
    //   181: aload_3
    //   182: invokevirtual toString : ()Ljava/lang/String;
    //   185: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   188: pop
    //   189: aload_0
    //   190: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   193: aload_1
    //   194: invokevirtual add : (Ljava/lang/Object;)Z
    //   197: pop
    //   198: aload_0
    //   199: monitorexit
    //   200: iload_2
    //   201: ireturn
    //   202: astore_1
    //   203: aload_0
    //   204: monitorexit
    //   205: aload_1
    //   206: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	202	finally
    //   22	97	202	finally
    //   97	109	202	finally
    //   111	129	202	finally
    //   129	189	202	finally
    //   189	200	202	finally
    //   203	205	202	finally }
  
  public void attachController(FragmentHostCallback paramFragmentHostCallback, FragmentContainer paramFragmentContainer, Fragment paramFragment) {
    if (this.mHost == null) {
      this.mHost = paramFragmentHostCallback;
      this.mContainer = paramFragmentContainer;
      this.mParent = paramFragment;
      return;
    } 
    throw new IllegalStateException("Already attached");
  }
  
  public void attachFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("attach: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (paramFragment.mDetached) {
      paramFragment.mDetached = false;
      if (!paramFragment.mAdded)
        if (!this.mAdded.contains(paramFragment)) {
          if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("add from attach: ");
            stringBuilder.append(paramFragment);
            Log.v("FragmentManager", stringBuilder.toString());
          } 
          synchronized (this.mAdded) {
            this.mAdded.add(paramFragment);
            paramFragment.mAdded = true;
            if (paramFragment.mHasMenu && paramFragment.mMenuVisible) {
              this.mNeedMenuInvalidate = true;
              return;
            } 
          } 
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Fragment already added: ");
          stringBuilder.append(paramFragment);
          throw new IllegalStateException(stringBuilder.toString());
        }  
    } 
  }
  
  public FragmentTransaction beginTransaction() { return new BackStackRecord(this); }
  
  void completeExecute(BackStackRecord paramBackStackRecord, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    if (paramBoolean1) {
      paramBackStackRecord.executePopOps(paramBoolean3);
    } else {
      paramBackStackRecord.executeOps();
    } 
    ArrayList arrayList1 = new ArrayList(1);
    ArrayList arrayList2 = new ArrayList(1);
    arrayList1.add(paramBackStackRecord);
    arrayList2.add(Boolean.valueOf(paramBoolean1));
    if (paramBoolean2)
      FragmentTransition.startTransitions(this, arrayList1, arrayList2, 0, 1, true); 
    if (paramBoolean3)
      moveToState(this.mCurState, true); 
    SparseArray sparseArray = this.mActive;
    if (sparseArray != null) {
      int i = sparseArray.size();
      byte b;
      for (b = 0; b < i; b++) {
        Fragment fragment = (Fragment)this.mActive.valueAt(b);
        if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && paramBackStackRecord.interactsWith(fragment.mContainerId)) {
          if (fragment.mPostponedAlpha > 0.0F)
            fragment.mView.setAlpha(fragment.mPostponedAlpha); 
          if (paramBoolean3) {
            fragment.mPostponedAlpha = 0.0F;
          } else {
            fragment.mPostponedAlpha = -1.0F;
            fragment.mIsNewlyAdded = false;
          } 
        } 
      } 
    } 
  }
  
  void completeShowHideFragment(final Fragment fragment) {
    if (paramFragment.mView != null) {
      AnimationOrAnimator animationOrAnimator = loadAnimation(paramFragment, paramFragment.getNextTransition(), paramFragment.mHidden ^ true, paramFragment.getNextTransitionStyle());
      if (animationOrAnimator != null && animationOrAnimator.animator != null) {
        animationOrAnimator.animator.setTarget(paramFragment.mView);
        if (paramFragment.mHidden) {
          if (paramFragment.isHideReplaced()) {
            paramFragment.setHideReplaced(false);
          } else {
            final ViewGroup container = paramFragment.mContainer;
            final View animatingView = paramFragment.mView;
            viewGroup.startViewTransition(view);
            animationOrAnimator.animator.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator param1Animator) {
                    container.endViewTransition(animatingView);
                    param1Animator.removeListener(this);
                    if (this.val$fragment.mView != null)
                      this.val$fragment.mView.setVisibility(8); 
                  }
                });
          } 
        } else {
          paramFragment.mView.setVisibility(0);
        } 
        setHWLayerAnimListenerIfAlpha(paramFragment.mView, animationOrAnimator);
        animationOrAnimator.animator.start();
      } else {
        byte b;
        if (animationOrAnimator != null) {
          setHWLayerAnimListenerIfAlpha(paramFragment.mView, animationOrAnimator);
          paramFragment.mView.startAnimation(animationOrAnimator.animation);
          animationOrAnimator.animation.start();
        } 
        if (paramFragment.mHidden && !paramFragment.isHideReplaced()) {
          b = 8;
        } else {
          b = 0;
        } 
        paramFragment.mView.setVisibility(b);
        if (paramFragment.isHideReplaced())
          paramFragment.setHideReplaced(false); 
      } 
    } 
    if (paramFragment.mAdded && paramFragment.mHasMenu && paramFragment.mMenuVisible)
      this.mNeedMenuInvalidate = true; 
    paramFragment.mHiddenChanged = false;
    paramFragment.onHiddenChanged(paramFragment.mHidden);
  }
  
  public void detachFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("detach: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (!paramFragment.mDetached) {
      paramFragment.mDetached = true;
      if (paramFragment.mAdded) {
        if (DEBUG) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("remove from detach: ");
          stringBuilder.append(paramFragment);
          Log.v("FragmentManager", stringBuilder.toString());
        } 
        synchronized (this.mAdded) {
          this.mAdded.remove(paramFragment);
          if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
            this.mNeedMenuInvalidate = true; 
          paramFragment.mAdded = false;
          return;
        } 
      } 
    } 
  }
  
  public void dispatchActivityCreated() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(2);
  }
  
  public void dispatchConfigurationChanged(Configuration paramConfiguration) {
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = (Fragment)this.mAdded.get(b);
      if (fragment != null)
        fragment.performConfigurationChanged(paramConfiguration); 
    } 
  }
  
  public boolean dispatchContextItemSelected(MenuItem paramMenuItem) {
    if (this.mCurState < 1)
      return false; 
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = (Fragment)this.mAdded.get(b);
      if (fragment != null && fragment.performContextItemSelected(paramMenuItem))
        return true; 
    } 
    return false;
  }
  
  public void dispatchCreate() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(1);
  }
  
  public boolean dispatchCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater) {
    if (this.mCurState < 1)
      return false; 
    int i = 0;
    ArrayList arrayList = null;
    byte b = 0;
    while (b < this.mAdded.size()) {
      Fragment fragment = (Fragment)this.mAdded.get(b);
      byte b1 = i;
      ArrayList arrayList1 = arrayList;
      if (fragment != null) {
        b1 = i;
        arrayList1 = arrayList;
        if (fragment.performCreateOptionsMenu(paramMenu, paramMenuInflater)) {
          b1 = 1;
          arrayList1 = arrayList;
          if (arrayList == null)
            arrayList1 = new ArrayList(); 
          arrayList1.add(fragment);
        } 
      } 
      b++;
      i = b1;
      arrayList = arrayList1;
    } 
    if (this.mCreatedMenus != null)
      for (b = 0; b < this.mCreatedMenus.size(); b++) {
        Fragment fragment = (Fragment)this.mCreatedMenus.get(b);
        if (arrayList == null || !arrayList.contains(fragment))
          fragment.onDestroyOptionsMenu(); 
      }  
    this.mCreatedMenus = arrayList;
    return i;
  }
  
  public void dispatchDestroy() {
    this.mDestroyed = true;
    execPendingActions();
    dispatchStateChange(0);
    this.mHost = null;
    this.mContainer = null;
    this.mParent = null;
  }
  
  public void dispatchDestroyView() { dispatchStateChange(1); }
  
  public void dispatchLowMemory() {
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = (Fragment)this.mAdded.get(b);
      if (fragment != null)
        fragment.performLowMemory(); 
    } 
  }
  
  public void dispatchMultiWindowModeChanged(boolean paramBoolean) {
    for (int i = this.mAdded.size() - 1; i >= 0; i--) {
      Fragment fragment = (Fragment)this.mAdded.get(i);
      if (fragment != null)
        fragment.performMultiWindowModeChanged(paramBoolean); 
    } 
  }
  
  void dispatchOnFragmentActivityCreated(@NonNull Fragment paramFragment, @Nullable Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentActivityCreated(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentActivityCreated(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentAttached(@NonNull Fragment paramFragment, @NonNull Context paramContext, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentAttached(paramFragment, paramContext, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentAttached(this, paramFragment, paramContext); 
    } 
  }
  
  void dispatchOnFragmentCreated(@NonNull Fragment paramFragment, @Nullable Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentCreated(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentCreated(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentDestroyed(@NonNull Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDestroyed(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentDestroyed(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentDetached(@NonNull Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDetached(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentDetached(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentPaused(@NonNull Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPaused(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentPaused(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentPreAttached(@NonNull Fragment paramFragment, @NonNull Context paramContext, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreAttached(paramFragment, paramContext, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreAttached(this, paramFragment, paramContext); 
    } 
  }
  
  void dispatchOnFragmentPreCreated(@NonNull Fragment paramFragment, @Nullable Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreCreated(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreCreated(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentResumed(@NonNull Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentResumed(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentResumed(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentSaveInstanceState(@NonNull Fragment paramFragment, @NonNull Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentSaveInstanceState(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentSaveInstanceState(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentStarted(@NonNull Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStarted(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentStarted(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentStopped(@NonNull Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStopped(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentStopped(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentViewCreated(@NonNull Fragment paramFragment, @NonNull View paramView, @Nullable Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewCreated(paramFragment, paramView, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewCreated(this, paramFragment, paramView, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentViewDestroyed(@NonNull Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewDestroyed(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewDestroyed(this, paramFragment); 
    } 
  }
  
  public boolean dispatchOptionsItemSelected(MenuItem paramMenuItem) {
    if (this.mCurState < 1)
      return false; 
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = (Fragment)this.mAdded.get(b);
      if (fragment != null && fragment.performOptionsItemSelected(paramMenuItem))
        return true; 
    } 
    return false;
  }
  
  public void dispatchOptionsMenuClosed(Menu paramMenu) {
    if (this.mCurState < 1)
      return; 
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = (Fragment)this.mAdded.get(b);
      if (fragment != null)
        fragment.performOptionsMenuClosed(paramMenu); 
    } 
  }
  
  public void dispatchPause() { dispatchStateChange(3); }
  
  public void dispatchPictureInPictureModeChanged(boolean paramBoolean) {
    for (int i = this.mAdded.size() - 1; i >= 0; i--) {
      Fragment fragment = (Fragment)this.mAdded.get(i);
      if (fragment != null)
        fragment.performPictureInPictureModeChanged(paramBoolean); 
    } 
  }
  
  public boolean dispatchPrepareOptionsMenu(Menu paramMenu) {
    if (this.mCurState < 1)
      return false; 
    int i = 0;
    byte b = 0;
    while (b < this.mAdded.size()) {
      Fragment fragment = (Fragment)this.mAdded.get(b);
      byte b1 = i;
      if (fragment != null) {
        b1 = i;
        if (fragment.performPrepareOptionsMenu(paramMenu))
          b1 = 1; 
      } 
      b++;
      i = b1;
    } 
    return i;
  }
  
  public void dispatchResume() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(4);
  }
  
  public void dispatchStart() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(3);
  }
  
  public void dispatchStop() {
    this.mStopped = true;
    dispatchStateChange(2);
  }
  
  void doPendingDeferredStart() {
    if (this.mHavePendingDeferredStart) {
      this.mHavePendingDeferredStart = false;
      startPendingDeferredFragments();
    } 
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) { // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #7
    //   9: aload #7
    //   11: aload_1
    //   12: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload #7
    //   18: ldc_w '    '
    //   21: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   24: pop
    //   25: aload #7
    //   27: invokevirtual toString : ()Ljava/lang/String;
    //   30: astore #7
    //   32: aload_0
    //   33: getfield mActive : Landroid/util/SparseArray;
    //   36: astore #8
    //   38: aload #8
    //   40: ifnull -> 165
    //   43: aload #8
    //   45: invokevirtual size : ()I
    //   48: istore #6
    //   50: iload #6
    //   52: ifle -> 165
    //   55: aload_3
    //   56: aload_1
    //   57: invokevirtual print : (Ljava/lang/String;)V
    //   60: aload_3
    //   61: ldc_w 'Active Fragments in '
    //   64: invokevirtual print : (Ljava/lang/String;)V
    //   67: aload_3
    //   68: aload_0
    //   69: invokestatic identityHashCode : (Ljava/lang/Object;)I
    //   72: invokestatic toHexString : (I)Ljava/lang/String;
    //   75: invokevirtual print : (Ljava/lang/String;)V
    //   78: aload_3
    //   79: ldc_w ':'
    //   82: invokevirtual println : (Ljava/lang/String;)V
    //   85: iconst_0
    //   86: istore #5
    //   88: iload #5
    //   90: iload #6
    //   92: if_icmpge -> 165
    //   95: aload_0
    //   96: getfield mActive : Landroid/util/SparseArray;
    //   99: iload #5
    //   101: invokevirtual valueAt : (I)Ljava/lang/Object;
    //   104: checkcast android/support/v4/app/Fragment
    //   107: astore #8
    //   109: aload_3
    //   110: aload_1
    //   111: invokevirtual print : (Ljava/lang/String;)V
    //   114: aload_3
    //   115: ldc_w '  #'
    //   118: invokevirtual print : (Ljava/lang/String;)V
    //   121: aload_3
    //   122: iload #5
    //   124: invokevirtual print : (I)V
    //   127: aload_3
    //   128: ldc_w ': '
    //   131: invokevirtual print : (Ljava/lang/String;)V
    //   134: aload_3
    //   135: aload #8
    //   137: invokevirtual println : (Ljava/lang/Object;)V
    //   140: aload #8
    //   142: ifnull -> 156
    //   145: aload #8
    //   147: aload #7
    //   149: aload_2
    //   150: aload_3
    //   151: aload #4
    //   153: invokevirtual dump : (Ljava/lang/String;Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V
    //   156: iload #5
    //   158: iconst_1
    //   159: iadd
    //   160: istore #5
    //   162: goto -> 88
    //   165: aload_0
    //   166: getfield mAdded : Ljava/util/ArrayList;
    //   169: invokevirtual size : ()I
    //   172: istore #6
    //   174: iload #6
    //   176: ifle -> 258
    //   179: aload_3
    //   180: aload_1
    //   181: invokevirtual print : (Ljava/lang/String;)V
    //   184: aload_3
    //   185: ldc_w 'Added Fragments:'
    //   188: invokevirtual println : (Ljava/lang/String;)V
    //   191: iconst_0
    //   192: istore #5
    //   194: iload #5
    //   196: iload #6
    //   198: if_icmpge -> 258
    //   201: aload_0
    //   202: getfield mAdded : Ljava/util/ArrayList;
    //   205: iload #5
    //   207: invokevirtual get : (I)Ljava/lang/Object;
    //   210: checkcast android/support/v4/app/Fragment
    //   213: astore #8
    //   215: aload_3
    //   216: aload_1
    //   217: invokevirtual print : (Ljava/lang/String;)V
    //   220: aload_3
    //   221: ldc_w '  #'
    //   224: invokevirtual print : (Ljava/lang/String;)V
    //   227: aload_3
    //   228: iload #5
    //   230: invokevirtual print : (I)V
    //   233: aload_3
    //   234: ldc_w ': '
    //   237: invokevirtual print : (Ljava/lang/String;)V
    //   240: aload_3
    //   241: aload #8
    //   243: invokevirtual toString : ()Ljava/lang/String;
    //   246: invokevirtual println : (Ljava/lang/String;)V
    //   249: iload #5
    //   251: iconst_1
    //   252: iadd
    //   253: istore #5
    //   255: goto -> 194
    //   258: aload_0
    //   259: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   262: astore #8
    //   264: aload #8
    //   266: ifnull -> 360
    //   269: aload #8
    //   271: invokevirtual size : ()I
    //   274: istore #6
    //   276: iload #6
    //   278: ifle -> 360
    //   281: aload_3
    //   282: aload_1
    //   283: invokevirtual print : (Ljava/lang/String;)V
    //   286: aload_3
    //   287: ldc_w 'Fragments Created Menus:'
    //   290: invokevirtual println : (Ljava/lang/String;)V
    //   293: iconst_0
    //   294: istore #5
    //   296: iload #5
    //   298: iload #6
    //   300: if_icmpge -> 360
    //   303: aload_0
    //   304: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   307: iload #5
    //   309: invokevirtual get : (I)Ljava/lang/Object;
    //   312: checkcast android/support/v4/app/Fragment
    //   315: astore #8
    //   317: aload_3
    //   318: aload_1
    //   319: invokevirtual print : (Ljava/lang/String;)V
    //   322: aload_3
    //   323: ldc_w '  #'
    //   326: invokevirtual print : (Ljava/lang/String;)V
    //   329: aload_3
    //   330: iload #5
    //   332: invokevirtual print : (I)V
    //   335: aload_3
    //   336: ldc_w ': '
    //   339: invokevirtual print : (Ljava/lang/String;)V
    //   342: aload_3
    //   343: aload #8
    //   345: invokevirtual toString : ()Ljava/lang/String;
    //   348: invokevirtual println : (Ljava/lang/String;)V
    //   351: iload #5
    //   353: iconst_1
    //   354: iadd
    //   355: istore #5
    //   357: goto -> 296
    //   360: aload_0
    //   361: getfield mBackStack : Ljava/util/ArrayList;
    //   364: astore #8
    //   366: aload #8
    //   368: ifnull -> 473
    //   371: aload #8
    //   373: invokevirtual size : ()I
    //   376: istore #6
    //   378: iload #6
    //   380: ifle -> 473
    //   383: aload_3
    //   384: aload_1
    //   385: invokevirtual print : (Ljava/lang/String;)V
    //   388: aload_3
    //   389: ldc_w 'Back Stack:'
    //   392: invokevirtual println : (Ljava/lang/String;)V
    //   395: iconst_0
    //   396: istore #5
    //   398: iload #5
    //   400: iload #6
    //   402: if_icmpge -> 473
    //   405: aload_0
    //   406: getfield mBackStack : Ljava/util/ArrayList;
    //   409: iload #5
    //   411: invokevirtual get : (I)Ljava/lang/Object;
    //   414: checkcast android/support/v4/app/BackStackRecord
    //   417: astore #8
    //   419: aload_3
    //   420: aload_1
    //   421: invokevirtual print : (Ljava/lang/String;)V
    //   424: aload_3
    //   425: ldc_w '  #'
    //   428: invokevirtual print : (Ljava/lang/String;)V
    //   431: aload_3
    //   432: iload #5
    //   434: invokevirtual print : (I)V
    //   437: aload_3
    //   438: ldc_w ': '
    //   441: invokevirtual print : (Ljava/lang/String;)V
    //   444: aload_3
    //   445: aload #8
    //   447: invokevirtual toString : ()Ljava/lang/String;
    //   450: invokevirtual println : (Ljava/lang/String;)V
    //   453: aload #8
    //   455: aload #7
    //   457: aload_2
    //   458: aload_3
    //   459: aload #4
    //   461: invokevirtual dump : (Ljava/lang/String;Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V
    //   464: iload #5
    //   466: iconst_1
    //   467: iadd
    //   468: istore #5
    //   470: goto -> 398
    //   473: aload_0
    //   474: monitorenter
    //   475: aload_0
    //   476: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   479: ifnull -> 570
    //   482: aload_0
    //   483: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   486: invokevirtual size : ()I
    //   489: istore #6
    //   491: iload #6
    //   493: ifle -> 570
    //   496: aload_3
    //   497: aload_1
    //   498: invokevirtual print : (Ljava/lang/String;)V
    //   501: aload_3
    //   502: ldc_w 'Back Stack Indices:'
    //   505: invokevirtual println : (Ljava/lang/String;)V
    //   508: iconst_0
    //   509: istore #5
    //   511: iload #5
    //   513: iload #6
    //   515: if_icmpge -> 570
    //   518: aload_0
    //   519: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   522: iload #5
    //   524: invokevirtual get : (I)Ljava/lang/Object;
    //   527: checkcast android/support/v4/app/BackStackRecord
    //   530: astore_2
    //   531: aload_3
    //   532: aload_1
    //   533: invokevirtual print : (Ljava/lang/String;)V
    //   536: aload_3
    //   537: ldc_w '  #'
    //   540: invokevirtual print : (Ljava/lang/String;)V
    //   543: aload_3
    //   544: iload #5
    //   546: invokevirtual print : (I)V
    //   549: aload_3
    //   550: ldc_w ': '
    //   553: invokevirtual print : (Ljava/lang/String;)V
    //   556: aload_3
    //   557: aload_2
    //   558: invokevirtual println : (Ljava/lang/Object;)V
    //   561: iload #5
    //   563: iconst_1
    //   564: iadd
    //   565: istore #5
    //   567: goto -> 511
    //   570: aload_0
    //   571: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   574: ifnull -> 613
    //   577: aload_0
    //   578: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   581: invokevirtual size : ()I
    //   584: ifle -> 613
    //   587: aload_3
    //   588: aload_1
    //   589: invokevirtual print : (Ljava/lang/String;)V
    //   592: aload_3
    //   593: ldc_w 'mAvailBackStackIndices: '
    //   596: invokevirtual print : (Ljava/lang/String;)V
    //   599: aload_3
    //   600: aload_0
    //   601: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   604: invokevirtual toArray : ()[Ljava/lang/Object;
    //   607: invokestatic toString : ([Ljava/lang/Object;)Ljava/lang/String;
    //   610: invokevirtual println : (Ljava/lang/String;)V
    //   613: aload_0
    //   614: monitorexit
    //   615: aload_0
    //   616: getfield mPendingActions : Ljava/util/ArrayList;
    //   619: astore_2
    //   620: aload_2
    //   621: ifnull -> 709
    //   624: aload_2
    //   625: invokevirtual size : ()I
    //   628: istore #6
    //   630: iload #6
    //   632: ifle -> 709
    //   635: aload_3
    //   636: aload_1
    //   637: invokevirtual print : (Ljava/lang/String;)V
    //   640: aload_3
    //   641: ldc_w 'Pending Actions:'
    //   644: invokevirtual println : (Ljava/lang/String;)V
    //   647: iconst_0
    //   648: istore #5
    //   650: iload #5
    //   652: iload #6
    //   654: if_icmpge -> 709
    //   657: aload_0
    //   658: getfield mPendingActions : Ljava/util/ArrayList;
    //   661: iload #5
    //   663: invokevirtual get : (I)Ljava/lang/Object;
    //   666: checkcast android/support/v4/app/FragmentManagerImpl$OpGenerator
    //   669: astore_2
    //   670: aload_3
    //   671: aload_1
    //   672: invokevirtual print : (Ljava/lang/String;)V
    //   675: aload_3
    //   676: ldc_w '  #'
    //   679: invokevirtual print : (Ljava/lang/String;)V
    //   682: aload_3
    //   683: iload #5
    //   685: invokevirtual print : (I)V
    //   688: aload_3
    //   689: ldc_w ': '
    //   692: invokevirtual print : (Ljava/lang/String;)V
    //   695: aload_3
    //   696: aload_2
    //   697: invokevirtual println : (Ljava/lang/Object;)V
    //   700: iload #5
    //   702: iconst_1
    //   703: iadd
    //   704: istore #5
    //   706: goto -> 650
    //   709: aload_3
    //   710: aload_1
    //   711: invokevirtual print : (Ljava/lang/String;)V
    //   714: aload_3
    //   715: ldc_w 'FragmentManager misc state:'
    //   718: invokevirtual println : (Ljava/lang/String;)V
    //   721: aload_3
    //   722: aload_1
    //   723: invokevirtual print : (Ljava/lang/String;)V
    //   726: aload_3
    //   727: ldc_w '  mHost='
    //   730: invokevirtual print : (Ljava/lang/String;)V
    //   733: aload_3
    //   734: aload_0
    //   735: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   738: invokevirtual println : (Ljava/lang/Object;)V
    //   741: aload_3
    //   742: aload_1
    //   743: invokevirtual print : (Ljava/lang/String;)V
    //   746: aload_3
    //   747: ldc_w '  mContainer='
    //   750: invokevirtual print : (Ljava/lang/String;)V
    //   753: aload_3
    //   754: aload_0
    //   755: getfield mContainer : Landroid/support/v4/app/FragmentContainer;
    //   758: invokevirtual println : (Ljava/lang/Object;)V
    //   761: aload_0
    //   762: getfield mParent : Landroid/support/v4/app/Fragment;
    //   765: ifnull -> 788
    //   768: aload_3
    //   769: aload_1
    //   770: invokevirtual print : (Ljava/lang/String;)V
    //   773: aload_3
    //   774: ldc_w '  mParent='
    //   777: invokevirtual print : (Ljava/lang/String;)V
    //   780: aload_3
    //   781: aload_0
    //   782: getfield mParent : Landroid/support/v4/app/Fragment;
    //   785: invokevirtual println : (Ljava/lang/Object;)V
    //   788: aload_3
    //   789: aload_1
    //   790: invokevirtual print : (Ljava/lang/String;)V
    //   793: aload_3
    //   794: ldc_w '  mCurState='
    //   797: invokevirtual print : (Ljava/lang/String;)V
    //   800: aload_3
    //   801: aload_0
    //   802: getfield mCurState : I
    //   805: invokevirtual print : (I)V
    //   808: aload_3
    //   809: ldc_w ' mStateSaved='
    //   812: invokevirtual print : (Ljava/lang/String;)V
    //   815: aload_3
    //   816: aload_0
    //   817: getfield mStateSaved : Z
    //   820: invokevirtual print : (Z)V
    //   823: aload_3
    //   824: ldc_w ' mStopped='
    //   827: invokevirtual print : (Ljava/lang/String;)V
    //   830: aload_3
    //   831: aload_0
    //   832: getfield mStopped : Z
    //   835: invokevirtual print : (Z)V
    //   838: aload_3
    //   839: ldc_w ' mDestroyed='
    //   842: invokevirtual print : (Ljava/lang/String;)V
    //   845: aload_3
    //   846: aload_0
    //   847: getfield mDestroyed : Z
    //   850: invokevirtual println : (Z)V
    //   853: aload_0
    //   854: getfield mNeedMenuInvalidate : Z
    //   857: ifeq -> 880
    //   860: aload_3
    //   861: aload_1
    //   862: invokevirtual print : (Ljava/lang/String;)V
    //   865: aload_3
    //   866: ldc_w '  mNeedMenuInvalidate='
    //   869: invokevirtual print : (Ljava/lang/String;)V
    //   872: aload_3
    //   873: aload_0
    //   874: getfield mNeedMenuInvalidate : Z
    //   877: invokevirtual println : (Z)V
    //   880: aload_0
    //   881: getfield mNoTransactionsBecause : Ljava/lang/String;
    //   884: ifnull -> 907
    //   887: aload_3
    //   888: aload_1
    //   889: invokevirtual print : (Ljava/lang/String;)V
    //   892: aload_3
    //   893: ldc_w '  mNoTransactionsBecause='
    //   896: invokevirtual print : (Ljava/lang/String;)V
    //   899: aload_3
    //   900: aload_0
    //   901: getfield mNoTransactionsBecause : Ljava/lang/String;
    //   904: invokevirtual println : (Ljava/lang/String;)V
    //   907: return
    //   908: astore_1
    //   909: aload_0
    //   910: monitorexit
    //   911: aload_1
    //   912: athrow
    // Exception table:
    //   from	to	target	type
    //   475	491	908	finally
    //   496	508	908	finally
    //   518	561	908	finally
    //   570	613	908	finally
    //   613	615	908	finally
    //   909	911	908	finally }
  
  public void enqueueAction(OpGenerator paramOpGenerator, boolean paramBoolean) { // Byte code:
    //   0: iload_2
    //   1: ifne -> 8
    //   4: aload_0
    //   5: invokespecial checkStateLoss : ()V
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield mDestroyed : Z
    //   14: ifne -> 61
    //   17: aload_0
    //   18: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   21: ifnonnull -> 27
    //   24: goto -> 61
    //   27: aload_0
    //   28: getfield mPendingActions : Ljava/util/ArrayList;
    //   31: ifnonnull -> 45
    //   34: aload_0
    //   35: new java/util/ArrayList
    //   38: dup
    //   39: invokespecial <init> : ()V
    //   42: putfield mPendingActions : Ljava/util/ArrayList;
    //   45: aload_0
    //   46: getfield mPendingActions : Ljava/util/ArrayList;
    //   49: aload_1
    //   50: invokevirtual add : (Ljava/lang/Object;)Z
    //   53: pop
    //   54: aload_0
    //   55: invokevirtual scheduleCommit : ()V
    //   58: aload_0
    //   59: monitorexit
    //   60: return
    //   61: iload_2
    //   62: ifeq -> 68
    //   65: aload_0
    //   66: monitorexit
    //   67: return
    //   68: new java/lang/IllegalStateException
    //   71: dup
    //   72: ldc_w 'Activity has been destroyed'
    //   75: invokespecial <init> : (Ljava/lang/String;)V
    //   78: athrow
    //   79: astore_1
    //   80: aload_0
    //   81: monitorexit
    //   82: aload_1
    //   83: athrow
    // Exception table:
    //   from	to	target	type
    //   10	24	79	finally
    //   27	45	79	finally
    //   45	60	79	finally
    //   65	67	79	finally
    //   68	79	79	finally
    //   80	82	79	finally }
  
  void ensureInflatedFragmentView(Fragment paramFragment) {
    if (paramFragment.mFromLayout && !paramFragment.mPerformedCreateView) {
      paramFragment.performCreateView(paramFragment.performGetLayoutInflater(paramFragment.mSavedFragmentState), null, paramFragment.mSavedFragmentState);
      if (paramFragment.mView != null) {
        paramFragment.mInnerView = paramFragment.mView;
        paramFragment.mView.setSaveFromParentEnabled(false);
        if (paramFragment.mHidden)
          paramFragment.mView.setVisibility(8); 
        paramFragment.onViewCreated(paramFragment.mView, paramFragment.mSavedFragmentState);
        dispatchOnFragmentViewCreated(paramFragment, paramFragment.mView, paramFragment.mSavedFragmentState, false);
        return;
      } 
      paramFragment.mInnerView = null;
    } 
  }
  
  public boolean execPendingActions() {
    ensureExecReady(true);
    bool = false;
    while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
        cleanupExec();
      } finally {
        cleanupExec();
      } 
    } 
    doPendingDeferredStart();
    burpActive();
    return bool;
  }
  
  public void execSingleAction(OpGenerator paramOpGenerator, boolean paramBoolean) {
    if (paramBoolean && (this.mHost == null || this.mDestroyed))
      return; 
    ensureExecReady(paramBoolean);
    if (paramOpGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
      } finally {
        cleanupExec();
      } 
    } 
    doPendingDeferredStart();
    burpActive();
  }
  
  public boolean executePendingTransactions() {
    boolean bool = execPendingActions();
    forcePostponedTransactions();
    return bool;
  }
  
  @Nullable
  public Fragment findFragmentById(int paramInt) {
    int i;
    for (i = this.mAdded.size() - 1; i >= 0; i--) {
      Fragment fragment = (Fragment)this.mAdded.get(i);
      if (fragment != null && fragment.mFragmentId == paramInt)
        return fragment; 
    } 
    SparseArray sparseArray = this.mActive;
    if (sparseArray != null)
      for (i = sparseArray.size() - 1; i >= 0; i--) {
        Fragment fragment = (Fragment)this.mActive.valueAt(i);
        if (fragment != null && fragment.mFragmentId == paramInt)
          return fragment; 
      }  
    return null;
  }
  
  @Nullable
  public Fragment findFragmentByTag(@Nullable String paramString) {
    if (paramString != null)
      for (int i = this.mAdded.size() - 1; i >= 0; i--) {
        Fragment fragment = (Fragment)this.mAdded.get(i);
        if (fragment != null && paramString.equals(fragment.mTag))
          return fragment; 
      }  
    SparseArray sparseArray = this.mActive;
    if (sparseArray != null && paramString != null)
      for (int i = sparseArray.size() - 1; i >= 0; i--) {
        Fragment fragment = (Fragment)this.mActive.valueAt(i);
        if (fragment != null && paramString.equals(fragment.mTag))
          return fragment; 
      }  
    return null;
  }
  
  public Fragment findFragmentByWho(String paramString) {
    SparseArray sparseArray = this.mActive;
    if (sparseArray != null && paramString != null)
      for (int i = sparseArray.size() - 1; i >= 0; i--) {
        Fragment fragment = (Fragment)this.mActive.valueAt(i);
        if (fragment != null) {
          fragment = fragment.findFragmentByWho(paramString);
          if (fragment != null)
            return fragment; 
        } 
      }  
    return null;
  }
  
  public void freeBackStackIndex(int paramInt) { // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   6: iload_1
    //   7: aconst_null
    //   8: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   11: pop
    //   12: aload_0
    //   13: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   16: ifnonnull -> 30
    //   19: aload_0
    //   20: new java/util/ArrayList
    //   23: dup
    //   24: invokespecial <init> : ()V
    //   27: putfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   30: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   33: ifeq -> 68
    //   36: new java/lang/StringBuilder
    //   39: dup
    //   40: invokespecial <init> : ()V
    //   43: astore_2
    //   44: aload_2
    //   45: ldc_w 'Freeing back stack index '
    //   48: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: pop
    //   52: aload_2
    //   53: iload_1
    //   54: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   57: pop
    //   58: ldc 'FragmentManager'
    //   60: aload_2
    //   61: invokevirtual toString : ()Ljava/lang/String;
    //   64: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   67: pop
    //   68: aload_0
    //   69: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   72: iload_1
    //   73: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   76: invokevirtual add : (Ljava/lang/Object;)Z
    //   79: pop
    //   80: aload_0
    //   81: monitorexit
    //   82: return
    //   83: astore_2
    //   84: aload_0
    //   85: monitorexit
    //   86: aload_2
    //   87: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	83	finally
    //   30	68	83	finally
    //   68	82	83	finally
    //   84	86	83	finally }
  
  int getActiveFragmentCount() {
    SparseArray sparseArray = this.mActive;
    return (sparseArray == null) ? 0 : sparseArray.size();
  }
  
  List<Fragment> getActiveFragments() {
    SparseArray sparseArray = this.mActive;
    if (sparseArray == null)
      return null; 
    int i = sparseArray.size();
    ArrayList arrayList = new ArrayList(i);
    for (byte b = 0; b < i; b++)
      arrayList.add(this.mActive.valueAt(b)); 
    return arrayList;
  }
  
  public FragmentManager.BackStackEntry getBackStackEntryAt(int paramInt) { return (FragmentManager.BackStackEntry)this.mBackStack.get(paramInt); }
  
  public int getBackStackEntryCount() {
    ArrayList arrayList = this.mBackStack;
    return (arrayList != null) ? arrayList.size() : 0;
  }
  
  @Nullable
  public Fragment getFragment(Bundle paramBundle, String paramString) {
    int i = paramBundle.getInt(paramString, -1);
    if (i == -1)
      return null; 
    Fragment fragment = (Fragment)this.mActive.get(i);
    if (fragment == null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Fragment no longer exists for key ");
      stringBuilder.append(paramString);
      stringBuilder.append(": index ");
      stringBuilder.append(i);
      throwException(new IllegalStateException(stringBuilder.toString()));
    } 
    return fragment;
  }
  
  public List<Fragment> getFragments() {
    if (this.mAdded.isEmpty())
      return Collections.emptyList(); 
    synchronized (this.mAdded) {
      return (List)this.mAdded.clone();
    } 
  }
  
  LayoutInflater.Factory2 getLayoutInflaterFactory() { return this; }
  
  @Nullable
  public Fragment getPrimaryNavigationFragment() { return this.mPrimaryNav; }
  
  public void hideFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("hide: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (!paramFragment.mHidden) {
      paramFragment.mHidden = true;
      paramFragment.mHiddenChanged = true ^ paramFragment.mHiddenChanged;
    } 
  }
  
  public boolean isDestroyed() { return this.mDestroyed; }
  
  boolean isStateAtLeast(int paramInt) { return (this.mCurState >= paramInt); }
  
  public boolean isStateSaved() { return (this.mStateSaved || this.mStopped); }
  
  AnimationOrAnimator loadAnimation(Fragment paramFragment, int paramInt1, boolean paramBoolean, int paramInt2) {
    int i = paramFragment.getNextAnim();
    Animation animation = paramFragment.onCreateAnimation(paramInt1, paramBoolean, i);
    if (animation != null)
      return new AnimationOrAnimator(animation); 
    animator = paramFragment.onCreateAnimator(paramInt1, paramBoolean, i);
    if (animator != null)
      return new AnimationOrAnimator(animator); 
    if (i != 0) {
      boolean bool = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(i));
      byte b2 = 0;
      byte b1 = b2;
      if (bool)
        try {
          Animation animation1 = AnimationUtils.loadAnimation(this.mHost.getContext(), i);
          if (animation1 != null)
            return new AnimationOrAnimator(animation1); 
          b1 = 1;
        } catch (android.content.res.Resources.NotFoundException animator) {
          throw animator;
        } catch (RuntimeException animator) {
          b1 = b2;
        }  
      if (b1 == 0)
        try {
          animator = AnimatorInflater.loadAnimator(this.mHost.getContext(), i);
          if (animator != null)
            return new AnimationOrAnimator(animator); 
        } catch (RuntimeException animator) {
          Animation animation1;
          if (!bool) {
            animation1 = AnimationUtils.loadAnimation(this.mHost.getContext(), i);
            if (animation1 != null)
              return new AnimationOrAnimator(animation1); 
          } else {
            throw animation1;
          } 
        }  
    } 
    if (paramInt1 == 0)
      return null; 
    paramInt1 = transitToStyleIndex(paramInt1, paramBoolean);
    if (paramInt1 < 0)
      return null; 
    switch (paramInt1) {
      default:
        paramInt1 = paramInt2;
        if (paramInt2 == 0) {
          paramInt1 = paramInt2;
          if (this.mHost.onHasWindowAnimations())
            paramInt1 = this.mHost.onGetWindowAnimations(); 
        } 
        break;
      case 6:
        return makeFadeAnimation(this.mHost.getContext(), 1.0F, 0.0F);
      case 5:
        return makeFadeAnimation(this.mHost.getContext(), 0.0F, 1.0F);
      case 4:
        return makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 1.075F, 1.0F, 0.0F);
      case 3:
        return makeOpenCloseAnimation(this.mHost.getContext(), 0.975F, 1.0F, 0.0F, 1.0F);
      case 2:
        return makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 0.975F, 1.0F, 0.0F);
      case 1:
        return makeOpenCloseAnimation(this.mHost.getContext(), 1.125F, 1.0F, 0.0F, 1.0F);
    } 
    return (paramInt1 == 0) ? null : null;
  }
  
  void makeActive(Fragment paramFragment) {
    if (paramFragment.mIndex >= 0)
      return; 
    int i = this.mNextFragmentIndex;
    this.mNextFragmentIndex = i + 1;
    paramFragment.setIndex(i, this.mParent);
    if (this.mActive == null)
      this.mActive = new SparseArray(); 
    this.mActive.put(paramFragment.mIndex, paramFragment);
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Allocated fragment index ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
  }
  
  void makeInactive(Fragment paramFragment) {
    if (paramFragment.mIndex < 0)
      return; 
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Freeing fragment index ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    this.mActive.put(paramFragment.mIndex, null);
    paramFragment.initState();
  }
  
  void moveFragmentToExpectedState(Fragment paramFragment) {
    if (paramFragment == null)
      return; 
    int j = this.mCurState;
    int i = j;
    if (paramFragment.mRemoving)
      if (paramFragment.isInBackStack()) {
        i = Math.min(j, 1);
      } else {
        i = Math.min(j, 0);
      }  
    moveToState(paramFragment, i, paramFragment.getNextTransition(), paramFragment.getNextTransitionStyle(), false);
    if (paramFragment.mView != null) {
      Fragment fragment = findFragmentUnder(paramFragment);
      if (fragment != null) {
        View view = fragment.mView;
        ViewGroup viewGroup = paramFragment.mContainer;
        i = viewGroup.indexOfChild(view);
        j = viewGroup.indexOfChild(paramFragment.mView);
        if (j < i) {
          viewGroup.removeViewAt(j);
          viewGroup.addView(paramFragment.mView, i);
        } 
      } 
      if (paramFragment.mIsNewlyAdded && paramFragment.mContainer != null) {
        if (paramFragment.mPostponedAlpha > 0.0F)
          paramFragment.mView.setAlpha(paramFragment.mPostponedAlpha); 
        paramFragment.mPostponedAlpha = 0.0F;
        paramFragment.mIsNewlyAdded = false;
        AnimationOrAnimator animationOrAnimator = loadAnimation(paramFragment, paramFragment.getNextTransition(), true, paramFragment.getNextTransitionStyle());
        if (animationOrAnimator != null) {
          setHWLayerAnimListenerIfAlpha(paramFragment.mView, animationOrAnimator);
          if (animationOrAnimator.animation != null) {
            paramFragment.mView.startAnimation(animationOrAnimator.animation);
          } else {
            animationOrAnimator.animator.setTarget(paramFragment.mView);
            animationOrAnimator.animator.start();
          } 
        } 
      } 
    } 
    if (paramFragment.mHiddenChanged)
      completeShowHideFragment(paramFragment); 
  }
  
  void moveToState(int paramInt, boolean paramBoolean) {
    if (this.mHost != null || paramInt == 0) {
      if (!paramBoolean && paramInt == this.mCurState)
        return; 
      this.mCurState = paramInt;
      if (this.mActive != null) {
        int i = this.mAdded.size();
        for (paramInt = 0; paramInt < i; paramInt++)
          moveFragmentToExpectedState((Fragment)this.mAdded.get(paramInt)); 
        i = this.mActive.size();
        for (paramInt = 0; paramInt < i; paramInt++) {
          Fragment fragment = (Fragment)this.mActive.valueAt(paramInt);
          if (fragment != null && (fragment.mRemoving || fragment.mDetached) && !fragment.mIsNewlyAdded)
            moveFragmentToExpectedState(fragment); 
        } 
        startPendingDeferredFragments();
        if (this.mNeedMenuInvalidate) {
          FragmentHostCallback fragmentHostCallback = this.mHost;
          if (fragmentHostCallback != null && this.mCurState == 4) {
            fragmentHostCallback.onSupportInvalidateOptionsMenu();
            this.mNeedMenuInvalidate = false;
          } 
        } 
      } 
      return;
    } 
    throw new IllegalStateException("No activity");
  }
  
  void moveToState(Fragment paramFragment) { moveToState(paramFragment, this.mCurState, 0, 0, false); }
  
  void moveToState(Fragment paramFragment, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    boolean bool1 = paramFragment.mAdded;
    boolean bool = true;
    if (!bool1 || paramFragment.mDetached) {
      int j = paramInt1;
      paramInt1 = j;
      if (j > 1)
        paramInt1 = 1; 
    } 
    int i = paramInt1;
    if (paramFragment.mRemoving) {
      i = paramInt1;
      if (paramInt1 > paramFragment.mState)
        if (paramFragment.mState == 0 && paramFragment.isInBackStack()) {
          i = 1;
        } else {
          i = paramFragment.mState;
        }  
    } 
    paramInt1 = i;
    if (paramFragment.mDeferStart) {
      paramInt1 = i;
      if (paramFragment.mState < 3) {
        paramInt1 = i;
        if (i > 2)
          paramInt1 = 2; 
      } 
    } 
    if (paramFragment.mState <= paramInt1) {
      if (paramFragment.mFromLayout && !paramFragment.mInLayout)
        return; 
      if (paramFragment.getAnimatingAway() != null || paramFragment.getAnimator() != null) {
        paramFragment.setAnimatingAway(null);
        paramFragment.setAnimator(null);
        moveToState(paramFragment, paramFragment.getStateAfterAnimating(), 0, 0, true);
      } 
      paramInt3 = paramInt1;
      paramInt2 = paramInt1;
      switch (paramFragment.mState) {
        case 0:
          if (paramInt1 > 0) {
            if (DEBUG) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("moveto CREATED: ");
              stringBuilder.append(paramFragment);
              Log.v("FragmentManager", stringBuilder.toString());
            } 
            paramInt2 = paramInt1;
            if (paramFragment.mSavedFragmentState != null) {
              paramFragment.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
              paramFragment.mSavedViewState = paramFragment.mSavedFragmentState.getSparseParcelableArray("android:view_state");
              paramFragment.mTarget = getFragment(paramFragment.mSavedFragmentState, "android:target_state");
              if (paramFragment.mTarget != null)
                paramFragment.mTargetRequestCode = paramFragment.mSavedFragmentState.getInt("android:target_req_state", 0); 
              if (paramFragment.mSavedUserVisibleHint != null) {
                paramFragment.mUserVisibleHint = paramFragment.mSavedUserVisibleHint.booleanValue();
                paramFragment.mSavedUserVisibleHint = null;
              } else {
                paramFragment.mUserVisibleHint = paramFragment.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
              } 
              paramInt2 = paramInt1;
              if (!paramFragment.mUserVisibleHint) {
                paramFragment.mDeferStart = true;
                paramInt2 = paramInt1;
                if (paramInt1 > 2)
                  paramInt2 = 2; 
              } 
            } 
            FragmentManagerImpl fragmentManagerImpl = this.mHost;
            paramFragment.mHost = fragmentManagerImpl;
            Fragment fragment = this.mParent;
            paramFragment.mParentFragment = fragment;
            if (fragment != null) {
              fragmentManagerImpl = fragment.mChildFragmentManager;
            } else {
              fragmentManagerImpl = fragmentManagerImpl.getFragmentManagerImpl();
            } 
            paramFragment.mFragmentManager = fragmentManagerImpl;
            if (paramFragment.mTarget != null)
              if (this.mActive.get(paramFragment.mTarget.mIndex) == paramFragment.mTarget) {
                if (paramFragment.mTarget.mState < 1)
                  moveToState(paramFragment.mTarget, 1, 0, 0, true); 
              } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Fragment ");
                stringBuilder.append(paramFragment);
                stringBuilder.append(" declared target fragment ");
                stringBuilder.append(paramFragment.mTarget);
                stringBuilder.append(" that does not belong to this FragmentManager!");
                throw new IllegalStateException(stringBuilder.toString());
              }  
            dispatchOnFragmentPreAttached(paramFragment, this.mHost.getContext(), false);
            paramFragment.mCalled = false;
            paramFragment.onAttach(this.mHost.getContext());
            if (paramFragment.mCalled) {
              if (paramFragment.mParentFragment == null) {
                this.mHost.onAttachFragment(paramFragment);
              } else {
                paramFragment.mParentFragment.onAttachFragment(paramFragment);
              } 
              dispatchOnFragmentAttached(paramFragment, this.mHost.getContext(), false);
              if (!paramFragment.mIsCreated) {
                dispatchOnFragmentPreCreated(paramFragment, paramFragment.mSavedFragmentState, false);
                paramFragment.performCreate(paramFragment.mSavedFragmentState);
                dispatchOnFragmentCreated(paramFragment, paramFragment.mSavedFragmentState, false);
              } else {
                paramFragment.restoreChildFragmentState(paramFragment.mSavedFragmentState);
                paramFragment.mState = 1;
              } 
              paramFragment.mRetaining = false;
              paramInt1 = paramInt2;
            } else {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Fragment ");
              stringBuilder.append(paramFragment);
              stringBuilder.append(" did not call through to super.onAttach()");
              throw new SuperNotCalledException(stringBuilder.toString());
            } 
          } 
        case 1:
          ensureInflatedFragmentView(paramFragment);
          if (paramInt1 > 1) {
            if (DEBUG) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("moveto ACTIVITY_CREATED: ");
              stringBuilder.append(paramFragment);
              Log.v("FragmentManager", stringBuilder.toString());
            } 
            if (!paramFragment.mFromLayout) {
              ViewGroup viewGroup = null;
              if (paramFragment.mContainerId != 0) {
                if (paramFragment.mContainerId == -1) {
                  viewGroup = new StringBuilder();
                  viewGroup.append("Cannot create fragment ");
                  viewGroup.append(paramFragment);
                  viewGroup.append(" for a container view with no id");
                  throwException(new IllegalArgumentException(viewGroup.toString()));
                } 
                ViewGroup viewGroup1 = (ViewGroup)this.mContainer.onFindViewById(paramFragment.mContainerId);
                if (viewGroup1 == null && !paramFragment.mRestored) {
                  String str;
                  try {
                    str = paramFragment.getResources().getResourceName(paramFragment.mContainerId);
                  } catch (android.content.res.Resources.NotFoundException null) {
                    str = "unknown";
                  } 
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append("No view found for id 0x");
                  stringBuilder.append(Integer.toHexString(paramFragment.mContainerId));
                  stringBuilder.append(" (");
                  stringBuilder.append(str);
                  stringBuilder.append(") for fragment ");
                  stringBuilder.append(paramFragment);
                  throwException(new IllegalArgumentException(stringBuilder.toString()));
                } 
                viewGroup = viewGroup1;
              } 
              paramFragment.mContainer = viewGroup;
              paramFragment.performCreateView(paramFragment.performGetLayoutInflater(paramFragment.mSavedFragmentState), viewGroup, paramFragment.mSavedFragmentState);
              if (paramFragment.mView != null) {
                paramFragment.mInnerView = paramFragment.mView;
                paramFragment.mView.setSaveFromParentEnabled(false);
                if (viewGroup != null)
                  viewGroup.addView(paramFragment.mView); 
                if (paramFragment.mHidden)
                  paramFragment.mView.setVisibility(8); 
                paramFragment.onViewCreated(paramFragment.mView, paramFragment.mSavedFragmentState);
                dispatchOnFragmentViewCreated(paramFragment, paramFragment.mView, paramFragment.mSavedFragmentState, false);
                if (paramFragment.mView.getVisibility() == 0 && paramFragment.mContainer != null) {
                  paramBoolean = bool;
                } else {
                  paramBoolean = false;
                } 
                paramFragment.mIsNewlyAdded = paramBoolean;
              } else {
                paramFragment.mInnerView = null;
              } 
            } 
            paramFragment.performActivityCreated(paramFragment.mSavedFragmentState);
            dispatchOnFragmentActivityCreated(paramFragment, paramFragment.mSavedFragmentState, false);
            if (paramFragment.mView != null)
              paramFragment.restoreViewState(paramFragment.mSavedFragmentState); 
            paramFragment.mSavedFragmentState = null;
          } 
          paramInt3 = paramInt1;
        case 2:
          paramInt2 = paramInt3;
          if (paramInt3 > 2) {
            if (DEBUG) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("moveto STARTED: ");
              stringBuilder.append(paramFragment);
              Log.v("FragmentManager", stringBuilder.toString());
            } 
            paramFragment.performStart();
            dispatchOnFragmentStarted(paramFragment, false);
            paramInt2 = paramInt3;
          } 
        case 3:
          paramInt1 = paramInt2;
          if (paramInt2 > 3) {
            if (DEBUG) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("moveto RESUMED: ");
              stringBuilder.append(paramFragment);
              Log.v("FragmentManager", stringBuilder.toString());
            } 
            paramFragment.performResume();
            dispatchOnFragmentResumed(paramFragment, false);
            paramFragment.mSavedFragmentState = null;
            paramFragment.mSavedViewState = null;
            paramInt1 = paramInt2;
          } 
          break;
      } 
      paramInt2 = paramInt1;
    } else if (paramFragment.mState > paramInt1) {
      switch (paramFragment.mState) {
        default:
          paramInt2 = paramInt1;
          break;
        case 4:
          if (paramInt1 < 4) {
            if (DEBUG) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("movefrom RESUMED: ");
              stringBuilder.append(paramFragment);
              Log.v("FragmentManager", stringBuilder.toString());
            } 
            paramFragment.performPause();
            dispatchOnFragmentPaused(paramFragment, false);
          } 
        case 3:
          if (paramInt1 < 3) {
            if (DEBUG) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("movefrom STARTED: ");
              stringBuilder.append(paramFragment);
              Log.v("FragmentManager", stringBuilder.toString());
            } 
            paramFragment.performStop();
            dispatchOnFragmentStopped(paramFragment, false);
          } 
        case 2:
          if (paramInt1 < 2) {
            if (DEBUG) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("movefrom ACTIVITY_CREATED: ");
              stringBuilder.append(paramFragment);
              Log.v("FragmentManager", stringBuilder.toString());
            } 
            if (paramFragment.mView != null && this.mHost.onShouldSaveFragmentState(paramFragment) && paramFragment.mSavedViewState == null)
              saveFragmentViewState(paramFragment); 
            paramFragment.performDestroyView();
            dispatchOnFragmentViewDestroyed(paramFragment, false);
            if (paramFragment.mView != null && paramFragment.mContainer != null) {
              paramFragment.mContainer.endViewTransition(paramFragment.mView);
              paramFragment.mView.clearAnimation();
              AnimationOrAnimator animationOrAnimator = null;
              if (this.mCurState > 0 && !this.mDestroyed && paramFragment.mView.getVisibility() == 0 && paramFragment.mPostponedAlpha >= 0.0F)
                animationOrAnimator = loadAnimation(paramFragment, paramInt2, false, paramInt3); 
              paramFragment.mPostponedAlpha = 0.0F;
              if (animationOrAnimator != null)
                animateRemoveFragment(paramFragment, animationOrAnimator, paramInt1); 
              paramFragment.mContainer.removeView(paramFragment.mView);
            } 
            paramFragment.mContainer = null;
            paramFragment.mView = null;
            paramFragment.mViewLifecycleOwner = null;
            paramFragment.mViewLifecycleOwnerLiveData.setValue(null);
            paramFragment.mInnerView = null;
            paramFragment.mInLayout = false;
          } 
        case 1:
          paramInt2 = paramInt1;
          if (paramInt1 < 1) {
            if (this.mDestroyed)
              if (paramFragment.getAnimatingAway() != null) {
                View view = paramFragment.getAnimatingAway();
                paramFragment.setAnimatingAway(null);
                view.clearAnimation();
              } else if (paramFragment.getAnimator() != null) {
                Animator animator = paramFragment.getAnimator();
                paramFragment.setAnimator(null);
                animator.cancel();
              }  
            if (paramFragment.getAnimatingAway() != null || paramFragment.getAnimator() != null) {
              paramFragment.setStateAfterAnimating(paramInt1);
              paramInt2 = 1;
              break;
            } 
            if (DEBUG) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("movefrom CREATED: ");
              stringBuilder.append(paramFragment);
              Log.v("FragmentManager", stringBuilder.toString());
            } 
            if (!paramFragment.mRetaining) {
              paramFragment.performDestroy();
              dispatchOnFragmentDestroyed(paramFragment, false);
            } else {
              paramFragment.mState = 0;
            } 
            paramFragment.performDetach();
            dispatchOnFragmentDetached(paramFragment, false);
            paramInt2 = paramInt1;
            if (!paramBoolean) {
              if (!paramFragment.mRetaining) {
                makeInactive(paramFragment);
                paramInt2 = paramInt1;
                break;
              } 
              paramFragment.mHost = null;
              paramFragment.mParentFragment = null;
              paramFragment.mFragmentManager = null;
              paramInt2 = paramInt1;
            } 
          } 
          break;
      } 
    } else {
      paramInt2 = paramInt1;
    } 
    if (paramFragment.mState != paramInt2) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("moveToState: Fragment state for ");
      stringBuilder.append(paramFragment);
      stringBuilder.append(" not updated inline; ");
      stringBuilder.append("expected state ");
      stringBuilder.append(paramInt2);
      stringBuilder.append(" found ");
      stringBuilder.append(paramFragment.mState);
      Log.w("FragmentManager", stringBuilder.toString());
      paramFragment.mState = paramInt2;
    } 
  }
  
  public void noteStateNotSaved() {
    this.mSavedNonConfig = null;
    this.mStateSaved = false;
    this.mStopped = false;
    int i = this.mAdded.size();
    for (byte b = 0; b < i; b++) {
      Fragment fragment = (Fragment)this.mAdded.get(b);
      if (fragment != null)
        fragment.noteStateNotSaved(); 
    } 
  }
  
  public View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    if (!"fragment".equals(paramString))
      return null; 
    String str1 = paramAttributeSet.getAttributeValue(null, "class");
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, FragmentTag.Fragment);
    int i = 0;
    if (str1 == null)
      str1 = typedArray.getString(0); 
    int j = typedArray.getResourceId(1, -1);
    String str2 = typedArray.getString(2);
    typedArray.recycle();
    if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), str1))
      return null; 
    if (paramView != null)
      i = paramView.getId(); 
    if (i != -1 || j != -1 || str2 != null) {
      if (j != -1) {
        Fragment fragment = findFragmentById(j);
      } else {
        typedArray = null;
      } 
      StringBuilder stringBuilder1 = typedArray;
      if (typedArray == null) {
        stringBuilder1 = typedArray;
        if (str2 != null)
          stringBuilder1 = findFragmentByTag(str2); 
      } 
      FragmentHostCallback fragmentHostCallback = stringBuilder1;
      if (stringBuilder1 == null) {
        fragmentHostCallback = stringBuilder1;
        if (i != -1)
          fragmentHostCallback = findFragmentById(i); 
      } 
      if (DEBUG) {
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("onCreateView: id=0x");
        stringBuilder2.append(Integer.toHexString(j));
        stringBuilder2.append(" fname=");
        stringBuilder2.append(str1);
        stringBuilder2.append(" existing=");
        stringBuilder2.append(fragmentHostCallback);
        Log.v("FragmentManager", stringBuilder2.toString());
      } 
      if (fragmentHostCallback == null) {
        int k;
        stringBuilder1 = this.mContainer.instantiate(paramContext, str1, null);
        stringBuilder1.mFromLayout = true;
        if (j != 0) {
          k = j;
        } else {
          k = i;
        } 
        stringBuilder1.mFragmentId = k;
        stringBuilder1.mContainerId = i;
        stringBuilder1.mTag = str2;
        stringBuilder1.mInLayout = true;
        stringBuilder1.mFragmentManager = this;
        fragmentHostCallback = this.mHost;
        stringBuilder1.mHost = fragmentHostCallback;
        stringBuilder1.onInflate(fragmentHostCallback.getContext(), paramAttributeSet, stringBuilder1.mSavedFragmentState);
        addFragment(stringBuilder1, true);
      } else if (!fragmentHostCallback.mInLayout) {
        fragmentHostCallback.mInLayout = true;
        fragmentHostCallback.mHost = this.mHost;
        if (!fragmentHostCallback.mRetaining)
          fragmentHostCallback.onInflate(this.mHost.getContext(), paramAttributeSet, fragmentHostCallback.mSavedFragmentState); 
        FragmentHostCallback fragmentHostCallback1 = fragmentHostCallback;
      } else {
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append(paramAttributeSet.getPositionDescription());
        stringBuilder1.append(": Duplicate id 0x");
        stringBuilder1.append(Integer.toHexString(j));
        stringBuilder1.append(", tag ");
        stringBuilder1.append(str2);
        stringBuilder1.append(", or parent id 0x");
        stringBuilder1.append(Integer.toHexString(i));
        stringBuilder1.append(" with another fragment for ");
        stringBuilder1.append(str1);
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      if (this.mCurState < 1 && stringBuilder1.mFromLayout) {
        moveToState(stringBuilder1, 1, 0, 0, false);
      } else {
        moveToState(stringBuilder1);
      } 
      if (stringBuilder1.mView != null) {
        if (j != 0)
          stringBuilder1.mView.setId(j); 
        if (stringBuilder1.mView.getTag() == null)
          stringBuilder1.mView.setTag(str2); 
        return stringBuilder1.mView;
      } 
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Fragment ");
      stringBuilder1.append(str1);
      stringBuilder1.append(" did not create a view.");
      throw new IllegalStateException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramAttributeSet.getPositionDescription());
    stringBuilder.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
    stringBuilder.append(str1);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet) { return onCreateView(null, paramString, paramContext, paramAttributeSet); }
  
  public void performPendingDeferredStart(Fragment paramFragment) {
    if (paramFragment.mDeferStart) {
      if (this.mExecutingActions) {
        this.mHavePendingDeferredStart = true;
        return;
      } 
      paramFragment.mDeferStart = false;
      moveToState(paramFragment, this.mCurState, 0, 0, false);
    } 
  }
  
  public void popBackStack() { enqueueAction(new PopBackStackState(null, -1, 0), false); }
  
  public void popBackStack(int paramInt1, int paramInt2) {
    if (paramInt1 >= 0) {
      enqueueAction(new PopBackStackState(null, paramInt1, paramInt2), false);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad id: ");
    stringBuilder.append(paramInt1);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void popBackStack(@Nullable String paramString, int paramInt) { enqueueAction(new PopBackStackState(paramString, -1, paramInt), false); }
  
  public boolean popBackStackImmediate() {
    checkStateLoss();
    return popBackStackImmediate(null, -1, 0);
  }
  
  public boolean popBackStackImmediate(int paramInt1, int paramInt2) {
    checkStateLoss();
    execPendingActions();
    if (paramInt1 >= 0)
      return popBackStackImmediate(null, paramInt1, paramInt2); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad id: ");
    stringBuilder.append(paramInt1);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public boolean popBackStackImmediate(@Nullable String paramString, int paramInt) {
    checkStateLoss();
    return popBackStackImmediate(paramString, -1, paramInt);
  }
  
  boolean popBackStackState(ArrayList<BackStackRecord> paramArrayList1, ArrayList<Boolean> paramArrayList2, String paramString, int paramInt1, int paramInt2) {
    ArrayList arrayList = this.mBackStack;
    if (arrayList == null)
      return false; 
    if (paramString == null && paramInt1 < 0 && (paramInt2 & true) == 0) {
      paramInt1 = arrayList.size() - 1;
      if (paramInt1 < 0)
        return false; 
      paramArrayList1.add(this.mBackStack.remove(paramInt1));
      paramArrayList2.add(Boolean.valueOf(true));
      return true;
    } 
    int i = -1;
    if (paramString != null || paramInt1 >= 0) {
      int j;
      for (j = this.mBackStack.size() - 1; j >= 0; j--) {
        BackStackRecord backStackRecord = (BackStackRecord)this.mBackStack.get(j);
        if ((paramString != null && paramString.equals(backStackRecord.getName())) || (paramInt1 >= 0 && paramInt1 == backStackRecord.mIndex))
          break; 
      } 
      if (j < 0)
        return false; 
      i = j;
      if ((paramInt2 & true) != 0)
        for (paramInt2 = j - 1;; paramInt2--) {
          i = paramInt2;
          if (paramInt2 >= 0) {
            BackStackRecord backStackRecord = (BackStackRecord)this.mBackStack.get(paramInt2);
            if (paramString == null || !paramString.equals(backStackRecord.getName())) {
              i = paramInt2;
              if (paramInt1 >= 0) {
                i = paramInt2;
                if (paramInt1 == backStackRecord.mIndex)
                  continue; 
              } 
              break;
            } 
            continue;
          } 
          break;
        }  
    } 
    if (i == this.mBackStack.size() - 1)
      return false; 
    for (paramInt1 = this.mBackStack.size() - 1; paramInt1 > i; paramInt1--) {
      paramArrayList1.add(this.mBackStack.remove(paramInt1));
      paramArrayList2.add(Boolean.valueOf(true));
    } 
    return true;
  }
  
  public void putFragment(Bundle paramBundle, String paramString, Fragment paramFragment) {
    if (paramFragment.mIndex < 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Fragment ");
      stringBuilder.append(paramFragment);
      stringBuilder.append(" is not currently in the FragmentManager");
      throwException(new IllegalStateException(stringBuilder.toString()));
    } 
    paramBundle.putInt(paramString, paramFragment.mIndex);
  }
  
  public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks, boolean paramBoolean) { this.mLifecycleCallbacks.add(new FragmentLifecycleCallbacksHolder(paramFragmentLifecycleCallbacks, paramBoolean)); }
  
  public void removeFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("remove: ");
      stringBuilder.append(paramFragment);
      stringBuilder.append(" nesting=");
      stringBuilder.append(paramFragment.mBackStackNesting);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    boolean bool = paramFragment.isInBackStack();
    if (!paramFragment.mDetached || bool ^ true)
      synchronized (this.mAdded) {
        this.mAdded.remove(paramFragment);
        if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
          this.mNeedMenuInvalidate = true; 
        paramFragment.mAdded = false;
        paramFragment.mRemoving = true;
        return;
      }  
  }
  
  public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener) {
    ArrayList arrayList = this.mBackStackChangeListeners;
    if (arrayList != null)
      arrayList.remove(paramOnBackStackChangedListener); 
  }
  
  void reportBackStackChanged() {
    if (this.mBackStackChangeListeners != null)
      for (byte b = 0; b < this.mBackStackChangeListeners.size(); b++)
        ((FragmentManager.OnBackStackChangedListener)this.mBackStackChangeListeners.get(b)).onBackStackChanged();  
  }
  
  void restoreAllState(Parcelable paramParcelable, FragmentManagerNonConfig paramFragmentManagerNonConfig) {
    StringBuilder stringBuilder;
    if (paramParcelable == null)
      return; 
    FragmentManagerState fragmentManagerState = (FragmentManagerState)paramParcelable;
    if (fragmentManagerState.mActive == null)
      return; 
    if (paramFragmentManagerNonConfig != null) {
      boolean bool;
      List list2 = paramFragmentManagerNonConfig.getFragments();
      List list1 = paramFragmentManagerNonConfig.getChildNonConfigs();
      stringBuilder = paramFragmentManagerNonConfig.getViewModelStores();
      if (list2 != null) {
        bool = list2.size();
      } else {
        bool = false;
      } 
      byte b;
      for (b = 0; b < bool; b++) {
        Fragment fragment = (Fragment)list2.get(b);
        if (DEBUG) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("restoreAllState: re-attaching retained ");
          stringBuilder1.append(fragment);
          Log.v("FragmentManager", stringBuilder1.toString());
        } 
        byte b1;
        for (b1 = 0; b1 < fragmentManagerState.mActive.length && (fragmentManagerState.mActive[b1]).mIndex != fragment.mIndex; b1++);
        if (b1 == fragmentManagerState.mActive.length) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("Could not find active fragment with index ");
          stringBuilder1.append(fragment.mIndex);
          throwException(new IllegalStateException(stringBuilder1.toString()));
        } 
        FragmentState fragmentState = fragmentManagerState.mActive[b1];
        fragmentState.mInstance = fragment;
        fragment.mSavedViewState = null;
        fragment.mBackStackNesting = 0;
        fragment.mInLayout = false;
        fragment.mAdded = false;
        fragment.mTarget = null;
        if (fragmentState.mSavedFragmentState != null) {
          fragmentState.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
          fragment.mSavedViewState = fragmentState.mSavedFragmentState.getSparseParcelableArray("android:view_state");
          fragment.mSavedFragmentState = fragmentState.mSavedFragmentState;
        } 
      } 
    } else {
      stringBuilder = null;
      paramParcelable = null;
    } 
    this.mActive = new SparseArray(fragmentManagerState.mActive.length);
    int i;
    for (i = 0; i < fragmentManagerState.mActive.length; i++) {
      FragmentState fragmentState = fragmentManagerState.mActive[i];
      if (fragmentState != null) {
        ViewModelStore viewModelStore;
        FragmentManagerNonConfig fragmentManagerNonConfig;
        if (paramParcelable != null && i < paramParcelable.size()) {
          fragmentManagerNonConfig = (FragmentManagerNonConfig)paramParcelable.get(i);
        } else {
          fragmentManagerNonConfig = null;
        } 
        if (stringBuilder != null && i < stringBuilder.size()) {
          viewModelStore = (ViewModelStore)stringBuilder.get(i);
        } else {
          viewModelStore = null;
        } 
        Fragment fragment = fragmentState.instantiate(this.mHost, this.mContainer, this.mParent, fragmentManagerNonConfig, viewModelStore);
        if (DEBUG) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("restoreAllState: active #");
          stringBuilder1.append(i);
          stringBuilder1.append(": ");
          stringBuilder1.append(fragment);
          Log.v("FragmentManager", stringBuilder1.toString());
        } 
        this.mActive.put(fragment.mIndex, fragment);
        fragmentState.mInstance = null;
      } 
    } 
    if (paramFragmentManagerNonConfig != null) {
      List list = paramFragmentManagerNonConfig.getFragments();
      if (list != null) {
        i = list.size();
      } else {
        i = 0;
      } 
      byte b;
      for (b = 0; b < i; b++) {
        Fragment fragment = (Fragment)list.get(b);
        if (fragment.mTargetIndex >= 0) {
          fragment.mTarget = (Fragment)this.mActive.get(fragment.mTargetIndex);
          if (fragment.mTarget == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Re-attaching retained fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" target no longer exists: ");
            stringBuilder.append(fragment.mTargetIndex);
            Log.w("FragmentManager", stringBuilder.toString());
          } 
        } 
      } 
    } 
    this.mAdded.clear();
    if (fragmentManagerState.mAdded != null) {
      i = 0;
      while (i < fragmentManagerState.mAdded.length) {
        Fragment fragment = (Fragment)this.mActive.get(fragmentManagerState.mAdded[i]);
        if (fragment == null) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("No instantiated fragment for index #");
          stringBuilder1.append(fragmentManagerState.mAdded[i]);
          throwException(new IllegalStateException(stringBuilder1.toString()));
        } 
        fragment.mAdded = true;
        if (DEBUG) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("restoreAllState: added #");
          stringBuilder1.append(i);
          stringBuilder1.append(": ");
          stringBuilder1.append(fragment);
          Log.v("FragmentManager", stringBuilder1.toString());
        } 
        if (!this.mAdded.contains(fragment))
          synchronized (this.mAdded) {
            this.mAdded.add(fragment);
            i++;
            continue;
          }  
        throw new IllegalStateException("Already added!");
      } 
    } 
    if (fragmentManagerState.mBackStack != null) {
      this.mBackStack = new ArrayList(fragmentManagerState.mBackStack.length);
      for (i = 0; i < fragmentManagerState.mBackStack.length; i++) {
        BackStackRecord backStackRecord = fragmentManagerState.mBackStack[i].instantiate(this);
        if (DEBUG) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("restoreAllState: back stack #");
          stringBuilder1.append(i);
          stringBuilder1.append(" (index ");
          stringBuilder1.append(backStackRecord.mIndex);
          stringBuilder1.append("): ");
          stringBuilder1.append(backStackRecord);
          Log.v("FragmentManager", stringBuilder1.toString());
          PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
          backStackRecord.dump("  ", printWriter, false);
          printWriter.close();
        } 
        this.mBackStack.add(backStackRecord);
        if (backStackRecord.mIndex >= 0)
          setBackStackIndex(backStackRecord.mIndex, backStackRecord); 
      } 
    } else {
      this.mBackStack = null;
    } 
    if (fragmentManagerState.mPrimaryNavActiveIndex >= 0)
      this.mPrimaryNav = (Fragment)this.mActive.get(fragmentManagerState.mPrimaryNavActiveIndex); 
    this.mNextFragmentIndex = fragmentManagerState.mNextFragmentIndex;
  }
  
  FragmentManagerNonConfig retainNonConfig() {
    setRetaining(this.mSavedNonConfig);
    return this.mSavedNonConfig;
  }
  
  Parcelable saveAllState() {
    forcePostponedTransactions();
    endAnimatingAwayFragments();
    execPendingActions();
    this.mStateSaved = true;
    this.mSavedNonConfig = null;
    SparseArray sparseArray = this.mActive;
    if (sparseArray != null) {
      StringBuilder stringBuilder1;
      if (sparseArray.size() <= 0)
        return null; 
      int j = this.mActive.size();
      FragmentState[] arrayOfFragmentState = new FragmentState[j];
      int i = 0;
      byte b;
      for (b = 0; b < j; b++) {
        stringBuilder1 = (Fragment)this.mActive.valueAt(b);
        if (stringBuilder1 != null) {
          if (stringBuilder1.mIndex < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failure saving state: active ");
            stringBuilder.append(stringBuilder1);
            stringBuilder.append(" has cleared index: ");
            stringBuilder.append(stringBuilder1.mIndex);
            throwException(new IllegalStateException(stringBuilder.toString()));
          } 
          byte b1 = 1;
          FragmentState fragmentState = new FragmentState(stringBuilder1);
          arrayOfFragmentState[b] = fragmentState;
          if (stringBuilder1.mState > 0 && fragmentState.mSavedFragmentState == null) {
            fragmentState.mSavedFragmentState = saveFragmentBasicState(stringBuilder1);
            if (stringBuilder1.mTarget != null) {
              if (stringBuilder1.mTarget.mIndex < 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failure saving state: ");
                stringBuilder.append(stringBuilder1);
                stringBuilder.append(" has target not in fragment manager: ");
                stringBuilder.append(stringBuilder1.mTarget);
                throwException(new IllegalStateException(stringBuilder.toString()));
              } 
              if (fragmentState.mSavedFragmentState == null)
                fragmentState.mSavedFragmentState = new Bundle(); 
              putFragment(fragmentState.mSavedFragmentState, "android:target_state", stringBuilder1.mTarget);
              if (stringBuilder1.mTargetRequestCode != 0)
                fragmentState.mSavedFragmentState.putInt("android:target_req_state", stringBuilder1.mTargetRequestCode); 
            } 
          } else {
            fragmentState.mSavedFragmentState = stringBuilder1.mSavedFragmentState;
          } 
          i = b1;
          if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Saved state of ");
            stringBuilder.append(stringBuilder1);
            stringBuilder.append(": ");
            stringBuilder.append(fragmentState.mSavedFragmentState);
            Log.v("FragmentManager", stringBuilder.toString());
            i = b1;
          } 
        } 
      } 
      if (i == 0) {
        if (DEBUG)
          Log.v("FragmentManager", "saveAllState: no fragments!"); 
        return null;
      } 
      sparseArray = null;
      BackStackState[] arrayOfBackStackState = null;
      i = this.mAdded.size();
      if (i > 0) {
        int[] arrayOfInt = new int[i];
        b = 0;
        while (true) {
          stringBuilder1 = arrayOfInt;
          if (b < i) {
            arrayOfInt[b] = ((Fragment)this.mAdded.get(b)).mIndex;
            if (arrayOfInt[b] < 0) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Failure saving state: active ");
              stringBuilder.append(this.mAdded.get(b));
              stringBuilder.append(" has cleared index: ");
              stringBuilder.append(arrayOfInt[b]);
              throwException(new IllegalStateException(stringBuilder.toString()));
            } 
            if (DEBUG) {
              stringBuilder1 = new StringBuilder();
              stringBuilder1.append("saveAllState: adding fragment #");
              stringBuilder1.append(b);
              stringBuilder1.append(": ");
              stringBuilder1.append(this.mAdded.get(b));
              Log.v("FragmentManager", stringBuilder1.toString());
            } 
            b++;
            continue;
          } 
          break;
        } 
      } 
      ArrayList arrayList = this.mBackStack;
      StringBuilder stringBuilder2 = arrayOfBackStackState;
      if (arrayList != null) {
        i = arrayList.size();
        stringBuilder2 = arrayOfBackStackState;
        if (i > 0) {
          arrayOfBackStackState = new BackStackState[i];
          b = 0;
          while (true) {
            stringBuilder2 = arrayOfBackStackState;
            if (b < i) {
              arrayOfBackStackState[b] = new BackStackState((BackStackRecord)this.mBackStack.get(b));
              if (DEBUG) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("saveAllState: adding back stack #");
                stringBuilder2.append(b);
                stringBuilder2.append(": ");
                stringBuilder2.append(this.mBackStack.get(b));
                Log.v("FragmentManager", stringBuilder2.toString());
              } 
              b++;
              continue;
            } 
            break;
          } 
        } 
      } 
      arrayOfBackStackState = new FragmentManagerState();
      arrayOfBackStackState.mActive = arrayOfFragmentState;
      arrayOfBackStackState.mAdded = stringBuilder1;
      arrayOfBackStackState.mBackStack = stringBuilder2;
      Fragment fragment = this.mPrimaryNav;
      if (fragment != null)
        arrayOfBackStackState.mPrimaryNavActiveIndex = fragment.mIndex; 
      arrayOfBackStackState.mNextFragmentIndex = this.mNextFragmentIndex;
      saveNonConfig();
      return arrayOfBackStackState;
    } 
    return null;
  }
  
  Bundle saveFragmentBasicState(Fragment paramFragment) {
    Bundle bundle2 = null;
    if (this.mStateBundle == null)
      this.mStateBundle = new Bundle(); 
    paramFragment.performSaveInstanceState(this.mStateBundle);
    dispatchOnFragmentSaveInstanceState(paramFragment, this.mStateBundle, false);
    if (!this.mStateBundle.isEmpty()) {
      bundle2 = this.mStateBundle;
      this.mStateBundle = null;
    } 
    if (paramFragment.mView != null)
      saveFragmentViewState(paramFragment); 
    Bundle bundle1 = bundle2;
    if (paramFragment.mSavedViewState != null) {
      bundle1 = bundle2;
      if (bundle2 == null)
        bundle1 = new Bundle(); 
      bundle1.putSparseParcelableArray("android:view_state", paramFragment.mSavedViewState);
    } 
    bundle2 = bundle1;
    if (!paramFragment.mUserVisibleHint) {
      bundle2 = bundle1;
      if (bundle1 == null)
        bundle2 = new Bundle(); 
      bundle2.putBoolean("android:user_visible_hint", paramFragment.mUserVisibleHint);
    } 
    return bundle2;
  }
  
  @Nullable
  public Fragment.SavedState saveFragmentInstanceState(Fragment paramFragment) {
    if (paramFragment.mIndex < 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Fragment ");
      stringBuilder.append(paramFragment);
      stringBuilder.append(" is not currently in the FragmentManager");
      throwException(new IllegalStateException(stringBuilder.toString()));
    } 
    int i = paramFragment.mState;
    Fragment fragment = null;
    if (i > 0) {
      Fragment.SavedState savedState;
      Bundle bundle = saveFragmentBasicState(paramFragment);
      paramFragment = fragment;
      if (bundle != null)
        savedState = new Fragment.SavedState(bundle); 
      return savedState;
    } 
    return null;
  }
  
  void saveFragmentViewState(Fragment paramFragment) {
    if (paramFragment.mInnerView == null)
      return; 
    SparseArray sparseArray = this.mStateArray;
    if (sparseArray == null) {
      this.mStateArray = new SparseArray();
    } else {
      sparseArray.clear();
    } 
    paramFragment.mInnerView.saveHierarchyState(this.mStateArray);
    if (this.mStateArray.size() > 0) {
      paramFragment.mSavedViewState = this.mStateArray;
      this.mStateArray = null;
    } 
  }
  
  void saveNonConfig() {
    ArrayList arrayList1 = null;
    StringBuilder stringBuilder1 = null;
    ArrayList arrayList2 = null;
    StringBuilder stringBuilder3 = null;
    ArrayList arrayList3 = null;
    StringBuilder stringBuilder2 = null;
    if (this.mActive != null) {
      byte b = 0;
      while (true) {
        arrayList1 = stringBuilder1;
        arrayList2 = stringBuilder3;
        arrayList3 = stringBuilder2;
        if (b < this.mActive.size()) {
          Fragment fragment = (Fragment)this.mActive.valueAt(b);
          arrayList2 = stringBuilder1;
          arrayList3 = stringBuilder3;
          ArrayList arrayList7 = stringBuilder2;
          if (fragment != null) {
            FragmentManagerNonConfig fragmentManagerNonConfig;
            ArrayList arrayList9;
            ArrayList arrayList8;
            arrayList1 = stringBuilder1;
            if (fragment.mRetainInstance) {
              byte b1;
              arrayList2 = stringBuilder1;
              if (stringBuilder1 == null)
                arrayList2 = new ArrayList(); 
              arrayList2.add(fragment);
              if (fragment.mTarget != null) {
                b1 = fragment.mTarget.mIndex;
              } else {
                b1 = -1;
              } 
              fragment.mTargetIndex = b1;
              arrayList1 = arrayList2;
              if (DEBUG) {
                stringBuilder1 = new StringBuilder();
                stringBuilder1.append("retainNonConfig: keeping retained ");
                stringBuilder1.append(fragment);
                Log.v("FragmentManager", stringBuilder1.toString());
                arrayList1 = arrayList2;
              } 
            } 
            if (fragment.mChildFragmentManager != null) {
              fragment.mChildFragmentManager.saveNonConfig();
              fragmentManagerNonConfig = fragment.mChildFragmentManager.mSavedNonConfig;
            } else {
              fragmentManagerNonConfig = fragment.mChildNonConfig;
            } 
            stringBuilder1 = stringBuilder3;
            if (stringBuilder3 == null) {
              stringBuilder1 = stringBuilder3;
              if (fragmentManagerNonConfig != null) {
                arrayList9 = new ArrayList(this.mActive.size());
                byte b1 = 0;
                while (true) {
                  arrayList8 = arrayList9;
                  if (b1 < b) {
                    arrayList9.add(null);
                    b1++;
                    continue;
                  } 
                  break;
                } 
              } 
            } 
            if (arrayList8 != null)
              arrayList8.add(fragmentManagerNonConfig); 
            stringBuilder3 = stringBuilder2;
            if (stringBuilder2 == null) {
              stringBuilder3 = stringBuilder2;
              if (fragment.mViewModelStore != null) {
                ArrayList arrayList = new ArrayList(this.mActive.size());
                byte b1 = 0;
                while (true) {
                  arrayList9 = arrayList;
                  if (b1 < b) {
                    arrayList.add(null);
                    b1++;
                    continue;
                  } 
                  break;
                } 
              } 
            } 
            arrayList2 = arrayList1;
            arrayList3 = arrayList8;
            arrayList7 = arrayList9;
            if (arrayList9 != null) {
              arrayList9.add(fragment.mViewModelStore);
              arrayList7 = arrayList9;
              arrayList3 = arrayList8;
              arrayList2 = arrayList1;
            } 
          } 
          b++;
          ArrayList arrayList4 = arrayList2;
          ArrayList arrayList6 = arrayList3;
          ArrayList arrayList5 = arrayList7;
          continue;
        } 
        break;
      } 
    } 
    if (arrayList1 == null && arrayList2 == null && arrayList3 == null) {
      this.mSavedNonConfig = null;
      return;
    } 
    this.mSavedNonConfig = new FragmentManagerNonConfig(arrayList1, arrayList2, arrayList3);
  }
  
  void scheduleCommit() { // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   6: astore_3
    //   7: iconst_0
    //   8: istore_2
    //   9: aload_3
    //   10: ifnull -> 88
    //   13: aload_0
    //   14: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   17: invokevirtual isEmpty : ()Z
    //   20: ifne -> 88
    //   23: iconst_1
    //   24: istore_1
    //   25: goto -> 28
    //   28: aload_0
    //   29: getfield mPendingActions : Ljava/util/ArrayList;
    //   32: ifnull -> 93
    //   35: aload_0
    //   36: getfield mPendingActions : Ljava/util/ArrayList;
    //   39: invokevirtual size : ()I
    //   42: iconst_1
    //   43: if_icmpne -> 93
    //   46: iconst_1
    //   47: istore_2
    //   48: goto -> 93
    //   51: aload_0
    //   52: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   55: invokevirtual getHandler : ()Landroid/os/Handler;
    //   58: aload_0
    //   59: getfield mExecCommit : Ljava/lang/Runnable;
    //   62: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)V
    //   65: aload_0
    //   66: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   69: invokevirtual getHandler : ()Landroid/os/Handler;
    //   72: aload_0
    //   73: getfield mExecCommit : Ljava/lang/Runnable;
    //   76: invokevirtual post : (Ljava/lang/Runnable;)Z
    //   79: pop
    //   80: aload_0
    //   81: monitorexit
    //   82: return
    //   83: astore_3
    //   84: aload_0
    //   85: monitorexit
    //   86: aload_3
    //   87: athrow
    //   88: iconst_0
    //   89: istore_1
    //   90: goto -> 28
    //   93: iload_1
    //   94: ifne -> 51
    //   97: iload_2
    //   98: ifeq -> 104
    //   101: goto -> 51
    //   104: goto -> 80
    // Exception table:
    //   from	to	target	type
    //   2	7	83	finally
    //   13	23	83	finally
    //   28	46	83	finally
    //   51	80	83	finally
    //   80	82	83	finally
    //   84	86	83	finally }
  
  public void setBackStackIndex(int paramInt, BackStackRecord paramBackStackRecord) { // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   6: ifnonnull -> 20
    //   9: aload_0
    //   10: new java/util/ArrayList
    //   13: dup
    //   14: invokespecial <init> : ()V
    //   17: putfield mBackStackIndices : Ljava/util/ArrayList;
    //   20: aload_0
    //   21: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   24: invokevirtual size : ()I
    //   27: istore #4
    //   29: iload #4
    //   31: istore_3
    //   32: iload_1
    //   33: iload #4
    //   35: if_icmpge -> 109
    //   38: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   41: ifeq -> 96
    //   44: new java/lang/StringBuilder
    //   47: dup
    //   48: invokespecial <init> : ()V
    //   51: astore #5
    //   53: aload #5
    //   55: ldc_w 'Setting back stack index '
    //   58: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: pop
    //   62: aload #5
    //   64: iload_1
    //   65: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   68: pop
    //   69: aload #5
    //   71: ldc_w ' to '
    //   74: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: pop
    //   78: aload #5
    //   80: aload_2
    //   81: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   84: pop
    //   85: ldc 'FragmentManager'
    //   87: aload #5
    //   89: invokevirtual toString : ()Ljava/lang/String;
    //   92: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   95: pop
    //   96: aload_0
    //   97: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   100: iload_1
    //   101: aload_2
    //   102: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   105: pop
    //   106: goto -> 269
    //   109: iload_3
    //   110: iload_1
    //   111: if_icmpge -> 202
    //   114: aload_0
    //   115: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   118: aconst_null
    //   119: invokevirtual add : (Ljava/lang/Object;)Z
    //   122: pop
    //   123: aload_0
    //   124: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   127: ifnonnull -> 141
    //   130: aload_0
    //   131: new java/util/ArrayList
    //   134: dup
    //   135: invokespecial <init> : ()V
    //   138: putfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   141: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   144: ifeq -> 183
    //   147: new java/lang/StringBuilder
    //   150: dup
    //   151: invokespecial <init> : ()V
    //   154: astore #5
    //   156: aload #5
    //   158: ldc_w 'Adding available back stack index '
    //   161: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   164: pop
    //   165: aload #5
    //   167: iload_3
    //   168: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   171: pop
    //   172: ldc 'FragmentManager'
    //   174: aload #5
    //   176: invokevirtual toString : ()Ljava/lang/String;
    //   179: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   182: pop
    //   183: aload_0
    //   184: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   187: iload_3
    //   188: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   191: invokevirtual add : (Ljava/lang/Object;)Z
    //   194: pop
    //   195: iload_3
    //   196: iconst_1
    //   197: iadd
    //   198: istore_3
    //   199: goto -> 109
    //   202: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   205: ifeq -> 260
    //   208: new java/lang/StringBuilder
    //   211: dup
    //   212: invokespecial <init> : ()V
    //   215: astore #5
    //   217: aload #5
    //   219: ldc_w 'Adding back stack index '
    //   222: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: pop
    //   226: aload #5
    //   228: iload_1
    //   229: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   232: pop
    //   233: aload #5
    //   235: ldc_w ' with '
    //   238: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   241: pop
    //   242: aload #5
    //   244: aload_2
    //   245: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   248: pop
    //   249: ldc 'FragmentManager'
    //   251: aload #5
    //   253: invokevirtual toString : ()Ljava/lang/String;
    //   256: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   259: pop
    //   260: aload_0
    //   261: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   264: aload_2
    //   265: invokevirtual add : (Ljava/lang/Object;)Z
    //   268: pop
    //   269: aload_0
    //   270: monitorexit
    //   271: return
    //   272: astore_2
    //   273: aload_0
    //   274: monitorexit
    //   275: aload_2
    //   276: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	272	finally
    //   20	29	272	finally
    //   38	96	272	finally
    //   96	106	272	finally
    //   114	141	272	finally
    //   141	183	272	finally
    //   183	195	272	finally
    //   202	260	272	finally
    //   260	269	272	finally
    //   269	271	272	finally
    //   273	275	272	finally }
  
  public void setPrimaryNavigationFragment(Fragment paramFragment) {
    if (paramFragment == null || (this.mActive.get(paramFragment.mIndex) == paramFragment && (paramFragment.mHost == null || paramFragment.getFragmentManager() == this))) {
      this.mPrimaryNav = paramFragment;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Fragment ");
    stringBuilder.append(paramFragment);
    stringBuilder.append(" is not an active fragment of FragmentManager ");
    stringBuilder.append(this);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void showFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("show: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (paramFragment.mHidden) {
      paramFragment.mHidden = false;
      paramFragment.mHiddenChanged ^= true;
    } 
  }
  
  void startPendingDeferredFragments() {
    if (this.mActive == null)
      return; 
    for (byte b = 0; b < this.mActive.size(); b++) {
      Fragment fragment = (Fragment)this.mActive.valueAt(b);
      if (fragment != null)
        performPendingDeferredStart(fragment); 
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(128);
    stringBuilder.append("FragmentManager{");
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    stringBuilder.append(" in ");
    Fragment fragment = this.mParent;
    if (fragment != null) {
      DebugUtils.buildShortClassTag(fragment, stringBuilder);
    } else {
      DebugUtils.buildShortClassTag(this.mHost, stringBuilder);
    } 
    stringBuilder.append("}}");
    return stringBuilder.toString();
  }
  
  public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks) { // Byte code:
    //   0: aload_0
    //   1: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   4: astore #4
    //   6: aload #4
    //   8: monitorenter
    //   9: iconst_0
    //   10: istore_2
    //   11: aload_0
    //   12: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   15: invokevirtual size : ()I
    //   18: istore_3
    //   19: iload_2
    //   20: iload_3
    //   21: if_icmpge -> 54
    //   24: aload_0
    //   25: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   28: iload_2
    //   29: invokevirtual get : (I)Ljava/lang/Object;
    //   32: checkcast android/support/v4/app/FragmentManagerImpl$FragmentLifecycleCallbacksHolder
    //   35: getfield mCallback : Landroid/support/v4/app/FragmentManager$FragmentLifecycleCallbacks;
    //   38: aload_1
    //   39: if_acmpne -> 64
    //   42: aload_0
    //   43: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   46: iload_2
    //   47: invokevirtual remove : (I)Ljava/lang/Object;
    //   50: pop
    //   51: goto -> 54
    //   54: aload #4
    //   56: monitorexit
    //   57: return
    //   58: astore_1
    //   59: aload #4
    //   61: monitorexit
    //   62: aload_1
    //   63: athrow
    //   64: iload_2
    //   65: iconst_1
    //   66: iadd
    //   67: istore_2
    //   68: goto -> 19
    // Exception table:
    //   from	to	target	type
    //   11	19	58	finally
    //   24	51	58	finally
    //   54	57	58	finally
    //   59	62	58	finally }
  
  private static class AnimateOnHWLayerIfNeededListener extends AnimationListenerWrapper {
    View mView;
    
    AnimateOnHWLayerIfNeededListener(View param1View, Animation.AnimationListener param1AnimationListener) {
      super(param1AnimationListener);
      this.mView = param1View;
    }
    
    @CallSuper
    public void onAnimationEnd(Animation param1Animation) {
      if (ViewCompat.isAttachedToWindow(this.mView) || Build.VERSION.SDK_INT >= 24) {
        this.mView.post(new Runnable() {
              public void run() { FragmentManagerImpl.AnimateOnHWLayerIfNeededListener.this.mView.setLayerType(0, null); }
            });
      } else {
        this.mView.setLayerType(0, null);
      } 
      super.onAnimationEnd(param1Animation);
    }
  }
  
  class null implements Runnable {
    null() {}
    
    public void run() { this.this$0.mView.setLayerType(0, null); }
  }
  
  private static class AnimationListenerWrapper implements Animation.AnimationListener {
    private final Animation.AnimationListener mWrapped;
    
    AnimationListenerWrapper(Animation.AnimationListener param1AnimationListener) { this.mWrapped = param1AnimationListener; }
    
    @CallSuper
    public void onAnimationEnd(Animation param1Animation) {
      Animation.AnimationListener animationListener = this.mWrapped;
      if (animationListener != null)
        animationListener.onAnimationEnd(param1Animation); 
    }
    
    @CallSuper
    public void onAnimationRepeat(Animation param1Animation) {
      Animation.AnimationListener animationListener = this.mWrapped;
      if (animationListener != null)
        animationListener.onAnimationRepeat(param1Animation); 
    }
    
    @CallSuper
    public void onAnimationStart(Animation param1Animation) {
      Animation.AnimationListener animationListener = this.mWrapped;
      if (animationListener != null)
        animationListener.onAnimationStart(param1Animation); 
    }
  }
  
  private static class AnimationOrAnimator {
    public final Animation animation = null;
    
    public final Animator animator;
    
    AnimationOrAnimator(Animator param1Animator) {
      this.animator = param1Animator;
      if (param1Animator != null)
        return; 
      throw new IllegalStateException("Animator cannot be null");
    }
    
    AnimationOrAnimator(Animation param1Animation) {
      this.animator = null;
      if (param1Animation != null)
        return; 
      throw new IllegalStateException("Animation cannot be null");
    }
  }
  
  private static class AnimatorOnHWLayerIfNeededListener extends AnimatorListenerAdapter {
    View mView;
    
    AnimatorOnHWLayerIfNeededListener(View param1View) { this.mView = param1View; }
    
    public void onAnimationEnd(Animator param1Animator) {
      this.mView.setLayerType(0, null);
      param1Animator.removeListener(this);
    }
    
    public void onAnimationStart(Animator param1Animator) { this.mView.setLayerType(2, null); }
  }
  
  private static class EndViewTransitionAnimator extends AnimationSet implements Runnable {
    private boolean mAnimating = true;
    
    private final View mChild;
    
    private boolean mEnded;
    
    private final ViewGroup mParent;
    
    private boolean mTransitionEnded;
    
    EndViewTransitionAnimator(@NonNull Animation param1Animation, @NonNull ViewGroup param1ViewGroup, @NonNull View param1View) {
      super(false);
      this.mParent = param1ViewGroup;
      this.mChild = param1View;
      addAnimation(param1Animation);
      this.mParent.post(this);
    }
    
    public boolean getTransformation(long param1Long, Transformation param1Transformation) {
      this.mAnimating = true;
      if (this.mEnded)
        return true ^ this.mTransitionEnded; 
      if (!super.getTransformation(param1Long, param1Transformation)) {
        this.mEnded = true;
        OneShotPreDrawListener.add(this.mParent, this);
      } 
      return true;
    }
    
    public boolean getTransformation(long param1Long, Transformation param1Transformation, float param1Float) {
      this.mAnimating = true;
      if (this.mEnded)
        return true ^ this.mTransitionEnded; 
      if (!super.getTransformation(param1Long, param1Transformation, param1Float)) {
        this.mEnded = true;
        OneShotPreDrawListener.add(this.mParent, this);
      } 
      return true;
    }
    
    public void run() {
      if (!this.mEnded && this.mAnimating) {
        this.mAnimating = false;
        this.mParent.post(this);
        return;
      } 
      this.mParent.endViewTransition(this.mChild);
      this.mTransitionEnded = true;
    }
  }
  
  private static final class FragmentLifecycleCallbacksHolder {
    final FragmentManager.FragmentLifecycleCallbacks mCallback;
    
    final boolean mRecursive;
    
    FragmentLifecycleCallbacksHolder(FragmentManager.FragmentLifecycleCallbacks param1FragmentLifecycleCallbacks, boolean param1Boolean) {
      this.mCallback = param1FragmentLifecycleCallbacks;
      this.mRecursive = param1Boolean;
    }
  }
  
  static class FragmentTag {
    public static final int[] Fragment = { 16842755, 16842960, 16842961 };
    
    public static final int Fragment_id = 1;
    
    public static final int Fragment_name = 0;
    
    public static final int Fragment_tag = 2;
  }
  
  static interface OpGenerator {
    boolean generateOps(ArrayList<BackStackRecord> param1ArrayList1, ArrayList<Boolean> param1ArrayList2);
  }
  
  private class PopBackStackState implements OpGenerator {
    final int mFlags;
    
    final int mId;
    
    final String mName;
    
    PopBackStackState(String param1String, int param1Int1, int param1Int2) {
      this.mName = param1String;
      this.mId = param1Int1;
      this.mFlags = param1Int2;
    }
    
    public boolean generateOps(ArrayList<BackStackRecord> param1ArrayList1, ArrayList<Boolean> param1ArrayList2) {
      if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null) {
        FragmentManager fragmentManager = FragmentManagerImpl.this.mPrimaryNav.peekChildFragmentManager();
        if (fragmentManager != null && fragmentManager.popBackStackImmediate())
          return false; 
      } 
      return FragmentManagerImpl.this.popBackStackState(param1ArrayList1, param1ArrayList2, this.mName, this.mId, this.mFlags);
    }
  }
  
  static class StartEnterTransitionListener implements Fragment.OnStartEnterTransitionListener {
    final boolean mIsBack;
    
    private int mNumPostponed;
    
    final BackStackRecord mRecord;
    
    StartEnterTransitionListener(BackStackRecord param1BackStackRecord, boolean param1Boolean) {
      this.mIsBack = param1Boolean;
      this.mRecord = param1BackStackRecord;
    }
    
    public void cancelTransaction() { this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false); }
    
    public void completeTransaction() {
      int i = this.mNumPostponed;
      boolean bool1 = false;
      if (i > 0) {
        i = 1;
      } else {
        i = 0;
      } 
      FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
      int j = fragmentManagerImpl.mAdded.size();
      for (byte b = 0; b < j; b++) {
        Fragment fragment = (Fragment)fragmentManagerImpl.mAdded.get(b);
        fragment.setOnStartEnterTransitionListener(null);
        if (i != 0 && fragment.isPostponed())
          fragment.startPostponedEnterTransition(); 
      } 
      fragmentManagerImpl = this.mRecord.mManager;
      BackStackRecord backStackRecord = this.mRecord;
      boolean bool2 = this.mIsBack;
      if (i == 0)
        bool1 = true; 
      fragmentManagerImpl.completeExecute(backStackRecord, bool2, bool1, true);
    }
    
    public boolean isReady() { return (this.mNumPostponed == 0); }
    
    public void onStartEnterTransition() {
      this.mNumPostponed--;
      if (this.mNumPostponed != 0)
        return; 
      this.mRecord.mManager.scheduleCommit();
    }
    
    public void startListening() { this.mNumPostponed++; }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\app\FragmentManagerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */