package me.maxandroid.italker.frags.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.maxandroid.common.app.Application;
import me.maxandroid.italker.LaunchActivity;
import me.maxandroid.italker.R;
import me.maxandroid.italker.activities.MainActivity;
import me.maxandroid.italker.frags.media.GalleryFragment;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermissionFragment extends BottomSheetDialogFragment implements EasyPermissions.PermissionCallbacks {
    private static final int RC = 0x0100;
    GalleryFragment.TransStatusBottomSheetDialog dialog;

    public PermissionFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new GalleryFragment.TransStatusBottomSheetDialog(getContext());
        dialog.setCancelable(false);
        return dialog;
//        return new GalleryFragment.TransStatusBottomSheetDialog(getContext())
//                .setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_permission, container, false);
        root.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPerm();
            }
        });
        refreshState(root);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshState(getView());
    }

    private void refreshState(View root) {
        if (root == null) return;
        Context context = getContext();
        root.findViewById(R.id.im_state_permission_network).setVisibility(haveNetwork(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_read).setVisibility(haveRead(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_write).setVisibility(haveWrite(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_audio).setVisibility(haveRecordAudio(context) ? View.VISIBLE : View.GONE);
    }

    private static boolean haveNetwork(Context context) {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    private static boolean haveRead(Context context) {
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    private static boolean haveWrite(Context context) {
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    private static boolean haveRecordAudio(Context context) {
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    private static void show(FragmentManager manager) {
        new PermissionFragment()
                .show(manager, PermissionFragment.class.getName());
    }

    public static boolean haveAll(Context context, FragmentManager manager) {
        boolean haveAll = haveNetwork(context)
                && haveRead(context)
                && haveWrite(context)
                && haveRecordAudio(context);

        if (!haveAll) {
            show(manager);
        }
        return haveAll;
    }

    @AfterPermissionGranted(RC)
    private void requestPerm() {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            Application.showToast(R.string.label_permission_ok);
            refreshState(getView());
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.title_assist_permissions), RC, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        dialog.dismiss();
        if (getActivity() instanceof LaunchActivity) {
            MainActivity.show(getActivity());
            getActivity().finish();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .build()
                    .show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
