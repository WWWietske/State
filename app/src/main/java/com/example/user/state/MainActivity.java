package com.example.user.state;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.state.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Wietske Dotinga - 10781889
 * Todolist app with multiple lists of todos. The first screen shows the lists of todos and
 * allows the user to add a new list. The ListOverview activity shows a todolist when openend
 * by the user.
 */
public class MainActivity extends AppCompatActivity {

    EditText todoEdit;
    ListView todoListview;
    Button addButton;

    ArrayList<String> listsArraylist;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a new ArrayList upon opening the app
        listsArraylist = new ArrayList<>();

        // Try to open the saved todos and fill the arraylist with it
        open();

        // Initialize listview, edittext and button
        todoListview = (ListView) findViewById(R.id.todoListview);
        todoEdit = (EditText) findViewById(R.id.todoEdit);
        addButton = (Button) findViewById(R.id.addButton);

        // Initialize adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listsArraylist);
        todoListview.setAdapter(adapter);

        // Set onclicklistener for the button
        addButton.setOnClickListener(buttonClicked);

        listener();
    }

    View.OnClickListener buttonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Add the text from the edittext into the arraylist of todos
            String nameText = todoEdit.getText().toString();
            listsArraylist.add(nameText);
            adapter.notifyDataSetChanged();
            todoEdit.setText("");
            write();

            // Pass the name of the todolist to the other activity using intent
            Intent intent = new Intent(MainActivity.this, ListOverview.class);
            intent.putExtra("listName", nameText);
            startActivity(intent);
        }
    };

    /**
     * Open saved file of list names if any
     */
    public void open(){

        // Try to open the file
        try {
            Scanner scan = new Scanner(openFileInput("lists.txt"));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                listsArraylist.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the names of the lists to file
     */
    public void write(){

        // Try to create and write to the file
        try {
            PrintStream out = new PrintStream(openFileOutput("lists.txt", MODE_PRIVATE));

            // Write every line to the file
            for (int i = 0; i < listsArraylist.size(); i++) {
                out.println(listsArraylist.get(i));
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Listener function that contains on item click listener for the listview elements.
     */
    public void listener(){

        // OnItemLongClickListener to delete item by clicking on it
        todoListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Find out what list was clicked
                String todoListName = listsArraylist.get(position);

                // Change title of list into a .txt file title
                assert todoListName != null;
                String todoTitle = todoListName.replace(" ", "_") + ".txt";

                // Find file and delete it
                File dir = getFilesDir();
                File file = new File(dir, todoTitle);
                boolean deleted = file.delete();

                // Delete the item from the arraylist
                listsArraylist.remove(position);
                adapter.notifyDataSetChanged();
                write();

                return true;
            }
        });

        todoListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Find out what list was clicked
                String todoListName = listsArraylist.get(position);

                // Pass the name of the todolist to the other activity using intent
                Intent intent = new Intent(MainActivity.this, ListOverview.class);
                intent.putExtra("listName", todoListName);
                startActivity(intent);
            }
        });
    }
}