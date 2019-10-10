package org.strep.service.domain;

import java.util.Set;

/**
 * JPA Bean for the Dataset objects managed by application
 *
 * @author Ismael Vázqez
 */
public class Datatype {

    /**
     * The name of the datatype
     */
    private String datatype;

    /**
     * Datasets associated to this datatype
     */
    private Set<Dataset> datasets;

    /**
     * The default constructor
     */
    protected Datatype() {

    }

    public Datatype(String datatype) {
        this.datatype = datatype;
    }

    /**
     * Return the datasets associated to this datatype
     *
     * @return datasets associated to this datatype
     */
    public Set<Dataset> getDatasets() {
        return this.datasets;
    }

    /**
     * Stablish the datasets associated to this datatype
     *
     * @param datasets datasets associated to this datatype
     */
    public void setDatasets(Set<Dataset> datasets) {
        this.datasets = datasets;
    }

    /**
     * Return the name of the datatype
     *
     * @return the name of the datatype
     */
    public String getDatatype() {
        return this.datatype;
    }

    /**
     * Stablish the name of the datatype
     *
     * @param datatype the name of the datatype
     */
    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }
}
