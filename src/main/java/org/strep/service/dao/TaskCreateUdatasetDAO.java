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
package org.strep.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.strep.service.domain.Dataset;
import org.strep.service.domain.Datatype;
import org.strep.service.domain.Language;
import org.strep.service.domain.License;
import org.strep.service.domain.TaskCreateUdataset;

/**
 * The user tasks data access object
 */
public class TaskCreateUdatasetDAO {

    /**
     * The connection url to the database
     */
    private static final Logger logger = LogManager.getLogger(TaskCreateUdatasetDAO.class);

    /**
     * A constructor for create instances of TaskCreateUDatasetDAO
     */
    public TaskCreateUdatasetDAO() {
    }

    /**
     * Return a list of the waiting user tasks
     *
     * @return a list of the waiting user tasks
     */
    public ArrayList<TaskCreateUdataset> getWaitingUserTasks() {
        ArrayList<TaskCreateUdataset> waitingUtasks = new ArrayList<>();

        String query = "SELECT t.id, t.message, t.state, ud.limit_ham_percentage_eml,"
                + "ud.limit_spam_percentage_eml, ud.limit_ham_percentage_twtid, ud.limit_spam_percentage_twtid,"
                + "ud.limit_ham_percentage_tsms, ud.limit_spam_percentage_tsms,ud.limit_ham_percentage_ytbid,"
                + "ud.limit_spam_percentage_ytbid, ud.limit_ham_percentage_warc, ud.limit_spam_percentage_warc,"
                + "ud.limit_number_of_files, ud.limit_percentage_spam, ud.spam_mode, ud.date_to, ud.date_from "
                + "FROM task_create_udataset ud INNER JOIN task t ON t.id = ud.id WHERE t.state='waiting'";
        try (Connection connection = ConnectionPool.getDataSourceConnection();
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                DatasetDAO datasetDAO = new DatasetDAO();
                Dataset dataset = datasetDAO.getDatasetByTaskId(rs.getLong(1));

                if (dataset != null) {
                    Long taskId = rs.getLong(1);
                    String message = rs.getString(2);
                    String state = rs.getString(3);
                    int limitPercentageSpam = rs.getInt(15);
                    int limitNumberOfFiles = rs.getInt(14);
                    Date dateFrom = rs.getDate(18);
                    Date dateTo = rs.getDate(17);

                    LanguageDAO languageDAO = new LanguageDAO();
                    LicenseDAO licenseDAO = new LicenseDAO();
                    DatatypeDAO datatypeDAO = new DatatypeDAO();

                    ArrayList<Language> languages = languageDAO.getLanguages(taskId);
                    ArrayList<License> licenses = licenseDAO.getLicenses(taskId);
                    ArrayList<Datatype> datatypes = datatypeDAO.getDatatypes(taskId);
                    ArrayList<Dataset> datasets = datasetDAO.getDatasetsUserTask(taskId);

                    int limitSpamPercentageEml = rs.getInt(5);
                    int limitHamPercentageEml = rs.getInt(4);

                    int limitSpamPercentageWarc = rs.getInt(13);
                    int limitHamPercentageWarc = rs.getInt(12);

                    int limitSpamPercentageYtbid = rs.getInt(11);
                    int limitHamPercentageYtbid = rs.getInt(10);

                    int limitSpamPercentageTsms = rs.getInt(9);
                    int limitHamPercentageTsms = rs.getInt(8);

                    int limitSpamPercentageTwtid = rs.getInt(7);
                    int limitHamPercentageTwtid = rs.getInt(6);

                    boolean spamMode = rs.getBoolean(16);

                    TaskCreateUdataset taskCreateUdataset = new TaskCreateUdataset(taskId, dataset, state, message, limitPercentageSpam, limitNumberOfFiles,
                            dateFrom, dateTo, languages, datatypes, licenses, datasets, limitSpamPercentageEml, limitHamPercentageEml, limitSpamPercentageWarc,
                            limitHamPercentageWarc, limitSpamPercentageYtbid, limitHamPercentageYtbid, limitSpamPercentageTsms, limitHamPercentageTsms, limitSpamPercentageTwtid,
                            limitHamPercentageTwtid, spamMode);

                    waitingUtasks.add(taskCreateUdataset);
                }
            }
        } catch (SQLException sqlException) {
           logger.warn("[ERROR getWaitingUserTasks]: " + sqlException.getMessage());
        }

        return waitingUtasks;
    }

}
