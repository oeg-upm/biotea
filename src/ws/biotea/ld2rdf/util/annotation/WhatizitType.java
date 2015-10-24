package ws.biotea.ld2rdf.util.annotation;

/**
 * Created by IntelliJ IDEA.
 * User: ljgarcia
 * Date: 26/07/11
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public enum WhatizitType {
    GO("go"),
    UNIPROT("uniprot"),
    E("e"),
    SPECIES("species"),
    CHEBI("chebi"),
    DISEASE("disease"),
    DRUG("drug");

    private String type;
    private WhatizitType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
