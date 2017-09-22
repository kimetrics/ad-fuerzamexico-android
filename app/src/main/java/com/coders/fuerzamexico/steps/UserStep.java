package com.coders.fuerzamexico.steps;

import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.coders.fuerzamexico.R;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.regex.Pattern;

/**
 * Created by usuario on 21/09/17.
 */

public class UserStep extends AbstractStep {
    @Override
    public String name() {
        return "Usuario";
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    private EditText txtName;
    private EditText txtEmail;
    private EditText txtPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_user, container, false);
        txtName = v.findViewById(R.id.txtName);
        txtEmail = v.findViewById(R.id.txtEmail);
        txtPhone = v.findViewById(R.id.txtPhone);
        return v;
    }

    boolean isEmailValid = true;

    @Override
    public boolean nextIf() {
        boolean isValid = txtName.getText().toString().length() > 4;
        isEmailValid = isEmailValid(txtEmail.getText().toString());

        if(!txtEmail.getText().toString().isEmpty()){
            isValid = isEmailValid;
        }

        mStepper.getExtras().putString("NAME", txtName.getText().toString());
        mStepper.getExtras().putString("EMAIL", txtEmail.getText().toString());
        mStepper.getExtras().putString("PHONE", txtPhone.getText().toString());

        return isValid;
    }

    @Override
    public String error() {
        String msj = "<b>Error</b> <small>Ingresa tu nombre completo</small>";
        if(!isEmailValid){
            msj = "<b>Error</b> <small>El email no es válido</small>";
        }
        return msj;
    }

    private boolean isEmailValid(String email){
        Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );

        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
}
