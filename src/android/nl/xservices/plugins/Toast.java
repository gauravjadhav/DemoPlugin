package com.capgemini.plugins;

import android.view.Gravity;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.net.Uri;
import android.view.Menu;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.webkit.MimeTypeMap;

public class Toast extends CordovaPlugin {

	private static final String ACTION_SHOW_EVENT = "show";
	private static final String ACTION_UPLOAD = "upload";
	protected String mFilePath;
	protected String mFileName;
	protected String mFileExtension;
	protected String mWorkOrderId;
	protected String mFileSize;
	protected String mNoteType;
	protected String mSectionId;
	protected String mCreatedBy;
	protected String mSurveyorId;
	protected String mUrl;
	protected String response = "";
	protected CallbackContext mCallbackContext;
	
	
	@Override
	public boolean execute(String action, JSONArray args,
			final CallbackContext callbackContext) throws JSONException {

		mCallbackContext = callbackContext;
		if(action.equals(ACTION_SHOW_EVENT)) {
		
		try {
			JSONObject arg_object = args.getJSONObject(0);
			mFileName = arg_object.getString("FileName");
			mFilePath = findVideo(mFileName);
			mWorkOrderId = arg_object.getString("WorkOrderId");
			mNoteType = arg_object.getString("NoteType");
			mSectionId = arg_object.getString("SectionId");
			mFileSize = arg_object.getString("FileSize");
			mFileExtension = arg_object.getString("FileExtension");
			mCreatedBy = arg_object.getString("CreatedBy");
			mSurveyorId = arg_object.getString("SurveyorId");
			mUrl = arg_object.getString("Url");
			
			//byte[] encodedString = convertToBase64(Uri.fromFile(new File(findVideo(path))));
			new MyTestAsync().execute();
			callAPI();
			//callbackContext.success("success");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			callbackContext.error(e.toString());
			return false;
		}
		
		} else if(action.equals(ACTION_UPLOAD)) {
			Intent i=  new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			i.setType("image/* vidoe/*");
			startActivityForResult(i, GALLERY_INTENT_CALLED);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == GALLERY_INTENT_CALLED) {
				if (null == data)
					return;

				String selectedImagePath;
				Uri selectedImageUri = data.getData();

				// MEDIA GALLERY
				selectedImagePath = ImageFilePath.getPath(
						cordova.getActivity(), selectedImageUri);
				
				ContentResolver cR = cordova.getActivity().getContentResolver();
				MimeTypeMap mime = MimeTypeMap.getSingleton();
				String type = cR.getType(selectedImageUri);
				String ext = mime.getExtensionFromMimeType(cR.getType(selectedImageUri));
				
				File f = new File(selectedImagePath);
				
				//Log.i("Image File Path", "" + selectedImagePath);
				//txta.setText("Image File Path : \n" + selectedImagePath + "\ntype is : " + type + "\nname is : " + f.getName() + "\next is : " + ext);
				mCallbackContext.success(selectedImagePath + "," + f.getName() + "," + ext + "," + type);
			}
		}
	}
	
	public class MyTestAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			//findVideo(mFileName);
	        //callAPI();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			mCallbackContext.success("success");
		}
    	
    }
	
	public void callAPI() {
		try {
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("WorkOrderId", mWorkOrderId));
			param.add(new BasicNameValuePair("NoteType", mNoteType));
			param.add(new BasicNameValuePair("SectionId", mSectionId));
			param.add(new BasicNameValuePair("FileName", mFileName));
			param.add(new BasicNameValuePair("FilePath", mFilePath));
			param.add(new BasicNameValuePair("FileSize", mFileSize));
			param.add(new BasicNameValuePair("FileExtension", mFileExtension));
			param.add(new BasicNameValuePair("BinaryValue",
					convertToBase64(Uri.fromFile(new File(mFilePath)))));
			param.add(new BasicNameValuePair("CreatedBy", mCreatedBy));
			param.add(new BasicNameValuePair("SurveyorId", mSurveyorId));

			getJsonFromURL(mUrl, 2, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

 public String findVideo(String filename) {
  final String selection = MediaStore.Video.VideoColumns.DISPLAY_NAME
    + " LIKE '%" + filename + "%'";

  Cursor cursor = cordova.getActivity().getContentResolver()
    .query(MediaStore.Files.getContentUri("external"), null,
      selection, null, null);
  if (cursor == null || !cursor.moveToFirst()) {
   return null;
  }
  String filePath;
  final int columnIndex = cursor
    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
  filePath = cursor.getString(columnIndex);
  cursor.close();
  Log.d("nihar testing only", "nihar test filepath :- " + filePath);
  return filePath;
 }


	
	public String convertToBase64(Uri uri) {
  InputStream is = null;
  try {
   is = cordova.getActivity().getContentResolver().openInputStream(uri);
  } catch (FileNotFoundException e) {
   e.printStackTrace();
  }
  ByteArrayOutputStream objByteArrayOS = new ByteArrayOutputStream();
  byte[] byteBufferString = new byte[(int) new File(uri.getPath()).length()];
  try {
   for (int readNum; (readNum = is.read(byteBufferString)) != -1;) {
    objByteArrayOS.write(byteBufferString, 0, readNum);
   }
  } catch (IOException e) {
   e.printStackTrace();
  }
  String videodata = Base64.encodeToString(byteBufferString,
    Base64.DEFAULT);

  return videodata;
 }

	protected String getJsonFromURL(String url, int method,
            List<NameValuePair> params) {
		// JSON Parsing will be done here
		
		try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
             
            // Checking http request method type
            if (method == 2) {
            	Log.d("nihar url", "nihar url :- " + url);
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                
                httpResponse = httpClient.execute(httpPost);
 
            } else if (method == 1) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                Log.d("nihar url", "nihar get url :- " + url);
                HttpGet httpGet = new HttpGet(url);
 
                httpResponse = httpClient.execute(httpGet);
 
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            Log.d("nihar testing response is :- ", "Nihar Response :- " + response);
			mCallbackContext.success("success");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        return response;
		
	}
	
	
}
