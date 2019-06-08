package org.datasetservice.domain;

import java.util.Date;
import java.util.List;

/**
 * JPA Bean for the Dataset objects managed by application
 * @author Ismael Vázqez
 */
public class TaskCreateUdataset extends Task
{
    private int limitSpamPercentageEml;

    private int limitHamPercentageEml;

    private int limitSpamPercentageTwtid;
    
    private int limitHamPercentageTwtid;

    private int limitSpamPercentageTytb;

    private int limitHamPercentageTytb;
    
    private int limitSpamPercentageWarc;

    private int limitHamPercentageWarc;

    private int limitSpamPercentageTsms;

    private int limitHamPercentageTsms;
    /**
     * The limit of percentage of spam in the new dataset
     */
    private int limitPercentageSpam;

    /**
     * The limit of files of the new dataset
     */
    private int limitNumberOfFiles;

    /**
     * The date from which you can pick up files from the original datasets
     */
    private Date dateFrom;

    /**
     * The date until which you can pick up files from the original datasets
     */
    private Date dateTo;

    /**
     * The languages selected in the filters to construct the new dataset
     */
    private List<Language> languages;

    /**
     * The datatypes selected in the filters to construct the new dataset
     */
    private List<Datatype> datatypes;

    /**
     * The licenses selected in the filters to construct the new dataset
     */
    private List<License> licenses;

    /**
     * The datasets used to construct the new dataset
     */
    private List<Dataset> datasets;

    private boolean spamMode;

    /**
     * Creates an instance of TaskCreateUdataset
     * @param dataset The dataset associated to the task
     * @param state The state of the task
     * @param message The message of the task when failed
     * @param limitPercentageSpam The limit of percentage of spam in the new dataset
     * @param limitNumberOfFiles The limit of files of the new dataset
     * @param dateFrom The date from which you can pick up files from the original datasets
     * @param dateTo  The date until which you can pick up files from the original datasets
     * @param languages The languages selected in the filters to construct the new dataset
     * @param datatypes The datatypes selected in the filters to construct the new dataset
     * @param licenses The licenses selected in the filters to construct the new dataset
     * @param limitPercentageEml The limit of percentage of .eml files
     * @param limitPercentageTytb The limit of percentage of .tytb files
     * @param limitPercentageTsms The limit of percentage of .tsms files
     * @param limitPercentageTwtid The limit of percentage of .twtid files
     * @param limitPercentageWarc The limit of percentage of .warc files
     */
    public TaskCreateUdataset(Long id, Dataset dataset, String state, String message, int limitPercentageSpam, int limitNumberOfFiles,
    Date dateFrom, Date dateTo, List<Language> languages, List<Datatype> datatypes,List<License> licenses, List<Dataset> datasets,
    int limitSpamPercentageEml,
    int limitHamPercentageEml,int limitSpamPercentageWarc, int limitHamPercentageWarc, int limitSpamPercentageTytb, int limitHamPercentageTytb,
    int limitSpamPercentageTsms, int limitHamPercentageTsms, int limitSpamPercentageTwtid, int limitHamPercentageTwtid, boolean spamMode)
    {
        super(id,dataset, state, message);
        this.limitSpamPercentageEml = limitSpamPercentageEml;
        this.limitHamPercentageEml = limitHamPercentageEml;
        this.limitSpamPercentageTsms = limitSpamPercentageTsms;
        this.limitHamPercentageTsms = limitHamPercentageTsms;
        this.limitSpamPercentageTwtid = limitSpamPercentageTwtid;
        this.limitHamPercentageTwtid = limitHamPercentageTwtid;
        this.limitSpamPercentageWarc = limitSpamPercentageWarc;
        this.limitHamPercentageWarc = limitHamPercentageWarc;
        this.limitSpamPercentageTytb = limitSpamPercentageTytb;
        this.limitHamPercentageTytb = limitHamPercentageTytb;
        this.limitPercentageSpam = limitPercentageSpam;
        this.limitNumberOfFiles = limitNumberOfFiles;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.languages = languages;
        this.datatypes = datatypes;
        this.licenses = licenses;
        this.datasets = datasets;
        this.spamMode = spamMode;
    }

    /**
     * The default constructor
     */
    public TaskCreateUdataset()
    {
        super();
    }

    /**
     * Return the limit of percentage of spam in the new dataset
     * @return the limit of percentage of spam in the new dataset
     */
    public int getLimitPercentageSpam() {
        return this.limitPercentageSpam;
    }

    /**
     * Stablish the limit of percentage of spam in the new dataset
     * @param limitPercentageSpam the limit of percentage of spam in the new dataset
     */
    public void setLimitPercentageSpam(int limitPercentageSpam) {
        this.limitPercentageSpam = limitPercentageSpam;
    }

    /**
     * Return the limit of files in the new dataset
     * @return the limit of files in the new dataset
     */
    public int getLimitNumberOfFiles() {
        return this.limitNumberOfFiles;
    }

    /**
     * Stablish the limit of files in the new dataset
     * @param limitNumberOfFiles the limit of files in the new dataset
     */
    public void setLimitNumberOfFiles(int limitNumberOfFiles) {
        this.limitNumberOfFiles = limitNumberOfFiles;
    }

    /**
     * Return the date from which you can pick up files from the original datasets
     * @return  the date from which you can pick up files from the original datasets
     */
    public Date getDateFrom() {
        return this.dateFrom;
    }

    /**
     * Stablish the date from which you can pick up files from the original datasets
     * @param dateFrom the date from which you can pick up files from the original datasets
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * Return the date until you can pick up files from the original datasets
     * @return the date until you can pick up files from the original datasets
     */
    public Date getDateTo() {
        return this.dateTo;
    }

    /**
     * Stablish the date until you can pick up files from the original datasets
     * @param dateTo the date until you can pick up files from the original datasets
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * Return the list of the languages selected in the filters to construct the new dataset
     * @return the list of the languages selected in the filters to construct the new dataset
     */
    public List<Language> getLanguages() {
        return this.languages;
    }

    /**
     * Stablish the list of the languages selected in the filters to construct the new dataset
     * @param languages the list of the languages selected in the filters to construct the new dataset
     */
    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    /**
     * Return the list of the datatypes selected in the filters to construct the new dataset
     * @return the list of the datatypes selected in the filters to construct the new dataset
     */
    public List<Datatype> getDatatypes() {
        return this.datatypes;
    }

    /**
     * Stablish the list of the datatypes selected in the filters to construct the new dataset
     * @param datatypes the list of the datatypes selected in the filters to construct the new dataset
     */
    public void setDatatypes(List<Datatype> datatypes) {
        this.datatypes = datatypes;
    }

    /**
     * Return the list of licenses selected in the filters to construct the new dataset
     * @return the list of licenses selected in the filters to construct the new dataset
     */
    public List<License> getLicenses()
    {
        return licenses;
    }

    /**
     * Stablish the list of licenses selected in the filters to construct the new dataset
     * @param licenses the list of licenses selected in the filters to construct the new dataset
     */
    public void setLicenses(List<License> licenses)
    {
        this.licenses = licenses;
    }

    /**
     * Return the list of datasets used to construct the new dataset
     * @return the list of datasets used to construct the new dataset
     */
    public List<Dataset> getDatasets()
    {
        return this.datasets;
    }

    /**
     * Stablish the list of datasets used to construct the new dataset
     * @param datasets the list of datasets used to construct the new dataset
     */
    public void setDatasets(List<Dataset> datasets)
    {
        this.datasets = datasets;
    }

    public int getLimitSpamPercentageEml()
    {
        return this.limitSpamPercentageEml;
    }
    
    public void setLimitSpamPercentageEml(int limitSpamPercentageEml)
    {
        this.limitSpamPercentageEml = limitSpamPercentageEml;
    }
    
    public int getLimitHamPercentageEml()
    {
        return this.limitHamPercentageEml;
    }
    
    public void setLimitHamPercentageEml(int limitHamPercentageEml)
    {
        this.limitHamPercentageEml = limitHamPercentageEml;
    }

    public int getLimitSpamPercentageWarc()
    {
        return this.limitSpamPercentageWarc;
    }
    
    public void setLimitSpamPercentageWarc(int limitSpamPercentageWarc)
    {
        this.limitSpamPercentageWarc = limitSpamPercentageWarc;
    }
    
    public int getLimitHamPercentageWarc()
    {
        return this.limitHamPercentageWarc;
    }
    
    public void setLimitHamPercentageWarc(int limitHamPercentageWarc)
    {
        this.limitHamPercentageWarc = limitHamPercentageWarc;
    }
    

    public int getLimitSpamPercentageTsms()
    {
        return this.limitSpamPercentageTsms;
    }
    
    public void setLimitSpamPercentageTsms(int limitSpamPercentageTsms)
    {
        this.limitSpamPercentageTsms = limitSpamPercentageTsms;
    }
    
    public int getLimitHamPercentageTsms()
    {
        return this.limitHamPercentageTsms;
    }
    
    public void setLimitHamPercentageTsms(int limitHamPercentageTsms)
    {
        this.limitHamPercentageTsms = limitHamPercentageTsms;
    }

    public int getLimitSpamPercentageTytb()
    {
        return this.limitSpamPercentageTytb;
    }
    
    public void setLimitSpamPercentageTytb(int limitSpamPercentageTytb)
    {
        this.limitSpamPercentageTytb = limitSpamPercentageTytb;
    }
    
    public int getLimitHamPercentageTytb()
    {
        return this.limitHamPercentageTytb;
    }
    
    public void setLimitHamPercentageTytb(int limitHamPercentageTytb)
    {
        this.limitHamPercentageTytb = limitHamPercentageTytb;
    }

    public int getLimitSpamPercentageTwtid()
    {
        return this.limitSpamPercentageTwtid;
    }
    
    public void setLimitSpamPercentageTwtid(int limitSpamPercentageTwtid)
    {
        this.limitSpamPercentageTwtid = limitSpamPercentageTwtid;
    }
    
    public int getLimitHamPercentageTwtid()
    {
        return this.limitHamPercentageTwtid;
    }
    
    public void setLimitHamPercentageTwtid(int limitHamPercentageTwtid)
    {
        this.limitHamPercentageTwtid = limitHamPercentageTwtid;
    }

    public boolean getSpamMode()
    {
        return this.spamMode;
    }

    public void setSpamMode(boolean spamMode)
    {
        this.spamMode = spamMode;
    }

    public String toStringLicenses()
    {
        StringBuilder stringBuilder = new StringBuilder();

        if(this.getLicenses().size()==0)
        {
            stringBuilder.append("Not licenses selected");
        }
        else
        {
            for(License license : this.getLicenses())
            {
                stringBuilder.append(license.getName() + " ");
            }
        }

        return stringBuilder.toString();
    }

    public String toStringDatatypes()
    {
        StringBuilder stringBuilder = new StringBuilder();

        if(this.getDatatypes().size()==0)
        {
            stringBuilder.append("Not datatypes selected");
        }
        else
        {
            for(Datatype datatype : this.getDatatypes())
            {
                stringBuilder.append(datatype.getDatatype() + " ");
            }
        }

        return stringBuilder.toString();
    }

    public String toStringLanguages()
    {
        StringBuilder stringBuilder = new StringBuilder();

        if(this.getLanguages().size()==0)
        {
            stringBuilder.append("Not languages selected");
        }
        else
        {
            for(Language language : this.getLanguages())
            {
                stringBuilder.append(language.getLanguage() + " ");
            } 
        }

        return stringBuilder.toString();
    }

    public String toStringDate()
    {
        StringBuilder stringBuilder = new StringBuilder();

        if(dateTo!=null && dateFrom!=null)
        {
            stringBuilder.append("From "+this.getDateFrom().toString()+" until "+ this.getDateTo().toString());
        }
        else
        {
            stringBuilder.append("Not dates selected");
        }

        return stringBuilder.toString();
    }

    //Modify this method to show only the selected parameters: by spam or by datatypes
    public String toStringParameters()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("File number: " + this.limitNumberOfFiles +"\n");
        stringBuilder.append("%Spam: " + this.limitPercentageSpam +"\n");
        stringBuilder.append("% Spam .eml: "+ this.limitSpamPercentageEml+"\n");
        stringBuilder.append("% Spam .warc: " + this.limitSpamPercentageWarc + "\n");
        stringBuilder.append("% Spam .tsms: " + this.limitSpamPercentageTsms + "\n");
        stringBuilder.append("% Spam .twtid: " + this.limitSpamPercentageTwtid +"\n");
        stringBuilder.append("% Spam .tytb: " + this.limitSpamPercentageTytb + "\n");
        stringBuilder.append("% Ham .eml: "+ this.limitHamPercentageEml+"\n");
        stringBuilder.append("% Ham .warc: " + this.limitHamPercentageWarc + "\n");
        stringBuilder.append("% Ham .tsms: " + this.limitHamPercentageTsms + "\n");
        stringBuilder.append("% Ham .twtid: " + this.limitHamPercentageTwtid +"\n");
        stringBuilder.append("% Ham .tytb: " + this.limitHamPercentageTytb + "\n");

        return stringBuilder.toString();
    }

}