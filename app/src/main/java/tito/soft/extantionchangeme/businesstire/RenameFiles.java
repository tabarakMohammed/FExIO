package tito.soft.extantionchangeme.businesstire;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.File;


public class RenameFiles extends Service {

    String path, extEdit;
    File dir;
    int MAX_PROGRESS = 0, loadingCounter = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


            Bundle extras = intent.getExtras();
            path = (String) extras.get("path");
            dir = (File) extras.get("dir");
            extEdit = (String) extras.get("ext");


        new Thread(() -> {
            calculateMAx(path);
            renameFilesInDir(path, dir.getAbsolutePath(), extEdit);
        }).start();


        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void renameFilesInDir(String root, String dirIn, String ext) {

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("APP_SPECIFIC_BROADCAST");
        File file = new File(root);
        if (file.length() == 0) {
            return;
        }
        File[] list = file.listFiles();

        broadcastIntent.putExtra("MAX_PROGRESS", MAX_PROGRESS);
        broadcastIntent.putExtra("loadingCounter", loadingCounter);
        sendBroadcast(broadcastIntent);


        String distPathManager;


        assert list != null;
        for (File checkFile : list) {

            if (checkFile.isFile()) {
                String NameDistPathManager;
                distPathManager = checkFile.getAbsolutePath().substring(checkFile.getPath().lastIndexOf("/"));

                if (checkFile.getPath().contains(".")) {
                    NameDistPathManager = distPathManager.replaceAll("\\.\\w*", "");
                } else {
                    NameDistPathManager = distPathManager;
                }
                try {

                    checkFile.renameTo(new File(dirIn, NameDistPathManager + ext));
                    loadingCounter++;
                    broadcastIntent.putExtra("loadingCounter", loadingCounter);
                    sendBroadcast(broadcastIntent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (checkFile.isDirectory()) {
                renameFilesInDir(checkFile.getAbsolutePath(), dir.getAbsolutePath(), extEdit);
            }


        }
    }

    void calculateMAx(String root) {
        File file = new File(root);
        File[] list = file.listFiles();
        assert list != null;
        for (File checkFile : list) {
            if (checkFile.isFile()) {
                MAX_PROGRESS++;
            }
            if (checkFile.isDirectory()) {
                calculateMAx(checkFile.getAbsolutePath());
            }

        }

    }

}
