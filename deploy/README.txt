##############################
#Biotea
##############################
RDF as an Interface to scientific publications.

Read more at http://www.jbiomedsem.com/content/4/S1/S5

Please cite this work as
Leyla Jael García-Castro, Casey McLaughlin, Alexander García Castro: Biotea: RDFizing PubMed Central in support for the paper as an interface to the Web of Data. J. Biomedical Semantics 4(S-1): S5 (2013)

##############################
#Main Class
##############################
PmcOpenAccessRDFHandlerWithThreads
Usage: 
-in <xml papers dir> -out <output dir> -sections 
-all or -rdfAndAnnotations or any of -rdf -whatizit -ncboAnnotator -bio2rdf (all are optional, if none one present -all)
-pipeline <pipeline> (optional, whatizitEuropePmcAll by default, only used when -whatizit or all are present) Pipeline to be used by Whatizit AO annotator (values: whatizitEuropePmcGenesProteins, whatizitSwissprot, whatizitEuropePmcAll, etc)" + 
-sections (optional) If present, paper sections will be rdfized");

Recommended flow
1. PmcOpenAccessRDFHandlerWithThreads -in <input dir> -out <output dir> -rdf -sections
2. PmcOpenAccessRDFHandlerWithThreads -in <input dir> -out <output dir> -ncboAnnotator
3. PmcOpenAccessRDFHandlerWithThreads -in <input dir> -out <output dir> -whatizit -pipeline whatizitEuropePmcAll

Notes: 
- You can use either Biotea or Bio2RDF URI styles. If you use Bio2RDF, in order to keep consistency, please use the
-bio2rdf flag when running the process

- If you decide not to annotate the sections, it will not be possible to run later the RDFization process only to this end. 
It is either metadata+sections or metadata only, only sections is not an option

- Once an input file has been processed and an output file has been created, running the RDFization process again will not
process the already processed articles as they already exist in the output directory. You would need either to clean the
output directory ot to use another one.

- Not all the content will be RDFizated. For instance, formulas will not. Content not included in the RDF output will be reported as a WARN in the logs.
We were not interested in RDFizating all the content but still we wanted to keep a trace of what was not RDFized.

##############################
#config.properties
##############################
config.properties has information about the dirs where the WSDL for Whatizit is located, 
configure that first

You can use either Biotea or Bio2RDF URI styles. If you use Bio2RDF, in order to keep consistency, please use the
-bio2rdf flag when running the process. The base URI property is named biotea.base

If biotea.withBio is true, links to Bio2RDF and identifiers.org will be added

For whatizit, GO and CHEBI can be included or not, check config.properties

For NCBO Annoator is possible to include only or exclude some ontologies. You will need to specify
the enumeration name of the ontology, see names at ws.biotea.ld2rdf.util.ncbo.annotator.Ontology

IMPORTANT: REmember to configure your Bioportal API key in order to use the NCBOAnnotator

IMPORTANT: If you are annotating with Whatizit, remember to point to your local copy of whatizit.wsdl.dir

##############################
#Running environment
##############################
###Input and Output resources
You will need an input dir with all dtds required, as those in /pmc_dtds; 
You will need in the same input directory the NXML files that you want to process 
(a sample file is provided in /temp AAPS_J_2008_Apr_2_10(1)_193-199.nxml)
You will need an output directory with one subdirs output/AO_annotations

###jar file 
Make sure the jar, config.properties, log4j.properties, server-bindings.xml, and whatizit.wsdl 
are all in the same directory, one next to each other.
Modify config.properties and log4j.properties to point to the right paths in your machine

###logs
Do not forget to modify log4j.properties according to what you want to include in the logs and
where you want to locate them
  
##############################
#Running options, some examples
##############################
RDF generation for articles only
-in D:\workspace\biotea_rdf4pmc\temp\toProcess -out D:\workspace\biotea_rdf4pmc\temp -rdf

RDF generation for articles only, including sections
-in D:\workspace\biotea_rdf4pmc\temp\toProcess -out D:\workspace\biotea_rdf4pmc\temp -rdf -sections

RDF generation for NCBO Annotator only (RDF for article and sections must already exist in the output directory)
-in D:\workspace\biotea_rdf4pmc\temp\toProcess -out D:\workspace\biotea_rdf4pmc\temp -ncboAnnotator

RDF generation for Whatizit only (RDF for article and sections must already exist in the output directory)
-in D:\workspace\biotea_rdf4pmc\temp\toProcess -out D:\workspace\biotea_rdf4pmc\temp -pipeline whatizitEuropePmcAll -whatizit

RDF for article, sections, and annotations
-in D:\workspace\biotea_rdf4pmc\temp\toProcess -out D:\workspace\biotea_rdf4pmc\temp rdfAndAnnotations -sections -pipeline whatizitEuropePmcAll

All
-in D:\workspace\biotea_rdf4pmc\temp\toProcess -out D:\workspace\biotea_rdf4pmc\temp -pipeline whatizitEuropePmcAll -all

##############################
#Some additional notes
##############################
- NCBOAnnotator may be not responsive all the time. What we do in Biotea is to annotate as many paragraphs as possible but there is no guaranty 
that all of them will be annotated. Furthermore, it could be that more or less paragraphs are annotated if you try again because of NCBOAnnotator 
responses. In order to alter this behaviour, and perhaps annotate all or nothing, you will need to adjust the NCBOAnnotator class

- Whatizit has not been updated lately so annotations will be out of sync regarding current versions of the ontologies and datasets used there.