package devandroid.glaucomv.imagemflutuante;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class FloatingImageService extends Service {

    private WindowManager windowManager;
    private ImageView floatingImage;
    private float offsetX, offsetY;
    private int originalX, originalY;

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        floatingImage = new ImageView(this);
        floatingImage.setImageResource(R.drawable.retangulo);
        floatingImage.setBackgroundColor(Color.WHITE);
        floatingImage.setVisibility(View.VISIBLE);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                270,  // Largura da imagem
                60,   // Altura da imagem
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
        );
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        //layoutParams.gravity = Gravity.TOP | Gravity.START;
        //layoutParams.x = 210;  // Posição X da margem esquerda
        layoutParams.y = 115;  // Posição Y do topo

        floatingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica de clique aqui
            }
        });

        floatingImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        offsetX = event.getRawX();
                        offsetY = event.getRawY();
                        originalX = layoutParams.x;
                        originalY = layoutParams.y;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        layoutParams.x = (int) (originalX + (event.getRawX() - offsetX));
                        layoutParams.y = (int) (originalY + (event.getRawY() - offsetY));
                        windowManager.updateViewLayout(floatingImage, layoutParams);
                        return true;
                }
                return false;
            }
        });

        windowManager.addView(floatingImage, layoutParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingImage != null && windowManager != null) {
            windowManager.removeView(floatingImage);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
