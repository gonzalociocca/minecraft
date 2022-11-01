package org.syntax.pr.reports;

public class Report
{
    private String reporter;
    private String reported;
    private String message;
    private Status status;

    public Report(String reporter, String reported, String message)
    {
        setReporter(reporter);
        setReported(reported);
        setMessage(message);
        setStatus(Status.EN_ESPERA);
    }

    public Report(String reporter, String reported, String message, Status status)
    {
        setReporter(reporter);
        setReported(reported);
        setMessage(message);
        setStatus(Status.EN_ESPERA);
        setStatus(status);
    }

    public String getReporter()
    {
        return this.reporter;
    }

    public void setReporter(String reporter)
    {
        this.reporter = reporter;
    }

    public String getReported()
    {
        return this.reported;
    }

    public void setReported(String reported)
    {
        this.reported = reported;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Status getStatus()
    {
        return this.status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }
}
