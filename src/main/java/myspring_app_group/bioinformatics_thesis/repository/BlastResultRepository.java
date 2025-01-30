package myspring_app_group.bioinformatics_thesis.repository;

import myspring_app_group.bioinformatics_thesis.model.BlastResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlastResultRepository extends JpaRepository<BlastResult, Long> {
}