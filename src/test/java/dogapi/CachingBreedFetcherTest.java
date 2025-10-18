package dogapi;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CachingBreedFetcherTest {

    // Fix 1: Add throws declaration
    @Test
    void testCachingAvoidsRedundantCalls() throws BreedFetcher.BreedNotFoundException {
        BreedFetcherForLocalTesting mock = new BreedFetcherForLocalTesting();
        CachingBreedFetcher cachingFetcher = new CachingBreedFetcher(mock);

        List<String> firstCall = cachingFetcher.getSubBreeds("hound");
        List<String> secondCall = cachingFetcher.getSubBreeds("hound");

        assertEquals(List.of("afghan", "basset"), firstCall);
        assertEquals(firstCall, secondCall);
        assertEquals(1, mock.getCallCount(), "Fetcher should only be called once due to caching");
    }

    // Fix 2: Add throws declaration (even though it uses assertThrows)
    @Test
    void testExceptionStillPropagates() throws BreedFetcher.BreedNotFoundException {
        BreedFetcherForLocalTesting mock = new BreedFetcherForLocalTesting();
        CachingBreedFetcher cachingFetcher = new CachingBreedFetcher(mock);

        assertThrows(BreedFetcher.BreedNotFoundException.class, () -> cachingFetcher.getSubBreeds("dragon"));
        assertEquals(1, mock.getCallCount(), "Fetcher should be called even if breed is invalid");
    }

    // Fix 3: Add throws declaration (even though it uses assertThrows)
    @Test
    void testExceptionRepeatsCalls() throws BreedFetcher.BreedNotFoundException {
        BreedFetcherForLocalTesting mock = new BreedFetcherForLocalTesting();
        CachingBreedFetcher cachingFetcher = new CachingBreedFetcher(mock);

        assertThrows(BreedFetcher.BreedNotFoundException.class, () -> cachingFetcher.getSubBreeds("dragon"));
        assertThrows(BreedFetcher.BreedNotFoundException.class, () -> cachingFetcher.getSubBreeds("dragon"));
        assertEquals(2, mock.getCallCount(), "Fetcher should be called again even if breed is invalid");
    }

    @Test
    void testCachingAvoidsRedundantCallsCheckCallsMade() throws BreedFetcher.BreedNotFoundException {
        BreedFetcherForLocalTesting mock = new BreedFetcherForLocalTesting();
        CachingBreedFetcher cachingFetcher = new CachingBreedFetcher(mock);

        cachingFetcher.getSubBreeds("hound");
        cachingFetcher.getSubBreeds("hound");

        assertEquals(1, cachingFetcher.getCallsMade(),
                "Fetcher should only be called once due to caching. " +
                        "Make sure that your implementation is recording how many calls have been made!");
    }

    @Test
    void testExceptionStillPropagatesCheckCallsMade() {
        BreedFetcherForLocalTesting mock = new BreedFetcherForLocalTesting();
        CachingBreedFetcher cachingFetcher = new CachingBreedFetcher(mock);

        // This test only needs assertThrows, which handles the exception internally.
        assertThrows(BreedFetcher.BreedNotFoundException.class, () -> cachingFetcher.getSubBreeds("dragon"));
        assertEquals(1, cachingFetcher.getCallsMade(),
                "Fetcher should be called even if breed is invalid. " +
                        "Make sure that your implementation is recording how many calls have been made!");
    }

    @Test
    void testExceptionRepeatsCallsCheckCallsMade() {
        BreedFetcherForLocalTesting mock = new BreedFetcherForLocalTesting();
        CachingBreedFetcher cachingFetcher = new CachingBreedFetcher(mock);

        // This test only needs assertThrows, which handles the exception internally.
        assertThrows(BreedFetcher.BreedNotFoundException.class, () -> cachingFetcher.getSubBreeds("dragon"));
        assertThrows(BreedFetcher.BreedNotFoundException.class, () -> cachingFetcher.getSubBreeds("dragon"));
        assertEquals(2, cachingFetcher.getCallsMade(),
                "Fetcher should be called again even if breed is invalid. " +
                        "Make sure that your implementation is recording how many calls have been made!");
    }
}
