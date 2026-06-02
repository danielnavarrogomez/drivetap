package com.example.drivetap.car;

import androidx.annotation.NonNull;
import androidx.car.app.Screen;
import androidx.car.app.Session;
import androidx.car.app.model.Template;

public class DriveTapSession extends Session {
    @NonNull
    @Override
    public Screen onCreateScreen(@NonNull android.content.Intent intent) {
        return new DriveTapScreen(getCarContext());
    }
}
