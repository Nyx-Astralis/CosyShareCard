
package com.studio868.cosysharecard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

import com.studio868.cosysharecard.databinding.CosyShareCardBinding;

public final class CosyShareCardImageProcessor {

    private CosyShareCardImageProcessor() {
        // Prevent instantiation.
    }

    static Bitmap createBitmap(CosyShareCardBinding binding) {

        View view = binding.getRoot();

        int width = view.getResources().getDisplayMetrics().widthPixels;
        int height = Math.round(width * 16f / 9f);
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        view.layout(0, 0, width, height);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


    static Bitmap blur(Context context, Bitmap source, float radiusDp) {

        if (context == null || source == null) {
            throw new IllegalArgumentException("Context and bitmap cannot be null.");
        }

        if (radiusDp < 0) {
            throw new IllegalArgumentException("Blur radius cannot be negative.");
        }

        if (radiusDp == 0) {
            return source;
        }

        final int downsample = 4;

        int width = Math.max(1, source.getWidth() / downsample);
        int height = Math.max(1, source.getHeight() / downsample);

        Bitmap smallBitmap = Bitmap.createScaledBitmap(source, width, height, true);

        int[] pixels = new int[width * height];

        smallBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        float density = context.getResources().getDisplayMetrics().density;
        int radius = Math.max(1, Math.round(radiusDp * density / downsample));

        for (int i = 0; i < 3; i++) {
            blurHorizontal(pixels, width, height, radius);
            blurVertical(pixels, width, height, radius);
        }

        Bitmap blurredBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        blurredBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        Bitmap result = Bitmap.createScaledBitmap(blurredBitmap, source.getWidth(), source.getHeight(), true);

        if (smallBitmap != source) {
            smallBitmap.recycle();
        }

        blurredBitmap.recycle();
        return result;
    }

    private static void blurHorizontal(int[] pixels, int width, int height, int radius) {
        int[] result = new int[pixels.length];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                long a = 0;
                long r = 0;
                long g = 0;
                long b = 0;
                int count = 0;

                for (int dx = -radius; dx <= radius; dx++) {
                    int sampleX = Math.max(0, Math.min(width - 1, x + dx)
                    );

                    int color = pixels[y * width + sampleX];

                    a += (color >>> 24) & 0xFF;
                    r += (color >>> 16) & 0xFF;
                    g += (color >>> 8) & 0xFF;
                    b += color & 0xFF;

                    count++;
                }

                result[y * width + x] = ((int) (a / count) << 24) | ((int) (r / count) << 16) | ((int) (g / count) << 8) | (int) (b / count);
            }
        }

        System.arraycopy(result, 0, pixels, 0, pixels.length);
    }

    private static void blurVertical(int[] pixels, int width, int height, int radius) {
        int[] result = new int[pixels.length];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                long a = 0;
                long r = 0;
                long g = 0;
                long b = 0;
                int count = 0;

                for (int dy = -radius; dy <= radius; dy++) {
                    int sampleY = Math.max(0, Math.min(height - 1, y + dy)
                    );

                    int color = pixels[sampleY * width + x];

                    a += (color >>> 24) & 0xFF;
                    r += (color >>> 16) & 0xFF;
                    g += (color >>> 8) & 0xFF;
                    b += color & 0xFF;

                    count++;
                }

                result[y * width + x] = ((int) (a / count) << 24) | ((int) (r / count) << 16) | ((int) (g / count) << 8) | (int) (b / count);
            }
        }

        System.arraycopy(result, 0, pixels, 0, pixels.length);
    }


    static void applyBackgroundBlur(CosyShareCardBinding binding, CosyShareCardBuilder builder) {
        ImageView backgroundView = binding.ivShareBackground;
        Drawable drawable = backgroundView.getDrawable();

        if (drawable == null || builder.getBlurRadius() == 0) {
            return;
        }

        Bitmap source;

        if (drawable instanceof BitmapDrawable && ((BitmapDrawable) drawable).getBitmap() != null) {
            source = ((BitmapDrawable) drawable).getBitmap();
        } else {

            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();

            if (width <= 0 || height <= 0) {
                return;
            }

            source = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(source);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
        }

        Bitmap blurred = blur(builder.getContext(), source, builder.getBlurRadius());
        backgroundView.setImageBitmap(blurred);
    }


}
