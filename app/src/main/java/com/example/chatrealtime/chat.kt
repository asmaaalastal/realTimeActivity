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
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.message_item.*

class chat : AppCompatActivity() {
    private lateinit var messagesRef: DatabaseReference
    private lateinit var recyclerMessage: RecyclerView
    private lateinit var message: EditText
    private lateinit var send: Button
    private lateinit var senderUid: String
    private lateinit var receiverUid:String
    val messageList = mutableListOf<Message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        recyclerMessage = findViewById(R.id.recyclerView_message)
        message = findViewById(R.id.message_input)
        send = findViewById(R.id.btn_send)

        senderUid = intent.getStringExtra("id").toString()
        receiverUid = "Yeh0MqTJyXfyKWdr3BAohfn4GDz1"
        send.setOnClickListener {
            val messageText = message.text.toString().trim()
            Log.d("messageText", messageText)
            if (messageText.isNotEmpty()){
                messageSend(messageText)
                message.setText("")
            }
        }
        messagesRef = FirebaseDatabase.getInstance().getReference("chat")
        val messageAdapter = MessagesAdapter(this,messageList,senderUid)
        recyclerMessage.layoutManager = LinearLayoutManager(this)
        recyclerMessage.adapter = messageAdapter
        messagesRef.addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message!= null){
                    messageList.add(message)
                    Log.d("messageDisplay", "Success")
                    messageAdapter.notifyItemInserted(messageList.size -1)
                    recyclerMessage.scrollToPosition(messageList.size -1)
                }

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
        Log.e("Asmaa","1")
        FirebaseDatabase.getInstance().getReference("chat").push().setValue(message)
        Log.e("Asmaa","1")
        Log.d("messageSend", "Success")
    }
}