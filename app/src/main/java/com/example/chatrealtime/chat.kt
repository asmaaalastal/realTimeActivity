package com.example.chatrealtime

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.health.UidHealthStats
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatrealtime.adapter.Message
import com.example.chatrealtime.adapter.MessagesAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.message_item.*

class chat : AppCompatActivity() {
    private lateinit var recyclerMessage: RecyclerView
    private lateinit var message: EditText
    private lateinit var send: Button
    private lateinit var senderUid: String
    private lateinit var receiverUid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        recyclerMessage = recyclerView_message
        message = message_input
        send = btn_send
        senderUid = "Yeh0MqTJyXfyKWdr3BAohfn4GDz1"
        receiverUid = "jjcMjL53XbXA9wiUqmoUCAoWkuI3"
        send.setOnClickListener {
            val messageText = message.text.toString().trim()
            if (messageText.isNotEmpty()){
                messageSend(messageText)
                message.setText("")
            }
        }
        val messageList = mutableListOf<Message>()
        val messageAdapter = MessagesAdapter(this,messageList,senderUid)
        recyclerMessage.layoutManager = LinearLayoutManager(this)
        recyclerMessage.adapter = messageAdapter
        FirebaseDatabase.getInstance().getReference("chat").addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
//                val time = snapshot.getValue(Message::class.java)
                if (message!= null){
                    messageList.add(message)
                    Log.d("messageDisplay", "Success")
//                    if (time != null) {
//                        messageList.add(time)
//                    }
                }
                messageAdapter.notifyItemInserted(messageList.size -1)
                recyclerMessage.scrollToPosition(messageList.size -1)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
    private fun messageSend(messageText:String){
        val timestamp = System.currentTimeMillis()
        val message = Message(messageText,senderUid,receiverUid,timestamp)
        FirebaseDatabase.getInstance().getReference("chat").push().setValue(message)
        Log.d("messageSend", "Success")
    }
}