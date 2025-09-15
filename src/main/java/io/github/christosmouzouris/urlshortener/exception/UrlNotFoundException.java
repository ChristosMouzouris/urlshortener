package io.github.christosmouzouris.urlshortener.exception;

public class UrlNotFoundException extends RuntimeException{
    public UrlNotFoundException(String identifier){
        super("Url not found: " + identifier);
    }
}
