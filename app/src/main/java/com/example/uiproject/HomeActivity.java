package com.example.uiproject;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import com.example.uiproject.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<PickVisualMediaRequest> photoPicker =
            registerForActivityResult(
                    new ActivityResultContracts.PickVisualMedia(),
                    uri -> {
                        if (uri != null) {
                            selectedImageUri = uri;
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        // 업로드 버튼 눌렀을 때 실행될 이미지 선택 기능
        binding.uploadButton.setOnClickListener(v ->{
            PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                    // 선택가능한 미디어 종류를 지정 ImageOnly.INSTANCE로 이미지만 선택하도록함
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build();
            photoPicker.launch(request);

        });
    }


}
