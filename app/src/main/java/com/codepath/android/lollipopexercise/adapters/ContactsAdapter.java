package com.codepath.android.lollipopexercise.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.android.lollipopexercise.R;
import com.codepath.android.lollipopexercise.activities.DetailsActivity;
import com.codepath.android.lollipopexercise.models.Contact;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

// Provide the underlying view for an individual list item.
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.VH> {
    private Activity mContext;
    private List<Contact> mContacts;

    public ContactsAdapter(Activity context, List<Contact> contacts) {
        mContext = context;
        if (contacts == null) {
            throw new IllegalArgumentException("contacts must not be null");
        }
        mContacts = contacts;
    }

    // Inflate the view based on the viewType provided.
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new VH(itemView, mContext);
    }

    // Display data at the specified position
    @Override
    public void onBindViewHolder(final VH holder, int position) {
        Contact contact = mContacts.get(position);
        holder.rootView.setTag(contact);
        holder.tvName.setText(contact.getName());

        //Defining a listener for image loading
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.ivProfile.setImageBitmap(bitmap);
                Palette p = Palette.from(bitmap).generate();
                if(p.getVibrantSwatch() != null) {
                    int color = p.getVibrantSwatch().getRgb();
                    holder.vPalette.setBackgroundColor(color);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        holder.ivProfile.setTag(target);

        Picasso.with(mContext).load(contact.getThumbnailDrawable()).into(target);
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    // Provide a reference to the views for each contact item
    public final static class VH extends RecyclerView.ViewHolder {
        final View rootView;
        final ImageView ivProfile;
        final TextView tvName;
        final View vPalette;

        public VH(final View itemView, final Context context) {
            super(itemView);
            rootView = itemView;
            ivProfile = (ImageView)itemView.findViewById(R.id.ivProfile);
            tvName = (TextView)itemView.findViewById(R.id.tvName);
            vPalette = itemView.findViewById(R.id.vPalette);

            // Navigate to contact details activity on click of card view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Contact contact = (Contact)v.getTag();
                    if (contact != null) {
                        // Fire an intent when a contact is selected
                        // Pass contact object in the bundle and populate details activity.
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra(DetailsActivity.EXTRA_CONTACT, contact);

                        Pair<View, String> p1 = Pair.create((View)ivProfile, "profile");
                        Pair<View, String> p2 = Pair.create(vPalette, "palette_transition");
                        Pair<View, String> p3 = Pair.create((View)tvName, "name_transition");

                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) context, p1, p2, p3);
                        context.startActivity(intent, options.toBundle());
                    }
                }
            });
        }
    }
}
