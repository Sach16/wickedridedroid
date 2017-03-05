/**
 *
 */
package com.wickedride.app.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import com.wickedride.app.R;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.utils.ImageUtil;
import com.wickedride.app.utils.Util;

import java.io.File;

/**
 * @author Hari K J
 */
public class CameraActivity extends Activity {

    private static final int GALLERY_INTENT_CALLED = 10001;
    private static final int GALLERY_KITKAT_INTENT_CALLED = 10002;
    private String imgPath;
    private File photoImageFile;
    private String photoImagePath;
    private String from_screen;
    private AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_frame);
        Bundle arguments = getIntent().getExtras();
        boolean showRemove = false;
        byte b = 1;
        b += 1;
        System.out.println(b);
        if (arguments != null) {
            showRemove = arguments.getBoolean(Constants.CAMERAOPTIONS_SHOWREMOVE, false);
            from_screen = arguments.getString(Constants.FROM_SCREEN, "null");
        }
        displayMenuDialog();
    }

    private void displayMenuDialog() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        builder = new AlertDialog.Builder(CameraActivity.this);
        builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
                    dialog.dismiss();
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    capturePhoto();
                } else if (items[item].equals("Choose from Library")) {
                    choosePhoto();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    onBackPressed();
                }
            }
        });
        builder.show();

        /*final Dialog dialog = new Dialog(CameraActivity.this, R.style.CustomDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_display_image_layout);
        ListView listView = (ListView) dialog.findViewById(R.id.listView_diaplay_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        String[] names;
        int[] icons;

            if (showRemove) {
                names = new String[]{getString(R.string.remove_image), getString(R.string.take_photo), getString(R.string.choose_library)};
                icons = new int[]{R.drawable.custom_search_close, R.drawable.camer, R.drawable.library};
            } else {
                names = new String[]{getString(R.string.take_photo), getString(R.string.choose_library)};
                icons = new int[]{R.drawable.camer, R.drawable.library};
            }

        ListDialogAdapter dialogAdapter = new ListDialogAdapter(this, icons, names);
        listView.setAdapter(dialogAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (!showRemove) {
                        position += 1;
                    }
                    switch (position) {
                        case 0:
                            setResult(RequestCode.REMOVE_IMG);
                            finish();
                            break;
                        case 1:
                            capturePhoto();
                            break;
                        case 2:

                            choosePhoto();
                            break;
                    }
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        dialog.show();*/
    }

    /**
     * Choose the photo from the picker
     */
    public void choosePhoto() {

        try {
            if (Build.VERSION.SDK_INT < 19) {
                    Log.e("From if ", "ChatActivity1");
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT_CALLED);

            } else {
                    Log.e("From if ", "ChatActivity2");
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);

            }

        } catch (Exception e) {

        }
    }

    /**
     * Capture the photo from the camera
     */
    public void capturePhoto() {

        Intent intent;
        try {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoImageFile = ImageUtil.getTempFile(this);
            photoImagePath = photoImageFile.getPath();

            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoImageFile));
            startActivityForResult(intent, Constants.FROMCAMERA);

        } catch (Exception e) {
            Log.e("CameraActivity", e.toString());
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
     * android.content.Intent)
     */

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        Intent intent;

        Log.e("Request Code is", "" + requestCode);
        switch (requestCode) {
             case Constants.FROMCAMERA:
                Log.e("RequestCode.FROMCAMERA", "" + Constants.FROMCAMERA);

                if (resultCode != Activity.RESULT_OK) {
                    this.finish();
                    return;
                }
                if (resultCode == Activity.RESULT_OK) {
                    if (photoImageFile != null)
                        imgPath = photoImageFile.getPath();
                    else if (photoImagePath != null)
                        imgPath = photoImagePath;
                    else {
                        imgPath = photoImagePath;
                    }
                }
                break;


            case Constants.FROMGALLERY:

                Log.e("CameraActivity", "RequestCode.FROMGALLERY" + Constants.FROMGALLERY);

                if (resultCode != Activity.RESULT_OK) return;
                try {
                    uri = data.getData();
                    intent = new Intent();
                    intent.putExtra(Constants.DATA, uri.toString());
                    setResult(Constants.FROMCAMERA_SUCCESS, intent);
                    finish();
                } catch (Exception e) {
                    Log.e("CameraActivityGallery", e.toString());
                }

                break;
            case (GALLERY_INTENT_CALLED):
            case (GALLERY_KITKAT_INTENT_CALLED):

                Log.e("CameraActivity", "GALLERY_INTENT_CALLED" + GALLERY_INTENT_CALLED);
                Log.e("CameraActivity", "GALLERY_KITKAT_INTENT" + GALLERY_KITKAT_INTENT_CALLED);

                if (resultCode == RESULT_OK) {
                    Uri imageUri = null;
                    if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {

                        Log.d("CameraActivity", "Raw Uri::" + data.getData());

                        String filePath = Util.getPath(getApplicationContext(), data.getData());
                        Log.d("CameraActivity", "Raw Path::" + filePath);
                        if (data.getData() == null || filePath == null) {
                            Log.d("CameraActivity", "Raw Path & Raw Uri Null::");
                            return;
                        }
                        if (filePath.endsWith("jpeg") || filePath.endsWith("jpg") || filePath.endsWith("png") || filePath.endsWith("bmp") || filePath.endsWith("gif")) {
                            final int takeFlags = data.getFlags()
                                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                            if(takeFlags != 0) {


                                // Check for the fresh data.
                                getContentResolver().takePersistableUriPermission(data.getData(), takeFlags);

				               /* now extract ID from Uri path using getLastPathSegment() and then split with ":"
                                     then call get Uri to for Internal storage or External storage for media I have used getUri()
				                 */

                                String id = data.getData().getLastPathSegment().split(":")[1];
                                final String[] imageColumns = {MediaStore.Images.Media.DATA};
                                final String imageOrderBy = null;

                                uri = getUri();


                                Cursor imageCursor = managedQuery(uri, imageColumns, MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);

                                if (imageCursor.moveToFirst()) {
                                    imgPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                                }
                                imageUri = Uri.parse(new File(imgPath).toString());

                                Log.e("path", imgPath); // use selectedImagePath
                            } else{
                                imageUri = data.getData();
                                imgPath = filePath;
                            }
                        }


                    } else if (requestCode == GALLERY_INTENT_CALLED) {
                        if (resultCode == Activity.RESULT_OK) {
                            imageUri = data.getData();
                            imgPath = ImageUtil.getRealPathFromURI(this, imageUri);//CameraView.getImagePathFromUri(selectedImageUri, CameraOptionsActivity.this);
                        }
                    }
                    if (imageUri != null) {
                        intent = new Intent();
                        intent.putExtra(Constants.FILE_PATH, data.getData().toString());
                        intent.putExtra(Constants.DATA, imgPath);
                        setResult(Constants.FROMCAMERA_SUCCESS, intent);
                    }
                }
                break;
        }

        if (imgPath != null) {
            Log.i("CameraActivity", "imgPath :" + imgPath + "::");
            intent = new Intent();
            if (data!=null && data.getData() != null) {
                Log.i("filepath", data.getData().toString());
                intent.putExtra(Constants.FILE_PATH, data.getData().toString());
            }
            intent.putExtra(Constants.DATA, imgPath);
            setResult(Constants.FROMCAMERA_SUCCESS, intent);
        }

        finish();

    }

    // By using this method get the Uri of Internal/External Storage for Media
    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }


}
