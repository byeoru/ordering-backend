package com.byeoru.ordering_server

class FcmMessage(val validate_only: Boolean,
                 val message: Message) {

    class Message(val data: Data,
                  val token: String)

    class Data(val title: String,
               val body: String,
               val channel_id: String)
}