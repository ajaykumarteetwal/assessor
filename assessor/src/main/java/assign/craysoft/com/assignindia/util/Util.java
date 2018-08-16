package assign.craysoft.com.assignindia.util;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import assign.craysoft.com.assignindia.R;

/**
 * Created by ajay on 11/01/17.
 */

public class Util {
    private static SimpleDateFormat inputFormat3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
    private static SimpleDateFormat inputFormat2 = new SimpleDateFormat("dd/MM/yyyy HH/mm/ss", Locale.getDefault());
    private static SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd:HH:mm", Locale.getDefault());
    private static SimpleDateFormat outputFormat1 = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault());

    static {
        inputFormat1.setTimeZone(TimeZone.getTimeZone("UTC"));
        inputFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
        inputFormat3.setTimeZone(TimeZone.getTimeZone("UTC"));
        outputFormat1.setTimeZone(TimeZone.getDefault());
    }


    public static void sleep(long milliSec) {
        try {
            Thread.sleep(milliSec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isValidEmail(CharSequence email) {
        if (email == null || TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static String validateCardMonthMM(String mm) {
        return (mm = mm.trim()).matches("(?:0[1-9]|1[0-2])") ? mm : null;
    }

    public static String validateCardYear(String month, String year) {
        if ((year = year.trim()).matches("^(?:[0-9][0-9])?[0-9][0-9]$") && validateCardMonthMM(month) != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
            simpleDateFormat.setLenient(false);
            try {
                return !simpleDateFormat.parse(validateCardMonthMM(month) + "/" + year).before(new Date()) ? year : null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String validateCardCvv(String cvv) {
        return (cvv = cvv.trim()).matches("^[0-9]{3,4}$") ? cvv : null;
    }

    public static boolean isValidMobile2(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 6 || phone.length() > 13) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    public static boolean validateFirstName(String firstName) {
        return firstName.matches("[A-Z][a-zA-Z]*");
    }

    public static String validateName(String name) {
        return (name = name.trim()).matches("^[a-zA-Z\\\\s]*$") ? name : null;
    }

    public static String validateCardNumber(String ccNum) {
        if (ccNum != null && !((ccNum = ccNum.replace("-", "").trim()).isEmpty())) {
            ArrayList<String> listOfPattern = new ArrayList<String>();
            String ptVisa = "^4[0-9]{6,}$";
            listOfPattern.add(ptVisa);
            String ptMasterCard = "^5[1-5][0-9]{5,}$";
            listOfPattern.add(ptMasterCard);
            String ptAmeExp = "^3[47][0-9]{5,}$";
            listOfPattern.add(ptAmeExp);
            String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
            listOfPattern.add(ptDinClb);
            String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
            listOfPattern.add(ptDiscover);
            String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
            listOfPattern.add(ptJcb);
            for (String p : listOfPattern)
                if (ccNum.matches(p))
                    return ccNum;
        }
        return null;
    }

    public static boolean validateLastName(String lastName) {
        return lastName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
    }

    public static boolean validateHouseNumber(String lastName) {
        return lastName.length() > 2;
    }

    public static boolean isValidPasswordLogin(String password) {
        if (TextUtils.isEmpty(password) || password.length() < 4)
            return false;
        Pattern pattern;
        Matcher matcher;
//        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{3,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return true;//matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password) || password.length() < 4)
            return false;
        /*
        Pattern pattern;
        Matcher matcher;
//        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{3,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();*/
        return true;
    }

    @TargetApi(21)
    public static void buttonAnimation(View myView) {
        if (myView != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            try {
                int cx = myView.getMeasuredWidth() / 2;
                int cy = myView.getMeasuredHeight() / 2;
                int radius = Math.max(myView.getWidth(), myView.getHeight()) / 2;
                Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, radius * .98f, radius);
                anim.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static String getServerToLocalStringTime(String dateString) {
        try {
            return outputFormat1.format(inputFormat1.parse(dateString));
        } catch (Exception e) {
            try {
                return outputFormat1.format(inputFormat2.parse(dateString));
            } catch (Exception e2) {
                try {
                    return outputFormat1.format(inputFormat3.parse(dateString));
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
        return dateString;
    }

    public static boolean isNetworkOnline(Context context) {
        boolean status = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            status = (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!status)
            Toast.makeText(context, "No Internet connectivity.", Toast.LENGTH_SHORT).show();
        return status;

    }

    public static String getLocalToServerStringTime(String dateString) {
        try {
            return inputFormat1.format(outputFormat1.parse(dateString));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }

    public static String getCurrentServerStringTime() {
        try {
            return inputFormat1.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toCamelCase(final String init) {
        if (init == null)
            return null;
        StringBuilder ret = new StringBuilder(init.length());
        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length() == init.length()))
                ret.append(" ");
        }
        return ret.toString();
    }

    public static void shareApp(Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getMessage(context));
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    private static String getMessage(Context context) {
        return "Hey check out my app at: https://play.google.com/store/apps/details?id=" + context.getPackageName();
    }

    public static void openWhatsAppConversationFor(Context context, String phoneNumber) {
        String packegeName = "com.whatsapp";
        if (isThisPackageInstalled(context, packegeName)) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setComponent(new ComponentName(packegeName, "com.whatsapp.Conversation"));
            intent.putExtra("jid", PhoneNumberUtils.stripSeparators(phoneNumber.replaceAll("/+", "")) + "@s.whatsapp.net");//phone number without "+" prefix
            intent.putExtra(Intent.EXTRA_TEXT, getMessage(context));
            context.startActivity(intent);
        } else {
            Uri uri = Uri.parse("market://details?id=" + packegeName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
            Toast.makeText(context, "WhatsApp is not Install", Toast.LENGTH_SHORT).show();

        }
    }

    private static boolean isThisPackageInstalled(Context context, String packegeName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packegeName, PackageManager.GET_ACTIVITIES);
            if (packageInfo != null)
                return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static <T extends Fragment> T loadFragment(AppCompatActivity activity, T fragment, String name) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager != null) {
            if (fragmentManager.getBackStackEntryCount() > 0)
                fragmentManager.popBackStackImmediate();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(fragment);
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.fragmentContainner, fragment, name);
            transaction.addToBackStack(name);
            transaction.commit();
        }
        return fragment;
    }

    public static File getOutputMediaFile(Context context) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/" + context.getString(R.string.app_name));
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }
        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        return mediaFile;
    }

    public static File getOutputImageFile(Context context) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "" + context.getString(R.string.app_name));
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    public static void showDialog(Context context, String message, @Nullable final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        if (listener != null)
            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        builder.setPositiveButton(listener != null ? android.R.string.yes : android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.onClick(dialog, which);
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public static String checkDigit(long millis) {
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    private boolean validateDate(String input) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
        simpleDateFormat.setLenient(false);
        try {
            return !simpleDateFormat.parse(input).before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
