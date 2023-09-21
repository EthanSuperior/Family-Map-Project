package com.example.familymapclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shared.model.AuthToken;
import com.example.shared.model.Person;
import com.example.shared.request.LoginRequest;
import com.example.shared.request.RegisterRequest;
import com.example.shared.result.LoginResult;
import com.example.shared.result.RegisterResult;

public class LoginFragment extends Fragment {
    private EditText hostNameEditText, portNumberEditText, usernameEditText, passwordEditText, firstNameEditText, lastNameEditText, emailEditText;
    private RadioGroup genderRadioGroup;
    private Button loginButton, registerButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginFragment fragment = this;
        hostNameEditText = view.findViewById(R.id.hostName);
        portNumberEditText = view.findViewById(R.id.portNumber);
        usernameEditText = view.findViewById(R.id.username);
        passwordEditText = view.findViewById(R.id.password);
        firstNameEditText = view.findViewById(R.id.firstName);
        lastNameEditText = view.findViewById(R.id.lastName);
        emailEditText = view.findViewById(R.id.email);

        genderRadioGroup = view.findViewById(R.id.gender);

        loginButton = view.findViewById(R.id.login);
        registerButton = view.findViewById(R.id.register);

        TextWatcher infoWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Enable Log-in
                loginButton.setEnabled(!hostNameEditText.getText().toString().isEmpty() && !portNumberEditText.getText().toString().isEmpty() &&
                        !usernameEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty());

                //Enable Register
                registerButton.setEnabled(!hostNameEditText.getText().toString().isEmpty() && !portNumberEditText.getText().toString().isEmpty() &&
                        !usernameEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty() &&
                        !firstNameEditText.getText().toString().isEmpty() && !lastNameEditText.getText().toString().isEmpty() &&
                        !emailEditText.getText().toString().isEmpty() && genderRadioGroup.getCheckedRadioButtonId() != -1);
            }
        };

        hostNameEditText.addTextChangedListener(infoWatcher);
        portNumberEditText.addTextChangedListener(infoWatcher);
        usernameEditText.addTextChangedListener(infoWatcher);
        passwordEditText.addTextChangedListener(infoWatcher);
        firstNameEditText.addTextChangedListener(infoWatcher);
        lastNameEditText.addTextChangedListener(infoWatcher);
        emailEditText.addTextChangedListener(infoWatcher);

        loginButton.setEnabled(!hostNameEditText.getText().toString().isEmpty() && !portNumberEditText.getText().toString().isEmpty() &&
                !usernameEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty());

        registerButton.setEnabled(!hostNameEditText.getText().toString().isEmpty() && !portNumberEditText.getText().toString().isEmpty() &&
                !usernameEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty() &&
                !firstNameEditText.getText().toString().isEmpty() && !lastNameEditText.getText().toString().isEmpty() &&
                !emailEditText.getText().toString().isEmpty() && genderRadioGroup.getCheckedRadioButtonId() != -1);

        genderRadioGroup = view.findViewById(R.id.gender);

        loginButton.setOnClickListener(v -> {
            ServerProxy.setURL(hostNameEditText.getText().toString(), portNumberEditText.getText().toString());
            LoginTask task = new LoginTask();
            task.execute(new LoginRequest(usernameEditText.getText().toString(), passwordEditText.getText().toString()));
        });

        registerButton.setOnClickListener(v -> {
            ServerProxy.setURL(hostNameEditText.getText().toString(), portNumberEditText.getText().toString());
            RegisterTask task = new RegisterTask();
            task.execute(new RegisterRequest(usernameEditText.getText().toString(), passwordEditText.getText().toString(), emailEditText.getText().toString(),
                    firstNameEditText.getText().toString(), lastNameEditText.getText().toString(), (genderRadioGroup.getCheckedRadioButtonId() == R.id.radioMale) ? "m" : "f"));
        });
    }

    private class LoginTask extends AsyncTask<LoginRequest, Integer, LoginResult> {

        @Override
        protected void onPostExecute(LoginResult loginResult) {
            super.onPostExecute(loginResult);
            System.out.println(loginResult.message);
            if (loginResult.success) {
                DataCache.authToken = new AuthToken(loginResult.getUserName(), loginResult.getAuthToken());
                DataCache.userID = loginResult.getPersonID();
                RetrieveDataTask retrieveDataTask = new RetrieveDataTask();
                retrieveDataTask.execute(DataCache.authToken);
                //Change Fragment
            } else {
                Toast.makeText(loginButton.getContext(), "Login Failed", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected LoginResult doInBackground(LoginRequest... loginRequests) {
            return new ServerProxy().login(loginRequests[0]);
        }
    }

    private class RegisterTask extends AsyncTask<RegisterRequest, Integer, RegisterResult> {

        @Override
        protected RegisterResult doInBackground(RegisterRequest... registerRequests) {
            return ServerProxy.register(registerRequests[0]);
        }

        @Override
        protected void onPostExecute(RegisterResult registerResult) {
            super.onPostExecute(registerResult);
            if (registerResult.success) {
                DataCache.authToken = new AuthToken(registerResult.getUserName(), registerResult.getAuthToken());
                DataCache.userID = registerResult.getPersonID();
                RetrieveDataTask retrieveDataTask = new RetrieveDataTask();
                retrieveDataTask.execute(DataCache.authToken);
            } else {
                Toast.makeText(registerButton.getContext(), "Register Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class RetrieveDataTask extends AsyncTask<AuthToken, Integer, String> {
        @Override
        protected String doInBackground(AuthToken... authTokens) {
            AuthToken token = authTokens[0];
            DataCache.readPeople(ServerProxy.getPeople(token));
            DataCache.readEvents(ServerProxy.getEvents(token));
            Person user = DataCache.getUser();
            return "Welcome " + user.getFirstName() + " " + user.getLastName();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Change Fragment
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("IsLoggedIn", true);
            startActivity(intent);
        }
    }
}