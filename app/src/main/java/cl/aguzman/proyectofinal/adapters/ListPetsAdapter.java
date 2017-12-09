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
import cl.aguzman.proyectofinal.interfaces.GetMedicalHistory;
import cl.aguzman.proyectofinal.models.MedicalHistory;

public class ListPetsAdapter extends FirebaseRecyclerAdapter<MedicalHistory, ListPetsAdapter.PetHolder>{

    private GetMedicalHistory callback;

    public ListPetsAdapter(GetMedicalHistory callback, Query ref) {
        super(MedicalHistory.class, R.layout.item_list_pet, PetHolder.class, ref);
        this.callback = callback;
    }

    @Override
    protected void populateViewHolder(final PetHolder viewHolder, MedicalHistory model, int position) {
        LinearLayout item = viewHolder.itemPetLl;
        Picasso.with(viewHolder.imgPetIv.getContext()).load(model.getPhotoPet()).into(viewHolder.imgPetIv);
        viewHolder.namePetTv.setText(model.getNamePet());
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int auxPosition = viewHolder.getAdapterPosition();
                String namePet = getItem(auxPosition).getNamePet();
                String photoPetUrl = getItem(auxPosition).getPhotoPet();
                callback.getMedicalHistory(namePet, photoPetUrl);
            }
        });
    }

    public static class PetHolder extends RecyclerView.ViewHolder {
        LinearLayout itemPetLl;
        ImageView imgPetIv;
        TextView namePetTv;
        public PetHolder(View itemView) {
            super(itemView);
            itemPetLl = (LinearLayout) itemView.findViewById(R.id.itemPetLl);
            imgPetIv = (ImageView) itemView.findViewById(R.id.photoPetIv);
            namePetTv = (TextView) itemView.findViewById(R.id.namePetTv);
        }
    }
}
