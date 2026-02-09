package com.example.foodplanner.Presentation.loginScreen.view;

import static android.provider.Settings.System.getString;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.foodplanner.Presentation.loginScreen.presenter.LoginPresenterImp;
import com.example.foodplanner.R;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment implements LoginViewer {

    private TextView goToSignUp;
    private Button loginButton;
    private EditText loginEmailInput;
    private EditText loginPassInput;
    private Button signIngoogleButton;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private LoginPresenterImp presenter;

    public LoginFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData())
                                    .getResult(ApiException.class);
                            presenter.loginWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            onLoginError("Google Sign In Failed");
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goToSignUp = view.findViewById(R.id.loginSignUp);
        loginButton = view.findViewById(R.id.loginButton);
        loginPassInput = view.findViewById(R.id.loginPassInput);
        loginEmailInput = view.findViewById(R.id.loginEmailInput);
        signIngoogleButton = view.findViewById(R.id.signIngoogleButton);

        presenter = new LoginPresenterImp(this,getContext());
        mAuth = FirebaseAuth.getInstance();
        presenter.cheekIfUserExist();
        presenter.setupGoogle();


        loginButton.setOnClickListener(v -> {
            presenter.loginWithEmail(loginEmailInput.getText().toString(), loginPassInput.getText().toString());
        });

        signIngoogleButton.setOnClickListener(v -> {
            presenter.openGoogleSignInMap();
        });

        goToSignUp.setOnClickListener(v -> NavHostFragment.findNavController(this)
                .navigate(R.id.action_loginFragment_to_registrationFragment));
    }


    private void navigateToHome() {
        NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_homeFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (mAuth.getCurrentUser() != null) navigateToHome();
    }

    //
    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public void onLoginSuccess() {
        navigateToHome();
    }

    @Override
    public void setupGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }


    @Override
    public void openGoogleSignInMap() {
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                googleSignInLauncher.launch(signInIntent);
            });
        }
    }
    @Override
    public void onLoginError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dispose(); // Memory Leak
    }
}