package com.dhiyaulhaqza.tododo.data;

import android.content.Context;
import android.util.Log;

import com.dhiyaulhaqza.tododo.model.Todo;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by dhiyaulhaqza on 7/29/17.
 */

public class DataTransaction {

    private Realm realm;

    public DataTransaction(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public RealmResults<Todo> readAllTodo() {
        return realm.where(Todo.class).findAll().sort("id", Sort.DESCENDING);
    }

    public void updateTodo(Todo todo) {
        realm.beginTransaction();
        realm.insertOrUpdate(todo);
        realm.commitTransaction();
        Log.d("TAG", "update");
    }

    public void deleteTodo(Todo todo) {
        realm.beginTransaction();
        todo.deleteFromRealm();
        realm.commitTransaction();
    }

    public int getUniqueId(int updatedId) {
        int uniqueId = 0;
        if (!(readAllTodo().size() == 0)) {
            uniqueId = realm.where(Todo.class).max("id").intValue() + 1;
            if (updatedId != -1)    {
                uniqueId = updatedId;
            }
        }
        return uniqueId;
    }
}
