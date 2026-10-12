package com.example.sqliteapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etContact, etDob;
    private Button btnAdd, btnUpdate, btnArchive;
    private CheckBox chkShowArchived;
    private ListView listRecords;

    private DatabaseHelper DB;
    private final List<UserRecord> records = new ArrayList<>();
    private ArrayAdapter<UserRecord> adapter;
    private UserRecord selected = null; // the record currently chosen in the list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etContact = findViewById(R.id.etContact);
        etDob = findViewById(R.id.etDob);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnArchive = findViewById(R.id.btnArchive);
        chkShowArchived = findViewById(R.id.chkShowArchived);
        listRecords = findViewById(R.id.listRecords);

        DB = new DatabaseHelper(this);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1, records);
        listRecords.setAdapter(adapter);

        refreshList();

        // ADD
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                if (name.isEmpty()) {
                    toast("Enter a name");
                    return;
                }
                boolean ok = DB.insertuserdata(name,
                        etContact.getText().toString().trim(),
                        etDob.getText().toString().trim());
                toast(ok ? "New entry added" : "Entry not added");
                if (ok) refreshList();
            }
        });

        // UPDATE the selected record
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == null) {
                    toast("Tap a record in the list first");
                    return;
                }
                String name = etName.getText().toString().trim();
                if (name.isEmpty()) {
                    toast("Enter a name");
                    return;
                }
                boolean ok = DB.updateuserdata(selected.id, name,
                        etContact.getText().toString().trim(),
                        etDob.getText().toString().trim());
                toast(ok ? "Entry updated" : "Entry not updated");
                if (ok) refreshList();
            }
        });

        // ARCHIVE / RESTORE the selected record (nothing is deleted)
        btnArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == null) {
                    toast("Tap a record in the list first");
                    return;
                }
                boolean archiveIt = !selected.archived;
                boolean ok = DB.setArchived(selected.id, archiveIt);
                toast(ok ? (archiveIt ? "Entry archived" : "Entry restored")
                        : "Action failed");
                if (ok) refreshList();
            }
        });

        // Tap a record: load it into the fields
        listRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = records.get(position);
                etName.setText(selected.name);
                etContact.setText(selected.contact);
                etDob.setText(selected.dob);
                btnArchive.setText(selected.archived ? "RESTORE" : "ARCHIVE");
            }
        });

        // Long-press: permanent delete (with confirmation)
        listRecords.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final UserRecord rec = records.get(position);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete permanently?")
                        .setMessage(rec.name + " will be removed for good.\n"
                                + "Use ARCHIVE if you may need it later.")
                        .setPositiveButton("Delete", (d, w) -> {
                            if (DB.deletePermanently(rec.id)) {
                                toast("Entry deleted");
                                refreshList();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
        });

        // Show / hide archived records
        chkShowArchived.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                refreshList();
            }
        });
    }

    // Reload the list from the database and reset the form
    private void refreshList() {
        records.clear();
        records.addAll(DB.getAll(chkShowArchived.isChecked()));
        adapter.notifyDataSetChanged();

        selected = null;
        listRecords.clearChoices();
        etName.setText("");
        etContact.setText("");
        etDob.setText("");
        btnArchive.setText("ARCHIVE");
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}