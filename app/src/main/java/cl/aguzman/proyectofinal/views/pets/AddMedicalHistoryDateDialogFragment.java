package cl.aguzman.proyectofinal.views.pets;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.models.MedicalHistory;

public class AddMedicalHistoryDateDialogFragment extends DialogFragment {

    DatePicker date;
    EditText description;
    Button addMedicalHistory;
    DatabaseReference datesRef = new Queries().getDates();
    String key;

    public AddMedicalHistoryDateDialogFragment() {
    }

    public static AddMedicalHistoryDateDialogFragment newInstance(String key){
        AddMedicalHistoryDateDialogFragment medicalHistory = new AddMedicalHistoryDateDialogFragment();
        Bundle args = new Bundle();
        args.putString("key", key);
        medicalHistory.setArguments(args);
        return medicalHistory;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key = getArguments().getString("key");
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_medical_history_date, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        date = (DatePicker) view.findViewById(R.id.dateHmDp);
        description = (EditText) view.findViewById(R.id.descriptionHmEt);
        addMedicalHistory = (Button) view.findViewById(R.id.addMedicalHistoryBtn);

        addMedicalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int editTextDescription = description.getText().length();
                String textDescription = description.getText().toString();

                int day = date.getDayOfMonth();
                int month = date.getMonth();
                int year = date.getYear();

                MedicalHistory medicalHistoryDates = new MedicalHistory();
                medicalHistoryDates.setDay(day);
                medicalHistoryDates.setMonth(month);
                medicalHistoryDates.setYear(year);
                medicalHistoryDates.setDescriptionMedical(textDescription);

                if(editTextDescription > 10){
                    datesRef.child(new CurrentUser().getCurrentUid()).child(key).push().setValue(medicalHistoryDates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            getDialog().dismiss();
                        }
                    });
                }
            }
        });
    }
}
