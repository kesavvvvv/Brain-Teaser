package android.support.v4.app;

import android.graphics.Rect;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class FragmentTransition {
  private static final int[] INVERSE_OPS;
  
  private static final FragmentTransitionImpl PLATFORM_IMPL;
  
  private static final FragmentTransitionImpl SUPPORT_IMPL;
  
  static  {
    Object object;
    INVERSE_OPS = new int[] { 0, 3, 0, 1, 5, 4, 7, 6, 9, 8 };
    if (Build.VERSION.SDK_INT >= 21) {
      object = new FragmentTransitionCompat21();
    } else {
      object = null;
    } 
    PLATFORM_IMPL = object;
    SUPPORT_IMPL = resolveSupportImpl();
  }
  
  private static void addSharedElementsWithMatchingNames(ArrayList<View> paramArrayList, ArrayMap<String, View> paramArrayMap, Collection<String> paramCollection) {
    for (int i = paramArrayMap.size() - 1; i >= 0; i--) {
      View view = (View)paramArrayMap.valueAt(i);
      if (paramCollection.contains(ViewCompat.getTransitionName(view)))
        paramArrayList.add(view); 
    } 
  }
  
  private static void addToFirstInLastOut(BackStackRecord paramBackStackRecord, BackStackRecord.Op paramOp, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean1, boolean paramBoolean2) { // Byte code:
    //   0: aload_1
    //   1: getfield fragment : Landroid/support/v4/app/Fragment;
    //   4: astore #15
    //   6: aload #15
    //   8: ifnonnull -> 12
    //   11: return
    //   12: aload #15
    //   14: getfield mContainerId : I
    //   17: istore #10
    //   19: iload #10
    //   21: ifne -> 25
    //   24: return
    //   25: iload_3
    //   26: ifeq -> 42
    //   29: getstatic android/support/v4/app/FragmentTransition.INVERSE_OPS : [I
    //   32: aload_1
    //   33: getfield cmd : I
    //   36: iaload
    //   37: istore #5
    //   39: goto -> 48
    //   42: aload_1
    //   43: getfield cmd : I
    //   46: istore #5
    //   48: iconst_0
    //   49: istore #6
    //   51: iconst_0
    //   52: istore #7
    //   54: iconst_0
    //   55: istore #9
    //   57: iconst_0
    //   58: istore #8
    //   60: iconst_0
    //   61: istore #13
    //   63: iconst_0
    //   64: istore #12
    //   66: iload #5
    //   68: iconst_1
    //   69: if_icmpeq -> 377
    //   72: iload #5
    //   74: tableswitch default -> 108, 3 -> 279, 4 -> 189, 5 -> 123, 6 -> 279, 7 -> 377
    //   108: iconst_0
    //   109: istore #11
    //   111: iconst_0
    //   112: istore #6
    //   114: iconst_0
    //   115: istore #7
    //   117: iconst_0
    //   118: istore #5
    //   120: goto -> 428
    //   123: iload #4
    //   125: ifeq -> 170
    //   128: iload #12
    //   130: istore #11
    //   132: aload #15
    //   134: getfield mHiddenChanged : Z
    //   137: ifeq -> 167
    //   140: iload #12
    //   142: istore #11
    //   144: aload #15
    //   146: getfield mHidden : Z
    //   149: ifne -> 167
    //   152: iload #12
    //   154: istore #11
    //   156: aload #15
    //   158: getfield mAdded : Z
    //   161: ifeq -> 167
    //   164: iconst_1
    //   165: istore #11
    //   167: goto -> 177
    //   170: aload #15
    //   172: getfield mHidden : Z
    //   175: istore #11
    //   177: iconst_0
    //   178: istore #6
    //   180: iconst_0
    //   181: istore #7
    //   183: iconst_1
    //   184: istore #5
    //   186: goto -> 428
    //   189: iload #4
    //   191: ifeq -> 236
    //   194: iload #6
    //   196: istore #5
    //   198: aload #15
    //   200: getfield mHiddenChanged : Z
    //   203: ifeq -> 233
    //   206: iload #6
    //   208: istore #5
    //   210: aload #15
    //   212: getfield mAdded : Z
    //   215: ifeq -> 233
    //   218: iload #6
    //   220: istore #5
    //   222: aload #15
    //   224: getfield mHidden : Z
    //   227: ifeq -> 233
    //   230: iconst_1
    //   231: istore #5
    //   233: goto -> 263
    //   236: iload #7
    //   238: istore #5
    //   240: aload #15
    //   242: getfield mAdded : Z
    //   245: ifeq -> 263
    //   248: iload #7
    //   250: istore #5
    //   252: aload #15
    //   254: getfield mHidden : Z
    //   257: ifne -> 263
    //   260: iconst_1
    //   261: istore #5
    //   263: iconst_0
    //   264: istore #11
    //   266: iconst_1
    //   267: istore #6
    //   269: iload #5
    //   271: istore #7
    //   273: iconst_0
    //   274: istore #5
    //   276: goto -> 428
    //   279: iload #4
    //   281: ifeq -> 334
    //   284: aload #15
    //   286: getfield mAdded : Z
    //   289: ifne -> 327
    //   292: aload #15
    //   294: getfield mView : Landroid/view/View;
    //   297: ifnull -> 327
    //   300: aload #15
    //   302: getfield mView : Landroid/view/View;
    //   305: invokevirtual getVisibility : ()I
    //   308: ifne -> 327
    //   311: aload #15
    //   313: getfield mPostponedAlpha : F
    //   316: fconst_0
    //   317: fcmpl
    //   318: iflt -> 327
    //   321: iconst_1
    //   322: istore #5
    //   324: goto -> 331
    //   327: iload #9
    //   329: istore #5
    //   331: goto -> 361
    //   334: iload #8
    //   336: istore #5
    //   338: aload #15
    //   340: getfield mAdded : Z
    //   343: ifeq -> 361
    //   346: iload #8
    //   348: istore #5
    //   350: aload #15
    //   352: getfield mHidden : Z
    //   355: ifne -> 361
    //   358: iconst_1
    //   359: istore #5
    //   361: iconst_0
    //   362: istore #11
    //   364: iconst_1
    //   365: istore #6
    //   367: iload #5
    //   369: istore #7
    //   371: iconst_0
    //   372: istore #5
    //   374: goto -> 428
    //   377: iload #4
    //   379: ifeq -> 392
    //   382: aload #15
    //   384: getfield mIsNewlyAdded : Z
    //   387: istore #11
    //   389: goto -> 419
    //   392: iload #13
    //   394: istore #11
    //   396: aload #15
    //   398: getfield mAdded : Z
    //   401: ifne -> 419
    //   404: iload #13
    //   406: istore #11
    //   408: aload #15
    //   410: getfield mHidden : Z
    //   413: ifne -> 419
    //   416: iconst_1
    //   417: istore #11
    //   419: iconst_0
    //   420: istore #6
    //   422: iconst_0
    //   423: istore #7
    //   425: iconst_1
    //   426: istore #5
    //   428: aload_2
    //   429: iload #10
    //   431: invokevirtual get : (I)Ljava/lang/Object;
    //   434: checkcast android/support/v4/app/FragmentTransition$FragmentContainerTransition
    //   437: astore_1
    //   438: iload #11
    //   440: ifeq -> 470
    //   443: aload_1
    //   444: aload_2
    //   445: iload #10
    //   447: invokestatic ensureContainer : (Landroid/support/v4/app/FragmentTransition$FragmentContainerTransition;Landroid/util/SparseArray;I)Landroid/support/v4/app/FragmentTransition$FragmentContainerTransition;
    //   450: astore_1
    //   451: aload_1
    //   452: aload #15
    //   454: putfield lastIn : Landroid/support/v4/app/Fragment;
    //   457: aload_1
    //   458: iload_3
    //   459: putfield lastInIsPop : Z
    //   462: aload_1
    //   463: aload_0
    //   464: putfield lastInTransaction : Landroid/support/v4/app/BackStackRecord;
    //   467: goto -> 470
    //   470: iload #4
    //   472: ifne -> 553
    //   475: iload #5
    //   477: ifeq -> 553
    //   480: aload_1
    //   481: ifnull -> 498
    //   484: aload_1
    //   485: getfield firstOut : Landroid/support/v4/app/Fragment;
    //   488: aload #15
    //   490: if_acmpne -> 498
    //   493: aload_1
    //   494: aconst_null
    //   495: putfield firstOut : Landroid/support/v4/app/Fragment;
    //   498: aload_0
    //   499: getfield mManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   502: astore #14
    //   504: aload #15
    //   506: getfield mState : I
    //   509: iconst_1
    //   510: if_icmpge -> 550
    //   513: aload #14
    //   515: getfield mCurState : I
    //   518: iconst_1
    //   519: if_icmplt -> 550
    //   522: aload_0
    //   523: getfield mReorderingAllowed : Z
    //   526: ifne -> 550
    //   529: aload #14
    //   531: aload #15
    //   533: invokevirtual makeActive : (Landroid/support/v4/app/Fragment;)V
    //   536: aload #14
    //   538: aload #15
    //   540: iconst_1
    //   541: iconst_0
    //   542: iconst_0
    //   543: iconst_0
    //   544: invokevirtual moveToState : (Landroid/support/v4/app/Fragment;IIIZ)V
    //   547: goto -> 553
    //   550: goto -> 553
    //   553: iload #7
    //   555: ifeq -> 605
    //   558: aload_1
    //   559: astore #14
    //   561: aload #14
    //   563: ifnull -> 577
    //   566: aload #14
    //   568: astore_1
    //   569: aload #14
    //   571: getfield firstOut : Landroid/support/v4/app/Fragment;
    //   574: ifnonnull -> 605
    //   577: aload #14
    //   579: aload_2
    //   580: iload #10
    //   582: invokestatic ensureContainer : (Landroid/support/v4/app/FragmentTransition$FragmentContainerTransition;Landroid/util/SparseArray;I)Landroid/support/v4/app/FragmentTransition$FragmentContainerTransition;
    //   585: astore_1
    //   586: aload_1
    //   587: aload #15
    //   589: putfield firstOut : Landroid/support/v4/app/Fragment;
    //   592: aload_1
    //   593: iload_3
    //   594: putfield firstOutIsPop : Z
    //   597: aload_1
    //   598: aload_0
    //   599: putfield firstOutTransaction : Landroid/support/v4/app/BackStackRecord;
    //   602: goto -> 605
    //   605: iload #4
    //   607: ifne -> 633
    //   610: iload #6
    //   612: ifeq -> 633
    //   615: aload_1
    //   616: ifnull -> 633
    //   619: aload_1
    //   620: getfield lastIn : Landroid/support/v4/app/Fragment;
    //   623: aload #15
    //   625: if_acmpne -> 633
    //   628: aload_1
    //   629: aconst_null
    //   630: putfield lastIn : Landroid/support/v4/app/Fragment;
    //   633: return }
  
  public static void calculateFragments(BackStackRecord paramBackStackRecord, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean) {
    int i = paramBackStackRecord.mOps.size();
    for (byte b = 0; b < i; b++)
      addToFirstInLastOut(paramBackStackRecord, (BackStackRecord.Op)paramBackStackRecord.mOps.get(b), paramSparseArray, false, paramBoolean); 
  }
  
  private static ArrayMap<String, String> calculateNameOverrides(int paramInt1, ArrayList<BackStackRecord> paramArrayList1, ArrayList<Boolean> paramArrayList2, int paramInt2, int paramInt3) {
    ArrayMap arrayMap = new ArrayMap();
    while (--paramInt3 >= paramInt2) {
      BackStackRecord backStackRecord = (BackStackRecord)paramArrayList1.get(paramInt3);
      if (backStackRecord.interactsWith(paramInt1)) {
        boolean bool = ((Boolean)paramArrayList2.get(paramInt3)).booleanValue();
        if (backStackRecord.mSharedElementSourceNames != null) {
          ArrayList arrayList2;
          ArrayList arrayList1;
          int i = backStackRecord.mSharedElementSourceNames.size();
          if (bool) {
            arrayList1 = backStackRecord.mSharedElementSourceNames;
            arrayList2 = backStackRecord.mSharedElementTargetNames;
          } else {
            arrayList2 = backStackRecord.mSharedElementSourceNames;
            arrayList1 = backStackRecord.mSharedElementTargetNames;
          } 
          byte b;
          for (b = 0; b < i; b++) {
            String str1 = (String)arrayList2.get(b);
            String str2 = (String)arrayList1.get(b);
            String str3 = (String)arrayMap.remove(str2);
            if (str3 != null) {
              arrayMap.put(str1, str3);
            } else {
              arrayMap.put(str1, str2);
            } 
          } 
        } 
      } 
      paramInt3--;
    } 
    return arrayMap;
  }
  
  public static void calculatePopFragments(BackStackRecord paramBackStackRecord, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean) {
    if (!paramBackStackRecord.mManager.mContainer.onHasView())
      return; 
    for (int i = paramBackStackRecord.mOps.size() - 1; i >= 0; i--)
      addToFirstInLastOut(paramBackStackRecord, (BackStackRecord.Op)paramBackStackRecord.mOps.get(i), paramSparseArray, true, paramBoolean); 
  }
  
  static void callSharedElementStartEnd(Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean1, ArrayMap<String, View> paramArrayMap, boolean paramBoolean2) {
    SharedElementCallback sharedElementCallback;
    if (paramBoolean1) {
      sharedElementCallback = paramFragment2.getEnterTransitionCallback();
    } else {
      sharedElementCallback = sharedElementCallback.getEnterTransitionCallback();
    } 
    if (sharedElementCallback != null) {
      int i;
      ArrayList arrayList1 = new ArrayList();
      ArrayList arrayList2 = new ArrayList();
      if (paramArrayMap == null) {
        i = 0;
      } else {
        i = paramArrayMap.size();
      } 
      byte b;
      for (b = 0; b < i; b++) {
        arrayList2.add(paramArrayMap.keyAt(b));
        arrayList1.add(paramArrayMap.valueAt(b));
      } 
      if (paramBoolean2) {
        sharedElementCallback.onSharedElementStart(arrayList2, arrayList1, null);
        return;
      } 
      sharedElementCallback.onSharedElementEnd(arrayList2, arrayList1, null);
    } 
  }
  
  private static boolean canHandleAll(FragmentTransitionImpl paramFragmentTransitionImpl, List<Object> paramList) {
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      if (!paramFragmentTransitionImpl.canHandle(paramList.get(b)))
        return false; 
      b++;
    } 
    return true;
  }
  
  static ArrayMap<String, View> captureInSharedElements(FragmentTransitionImpl paramFragmentTransitionImpl, ArrayMap<String, String> paramArrayMap, Object paramObject, FragmentContainerTransition paramFragmentContainerTransition) {
    Fragment fragment = paramFragmentContainerTransition.lastIn;
    View view = fragment.getView();
    if (paramArrayMap.isEmpty() || paramObject == null || view == null) {
      paramArrayMap.clear();
      return null;
    } 
    ArrayMap arrayMap = new ArrayMap();
    paramFragmentTransitionImpl.findNamedViews(arrayMap, view);
    ArrayList arrayList = paramFragmentContainerTransition.lastInTransaction;
    if (paramFragmentContainerTransition.lastInIsPop) {
      paramObject = fragment.getExitTransitionCallback();
      arrayList = arrayList.mSharedElementSourceNames;
    } else {
      paramObject = fragment.getEnterTransitionCallback();
      arrayList = arrayList.mSharedElementTargetNames;
    } 
    if (arrayList != null) {
      arrayMap.retainAll(arrayList);
      arrayMap.retainAll(paramArrayMap.values());
    } 
    if (paramObject != null) {
      paramObject.onMapSharedElements(arrayList, arrayMap);
      int i;
      for (i = arrayList.size() - 1; i >= 0; i--) {
        String str = (String)arrayList.get(i);
        paramObject = (View)arrayMap.get(str);
        if (paramObject == null) {
          paramObject = findKeyForValue(paramArrayMap, str);
          if (paramObject != null)
            paramArrayMap.remove(paramObject); 
        } else if (!str.equals(ViewCompat.getTransitionName(paramObject))) {
          str = findKeyForValue(paramArrayMap, str);
          if (str != null)
            paramArrayMap.put(str, ViewCompat.getTransitionName(paramObject)); 
        } 
      } 
    } else {
      retainValues(paramArrayMap, arrayMap);
    } 
    return arrayMap;
  }
  
  private static ArrayMap<String, View> captureOutSharedElements(FragmentTransitionImpl paramFragmentTransitionImpl, ArrayMap<String, String> paramArrayMap, Object paramObject, FragmentContainerTransition paramFragmentContainerTransition) {
    if (paramArrayMap.isEmpty() || paramObject == null) {
      paramArrayMap.clear();
      return null;
    } 
    paramObject = paramFragmentContainerTransition.firstOut;
    ArrayMap arrayMap = new ArrayMap();
    paramFragmentTransitionImpl.findNamedViews(arrayMap, paramObject.getView());
    ArrayList arrayList = paramFragmentContainerTransition.firstOutTransaction;
    if (paramFragmentContainerTransition.firstOutIsPop) {
      paramObject = paramObject.getEnterTransitionCallback();
      arrayList = arrayList.mSharedElementTargetNames;
    } else {
      paramObject = paramObject.getExitTransitionCallback();
      arrayList = arrayList.mSharedElementSourceNames;
    } 
    arrayMap.retainAll(arrayList);
    if (paramObject != null) {
      paramObject.onMapSharedElements(arrayList, arrayMap);
      int i;
      for (i = arrayList.size() - 1; i >= 0; i--) {
        String str = (String)arrayList.get(i);
        paramObject = (View)arrayMap.get(str);
        if (paramObject == null) {
          paramArrayMap.remove(str);
        } else if (!str.equals(ViewCompat.getTransitionName(paramObject))) {
          str = (String)paramArrayMap.remove(str);
          paramArrayMap.put(ViewCompat.getTransitionName(paramObject), str);
        } 
      } 
    } else {
      paramArrayMap.retainAll(arrayMap.keySet());
    } 
    return arrayMap;
  }
  
  private static FragmentTransitionImpl chooseImpl(Fragment paramFragment1, Fragment paramFragment2) {
    ArrayList arrayList = new ArrayList();
    if (paramFragment1 != null) {
      Object object2 = paramFragment1.getExitTransition();
      if (object2 != null)
        arrayList.add(object2); 
      object2 = paramFragment1.getReturnTransition();
      if (object2 != null)
        arrayList.add(object2); 
      Object object1 = paramFragment1.getSharedElementReturnTransition();
      if (object1 != null)
        arrayList.add(object1); 
    } 
    if (paramFragment2 != null) {
      Object object = paramFragment2.getEnterTransition();
      if (object != null)
        arrayList.add(object); 
      object = paramFragment2.getReenterTransition();
      if (object != null)
        arrayList.add(object); 
      object = paramFragment2.getSharedElementEnterTransition();
      if (object != null)
        arrayList.add(object); 
    } 
    if (arrayList.isEmpty())
      return null; 
    FragmentTransitionImpl fragmentTransitionImpl = PLATFORM_IMPL;
    if (fragmentTransitionImpl != null && canHandleAll(fragmentTransitionImpl, arrayList))
      return PLATFORM_IMPL; 
    fragmentTransitionImpl = SUPPORT_IMPL;
    if (fragmentTransitionImpl != null && canHandleAll(fragmentTransitionImpl, arrayList))
      return SUPPORT_IMPL; 
    if (PLATFORM_IMPL == null && SUPPORT_IMPL == null)
      return null; 
    throw new IllegalArgumentException("Invalid Transition types");
  }
  
  static ArrayList<View> configureEnteringExitingViews(FragmentTransitionImpl paramFragmentTransitionImpl, Object paramObject, Fragment paramFragment, ArrayList<View> paramArrayList, View paramView) {
    ArrayList arrayList = null;
    if (paramObject != null) {
      ArrayList arrayList1 = new ArrayList();
      View view = paramFragment.getView();
      if (view != null)
        paramFragmentTransitionImpl.captureTransitioningViews(arrayList1, view); 
      if (paramArrayList != null)
        arrayList1.removeAll(paramArrayList); 
      arrayList = arrayList1;
      if (!arrayList1.isEmpty()) {
        arrayList1.add(paramView);
        paramFragmentTransitionImpl.addTargets(paramObject, arrayList1);
        arrayList = arrayList1;
      } 
    } 
    return arrayList;
  }
  
  private static Object configureSharedElementsOrdered(final FragmentTransitionImpl impl, ViewGroup paramViewGroup, final View nonExistentView, final ArrayMap<String, String> nameOverrides, final FragmentContainerTransition fragments, final ArrayList<View> sharedElementsOut, final ArrayList<View> sharedElementsIn, final Object enterTransition, final Object inEpicenter) {
    final Fragment inFragment = paramFragmentContainerTransition.lastIn;
    final Fragment outFragment = paramFragmentContainerTransition.firstOut;
    if (fragment1 != null) {
      final Object finalSharedElementTransition;
      if (fragment2 == null)
        return null; 
      final boolean inIsPop = paramFragmentContainerTransition.lastInIsPop;
      if (paramArrayMap.isEmpty()) {
        object = null;
      } else {
        object = getSharedElementTransition(paramFragmentTransitionImpl, fragment1, fragment2, bool);
      } 
      ArrayMap arrayMap = captureOutSharedElements(paramFragmentTransitionImpl, paramArrayMap, object, paramFragmentContainerTransition);
      if (paramArrayMap.isEmpty()) {
        object = null;
      } else {
        paramArrayList1.addAll(arrayMap.values());
      } 
      if (paramObject1 == null && paramObject2 == null && object == null)
        return null; 
      callSharedElementStartEnd(fragment1, fragment2, bool, arrayMap, true);
      if (object != null) {
        Rect rect = new Rect();
        paramFragmentTransitionImpl.setSharedElementTargets(object, paramView, paramArrayList1);
        setOutEpicenter(paramFragmentTransitionImpl, object, paramObject2, arrayMap, paramFragmentContainerTransition.firstOutIsPop, paramFragmentContainerTransition.firstOutTransaction);
        if (paramObject1 != null)
          paramFragmentTransitionImpl.setEpicenter(paramObject1, rect); 
        paramObject2 = rect;
      } else {
        paramObject2 = null;
      } 
      OneShotPreDrawListener.add(paramViewGroup, new Runnable() {
            public void run() {
              ArrayMap arrayMap = FragmentTransition.captureInSharedElements(impl, nameOverrides, finalSharedElementTransition, fragments);
              if (arrayMap != null) {
                sharedElementsIn.addAll(arrayMap.values());
                sharedElementsIn.add(nonExistentView);
              } 
              FragmentTransition.callSharedElementStartEnd(inFragment, outFragment, inIsPop, arrayMap, false);
              Object object = finalSharedElementTransition;
              if (object != null) {
                impl.swapSharedElementTargets(object, sharedElementsOut, sharedElementsIn);
                View view = FragmentTransition.getInEpicenterView(arrayMap, fragments, enterTransition, inIsPop);
                if (view != null)
                  impl.getBoundsOnScreen(view, inEpicenter); 
              } 
            }
          });
      return object;
    } 
    return null;
  }
  
  private static Object configureSharedElementsReordered(final FragmentTransitionImpl impl, ViewGroup paramViewGroup, final View epicenter, ArrayMap<String, String> paramArrayMap, final FragmentContainerTransition epicenterView, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2, Object paramObject1, Object paramObject2) {
    final Fragment inFragment = paramFragmentContainerTransition.lastIn;
    final Fragment outFragment = paramFragmentContainerTransition.firstOut;
    if (fragment1 != null)
      fragment1.getView().setVisibility(0); 
    if (fragment1 != null) {
      Object object2;
      Object object1;
      if (fragment2 == null)
        return null; 
      final boolean inIsPop = paramFragmentContainerTransition.lastInIsPop;
      if (paramArrayMap.isEmpty()) {
        object2 = null;
      } else {
        object2 = getSharedElementTransition(paramFragmentTransitionImpl, fragment1, fragment2, bool);
      } 
      ArrayMap arrayMap2 = captureOutSharedElements(paramFragmentTransitionImpl, paramArrayMap, object2, paramFragmentContainerTransition);
      final ArrayMap inSharedElements = captureInSharedElements(paramFragmentTransitionImpl, paramArrayMap, object2, paramFragmentContainerTransition);
      if (paramArrayMap.isEmpty()) {
        if (arrayMap2 != null)
          arrayMap2.clear(); 
        if (arrayMap1 != null)
          arrayMap1.clear(); 
        paramArrayMap = null;
      } else {
        addSharedElementsWithMatchingNames(paramArrayList1, arrayMap2, paramArrayMap.keySet());
        addSharedElementsWithMatchingNames(paramArrayList2, arrayMap1, paramArrayMap.values());
        object1 = object2;
      } 
      if (paramObject1 == null && paramObject2 == null && object1 == null)
        return null; 
      callSharedElementStartEnd(fragment1, fragment2, bool, arrayMap2, true);
      if (object1 != null) {
        paramArrayList2.add(paramView);
        paramFragmentTransitionImpl.setSharedElementTargets(object1, paramView, paramArrayList1);
        setOutEpicenter(paramFragmentTransitionImpl, object1, paramObject2, arrayMap2, paramFragmentContainerTransition.firstOutIsPop, paramFragmentContainerTransition.firstOutTransaction);
        Rect rect = new Rect();
        View view = getInEpicenterView(arrayMap1, paramFragmentContainerTransition, paramObject1, bool);
        if (view != null)
          paramFragmentTransitionImpl.setEpicenter(paramObject1, rect); 
      } else {
        paramView = null;
        paramFragmentContainerTransition = null;
      } 
      OneShotPreDrawListener.add(paramViewGroup, new Runnable() {
            public void run() {
              FragmentTransition.callSharedElementStartEnd(inFragment, outFragment, inIsPop, inSharedElements, false);
              View view = epicenterView;
              if (view != null)
                impl.getBoundsOnScreen(view, epicenter); 
            }
          });
      return object1;
    } 
    return null;
  }
  
  private static void configureTransitionsOrdered(FragmentManagerImpl paramFragmentManagerImpl, int paramInt, FragmentContainerTransition paramFragmentContainerTransition, View paramView, ArrayMap<String, String> paramArrayMap) {
    if (paramFragmentManagerImpl.mContainer.onHasView()) {
      ViewGroup viewGroup = (ViewGroup)paramFragmentManagerImpl.mContainer.onFindViewById(paramInt);
    } else {
      paramFragmentManagerImpl = null;
    } 
    if (paramFragmentManagerImpl == null)
      return; 
    Fragment fragment1 = paramFragmentContainerTransition.lastIn;
    Fragment fragment2 = paramFragmentContainerTransition.firstOut;
    FragmentTransitionImpl fragmentTransitionImpl = chooseImpl(fragment2, fragment1);
    if (fragmentTransitionImpl == null)
      return; 
    boolean bool1 = paramFragmentContainerTransition.lastInIsPop;
    boolean bool2 = paramFragmentContainerTransition.firstOutIsPop;
    Object object3 = getEnterTransition(fragmentTransitionImpl, fragment1, bool1);
    Object object2 = getExitTransition(fragmentTransitionImpl, fragment2, bool2);
    ArrayList arrayList3 = new ArrayList();
    ArrayList arrayList1 = new ArrayList();
    Object object4 = configureSharedElementsOrdered(fragmentTransitionImpl, paramFragmentManagerImpl, paramView, paramArrayMap, paramFragmentContainerTransition, arrayList3, arrayList1, object3, object2);
    if (object3 == null && object4 == null && object2 == null)
      return; 
    ArrayList arrayList2 = configureEnteringExitingViews(fragmentTransitionImpl, object2, fragment2, arrayList3, paramView);
    if (arrayList2 == null || arrayList2.isEmpty())
      object2 = null; 
    fragmentTransitionImpl.addTarget(object3, paramView);
    Object object1 = mergeTransitions(fragmentTransitionImpl, object3, object2, object4, fragment1, paramFragmentContainerTransition.lastInIsPop);
    if (object1 != null) {
      arrayList3 = new ArrayList();
      fragmentTransitionImpl.scheduleRemoveTargets(object1, object3, arrayList3, object2, arrayList2, object4, arrayList1);
      scheduleTargetChange(fragmentTransitionImpl, paramFragmentManagerImpl, fragment1, paramView, arrayList1, object3, arrayList3, object2, arrayList2);
      fragmentTransitionImpl.setNameOverridesOrdered(paramFragmentManagerImpl, arrayList1, paramArrayMap);
      fragmentTransitionImpl.beginDelayedTransition(paramFragmentManagerImpl, object1);
      fragmentTransitionImpl.scheduleNameReset(paramFragmentManagerImpl, arrayList1, paramArrayMap);
      return;
    } 
  }
  
  private static void configureTransitionsReordered(FragmentManagerImpl paramFragmentManagerImpl, int paramInt, FragmentContainerTransition paramFragmentContainerTransition, View paramView, ArrayMap<String, String> paramArrayMap) {
    if (paramFragmentManagerImpl.mContainer.onHasView()) {
      ViewGroup viewGroup = (ViewGroup)paramFragmentManagerImpl.mContainer.onFindViewById(paramInt);
    } else {
      paramFragmentManagerImpl = null;
    } 
    if (paramFragmentManagerImpl == null)
      return; 
    Fragment fragment2 = paramFragmentContainerTransition.lastIn;
    Fragment fragment1 = paramFragmentContainerTransition.firstOut;
    FragmentTransitionImpl fragmentTransitionImpl = chooseImpl(fragment1, fragment2);
    if (fragmentTransitionImpl == null)
      return; 
    boolean bool1 = paramFragmentContainerTransition.lastInIsPop;
    boolean bool2 = paramFragmentContainerTransition.firstOutIsPop;
    ArrayList arrayList2 = new ArrayList();
    ArrayList arrayList3 = new ArrayList();
    Object object3 = getEnterTransition(fragmentTransitionImpl, fragment2, bool1);
    Object object2 = getExitTransition(fragmentTransitionImpl, fragment1, bool2);
    Object object4 = configureSharedElementsReordered(fragmentTransitionImpl, paramFragmentManagerImpl, paramView, paramArrayMap, paramFragmentContainerTransition, arrayList3, arrayList2, object3, object2);
    if (object3 == null && object4 == null && object2 == null)
      return; 
    Object object1 = object2;
    object2 = configureEnteringExitingViews(fragmentTransitionImpl, object1, fragment1, arrayList3, paramView);
    ArrayList arrayList1 = configureEnteringExitingViews(fragmentTransitionImpl, object3, fragment2, arrayList2, paramView);
    setViewVisibility(arrayList1, 4);
    Object object5 = mergeTransitions(fragmentTransitionImpl, object3, object1, object4, fragment2, bool1);
    if (object5 != null) {
      replaceHide(fragmentTransitionImpl, object1, fragment1, object2);
      ArrayList arrayList = fragmentTransitionImpl.prepareSetNameOverridesReordered(arrayList2);
      fragmentTransitionImpl.scheduleRemoveTargets(object5, object3, arrayList1, object1, object2, object4, arrayList2);
      fragmentTransitionImpl.beginDelayedTransition(paramFragmentManagerImpl, object5);
      fragmentTransitionImpl.setNameOverridesReordered(paramFragmentManagerImpl, arrayList3, arrayList2, arrayList, paramArrayMap);
      setViewVisibility(arrayList1, 0);
      fragmentTransitionImpl.swapSharedElementTargets(object4, arrayList3, arrayList2);
      return;
    } 
  }
  
  private static FragmentContainerTransition ensureContainer(FragmentContainerTransition paramFragmentContainerTransition, SparseArray<FragmentContainerTransition> paramSparseArray, int paramInt) {
    FragmentContainerTransition fragmentContainerTransition = paramFragmentContainerTransition;
    if (paramFragmentContainerTransition == null) {
      fragmentContainerTransition = new FragmentContainerTransition();
      paramSparseArray.put(paramInt, fragmentContainerTransition);
    } 
    return fragmentContainerTransition;
  }
  
  private static String findKeyForValue(ArrayMap<String, String> paramArrayMap, String paramString) {
    int i = paramArrayMap.size();
    for (byte b = 0; b < i; b++) {
      if (paramString.equals(paramArrayMap.valueAt(b)))
        return (String)paramArrayMap.keyAt(b); 
    } 
    return null;
  }
  
  private static Object getEnterTransition(FragmentTransitionImpl paramFragmentTransitionImpl, Fragment paramFragment, boolean paramBoolean) {
    Object object;
    if (paramFragment == null)
      return null; 
    if (paramBoolean) {
      object = paramFragment.getReenterTransition();
    } else {
      object = object.getEnterTransition();
    } 
    return paramFragmentTransitionImpl.cloneTransition(object);
  }
  
  private static Object getExitTransition(FragmentTransitionImpl paramFragmentTransitionImpl, Fragment paramFragment, boolean paramBoolean) {
    Object object;
    if (paramFragment == null)
      return null; 
    if (paramBoolean) {
      object = paramFragment.getReturnTransition();
    } else {
      object = object.getExitTransition();
    } 
    return paramFragmentTransitionImpl.cloneTransition(object);
  }
  
  static View getInEpicenterView(ArrayMap<String, View> paramArrayMap, FragmentContainerTransition paramFragmentContainerTransition, Object paramObject, boolean paramBoolean) {
    BackStackRecord backStackRecord = paramFragmentContainerTransition.lastInTransaction;
    if (paramObject != null && paramArrayMap != null && backStackRecord.mSharedElementSourceNames != null && !backStackRecord.mSharedElementSourceNames.isEmpty()) {
      String str;
      if (paramBoolean) {
        str = (String)backStackRecord.mSharedElementSourceNames.get(0);
      } else {
        str = (String)str.mSharedElementTargetNames.get(0);
      } 
      return (View)paramArrayMap.get(str);
    } 
    return null;
  }
  
  private static Object getSharedElementTransition(FragmentTransitionImpl paramFragmentTransitionImpl, Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean) {
    Object object;
    if (paramFragment1 == null || paramFragment2 == null)
      return null; 
    if (paramBoolean) {
      object = paramFragment2.getSharedElementReturnTransition();
    } else {
      object = object.getSharedElementEnterTransition();
    } 
    return paramFragmentTransitionImpl.wrapTransitionInSet(paramFragmentTransitionImpl.cloneTransition(object));
  }
  
  private static Object mergeTransitions(FragmentTransitionImpl paramFragmentTransitionImpl, Object paramObject1, Object paramObject2, Object paramObject3, Fragment paramFragment, boolean paramBoolean) {
    byte b = 1;
    boolean bool = b;
    if (paramObject1 != null) {
      bool = b;
      if (paramObject2 != null) {
        bool = b;
        if (paramFragment != null) {
          if (paramBoolean) {
            paramBoolean = paramFragment.getAllowReturnTransitionOverlap();
          } else {
            paramBoolean = paramFragment.getAllowEnterTransitionOverlap();
          } 
          bool = paramBoolean;
        } 
      } 
    } 
    return bool ? paramFragmentTransitionImpl.mergeTransitionsTogether(paramObject2, paramObject1, paramObject3) : paramFragmentTransitionImpl.mergeTransitionsInSequence(paramObject2, paramObject1, paramObject3);
  }
  
  private static void replaceHide(FragmentTransitionImpl paramFragmentTransitionImpl, Object paramObject, Fragment paramFragment, final ArrayList<View> exitingViews) {
    if (paramFragment != null && paramObject != null && paramFragment.mAdded && paramFragment.mHidden && paramFragment.mHiddenChanged) {
      paramFragment.setHideReplaced(true);
      paramFragmentTransitionImpl.scheduleHideFragmentView(paramObject, paramFragment.getView(), paramArrayList);
      OneShotPreDrawListener.add(paramFragment.mContainer, new Runnable() {
            public void run() { FragmentTransition.setViewVisibility(exitingViews, 4); }
          });
    } 
  }
  
  private static FragmentTransitionImpl resolveSupportImpl() {
    try {
      return (FragmentTransitionImpl)Class.forName("android.support.transition.FragmentTransitionSupport").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    } catch (Exception exception) {
      return null;
    } 
  }
  
  private static void retainValues(ArrayMap<String, String> paramArrayMap1, ArrayMap<String, View> paramArrayMap2) {
    for (int i = paramArrayMap1.size() - 1; i >= 0; i--) {
      if (!paramArrayMap2.containsKey((String)paramArrayMap1.valueAt(i)))
        paramArrayMap1.removeAt(i); 
    } 
  }
  
  private static void scheduleTargetChange(final FragmentTransitionImpl impl, ViewGroup paramViewGroup, final Fragment inFragment, final View nonExistentView, final ArrayList<View> sharedElementsIn, final Object enterTransition, final ArrayList<View> enteringViews, final Object exitTransition, final ArrayList<View> exitingViews) { OneShotPreDrawListener.add(paramViewGroup, new Runnable() {
          public void run() {
            Object object = enterTransition;
            if (object != null) {
              impl.removeTarget(object, nonExistentView);
              object = FragmentTransition.configureEnteringExitingViews(impl, enterTransition, inFragment, sharedElementsIn, nonExistentView);
              enteringViews.addAll(object);
            } 
            if (exitingViews != null) {
              if (exitTransition != null) {
                object = new ArrayList();
                object.add(nonExistentView);
                impl.replaceTargets(exitTransition, exitingViews, object);
              } 
              exitingViews.clear();
              exitingViews.add(nonExistentView);
            } 
          }
        }); }
  
  private static void setOutEpicenter(FragmentTransitionImpl paramFragmentTransitionImpl, Object paramObject1, Object paramObject2, ArrayMap<String, View> paramArrayMap, boolean paramBoolean, BackStackRecord paramBackStackRecord) {
    if (paramBackStackRecord.mSharedElementSourceNames != null && !paramBackStackRecord.mSharedElementSourceNames.isEmpty()) {
      String str;
      if (paramBoolean) {
        str = (String)paramBackStackRecord.mSharedElementTargetNames.get(0);
      } else {
        str = (String)str.mSharedElementSourceNames.get(0);
      } 
      View view = (View)paramArrayMap.get(str);
      paramFragmentTransitionImpl.setEpicenter(paramObject1, view);
      if (paramObject2 != null)
        paramFragmentTransitionImpl.setEpicenter(paramObject2, view); 
    } 
  }
  
  static void setViewVisibility(ArrayList<View> paramArrayList, int paramInt) {
    if (paramArrayList == null)
      return; 
    for (int i = paramArrayList.size() - 1; i >= 0; i--)
      ((View)paramArrayList.get(i)).setVisibility(paramInt); 
  }
  
  static void startTransitions(FragmentManagerImpl paramFragmentManagerImpl, ArrayList<BackStackRecord> paramArrayList1, ArrayList<Boolean> paramArrayList2, int paramInt1, int paramInt2, boolean paramBoolean) {
    if (paramFragmentManagerImpl.mCurState < 1)
      return; 
    SparseArray sparseArray = new SparseArray();
    int i;
    for (i = paramInt1; i < paramInt2; i++) {
      BackStackRecord backStackRecord = (BackStackRecord)paramArrayList1.get(i);
      if (((Boolean)paramArrayList2.get(i)).booleanValue()) {
        calculatePopFragments(backStackRecord, sparseArray, paramBoolean);
      } else {
        calculateFragments(backStackRecord, sparseArray, paramBoolean);
      } 
    } 
    if (sparseArray.size() != 0) {
      View view = new View(paramFragmentManagerImpl.mHost.getContext());
      int j = sparseArray.size();
      for (i = 0; i < j; i++) {
        int k = sparseArray.keyAt(i);
        ArrayMap arrayMap = calculateNameOverrides(k, paramArrayList1, paramArrayList2, paramInt1, paramInt2);
        FragmentContainerTransition fragmentContainerTransition = (FragmentContainerTransition)sparseArray.valueAt(i);
        if (paramBoolean) {
          configureTransitionsReordered(paramFragmentManagerImpl, k, fragmentContainerTransition, view, arrayMap);
        } else {
          configureTransitionsOrdered(paramFragmentManagerImpl, k, fragmentContainerTransition, view, arrayMap);
        } 
      } 
    } 
  }
  
  static boolean supportsTransition() { return (PLATFORM_IMPL != null || SUPPORT_IMPL != null); }
  
  static class FragmentContainerTransition {
    public Fragment firstOut;
    
    public boolean firstOutIsPop;
    
    public BackStackRecord firstOutTransaction;
    
    public Fragment lastIn;
    
    public boolean lastInIsPop;
    
    public BackStackRecord lastInTransaction;
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\app\FragmentTransition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */