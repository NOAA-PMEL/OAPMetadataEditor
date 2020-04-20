import grails.util.BuildSettings
import grails.util.Environment
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT_CLR', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern =
                '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
                        '%clr(%5p) ' + // Log level
                        '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                        '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                        '%m%n%wex' // Message
    }
}
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = '%d{yyyy-MM-dd HH:mm:ss.SSS} ' + // Date
                        '%5p ' + // Log level
                        '--- [%15.15t] ' + // Thread
                        '%-40.40logger{39} : ' + // Logger
                        '%m%n%wex' // Message
    }
}
appender('FILER', FileAppender) {
    file = "logs/MetadataEditor.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = '%d{yyyy-MM-dd HH:mm:ss.SSS} ' + // Date
                '%5p ' + // Log level
                '--- [%15.15t] ' + // Thread
                '%-40.40logger{39} : ' + // Logger
                '%m%n%wex' // Message
    }
}

def targetDir = BuildSettings.TARGET_DIR
def currentEnv = Environment.getCurrent()
System.out.println("Current environment: " + currentEnv)
if (Environment.isDevelopmentMode() && targetDir != null) {
    System.out.println("Seems to be DevMode " + Environment.isDevelopmentMode() + " with targetDir: "+ targetDir)
    logger("grails.app", DEBUG, ['STDOUT_CLR', 'FILER'], false)
    logger("oap", DEBUG, ['STDOUT_CLR', 'FILER'], false)
    logger("org.hibernate.SQL", DEBUG, ['STDOUT_CLR', 'FILER'], false)
    logger("org.hibernate.type.descriptor.sql.BasicBinder", TRACE, ['STDOUT_CLR', 'FILER'], false)
//    logger("org.hibernate.type.descriptor.sql", TRACE, ['STDOUT_CLR', 'FILER'], false) // OR ?
} else {
    System.out.println("NOT DevMode with targetDir: "+ targetDir)
    logger("grails.app", DEBUG, ['FILER'])
    logger("oap", DEBUG, ['FILER'])
}
root(ERROR, ['STDOUT'])

