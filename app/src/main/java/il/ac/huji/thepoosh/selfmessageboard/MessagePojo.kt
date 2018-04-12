@file:JvmName("MessagePojo")

package il.ac.huji.thepoosh.selfmessageboard

import org.json.JSONObject

data class MessagePojo(val nameOfAuthor: String, val content: String, val timestamp: Long) {

    fun toJsonObject() :JSONObject{
        val output= JSONObject()
        output.put(KEY_NAME, nameOfAuthor)
        output.put(KEY_CONTENT, content)
        output.put(KEY_TIMESTAMP, timestamp)
        return output
    }
    companion object {
        val KEY_NAME = "name"
        val KEY_CONTENT = "content"
        val KEY_TIMESTAMP = "timestamp"

        fun fromJsonObject(input: JSONObject): MessagePojo {
            return MessagePojo(
                    input.getString(KEY_NAME),
                    input.getString(KEY_CONTENT),
                    input.getLong(KEY_TIMESTAMP)
            )
        }
    }
}
