package com.example.contactsapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsapp.Database.DatabaseHelper;
import com.example.contactsapp.Database.DatabaseMethods;
import com.example.contactsapp.Main.MainActivity;
import com.example.contactsapp.Model.Contact;
import com.example.contactsapp.R;
import com.example.contactsapp.Utils.BitmapByteConverter;
import com.example.contactsapp.Utils.Permissions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouriteViewHolder> {

    private Context mContext;
    private ArrayList<Contact> favouriteList;

    private Dialog mDialog;

    public FavouritesAdapter(Context mContext, ArrayList<Contact> favouriteList) {
        this.mContext = mContext;
        this.favouriteList = favouriteList;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_favourite,parent,false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavouriteViewHolder holder, int position) {
        final Contact contact = favouriteList.get(position);
        holder.favouriteName.setText(contact.getName());
        holder.favouritePhone.setText(contact.getPhone());
        if (contact.getProfile_photo() != null){
            Bitmap bitmap = BitmapByteConverter.getImage(contact.getProfile_photo());
            holder.imgPhoto.setImageBitmap(bitmap);
        }else {
            holder.imgPhoto.setImageResource(R.drawable.default_pp_32dp);
        }
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
                DatabaseMethods databaseMethods = new DatabaseMethods();
                contact.setFavourite("not favourite");
                if (databaseMethods.updateContact(databaseHelper,contact,contact.getId())){
                    Toast.makeText(mContext, "Deleted from Favourites", Toast.LENGTH_SHORT).show();
                    favouriteList = databaseMethods.getAllFavourites(databaseHelper);
                    notifyDataSetChanged();
                }
            }
        });
        
        holder.favouriteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(holder);
            }
        });
    }

    private void showDialog(FavouriteViewHolder viewHolder) {
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

        String name = favouriteList.get(viewHolder.getAdapterPosition()).getName();
        final String phone = favouriteList.get(viewHolder.getAdapterPosition()).getPhone();
        final String email = favouriteList.get(viewHolder.getAdapterPosition()).getEmail();
        byte[] photo = favouriteList.get(viewHolder.getAdapterPosition()).getProfile_photo();

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
                Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",phone,null));
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

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder{

        TextView favouriteName,favouritePhone;
        ImageView img_delete;
        CircleImageView imgPhoto;
        LinearLayout favouriteItem;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);

            favouriteItem = itemView.findViewById(R.id.favourite_item);
            favouriteName = itemView.findViewById(R.id.favourite_name);
            favouritePhone = itemView.findViewById(R.id.favourite_phone_number);
            img_delete = itemView.findViewById(R.id.ic_delete);
            imgPhoto = itemView.findViewById(R.id.img_favourite);
        }
    }
}
