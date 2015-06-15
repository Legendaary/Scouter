package porcomsci.basketballscout.com.basketballscount.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import porcomsci.basketballscout.com.basketballscount.R;

/**
 * Created by GiftzyEiei on 15/6/2558.
 */
public class DialogUtil {
    public static void showOkDialog(Context context, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
