package com.dhiyaulhaqza.tododo.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.dhiyaulhaqza.tododo.R;
import com.dhiyaulhaqza.tododo.data.DataTransaction;
import com.dhiyaulhaqza.tododo.databinding.FragmentTodoEditorBinding;
import com.dhiyaulhaqza.tododo.model.Todo;

/**
 * Created by dhiyaulhaqza on 7/29/17.
 */

public class TodoEditorDialog extends DialogFragment {

    private FragmentTodoEditorBinding binding;
    private Context context;

    private DataTransaction dataTransaction;
    private int updatedId = -1;
    String title = "Add Todo";

    private Todo todo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(binding.getRoot());

        setupTodoIfExist();

        builder.setTitle(title)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveTodo();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        return builder.show();
    }

    private void setupTodoIfExist() {
        todo = new Todo();
        Todo managedTodo;
        if (getArguments() != null) {
            managedTodo = (Todo) getArguments().getSerializable("update");
            if (managedTodo != null) {
                updatedId = managedTodo.getId();
                todo.setId(updatedId);
                todo.setTitle(managedTodo.getTitle());
                todo.setDescription(managedTodo.getDescription());
                writeUpdateTodo();
            }
            title = "Edit Todo";
        } else {
            todo.setId(dataTransaction.getUniqueId(updatedId));
        }
    }

    private void writeUpdateTodo() {
        binding.etEditorTitle.setText(todo.getTitle());
        binding.etEditorDescription.setText(todo.getDescription());
    }

    private void saveTodo() {
        String title = binding.etEditorTitle.getText().toString().trim();
        String description = binding.etEditorDescription.getText().toString().trim();

        todo.setTitle(title);
        todo.setDescription(description);

        dataTransaction.updateTodo(todo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_todo_editor, null, false);
        dataTransaction = new DataTransaction(context);
    }
}
