package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.util.SparseArray;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Set;

@RestrictTo({RestrictTo.Scope.LIBRARY})
class VersionedParcelStream extends VersionedParcel {
  private static final int TYPE_BOOLEAN = 5;
  
  private static final int TYPE_BOOLEAN_ARRAY = 6;
  
  private static final int TYPE_DOUBLE = 7;
  
  private static final int TYPE_DOUBLE_ARRAY = 8;
  
  private static final int TYPE_FLOAT = 13;
  
  private static final int TYPE_FLOAT_ARRAY = 14;
  
  private static final int TYPE_INT = 9;
  
  private static final int TYPE_INT_ARRAY = 10;
  
  private static final int TYPE_LONG = 11;
  
  private static final int TYPE_LONG_ARRAY = 12;
  
  private static final int TYPE_NULL = 0;
  
  private static final int TYPE_STRING = 3;
  
  private static final int TYPE_STRING_ARRAY = 4;
  
  private static final int TYPE_SUB_BUNDLE = 1;
  
  private static final int TYPE_SUB_PERSISTABLE_BUNDLE = 2;
  
  private static final Charset UTF_16 = Charset.forName("UTF-16");
  
  private final SparseArray<InputBuffer> mCachedFields = new SparseArray();
  
  private DataInputStream mCurrentInput;
  
  private DataOutputStream mCurrentOutput;
  
  private FieldBuffer mFieldBuffer;
  
  private boolean mIgnoreParcelables;
  
  private final DataInputStream mMasterInput;
  
  private final DataOutputStream mMasterOutput;
  
  public VersionedParcelStream(InputStream paramInputStream, OutputStream paramOutputStream) {
    InputStream inputStream = null;
    if (paramInputStream != null) {
      paramInputStream = new DataInputStream(paramInputStream);
    } else {
      paramInputStream = null;
    } 
    this.mMasterInput = paramInputStream;
    paramInputStream = inputStream;
    if (paramOutputStream != null)
      dataOutputStream = new DataOutputStream(paramOutputStream); 
    this.mMasterOutput = dataOutputStream;
    this.mCurrentInput = this.mMasterInput;
    this.mCurrentOutput = this.mMasterOutput;
  }
  
  private void readObject(int paramInt, String paramString, Bundle paramBundle) {
    StringBuilder stringBuilder;
    switch (paramInt) {
      default:
        stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown type ");
        stringBuilder.append(paramInt);
        throw new RuntimeException(stringBuilder.toString());
      case 14:
        paramBundle.putFloatArray(stringBuilder, readFloatArray());
        return;
      case 13:
        paramBundle.putFloat(stringBuilder, readFloat());
        return;
      case 12:
        paramBundle.putLongArray(stringBuilder, readLongArray());
        return;
      case 11:
        paramBundle.putLong(stringBuilder, readLong());
        return;
      case 10:
        paramBundle.putIntArray(stringBuilder, readIntArray());
        return;
      case 9:
        paramBundle.putInt(stringBuilder, readInt());
        return;
      case 8:
        paramBundle.putDoubleArray(stringBuilder, readDoubleArray());
        return;
      case 7:
        paramBundle.putDouble(stringBuilder, readDouble());
        return;
      case 6:
        paramBundle.putBooleanArray(stringBuilder, readBooleanArray());
        return;
      case 5:
        paramBundle.putBoolean(stringBuilder, readBoolean());
        return;
      case 4:
        paramBundle.putStringArray(stringBuilder, (String[])readArray(new String[0]));
        return;
      case 3:
        paramBundle.putString(stringBuilder, readString());
        return;
      case 2:
        paramBundle.putBundle(stringBuilder, readBundle());
        return;
      case 1:
        paramBundle.putBundle(stringBuilder, readBundle());
        return;
      case 0:
        break;
    } 
    paramBundle.putParcelable(stringBuilder, null);
  }
  
  private void writeObject(Object paramObject) {
    if (paramObject == null) {
      writeInt(0);
      return;
    } 
    if (paramObject instanceof Bundle) {
      writeInt(1);
      writeBundle((Bundle)paramObject);
      return;
    } 
    if (paramObject instanceof String) {
      writeInt(3);
      writeString((String)paramObject);
      return;
    } 
    if (paramObject instanceof String[]) {
      writeInt(4);
      writeArray((String[])paramObject);
      return;
    } 
    if (paramObject instanceof Boolean) {
      writeInt(5);
      writeBoolean(((Boolean)paramObject).booleanValue());
      return;
    } 
    if (paramObject instanceof boolean[]) {
      writeInt(6);
      writeBooleanArray((boolean[])paramObject);
      return;
    } 
    if (paramObject instanceof Double) {
      writeInt(7);
      writeDouble(((Double)paramObject).doubleValue());
      return;
    } 
    if (paramObject instanceof double[]) {
      writeInt(8);
      writeDoubleArray((double[])paramObject);
      return;
    } 
    if (paramObject instanceof Integer) {
      writeInt(9);
      writeInt(((Integer)paramObject).intValue());
      return;
    } 
    if (paramObject instanceof int[]) {
      writeInt(10);
      writeIntArray((int[])paramObject);
      return;
    } 
    if (paramObject instanceof Long) {
      writeInt(11);
      writeLong(((Long)paramObject).longValue());
      return;
    } 
    if (paramObject instanceof long[]) {
      writeInt(12);
      writeLongArray((long[])paramObject);
      return;
    } 
    if (paramObject instanceof Float) {
      writeInt(13);
      writeFloat(((Float)paramObject).floatValue());
      return;
    } 
    if (paramObject instanceof float[]) {
      writeInt(14);
      writeFloatArray((float[])paramObject);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unsupported type ");
    stringBuilder.append(paramObject.getClass());
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void closeField() {
    fieldBuffer = this.mFieldBuffer;
    if (fieldBuffer != null)
      try {
        if (fieldBuffer.mOutput.size() != 0)
          this.mFieldBuffer.flushField(); 
        this.mFieldBuffer = null;
        return;
      } catch (IOException fieldBuffer) {
        throw new VersionedParcel.ParcelException(fieldBuffer);
      }  
  }
  
  protected VersionedParcel createSubParcel() { return new VersionedParcelStream(this.mCurrentInput, this.mCurrentOutput); }
  
  public boolean isStream() { return true; }
  
  public boolean readBoolean() {
    try {
      return this.mCurrentInput.readBoolean();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public Bundle readBundle() {
    int i = readInt();
    if (i < 0)
      return null; 
    Bundle bundle = new Bundle();
    for (byte b = 0; b < i; b++) {
      String str = readString();
      readObject(readInt(), str, bundle);
    } 
    return bundle;
  }
  
  public byte[] readByteArray() {
    try {
      int i = this.mCurrentInput.readInt();
      if (i > 0) {
        byte[] arrayOfByte = new byte[i];
        this.mCurrentInput.readFully(arrayOfByte);
        return arrayOfByte;
      } 
      return null;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public double readDouble() {
    try {
      return this.mCurrentInput.readDouble();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public boolean readField(int paramInt) {
    inputBuffer = (InputBuffer)this.mCachedFields.get(paramInt);
    if (inputBuffer != null) {
      this.mCachedFields.remove(paramInt);
      this.mCurrentInput = inputBuffer.mInputStream;
      return true;
    } 
    try {
      while (true) {
        int k = this.mMasterInput.readInt();
        int j = k & 0xFFFF;
        int i = j;
        if (j == 65535)
          i = this.mMasterInput.readInt(); 
        inputBuffer = new InputBuffer(0xFFFF & k >> 16, i, this.mMasterInput);
        if (inputBuffer.mFieldId == paramInt) {
          this.mCurrentInput = inputBuffer.mInputStream;
          return true;
        } 
        this.mCachedFields.put(inputBuffer.mFieldId, inputBuffer);
      } 
    } catch (IOException inputBuffer) {
      return false;
    } 
  }
  
  public float readFloat() {
    try {
      return this.mCurrentInput.readFloat();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public int readInt() {
    try {
      return this.mCurrentInput.readInt();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public long readLong() {
    try {
      return this.mCurrentInput.readLong();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public <T extends Parcelable> T readParcelable() { return null; }
  
  public String readString() {
    try {
      int i = this.mCurrentInput.readInt();
      if (i > 0) {
        byte[] arrayOfByte = new byte[i];
        this.mCurrentInput.readFully(arrayOfByte);
        return new String(arrayOfByte, UTF_16);
      } 
      return null;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public IBinder readStrongBinder() { return null; }
  
  public void setOutputField(int paramInt) {
    closeField();
    this.mFieldBuffer = new FieldBuffer(paramInt, this.mMasterOutput);
    this.mCurrentOutput = this.mFieldBuffer.mDataStream;
  }
  
  public void setSerializationFlags(boolean paramBoolean1, boolean paramBoolean2) {
    if (paramBoolean1) {
      this.mIgnoreParcelables = paramBoolean2;
      return;
    } 
    throw new RuntimeException("Serialization of this object is not allowed");
  }
  
  public void writeBoolean(boolean paramBoolean) {
    try {
      this.mCurrentOutput.writeBoolean(paramBoolean);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeBundle(Bundle paramBundle) {
    if (paramBundle != null) {
      try {
        Set set = paramBundle.keySet();
        this.mCurrentOutput.writeInt(set.size());
        for (String str : set) {
          writeString(str);
          writeObject(paramBundle.get(str));
        } 
      } catch (IOException paramBundle) {
        throw new VersionedParcel.ParcelException(paramBundle);
      } 
    } else {
      this.mCurrentOutput.writeInt(-1);
      return;
    } 
  }
  
  public void writeByteArray(byte[] paramArrayOfByte) {
    if (paramArrayOfByte != null)
      try {
        this.mCurrentOutput.writeInt(paramArrayOfByte.length);
        this.mCurrentOutput.write(paramArrayOfByte);
        return;
      } catch (IOException paramArrayOfByte) {
        throw new VersionedParcel.ParcelException(paramArrayOfByte);
      }  
    this.mCurrentOutput.writeInt(-1);
  }
  
  public void writeByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
    if (paramArrayOfByte != null)
      try {
        this.mCurrentOutput.writeInt(paramInt2);
        this.mCurrentOutput.write(paramArrayOfByte, paramInt1, paramInt2);
        return;
      } catch (IOException paramArrayOfByte) {
        throw new VersionedParcel.ParcelException(paramArrayOfByte);
      }  
    this.mCurrentOutput.writeInt(-1);
  }
  
  public void writeDouble(double paramDouble) {
    try {
      this.mCurrentOutput.writeDouble(paramDouble);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeFloat(float paramFloat) {
    try {
      this.mCurrentOutput.writeFloat(paramFloat);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeInt(int paramInt) {
    try {
      this.mCurrentOutput.writeInt(paramInt);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeLong(long paramLong) {
    try {
      this.mCurrentOutput.writeLong(paramLong);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeParcelable(Parcelable paramParcelable) {
    if (this.mIgnoreParcelables)
      return; 
    throw new RuntimeException("Parcelables cannot be written to an OutputStream");
  }
  
  public void writeString(String paramString) {
    if (paramString != null)
      try {
        byte[] arrayOfByte = paramString.getBytes(UTF_16);
        this.mCurrentOutput.writeInt(arrayOfByte.length);
        this.mCurrentOutput.write(arrayOfByte);
        return;
      } catch (IOException paramString) {
        throw new VersionedParcel.ParcelException(paramString);
      }  
    this.mCurrentOutput.writeInt(-1);
  }
  
  public void writeStrongBinder(IBinder paramIBinder) {
    if (this.mIgnoreParcelables)
      return; 
    throw new RuntimeException("Binders cannot be written to an OutputStream");
  }
  
  public void writeStrongInterface(IInterface paramIInterface) {
    if (this.mIgnoreParcelables)
      return; 
    throw new RuntimeException("Binders cannot be written to an OutputStream");
  }
  
  private static class FieldBuffer {
    final DataOutputStream mDataStream = new DataOutputStream(this.mOutput);
    
    private final int mFieldId;
    
    final ByteArrayOutputStream mOutput = new ByteArrayOutputStream();
    
    private final DataOutputStream mTarget;
    
    FieldBuffer(int param1Int, DataOutputStream param1DataOutputStream) {
      this.mFieldId = param1Int;
      this.mTarget = param1DataOutputStream;
    }
    
    void flushField() {
      int i;
      this.mDataStream.flush();
      int j = this.mOutput.size();
      int k = this.mFieldId;
      if (j >= 65535) {
        i = 65535;
      } else {
        i = j;
      } 
      this.mTarget.writeInt(k << 16 | i);
      if (j >= 65535)
        this.mTarget.writeInt(j); 
      this.mOutput.writeTo(this.mTarget);
    }
  }
  
  private static class InputBuffer {
    final int mFieldId;
    
    final DataInputStream mInputStream;
    
    private final int mSize;
    
    InputBuffer(int param1Int1, int param1Int2, DataInputStream param1DataInputStream) throws IOException {
      this.mSize = param1Int2;
      this.mFieldId = param1Int1;
      byte[] arrayOfByte = new byte[this.mSize];
      param1DataInputStream.readFully(arrayOfByte);
      this.mInputStream = new DataInputStream(new ByteArrayInputStream(arrayOfByte));
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\androidx\versionedparcelable\VersionedParcelStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */