package org.datasetservice.domain;

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
