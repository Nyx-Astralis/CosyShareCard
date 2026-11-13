package com.studio868.cosysharecard;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.studio868.cosysharecard.databinding.CosyShareCardBinding;



public final class CosyShareCardRenderer {

    private static final float CARD_HEIGHT_RATIO = 0.645f;

    private CosyShareCardRenderer() {
        // Prevent instantiation.
    }

    static CosyShareCardBinding render(CosyShareCardBuilder builder) {

        Context themedContext = new ContextThemeWrapper(builder.getContext(), R.style.CosyShareCardRenderTheme);//Or  R.style.CosyShareCardRenderTheme
        CosyShareCardBinding binding = CosyShareCardBinding.inflate(LayoutInflater.from(themedContext));

        FrameLayout.LayoutParams cardParams = (FrameLayout.LayoutParams) binding.cardShareContent.getLayoutParams();
        cardParams.height = Math.round(builder.getContext().getResources().getDisplayMetrics().widthPixels * (16f / 9f) * CARD_HEIGHT_RATIO);

        binding.cardShareContent.setLayoutParams(cardParams);

        applyAppIcon(binding, builder);
        applyText(binding, builder);
        applyTheme(binding, builder);
        CosyShareCardImageProcessor.applyBackgroundBlur(binding, builder);

        return binding;
    }

    private static void applyText(CosyShareCardBinding binding, CosyShareCardBuilder builder) {
        binding.tvShareHeader.setText(builder.getHeader() != null ? builder.getHeader() : "");
        binding.tvShareTitle.setText(builder.getTitle() != null ? builder.getTitle() : "");
        binding.tvShareDescription.setText(builder.getDescription() != null ? builder.getDescription() : "");
        binding.tvShareSideText.setText(builder.getSideText() != null ? builder.getSideText() : "");
        binding.tvShareAuthor.setText(builder.getAuthor() != null ? builder.getAuthor() : "");
        binding.tvShareBrand.setText(builder.getBrand() != null ? builder.getBrand() : "");
    }

    private static void applyTheme(CosyShareCardBinding binding, CosyShareCardBuilder builder) {

        int nightMode = builder.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDark;

        switch (builder.getTheme()) {
            case LIGHT:
                isDark = false;
                break;

            case DARK:
                isDark = true;
                break;

            case AUTO:
            default:
                isDark = nightMode == Configuration.UI_MODE_NIGHT_YES;
                break;
        }

        int primaryColor = builder.getContext().getColor(isDark ? R.color.share_card_text_primary_dark : R.color.share_card_text_primary_light);
        int secondaryColor = builder.getContext().getColor(isDark ? R.color.share_card_text_secondary_dark : R.color.share_card_text_secondary_light);
        int backgroundColor = builder.getContext().getColor(isDark ? R.color.share_card_background_dark : R.color.share_card_background_light);
        int dividerColor = builder.getContext().getColor(isDark ? R.color.share_card_divider_dark : R.color.share_card_divider_light);

        binding.cardShareContent.setCardBackgroundColor(backgroundColor);
        binding.divider1.setBackgroundColor(dividerColor);
        binding.divider2.setBackgroundColor(dividerColor);
        binding.divider3.setBackgroundColor(dividerColor);
        binding.tvShareHeader.setTextColor(primaryColor);
        binding.tvShareTitle.setTextColor(primaryColor);
        binding.tvShareDescription.setTextColor(secondaryColor);
        binding.tvShareSideText.setTextColor(secondaryColor);
        binding.tvShareAuthor.setTextColor(secondaryColor);
        binding.tvShareBrand.setTextColor(secondaryColor);

    }

    private static void applyAppIcon(CosyShareCardBinding binding, CosyShareCardBuilder builder) {
        try {
            binding.ivShareAppIcon.setImageDrawable(builder.getAppContext().getPackageManager().getApplicationIcon(builder.getAppContext().getPackageName()));
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException(
                "Unable to load the application's launcher icon.",
                e
            );
        }
    }
}
