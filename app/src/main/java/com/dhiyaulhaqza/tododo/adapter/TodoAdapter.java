package com.dhiyaulhaqza.tododo.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhiyaulhaqza.tododo.R;
import com.dhiyaulhaqza.tododo.databinding.ListItemBinding;
import com.dhiyaulhaqza.tododo.model.Todo;

import java.util.List;

/**
 * Created by dhiyaulhaqza on 7/29/17.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<Todo> todos;
    private ListItemBinding binding;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemViewCallback callback = (ItemViewCallback) context;
                    callback.onItemViewClickListener(todos.get(getAdapterPosition()));
                }
            });
        }
    }

    public TodoAdapter(List<Todo> todos) {
        this.todos = todos;
    }

    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        context = parent.getContext();
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(binding.getRoot());
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Todo todo = todos.get(position);
        if (todo != null) {

            binding.tvItemTitle.setText(todo.getTitle());
            binding.tvItemDescription.setText(todo.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }
}