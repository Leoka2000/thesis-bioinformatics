package myspring_app_group.bioinformatics_thesis.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class BlastResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sequence;
    private String hitId;

    @Column(columnDefinition = "TEXT") // Use TEXT for larger fields
    private String hitDefinition;

    private String hitAccession;
    private int hitLength;
    private double bitScore;
    private double evalue;
    private int identity;
    private int positive;
    private int gaps;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getHitId() {
        return hitId;
    }

    public void setHitId(String hitId) {
        this.hitId = hitId;
    }

    public String getHitDefinition() {
        return hitDefinition;
    }

    public void setHitDefinition(String hitDefinition) {
        this.hitDefinition = hitDefinition;
    }

    public String getHitAccession() {
        return hitAccession;
    }

    public void setHitAccession(String hitAccession) {
        this.hitAccession = hitAccession;
    }

    public int getHitLength() {
        return hitLength;
    }

    public void setHitLength(int hitLength) {
        this.hitLength = hitLength;
    }

    public double getBitScore() {
        return bitScore;
    }

    public void setBitScore(double bitScore) {
        this.bitScore = bitScore;
    }

    public double getEvalue() {
        return evalue;
    }

    public void setEvalue(double evalue) {
        this.evalue = evalue;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public int getGaps() {
        return gaps;
    }

    public void setGaps(int gaps) {
        this.gaps = gaps;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}