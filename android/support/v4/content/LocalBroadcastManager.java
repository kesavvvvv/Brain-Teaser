package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public final class LocalBroadcastManager {
  private static final boolean DEBUG = false;
  
  static final int MSG_EXEC_PENDING_BROADCASTS = 1;
  
  private static final String TAG = "LocalBroadcastManager";
  
  private static LocalBroadcastManager mInstance;
  
  private static final Object mLock = new Object();
  
  private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap();
  
  private final Context mAppContext;
  
  private final Handler mHandler;
  
  private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList();
  
  private final HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> mReceivers = new HashMap();
  
  private LocalBroadcastManager(Context paramContext) {
    this.mAppContext = paramContext;
    this.mHandler = new Handler(paramContext.getMainLooper()) {
        public void handleMessage(Message param1Message) {
          if (param1Message.what != 1) {
            super.handleMessage(param1Message);
            return;
          } 
          LocalBroadcastManager.this.executePendingBroadcasts();
        }
      };
  }
  
  @NonNull
  public static LocalBroadcastManager getInstance(@NonNull Context paramContext) {
    synchronized (mLock) {
      if (mInstance == null)
        mInstance = new LocalBroadcastManager(paramContext.getApplicationContext()); 
      return mInstance;
    } 
  }
  
  void executePendingBroadcasts() {
    while (true) {
      synchronized (this.mReceivers) {
        int i = this.mPendingBroadcasts.size();
        if (i <= 0)
          return; 
        BroadcastRecord[] arrayOfBroadcastRecord = new BroadcastRecord[i];
        this.mPendingBroadcasts.toArray(arrayOfBroadcastRecord);
        this.mPendingBroadcasts.clear();
        for (i = 0; i < arrayOfBroadcastRecord.length; i++) {
          null = arrayOfBroadcastRecord[i];
          int j = null.receivers.size();
          for (byte b = 0; b < j; b++) {
            ReceiverRecord receiverRecord = (ReceiverRecord)null.receivers.get(b);
            if (!receiverRecord.dead)
              receiverRecord.receiver.onReceive(this.mAppContext, null.intent); 
          } 
        } 
      } 
    } 
  }
  
  public void registerReceiver(@NonNull BroadcastReceiver paramBroadcastReceiver, @NonNull IntentFilter paramIntentFilter) {
    synchronized (this.mReceivers) {
      ReceiverRecord receiverRecord = new ReceiverRecord(paramIntentFilter, paramBroadcastReceiver);
      ArrayList arrayList2 = (ArrayList)this.mReceivers.get(paramBroadcastReceiver);
      ArrayList arrayList1 = arrayList2;
      if (arrayList2 == null) {
        arrayList1 = new ArrayList(1);
        this.mReceivers.put(paramBroadcastReceiver, arrayList1);
      } 
      arrayList1.add(receiverRecord);
      for (byte b = 0; b < paramIntentFilter.countActions(); b++) {
        String str = paramIntentFilter.getAction(b);
        arrayList1 = (ArrayList)this.mActions.get(str);
        ArrayList arrayList = arrayList1;
        if (arrayList1 == null) {
          arrayList = new ArrayList(1);
          this.mActions.put(str, arrayList);
        } 
        arrayList.add(receiverRecord);
      } 
      return;
    } 
  }
  
  public boolean sendBroadcast(@NonNull Intent paramIntent) {
    ArrayList arrayList2;
    Set set;
    String str3;
    Uri uri;
    String str2;
    String str1;
    byte b1;
    synchronized (this.mReceivers) {
      str2 = paramIntent.getAction();
      str1 = paramIntent.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
      uri = paramIntent.getData();
      str3 = paramIntent.getScheme();
      set = paramIntent.getCategories();
      if ((paramIntent.getFlags() & 0x8) != 0) {
        b1 = 1;
      } else {
        b1 = 0;
      } 
      if (b1) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Resolving type ");
        stringBuilder.append(str1);
        stringBuilder.append(" scheme ");
        stringBuilder.append(str3);
        stringBuilder.append(" of intent ");
        stringBuilder.append(paramIntent);
        Log.v("LocalBroadcastManager", stringBuilder.toString());
      } 
      arrayList2 = (ArrayList)this.mActions.get(paramIntent.getAction());
      if (arrayList2 != null) {
        if (b1) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Action list: ");
          stringBuilder.append(arrayList2);
          Log.v("LocalBroadcastManager", stringBuilder.toString());
        } 
      } else {
        return false;
      } 
    } 
    ArrayList arrayList1 = null;
    for (byte b2 = 0; b2 < arrayList2.size(); b2++) {
      ReceiverRecord receiverRecord = (ReceiverRecord)arrayList2.get(b2);
      if (b1) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Matching against filter ");
        stringBuilder.append(receiverRecord.filter);
        Log.v("LocalBroadcastManager", stringBuilder.toString());
      } 
      if (receiverRecord.broadcasting) {
        if (b1)
          Log.v("LocalBroadcastManager", "  Filter's target already added"); 
      } else {
        IntentFilter intentFilter = receiverRecord.filter;
        ArrayList arrayList = arrayList1;
        int i = intentFilter.match(str2, str1, str3, uri, set, "LocalBroadcastManager");
        if (i >= 0) {
          if (b1) {
            arrayList1 = new StringBuilder();
            arrayList1.append("  Filter matched!  match=0x");
            arrayList1.append(Integer.toHexString(i));
            Log.v("LocalBroadcastManager", arrayList1.toString());
          } 
          if (arrayList == null)
            arrayList = new ArrayList(); 
          arrayList.add(receiverRecord);
          receiverRecord.broadcasting = true;
          arrayList1 = arrayList;
        } else if (b1) {
          StringBuilder stringBuilder;
          String str;
          switch (i) {
            default:
              str = "unknown reason";
              stringBuilder = new StringBuilder();
              stringBuilder.append("  Filter did not match: ");
              stringBuilder.append(str);
              Log.v("LocalBroadcastManager", stringBuilder.toString());
              break;
            case -1:
              str = "type";
              stringBuilder = new StringBuilder();
              stringBuilder.append("  Filter did not match: ");
              stringBuilder.append(str);
              Log.v("LocalBroadcastManager", stringBuilder.toString());
              break;
            case -2:
              str = "data";
              stringBuilder = new StringBuilder();
              stringBuilder.append("  Filter did not match: ");
              stringBuilder.append(str);
              Log.v("LocalBroadcastManager", stringBuilder.toString());
              break;
            case -3:
              str = "action";
              stringBuilder = new StringBuilder();
              stringBuilder.append("  Filter did not match: ");
              stringBuilder.append(str);
              Log.v("LocalBroadcastManager", stringBuilder.toString());
              break;
            case -4:
              str = "category";
              stringBuilder = new StringBuilder();
              stringBuilder.append("  Filter did not match: ");
              stringBuilder.append(str);
              Log.v("LocalBroadcastManager", stringBuilder.toString());
              break;
          } 
        } 
      } 
    } 
    if (arrayList1 != null) {
      for (b1 = 0; b1 < arrayList1.size(); b1++)
        ((ReceiverRecord)arrayList1.get(b1)).broadcasting = false; 
      this.mPendingBroadcasts.add(new BroadcastRecord(paramIntent, arrayList1));
      if (!this.mHandler.hasMessages(1))
        this.mHandler.sendEmptyMessage(1); 
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_8} */
      return true;
    } 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_8} */
    return false;
  }
  
  public void sendBroadcastSync(@NonNull Intent paramIntent) {
    if (sendBroadcast(paramIntent))
      executePendingBroadcasts(); 
  }
  
  public void unregisterReceiver(@NonNull BroadcastReceiver paramBroadcastReceiver) {
    synchronized (this.mReceivers) {
      ArrayList arrayList = (ArrayList)this.mReceivers.remove(paramBroadcastReceiver);
      if (arrayList == null)
        return; 
      for (int i = arrayList.size() - 1;; i--) {
        if (i >= 0) {
          ReceiverRecord receiverRecord = (ReceiverRecord)arrayList.get(i);
          receiverRecord.dead = true;
          for (byte b = 0; b < receiverRecord.filter.countActions(); b++) {
            String str = receiverRecord.filter.getAction(b);
            ArrayList arrayList1 = (ArrayList)this.mActions.get(str);
            if (arrayList1 != null) {
              int j;
              for (j = arrayList1.size() - 1;; j--) {
                if (j >= 0) {
                  ReceiverRecord receiverRecord1 = (ReceiverRecord)arrayList1.get(j);
                  if (receiverRecord1.receiver == paramBroadcastReceiver) {
                    receiverRecord1.dead = true;
                    arrayList1.remove(j);
                  } 
                } else {
                  if (arrayList1.size() <= 0)
                    this.mActions.remove(str); 
                  break;
                } 
              } 
            } 
          } 
        } else {
          return;
        } 
      } 
    } 
  }
  
  private static final class BroadcastRecord {
    final Intent intent;
    
    final ArrayList<LocalBroadcastManager.ReceiverRecord> receivers;
    
    BroadcastRecord(Intent param1Intent, ArrayList<LocalBroadcastManager.ReceiverRecord> param1ArrayList) {
      this.intent = param1Intent;
      this.receivers = param1ArrayList;
    }
  }
  
  private static final class ReceiverRecord {
    boolean broadcasting;
    
    boolean dead;
    
    final IntentFilter filter;
    
    final BroadcastReceiver receiver;
    
    ReceiverRecord(IntentFilter param1IntentFilter, BroadcastReceiver param1BroadcastReceiver) {
      this.filter = param1IntentFilter;
      this.receiver = param1BroadcastReceiver;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder(128);
      stringBuilder.append("Receiver{");
      stringBuilder.append(this.receiver);
      stringBuilder.append(" filter=");
      stringBuilder.append(this.filter);
      if (this.dead)
        stringBuilder.append(" DEAD"); 
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\content\LocalBroadcastManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */