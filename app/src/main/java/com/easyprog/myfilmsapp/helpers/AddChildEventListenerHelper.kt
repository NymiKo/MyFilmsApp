package com.easyprog.myfilmsapp.helpers

import android.util.Log
import android.widget.Toast
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class AddChildEventListenerHelper(val onSuccess:(DataSnapshot) -> Unit): ChildEventListener {
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        onSuccess(snapshot)
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        Log.e("CHANGE", "Ссылка была изменена")
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        TODO("Not yet implemented")
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        TODO("Not yet implemented")
    }

    override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
    }
}