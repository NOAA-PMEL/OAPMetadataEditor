package oap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class XmlServiceTest {

    public static void main(String[] args) {
//        String filename = "/Users/kamb/workspace/oa_dashboard_test_data/NCEI/49UP20130619/0175954-trimmed.xml";
        String filename = "/users/kamb/workspace/oa_dashboard_test_data/test_xml/oap_metadata_fully_filled_out.xml.out";
        if ( args.length > 0 ) {
            filename = args[0];
        }

        File infile = new File(filename);
        File outfile = new File(filename+".oldstyle");

        try ( FileInputStream fis = new FileInputStream(infile); ) {
           oap.XmlService oaxSvc = new oap.XmlService();
           Document doc = (Document)oaxSvc.createDocumentFromLegacyXML(fis);
            System.out.println(doc);
            String xml = (String) oaxSvc.createXml(doc);
            try (FileOutputStream fos = new FileOutputStream(outfile)) {
                fos.write(xml.getBytes("UTF8"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
