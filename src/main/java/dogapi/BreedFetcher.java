package dogapi;

import java.util.List;

/**
 * Interface for the service of getting sub breeds of a given dog breed.
 */
public interface BreedFetcher {

    /**
     * Fetch the list of sub breeds for the given breed.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist
     */
    // Must declare the checked exception in the signature
    List<String> getSubBreeds(String breed) throws BreedNotFoundException;


    // Must extend Exception instead of RuntimeException
    class BreedNotFoundException extends Exception {
        public BreedNotFoundException(String breed) {
            super("Breed not found: " + breed);
        }
    }
}