package com.example.user.state;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.state.ListOverview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

// TODO listview met fragment voor de todos gebruiken? Dan kan je dat deel gewoon steeds hergebruiken ofzo.

/**
 * Wietske Dotinga - 10781889
 * This activity displays the to do list that was selected by the user. The user selects a list
 * in the mainactivity.
 */
public class ListOverview extends AppCompatActivity {

    EditText todoEdit;
    ListView todoListview;
    Button addButton;

    ArrayList<String> todoArraylist;
    ArrayAdapter adapter;
    String todoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_overview);

        // Set a new ArrayList upon opening the app
        todoArraylist = new ArrayList<>();

        // Get extras
        Bundle extras = getIntent().getExtras();
        String title = extras.getString("listName");

        // Change title of list into a .txt file title
        assert title != null;
        todoTitle = title.replace(" ", "_") + ".txt";

        // Try to open the saved todos and fill the arraylist with it
        open(todoTitle);

        // Initialize listview, edittext and button
        todoListview = (ListView) findViewById(R.id.todoListview);
        todoEdit = (EditText) findViewById(R.id.todoEdit);
        addButton = (Button) findViewById(R.id.addButton);

        // Initialize adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoArraylist);
        todoListview.setAdapter(adapter);

        // Set onclicklistener for the button
        addButton.setOnClickListener(buttonClicked);

        // Call the listener function with the onclicklisteners for the listview
        listener();
    }

    View.OnClickListener buttonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Add the text from the edittext into the arraylist of todos
            String todoText = todoEdit.getText().toString();
            todoArraylist.add(todoText);
            adapter.notifyDataSetChanged();
            todoEdit.setText("");
            write(todoTitle);
        }
    };


    /**
     * Open file if any exist, otherwise create a new one
     */
    public void open(String listName){

        // Try to open the file
        try {
            Scanner scan = new Scanner(openFileInput(listName));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                todoArraylist.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the arraylist items to the .txt file
     */
    public void write(String listName){

        // Try to create and write to the file
        try {
            PrintStream out = new PrintStream(openFileOutput(listName, MODE_PRIVATE));

            // Write every line to the file
            for (int i = 0; i < todoArraylist.size(); i++) {
                out.println(todoArraylist.get(i));
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function for an onclicklistener on the listview elements
     */
    public void listener(){

        // OnItemLongClickListener to delete item by clicking on it
        todoListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Delete the item from the arraylist
                todoArraylist.remove(position);
                adapter.notifyDataSetChanged();
                write(todoTitle);
                return true;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Store text from the edittext
        String editText = todoEdit.getText().toString();
        outState.putString("edit", editText);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Receive text from the edittext
        String editText = savedInstanceState.getString("edit");
        todoEdit.setText(editText);
    }
}