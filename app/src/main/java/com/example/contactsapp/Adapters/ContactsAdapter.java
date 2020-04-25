package com.example.contactsapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsapp.Database.DatabaseHelper;
import com.example.contactsapp.Database.DatabaseMethods;
import com.example.contactsapp.Detail.DetailActivity;
import com.example.contactsapp.Main.MainActivity;
import com.example.contactsapp.Model.Contact;
import com.example.contactsapp.R;
import com.example.contactsapp.Utils.BitmapByteConverter;
import com.example.contactsapp.Utils.Permissions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private static final String TAG = "RecyclerView";

    private Context mContext;
    private List<Contact> contactList;
    private Dialog mDialog;

    public ContactsAdapter(Context mContext, List<Contact> contactList) {
        this.mContext = mContext;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewPhone.setText(contactList.get(position).getPhone());
        holder.textViewName.setText(contactList.get(position).getName());

        if (contactList.get(position).getProfile_photo() != null){
            Bitmap bitmap = BitmapByteConverter.getImage(contactList.get(position).getProfile_photo());
            holder.imageViewPhoto.setImageBitmap(bitmap);
        }else {
            holder.imageViewPhoto.setImageResource(R.drawable.default_pp_32dp);
        }

        showDialog(holder);
        showPopUp(holder,contactList.get(position).getId());
    }

    private void showPopUp(final ViewHolder viewHolder, final int id) {
        viewHolder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext,viewHolder.img_more);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.action_detail:
                                openFragment(id);
                                return true;
                            case R.id.action_delete:
                                deleteContact(id);
                                return true;
                            case R.id.action_add_to_favourites:
                                addToFavourites(id);
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void addToFavourites(int id) {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        Contact contact = new DatabaseMethods().getContact(databaseHelper,id);

        if (contact.getFavourite().equals("favourite")){
            Toast.makeText(mContext, "Already added to favorites", Toast.LENGTH_SHORT).show();
        }else {
            contact.setFavourite("favourite");
            if (new DatabaseMethods().updateContact(databaseHelper,contact,id)){
                Toast.makeText(mContext, "Added to Favourites", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteContact(int id) {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        DatabaseMethods databaseMethods = new DatabaseMethods();
        if (databaseMethods.deleteContact(databaseHelper,id) > 0 ){
            Toast.makeText(mContext, "Contact Deleted", Toast.LENGTH_SHORT).show();
            contactList = databaseMethods.getAllContacts(databaseHelper);
            notifyDataSetChanged();
        }
    }

    private void openFragment(int id) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("forWhat",mContext.getString(R.string.intent_name_detailContact));
        intent.putExtra("id",id);
        mContext.startActivity(intent);
    }

    private void showDialog(final ViewHolder viewHolder) {

        viewHolder.contactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = new Dialog(mContext);
                mDialog.setContentView(R.layout.contact_dialog);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();

                Button btnCall = mDialog.findViewById(R.id.btnCall);
                Button btnSendMessage = mDialog.findViewById(R.id.btnSendMessage);
                Button btnSendEmail = mDialog.findViewById(R.id.btnSendEmail);
                TextView dialogContactName = mDialog.findViewById(R.id.dialog_contact_name);
                TextView dialogContactPhone = mDialog.findViewById(R.id.dialog_contact_number);
                TextView dialogClose = mDialog.findViewById(R.id.dialog_close);
                CircleImageView dialogPhoto = mDialog.findViewById(R.id.dialog_image);

                String name = contactList.get(viewHolder.getAdapterPosition()).getName();
                final String phone = contactList.get(viewHolder.getAdapterPosition()).getPhone();
                final String email = contactList.get(viewHolder.getAdapterPosition()).getEmail();
                byte[] photo = contactList.get(viewHolder.getAdapterPosition()).getProfile_photo();

                dialogContactName.setText(name);
                dialogContactPhone.setText(phone);
                if (photo != null){
                    dialogPhoto.setImageBitmap(BitmapByteConverter.getImage(photo));
                }else {
                    dialogPhoto.setImageResource(R.drawable.default_pp_128dp);
                }
                dialogClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });

                btnSendEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{email});
                        mContext.startActivity(Intent.createChooser(emailIntent,"Send Email Using: "));
                    }
                });

                btnSendMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",phone,null));
                        mContext.startActivity(smsIntent);
                    }
                });

                btnCall.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(View v) {
                        if (((MainActivity)mContext).checkPermissionsArray(Permissions.PHONE_PERMISSIONS)){
                            Intent callIntent = new Intent(Intent.ACTION_CALL,Uri.fromParts("tel",phone,null));
                            mContext.startActivity(callIntent);
                        }else {
                            ((MainActivity)mContext).verifyPermissions(Permissions.PHONE_PERMISSIONS);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


    public static class  ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName,textViewPhone;
        ImageView img_more;
        CircleImageView imageViewPhoto;
        LinearLayout contactItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contactItem = itemView.findViewById(R.id.contact_item);
            textViewName = itemView.findViewById(R.id.edit_contact_name);
            textViewPhone = itemView.findViewById(R.id.phone_number);
            imageViewPhoto = itemView.findViewById(R.id.imageViewContact);
            img_more = itemView.findViewById(R.id.img_more);
        }
    }
}
