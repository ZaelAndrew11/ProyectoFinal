package cl.aguzman.proyectofinal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.models.MedicalHistory;

public class ListMedicalHistoryAdapter extends FirebaseRecyclerAdapter<MedicalHistory, ListMedicalHistoryAdapter.MedicalHistoryViewholder>{


    public ListMedicalHistoryAdapter(Query ref) {
        super(MedicalHistory.class, R.layout.item_list_medical_history, MedicalHistoryViewholder.class, ref);
    }


    @Override
    protected void populateViewHolder(MedicalHistoryViewholder viewHolder, MedicalHistory model, int position) {
        viewHolder.descriptionIte.setText(model.getDescriptionMedical());
        viewHolder.dayItem.setText(String.valueOf(model.getDay()));
        viewHolder.monthItem.setText(String.valueOf(model.getMonth()));
        viewHolder.yearItem.setText(String.valueOf(model.getYear()));
    }


    public static class MedicalHistoryViewholder extends RecyclerView.ViewHolder {

        TextView descriptionIte;
        TextView dayItem;
        TextView monthItem;
        TextView yearItem;

        public MedicalHistoryViewholder(View itemView) {
            super(itemView);
            descriptionIte = (TextView) itemView.findViewById(R.id.descriptionItemTv);
            dayItem = (TextView) itemView.findViewById(R.id.dayItemTv);
            monthItem = (TextView) itemView.findViewById(R.id.monthItemTv);
            yearItem = (TextView) itemView.findViewById(R.id.yearItemTv);
        }
    }

}
