package oap

import grails.transaction.Transactional

@Transactional
class VocabularyService {

    static enum Vocabularies {
        VARIABLES,
        PLATFORMS,
        INSTRUMENTS,
        INSTITUTIONS,
        OBSERVATION_TYPES,
        PROJECTS
    }
    def ncei_observationTypes = [
            "Surface underway",
            "Surface (discrete samples)",
            "Profile (CTD continuous)",
            "Profile (discrete samples)",
            "Profile (undulating, gliders, etc.)",
            "Time-series",
            "Time-series (profile)",
            "Pump cast",
            "Model output",
            "Laboratory experiment",
            "Fish examination",
            "Biological tows",
            "Marine mammal observation",
            "Other"
    ]
    def observationTypes = [
            "ATLAS",
            "BENTHIC STUDY",
            "BUOY - DRIFTING",
            "BUOY - MOORED",
            "CURRENT MEASUREMENTS",
            "DERIVED PRODUCTS",
            "DISCRETE SAMPLING",
            "DOCUMENTATION ONLY - NO OBSERVATION TYPE",
            "FISH EXAMINATION",
            "GIS PRODUCT",
            "ICE CORE",
            "ICE HOLE SAMPLING",
            "IMAGERY",
            "INTEGRATED PROFILES",
            "LABORATORY EXPERIMENTS",
            "MANUAL SAMPLE COLLECTION",
            "MARINE MAMMAL OBSERVATION",
            "MODEL OUTPUT",
            "NAVIGATIONAL",
            "PORE WATER CHEMISTRY",
            "PROFILE (e.g., CTD)",
            "PUMP CAST",
            "SATELLITE DATA",
            "SEDIMENT ANALYSIS",
            "SURFACE MEASUREMENTS (EXCLUDING UNDERWAY)",
            "SURFACE UNDERWAY",
            "SURVEY - BIOLOGICAL",
            "SURVEY - CORAL REEF",
            "TIME SERIES",
            "TIME SERIES PROFILE",
            "TOWS",
            "TRAWL",
            "UNDULATING PROFILE (e.g., ARGO)",
            "VISUAL OBSERVATION"
    ]

    abstract static class Vocabulary<T> {
        Vocabularies vocabulary
        Set<T> items = new LinkedHashSet<>()

        Vocabulary(Vocabularies vocab) {
            vocabulary = vocab
        }

        Vocabulary(Vocabularies vocab, Collection<T> terms) {
            vocabulary = vocab
            items.addAll(terms);
        }

        String stdKey(String variable) {
            String std = variable.toLowerCase().trim().replaceAll("\\s+", "_")
            return std
        }

        String stdName(String variable) {
            String std = variable.trim().replaceAll("\\s+", " ")
            return std
        }

        Set<T> getVocabularyItems() {
            return items;
        }
    }
    static class DynamicVocabulary<T> extends Vocabulary {
        String sourceFileName
        File sourceFile
        Date lastLoaded
        DynamicVocabulary(Vocabularies vocab, String fileName) {
            super(vocab);
            sourceFileName = fileName
            sourceFile = new File(sourceFileName)
            if ( ! sourceFile.exists() || sourceFile.isDirectory()) {
                throw new RuntimeException("Vocabulary file " + sourceFile.getAbsolutePath() + " NOT FOUND!")
            }
            if ( ! ( sourceFile.canRead() && sourceFile.canWrite())) {
                throw new RuntimeException("Vocabulary file " + sourceFile.getAbsolutePath() + " cannot be read and/or written.")
            }
        }
        boolean needsReloading() {
            Date lastModified = new Date(sourceFile.lastModified())
            return lastModified.after(lastLoaded)
        }
        @Override
        Set<T> getVocabularyItems() {
            synchronized (items) {
                if ( items.isEmpty() || needsReloading()) {
                    loadVocabulary()
                }
                return items
            }
        }
        void loadVocabulary() {
            synchronized (items) {
                System.out.print("loading vocabulary :" + vocabulary + " ")
                BufferedReader reader = null
                try {
                    InputStream sourceResource = new FileInputStream(sourceFile)
                    reader = new BufferedReader(new InputStreamReader(sourceResource))
                    String line
                    TreeSet<String> vocabItems = new TreeSet<>();
                    // read and sort items
                    while ( (line=reader.readLine()) != null) {
                        String[] parts = line.split("[:=]")
                        String var = parts.length > 1 ? parts[1] : parts[0]
                        vocabItems.add(stdName(var))
                    }
                    items.clear()
                    for (String item : vocabItems) {
                        items.add(item);
                    }
                    lastLoaded = new Date()
                    System.out.println(lastLoaded)
                } catch (Exception ex) {
                    ex.printStackTrace()
                } finally {
                    if ( reader ) {
                        try { reader.close() }
                        catch (Exception ex) {
                            log(ex)
                        }
                    }
                }
            }
        }
    }

    static class SimpleVocabulary<String> extends Vocabulary {
        SimpleVocabulary(Vocabularies vocab, Collection<String> terms) {
            super(vocab);
            items.addAll(terms);
        }
    }

    def getNceiVariableSource() {
        return "content/MetadataEditor/vocabularies/variables/ncei/ncei_variables.txt"
    }
    def getNceiPlatformsSource() {
        return "content/MetadataEditor/vocabularies/platforms/ncei/ncei_platforms.txt"
    }
    def getNceiInstrumentsSource() {
        return "content/MetadataEditor/vocabularies/instruments/ncei/ncei_instruments.txt"
    }
    def getNceiInstitutionsSource() {
        return "content/MetadataEditor/vocabularies/institutions/ncei/ncei_institutions.txt"
    }

    Map<Vocabularies, Vocabulary> vocabularyMap
    VocabularyService() {
        vocabularyMap = new HashMap<>()
        vocabularyMap.put(Vocabularies.VARIABLES, new DynamicVocabulary(Vocabularies.VARIABLES, getNceiVariableSource()))
        vocabularyMap.put(Vocabularies.PLATFORMS, new DynamicVocabulary(Vocabularies.PLATFORMS, getNceiPlatformsSource()))
        vocabularyMap.put(Vocabularies.INSTRUMENTS, new DynamicVocabulary(Vocabularies.INSTRUMENTS, getNceiInstrumentsSource()))
        vocabularyMap.put(Vocabularies.INSTITUTIONS, new DynamicVocabulary(Vocabularies.INSTITUTIONS, getNceiInstitutionsSource()))
        vocabularyMap.put(Vocabularies.OBSERVATION_TYPES, new SimpleVocabulary(Vocabularies.OBSERVATION_TYPES,
                                                                               ncei_observationTypes))
    }
    def getNceiVariables() {
        Vocabulary nceiVariables = vocabularyMap.get(Vocabularies.VARIABLES)
        return nceiVariables.getVocabularyItems()
    }
    def getNceiPlatforms() {
        Vocabulary nceiPlatforms = vocabularyMap.get(Vocabularies.PLATFORMS)
        return nceiPlatforms.getVocabularyItems()
    }
    def getNceiInstruments() {
        Vocabulary nceiInstruments = vocabularyMap.get(Vocabularies.INSTRUMENTS)
        return nceiInstruments.getVocabularyItems()
    }
    def getNceiInstitutions() {
        Vocabulary nceiInstitutions = vocabularyMap.get(Vocabularies.INSTITUTIONS)
        return nceiInstitutions.getVocabularyItems()
    }
    def getNceiObservationTypes() {
        Vocabulary nceiObservasions = vocabularyMap.get(Vocabularies.OBSERVATION_TYPES)
        return nceiObservasions.getVocabularyItems()
    }
//    def noteNewVariable(String newVariable) {
//        String std = newVariable.toLowerCase().trim().replaceAll("\\s+", " ")
//        File newVarsFile = getNewVariablesFile()
//        FileWriter writer = null
//        try {
//            writer = new FileWriter(newVarsFile)
//            writer.append(std)
//        } catch (Exception ex) {
//            ex.printStackTrace()
//        } finally {
//            if ( writer ) {
//                try { writer.close() }
//                catch (Exception ex) {}
//            }
//        }
//    }
//    def getNewVariablesFile() {
//        File contentRoot = new File("content/MetadataEditor/vocabularies/variables/ncei")
//        System.out.println("contentRoot: " + contentRoot.getAbsolutePath())
//        if ( !contentRoot.exists()) {
//            if ( ! contentRoot.mkdirs()) {
//                log.warn("Unable to create vocabulary content root directory: " + contentRoot.getAbsolutePath())
//                contentRoot = new File(System.getProperty("user.dir", "."))
//                log.warn("Using current working directory: " + contentRoot.getAbsolutePath())
//            }
//        }
//        File newVarsFile = new File(contentRoot, "ncei_additions.txt")
//        return newVarsFile
//    }
}
