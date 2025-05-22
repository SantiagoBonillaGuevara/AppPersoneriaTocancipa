package com.personeriatocancipa.app.data.datasource

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject

class GeminiDataSource(private val apiKey: String) {

    private val client = OkHttpClient()

    suspend fun sendMessageToGemini(message: String): Result<String> {
        return try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=$apiKey"

            val requestBody = JSONObject().apply {
                put("contents", listOf(
                    mapOf("role" to "user", "parts" to listOf(mapOf("text" to message)))
                ))
                put("generationConfig", mapOf(
                    "temperature" to 0.7,
                    "topK" to 1,
                    "topP" to 1,
                    "maxOutputTokens" to 256
                ))
            }

            val request = Request.Builder()
                .url(url)
                .post(RequestBody.create("application/json".toMediaTypeOrNull(), requestBody.toString()))
                .build()

            val response = client.newCall(request).execute()
            val body = response.body?.string()

            if (response.isSuccessful && body != null) {
                val text = JSONObject(body)
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONArray("content")
                    .getJSONArray(0)
                    .getJSONObject(0)
                    .getString("text")

                Result.success(text)
            } else {
                Result.failure(Exception("Error: ${response.code} - $body"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
