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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Locale;

/**
 * Full-screen fictional system-failure simulation.
 * It never reads the device lock PIN, changes device credentials,
 * locks the device, invokes power actions, or performs a real reboot.
 */
public class MainActivity extends Activity {
    private static final int STATE_VIDEO = 0;
    private static final int STATE_FAKE_FAILURE = 1;
    private static final int STATE_CODE_GATE = 2;
    private static final int STATE_FAKE_RESTART = 3;

    private static final int MAX_ATTEMPTS = 10;
    private static final int MAX_CODE_LENGTH = 8;

    private FrameLayout root;
    private TextView codeDots;
    private TextView attemptsLine;
    private TextView responseLine;
    private StringBuilder demoCode = new StringBuilder();
    private int state = STATE_VIDEO;
    private int attemptsLeft = MAX_ATTEMPTS;
    private ValueAnimator progressAnimator;
    private VideoView activeVideo;

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

        // Start the local bundled video immediately. No screen pinning or lock-task mode.
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

        activeVideo = new VideoView(this);
        activeVideo.setBackgroundColor(Color.BLACK);
        root.removeAllViews();
        root.addView(activeVideo, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoId);
        activeVideo.setVideoURI(videoUri);
        activeVideo.setOnPreparedListener(player -> {
            player.setLooping(false);
            activeVideo.start();
        });
        activeVideo.setOnCompletionListener(player -> showFakeFailure());
        activeVideo.setOnErrorListener((player, what, extra) -> {
            showIntroFallback();
            return true;
        });
    }

    private void showIntroFallback() {
        if (isFinishing() || state != STATE_VIDEO) return;
        root.removeAllViews();
        root.setBackgroundColor(Color.rgb(2, 4, 9));

        LinearLayout intro = makeColumn(Gravity.CENTER, 28);
        root.addView(intro, matchParent());

        TextView header = makeText("BOOT SEQUENCE // 01", 12, 0xFFFF465C, true);
        header.setGravity(Gravity.CENTER);
        intro.addView(header, fullWidth());
        addGap(intro, 28);

        TextView title = makeText("SYSTEM DIAGNOSTIC", 27, Color.WHITE, true);
        title.setGravity(Gravity.CENTER);
        intro.addView(title, fullWidth());
        addGap(intro, 14);

        TextView detail = makeText("Analyzing system state...\nChecking critical services...", 14, 0xFFADB8C8, false);
        detail.setGravity(Gravity.CENTER);
        detail.setLineSpacing(dp(6), 1f);
        intro.addView(detail, fullWidth());
        addGap(intro, 30);

        ProgressBar pulse = new ProgressBar(this);
        pulse.setIndeterminate(true);
        intro.addView(pulse, new LinearLayout.LayoutParams(dp(42), dp(42)));

        fadeIn(intro, 700);
        root.postDelayed(() -> {
            if (!isFinishing() && state == STATE_VIDEO) showFakeFailure();
        }, 4200L);
    }

    private void showFakeFailure() {
        if (isFinishing() || state != STATE_VIDEO) return;
        state = STATE_FAKE_FAILURE;
        root.removeAllViews();
        root.setBackgroundColor(0xFF020307);

        LinearLayout panel = makeColumn(Gravity.CENTER_VERTICAL, 28);
        root.addView(panel, matchParent());

        TextView brand = makeText("SYSTEM // KERNEL MONITOR", 11, 0xFF9BA6B6, true);
        brand.setLetterSpacing(0.13f);
        panel.addView(brand, fullWidth());
        addGap(panel, 34);

        TextView error = makeText("SYSTEM FAILURE", 31, 0xFFFF334F, true);
        panel.addView(error, fullWidth());
        addGap(panel, 13);

        TextView explanation = makeText(
                "A critical system exception was detected.\nPreparing secure recovery environment...",
                14, 0xFFD1D7E0, false);
        explanation.setLineSpacing(dp(5), 1f);
        panel.addView(explanation, fullWidth());
        addGap(panel, 34);

        TextView status = makeText("Stopping user interface services...", 12, Color.WHITE, false);
        panel.addView(status, fullWidth());
        addGap(panel, 12);

        ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        bar.setMax(100);
        bar.setProgress(0);
        bar.setProgressTintList(android.content.res.ColorStateList.valueOf(0xFFFF334F));
        bar.setProgressBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF292D35));
        LinearLayout.LayoutParams barParams = fullWidth();
        barParams.height = dp(5);
        panel.addView(bar, barParams);
        addGap(panel, 10);

        TextView percent = makeText("0%", 12, 0xFFB7C0CE, true);
        percent.setGravity(Gravity.RIGHT);
        panel.addView(percent, fullWidth());

        fadeIn(panel, 450);
        progressAnimator = ValueAnimator.ofInt(0, 100);
        progressAnimator.setDuration(3600L);
        progressAnimator.addUpdateListener(animation -> {
            int value = (Integer) animation.getAnimatedValue();
            bar.setProgress(value);
            percent.setText(String.format(Locale.US, "%d%%", value));
            if (value < 35) status.setText("Stopping user interface services...");
            else if (value < 70) status.setText("Checking recovery partition...");
            else if (value < 96) status.setText("Preparing secure boot...");
            else status.setText("Recovery environment ready.");
        });
        progressAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                showCodeGate();
            }
        });
        progressAnimator.start();
    }

    private void showCodeGate() {
        if (isFinishing()) return;
        state = STATE_CODE_GATE;
        demoCode.setLength(0);
        root.removeAllViews();
        root.setBackgroundColor(0xFF03060C);

        LinearLayout panel = makeColumn(Gravity.CENTER_VERTICAL, 22);
        root.addView(panel, matchParent());

        TextView eyebrow = makeText("SECURE BOOT  /  RECOVERY MODE", 11, 0xFF9CA9BA, true);
        eyebrow.setLetterSpacing(0.10f);
        panel.addView(eyebrow, fullWidth());
        addGap(panel, 22);

        TextView title = makeText("DEVICE RECOVERY", 27, Color.WHITE, true);
        panel.addView(title, fullWidth());
        addGap(panel, 10);

        TextView description = makeText(
                "Verification required to restore the system session.",
                14, 0xFFC1CAD7, false);
        description.setLineSpacing(dp(4), 1f);
        panel.addView(description, fullWidth());
        addGap(panel, 26);

        attemptsLine = makeText("ATTEMPTS REMAINING: 10 / 10", 12, 0xFFFF5368, true);
        panel.addView(attemptsLine, fullWidth());
        addGap(panel, 12);

        codeDots = makeText("— — — — — —", 26, Color.WHITE, true);
        codeDots.setGravity(Gravity.CENTER);
        codeDots.setLetterSpacing(0.18f);
        codeDots.setPadding(0, dp(16), 0, dp(16));
        codeDots.setBackground(roundRect(0xFF111824, 14, 0xFF303B4B));
        panel.addView(codeDots, fullWidth());
        addGap(panel, 12);

        responseLine = makeText("ENTER RECOVERY CODE", 11, 0xFF9CA9BA, true);
        responseLine.setGravity(Gravity.CENTER);
        panel.addView(responseLine, fullWidth());
        addGap(panel, 14);

        GridLayout keypad = new GridLayout(this);
        keypad.setColumnCount(3);
        keypad.setRowCount(4);
        keypad.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        keypad.setUseDefaultMargins(false);
        LinearLayout.LayoutParams keypadParams = fullWidth();
        keypadParams.gravity = Gravity.CENTER_HORIZONTAL;
        panel.addView(keypad, keypadParams);

        String[] keys = {"1","2","3","4","5","6","7","8","9","DEL","0","ENTER"};
        for (String key : keys) {
            Button button = new Button(this);
            button.setText(key);
            button.setTextSize(15);
            button.setTextColor(Color.WHITE);
            button.setAllCaps(true);
            button.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
            button.setBackground(roundRect(0xFF172231, 12, 0xFF344154));
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0;
            lp.height = dp(54);
            lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            lp.setMargins(dp(4), dp(4), dp(4), dp(4));
            keypad.addView(button, lp);
            button.setOnClickListener(v -> {
                if ("DEL".equals(key)) deleteCodeDigit();
                else if ("ENTER".equals(key)) submitDemoCode();
                else appendCodeDigit(key);
            });
        }

        addGap(panel, 14);
        Button giveUp = new Button(this);
        giveUp.setText("GIVE UP  /  RESTART");
        giveUp.setTextColor(Color.WHITE);
        giveUp.setTextSize(12);
        giveUp.setAllCaps(true);
        giveUp.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        giveUp.setBackground(roundRect(0xFF681E2B, 12, 0xFFB33147));
        panel.addView(giveUp, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(50)));
        giveUp.setOnClickListener(v -> showFakeRestart());

        addGap(panel, 12);
        TextView safety = makeText(
                "SIMULATION CODE ONLY — NEVER ENTER YOUR REAL DEVICE PIN.",
                9, 0xFFFFC46B, true);
        safety.setGravity(Gravity.CENTER);
        safety.setLineSpacing(dp(3), 1f);
        panel.addView(safety, fullWidth());

        fadeIn(panel, 400);
    }

    private void appendCodeDigit(String digit) {
        if (state != STATE_CODE_GATE || demoCode.length() >= MAX_CODE_LENGTH) return;
        demoCode.append(digit);
        updateCodeDots();
    }

    private void deleteCodeDigit() {
        if (state != STATE_CODE_GATE || demoCode.length() == 0) return;
        demoCode.deleteCharAt(demoCode.length() - 1);
        updateCodeDots();
    }

    private void updateCodeDots() {
        if (codeDots == null) return;
        if (demoCode.length() == 0) {
            codeDots.setText("— — — — — —");
            return;
        }
        StringBuilder dots = new StringBuilder();
        for (int i = 0; i < demoCode.length(); i++) {
            if (i > 0) dots.append(' ');
            dots.append('•');
        }
        codeDots.setText(dots.toString());
    }

    private void submitDemoCode() {
        if (state != STATE_CODE_GATE) return;
        if (TextUtils.isEmpty(demoCode)) {
            responseLine.setText("ENTER A SIMULATION CODE");
            return;
        }

        // Deliberately discard the entered value. It is not stored, logged or transmitted.
        demoCode.setLength(0);
        updateCodeDots();
        attemptsLeft--;
        attemptsLine.setText("ATTEMPTS REMAINING: " + attemptsLeft + " / " + MAX_ATTEMPTS);
        responseLine.setText("ACCESS DENIED  //  RETRY REQUIRED");
        responseLine.setTextColor(0xFFFF5368);

        if (attemptsLeft <= 0) {
            root.postDelayed(this::showFakeRestart, 650L);
        }
    }

    private void showFakeRestart() {
        if (isFinishing() || state == STATE_FAKE_RESTART) return;
        state = STATE_FAKE_RESTART;
        demoCode.setLength(0);
        if (progressAnimator != null) progressAnimator.cancel();
        root.removeAllViews();
        root.setBackgroundColor(Color.BLACK);

        LinearLayout panel = makeColumn(Gravity.CENTER, 28);
        root.addView(panel, matchParent());

        TextView symbol = makeText("SYSTEM", 15, 0xFF9EABB9, true);
        symbol.setGravity(Gravity.CENTER);
        symbol.setLetterSpacing(0.25f);
        panel.addView(symbol, fullWidth());
        addGap(panel, 22);

        TextView title = makeText("RESTARTING", 29, Color.WHITE, true);
        title.setGravity(Gravity.CENTER);
        panel.addView(title, fullWidth());
        addGap(panel, 12);

        TextView status = makeText("Restoring system session...", 12, 0xFFABB6C4, false);
        status.setGravity(Gravity.CENTER);
        panel.addView(status, fullWidth());
        addGap(panel, 25);

        ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        bar.setMax(100);
        bar.setProgress(0);
        bar.setProgressTintList(android.content.res.ColorStateList.valueOf(0xFF65A7FF));
        LinearLayout.LayoutParams bp = fullWidth();
        bp.height = dp(4);
        panel.addView(bar, bp);
        addGap(panel, 12);

        TextView percent = makeText("0%", 11, 0xFF8794A5, false);
        percent.setGravity(Gravity.CENTER);
        panel.addView(percent, fullWidth());

        fadeIn(panel, 350);
        progressAnimator = ValueAnimator.ofInt(0, 100);
        progressAnimator.setDuration(3200L);
        progressAnimator.addUpdateListener(animation -> {
            int value = (Integer) animation.getAnimatedValue();
            bar.setProgress(value);
            percent.setText(value + "%");
            if (value > 78) status.setText("System session restored.");
        });
        progressAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                // End only this app. The Android device itself is never rebooted.
                finish();
            }
        });
        progressAnimator.start();
    }

    private LinearLayout makeColumn(int gravity, int horizontalPadding) {
        LinearLayout column = new LinearLayout(this);
        column.setOrientation(LinearLayout.VERTICAL);
        column.setGravity(gravity);
        column.setPadding(dp(horizontalPadding), dp(24), dp(horizontalPadding), dp(24));
        return column;
    }

    private TextView makeText(String value, int sizeSp, int color, boolean bold) {
        TextView text = new TextView(this);
        text.setText(value);
        text.setTextSize(sizeSp);
        text.setTextColor(color);
        text.setTypeface(Typeface.MONOSPACE, bold ? Typeface.BOLD : Typeface.NORMAL);
        return text;
    }

    private GradientDrawable roundRect(int color, int radiusDp, int strokeColor) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color);
        bg.setCornerRadius(dp(radiusDp));
        bg.setStroke(dp(1), strokeColor);
        return bg;
    }

    private FrameLayout.LayoutParams matchParent() {
        return new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
    }

    private LinearLayout.LayoutParams fullWidth() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void addGap(LinearLayout parent, int heightDp) {
        View gap = new View(this);
        parent.addView(gap, new LinearLayout.LayoutParams(1, dp(heightDp)));
    }

    private void fadeIn(View view, long duration) {
        AlphaAnimation animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(duration);
        view.startAnimation(animation);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        // A normal Android exit must always remain available.
        finish();
    }

    @Override
    protected void onDestroy() {
        if (progressAnimator != null) progressAnimator.cancel();
        if (activeVideo != null) {
            try {
                activeVideo.stopPlayback();
            } catch (RuntimeException ignored) { }
        }
        demoCode.setLength(0);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }
}
