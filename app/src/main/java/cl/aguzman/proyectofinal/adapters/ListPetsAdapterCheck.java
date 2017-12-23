package cl.aguzman.proyectofinal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.CallVetCallback;
import cl.aguzman.proyectofinal.models.MedicalHistory;

public class ListPetsAdapterCheck extends FirebaseRecyclerAdapter<MedicalHistory, ListPetsAdapterCheck.PetsCheckViewholder> {

    private CallVetCallback callback;
    private ArrayList arrayList = new ArrayList();

    public ListPetsAdapterCheck(CallVetCallback callback, Query ref) {
        super(MedicalHistory.class, R.layout.item_list_pet_check, PetsCheckViewholder.class, ref);
        this.callback = callback;
    }

    @Override
    protected void populateViewHolder(final PetsCheckViewholder viewHolder, MedicalHistory model, int position) {
        CheckBox check = viewHolder.checkBox;
        check.setText(model.getNamePet().toUpperCase());
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                int pos = viewHolder.getAdapterPosition();
                final String name = getItem(pos).getNamePet();
                String key = getItem(pos).getKey();
                Query query = new Queries().getDates().child(new CurrentUser().getCurrentUid()).child(key).orderByKey().limitToLast(1);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String description = ds.child("descriptionMedical").getValue().toString();
                            if (isChecked) {
                                arrayList.add(name);
                                arrayList.add(description);
                            } else {
                                arrayList.remove(name);
                                arrayList.remove(description);
                            }

                            callback.infoPets(arrayList);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    public static class PetsCheckViewholder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public PetsCheckViewholder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkPetCb);
        }
    }
}
