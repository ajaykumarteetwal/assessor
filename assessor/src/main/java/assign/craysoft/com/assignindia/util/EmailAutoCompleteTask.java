package assign.craysoft.com.assignindia.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajay on 07/02/17.
 */

public class EmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {

    private final Activity activity;
    private final AutoCompleteTextView mEmailView;

    public EmailAutoCompleteTask(Activity activity, AutoCompleteTextView mEmailView) {
        this.activity = activity;
        this.mEmailView = mEmailView;
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        ArrayList<String> emailAddressCollection = new ArrayList<>();
        ContentResolver cr = activity.getContentResolver();
        Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);
        while (emailCur.moveToNext()) {
            String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            emailAddressCollection.add(email);
        }
        emailCur.close();
        return emailAddressCollection;
    }

    @Override
    protected void onPostExecute(List<String> emailAddressCollection) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        if (mEmailView != null)
            mEmailView.setAdapter(adapter);
    }
}
