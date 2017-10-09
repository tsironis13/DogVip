package com.tsiro.dogvip.lovematch.viewmodel;

import android.util.Log;

import com.tsiro.dogvip.POJO.lovematch.LoveMatchRequest;
import com.tsiro.dogvip.POJO.lovematch.LoveMatchResponse;
import com.tsiro.dogvip.app.AppConfig;
import com.tsiro.dogvip.lovematch.LoveMatchContract;
import com.tsiro.dogvip.requestmngrlayer.LoveMatchRequestManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.AsyncProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by giannis on 8/10/2017.
 */

public class LikeDislikeViewModel extends LoveMatchViewModel implements LoveMatchContract.LikeDislikePetViewModel {

    private static final String debugTag = GetPetsViewModel.class.getSimpleName();
    private AsyncProcessor<LoveMatchResponse> mProcessor;
    private Disposable mLoveMatchDisp;
//    private LoveMatchContract.View mViewClback1;

//    @Inject
    public LikeDislikeViewModel(LoveMatchRequestManager mLoveMatchRequestManager) {
        super(mLoveMatchRequestManager);
    }

    @Override
    public void onViewResumed() {
//        super.onViewResumed();
        if (mLoveMatchDisp != null && getRequestState() != AppConfig.REQUEST_RUNNING) {
            mProcessor
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LikeDisplikeObserver());
        }
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
//        mViewClback = null;
        if (mLoveMatchDisp != null) {
            mLoveMatchDisp.dispose();
        }
    }

    private void onSuccessGetPets(LoveMatchResponse response) {
        mLoveMatchDisp = null;
//        onSuccessGetPets1(response);
//        mViewClback.onSuccess(response);
    }

    private void onErrorGetPets(int code) {
        mLoveMatchDisp = null;
//        onErrorGetPets1(code);
//        mViewClback.onError(code);
//        if (mViewClback != null) setRequestState(AppConfig.REQUEST_NONE);
    }

    @Override
    public void likeDislikePet(LoveMatchRequest request) {

    }

//    public void likeDislikePet(LoveMatchRequest request) {
//        if (getRequestState() != AppConfig.REQUEST_RUNNING) {
////            mViewClback = view;
//            setRequestState(AppConfig.REQUEST_RUNNING);
//            mProcessor = AsyncProcessor.create();
//            mLoveMatchDisp = mProcessor
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeWith(new LikeDisplikeObserver());
//
//            mLoveMatchRequestManager.getPetsByFilter(request, this).subscribe(mProcessor);
//        }
//    }

//    @Override
//    public void getPetsByFilter(LoveMatchRequest request) {
////        if (getRequestState() != AppConfig.REQUEST_RUNNING) {
////            setRequestState(AppConfig.REQUEST_RUNNING);
////            mProcessor = AsyncProcessor.create();
////            mLoveMatchDisp = mProcessor
////                    .subscribeOn(Schedulers.io())
////                    .observeOn(AndroidSchedulers.mainThread())
////                    .subscribeWith(new GetPetsByFilterObserver());
////
////            mLoveMatchRequestManager.getPetsByFilter(request, this).subscribe(mProcessor);
////        }
//    }

    private class LikeDisplikeObserver extends DisposableSubscriber<LoveMatchResponse> {

        @Override
        public void onNext(LoveMatchResponse response) {
            if (response.getCode() != AppConfig.STATUS_OK) {
                onErrorGetPets(AppConfig.getCodes().get(response.getCode()));
            } else {
                onSuccessGetPets(response);
            }
        }

        @Override
        public void onError(Throwable t) {
            onErrorGetPets(AppConfig.getCodes().get(AppConfig.STATUS_ERROR));
        }

        @Override
        public void onComplete() {

        }
    }
}
