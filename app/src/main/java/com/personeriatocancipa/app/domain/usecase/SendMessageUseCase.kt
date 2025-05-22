// app/src/main/java/com/personeriatocancipa/app/domain/usecase/SendMessageUseCase.kt
package com.personeriatocancipa.app.domain.usecase

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class SendMessageUseCase(
    private val apiKey: String,
    private val client: OkHttpClient = OkHttpClient()
) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // 1) Nuevas clases de datos
    @JsonClass(generateAdapter = true)
    data class GenerateContentRequest(val contents: List<Content>) {
        @JsonClass(generateAdapter = true)
        data class Content(val role: String, val parts: List<Part>)
        @JsonClass(generateAdapter = true)
        data class Part(val text: String)
    }

    @JsonClass(generateAdapter = true)
    data class GenerateContentResponse(val candidates: List<Candidate>) {
        @JsonClass(generateAdapter = true)
        data class Candidate(val content: Content, val finishReason: String?) {
            @JsonClass(generateAdapter = true)
            data class Content(val parts: List<GenerateContentRequest.Part>, val role: String)
        }
    }

    suspend operator fun invoke(userMessage: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                // 2) Nuevo endpoint
                val url =
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"

                // 3) Montar el body con la nueva estructura
                val reqObj = GenerateContentRequest(
                    listOf(
                        GenerateContentRequest.Content(
                            role = "user",
                            parts = listOf(GenerateContentRequest.Part(text = userMessage))
                        )
                    )
                )
                val reqAdapter = moshi.adapter(GenerateContentRequest::class.java)
                val reqJson = reqAdapter.toJson(reqObj)
                val body = reqJson.toRequestBody("application/json".toMediaType())

                // 4) Llamada HTTP
                val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()

                client.newCall(request).execute().use { resp ->
                    if (!resp.isSuccessful) {
                        return@withContext Result.failure(Exception("Gemini API error ${resp.code}"))
                    }

                    // 5) Parsear respuesta con la nueva clase
                    val respStr = resp.body!!.string()
                    val respAdapter = moshi.adapter(GenerateContentResponse::class.java)
                    val parsed = respAdapter.fromJson(respStr)
                        ?: return@withContext Result.failure(Exception("Invalid JSON from Gemini"))
                    val output = parsed.candidates
                        .firstOrNull()
                        ?.content
                        ?.parts
                        ?.firstOrNull()
                        ?.text
                        ?: return@withContext Result.failure(Exception("Empty response from model"))

                    Result.success(output)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
