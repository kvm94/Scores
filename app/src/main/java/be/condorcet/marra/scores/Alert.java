package be.condorcet.marra.scores;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Kevin on 22-12-16.
 */

public class Alert {


    public static void showSimpleAlert(Context context, String message){
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }

    public static void showSimpleErrorAlert(Context context, String message){
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.error))
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }

    public static void showConfirmationMessage(Context context, String message, final Activity activity){
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .show();
    }
}
