package com.englishstudy.englishstudy.repository;

import com.englishstudy.englishstudy.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}