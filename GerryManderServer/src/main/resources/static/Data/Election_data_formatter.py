import csv

        # County,FIPS,VTD,cntyvtd,Office,Name,Party,Incumbent,Votes
quit_b=0
while(quit_b==0):
    file_name=input("Filename: ")
    # file_name=file_name+".csv"
    with open(file_name+'.csv') as csv_file:
        pres_election_results = csv.DictReader(csv_file)
        fieldnames=['County','cntyvtd','Office','Democrat','Republican','Green','Libertarian']
        formatted_pres_election_results = {}
        for row in pres_election_results:
            try:
                formatted_pres_election_results[row['cntyvtd']]['cntyvtd']=row['cntyvtd']
            except:
                formatted_pres_election_results[row['cntyvtd']]={
                'County':'',
                'cntyvtd':'',
                'Office':'',
                'Democrat':0,
                'Republican':0,
                'Green':0,
                'Libertarian':0
                }
                formatted_pres_election_results[row['cntyvtd']]['cntyvtd']=row['cntyvtd']
            formatted_pres_election_results[row['cntyvtd']]['County']=row['County']
            formatted_pres_election_results[row['cntyvtd']]['Office']=row['Office']
            if row['Party']=='D':
                formatted_pres_election_results[row['cntyvtd']]['Democrat']=row['Votes']
            if row['Party']=='R':
                formatted_pres_election_results[row['cntyvtd']]['Republican']=row['Votes']
            if row['Party']=='G':
                formatted_pres_election_results[row['cntyvtd']]['Green']=row['Votes']
            if row['Party']=='L':
                formatted_pres_election_results[row['cntyvtd']]['Libertarian']=row['Votes']
        i=0
        for key in formatted_pres_election_results:
            if i==100:
                break
            print(formatted_pres_election_results[key])
            i=i+1
        new_file_name=file_name+'_Formatted.csv'
        with open(new_file_name,'w',newline='') as new_file:
            csv_writer=csv.DictWriter(new_file,fieldnames=fieldnames)
            csv_writer.writeheader()
            for key in formatted_pres_election_results:
                csv_writer.writerow(formatted_pres_election_results[key])
    quit_b=int(input("If you want to quit press 1: \n"))
