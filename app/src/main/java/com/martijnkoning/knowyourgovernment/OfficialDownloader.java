package com.martijnkoning.knowyourgovernment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OfficialDownloader extends AsyncTask<String, Integer, String> {

    private static final String URL1 = "https://www.googleapis.com/civicinfo/v2/representatives?key=";
    private static final String URL2 = "&address=";
    private static final String KEY = "AIzaSyADWCvlkNPsF14ipeDYdtInX-sRAce9PJ8";

    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;

    private static final String TAG = "OfficialDownloader";

    OfficialDownloader(MainActivity ma) {
        mainActivity = ma;
    }

    private List<Official> officialList = new ArrayList<>();

    @Override
    protected void onPostExecute(String s) {
        String address = parseJSONAddress(s);
        parseJSONOffices(s);
        mainActivity.createList(address, officialList);
    }

    @Override
    protected String doInBackground(String... strings) {
        Uri dataUri = Uri.parse(URL1 + KEY + URL2 + strings[0]);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "doInBackground: ResponseCode: " + conn.getResponseCode());

            if (conn.getResponseCode() == 404)
                return null;

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        return sb.toString();
    }

    private String parseJSONAddress(String s) {

        try {
            JSONObject jOfficial = new JSONObject(s);
            String locCity = jOfficial.getJSONObject("normalizedInput").getString("city");
            String locState = jOfficial.getJSONObject("normalizedInput").getString("state");
            String locZip = jOfficial.getJSONObject("normalizedInput").getString("zip");

            Log.d(TAG, "parseJSON: " + locCity + ", " + locState + " " + locZip);

            return locCity + ", " + locState + " " + locZip;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void parseJSONOffices(String s) {
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray offices = jObjMain.getJSONArray("offices");

            for (int i = 0; i < offices.length(); i++) {
                JSONObject jOffice = (JSONObject) offices.get(i);
                String office = jOffice.getString("name");
                JSONArray indices = jOffice.getJSONArray("officialIndices");

                for (int j = 0; j < indices.length(); j++) {
                    officialList.add(new Official(office));
                }
                Log.d(TAG, "parseJSONOffices: " + office + " " + indices);
            }
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray officials = jObjMain.getJSONArray("officials");

            for (int i = 0; i < officialList.size(); i++) {
                JSONObject jOfficial = (JSONObject) officials.get(i);

                String name = jOfficial.getString("name");
                officialList.get(i).setName(name);

                if (jOfficial.has("address")) {
                    JSONArray addresses = jOfficial.getJSONArray("address");
                    JSONObject address = (JSONObject) addresses.get(0);

                    String line1, line2, line3;
                    if (address.has("line1")) {
                        line1 = address.getString("line1");
                        officialList.get(i).setAddress(line1);
                        if (address.has("line2")) {
                            line2 = address.getString("line2");
                            officialList.get(i).setAddress(line1 + "\n" + line2);
                            if (address.has("line3")) {
                                line3 = address.getString("line3");
                                officialList.get(i).setAddress(line1 + "\n" + line2+ "\n" + line3);
                            }
                        }
                    }
                    if (address.has("city")) {
                        String city = address.getString("city");
                        officialList.get(i).setCity(city);
                    }
                    if (address.has("state")) {
                        String state = address.getString("state");
                        officialList.get(i).setState(state);
                    }
                    if (address.has("zip")) {
                        String zip = address.getString("zip");
                        officialList.get(i).setZip(zip);
                    }

                }
                if (jOfficial.has("party")) {
                    String party = jOfficial.getString("party");
                    officialList.get(i).setParty(party);
                }
                if (jOfficial.has("phones")) {
                    JSONArray phones = jOfficial.getJSONArray("phones");
                    String phone = phones.get(0).toString();
                    officialList.get(i).setPhone(phone);
                }
                if (jOfficial.has("urls")) {
                    JSONArray urls = jOfficial.getJSONArray("urls");
                    String url = urls.get(0).toString();
                    officialList.get(i).setUrl(url);
                }
                if (jOfficial.has("emails")) {
                    JSONArray emails = jOfficial.getJSONArray("emails");
                    String email = emails.get(0).toString();
                    officialList.get(i).setEmail(email);
                }
                if (jOfficial.has("photoUrl")) {
                    String photoUrl = jOfficial.getString("photoUrl");
                    officialList.get(i).setPhoto(photoUrl);
                }

                if (jOfficial.has("channels")) {
                    JSONArray channels = jOfficial.getJSONArray("channels");
                    for (int j = 0; j < channels.length(); j++){
                        JSONObject channel = (JSONObject) channels.get(j);
                        String type = channel.getString("type");
                        String id = channel.getString("id");
                        if (type.equalsIgnoreCase("googleplus")) {
                            officialList.get(i).setGoogle(id);
                        }
                        else if (type.equalsIgnoreCase("facebook")) {
                            officialList.get(i).setFacebook(id);
                        }
                        else if (type.equalsIgnoreCase("twitter")) {
                            officialList.get(i).setTwitter(id);
                        }
                        else if (type.equalsIgnoreCase("youtube")) {
                            officialList.get(i).setYoutube(id);
                        }
                    }
                }

            }
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
