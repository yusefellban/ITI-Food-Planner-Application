package com.example.foodplanner.Data.auth.datasource;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;

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
                        FirebaseUser user = authResult.getUser();
                        if (user != null) {
                            // تحديث الـ Profile بالاسم داخل Firebase Auth
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(task -> {
                                        // بعد نجاح تحديث الاسم، سجل في Firestore
                                        saveToFirestore(user.getUid(), fullName, email, emitter, authResult);
                                    });
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }


    @Override
    public Single<FirebaseUser> getCurrentUser() {
        return Single.create(emitter -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                emitter.onSuccess(user);
            } else {
                emitter.onError(new Throwable("No user logged in"));
            }
        });
    }

    private void saveToFirestore(String uid, String name, String email, SingleEmitter<AuthResult> emitter, AuthResult result) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("fullName", name);
        userMap.put("email", email);

        FirebaseFirestore.getInstance().collection("users").document(uid)
                .set(userMap)
                .addOnSuccessListener(aVoid -> emitter.onSuccess(result))
                .addOnFailureListener(emitter::onError);
    }
}
