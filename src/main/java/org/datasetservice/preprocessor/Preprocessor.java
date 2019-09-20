package org.datasetservice.preprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bdp4j.pipe.AbstractPipe;
import org.bdp4j.pipe.Pipe;
import org.bdp4j.pipe.SerialPipes;
import org.bdp4j.types.Instance;
import org.bdp4j.util.Configurator;
import org.bdp4j.util.PipeInfo;
import org.bdp4j.util.PipeProvider;
import org.datasetservice.dao.DatasetDAO;
import org.datasetservice.dao.FileDAO;
import org.datasetservice.dao.TaskCreateUdatasetDAO;
import org.datasetservice.dao.TaskDAO;
import org.datasetservice.domain.TaskCreateSdataset;
import org.datasetservice.domain.TaskCreateUPreprocessing;
import org.datasetservice.domain.TaskCreateUdataset;
import org.nlpa.pipe.impl.File2StringBufferPipe;
import org.nlpa.pipe.impl.GuessDateFromFilePipe;
import org.nlpa.pipe.impl.GuessLanguageFromStringBufferPipe;
import org.nlpa.pipe.impl.StoreFileExtensionPipe;
import org.nlpa.pipe.impl.TargetAssigningFromPathPipe;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Class for perform system, user and preprocessing tasks
 */
public class Preprocessor {

    /**
     * The file instances
     */
    private static ArrayList<Instance> instances;

    /**
     * The url to access the database
     */
    private String url;

    /**
     * The user to access the database
     */
    private String user;

    /**
     * The password to access the database
     */
    private String password;

    /**
     * The path of the dataset storage in file system
     */
    private String datasetStorage;

    /**
     * The path of the pipeline storage in file system
     */
    private String pipelineStorage;

    /**
     * The path of the output storage in file system
     */
    private String outputStorage;



    /**
     * Constructor for create instances of preprocessor objects
     * @param url the url to connect the database
     * @param user the user of the database
     * @param password the password of the database
     * @param datasetStorage the path to the dataset storage
     * @param pipelineStorage the path to the pipeline storage
     * @param outputStorage the path to the csv storage
     */
    public Preprocessor(String url, String user, String password, String datasetStorage, String pipelineStorage, String outputStorage) {
        instances = new ArrayList<Instance>();
        this.url = url;
        this.user = user;
        this.password = password;
        this.datasetStorage = datasetStorage;
        this.pipelineStorage = pipelineStorage;
        this.outputStorage = outputStorage;
    }

    /**
     * Execute a system task, extracting all metadata of the dataset and inserting in the database
     * @param task the system task
     * @return true if successfully completed, false in other case
     */
    public boolean preprocessSystemTask(TaskCreateSdataset task) {
        boolean success = false;
        String datasetName = task.getDataset().getName();
        String pathToDataset = datasetStorage.concat(File.separator + datasetName + ".zip");
        String pathDest = datasetStorage.concat(File.separator + datasetName);
        TaskDAO taskDAO = new TaskDAO(url, user, password);
        DatasetDAO datasetDAO = new DatasetDAO(url, user, password);
        FileDAO fileDAO = new FileDAO(url, user, password);

        if (Zip.unzip(pathToDataset, pathDest)) {

            Zip.delete(pathToDataset);
            taskDAO.changeState(null, "executing", task.getId());

            generateInstances(pathDest);
            
            
            AbstractPipe p = new SerialPipes(new AbstractPipe[] { new TargetAssigningFromPathPipe(),
                    new StoreFileExtensionPipe(), new GuessDateFromFilePipe(), new File2StringBufferPipe(), new GuessLanguageFromStringBufferPipe() });
                    
            
            p.pipeAll(instances);
            

            for (Instance i : instances) {
                org.datasetservice.domain.File file;

                if ((file = retrieveInstanceData(i)) != null) {
                    fileDAO.insertDatasetFile(file, task.getDataset());
                }
            }

            ArrayList<org.datasetservice.domain.File> datasetFiles = fileDAO.getDatasetFiles(datasetName);

            int percentageSpam = this.calculatePercentage(datasetFiles);
            int percentageHam = 100 - percentageSpam;
            Date dateFrom = this.calculateDateFrom(datasetFiles);
            Date dateTo = this.calculateDateTo(datasetFiles);

            datasetDAO.completeFields(datasetName, percentageHam, percentageSpam, dateFrom, dateTo);

            Zip.zip(pathDest);
            taskDAO.changeState(null, "success", task.getId());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            datasetDAO.setAvailable(datasetName, true);
        } else {
            taskDAO.changeState("Failed to uncompress the dataset", "failed", task.getId());
            Zip.delete(pathToDataset);
        }

        resetInstances();

        return success;
    }

    /**
     * Execute a user task, combining datasets for generate a new one
     * @param task the user task
     * @return true if successfully completed, false in other case
     */
    public boolean preprocessUserTask(TaskCreateUdataset task) {
        boolean success = false;

        DatasetDAO datasetDAO = new DatasetDAO(url, user, password);
        TaskDAO taskDAO = new TaskDAO(url, user, password);
        FileDAO fileDAO = new FileDAO(url, user, password);

        TaskCreateUdatasetDAO taskCreateUdatasetDAO = new TaskCreateUdatasetDAO(url, user, password);

            if (fileDAO.posibleAfterFilters(task)) {
                ArrayList<org.datasetservice.domain.File> files = fileDAO.getRandomFiles(task,
                        task.getSpamMode());

                ArrayList<org.datasetservice.domain.File> spamFiles = new ArrayList<org.datasetservice.domain.File>();
                ArrayList<org.datasetservice.domain.File> hamFiles = new ArrayList<org.datasetservice.domain.File>();

                for (org.datasetservice.domain.File file : files) {
                    if (file.getType().equals("spam"))
                        spamFiles.add(file);
                    else
                        hamFiles.add(file);
                }

                String datasetName = task.getDataset().getName();
                File newDirectory = new File(datasetStorage + File.separator + datasetName);

                if (!newDirectory.exists())
                    newDirectory.mkdir();

                File newDirectorySpam = new File(datasetStorage + File.separator + datasetName + File.separator + "_spam_");
                File newDirectoryHam = new File(datasetStorage + File.separator + datasetName + File.separator + "_ham_");

                if (!newDirectorySpam.exists())
                    newDirectorySpam.mkdir();

                if (!newDirectoryHam.exists())
                    newDirectoryHam.mkdir();

                for (org.datasetservice.domain.File spamFile : spamFiles) {
                    String path = spamFile.getPath();
                    int lastSeparator = path.lastIndexOf(File.separator);
                    String fileName = path.substring(lastSeparator);
                    String pathDest = newDirectorySpam.getAbsolutePath() + File.separator + fileName;

                    copyFile(path, pathDest);
                    fileDAO.insertFileById(spamFile, task.getDataset());
                }

                for (org.datasetservice.domain.File hamFile : hamFiles) {
                    String path = hamFile.getPath();
                    int lastSeparator = path.lastIndexOf(File.separator);
                    String fileName = path.substring(lastSeparator);
                    String pathDest = newDirectoryHam.getAbsolutePath() + File.separator + fileName;

                    copyFile(path, pathDest);
                    fileDAO.insertFileById(hamFile, task.getDataset());
                }

                Zip.zip(newDirectory.getAbsolutePath());

                ArrayList<org.datasetservice.domain.File> datasetFiles = fileDAO.getDatasetFiles(datasetName);

                
                int percentageSpam = this.calculatePercentage(datasetFiles);
                int percentageHam = 100 - percentageSpam;
                System.out.println("PERCENTAGE SPAM"+ percentageSpam);
                System.out.println("PERCENTAGE HAM"+ percentageHam);
                Date dateFrom = this.calculateDateFrom(datasetFiles);
                Date dateTo = this.calculateDateTo(datasetFiles);

                datasetDAO.completeFields(datasetName, percentageSpam, percentageHam, dateFrom, dateTo);
                taskCreateUdatasetDAO.stablishLicense(task);
                taskDAO.changeState(null, "success", task.getId());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                datasetDAO.setAvailable(task.getDataset().getName(), true);
            } 
            else 
            {
                taskDAO.changeState("Not posible after filters", "failed", task.getId());
            }

        return success;
    }

    /**
     * Execute preprocessing task, generating csv in output folder
     * @param task the task to execute
     * @return true if successfully completed, false in other case
     */
    public boolean preprocessDataset(TaskCreateUPreprocessing task)
    {
        boolean success = false;
        String xmlPath = pipelineStorage+task.getPreprocessDataset().getName()+task.getId()+".xml";
        File file = new File(xmlPath);
        TaskDAO taskDAO = new TaskDAO(url, user, password);

        taskDAO.changeState(null, "executing", task.getId());

        if(!file.exists())
        {
            FileOutputStream fos;
            
            try
            {
                fos = new FileOutputStream(file);
                fos.write(task.getPipeline());
                fos.close();
            }
            catch(FileNotFoundException ioException)
            {
                return success;
            }
            catch(IOException ioException)
            {
                return success;
            }
        }

        
        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(file);

            document.normalize();

            NodeList configNodes = document.getElementsByTagName("general");
            for(int i = 0; i<configNodes.getLength();i++)
            {
                Element element = (Element) configNodes.item(i);
                element.getParentNode().removeChild(element);
            }

            Element rootNode = (Element)document.getElementsByTagName("configuration").item(0);
            
            Element general = document.createElement("general");
            Element samplesFolder = document.createElement("samplesFolder");
            samplesFolder.setTextContent(datasetStorage+task.getPreprocessDataset().getName());
            Element outputDir = document.createElement("outputFolder");
            outputDir.setTextContent(outputStorage);
            general.appendChild(samplesFolder);
            general.appendChild(outputDir);
            rootNode.appendChild(general);
            

            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            Writer out = new StringWriter();
            tf.transform(new DOMSource(document), new StreamResult(out));
            
            file.delete();
            FileWriter fileWriter = new FileWriter(new File(xmlPath));
            fileWriter.write(out.toString());
            System.out.println(out.toString());
            fileWriter.close();

        }
        catch(Exception e)
        {

        }
        
        Configurator configurator = Configurator.getInstance(xmlPath);

        configurator.configureApp();

        System.out.println(configurator.getProp(Configurator.SAMPLES_FOLDER));
        System.out.println(configurator.getProp(Configurator.OUTPUT_FOLDER));

        PipeProvider pipeProvider = new PipeProvider(configurator.getProp(Configurator.PLUGINS_FOLDER));
        HashMap<String, PipeInfo> pipes = pipeProvider.getPipes();

        Pipe p = configurator.configurePipeline(pipes);

        generateInstances(configurator.getProp(Configurator.SAMPLES_FOLDER));

        p.pipeAll(instances);
        resetInstances();

        File csv = new File(outputStorage+"output.csv");

        if(csv.renameTo(new File(outputStorage+task.getPreprocessDataset().getName()+task.getId()+".csv")))
        {
            taskDAO.changeState(null, "success", task.getId());
        }
        else
        {
            taskDAO.changeState(null, "failed", task.getId());
        }
        
        
        return success;
    }

    private void copyFile(String pathSource, String pathDest) {

        FileInputStream fis;
        FileOutputStream fos;

        byte[] buffer = new byte[1024];
        int length;

        try {
            fis = new FileInputStream(new File(pathSource));
            fos = new FileOutputStream(new File(pathDest));

            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                fos.write(buffer, 0, length);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


    /**
     * Generate a instance List on instances attribute by recursivelly finding all
     * files included in testDir directory
     *
     * @param testDir The directory where the instances should be loaded
     */
    private static void generateInstances(String testDir) {
        try {
            Files.walk(Paths.get(testDir)).filter(Files::isRegularFile).forEach(FileMng::visit);
        } catch (IOException e) {
            System.exit(0);
        }
    }

    /**
     * Reset static instances
     */
    private static void resetInstances() {
        ArrayList<Instance> emptyInstances = new ArrayList<Instance>();
        instances = emptyInstances;
    }

    /**
     * Obtain data of an instance
     * @param i the instance
     * @return the generated file based on instance data
     */
    private org.datasetservice.domain.File retrieveInstanceData(Instance i) {
        try {
            String path = i.getName().toString();
            String type = i.getTarget().toString();
            String extension = i.getProperty("extension").toString();
            String language = i.getProperty("language").toString();
            Date date = null;

            if(!(i.getProperty("date") instanceof String))
            {
                date = (Date) i.getProperty("date");
            }
            
            String finalExtension = "." + extension;

            org.datasetservice.domain.File file = new org.datasetservice.domain.File(path, type, language, date,
                    finalExtension);
            return file;
        } catch (NullPointerException npException) {
            return null;
        }

    }

    /**
     * Calculate percentage of spam for the specified files
     * @param datasetFiles the files of the dataset
     * @return the percentage of spam
     */
    private int calculatePercentage(ArrayList<org.datasetservice.domain.File> datasetFiles) {
        int total = datasetFiles.size();
        int spamFiles = 0;
        int spamPercentage = 0;

        for (org.datasetservice.domain.File file : datasetFiles) {
            if (file.getType().equals("spam")) {
                spamFiles++;
            }
        }

        spamPercentage = (int) Math.ceil((double) spamFiles * 100.00 / (double) total);

        return spamPercentage;
    }

    /**
     * Calculate initial messages date
     * @param datasetFiles the dataset files
     * @return the initial messages date
     */
    private Date calculateDateFrom(ArrayList<org.datasetservice.domain.File> datasetFiles) {
        Date actualDate = new Date(Long.MAX_VALUE);

        for (org.datasetservice.domain.File file : datasetFiles) {
            if (file.getDate() != null && file.getDate().compareTo(actualDate) < 0) {
                actualDate = file.getDate();
            }
        }

        if(actualDate.equals(new Date(Long.MAX_VALUE)))
            actualDate = null;

        return actualDate;
    }

    /**
     * Calculate final messages date
     * @param datasetFiles the dataset files
     * @return the final messages date
     */
    private Date calculateDateTo(ArrayList<org.datasetservice.domain.File> datasetFiles) {
        Date actualDate = new Date(Long.MIN_VALUE);

        for (org.datasetservice.domain.File file : datasetFiles) {
            if (file.getDate()!=null && file.getDate().compareTo(actualDate) > 0) {
                actualDate = file.getDate();
            }
        }
        if(actualDate.equals(new Date(Long.MIN_VALUE)))
            actualDate = null;

        return actualDate;
    }

    /**
     * Used to add a new instance on instances attribute when a new file is
     * detected.
     */
    static class FileMng {

        /**
         * Include a filne in the instancelist
         *
         * @param path The path of the file
         */
        static void visit(Path path) {
            File data = path.toFile();
            String target = null;
            String name = data.getPath();
            File source = data;

            instances.add(new Instance(data, target, name, source));
        }
    }
}