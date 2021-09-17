package net.revolutionsystems.demographic_analysis.Repository;

import net.revolutionsystems.demographic_analysis.Entity.city_Population;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface Repo_Population extends JpaRepository<city_Population,Long> {

}
