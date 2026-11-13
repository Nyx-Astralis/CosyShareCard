
package com.studio868.cosysharecard;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class CosyShareCardSharer {

    private CosyShareCardSharer() {
        // Prevent instantiation.
    }

    static Intent createShareIntent(Context context, Bitmap bitmap, String shareText, String shareLink) throws IOException {

        if (context == null || bitmap == null) {
            throw new IllegalArgumentException(
                "Context and bitmap cannot be null."
            );
        }

        File imageDirectory = new File(context.getCacheDir(), "shared_images");

        if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
            throw new IOException("Could not create image directory.");
        }

        File imageFile = new File(imageDirectory, "share_card_" + System.currentTimeMillis() + ".png");

        try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {

            boolean saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            if (!saved) {
                throw new IOException("Could not save share-card image.");
            }

            outputStream.flush();
        }

        Uri imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".cosysharecard.fileprovider", imageFile);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

        StringBuilder message = new StringBuilder();

        if (shareText != null && !shareText.trim().isEmpty()) {
            message.append(shareText.trim());
        }

        if (shareLink != null && !shareLink.trim().isEmpty()) {
            if (message.length() > 0) {
                message.append("\n\n");
            }

            message.append(shareLink.trim());
        }

        if (message.length() > 0) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, message.toString());
        }

        shareIntent.setClipData(ClipData.newUri(context.getContentResolver(), "Share Card", imageUri));

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return Intent.createChooser(shareIntent, "Share image");
    }
}
