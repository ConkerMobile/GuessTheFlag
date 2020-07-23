package com.conkermobile.guesstheflag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import static com.conkermobile.guesstheflag.App.countryCode;
import static com.conkermobile.guesstheflag.App.countryNames;
import static com.conkermobile.guesstheflag.App.flagUrl;

public class FlagListActivity extends Activity {

    ListView flagsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_list);
        flagsListView = findViewById(R.id.flagsListView);

        final ListView list = findViewById(R.id.flagsListView);
        ArrayList<SubjectData> arrayList = new ArrayList<SubjectData>();
        for (int i=0; i<countryNames.size(); i++) {
            arrayList.add
                    (new SubjectData
                            (countryNames.get(i),
                                    "https://raw.githubusercontent.com/hjnilsson/country-flags/master/png1000px/",
                                    flagUrl + countryCode.get(i).toLowerCase() + ".png"));
        }
        CustomAdapter customAdapter = new CustomAdapter(this, arrayList);
        flagsListView.setAdapter(customAdapter);


    }
}