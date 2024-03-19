/*-
 * #%L
 * STRep Service
 * %%
 * Copyright (C) 2019 - 2024 SING Group (University of Vigo)
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
