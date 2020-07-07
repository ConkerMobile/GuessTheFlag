package com.conkermobile.guesstheflag;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.plattysoft.leonids.ParticleSystem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {
    String flagUrl = "https://raw.githubusercontent.com/hjnilsson/country-flags/master/png1000px/";
    ArrayList<String> answersPicked = new ArrayList<String>();
    ArrayList<String> flagNames = new ArrayList<String>(Arrays.asList("AF", "Afghanistan", "AX", "Aland Islands", "AL", "Albania", "DZ", "Algeria", "AS", "American Samoa", "AD", "Andorra", "AO", "Angola", "AI", "Anguilla", "AQ", "Antarctica", "AG", "Antigua and Barbuda", "AR", "Argentina", "AM", "Armenia", "AW", "Aruba", "AU", "Australia", "AT", "Austria", "AZ", "Azerbaijan", "BS", "Bahamas", "BH", "Bahrain", "BD", "Bangladesh", "BB", "Barbados", "BY", "Belarus", "BE", "Belgium", "BZ", "Belize", "BJ", "Benin", "BM", "Bermuda", "BT", "Bhutan", "BO", "Bolivia", "BQ", "Bonaire, Saint Eustatius and Saba", "BA", "Bosnia and Herzegovina", "BW", "Botswana", "BV", "Bouvet Island", "BR", "Brazil", "IO", "British Indian Ocean Territory", "VG", "British Virgin Islands", "BN", "Brunei", "BG", "Bulgaria", "BF", "Burkina Faso", "BI", "Burundi", "KH", "Cambodia", "CM", "Cameroon", "CA", "Canada", "CV", "Cape Verde", "KY", "Cayman Islands", "CF", "Central African Republic", "TD", "Chad", "CL", "Chile", "CN", "China", "CX", "Christmas Island", "CC", "Cocos Islands", "CO", "Colombia", "KM", "Comoros", "CK", "Cook Islands", "CR", "Costa Rica", "HR", "Croatia", "CU", "Cuba", "CW", "Curacao", "CY", "Cyprus", "CZ", "Czech Republic", "CD", "Democratic Republic of the Congo", "DK", "Denmark", "DJ", "Djibouti", "DM", "Dominica", "DO", "Dominican Republic", "TL", "East Timor", "EC", "Ecuador", "EG", "Egypt", "SV", "El Salvador", "GQ", "Equatorial Guinea", "ER", "Eritrea", "EE", "Estonia", "ET", "Ethiopia", "FK", "Falkland Islands", "FO", "Faroe Islands", "FJ", "Fiji", "FI", "Finland", "FR", "France", "GF", "French Guiana", "PF", "French Polynesia", "TF", "French Southern Territories", "GA", "Gabon", "GM", "Gambia", "GE", "Georgia", "DE", "Germany", "GH", "Ghana", "GI", "Gibraltar", "GR", "Greece", "GL", "Greenland", "GD", "Grenada", "GP", "Guadeloupe", "GU", "Guam", "GT", "Guatemala", "GG", "Guernsey", "GN", "Guinea", "GW", "Guinea-Bissau", "GY", "Guyana", "HT", "Haiti", "HM", "Heard Island and McDonald Islands", "HN", "Honduras", "HK", "Hong Kong", "HU", "Hungary", "IS", "Iceland", "IN", "India", "ID", "Indonesia", "IR", "Iran", "IQ", "Iraq", "IE", "Ireland", "IM", "Isle of Man", "IL", "Israel", "IT", "Italy", "CI", "Ivory Coast", "JM", "Jamaica", "JP", "Japan", "JE", "Jersey", "JO", "Jordan", "KZ", "Kazakhstan", "KE", "Kenya", "KI", "Kiribati", "XK", "Kosovo", "KW", "Kuwait", "KG", "Kyrgyzstan", "LA", "Laos", "LV", "Latvia", "LB", "Lebanon", "LS", "Lesotho", "LR", "Liberia", "LY", "Libya", "LI", "Liechtenstein", "LT", "Lithuania", "LU", "Luxembourg", "MO", "Macao", "MK", "Macedonia", "MG", "Madagascar", "MW", "Malawi", "MY", "Malaysia", "MV", "Maldives", "ML", "Mali", "MT", "Malta", "MH", "Marshall Islands", "MQ", "Martinique", "MR", "Mauritania", "MU", "Mauritius", "YT", "Mayotte", "MX", "Mexico", "FM", "Micronesia", "MD", "Moldova", "MC", "Monaco", "MN", "Mongolia", "ME", "Montenegro", "MS", "Montserrat", "MA", "Morocco", "MZ", "Mozambique", "MM", "Myanmar", "NA", "Namibia", "NR", "Nauru", "NP", "Nepal", "NL", "Netherlands", "AN", "Netherlands Antilles", "NC", "New Caledonia", "NZ", "New Zealand", "NI", "Nicaragua", "NE", "Niger", "NG", "Nigeria", "NU", "Niue", "NF", "Norfolk Island", "KP", "North Korea", "MP", "Northern Mariana Islands", "NO", "Norway", "OM", "Oman", "PK", "Pakistan", "PW", "Palau", "PS", "Palestinian Territory", "PA", "Panama", "PG", "Papua New Guinea", "PY", "Paraguay", "PE", "Peru", "PH", "Philippines", "PN", "Pitcairn", "PL", "Poland", "PT", "Portugal", "PR", "Puerto Rico", "QA", "Qatar", "CG", "Republic of the Congo", "RE", "Reunion", "RO", "Romania", "RU", "Russia", "RW", "Rwanda", "BL", "Saint Barthelemy", "SH", "Saint Helena", "KN", "Saint Kitts and Nevis", "LC", "Saint Lucia", "MF", "Saint Martin", "PM", "Saint Pierre and Miquelon", "VC", "Saint Vincent and the Grenadines", "WS", "Samoa", "SM", "San Marino", "ST", "Sao Tome and Principe", "SA", "Saudi Arabia", "SN", "Senegal", "RS", "Serbia", "SC", "Seychelles", "SL", "Sierra Leone", "SG", "Singapore", "SX", "Sint Maarten", "SK", "Slovakia", "SI", "Slovenia", "SB", "Solomon Islands", "SO", "Somalia", "ZA", "South Africa", "GS", "South Georgia and the South Sandwich Islands", "KR", "South Korea", "SS", "South Sudan", "ES", "Spain", "LK", "Sri Lanka", "SD", "Sudan", "SR", "Suriname", "SJ", "Svalbard and Jan Mayen", "SZ", "Swaziland", "SE", "Sweden", "CH", "Switzerland", "SY", "Syria", "TW", "Taiwan", "TJ", "Tajikistan", "TZ", "Tanzania", "TH", "Thailand", "TG", "Togo", "TK", "Tokelau", "TO", "Tonga", "TT", "Trinidad and Tobago", "TN", "Tunisia", "TR", "Turkey", "TM", "Turkmenistan", "TC", "Turks and Caicos Islands", "TV", "Tuvalu", "VI", "U.S. Virgin Islands", "UG", "Uganda", "UA", "Ukraine", "AE", "United Arab Emirates", "GB", "United Kingdom", "US", "United States", "UM", "United States Minor Outlying Islands", "UY", "Uruguay", "UZ", "Uzbekistan", "VU", "Vanuatu", "VA", "Vatican", "VE", "Venezuela", "VN", "Vietnam", "WF", "Wallis and Futuna", "EH", "Western Sahara", "YE", "Yemen", "ZM", "Zambia", "ZW", "Zimbabwe"));
    ArrayList<String> countryCode = new ArrayList<>();
    ArrayList<String> countryNames = new ArrayList<>();
    String[] answers = new String[4];
    ImageView imageView;
    int chosenCar = 0;
    int locationOfCorrectAnswer = 0;
    int questionsLeft = 0;
    int points = 10;
    int correctQuestions = 0;
    int questionsAnswered;
    int wrongQuestions;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    View carGoButton;
    Button playAgainButton;
    TextView textView;
    TextView pointsTextView;
    TextView scoreTextView;
    TextView timerTextView;
    TextView incorrectTextView;
    TextView correctTextView;
    TextView questionsLeftTextView;
    boolean buttonSelected = false;
    boolean done = false;
    private InterstitialAd mInterstitialAd;

    public void start(View view) {

        questionsLeftTextView.setVisibility(View.VISIBLE);

        findViewById(R.id.cover).setVisibility(View.INVISIBLE);

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.i("admob", "not loaded.");
        }
        correctTextView.setVisibility(View.VISIBLE);
        incorrectTextView.setVisibility(View.VISIBLE);
        scoreTextView.setVisibility(View.VISIBLE);
        carGoButton.setVisibility(View.INVISIBLE);
        button0.setVisibility(View.VISIBLE);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        scoreTextView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }

    public void seeFlags(View view) {

    }

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
    }
    public synchronized void carChosen(View view) {
        Log.i("carChosen", "clicked: "+view.getTag());
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            Log.i("carChosen", "ignored: "+view.getTag().toString());
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
        if (correctQuestions+wrongQuestions >= 15 && questionsLeft == 0) {
            seeReview();
        }
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

        for (int i=0; i<flagNames.size(); i+=2){
            countryCode.add(flagNames.get(i));
            countryNames.add(flagNames.get(i+1));
        }


        imageView = findViewById(R.id.imageView);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        playAgainButton = findViewById(R.id.playAgainButton);
        carGoButton = findViewById(R.id.carGoButton);
        textView = findViewById(R.id.textView);
        pointsTextView = findViewById(R.id.pointsTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        timerTextView = findViewById(R.id.timerTextView);
        incorrectTextView = findViewById(R.id.incorrectTextView);
        correctTextView = findViewById(R.id.correctTextView);
        correctTextView.setText("😀: 0");
        incorrectTextView.setText("😟: 0");
        newQuestion();
        button0.setVisibility(View.INVISIBLE);
        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.INVISIBLE);
        carGoButton.setVisibility(View.VISIBLE);
        textView.setVisibility(View.INVISIBLE);
        timerTextView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        correctTextView.setVisibility(View.INVISIBLE);
        incorrectTextView.setVisibility(View.INVISIBLE);
        questionsLeftTextView = findViewById(R.id.questionsLeftTextView);


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
        mInterstitialAd.setAdUnitId("ca-app-pub-1219365158574831/5503613787");
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