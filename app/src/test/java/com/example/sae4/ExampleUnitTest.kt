package com.example.sae4

import android.content.Context
import android.util.Pair
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//@RunWith(AndroidJUnit4::class)
class RequestApiTest {

    @Mock
    private lateinit var context: Context

    private lateinit var requestApi: RequestApi
    lateinit var bouttonPosition : ImageButton
    lateinit var bouttonRecherche: ImageButton
    lateinit var rechercheEdit: EditText

    private lateinit var nomVille : TextView

    private val position = Position(context, nomVille,bouttonPosition,bouttonRecherche,rechercheEdit)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        requestApi = RequestApi(context, position)
    }

    /*@Test
    fun testRequest() {
        val queueMock = mock(RequestQueue::class.java)
        val imageViewMock = mock(ImageView::class.java)
        val temperatureMock = mock(TextView::class.java)
        val windParHourMock = mock(TextView::class.java)
        val response = """
            {
                "info": {
                    "latitude": 50.1,
                    "longitude": 4.5,
                    "generationtime_ms": 50.1,
                    "utc_offset_seconds": 0,
                    "timezone": "Europe/Brussels",
                    "timezone_abbreviation": "CET",
                    "elevation": 0.0,
                    "hourly_units": {
                        "time": "utc",
                        "temperature_2m": "C",
                        "weathercode": "",
                        "windspeed_10m": "kmh"
                    },
                    "hourly": {
                        "time": [
                            "2023-04-08 00:00:00+00:00",
                            "2023-04-08 01:00:00+00:00"
                        ],
                        "temperature_2m": [
                            10.0,
                            9.9
                        ],
                        "weathercode": [
                            0,
                            1
                        ],
                        "windspeed_10m": [
                            5.0,
                            5.5
                        ]
                    }
                }
            }
        """.trimIndent()

        `when`(queueMock.add(any(StringRequest::class.java)))
            .thenAnswer { invocation ->
                val request = invocation.arguments[0] as StringRequest
                assertNotNull(request)
                assertEquals(Request.Method.GET, request.method)
                assertEquals("http://10.0.2.2:8000/weather/hourly?latitude=0.0&longitude=0.0&start_date=2023-04-07&end_date=2023-04-07", request.url)
                assertNotNull(request.headers)
                assertNotNull(request.body)
                request.parseNetworkResponse(NetworkResponse(response.toByteArray()))
            }

        `when`(context.getString(R.string.error_request)).thenReturn("Error")

        requestApi.request(queueMock, imageViewMock, temperatureMock, windParHourMock)

        assertNotNull(requestApi.rootHourly)
        assertEquals(50.1, requestApi.rootHourly!!.info.latitude, 0.0)
        assertEquals(4.5, requestApi.rootHourly!!.info.longitude, 0.0)
        assertEquals(2, requestApi.rootHourly!!.info.hourly.time.size)
        assertEquals("2023-04-08 00:00:00+00:00", requestApi.rootHourly!!.info.hourly.time
    }*/

    @Test
    fun testPosition() {
        requestApi.setNewLocalisation(47.0,37.0)
        assertEquals(47.0,requestApi.getLatitude())
        assertEquals(37.0,requestApi.getLongitude())
    }

    @Test
    fun testGetCity() {
        requestApi.requestLocCity("Nantes")
        assertEquals(47.0, requestApi.getLatitude())
    }
}