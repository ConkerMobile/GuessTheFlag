package com.conkermobile.guesstheflag;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.ads.*;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.plattysoft.leonids.ParticleSystem;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import static com.conkermobile.guesstheflag.App.countryCode;
import static com.conkermobile.guesstheflag.App.countryNames;
import static com.conkermobile.guesstheflag.App.flagUrl;

public class MainActivity extends Activity {
    ArrayList<String> answersPicked = new ArrayList<String>();
    String[] answers = new String[4];
    ImageView imageView;
    int chosenCar = 0;
    int locationOfCorrectAnswer = 0;
    int questionsLeft = 0;
    int points = 10;
    int correctQuestions = 0;
    int wrongQuestions;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button playAgainButton;
    TextView textView;
    TextView pointsTextView;
    TextView scoreTextView;
    TextView timerTextView;
    TextView incorrectTextView;
    TextView correctTextView;
    TextView questionsLeftTextView;
    boolean buttonSelected = false;
    private InterstitialAd mInterstitialAd;

    public void seeReview() {
        imageView.setVisibility(View.INVISIBLE);
        pointsTextView.setVisibility(View.VISIBLE);
        playAgainButton.setVisibility(View.VISIBLE);
        pointsTextView.setText(points + " points earned");
        scoreTextView.setVisibility(View.VISIBLE);
        button0.setVisibility(View.INVISIBLE);
        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.INVISIBLE);textView.setVisibility(View.INVISIBLE);
        timerTextView.setVisibility(View.INVISIBLE);
        questionsLeftTextView.setVisibility(View.INVISIBLE);
        correctTextView.setVisibility(View.INVISIBLE);
        incorrectTextView.setVisibility(View.INVISIBLE);
    }

    public void playAgain(View view) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        else {
            Log.i("admob", "ad not loaded, cannot show.");
        }
        logoDownloader imageTask = new logoDownloader();
        Bitmap carLogo = null;

        try {
            carLogo = imageTask.execute(flagUrl + countryCode.get(chosenCar).toLowerCase() + ".png").get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(carLogo);
        playAgainButton.setVisibility(View.INVISIBLE);
        pointsTextView.setVisibility(View.INVISIBLE);
        button0.setVisibility(View.VISIBLE);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        wrongQuestions = 0;
        correctQuestions = 0;
        correctTextView.setText("😀: " + correctQuestions);
        incorrectTextView.setText("😟: " + wrongQuestions);
        questionsLeft = 15;
        questionsLeftTextView.setText(questionsLeft+" ❓ left");
        textView.setVisibility(View.INVISIBLE);
        timerTextView.setVisibility(View.INVISIBLE);
        scoreTextView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        incorrectTextView.setVisibility(View.VISIBLE);
        correctTextView.setVisibility(View.VISIBLE);
        questionsLeftTextView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.INVISIBLE);
        newQuestion();
    }
    public synchronized void carChosen(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        processCarChoice(view);

    }

    public class logoDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {

                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void newQuestion() {
        mLastClickTime = 0;
        try {
            Random rand = new Random();

            chosenCar = rand.nextInt(countryNames.size());

            logoDownloader imageTask = new logoDownloader();

            Bitmap carLogo = imageTask.execute(flagUrl + countryCode.get(chosenCar).toLowerCase() + ".png").get();

            imageView.setImageBitmap(carLogo);

            locationOfCorrectAnswer = rand.nextInt(4);

            int incorrectAnswerLocation;

            for (int i = 0; i < 4; i++) {
                if (i == locationOfCorrectAnswer) {
                    answers[i] = countryNames.get(chosenCar);

                } else {
                    incorrectAnswerLocation = rand.nextInt(countryCode.size());

                    while (incorrectAnswerLocation == chosenCar) {
                        incorrectAnswerLocation = rand.nextInt(countryCode.size());
                    }

                    answers[i] = countryNames.get(incorrectAnswerLocation);
                }
            }
            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        buttonSelected = false;
    }

    int millisUntilDone = 30000;
    int countDownInterval = 1000;
    private long mLastClickTime = 0;

    public void processCarChoice(View view){
        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {
            textView.setVisibility(View.VISIBLE);
            questionsLeft = 15-(correctQuestions+wrongQuestions);
            textView.setText("Correct! WooHoo!");
            correctQuestions += 1;
            correctTextView.setText("😀: " + correctQuestions);
            new ParticleSystem(this, 50, R.mipmap.star, 1000)
                    .setSpeedRange(0.2f, 0.5f)
                    .oneShot(view, 50);
            newQuestion();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    textView.setVisibility(View.INVISIBLE);
                }
            }.start();
            if (correctQuestions+wrongQuestions >= 15) {
                countDownTimer.cancel();
                seeReview();
            }
            points += 5;
            questionsLeft -= 1;
            scoreTextView.setText(correctQuestions + " / 15");
            questionsLeftTextView.setText(questionsLeft + " ❓ left");
            points+= 5;
        } else {

            questionsLeft = 15-(correctQuestions+wrongQuestions);
            button0.setVisibility(View.INVISIBLE);
            button1.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            timerTextView.setVisibility(View.INVISIBLE);
            wrongQuestions +=1;
            incorrectTextView.setText("😟: " + wrongQuestions);
            textView.setText("Oops! It's actually " + countryNames.get(chosenCar));
            textView.setVisibility(View.VISIBLE);
            points-=2;
            new ParticleSystem(this, 50, R.mipmap.down, 1000)
                    .setSpeedRange(0.2f, 0.5f)
                    .oneShot(view, 50);
            CountDownTimer wait = new CountDownTimer(1000, 500) {
                @Override
                public void onTick(long l) {
                    timerTextView.setText(String.valueOf(l / 500 + "s"));
                }

                @Override
                public void onFinish() {
                    correctTextView.setText("😀: " + correctQuestions);
                    scoreTextView.setText(correctQuestions + " / 15");
                    textView.setVisibility(View.INVISIBLE);
                    questionsLeft -= 1;
                    button0.setVisibility(View.VISIBLE);
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    timerTextView.setVisibility(View.INVISIBLE);
                    newQuestion();
                    questionsLeftTextView.setText(questionsLeft + " ❓ left");
                }
            }.start();

            if (correctQuestions+wrongQuestions >= 15) {
                wait.cancel();
                seeReview();
            }

        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        playAgainButton = findViewById(R.id.playAgainButton);
        textView = findViewById(R.id.textView);
        pointsTextView = findViewById(R.id.pointsTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        timerTextView = findViewById(R.id.timerTextView);
        incorrectTextView = findViewById(R.id.incorrectTextView);
        correctTextView = findViewById(R.id.correctTextView);
        correctTextView.setText("😀: 0");
        incorrectTextView.setText("😟: 0");
        button0.setVisibility(View.VISIBLE);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        timerTextView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
        correctTextView.setVisibility(View.VISIBLE);
        incorrectTextView.setVisibility(View.VISIBLE);
        questionsLeftTextView = findViewById(R.id.questionsLeftTextView);
        questionsLeftTextView.setVisibility(View.VISIBLE);
        newQuestion();


        //DownloadTask task = new DownloadTask();
        //String result = null;

        try {
            //result = task.execute("").get();


        } catch (Exception e) {
            e.printStackTrace();
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.i("admob", "initialization complete.");
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1219365158574831/7050222069");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("admob", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("admob", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.i("admob", "onAdOpened");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.i("admob", "onAdClicked");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("admob", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                Log.i("admob", "onAdClosed");
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

    }
}