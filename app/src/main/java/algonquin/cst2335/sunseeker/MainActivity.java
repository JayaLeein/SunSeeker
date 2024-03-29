package algonquin.cst2335.sunseeker;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText latitudeEditText, longitudeEditText;
    private TextView sunriseTextView, sunsetTextView;
    private RequestQueue requestQueue;
    private final String API_URL = "https://api.sunrise-sunset.org/json?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        sunriseTextView = findViewById(R.id.sunriseTextView);
        sunsetTextView = findViewById(R.id.sunsetTextView);
        requestQueue = Volley.newRequestQueue(this);

        findViewById(R.id.lookupButton).setOnClickListener(view -> {
            String latitude = latitudeEditText.getText().toString();
            String longitude = longitudeEditText.getText().toString();

            // 构造 API 请求 URL
            String url = API_URL + "lat=" + latitude + "&lng=" + longitude;

            // 发送 API 请求
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject results = response.getJSONObject("results");
                                String sunrise = results.getString("sunrise");
                                String sunset = results.getString("sunset");

                                // 使用 SimpleDateFormat 格式化时间
                                SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
                                SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                sunrise = outputFormat.format(inputFormat.parse(sunrise));
                                sunset = outputFormat.format(inputFormat.parse(sunset));

                                // 更新 UI 显示日出和日落时间
                                sunriseTextView.setText("Sunrise: " + sunrise);
                                sunsetTextView.setText("Sunset: " + sunset);
                            } catch (JSONException | java.text.ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("API Error", "Error occurred while fetching data: " + error.getMessage());
                    Toast.makeText(MainActivity.this, "Error occurred while fetching data", Toast.LENGTH_SHORT).show();
                }
            });

            // 将请求添加到请求队列
            requestQueue.add(request);
        });
    }
}
