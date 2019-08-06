package com.example.androidbooks;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class AndroidBooksGetterTask {
    public static final String LOG_TAG = AndroidBooksGetterTask.class.getSimpleName();

    /**
     * Query the USGS dataset and return an {@link AndroidBooksModel} object to represent a single earthquake.
     */
    public static ArrayList<AndroidBooksModel> fetchEarthquakeData(String requestUrl) {
        Log.i(LOG_TAG, "fetchEarthquakeData");

        // Create URL
        URL url = null;
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        url = createUrl(requestUrl);
        jsonResponse = makeHttpRequest(url);

        // Extract relevant fields from the JSON response and create an {@link AndroidBooksModel} object
        ArrayList<AndroidBooksModel> androidBooksModels = extractFeatureFromJson(jsonResponse);

        // Return the {@link AndroidBooksModel}
        return androidBooksModels;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) {

        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        //try to connect with server;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<AndroidBooksModel> extractFeatureFromJson(String jsonResponse) {


        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }


        ArrayList<AndroidBooksModel> androidBooksModels = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            if (root.getInt("totalItems") == 0) {
                return null;
            } else {
                JSONArray itemsArray = root.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {

                    String bookName, authorsString = "Not authors mentioned",
                            imageUrl = "", language, detailsLink, currency, price, currencyAndPrice = "";

                    JSONObject jsonObject = itemsArray.getJSONObject(i);

                    bookName = jsonObject.getJSONObject("volumeInfo").getString("title");

                    if (jsonObject.getJSONObject("volumeInfo").has("authors")) {
                        authorsString = jsonObject.getJSONObject("volumeInfo").getJSONArray("authors").getString(0);
                    }
                    if (jsonObject.getJSONObject("volumeInfo").has("imageLinks")) {
                        imageUrl = jsonObject.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail");
                    }

                    language = jsonObject.getJSONObject("volumeInfo").getString("language");
                    detailsLink = jsonObject.getJSONObject("volumeInfo").getString("infoLink");


                    if (jsonObject.getJSONObject("saleInfo").has("listPrice")) {

                        currency = jsonObject.getJSONObject("saleInfo").getJSONObject("listPrice").getString("currencyCode");
                        price = Double.toString(jsonObject.getJSONObject("saleInfo").getJSONObject("listPrice").getDouble("amount"));
                        currencyAndPrice = currency + " " + price;
                    }

                    androidBooksModels.add(new AndroidBooksModel(bookName, imageUrl, language, authorsString, currencyAndPrice, detailsLink));

                }
            }

            } catch(JSONException e){
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }

        return androidBooksModels;

    }


}
