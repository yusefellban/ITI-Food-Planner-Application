package com.example.foodplanner.Data.auth.datasource;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;

public class AuthRemoteDataSourceImp implements AuthRemoteDataSource {
    private final FirebaseAuth mAuth;

    public AuthRemoteDataSourceImp() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public Single<AuthResult> loginWithEmail(String email, String password) {
        return Single.create(emitter ->
                mAuth.signInWithEmailAndPassword(email.trim(), password.trim())
                        .addOnSuccessListener(emitter::onSuccess)
                        .addOnFailureListener(emitter::onError)
        );
    }

    @Override
    public Single<AuthResult> loginWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        return Single.create(emitter ->
                mAuth.signInWithCredential(credential)
                        .addOnSuccessListener(emitter::onSuccess)
                        .addOnFailureListener(emitter::onError)
        );
    }

    /// registration
    public Single<AuthResult> registerWithEmail(String fullName, String email, String password) {
        return Single.create(emitter -> {
            mAuth.createUserWithEmailAndPassword(email.trim(), password.trim())
                    .addOnSuccessListener(authResult -> {
                        String uid = authResult.getUser().getUid();
                        Map<String, Object> user = new HashMap<>();
                        user.put("fullName", fullName);
                        user.put("email", email);

                        FirebaseFirestore.getInstance().collection("users").document(uid)
                                .set(user)
                                .addOnSuccessListener(aVoid -> emitter.onSuccess(authResult))
                                .addOnFailureListener(emitter::onError);
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }

}
