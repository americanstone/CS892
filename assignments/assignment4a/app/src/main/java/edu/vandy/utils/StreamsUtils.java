package edu.vandy.utils;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Helpful methods for manipulating various Java 8 Streams features.
 */
public class StreamsUtils {
    /**
     * A utility class should always define a private constructor.
     */
    private StreamsUtils() {
    }

    /**
     * Submits all @a callables in the Collection to the @a executor.
     * This method returns immediately and does not block the caller.
     *
     * @return A list of futures to all the submitted callables.
     */
    public static List<Future<?>> submitAll
            (ExecutorService executor,
             Collection<? extends Callable<?>> callables) {
        // TODO -- you fill in here.
        return callables.stream().map( c -> executor.submit(c)).collect(Collectors.toList());
    }

    /**
     * Create a CompletableFuture that when completed will convert all
     * the completed CompletableFutures in the @a futures parameter
     * into a list of results.
     * @param futures A list of completable futures.
     * @return A CompletableFuture containing a List with all the joined results.
     */
    public static <T> CompletableFuture<List<T>> joinAll(List<CompletableFuture<T>> futures) {
        // Obtain a CompletableFuture that will be complete when all
        // of the futures have completed.
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));

        // When all the futures have completed return a list of the
        // joined elements.
        return allDoneFuture.thenApply(v ->
                                       futures
                                       .stream()
                                       .map(CompletableFuture::join)
                                       .collect(toList()));
    }

    /**
     * Maps the values of an Enum type to a corresponding array of
     * Strings.
     */
    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays
            .stream(e.getEnumConstants())
            .map(Enum::name)
            .toArray(String[]::new);
    }

    /**
     * A generic negation predicate that can be used to negate the
     * return value of urlCached (used by Collection.filter() calls).
     *
     * @return The negation of the input predicate.
     */
    public static<T> Predicate<T> not(Predicate<T> p) {
        return p.negate();
    }
}
