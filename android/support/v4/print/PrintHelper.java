package android.support.v4.print;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class PrintHelper {
  @SuppressLint({"InlinedApi"})
  public static final int COLOR_MODE_COLOR = 2;
  
  @SuppressLint({"InlinedApi"})
  public static final int COLOR_MODE_MONOCHROME = 1;
  
  static final boolean IS_MIN_MARGINS_HANDLING_CORRECT;
  
  private static final String LOG_TAG = "PrintHelper";
  
  private static final int MAX_PRINT_SIZE = 3500;
  
  public static final int ORIENTATION_LANDSCAPE = 1;
  
  public static final int ORIENTATION_PORTRAIT = 2;
  
  static final boolean PRINT_ACTIVITY_RESPECTS_ORIENTATION;
  
  public static final int SCALE_MODE_FILL = 2;
  
  public static final int SCALE_MODE_FIT = 1;
  
  int mColorMode = 2;
  
  final Context mContext;
  
  BitmapFactory.Options mDecodeOptions = null;
  
  final Object mLock = new Object();
  
  int mOrientation = 1;
  
  int mScaleMode = 2;
  
  static  {
    int i = Build.VERSION.SDK_INT;
    byte b2 = 0;
    if (i < 20 || Build.VERSION.SDK_INT > 23) {
      b1 = 1;
    } else {
      b1 = 0;
    } 
    PRINT_ACTIVITY_RESPECTS_ORIENTATION = b1;
    byte b1 = b2;
    if (Build.VERSION.SDK_INT != 23)
      b1 = 1; 
    IS_MIN_MARGINS_HANDLING_CORRECT = b1;
  }
  
  public PrintHelper(@NonNull Context paramContext) { this.mContext = paramContext; }
  
  static Bitmap convertBitmapForColorMode(Bitmap paramBitmap, int paramInt) {
    if (paramInt != 1)
      return paramBitmap; 
    Bitmap bitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    ColorMatrix colorMatrix = new ColorMatrix();
    colorMatrix.setSaturation(0.0F);
    paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    canvas.drawBitmap(paramBitmap, 0.0F, 0.0F, paint);
    canvas.setBitmap(null);
    return bitmap;
  }
  
  @RequiresApi(19)
  private static PrintAttributes.Builder copyAttributes(PrintAttributes paramPrintAttributes) {
    PrintAttributes.Builder builder = (new PrintAttributes.Builder()).setMediaSize(paramPrintAttributes.getMediaSize()).setResolution(paramPrintAttributes.getResolution()).setMinMargins(paramPrintAttributes.getMinMargins());
    if (paramPrintAttributes.getColorMode() != 0)
      builder.setColorMode(paramPrintAttributes.getColorMode()); 
    if (Build.VERSION.SDK_INT >= 23 && paramPrintAttributes.getDuplexMode() != 0)
      builder.setDuplexMode(paramPrintAttributes.getDuplexMode()); 
    return builder;
  }
  
  static Matrix getMatrix(int paramInt1, int paramInt2, RectF paramRectF, int paramInt3) {
    Matrix matrix = new Matrix();
    float f = paramRectF.width() / paramInt1;
    if (paramInt3 == 2) {
      f = Math.max(f, paramRectF.height() / paramInt2);
    } else {
      f = Math.min(f, paramRectF.height() / paramInt2);
    } 
    matrix.postScale(f, f);
    matrix.postTranslate((paramRectF.width() - paramInt1 * f) / 2.0F, (paramRectF.height() - paramInt2 * f) / 2.0F);
    return matrix;
  }
  
  static boolean isPortrait(Bitmap paramBitmap) { return (paramBitmap.getWidth() <= paramBitmap.getHeight()); }
  
  private Bitmap loadBitmap(Uri paramUri, BitmapFactory.Options paramOptions) throws FileNotFoundException {
    if (paramUri != null) {
      Context context = this.mContext;
      if (context != null) {
        InputStream inputStream = null;
        try {
          inputStream1 = context.getContentResolver().openInputStream(paramUri);
          inputStream = inputStream1;
          return BitmapFactory.decodeStream(inputStream1, null, paramOptions);
        } finally {
          if (inputStream != null)
            try {
              inputStream.close();
            } catch (IOException paramOptions) {
              Log.w("PrintHelper", "close fail ", paramOptions);
            }  
        } 
      } 
    } 
    throw new IllegalArgumentException("bad argument to loadBitmap");
  }
  
  public static boolean systemSupportsPrint() { return (Build.VERSION.SDK_INT >= 19); }
  
  public int getColorMode() { return this.mColorMode; }
  
  public int getOrientation() { return (Build.VERSION.SDK_INT >= 19 && this.mOrientation == 0) ? 1 : this.mOrientation; }
  
  public int getScaleMode() { return this.mScaleMode; }
  
  Bitmap loadConstrainedBitmap(Uri paramUri) throws FileNotFoundException {
    if (paramUri != null && this.mContext != null) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      loadBitmap(paramUri, options);
      int i = options.outWidth;
      int j = options.outHeight;
      if (i > 0) {
        if (j <= 0)
          return null; 
        int m = Math.max(i, j);
        int k;
        for (k = 1; m > 3500; k <<= true)
          m >>>= 1; 
        if (k) {
          if (Math.min(i, j) / k <= 0)
            return null; 
          object = this.mLock;
          /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
          try {
            this.mDecodeOptions = new BitmapFactory.Options();
            this.mDecodeOptions.inMutable = true;
            this.mDecodeOptions.inSampleSize = k;
            BitmapFactory.Options options1 = this.mDecodeOptions;
            try {
              /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
              try {
                object = loadBitmap(paramUri, options1);
              } finally {
                object = null;
              } 
            } finally {}
          } finally {}
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
          throw paramUri;
        } 
        return null;
      } 
      return null;
    } 
    throw new IllegalArgumentException("bad argument to getScaledBitmap");
  }
  
  public void printBitmap(@NonNull String paramString, @NonNull Bitmap paramBitmap) { printBitmap(paramString, paramBitmap, null); }
  
  public void printBitmap(@NonNull String paramString, @NonNull Bitmap paramBitmap, @Nullable OnPrintFinishCallback paramOnPrintFinishCallback) {
    if (Build.VERSION.SDK_INT >= 19) {
      PrintAttributes.MediaSize mediaSize;
      if (paramBitmap == null)
        return; 
      PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
      if (isPortrait(paramBitmap)) {
        mediaSize = PrintAttributes.MediaSize.UNKNOWN_PORTRAIT;
      } else {
        mediaSize = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
      } 
      PrintAttributes printAttributes = (new PrintAttributes.Builder()).setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
      printManager.print(paramString, new PrintBitmapAdapter(paramString, this.mScaleMode, paramBitmap, paramOnPrintFinishCallback), printAttributes);
      return;
    } 
  }
  
  public void printBitmap(@NonNull String paramString, @NonNull Uri paramUri) throws FileNotFoundException { printBitmap(paramString, paramUri, null); }
  
  public void printBitmap(@NonNull String paramString, @NonNull Uri paramUri, @Nullable OnPrintFinishCallback paramOnPrintFinishCallback) throws FileNotFoundException {
    if (Build.VERSION.SDK_INT < 19)
      return; 
    PrintUriAdapter printUriAdapter = new PrintUriAdapter(paramString, paramUri, paramOnPrintFinishCallback, this.mScaleMode);
    PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
    PrintAttributes.Builder builder = new PrintAttributes.Builder();
    builder.setColorMode(this.mColorMode);
    int i = this.mOrientation;
    if (i == 1 || i == 0) {
      builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
    } else if (i == 2) {
      builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
    } 
    printManager.print(paramString, printUriAdapter, builder.build());
  }
  
  public void setColorMode(int paramInt) { this.mColorMode = paramInt; }
  
  public void setOrientation(int paramInt) { this.mOrientation = paramInt; }
  
  public void setScaleMode(int paramInt) { this.mScaleMode = paramInt; }
  
  @RequiresApi(19)
  void writeBitmap(final PrintAttributes attributes, final int fittingMode, final Bitmap bitmap, final ParcelFileDescriptor fileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
    final PrintAttributes pdfAttributes;
    if (IS_MIN_MARGINS_HANDLING_CORRECT) {
      printAttributes = paramPrintAttributes;
    } else {
      printAttributes = copyAttributes(paramPrintAttributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build();
    } 
    (new AsyncTask<Void, Void, Throwable>() {
        protected Throwable doInBackground(Void... param1VarArgs) {
          try {
            if (cancellationSignal.isCanceled())
              return null; 
            printedPdfDocument = new PrintedPdfDocument(PrintHelper.this.mContext, pdfAttributes);
            bitmap = PrintHelper.convertBitmapForColorMode(bitmap, pdfAttributes.getColorMode());
            boolean bool = cancellationSignal.isCanceled();
            if (bool)
              return null; 
            try {
              RectF rectF;
              PdfDocument.Page page = printedPdfDocument.startPage(1);
              if (PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                rectF = new RectF(page.getInfo().getContentRect());
              } else {
                PrintedPdfDocument printedPdfDocument1 = new PrintedPdfDocument(PrintHelper.this.mContext, attributes);
                PdfDocument.Page page1 = printedPdfDocument1.startPage(1);
                rectF = new RectF(page1.getInfo().getContentRect());
                printedPdfDocument1.finishPage(page1);
                printedPdfDocument1.close();
              } 
              Matrix matrix = PrintHelper.getMatrix(bitmap.getWidth(), bitmap.getHeight(), rectF, fittingMode);
              if (!PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                matrix.postTranslate(rectF.left, rectF.top);
                page.getCanvas().clipRect(rectF);
              } 
              page.getCanvas().drawBitmap(bitmap, matrix, null);
              printedPdfDocument.finishPage(page);
              bool = cancellationSignal.isCanceled();
              if (bool)
                return null; 
              printedPdfDocument.writeTo(new FileOutputStream(fileDescriptor.getFileDescriptor()));
            } finally {
              printedPdfDocument.close();
              parcelFileDescriptor = fileDescriptor;
              if (parcelFileDescriptor != null)
                try {
                  fileDescriptor.close();
                } catch (IOException parcelFileDescriptor) {} 
              if (bitmap != bitmap)
                bitmap.recycle(); 
            } 
          } catch (Throwable param1VarArgs) {
            return null;
          } 
          return null;
        }
        
        protected void onPostExecute(Throwable param1Throwable) {
          if (cancellationSignal.isCanceled()) {
            writeResultCallback.onWriteCancelled();
            return;
          } 
          if (param1Throwable == null) {
            writeResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
            return;
          } 
          Log.e("PrintHelper", "Error writing printed content", param1Throwable);
          writeResultCallback.onWriteFailed(null);
        }
      }).execute(new Void[0]);
  }
  
  public static interface OnPrintFinishCallback {
    void onFinish();
  }
  
  @RequiresApi(19)
  private class PrintBitmapAdapter extends PrintDocumentAdapter {
    private PrintAttributes mAttributes;
    
    private final Bitmap mBitmap;
    
    private final PrintHelper.OnPrintFinishCallback mCallback;
    
    private final int mFittingMode;
    
    private final String mJobName;
    
    PrintBitmapAdapter(String param1String, int param1Int, Bitmap param1Bitmap, PrintHelper.OnPrintFinishCallback param1OnPrintFinishCallback) {
      this.mJobName = param1String;
      this.mFittingMode = param1Int;
      this.mBitmap = param1Bitmap;
      this.mCallback = param1OnPrintFinishCallback;
    }
    
    public void onFinish() {
      PrintHelper.OnPrintFinishCallback onPrintFinishCallback = this.mCallback;
      if (onPrintFinishCallback != null)
        onPrintFinishCallback.onFinish(); 
    }
    
    public void onLayout(PrintAttributes param1PrintAttributes1, PrintAttributes param1PrintAttributes2, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.LayoutResultCallback param1LayoutResultCallback, Bundle param1Bundle) {
      this.mAttributes = param1PrintAttributes2;
      param1LayoutResultCallback.onLayoutFinished((new PrintDocumentInfo.Builder(this.mJobName)).setContentType(1).setPageCount(1).build(), true ^ param1PrintAttributes2.equals(param1PrintAttributes1));
    }
    
    public void onWrite(PageRange[] param1ArrayOfPageRange, ParcelFileDescriptor param1ParcelFileDescriptor, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.WriteResultCallback param1WriteResultCallback) { PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, param1ParcelFileDescriptor, param1CancellationSignal, param1WriteResultCallback); }
  }
  
  @RequiresApi(19)
  private class PrintUriAdapter extends PrintDocumentAdapter {
    PrintAttributes mAttributes;
    
    Bitmap mBitmap;
    
    final PrintHelper.OnPrintFinishCallback mCallback;
    
    final int mFittingMode;
    
    final Uri mImageFile;
    
    final String mJobName;
    
    AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
    
    PrintUriAdapter(String param1String, Uri param1Uri, PrintHelper.OnPrintFinishCallback param1OnPrintFinishCallback, int param1Int) {
      this.mJobName = param1String;
      this.mImageFile = param1Uri;
      this.mCallback = param1OnPrintFinishCallback;
      this.mFittingMode = param1Int;
      this.mBitmap = null;
    }
    
    void cancelLoad() {
      synchronized (PrintHelper.this.mLock) {
        if (PrintHelper.this.mDecodeOptions != null) {
          if (Build.VERSION.SDK_INT < 24)
            PrintHelper.this.mDecodeOptions.requestCancelDecode(); 
          PrintHelper.this.mDecodeOptions = null;
        } 
        return;
      } 
    }
    
    public void onFinish() {
      super.onFinish();
      cancelLoad();
      AsyncTask asyncTask = this.mLoadBitmap;
      if (asyncTask != null)
        asyncTask.cancel(true); 
      PrintHelper.OnPrintFinishCallback onPrintFinishCallback = this.mCallback;
      if (onPrintFinishCallback != null)
        onPrintFinishCallback.onFinish(); 
      Bitmap bitmap = this.mBitmap;
      if (bitmap != null) {
        bitmap.recycle();
        this.mBitmap = null;
      } 
    }
    
    public void onLayout(PrintAttributes param1PrintAttributes1, PrintAttributes param1PrintAttributes2, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.LayoutResultCallback param1LayoutResultCallback, Bundle param1Bundle) { // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: aload_2
      //   4: putfield mAttributes : Landroid/print/PrintAttributes;
      //   7: aload_0
      //   8: monitorexit
      //   9: aload_3
      //   10: invokevirtual isCanceled : ()Z
      //   13: ifeq -> 22
      //   16: aload #4
      //   18: invokevirtual onLayoutCancelled : ()V
      //   21: return
      //   22: aload_0
      //   23: getfield mBitmap : Landroid/graphics/Bitmap;
      //   26: ifnull -> 64
      //   29: aload #4
      //   31: new android/print/PrintDocumentInfo$Builder
      //   34: dup
      //   35: aload_0
      //   36: getfield mJobName : Ljava/lang/String;
      //   39: invokespecial <init> : (Ljava/lang/String;)V
      //   42: iconst_1
      //   43: invokevirtual setContentType : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   46: iconst_1
      //   47: invokevirtual setPageCount : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   50: invokevirtual build : ()Landroid/print/PrintDocumentInfo;
      //   53: iconst_1
      //   54: aload_2
      //   55: aload_1
      //   56: invokevirtual equals : (Ljava/lang/Object;)Z
      //   59: ixor
      //   60: invokevirtual onLayoutFinished : (Landroid/print/PrintDocumentInfo;Z)V
      //   63: return
      //   64: aload_0
      //   65: new android/support/v4/print/PrintHelper$PrintUriAdapter$1
      //   68: dup
      //   69: aload_0
      //   70: aload_3
      //   71: aload_2
      //   72: aload_1
      //   73: aload #4
      //   75: invokespecial <init> : (Landroid/support/v4/print/PrintHelper$PrintUriAdapter;Landroid/os/CancellationSignal;Landroid/print/PrintAttributes;Landroid/print/PrintAttributes;Landroid/print/PrintDocumentAdapter$LayoutResultCallback;)V
      //   78: iconst_0
      //   79: anewarray android/net/Uri
      //   82: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
      //   85: putfield mLoadBitmap : Landroid/os/AsyncTask;
      //   88: return
      //   89: astore_1
      //   90: aload_0
      //   91: monitorexit
      //   92: aload_1
      //   93: athrow
      // Exception table:
      //   from	to	target	type
      //   2	9	89	finally
      //   90	92	89	finally }
    
    public void onWrite(PageRange[] param1ArrayOfPageRange, ParcelFileDescriptor param1ParcelFileDescriptor, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.WriteResultCallback param1WriteResultCallback) { PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, param1ParcelFileDescriptor, param1CancellationSignal, param1WriteResultCallback); }
  }
  
  class null extends AsyncTask<Uri, Boolean, Bitmap> {
    protected Bitmap doInBackground(Uri... param1VarArgs) {
      try {
        return this.this$1.this$0.loadConstrainedBitmap(this.this$1.mImageFile);
      } catch (FileNotFoundException param1VarArgs) {
        return null;
      } 
    }
    
    protected void onCancelled(Bitmap param1Bitmap) {
      layoutResultCallback.onLayoutCancelled();
      this.this$1.mLoadBitmap = null;
    }
    
    protected void onPostExecute(Bitmap param1Bitmap) { // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: invokespecial onPostExecute : (Ljava/lang/Object;)V
      //   5: aload_1
      //   6: astore_3
      //   7: aload_1
      //   8: ifnull -> 113
      //   11: getstatic android/support/v4/print/PrintHelper.PRINT_ACTIVITY_RESPECTS_ORIENTATION : Z
      //   14: ifeq -> 32
      //   17: aload_1
      //   18: astore_3
      //   19: aload_0
      //   20: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   23: getfield this$0 : Landroid/support/v4/print/PrintHelper;
      //   26: getfield mOrientation : I
      //   29: ifne -> 113
      //   32: aload_0
      //   33: monitorenter
      //   34: aload_0
      //   35: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   38: getfield mAttributes : Landroid/print/PrintAttributes;
      //   41: invokevirtual getMediaSize : ()Landroid/print/PrintAttributes$MediaSize;
      //   44: astore #4
      //   46: aload_0
      //   47: monitorexit
      //   48: aload_1
      //   49: astore_3
      //   50: aload #4
      //   52: ifnull -> 113
      //   55: aload_1
      //   56: astore_3
      //   57: aload #4
      //   59: invokevirtual isPortrait : ()Z
      //   62: aload_1
      //   63: invokestatic isPortrait : (Landroid/graphics/Bitmap;)Z
      //   66: if_icmpeq -> 113
      //   69: new android/graphics/Matrix
      //   72: dup
      //   73: invokespecial <init> : ()V
      //   76: astore_3
      //   77: aload_3
      //   78: ldc 90.0
      //   80: invokevirtual postRotate : (F)Z
      //   83: pop
      //   84: aload_1
      //   85: iconst_0
      //   86: iconst_0
      //   87: aload_1
      //   88: invokevirtual getWidth : ()I
      //   91: aload_1
      //   92: invokevirtual getHeight : ()I
      //   95: aload_3
      //   96: iconst_1
      //   97: invokestatic createBitmap : (Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
      //   100: astore_3
      //   101: goto -> 113
      //   104: astore_1
      //   105: goto -> 109
      //   108: astore_1
      //   109: aload_0
      //   110: monitorexit
      //   111: aload_1
      //   112: athrow
      //   113: aload_0
      //   114: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   117: astore_1
      //   118: aload_1
      //   119: aload_3
      //   120: putfield mBitmap : Landroid/graphics/Bitmap;
      //   123: aload_3
      //   124: ifnull -> 176
      //   127: new android/print/PrintDocumentInfo$Builder
      //   130: dup
      //   131: aload_1
      //   132: getfield mJobName : Ljava/lang/String;
      //   135: invokespecial <init> : (Ljava/lang/String;)V
      //   138: iconst_1
      //   139: invokevirtual setContentType : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   142: iconst_1
      //   143: invokevirtual setPageCount : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   146: invokevirtual build : ()Landroid/print/PrintDocumentInfo;
      //   149: astore_1
      //   150: aload_0
      //   151: getfield val$newPrintAttributes : Landroid/print/PrintAttributes;
      //   154: aload_0
      //   155: getfield val$oldPrintAttributes : Landroid/print/PrintAttributes;
      //   158: invokevirtual equals : (Ljava/lang/Object;)Z
      //   161: istore_2
      //   162: aload_0
      //   163: getfield val$layoutResultCallback : Landroid/print/PrintDocumentAdapter$LayoutResultCallback;
      //   166: aload_1
      //   167: iconst_1
      //   168: iload_2
      //   169: ixor
      //   170: invokevirtual onLayoutFinished : (Landroid/print/PrintDocumentInfo;Z)V
      //   173: goto -> 184
      //   176: aload_0
      //   177: getfield val$layoutResultCallback : Landroid/print/PrintDocumentAdapter$LayoutResultCallback;
      //   180: aconst_null
      //   181: invokevirtual onLayoutFailed : (Ljava/lang/CharSequence;)V
      //   184: aload_0
      //   185: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   188: aconst_null
      //   189: putfield mLoadBitmap : Landroid/os/AsyncTask;
      //   192: return
      // Exception table:
      //   from	to	target	type
      //   34	46	108	finally
      //   46	48	104	finally
      //   109	111	108	finally }
    
    protected void onPreExecute() { cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            public void onCancel() {
              PrintHelper.PrintUriAdapter.null.this.this$1.cancelLoad();
              PrintHelper.PrintUriAdapter.null.this.cancel(false);
            }
          }); }
  }
  
  class null implements CancellationSignal.OnCancelListener {
    public void onCancel() {
      this.this$2.this$1.cancelLoad();
      this.this$2.cancel(false);
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\print\PrintHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */