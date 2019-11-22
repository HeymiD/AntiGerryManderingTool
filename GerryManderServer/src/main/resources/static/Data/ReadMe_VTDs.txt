Texas Legislative Council
Research Division
FTP Directory


VOTING TABULATION DISTRICTS (VTDs)
ftp://ftpgis1.tlc.state.tx.us/2011_Redistricting_Data/VTDs

Election data is reported by election precincts, which frequently change and may
not conform to census geographical units.  Council staff worked with the counties
throughout the decade to build a statewide precinct geographic database for each
election.  For 2010, the Census Bureau allowed states to submit their 2010 primary
precinct boundaries, even if they were drawn on nonvisible features, for inclusion
in the 2010 TIGER/Line database.  

Voting Tabulation Districts (VTDs), the census geographic equivalent of county
election precincts, are created for the purpose of relating 2010 Census population
data to election precinct data.  VTDs can differ from actual election precincts
because precincts do not always follow census geography.  The VTDs in the
redistricting database closely correspond to the 8,936 precincts in effect for the
2018 general election.  On the occasion that a precinct is in two noncontiguous pieces,
it is a suffixed VTD in the database.  For example, if precinct 0001 had two non-contiguous
areas, the corresponding VTD would be VTD 0001A and VTD 0001B.  If a 2018 general 
election precinct does not match any census geography, it is consolidated with an 
adjacent precinct and given that precinct's corresponding VTD number.  
There are 9,082 VTDs in the 2018 general election VTDs shapefile.

GIS users can join the election data located at ftp://ftpgis1.tlc.texas.gov/elections
to the 2018 general election VTDs shapefile in this directory.  Use the common field
name 'CNTYVTD' to join the data.

GIS users can join the population data located at
ftp://ftpgis1.tlc.texas.gov/2011_Redistricting_Data/VTDs/Population/
to the 2018 general election VTDs shapfile in this directory. Use the common field
name 'vtdkey' to join the data.

Note:  The 2018 general election VTDs in the council's redistricting geographic file
are not the same as the 2010 primary VTDs in the Census Bureau's TIGER/Line Shapefile
and do not correspond with the population data reported for the 2010 primary VTDs in
the Census Bureau's 2010 PL 94-171 file.


GEOGRAPHY FOLDER

The VTDs shapefile (.shp) is in a compressed file (.zip) format.

VTDs.zip - 2018 General Election VTDs

The shapefile has 2010 Census population data by VTD.


cnty (num) - County FIPS Census Code
Color (num) - Color assignment for symbology
vtd (txt) - VTD name (2018 General Election)
cntyvtd (txt) - Unique code used to join geographic data
vtdkey (num) - Unique code used to join to geographic data
e_ang (num) - Anglo Population
e_oth (num) - Other Population
e_hsp (num) - Hispanic Population
e_total (num) - Total Population
e_vap (num) - Voting Age Population
e_blak (num) - Black Population
e_bh (num) - Black+Hispanic Population
e_angvap (num) - Anglo Voting Age Population
e_hspvap (num) - Hispanic Voting Age Population
e_bhvap (num) - Black+Hispanic Voting Age Population
e_blakvap (num) - Black Voting Age Population
e_othvap (num) - Other Voting Age Population



POPULATION FOLDER

In accordance with Public Law 94-171, the Census Bureau is required to provide the
states with the official census population numbers needed for redistricting,
including total and voting age population by race and ethnicity for every
census geographic level.

The council groups the population data into five race and ethnicity categories for
redistricting modeling and reporting:

BLACK - Encompasses all people identifying themselves as Black, African American,
or Negro on the census questionnaire, even if they also identified themselves with
other racial/ethnic groups.

HISPANIC - Encompasses all people identifying themselves as Hispanic, Latino, or
Spanish origin, whatever their race.

BLACK+HISPANIC - A combined total of all those identifying themselves as Black
and all those identifying themselves as Hispanic, adjusted so that those
identifying themselves as both Black and Hispanic are not counted twice.

ANGLO - Includes all people who selected White as their only race and did not
identify themselves as Hispanic, Latino, or Spanish origin.

OTHER - Encompasses everyone who does not fall into any of the four classifications
described above.

VAP - Voting age population, persons in a geographic unit who are at least 18 years of age.


The comma-separated values file (.csv) contains the population by VTD in a
compressed file (.zip) format.

VTDsPop.zip - 2018 General Election VTDs, 2010 Census Population

cnty (num) - County FIPS Census Code
vtd (txt) - VTD name (2018 General Election)
cntyvtd (txt) - Unique code used to join geographic data
vtdkey (num) - Unique code used to join to geographic data
e_ang (num) - Anglo Population
e_oth (num) - Other Population
e_hsp (num) - Hispanic Population
e_total (num) - Total Population
e_vap (num) - Voting Age Population
e_blak (num) - Black Population
e_bh (num) - Black+Hispanic Population
e_angvap (num) - Anglo Voting Age Population
e_hspvap (num) - Hispanic Voting Age Population
e_bhvap (num) - Black+Hispanic Voting Age Population
e_blakvap (num) - Black Voting Age Population
e_othvap (num) - Other Voting Age Population



Last modified February 5, 2019.