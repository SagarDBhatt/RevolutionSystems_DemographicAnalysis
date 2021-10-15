package net.revolutionsystems.demographic_analysis.Repository;

import net.revolutionsystems.demographic_analysis.Entity.Population;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface Repo_Population extends JpaRepository<Population,Long> {

    public String qry_findByCityAndState = "  SELECT * \n" +
            "  FROM [USPopulation].[dbo].[Population]\n" +
            "  WHERE City_Name = :city AND State_Name = :state";

    public String qry_findByState = "  SELECT * \n" +
            "  FROM [USPopulation].[dbo].[Population]\n" +
            "  WHERE State_Name = :state";

    public String qry_findAllCities = "  SELECT * \n" +
            "  FROM [USPopulation].[dbo].[Population]";

    @Transactional
    @Modifying
    @Query(value = qry_findByCityAndState, nativeQuery = true)
    public List<Map<String,Object>> findByCity_NameAndState_Name(String city, String state);

    @Modifying
    @Transactional
    @Query(value = qry_findByState, nativeQuery = true)
    public List<Map<String,Object>> findByState(String state);

    @Modifying
    @Transactional
    @Query(value = qry_findAllCities, nativeQuery = true)
    public List<Map<String,Object>> findAllCities();

}
