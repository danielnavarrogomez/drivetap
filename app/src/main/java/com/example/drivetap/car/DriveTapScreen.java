package com.example.drivetap.car;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.car.app.CarToast;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.GridItem;
import androidx.car.app.model.GridTemplate;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.MessageTemplate;
import androidx.car.app.model.Template;
import androidx.core.graphics.drawable.IconCompat;

import com.example.drivetap.data.EndpointConfig;
import com.example.drivetap.data.EndpointRepository;
import com.example.drivetap.net.EndpointCaller;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DriveTapScreen extends Screen {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final EndpointRepository repository;
    private final CarContext carContext;

    public DriveTapScreen(@NonNull CarContext carContext) {
        super(carContext);
        this.carContext = carContext;
        repository = new EndpointRepository(carContext);
    }

    @NonNull
    @Override
    public Template onGetTemplate() {
        List<EndpointConfig> endpoints = repository.load();
        if (endpoints.isEmpty()) {
            return new MessageTemplate.Builder("Create taps in the DriveTap phone app.")
                    .setTitle("DriveTap")
                    .setHeaderAction(Action.APP_ICON)
                    .build();
        }

        ItemList.Builder list = new ItemList.Builder();
        for (EndpointConfig endpoint : endpoints) {
            list.addItem(new GridItem.Builder()
                    .setTitle(endpoint.name)
                    .setImage(buttonIcon(endpoint), GridItem.IMAGE_TYPE_LARGE)
                    .setOnClickListener(() -> callEndpoint(endpoint))
                    .build());
        }

        return new GridTemplate.Builder()
                .setTitle("DriveTap")
                .setHeaderAction(Action.APP_ICON)
                .setSingleList(list.build())
                .build();
    }

    private void callEndpoint(EndpointConfig endpoint) {
        CarToast.makeText(getCarContext(), "Calling " + endpoint.name, CarToast.LENGTH_SHORT).show();
        executor.execute(() -> {
            EndpointCaller.Result result = EndpointCaller.call(endpoint);
            String message = result.ok
                    ? endpoint.name + ": OK"
                    : endpoint.name + ": " + errorText(result);
            CarToast.makeText(getCarContext(), message, CarToast.LENGTH_LONG).show();
        });
    }

    private static String errorText(EndpointCaller.Result result) {
        if (result.statusCode > 0) {
            return "HTTP " + result.statusCode;
        }
        return "error";
    }

    private CarIcon buttonIcon(EndpointConfig endpoint) {
        return new CarIcon.Builder(IconCompat.createWithBitmap(buttonBitmap(endpoint))).build();
    }

    private static Bitmap buttonBitmap(EndpointConfig endpoint) {
        int color = parseColor(endpoint.color);
        Bitmap bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        RectF shadow = new RectF(16, 18, 112, 116);
        RectF button = new RectF(16, 14, 112, 110);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(90, 0, 0, 0));
        canvas.drawRoundRect(shadow, 24, 24, paint);

        paint.setColor(color);
        canvas.drawRoundRect(button, 24, 24, paint);

        paint.setColor(adjustColor(color, 1.18f));
        canvas.drawRoundRect(new RectF(20, 18, 108, 55), 20, 20, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(adjustColor(color, 0.78f));
        canvas.drawRoundRect(button, 24, 24, paint);

        drawGlyph(canvas, endpoint.iconId);
        return bitmap;
    }

    private static void drawGlyph(Canvas canvas, String iconId) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        switch (iconId == null ? "bolt" : iconId) {
            case "garage":
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(7);
                canvas.drawRect(38, 54, 90, 88, paint);
                canvas.drawLine(38, 64, 90, 64, paint);
                canvas.drawLine(38, 76, 90, 76, paint);
                break;
            case "door":
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(7);
                canvas.drawRect(45, 36, 83, 92, paint);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(74, 65, 4, paint);
                break;
            case "lock":
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(7);
                canvas.drawRoundRect(new RectF(42, 58, 86, 92), 8, 8, paint);
                canvas.drawArc(new RectF(48, 34, 80, 70), 200, 140, false, paint);
                break;
            case "light":
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(7);
                canvas.drawCircle(64, 54, 18, paint);
                canvas.drawLine(54, 80, 74, 80, paint);
                canvas.drawLine(57, 92, 71, 92, paint);
                break;
            case "bell":
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(7);
                Path bell = new Path();
                bell.moveTo(44, 80);
                bell.quadTo(64, 92, 84, 80);
                bell.lineTo(78, 54);
                bell.quadTo(64, 34, 50, 54);
                bell.close();
                canvas.drawPath(bell, paint);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(64, 92, 5, paint);
                break;
            case "web":
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(6);
                canvas.drawCircle(64, 64, 28, paint);
                canvas.drawLine(36, 64, 92, 64, paint);
                canvas.drawLine(64, 36, 64, 92, paint);
                canvas.drawOval(new RectF(50, 36, 78, 92), paint);
                break;
            case "home":
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(7);
                Path home = new Path();
                home.moveTo(36, 64);
                home.lineTo(64, 40);
                home.lineTo(92, 64);
                home.moveTo(44, 62);
                home.lineTo(44, 92);
                home.lineTo(82, 92);
                home.lineTo(82, 62);
                canvas.drawPath(home, paint);
                break;
            case "bolt":
            default:
                paint.setStyle(Paint.Style.FILL);
                Path bolt = new Path();
                bolt.moveTo(70, 32);
                bolt.lineTo(44, 70);
                bolt.lineTo(63, 70);
                bolt.lineTo(55, 98);
                bolt.lineTo(84, 58);
                bolt.lineTo(64, 58);
                bolt.close();
                canvas.drawPath(bolt, paint);
                break;
        }
    }

    private static int parseColor(String rawColor) {
        try {
            return Color.parseColor(rawColor);
        } catch (IllegalArgumentException ignored) {
            return Color.rgb(11, 110, 253);
        }
    }

    private static int adjustColor(int color, float factor) {
        return Color.rgb(
                clamp(Math.round(Color.red(color) * factor)),
                clamp(Math.round(Color.green(color) * factor)),
                clamp(Math.round(Color.blue(color) * factor)));
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
