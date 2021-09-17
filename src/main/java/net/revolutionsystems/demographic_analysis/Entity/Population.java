package net.revolutionsystems.demographic_analysis.Entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Population")
public class Population {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long City_id;

    @Getter @Setter
    private String City_Name;

    @Getter @Setter
    private String County_Name;

    @Getter @Setter
    private String State_Name;

    @Getter @Setter
    private Double Latitude;

    @Getter @Setter
    private Double Longitude;

    @Getter @Setter
    private Double Population;

    @Getter @Setter
    private Double Pop_Density;

    @Getter @Setter
    private String Timezone;

}
