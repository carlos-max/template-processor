package com.carlosmax.docprocessor.repository;

import com.carlosmax.docprocessor.domain.Header;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeaderRepository extends JpaRepository<Header, Long> {
}
