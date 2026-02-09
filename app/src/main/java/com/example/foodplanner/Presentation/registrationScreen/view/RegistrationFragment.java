package com.example.foodplanner.Presentation.registrationScreen.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplanner.Presentation.registrationScreen.presenter.RegistrationPresenter;
import com.example.foodplanner.Presentation.registrationScreen.presenter.RegistrationPresenterImp;
import com.example.foodplanner.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegistrationFragment extends Fragment implements RegistrationViewer{
  private TextView registrationToLogin;

  private Button registrationButton;
  private EditText registrationFullNameInput;
  private EditText registrationEmailInput;
  private EditText registrationPasswordInput;
  private Button googleBtn;

    private FirebaseAuth mAuth;


    private RegistrationPresenter presenter;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private GoogleSignInClient mGoogleSignInClient;

    public RegistrationFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult(ApiException.class);
                            presenter.registerWithGoogle(account.getIdToken());
                        } catch (ApiException e) { onRegistrationError("Google Failed"); }
                    }
                }
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registrationToLogin = view.findViewById(R.id.registrationToLogin);
        registrationButton = view.findViewById(R.id.registrationButton);

        registrationFullNameInput = view.findViewById(R.id.registrationFullNameInput);
        registrationEmailInput = view.findViewById(R.id.registrationEmailInput);
        registrationPasswordInput = view.findViewById(R.id.registrationPasswordInput);

        googleBtn = view.findViewById(R.id.SinUpWithGoogleButton);


        mAuth = FirebaseAuth.getInstance();


        presenter = new RegistrationPresenterImp(this);

        setupGoogleClient();

        registrationButton.setOnClickListener(v -> {
            presenter.register(
                    registrationFullNameInput.getText().toString(),
                    registrationEmailInput.getText().toString(),
                    registrationPasswordInput.getText().toString()
            );
        });

        googleBtn.setOnClickListener(v -> presenter.onGoogleBtnClicked());
        registrationToLogin.setOnClickListener((v) -> {
            NavHostFragment.findNavController(RegistrationFragment.this)
                    .navigate(R.id.action_registrationFragment_to_loginFragment);
        });


    }
    @Override
    public void openGoogleSignInMap() {
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            googleSignInLauncher.launch(mGoogleSignInClient.getSignInIntent());
        });
    }

    @Override
    public void setupGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override public void onRegistrationSuccess() {
        Toast.makeText(getContext(), "Registration Success!", Toast.LENGTH_SHORT).show();
        NavHostFragment.findNavController(this).navigate(R.id.action_registrationFragment_to_loginFragment);
    }

    @Override public void onRegistrationError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}