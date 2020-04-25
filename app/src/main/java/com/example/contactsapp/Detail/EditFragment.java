package com.example.contactsapp.Detail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactsapp.Database.DatabaseHelper;
import com.example.contactsapp.Database.DatabaseMethods;
import com.example.contactsapp.Main.MainActivity;
import com.example.contactsapp.Model.Contact;
import com.example.contactsapp.R;
import com.example.contactsapp.Utils.BitmapByteConverter;
import com.example.contactsapp.Utils.Permissions;
import com.example.contactsapp.Utils.SendData;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {

    private static final String TAG = "EditFragment";
    private View view;

    private Toolbar toolbar;

    private TextView contact_name,contact_phone,contact_email;
    private ImageView img_choose_photo,img_contact;

    private int id;
    private Contact contact;
    private String tempName;
    private String tempPhone;
    private String tempEmail;

    private String checkPhotoPicked = "not picked";
    private Uri pickedImage;

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit, container, false);

        setupToolbar();
        initViews();
        setContactInfo();
        managePermissions();

        return view;
    }

    private void managePermissions() {
        img_choose_photo = view.findViewById(R.id.img_choose_photo);
        img_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >=22){
                    if (checkPermissionsArray(Permissions.PERMISSIONS)){
                        showDialog();
                    }else {
                        verifyPermissions(Permissions.PERMISSIONS);
                    }
                }
            }
        });
    }

    private void verifyPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(
                getActivity(),
                permissions,
                Permissions.PReqCode
        );
    }

    private boolean checkPermissionsArray(String[] permissions) {
        for (int i=0; i<permissions.length;i++){
            String permission = permissions[i];
            if (!checkPermissions(permission)){
                return false;
            }
        }
        return true;
    }

    private boolean checkPermissions(String permission) {
        int permissionRequest = ActivityCompat.checkSelfPermission(getActivity(),permission);
        if (permissionRequest != PackageManager.PERMISSION_GRANTED){
            return false;
        }else {
            return true;
        }
    }

    private void showDialog() {
        CharSequence options[] = new CharSequence[]{"Choose from the memory","Take a new photo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    openGallery();
                }
                if (which == 1){
                    openCamera();
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,Permissions.CAMERAREQUESTCODE);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Permissions.GALLERYREQUESTCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && requestCode == Permissions.GALLERYREQUESTCODE && data != null){
                pickedImage = data.getData();
                img_contact.setImageURI(pickedImage);
                checkPhotoPicked = "picked";
            }
            if (resultCode == RESULT_OK && requestCode == Permissions.CAMERAREQUESTCODE && data !=null){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                img_contact.setImageBitmap(bitmap);
                checkPhotoPicked = "picked";
            }
        }catch (Exception e){

        }
    }

    //Set,Init,Setup
    private void setContactInfo() {
        img_contact = view.findViewById(R.id.img_contact_detail);

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        DatabaseMethods databaseMethods = new DatabaseMethods();
        contact = databaseMethods.getContact(databaseHelper,id);

        contact_name.setText(contact.getName());
        contact_phone.setText(contact.getPhone().substring(2,14));
        if (!contact.getEmail().equals("")){
            contact_email.setText(contact.getEmail());
        }
        if (contact.getProfile_photo() != null){
            Bitmap bitmap = BitmapByteConverter.getImage(contact.getProfile_photo());
            img_contact.setImageBitmap(bitmap);
        }else {
            img_contact.setImageResource(R.drawable.default_pp_256dp);
        }
    }

    private void initViews() {
        contact_name = view.findViewById(R.id.edit_contact_name);
        contact_phone = view.findViewById(R.id.edit_contact_phone);
        contact_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        contact_email = view.findViewById(R.id.edit_contact_email);
    }

    private void setupToolbar() {
        toolbar = view.findViewById(R.id.edit_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Contact");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Options Menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_action:
                deleteContact();
                return true;
            case R.id.action_confirm:
                if (anyChange()){
                    updateContact();
                }else {
                    Toast.makeText(getContext(), "Nothing Changed", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return false;
        }
    }

    private void updateContact() {
        Bitmap bitmap = ((BitmapDrawable) img_contact.getDrawable()).getBitmap();
        Contact tempContact = new Contact(id,tempName,"0 " + tempPhone,tempEmail, BitmapByteConverter.getBytes(bitmap),contact.getFavourite());
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        DatabaseMethods databaseMethods = new DatabaseMethods();

        if (databaseMethods.updateContact(databaseHelper,tempContact,id)){
            Toast.makeText(getContext(), "Contact Updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }else {
            Toast.makeText(getContext(), "Contasdfasdfact Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean anyChange() {
        tempName = contact_name.getText().toString();
        tempPhone = contact_phone.getText().toString();
        tempEmail = contact_email.getText().toString();

        if (checkPhotoPicked.equals("picked")){
            return true;
        }else {
            if (!tempName.equals(contact.getName()) || !tempEmail.equals(contact.getEmail()) || !contact.getPhone().equals("0 " + tempPhone)){
                return true;
            }else {
                return false;
            }
        }
    }

    private void deleteContact() {
        final DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        final DatabaseMethods databaseMethods = new DatabaseMethods();

        View v = getActivity().findViewById(android.R.id.content);

        Snackbar snackbar = Snackbar.make(v,"Delete Contact?",3000)
                .setAction("Delete", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (databaseMethods.deleteContact(databaseHelper,id) > 0){
                            Snackbar.make(v ,"Contact Deleted",Snackbar.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else {
                            Toast.makeText(getContext(), "OLMADI", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        snackbar.getView().setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        snackbar.setActionTextColor(getActivity().getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    //EventBus
    @Subscribe(sticky = true)
    public void getID(SendData data){
        id = data.getId();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
