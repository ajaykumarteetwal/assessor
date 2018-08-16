package assign.craysoft.com.assignindia.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import assign.craysoft.com.assignindia.R;
import assign.craysoft.com.assignindia.util.Util;

public class NetworkUtil {
    private static NetworkUtil instance = new NetworkUtil();

    private NetworkUtil() {
    }

    public static NetworkUtil getInstance() {
        return instance;
    }

    public void connectPostRequest(final Context context, String url, String postJson, final CallBack callBack, final boolean loader) {
        new AsyncTask<String, Void, JSONObject>() {
            private ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                if (Util.isNetworkOnline(context)) ;
                if (loader)
                    dialog = ProgressDialog.show(context, "", context.getString(R.string.pleaseWait), false, true);
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                synchronized (this) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    String url = strings[0];
                    String data = strings[1];
                    if (url.endsWith(".json"))
                        return readFromFile(context, strings[0]);
                    else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                if (callBack != null) {
                    if (jsonObject != null)
                        callBack.done(jsonObject);
                    else
                        callBack.error(jsonObject);
                }
            }
        }.execute(url, postJson);
    }

    public void connectGetRequest(final Context context, String url, String params, final CallBack callBack) {

    }

    public JSONObject readFromFile(Context context, String fileName) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = context.getResources().getAssets().open(fileName);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (inputStreamReader != null)
                    inputStreamReader.close();
                if (inputStream != null)
                    inputStream.close();
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return new JSONObject(stringBuilder.toString());
    }

    public interface CallBack {
        void done(JSONObject jsonObject);

        void error(JSONObject jsonObject);
    }

}
