package com.project.jemberliburan.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.project.jemberliburan.R;

public class LogoutConfirmationDialog extends DialogFragment {

    // Interface untuk komunikasi dengan fragment/activity
    public interface LogoutListener {
        void onLogoutConfirmed();
    }

    private LogoutListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Cek apakah parent fragment mengimplementasikan LogoutListener
        if (getParentFragment() instanceof LogoutListener) {
            listener = (LogoutListener) getParentFragment();
        } else if (context instanceof LogoutListener) {
            listener = (LogoutListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " harus mengimplementasikan LogoutListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate layout dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_logout_confirmation, null);

        // Inisialisasi komponen dalam dialog

        TextView textViewTitle = view.findViewById(R.id.textViewTitle);
        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        Button buttonConfirm = view.findViewById(R.id.buttonConfirm);

        // Anda dapat menyesuaikan teks atau ikon secara dinamis jika diperlukan

        // Membuat builder untuk dialog dengan tema kustom
        Dialog dialog = new Dialog(getActivity(), R.style.TransparentDialog);
        dialog.setContentView(view);
        dialog.setCancelable(false); // Tidak bisa ditutup dengan klik di luar dialog

        // Aksi tombol "Tidak"
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(); // Tutup dialog
            }
        });

        // Aksi tombol "Ya"
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(); // Tutup dialog
                if (listener != null) {
                    listener.onLogoutConfirmed(); // Panggil metode logout
                }
            }
        });

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Mengatur background window menjadi transparan
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
