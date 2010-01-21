/*
 *  YUI Test
 *  Author: Nicholas C. Zakas <nzakas@yahoo-inc.com>
 *  Copyright (c) 2009, Yahoo! Inc. All rights reserved.
 *  Code licensed under the BSD License:
 *      http://developer.yahoo.net/yui/license.txt
 */

package com.yahoo.platform.yuitest.coverage.report;

import com.yahoo.platform.yuitest.coverage.results.FileReport;
import com.yahoo.platform.yuitest.coverage.results.SummaryReport;
import com.yahoo.platform.yuitest.writers.ReportWriter;
import com.yahoo.platform.yuitest.writers.ReportWriterFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

/**
 *
 * @author Nicholas C. Zakas
 */
public class HTMLReportGenerator implements ReportGenerator {

    private File outputdir = null;
    private boolean verbose = false;

    /**
     * Creates a new HTMLReportGenerator
     * @param outputdir The output directory for the HTML report.
     * @param verbose True to output additional information to the console.
     */
    public HTMLReportGenerator(String outputdir, boolean verbose){
        this.outputdir = new File(outputdir);
        this.verbose = verbose;

        //create directory if not already there
        if (!this.outputdir.exists()){
            this.outputdir.mkdirs();
        }
    }

    public void generate(SummaryReport report) throws IOException {
        Date now = new Date();
        generateSummaryPage(report, now);
        generateFilePages(report, now);
    }

    /**
     * Generates the index.html page for the HTML report.
     * @param report The coverage information to generate a report for.
     * @param date The date associated with the report.
     * @throws IOException When a file cannot be written to.
     */
    private void generateSummaryPage(SummaryReport report, Date date) throws IOException {

        String outputFile = outputdir.getAbsolutePath() + File.separator + "index.html";
        Writer out = new OutputStreamWriter(new FileOutputStream(outputFile));

        if (verbose){
            System.err.println("[INFO] Outputting " + outputFile);
        }

        ReportWriter reportWriter = (new ReportWriterFactory<SummaryReport>()).getWriter(out, "CoverageSummaryReportHTML");
        reportWriter.write(report, date);
        out.close();
    }

    /**
     * Generates a report page for each file in the coverage information.
     * @param report The coverage information to generate reports for.
     * @param date The date associated with the report.
     * @throws IOException When a file cannot be written to.
     */
    private void generateFilePages(SummaryReport report, Date date) throws IOException {

        FileReport[] fileReports = report.getFileReports();

        for (int i=0; i < fileReports.length; i++){
            generateFilePage(fileReports[i], date);
        }
    }

    /**
     * Generates a report page for the file coverage information.
     * @param report The coverage information to generate reports for.
     * @param date The date associated with the report.
     * @throws IOException When a file cannot be written to.
     */
    private void generateFilePage(FileReport report, Date date) throws IOException {
        String outputFile = outputdir.getAbsolutePath() + File.separator + report.getReportName() + ".html";

        if (verbose){
            System.err.println("[INFO] Outputting " + outputFile);
        }

        Writer out = new OutputStreamWriter(new FileOutputStream(outputFile));
        ReportWriter reportWriter = (new ReportWriterFactory<FileReport>()).getWriter(out, "CoverageFileReportHTML");
        reportWriter.write(report, date);
        out.close();
    }
}
