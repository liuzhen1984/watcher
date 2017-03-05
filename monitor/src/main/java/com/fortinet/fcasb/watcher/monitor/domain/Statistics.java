package com.fortinet.fcasb.watcher.monitor.domain;

import java.io.Serializable;

/**
 * Created by zliu on 17/3/3.
 */
public class Statistics implements Serializable{

    private static final long serialVersionUID = -2047728067852756058L;
    private String type;
    private String metrics;
    private String filter;
    private String time;
    private String value;

    @Override
    public String toString() {
        return "" +
                " time='" + time + '\'' +
                ", type='" + type + '\'' +
                ", metrics='" + metrics + '\'' +
                ", filter='" + filter + '\'' +
                ", value='" + value + '\'';
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public enum Type{
        System(),
        Progress();
    }

    public interface Metrics {
        public enum SYSTEM{
            CPUUtilization(),
            MEMCPUUtilization(),
            Runtime();

        }
        public enum PROGRESS{
            IS_RUNNING(),
            CPUUtilization(),
            MEMCPUUtilization(),
            Runtime();
        }
    }
}
