package tito.soft.extantionchangeme;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView, recyclerView2, recyclerView3, recyclerView4;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    String[] small;

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    String extEdit = ".mp3";
    File dir = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView extensionText = findViewById(R.id.extensionText);
        progressBar = findViewById(R.id.progressBar);


        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        small = getResources().getStringArray(R.array.AUDIO);
        mAdapter = new RecyclerPlugin(small, food -> {
            //  Log.d("", food );
            extensionText.setText(food);
        });
        recyclerView.setAdapter(mAdapter);

        recyclerView2 = findViewById(R.id.my_recycler_view2);
        recyclerView2.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager);
        small = getResources().getStringArray(R.array.IMAGE);
        mAdapter = new RecyclerPlugin(small, food -> {
            //  Log.d("", food );
            extensionText.setText(food);
        });
        recyclerView2.setAdapter(mAdapter);
        recyclerView2.setNestedScrollingEnabled(false);

        recyclerView3 = findViewById(R.id.my_recycler_view3);
        recyclerView3.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(layoutManager);
        small = getResources().getStringArray(R.array.VIDEO);
        mAdapter = new RecyclerPlugin(small, food -> {
            //  Log.d("", food );
            extensionText.setText(food);
        });
        recyclerView3.setAdapter(mAdapter);
        recyclerView3.setNestedScrollingEnabled(false);

        recyclerView4 = findViewById(R.id.my_recycler_view4);
        recyclerView4.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView4.setLayoutManager(layoutManager);
        small = getResources().getStringArray(R.array.TEXT);
        mAdapter = new RecyclerPlugin(small, food -> {
            //  Log.d("", food );
            extensionText.setText(food);
        });
        recyclerView4.setAdapter(mAdapter);
        recyclerView4.setNestedScrollingEnabled(false);


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);


        Button showMe = findViewById(R.id.showMe);


        showMe.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            public void onClick(View v) {
                extEdit = extensionText.getText().toString();
                OpenFolder();

            }
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
            String s = data.getData().getPath();
            String sx[] = s.split(":");
            Log.d("", "s: " + sx[1]);

            String path = Environment.getExternalStorageDirectory().toString() + "/" + sx[1] + "/";
            dir = new File(path + "/Result OF Files");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //   deleteEmptyFolders(path);
            walk(path, dir.getAbsolutePath(), extEdit);
        }
    }


//     public void deleteEmptyFolders(String root){
//         File file = new File(root);
//          File[] folderList;
//
//         if(file == null){
//             return;
//         } else {
//             folderList = file.listFiles();
//             for (File folder : folderList) {
//                  if(folder.isDirectory()){
//                      try {
//                       if (folder.listFiles().length == 0) {
//
//                           folder.delete();
//
//                       }else{
//
//                           deleteEmptyFolders(folder.getAbsolutePath());
//                       }
//
//                     }catch (NullPointerException e){
//                        // e.printStackTrace();
//                     }
//                  }
//             }
//         }
//     }

    public void walk(String root, String dirin, String ext) {


        File file = new File(root);

        if (file == null) {
            return;
        }
        File[] list = file.listFiles();
        progressBar.setMax(list.length);
        String distPathManager;


        for (File f : list) {
            progressStatus += 1;
            progressBar.setProgress(progressStatus);


            if (f.isFile()) {
                String NameDistPathManager;
                distPathManager = f.getAbsolutePath().substring(f.getPath().lastIndexOf("/"));

                if (f.getPath().contains(".")) {
                    NameDistPathManager = distPathManager.replaceAll("\\.\\w*", "");
                } else {
                    NameDistPathManager = distPathManager;
                }
                try {

                    f.renameTo(new File(dirin, NameDistPathManager + ext));


                } catch (Exception e) {
                    e.printStackTrace();
                }

                TextView _textView = findViewById(R.id.textView);
                _textView.setText("all is ok ! " + progressStatus);

            } else if (f.isDirectory()) {

                walk(f.getAbsolutePath(), dir.getAbsolutePath(), extEdit);


            }


        }
    }

/*

                         copyFile(f,new File( dirin,NameDistPathManager+ext));
                         InputStream in = new FileInputStream(f.getAbsolutePath());
                         OutputStream out = new FileOutputStream(new File( dirin,NameDistPathManager+ext));
                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                             FileUtils.copy(in,out);
                         }

                         void copyFile(File src, File dst) throws IOException {

         FileChannel inChannel = new FileInputStream(src).getChannel();
         FileChannel outChannel = new FileOutputStream(dst).getChannel();
         try {
             inChannel.transferTo(0, inChannel.size(), outChannel);
         } finally {
             if (inChannel != null)
                 inChannel.close();
             if (outChannel != null)
                 outChannel.close();
         }
     }

* */

}