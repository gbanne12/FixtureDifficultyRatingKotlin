package data.model

import com.squareup.moshi.Moshi
import exception.NoFplResponseException
import fpl.url.FantasyEndpoint
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets

class ElementsDao {

    val elements: List<Element>
        @Throws(NoFplResponseException::class)
        get() {
            try {
                val jsonBinder = Moshi.Builder().build()
                val elementAdapter = jsonBinder.adapter(Element::class.java)
                val jsonString: String = IOUtils.toString(URL(FantasyEndpoint.STATIC.url), StandardCharsets.UTF_8)
                val elementsArray = JSONObject(jsonString).getJSONArray("elements")
                var elements: MutableList<Element> = ArrayList()
                for (i in 0 until elementsArray.length()) {
                    val element = elementAdapter.fromJson(elementsArray.get(i).toString())
                    if (element != null) {
                        elements.add(element)
                    }
                }
                return elements
            } catch (e: IOException) {
                throw NoFplResponseException(e.message, e)
            }
        }
}
