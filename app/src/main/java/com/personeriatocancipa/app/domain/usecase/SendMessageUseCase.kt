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

/**
 * UseCase que envía un mensaje al modelo Gemini (text-bison-001) y devuelve la respuesta.
 * Usa Moshi para serializar/deserializar JSON.
 */
class SendMessageUseCase(
    private val apiKey: String,
    private val client: OkHttpClient = OkHttpClient()
) {
    // Moshi builder with Kotlin reflection
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @JsonClass(generateAdapter = true)
    data class GenerateTextRequest(val prompt: Prompt) {
        @JsonClass(generateAdapter = true)
        data class Prompt(val text: String)
    }

    @JsonClass(generateAdapter = true)
    data class GenerateTextResponse(val candidates: List<Candidate>) {
        @JsonClass(generateAdapter = true)
        data class Candidate(val output: String)
    }

    suspend operator fun invoke(userMessage: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val url =
                    "https://generativelanguage.googleapis.com/v1beta2/models/text-bison-001:generateText?key=$apiKey"

                // build the request JSON
                val reqObj = GenerateTextRequest(
                    GenerateTextRequest.Prompt(text = userMessage)
                )
                val reqAdapter = moshi.adapter(GenerateTextRequest::class.java)
                val reqJson = reqAdapter.toJson(reqObj)
                val body = reqJson
                    .toRequestBody("application/json".toMediaType())

                // fire the HTTP call
                val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()

                client.newCall(request).execute().use { resp ->
                    if (!resp.isSuccessful) {
                        return@withContext Result.failure(Exception("Gemini API error ${resp.code}"))
                    }

                    // parse the response
                    val respStr = resp.body!!.string()
                    val respAdapter = moshi.adapter(GenerateTextResponse::class.java)
                    val parsed = respAdapter.fromJson(respStr)
                        ?: return@withContext Result.failure(Exception("Invalid JSON from Gemini"))
                    val output = parsed.candidates.firstOrNull()?.output
                        ?: return@withContext Result.failure(Exception("Empty response from model"))

                    Result.success(output)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
