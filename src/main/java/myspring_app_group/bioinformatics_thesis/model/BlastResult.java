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

    // New fields for BLAST output information
    private String program;
    private String version;
    private String reference;
    private String db;
    private String queryId;
    private String queryDef;
    private String queryLen;
    private String matrix;
    private String expect;
    private String gapOpen;
    private String gapExtend;
    private String filter;

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

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getQueryDef() {
        return queryDef;
    }

    public void setQueryDef(String queryDef) {
        this.queryDef = queryDef;
    }

    public String getQueryLen() {
        return queryLen;
    }

    public void setQueryLen(String queryLen) {
        this.queryLen = queryLen;
    }

    public String getMatrix() {
        return matrix;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getGapOpen() {
        return gapOpen;
    }

    public void setGapOpen(String gapOpen) {
        this.gapOpen = gapOpen;
    }

    public String getGapExtend() {
        return gapExtend;
    }

    public void setGapExtend(String gapExtend) {
        this.gapExtend = gapExtend;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}