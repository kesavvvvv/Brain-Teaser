package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public abstract class FragmentStatePagerAdapter extends PagerAdapter {
  private static final boolean DEBUG = false;
  
  private static final String TAG = "FragmentStatePagerAdapt";
  
  private FragmentTransaction mCurTransaction = null;
  
  private Fragment mCurrentPrimaryItem = null;
  
  private final FragmentManager mFragmentManager;
  
  private ArrayList<Fragment> mFragments = new ArrayList();
  
  private ArrayList<Fragment.SavedState> mSavedState = new ArrayList();
  
  public FragmentStatePagerAdapter(FragmentManager paramFragmentManager) { this.mFragmentManager = paramFragmentManager; }
  
  public void destroyItem(@NonNull ViewGroup paramViewGroup, int paramInt, @NonNull Object paramObject) {
    paramObject = (Fragment)paramObject;
    if (this.mCurTransaction == null)
      this.mCurTransaction = this.mFragmentManager.beginTransaction(); 
    while (this.mSavedState.size() <= paramInt)
      this.mSavedState.add(null); 
    ArrayList arrayList = this.mSavedState;
    if (paramObject.isAdded()) {
      Fragment.SavedState savedState = this.mFragmentManager.saveFragmentInstanceState(paramObject);
    } else {
      paramViewGroup = null;
    } 
    arrayList.set(paramInt, paramViewGroup);
    this.mFragments.set(paramInt, null);
    this.mCurTransaction.remove(paramObject);
  }
  
  public void finishUpdate(@NonNull ViewGroup paramViewGroup) {
    FragmentTransaction fragmentTransaction = this.mCurTransaction;
    if (fragmentTransaction != null) {
      fragmentTransaction.commitNowAllowingStateLoss();
      this.mCurTransaction = null;
    } 
  }
  
  public abstract Fragment getItem(int paramInt);
  
  @NonNull
  public Object instantiateItem(@NonNull ViewGroup paramViewGroup, int paramInt) {
    if (this.mFragments.size() > paramInt) {
      Fragment fragment1 = (Fragment)this.mFragments.get(paramInt);
      if (fragment1 != null)
        return fragment1; 
    } 
    if (this.mCurTransaction == null)
      this.mCurTransaction = this.mFragmentManager.beginTransaction(); 
    Fragment fragment = getItem(paramInt);
    if (this.mSavedState.size() > paramInt) {
      Fragment.SavedState savedState = (Fragment.SavedState)this.mSavedState.get(paramInt);
      if (savedState != null)
        fragment.setInitialSavedState(savedState); 
    } 
    while (this.mFragments.size() <= paramInt)
      this.mFragments.add(null); 
    fragment.setMenuVisibility(false);
    fragment.setUserVisibleHint(false);
    this.mFragments.set(paramInt, fragment);
    this.mCurTransaction.add(paramViewGroup.getId(), fragment);
    return fragment;
  }
  
  public boolean isViewFromObject(@NonNull View paramView, @NonNull Object paramObject) { return (((Fragment)paramObject).getView() == paramView); }
  
  public void restoreState(Parcelable paramParcelable, ClassLoader paramClassLoader) {
    if (paramParcelable != null) {
      Bundle bundle = (Bundle)paramParcelable;
      bundle.setClassLoader(paramClassLoader);
      Parcelable[] arrayOfParcelable = bundle.getParcelableArray("states");
      this.mSavedState.clear();
      this.mFragments.clear();
      if (arrayOfParcelable != null)
        for (byte b = 0; b < arrayOfParcelable.length; b++)
          this.mSavedState.add((Fragment.SavedState)arrayOfParcelable[b]);  
      for (String str : bundle.keySet()) {
        if (str.startsWith("f")) {
          int i = Integer.parseInt(str.substring(1));
          Fragment fragment = this.mFragmentManager.getFragment(bundle, str);
          if (fragment != null) {
            while (this.mFragments.size() <= i)
              this.mFragments.add(null); 
            fragment.setMenuVisibility(false);
            this.mFragments.set(i, fragment);
            continue;
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Bad fragment at key ");
          stringBuilder.append(str);
          Log.w("FragmentStatePagerAdapt", stringBuilder.toString());
        } 
      } 
    } 
  }
  
  public Parcelable saveState() {
    Bundle bundle = null;
    if (this.mSavedState.size() > 0) {
      bundle = new Bundle();
      Fragment.SavedState[] arrayOfSavedState = new Fragment.SavedState[this.mSavedState.size()];
      this.mSavedState.toArray(arrayOfSavedState);
      bundle.putParcelableArray("states", arrayOfSavedState);
    } 
    byte b = 0;
    while (b < this.mFragments.size()) {
      Fragment fragment = (Fragment)this.mFragments.get(b);
      Bundle bundle1 = bundle;
      if (fragment != null) {
        bundle1 = bundle;
        if (fragment.isAdded()) {
          bundle1 = bundle;
          if (bundle == null)
            bundle1 = new Bundle(); 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("f");
          stringBuilder.append(b);
          String str = stringBuilder.toString();
          this.mFragmentManager.putFragment(bundle1, str, fragment);
        } 
      } 
      b++;
      bundle = bundle1;
    } 
    return bundle;
  }
  
  public void setPrimaryItem(@NonNull ViewGroup paramViewGroup, int paramInt, @NonNull Object paramObject) {
    Fragment fragment = (Fragment)paramObject;
    paramObject = this.mCurrentPrimaryItem;
    if (fragment != paramObject) {
      if (paramObject != null) {
        paramObject.setMenuVisibility(false);
        this.mCurrentPrimaryItem.setUserVisibleHint(false);
      } 
      fragment.setMenuVisibility(true);
      fragment.setUserVisibleHint(true);
      this.mCurrentPrimaryItem = fragment;
    } 
  }
  
  public void startUpdate(@NonNull ViewGroup paramViewGroup) {
    if (paramViewGroup.getId() != -1)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ViewPager with adapter ");
    stringBuilder.append(this);
    stringBuilder.append(" requires a view id");
    throw new IllegalStateException(stringBuilder.toString());
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\app\FragmentStatePagerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */