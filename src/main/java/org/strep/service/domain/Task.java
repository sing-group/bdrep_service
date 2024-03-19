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
 * @author Ismael Vázqez
 */
public class Task {

    /**
     * The id of the task
     */
    private long id;

    /**
     * The dataset associated to this task
     */
    private Dataset dataset;

    /**
     * The state of the task
     */
    private String state;

    /**
     * The message of the task when failed
     *
     */
    private String message;

    /**
     * Creates an instance of the task
     *
     * @param dataset the dataset associated to the task
     * @param state the state of the task
     * @param message the message of the task when failed
     */
    public Task(Long id, Dataset dataset, String state, String message) {
        this.id = id;
        this.dataset = dataset;
        this.state = state;
        this.message = message;
    }

    /**
     * The default constructor of the task
     */
    public Task() {

    }

    /**
     * Return the dataset associated to the task
     *
     * @return the dataset associated to the task
     */
    public Dataset getDataset() {
        return this.dataset;
    }

    /**
     * Stablish the dataset associated to the task
     *
     * @param dataset the dataset associated to the task
     */
    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    /**
     * Return the id of the task
     *
     * @return the id of the task
     */
    public long getId() {
        return this.id;
    }

    /**
     * Stablish the id of the task
     *
     * @param id the id of the task
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Return the state of the task
     *
     * @return the state of the task
     */
    public String getState() {
        return this.state;
    }

    /**
     * Stablish the state of the task
     *
     * @param state the state of the task
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Return the message of the task when failed
     *
     * @return the message of the task when failed
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Stablish the message of the task when failed
     *
     * @param message the message of the task when failed
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
