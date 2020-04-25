package com.example.contactsapp.Utils;

import android.Manifest;

public class Permissions {

    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static final String[] PHONE_PERMISSIONS = {Manifest.permission.CALL_PHONE};

    public static final int GALLERYREQUESTCODE = 1;
    public static final int CAMERAREQUESTCODE = 2;
    public static final int PReqCode = 1;
}
