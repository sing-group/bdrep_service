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
public class Language {

    /**
     * The name of the language
     */
    private String language;

    /**
     * The datasets associated to this language
     */
    private Set<Dataset> datasets;

    /**
     * The default constructor
     */
    public Language() {
    }

    public Language(String language) {
        this.language = language;
    }

    /**
     * Return datasets associated to this language
     *
     * @return datasets associated to this language
     */
    public Set<Dataset> getDatasets() {
        return this.datasets;
    }

    /**
     * Stablish the datasets associated to this language
     *
     * @param datasets associated to this language
     */
    public void setDatasets(Set<Dataset> datasets) {
        this.datasets = datasets;
    }

    /**
     * Returns the name of the language
     *
     * @return the name of the language
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * Stablish the name of the language
     *
     * @param language the name of the language
     */
    public void setLanguage(String language) {
        this.language = language;
    }
}
