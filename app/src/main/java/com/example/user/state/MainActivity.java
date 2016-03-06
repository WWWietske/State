package com.example.user.lists;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.user.state.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

// TODO listview met fragment voor de todos gebruiken? Dan kan je dat deel gewoon steeds hergebruiken ofzo.

/**
 * Wietske Dotinga - 10781889
 * Todolist app with one screen. Todos can be added and can later be deleted with a longclick.
 * Using ArrayAdapter to add and delete todos from the listview. Arraylist is saved with
 * prinstream in a text file.
 */
public class MainActivity extends AppCompatActivity {

    EditText todoEdit;
    ListView todoListview;
    Button addButton;

    ArrayList<String> todoArraylist;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a new ArrayList upon opening the app
        todoArraylist = new ArrayList<>();

        // Try to open the saved todos and fill the arraylist with it
        open();

        // Initialize listview, edittext and button
        todoListview = (ListView) findViewById(R.id.todoListview);
        todoEdit = (EditText) findViewById(R.id.todoEdit);
        addButton = (Button) findViewById(R.id.addButton);

        // Initialize adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoArraylist);
        todoListview.setAdapter(adapter);

        // Set onclicklistener for the button
        addButton.setOnClickListener(buttonClicked);

        // OnItemLongClickListener to delete item by clicking on it
        todoListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Delete the item from the arraylist
                todoArraylist.remove(position);
                adapter.notifyDataSetChanged();
                write();
                return true;
            }
        });
    }

    View.OnClickListener buttonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Add the text from the edittext into the arraylist of todos
            String todoText = todoEdit.getText().toString();
            todoArraylist.add(todoText);
            adapter.notifyDataSetChanged();
            todoEdit.setText("");
            write();
        }
    };


    // Open saved file if any
    public void open(){

        // Try to open the file
        try {
            Scanner scan = new Scanner(openFileInput("todolist.txt"));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                todoArraylist.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Write the arraylist items to file
    public void write(){

        // Try to create and write to the file
        try {
            PrintStream out = new PrintStream(openFileOutput("todolist.txt", MODE_PRIVATE));

            // Write every line to the file
            for (int i = 0; i < todoArraylist.size(); i++) {
                out.println(todoArraylist.get(i));
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}