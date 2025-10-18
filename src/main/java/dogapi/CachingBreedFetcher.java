package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher fetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // 1. Check the cache
        if (cache.containsKey(breed.toLowerCase())) {
            return cache.get(breed.toLowerCase());
        }

        // 2. Not in cache, so call the underlying fetcher and increment call count
        callsMade++;
        List<String> result = fetcher.getSubBreeds(breed);

        // 3. Cache the successful result (cache key should be lower case for consistency)
        cache.put(breed.toLowerCase(), result);

        return result;
    }

    public int getCallsMade() {
        return callsMade;
    }
}