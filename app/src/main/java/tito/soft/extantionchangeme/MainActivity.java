package tito.soft.extantionchangeme;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

import tito.soft.extantionchangeme.businesstire.RenameFiles;

public class MainActivity extends AppCompatActivity   {
    Intent serviceIntent;

    String[] _extensions;
    private ProgressBar progressBar;
    String extEdit = ".mp3";
    File dir = null;
    public TextView _textView;
    private broadCastToMain receiver;





    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         RecyclerView recyclerView, recyclerView2, recyclerView3, recyclerView4;
         RecyclerView.Adapter mAdapter;
         RecyclerView.LayoutManager layoutManager;


        IntentFilter filter = new IntentFilter("APP_SPECIFIC_BROADCAST");
        receiver = new broadCastToMain();
        registerReceiver( receiver, filter);

        serviceIntent = new Intent(this,
                RenameFiles.class);

         _textView = findViewById(R.id.textView);


        TextView extensionText = findViewById(R.id.extensionText);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.WHITE));



        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        _extensions = getResources().getStringArray(R.array.AUDIO);
        mAdapter = new RecyclerPlugin(_extensions, extensionText::setText);
        recyclerView.setAdapter(mAdapter);

        recyclerView2 = findViewById(R.id.my_recycler_view2);
        recyclerView2.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager);
        _extensions = getResources().getStringArray(R.array.IMAGE);
        mAdapter = new RecyclerPlugin(_extensions, extensionText::setText);
        recyclerView2.setAdapter(mAdapter);
        recyclerView2.setNestedScrollingEnabled(false);

        recyclerView3 = findViewById(R.id.my_recycler_view3);
        recyclerView3.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(layoutManager);
        _extensions = getResources().getStringArray(R.array.VIDEO);
        mAdapter = new RecyclerPlugin(_extensions, extensionText::setText);
        recyclerView3.setAdapter(mAdapter);
        recyclerView3.setNestedScrollingEnabled(false);

        recyclerView4 = findViewById(R.id.my_recycler_view4);
        recyclerView4.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView4.setLayoutManager(layoutManager);
        _extensions = getResources().getStringArray(R.array.TEXT);
        mAdapter = new RecyclerPlugin(_extensions, extensionText::setText);
        recyclerView4.setAdapter(mAdapter);
        recyclerView4.setNestedScrollingEnabled(false);


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);


        Button showMe = findViewById(R.id.showMe);


        showMe.setOnClickListener(v -> {

            extEdit = extensionText.getText().toString();

            OpenFolder();
        });




    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void OpenFolder() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(Intent.createChooser(intent, "Select a folder"), 200);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {

            String fullDirPath = data.getData().getPath();
            String[] dirName = fullDirPath.split(":");


            String path = Environment.getExternalStorageDirectory().toString() + "/" + dirName[1] + "/";
            dir = new File(path + "/Result OF Files");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            serviceIntent.putExtra("path", path);
            serviceIntent.putExtra("dir",  dir);
            serviceIntent.putExtra("ext", extEdit);

            startService(serviceIntent);

        }
    }

    public class broadCastToMain  extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle extras = intent.getExtras();
            int MAX_PROGRESS = (int) extras.get("MAX_PROGRESS");
            int loadingCounter = (int) extras.get("loadingCounter");

            StringBuilder text = new StringBuilder();
            text =
                    loadingCounter == 0 ? text.append(" please wait for fetching the files ").append(MAX_PROGRESS):
                            text.append(" working on ").append(loadingCounter).append(" of ").append(MAX_PROGRESS)
                                    .append(" Done ");
            _textView.setText(text);
            progressBar.setMax(MAX_PROGRESS);
            progressBar.setProgress(loadingCounter);

        }


    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }





}