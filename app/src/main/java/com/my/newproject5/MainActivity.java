package com.my.newproject5;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Locale;

/**
 * A fictional game-utility themed visual simulation.
 * The surprise video starts only after the user taps INITIALIZE.
 * No device PIN, data, notification service, or power controls are accessed.
 */
public class MainActivity extends Activity {
    private static final int HOME = 0;
    private static final int LOADING = 1;
    private static final int VIDEO = 2;
    private static final int FAILURE = 3;
    private static final int ALERTS = 4;
    private static final int RECOVERY = 5;
    private static final int FAKE_RESTART = 6;

    private static final int MAX_ATTEMPTS = 10;
    private static final int MAX_CODE_LENGTH = 8;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final StringBuilder demoCode = new StringBuilder();
    private FrameLayout root;
    private VideoView activeVideo;
    private ValueAnimator animator;
    private TextView codeDots;
    private TextView attemptsLine;
    private TextView responseLine;
    private TextView alertCounter;
    private LinearLayout alertList;
    private int state = HOME;
    private int selectedProfile = 0;
    private int attemptsLeft = MAX_ATTEMPTS;
    private int alertCount = 0;
    private boolean english = false;

    private final String[][] alertMessages = {
            {"Tanda tangan modul tidak dikenal", "Unverified module signature"},
            {"Integritas runtime tidak cocok", "Runtime integrity mismatch"},
            {"Validasi peta memori gagal", "Memory map validation failed"},
            {"Respons proses tidak terduga", "Unexpected process response"},
            {"Handshake modul virtual ditolak", "Virtual module handshake rejected"},
            {"Layanan pemulihan kehabisan waktu", "Recovery service timeout"},
            {"Checksum engine tidak cocok", "Engine checksum mismatch"},
            {"Status sesi tidak tersedia", "Session state unavailable"},
            {"Modul yang dimasukkan tidak dikenali", "Injected module was not recognized"},
            {"Monitor sistem mendeteksi pengecualian", "System monitor raised an exception"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        english = getPreferences(MODE_PRIVATE).getBoolean("language_en", false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setStatusBarColor(Color.rgb(13, 15, 18));
        getWindow().setNavigationBarColor(Color.rgb(13, 15, 18));
        enterImmersiveMode();

        root = new FrameLayout(this);
        root.setBackgroundColor(0xFF101216);
        setContentView(root);
        showHome();
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

    private void showHome() {
        state = HOME;
        root.removeAllViews();
        root.setBackgroundColor(0xFF101216);

        LinearLayout page = column(0xFF101216, 20, 20, 20, 18);
        root.addView(page, matchParent());

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);
        page.addView(top, fullWidth());

        TextView brand = text("NEXUS", 22, Color.WHITE, true);
        brand.setLetterSpacing(0.08f);
        top.addView(brand, new LinearLayout.LayoutParams(0, dp(34), 1f));

        TextView build = text("UTILITY  /  4.1.0", 10, 0xFF9299A3, true);
        build.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        top.addView(build, wrap());
        Button language = button(english ? "EN" : "ID", 0xFF252A31, 0xFF414955);
        language.setTextSize(10);
        LinearLayout.LayoutParams languageParams = new LinearLayout.LayoutParams(dp(48), dp(36));
        languageParams.leftMargin = dp(10);
        top.addView(language, languageParams);
        language.setOnClickListener(v -> {
            english = !english;
            getPreferences(MODE_PRIVATE).edit().putBoolean("language_en", english).apply();
            showHome();
        });

        addGap(page, 22);

        LinearLayout hero = card(0xFF191D23, 18, 0xFF303640);
        page.addView(hero, fullWidth());
        LinearLayout heroInner = column(0x00000000, 18, 18, 18, 18);
        hero.addView(heroInner, fullWidth());

        TextView tag = text("MOBILE GAME UTILITY", 10, 0xFFE26A62, true);
        tag.setLetterSpacing(0.14f);
        heroInner.addView(tag, fullWidth());
        addGap(heroInner, 10);

        TextView title = text(tr("Mesin Performa", "Performance Engine"), 25, Color.WHITE, true);
        heroInner.addView(title, fullWidth());
        addGap(heroInner, 7);

        TextView desc = text(
                tr("Pilih profil lokal lalu mulai mesin utilitas.", "Choose a local profile and initialize the utility engine."),
                13, 0xFFADB4BE, false);
        desc.setLineSpacing(dp(4), 1f);
        heroInner.addView(desc, fullWidth());
        addGap(heroInner, 18);

        LinearLayout statusRow = new LinearLayout(this);
        statusRow.setOrientation(LinearLayout.HORIZONTAL);
        statusRow.setGravity(Gravity.CENTER_VERTICAL);
        statusRow.setPadding(dp(11), dp(10), dp(11), dp(10));
        statusRow.setBackground(roundRect(0xFF11151A, 10, 0xFF303740));
        heroInner.addView(statusRow, fullWidth());

        View dot = new View(this);
        dot.setBackground(roundRect(0xFF66C58B, 20, 0xFF66C58B));
        statusRow.addView(dot, new LinearLayout.LayoutParams(dp(8), dp(8)));
        TextView ready = text(tr("MESIN SIAP", "ENGINE READY"), 10, 0xFFC8D0D8, true);
        LinearLayout.LayoutParams readyParams = wrap();
        readyParams.leftMargin = dp(9);
        statusRow.addView(ready, readyParams);
        View spacer = new View(this);
        statusRow.addView(spacer, new LinearLayout.LayoutParams(0, 1, 1f));
        TextView local = text(tr("LOKAL", "LOCAL"), 9, 0xFF7F8995, true);
        statusRow.addView(local, wrap());

        addGap(page, 22);

        TextView choose = text(tr("PILIH PROFIL", "SELECT PROFILE"), 10, 0xFF929AA5, true);
        choose.setLetterSpacing(0.12f);
        page.addView(choose, fullWidth());
        addGap(page, 10);

        LinearLayout profiles = new LinearLayout(this);
        profiles.setOrientation(LinearLayout.HORIZONTAL);
        page.addView(profiles, fullWidth());
        profiles.addView(profileButton("FREE FIRE", 0), new LinearLayout.LayoutParams(0, dp(58), 1f));
        View profileGap = new View(this);
        profiles.addView(profileGap, new LinearLayout.LayoutParams(dp(10), 1));
        profiles.addView(profileButton("ROBLOX", 1), new LinearLayout.LayoutParams(0, dp(58), 1f));

        addGap(page, 20);

        TextView moduleTitle = text(tr("STATUS MODUL", "MODULE STATUS"), 10, 0xFF929AA5, true);
        moduleTitle.setLetterSpacing(0.12f);
        page.addView(moduleTitle, fullWidth());
        addGap(page, 9);

        LinearLayout modulePanel = card(0xFF171B20, 13, 0xFF2C333C);
        page.addView(modulePanel, fullWidth());
        LinearLayout modules = column(0x00000000, 14, 12, 14, 12);
        modulePanel.addView(modules, fullWidth());
        addModuleRow(modules, tr("Inti runtime", "Runtime core"), tr("SIAP", "READY"));
        addModuleRow(modules, tr("Antarmuka profil", "Profile interface"), tr("SIAP", "READY"));
        addModuleRow(modules, tr("Cache lokal", "Local cache"), tr("SIAGA", "STANDBY"));

        View fill = new View(this);
        page.addView(fill, new LinearLayout.LayoutParams(1, 0, 1f));

        Button init = button(tr("MULAI MESIN", "INITIALIZE ENGINE"), 0xFFB8423D, 0xFFE96C61);
        page.addView(init, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(54)));
        init.setOnClickListener(v -> startEngine());

        TextView download = text(tr("UNDUH VERSI TERBARU", "DOWNLOAD LATEST VERSION"), 9, 0xFFB8C2CE, true);
        download.setGravity(Gravity.CENTER);
        download.setPadding(0, dp(10), 0, dp(2));
        download.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://github.com/sky-pixel-Sp/SystemPrankSafe/releases/latest"))));
        page.addView(download, fullWidth());

        TextView footer = text(tr("DEMO VISUAL LOKAL  •  KEMBALI UNTUK KELUAR", "LOCAL VISUAL DEMO  •  BACK TO EXIT"), 9, 0xFF69727D, true);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(12), 0, dp(2));
        page.addView(footer, fullWidth());

        fadeIn(page, 350);
    }

    private View profileButton(String label, int profile) {
        boolean selected = selectedProfile == profile;
        Button button = button(label, selected ? 0xFF343941 : 0xFF191D23,
                selected ? 0xFFE16A61 : 0xFF333B45);
        button.setTextColor(selected ? Color.WHITE : 0xFFADB5BF);
        button.setOnClickListener(v -> {
            selectedProfile = profile;
            showHome();
        });
        return button;
    }

    private void addModuleRow(LinearLayout parent, String name, String stateText) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        parent.addView(row, fullWidth());
        TextView left = text(name, 12, 0xFFC3CAD2, false);
        row.addView(left, new LinearLayout.LayoutParams(0, dp(27), 1f));
        TextView right = text(stateText, 9, 0xFF7DC394, true);
        row.addView(right, wrap());
    }

    private void startEngine() {
        if (state != HOME) return;
        state = LOADING;
        root.removeAllViews();
        root.setBackgroundColor(0xFF101216);

        LinearLayout page = column(0xFF101216, 26, 26, 26, 26);
        page.setGravity(Gravity.CENTER);
        root.addView(page, matchParent());

        TextView brand = text("NEXUS // ENGINE", 12, 0xFFE16A61, true);
        brand.setGravity(Gravity.CENTER);
        brand.setLetterSpacing(0.16f);
        page.addView(brand, fullWidth());
        addGap(page, 24);

        TextView title = text(tr("Menginisialisasi modul", "Initializing modules"), 24, Color.WHITE, true);
        title.setGravity(Gravity.CENTER);
        page.addView(title, fullWidth());
        addGap(page, 12);

        TextView status = text(tr("Memuat komponen runtime...", "Loading runtime components..."), 12, 0xFFABB4BF, false);
        status.setGravity(Gravity.CENTER);
        page.addView(status, fullWidth());
        addGap(page, 22);

        ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        bar.setMax(100);
        bar.setProgress(0);
        bar.setProgressTintList(android.content.res.ColorStateList.valueOf(0xFFE05A50));
        bar.setProgressBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF30353D));
        LinearLayout.LayoutParams bp = fullWidth();
        bp.height = dp(5);
        page.addView(bar, bp);
        addGap(page, 10);

        TextView percent = text("0%", 11, 0xFF87919C, true);
        percent.setGravity(Gravity.CENTER);
        page.addView(percent, fullWidth());

        String[] stages = english ? new String[] {
                "Loading runtime components...",
                "Preparing selected profile...",
                "Resolving local modules...",
                "Starting engine session..."
        } : new String[] {
                "Memuat komponen runtime...",
                "Menyiapkan profil terpilih...",
                "Memeriksa modul lokal...",
                "Memulai sesi engine..."
        };
        animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(1900L);
        animator.addUpdateListener(a -> {
            int value = (Integer) a.getAnimatedValue();
            bar.setProgress(value);
            percent.setText(value + "%");
            int idx = Math.min(stages.length - 1, value / 26);
            status.setText(stages[idx]);
        });
        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                if (state == LOADING) playSurpriseVideo();
            }
        });
        fadeIn(page, 300);
        animator.start();
    }

    private void playSurpriseVideo() {
        if (isFinishing() || state != LOADING) return;
        state = VIDEO;
        root.removeAllViews();
        root.setBackgroundColor(Color.BLACK);

        int videoId = getResources().getIdentifier("prank_video", "raw", getPackageName());
        if (videoId == 0) {
            showFailure();
            return;
        }

        activeVideo = new VideoView(this);
        activeVideo.setBackgroundColor(Color.BLACK);
        root.addView(activeVideo, matchParent());
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + videoId);
        activeVideo.setVideoURI(uri);
        activeVideo.setOnPreparedListener(player -> {
            player.setLooping(false);
            activeVideo.start();
        });
        activeVideo.setOnCompletionListener(player -> showFailure());
        activeVideo.setOnErrorListener((player, what, extra) -> {
            showFailure();
            return true;
        });
    }

    private void showFailure() {
        if (isFinishing() || (state != VIDEO && state != LOADING)) return;
        state = FAILURE;
        if (activeVideo != null) {
            try { activeVideo.stopPlayback(); } catch (RuntimeException ignored) { }
            activeVideo = null;
        }
        root.removeAllViews();
        root.setBackgroundColor(0xFF08090C);

        LinearLayout page = column(0x00000000, 25, 28, 25, 28);
        root.addView(page, matchParent());

        TextView small = text(tr("NEXUS / MONITOR RUNTIME", "NEXUS / RUNTIME MONITOR"), 10, 0xFF929AA4, true);
        small.setLetterSpacing(0.12f);
        page.addView(small, fullWidth());
        addGap(page, 34);

        TextView headline = text(tr("KEGAGALAN SISTEM", "SYSTEM FAILURE"), 30, 0xFFE34D4D, true);
        page.addView(headline, fullWidth());
        addGap(page, 12);

        TextView detail = text(
                tr("Kondisi runtime tidak terduga. Menyiapkan layanan pemulihan.", "Unexpected runtime state. Recovery services are being initialized."),
                13, 0xFFB9C1CB, false);
        detail.setLineSpacing(dp(4), 1f);
        page.addView(detail, fullWidth());
        addGap(page, 32);

        TextView status = text(tr("Memvalidasi integritas runtime...", "Validating runtime integrity..."), 11, Color.WHITE, false);
        page.addView(status, fullWidth());
        addGap(page, 12);

        ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        bar.setMax(100);
        bar.setProgressTintList(android.content.res.ColorStateList.valueOf(0xFFE34D4D));
        bar.setProgressBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF292D34));
        LinearLayout.LayoutParams bp = fullWidth();
        bp.height = dp(5);
        page.addView(bar, bp);
        addGap(page, 10);

        TextView percent = text("0%", 10, 0xFF8C96A3, true);
        percent.setGravity(Gravity.RIGHT);
        page.addView(percent, fullWidth());

        animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(2600L);
        animator.addUpdateListener(a -> {
            int value = (Integer) a.getAnimatedValue();
            bar.setProgress(value);
            percent.setText(value + "%");
            if (value < 35) status.setText(tr("Memvalidasi integritas runtime...", "Validating runtime integrity..."));
            else if (value < 70) status.setText(tr("Respons modul tidak terduga...", "Unexpected module response..."));
            else if (value < 96) status.setText(tr("Layanan pemulihan tidak tersedia...", "Recovery service unavailable..."));
            else status.setText(tr("Beralih ke antarmuka pemulihan...", "Escalating to recovery interface..."));
        });
        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                showAlertStorm();
            }
        });
        fadeIn(page, 350);
        animator.start();
    }

    private void showAlertStorm() {
        if (isFinishing()) return;
        state = ALERTS;
        alertCount = 0;
        root.removeAllViews();
        root.setBackgroundColor(0xFF090A0D);

        LinearLayout page = column(0x00000000, 18, 24, 18, 20);
        root.addView(page, matchParent());

        TextView header = text(tr("PERINGATAN RUNTIME", "RUNTIME ALERTS"), 11, 0xFFE36A60, true);
        header.setLetterSpacing(0.14f);
        page.addView(header, fullWidth());
        addGap(page, 12);

        TextView title = text(tr("Beberapa pengecualian terdeteksi", "Multiple exceptions detected"), 21, Color.WHITE, true);
        page.addView(title, fullWidth());
        addGap(page, 7);

        alertCounter = text(tr("ANTREAN EVENT  00 / 50", "EVENT QUEUE  00 / 50"), 10, 0xFF9BA5B1, true);
        page.addView(alertCounter, fullWidth());
        addGap(page, 16);

        ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        bar.setMax(50);
        bar.setProgressTintList(android.content.res.ColorStateList.valueOf(0xFFE05B52));
        bar.setProgressBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF292D34));
        LinearLayout.LayoutParams bp = fullWidth();
        bp.height = dp(4);
        page.addView(bar, bp);
        addGap(page, 15);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);
        page.addView(scroll, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
        alertList = column(0x00000000, 0, 0, 0, 0);
        scroll.addView(alertList, new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));

        TextView note = text(tr("Memproses event visual lokal...", "Processing local visual events..."), 9, 0xFF737D88, false);
        note.setGravity(Gravity.CENTER);
        page.addView(note, fullWidth());

        Runnable[] tick = new Runnable[1];
        tick[0] = () -> {
            if (state != ALERTS || isFinishing()) return;
            alertCount++;
            bar.setProgress(alertCount);
            alertCounter.setText(String.format(Locale.US, english ? "EVENT QUEUE  %02d / 50" : "ANTREAN EVENT  %02d / 50", alertCount));
            String msg = alertMessages[(alertCount - 1) % alertMessages.length][english ? 1 : 0];
            addAlertCard(alertList, alertCount, msg);
            if (alertList.getChildCount() > 5) alertList.removeViewAt(0);
            scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
            if (alertCount >= 50) {
                handler.postDelayed(this::showRecovery, 500L);
            } else {
                handler.postDelayed(tick[0], 125L);
            }
        };
        handler.postDelayed(tick[0], 180L);
    }

    private void addAlertCard(LinearLayout list, int index, String message) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(12), dp(10), dp(12), dp(10));
        card.setBackground(roundRect(0xFF181C22, 9, 0xFF38313A));
        LinearLayout.LayoutParams cp = fullWidth();
        cp.bottomMargin = dp(7);
        list.addView(card, cp);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        card.addView(row, fullWidth());

        TextView number = text(String.format(Locale.US, "#%02d", index), 9, 0xFFE16A61, true);
        row.addView(number, wrap());
        TextView stateText = text(tr("  TERDETEKSI", "  DETECTED"), 9, 0xFF9AA4AF, true);
        row.addView(stateText, wrap());

        TextView detail = text(message, 11, 0xFFD0D5DC, false);
        LinearLayout.LayoutParams dp = fullWidth();
        dp.topMargin = dp(5);
        card.addView(detail, dp);
    }

    private void showRecovery() {
        if (isFinishing()) return;
        state = RECOVERY;
        demoCode.setLength(0);
        attemptsLeft = MAX_ATTEMPTS;
        root.removeAllViews();
        root.setBackgroundColor(0xFF080B10);

        LinearLayout page = column(0x00000000, 22, 24, 22, 20);
        root.addView(page, matchParent());

        TextView eyebrow = text(tr("LINGKUNGAN PEMULIHAN", "RECOVERY ENVIRONMENT"), 10, 0xFFE16A61, true);
        eyebrow.setLetterSpacing(0.12f);
        page.addView(eyebrow, fullWidth());
        addGap(page, 20);

        TextView title = text(tr("VERIFIKASI SESI", "SESSION VERIFICATION"), 23, Color.WHITE, true);
        page.addView(title, fullWidth());
        addGap(page, 9);

        TextView description = text(
                tr("Kode simulasi diperlukan untuk melanjutkan sesi ini.", "A demo code is required to continue this simulated session."),
                12, 0xFFB8C1CB, false);
        description.setLineSpacing(dp(4), 1f);
        page.addView(description, fullWidth());
        addGap(page, 22);

        attemptsLine = text(tr("SISA PERCOBAAN  10 / 10", "ATTEMPTS REMAINING  10 / 10"), 10, 0xFFE56B60, true);
        page.addView(attemptsLine, fullWidth());
        addGap(page, 11);

        codeDots = text("— — — — — —", 25, Color.WHITE, true);
        codeDots.setGravity(Gravity.CENTER);
        codeDots.setPadding(0, dp(17), 0, dp(17));
        codeDots.setBackground(roundRect(0xFF171C24, 12, 0xFF353D48));
        page.addView(codeDots, fullWidth());
        addGap(page, 11);

        responseLine = text(tr("MASUKKAN KODE DEMO", "ENTER DEMO CODE"), 10, 0xFF929DAA, true);
        responseLine.setGravity(Gravity.CENTER);
        page.addView(responseLine, fullWidth());
        addGap(page, 10);

        GridLayout keypad = new GridLayout(this);
        keypad.setColumnCount(3);
        keypad.setRowCount(4);
        LinearLayout.LayoutParams kp = fullWidth();
        page.addView(keypad, kp);
        String[] keys = {"1","2","3","4","5","6","7","8","9",tr("HAPUS", "DEL"),"0","ENTER"};
        for (String key : keys) {
            Button b = button(key, 0xFF171D25, 0xFF39434F);
            b.setTextColor(Color.WHITE);
            b.setTextSize(14);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0;
            lp.height = dp(47);
            lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            lp.setMargins(dp(3), dp(3), dp(3), dp(3));
            keypad.addView(b, lp);
            b.setOnClickListener(v -> {
                if (tr("HAPUS", "DEL").equals(key)) deleteDigit();
                else if ("ENTER".equals(key)) submitDemoCode();
                else appendDigit(key);
            });
        }

        addGap(page, 10);
        Button giveUp = button(tr("LANJUT KE RESTART", "CONTINUE TO RESTART"), 0xFF873B37, 0xFFE06A60);
        page.addView(giveUp, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(48)));
        giveUp.setOnClickListener(v -> showFakeRestart());

        TextView safety = text(tr("INPUT DEMO SAJA  •  JANGAN MASUKKAN PIN HP", "DEMO INPUT ONLY  •  NEVER ENTER YOUR PHONE PIN"), 8, 0xFFE5B777, true);
        safety.setGravity(Gravity.CENTER);
        safety.setPadding(0, dp(10), 0, 0);
        page.addView(safety, fullWidth());
        fadeIn(page, 350);
    }

    private void appendDigit(String digit) {
        if (state != RECOVERY || demoCode.length() >= MAX_CODE_LENGTH) return;
        demoCode.append(digit);
        updateDots();
    }

    private void deleteDigit() {
        if (state != RECOVERY || demoCode.length() == 0) return;
        demoCode.deleteCharAt(demoCode.length() - 1);
        updateDots();
    }

    private void updateDots() {
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
        if (state != RECOVERY) return;
        if (TextUtils.isEmpty(demoCode)) {
            responseLine.setText(tr("MASUKKAN KODE DEMO", "ENTER A DEMO CODE"));
            return;
        }

        // Discard the demo input immediately; never store, log, or transmit it.
        demoCode.setLength(0);
        updateDots();
        attemptsLeft--;
        attemptsLine.setText(tr("SISA PERCOBAAN  ", "ATTEMPTS REMAINING  ") + attemptsLeft + " / " + MAX_ATTEMPTS);
        responseLine.setText(tr("VERIFIKASI GAGAL", "VERIFICATION FAILED"));
        responseLine.setTextColor(0xFFE56B60);
        if (attemptsLeft <= 0) handler.postDelayed(this::showFakeRestart, 650L);
    }

    private void showFakeRestart() {
        if (isFinishing() || state == FAKE_RESTART) return;
        state = FAKE_RESTART;
        demoCode.setLength(0);
        if (animator != null) animator.cancel();
        handler.removeCallbacksAndMessages(null);
        root.removeAllViews();
        root.setBackgroundColor(Color.BLACK);

        LinearLayout page = column(0x00000000, 24, 24, 24, 24);
        page.setGravity(Gravity.CENTER);
        root.addView(page, matchParent());

        TextView logo = text("NEXUS", 14, 0xFF9DA8B5, true);
        logo.setGravity(Gravity.CENTER);
        logo.setLetterSpacing(0.24f);
        page.addView(logo, fullWidth());
        addGap(page, 22);

        TextView title = text(tr("MEMULAI ULANG", "RESTARTING"), 26, Color.WHITE, true);
        title.setGravity(Gravity.CENTER);
        page.addView(title, fullWidth());
        addGap(page, 10);

        TextView status = text(tr("Memulihkan sesi lokal...", "Restoring local session..."), 11, 0xFF9AA5B2, false);
        status.setGravity(Gravity.CENTER);
        page.addView(status, fullWidth());
        addGap(page, 23);

        ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        bar.setMax(100);
        bar.setProgressTintList(android.content.res.ColorStateList.valueOf(0xFF7796C8));
        LinearLayout.LayoutParams bp = fullWidth();
        bp.height = dp(4);
        page.addView(bar, bp);
        addGap(page, 10);

        TextView percent = text("0%", 10, 0xFF7E8996, false);
        percent.setGravity(Gravity.CENTER);
        page.addView(percent, fullWidth());

        animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(3200L);
        animator.addUpdateListener(a -> {
            int value = (Integer) a.getAnimatedValue();
            bar.setProgress(value);
            percent.setText(value + "%");
            if (value > 82) status.setText(tr("Sesi siap.", "Session ready."));
        });
        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                // Only this Activity closes. The phone is never rebooted.
                finish();
            }
        });
        fadeIn(page, 300);
        animator.start();
    }

    private String tr(String indonesia, String englishText) {
        return english ? englishText : indonesia;
    }

    private LinearLayout column(int bg, int left, int top, int right, int bottom) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(left), dp(top), dp(right), dp(bottom));
        if (bg != 0) layout.setBackgroundColor(bg);
        return layout;
    }

    private LinearLayout card(int bg, int radius, int stroke) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackground(roundRect(bg, radius, stroke));
        return layout;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setTypeface(Typeface.create(bold ? "sans-serif-medium" : "sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        return t;
    }

    private Button button(String label, int bgColor, int strokeColor) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(11);
        b.setAllCaps(true);
        b.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
        b.setTextColor(Color.WHITE);
        b.setBackground(roundRect(bgColor, 11, strokeColor));
        return b;
    }

    private GradientDrawable roundRect(int color, int radiusDp, int strokeColor) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radiusDp));
        d.setStroke(dp(1), strokeColor);
        return d;
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

    private LinearLayout.LayoutParams wrap() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void addGap(LinearLayout parent, int heightDp) {
        View v = new View(this);
        parent.addView(v, new LinearLayout.LayoutParams(1, dp(heightDp)));
    }

    private void fadeIn(View view, long duration) {
        AlphaAnimation a = new AlphaAnimation(0f, 1f);
        a.setDuration(duration);
        view.startAnimation(a);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        // Android Back remains available as an emergency exit.
        finish();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        if (animator != null) animator.cancel();
        if (activeVideo != null) {
            try { activeVideo.stopPlayback(); } catch (RuntimeException ignored) { }
        }
        demoCode.setLength(0);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }
}
