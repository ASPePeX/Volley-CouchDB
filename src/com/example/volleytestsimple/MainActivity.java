package com.example.volleytestsimple;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends Activity {

	private TextView txtDisplay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtDisplay = (TextView) findViewById(R.id.txtDisplay);

		final ArrayList<String> indexList = new ArrayList<String>();

		final RequestQueue queue = Volley.newRequestQueue(this);

		final StringBuilder output = new StringBuilder();
		
		ImageLoader mImageLoader;
		

		String url = "http://141.28.122.106:5984/fufloma/_all_docs";
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						try {
							JSONArray jsonArray = response.getJSONArray("rows");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject childJSONObject = jsonArray
										.getJSONObject(i);
								indexList.add(childJSONObject.getString("id"));
							}

							// Second Request stuff
							String baseURL = "http://141.28.122.106:5984/fufloma/";
							for (String id : indexList) {

								JsonObjectRequest jsObjRequest = new JsonObjectRequest(
										Request.Method.GET, baseURL + id, null,
										new Response.Listener<JSONObject>() {

											@Override
											public void onResponse(
													JSONObject response) {
												// TODO Auto-generated method
												// stub
												output.append("Response => "
														+ response.toString()
														+ "\r\n\r\n");
												findViewById(R.id.progressBar1)
														.setVisibility(
																View.GONE);
												txtDisplay.setText(output
														.toString());
											}

										}, new Response.ErrorListener() {

											@Override
											public void onErrorResponse(
													VolleyError error) {
												// TODO Auto-generated method
												// stub

											}
										});

								queue.add(jsObjRequest);

							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
						findViewById(R.id.progressBar1)
								.setVisibility(View.GONE);
						Log.i("VolleyTestSimple", "Here we go!");
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.e("VolleyTestSimple", "Didn't get shit!");
					}
				});

		queue.add(jsObjRequest);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
