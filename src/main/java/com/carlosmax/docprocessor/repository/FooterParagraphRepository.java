package com.carlosmax.docprocessor.repository;

import com.carlosmax.docprocessor.domain.FooterParagraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FooterParagraphRepository extends JpaRepository<FooterParagraph, Long> {
}
