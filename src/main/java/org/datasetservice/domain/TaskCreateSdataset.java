package org.datasetservice.domain;


/**
 * JPA Bean for the Dataset objects managed by application
 * @author Ismael Vázqez
 */
public class TaskCreateSdataset extends Task
{

    /**
     * Creates an instance of TaskCreateSdataset
     * @param dataset the dataset associated to the task
     * @param state the state of the task
     * @param message the message of the task when failed
     */
    public TaskCreateSdataset(Long id,Dataset dataset, String state, String message)
    {
        super(id,dataset, state, message);
    }

    /**
     * The default constructor
     */
    public TaskCreateSdataset()
    {
        super();
    }
}