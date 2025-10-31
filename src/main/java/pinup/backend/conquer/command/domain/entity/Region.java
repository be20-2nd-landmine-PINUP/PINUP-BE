package pinup.backend.conquer.command.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "region")
public class Region {

    @Id
    @Column(name = "region_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionId;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "region_depth1")
    private String regionDepth1;

    @Column(name = "region_depth2")
    private String regionDepth2;

    @Column(name = "region_depth3")
    private String regionDepth3;


}
