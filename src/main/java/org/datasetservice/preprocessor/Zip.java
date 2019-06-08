package org.datasetservice.preprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.datasetservice.domain.Dataset;

public class Zip
{

    public static void zip(String path)
    {
        File zipFile = new File(path.concat(".zip"));
        File dirHam = new File(path+File.separator+"_ham_/");
        File dirSpam = new File(path+File.separator+"_spam_/");

        System.out.println(zipFile.getAbsolutePath());
        System.out.println(dirHam.getAbsolutePath());
        System.out.println(dirSpam.getAbsolutePath());

        FileOutputStream fos;
        ZipOutputStream zos;
        byte[] buffer = new byte[1024];
        int length;

        try
        {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            zos.putNextEntry(new ZipEntry("_ham_/"));

            for(File fileHam : dirHam.listFiles())
            {
                zos.putNextEntry(new ZipEntry("_ham_"+File.separator+fileHam.getName()));
                FileInputStream fis = new FileInputStream(fileHam);
                while((length = fis.read(buffer, 0, 1024))!=-1)
                {
                    zos.write(buffer, 0, length);
                }
                fis.close();
                zos.closeEntry();
            }
            zos.closeEntry();

            zos.putNextEntry(new ZipEntry("_spam_/"));

            for(File fileSpam : dirSpam.listFiles())
            {
                zos.putNextEntry(new ZipEntry("_spam_"+File.separator+ fileSpam.getName()));
                FileInputStream fis = new FileInputStream(fileSpam);
                while((length = fis.read(buffer, 0, 1024))!=-1)
                {
                    zos.write(buffer, 0, length);
                }
                fis.close();
                zos.closeEntry();
            }
            zos.closeEntry();
            zos.close();
            fos.close();
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    public static boolean unzip(String path, String destPath)
    {
        boolean success = true;

        File destFile = new File(destPath);
        File sourceFile = new File(path);

        //Checks entries of the zip to verify if the format is correct
        try
        {
            ZipFile zipFile = new ZipFile(sourceFile);
            Enumeration<?> entries = zipFile.entries();

            if(!checkFormat(entries))
            {
                zipFile.close();
                success = false;
                return success;
            }  

            zipFile.close();
        }
        catch(ZipException ze)
        {
            success = false; 
            return success;
        }
        catch(IOException ioE)
        {
            success = false;
            return success;
        }
        
        //if format is correct copy files in destination directory
        if(!destFile.exists())
            destFile.mkdirs();

        FileInputStream fis;

        byte[] buffer = new byte[1024];

        try
        {
            fis = new FileInputStream(sourceFile);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry zipEntry = zis.getNextEntry();

            while(zipEntry!=null)
            {
                if(zipEntry.isDirectory())
                {
                    File newDirectory = 
                    new File(destPath + File.separator + zipEntry.getName());

                    if(!newDirectory.exists())
                        newDirectory.mkdir(); 
                        
                    zipEntry = zis.getNextEntry();
                }
                else
                {
                    File newFile = new File(destPath + File.separator + zipEntry.getName());
                    FileOutputStream fos = new FileOutputStream(newFile);

                    int len;
                    while((len = zis.read(buffer))>0)
                    {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    zis.closeEntry();
                    zipEntry = zis.getNextEntry();
                }

            }
            zis.closeEntry();
            zis.close();
            fis.close();
        }
        catch(FileNotFoundException fnfException)
        {
            success = false;
            return success;
        }
        catch(IOException ioException)
        {
            success = false;
            return success;
        }

        return success;
    }

    public static void delete(String pathToDataset)
    {
        File zipToDelete = new File(pathToDataset);

        zipToDelete.delete();
    }

    private static boolean checkFormat(Enumeration<?> zipEntries)
    {
        boolean success = true;
        ArrayList<String> necesaryEntries = new ArrayList<String>();
        necesaryEntries.add("_ham_/");
        necesaryEntries.add("_spam_/");

        while(zipEntries.hasMoreElements())
        {
            ZipEntry ze = (ZipEntry) zipEntries.nextElement();
            String entryName = ze.getName();

            if(necesaryEntries.contains(entryName))
            {
                necesaryEntries.remove(entryName);
            }
            else if(!entryName.startsWith("_ham_/") && !entryName.startsWith("_spam_/"))
            {
                success = false;
                break;
            }
        }

        if(!necesaryEntries.isEmpty())
        {
            success = false;
        }

        return success;
    }

}
