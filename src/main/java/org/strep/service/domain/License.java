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

/**
 * JPA Bean for the Dataset objects managed by application
 *
 * @author Ismael Vázquez
 */
public class License {

    /**
     * The name of the license
     */
    private String name;

    /**
     * The legal code of the license
     */
    private byte[] description;

    /**
     * The url to the original source of the license
     */
    private String url;

    /**
     * Creates an instance of the license
     *
     * @param name the name of the license
     * @param description the legal code of the license
     * @param url the url to the original source of the license
     */
    public License(String name, byte[] description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public License(String name) {
        this.name = name;
    }

    /**
     * The default constructor
     */
    public License() {
    }

    /**
     * Return the name of the license
     *
     * @return the name of the license
     */
    public String getName() {
        return name;
    }

    /**
     * Stablish the name of the license
     *
     * @param name the name of the license
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the legal code of the license
     *
     * @return
     */
    public byte[] getDescription() {
        return description;
    }

    /**
     * Stablish the legal code of the license
     *
     * @param description the legal code of the license
     */
    public void setDescription(byte[] description) {
        this.description = description;
    }

    /**
     * Return the url to the original source of the license
     *
     * @return the url to the original source of the license
     */
    public String getUrl() {
        return url;
    }

    /**
     * Stablish the url to the original source of the license
     *
     * @param url the url to the original source of the license
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
