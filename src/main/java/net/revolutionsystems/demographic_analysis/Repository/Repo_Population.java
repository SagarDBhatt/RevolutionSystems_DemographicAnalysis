package net.revolutionsystems.demographic_analysis.Repository;

import net.revolutionsystems.demographic_analysis.Entity.Population;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Repo_Population extends JpaRepository<Population,Long> {

}
