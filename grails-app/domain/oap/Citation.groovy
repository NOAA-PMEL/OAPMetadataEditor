package oap

class Citation {
    static belongsTo = [document: Document]
    String title;
    String platformAbstract;
    String purpose;
    String projects;
    String expocode;
    String cruiseId;
    String section;
    String citationAuthorList;
    String references;
    String supplementalInformation;
    String researchProjects
    static constraints = {
        document (nullable: true)
        title (nullable: true, type: 'text')
        platformAbstract (nullable: true, type: 'text')
        purpose (nullable: true)
        projects (nullable: true, type: 'text')
        expocode (nullable: true)
        cruiseId (nullable: true)
        section (nullable: true)
        citationAuthorList (nullable: true, type: 'text')
        references (nullable: true, type: 'text')
        supplementalInformation (nullable: true, type: 'text')
        researchProjects(nullable: true, type: 'text')
    }
}
