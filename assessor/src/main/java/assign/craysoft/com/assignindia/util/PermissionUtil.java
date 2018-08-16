package assign.craysoft.com.assignindia.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ajay on 14-May-16.
 */
public class PermissionUtil {

    private final int REQUEST_CODE = new Random().nextInt(34) + 1;
    private PermissionObserver observable;
    private String why;

    public PermissionUtil() {

    }

    /**
     * Check permissions Status.
     *
     * @param activity    Current Activity
     * @param permissions android.Manifest
     * @return true if all permissions will be pre-granted.
     */
    public boolean havePermissionOf(@NonNull Activity activity, @NonNull String... permissions) {
        boolean status = Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1;
        if (!status)
            for (String permission : permissions) {
                status = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if (!status)
                    break;
            }
        return status;
    }

    /**
     * Request for permissions to be grant.
     *
     * @param activity    Current Activity
     * @param permissions android.Manifest
     * @param why         Reason of permissions
     * @param observable  permissions result observer {@notifyObservers(Object) a boolean as status}
     */
    public void requestPermission(@NonNull Activity activity, @NonNull String[] permissions, @NonNull String why, @Nullable PermissionObserver observable) {
        requestPermission(activity, permissions, why, observable, false);
    }

    private void requestPermission(final Activity activity, final String[] permissions, final String why, final PermissionObserver observable, boolean isSelf) {
        this.observable = observable;
        this.why = why;
        assert permissions != null;
        final ArrayList<String> unGrantedPermissions = new ArrayList<String>();
        for (String permission : permissions)
            if (!havePermissionOf(activity, permission))
                if (!unGrantedPermissions.contains(permission))
                    unGrantedPermissions.add(permission);
        if (!unGrantedPermissions.isEmpty()) {
            if (!isSelf && ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    unGrantedPermissions.get(unGrantedPermissions.size() > 1 ? new Random().nextInt(unGrantedPermissions.size()) : 0))) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(why);
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission(activity, permissions, why, observable, true);
                        dialog.cancel();
                    }
                });
                builder.create().show();
            } else
                ActivityCompat.requestPermissions(activity, unGrantedPermissions.toArray(new String[unGrantedPermissions.size()]), REQUEST_CODE);
        } else if (observable != null)
            observable.notifyObservers(true);
    }

    /**
     * parse permissions result to respond result Observer {@requestPermission}
     *
     * @param requestCode  request code .
     * @param permissions  request permissions.
     * @param grantResults grant Result of permissions
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (observable != null) {
                boolean status = grantResults.length > 0;
                for (int grantResult : grantResults) {
                    status = grantResult == PackageManager.PERMISSION_GRANTED;
                    if (!status)
                        break;
                }
                observable.notifyObservers(status);
            }
        }
    }

    public void openAppSetting(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
        Toast.makeText(context, why == null ? "Permissions required to use this feature." : why, Toast.LENGTH_SHORT).show();
    }

    public interface PermissionObserver {
        void notifyObservers(boolean status);
    }
}
