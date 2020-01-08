package com.traore.abasse.tafsir.application.ui.tools;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traore.abasse.tafsir.application.R;

public class ToolsFragment extends Fragment {


    private EditText name,prenom,ville,pays,telephone,mail,commentaire;
    private Button valide;
    private Tool tool;
    private ProgressBar progressBar;


    DatabaseReference database ;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        initialisation(root);

        return root;
    }

    //methode initiation des composants
    private void initialisation(View root) {

        commentaire = root.findViewById(R.id.enregistrement_commentaire);
        name = root.findViewById(R.id.enregistrement_nom);
        prenom = root.findViewById(R.id.enregistrement_prenom);
        ville = root.findViewById(R.id.enregistrement_ville);
        pays = root.findViewById(R.id.enregistrement_pays);
        telephone = root.findViewById(R.id.enregistrement_telephone);
        mail = root.findViewById(R.id.enregistrement_mail);
        valide = root.findViewById(R.id.btn_enregistrement_valide);
        valide.setOnClickListener(valider);

        progressBar =root.findViewById(R.id.tool_progressbar);
        progressBar.setVisibility(View.GONE);


    }

    //methode de click du boutton valider
    private View.OnClickListener valider = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            instanceTool();
        }
    };

    //methode instanciation de tool
    private void instanceTool()
    {
        progressBar.setVisibility(View.VISIBLE);
        database = FirebaseDatabase.getInstance().getReference();
        String tname = name.getText().toString();
        String tprenom = prenom.getText().toString();
        String tville = ville.getText().toString();
        String tpays = pays.getText().toString();
        String tTelephone = telephone.getText().toString();
        String tmail = mail.getText().toString();
        String tcommentaire = commentaire.getText().toString();

        tool = new Tool(tprenom,tname,tville,tpays,tmail,tTelephone,tcommentaire);
        String key = database.push().getKey();
        database.child("users").child(key).setValue(tool).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                name.setText("");
                prenom.setText("");
                ville.setText("");
                pays.setText("");
                telephone.setText("");
                mail.setText("");
                commentaire.setText("");

            }
        });

    }


}