package com.carlosmax.docprocessor.repository;

import com.carlosmax.docprocessor.domain.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Template getByName(String templateName);
}
