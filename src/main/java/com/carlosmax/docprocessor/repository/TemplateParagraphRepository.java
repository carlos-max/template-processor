package com.carlosmax.docprocessor.repository;

import com.carlosmax.docprocessor.domain.TemplateParagraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateParagraphRepository extends JpaRepository<TemplateParagraph, Long> {
}
