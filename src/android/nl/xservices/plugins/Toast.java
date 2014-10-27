package nl.xservices.plugins;

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


public class Toast extends CordovaPlugin {

	private static final String ACTION_SHOW_EVENT = "show";
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
	
	
	@Override
	public boolean execute(String action, JSONArray args,
			final CallbackContext callbackContext) throws JSONException {

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
			callbackContext.success(encodedString);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			callbackContext.error(e.toString());
			return false;
		}

	}

	public class MyTestAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			findVideo(filename);
	        callAPI();
			return null;
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
					convertToBase64(Uri.fromFile(new File(findVideo(mFileName))))));
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

	
}
