package oap

class Citation {
    static belongsTo = [document: Document]
    String title
    String datasetAbstract
    String useLimitation
    String purpose
    String expocode
    String cruiseId
    String section
    String citationAuthorList
    // The word "references" is a mysql reserved word.
    String scientificReferences
    String supplementalInformation
    String researchProjects
    String methodsApplied
    static constraints = {
        document (nullable: true)
        title (nullable: true, type: 'text')
        datasetAbstract (nullable: true, type: 'text')
        useLimitation (nullable: true, type: 'text')
        purpose (nullable: true, type: 'text')
        expocode (nullable: true)
        cruiseId (nullable: true)
        section (nullable: true)
        citationAuthorList (nullable: true, type: 'text')
        scientificReferences (nullable: true, type: 'text')
        supplementalInformation (nullable: true, type: 'text')
        researchProjects(nullable: true, type: 'text')
        methodsApplied(nullable: true, type: 'text')
    }
    public  boolean isEmpty() {
        return Document.emptyOrNull(title) &&
               Document.emptyOrNull(datasetAbstract) &&
               Document.emptyOrNull(useLimitation) &&
               Document.emptyOrNull(purpose) &&
               Document.emptyOrNull(expocode) &&
               Document.emptyOrNull(cruiseId) &&
               Document.emptyOrNull(section) &&
               Document.emptyOrNull(citationAuthorList) &&
               Document.emptyOrNull(scientificReferences) &&
               Document.emptyOrNull(supplementalInformation) &&
               Document.emptyOrNull(researchProjects) &&
               Document.emptyOrNull(methodsApplied)
    }
}
