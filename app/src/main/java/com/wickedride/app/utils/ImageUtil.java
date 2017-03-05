/*
 * Copyright (c) 2014 Mayashree
 * All Rights Reserved.
 * @since Jan 9, 2014 
 * @author BalaKJ
 */
package com.wickedride.app.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author BalaKJ
 */
public final class ImageUtil {

	public static String getMediaStorePath(ContentResolver cr, Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = null;
		String path = "";
		try {
			cursor = cr.query(uri, projection, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				path = cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return path;
	}

	public static Bitmap decodeFile(File f, int maxSize) {
		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();

			int scale = 1;
			if (o.outHeight > maxSize || o.outWidth > maxSize) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(maxSize
								/ (double) Math.max(o.outHeight, o.outWidth))
								/ Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
		} catch (IOException e) {
			Log.e("Util", e.toString());
		}
		return b;
	}

	public static String storeBitmap(Context context, Uri url) throws Exception {
		File f = getTempFile(context);
		OutputStream os = null;

		try {
			InputStream is = null;
			if (url.toString().startsWith(
					"content://com.google.android.gallery3d")
					|| url.toString().startsWith(
							"content://com.android.gallery3d")) {
				is = context.getContentResolver().openInputStream(url);
			} else {
				is = new URL(url.toString()).openStream();
			}

			os = new FileOutputStream(f);
			copy(is, os);

			os.close();
		} finally {
			if (os != null)
				os.close();
		}
		return f.getAbsolutePath();
	}

	public static final int DEFAULT_BUFFER_SIZE = 8192;
	public static int IMAGE_MAX_SIZE = 0;

	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static Bitmap getBitmap(Context context, Uri imageUri) {
		context.getContentResolver().notifyChange(imageUri, null);
		ContentResolver cr = context.getContentResolver();
		Bitmap bitmap = null;
		try {
			//bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr,imageUri);
			
			BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            bitmap = BitmapFactory.decodeFile(getRealPathFromURI(context, imageUri), options);
            
            bitmap = MediaStore.Images.Media.getBitmap(cr,imageUri);
			
		} catch (Exception e) {
			Log.i("asdasd", e.getMessage());
		}
		return bitmap;
	}
	
	
	/**
	 * @param context
	 * @param contentUri
	 * @return
	 */
	public static String getRealPathFromURI(Context context, Uri contentUri) {
		
		// request only the image ID to be returned
		String[] proj = {MediaStore.Images.Media.DATA};
		// Create the cursor pointing to the SDCard
		Cursor cursor = context.getContentResolver().query( contentUri,
		        proj, 
		        null,
		        null,
		        null);
		
		//String[] proj = { (fileType == Consts.CLIP_FILETYPE_IMAGE ) ? MediaStore.Images.Media.DATA : MediaStore.Audio.Media.DATA };			
		//Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null,null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index); 
		cursor.close();
		return path;

	}
	

	// decodes image and scales it to reduce memory consumption
	public static Bitmap decodeFile(String imageFilePath) {

		Uri uri1 = Uri.parse("file:" + imageFilePath);
		File f = new File(uri1.getPath());

		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();

			int scale = 1;
			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(IMAGE_MAX_SIZE
								/ (double) Math.max(o.outHeight, o.outWidth))
								/ Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
		} catch (IOException e) {
		}
		return b;
	}

	public static String fileExtToMimeType(String path) {
		// File extension
		int pos = path.lastIndexOf(".");
		String ext = path.substring(pos + 1, path.length());

		// Mime tpe form extension
		MimeTypeMap map = MimeTypeMap.getSingleton();
		String mimeType = map.getMimeTypeFromExtension(ext);

		return mimeType;
	}

	public static File getTempFile(Context context) throws Exception {
		File cacheDir;

		// if the device has an SD card
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory()
							+ "/MyRegistry", "/temp/");
		} else {
			// it does not have an SD card
			cacheDir = context.getCacheDir();
		}
		if (!cacheDir.exists()) {
			boolean success = cacheDir.mkdirs();
			if (!success) {
				throw new Exception("Not create cache dir");
			}
		}

		File f = new File(cacheDir, "temp.jpg");
		return f;
	}

	public static File getTempImageFile(Context context) throws Exception {
		File cacheDir;

		// if the device has an SD card
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory()
							+ "/MyRegistry", "/temp/");
		} else {
			// it does not have an SD card
			cacheDir = context.getCacheDir();
		}
		if (!cacheDir.exists()) {
			boolean success = cacheDir.mkdirs();
			if (!success) {
				throw new Exception("Not create cache dir");
			}
		}

		File f = new File(cacheDir, "tempimage.jpg");
		return f;
	}

	public static void saveBitmap(File file, Bitmap bmp) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static RoundedBitmapDrawable getRoundImg(Bitmap bmp){
		RoundedBitmapDrawable roundBitMap = RoundedBitmapDrawableFactory.create(null, bmp);
		roundBitMap.setCornerRadius(100);

		return roundBitMap;
	}

}