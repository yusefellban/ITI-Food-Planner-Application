package com.example.foodplanner.Presentation.profileScreen.view;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Data.user.Entity.UserEntity;
import com.example.foodplanner.Presentation.profileScreen.presenter.ProfilePresenterImp;
import com.example.foodplanner.R;


public class ProfileFragment extends Fragment implements ProfileView {

    private ImageView profileImage;
    private ImageView profileUploadTextClick;
    private TextView profileUserName;
    private TextView profileUserEmail;
    private ProfilePresenterImp prsenter;
    private Button logout;

    private  ActivityResultLauncher<PickVisualMediaRequest> pickMedia ;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        String imagePath = uri.toString();
                        Glide.with(this).load(imagePath).placeholder(R.drawable.ic_user_guest).into(profileImage);

                        prsenter.changeProfileImage(imagePath);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImage = view.findViewById(R.id.profileUserImage);
        profileUploadTextClick = view.findViewById(R.id.profileUploadText);
        profileUserName = view.findViewById(R.id.profileUserName);
        profileUserEmail = view.findViewById(R.id.profileUserEmail);
        logout = view.findViewById(R.id.logoutBtn);
        prsenter=new ProfilePresenterImp(getContext(),this);

        profileUploadTextClick.setOnClickListener((v) -> {
            // start aploading the dd it in data pase and Live data will show it
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        prsenter.getCurrentLocalUser();
        prsenter.trackProfileImage();


        logout.setOnClickListener((v)->{
            prsenter.performLogout();
        });


    }




    @Override
    public void displayImage(String path) {
        if (getActivity() != null) {
            Glide.with(this)
                    .load(path)
                    .placeholder(R.drawable.ic_user_guest)
                    .error(R.drawable.ic_user_guest)
                    .into(profileImage);
        }
    }


    @Override
    public void showSuccessMessage(String message) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showUserData(String name, String email, String photoUrl) {
        profileUserName.setText(name);
        profileUserEmail.setText(email);
        Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.ic_user_guest)
                .into(profileImage);
    }

    @Override
    public void onLogoutSuccess() {
        NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_loginFragment);
    }
}