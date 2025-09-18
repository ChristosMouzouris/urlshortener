package io.github.christosmouzouris.urlshortener.repository;

import io.github.christosmouzouris.urlshortener.model.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
}
