package com.carlosmax.docprocessor.repository;

import com.carlosmax.docprocessor.domain.Footer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FooterRepository extends JpaRepository<Footer, Long> {
}
