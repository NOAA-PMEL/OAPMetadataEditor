package oap

import java.lang.reflect.Field
import java.lang.reflect.Modifier

import gov.noaa.ncei.oads.xml.v_a0_2_2s.BaseVariableType

import org.apache.catalina.core.ApplicationPart

class UtilsService {

    File stash(ApplicationPart part) {
        System.out.println(part)
        File stashFile = getUploadStashFile(part.getSubmittedFileName())
        def partStream = part.getInputStream()
        FileOutputStream fout = new FileOutputStream(stashFile)
        try {
            streamCopy(partStream, fout)
        } finally {
            if ( partStream != null )  partStream.close()
            if ( fout != null ) fout.close()
        }
        return stashFile
    }

    File getUploadStashFile(String filename) {
        File stashFile = new File(getStashDir(), filename)
        return stashFile
    }

    // From java.nio.file.Files, since there it is private. WTF?
    long streamCopy(InputStream source, OutputStream sink)
            throws IOException
    {
        long nread = 0L
        byte[] buf = new byte[8192]
        int n
        while ((n = source.read(buf)) > 0) {
            sink.write(buf, 0, n)
            nread += n
        }
        return nread
    }

    static File stashRoot = null
    File getStashDir() {
        if ( stashRoot == null ) {
            stashRoot = new File("content/MetadataEditor/stash")
            System.out.println("stashRoot: " + stashRoot.getAbsolutePath())
            if ( !stashRoot.exists()) {
                if ( ! stashRoot.mkdirs()) {
                    log.warn("Unable to create stash root directory: " + stashRoot.getAbsolutePath())
                    stashRoot = new File("." )
                    log.warn("Using current working directory: " + stashRoot.getAbsolutePath())
                }
            }
        }
        File stashDir = new File(stashRoot, String.valueOf(System.currentTimeMillis()))
        if ( ! stashDir.mkdirs()) {
            log.warn("Unable to create stash directory: " + stashDir.getAbsolutePath())
            stashDir = new File("." )
            log.warn("Using current working directory: " + stashDir.getAbsolutePath())
        }
        return stashDir
    }

    String idToShortURL(long n)
    {
        // Map to store 62 possible characters
//        char map62[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray()
        char[] map36 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray()

        StringBuffer shorturl = new StringBuffer()

        // Convert given integer id to a base 36 number
        while (n > 0)
        {
            // use above map to store actual character
            // in short url
            int mod = (int)(n % 36)
            char c = map36[mod]
            if ( c == 'I' ) c = '1'
            else if ( c == 'O' || c == 'Q' ) c = '0'
            shorturl.append(c)
            n = n / 36
        }

        // Reverse shortURL to complete base conversion
        return shorturl.reverse().toString()
    }

    static List<Field> getAllFields(Class clazz) {
        if (clazz == null || clazz.getName().endsWith("Object")) {
            return Collections.emptyList()
        }

        List<Field> result = new ArrayList<Field>(getAllFields(clazz.getSuperclass()))
//        List<Field> filteredFields = Arrays.stream(clazz.getDeclaredFields())
//                .filter(f -> Modifier.isPublic(f.getModifiers()) || Modifier.isProtected(f.getModifiers()))
//                .collect(Collectors.toList())
        List<Field> filteredFields = new ArrayList<>()
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if ( (field.getModifiers() & Modifier.PRIVATE) == 0 ) {
                field.setAccessible(true)
                filteredFields.add(field)
            }
        }
        result.addAll(filteredFields)
        return result
    }
    static String spaces(int n) {
        String space = ""
        for (int i = 0; i < n; i++) {
            space += "."
        }
        return space
    }
    public static boolean isEmpty(Object obj) throws IllegalArgumentException, IllegalAccessException {
        return isEmpty(obj, 0)
    }
    public static boolean isEmpty(Object obj, int depth) throws IllegalArgumentException, IllegalAccessException {
        boolean empty = true
        if ( obj == null ) { return true; }
        if ( obj instanceof String ) { return ((String)obj).trim().isEmpty(); }
//        System.out.print(spaces(depth))
        if ( obj instanceof Collection<?> ) {
            Collection<?> objc = (Collection<?>)obj
            if ( objc.isEmpty() ) { return true; }
            for (Object cobj : objc) {
                if ( ! isEmpty(cobj, depth+1)) { return false; }
            }
        }
        if ( obj.getClass().getName().endsWith("Type") ||
                obj instanceof BaseVariableType ) {
            int idx = 0
            List<Field> fields = getAllFields(obj.getClass())
            if ( fields.size() == 0 ) { // what the hell?
//                System.out.println("Found object with no fields: " + obj)
                return false
            }
            while ( empty && idx < fields.size()) {
                Field field = fields.get(idx++)
                if ( (field.getModifiers() & Modifier.PRIVATE) == 0 ) {
                    field.setAccessible(true)
                    Object fo = field.get(obj)
                    empty = isEmpty(fo, depth+1)
//                } else {
//                    System.out.println("no access : " + field)
                }
            }
            return empty
        } else {
//            logger.trace(" :: " + obj)
            return false
        }
    }
}
