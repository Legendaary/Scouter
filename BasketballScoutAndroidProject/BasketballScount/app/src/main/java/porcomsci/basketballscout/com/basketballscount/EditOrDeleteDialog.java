package porcomsci.basketballscout.com.basketballscount;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;


/**
 * Created by Opal on 3/23/15 AD.
 */
public class EditOrDeleteDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.edit_or_delete_dialog, null));

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
