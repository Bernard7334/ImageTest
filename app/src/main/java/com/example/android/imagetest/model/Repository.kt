package com.example.android.imagetest.model

import android.content.Context
import android.util.MalformedJsonException
import com.example.android.imagetest.R
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder

class Repository {
    //@Throws(ex) - lazy way
    fun deSerialize(context: Context): List<MovieResponse>{
        try {
            // the block of code that could trigger an un-checked Exception
            val jsonFileIS = context.resources.openRawResource(R.raw.movie_gist)
            val jsonString = convertIStoString(jsonFileIS)
            val movieResponseResult = convertStringtoDataClass(jsonString)
            return movieResponseResult
        }
        catch (ex: Exception){
            //catch the error to prevent runtime crashes
            ex.printStackTrace()
            return emptyList()
        }
        finally {
            // it will always be executed,(unless you call system.exit) for cleanup
            //close sockets or databse connecitons for example
            println("Finally will be always invoked.")
        }

    }//deserializee

    private fun convertIStoString(inputStream: InputStream): String{
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line = reader.readLine()
        val result = StringBuilder()
        while(line != null){
            result.append(line)
            line = reader.readLine()
        }
        return result.toString()
    }

    private fun convertStringtoDataClass(inputString: String):List<MovieResponse>{
        val jsonArray = JSONArray(inputString)
        val result = mutableListOf<MovieResponse>()
        for (i in 0 until jsonArray.length()){
            val elementJSONObject = jsonArray.getJSONObject(i)
            val movieResponse = MovieResponse(
                elementJSONObject.getString("title"),
                elementJSONObject.getString("image"),
                elementJSONObject.getDouble("rating"),
                elementJSONObject.getInt("releaseYear"),
                elementJSONObject.getJSONArray("genre").toList()
            )
            result.add(movieResponse)
        }
        return result
    }

    //extension function
    //fun TARGEETNAME . funName(optionals):Optionals
    private fun JSONArray.toList(): List<String>{
        val result = mutableListOf<String>()
        for (i in 0 until this.length()){
            result.add(
                this.getString(i)
            )
        }
        return result
    }

}