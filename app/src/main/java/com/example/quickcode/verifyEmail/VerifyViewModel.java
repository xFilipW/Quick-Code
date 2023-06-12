package com.example.quickcode.verifyEmail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.lifecycle.ViewModel;

import com.example.quickcode.R;
import com.example.quickcode.common.utils.ScrollUtils;
import com.example.quickcode.databinding.ActivityVerifyEmailBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class VerifyViewModel extends ViewModel {

    public static final int DISMISS_DELAY_MILLIS = 200;

    void setupToolbarBehaviour(ActivityVerifyEmailBinding pinBinding, Runnable runnable) {
        pinBinding.toolbar.setNavigationOnClickListener(
                v -> v.postDelayed(runnable, DISMISS_DELAY_MILLIS)
        );
    }

    @SuppressLint("ClickableViewAccessibility")
    void setTouchListeners(ActivityVerifyEmailBinding binding) {
        binding.pinView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                ScrollUtils.smartScrollTo(() -> binding.pinView, "touch");
            }
            return false;
        });

        binding.scrollView.setOnTouchListener((v, event) -> {
            if (binding.pinView.hasFocus()) {
                binding.pinView.clearFocus();
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    void setFocusListeners(ActivityVerifyEmailBinding binding) {
        binding.pinView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ScrollUtils.smartScrollTo(() -> binding.pinView, "focus");
            }
        });
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
}
