import { Utils } from "@nativescript/core";

declare var android: any;

export class UIHelper {
    static showToast(message: string) {
        if (typeof android !== 'undefined') {
            android.widget.Toast.makeText(
                Utils.android.getApplicationContext(),
                message,
                android.widget.Toast.LENGTH_SHORT
            ).show();
        }
    }
}
