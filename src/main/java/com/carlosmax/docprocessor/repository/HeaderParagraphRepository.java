package com.carlosmax.docprocessor.repository;

import com.carlosmax.docprocessor.domain.HeaderParagraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeaderParagraphRepository extends JpaRepository<HeaderParagraph, Long> {
}
