package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();
    private static final String API_URL_BASE = "https://dog.ceo/api/breed/";

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String url = API_URL_BASE + breed.toLowerCase() + "/list";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.code() == 404) {
                // The API returns 404 with a specific message for "Breed not found"
                throw new BreedNotFoundException(breed);
            }

            // Check for general API/network errors and treat them as BreedNotFoundException
            if (!response.isSuccessful()) {
                throw new BreedNotFoundException(breed + " (API call failed with code " + response.code() + ")");
            }

            // Parse the JSON response body
            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            String status = json.getString("status");

            if ("error".equals(status)) {
                // If the status is 'error' for any reason other than 404 (e.g., specific error in body), treat as not found
                throw new BreedNotFoundException(breed + " (" + json.getString("message") + ")");
            }

            JSONArray messageArray = json.getJSONArray("message");
            List<String> subBreeds = new ArrayList<>();
            for (int i = 0; i < messageArray.length(); i++) {
                subBreeds.add(messageArray.getString(i));
            }

            return subBreeds;

        } catch (IOException e) {
            // Treat I/O and network errors as BreedNotFoundException as per documentation
            throw new BreedNotFoundException(breed + " (Network/IO Error: " + e.getMessage() + ")");
        }
    }
}