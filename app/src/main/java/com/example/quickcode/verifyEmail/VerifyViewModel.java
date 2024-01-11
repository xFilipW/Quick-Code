package com.example.quickcode.verifyEmail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.lifecycle.ViewModel;

import com.example.quickcode.R;
import com.example.quickcode.common.utils.ResizeUtils;
import com.example.quickcode.consts.Consts;
import com.example.quickcode.databinding.ActivityVerifyEmailBinding;
import com.example.quickcode.databinding.FragmentVerifyPinviewBinding;
import com.example.quickcode.rest.QuickCodeClient;
import com.example.quickcode.rest.verify.VerifyError;
import com.example.quickcode.rest.verify.VerifyFailure;
import com.example.quickcode.rest.verify.VerifyListener;
import com.example.quickcode.rest.verify.VerifyResponse;
import com.example.quickcode.rest.verify.VerifySuccess;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class VerifyViewModel extends ViewModel {

    public static final int DISMISS_DELAY_MILLIS = 200;

    void setupToolbarBehaviour(ActivityVerifyEmailBinding pinBinding, Runnable runnable) {
        pinBinding.toolbar.setNavigationOnClickListener(
                v -> v.postDelayed(runnable, DISMISS_DELAY_MILLIS)
        );
    }

    private static void scrollToTopsViewBorder(FragmentVerifyPinviewBinding binding, ImageView targetView) {
        binding.getRoot().post(() -> binding.scrollView.smoothScrollTo(0, targetView.getTop()));
    }

    @SuppressLint("ClickableViewAccessibility")
    void setFocusListeners(ActivityVerifyEmailBinding binding) {
        View currentView = binding.viewSwitcher.getCurrentView();

        if (currentView.getId() == R.id.switch_view_pin) {
            FragmentVerifyPinviewBinding fragmentBinding = FragmentVerifyPinviewBinding.bind(currentView);

            fragmentBinding.pinView.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    //ResizeUtils.resizeView((int) (fragmentBinding.getRoot().getHeight() * .35f), fragmentBinding.test);
                    ResizeUtils.resizeView((int) (fragmentBinding.getRoot().getHeight() * .35f), fragmentBinding.bottomSpacer);
                    scrollToTopsViewBorder(fragmentBinding, fragmentBinding.imageView);
                }
            });
        }
    }

    public void setExpanded(BottomSheetDialog bottomSheetDialog) {
        View bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);

        if (bottomSheet instanceof FrameLayout) {
            Context context = bottomSheet.getContext();

            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            bottomSheet.setLayoutParams(layoutParams);
            bottomSheet.setMinimumHeight(context.getResources().getDisplayMetrics().heightPixels);

            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setDraggable(false);
        }
    }

    public void setDraggable(BottomSheetDialog bottomSheetDialog, boolean draggable) {
        View bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);

        if (bottomSheet instanceof FrameLayout) {
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setDraggable(draggable);
        }
    }

    void setBottomSheetDimDisabled(Dialog dialog) {
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    void configureDialogOnShow(BottomSheetDialog onShowDialog) {
        setExpanded(onShowDialog);
        setDraggable(onShowDialog, false);
    }

    public void verifyUser(long userId, String token, VerifyListener verifyListener) {
        QuickCodeClient instance = QuickCodeClient.getInstance();

        Handler handler = new Handler(Looper.getMainLooper());
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(3));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Call<VerifyResponse> verify = instance.verify(userId, token);
            try {
                Response<VerifyResponse> execute = verify.execute();
                VerifyResponse body = execute.body();

                if (execute.isSuccessful()) {
                    handler.post(() ->
                            verifyListener.onVerify(new VerifySuccess(body))
                    );
                } else {
                    try {
                        JSONObject jsonObjectError = new JSONObject(execute.errorBody().string());
                        int errorCode = jsonObjectError.optInt("error_code");

                        handler.post(() ->
                                verifyListener.onVerify(new VerifyFailure(errorCode))
                        );

                    } catch (JSONException e) {
                        handler.post(() -> {
                            verifyListener.onVerify(new VerifyError(e));
                            e.printStackTrace();
                        });
                    }
                }
            } catch (IOException e) {
                handler.post(() -> {
                    verifyListener.onVerify(new VerifyError(e));
                    e.printStackTrace();
                });
            }
        });
    }

    public long getUserId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(Consts.SP_USER_ID, -1);
    }

    public String getDisplayUsername(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(Consts.SP_USERNAME, null);
    }
}
