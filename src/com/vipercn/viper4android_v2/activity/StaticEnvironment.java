package com.vipercn.viper4android_v2.activity;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StaticEnvironment {

    private static boolean sEnvironmentInitialized;
    private static String sExternalStoragePath = "";
    private static String sV4aRoot = "";
    private static String sV4aKernelPath = "";
	private static String sV4aCustomDDCPath = "";
    private static String sV4aProfilePath = "";

    private static boolean checkWritable(String mFileName) {
        try {
            byte[] mBlank = new byte[16];
            FileOutputStream fosOutput = new FileOutputStream(mFileName);
            fosOutput.write(mBlank);
            fosOutput.flush();
            fosOutput.close();
            fosOutput = null;
            mBlank = null;
            return new File(mFileName).delete();
        } catch (FileNotFoundException e) {
            Log.i("ViPER4Android", "FileNotFoundException, msg = " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.i("ViPER4Android", "IOException, msg = " + e.getMessage());
            return false;
        }
    }

    private static void proceedExternalStoragePath() {
        // Get path
        String mExternalStoragePathName = Environment.getExternalStorageDirectory().getAbsolutePath();

        // Check writable
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.i("ViPER4Android", "Really terrible thing, external storage detection failed. V4A may malfunction");
        } else {
            String externalPath;
            externalPath = mExternalStoragePathName.endsWith("/") ? mExternalStoragePathName : mExternalStoragePathName + "/" + "v4a_test_file";
            Log.i("ViPER4Android", "Now checking for external storage writable, file = " + externalPath);
            sExternalStoragePath = mExternalStoragePathName.endsWith("/") ? mExternalStoragePathName : mExternalStoragePathName + "/";
            sV4aRoot = sExternalStoragePath + "ViPER4Android/";
            sV4aKernelPath = sV4aRoot + "Kernel/";
            sV4aCustomDDCPath = sV4aRoot + "DDC/";
            sV4aProfilePath = sV4aRoot + "Profile/";
            if (checkWritable(externalPath)) {
                new File(sV4aKernelPath).mkdirs();
                new File(sV4aCustomDDCPath).mkdirs();
                new File(sV4aProfilePath).mkdirs();
            }
        }
    }

    public static boolean isEnvironmentInitialized() {
        return sEnvironmentInitialized;
    }

    public static void initEnvironment() {
        try {
            proceedExternalStoragePath();
        } catch (Exception e) {
            String mExternalStoragePathName = Environment.getExternalStorageDirectory().getAbsolutePath();
            sExternalStoragePath = mExternalStoragePathName.endsWith("/") ? mExternalStoragePathName : mExternalStoragePathName + "/";
            sV4aRoot = sExternalStoragePath + "ViPER4Android/";
            sV4aKernelPath = sV4aRoot + "Kernel/";
			sV4aCustomDDCPath = sV4aRoot + "DDC/";
            sV4aProfilePath = sV4aRoot + "Profile/";
        }
        sEnvironmentInitialized = true;
    }

    public static String getExternalStoragePath() {
        return sExternalStoragePath;
    }

    public static String getV4aRootPath() {
        return sV4aRoot;
    }

    public static String getV4aKernelPath() {
        return sV4aKernelPath;
    }

    public static String getV4aCustomDDCPath() {
        return sV4aCustomDDCPath;
    }

    public static String getV4aProfilePath() {
        return sV4aProfilePath;
    }
}
