package android.support.v4.app;

import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(21)
class FragmentTransitionCompat21 extends FragmentTransitionImpl {
  private static boolean hasSimpleTarget(Transition paramTransition) { return (!isNullOrEmpty(paramTransition.getTargetIds()) || !isNullOrEmpty(paramTransition.getTargetNames()) || !isNullOrEmpty(paramTransition.getTargetTypes())); }
  
  public void addTarget(Object paramObject, View paramView) {
    if (paramObject != null)
      ((Transition)paramObject).addTarget(paramView); 
  }
  
  public void addTargets(Object paramObject, ArrayList<View> paramArrayList) {
    paramObject = (Transition)paramObject;
    if (paramObject == null)
      return; 
    if (paramObject instanceof TransitionSet) {
      paramObject = (TransitionSet)paramObject;
      int i = paramObject.getTransitionCount();
      for (byte b = 0; b < i; b++)
        addTargets(paramObject.getTransitionAt(b), paramArrayList); 
    } else if (!hasSimpleTarget(paramObject) && isNullOrEmpty(paramObject.getTargets())) {
      int i = paramArrayList.size();
      for (byte b = 0; b < i; b++)
        paramObject.addTarget((View)paramArrayList.get(b)); 
    } 
  }
  
  public void beginDelayedTransition(ViewGroup paramViewGroup, Object paramObject) { TransitionManager.beginDelayedTransition(paramViewGroup, (Transition)paramObject); }
  
  public boolean canHandle(Object paramObject) { return paramObject instanceof Transition; }
  
  public Object cloneTransition(Object paramObject) {
    Transition transition = null;
    if (paramObject != null)
      transition = ((Transition)paramObject).clone(); 
    return transition;
  }
  
  public Object mergeTransitionsInSequence(Object paramObject1, Object paramObject2, Object paramObject3) {
    Object object = null;
    paramObject1 = (Transition)paramObject1;
    paramObject2 = (Transition)paramObject2;
    paramObject3 = (Transition)paramObject3;
    if (paramObject1 != null && paramObject2 != null) {
      paramObject1 = (new TransitionSet()).addTransition(paramObject1).addTransition(paramObject2).setOrdering(1);
    } else if (paramObject1 == null) {
      paramObject1 = object;
      if (paramObject2 != null)
        paramObject1 = paramObject2; 
    } 
    if (paramObject3 != null) {
      paramObject2 = new TransitionSet();
      if (paramObject1 != null)
        paramObject2.addTransition(paramObject1); 
      paramObject2.addTransition(paramObject3);
      return paramObject2;
    } 
    return paramObject1;
  }
  
  public Object mergeTransitionsTogether(Object paramObject1, Object paramObject2, Object paramObject3) {
    TransitionSet transitionSet = new TransitionSet();
    if (paramObject1 != null)
      transitionSet.addTransition((Transition)paramObject1); 
    if (paramObject2 != null)
      transitionSet.addTransition((Transition)paramObject2); 
    if (paramObject3 != null)
      transitionSet.addTransition((Transition)paramObject3); 
    return transitionSet;
  }
  
  public void removeTarget(Object paramObject, View paramView) {
    if (paramObject != null)
      ((Transition)paramObject).removeTarget(paramView); 
  }
  
  public void replaceTargets(Object paramObject, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2) {
    paramObject = (Transition)paramObject;
    if (paramObject instanceof TransitionSet) {
      paramObject = (TransitionSet)paramObject;
      int i = paramObject.getTransitionCount();
      byte b;
      for (b = 0; b < i; b++)
        replaceTargets(paramObject.getTransitionAt(b), paramArrayList1, paramArrayList2); 
    } else if (!hasSimpleTarget(paramObject)) {
      List list = paramObject.getTargets();
      if (list != null && list.size() == paramArrayList1.size() && list.containsAll(paramArrayList1)) {
        if (paramArrayList2 == null) {
          i = 0;
        } else {
          i = paramArrayList2.size();
        } 
        byte b;
        for (b = 0; b < i; b++)
          paramObject.addTarget((View)paramArrayList2.get(b)); 
        int i;
        for (i = paramArrayList1.size() - 1; i >= 0; i--)
          paramObject.removeTarget((View)paramArrayList1.get(i)); 
      } 
    } 
  }
  
  public void scheduleHideFragmentView(Object paramObject, final View fragmentView, final ArrayList<View> exitingViews) { ((Transition)paramObject).addListener(new Transition.TransitionListener() {
          public void onTransitionCancel(Transition param1Transition) {}
          
          public void onTransitionEnd(Transition param1Transition) {
            param1Transition.removeListener(this);
            fragmentView.setVisibility(8);
            int i = exitingViews.size();
            for (byte b = 0; b < i; b++)
              ((View)exitingViews.get(b)).setVisibility(0); 
          }
          
          public void onTransitionPause(Transition param1Transition) {}
          
          public void onTransitionResume(Transition param1Transition) {}
          
          public void onTransitionStart(Transition param1Transition) {}
        }); }
  
  public void scheduleRemoveTargets(Object paramObject1, final Object enterTransition, final ArrayList<View> enteringViews, final Object exitTransition, final ArrayList<View> exitingViews, final Object sharedElementTransition, final ArrayList<View> sharedElementsIn) { ((Transition)paramObject1).addListener(new Transition.TransitionListener() {
          public void onTransitionCancel(Transition param1Transition) {}
          
          public void onTransitionEnd(Transition param1Transition) {}
          
          public void onTransitionPause(Transition param1Transition) {}
          
          public void onTransitionResume(Transition param1Transition) {}
          
          public void onTransitionStart(Transition param1Transition) {
            Object object = enterTransition;
            if (object != null)
              FragmentTransitionCompat21.this.replaceTargets(object, enteringViews, null); 
            object = exitTransition;
            if (object != null)
              FragmentTransitionCompat21.this.replaceTargets(object, exitingViews, null); 
            object = sharedElementTransition;
            if (object != null)
              FragmentTransitionCompat21.this.replaceTargets(object, sharedElementsIn, null); 
          }
        }); }
  
  public void setEpicenter(Object paramObject, final Rect epicenter) {
    if (paramObject != null)
      ((Transition)paramObject).setEpicenterCallback(new Transition.EpicenterCallback() {
            public Rect onGetEpicenter(Transition param1Transition) {
              Rect rect = epicenter;
              return (rect == null || rect.isEmpty()) ? null : epicenter;
            }
          }); 
  }
  
  public void setEpicenter(Object paramObject, View paramView) {
    if (paramView != null) {
      paramObject = (Transition)paramObject;
      final Rect epicenter = new Rect();
      getBoundsOnScreen(paramView, rect);
      paramObject.setEpicenterCallback(new Transition.EpicenterCallback() {
            public Rect onGetEpicenter(Transition param1Transition) { return epicenter; }
          });
    } 
  }
  
  public void setSharedElementTargets(Object paramObject, View paramView, ArrayList<View> paramArrayList) {
    paramObject = (TransitionSet)paramObject;
    List list = paramObject.getTargets();
    list.clear();
    int i = paramArrayList.size();
    byte b;
    for (b = 0; b < i; b++)
      bfsAddViewChildren(list, (View)paramArrayList.get(b)); 
    list.add(paramView);
    paramArrayList.add(paramView);
    addTargets(paramObject, paramArrayList);
  }
  
  public void swapSharedElementTargets(Object paramObject, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2) {
    paramObject = (TransitionSet)paramObject;
    if (paramObject != null) {
      paramObject.getTargets().clear();
      paramObject.getTargets().addAll(paramArrayList2);
      replaceTargets(paramObject, paramArrayList1, paramArrayList2);
    } 
  }
  
  public Object wrapTransitionInSet(Object paramObject) {
    if (paramObject == null)
      return null; 
    TransitionSet transitionSet = new TransitionSet();
    transitionSet.addTransition((Transition)paramObject);
    return transitionSet;
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\app\FragmentTransitionCompat21.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */