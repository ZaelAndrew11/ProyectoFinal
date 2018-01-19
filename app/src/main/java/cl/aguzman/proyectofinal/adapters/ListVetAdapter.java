package cl.aguzman.proyectofinal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.interfaces.GetContentCallback;
import cl.aguzman.proyectofinal.models.Vet;

public class ListVetAdapter extends FirebaseRecyclerAdapter<Vet, ListVetAdapter.VetHolder>{

    private GetContentCallback callback;

    public ListVetAdapter(GetContentCallback callback, Query ref) {
        super(Vet.class, R.layout.item_list_vet, VetHolder.class, ref);
        this.callback = callback;
    }

    @Override
    protected void populateViewHolder(final VetHolder viewHolder, Vet model, int position) {
        String getName = model.getName();
        ImageView vetImage = viewHolder.itemLogoVet;
        LinearLayout item = viewHolder.itemVet;
        if (model.getImage().equals("")){
            Picasso.with(vetImage.getContext()).load(R.mipmap.placeholder_icon).into(viewHolder.itemLogoVet);
        }else {
            Picasso.with(vetImage.getContext()).load(model.getImage()).into(viewHolder.itemLogoVet);
        }
        viewHolder.itemNameVet.setText(getName.substring(0,1).toUpperCase()+getName.substring(1));
        viewHolder.itemScoreVet.setText(String.valueOf(model.getScore()));


        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                String uid = getItem(pos).getUid();
                String key = getItem(pos).getKey();
                callback.getDetail(uid, key);
            }
        });
    }

    public void updateList(){
        notifyDataSetChanged();
    }


    public static class VetHolder extends RecyclerView.ViewHolder{
        ImageView itemLogoVet;
        TextView itemNameVet;
        TextView itemScoreVet;
        LinearLayout itemVet;
        public VetHolder(View itemView) {
            super(itemView);
            itemVet = (LinearLayout) itemView;
            itemLogoVet = (ImageView) itemView.findViewById(R.id.imageItemVet);
            itemNameVet = (TextView) itemView.findViewById(R.id.nameItemVet);
            itemScoreVet = (TextView) itemView.findViewById(R.id.scoreItemVet);
        }
    }
}
