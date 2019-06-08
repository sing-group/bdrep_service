package org.datasetservice.domain;

import java.util.Set;


/**
 * JPA Bean for the Dataset objects managed by application
 * @author Ismael Vázqez
 */
public class Language
{

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
    public Language()
    {

    }

    public Language(String language)
    {
        this.language = language;
    }

    /**
     * Return datasets associated to this language
     * @return datasets associated to this language
     */
    public Set<Dataset> getDatasets()
    {
        return this.datasets;
    }

    /**
     * Stablish the datasets associated to this language
     * @param datasets associated to this language
     */
    public void setDatasets(Set<Dataset> datasets)
    {
        this.datasets = datasets;
    }

    /**
     * Returns the name of the language
     * @return the name of the language
     */ 
    public String getLanguage()
    {
        return this.language;
    }

    /**
     * Stablish the name of the language
     * @param language the name of the language
     */
    public void setLanguage(String language)
    {
        this.language = language;
    }
}