package com.dhiyaulhaqza.tododo.view;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.dhiyaulhaqza.tododo.adapter.ItemViewCallback;
import com.dhiyaulhaqza.tododo.R;
import com.dhiyaulhaqza.tododo.adapter.TodoAdapter;
import com.dhiyaulhaqza.tododo.data.DataTransaction;
import com.dhiyaulhaqza.tododo.databinding.ActivityMainBinding;
import com.dhiyaulhaqza.tododo.model.Todo;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements ItemViewCallback {

    private ActivityMainBinding binding;
    private TodoAdapter todoAdapter;
    private List<Todo> todoList = new ArrayList<>();
    private DataTransaction dataTransaction;
    private RealmResults<Todo> todoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        dataTransaction = new DataTransaction(this);
        setupRecyclerView();

        todoResult = dataTransaction.readAllTodo();
        todoResult.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Todo>>() {
            @Override
            public void onChange(RealmResults<Todo> todos, OrderedCollectionChangeSet changeSet) {
                // kadang data ke update tp view nya engga ~_~ jd sementara bikin adapter baru tiap update
                updateData(todos);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                dataTransaction.deleteTodo(todoList.get(position));
            }
        }).attachToRecyclerView(binding.rvMain);
    }

    private void updateData(List<Todo> todos) {
        todoAdapter = new TodoAdapter(todoList);
        binding.rvMain.setAdapter(todoAdapter);
        todoList.clear();
        todoList.addAll(todos);
        todoAdapter.notifyDataSetChanged();

        if (todos.size() == 0)  {
            binding.tvMainEmptyData.setVisibility(View.VISIBLE);
        } else {
            binding.tvMainEmptyData.setVisibility(View.INVISIBLE);
        }
//        todoResult = dataTransaction.readAllTodo();
//        todoAdapter = new TodoAdapter(todoList);
//        binding.rvMain.setAdapter(todoAdapter);
//        todoAdapter.updateTodoList(todoResult);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData(dataTransaction.readAllTodo());
    }

    private void setupRecyclerView() {
        binding.rvMain.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvMain.setLayoutManager(layoutManager);

        todoAdapter = new TodoAdapter(todoList);
        binding.rvMain.setAdapter(todoAdapter);
    }

    public void addTodo(View view) {
        DialogFragment todoEditorDialog = new TodoEditorDialog();
        todoEditorDialog.show(getFragmentManager(), "add");
    }

    @Override
    public void onItemViewClickListener(Todo todo) {
        DialogFragment todoEditorDialog = new TodoEditorDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("update", todo);
        todoEditorDialog.setArguments(bundle);
        todoEditorDialog.show(getFragmentManager(), "update");
        Log.d("TAG", todo.getId() + " id");
    }
}
