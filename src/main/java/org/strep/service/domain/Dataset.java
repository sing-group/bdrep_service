package org.strep.service.domain;

import java.util.Set;
import java.util.Date;

/**
 * JPA Bean for the Dataset objects managed by application
 *
 * @author Ismael Vázqez
 */
public class Dataset {

    /**
     * The name of the dataset
     */
    private String name;

    /**
     * The author of the dataset
     */
    private String author;

    /**
     * The description of the dataset
     */
    private String description;

    /**
     * The pipeline used to generate the dataset
     */
    private byte[] pipeline;

    /**
     * The doi for the dataset
     */
    private String doi;

    /**
     * The accesstype for the dataset
     */
    private String access;

    /**
     * The languages of the dataset
     */
    private Set<Language> language;

    /**
     * The data types of the dataset
     */
    private Set<Datatype> datatypes;
    /**
     * The type of the dataset
     */
    private String type;

    /**
     * The date when the dataset was uploaded
     */
    private Date uploadDate;

    /**
     * The URL to access the dataset when the access is public
     */
    private String url;

    /**
     * The files of the dataset
     */
    private Set<File> files;

    /**
     * The percentage of Spam
     */
    private Integer spamPercentage;

    /**
     * The percentage of ham
     */
    private Integer hamPercentage;

    /**
     * Indicates that the dataset is available or not
     */
    private boolean available;

    /**
     * The task associated to this dataset
     */
    private Task task;

    /**
     * The license of the dataset
     */
    private License license;

    /**
     * The date of the first file of the dataset
     */
    private Date firstFileDate;

    /**
     * The date of the last file of the dataset
     */
    private Date lastFileDate;

    /**
     * The default constructor
     */
    public Dataset() {

    }

    public Dataset(String name) {
        this.name = name;
    }

    public Dataset(String name, String url, String author, String description, String access, Integer spamPercentage, Integer hamPercentage,
            String type, License license) {
        this.name = name;
        this.url = url;
        this.author = author;
        this.description = description;
        this.access = access;
        this.spamPercentage = spamPercentage;
        this.hamPercentage = hamPercentage;
        this.type = type;
        this.available = false;
        this.license = license;
    }

    /**
     * Return the name of the dataset
     *
     * @return the name of the dataset
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the dataset
     *
     * @param name the mame of the dataset
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the url of the dataset
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url of the dataset
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Return the author of the dataset
     *
     * @return the author of the dataset
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the name of the author
     *
     * @param author the name of the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns the description of the dataset
     *
     * @return the description of the dataset
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the datset
     *
     * @param description the description of the datset
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the preprocessing pipeline used to generate the corpus
     *
     * @return the preprocessing pipeline used to generate the corpus
     */
    public byte[] getPipeline() {
        return pipeline;
    }

    /**
     * Stablish the preprocessing pipeline used to generate the corpus
     *
     * @param pipeline the preprocessing pipeline used to generate the corpus
     */
    public void setPipeline(byte[] pipeline) {
        this.pipeline = pipeline;
    }

    /**
     * Returns the DOI (Document Identifier for the corpus)
     *
     * @return the DOI (Document Identifier for the corpus)
     */
    public String getDoi() {
        return doi;
    }

    /**
     * Sets the the DOI (Document Identifier for the corpus)
     *
     * @param doi the DOI (Document Identifier for the corpus)
     */
    public void setDoi(String doi) {
        this.doi = doi;
    }

    /**
     * Returns the access for the corpus
     *
     * @return the access for the corpus
     */
    public String getAccess() {
        return access;
    }

    /**
     * Stablish the access for the corpus
     *
     * @param access the access for the corpus
     */
    public void setAccess(String access) {
        this.access = access;
    }

    /**
     * Returns the languages contained in the corpus
     *
     * @return the languages contained in the corpus
     */
    public Set<Language> getLanguage() {
        return language;
    }

    /**
     * Stablish the languages contained in the corpus
     *
     * @param language the languages contained in the corpus
     */
    public void setLanguage(Set<Language> language) {
        this.language = language;
    }

    /**
     * Returns the datatypes of the dataset
     *
     * @return the datatypes of the dataset
     */
    public Set<Datatype> getDatatypes() {
        return this.datatypes;
    }

    /**
     * Stablish the datatypes of the dataset
     *
     * @param datatypes the datatypes of the dataset
     */
    public void setDatatypes(Set<Datatype> datatypes) {
        this.datatypes = datatypes;
    }

    /**
     * Returns the date when the corpus was uploaded
     *
     * @return the date when the corpus was uploaded
     */
    public Date getUploadDate() {
        return uploadDate;
    }

    /**
     * Stablish the date when the corpus was uploaded
     *
     * @param uploadDate the date when the corpus was uploaded
     */
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
     * Returns the percentage of spam messages
     *
     * @return the percentage of spam messages
     */
    public Integer getSpamPercentage() {
        return spamPercentage;
    }

    /**
     * Stablish the percentage of spam messages
     *
     * @param spamPercentage the percentage of spam messages
     */
    public void setSpamPercentage(int spamPercentage) {
        this.spamPercentage = new Integer(spamPercentage);
    }

    /**
     * Returns the percentage of ham messages
     *
     * @return the percentage of ham messages
     */
    public Integer getHamPercentage() {
        return hamPercentage;
    }

    /**
     * Stablish the percentage of ham messages
     *
     * @param hamPercentage the percentage of ham messages
     */
    public void setHamPercentage(int hamPercentage) {
        this.hamPercentage = new Integer(hamPercentage);
    }

    /**
     * Returns the type of the corpus
     *
     * @return the type of the corpus
     */
    public String getType() {
        return type;
    }

    /**
     * Stablish the type of the corpus
     *
     * @param type the type of the corpus
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the files included in the dataset
     *
     * @return the list of files included in the dataset
     */
    public Set<File> getFiles() {
        return files;
    }

    /**
     * Returns if the dataset is available or not
     *
     * @return the availability of the dataset
     */
    public boolean getAvailable() {
        return available;
    }

    /**
     * Stablish the availability of the dataset
     *
     * @param available the availability
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Returns the task associated to this dataset
     *
     * @return the task associated to this dataset
     */
    public Task getTask() {
        return task;
    }

    /**
     * Stablish the task associated to this dataset
     *
     * @param task the task of the dataset
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Returns the license of the dataset
     *
     * @return the license of the dataset
     */
    public License getLicense() {
        return this.license;
    }

    /**
     * Stablish the license of the dataset
     *
     * @param license the license of the dataset
     */
    public void setLicense(License license) {
        this.license = license;
    }

    /**
     * Returns the date of the first file of the dataset
     *
     * @return the date of the first file of the dataset
     */
    public Date getFirstFileDate() {
        return firstFileDate;
    }

    /**
     * Stablish the date of the first file of the dataset
     *
     * @param firstFileDate the date of the first file of the dataset
     */
    public void setFirstFileDate(Date firstFileDate) {
        this.firstFileDate = firstFileDate;
    }

    /**
     * Returns the date of the last file of the dataset
     *
     * @return the date of the last file of the dataset
     */
    public Date getLastFileDate() {
        return lastFileDate;
    }

    /**
     * Stablish the date of the last file of the dataset
     *
     * @param lastFileDate the date of the last file of the dataset
     */
    public void setLastFileDate(Date lastFileDate) {
        this.lastFileDate = lastFileDate;
    }
}
