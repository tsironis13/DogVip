package com.tsiro.dogvip.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.tsiro.dogvip.POJO.DialogActions;
import com.tsiro.dogvip.R;
import com.tsiro.dogvip.accountmngr.MyAccountManager;
import com.tsiro.dogvip.utilities.common.CommonUtls;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by giannis on 4/6/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements Lifecycle.View {

    private static final String debugTag = BaseActivity.class.getSimpleName();
    public abstract Lifecycle.ViewModel getViewModel();
    private Dialog.Builder mBuilder;
    private ProgressDialog mProgressDialog;
    private MyAccountManager myAccountManager;
    private CommonUtls mCommonUtls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAccountManager = new MyAccountManager(this);
        mCommonUtls = new CommonUtls(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getViewModel() != null) getViewModel().onViewAttached(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getViewModel() != null) getViewModel().onViewResumed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getViewModel() != null) getViewModel().onViewDetached();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public MyAccountManager getMyAccountManager() {
        return myAccountManager;
    }


    //    public Observable<DialogActions> pickDateDialog() {
//        return Observable.create(new ObservableOnSubscribe<DialogActions>() {
//            @Override
//            public void subscribe(@NonNull final ObservableEmitter<DialogActions> subscriber) throws Exception {
//                mBuilder = new DatePickerDialog.Builder() {
//                    @Override
//                    public void onPositiveActionClicked(DialogFragment fragment) {
//                        DatePickerDialog dialog = (DatePickerDialog) fragment.getDialog();
////                        Log.e("aa", dialog.getDate()/1000+"");
//                        subscriber.onNext(new DialogActions("", 1, dialog.getDate()/1000, dialog.getFormattedDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()))));
//                        super.onPositiveActionClicked(fragment);
//                    }
//                    @Override
//                    public void onNegativeActionClicked(DialogFragment fragment) {
//                        super.onNegativeActionClicked(fragment);
//                        subscriber.onNext(new DialogActions("", 0, 0, ""));
//                    }
//                };
//                mBuilder
//                        .positiveAction(getResources().getString(R.string.ok))
//                        .negativeAction(getResources().getString(R.string.cancel));
//                showDialog();
//            }
//        });
//    }

    public Observable<DialogActions> initializeGenericDialog(final String action, final String desc, final String title, final String negativeBtnTxt, final String positiveBtnTxt) {
        return Observable.create(new ObservableOnSubscribe<DialogActions>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<DialogActions> subscriber) throws Exception {
                mBuilder = new SimpleDialog.Builder() {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        super.onPositiveActionClicked(fragment);
                        subscriber.onNext(new DialogActions(action, 1, 0, null));
                    }
                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        super.onNegativeActionClicked(fragment);
                        subscriber.onNext(new DialogActions(action, 0, 0, null));
                    }
                };
                ((SimpleDialog.Builder)mBuilder).message(desc)
                        .title(title)
                        .positiveAction(positiveBtnTxt)
                        .negativeAction(negativeBtnTxt);
                showDialog();
            }
        });
    }

    //check if network is available
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public ProgressDialog initializeProgressDialog(String msg) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
//        Log.e(debugTag, "SHOW DIALOG");
        mProgressDialog.show();

        return mProgressDialog;
    }

    public void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
    }

    private void showDialog() {
        DialogFragment fragment = DialogFragment.newInstance(mBuilder);
        fragment.show(getSupportFragmentManager(), null);
    }

    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public CommonUtls getCommonUtls() {
        return mCommonUtls;
    }
}
