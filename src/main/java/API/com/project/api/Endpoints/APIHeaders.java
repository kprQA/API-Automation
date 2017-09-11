package API.com.project.api.Endpoints;

public enum APIHeaders {
    REC_MAX_SIZE("num_Record_MAX_SIZE"),
    REC_SKIPPED("num_Record_OFFSET"),
    REC_TOTAL_COUNT("num_RECORD_TOTAl_COUNT");
       
    private String headers;

    APIHeaders(String h) {
        this.headers = h;
    }

    public String toString() {
        return this.headers;
    }
}

