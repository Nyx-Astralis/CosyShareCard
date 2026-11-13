package com.studio868.cosysharecard;

import android.content.Intent;
import android.graphics.Bitmap;

import com.studio868.cosysharecard.databinding.CosyShareCardBinding;

import java.io.IOException;

public final class CosyShareCard {

    private CosyShareCard() {
        // Prevent instantiation.
    }

    public static Intent createShareIntent(CosyShareCardBuilder builder) throws IOException {

        if (builder == null) {
            throw new IllegalArgumentException(
                "Builder cannot be null."
            );
        }

        Bitmap bitmap = renderCard(builder);

        try {
            return CosyShareCardSharer.createShareIntent(
                builder.getContext(),
                bitmap,
                builder.getShareText(),
                builder.getShareLink()
            );
        } finally {
            bitmap.recycle();
        }
    }

    private static Bitmap renderCard(CosyShareCardBuilder builder) {
        CosyShareCardBinding binding = CosyShareCardRenderer.render(builder);
        return CosyShareCardImageProcessor.createBitmap(binding);
    }
}
