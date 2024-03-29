package androidx.versionedparcelable;

import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ArraySet;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseBooleanArray;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class VersionedParcel {
  private static final int EX_BAD_PARCELABLE = -2;
  
  private static final int EX_ILLEGAL_ARGUMENT = -3;
  
  private static final int EX_ILLEGAL_STATE = -5;
  
  private static final int EX_NETWORK_MAIN_THREAD = -6;
  
  private static final int EX_NULL_POINTER = -4;
  
  private static final int EX_PARCELABLE = -9;
  
  private static final int EX_SECURITY = -1;
  
  private static final int EX_UNSUPPORTED_OPERATION = -7;
  
  private static final String TAG = "VersionedParcel";
  
  private static final int TYPE_BINDER = 5;
  
  private static final int TYPE_PARCELABLE = 2;
  
  private static final int TYPE_SERIALIZABLE = 3;
  
  private static final int TYPE_STRING = 4;
  
  private static final int TYPE_VERSIONED_PARCELABLE = 1;
  
  private Exception createException(int paramInt, String paramString) {
    StringBuilder stringBuilder;
    switch (paramInt) {
      default:
        stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown exception code: ");
        stringBuilder.append(paramInt);
        stringBuilder.append(" msg ");
        stringBuilder.append(paramString);
        return new RuntimeException(stringBuilder.toString());
      case -1:
        return new SecurityException(paramString);
      case -2:
        return new BadParcelableException(paramString);
      case -3:
        return new IllegalArgumentException(paramString);
      case -4:
        return new NullPointerException(paramString);
      case -5:
        return new IllegalStateException(paramString);
      case -6:
        return new NetworkOnMainThreadException();
      case -7:
        return new UnsupportedOperationException(paramString);
      case -9:
        break;
    } 
    return (Exception)readParcelable();
  }
  
  private static <T extends VersionedParcelable> Class findParcelClass(T paramT) throws ClassNotFoundException { return findParcelClass(paramT.getClass()); }
  
  private static Class findParcelClass(Class<? extends VersionedParcelable> paramClass) throws ClassNotFoundException { return Class.forName(String.format("%s.%sParcelizer", new Object[] { paramClass.getPackage().getName(), paramClass.getSimpleName() }), false, paramClass.getClassLoader()); }
  
  @NonNull
  protected static Throwable getRootCause(@NonNull Throwable paramThrowable) {
    while (paramThrowable.getCause() != null)
      paramThrowable = paramThrowable.getCause(); 
    return paramThrowable;
  }
  
  private <T> int getType(T paramT) {
    if (paramT instanceof String)
      return 4; 
    if (paramT instanceof Parcelable)
      return 2; 
    if (paramT instanceof VersionedParcelable)
      return 1; 
    if (paramT instanceof Serializable)
      return 3; 
    if (paramT instanceof IBinder)
      return 5; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramT.getClass().getName());
    stringBuilder.append(" cannot be VersionedParcelled");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private <T, S extends Collection<T>> S readCollection(int paramInt, S paramS) {
    paramInt = readInt();
    if (paramInt < 0)
      return null; 
    if (paramInt != 0) {
      int n = readInt();
      if (paramInt < 0)
        return null; 
      int i = paramInt;
      int j = paramInt;
      int k = paramInt;
      int m = paramInt;
      switch (n) {
        default:
          return paramS;
        case 5:
          while (i > 0) {
            paramS.add(readStrongBinder());
            i--;
          } 
          return paramS;
        case 4:
          while (j > 0) {
            paramS.add(readString());
            j--;
          } 
          return paramS;
        case 3:
          while (k > 0) {
            paramS.add(readSerializable());
            k--;
          } 
          return paramS;
        case 2:
          while (m > 0) {
            paramS.add(readParcelable());
            m--;
          } 
          return paramS;
        case 1:
          break;
      } 
      while (paramInt > 0) {
        paramS.add(readVersionedParcelable());
        paramInt--;
      } 
    } 
    return paramS;
  }
  
  private Exception readException(int paramInt, String paramString) { return createException(paramInt, paramString); }
  
  private int readExceptionCode() { return readInt(); }
  
  protected static <T extends VersionedParcelable> T readFromParcel(String paramString, VersionedParcel paramVersionedParcel) {
    try {
      return (T)(VersionedParcelable)Class.forName(paramString, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", new Class[] { VersionedParcel.class }).invoke(null, new Object[] { paramVersionedParcel });
    } catch (IllegalAccessException paramString) {
      throw new RuntimeException("VersionedParcel encountered IllegalAccessException", paramString);
    } catch (InvocationTargetException paramString) {
      if (paramString.getCause() instanceof RuntimeException)
        throw (RuntimeException)paramString.getCause(); 
      throw new RuntimeException("VersionedParcel encountered InvocationTargetException", paramString);
    } catch (NoSuchMethodException paramString) {
      throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", paramString);
    } catch (ClassNotFoundException paramString) {
      throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", paramString);
    } 
  }
  
  private <T> void writeCollection(Collection<T> paramCollection, int paramInt) {
    setOutputField(paramInt);
    if (paramCollection == null) {
      writeInt(-1);
      return;
    } 
    paramInt = paramCollection.size();
    writeInt(paramInt);
    if (paramInt > 0) {
      paramInt = getType(paramCollection.iterator().next());
      writeInt(paramInt);
      switch (paramInt) {
        default:
          return;
        case 5:
          iterator = paramCollection.iterator();
          while (iterator.hasNext())
            writeStrongBinder((IBinder)iterator.next()); 
          return;
        case 4:
          iterator = iterator.iterator();
          while (iterator.hasNext())
            writeString((String)iterator.next()); 
          return;
        case 3:
          iterator = iterator.iterator();
          while (iterator.hasNext())
            writeSerializable((Serializable)iterator.next()); 
          return;
        case 2:
          iterator = iterator.iterator();
          while (iterator.hasNext())
            writeParcelable((Parcelable)iterator.next()); 
          return;
        case 1:
          break;
      } 
      Iterator iterator = iterator.iterator();
      while (iterator.hasNext())
        writeVersionedParcelable((VersionedParcelable)iterator.next()); 
    } 
  }
  
  private void writeSerializable(Serializable paramSerializable) {
    if (paramSerializable == null) {
      writeString(null);
      return;
    } 
    String str = paramSerializable.getClass().getName();
    writeString(str);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objectOutputStream.writeObject(paramSerializable);
      objectOutputStream.close();
      writeByteArray(byteArrayOutputStream.toByteArray());
      return;
    } catch (IOException paramSerializable) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("VersionedParcelable encountered IOException writing serializable object (name = ");
      stringBuilder.append(str);
      stringBuilder.append(")");
      throw new RuntimeException(stringBuilder.toString(), paramSerializable);
    } 
  }
  
  protected static <T extends VersionedParcelable> void writeToParcel(T paramT, VersionedParcel paramVersionedParcel) {
    try {
      findParcelClass(paramT).getDeclaredMethod("write", new Class[] { paramT.getClass(), VersionedParcel.class }).invoke(null, new Object[] { paramT, paramVersionedParcel });
      return;
    } catch (IllegalAccessException paramT) {
      throw new RuntimeException("VersionedParcel encountered IllegalAccessException", paramT);
    } catch (InvocationTargetException paramT) {
      if (paramT.getCause() instanceof RuntimeException)
        throw (RuntimeException)paramT.getCause(); 
      throw new RuntimeException("VersionedParcel encountered InvocationTargetException", paramT);
    } catch (NoSuchMethodException paramT) {
      throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", paramT);
    } catch (ClassNotFoundException paramT) {
      throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", paramT);
    } 
  }
  
  private void writeVersionedParcelableCreator(VersionedParcelable paramVersionedParcelable) {
    try {
      Class clazz = findParcelClass(paramVersionedParcelable.getClass());
      writeString(clazz.getName());
      return;
    } catch (ClassNotFoundException classNotFoundException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramVersionedParcelable.getClass().getSimpleName());
      stringBuilder.append(" does not have a Parcelizer");
      throw new RuntimeException(stringBuilder.toString(), classNotFoundException);
    } 
  }
  
  protected abstract void closeField();
  
  protected abstract VersionedParcel createSubParcel();
  
  public boolean isStream() { return false; }
  
  protected <T> T[] readArray(T[] paramArrayOfT) {
    int i = readInt();
    if (i < 0)
      return null; 
    ArrayList arrayList = new ArrayList(i);
    if (i != 0) {
      int i1 = readInt();
      if (i < 0)
        return null; 
      int j = i;
      int k = i;
      int m = i;
      int n = i;
      switch (i1) {
        default:
          return (T[])arrayList.toArray(paramArrayOfT);
        case 5:
          while (j > 0) {
            arrayList.add(readStrongBinder());
            j--;
          } 
        case 4:
          while (k > 0) {
            arrayList.add(readString());
            k--;
          } 
        case 3:
          while (m > 0) {
            arrayList.add(readSerializable());
            m--;
          } 
        case 2:
          while (n > 0) {
            arrayList.add(readParcelable());
            n--;
          } 
        case 1:
          break;
      } 
      while (i > 0) {
        arrayList.add(readVersionedParcelable());
        i--;
      } 
    } 
  }
  
  public <T> T[] readArray(T[] paramArrayOfT, int paramInt) { return !readField(paramInt) ? paramArrayOfT : (T[])readArray(paramArrayOfT); }
  
  protected abstract boolean readBoolean();
  
  public boolean readBoolean(boolean paramBoolean, int paramInt) { return !readField(paramInt) ? paramBoolean : readBoolean(); }
  
  protected boolean[] readBooleanArray() {
    int i = readInt();
    if (i < 0)
      return null; 
    boolean[] arrayOfBoolean = new boolean[i];
    for (byte b = 0; b < i; b++) {
      boolean bool;
      if (readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      arrayOfBoolean[b] = bool;
    } 
    return arrayOfBoolean;
  }
  
  public boolean[] readBooleanArray(boolean[] paramArrayOfBoolean, int paramInt) { return !readField(paramInt) ? paramArrayOfBoolean : readBooleanArray(); }
  
  protected abstract Bundle readBundle();
  
  public Bundle readBundle(Bundle paramBundle, int paramInt) { return !readField(paramInt) ? paramBundle : readBundle(); }
  
  public byte readByte(byte paramByte, int paramInt) { return !readField(paramInt) ? paramByte : (byte)(readInt() & 0xFF); }
  
  protected abstract byte[] readByteArray();
  
  public byte[] readByteArray(byte[] paramArrayOfByte, int paramInt) { return !readField(paramInt) ? paramArrayOfByte : readByteArray(); }
  
  public char[] readCharArray(char[] paramArrayOfChar, int paramInt) {
    if (!readField(paramInt))
      return paramArrayOfChar; 
    int i = readInt();
    if (i < 0)
      return null; 
    paramArrayOfChar = new char[i];
    for (paramInt = 0; paramInt < i; paramInt++)
      paramArrayOfChar[paramInt] = (char)readInt(); 
    return paramArrayOfChar;
  }
  
  protected abstract double readDouble();
  
  public double readDouble(double paramDouble, int paramInt) { return !readField(paramInt) ? paramDouble : readDouble(); }
  
  protected double[] readDoubleArray() {
    int i = readInt();
    if (i < 0)
      return null; 
    double[] arrayOfDouble = new double[i];
    for (byte b = 0; b < i; b++)
      arrayOfDouble[b] = readDouble(); 
    return arrayOfDouble;
  }
  
  public double[] readDoubleArray(double[] paramArrayOfDouble, int paramInt) { return !readField(paramInt) ? paramArrayOfDouble : readDoubleArray(); }
  
  public Exception readException(Exception paramException, int paramInt) {
    if (!readField(paramInt))
      return paramException; 
    paramInt = readExceptionCode();
    return (paramInt != 0) ? readException(paramInt, readString()) : paramException;
  }
  
  protected abstract boolean readField(int paramInt);
  
  protected abstract float readFloat();
  
  public float readFloat(float paramFloat, int paramInt) { return !readField(paramInt) ? paramFloat : readFloat(); }
  
  protected float[] readFloatArray() {
    int i = readInt();
    if (i < 0)
      return null; 
    float[] arrayOfFloat = new float[i];
    for (byte b = 0; b < i; b++)
      arrayOfFloat[b] = readFloat(); 
    return arrayOfFloat;
  }
  
  public float[] readFloatArray(float[] paramArrayOfFloat, int paramInt) { return !readField(paramInt) ? paramArrayOfFloat : readFloatArray(); }
  
  protected abstract int readInt();
  
  public int readInt(int paramInt1, int paramInt2) { return !readField(paramInt2) ? paramInt1 : readInt(); }
  
  protected int[] readIntArray() {
    int i = readInt();
    if (i < 0)
      return null; 
    int[] arrayOfInt = new int[i];
    for (byte b = 0; b < i; b++)
      arrayOfInt[b] = readInt(); 
    return arrayOfInt;
  }
  
  public int[] readIntArray(int[] paramArrayOfInt, int paramInt) { return !readField(paramInt) ? paramArrayOfInt : readIntArray(); }
  
  public <T> List<T> readList(List<T> paramList, int paramInt) { return !readField(paramInt) ? paramList : (List)readCollection(paramInt, new ArrayList()); }
  
  protected abstract long readLong();
  
  public long readLong(long paramLong, int paramInt) { return !readField(paramInt) ? paramLong : readLong(); }
  
  protected long[] readLongArray() {
    int i = readInt();
    if (i < 0)
      return null; 
    long[] arrayOfLong = new long[i];
    for (byte b = 0; b < i; b++)
      arrayOfLong[b] = readLong(); 
    return arrayOfLong;
  }
  
  public long[] readLongArray(long[] paramArrayOfLong, int paramInt) { return !readField(paramInt) ? paramArrayOfLong : readLongArray(); }
  
  protected abstract <T extends Parcelable> T readParcelable();
  
  public <T extends Parcelable> T readParcelable(T paramT, int paramInt) { return !readField(paramInt) ? paramT : (T)readParcelable(); }
  
  protected Serializable readSerializable() {
    String str = readString();
    if (str == null)
      return null; 
    byteArrayInputStream = new ByteArrayInputStream(readByteArray());
    try {
      return (Serializable)(new ObjectInputStream(byteArrayInputStream) {
          protected Class<?> resolveClass(ObjectStreamClass param1ObjectStreamClass) throws IOException, ClassNotFoundException {
            Class clazz = Class.forName(param1ObjectStreamClass.getName(), false, getClass().getClassLoader());
            return (clazz != null) ? clazz : super.resolveClass(param1ObjectStreamClass);
          }
        }).readObject();
    } catch (IOException byteArrayInputStream) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("VersionedParcelable encountered IOException reading a Serializable object (name = ");
      stringBuilder.append(str);
      stringBuilder.append(")");
      throw new RuntimeException(stringBuilder.toString(), byteArrayInputStream);
    } catch (ClassNotFoundException byteArrayInputStream) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("VersionedParcelable encountered ClassNotFoundException reading a Serializable object (name = ");
      stringBuilder.append(str);
      stringBuilder.append(")");
      throw new RuntimeException(stringBuilder.toString(), byteArrayInputStream);
    } 
  }
  
  public <T> Set<T> readSet(Set<T> paramSet, int paramInt) { return !readField(paramInt) ? paramSet : (Set)readCollection(paramInt, new ArraySet()); }
  
  @RequiresApi(api = 21)
  public Size readSize(Size paramSize, int paramInt) { return !readField(paramInt) ? paramSize : (readBoolean() ? new Size(readInt(), readInt()) : null); }
  
  @RequiresApi(api = 21)
  public SizeF readSizeF(SizeF paramSizeF, int paramInt) { return !readField(paramInt) ? paramSizeF : (readBoolean() ? new SizeF(readFloat(), readFloat()) : null); }
  
  public SparseBooleanArray readSparseBooleanArray(SparseBooleanArray paramSparseBooleanArray, int paramInt) {
    if (!readField(paramInt))
      return paramSparseBooleanArray; 
    int i = readInt();
    if (i < 0)
      return null; 
    paramSparseBooleanArray = new SparseBooleanArray(i);
    for (paramInt = 0; paramInt < i; paramInt++)
      paramSparseBooleanArray.put(readInt(), readBoolean()); 
    return paramSparseBooleanArray;
  }
  
  protected abstract String readString();
  
  public String readString(String paramString, int paramInt) { return !readField(paramInt) ? paramString : readString(); }
  
  protected abstract IBinder readStrongBinder();
  
  public IBinder readStrongBinder(IBinder paramIBinder, int paramInt) { return !readField(paramInt) ? paramIBinder : readStrongBinder(); }
  
  protected <T extends VersionedParcelable> T readVersionedParcelable() {
    String str = readString();
    return (str == null) ? null : (T)readFromParcel(str, createSubParcel());
  }
  
  public <T extends VersionedParcelable> T readVersionedParcelable(T paramT, int paramInt) { return !readField(paramInt) ? paramT : (T)readVersionedParcelable(); }
  
  protected abstract void setOutputField(int paramInt);
  
  public void setSerializationFlags(boolean paramBoolean1, boolean paramBoolean2) {}
  
  protected <T> void writeArray(T[] paramArrayOfT) {
    if (paramArrayOfT == null) {
      writeInt(-1);
      return;
    } 
    int i = paramArrayOfT.length;
    byte b2 = 0;
    byte b3 = 0;
    byte b4 = 0;
    byte b5 = 0;
    byte b1 = 0;
    writeInt(i);
    if (i > 0) {
      int j = getType(paramArrayOfT[0]);
      writeInt(j);
      switch (j) {
        default:
          return;
        case 5:
          while (b1 < i) {
            writeStrongBinder((IBinder)paramArrayOfT[b1]);
            b1++;
          } 
          return;
        case 4:
          while (b2 < i) {
            writeString((String)paramArrayOfT[b2]);
            b2++;
          } 
          return;
        case 3:
          while (b3 < i) {
            writeSerializable((Serializable)paramArrayOfT[b3]);
            b3++;
          } 
          return;
        case 2:
          while (b4 < i) {
            writeParcelable((Parcelable)paramArrayOfT[b4]);
            b4++;
          } 
          return;
        case 1:
          break;
      } 
      while (b5 < i) {
        writeVersionedParcelable((VersionedParcelable)paramArrayOfT[b5]);
        b5++;
      } 
    } 
  }
  
  public <T> void writeArray(T[] paramArrayOfT, int paramInt) {
    setOutputField(paramInt);
    writeArray(paramArrayOfT);
  }
  
  protected abstract void writeBoolean(boolean paramBoolean);
  
  public void writeBoolean(boolean paramBoolean, int paramInt) {
    setOutputField(paramInt);
    writeBoolean(paramBoolean);
  }
  
  protected void writeBooleanArray(boolean[] paramArrayOfBoolean) { throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:553)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n"); }
  
  public void writeBooleanArray(boolean[] paramArrayOfBoolean, int paramInt) {
    setOutputField(paramInt);
    writeBooleanArray(paramArrayOfBoolean);
  }
  
  protected abstract void writeBundle(Bundle paramBundle);
  
  public void writeBundle(Bundle paramBundle, int paramInt) {
    setOutputField(paramInt);
    writeBundle(paramBundle);
  }
  
  public void writeByte(byte paramByte, int paramInt) {
    setOutputField(paramInt);
    writeInt(paramByte);
  }
  
  protected abstract void writeByteArray(byte[] paramArrayOfByte);
  
  public void writeByteArray(byte[] paramArrayOfByte, int paramInt) {
    setOutputField(paramInt);
    writeByteArray(paramArrayOfByte);
  }
  
  protected abstract void writeByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public void writeByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3) {
    setOutputField(paramInt3);
    writeByteArray(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public void writeCharArray(char[] paramArrayOfChar, int paramInt) {
    setOutputField(paramInt);
    if (paramArrayOfChar != null) {
      int i = paramArrayOfChar.length;
      writeInt(i);
      for (paramInt = 0; paramInt < i; paramInt++)
        writeInt(paramArrayOfChar[paramInt]); 
      return;
    } 
    writeInt(-1);
  }
  
  protected abstract void writeDouble(double paramDouble);
  
  public void writeDouble(double paramDouble, int paramInt) {
    setOutputField(paramInt);
    writeDouble(paramDouble);
  }
  
  protected void writeDoubleArray(double[] paramArrayOfDouble) {
    if (paramArrayOfDouble != null) {
      int i = paramArrayOfDouble.length;
      writeInt(i);
      for (byte b = 0; b < i; b++)
        writeDouble(paramArrayOfDouble[b]); 
      return;
    } 
    writeInt(-1);
  }
  
  public void writeDoubleArray(double[] paramArrayOfDouble, int paramInt) {
    setOutputField(paramInt);
    writeDoubleArray(paramArrayOfDouble);
  }
  
  public void writeException(Exception paramException, int paramInt) {
    setOutputField(paramInt);
    if (paramException == null) {
      writeNoException();
      return;
    } 
    paramInt = 0;
    if (paramException instanceof Parcelable && paramException.getClass().getClassLoader() == Parcelable.class.getClassLoader()) {
      paramInt = -9;
    } else if (paramException instanceof SecurityException) {
      paramInt = -1;
    } else if (paramException instanceof BadParcelableException) {
      paramInt = -2;
    } else if (paramException instanceof IllegalArgumentException) {
      paramInt = -3;
    } else if (paramException instanceof NullPointerException) {
      paramInt = -4;
    } else if (paramException instanceof IllegalStateException) {
      paramInt = -5;
    } else if (paramException instanceof NetworkOnMainThreadException) {
      paramInt = -6;
    } else if (paramException instanceof UnsupportedOperationException) {
      paramInt = -7;
    } 
    writeInt(paramInt);
    if (paramInt == 0) {
      if (paramException instanceof RuntimeException)
        throw (RuntimeException)paramException; 
      throw new RuntimeException(paramException);
    } 
    writeString(paramException.getMessage());
    if (paramInt != -9)
      return; 
    writeParcelable((Parcelable)paramException);
  }
  
  protected abstract void writeFloat(float paramFloat);
  
  public void writeFloat(float paramFloat, int paramInt) {
    setOutputField(paramInt);
    writeFloat(paramFloat);
  }
  
  protected void writeFloatArray(float[] paramArrayOfFloat) {
    if (paramArrayOfFloat != null) {
      int i = paramArrayOfFloat.length;
      writeInt(i);
      for (byte b = 0; b < i; b++)
        writeFloat(paramArrayOfFloat[b]); 
      return;
    } 
    writeInt(-1);
  }
  
  public void writeFloatArray(float[] paramArrayOfFloat, int paramInt) {
    setOutputField(paramInt);
    writeFloatArray(paramArrayOfFloat);
  }
  
  protected abstract void writeInt(int paramInt);
  
  public void writeInt(int paramInt1, int paramInt2) {
    setOutputField(paramInt2);
    writeInt(paramInt1);
  }
  
  protected void writeIntArray(int[] paramArrayOfInt) {
    if (paramArrayOfInt != null) {
      int i = paramArrayOfInt.length;
      writeInt(i);
      for (byte b = 0; b < i; b++)
        writeInt(paramArrayOfInt[b]); 
      return;
    } 
    writeInt(-1);
  }
  
  public void writeIntArray(int[] paramArrayOfInt, int paramInt) {
    setOutputField(paramInt);
    writeIntArray(paramArrayOfInt);
  }
  
  public <T> void writeList(List<T> paramList, int paramInt) { writeCollection(paramList, paramInt); }
  
  protected abstract void writeLong(long paramLong);
  
  public void writeLong(long paramLong, int paramInt) {
    setOutputField(paramInt);
    writeLong(paramLong);
  }
  
  protected void writeLongArray(long[] paramArrayOfLong) {
    if (paramArrayOfLong != null) {
      int i = paramArrayOfLong.length;
      writeInt(i);
      for (byte b = 0; b < i; b++)
        writeLong(paramArrayOfLong[b]); 
      return;
    } 
    writeInt(-1);
  }
  
  public void writeLongArray(long[] paramArrayOfLong, int paramInt) {
    setOutputField(paramInt);
    writeLongArray(paramArrayOfLong);
  }
  
  protected void writeNoException() { writeInt(0); }
  
  protected abstract void writeParcelable(Parcelable paramParcelable);
  
  public void writeParcelable(Parcelable paramParcelable, int paramInt) {
    setOutputField(paramInt);
    writeParcelable(paramParcelable);
  }
  
  public void writeSerializable(Serializable paramSerializable, int paramInt) {
    setOutputField(paramInt);
    writeSerializable(paramSerializable);
  }
  
  public <T> void writeSet(Set<T> paramSet, int paramInt) { writeCollection(paramSet, paramInt); }
  
  @RequiresApi(api = 21)
  public void writeSize(Size paramSize, int paramInt) {
    boolean bool;
    setOutputField(paramInt);
    if (paramSize != null) {
      bool = true;
    } else {
      bool = false;
    } 
    writeBoolean(bool);
    if (paramSize != null) {
      writeInt(paramSize.getWidth());
      writeInt(paramSize.getHeight());
    } 
  }
  
  @RequiresApi(api = 21)
  public void writeSizeF(SizeF paramSizeF, int paramInt) {
    boolean bool;
    setOutputField(paramInt);
    if (paramSizeF != null) {
      bool = true;
    } else {
      bool = false;
    } 
    writeBoolean(bool);
    if (paramSizeF != null) {
      writeFloat(paramSizeF.getWidth());
      writeFloat(paramSizeF.getHeight());
    } 
  }
  
  public void writeSparseBooleanArray(SparseBooleanArray paramSparseBooleanArray, int paramInt) {
    setOutputField(paramInt);
    if (paramSparseBooleanArray == null) {
      writeInt(-1);
      return;
    } 
    int i = paramSparseBooleanArray.size();
    writeInt(i);
    for (paramInt = 0; paramInt < i; paramInt++) {
      writeInt(paramSparseBooleanArray.keyAt(paramInt));
      writeBoolean(paramSparseBooleanArray.valueAt(paramInt));
    } 
  }
  
  protected abstract void writeString(String paramString);
  
  public void writeString(String paramString, int paramInt) {
    setOutputField(paramInt);
    writeString(paramString);
  }
  
  protected abstract void writeStrongBinder(IBinder paramIBinder);
  
  public void writeStrongBinder(IBinder paramIBinder, int paramInt) {
    setOutputField(paramInt);
    writeStrongBinder(paramIBinder);
  }
  
  protected abstract void writeStrongInterface(IInterface paramIInterface);
  
  public void writeStrongInterface(IInterface paramIInterface, int paramInt) {
    setOutputField(paramInt);
    writeStrongInterface(paramIInterface);
  }
  
  protected void writeVersionedParcelable(VersionedParcelable paramVersionedParcelable) {
    if (paramVersionedParcelable == null) {
      writeString(null);
      return;
    } 
    writeVersionedParcelableCreator(paramVersionedParcelable);
    VersionedParcel versionedParcel;
    (versionedParcel = createSubParcel()).writeToParcel(paramVersionedParcelable, versionedParcel);
    versionedParcel.closeField();
  }
  
  public void writeVersionedParcelable(VersionedParcelable paramVersionedParcelable, int paramInt) {
    setOutputField(paramInt);
    writeVersionedParcelable(paramVersionedParcelable);
  }
  
  public static class ParcelException extends RuntimeException {
    public ParcelException(Throwable param1Throwable) { super(param1Throwable); }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\androidx\versionedparcelable\VersionedParcel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */