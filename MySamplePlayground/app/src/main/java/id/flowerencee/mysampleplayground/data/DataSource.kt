package id.flowerencee.mysampleplayground.data

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.flowerencee.mysampleplayground.R
import id.flowerencee.mysampleplayground.objects.CardData
import id.flowerencee.mysampleplayground.objects.CheckBoxData
import id.flowerencee.mysampleplayground.objects.Constants
import id.flowerencee.mysampleplayground.objects.FormData
import id.flowerencee.mysampleplayground.objects.GenericData
import id.flowerencee.mysampleplayground.objects.JsonData
import id.flowerencee.mysampleplayground.objects.Option
import id.flowerencee.mysampleplayground.objects.TextData
import java.io.InputStreamReader

class DataSource(private val context: Context) {
    fun getSampleData(): ArrayList<FormData> {
        val resourceId: Int = R.raw.source

        val gson = Gson()
        val jsonString = readJsonFromRaw(context, resourceId)
        val responseType = object : TypeToken<List<JsonData>>() {}.type
        val rawResponse: List<JsonData> = gson.fromJson(jsonString, responseType)
        val result = ArrayList<FormData>()
        rawResponse.forEach { jsonData ->
            val form = FormData()
            form.apply {
                id = jsonData.id
                label = jsonData.header.label
                subLabel = jsonData.header.subLabel
                type = jsonData.type
            }
            when (jsonData.type) {
                Constants.FORM_TYPE.TEXT -> {
                    val texts = ArrayList<TextData>()
                    (jsonData.data as ArrayList<GenericData>).forEach {
                        val textData = TextData()
                        textData.apply {
                            id = it.id
                            text = it.text
                            position = it.position
                            clickable = it.clickable
                        }
                        texts.add(textData)
                    }
                    form.texts = texts
                }

                Constants.FORM_TYPE.INPUT_TEXT -> {
                    val texts = ArrayList<TextData>()
                    (jsonData.data as ArrayList<GenericData>).forEach {
                        val textData = TextData()
                        textData.apply {
                            id = it.id
                            hint = it.hint
                            inputType = it.inputType
                            limit = it.limit
                        }
                        texts.add(textData)
                    }
                    form.texts = texts
                }

                Constants.FORM_TYPE.WEB_VIEW -> form.webViews =
                    (jsonData.values as ArrayList<String>)

                Constants.FORM_TYPE.CHECK_BOX -> {
                    val checkBoxes = ArrayList<CheckBoxData>()
                    (jsonData.option as ArrayList<Option>).forEach {
                        val checkBox = CheckBoxData()
                        checkBox.apply {
                            id = it.id
                            label = it.label
                        }
                        checkBoxes.add(checkBox)
                    }
                    form.checkboxes = checkBoxes
                }

                Constants.FORM_TYPE.CARDS, Constants.FORM_TYPE.SELECTABLE, Constants.FORM_TYPE.QUANTITY -> {
                    val cards = ArrayList<CardData>()
                    (jsonData.data as ArrayList<GenericData>).forEach {
                        val card = CardData()
                        card.apply {
                            id = it.id
                            title = it.title
                            description = it.description
                            imageUrl = it.imageUrl
                            position = it.position
                            price = it.price
                            limit = it.limit
                        }
                        cards.add(card)
                    }
                    form.cards = cards
                }
            }
            result.add(form)
        }
        return result
    }

    private fun readJsonFromRaw(context: Context, resourceId: Int): String {
        val resources: Resources = context.resources
        val inputStream = resources.openRawResource(resourceId)

        return try {
            val reader = InputStreamReader(inputStream)

            val jsonString = reader.readText()
            jsonString
        } catch (e: Exception) {
            e.printStackTrace()
            inputStream.close()
            ""
        } finally {
            inputStream.close()
        }
    }
}