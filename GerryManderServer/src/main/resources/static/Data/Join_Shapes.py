import pandas
import numpy

elec_file = input('Election Data: ')
# congressional_elec_file = input('Congressional Election Data: ')

vtds = pandas.read_csv("vtds.csv")
precincts = pandas.read_csv("precincts.csv")
result = vtds.merge(precincts, left_on='CNTYVTD', right_on='PCTKEY', how='outer')
nan_rows = result[result['PCTKEY'].isnull()]
unmapped = nan_rows['CNTYVTD']

elec = pandas.read_csv(elec_file+'.csv')
# congressional_elec = pandas.read_csv(congressional_elec_file+'.csv')

unmapped_elec = elec.loc[elec['cntyvtd'].isin(unmapped)]
fieldNames = ['County','cntyvtd','Office','Democrat','Republican','Green','Libertarian']

combined_mapping_elec = {}

for index, row in unmapped_elec.iterrows():
    new_cntyvtd = row['cntyvtd'][:len(row['cntyvtd'])-1]
    try:
        combined_mapping_elec[new_cntyvtd]['Green']=combined_mapping_elec[new_cntyvtd]['Green']+int(row['Green'])
        combined_mapping_elec[new_cntyvtd]['Democrat'] = combined_mapping_elec[new_cntyvtd]['Democrat'] + int(row['Democrat'])
        combined_mapping_elec[new_cntyvtd]['Republican'] = combined_mapping_elec[new_cntyvtd]['Republican'] + int(row['Republican'])
        combined_mapping_elec[new_cntyvtd]['Libertarian'] = combined_mapping_elec[new_cntyvtd]['Libertarian'] + int(row['Libertarian'])
    except:
        combined_mapping_elec[new_cntyvtd]={
            'County': '', 'cntyvtd':new_cntyvtd, 'Office':'', 'Democrat':0, 'Republican':0, 'Green':0, 'Libertarian':0}
        combined_mapping_elec[new_cntyvtd]['County'] = row['County']
        combined_mapping_elec[new_cntyvtd]['Office'] = row['Office']
        combined_mapping_elec[new_cntyvtd]['Green'] = combined_mapping_elec[new_cntyvtd]['Green'] + int(row['Green'])
        combined_mapping_elec[new_cntyvtd]['Democrat'] = combined_mapping_elec[new_cntyvtd]['Democrat'] + int(row['Democrat'])
        combined_mapping_elec[new_cntyvtd]['Republican'] = combined_mapping_elec[new_cntyvtd]['Republican'] + int(row['Republican'])
        combined_mapping_elec[new_cntyvtd]['Libertarian'] = combined_mapping_elec[new_cntyvtd]['Libertarian'] + int(row['Libertarian'])

combined_mapping_elec_data = []

for row in combined_mapping_elec:
    combined_mapping_elec_data.append(combined_mapping_elec[row])

combined_mapping_elec_DF = pandas.DataFrame(combined_mapping_elec_data)

# mapped_elec_to_precincts = precincts.merge(elec,right_on='cntyvtd', left_on='PCTKEY', how='left')
# mapped_elec_to_precincts = mapped_elec_to_precincts.merge(combined_mapping_elec_DF,left_on='PCTKEY',right_on='cntyvtd',how='outer')
cleaned_elec = pandas.concat([elec,combined_mapping_elec_DF])
mapped_elec_to_precincts = precincts.merge(cleaned_elec, left_on='PCTKEY', right_on='cntyvtd',how='left')
# print(mapped_elec_to_precincts)
mapped_elec_to_precincts.to_csv(r''+elec_file+'_standardized.csv')







