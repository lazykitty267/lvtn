package bk.lvtn;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bk.lvtn.fragment_adapter.ReportAdapter;
import bk.lvtn.fragment_adapter.Template;
import bk.lvtn.fragment_adapter.TemplateAdapter;
import dataService.DataService;
import dataService.DatabaseConnection;
import entity.Report;
import entity.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerActivity extends Fragment {


    public ManagerActivity() {
        // Required empty public constructor
    }
    ArrayList<Report> arrRp ;
    ReportAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_manager, container, false);
        //get user reports
        DatabaseConnection databaseConnection = new DatabaseConnection();
        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
        DataService dataService = new DataService();
        User user = dataService.getCurrentUser(getActivity());
        arrRp = new ArrayList<>();
        databaseReference.child(user.getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Report report = postSnapshot.getValue(Report.class);
                    arrRp.add(report);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ListView listView = (ListView)view.findViewById(R.id.list_user_reports);
        adapter = new ReportAdapter(getActivity(), arrRp,R.layout.item_inlist_report);
        listView.setAdapter(adapter);
        return view;
    }

}
