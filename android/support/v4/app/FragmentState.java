package android.support.v4.app;

import android.arch.lifecycle.ViewModelStore;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

final class FragmentState implements Parcelable {
  public static final Parcelable.Creator<FragmentState> CREATOR = new Parcelable.Creator<FragmentState>() {
      public FragmentState createFromParcel(Parcel param1Parcel) { return new FragmentState(param1Parcel); }
      
      public FragmentState[] newArray(int param1Int) { return new FragmentState[param1Int]; }
    };
  
  final Bundle mArguments;
  
  final String mClassName;
  
  final int mContainerId;
  
  final boolean mDetached;
  
  final int mFragmentId;
  
  final boolean mFromLayout;
  
  final boolean mHidden;
  
  final int mIndex;
  
  Fragment mInstance;
  
  final boolean mRetainInstance;
  
  Bundle mSavedFragmentState;
  
  final String mTag;
  
  FragmentState(Parcel paramParcel) {
    this.mClassName = paramParcel.readString();
    this.mIndex = paramParcel.readInt();
    int i = paramParcel.readInt();
    byte b2 = 1;
    if (i != 0) {
      b1 = 1;
    } else {
      b1 = 0;
    } 
    this.mFromLayout = b1;
    this.mFragmentId = paramParcel.readInt();
    this.mContainerId = paramParcel.readInt();
    this.mTag = paramParcel.readString();
    if (paramParcel.readInt() != 0) {
      b1 = 1;
    } else {
      b1 = 0;
    } 
    this.mRetainInstance = b1;
    if (paramParcel.readInt() != 0) {
      b1 = 1;
    } else {
      b1 = 0;
    } 
    this.mDetached = b1;
    this.mArguments = paramParcel.readBundle();
    if (paramParcel.readInt() != 0) {
      b1 = b2;
    } else {
      b1 = 0;
    } 
    this.mHidden = b1;
    this.mSavedFragmentState = paramParcel.readBundle();
  }
  
  FragmentState(Fragment paramFragment) {
    this.mClassName = paramFragment.getClass().getName();
    this.mIndex = paramFragment.mIndex;
    this.mFromLayout = paramFragment.mFromLayout;
    this.mFragmentId = paramFragment.mFragmentId;
    this.mContainerId = paramFragment.mContainerId;
    this.mTag = paramFragment.mTag;
    this.mRetainInstance = paramFragment.mRetainInstance;
    this.mDetached = paramFragment.mDetached;
    this.mArguments = paramFragment.mArguments;
    this.mHidden = paramFragment.mHidden;
  }
  
  public int describeContents() { return 0; }
  
  public Fragment instantiate(FragmentHostCallback paramFragmentHostCallback, FragmentContainer paramFragmentContainer, Fragment paramFragment, FragmentManagerNonConfig paramFragmentManagerNonConfig, ViewModelStore paramViewModelStore) {
    if (this.mInstance == null) {
      Context context = paramFragmentHostCallback.getContext();
      Bundle bundle2 = this.mArguments;
      if (bundle2 != null)
        bundle2.setClassLoader(context.getClassLoader()); 
      if (paramFragmentContainer != null) {
        this.mInstance = paramFragmentContainer.instantiate(context, this.mClassName, this.mArguments);
      } else {
        this.mInstance = Fragment.instantiate(context, this.mClassName, this.mArguments);
      } 
      Bundle bundle1 = this.mSavedFragmentState;
      if (bundle1 != null) {
        bundle1.setClassLoader(context.getClassLoader());
        this.mInstance.mSavedFragmentState = this.mSavedFragmentState;
      } 
      this.mInstance.setIndex(this.mIndex, paramFragment);
      Fragment fragment1 = this.mInstance;
      fragment1.mFromLayout = this.mFromLayout;
      fragment1.mRestored = true;
      fragment1.mFragmentId = this.mFragmentId;
      fragment1.mContainerId = this.mContainerId;
      fragment1.mTag = this.mTag;
      fragment1.mRetainInstance = this.mRetainInstance;
      fragment1.mDetached = this.mDetached;
      fragment1.mHidden = this.mHidden;
      fragment1.mFragmentManager = paramFragmentHostCallback.mFragmentManager;
      if (FragmentManagerImpl.DEBUG) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Instantiated fragment ");
        stringBuilder.append(this.mInstance);
        Log.v("FragmentManager", stringBuilder.toString());
      } 
    } 
    Fragment fragment = this.mInstance;
    fragment.mChildNonConfig = paramFragmentManagerNonConfig;
    fragment.mViewModelStore = paramViewModelStore;
    return fragment;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) { throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n"); }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\app\FragmentState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */