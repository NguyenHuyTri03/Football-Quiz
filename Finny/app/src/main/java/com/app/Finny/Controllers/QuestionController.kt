package com.app.Finny.Controllers

import com.app.Finny.Models.QuestionModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await



class QuestionController {
    // Create a firestore instance
    private val db = FirebaseFirestore.getInstance()

    suspend fun getAllByDifficulty(difficulty: String): List<QuestionModel> {
        val questions: List<QuestionModel>

        val documents = db.collection("${difficulty}_questions").get().await()

        questions = documents.toObjects<QuestionModel>()

        return questions
    }

    suspend fun getAll(): List<QuestionModel> {
        val questions = mutableListOf<QuestionModel>()
        val difficulty = mutableListOf<String>()
        difficulty.add("easy")
        difficulty.add("medium")
        difficulty.add("expert")

        difficulty.forEach { diff ->
            val documents = db.collection("${diff}_questions").get().await()
            val questionList: List<QuestionModel> = documents.toObjects<QuestionModel>()

            questionList.forEach { question ->
                questions.add(question)
            }
        }

//        println(questions)

        return questions
    }

//    fun getOne(id: String) {
//
//    }
//
//    fun update(question: QuestionModel) {
//
//    }
//
    suspend fun delete(difficulty: String, id: String) {
        db.collection("${difficulty}_questions").document(id).delete()
    }






    /*** DEPRECATED , DO NOT RUN
     *

    // callback example
    fun getAllByDifficulty(difficulty: String, callback: (res: List<QuestionModel>) -> Unit) {
        var questions = mutableListOf<QuestionModel>()

        db.collection("${difficulty}_questions").get()
            .addOnSuccessListener { documents ->
            var i = 0
            for(document in documents) {
                val data = document.data
                val option_list: List<String> = data.get("options") as List<String>

                val question = QuestionModel(document.id, data.get("image_url").toString(), data.get("question").toString(), option_list, data.get("correct").toString())
                questions.add(question)
            }

            callback.invoke(questions)
        }
    }

    // Get link from GG Drive and format them to bitmap strings

    // Imports for encode/decode Bitmap to String
    import java.io.ByteArrayOutputStream
    import java.net.URL
    import java.util.concurrent.Executors
    import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base64
    import android.graphics.Bitmap
    import android.graphics.BitmapFactory

    suspend fun updatePicturesURL() {
        val rawLinks = "https://drive.google.com/file/d/1MJ1_8ZkNGOGoYwWb454PaW4Nw4SgmdE6/view?usp=sharing, https://drive.google.com/file/d/1dXtjBp-oOrt9nacvEj0OO4LWGGKRp41R/view?usp=sharing, https://drive.google.com/file/d/1yVqAthnDnXgVAOv59hjaF-iuzkjDhYxq/view?usp=sharing, https://drive.google.com/file/d/1b1uIdRbgpvWeD3em4CFcEFOBLEuSRxbX/view?usp=sharing, https://drive.google.com/file/d/1uhpDg1QAxCtuuGIMotAVeWUvC99uJ1b-/view?usp=sharing, https://drive.google.com/file/d/1loeZYDye2SzOIsC1j9G36TEWgvclN55P/view?usp=sharing, https://drive.google.com/file/d/100wQ4KHghac8j51LHnwI1Cha7amAV71F/view?usp=sharing, https://drive.google.com/file/d/1x6LpUuu-SDC-exAj0d2eNRAMhlF6Wtzu/view?usp=sharing, https://drive.google.com/file/d/1TElZ5y1Fg3-MZxpikUEDWxz6W5LqftGX/view?usp=sharing, https://drive.google.com/file/d/1cKRM5tL8WITYvieM1BcIUObqmM9qAh8c/view?usp=sharing, https://drive.google.com/file/d/1zz7bsxxWQgtCxuPwhu35moAluZGjO_WJ/view?usp=sharing, https://drive.google.com/file/d/169cduCDB8gBSLYJQN3KOzcSdcVDj_yzQ/view?usp=sharing, https://drive.google.com/file/d/1ZgclGDWLoZUcrIWRR_cZt-dvkE2mhP4G/view?usp=sharing, https://drive.google.com/file/d/1YJJNYZM4iLgezgr7NFek57-VS4yGYdPM/view?usp=sharing, https://drive.google.com/file/d/1b9MSSgetsApeuGaTf-_TOk4zS8aCrhFN/view?usp=sharing"
        val links: List<String> = rawLinks.split(", ")
        val processedLinks: MutableList<String> = mutableListOf()

        links.forEach { img ->
            processedLinks.add("https://drive.google.com/uc?export=view&id=${img.split("/")[5]}")
        }

        val questionRef = db.collection("medium_questions")
        val documents = questionRef.get().await()
        val list: List<QuestionModel>

        list = documents.toObjects(QuestionModel::class.java)

        var i = 0
        list.forEach { question ->
            val imgURL = processedLinks[i]
            var image:Bitmap?
            val executor = Executors.newSingleThreadExecutor()

            executor.execute {
                val `in` = URL(imgURL).openStream()
                image = BitmapFactory.decodeStream(`in`)

                questionRef.document(question.id)
                    .update("image_url", encodeImage(image))
                    .addOnSuccessListener {
                        println("${question.id} -> success")
                    }
                    .addOnFailureListener { err ->
                        println(err)
                    }
            }
        }
    }

    private fun encodeImage(bm: Bitmap?): String? {
        val baos = ByteArrayOutputStream()
        bm?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeBase64String(b)
    }

     ***/
}