import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Inheritance: OpenWeatherMapProvider IS-A WeatherProvider
public class OpenWeatherMapProvider extends WeatherProvider {

    // ✅ Your FREE API key here
    private static final String API_KEY = "383702fee7ace43edf89ffd70d3dbc85";

    private static final String BASE_URL =
            "https://api.openweathermap.org/data/2.5/weather";

    @Override
    public WeatherData getWeatherFor(String city, String countryCode) {
        String formattedCountry = formatCountryCode(countryCode);

        try {
            String query = city;
            if (formattedCountry != null && !formattedCountry.isEmpty()) {
                query = city + "," + formattedCountry;
            }

            String urlStr = BASE_URL
                    + "?q=" + query.replace(" ", "%20")
                    + "&units=metric"
                    + "&appid=" + API_KEY;

            String json = sendGetRequest(urlStr);

            // Check for error like city not found
            if (json.contains("\"cod\":\"404\"") || json.contains("\"cod\":404")) {
                return new WeatherData(city, formattedCountry, 0, 0, 0, 0,
                        "N/A", false, "City not found or invalid country code.");
            }

            return parseWeatherData(json);

        } catch (Exception e) {
            return new WeatherData(city, formattedCountry, 0, 0, 0, 0,
                    "N/A", false, "Error connecting to weather service: " + e.getMessage());
        }
    }

    // Sends HTTP GET request and returns body as string
    private String sendGetRequest(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        int status = con.getResponseCode();

        BufferedReader in;
        if (status >= 200 && status <= 299) {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        con.disconnect();

        return content.toString();
    }

    // Very simple JSON parsing for OpenWeather response
    private WeatherData parseWeatherData(String json) {
        String cityName = extractString(json, "\"name\":\"", "\"");
        String country = extractString(json, "\"country\":\"", "\"");

        double temp = extractDouble(json, "\"temp\":");
        double feelsLike = extractDouble(json, "\"feels_like\":");
        int humidity = (int) extractDouble(json, "\"humidity\":");
        double windSpeed = extractDouble(json, "\"speed\":");

        // weather[0].description
        String condition = extractString(json, "\"description\":\"", "\"");

        return new WeatherData(
                cityName,
                country,
                round(temp),
                round(feelsLike),
                humidity,
                round(windSpeed),
                capitalizeWords(condition),
                true,
                null
        );
    }

    // Small helper to extract double after a key
    private double extractDouble(String json, String key) {
        int index = json.indexOf(key);
        if (index == -1) return 0.0;

        index += key.length();
        StringBuilder sb = new StringBuilder();

        while (index < json.length()) {
            char c = json.charAt(index);
            if ((c >= '0' && c <= '9') || c == '.' || c == '-') {
                sb.append(c);
            } else {
                break;
            }
            index++;
        }

        try {
            return Double.parseDouble(sb.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // Helper to extract string between startKey and endChar
    private String extractString(String json, String startKey, String endChar) {
        int start = json.indexOf(startKey);
        if (start == -1) return "N/A";

        start += startKey.length();
        int end = json.indexOf(endChar, start);
        if (end == -1) return "N/A";

        return json.substring(start, end);
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    // Capitalize each word of condition like "light rain" -> "Light Rain"
    private String capitalizeWords(String text) {
        if (text == null || text.isEmpty() || text.equals("N/A")) {
            return text;
        }
        String[] parts = text.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.length() > 0) {
                sb.append(Character.toUpperCase(p.charAt(0)))
                  .append(p.substring(1))
                  .append(" ");
            }
        }
        return sb.toString().trim();
    }
}
