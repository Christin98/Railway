package com.kha.train.dialog;

import android.app.Dialog;
import android.content.Context;
import com.kha.train.R;

public class DataLoaderdialog extends Dialog {
    public DataLoaderdialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_loader_view);
        setCancelable(false);
    }
}
