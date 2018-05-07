# OAP Metadata Editor

#### Legal Disclaimer
*This repository is a software product and is not official communication 
of the National Oceanic and Atmospheric Administration (NOAA), or the 
United States Department of Commerce (DOC).  All NOAA GitHub project 
code is provided on an 'as is' basis and the user assumes responsibility 
for its use.  Any claims against the DOC or DOC bureaus stemming from 
the use of this GitHub project will be governed by all applicable Federal 
law.  Any reference to specific commercial products, processes, or services 
by service mark, trademark, manufacturer, or otherwise, does not constitute 
or imply their endorsement, recommendation, or favoring by the DOC. 
The DOC seal and logo, or the seal and logo of a DOC bureau, shall not 
be used in any manner to imply endorsement of any commercial product 
or activity by the DOC or the United States Government.*

This is a Grails-based Web-app for entering and editing OAP Metadata. Capabilities include:

1. Users can enter from scratch metadata by filling out the form which gives feedback on the validity of the entries as each section is saved.
2. Users can save the resulting metadata to an XML file on their local system.
3. Users can upload an existing XML file which will populate the user interface, which can then be edited and saved to either make changes to an exsiting entry or to start a new entry based on a previous one.

The uploaded and saved metadata files are stored on the server by EXPOCODE.
