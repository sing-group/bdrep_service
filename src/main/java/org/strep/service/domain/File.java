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

import java.util.Date;
import java.util.Set;

/**
 * JPA Bean for the File objects managed by application
 *
 * @author Ismael Vázqez
 */
public class File {

    /**
     * The id of the file
     */
    private Long id;

    /**
     * The path of the file
     */
    private String path;

    /**
     * The type of the file: spam or ham
     */
    private String type;

    /**
     * The language of the file
     */
    private String language;

    /**
     * The date of the file
     */
    private Date date;

    /**
     * The extension of the file
     */
    private String extension;

    /**
     * The datasets related with the
     */
    private Set<Dataset> datasets;

    /**
     * The default constructor
     */
    public File() {
    }

    public File(String path, String type, String language, Date date, String extension) {
        this.path = path;
        this.type = type;
        this.language = language;
        this.extension = extension;
        this.date = date;
    }

    public File(Long id, String path, String type, String language, Date date, String extension) {
        this.id = id;
        this.path = path;
        this.type = type;
        this.language = language;
        this.date = date;
        this.extension = extension;
    }

    /**
     * Returns the extension of the file
     *
     * @return the file extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Stablish the extension of the file
     *
     * @param extension the file extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Returns the path of the file
     *
     * @return the path of the file
     */
    public String getPath() {
        return path;
    }

    /**
     * Stablish the path of the file
     *
     * @param path the path of the file
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Returns the type of the file
     *
     * @return the type of the file
     */
    public String getType() {
        return type;
    }

    /**
     * Stablish the type of the file
     *
     * @param type the type of the file
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the language of the file
     *
     * @return the language of the file
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Stablish the language of the file
     *
     * @param language the language of the file
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Return the date of the file
     *
     * @return the date of the file
     */
    public Date getDate() {
        return date;
    }

    /**
     * Stablish the date of the file
     *
     * @param date the date of the file
     */
    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
