package com.tsiro.dogvip.petsitters.petsitter.other_details;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.jakewharton.rxbinding2.view.RxView;
import com.tsiro.dogvip.POJO.petsitter.PetSitterObj;
import com.tsiro.dogvip.R;
import com.tsiro.dogvip.databinding.ServicesFrgmtBinding;
import com.tsiro.dogvip.utilities.eventbus.RxEventBus;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by giannis on 8/9/2017.
 */

public class ServicesFrgmt extends Fragment implements PetSitterOtherDtlsContract.ViewFragment {

    private static final String debugTag = ServicesFrgmt.class.getSimpleName();
    private ServicesFrgmtBinding mBinding;
    private View mView;
    private PetSitterOtherDtlsContract.View viewContract;
    private PetSitterObj petSitterObj;
    private ArrayList<Integer> services = new ArrayList<>();

    public static ServicesFrgmt newInstance(PetSitterObj petSitterObj) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("parcelable_obj", petSitterObj);
        ServicesFrgmt servicesFrgmt = new ServicesFrgmt();
        servicesFrgmt.setArguments(bundle);
        return servicesFrgmt;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if ( context instanceof Activity) this.viewContract = (PetSitterOtherDtlsContract.View) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.services_frgmt, container, false);
            mView = mBinding.getRoot();
        }
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PetSitterOtherDtlsPresenter petSitterOtherDtlsPresenter = new PetSitterOtherDtlsPresenter(this);
        mBinding.setPresenter(petSitterOtherDtlsPresenter);
        if (getArguments() != null) {
            petSitterObj = getArguments().getParcelable(getResources().getString(R.string.parcelable_obj));
            services.add(1);
            services.add(6);
            petSitterObj.setServices(services);
            fetchPetSitterServices();
//            mBinding.setObj(petSitterObj);
//            Log.e(debugTag, petSitterObj.getId() + " ID, NAME: " + petSitterObj.getName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Disposable disp = RxView.clicks(mBinding.nextBtn).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                saveDetails();
            }
        });
        RxEventBus.add(this, disp);
        Disposable disp1 = RxView.clicks(mBinding.previousBtn).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                viewContract.onPreviousClick(0);
            }
        });
        RxEventBus.add(this, disp1);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxEventBus.unregister(this);
    }

    @Override
    public void onServiceCheckBoxClick(View view) {
        if (view instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) view;
            if (checkBox.isChecked()) {
                services.add(Integer.valueOf(view.getTag().toString()));
            } else {
                if (services.contains(Integer.valueOf(view.getTag().toString()))) services.remove(Integer.valueOf(view.getTag().toString()));
            }
            Log.e(debugTag, view.getTag() + " TAG, "+ checkBox.isChecked());
        }
    }

    private void fetchPetSitterServices() {
        for (Integer service: services) {
            View view = mBinding.containerRlt.findViewWithTag(String.valueOf(service));
            if (view instanceof CheckBox) ((CheckBox) view).setChecked(true);
        }
    }

    private void saveDetails() {
        petSitterObj.setServices(services);
        viewContract.onNextClick(2, petSitterObj);
    }
}
