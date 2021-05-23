package com.example.pickupmeal.sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pickupmeal.R;
import com.example.pickupmeal.interfaces.OnClickViewJListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddFoodBottomSheet extends BottomSheetDialogFragment {

    private OnClickViewJListener listener;

    public void setListener(OnClickViewJListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_food_bottom_sheet, container, false);

        final EditText edtName = (EditText)v.findViewById(R.id.edt_name);
        final EditText edtCalo = (EditText)v.findViewById(R.id.edt_calo);
        Button btnConfirm = (Button)v.findViewById(R.id.btn_confirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().toString().isEmpty() || edtCalo.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin trường!", Toast.LENGTH_SHORT).show();
                    return;
                }

                listener.onClick(edtName.getText().toString(), Integer.valueOf(edtCalo.getText().toString()));
            }
        });

        return v;
    }
}
