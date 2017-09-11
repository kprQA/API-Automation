package API.com.project.api.Endpoints;

public enum APIEndPoints {
    BASE_URL("baseUrl"),
    COURSES("courses"),
    PROGRAMS("programs"),
    CONCENTRATIONS("concentations"),
    FACULTY_BIO("faculty"),
    LIMIT_RECORDS_POSTFIX("limitRecordPostFix"),
    SAIL_TERMS("sailTerms"),
    SAIL_REGISTARTIONS("sailRegistrations"),
	SAIL_COURSES("sailCourses"),
	SAIL_COURSEREGISTRATION("sailcourseregistration"),
    SAIL_COURSESECTION("sailcoursesection"),
    SAIL_ALLUSERS("sailallusers"),
    SAIL_JOBPLACEMENTS("sailjobplacements"),
    SAIL_USER("sailuser");
	
    private String endPoint;

    APIEndPoints(String url) {
        this.endPoint = url;
    }

    public String toString() {
        return this.endPoint;
    }

}