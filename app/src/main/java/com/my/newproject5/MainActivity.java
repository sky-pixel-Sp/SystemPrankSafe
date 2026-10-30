package com.my.newproject5;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Locale;

/**
 * Harmless fullscreen prank.
 * No device-admin, accessibility, root, overlay or power permissions.
 * The app never powers off or locks the real device.
 */
public class MainActivity extends Activity {
    private static final int STATE_VIDEO = 0;
    private static final int STATE_SHUTDOWN_FAKE = 1;
    private static final int STATE_BLACKOUT = 2;
    private static final int STATE_REVEAL = 3;

    private FrameLayout root;
    private TextView statusLine;
    private TextView percentLine;
    private ProgressBar progressBar;
    private int state = STATE_VIDEO;
    private ValueAnimator shutdownAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().setNavigationBarColor(Color.BLACK);
        enterImmersiveMode();

        root = new FrameLayout(this);
        root.setBackgroundColor(Color.BLACK);
        setContentView(root);
        playUserVideoOrFallback();
    }

    private void enterImmersiveMode() {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
    }

    private void playUserVideoOrFallback() {
        state = STATE_VIDEO;
        int videoId = getResources().getIdentifier("prank_video", "raw", getPackageName());
        if (videoId == 0) {
            showIntroFallback();
            return;
        }

        VideoView videoView = new VideoView(this);
        videoView.setBackgroundColor(Color.BLACK);
        root.removeAllViews();
        root.addView(videoView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoId);
        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(mediaPlayer -> {
            mediaPlayer.setLooping(false);
            videoView.start();
        });
        videoView.setOnCompletionListener(mediaPlayer -> showFakeShutdown());
        videoView.setOnErrorListener((mediaPlayer, what, extra) -> {
            showIntroFallback();
            return true;
        });
    }

    private void showIntroFallback() {
        if (isFinishing() || state != STATE_VIDEO) return;
        root.removeAllViews();
        root.setBackgroundColor(Color.rgb(3, 5, 9));

        LinearLayout intro = new LinearLayout(this);
        intro.setOrientation(LinearLayout.VERTICAL);
        intro.setGravity(Gravity.CENTER);
        intro.setPadding(dp(28), dp(28), dp(28), dp(28));
        root.addView(intro, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        TextView mark = makeText("SYSTEM // ALERT", 13, Color.rgb(255, 60, 80), true);
        mark.setGravity(Gravity.CENTER);
        mark.setLetterSpacing(0.18f);
        intro.addView(mark, fullWidth());
        addGap(intro, 26);

        TextView title = makeText("SYSTEM DIAGNOSTIC", 25, Color.WHITE, true);
        title.setGravity(Gravity.CENTER);
        intro.addView(title, fullWidth());
        addGap(intro, 14);

        TextView detail = makeText(
                "Memindai antarmuka perangkat...\nMenyiapkan simulasi visual.",
                14, Color.rgb(180, 190, 205), false);
        detail.setGravity(Gravity.CENTER);
        detail.setLineSpacing(dp(6), 1f);
        intro.addView(detail, fullWidth());
        addGap(intro, 28);

        ProgressBar pulse = new ProgressBar(this);
        pulse.setIndeterminate(true);
        intro.addView(pulse, new LinearLayout.LayoutParams(dp(42), dp(42)));

        AlphaAnimation fade = new AlphaAnimation(0f, 1f);
        fade.setDuration(700);
        intro.startAnimation(fade);
        root.postDelayed(() -> {
            if (!isFinishing() && state == STATE_VIDEO) showFakeShutdown();
        }, 4200L);
    }

    private void showFakeShutdown() {
        if (isFinishing() || state != STATE_VIDEO) return;
        state = STATE_SHUTDOWN_FAKE;
        root.removeAllViews();
        root.setBackgroundColor(Color.rgb(2, 3, 5));

        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setGravity(Gravity.CENTER_VERTICAL);
        panel.setPadding(dp(28), dp(30), dp(28), dp(30));
        root.addView(panel, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        TextView brand = makeText("ANDROID // SYSTEM DIAGNOSTIC", 12, Color.LTGRAY, true);
        brand.setLetterSpacing(0.12f);
        panel.addView(brand, fullWidth());
        addGap(panel, 34);

        TextView error = makeText("CRITICAL SYSTEM ERROR", 25, Color.rgb(255, 55, 74), true);
        panel.addView(error, fullWidth());
        addGap(panel, 12);

        TextView explanation = makeText(
                "A fatal exception has been detected.\nPreparing simulated power sequence...",
                15, Color.rgb(205, 209, 216), false);
        explanation.setLineSpacing(dp(5), 1f);
        panel.addView(explanation, fullWidth());
        addGap(panel, 34);

        statusLine = makeText("Stopping background processes...", 13, Color.WHITE, false);
        panel.addView(statusLine, fullWidth());
        addGap(panel, 12);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        progressBar.setProgressTintList(android.content.res.ColorStateList.valueOf(Color.rgb(255, 55, 74)));
        progressBar.setProgressBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(42, 44, 50)));
        LinearLayout.LayoutParams progressParams = fullWidth();
        progressParams.height = dp(5);
        panel.addView(progressBar, progressParams);
        addGap(panel, 10);

        percentLine = makeText("0%", 12, Color.LTGRAY, true);
        percentLine.setGravity(Gravity.RIGHT);
        panel.addView(percentLine, fullWidth());
        addGap(panel, 30);

        TextView footer = makeText("SIMULASI SAJA • BACK UNTUK KELUAR", 10, Color.GRAY, true);
        footer.setLetterSpacing(0.07f);
        panel.addView(footer, fullWidth());

        AlphaAnimation fade = new AlphaAnimation(0f, 1f);
        fade.setDuration(450);
        panel.startAnimation(fade);

        shutdownAnimator = ValueAnimator.ofInt(0, 100);
        shutdownAnimator.setDuration(3300L);
        shutdownAnimator.addUpdateListener(animation -> {
            int value = (Integer) animation.getAnimatedValue();
            progressBar.setProgress(value);
            percentLine.setText(String.format(Locale.US, "%d%%", value));
            if (value < 30) {
                statusLine.setText("Stopping background processes...");
            } else if (value < 65) {
                statusLine.setText("Unmounting system interface...");
            } else if (value < 95) {
                statusLine.setText("Closing display services...");
            } else {
                statusLine.setText("Power sequence complete.");
            }
        });
        shutdownAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                showBriefBlackout();
            }
        });
        shutdownAnimator.start();
    }

    private void showBriefBlackout() {
        if (isFinishing()) return;
        state = STATE_BLACKOUT;
        root.removeAllViews();
        root.setBackgroundColor(Color.BLACK);
        root.setOnClickListener(v -> showPrankReveal());
        root.postDelayed(() -> {
            if (state == STATE_BLACKOUT && !isFinishing()) showPrankReveal();
        }, 1100L);
    }

    private void showPrankReveal() {
        if (isFinishing() || state == STATE_REVEAL) return;
        state = STATE_REVEAL;
        root.setOnClickListener(null);
        root.removeAllViews();
        root.setBackgroundColor(Color.rgb(7, 12, 10));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER);
        content.setPadding(dp(28), dp(28), dp(28), dp(28));
        root.addView(content, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        TextView emoji = makeText("😈", 48, Color.WHITE, false);
        emoji.setGravity(Gravity.CENTER);
        content.addView(emoji, fullWidth());
        addGap(content, 16);

        TextView title = makeText("KENA PRANK!", 29, Color.rgb(91, 245, 151), true);
        title.setGravity(Gravity.CENTER);
        content.addView(title, fullWidth());
        addGap(content, 16);

        TextView message = makeText(
                "Tenang, HP kamu aman.\nTidak ada shutdown sungguhan, data dihapus, atau sistem yang diubah.",
                16, Color.rgb(225, 232, 228), false);
        message.setGravity(Gravity.CENTER);
        message.setLineSpacing(dp(7), 1f);
        content.addView(message, fullWidth());
        addGap(content, 30);

        Button exit = new Button(this);
        exit.setText("TUTUP PRANK");
        exit.setTextColor(Color.BLACK);
        exit.setTextSize(14);
        exit.setAllCaps(false);
        exit.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        GradientDrawable buttonBg = new GradientDrawable();
        buttonBg.setColor(Color.rgb(91, 245, 151));
        buttonBg.setCornerRadius(dp(14));
        exit.setBackground(buttonBg);
        content.addView(exit, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(54)));
        exit.setOnClickListener(v -> finish());

        AlphaAnimation reveal = new AlphaAnimation(0f, 1f);
        reveal.setDuration(500);
        content.startAnimation(reveal);
    }

    private TextView makeText(String value, int sizeSp, int color, boolean bold) {
        TextView text = new TextView(this);
        text.setText(value);
        text.setTextSize(sizeSp);
        text.setTextColor(color);
        text.setTypeface(Typeface.MONOSPACE, bold ? Typeface.BOLD : Typeface.NORMAL);
        return text;
    }

    private LinearLayout.LayoutParams fullWidth() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    private void addGap(LinearLayout parent, int heightDp) {
        View gap = new View(this);
        parent.addView(gap, new LinearLayout.LayoutParams(1, dp(heightDp)));
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (shutdownAnimator != null) shutdownAnimator.cancel();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }
}
