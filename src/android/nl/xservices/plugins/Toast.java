package nl.xservices.plugins;

import android.view.Gravity;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/*
    // TODO nice way for the Toast plugin to offer a longer delay than the default short and long options
    // TODO also look at https://github.com/JohnPersano/Supertoasts
    new CountDownTimer(6000, 1000) {
      public void onTick(long millisUntilFinished) {toast.show();}
      public void onFinish() {toast.show();}
    }.start();
 */
public class Toast extends CordovaPlugin {

  private static final String ACTION_SHOW_EVENT = "show";

  @Override
  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
  
  //callbackContext.error("my test only");
//		String data = "Testing Java Code";
//		callbackContext.success(data);
//			return true;
			
//    if (ACTION_SHOW_EVENT.equals(action)) {

//      final String message = args.getString(0);
//      final String duration = args.getString(1);
//      final String position = args.getString(2);

//      cordova.getActivity().runOnUiThread(new Runnable() {
//        public void run() {
		
//		callbackContext.error("my test only");
//		String data = "Testing Java Code";
//			return data;
		
//          android.widget.Toast toast = android.widget.Toast.makeText(webView.getContext(), message, 0);

//          if ("top".equals(position)) {
//            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 20);
//          } else  if ("bottom".equals(position)) {
//            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 20);
//          } else if ("center".equals(position)) {
//            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
//          } else {
//            callbackContext.error("invalid position. valid options are 'top', 'center' and 'bottom'");
//            return;
//          }

		  //callbackContext.success("invalid position. valid options are 'top', 'center' and 'bottom'");
		  
//          if ("short".equals(duration)) {
//            toast.setDuration(android.widget.Toast.LENGTH_SHORT);
//          } else if ("long".equals(duration)) {
//            toast.setDuration(android.widget.Toast.LENGTH_LONG);
//          } else {
//            callbackContext.error("invalid duration. valid options are 'short' and 'long'");
 //           return;
//          }

//          toast.show();
//          callbackContext.success("Testing ok");
//        }
//      });

//      return true;
//    } else {
//      callbackContext.error("toast." + action + " is not a supported function. Did you mean '" + ACTION_SHOW_EVENT + "'?");
//      return false;
//    }
	
	
	try {
			//Log.d("nihar testing", "nihar action " + action);
			JSONObject arg_object = args.getJSONObject(0);
				String path = arg_object.getString("filepath");
			//Log.d("nihar testing", "nihar path " + path);
			File file = new File(path);
			byte[] bytes = null;
			bytes = loadFile(file);
			String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
			//Log.d("nihar testing only", "nihar test base file :- "
				//	+ encodedString);
			callbackContext.success(encodedString);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			callbackContext.error(e.toString());
			return false;
		}

  }
  
  private static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		is.close();
		return bytes;
	}
}
