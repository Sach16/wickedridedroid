package com.wickedride.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.wickedride.app.R;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.utils.ImageUtil;
import com.wickedride.app.utils.Util;
import com.wickedride.app.views.TouchImageView;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Hari K J
 */
public class EditImageActivity extends Activity implements View.OnClickListener {
    @InjectView(R.id.editimage_image)
    TouchImageView editImage;

    @InjectView(R.id.editimage_mask)
    ImageView editMask;

    private Bitmap imageBmp;
    private String imagePath;
    private boolean circle;
    private final String TAG = EditImageActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editimage_square);
        circle=false;
        ButterKnife.inject(this);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Uri sharedImageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                imagePath =getImagePathFromUri(intent,sharedImageUri);
            }
        }else{
                imagePath = getIntent().getExtras().getString(Constants.DATA);
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        findViewById(R.id.editimage_rotate).setOnClickListener(this);
        findViewById(R.id.editimage_done).setOnClickListener(this);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) editImage.getLayoutParams();
        lp.width = width;
        lp.height = height;
        editImage.setLayoutParams(lp);

        lp = (RelativeLayout.LayoutParams) editMask.getLayoutParams();
        lp.width = width;
        lp.height = height;
        editMask.setLayoutParams(lp);

        buildView(Util.Image.decodeFile(imagePath));
        //initChildActionBar("Crop Photo");
        // initActionbar(R.string.crop_picture);
    }

   /* public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tick_icon_menu, menu);
        return true;
    }*/



    /**
     *
     */
    private void buildView(Bitmap bm) {

        if (imagePath != null) {
            Display mDisplay = getWindowManager().getDefaultDisplay();
            int width = mDisplay.getWidth();
            //RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) findViewById(R.id.editimage_mask).getLayoutParams();
            //lp.height = width;
            //findViewById(R.id.editimage_mask).setLayoutParams(lp);
            editImage = (TouchImageView) findViewById(R.id.editimage_image);
            File file = new File(imagePath);
            Uri uri = Uri.fromFile(file);
            //editImage.setImageURI(uri);
            Util.Image.IMAGE_MAX_SIZE = mDisplay.getHeight();

            imageBmp = bm;//Util.Image.decodeFile(imagePath);
            if (imageBmp != null) {
//                if (imageBmp.getHeight() < imageBmp.getWidth()) {
//                    rotateImage();
//                }

                editImage.setImageBitmap(imageBmp);
                float scale = (float) 1.234;
//                editImage.setZoom(scale);
                editImage.setMaxZoom(4);
                Log.d(TAG, "PATH build view imageBmp" + imageBmp);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    private void saveNclose() {
        TouchImageView touchImageView = (TouchImageView) findViewById(R.id.editimage_image);
        touchImageView.setDrawingCacheEnabled(true);

        Bitmap screenshot = loadBitmapFromView(touchImageView);
        Bitmap cropimg = null;
        if(screenshot.getWidth() >640 && screenshot.getHeight()>640) {
            cropimg = Bitmap.createScaledBitmap(screenshot,640, 640,true);
        }else {
            cropimg = Bitmap.createScaledBitmap(screenshot,640, 640,true);
//             cropimg = Bitmap.createBitmap(screenshot);
        }
        touchImageView.setDrawingCacheEnabled(false);
        screenshot.recycle();
        File file = null;
        Uri uri = null;
        try {
            file = Util.Image.getTempImageFile(this, "tempimage.jpg");
            uri = Uri.fromFile(file);
            Util.Image.saveBitmap(file, cropimg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*if(!getIntent().getExtras().getBoolean(IntentConstants.CIRCLE) && getIntent().hasExtra(IntentConstants.FILE_PATH)) {
            Intent openVideoUpload = new Intent(getApplicationContext(), FileUploadActivity.class);
            openVideoUpload.putExtra(IntentConstants.FILE_PATH, file.getAbsolutePath());
            startActivity(openVideoUpload);
        }else if(getIntent().getParcelableExtra(Intent.EXTRA_STREAM) != null){
            Intent openImageShare = new Intent(getApplicationContext(), ShareActivity.class);
            openImageShare.putExtra(IntentConstants.FILE_PATH, file.getAbsolutePath());
            startActivity(openImageShare);
        }else{*/
            Intent intent = new Intent();
            intent.putExtra(Constants.DATA, uri.getPath());
            Log.d(TAG, uri.toString() + " is the result");
            setResult(Constants.FROM_EDITIMAGE_SUCCESS, intent);
        //}
        finish();

    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
//        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.draw(c);
        return b;
    }

    private Bitmap rotateImageAngle(Bitmap source, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    protected void rotateImage() {
        // createa matrix for the manipulation
        Matrix matrix = new Matrix();
        // rotate the Bitmap
        matrix.postRotate(90);
        // recreate the new Bitmap
        imageBmp = Bitmap.createBitmap(imageBmp, 0, 0, imageBmp.getWidth(), imageBmp.getHeight(), matrix, true);
        editImage.resetZoom();
        editImage.setImageBitmap(imageBmp);
        editImage.setMaxZoom(4);

    }

    /* (non-Javadoc)
     * @see com.clipix.clipix.fragments.ClipixBaseFragment#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUESTCODE_GETPHOTO) {
            if (resultCode == Constants.RESULTCODE_FROMCAMERA_SUCCESS) {
                imagePath = data.getExtras().getString(Constants.DATA);
                try
                {
                    ExifInterface ei = new ExifInterface(imagePath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    Bitmap bm=null;
                    Bitmap bitmap = Util.Image.decodeFile(imagePath);
                    Log.v("orientation", " : " + orientation);
                    switch(orientation)
                    {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            bm=rotateImageAngle(bitmap, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            bm=rotateImageAngle(bitmap, 180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            bm=rotateImageAngle(bitmap, 270);
                            break;
                        default:
                            bm=bitmap;
                    }
                    buildView(bm);
//                sample.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editimage_rotate:
                rotateImage();
                break;
            case R.id.editimage_done:
                saveNclose();
                break;
        }
    }

    private String getImagePathFromUri(Intent data, Uri muri){
        String imgPath = null;
        if (Build.VERSION.SDK_INT < 19) {

            String[] proj = { MediaStore.Images.Media.DATA };

            CursorLoader cursorLoader = new CursorLoader(
                    this,
                    muri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if(cursor != null){
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                imgPath = cursor.getString(column_index);
            }

        } else {
                imgPath = ImageUtil.getRealPathFromURI(this, muri);
        }

        return imgPath;
    }

    // By using this method get the Uri of Internal/External Storage for Media
    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }
}
