package io.github.christosmouzouris.urlshortener.exception;

public class UrlUpdateFailedException extends RuntimeException {
    public UrlUpdateFailedException(String identifier) {
        super("Url: " + identifier + " was found but some concurrent operation or DB " +
                "prevented the update");
    }
}
